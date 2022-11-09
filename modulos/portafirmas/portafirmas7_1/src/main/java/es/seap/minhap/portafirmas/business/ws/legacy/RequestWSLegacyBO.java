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

package es.seap.minhap.portafirmas.business.ws.legacy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.configuration.FilterBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.RequestDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfActionsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfFilesDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfNoticeRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.utils.AuthenticatorConstants;
import es.seap.minhap.portafirmas.utils.CheckResult;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RequestWSLegacyBO {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(RequestWSLegacyBO.class);

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private RequestDAO requestDAO;

	@Autowired
	private CustodyServiceFactory custodyServiceFactory;

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private RequestBO requestBO;

	@Autowired
	private FilterBO filterBO;

	@Autowired
	private ApplicationVO applicationVO;

	/*
	 * WS METHODS
	 */

	/**
	 * M&eacute;todo que permite a un remitente crear una nueva petici&oacute;n en la BD.
	 * @param userRemitter Usuario remitente de la petici&oacute;n.
	 * @result C&oacute;digo hash de la nueva petici&oacute;n.
	 */
	public String insertRequestWS(String userRemitter) {
		log.info("insertRequestWS init: " + userRemitter);
		String result = null;

		// Request
		PfRequestsDTO requestDTO = new PfRequestsDTO();
		log.debug("Searching default application");
		PfApplicationsDTO applicationDTO = (PfApplicationsDTO) baseDAO.queryElementOneParameter("request.applicationPfirma", null, null);
		log.debug("Inserting default request");
		result = insertRequestWS(requestDTO, applicationDTO);

		// Remitter
		log.debug("Checking remitter user");
		PfUsersDTO remitter = checkUser(userRemitter, new CheckResult(0), false);
		// if (remitter != null) {
		// log.debug("Remitter user found, proceeding to insert it");
		// insertRemitterWS(requestDTO, remitter);
		insertRemitterWS(requestDTO, remitter, null, null, null);
		// }

		log.info("insertRequestWS end:" + result);
		return result;
	}

	/**
	 * M&eacute;todo para actualizar la informaci&oacute;n contenida en una petici&oacute;n.
	 * @param chash Identificador de petici&oacute;n.
	 * @param capplication Aplicaci&oacute;n a la que se asocia la peticion.
	 * @param cascadeSign Booleano que define la firma en paralelo o bien en cascada.
	 * @param orderedSign Booleano que define que la firma de documento a su v&eacute;z este limitada en un determinada orden (deprecado).
	 * @param emailNotify Booleano de noficaci&oacute;n del remitente v&iacute;a email.
	 * @param mobileNotify Booleano de noficaci&oacute;n del remitente v&iacute;a sms.
	 * @param adviseNotify Booleano de noficaci&oacute;n del remitente.
	 * @param fstart Fecha de inicio a partir de la cual se puede firmar el/los documentos de la petici&oacute;n.
	 * @param fexpiration Fecha m&aacute;xima de firma, a efectos &uacute;nicamente informativos.
	 * @param remitterName Nombre del remitente.
	 * @param remitterEmail Direcci&oacute;n de correo el&eacute;ctronico para el remintente.
	 * @param mobileRemitter N&uacute;mero de movil del remitente.
	 * @param dreference Referencia, por lo general, expediente al que pertenece la petici&oacute;n en la aplicaci&oacute;n cliente a t&iacute;tulo informativo.
	 * @param dsubject Asunto sobre el que versa la petici&oacute;n como t&iacute;tulo descriptivo de la petici&oacute;n.
	 * @param npriority Prioridad de la petici&oacute;n.
	 * @param user Usuario de portafirmas para vincular la petici&oacute;n a un usuario del propio portafirmas.
	 * @param parentHashRequest Identificador de la petici&oacute;n para el anidamiento de peticiones.
	 * @param trequest Texto aclarativo y ampliado como nota adjunta a la petici&oacute;n .
	 * @return 0 si todo es correcto, -1 en caso contrario.
	 */
	@Transactional(readOnly = false)
	public long updateRequestWS(String chash, String capplication,
			boolean cascadeSign, boolean orderedSign, boolean emailNotify,
			boolean mobileNotify, boolean adviseNotify, Calendar fstart,
			Calendar fexpiration, String remitterName, String remitterEmail,
			String mobileRemitter, String dreference, String dsubject,
			BigDecimal npriority, String user, String parentHashRequest,
			String trequest) {
		log.info("updateRequestWS init: " + chash + " ...");
		CheckResult result = new CheckResult(0);

		// Checking request
		PfRequestsDTO req = checkRequest(chash, true, result);
		if (req != null) {
			// Checking application
			PfApplicationsDTO app = checkApplication(capplication, result);
			if (app != null) {
				req.setPfApplication(app);
				boolean changeSignType = false;
				log.debug("Check sign type");
				if (req.getLcascadeSign().booleanValue() != cascadeSign) {
					log.debug("Change sign type");
					changeSignType = true;
				}

				req.setLcascadeSign(cascadeSign);
				req.setFstart(fstart != null ? fstart.getTime() : null);
				req.setFexpiration(fexpiration != null ? fexpiration.getTime()
						: null);
				if (dreference != null && !"".equals(dreference)) {
					req.setDreference(dreference);
				}
				if (dsubject != null && !"".equals(dsubject)) {
					req.setDsubject(dsubject);
				}
				log.debug("Updating request");
				baseDAO.update(req);

				// Request text
				log.debug("Checking request text");
				if (trequest != null && !"".equals(trequest)) {
					log.debug("Searching request text");
					PfRequestsTextDTO reqText = (PfRequestsTextDTO) baseDAO
							.queryElementOneParameter(
									"request.requestTextHash", "hash", chash);
					log.debug("Updating request text");
					reqText.setTrequest(trequest);
					baseDAO.update(reqText);
				}

				// Remitter
				log.debug("Checking remitter user");
				PfUsersDTO userDTO = checkUser(user, result, false);
				// if (userDTO != null) {
				insertRemitterWS(req, userDTO, remitterName, remitterEmail,
						mobileRemitter);
				// }

				log.debug("Changing sign type");
				if (changeSignType) {
					changeSignLinesType(req);
				}
			}
		}
		log.info("updateRequestWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que elimina un firmante de una petici&oacute;n.
	 * @param cdni Dni del firmante.
	 * @param chash hash de la petici&oacute;n.
	 * @return 0 si hay todo bien, -1 si ha fallado algo.
	 */
	public long deleteRequestSignerWS(String cdni, String chash) {
		log.info("deleteRequestSignerWS init: " + cdni + ", " + chash);
		CheckResult result = new CheckResult(0);
		// Checking request
		PfRequestsDTO reqDTO = checkRequest(chash, false, result);
		if (reqDTO != null) {
			// Checking exists signer
			PfSignLinesDTO signLine = checkSigner(cdni, chash);
			if (signLine != null) {
				log.debug("Deleting signer");
				String requestState = checkStateRequest(cdni, chash);
				if (Constants.C_TAG_SIGNED.equals(requestState)
						|| Constants.C_TAG_REJECTED.equals(requestState)) {
					log.error("Request signed or rejected by the user, operation not valid");
					result.setValue(-1);
				} else {
					log.debug("Deleting signer");
					deleteSignerWS(reqDTO, signLine, requestState);
				}
			} else {
				log.error("Operation not valid because signer not exists");
				result.setValue(-1);
			}
		}
		log.info("deleteRequestSignerWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que permite insertar un nuevo documento enuna petici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @param cdocumentType Tipo de documento.
	 * @param fileName Nombre del fichero.
	 * @param mimeFile Tipo MIME del fichero.
	 * @param bfile Bytes del fichero.
	 * @return Hash del documento.
	 * @throws CustodyServiceException
	 * @throws PfirmaException
	 */
	public String insertRequestDocumentWS(String chash, String cdocumentType,
			String fileName, String mimeFile, byte[] bfile)
			throws CustodyServiceException, PfirmaException {
		log.info("insertRequestDocumentWS init: " + chash + "...");
		String result = "";

		// Checking request
		PfRequestsDTO req = checkRequest(chash, true, new CheckResult(0));
		if (req != null) {
			// Checking document
			if (fileName != null && !"".equals(fileName) && bfile != null
					&& bfile.length > 0) {
				// Checking documentType
				PfDocumentTypesDTO documentType = checkDocumentType(cdocumentType);
				if (documentType != null) {
					PfDocumentsDTO requestDocument = createDocumentWS(fileName, mimeFile, documentType);
					log.debug("Inserting document");
					result = insertDocumentsWS(req, requestDocument, bfile);
				}
			} else {
				log.error("File name or binary content not valid");
			}
		}
		log.info("insertRequestDocumentWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que comprueba si una petici&oacute;n ha sido enviada.
	 * @param chash hash de la petici&oacute;n.
	 * @return "True" si ha sido enviada, "false" en caso contrario.
	 */
	public boolean isSendRequestWS(String chash) {
		log.info("isSendRequestWS init: " + chash);
		boolean result = false;

		// Checking request
		result = checkSendRequest(chash);

		log.info("isSendRequestWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que env&iacute;a una petici&oacute;n.
	 * @param chash hash de la petici&oacute;n.
	 * @return 0 si ha ido todo bien, "false" en caso contrario.
	 */
	public long sendRequestWS(String chash) {
		log.info("sendRequestWS init: " + chash);
		CheckResult result = new CheckResult(0);

		// Checking request
		PfRequestsDTO req = checkRequest(chash, true, result);
		if (req != null) {
			if (!checkSendRequest(chash)) {
				// Checking documents and sign lines
				log.debug("Checking documents");
				List<AbstractBaseDTO> documentList = baseDAO
						.queryListOneParameter("request.requestHashDocuments", "hash", chash);
				if (documentList != null && !documentList.isEmpty()) {
					log.debug("Documents found");
					log.debug("Checking sign lines");
					if (req.getPfSignsLines() != null
							&& !req.getPfSignsLines().isEmpty()) {
						log.debug("Sign lines found");
						log.debug("Sending request to signers");
						insertTagWS(req);
						// Execute filters
						filterBO.applyFilterOneRequest(req);

						// Execute notice
						noticeBO.noticeNewRequest(req, applicationVO.getEmail(), applicationVO.getSMS());
					} else {
						log.error("Sign lines not found");
						result.setValue(-1);
					}
				} else {
					log.error("Documents not found");
					result.setValue(-1);
				}
			}
		}

		log.info("sendRequestWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que comprueba si existe una petici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @return Petici&oacute;n.
	 */
	public PfRequestsDTO queryRequestWS(String chash) {
		log.info("queryRequestWS init: " + chash);
		PfRequestsDTO result = null;

		// Checking request and query
		result = checkRequest(chash, false, new CheckResult(0));

		log.info("queryRequestWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que devuelve el texto de una petici&oacute;n.
	 * @param request Petici&oacute;n.
	 * @return Texto de la petici&oacute;n.
	 */
	public PfRequestsTextDTO queryRequestTextWS(PfRequestsDTO request) {
		log.info("queryRequestTextWS init: " + request);
		PfRequestsTextDTO result = null;

		if (request != null) {
			// Query text request
			log.debug("Querying text request");
			result = requestDAO.queryRequestText(request);
		}

		log.info("queryRequestTextWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que devuelve los firmantes de una petici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @return Listado de firmantes.
	 */
	public List<AbstractBaseDTO> querySignersWS(String chash) {
		log.info("querySignersWS init: " + chash);
		List<AbstractBaseDTO> result = null;

		// Checking request
		PfRequestsDTO req = checkRequest(chash, false, new CheckResult(0));
		if (req != null) {
			result = baseDAO.queryListOneParameter("request.requestHashUsers", "hash", chash);
		}

		log.info("querySignersWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que recupera un usuario de la BD.
	 * @param cdni Dni del usuario.
	 * @return Usuario.
	 */
	public PfUsersDTO queryUserWS(String cdni) {
		log.info("queryUserWS init: " + cdni);
		PfUsersDTO result = null;

		// Checking user
		result = checkUser(cdni, new CheckResult(0), true);

		log.info("queryUserWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que recupera los documentos de una petici&oacute;n.
	 * @param chash hash de la petici&oacute;n.
	 * @return Listado de pares (Documento, Tama&ntilde;o).
	 * @throws CustodyServiceException
	 */
	public Object[] queryDocumentsWS(String chash)
			throws CustodyServiceException {
		log.info("queryDocumentsWS init: " + chash);
		Object[] result = null;
		List<AbstractBaseDTO> resultDoc = null;
		List<BigDecimal> resultSize = null;

		// Checking request
		PfRequestsDTO req = checkRequest(chash, false, new CheckResult(0));
		if (req != null) {
			log.debug("Querying documents");
			resultDoc = baseDAO.queryListOneParameter(
					"request.requestHashDocuments", "hash", chash);
			if (resultDoc != null && !resultDoc.isEmpty()) {
				resultSize = new ArrayList<BigDecimal>();
				CustodyServiceOutput custodyService = null;
				CustodyServiceOutputDocument custodyDocument = null;
				BigDecimal size = null;
				for (AbstractBaseDTO docDTO : resultDoc) {
					custodyService = custodyServiceFactory
							.createCustodyServiceOutput(((PfDocumentsDTO) (docDTO))
									.getPfFile().getCtype());
					custodyDocument = new CustodyServiceOutputDocument();
					custodyDocument.setIdentifier(((PfDocumentsDTO) docDTO).getChash());
					custodyDocument.setUri(((PfDocumentsDTO) docDTO).getPfFile().getCuri());
					custodyDocument.setIdEni(((PfDocumentsDTO) docDTO).getPfFile().getIdEni());
					custodyDocument.setRefNasDir3(((PfDocumentsDTO) docDTO).getPfFile().getRefNasDir3());
					size = custodyService.fileSize(custodyDocument);
					if (size == null) {
						log.error("Error calculating size:"
								+ ((PfDocumentsDTO) docDTO).getChash());
					} else {
						resultSize.add(size);
					}
				}
				if (resultDoc.size() != resultSize.size()) {
					log.error("Error in sizes");
				} else {
					result = new Object[] { resultDoc, resultSize };
					log.debug("Documents found");
				}
			} else {
				log.debug("Documents not found");
			}
		}

		log.info("queryDocumentsWS end: " + Arrays.toString(result));
		return result;
	}

	/**
	 * M&eacute;todo que permite descargar el contenido de un documento en bytes.
	 * @param chash hash del documento.
	 * @return Bytes del documento.
	 * @throws CustodyServiceException
	 */
	public byte[] downloadDocumentWS(String chash)
			throws CustodyServiceException {
		log.info("downloadDocumentWS init: " + chash);
		byte[] result = null;

		// Checking document
		PfDocumentsDTO doc = checkDocument(chash, new CheckResult(0));
		if (doc != null) {
			log.debug("Creating file service");
			CustodyServiceOutput custodyService = custodyServiceFactory
					.createCustodyServiceOutput(doc.getPfFile().getCtype());
			log.debug("Downloading document");
			CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
			custodyDocument.setIdentifier(doc.getChash());
			custodyDocument.setUri(doc.getPfFile().getCuri());
			custodyDocument.setIdEni(doc.getPfFile().getIdEni());
			custodyDocument.setRefNasDir3(doc.getPfFile().getRefNasDir3());
			result = custodyService.downloadFile(custodyDocument);
			
			if (result != null) {
				log.debug("Download file succesfully");
			} else {
				log.error("Download file error");
			}
		}

		log.info("downloadDocumentWS end: "
				+ (result == null ? "NULO" : "NO NULO"));
		return result;
	}

	/**
	 * M&eacute;todo que permite borrar un documento.
	 * @param chash hash del documento.
	 * @return 0 si ha ido bien, -1 si ha ido mal.
	 */
	@Transactional(readOnly = false)
	public long deleteDocumentWS(String chash) {
		log.info("deleteDocumentWS init: " + chash);
		CheckResult result = new CheckResult(0);

		// Checking document
		PfDocumentsDTO doc = checkDocument(chash, result);
		if (doc != null) {
			// Checking request
			PfRequestsDTO req = checkRequest(doc.getPfRequest().getChash(),	true, result);
			if (req != null) {
				log.debug("Deleting document");
				baseDAO.delete(doc);

				// Change udpate date
				req.setFmodified(Calendar.getInstance().getTime());
				baseDAO.update(req);

			}
		}

		log.info("deleteDocumentWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que recupera las notificaciones de una petici&oacute;n.
	 * @param chash hash de la petici&oacute;n.
	 * @return Listado de notificaciones de la petici&oacute;n.
	 */
	public List<AbstractBaseDTO> queryNoticesRequestWS(String chash) {
		log.info("queryNoticesRequestWS init: " + chash);
		List<AbstractBaseDTO> result = null;

		PfRequestsDTO req = checkRequest(chash, false, new CheckResult(0));
		if (req != null) {
			log.debug("Querying notices");
			result = baseDAO.queryListOneParameter(
					"request.requestNoticesHash", "hash", chash);
			if (result != null) {
				log.debug("Notices found");
			} else {
				log.debug("Notices not found");
			}
		}

		log.info("queryNoticesRequestWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que recupera las etiquetas de una eptici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @return Petici&oacute;n.
	 */
	public PfRequestsDTO queryStatesRequestWS(String chash) {
		log.info("queryStatesRequestWS init: " + chash);
		PfRequestsDTO result = null;

		PfRequestsDTO req = checkRequest(chash, false, new CheckResult(0));
		if (req != null) {
			log.debug("Querying states");
			result = (PfRequestsDTO) baseDAO.queryElementOneParameter(
					"request.statesRequestHash", "hash", chash);
			if (result != null) {
				log.debug("States found");
			} else {
				log.debug("States not found");
			}
		}

		log.info("queryStatesRequestWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que recupera todas las etiquetas de estado.
	 * @return Listado de etiquetas de estado.
	 */
	public List<AbstractBaseDTO> queryStatesListWS() {
		log.info("queryStatesListWS init");
		List<AbstractBaseDTO> result = null;

		result = baseDAO.queryListOneParameter("request.statesAll", null, null);

		log.info("queryStatesListWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que permite descargar la firma de una petici&oacute;n.
	 * @param chash hash dela petici&oacute;n.
	 * @param transactionId Identificador de transacci&oacute;n.
	 * @return Bytes de la firma.
	 * @throws CustodyServiceException
	 */
	public byte[] downloadSignWS(String chash, String transactionId)
			throws CustodyServiceException {
		log.info("downloadSignWS init: " + chash + ", " + transactionId);
		byte[] result = null;

		// Checking document
		PfDocumentsDTO doc = checkDocument(chash, new CheckResult(0));
		if (doc != null) {
			log.debug("Creating sign service");
			PfSignsDTO fileSign = custodyServiceFactory.signFileQuery(chash,
					transactionId);
			CustodyServiceOutputSign custodySign = new CustodyServiceOutputSign();
			String storageType = fileSign.getCtype();
			custodySign.setType(Constants.SIGN_TYPE_SERVER);
			custodySign.setIdentifier(fileSign.getPrimaryKeyString());
			custodySign.setUri(fileSign.getCuri());
			custodySign.setIdEni(fileSign.getRefNASIdEniFirma());
			custodySign.setRefNasDir3(fileSign.getRefNASDir3Firma());

			CustodyServiceOutput signService = custodyServiceFactory
					.createCustodyServiceOutput(storageType);
			log.debug("Downloading sign");
			result = signService.downloadSign(custodySign);
			
			if (result != null) {
				log.debug("Download sign succesfully");
			} else {
				log.error("Download sign error");
			}
		}

		log
				.info("downloadSignWS end: "
						+ (result == null ? "NULO" : "NO NULO"));
		return result;
	}

	/**
	 * M&eacute;todo que permite insertar una acci&oacute;n a una petici&oacute;n.
	 * @param chash hash de la petici&oacute;n.
	 * @param state Estado de la petici&oacute;n.
	 * @param action Acci&oacute;n.
	 * @param type Tipo de la acci&oacute;n.
	 * @return 0 si ha ido todo bien, -1 en caso contrario.
	 */
	@Transactional(readOnly = false)
	public long insertActionRequestWS(String chash, String state,
			String action, String type) {
		log.info("insertActionRequestWS init: " + chash + " ...");
		CheckResult result = new CheckResult(0);

		log.debug("Checking action");
		if (action != null && !"".equals(action)) {
			// Checking document
			PfDocumentsDTO docDTO = checkDocument(chash, result);
			if (docDTO != null) {
				// Checking state
				PfTagsDTO tagDTO = checkState(state, result);
				if (tagDTO != null) {
					log.debug("Inserting action");
					PfActionsDTO actionDTO = new PfActionsDTO();
					actionDTO.setCtype(type != null && !"".equals(type) ? type : "WEB");
					actionDTO.setPfDocument(docDTO);
					actionDTO.setPfTag(tagDTO);
					actionDTO.setTaction(action);
					baseDAO.insertOrUpdate(actionDTO);

					// Change udpate date
					docDTO.getPfRequest().setFmodified(
							Calendar.getInstance().getTime());
					baseDAO.update(docDTO.getPfRequest());

				}
			}
		} else {
			log.error("Action not valid");
			result.setValue(-1);
		}

		log.info("insertActionRequestWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que permite insertar una notificaci&oacute;n.
	 * @param chash hash de la petici&oacute;n.
	 * @param notice Notificaci&oacute;n a insertar.
	 * @return 0 si ha ido todo bien, -1 en caso contrario.
	 */
	@Transactional(readOnly = false)
	public long insertNoticeWS(String chash, String notice) {
		log.info("insertNoticeWS init: " + chash + ", " + notice);
		CheckResult result = new CheckResult(0);

		PfRequestsDTO req = checkRequest(chash, true, result);
		if (req != null) {
			if (checkNotice(chash, notice, result) == null) {
				log.debug("Querying notice");
				PfTagsDTO tag = applicationVO.getStateTags().get(notice);
				if (tag != null) {
					result.setValue(0);
					log.debug("Inserting notice");
					PfNoticeRequestsDTO noticeRequestDTO = new PfNoticeRequestsDTO();
					noticeRequestDTO.setPfRequest(req);
					noticeRequestDTO.setPfTag(tag);
					baseDAO.insertOrUpdate(noticeRequestDTO);

					// Change udpate date
					req.setFmodified(Calendar.getInstance().getTime());
					baseDAO.update(req);
				} else {
					log.error("Tag not found");
					result.setValue(-1);
				}
			} else {
				log.error("Notice exists");
				result.setValue(-1);
			}
		} else {
			log.error("Notice not valid");
			result.setValue(-1);
		}

		log.info("insertNoticeWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que elimina una notificaci&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @param notice Notificaci&oacute;n.
	 * @return 0 si ha ido todo bien, -1 en caso contrario.
	 */
	@Transactional(readOnly = false)
	public long deleteNoticeWS(String chash, String notice) {
		log.info("deleteNoticeWS init: " + chash + ", " + notice);
		CheckResult result = new CheckResult(0);

		PfRequestsDTO req = checkRequest(chash, true, result);
		if (req != null) {
			PfNoticeRequestsDTO noticeReq = checkNotice(chash, notice, result);
			if (noticeReq != null) {
				log.debug("Deleting notice");
				baseDAO.delete(noticeReq);

				// Change udpate date
				req.setFmodified(Calendar.getInstance().getTime());
				baseDAO.update(req);
			}
		} else {
			log.error("Notice not valid");
			result.setValue(-1);
		}

		log.info("deleteNoticeWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que cambia un firmante de una petici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @param sourceDni DNI del usuario a cambiar.
	 * @param targetDni DNI del nuevo usuario.
	 * @param name Nombre del nuevo usuario.
	 * @param surname1 Primer apellido del nuevo usuario.
	 * @param surname2 Segundo apellido del nuevo usuario.
	 * @param newUser Indica si el nuevo usuario ya existe o no.
	 * @return 0 si ha ido todo bien, -1 en caso contrario.
	 */
	@Transactional(readOnly = false)
	public long changeSignerRequestWS(String chash, String sourceDni,
			String targetDni, String name, String surname1, String surname2,
			String newUser) {
		log.info("changeSignerRequestWS init: " + chash + " ...");
		CheckResult result = new CheckResult(0);

		log.debug("Checking signers");
		if (sourceDni != null && !"".equals(sourceDni) && targetDni != null
				&& !"".equals(targetDni)) {
			PfRequestsDTO req = checkRequest(chash, false, result);
			if (req != null) {
				log.debug("Checking source signer");
				PfSignLinesDTO sourceSignLine = checkSigner(sourceDni, chash);
				if (sourceSignLine != null) {
					log.debug("Source signer found");
					String requestState = checkStateRequest(sourceDni, chash);
					if (Constants.C_TAG_SIGNED.equals(requestState)
							|| Constants.C_TAG_REJECTED.equals(requestState)) {
						log.error("Request signed or rejected by the user, operation not valid");
						result.setValue(-1);
					} else {
						if (checkSigner(targetDni, chash) == null) {
							PfUsersDTO targetUser = checkUser(targetDni, result, false);
							if (targetUser == null && newUser != null
									&& Constants.C_YES.equals(newUser)) {
								log.debug("Inserting target signer");
								targetUser = insertUser(targetDni, name, surname1, surname2);
							}
							if (targetUser != null) {
								log.debug("Changing signers");
								changeSignerWS(req, sourceSignLine, targetUser, requestState);
							} else {
								log.error("Target signer not found or not insert");
								result.setValue(-1);
							}
						} else {
							log.error("Target signer found in this request");
							result.setValue(-1);
						}
					}

				} else {
					log.error("Source signer not found");
					result.setValue(-1);
				}
			}
		} else {
			log.error("Source or target not valid");
			result.setValue(-1);
		}

		log.info("changeSignerRequestWS end: " + result);
		return result.getValue();
	}

	/**
	 * M&eacute;todo que recupera los comentarios de un usuario para una petici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @param dni Dni del usuario.
	 * @return Comentario.
	 */
	public String queryObservationRequestWS(String chash, String dni) {
		log.info("queryObservationRequestWS init: " + chash + ", " + dni);
		String result = "";

		log.debug("Checking dni");
		if (dni != null && !"".equals(dni)) {
			PfDocumentsDTO doc = checkDocument(chash, new CheckResult(0));
			if (doc != null) {
				log.debug("Querying observation");
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("hash", chash);
				parameters.put("dni", dni.toUpperCase());
				PfCommentsDTO comm = (PfCommentsDTO) baseDAO
						.queryElementMoreParameters("request.textRejectHash",
								parameters);
				if (comm != null) {
					log.debug("Observation found");
					result = comm.getTcomment();
				} else {
					log.error("Observation not found");
				}
			}
		} else {
			log.error("Dni not valid");
		}

		log.info("queryObservationRequestWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que inserta un firmante en una petici&oacute;n.
	 * @param cdni Dni del firmante.
	 * @param name Nombre del firmante.
	 * @param surname1 Primer apellido del firmante.
	 * @param surname2 Segundo apellido del firmante.
	 * @param chash Hash de la petici&oacute;n.
	 * @param newUser Define si el usuario es nuevo o no.
	 * @return 0 si ha ido bien, -1 en caso contrario.
	 */
	@Transactional(readOnly = false)
	public long insertRequestSignerWS(String cdni, String name,
			String surname1, String surname2, String chash, boolean newUser) {

		log.info("insertRequestSignerWS init: " + cdni + ", " + chash);
		CheckResult result = new CheckResult(0);
		// Checking request
		PfRequestsDTO reqDTO = checkRequest(chash, false, result);
		if (reqDTO != null) {
			// Checking exists signer
			if (checkSigner(cdni, chash) != null) {
				log.error("Operation not valid because signer exists");
				result.setValue(-1);
			} else {
				PfUsersDTO user = checkUser(cdni, result, name != null ? false : true);
				if (user == null && !"".equals(cdni) && newUser) {
					log.debug("Inserting new user");
					user = insertUser(cdni, name, surname1, surname2);
				}
				if (user != null) {
					log.debug("Inserting signer");
					if (checkSendRequest(chash)) {
						log.debug("Request sended, inserting state tag");
					}
					log.debug("Search sign line previous");
					PfSignLinesDTO signLinePrevious = findSignLinePrevious(chash);
					String userPrevious = null;
					if (signLinePrevious != null) {
						log.debug("Sign line previous found");
						userPrevious = signLinePrevious.getPfSigners()
								.iterator().next().getPfUser().getCidentifier();
					} else {
						log.debug("Sign line previous not found");
					}
					String requestState = checkStateRequest(userPrevious, chash);
					insertSignerWS(reqDTO, user, requestState, signLinePrevious);
				}
			}
		}

		log.info("insertRequestSignerWS end: " + result);
		return result.getValue();

	}

	/**
	 * M&eacute;todo que permite borrar una petici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @return 0 si ha ido todo bien, -1 en caso contrario.
	 */
	@Transactional(readOnly = false)
	public long deleteRequestWS(String chash) {
		log.info("deleteRequestWS init: " + chash);
		CheckResult result = new CheckResult(0);

		PfRequestsDTO req = checkRequest(chash, true, result);
		if (req != null) {
			log.debug("Deleting request");
			baseDAO.delete(req);
		}

		log.info("deleteRequestWS end: " + result);
		return result.getValue();
	}

	/**
	 * Recupera todos los usuarios.
	 * @return Listado de usuarios.
	 */
	public List<AbstractBaseDTO> queryUsersAllWS() {
		log.info("queryUsersAllWS init");
		List<AbstractBaseDTO> result = null;

		result = baseDAO.queryListOneParameter("request.userAll", null, null);

		log.info("queryUsersAllWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que comprueba si un documento est&aacute; firmado.
	 * @param chash Hash del documento.
	 * @return Identificador de transacci&oacute;n de la firma.
	 */
	public double queryDocumetSignWS(String chash) {
		log.info("queryDocumetSignWS init: " + chash);
		double result = 0.0;

		PfDocumentsDTO docDTO = checkDocument(chash, new CheckResult(0));
		if (docDTO != null) {
			log.debug("Querying sign");
			List<AbstractBaseDTO> signList = baseDAO.queryListOneParameter(
					"request.signDocumentHash", "hash", chash);
			if (signList != null && !signList.isEmpty()) {
				log.debug("Sign found");
				result = Double.parseDouble(((PfSignsDTO) signList.get(0))
						.getCtransaction());
			} else {
				log.error("Sign found");
				result = -1;
			}
		} else {
			result = -1;
		}

		log.info("queryDocumetSignWS end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que recupera el formato de firma de una petici&oacute;n.
	 * @param request Hash de la petici&oacute;n.
	 * @return Formato de firma.
	 */
	public String queryRequestSignFormat(String request) {
		log.info("queryRequestSignFormat init: " + request);
		String result = null;
		CheckResult resultCheckRequest = new CheckResult(0);

		// Se busca la petici&oacute;n
		PfRequestsDTO peticion = checkRequest(request, false, resultCheckRequest);

		// Se busca la aplicaci&oacute;n asociada
		PfApplicationsDTO aplicacion = checkApplication(peticion.getPfApplication().getPrimaryKeyString(), resultCheckRequest);

		// Se obtiene la configuraci&oacute;n
		PfConfigurationsDTO configuracion = aplicacion.getPfConfiguration();

		log.debug("Querying sign format");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idConf", configuracion.getPrimaryKey());
		parameters.put("tvalor", AuthenticatorConstants.PARAM_SIGNATURE_FORMAT);
		PfConfigurationsParameterDTO paramConfig = 
				(PfConfigurationsParameterDTO) baseDAO.queryElementMoreParameters("request.paramValue", parameters);
		if (paramConfig != null) {
			log.debug("Sign format found");
			result = paramConfig.getTvalue();
		} else {
			log.error("Sign format found");
		}

		log.info("queryRequestSignFormat end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo que obtiene el remitente del hist&oacute;rico de una petici&oacute;n.
	 * @param req Petici&oacute;n.
	 * @param chistory C&oacute;digo de hist&oacute;rico.
	 * @return
	 */
	public String queryRemitter(PfRequestsDTO req, String chistory) {
		String result = null;
		if (Constants.C_HISTORICREQUEST_REMITTERNAME.equals(chistory)) {
			result = "DESCONOCIDO";
		}
		PfHistoricRequestsDTO historic = queryHistory(req, chistory);

		if (historic != null) {
			result = historic.getThistoricRequest();
		}

		return result;
	}

	/*
	 * AUXILIARY METHODS
	 */

	/**
	 * M&eacute;todo que a&ntilde;ade o actualiza un hist&oacute;rico a una petici&oacute;n.
	 * @param requestDTo Petici&oacute;n.
	 * @param chistory C&oacute;digo de hist&oacute;rico.
	 * @param dhistory Texto del hist&oacute;rico.
	 */
	@Transactional(readOnly = false)
	private void insertOrUpdateHistoryWS(PfRequestsDTO requestDTO,
			String chistory, String dhistory) {
		PfHistoricRequestsDTO historicRequestDTO = null;
		historicRequestDTO = queryHistory(requestDTO, chistory);
		if (historicRequestDTO == null) {
			historicRequestDTO = new PfHistoricRequestsDTO();
			historicRequestDTO.setPfRequest(requestDTO);
		}
		historicRequestDTO.setChistoricRequest(chistory);
		historicRequestDTO.setThistoricRequest(dhistory);
		baseDAO.insertOrUpdate(historicRequestDTO);
	}

	/**
	 * M&eacute;todo que elimina un hist&oacute;rico de petici&oacute;n.
	 * @param requestDTO Petici&oacute;n.
	 * @param chistory C&oacute;digo de hist&oacute;rico.
	 */
	@Transactional(readOnly = false)
	private void deleteHistoryWS(PfRequestsDTO requestDTO, String chistory) {
		PfHistoricRequestsDTO historicRequestDTO = null;
		historicRequestDTO = queryHistory(requestDTO, chistory);
		if (historicRequestDTO != null) {
			baseDAO.delete(historicRequestDTO);
		}
	}

	/**
	 * M&ntilde;etodo que recupera el hist&oacute;rico de una petici&oacute;n.
	 * @param requestDTO Petici&oacute;n.
	 * @param chistory C&oacute;digo del hist&oacute;rico.
	 * @return Hst&oacute;rico.
	 */
	private PfHistoricRequestsDTO queryHistory(PfRequestsDTO requestDTO,
			String chistory) {
		PfHistoricRequestsDTO historicRequestDTO;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("chist", chistory);
		parameters.put("req", requestDTO);

		historicRequestDTO = (PfHistoricRequestsDTO) baseDAO
				.queryElementMoreParameters("request.requestHistoryID",
						parameters);
		return historicRequestDTO;
	}

	/**
	 * M&eacute;todo que asocia un documento a una petici&oacute;n.
	 * @param requestDTO Petici&oacute;n.
	 * @param requestDocument Documento.
	 * @param requestFile Fichero.
	 * @return Hash del documento.
	 * @throws CustodyServiceException
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false)
	private String insertDocumentsWS(PfRequestsDTO requestDTO,
			PfDocumentsDTO requestDocument, byte[] requestFile)
			throws CustodyServiceException, PfirmaException {
		try {
			log.debug("Uploading file");
			InputStream inputStore = new ByteArrayInputStream(requestFile);
			InputStream inputCheck = new ByteArrayInputStream(requestFile);
			requestDocument.setPfRequest(requestDTO);
			requestDocument.setChash(Util.getInstance().createHash());
			PfFilesDTO fileDTO = requestBO.insertFile(requestDocument,
					inputStore, inputCheck, requestFile.length);
			requestDocument.setPfFile(fileDTO);
			log.debug("Inserting document");
			baseDAO.insertOrUpdate(requestDocument);

			// Change udpate date
			requestDTO.setFmodified(Calendar.getInstance().getTime());
			baseDAO.update(requestDTO);

			return requestDocument.getChash();
		} catch (IOException e) {
			throw new PfirmaException("Error uploading file");
		}
	}

	/**
	 * M&eacute;todo que permite insertar un nuevo remitente en una petici&oacute;n.
	 * @param req Petici&oacute;n.
	 * @param user Usuario remitente.
	 * @param remitterName Nombre del remitente.
	 * @param remitterEmail Email del remitente.
	 * @param remitterMobile M&oacute;vil del remitente.
	 */
	@Transactional(readOnly = false)
	private void insertRemitterWS(PfRequestsDTO req, PfUsersDTO user,
			String remitterName, String remitterEmail, String remitterMobile) {
		PfUsersRemitterDTO remitterDTO = null;
		if (req.getPfUsersRemitters() != null
				&& !req.getPfUsersRemitters().isEmpty()) {
			remitterDTO = req.getPfUsersRemitters().iterator().next();
		}

		if (remitterDTO == null && user != null) {
			remitterDTO = new PfUsersRemitterDTO();
			remitterDTO.setPfUser(user);
			remitterDTO.setPfRequest(req);
		}
		if (remitterDTO != null) {
			remitterDTO.setLnotifyEmail(false);
			remitterDTO.setLnotifyMobile(false);
			baseDAO.insertOrUpdate(remitterDTO);
		}
		insertOrUpdateHistoryWS(req, Constants.C_HISTORICREQUEST_REMITTERNAME,
				remitterName != null ? remitterName
						: Constants.D_HISTORICREQUEST_UNKNOW);

		if (remitterEmail != null && !"".equals(remitterEmail)) {
			insertOrUpdateHistoryWS(req,
					Constants.C_HISTORICREQUEST_REMITTEREMAIL, remitterEmail);
		} else {
			deleteHistoryWS(req, Constants.C_HISTORICREQUEST_REMITTEREMAIL);
		}
		if (remitterMobile != null && !"".equals(remitterMobile)) {
			insertOrUpdateHistoryWS(req,
					Constants.C_HISTORICREQUEST_REMITTERMOBILE, remitterMobile);
		} else {
			deleteHistoryWS(req, Constants.C_HISTORICREQUEST_REMITTERMOBILE);
		}
	}

	/**
	 * M&eacute;todo que inserta un firmante en una petici&oacute;n.
	 * @param requestDTO Petici&oacute;n.
	 * @param userDTO Usuario firmante.
	 * @param requestState Estado de la petici&oacute;n.
	 * @param signLineCascade L&iacute;nea de firma en cascada.
	 */
	@Transactional(readOnly = false)
	private void insertSignerWS(PfRequestsDTO requestDTO, PfUsersDTO userDTO,
			String requestState, PfSignLinesDTO signLineCascade) {
		// Signers and signLines
		PfSignLinesDTO signLineDTO = new PfSignLinesDTO();
		signLineDTO.setCtype(Constants.C_TYPE_SIGNLINE_SIGN);
		signLineDTO.setPfRequest(requestDTO);
		if (signLineCascade != null && requestDTO.getLcascadeSign()) {
			signLineDTO.setPfSignLine(signLineCascade);
		}
		baseDAO.insertOrUpdate(signLineDTO);

		PfSignersDTO signerDTO = new PfSignersDTO();
		signerDTO.setPfSignLine(signLineDTO);
		signerDTO.setPfUser(userDTO);
		baseDAO.insertOrUpdate(signerDTO);

		// Request sended
		if (requestState != null) {
			log.debug("Inserting tags");
			PfRequestTagsDTO rTagDTO = new PfRequestTagsDTO();
			rTagDTO.setPfRequest(requestDTO);
			rTagDTO.setPfUser(userDTO);
			PfTagsDTO tagDTO = null;

			// Cascade sign
			if (requestDTO.getLcascadeSign()) {
				log.debug("Cascade sign: " + requestState);
				if (Constants.C_TAG_SIGNED.equals(requestState)) {
					tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
				} else if (Constants.C_TAG_REJECTED.equals(requestState)) {
					tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_REJECTED);
				} else {
					tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING);
				}
			}
			// Not cascade sign
			else {
				log.debug("Not cascade sign: " + requestState);
				if (Constants.C_TAG_REJECTED.equals(requestState)) {
					tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_REJECTED);
				} else {
					tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
				}
			}

			rTagDTO.setPfTag(tagDTO);
			baseDAO.insertOrUpdate(rTagDTO);

			// Change udpate date
			requestDTO.setFmodified(Calendar.getInstance().getTime());
			baseDAO.update(requestDTO);
		}
	}

	/**
	 * M&eacute;todo que cambia un firmante de una petici&oacute;n.
	 * @param requestDTO Petici&oacute;n.
	 * @param sourceSignLine L&iacute;nea de firma a cambiar.
	 * @param targetUser Usuario firmante nuevo.
	 * @param requestState Estado a actualizar.
	 */
	@Transactional(readOnly = false)
	private void changeSignerWS(PfRequestsDTO requestDTO,
			PfSignLinesDTO sourceSignLine, PfUsersDTO targetUser,
			String requestState) {
		PfSignersDTO signer = sourceSignLine.getPfSigners().iterator().next();
		PfUsersDTO sourceUser = signer.getPfUser();
		signer.setPfUser(targetUser);
		baseDAO.update(signer);

		// state
		if (requestState != null) {
			for (PfRequestTagsDTO rTag : requestDTO.getPfRequestsTags()) {
				if (rTag.getPfUser() != null
						&& rTag.getPfUser().getPrimaryKey().equals(sourceUser.getPrimaryKey())) {
					requestDTO.getPfRequestsTags().remove(rTag);
					baseDAO.delete(rTag);
				}
			}
			PfRequestTagsDTO requestTag = new PfRequestTagsDTO();
			requestTag.setPfRequest(requestDTO);
			PfTagsDTO tagDTO = null;
			if (Constants.C_TAG_AWAITING.equals(requestState)) {
				tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING);
			} else {
				tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
			}
			requestTag.setPfTag(tagDTO);
			requestTag.setPfUser(targetUser);
			baseDAO.update(requestTag);
		}

		// Change udpate date
		requestDTO.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(requestDTO);

	}

	/**
	 * M&eacute;todo que elimina un firmante de una petici&oacute;n.
	 * @param requestDTO Petici&oacute;n.
	 * @param signLine L&iacute;nea de firma a borrar.
	 * @param requestState Estado de la petici&oacute;n antes del borrado.
	 */
	@Transactional(readOnly = false)
	private void deleteSignerWS(PfRequestsDTO requestDTO, PfSignLinesDTO signLine, String requestState) {
		log.debug("Recolocate signers");
		PfSignLinesDTO signLineNext = null;
		if (signLine.getPfSignLines() != null && !signLine.getPfSignLines().isEmpty()) {
			signLineNext = signLine.getPfSignLines().iterator().next();
			signLineNext.setPfSignLine(signLine.getPfSignLine());
			baseDAO.update(signLineNext);
		}

		PfSignersDTO signer = (PfSignersDTO) signLine.getPfSigners().iterator().next();
		PfUsersDTO user = signer.getPfUser();
		baseDAO.delete(signer);
		baseDAO.delete(signLine);

		// Change udpate date
		requestDTO.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(requestDTO);

		// state tag
		if (requestState != null) {
			PfUsersDTO userNext = null;
			if (signLineNext != null) {
				userNext = signLineNext.getPfSigners().iterator().next().getPfUser();
			}
			changeTagsUserWS(requestDTO.getPfRequestsTags(), user, userNext, requestState);
		}
	}

	/**
	 * M&eacute;todo que cambio el estado de todas las peticiones de un usuario.
	 * @param list Listado de peticiones.
	 * @param user Usuario.
	 * @param userNext Siguiente usuario definido en la l&iacute;nea de firma de la petici&oacute;n.
	 * @param requestState Estado actual de la petici&oacute;n.
	 */
	@Transactional(readOnly = false)
	private void changeTagsUserWS(Set<PfRequestTagsDTO> list, PfUsersDTO user,
			PfUsersDTO userNext, String requestState) {
		for (PfRequestTagsDTO rTag : list) {
			if (rTag.getPfUser() != null
					&& rTag.getPfUser().getPrimaryKey().equals(user.getPrimaryKey())) {
				baseDAO.delete(rTag);
			}
			if (rTag.getPfUser() != null
					&& userNext != null
					&& rTag.getPfUser().getPrimaryKey().equals(userNext.getPrimaryKey())) {
				if (Constants.C_TAG_NEW.equals(requestState)
						|| Constants.C_TAG_READ.equals(requestState)) {
					PfTagsDTO tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
					rTag.setPfTag(tagDTO);
					baseDAO.update(rTag);
				}
			}
		}
	}

	/**
	 * M&eacute;todo que a&ntilde;ade la etiqueta NUEVA o en ESPERA a una petici&oacute;n para los firmantes correspondientes.
	 * @param requestDTO Petici&oacute;n etiquetada.
	 */
	@Transactional(readOnly = false)
	private void insertTagWS(PfRequestsDTO requestDTO) {
		PfRequestTagsDTO requestTagDTO = null;
		PfTagsDTO tagNew = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
		PfTagsDTO tagAwaiting = null;

		if (requestDTO.getLcascadeSign()) {
			tagAwaiting = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING);
		}

		for (PfSignLinesDTO sliDTO : requestDTO.getPfSignsLines()) {
			requestTagDTO = new PfRequestTagsDTO();
			requestTagDTO.setPfRequest(requestDTO);
			if (requestDTO.getLcascadeSign() && sliDTO.getPfSignLine() != null) {
				requestTagDTO.setPfTag(tagAwaiting);
			} else {
				requestTagDTO.setPfTag(tagNew);
			}
			PfSignersDTO signerDTO = ((PfSignLinesDTO) sliDTO).getPfSigners().iterator().next();
			requestTagDTO.setPfUser(signerDTO.getPfUser());
			baseDAO.insertOrUpdate(requestTagDTO);
		}

		// Change udpate date
		requestDTO.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(requestDTO);

	}

	/**
	 * M&eacute;todo que inserta una nueva petici&oacute;n en la BD.
	 * @param requestDTO Petici&oacute;n.
	 * @param applicationDTO Aplicaci&oacute;n que env&iacute;a la petici&oacute;n.
	 * @return C&oacute;digo hash de la petici&oacute;n que la identifica en la BD.
	 */
	@Transactional(readOnly = false)
	private String insertRequestWS(PfRequestsDTO requestDTO, PfApplicationsDTO applicationDTO) {
		requestDTO.setChash(Util.getInstance().createHash());
		requestDTO.setFentry(new Date());
		requestDTO.setLcascadeSign(false);
		requestDTO.setLfirstSignerSign(false);
		requestDTO.setDsubject("DESCONOCIDO");
		requestDTO.setDreference("DESCONOCIDO");
		requestDTO.setPfApplication(applicationDTO);
		// Añadimos que se notifiquen a todos, remitente y destinatarios.
		// Como no está en la interfaz de servicios web se establece a false.
		requestDTO.setLOnlyNotifyActionsToRemitter(false);
		baseDAO.insertOrUpdate(requestDTO);

		return requestDTO.getChash();
	}

	/**
	 * M&eacute;todo que construye un objeto documento a partir de sus datos.
	 * @param fileName Nombre del documento.
	 * @param fileMime Tipo MIME del documento.
	 * @param documentType Tipo del documento
	 * @return Documento.
	 */
	private PfDocumentsDTO createDocumentWS(String fileName, String fileMime,
			PfDocumentTypesDTO documentType) {
		PfDocumentsDTO documentDTO = new PfDocumentsDTO();
		documentDTO.setDname(fileName);
		documentDTO.setDmime(fileMime);
		documentDTO.setLsign(true);

		documentDTO.setPfDocumentType(documentType);
		return documentDTO;
	}

	/**
	 * M&eacute;todo que comprueba que si una petici&oacute;n existe.
	 * @param chash Hash de la petici&oacute;n.
	 * @param sendCheck Si es "True" indica que se debe comprobar si la petici&oacute;n ha sido enviada.
	 * @param result Resultado de la operaci&oacute;n.
	 * @return La variable de entrada result.
	 */
	private PfRequestsDTO checkRequest(String chash, boolean sendCheck,	CheckResult result) {
		PfRequestsDTO req = null;
		log.debug("Checking request");
		if (chash != null && !"".equals(chash)) {
			log.debug("Searching request");
			req = (PfRequestsDTO) baseDAO.queryElementOneParameter(
					"request.requestHash", "hash", chash);
			if (req != null) {
				log.debug("Request found");
				if (sendCheck) {
					if (checkSendRequest(chash)) {
						// request sended
						log.error("Operation not valid because request is sended");
						req = null;
						result.setValue(-1);
					}
				}
			} else {
				// request not found
				log.error("Request: " + chash + " not found");
				result.setValue(-1);
			}
		} else {
			// request not valid
			log.error("Request not valid");
			result.setValue(-1);
		}
		return req;
	}

	/**
	 * M&eacute;todo que comprueba si una aplicaci&oacute;n existe.
	 * @param capplication C&oacute;digo de aplicaci&oacute;n.
	 * @param result Resultado de la b&uacute;squeda.
	 * @return Aplicaci&oacute;n encontrada.
	 */
	private PfApplicationsDTO checkApplication(String capplication,
			CheckResult result) {
		PfApplicationsDTO app = null;
		log.debug("Checking application");
		if (capplication != null && !"".equals(capplication)) {
			log.debug("Searching application");
			app = (PfApplicationsDTO) baseDAO.queryElementOneParameter(
					"request.applicationId", "capp", capplication);
			if (app != null) {
				log.debug("Application found");
			} else {
				// application not found
				log.debug("Application: " + capplication + " not found");
				// result.setValue(-1);
			}
		} else {
			// application not valid
			log.debug("Application not valid");
			// result.setValue(-1);
		}
		if (app == null) {
			log.debug("Assigned to default application");
			app = (PfApplicationsDTO) baseDAO.queryElementOneParameter(
					"request.applicationPfirma", null, null);
		}
		return app;
	}

	/**
	 * M&eacute;todo que comprueba si firmante existe.
	 * @param cdni Dni del usuario firmante.
	 * @param chash C&oacute;digo hash del firmante.
	 * @return La primera l&iacute;nea de firma del firmante.
	 */
	private PfSignLinesDTO checkSigner(String cdni, String chash) {
		PfSignLinesDTO result = null;
		log.debug("Checking exists signer");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("hash", chash);
		parameters.put("dni", cdni.toUpperCase());
		PfRequestsDTO req = (PfRequestsDTO) baseDAO.queryElementMoreParameters(
				"request.requestHashSigner", parameters);
		if (req != null) {
			log.debug("Signer exists");
			result = req.getPfSignsLines().iterator().next();
		} else {
			log.debug("Signer not exists");
		}
		return result;
	}

	/**
	 * M&eacute;todo que obtiene el estado etiquetado en una petici&oacute;n de un usuario.
	 * @param cdni Dni del usuario.
	 * @param chash Hash de la petici&oacute;n.
	 * @return C&oacute;digo de la etiqueta.
	 */
	private String checkStateRequest(String cdni, String chash) {
		String result = null;
		log.debug("Checking state signer");
		if (null != cdni) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("hash", chash);
			parameters.put("dni", cdni.toUpperCase());
			PfTagsDTO tag = (PfTagsDTO) baseDAO.queryElementMoreParameters(
					"request.stateTagHashUser", parameters);
			if (tag != null) {
				log.debug("Request sent");
				result = tag.getCtag();
			} else {
				log.debug("Request not sent");
			}
		}
		return result;
	}

	/**
	 * M&eacute;todo que comprueba que un tipod e documento existe.
	 * @param cdocumentType Identificador del tipo de documento.
	 * @return Tipode documento. Si no existiera, se devuelve el tipo por defecto.
	 */
	private PfDocumentTypesDTO checkDocumentType(String cdocumentType) {
		PfDocumentTypesDTO documentType = null;
		log.debug("Checking documentType");
		if (cdocumentType != null && !"".equals(cdocumentType)) {
			log.debug("Searching documentType");
			documentType = (PfDocumentTypesDTO) baseDAO.queryElementOneParameter("request.documentTypeId", "type", cdocumentType);
			if (documentType != null) {
				log.debug("DocumentType found");
			} else {
				// application not found
				log.debug("DocumentType: " + cdocumentType + " not found");
			}
		} else {
			// application not valid
			log.debug("DocumentType not valid");
		}
		if (documentType == null) {
			log.debug("Assigned to default document type");
			documentType = (PfDocumentTypesDTO) baseDAO
					.queryElementOneParameter("request.documentTypeId", "type",
							Constants.C_DOCUMENTTYPE_GENERIC);
		}
		return documentType;
	}

	/**
	 * M&eacute;todo que comprueba si una petici&oacute;n ha sido enviada.
	 * @param chash C&oacute;digo hash de la petici&oacute;n enviada.
	 * @return "True" si ha sido enviada, "false" en caso contrario.
	 */
	private boolean checkSendRequest(String chash) {
		boolean result = false;
		log.debug("Checking request send");

		if (chash != null && !"".equals(chash)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("chash", chash);
			Long reqCount = baseDAO.queryCount("request.requestStateTagAssociated", params);
			if (reqCount > 0) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * M&eacute;todo que comprueba que un usuario existe en la BD.
	 * @param cdni Dni del usuario.
	 * @param result Resultado de la b&uacute;squeda del usuario.
	 * @param error Booleano que indica si debe guardarse el c&oacute;digo de error en "result" y cambia el modo de depuraci&oacute;n.
	 * @return Usuario buscado.
	 */
	private PfUsersDTO checkUser(String cdni, CheckResult result, boolean error) {
		PfUsersDTO user = null;
		log.debug("Checking user");
		if (cdni != null && !"".equals(cdni)) {
			log.debug("Checking user");
			user = (PfUsersDTO) baseDAO.queryElementOneParameter(
					"request.usersDni", "dni", cdni.toUpperCase());
			if (user != null) {
				log.debug("User found");
			} else {
				if (error) {
					log.error("User not found");
					result.setValue(-1);
				} else {
					log.debug("User not found");
				}
			}
		} else {
			if (error) {
				log.error("User not valid");
				result.setValue(-1);
			} else {
				log.debug("User not valid");
			}
		}
		return user;
	}

	/**
	 * M&eacute;todo que comprueba si un documento existe.
	 * @param chash hash del documento.
	 * @param result resultado de la b&uacute;squeda.
	 * @return Documento encontrado.
	 */
	private PfDocumentsDTO checkDocument(String chash, CheckResult result) {
		PfDocumentsDTO doc = null;
		log.debug("Checking document");
		if (chash != null && !"".equals(chash)) {
			doc = (PfDocumentsDTO) baseDAO.queryElementOneParameter(
					"request.documentHash", "hash", chash);
			if (doc != null) {
				log.debug("Document found");
			} else {
				log.error("Document not found");
				result.setValue(-1);
			}
		} else {
			log.error("Document hash not valid");
			result.setValue(-1);
		}
		return doc;
	}

	/**
	 * M&eacute;todo que comprueba si una etiqueta existe.
	 * @param state Etiqueta de estado.
	 * @param result resultado de la b&uacute;squeda.
	 * @return Etiqueta encontrada.
	 */
	private PfTagsDTO checkState(String state, CheckResult result) {
		PfTagsDTO tag = null;
		log.debug("Checking state");
		if (state != null && !"".equals(state)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ctag", state);
			params.put("ctype", "ESTADO");
			log.debug("Querying state");
			tag = (PfTagsDTO) baseDAO.queryElementMoreParameters(
					"request.tagAll", params);
			if (tag != null) {
				log.debug("State found");
			} else {
				log.error("State not found");
				result.setValue(-1);
			}
		} else {
			log.error("State not valid");
			result.setValue(-1);
		}
		return tag;
	}

	/**
	 * Comprueba que una notificaci&oacute;n existe.
	 * @param chash hash de la petici&oacute;n.
	 * @param notice Notificaci&oacute;n a buscar.
	 * @param result 0 si ha ido bien, -1 en caso contrario.
	 * @return
	 */
	private PfNoticeRequestsDTO checkNotice(String chash, String notice,
			CheckResult result) {
		PfNoticeRequestsDTO noticeReq = null;
		log.debug("Checking notice");
		if (notice != null && !"".equals(notice)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("hash", chash);
			params.put("noticeId", notice);
			noticeReq = (PfNoticeRequestsDTO) baseDAO
					.queryElementMoreParameters("request.noticeRequestHash",
							params);
			if (noticeReq != null) {
				log.debug("Notice found");
			} else {
				log.error("Notice not found");
				result.setValue(-1);
			}
		} else {
			log.error("Notice not valid");
			result.setValue(-1);
		}
		return noticeReq;
	}

	/**
	 * Inserta un nuevo usuario en la BD.
	 * @param identifier Identificador del usuario (DNI).
	 * @param name Nombre del usuario.
	 * @param surname1 Primer apellido.
	 * @param surname2 Segundo apellido.
	 * @return El usuario.
	 */
	@Transactional(readOnly = false)
	private PfUsersDTO insertUser(String identifier, String name,
			String surname1, String surname2) {
		PfUsersDTO user = new PfUsersDTO();

		user.setDname(name);
		user.setDsurname1(surname1);
		user.setDsurname2(surname2);
		user.setCidentifier(identifier);
		user.setCtype(Constants.C_TYPE_TAG_USER);
		user.setLvalid(true);
		baseDAO.insertOrUpdate(user);

		return user;
	}

	/**
	 * M&eacute;todo que obtiene la primera l&iacute;nea de firma de una petici&oacute;n.
	 * @param chash Hash de la petici&oacute;n.
	 * @return L&iacute;nea de firma.
	 */
	private PfSignLinesDTO findSignLinePrevious(String chash) {
		PfSignLinesDTO result = null;
		List<AbstractBaseDTO> list = baseDAO.queryListOneParameter(
				"request.requestSignLineDesc", "chash", chash);
		if (list != null && !list.isEmpty()) {
			result = (PfSignLinesDTO) list.get(0);
		}

		return result;
	}

	/**
	 * M&eacute;todo que cambia el tipo (cascada, paralelo) de las l&iacute;neas de firma de una petici&oacute;n.
	 * @param req Petici&oacute;n.
	 */
	@Transactional(readOnly = false)
	private void changeSignLinesType(PfRequestsDTO req) {
		PfSignLinesDTO signLinePrevious = null;
		List<AbstractBaseDTO> list = baseDAO.queryListOneParameter(
				"request.requestSignLineAsc", "chash", req.getChash());
		if (list != null && !list.isEmpty()) {
			for (AbstractBaseDTO signLine : list) {
				if (req.getLcascadeSign()) {
					if (signLinePrevious != null) {
						((PfSignLinesDTO) signLine).setPfSignLine(signLinePrevious);
						baseDAO.update(signLine);
					}
					signLinePrevious = (PfSignLinesDTO) signLine;
				} else {
					((PfSignLinesDTO) signLine).setPfSignLine(null);
					baseDAO.update(signLine);
				}
			}
		}
	}

}
