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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import es.guadaltel.framework.authenticator.impl.util.Base64Coder;
import es.guadaltel.framework.signer.impl.util.ConstantsSigner;
import es.juntadeandalucia.cice.pfirma.data.SignerBlock;
import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.ws.Afirma5BO;
import es.seap.minhap.portafirmas.business.ws.CertificateBO;
import es.seap.minhap.portafirmas.business.ws.EEUtilOperFirmaBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfBlockSignsDTO;
import es.seap.minhap.portafirmas.domain.PfBlocksDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.exceptions.BlockRequestException;
import es.seap.minhap.portafirmas.exceptions.CertificateException;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.exceptions.InvalidSignException;
import es.seap.minhap.portafirmas.exceptions.InvalidSignKOException;
import es.seap.minhap.portafirmas.exceptions.TimeStampingException;
import es.seap.minhap.portafirmas.utils.AuthenticatorConstants;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputSign;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.web.beans.signature.DocumentSignature;
import es.seap.minhap.portafirmas.web.beans.signature.DocumentSignatureConfig;
import es.seap.minhap.portafirmas.web.beans.signature.RequestSignature;
import es.seap.minhap.portafirmas.web.beans.signature.RequestSignatureConfig;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.RespuestaAmpliarFirma;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.RespuestaValidarFirma;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.InterfazGenericaBO;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.exception.InterfazGenericaClientException;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SignBO {

	
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private CustodyServiceFactory custodyServiceFactory;

	@Autowired
	private TagBO tagBO;
	
	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private EEUtilOperFirmaBO eeUtilOperFirmaBO;
	
	@Autowired
	private Afirma5BO afirma5BO;
	
	@Autowired
	CertificateBO certificateBO;
	
	@Autowired
	private ApplicationVO applicationVO;
	
	@Autowired
	private BinaryDocumentsBO binaryDocumentsBO;

    @Autowired
    private UtilComponent util;

	@Autowired
	private InterfazGenericaBO interfazGenericaBO; 

	@Autowired
	private ApplicationBO applicationBO;
	
	Logger log = Logger.getLogger(SignBO.class);

	/**
	 * 
	 * @param requestList Lista de peticiones que se quieren firmar
	 * @param idConf Idetificador de la configuraci&oacute;n del servidor de firma seleccionado
	 * @param userDTO Objeto que define el usuario que quiere realizar la firma o firmas
	 * @param userJobDTO Objeto que define un cargo v&aacute;lido del usuario que quiere realizar la firma
	 * @return Lista de objetos que contienen la informaci&oacute;n de las peticiones a firmar
	 */
	public List<AbstractBaseDTO> queryRequestsData(
			List<AbstractBaseDTO> requestList, long idConf, PfUsersDTO userDTO,
			PfUsersDTO userJobDTO) {
		Object[] pkList = new Object[requestList.size()];
		for (int i = 0; i < requestList.size(); i++) {
			pkList[i] = new Long(((PfRequestTagsDTO) requestList.get(i)).getPrimaryKey());
		}
		Map<String, Object[]> parametersList = new HashMap<String, Object[]>();
		Map<String, Object> parametersSimple = new HashMap<String, Object>();
		parametersList.put("listPks", pkList);
		parametersSimple.put("idConf", new Long(idConf));
		parametersSimple.put("usr", userDTO);
		parametersSimple.put("usrJob", userJobDTO);
		List<AbstractBaseDTO> requests = 
			baseDAO.queryListMoreParametersListComplex("sign.requestTagsDataPetitionList", parametersList,	parametersSimple);
		return requests;
	}

	/**
	 * 
	 * @param request Peticiones que se quieren firmar
	 * @param userDTO Objeto que define el usuario que quiere realizar la firma o firmas
	 * @return Lista de objetos que contienen la informaci&oacute;n de las peticiones a firmar
	 */
	public AbstractBaseDTO queryRequestData(AbstractBaseDTO request, PfUsersDTO userDTO) {
		long idConf = ((PfRequestTagsDTO)request).getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pk", ((PfRequestTagsDTO) request).getPrimaryKey());
		parameters.put("idConf", new Long(idConf));
		parameters.put("usr", userDTO);
		parameters.put("usrJob", userDTO.getValidJob());
		AbstractBaseDTO result = baseDAO.queryElementMoreParameters("sign.requestTagsDataPetition", parameters);
		return result;
	}

	/**
	 * M&eacute;todo que obtiene las firmas realizadas sobre un documento.
	 * @param hashDoc Valor hash del documento en cuesti&oacute;n.
	 * @return Lista de firmas del documento de entrada.
	 */
	public List<AbstractBaseDTO> orderedSigns(String hashDoc) {
		List<AbstractBaseDTO> signs = baseDAO.queryListOneParameter("request.signsOrderedDateReport", "hashDoc", hashDoc);
		return signs;
	}

	/**
	 * Método que genera la configuración de firma para una petición
	 * @param requestTag Petición
	 * @param usuarioConectado 
	 * @return Configuración de firma de la petición
	 * @throws CustodyServiceException
	 * @throws IOException
	 * @throws Exception
	 */
	@Transactional
	public RequestSignatureConfig getSignatureConfiguration(PfRequestTagsDTO requestTag, PfUsersDTO usuarioConectado) {
		RequestSignatureConfig requestSignatureConfig;
		try {
			requestBO.bloquearPeticion(requestTag, usuarioConectado);
			// Se guarda la petición
			requestSignatureConfig = new RequestSignatureConfig();
			requestSignatureConfig.setRequestTagHash(requestTag.getChash());

			// Se define tipo
			if ((requestTag.getStateUser() != null && requestTag.getStateUser().getPfTag().isPass()) ||
				(requestTag.getStateJob() != null && requestTag.getStateJob().getPfTag().isPass())) {
				requestSignatureConfig.setType(Constants.C_TYPE_SIGNLINE_PASS);
			} else {
				requestSignatureConfig.setType(Constants.C_TYPE_SIGNLINE_SIGN);

				// Se carga la configuración por defecto de la aplicación
				Long idConf = requestTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
				Configuration conf = loadSignProperties(idConf);

				List<DocumentSignatureConfig> documentsConfig = new ArrayList<DocumentSignatureConfig>();
				for (PfDocumentsDTO document : requestTag.getPfRequest().getPfDocumentsList()) {
					// Se genera la configuración de cada documento contenido en la petición
					DocumentSignatureConfig documentSignatureConfig = null;
					if (document.getLsign()) {
						// Se cargan las firmas si el documento ya ha sido firmado en Portafirmas
						List<AbstractBaseDTO> signs = this.orderedSigns(document.getChash());
						if (Constants.C_DOCUMENTTYPE_FACTURAE.equals(document.getPfDocumentType().getCdocumentType())){
							//Obtenemos la configuracion de firma de las facturae
							PfConfigurationsDTO confDTO = (PfConfigurationsDTO) baseDAO.queryElementOneParameter(
									"administration.configurationByCode", "code", Constants.FACTURAE_CODE_CONF);
							Configuration confFacturae = loadSignProperties(confDTO.getPrimaryKey());
							documentSignatureConfig = this.obtenerConfiguracionFirma(signs, requestTag, confFacturae, document);
							this.obtenerContenidoFirma(signs, requestTag, confFacturae, document, documentSignatureConfig);
						} else {
							documentSignatureConfig = this.obtenerConfiguracionFirma(signs, requestTag, conf, document);
							this.obtenerContenidoFirma(signs, requestTag, conf, document, documentSignatureConfig);
						}
						documentsConfig.add(documentSignatureConfig);
					}
				}
				requestSignatureConfig.setDocumentsConfig(documentsConfig);
				
			}

		} catch (BlockRequestException e) {
			requestSignatureConfig = new RequestSignatureConfig();
			requestSignatureConfig.setRequestTagHash(requestTag.getChash());
			requestSignatureConfig.setType("ERROR");
			requestSignatureConfig.setErrorMessage(Constants.MSG_BLOCK_REQUEST_ERROR);
		} catch (Exception e) {
			log.error("Error en la petición: " + requestTag.getChash(), e);
			requestSignatureConfig = new RequestSignatureConfig();
			requestSignatureConfig.setRequestTagHash(requestTag.getChash());
			requestSignatureConfig.setType("ERROR");
			requestSignatureConfig.setErrorMessage(Constants.MSG_GENERIC_ERROR);
		}	
		return requestSignatureConfig;
	}

	public DocumentSignatureConfig obtenerConfiguracionFirma(List<AbstractBaseDTO> signs, PfRequestTagsDTO requestTag, Configuration conf, PfDocumentsDTO document) {

		String signatureFormat = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);
		String operacion = null	;
		
		// No hay firmas si el documento todavía no se ha firmado en Portafirmas 
		if (signs.isEmpty()) {
			signatureFormat = controlarPAdESparaPDFs(document, signatureFormat);

			// Si el documento original ya era un documento firmado y el formato de firma es XADES o CADES 
			if (document.getLissign() && isXAdesOrCAdES(signatureFormat)) {
				// Si la petición es en cascada, por defecto se deberá contrafirmar 
				if (requestTag.getPfRequest().getLcascadeSign()) {
					operacion = Constants.SIGN_COUNTERSIGN_OPPERATION;
				// Si la petición es en paralelo, por defecto se deberá cofirmar							
				} else {
					operacion = Constants.SIGN_COSIGN_OPPERATION;
				}
			} else {
				operacion = Constants.SIGN_SIGN_OPPERATION;
			}
		// el documento ya se firmó en Portafirmas, entonces existen firmas y se toma la última que se creó
		} else {
			PfSignsDTO sign = (PfSignsDTO) signs.get(0);
			signatureFormat = sign.getCformat();
			if (isXAdesOrCAdES(signatureFormat)) {
				if (requestTag.getPfRequest().getLcascadeSign()) {
					operacion = Constants.SIGN_COUNTERSIGN_OPPERATION;
				} else {
					operacion = Constants.SIGN_COSIGN_OPPERATION;
				}
			} else {
				operacion = Constants.SIGN_SIGN_OPPERATION;
			}
		}

		String signatureAlgorithm = util.evaluarAlgoritmoDefecto(conf.getString(ConstantsSigner.AFIRMA5_HASH_ALGORITH));
		String signParameter = util.getSignatureParameters(requestTag.getPfRequest(), signatureFormat, document.getDmime());
		String signFormatParameter = util.getSignFormatParameter(signatureFormat);	
		
		DocumentSignatureConfig documentSignatureConfig = new DocumentSignatureConfig();
		documentSignatureConfig.setDocumentHash(document.getChash());
		documentSignatureConfig.setOperation(operacion);
		documentSignatureConfig.setFormat(signatureFormat);
		documentSignatureConfig.setHashAlgorithm(signatureAlgorithm);
		documentSignatureConfig.setSignParameter(signParameter);
		documentSignatureConfig.setSignFormatParameter(signFormatParameter);
		documentSignatureConfig.setSelloTiempo(requestTag.getPfRequest().getLtimestamp().equals(Constants.C_YES));
		documentSignatureConfig.setXades(isXAdes(signatureFormat));
		
		return documentSignatureConfig;
	}

	 /**
		Si el formato de firma solicitado es PAdES y el documento no es un PDF, entonces se fuerza la firma XADES
		(Ojo a la notación! Constants.SIGN_FORMAT_PDF = "PDF" y no "PADES", por ejemplo, en bb.dd. de Portafirmas)
	 */
	private String controlarPAdESparaPDFs(PfDocumentsDTO document, String signatureFormat) {
		if (signatureFormat.equals(Constants.SIGN_FORMAT_PDF) && 
			!document.getDmime().equals("application/pdf")) {
			signatureFormat = Constants.SIGN_FORMAT_XADES_IMPLICIT;
		}
		return signatureFormat;
	}

	// Quedan fuera las PAdES y las FacturaE que no admiten contrafirma ni cofirma
	private boolean isXAdesOrCAdES(String signatureFormat) {
		return signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_IMPLICIT)  ||
		 signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_ENVELOPED) ||
		 signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_ENVELOPING) ||
		 signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_CADES);
	}
	
		private boolean isXAdes(String signatureFormat) {
			return signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_IMPLICIT)  ||
			 signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_ENVELOPED) ||
			 signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_ENVELOPING);
		}
	
	private void obtenerContenidoFirma(List<AbstractBaseDTO> signs, PfRequestTagsDTO requestTag, Configuration conf, PfDocumentsDTO document, DocumentSignatureConfig documentSignatureConfig) throws Exception {
		// Parámetros iniciales de la configuración
		String signatureMode = conf.getString(Constants.C_PARAMETER_SIGNATURE_MODE);
		String signatureFormat = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);
		String toSign = null;
		String modo = null;
		if (Constants.FACTURAE_FIRMA_SIGNATURE_FORMAT.equals(signatureFormat)){
			modo = Constants.SIGN_BINARY;
			StringBuffer sb = new StringBuffer();
			appendBase64Document (document, sb);
			toSign = sb.toString();
		} else {
			if (signs.isEmpty()) {
				// Si el documento no es un PDF se fuerza la firma XADES
				if (signatureFormat.equals(Constants.SIGN_FORMAT_PDF) && 
					!document.getDmime().equals("application/pdf")) {
					signatureMode = Constants.SIGN_BINARY_SIGNATURE_MODE;
				}
				// Comprueba el modo en el que se pasarán los datos (Hash or Binary)
				if (Constants.SIGN_HASH_SIGNATURE_MODE.equals(signatureMode)) {
					modo = Constants.SIGN_HASH;
					Base64Coder coder = new Base64Coder();
					byte[] hashBinary = Hex.decodeHex(document.getPfFile().getChash().toCharArray());
					String hashb64 = new String(coder.encodeBase64(hashBinary));
					toSign = hashb64;
				} else if (Constants.SIGN_BINARY_SIGNATURE_MODE.equals(signatureMode)) {
					modo = Constants.SIGN_BINARY;
					StringBuffer sb = new StringBuffer();
					appendBase64Document (document, sb);
					toSign = sb.toString();
				} else {
					log.error("Signature mode not defined or incorrect. Check server configuration parameters");
					throw new Exception("Signature mode not defined or incorrect. Check server configuration parameters");
				}
			} else {
				if (Constants.SIGN_HASH_SIGNATURE_MODE.equals(signatureMode)) {
					modo = Constants.SIGN_HASH;
				} else if (Constants.SIGN_BINARY_SIGNATURE_MODE.equals(signatureMode)) {
					modo = Constants.SIGN_BINARY;
				}
				// Si es multifirma el fichero se codifica en Base64
				PfSignsDTO sign = (PfSignsDTO) signs.get(0);
				StringBuffer sb = new StringBuffer();
				this.appendBase64Sign(sign, sb);
				toSign = sb.toString();
			}
		}
		documentSignatureConfig.setData(toSign);
		documentSignatureConfig.setMode(modo);
	}

//	public RequestSignatureConfig getSignatureConfigRequestFIRe(PfRequestTagsDTO requestTag) throws Exception {
//		RequestSignatureConfig requestSignatureConfig = new RequestSignatureConfig();	
//		requestSignatureConfig.setRequestTagId(requestTag.getChash());
//		// Se define si es un visto bueno o una firma
//		if ((requestTag.getStateUser() != null && requestTag.getStateUser().getPfTag().isPass()) ||
//			(requestTag.getStateJob() != null && requestTag.getStateJob().getPfTag().isPass())) {
//			requestSignatureConfig.setType(Constants.C_TYPE_SIGNLINE_PASS);
//		} else {
//			requestSignatureConfig.setType(Constants.C_TYPE_SIGNLINE_SIGN);
//			requestSignatureConfig.setDocumentsConfig(getSignatureConfigsDocumentFIRe(requestTag));
//		}
//		return requestSignatureConfig;
//	}
//
//	// Se generan las configuraciones de cada documento contenido en la petición
//	private List<DocumentSignatureConfig> getSignatureConfigsDocumentFIRe(PfRequestTagsDTO requestTag) throws Exception {
//		List<DocumentSignatureConfig> documentsConfig = new ArrayList<DocumentSignatureConfig>();
//		for (PfDocumentsDTO document : requestTag.getPfRequest().getPfDocumentsList()) {
//			// Se carga la configuración por defecto de la aplicación
//			Long idConf = requestTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
//			Configuration conf = loadSignProperties(idConf);
//			
//			// Parámetros iniciales de la configuración
//			String signatureFormat = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);
//			String operacion = null	;
//
//			List<AbstractBaseDTO> signs = this.orderedSigns(document.getChash());
//			if (signs.isEmpty()) {
//				// Si el documento no es un PDF se fuerza la firma XADES
//				if (signatureFormat.equals(Constants.SIGN_FORMAT_PDF) && 
//					!document.getDmime().equals("application/pdf")) {
//					signatureFormat = Constants.SIGN_FORMAT_XADES_IMPLICIT;
//				}
//
//				// Si el documento ya era una firma, y el formato de firma es XADES o CADES, indicamos la operación a realizar 
//				if ((signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_IMPLICIT)  ||
//					 signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_XADES_ENVELOPED) ||
//					 signatureFormat.equalsIgnoreCase(Constants.SIGN_FORMAT_CADES)) &&
//					 document.getLissign()) {
//					// Si la petición es en cascada, por defecto se deberá contrafirmar 
//					if (requestTag.getPfRequest().getLcascadeSign()) {
//						operacion = Constants.SIGN_COUNTERSIGN_OPPERATION;
//					// Si la petición es en paralelo, por defecto se deberá cofirmar							
//					} else {
//						operacion = Constants.SIGN_COSIGN_OPPERATION;
//					}
//				// Si no, la operación a realizar será firmar
//				} else {
//					operacion = Constants.SIGN_SIGN_OPPERATION;
//				}
//
//			} else {
//				// Es multifirma. Ya se generaron firmas en Portafirmas.
//				PfSignsDTO sign = (PfSignsDTO) signs.get(0);
//				signatureFormat = sign.getCformat();
//				
//				/* Si la petición es en cascada, por defecto se deberá contrafirmar */
//				if (requestTag.getPfRequest().getLcascadeSign()) {
//					operacion = Constants.SIGN_COUNTERSIGN_OPPERATION;
//				/* Si la petición es en paralelo, por defecto se deberá cofirmar */							
//				} else {
//					operacion = Constants.SIGN_COSIGN_OPPERATION;
//				}
//				
//			}
//			
//			String signParameter = util.getSignatureParameters(signatureFormat, document.getDmime());
//			String signFormatParameter = util.getSignFormatParameter(signatureFormat, operacion);
//			
//			DocumentSignatureConfig documentSignatureConfig = new DocumentSignatureConfig(); ;
//			documentSignatureConfig.setDocumentHash(document.getChash());
//			documentSignatureConfig.setOperation(operacion);
//			documentSignatureConfig.setFormat(signatureFormat);
//			documentSignatureConfig.setSignParameter(signParameter);
//			documentSignatureConfig.setSignFormatParameter(signFormatParameter);
//			documentSignatureConfig.setSelloTiempo(requestTag.getPfRequest().getLtimestamp().equals(Constants.C_YES));
//			documentsConfig.add(documentSignatureConfig);
//		}
//		return documentsConfig;
//	}
	
	// NO usado, para que FIRe pueda obtener el documento a partir del fichero en disco
	public byte[] obtenerContenidoFirma(String nombreFichero) throws Exception {
		File fichero = new File(Constants.PATH_TEMP + nombreFichero);
		InputStream in = new BufferedInputStream(new FileInputStream(fichero));
		byte[] data = IOUtils.toByteArray(in);
		IOUtils.closeQuietly(in);
		return data;
	}
	
	//Obtención del archivo directamente de BB.DD., sin crear fichero en disco
	public byte[] obtenerContenidoFirmaDirecto(String documentHash) throws Exception {
		List<AbstractBaseDTO> signs = this.orderedSigns(documentHash);
		byte[] data = null;
						
		if (signs.isEmpty()) {
			PfDocumentsDTO documentDTO = binaryDocumentsBO.getDocumentDTOByHash(documentHash);
			data = binaryDocumentsBO.getDocumentByDocumentDTO (documentDTO);
		} else {
			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash(documentHash);
			data = binaryDocumentsBO.getSignatureBySignDTO(signDTO);
		}
		return data;
	}
	
	/**
	 * M&eacute;todo que genera una cadena de texto con la informaci&oacute;n de los datos que se van a firmar.
	 * @param conf Configuraci&oacute;n del servidor de firma seleccionado.
	 * @param request Listado con las peticiones que se van a firmar.
	 * @param signableDocs Listado vac&iacute;o de documentos. Al terminar la ejecuci&oacute;n del m&eacute;todo contendr&aacute; los documentos que se van a firmar
	 * @return Cadena de texto con la informaci&oacute;n de los documentos a firmar. Contiene el formato de firma, el modo de firma y la ruta con los datos en Base64 a firmar.
	 * @throws CustodyServiceException
	 * @throws IOException
	 * @throws Exception
	 */
	public String getDataToSign(Configuration conf,
			List<AbstractBaseDTO> requestTagList, List<PfDocumentsDTO> signableDocs,
			List<Integer> relacionPeticionesDocumentos)
			throws CustodyServiceException, IOException, Exception {
		StringBuffer dataSign = new StringBuffer();
		int numDocs;
		Iterator<AbstractBaseDTO> itReq = requestTagList.iterator();
		while (itReq.hasNext()) {
			numDocs = 0;
			PfRequestsDTO req = ((PfRequestTagsDTO) itReq.next()).getPfRequest();
			List<PfDocumentsDTO> docs = req.getPfDocumentsList();
			Iterator<PfDocumentsDTO> itDocs = docs.iterator();
			while (itDocs.hasNext()) {
				String signatureMode = conf.getString(Constants.C_PARAMETER_SIGNATURE_MODE);
				String signatureFormat = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);
				String operacion = "";
				String modo = "";
				String toSign = "";
				PfDocumentsDTO doc = itDocs.next();
				if (doc.getLsign()) {
					signableDocs.add(doc);
					// Check if document is signed in Portafirmas
					List<AbstractBaseDTO> signs = orderedSigns(doc.getChash());
					if (signs.isEmpty()) {

						// Check if the document's mimetype is correct (if application/pdf -> signatureMode = BINARIO & signatureFormat = PDF) //
						if (signatureFormat.equals(Constants.SIGN_FORMAT_PDF) && 
							!doc.getDmime().equals("application/pdf")) {
							signatureMode = Constants.SIGN_BINARY_SIGNATURE_MODE;
//							signatureFormat = Constants.SIGN_FORMAT_XADES_ENVELOPING;
							signatureFormat = Constants.SIGN_FORMAT_XADES_IMPLICIT;
						}
						
						// Si el documento ya era una firma, y el formato de firma es XADES o CADES, indicamos la operación a realizar 
						if (isXAdesOrCAdES(signatureFormat) &&
							 doc.getLissign()) {
							// Si la petición es en cascada, por defecto se deberá contrafirmar 
							if (req.getLcascadeSign()) {
								operacion = Constants.SIGN_COUNTERSIGN_OPPERATION_PREFFIX;
							// Si la petición es en paralelo, por defecto se deberá cofirmar							
							} else {
								operacion = Constants.SIGN_COSIGN_OPPERATION_PREFFIX;
							}
						// Si no, la operación a realizar será firmar
						} else {
							operacion = Constants.SIGN_SIGN_OPPERATION_PREFFIX;
						}
						
						
						// Check signature mode (Hash or Binary)
						if (Constants.SIGN_HASH_SIGNATURE_MODE.equals(signatureMode)) {
							modo = Constants.SIGN_HASH_PREFFIX;
							Base64Coder coder = new Base64Coder();
							byte[] hashBinary = Hex.decodeHex(doc.getPfFile().getChash().toCharArray());
							String hashb64 = new String(coder.encodeBase64(hashBinary));
							toSign = hashb64;
						} else if (Constants.SIGN_BINARY_SIGNATURE_MODE.equals(signatureMode)) {
							modo = Constants.SIGN_BINARY_PREFFIX;
							StringBuffer sb = new StringBuffer();
							appendBase64Document (doc, sb);
							toSign = sb.toString();
						} else {
							log.error("Signature mode not defined or incorrect. Check server configuration parameters");
							throw new Exception("Signature mode not defined or incorrect. Check server configuration parameters");
						}
						
					} else {
						//It's a multisign, data must be sign encoded in Base64
						PfSignsDTO sign = (PfSignsDTO) signs.get(0);
						
						/* Si la petición es en cascada, por defecto se deberá contrafirmar */
						if (req.getLcascadeSign()) {
							operacion = Constants.SIGN_COUNTERSIGN_OPPERATION_PREFFIX;
						/* Si la petición es en paralelo, por defecto se deberá cofirmar */							
						} else {
							operacion = Constants.SIGN_COSIGN_OPPERATION_PREFFIX;
						}
						
						if (Constants.SIGN_HASH_SIGNATURE_MODE.equals(signatureMode)) {
							modo = Constants.SIGN_HASH_PREFFIX;
						} else if (Constants.SIGN_BINARY_SIGNATURE_MODE.equals(signatureMode)) {
							modo = Constants.SIGN_BINARY_PREFFIX;
						}
												
						signatureFormat = sign.getCformat();
						StringBuffer sb = new StringBuffer();
						appendBase64Sign(sign, sb);
						toSign = sb.toString();
						
					}
						
					dataSign.append(operacion);
					dataSign.append(modo);
					dataSign.append(signatureFormat);
					dataSign.append(";");
					dataSign.append(toSign);
					// miniapplet
					dataSign.append(";");
					dataSign.append(doc.getDmime());
							
					if (itReq.hasNext() || itDocs.hasNext()) {
						dataSign.append(",");
					}
				}

				// Se contabiliza el documento
				numDocs++;
			}

			// se añade la relación de documentos por petición
			relacionPeticionesDocumentos.add(numDocs);
		}

		log.debug("DATASIGN: " + dataSign.toString());
		return dataSign.toString();
		
	}

	/**
	 * M&eacute;todo que descarga y almacena en Base64 en un fichero temporal ({sgtic}/temp) un fichero que se va a firmar.
	 * @param doc Documento a descargar.
	 * @param buffer Buffer de texto en el que se a&ntilde;ade la cadena con la definici&oacute;n del servlet de descarga y la ruta temporal donde se encuentra el fichero en Base64.
	 * @throws CustodyServiceException
	 * @throws IOException
	 * @throws PfirmaException
	 */
	private void appendBase64Document(PfDocumentsDTO doc, StringBuffer buffer)
			throws CustodyServiceException, IOException, PfirmaException {
//		CustodyDAO custodyDAO = new CustodyDAO(baseDAO);
		CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(doc.getPfFile().getCtype());
		
		//StringBufferOutputStream sbf = new StringBufferOutputStream(buffer);
		//OutputStream reemplazoOut = new ReemplazarSaltosOutputStream(sbf);
		//OutputStream out = new Base64.OutputStream(reemplazoOut, Base64.ENCODE);

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		String sessionId = attr.getSessionId();

		String fichero = sessionId + "-" + doc.getChash() + "-" + System.currentTimeMillis() + ".data";
		File file = new File(Constants.PATH_TEMP + fichero);
		FileOutputStream out = new FileOutputStream(file);
		
		CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
		custodyDocument.setIdentifier(doc.getChash());
		custodyDocument.setUri(doc.getPfFile().getCuri());
		custodyDocument.setIdEni(doc.getPfFile().getIdEni());
		custodyDocument.setRefNasDir3(doc.getPfFile().getRefNasDir3());
		// Configuramos el servicio de custodia para que descargue el fichero completo aunque sea en sí mismo una firma XML, ya que,
		// en este caso, a Afirma habría que enviarle la firma para que lo cofirme o lo contrafirme.
		// ---- MINIAPPLET ---
		
		byte[] bytes = custodyService.downloadFile(custodyDocument);
		
		try {
			if(applicationBO.isTrifasicaActiva()) {
				out.write(bytes);
			} else {
				out.write((new Base64Coder()).encodeBase64(bytes));
			}
		} catch (Exception e) {
			out.close();
			throw new IOException ("No se puede codificar el fichero a base 64", e);
		}
		// ---- END MINIAPPLET ---
		//CLIENTE PESADO custodyService.downloadFile(custodyDocument, out);
		out.flush();
		out.close();

		buffer.append(fichero);
		 
	}

	/**
	 * M&eacute;todo que descarga y almacena en Base64 en un fichero temporal ({sgtic}/temp) una firma.
	 * @param sign Firma a descargar
	 * @param buffer Buffer de texto en el que se a&ntilde;ade la cadena con la definici&oacute;n del servlet de descarga y la ruta temporal donde se encuentra la firma en Base64.
	 * @throws CustodyServiceException
	 * @throws IOException
	 * @throws PfirmaException
	 */
	private void appendBase64Sign(PfSignsDTO sign, StringBuffer buffer)
			throws CustodyServiceException, IOException, PfirmaException {

		CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(sign.getCtype());

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		String sessionId = attr.getSessionId();

		String fichero = sessionId + "-" + System.currentTimeMillis() + ".data";
		File file = new File(Constants.PATH_TEMP + fichero);
		//StringBufferOutputStream sbf = new StringBufferOutputStream(buffer);
		//OutputStream out = new ReemplazarSaltosOutputStream(sbf);
		FileOutputStream out = new FileOutputStream(file);
		
		CustodyServiceOutputSign custodySign = new CustodyServiceOutputSign();
		custodySign.setType(Constants.SIGN_TYPE_SERVER);
		custodySign.setIdentifier(sign.getPrimaryKeyString());
		custodySign.setUri(sign.getCuri());
		custodySign.setIdEni(sign.getRefNASIdEniFirma());
		custodySign.setRefNasDir3(sign.getRefNASDir3Firma());
		
		// -- MINIAPPLET--
		byte[] bytes = custodyService.downloadSign(custodySign);
		
		// Codificamos a b64
		Base64Coder coder = new Base64Coder();
		try {
			if(applicationBO.isTrifasicaActiva()) {
				out.write(bytes);
			} else {
				out.write(coder.encodeBase64(bytes));
			}
		} catch (Exception e) {
			out.close();
			throw new IOException ("No se puede codificar el fichero a base 64", e);
		}
		// --FIN  MINIAPPLET--
		
		out.flush();
		out.close();
		
		buffer.append(fichero);
	}

	/**
	 * M&eacute;todo que completa el proceso de una firma masiva de documentos.
	 * @param signableDocs Lista con los documentos que se van a firmar.
	 * @param requestTags Lista de peticiones firmadas.
	 * @param signatures Lista de firmas de las peticiones. Es una cadena de texto con la informaci&oacute;n de las firmas.
	 * @param certificate Certificado usado para realizar la firma.
	 * @param idConf Idetificador de la configuraci&oacute;n del servidor de firma seleccionado.
	 * @param userDTO Objeto que define el usuario que quiere realizar la firma o firmas.
	 * @param userJobDTO Objeto que define un cargo v&aacute;lido del usuario que quiere realizar la firma.
	 * @param job Indica si el usuario que firma lo hace en calidad de Cargo.
	 * @param request 
	 * @param v 
	 * @param request 
	 * @throws CustodyServiceException
	 * @throws CertificateException 
	 * @throws InterfazGenericaClientException 
	 * @throws DocumentCantBeDownloadedException 
	 */
	@Transactional(readOnly = false, rollbackFor={CustodyServiceException.class, InvalidSignException.class, TimeStampingException.class, Exception.class})	
	public void endMassiveSign(PfRequestTagsDTO requestTag, 
								RequestSignature requestSignature, 
								PfUsersDTO userDTO,
							    boolean job,
							    boolean validateSigns) throws CustodyServiceException, InvalidSignException, TimeStampingException, CertificateException, InterfazGenericaClientException, DocumentCantBeDownloadedException {
		log.info("endMassiveSign init");

		long idConf = requestTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		Configuration conf = loadSignProperties(idConf);
		String signatureFormatDefault = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);

		for (DocumentSignature docSignature : requestSignature.getSignatures()) {
			// Datos de la firma
			String signature = docSignature.getSignature();
			String format = docSignature.getFormat().toUpperCase();

			endSignDocumento(requestTag, requestSignature.getFechaValidezCertificado(), userDTO, job, validateSigns, conf, signatureFormatDefault, signature.getBytes(), format, docSignature.getDocHash(), true);
			
		}

		// Se marca la petición como firmada
		List<AbstractBaseDTO> req = new ArrayList<AbstractBaseDTO>();
		requestTag.setValidada(true);
		AbstractBaseDTO requestSigned = queryRequestData(requestTag, userDTO);
		req.add(requestSigned);
		
		tagBO.changeStateToSignedList(req, userDTO, job);
		
		requestBO.markAsUnReadForRemitters(requestTag.getPfRequest());
		
		interfazGenericaBO.procesamientoExternos(requestTag.getPfRequest());
		
		log.info("endMassiveSign end");

	}

	/**
	 * M&eacute;todo que completa el proceso de una firma masiva de documentos.
	 * @param requestTag
	 * @param userDTO
	 * @param conf
	 * @param signatureFormatDefault
	 * @param firma
	 * @param format
	 * @param documentHash
	 * @throws CustodyServiceException
	 * @throws InvalidSignException
	 * @throws TimeStampingException
	 */
	public void endSignDocumentoFIRe(PfRequestTagsDTO requestTag, PfUsersDTO userDTO, Configuration conf, String signatureFormatDefault, byte[] firma, String format, String documentHash)
					throws CustodyServiceException, InvalidSignException, TimeStampingException {
		// Se pone el último parámetro a true y así le hacemos creer que se firmó por Miniapplet para que ponga el sello de tiempo, ya que FIRe no sabe hacerlo en la llamada "fireClient.addDocumentToBatch" de la clase FireBO.java
		endSignDocumento(requestTag, null, userDTO, false, true, conf, signatureFormatDefault, firma, format, documentHash, true);
	}

	public void endSignDocumento(PfRequestTagsDTO requestTag, Date fechaValidezCertificado, PfUsersDTO userDTO,
			boolean job, boolean validateSigns, Configuration conf, String signatureFormatDefault, byte[] firma,
			String format, String documentHash, boolean esMiniapplet)
			throws CustodyServiceException, InvalidSignException, TimeStampingException {
		
		if (Constants.SIGN_FORMAT_DEFAULT.equals(format)) {
			format = signatureFormatDefault;
		}
		conf.setProperty(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT, format);

		// Se recupera el firmante de la petición
		PfSignersDTO signerDTO = tagBO.lastSignerUnresolvedDocument(requestTag, userDTO, userDTO.getValidJob(), job);

		String storageType = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.FIRMAS.name());
		CustodyServiceInput custodyService = custodyServiceFactory.createCustodyServiceInput(storageType);

		// Se crea el objeto de firma
		PfSignsDTO signDTO = new PfSignsDTO();

		// Se recupera el documento firmado
		PfDocumentsDTO document = (PfDocumentsDTO) baseDAO.queryElementOneParameter("file.documentAll", "hash", documentHash);
		signDTO.setPfDocument(document);

		String transactionId = new Long(new Date().getTime()).toString();
		signDTO.setCtransaction(transactionId);
		
		signDTO.setCapplication(conf.getString(ConstantsSigner.AFIRMA5_APPLICATION));
		try {
			URL url = new URL(conf.getString(ConstantsSigner.AFIRMA5_ENDPOINT));
			signDTO.setCserver(url.getHost());
		} catch (MalformedURLException e) {
			log.error(e);
		}
		signDTO.setCformat(format);
		signDTO.setCtype(storageType);

		// Getting server date
		signDTO.setFstate(Calendar.getInstance().getTime());
		
		// Fecha validez del certificado con el que se creo la firma
		signDTO.setfValid(fechaValidezCertificado);

		// InputStream inputCSV = null;
		byte[] signBinary = null;
		Base64Coder coder = new Base64Coder();
		try {
			if(applicationBO.isTrifasicaActiva()) {
				signBinary = obtenerContenidoFirma(new String(coder.decodeBase64(firma)));
			} else {
				signBinary = coder.decodeBase64(firma);
			}
		} catch (Exception e) {
			log.error("Error in decode binary sign", e);
		}
				
		// --------------------- VALIDAR FIRMA -----------------------
		if (validateSigns) {
			try {
				validateSign(requestTag, document, signBinary);
			} catch (InvalidSignException e) {
				requestTag.setValidada(false);
				e.setDetalleDocumentoNoValido(document.getDname());					
				throw e;
			}
		}

		if(esMiniapplet) {
			//Sellado de tiempo (Si corresponde), se estampa sólo si se firmo con Miniapplet/Autofirma. FIRe lo estampa a petición.
			signBinary = addTimeStamp(requestTag, userDTO, signDTO, signBinary);
		}

		signDTO.setPfUser(userDTO);
		signDTO.setPfSigner(signerDTO);
		signDTO.setLmovil(false);

		baseDAO.insertOrUpdate(signDTO);

		saveSign(signDTO, userDTO, signBinary, custodyService);
	}

	public void validateSign(PfRequestTagsDTO requestTag, PfDocumentsDTO document, byte[] signBinary) throws InvalidSignException {
		if (applicationVO.getValidateSign()) {
			try {
				long idConf = requestTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
				validarFirmaSiActivo (signBinary, idConf);
			} catch (InvalidSignException e) {
				requestTag.setValidada(false);
				log.error("No se puede validar la firma para la firma del documento con pk: " + document.getPrimaryKeyString());
				e.setDetalleDocumentoNoValido(document.getDname());					
				throw e;
			}
		}
	}

	public byte[] addTimeStamp(PfRequestTagsDTO requestTag, PfUsersDTO userDTO, PfSignsDTO signDTO, byte[] signBinary) throws TimeStampingException {
		// Si se indicó en la petición se pone el sello de firma (y se valida la firma)
		if (requestTag.getPfRequest().getLtimestamp().equals(Constants.C_YES)){
			try {
				long idConf = requestTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
				signBinary = estamparSelloSiActivo (signBinary, idConf, null, signDTO.getCformat());
//				try {
//					int a = 1 / (int) (Math.random() * 4) % 4;
//				} catch (Exception e) {
//					throw e;
//				}
			} catch (TimeStampingException e ) {
				log.error("No se puede generar el sello de tiempo para la firma del documento con pk: " + signDTO.getPfDocument().getPrimaryKeyString());
				e.setDetalleDocumentoNoValido(signDTO.getPfDocument().getDname());					
				throw e;
			} catch (Exception e) {
				log.error("Error ampliando firma", e);
				TimeStampingException tse = new TimeStampingException ("No se ha podido ampliar la firma", e);
				tse.setDetalleDocumentoNoValido(signDTO.getPfDocument().getDname());
				throw tse;
			}
		}
		return signBinary;
	}
	
	/**
	 * Estampa el sello de tiempo a una firma y la devuelve.	 * 
	 * @param firma
	 * @param idConf
	 * @param request 
	 * @param format 
	 * @return
	 * @throws TimeStampingException
	 */
	private byte[]  estamparSelloSiActivo (byte[] firma, long idConf, ArrayList <byte[]>certificados, String format) throws TimeStampingException {
		byte[] signResult = firma;

		// Estampar sello de tiempo con Afirma
		if (afirma5BO.estaActivoSello(idConf)) {
			RespuestaAmpliarFirma respuestaAmpliarFirma = null;
				
			try {
				respuestaAmpliarFirma = afirma5BO.estamparSelloDeTiempo(firma, certificados, idConf, format);
				signResult = respuestaAmpliarFirma.getFirmaAmpliada();
			} catch (Throwable t) {
				log.error("Error ampliando firma", t);
				throw new TimeStampingException ("No se ha podido ampliar la firma", t);
			}
				
			if (respuestaAmpliarFirma.isError()) {
				log.error("Afirma esta devolviendo un error al intentar ampliar la firma: " + respuestaAmpliarFirma.getMensajeAmpliado());
				throw new TimeStampingException ("Afirma esta devolviendo un error al intentar ampliar la firma: " +  respuestaAmpliarFirma.getMensajeAmpliado());
			}
			// Estampar sello de tiempo con la plataforma eeutil
		} else if (eeUtilOperFirmaBO.checkTimestamp(idConf)) {
			try {
				signResult = eeUtilOperFirmaBO.estamparSelloDeTiempo(firma, null, idConf);
			} catch (EeutilException pe) {
				log.error("No se ha podido agregar el sello de tiempo: ", pe);
				throw new TimeStampingException ("No se ha podido agregar el sello de tiempo", pe);
			}
		}
		
		return signResult;
					
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
		} else if (eeUtilOperFirmaBO.checkValidarFirma(idConf)) {
			// Como los documentos de las firmas contienen al documento, no hace falta enviar ni el documento ni el hash.
			try {
				String valida = eeUtilOperFirmaBO.validarFirma(firma, null, null, null, null, idConf);	
				if (!"OK".contains(valida)) {	
					if(valida.contains("El certificado había expirado")){
						log.error("El certificado había expirado: " + valida);
						throw new InvalidSignException ("El certificado había expirado en el momento de la firma.");						
					}else{
						log.error("Firma no válida: " + valida);
						throw new InvalidSignException ("Firma no válida.");
					}
				}
			} catch (EeutilException pe) {
				log.error("No se ha podido validar la firma: ", pe);
				throw new InvalidSignException ("No se ha podido validar la firma", pe);
			}
		}
	}

	/**
	 * Método que guarda una firma de usuario en base de datos
	 * @param signDTO Firma a guardar
	 * @param userDTO Usuario que firma
	 * @param input Puntero al contenido de la firma
	 * @param size Tamaño de la firma
	 * @throws CustodyServiceException 
	 * @throws Exception
	 */
	public void saveSign(PfSignsDTO signDTO, PfUsersDTO userDTO, byte[] signBinary, CustodyServiceInput custodyService) throws CustodyServiceException  {
//		try {
//			int a = 1 / (int) (Math.random() * 4) % 4;
//		} catch (Exception pe) {
//			CustodyServiceException e = new CustodyServiceException ("Error", pe);
//			throw e;
//		}
		// Se preparan los datos binarios de la firma para guardarlos
		InputStream input = new ByteArrayInputStream(signBinary);
		int size = signBinary.length;

		CustodyServiceInputSign custodySign = new CustodyServiceInputSign();
		custodySign.setApplicationServer(signDTO.getCapplication());
		custodySign.setDate(signDTO.getFstate());
		custodySign.setFormat(signDTO.getCformat());
		custodySign.setIdentifier(signDTO.getPrimaryKeyString());
		custodySign.setServer(signDTO.getCserver());
		custodySign.setSize(new BigDecimal(size));
		custodySign.setTransaction(signDTO.getCtransaction());
		custodySign.setType(Constants.SIGN_TYPE_SERVER);
		custodySign.setUserFullName(userDTO.getFullName());
		custodySign.setUserIdentifier(userDTO.getCidentifier());

		List<CustodyServiceOutputDocument> custodyDocs = new ArrayList<CustodyServiceOutputDocument>();
		CustodyServiceOutputDocument doc = new CustodyServiceOutputDocument();
		doc.setIdentifier(signDTO.getPfDocument().getChash());
		doc.setUri(signDTO.getPfDocument().getPfFile().getCuri());			
		custodySign.setDocuments(custodyDocs);
		
		//Agustin-->Asigno a dir3 la entidad de sigem		
		//String dir3 = obtenerDir3(userDTO);
		String dir3 = signDTO.getPfDocument().getPfRequest().getDreference().split("_")[2];
		custodySign.setRefNasDir3(dir3);
		String idEni = Util.getInstance().generarIDEni(Util.getInstance().createHash(input), dir3);
		custodySign.setIdEni(idEni);
		
		String uri = custodyService.uploadSign(custodySign, input);
		signDTO.setCuri(uri);
		signDTO.setFmodified(Calendar.getInstance().getTime());
		signDTO.setRefNASDir3Firma(dir3);
		signDTO.setRefNASIdEniFirma(idEni);
//		if (signDTO.getfValid() == null) {
//			log.error("No se puede validar la firma no tiene fecha de validez");
//			throw new CustodyServiceException("No se puede validar la firma no tiene fecha de validez");
//		}
		
		baseDAO.insertOrUpdate(signDTO);
	}

	private String obtenerDir3(PfUsersDTO userDTO) {
		String dir3 = null;
		try {			
			if(userDTO.getCtype().equals(Constants.C_TYPE_USER_EXTERNAL)) {
				dir3 = userDTO.getPortafirmas().getDir3();
			} else {
				dir3 = userDTO.getPfProvince().getCcodigoprovincia();
			}
			if (util.esVacioONulo(dir3)) {
				dir3 = custodyServiceFactory.custodiaDir3PorDefecto();
			}
		} catch (Exception e) {
			dir3 = custodyServiceFactory.custodiaDir3PorDefecto();
		}
		return dir3;
	}

	/**
	 * Método que extrae el bloque de datos que se deben firmar para una determinada petición
	 * @param signableData Bloque completo de los datos a firmar
	 * @param relacionPeticionesDocumento Indices que relacionan las peticiones con su número de documentos
	 * @param indicePeticiones Petición actual
	 * @param indiceDocumentos Documento actual
	 * @return Bloque de datos a firmar
	 */
	public String extractDataToSign(String signableData, List<Integer> relacionPeticionesDocumentos,
									Integer indicePeticiones, AtomicInteger indiceDocumentos) {
		String result = "";

		String[] data = signableData.split(",");
		int numDocs = relacionPeticionesDocumentos.get(indicePeticiones);
		int indiceFin = indiceDocumentos.intValue() + numDocs;

		String[] extractedData = Arrays.copyOfRange(data, indiceDocumentos.intValue(), indiceFin);

		for (int i = 0; i < extractedData.length; i++) {
			result = result.concat(extractedData[i]);
			if (i < extractedData.length - 1) {
				result = result.concat(",");
			}
		}

		//indiceDocumentos = indiceDocumentos + numDocs;
		indiceDocumentos.addAndGet(numDocs);

		return result;
	}

	/**
	 * Método que obtiene el bloque de documentos a firmar
	 * @param signableDocs Bloque completo de documentos
	 * @param relacionPeticionesDocumento Indices que relacionan las peticiones con su número de documentos
	 * @param indicePeticiones Petición actual
	 * @param indiceDocumentos Documento actual
	 * @return Documentos a firmar
	 */
	public List<PfDocumentsDTO> extractDocsToSign(List<PfDocumentsDTO> signableDocs, List<Integer> relacionPeticionesDocumentos,
									Integer indicePeticiones, AtomicInteger indiceDocumentos) {
		List<PfDocumentsDTO> docs = null;
		
		int numDocs = relacionPeticionesDocumentos.get(indicePeticiones);
		docs = signableDocs.subList(indiceDocumentos.intValue(), indiceDocumentos.intValue() + numDocs);
		
		return docs;
	}

	/**
	 * M&eacute;todo que inserta una nueva firma en la base de datos.
	 * @param blockDTO Objeto que define un bloque de firmas.
	 * @param serverSigns Lista de firmas realizadas por el servidor. Corresponden con DTOs de firma.
	 * @param userDTO Objeto que define el usuario que quiere realizar la firma o firmas.
	 * @param userJobDTO Objeto que define un cargo v&aacute;lido del usuario que quiere realizar la firma.
	 * @param mapSign Mapa con la informaci&oacute;n completa de las firmas del bloque de la petici&oacute;n.
	 * @param conf Configuraci&oacute;n del servidor de firma seleccionado.
	 * @param mapSignKey
	 * @param job Indica si el usuario que firma lo hace en calidad de Cargo.
	 * @throws CustodyServiceException
	 */
	public void insertSign(PfBlocksDTO blockDTO, List<PfSignsDTO> serverSigns,
			PfUsersDTO userDTO, PfUsersDTO userJobDTO,
			Map<String, SignerBlock> mapSign, Configuration conf,
			boolean mapSignKey, boolean job, List<AbstractBaseDTO> requestTagList) throws CustodyServiceException {
		PfBlockSignsDTO blockSignDTO = null;
		String storageType = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.FIRMAS.name());
		CustodyServiceInput custodyService = custodyServiceFactory.createCustodyServiceInput(storageType);

		// Search signer
		PfSignersDTO signerDTO = null;

		for (PfSignsDTO serverSign : serverSigns) {
			// Se obtiene la etiqueta petción asociada a esta firma para poder obtener el usuario
			String dir3 ="";
			for (AbstractBaseDTO requestTag : requestTagList) {
				PfRequestTagsDTO reqTag = (PfRequestTagsDTO) requestTag;
				if (serverSign.getPfSigner().getPfSignLine().getPrimaryKeyString().equals(reqTag.getPfSignLine().getPrimaryKeyString())) {
					signerDTO = tagBO.lastSignerUnresolvedDocument(reqTag, userDTO, userJobDTO, job);
					//Agustin asigno a dir3 la entidad de sigem
					dir3 =reqTag.getPfRequest().getDreference().split("_")[2];
					break;
				}
			}

			serverSign.setCapplication(blockDTO.getCapplication());
			serverSign.setCserver(blockDTO.getCserver());
			serverSign.setCformat(Constants.SIGN_FORMAT_CMS);
			serverSign.setCtype(storageType);

			// TODO: Ver qu&eacute; fecha coger ... en principio misma q bloque
			serverSign.setFstate(blockDTO.getFsign());

			SignerBlock sign = null;
			if (mapSignKey) {
				sign = mapSign.get(serverSign.getCtransaction());
			} else {
				sign = mapSign.values().iterator().next();
			}
			InputStream input = null;
			int size = 0;
			try {
				byte[] signBinary = new Base64Coder().decodeBase64((sign
						.getSign().getBytes()));
				input = new ByteArrayInputStream(signBinary);
				size = signBinary.length;
			} catch (Exception e) {
				log.error("Error in decode binary sign");
//				throw new FacesException("Error in decode binary sign");
			}

			serverSign.setCtransaction(sign.getIdTransaction());

			// User
			serverSign.setPfUser(userDTO);

			// Signer
			serverSign.setPfSigner(signerDTO);

			blockSignDTO = new PfBlockSignsDTO();
			blockSignDTO.setPfBlock(blockDTO);
			blockSignDTO.setPfSign(serverSign);
			
//			if (serverSign.getfValid() == null) {
//				log.error("No se puede validar la firma no tiene fecha de validez");
//				throw new CustodyServiceException("No se puede validar la firma no tiene fecha de validez");
//			}

			baseDAO.insertOrUpdate(serverSign);
			baseDAO.insertOrUpdate(blockSignDTO);

			CustodyServiceInputSign custodySign = new CustodyServiceInputSign();
			custodySign.setApplicationServer(blockDTO.getCapplication());
			custodySign.setDate(blockDTO.getFsign());
			custodySign.setFormat(blockDTO.getCformat());
			custodySign.setIdentifier(serverSign.getPrimaryKeyString());
			custodySign.setServer(blockDTO.getCserver());
			custodySign.setSize(new BigDecimal(size));
			custodySign.setTransaction(serverSign.getCtransaction());
			custodySign.setType(Constants.SIGN_TYPE_SERVER);
			custodySign.setUserFullName(userDTO.getFullName());
			custodySign.setUserIdentifier(userDTO.getCidentifier());
			//Agustin asigno a dir3 la entidad de sigem
			//String dir3 = userDTO.getPfProvince().getCcodigoprovincia();	
			custodySign.setRefNasDir3(dir3);
			String idEni = Util.getInstance().generarIDEni(Util.getInstance().createHash(input), dir3);
			custodySign.setIdEni(idEni);

			List<CustodyServiceOutputDocument> custodyDocs = new ArrayList<CustodyServiceOutputDocument>();
			CustodyServiceOutputDocument doc = new CustodyServiceOutputDocument();
			doc.setIdentifier(serverSign.getPfDocument().getChash());
			doc.setUri(serverSign.getPfDocument().getPfFile().getCuri());			
			custodySign.setDocuments(custodyDocs);

			String uri = custodyService.uploadSign(custodySign, input);
			serverSign.setCuri(uri);
			serverSign.setFmodified(Calendar.getInstance().getTime());
			serverSign.setRefNASDir3Firma(dir3);
			serverSign.setRefNASIdEniFirma(idEni);
			baseDAO.insertOrUpdate(serverSign);

		}
	}

	public void insertChangesHistory(PfRequestsDTO request, String type,
			String change) {
		PfHistoricRequestsDTO changeToRegister = new PfHistoricRequestsDTO();
		changeToRegister.setPfRequest(request);
		changeToRegister.setChistoricRequest(type);
		changeToRegister.setThistoricRequest(change);
		baseDAO.insertOrUpdate(changeToRegister);
	}
	
	/**
	 * M&eacute;todo que devuelve el conjunto de par&aacute;metros de configuraci&oacute;n del servidor de firma seleccionado.
	 * @param idConf Idetificador de la configuraci&oacute;n del servidor de firma seleccionado.
	 * @return Objeto "Configuration" que contiene el conjunto de par&aacute;metros de configuraci&oacute;n del servidor de firma seleccionado. 
	 * @throws FacesException
	 */
	public Configuration loadSignProperties(long idConf) { // throws FacesException {
		Configuration conf = new CompositeConfiguration();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cparam", "FIRMA.%");
		parameters.put("idConf", idConf);
		List<AbstractBaseDTO> configurationParameters = baseDAO.queryListMoreParameters("request.signConfigParam", parameters);
		// Build Properties object for authenticator
		Map<String, String> auxMap = ConfigurationUtil.convierteListaParametrosConfiguracionEnMapa(configurationParameters);
		if (auxMap.containsValue(null) || auxMap.containsValue("")) {
//			throw new FacesException("Error loading authenticator parameters");
		} else {

			conf.addProperty(ConstantsSigner.AFIRMA5_SERVER_CERT_ALIAS, auxMap.get(AuthenticatorConstants.PARAM_SERVER_CERT_ALIAS));
			conf.addProperty(ConstantsSigner.AFIRMA5_APPLICATION, auxMap.get(AuthenticatorConstants.PARAM_APPLICATION));
			conf.addProperty(ConstantsSigner.AFIRMA5_TRUSTSTORE, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_TRUSTEDSTORE));
			conf.addProperty(ConstantsSigner.AFIRMA5_ENDPOINT, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_URL));
			conf.addProperty(ConstantsSigner.AFIRMA5_HASH_ALGORITH, auxMap.get(AuthenticatorConstants.PARAM_HASH_ALGORITHM));
			conf.addProperty(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT, auxMap.get(AuthenticatorConstants.PARAM_SIGNATURE_FORMAT));

			// Se a&ntilde;aden los nuevos par&aacute;metros para los distintos modos de
			// validaci&oacute;n.
			// No he visto el modo de validaci&oacute;n por ning&uacute;n sitio :S
			conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_OPTION, auxMap
					.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_MODE));
			if (auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_MODE) != null) {
				if (auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_MODE).equals(Constants.C_PARAMETER_USERTOKEN)) {
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_USER, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_USER));
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_PASSWORD, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_PASSWORD));
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_PASS_TYPE, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_PASSWORD_TYPE));
				} else if (auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_MODE).equals(Constants.C_PARAMETER_BINARY)) {
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_KEYSTORE, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_KEYSTORE));
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_KEYSTORE_TYPE, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_KEYSTORE_TYPE));
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_KEYSTORE_PASS, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_KEYSTORE_PWD));
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_CERT_ALIAS, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_CERT_ALIAS));
					conf.addProperty(ConstantsSigner.AFIRMA5_SECURITY_CERT_PASS, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_SECURITY_CERT_PWD));
				}
			}

			conf.addProperty(AuthenticatorConstants.AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE_PASS, auxMap.get(AuthenticatorConstants.PARAM_AFIRMA5_TRUSTEDSTORE_PASS));
			conf.addProperty(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_USER, auxMap.get(AuthenticatorConstants.PARAM_SERVER_HTTP_USER));
			conf.addProperty(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_PASS, auxMap.get(AuthenticatorConstants.PARAM_SERVER_HTTP_PASS));
			// Sign mode and data sign type
			conf.addProperty(Constants.C_PARAMETER_SIGN_MODE, auxMap.get(Constants.C_PARAMETER_SIGN_MODE));
			conf.addProperty(Constants.C_PARAMETER_SIGNATURE_MODE, auxMap.get(Constants.C_PARAMETER_SIGNATURE_MODE));
		}
		return conf;
	}

	/**
	 * Método que devuelve las firmas realizadas en una fecha concreta
	 * @param date Fecha
	 * @return Lista de firmas
	 */
	public List<AbstractBaseDTO> getSignsFromDate(Date date) {
		return baseDAO.queryListOneParameter("sign.signsByDate", "fecha", date);
	}

	/**
	 * Valida que todos los códigos hash de los documentos recibidos corresponden con los que se van a guardar
	 * @param requestSignatures
	 * @return
	 */
	public boolean validHashDocumentsCode(RequestSignature requestSignatures) {
		boolean isValid = true;
		List<DocumentSignature> signatures = requestSignatures.getSignatures();
		for (Iterator<DocumentSignature> it = signatures.iterator(); it.hasNext();) {
			DocumentSignature documentSignature = (DocumentSignature) it.next();
			isValid = validHashDocumentCode(documentSignature);
		}
		return isValid;
	}

	/**
	 * @param documentSignature
	 * @return
	 */
	private boolean validHashDocumentCode(DocumentSignature documentSignature) {
		documentSignature.getDocHash();
		return false;
	}

	public void comprobarFirmaTrifasica(RequestSignatureConfig signatureConfig) {
		if(signatureConfig.getType().equals(Constants.C_TYPE_SIGNLINE_SIGN)) {
			if(applicationBO.isTrifasicaActiva()) {
				List<DocumentSignatureConfig> documentsConfig = signatureConfig.getDocumentsConfig();
				for (DocumentSignatureConfig config : documentsConfig) {
					config.setMode(Constants.SIGN_HASH_SIGNATURE_MODE);
					config.setSignFormatParameter(config.getSignFormatParameter() + Constants.SUFIJO_OPERACION_TRIFASICA);
					config.setSignParameter(config.getSignParameter() + "\nserverUrl=" + applicationBO.obtenerTrifasicaURL());
				}
			}
		}
	}
	
}
