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

package es.seap.minhap.portafirmas.business.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.HistoricRequestBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.RequestDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.ws.WSUtil;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.CsvJustificante;
import es.seap.minhap.portafirmas.ws.bean.DocumentTypeList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJobList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserJobAssociatedList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserList;
import es.seap.minhap.portafirmas.ws.bean.ImportanceLevelList;
import es.seap.minhap.portafirmas.ws.bean.JobList;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.SeatList;
import es.seap.minhap.portafirmas.ws.bean.Signature;
import es.seap.minhap.portafirmas.ws.bean.SignatureSerializable;
import es.seap.minhap.portafirmas.ws.bean.StateList;
import es.seap.minhap.portafirmas.ws.bean.UserList;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class QueryServiceBO {

	private Logger log = Logger.getLogger(QueryServiceBO.class);
	
	@Autowired
	private TagBO tagBO;
	
	@Autowired
	private HistoricRequestBO historicRequestBO;

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private RequestDAO requestDAO;

	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private BinaryDocumentsBO binaryDocumentsBO;
	
	@Autowired
	private AuthenticationWSHelper authenticationWSHelper;

	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	SignatureServiceBO signatureServiceBO;

	/*
	 * WS METHODS
	 */

	/**
	 * Metodo que permite descargar un documento de la BD.
	 * @param authentication Parametro de autenticacion.
	 * @param documentId Identificador del documento.
	 */	
	@Transactional(readOnly=false)
	public DataHandler downloadDocument(Authentication authentication, String documentId) throws PfirmaException {
		log.debug("downloadDocument init: " + documentId);
		DataHandler result = null;
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			PfDocumentsDTO doc = requestBO.checkDocument(documentId);
			
			// Checking document
			
			if (doc != null) {
				log.debug("Checking user can access doocument");
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, doc.getPfRequest());
				
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(doc);
				result = new DataHandler(new ByteArrayDataSource(bytes, doc.getDmime()));
			}
			
			log.debug("downloadDocument end ");

			return result;
			
		} catch (DocumentCantBeDownloadedException de) {
			log.error ("No se puede descargar el documento: " + de.getDetalleDocumento(), de);
			throw new PfirmaException (de.getMessage());
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Metodo que recupera usuarios del Portafirmas.
	 * @param authentication Parametro de autenticacion.
	 * @param query Filtro de busqueda.
	 * @return Listado de usuarios.
	 * @throws PfirmaException Excepcion del Portafirmas.
	 */
	@Transactional(readOnly=false)
	public UserList queryUsers(Authentication authentication, String query) throws PfirmaException {
		log.warn("autenticación: " + authentication.getUserName() + " query: " + query);
		UserList result = null;
		List<AbstractBaseDTO> resultAux = null;		
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			if (query != null && !"".equals(query)) {
				log.debug("Query with filter");
				resultAux = baseDAO.queryListOneParameter("request.usersComplete", "find", query);
			} else {
				log.debug("Query without filter");
				resultAux = baseDAO.queryListOneParameter("request.userAll", null, null);
			}

			if (resultAux != null && !resultAux.isEmpty()) {
				log.debug("Users found");
				result = WSUtil.pfUsersDTOListToUserList(resultAux);
			} else {
				log.error("Users not found");
				throw new PfirmaException("Users not found");
			}

			log.info("queryUsers end ");

			return result;
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Metodo que permite descargar un fichero de firma de la BD.
	 * @param authentication Parametro de autenticacion.
	 * @param documentId Identificador del documento firmado.
	 * @return Firma.
	 * @throws PfirmaException Excepcion del Portafirmas.
	 */
	
	@Transactional(readOnly=false)
	public Signature downloadSign(Authentication authentication, String documentId) throws PfirmaException {
		log.info("downloadSign init: " + documentId);
		try {
						
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());				
			// Checking document
			PfDocumentsDTO doc = requestBO.checkDocument(documentId);
			//PfDocumentsDTO doc = null;
			authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, doc.getPfRequest());				
			return getSignature(doc);			
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}
	
	public Signature downloadSignNoAuthentication (String documentId) throws PfirmaException {
		log.info("downloadSignNoAuthentication init: " + documentId);
		try {
			PfDocumentsDTO doc = requestBO.checkDocument(documentId);
//			PfDocumentsDTO doc = null;
			Signature signature = getSignature(doc);
			
			log.info("downloadSignNoAuthentication end: " + documentId);
			return signature;
//		} catch (PfirmaException pf) {
//			throw pf;
		} catch (Exception e) {			
			throw new PfirmaException("No se puede descargar la firma " + e.getMessage(), e);
		}
			
	}
	
	/**
	 * Metodo que descarga un fichero de firma serializable.
	 * @param documentId Identificador del documento firmado.
	 * @return Firma serializable.
	 * @throws PfirmaException Excepcion del Portafirmas.
	 */
	public SignatureSerializable downloadSignSerializable(String documentId) throws PfirmaException {
		log.info("downloadSign init: " + documentId);
		try {			
			// Checking document
			PfDocumentsDTO doc = requestBO.checkDocument(documentId);
			//PfDocumentsDTO doc = null;
			log.info("downloadSign end ");
			return signatureServiceBO.getSignatureSerializable(doc);

//		} catch (PfirmaException pf) {
//			log.error("Error descargando firma: " + pf);
//			throw pf;
		} catch (Exception e) {
			log.error("Error descargando firma: ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}	

	/**
	 * Metodo que obtiene la firma de un documento.
	 * @param doc Documento.
	 * @return Firma del documento.
	 * @throws IOException 
	 * @throws CustodyServiceException
	 */
	private Signature getSignature(PfDocumentsDTO doc) throws DocumentCantBeDownloadedException, IOException {
		
		Signature signature = new Signature();

		// Si el documento es firmable se obtiene su firma (si no, es un anexo y no tiene firmas)
		if (doc.getLsign()) {
			SignatureSerializable signatureSerializable = signatureServiceBO.getSignatureSerializable(doc);
			BeanUtils.copyProperties(signatureSerializable, signature);
			
			if (signatureSerializable.getContentBytes() != null) {
				DataHandler content = new DataHandler(new ByteArrayDataSource(signatureSerializable.getContentBytes(), signatureSerializable.getMimeType()));
				signature.setContent(content);
			}
		}

		return signature;
	}
	

	/**
	 * Metodo que devuelve los tipos de documentos disponibles.
	 * @param authentication Parametro de autenticacion.
	 * @param query Filtro de tipos de documentos.
	 * @return Listado de tipos de documentos.
	 * @throws PfirmaException Excepcion de Portafirmas.
	 */
	@Transactional(readOnly=false)
	public DocumentTypeList queryDocumentTypes(Authentication authentication, String query)
			throws PfirmaException {
		log.info("queryDocumentTypes init: " + query);
		DocumentTypeList result = null;
		
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());			
		
			List<PfDocumentTypesDTO> resultAux = null;
			
			if (query != null && !"".equals(query)) {
				log.debug("Query with filter");
				resultAux = baseDAO.queryListOneParameter("administration.documentTypeQueryGeneral", "query", query);
				List<PfDocumentTypesDTO> resultAuxApp = baseDAO.queryListOneParameter("administration.documentTypeQueryApplication", "query", query);
				if (resultAuxApp != null && !resultAuxApp.isEmpty()) {
					resultAux.addAll(resultAuxApp);
				}
			} else {
				log.debug("Query without filter");
				resultAux = baseDAO.queryListOneParameter("administration.documentTypeAll", null, null);
			}

			if (resultAux != null && !resultAux.isEmpty()) {
				log.debug("Document types found");
				result = WSUtil.pfDocumentTypeDTOListToDocumentTypeList(resultAux);
			} else {
				log.error("Document types not found");
				throw new PfirmaException("Document types not found");
			}

			log.info("queryDocumentTypes end ");

			return result;
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Metodo que obtiene todos los cargos disponibles.
	 * @param authentication Parametro de autenticacion.
	 * @param query Filtro de cargos.
	 * @return Listado de cargos.
	 * @throws PfirmaException Excepcion del Portafirmas.
	 */
	@Transactional(readOnly=false)
	public JobList queryJobs(Authentication authentication, String query) throws PfirmaException {
		log.info("queryJobs init: " + query);
		JobList result = null;
		List<AbstractBaseDTO> resultAux = null;
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			if (query != null && !"".equals(query)) {
				log.debug("Query with filter");
				resultAux = baseDAO.queryListOneParameter(
						"administration.jobsQuery", "query", query);
			} else {
				log.debug("Query without filter");
				resultAux = baseDAO.queryListOneParameter(
						"administration.jobsAll", null, null);
			}

			if (resultAux != null && !resultAux.isEmpty()) {
				log.debug("Jobs found");
				result = WSUtil.pfUsersDTOListToJobList(resultAux);
			} else {
				log.error("Jobs not found");
				throw new PfirmaException("Jobs not found");
			}

			log.info("queryJobs end ");

			return result;
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}

	// TODO: Hay que ver en que idioma se va a devolver? De momento, se devuelve
	// tal como esta en BBDD
	/**
	 * Metodo que obtiene el listado con las etiquetas de estado disponibles. 
	 * @param authentication Parametro de autenticacion.
	 * @param query Fitlro de etiquetas de estado.
	 */
	@Transactional(readOnly=false)
	public StateList queryStates(Authentication authentication, String query) throws PfirmaException {
		log.info("queryStates init: " + query);
		StateList result = null;
		List<AbstractBaseDTO> resultAux = null;
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());			
			
			if (query != null && !"".equals(query)) {
				log.debug("Query with filter");
				resultAux = baseDAO.queryListOneParameter("request.tagID",
						"tagID", query);
			} else {
				log.debug("Query without filter");
				resultAux = baseDAO.queryListOneParameter("request.statesAll",
						null, null);
			}

			if (resultAux != null && !resultAux.isEmpty()) {
				log.debug("States found");
				result = WSUtil.pfTagsDTOListToStateList(resultAux);
			} else {
				log.error("States not found");
				throw new PfirmaException("States not found");
			}

			log.info("queryStates end ");

			return result;
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Metodo que obtiene un apeticion de la BD.
	 * @param authentication Parametro de autenticacion.
	 * @param requestId Identificador de la peticion.
	 * @return Peticion.
	 * @throws PfirmaException Excepcion del Portafirmas.
	 */
	@Transactional(readOnly=false)
	public Request queryRequest(Authentication authentication, String requestId) throws PfirmaException {
		log.info("queryRequest init: " + requestId);
		Request result = null;
		PfRequestsDTO resultAux = null;
		String text = null;
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			resultAux = checkRequest(requestId);
			authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, resultAux);
						
			// TODO: Revisar esto para ver como deber&iacute;a devolverlo. Hay que
			// tener en cuenta que el texto esta almacenado en HTML. yo creo
			// que as&iacute; tal cual esta bien pero si opinais otra cosa ...
			text = requestDAO.queryRequestText(resultAux).getTrequest();
			
			//[Teresa]El request no devuelve al firmante por lo tanto hay que comprobar si la línea de firma esta firmada y devolver el firmante
			//Eliminando las líneas
			actualizarFirmante(resultAux);
			
			result = WSUtil.pfRequestDTOToRequest(resultAux, text, applicationBO.getMapaParametrosAplicacion(resultAux.getPfApplication()), tagBO);	
			

			log.info("queryRequest end ");

			return result;
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error"+e.getMessage(), e);
		}
	}
	
	/**
	 * Esto no nos vale para firma en cascada ya que van a tener una sola línea de firma y muchos firmantes
	 * Solo vale para secuencia una línea de firma por firmante
	 * Lo he hablado con felipe y como nos urge sacarlo en un futuro nos quedará hacer la firma paralela
	 * **/
	private void actualizarFirmante(PfRequestsDTO request) {
		if (request != null) {
			if(request.getLcascadeSign()){
				Set<PfSignLinesDTO> signLines = request.getPfSignsLines();
				ArrayList<PfSignLinesDTO> lineasFirma = ordenarLineasFirma(signLines);
				List<AbstractBaseDTO> firmantes = historicRequestBO.getHistoricoByRequestValorPet(request, Constants.C_HISTORIC_REQUEST_SIGNED);
				int i = 0;
				for (AbstractBaseDTO abstractBaseDTO : firmantes) {
					List<PfSignersDTO> signersToDelete = new ArrayList<PfSignersDTO> ();
					PfHistoricRequestsDTO firma = (PfHistoricRequestsDTO)abstractBaseDTO;
					PfSignLinesDTO lineaFirma = lineasFirma.get(i);
					Set<PfSignersDTO> signers = lineaFirma.getPfSigners();
					for (PfSignersDTO signer : signers) {
						PfUsersDTO userFirma = signer.getPfUser();
						//System.out.println("userFirma.getPrimaryKey() "+userFirma.getPrimaryKey()+" - firma.getPfUser().getPrimaryKey() "+firma.getPfUser().getPrimaryKey());
						if(!userFirma.getPrimaryKey().equals(firma.getPfUser().getPrimaryKey())){
							signersToDelete.add(signer);
						}
					}
					if(signersToDelete.size()>0){
						signers.removeAll(signersToDelete);
					}
					i++;
				}
			}
			else{
				//quedaría ver como se hace la firma paralela
			}
		}		
	}

	private ArrayList<PfSignLinesDTO> ordenarLineasFirma(Set<PfSignLinesDTO> signLines) {
		ArrayList<PfSignLinesDTO> lineasResultado = new ArrayList<PfSignLinesDTO>();
		int[] vectorLinea = obtenerPrimaryKeyLinea(signLines);
		Arrays.sort(vectorLinea);
		for (int i = 0; i < vectorLinea.length; i++) {
			lineasResultado.add(i, obtenerPfSignLinesDTO(signLines, vectorLinea[i]));
		}
		return lineasResultado;
	}

	private PfSignLinesDTO obtenerPfSignLinesDTO(Set<PfSignLinesDTO> signLines,	int i) {
		PfSignLinesDTO resultado = new PfSignLinesDTO();
		boolean encontrado = false;
		for (PfSignLinesDTO signLine : signLines) {
			if(!encontrado && signLine.getPrimaryKey().intValue() == i){
				resultado = signLine;
				encontrado = true;
			}
		}
		return resultado;
	}

	private int[] obtenerPrimaryKeyLinea(Set<PfSignLinesDTO> signLines) {
		int [] resultado = new int [signLines.size()];
		int i = 0;
		for (PfSignLinesDTO signLine : signLines) {
			resultado[i] = signLine.getPrimaryKey().intValue();
			i++;
		}
		return resultado;
	}

	/**
	 * Metodo que recupera una peticion de la BD sin necesidad de autenticarse.
	 * @param requestId Identificador de la peticion.
	 * @return Peticion.
	 * @throws PfirmaException Excepcion de Portafirmas.
	 */
	public Request queryRequestNoAuthentication(String requestId) throws PfirmaException {
		log.info("queryRequestNoAuthentication init: " + requestId);
		Request result = null;
		PfRequestsDTO resultAux = null;
		String text = null;
		try {
			
			resultAux = checkRequest(requestId);						
			// TODO: Revisar esto para ver como deber&iacute;a devolverlo. Hay que
			// tener en cuenta que el texto esta almacenado en HTML. yo creo
			// que as&iacute; tal cual esta bien pero si opinais otra cosa ...
			text = requestDAO.queryRequestText(resultAux).getTrequest();
			
			//[Teresa]El request no devuelve al firmante por lo tanto hay que comprobar si la línea de firma esta firmada y devolver el firmante
			//Eliminando las líneas
			actualizarFirmante(resultAux);
			
			result = WSUtil.pfRequestDTOToRequest(resultAux, text, applicationBO.getMapaParametrosAplicacion(resultAux.getPfApplication()), tagBO);	

			log.info("queryRequestNoAuthentication end ");

			return result;
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}
	


	/**
	 * Método que devuelve una lista con los niveles de importancia disponibles para una petición.
	 * @param authentication Párametro de autenticación.
	 * @param query Filtro de entrada para los niveles de importancia.
	 * @return Lista de niveles de importancia.
	 * @throws PfirmaException
	 */
	@Transactional(readOnly=false)
	public ImportanceLevelList queryImportanceLevels(Authentication authentication, String query) throws PfirmaException {
		ImportanceLevelList levels = null;
		if (query != null && !query.equals("")) {
			PfImportanceLevelsDTO levelDTO = (PfImportanceLevelsDTO) 
				baseDAO.queryElementOneParameter("request.queryImportanceLevelByCode", "codLevel", query);
			ArrayList<PfImportanceLevelsDTO> listDTO = new ArrayList<PfImportanceLevelsDTO>();
			listDTO.add(levelDTO);
			levels = WSUtil.pfImportanceLevelListToImportanceLevelList(listDTO);
		}
		else {
			List<PfImportanceLevelsDTO> levelListDTO = baseDAO.queryListOneParameter("request.importanceLevels", null, null);
			levels = WSUtil.pfImportanceLevelListToImportanceLevelList(levelListDTO);
		}
		return levels;
	}

	/**
	 * Metodo que genera el CVS de una firma.
	 * @param authentication Parametro de autenticacion.
	 * @param firma Firma de la que se obtiene el CVS.
	 * @return CVS de la firma.
	 * @throws PfirmaException Excepcion del Portafirmas.
	 */
	@Transactional(readOnly=false)
	public String getCVS(Authentication authentication, Signature firma) throws PfirmaException {
		String result = null;
		try {
			log.warn("getCVS -> Usuario = " + authentication.getUserName());
			// Se comprueba la autenticidad del usuario
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());

			result = Util.getInstance().getCSV(firma.getContent().getInputStream());
			
			
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("CVS error", e);
		}

		return result;
	}

	/**
	 * Obtiene el informe de firma de un documento a partir de su CSV
	 * @param authentication Parámetro de autenticación.
	 * @param csv CSV del documento.
	 * @return Informe del documento
	 * @throws PfirmaException Error del Portafirmas.
	 */
	/*public DataHandler getReportFromCSV(Authentication authentication, String csv) throws PfirmaException {
		DataHandler result = null;
		try {
			// Se comprueba la autenticidad del usuario
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());

			PfSignsDTO documentSign = (PfSignsDTO) baseDAO.queryElementOneParameter("request.signByCsv", "csv", csv);
			PfDocumentsDTO document = documentSign.getPfDocument();
			String docMime = document.getDmime();

			// Obtengo el contenido binario de la firma
			byte[] firma = obtenerFirma(documentSign);

			// Obtengo el contenido binario del documento
			byte[] documento = obtenerDocumento(document, authentication);
			
			String formatoFirma = documentSign.getCformat();
			String ambitoDocumento = document.getPfDocumentScope().getCdescription();

//			// Obtenemos la URL de validación
//			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//			HttpServletRequest request = (HttpServletRequest) attr.getRequest();
//			String direccion = request.getRequestURL().toString();
//	        int finDireccionBase = direccion.lastIndexOf(request.getContextPath()) + request.getContextPath().length();
//	        String url = direccion.substring(0,finDireccionBase);

			// Obtenemos el informe con el documento, la firma y el CSV
			//byte[] informeBytes = eeUtilBO.getDocumentWithSignInfo(firma, documento, docMime, csv, formatoFirma, ambitoDocumento);
			byte[] informeBytes = eeUtilBO.getDocumentWithSignInfo(firma, documento, docMime, formatoFirma, Constants.COPIA_AUTENTICA, csv, ambitoDocumento);
			result = new DataHandler(new ByteArrayDataSource(informeBytes, Constants.SIGN_MIMETYPE_PDF));
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			throw new PfirmaException("Error en el informe", e);
		}
		return result;
	}*/
	
	@Transactional(readOnly=false)
	public EnhancedUserList queryEnhancedUsers (Authentication authentication, String queryUser, String querySeat) throws PfirmaException {
		log.info("queryEnhancedUsers init: " + queryUser);
		EnhancedUserList result = null;
		List<AbstractBaseDTO> resultAux = null;		
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			resultAux = queryEnhancedUserJob (queryUser, querySeat,  Constants.C_TYPE_USER_USER, "S");
			
			result = WSUtil.pfUsersDTOListToEnhancedUserList(resultAux);
			
			log.debug("Users Found: " + result.getEnhancedUser().size());			

			log.info("queryEnhancedUsers end ");

			return result;
		
		} catch (Exception e) {
			log.error("ERROR en queryEnhancedUsers ", e);
			throw new PfirmaException("Unknown error", e);
		}

	}
	
	@Transactional(readOnly=false)
	public EnhancedJobList queryEnhancedJobs (Authentication authentication, String queryUser, String querySeat) throws PfirmaException {
		log.info("queryEnhancedJobs init: " + queryUser);
		EnhancedJobList result = null;
		List<AbstractBaseDTO> resultAux = null;		
		try {
			
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			resultAux = queryEnhancedUserJob (queryUser, querySeat, Constants.C_TYPE_USER_JOB, "S");
			
			result = WSUtil.pfUsersDTOListToEnhancedJobList(resultAux);
			
			log.debug("Jobs Found: " + result.getEnhancedJob().size());
			log.info("queryEnhancedJobs end ");

			return result;
		
		} catch (Exception e) {
			log.error("ERROR en queryEnhancedJobs ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}
	@Transactional(readOnly=false)
	public SeatList querySeats (Authentication authentication, String query) throws PfirmaException {
		authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
		return querySeatsNoAuthentication (query);

	}
	@Transactional(readOnly=false)
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToJob (Authentication authentication, String jobIdentifier) throws PfirmaException {
		authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
		return queryEnhancedUserJobAssociatedToJobNoAuthentication(jobIdentifier);
	}
	@Transactional(readOnly=false)
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToUser (Authentication authentication, String userIdentifier) throws PfirmaException {
		authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
		return queryEnhancedUserJobAssociatedToUserNoAuthentication(userIdentifier);
	}
	
	public SeatList querySeatsNoAuthentication (String query) throws PfirmaException {
		log.info("querySeats init: " + query);
		SeatList result = null;
		List<AbstractBaseDTO> resultAux = null;
		
		try {
			if (query == null) {
				query = "";
			}
			
			resultAux = baseDAO.queryListOneParameter("request.querySeats", "find", query);
			
			result = WSUtil.pfProvinceDTOListToSeatList(resultAux);
			
			log.debug("Seats Found: " + result.getSeat().size());
			log.info("querySeats end ");

			return result;
		
		} catch (Exception e) {
			log.error("ERROR en querySeats ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}
	
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToUserNoAuthentication (String userIdentifier) throws PfirmaException {
		
		List<AbstractBaseDTO> list = this.queryEnhancedUserJob(userIdentifier, null, Constants.C_TYPE_USER_USER, "S");
		
		if (list.size() == 0) {
			throw new PfirmaException ("No existe el usuario con identificador " + userIdentifier);
		}
		
		PfUsersDTO userDTO = (PfUsersDTO) list.get(0);
		
		list =  baseDAO.queryListOneParameter("request.userJob", "usuario", userDTO);
		
		
		return WSUtil.userJobDTOToEnhancedUserJobAssociatedList(list);
		
	}
	
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToJobNoAuthentication (String jobIdentifier) throws PfirmaException {
		
		List<AbstractBaseDTO> list = this.queryEnhancedUserJob(jobIdentifier, null, Constants.C_TYPE_USER_JOB, "S");
		
		if (list.size() == 0) {
			throw new PfirmaException ("No existe el cargo con identificador " + jobIdentifier);
		}
		
		PfUsersDTO jobDTO = (PfUsersDTO) list.get(0);
		
		list = baseDAO.queryListOneParameter("administration.jobUserAdmAssociated", "job", jobDTO);		
		
		return WSUtil.userJobDTOToEnhancedUserJobAssociatedList(list);
		
	}
	
	public List<AbstractBaseDTO> queryEnhancedUserJob (String queryUser, String querySeat, String tipo, String queryValid) {		
		String paramUser = "";
		String paramSeat = "";
		String paramValid = "";
		
		if (queryUser != null) {
			paramUser = queryUser;
		}
		if (querySeat != null) {
			paramSeat = querySeat;
		}
		if (queryValid != null) {
			paramValid = queryValid;
		}
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("userQuery", paramUser);
		parameters.put("seatQuery", paramSeat);
		parameters.put("typeQuery", tipo);
		parameters.put("validQuery", paramValid);
		
		List<AbstractBaseDTO> list = baseDAO.queryListMoreParameters("request.enhancedUsersComplete", parameters);
		return list;
	}
	
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public CsvJustificante queryCSVyJustificante (Authentication authentication, String documentId) throws PfirmaException{
		CsvJustificante csvJustificante = new CsvJustificante();
					
		try {
			// Se comprueba la autenticidad del usuario
			PfUsersDTO pfUserDto = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			PfDocumentsDTO document = requestBO.checkDocument(documentId);
			if (document != null){
				log.debug("Checking user can access doocument");
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, document.getPfRequest());
			}
			else{
				throw new PfirmaException("El documento: "+documentId+" no existe");
			}
			
			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (documentId);
			byte[] bytes = binaryDocumentsBO.getReportBySignDTO(signDTO, applicationBO.loadApplication(pfUserDto.getCidentifier()));
			
			DataHandler content = new DataHandler(new ByteArrayDataSource(bytes, Constants.SIGN_MIMETYPE_PDF));
			
			csvJustificante.setContent(content);
			csvJustificante.setCsv(signDTO.getCsv());
			csvJustificante.setMime(Constants.PDF_MIME);
			
			
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			log.error("Error en queryCSVyJustificante", e);
			throw new PfirmaException("Error en queryCSVyJustificante", e);
		}
		return csvJustificante;
	}
	
	/*
	 * AUXILIARY
	 */
	/**
	 * Método que obtiene el contenido de una firma a partir de su DTO
	 * @param firma DTO de la firma
	 * @return Contenido binario de la firma
	 * @throws CustodyServiceException
	 */
	/*private byte[] obtenerFirma(PfSignsDTO firma) throws CustodyServiceException {

		CustodyServiceOutputSign sign = null;
		String storageType = null;
		byte[] signature = null;

		if (firma != null) {
			sign = new CustodyServiceOutputSign();
			storageType = firma.getCtype();
			sign.setType(Constants.SIGN_TYPE_SERVER);
			sign.setIdentifier(firma.getPrimaryKeyString());
			sign.setUri(firma.getCuri());
		}
		if (storageType != null) {
			log.debug("Creating sign service");
			CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(storageType);

			log.debug("Downloading sign");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			custodyService.downloadSign(sign, outputStream);
			signature = outputStream.toByteArray();
			log.debug("Download sign succesfully");				
		} else {
			log.debug("Download sign error");
		}

		return signature;
	}*/

	/*private byte[] obtenerDocumento(PfDocumentsDTO documento, Authentication authentication) throws CustodyServiceException, PfirmaException {

		byte[] result = null;

		if (documento != null) {
//			log.debug("Checking user can access document");
//			authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, documento.getPfRequest());
			
			log.debug("Creating file service");
			final CustodyServiceOutput custodyService =
				custodyServiceFactory.createCustodyServiceOutput(documento.getPfFile().getCtype());

			log.debug("Downloading document");
			final CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
			custodyDocument.setIdentifier(documento.getChash());
			custodyDocument.setUri(documento.getPfFile().getCuri());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			custodyService.downloadFile(custodyDocument, outputStream);
			result = outputStream.toByteArray();
			log.info("downloadDocument end ");
		}
		else {
			log.info("downloadDocument error");
		}

		return result;
	}*/

	/**
	 * Metodo que comprueba si una peticion existe en BD.
	 * @param chash Codigo hash de la peticion.
	 * @return La peticion buscada.
	 * @throws PfirmaException Excepcion del Portafirmas.
	 */
	public PfRequestsDTO checkRequest(String chash) throws PfirmaException {
		PfRequestsDTO req = null;
		if (chash != null && !"".equals(chash)) {
			req = (PfRequestsDTO) baseDAO.queryElementOneParameter(
					"request.requestHashWithSigners", "hash", chash);
			if (req == null) {
				throw new PfirmaException("Request not found");
			}
		} else {
			throw new PfirmaException("Request hash not valid");
		}
		return req;
	}
	
	/**
	 * Comprueba que existe una petición pero sin tener en cuenta las líneas de firma
	 * @param chash
	 * @return
	 * @throws PfirmaException
	 */
	public PfRequestsDTO checkRequestSimple(String chash) throws PfirmaException {
		PfRequestsDTO req = null;
		log.debug("Checking request");
		if (chash != null && !"".equals(chash)) {
			log.debug("Searching request");
			req = (PfRequestsDTO) baseDAO.queryElementOneParameter(
					"request.requestHash", "hash", chash);
			if (req != null) {
				log.debug("Request found");
			} else {
				// request not found
				log.error("Request: " + chash + " not found");
				throw new PfirmaException("Request not found");
			}
		} else {
			// request not valid
			log.error("Request not valid");
			throw new PfirmaException("Request hash not valid");
		}
		return req;
	}
	
}
