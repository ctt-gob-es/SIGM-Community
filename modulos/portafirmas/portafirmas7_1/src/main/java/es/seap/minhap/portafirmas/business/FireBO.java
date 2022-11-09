/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.business;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.gob.fire.client.BatchResult;
import es.gob.fire.client.CreateBatchResult;
import es.gob.fire.client.FireClient;
import es.gob.fire.client.SignOperationResult;
import es.gob.fire.client.SignProcessConstants.SignatureAlgorithm;
import es.gob.fire.client.SignProcessConstants.SignatureFormat;
import es.gob.fire.client.SignProcessConstants.SignatureOperation;
import es.gob.fire.client.SignProcessConstants.SignatureUpgrade;
import es.gob.fire.client.TransactionResult;
import es.guadaltel.framework.authenticator.impl.util.Base64Coder;
import es.guadaltel.framework.signer.impl.util.ConstantsSigner;
import es.seap.minhap.portafirmas.business.beans.FireErrors;
import es.seap.minhap.portafirmas.business.ws.Afirma5BO;
import es.seap.minhap.portafirmas.business.ws.SignatoryBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.exceptions.InvalidSignException;
import es.seap.minhap.portafirmas.exceptions.InvalidSignKOException;
import es.seap.minhap.portafirmas.exceptions.PortafirmasException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.web.beans.fire.FireDocument;
import es.seap.minhap.portafirmas.web.beans.fire.FireRequest;
import es.seap.minhap.portafirmas.web.beans.fire.FireTransaction;
import es.seap.minhap.portafirmas.web.beans.signature.DocumentSignatureConfig;
import es.seap.minhap.portafirmas.web.beans.signature.RequestSignatureConfig;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.RespuestaValidarFirma;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo;

/**
 * @author arturo.tejero, domingo.sanchez
 *
 */

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class FireBO {

	private static final String ERROR_DESCONOCIDO_FIRE = "error.desconocido";

	private static final boolean NO_STOP_ON_ERROR = false;
	
	@Autowired
	private Afirma5BO afirma5BO;

	@Resource(name="fireProperties")
	private Properties fireProperties;

	@Resource(name="messageFireErrors")
	private Properties messageFireErrors;

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private TagBO tagBO;

	@Autowired
	private RequestBO requestBO;

	@Autowired
	private SignBO signBO;

	@Autowired
	private PassBO passBO;

	@Autowired
	private SignatoryBO signatoryBO;

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private FireErrors fireErrors;
	
    @Autowired
    private UtilComponent util;

	private FireClient fireClient = null;
	
	private Properties confProperties = null;

	private Properties confPropertiesMobile = null;

	Logger log = Logger.getLogger(SignBO.class);

// MingoTarea: Hay que crear un component que haga la función del PostContrutct.
//	No funciona en pre-producción de la DSIC, luego probablemente no funciona en producción.
//    @PostConstruct
//    private void postConstruct() {
//		PfConfigurationsParameterDTO fireConexionParameter = 
//				(PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
//						Constants.QUERY_PFIRMA_GENERAL_PARAM,
//						Constants.QUERY_PARAM_CONF_VALUE,
//						Constants.C_PARAMETER_FIRE_ID);
//		fireClient = new FireClient(fireConexionParameter.getTvalue(), fireProperties);
//
//		confProperties = obtenerPropiedades("web");
//		confPropertiesMobile = obtenerPropiedades("mobile"); 
//   }

	/**
 	 * Invoca FIRe para realizar las peticiones indicadas
 	 * 
 	 * @param hashes
	 * @param usuarioConectado
	 * @return
	 * @throws Exception
	 */
	public FireTransaction getTransactionWeb(String[] hashes, PfUsersDTO usuarioConectado) throws Exception {
		if(fireClient == null) {
			fireClient = obtenerClienteFIRe();
		}
		if(confProperties == null) {
			confProperties = obtenerPropiedades("web");
		}
		return getTransaction(hashes, usuarioConectado, confProperties);
	}
	
	public FireTransaction getTransactionMobile(String[] hashes, PfUsersDTO usuarioConectado) throws Exception {
		if(fireClient == null) {
			fireClient = obtenerClienteFIRe();
		}
		if(confPropertiesMobile == null) {
			confPropertiesMobile = obtenerPropiedades("mobile");
		}
		return getTransaction(hashes, usuarioConectado, confPropertiesMobile);
	}

	private FireTransaction getTransaction(String[] hashes, PfUsersDTO usuarioConectado, Properties configuracion) throws Exception {
		String transactionId = null;
		String redirectUrl = "/pf/fire/saveSign";
		
		FireTransaction fireTransaccion = preProcesarPeticion(hashes, usuarioConectado);		

		if(existeSolicitudFirma(fireTransaccion.getMapVistoBueno(), hashes)) {
			transactionId = crearLote(usuarioConectado, configuracion);
			fireTransaccion.setTransactionId(transactionId);
			envioLote(fireTransaccion, usuarioConectado, configuracion);		

			SignOperationResult resultadoFirma = fireClient.signBatch(transactionId, usuarioConectado.getCidentifier(), NO_STOP_ON_ERROR);
			redirectUrl = resultadoFirma.getRedirectUrl();
		}
		
		fireTransaccion.setTransactionId(transactionId);
		fireTransaccion.setUrlRedirect(redirectUrl);
		
		return fireTransaccion;
	}

	/**
	 * Guarda las firmas obtenidas de FIRe de las peticiones indicadas
	 * 
	 * @param idTransaccion
	 * @param requestIds
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<FireRequest> signCloud(String idTransaccion, String[] requestIds, PfUsersDTO user) throws Exception {
		FireRequest fireRequest = null;
		List <FireRequest> listaPeticiones = new ArrayList <FireRequest>();
		BatchResult resultadoLote = fireClient.recoverBatchResult(idTransaccion, user.getCidentifier());
		FireTransaction fireTransaction = preProcesarPeticion(requestIds, user);
		fireTransaction.setTransactionId(idTransaccion);
		for (String hash : requestIds){
			PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(hash);
			if(fireTransaction.getMapVistoBueno().containsKey(hash)) {
				fireRequest = procesarVistoBueno(requestTagDTO, user);
			} else {
				fireRequest = procesarFirma(resultadoLote, fireTransaction, requestTagDTO, user);
				if(!fireRequest.isContieneErrores()) {
					avisoServicioWebOrigen(requestTagDTO);
				}
			}
			listaPeticiones.add(fireRequest);
		}

		return listaPeticiones;
	}

	/**
	 * @param transaccionId
	 * @param usuarioConectado
	 * @throws PortafirmasException con el mensaje de error registrado en FIRe
	 */
	public void obtenerError(String transaccionId, PfUsersDTO usuarioConectado) throws PortafirmasException {
		PortafirmasException error = null;
		TransactionResult erroresFire = null;
		try {
			erroresFire = fireClient.recoverErrorResult(transaccionId, usuarioConectado.getCidentifier());
			log.error("Error recuperado de FIRe: " + erroresFire.getErrorCode() + " transacción: " + transaccionId);
			
			switch (erroresFire.getErrorCode()) {
			case 1: case 4: case 6: case 8: case 103: case 104: case 105: case 202:
				error = new PortafirmasException(messageFireErrors.getProperty(Integer.toString(erroresFire.getErrorCode())));
				break;

			default:
				error = new PortafirmasException(messageFireErrors.getProperty(ERROR_DESCONOCIDO_FIRE));
				break;
			}
			throw error;
			
		} catch (PortafirmasException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error inesperado al intentar recuperar el error: ", e);
			throw new PortafirmasException(messageFireErrors.getProperty(ERROR_DESCONOCIDO_FIRE));
		}
	}

	private FireClient obtenerClienteFIRe(){
		PfConfigurationsParameterDTO fireConexionParameter = 
		(PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				Constants.QUERY_PFIRMA_GENERAL_PARAM,
				Constants.QUERY_PARAM_CONF_VALUE,
				Constants.C_PARAMETER_FIRE_ID);
		return new FireClient(fireConexionParameter.getTvalue(), fireProperties);
	}
    private Properties obtenerPropiedades(String interfaz) {
    	Properties propiedades = new Properties();
    	propiedades.setProperty("redirectOkUrl", fireProperties.getProperty(interfaz + ".redirectOkUrl"));
    	propiedades.setProperty("redirectErrorUrl", fireProperties.getProperty(interfaz + ".redirectErrorUrl"));
    	propiedades.setProperty("certOrigin", fireProperties.getProperty(interfaz + ".certOrigin"));
    	propiedades.setProperty("appName", fireProperties.getProperty(interfaz + ".appName"));
    	return propiedades;
	}

	private FireTransaction preProcesarPeticion(String[] hashes, PfUsersDTO usuarioConectado) throws Exception{
		Map<String, String> mapVistoBueno = new HashMap<String, String>();
		Map<String, String> mapFormatoFirma = new HashMap<String, String>();
		List<RequestSignatureConfig> signatureConfigList = new ArrayList<RequestSignatureConfig>();	

		for (String hash : hashes) {
			PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(hash);
			RequestSignatureConfig signatureConfig = configuracionFirma(requestTagDTO, usuarioConectado);
			signatureConfigList.add(signatureConfig);
			if (signatureConfig.getType().equalsIgnoreCase(Constants.C_TYPE_SIGNLINE_PASS)) {
				mapVistoBueno.put(hash, hash);
			} else {
				for (DocumentSignatureConfig documentConfig : signatureConfig.getDocumentsConfig()){
					mapFormatoFirma.put(documentConfig.getDocumentHash(), documentConfig.getFormat().toUpperCase());
				}
			}
		}
		
		FireTransaction fireTransaction = new FireTransaction();
		fireTransaction.setHashes(hashes);
		fireTransaction.setMapVistoBueno(mapVistoBueno);
		fireTransaction.setMapFormatoFirma(mapFormatoFirma);
		fireTransaction.setSignatureConfigList(signatureConfigList);
		return fireTransaction;
	}

	private RequestSignatureConfig configuracionFirma (PfRequestTagsDTO requestTagDTO, PfUsersDTO usuarioConectado) throws Exception{
		RequestSignatureConfig signatureConfig = null;
		try {
			signatureConfig = signBO.getSignatureConfiguration(requestTagDTO, usuarioConectado);
		} catch (Exception e) {
			signatureConfig = new RequestSignatureConfig();
			signatureConfig.setRequestTagHash(requestTagDTO.getChash());
			signatureConfig.setType("ERROR");
		}
		return signatureConfig;
	}

	private boolean existeSolicitudFirma(Map<String, String> mapVistoBueno, String[] hashes) {
		return mapVistoBueno.size() < hashes.length ;
	}

	private String crearLote(PfUsersDTO usuarioConectado, Properties config) throws Exception {
		CreateBatchResult result = fireClient.createBatchProcess(
				usuarioConectado.getCidentifier(),
				SignatureOperation.SIGN.toString(),
				SignatureFormat.PADES.toString(),
				SignatureAlgorithm.SHA256WITHRSA.toString(), null, null, config);
		return result.getTransactionId();
	}

	private void envioLote(FireTransaction fireTransaction, PfUsersDTO usuarioConectado, Properties confProperties) throws Exception{
		for (RequestSignatureConfig signatureConfig : fireTransaction.getSignatureConfigList()) {
			if (signatureConfig.getType().equalsIgnoreCase(Constants.C_TYPE_SIGNLINE_SIGN)) {
				for (DocumentSignatureConfig documentConfig : signatureConfig.getDocumentsConfig()){
					String documentoId = documentConfig.getDocumentHash();
					byte[] documento = signBO.obtenerContenidoFirmaDirecto(documentConfig.getDocumentHash());
					String formato = util.getSignFormatParameter(documentConfig.getFormat());
					String operacion = obtenerOperacionFirma(documentConfig, formato);
					String extra = obtenerExtra(documentConfig);
					// No se hace el sello de tiempo con FIRe, luego hay que hacer con @firma antes de guardar la firma.
					String upgrade = null; //obtenerSelloTiempo(formato, documentConfig);
					fireClient.addDocumentToBatch(fireTransaction.getTransactionId(), usuarioConectado.getCidentifier(), documentoId, documento, confProperties, operacion, formato, extra, upgrade);
				}
			}
		}
	}

	private String obtenerExtra(DocumentSignatureConfig documentConfig) throws Exception {
		return new String(new Base64Coder().encodeBase64(documentConfig.getSignParameter().getBytes()));
	}

	private String obtenerOperacionFirma(DocumentSignatureConfig documentConfig, String formato) {
		String operacion = SignatureOperation.SIGN.toString();
		// Si el formato es distinto a PAdES aplicaremos, si procede, la contrafirma y la cofirma 
		if(!formato.equals(SignatureFormat.PADES.toString()) && !formato.equals(SignatureFormat.FACTURAE.toString())) {
			if(documentConfig.getOperation().equals(Constants.SIGN_COSIGN_OPPERATION)) {
				operacion = SignatureOperation.COSIGN.toString();
			}
			if(documentConfig.getOperation().equals(Constants.SIGN_COUNTERSIGN_OPPERATION)) {
				operacion = SignatureOperation.COUNTERSIGN.toString();
			}
		}
		return operacion;
	}

	private String obtenerSelloTiempo(String formato, DocumentSignatureConfig documentConfig) {
		String upgrade = null;
		if (documentConfig.isSelloTiempo()){
			if(SignatureFormat.PADES.toString().equals(formato)) {
				upgrade = SignatureUpgrade.PADES_LTV_FORMAT.toString();
			} else {
				upgrade = SignatureUpgrade.T_FORMAT.toString();
			}
		}
		return upgrade;
	}

	private FireRequest procesarVistoBueno(PfRequestTagsDTO requestTagDTO, PfUsersDTO user) {
		passBO.pass(user, user.getValidJob(), requestTagDTO, false, false);
		requestBO.markAsUnReadForRemitters(requestTagDTO.getPfRequest());
		return obtenerFireRequest(requestTagDTO);
	}

	private FireRequest obtenerFireRequest(PfRequestTagsDTO requestTagDTO) {
		FireRequest fireRequest = new FireRequest();			
		fireRequest.setAsunto(requestTagDTO.getPfRequest().getDsubject());
		fireRequest.setId(requestTagDTO.getChash());
		fireRequest.setRemitters(requestTagDTO.getPfRequest().getRemitters());
		return fireRequest;
	}

	@Transactional(readOnly = false, rollbackFor={Exception.class})	
	private FireRequest procesarFirma(BatchResult resultadoLote, FireTransaction fireTransaction, PfRequestTagsDTO requestTagDTO, PfUsersDTO user) throws Exception {
		FireRequest fireRequest = obtenerFireRequest(requestTagDTO);
		try {
			// Se comprueba que la etiqueta-peticion no esté en un estado final para el usuario
			if (!tagBO.checkNotFinishedReqTag(requestTagDTO)) {
				fireRequest.setErrorPeticion("La petición no ha sido firmada por encontrarse en un estado final para el usuario.");			
			} else {						
				List <FireDocument> listaDocumentos = new ArrayList <FireDocument>();
				for (PfDocumentsDTO documentDTO : requestTagDTO.getPfRequest().getPfDocumentsList()){
					FireDocument fireDocument = procesarDocumento(resultadoLote, fireTransaction, user, documentDTO, requestTagDTO);
					listaDocumentos.add(fireDocument);
				}
				fireRequest.setDocumentos(listaDocumentos);
				if (!fireRequest.isContieneErrores()) {
					marcarPeticionAFirmada(requestTagDTO, user);
				}
			}
		} catch (Exception e) {
			if(fireRequest.getErrorPeticion() == null) {
				fireRequest.setErrorPeticion(Constants.MSG_GENERIC_ERROR);
			}
		}
		
		return fireRequest;
	}

	private FireDocument procesarDocumento(BatchResult resultadoLote, FireTransaction fireTransaction, PfUsersDTO user, PfDocumentsDTO documentoDTO, PfRequestTagsDTO requestTagDTO) throws Exception {
		FireDocument fireDocument = new FireDocument();
		fireDocument.setId(documentoDTO.getChash());
		fireDocument.setNombre(documentoDTO.getDname());

		if (!resultadoLote.get(fireDocument.getId()).isSigned()){
			String errorDescription = fireErrors.getErrorType(resultadoLote.get(fireDocument.getId()).getErrotType());					
			fireDocument.setError("Error de FIRe: " + errorDescription);
		} else {
			TransactionResult resultadoTransaccion = fireClient.recoverBatchSign(fireTransaction.getTransactionId(), user.getCidentifier(), fireDocument.getId());
			if (resultadoTransaccion != null) {
				if (resultadoTransaccion.getErrorMessage() == null){
					// Se registran las firmas de la petición						
					String signFormat = fireTransaction.getMapFormatoFirma().get(fireDocument.getId());
					try {
						byte[] firma = resultadoTransaccion.getResult();
						//[Agustin Diputacion de Ciudad Real #1229]
						//boolean firmanteValido = validarFirmante(user, firma, fireDocument.getId());
						//if (firmanteValido){
							endFireSign(requestTagDTO, firma, signFormat, user, fireDocument.getId());
						//}else {
						//	fireDocument.setError("El certificado utilizado para la firma no pertenece al usuario autenticado");					
						//}
					} catch (CustodyServiceException e) {
						fireDocument.setError("Error al guardar la firma");
					} catch (InvalidSignException e) {
						fireDocument.setError(e.getMessage());
					} catch (Exception e) {
						fireDocument.setError(Constants.MSG_GENERIC_ERROR);
					}
				} else{
					fireDocument.setError(resultadoTransaccion.getErrorMessage());
				}
			} else {
				fireDocument.setError(Constants.MSG_GENERIC_ERROR);
			}
		}
		return fireDocument;
	}

	private boolean validarFirmante (PfUsersDTO user, byte[] firma, String hashDoc){
		boolean firmanteValido = true;
		try {
			String nifCif = obtenerNifFirmante(firma);

			boolean esMismoUsuario = user.getCidentifier().equals(nifCif);
			boolean esSeudonimo = Constants.EEUTIL_SEUDONIMO.equals(nifCif);

			if (!esMismoUsuario && !esSeudonimo) {
				firmanteValido = false;
			}

		} catch (Exception e) {
			log.error("Se ha producido un error al validarFirmante: " + e);
			firmanteValido = false;
		}

		return firmanteValido;
	}
	
	/**
	 * Valida la firma, bien contra la pa
	 * @param firma
	 * @param idConf
	 * @throws InvalidSignException
	 * @throws SocketTimeoutException 
	 */
	private void validarFirmaSiActivo (byte[] firma, long idConf) throws InvalidSignException {

		if (afirma5BO.estaActivoValidarFirma(idConf)) {
			RespuestaValidarFirma respuestaValidarFirma = null;
			try {
				respuestaValidarFirma = afirma5BO.validarFirma(firma, idConf);
//				try {
//					int a = 1 / (int) (Math.random() * 4) % 4;
//				} catch (Exception e) {
//					throw e;
//				}
			}catch(SocketTimeoutException e){
				log.error("Se ha producido un timeout en la validación de la firma por @firma, ", e);
				throw new InvalidSignKOException("No se pudo validar la firma. Se superó el tiempo de respuesta máximo del servicio de validación.");
			}catch (Exception e){
				log.error("Error validando firma", e);
				throw new InvalidSignKOException ("No se pudo validar la firma. Falló en el servicio de validación de firma.");
			}
			if (respuestaValidarFirma.isError()) {
				String msg = respuestaValidarFirma.getMensaje();
				if(msg.contains("COD_103")){
					log.error("El certificado de la firma previa está revocado o es inválido: " + respuestaValidarFirma.getMensajeAmpliado());
					throw new InvalidSignException("El certificado de la firma previa está revocado o es inválido.");
				}else{
					log.error ("Afirma está devolviendo error al validar la firma: " + respuestaValidarFirma.getMensajeAmpliado());
					throw new InvalidSignException ("No se pudo validar la firma.");
				}
			} else if (!respuestaValidarFirma.isValido()) {
				if(respuestaValidarFirma.getMensajeAmpliado().contains("El certificado había expirado")){
					log.error("El certificado había expirado: " + respuestaValidarFirma.getMensajeAmpliado());
					throw new InvalidSignException ("El certificado había expirado en el momento de la firma.");						
				}else{
					log.error(("Firma no valida: " + respuestaValidarFirma.getMensajeAmpliado()));
					throw new InvalidSignException ("Firma no válida.");
				}
			}
		}
	}

	private String obtenerNifFirmante(byte[] firma) throws Exception {
		ListaFirmaInfo listaFirmantesUtil = signatoryBO.obtenerFirmantes(firma, null);
		int numeroFirmantes = listaFirmantesUtil.getInformacionFirmas().getInformacionFirmas().size();		
		return listaFirmantesUtil.getInformacionFirmas().getInformacionFirmas().get(numeroFirmantes - 1).getNifcif();
	}
	
	private void endFireSign(PfRequestTagsDTO requestTag, byte[] firma, String format, PfUsersDTO userDTO, String documentHash) throws Exception {
		long idConf = requestTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		Configuration conf = signBO.loadSignProperties(idConf);
		String signatureFormatDefault = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);

		signBO.endSignDocumentoFIRe(requestTag, userDTO, conf, signatureFormatDefault, firma, format, documentHash);
		// para sellar después signBO.endSignDocumento(requestTag, null, userDTO, false, true, conf, signatureFormatDefault, firma, format, documentHash, false);

	}

	private void marcarPeticionAFirmada(PfRequestTagsDTO requestTag, PfUsersDTO userDTO) throws Exception{
		try {
			// Se marca la petición como firmada
			List<AbstractBaseDTO> req = new ArrayList<AbstractBaseDTO>();
			requestTag.setValidada(true);
			AbstractBaseDTO requestSigned = signBO.queryRequestData(requestTag, userDTO);
			req.add(requestSigned);

			tagBO.changeStateToSignedList(req, userDTO, false);

			requestBO.markAsUnReadForRemitters(requestTag.getPfRequest());
		} catch (Exception e) {
			throw new Exception ("Se ha producido un error al marcar la peticion como firmada.", e);	
		}
	}

	private void avisoServicioWebOrigen(PfRequestTagsDTO requestTagDTO) {
		try {
			List<AbstractBaseDTO> signedRequests = new ArrayList<AbstractBaseDTO>();
			signedRequests.add(requestTagDTO);
			noticeBO.sendAdviceToAppServer(signedRequests);
		} catch (Exception e) {
			log.error("La noficiación a la aplicación de origen falló: ", e);
		}
	}

}
