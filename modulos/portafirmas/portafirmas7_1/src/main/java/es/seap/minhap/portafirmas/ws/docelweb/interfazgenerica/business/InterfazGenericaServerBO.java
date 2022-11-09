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

/**
 * Business object that implements the logical methods InterfazGenerica web services, 
 * where the server side Portafirmas system.
 */
package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.cxf.interceptor.Fault;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.interfazGenerica.repository.PortafirmasRepository;
import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebDocumentDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSpfirmaDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfFilesDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.AnularSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ComenzarSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ConsultarDocumentoFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ConsultarEstadoSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ConsultarSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.DocumentoSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.RegistrarDocumentoFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.RegistrarSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.SistemaGestionFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.SolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.AnularSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.AnularSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ComenzarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ComenzarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarDocumentoElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarDocumentoResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarEstadoSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarEstadoSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TODocumento;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TODocumentoSalida;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TOSolicitudConsulta;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TOSolicitudEstado;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TOSolicitudNueva;

/**
 * @author MINHAP
 * Business object that implements the logical methods InterfazGenerica web services, 
 * where the server side Portafirmas system.
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class InterfazGenericaServerBO {



	/**
	 * The logger object.
	 */
	private Logger log = Logger.getLogger(InterfazGenericaServerBO.class);

	/**
	 * The timestamp format supported.
	 */
	private final SimpleDateFormat sdf = new SimpleDateFormat(DocelwebConstants.DATETIME_FORMAT);

	/**
	 * The generic user remitter.
	 */
	private PfUsersDTO genericUserRemitter;

	/**
	 * The Base DAO.
	 */
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private PortafirmasRepository portafirmasRepository;

	/**
	 * The Request BO.
	 */
	@Autowired
	private RequestBO requestBO;

	/**
	 * The Tag BO.
	 */
	@Autowired
	private TagBO tagBO;
	
	/**
	 * The Notice BO.
	 */
	@Autowired
	private NoticeBO noticeBO;

	/**
	 * Pfirma Custody service factory.
	 */
	@Autowired
	private CustodyServiceFactory custodyServiceFactory;

	/**
	 * Pfirma tags types.
	 */
	@Autowired
	private ApplicationVO applicationVO;

	@Autowired
	private DateComponent date;
	
	/**
	 * Notification config tags
	 */
	private String[ ] notices = { "noticeRead", "noticeReject", "noticeSign" };

	// INICIO LÓGICA DE LOS SERVICIOS WEB DOCEL

	/**
	 * Transactional method to begin a new request service.
	 * @param parameters Manager code and the new request params.
	 * @return The unique request ID.
	 * @throws ComenzarSolicitudFault
	 * @throws SistemaGestionFault
	 */
	@Transactional
	public ComenzarSolicitudResponseElement comenzarSolicitud(ComenzarSolicitudElement parameters) throws ComenzarSolicitudFault, SistemaGestionFault {
		if (parameters != null) {
			ComenzarSolicitudResponseElement response = new ComenzarSolicitudResponseElement();
			// Comprobamos los datos de la nueva solicitud y obtenemos el
			// firmante relacionado.
			String managerCode = parameters.getCodigoSistemaGestor();
			Portafirmas portafirmas = checkSystemManagerCode(managerCode);
			TOSolicitudNueva newRequest = parameters.getSolicitud();
			List<PfUsersDTO> signerDTOs = checkInitRequestParams(newRequest);
			// Creamos la nueva solicitud.
			PfDocelwebRequestSpfirmaDTO docelRequest = new PfDocelwebRequestSpfirmaDTO();
			docelRequest.setdState(DocelwebConstants.REQUEST_STATE_NEW);
			docelRequest.setdDescription(newRequest.getDescripcion());			
			docelRequest.setdMultisignType(newRequest.getFirmaMultiple());			
			docelRequest.setdPriority(newRequest.getPrioridad());
			docelRequest.setdSenderName(newRequest.getNombreSolicitante());
			docelRequest.setdSignatureFormat(newRequest.getTipoFirma());
			if (newRequest.getTipoFirma().equalsIgnoreCase(DocelwebConstants.XAdES)) {
				// Solo incluimos la versión si es una firma XAdES
				docelRequest.setdXadesVersion(newRequest.getVersionFirma());
			}
			try {
				if (newRequest.getFechaLimite() != null) {
					docelRequest.setfDeadline(sdf.parse(newRequest.getFechaLimite()));
				}
			} catch (ParseException e) {
				throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + e.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E04_OTHER_ERROR);
			}
			// Relación de la nueva petición con el firmante y el sistema
			// de gestión.
			docelRequest.setPortafirmas(portafirmas);
			docelRequest.setPfSigners(signerDTOs);
			baseDAO.insertOrUpdate(docelRequest);
			// Devolvemos la clave primaria de la solicitud, qué hace las veces
			// de identificador de transacción.
			response.setResult(docelRequest.getPrimaryKey());
			return response;
		} else {
			throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "InterfazGenerica.comenzarSolicitud(parameters) WS request received with null params", Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	/**
	 * Trnasactional method to register a new document in an existing DOCEL request.
	 * @param parameters Manager code and new document params
	 * @return The unique document ID.
	 * @throws RegistrarDocumentoFault
	 * @throws SistemaGestionFault
	 * @throws SolicitudFault
	 */
	@Transactional
	public RegistrarDocumentoResponseElement registrarDocumento(RegistrarDocumentoElement parameters) throws RegistrarDocumentoFault, SistemaGestionFault, SolicitudFault {
		if (parameters != null) {
			RegistrarDocumentoResponseElement response = new RegistrarDocumentoResponseElement();
			// Comprobamos los parámetros de entrada para el registro de
			// documentos.
			String managerCode = parameters.getCodigoSistemaGestor();
			Portafirmas systemManagerDTO = checkSystemManagerCode(managerCode);
			Long transactionId = parameters.getIdSolicitud();
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = checkDocelRequestTransactionForSpfirma(transactionId);
			checkCorrespondenceSystemManagerRequest(systemManagerDTO, docelRequestDTO);
			TODocumento document = parameters.getDocumento();
			checkDocumentRegistrationParams(document);
			// Comprobamos que no existe un documento en la petición con la misma descripción
			PfDocelwebDocumentDTO docelwebDocumentDTO = existsDocumentDescriptionInRequest(document.getDescripcion(), docelRequestDTO);
			if (docelwebDocumentDTO == null) {
				docelwebDocumentDTO = new PfDocelwebDocumentDTO();
				docelwebDocumentDTO.setdDescription(document.getDescripcion());
				docelwebDocumentDTO.setdDocumentPath(document.getNombreDocumento());
				docelwebDocumentDTO.setdDocelType(document.getTipoDocumento());
				if (document.getNecesitaFirma().equalsIgnoreCase(DocelwebConstants.YES)) {
					docelwebDocumentDTO.setdReqSign(true);
				} else {
					docelwebDocumentDTO.setdReqSign(false);
				}
				if (document.getTipoDocumento().equalsIgnoreCase(DocelwebConstants.ELECTRONIC_DOC)) {
					try {
						// Contenido en base64/MTOM.
						log.debug("Leyendo contenido del documento Electrónico [Base64/MTOM]");
						byte[ ] docContent = document.getContenido();
						ByteArrayInputStream baisDocContent = new ByteArrayInputStream(docContent);
						String checkHash = Util.getInstance().createHash(baisDocContent);
						PfFilesDTO fileDTO = (PfFilesDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_FILE_CONTENT_HASH, DocelwebConstants.QUERY_PARAM_FILE_HASH, checkHash);
						if (fileDTO == null) {
							log.debug("No existe ningún contenido de documento con hash [" + checkHash + "]. Se almacena el nuevo contenido.");
							fileDTO = new PfFilesDTO();
							fileDTO.setChash(checkHash);
							fileDTO.setChashAlg(Constants.C_HASH_ALG_SHA1);
							CustodyServiceInput custodyInputService;
							String storageType = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.DOCUMENTOS.name());
							custodyInputService = custodyServiceFactory.createCustodyServiceInput(storageType);
							fileDTO.setCtype(storageType);
							String dir3 = "";
							try {
								UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
								PfUsersDTO user = authorization.getUserDTO();
								dir3 = user.getPfProvince().getCcodigoprovincia();
							} catch (Exception e) {
								//En caso de que sea un WS quien crea la petición no hay usuario logado
								// TODO: Hasta que las aplicaciones no estén asociadas a un dir3 ponemos el genérico de Administración del Estado EA9999999 es configurable en administracion custodia
								dir3 = custodyServiceFactory.custodiaDir3PorDefecto();
							}
							fileDTO.setRefNasDir3(dir3);
							fileDTO.setIdEni(Util.getInstance().generarIDEni(checkHash, dir3));
							baseDAO.insertOrUpdate(fileDTO);
							InputStream isDoc = new ByteArrayInputStream(docContent);
							CustodyServiceInputDocument inputDocument = Util.getInstance().docelDocumentToCustodyServiceInputDocument(document.getNombreDocumento(), fileDTO, isDoc.available());
							String uri = custodyInputService.uploadFile(inputDocument, isDoc);
							isDoc.close();
							if (uri != null && !uri.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
								fileDTO.setCuri(uri);
								baseDAO.insertOrUpdate(fileDTO);
							}
						} else {
							log.debug("Contenido del documento ya registrado con hash [" + checkHash + "]. No se almacena el contenido de nuevo.");
						}
						docelwebDocumentDTO.setdDispositionUrl(null);
						docelwebDocumentDTO.setPfFile(fileDTO);
					} catch (Exception e) {
						throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + e.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, e);
					}
				} else if (document.getTipoDocumento().equalsIgnoreCase(DocelwebConstants.DISPOSITION_DOC)) {
					log.debug("Leyendo contenido del documento de Disposición/URL [Base64/MTOM]");
					byte[ ] docContent = document.getContenido();
					docelwebDocumentDTO.setdDispositionUrl(new String(docContent));
					// docelwebDocumentDTO.setdDispositionUrl(new
					// String(document.getContenido()));
					docelwebDocumentDTO.setPfFile(null);
				} else if (document.getTipoDocumento().equalsIgnoreCase(DocelwebConstants.PAPER_DOC)) {
					log.debug("El contenido del documento en Papel debe ser nulo");
					docelwebDocumentDTO.setdDispositionUrl(null);
					docelwebDocumentDTO.setPfFile(null);
				}
				docelwebDocumentDTO.setPfRequest(docelRequestDTO);
				baseDAO.insertOrUpdate(docelwebDocumentDTO);
			}
			response.setResult(docelwebDocumentDTO.getPrimaryKey());
			return response;
		} else {
			throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "InterfazGenerica.registrarDocumento(parameters) WS request received with null params", Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	/**
	 * Transactional method that returns the document requested information. If the document is already signed, tyhen returned the sign.
	 * @param parameters Manager code and the document requested params
	 * @return The document info with the document content or sign content.
	 * @throws ConsultarDocumentoFault
	 * @throws SistemaGestionFault
	 * @throws SolicitudFault
	 * @throws DocumentoSolicitudFault
	 */
	@Transactional(readOnly=false)
	public ConsultarDocumentoResponseElement consultarDocumento(ConsultarDocumentoElement parameters) throws ConsultarDocumentoFault, SistemaGestionFault, SolicitudFault, DocumentoSolicitudFault {
		if (parameters != null) {
			ConsultarDocumentoResponseElement response = new ConsultarDocumentoResponseElement();
			// Comprobamos los parámetros de entrada para la consulta de
			// documentos.
			String managerCode = parameters.getCodigoSistemaGestor();
			Portafirmas systemManagerDTO = checkSystemManagerCode(managerCode);
			Long transactionId = parameters.getIdSolicitud();
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = checkDocelRequestTransactionForSpfirma(transactionId);
			checkCorrespondenceSystemManagerRequest(systemManagerDTO, docelRequestDTO);
			if (parameters.getIdDocumento() != null) {
				try {
					// Comprobamos que el documento se corresponde con la solicitud
					PfDocelwebDocumentDTO pfDocelwebDocumentDTO = checkCorrespondenceDocumentRequest(docelRequestDTO, parameters.getIdDocumento());
					TODocumentoSalida documentInfo = new TODocumentoSalida();
					PfSignsDTO pfSignDTO = getDocelDocumentSign(pfDocelwebDocumentDTO);
					if (pfSignDTO != null) {
						documentInfo.setFirmado(DocelwebConstants.YES);
						// Devolvemos la firma o el documento firmado.
						if (pfDocelwebDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.ELECTRONIC_DOC) || pfDocelwebDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.DISPOSITION_DOC)) {
							// Firma
							CustodyServiceOutput custodyOutputService = custodyServiceFactory.createCustodyServiceOutput(pfSignDTO.getCtype());
							CustodyServiceOutputSign outputSign = Util.getInstance().docelDocumentSignedToCustodyServiceOutputSign(pfSignDTO);
							documentInfo.setContenido(custodyOutputService.downloadSign(outputSign));
						} else if (pfDocelwebDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.PAPER_DOC)) {
							// NULL
							documentInfo.setContenido(null);
						}
					} else {
						documentInfo.setFirmado(DocelwebConstants.NO);
						// Delveolvemos el documento original.
						if (pfDocelwebDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.ELECTRONIC_DOC)) {
							// Documento
							CustodyServiceOutput custodyOutputService = custodyServiceFactory.createCustodyServiceOutput(pfDocelwebDocumentDTO.getPfFile().getCtype());
							CustodyServiceOutputDocument outputDocument = Util.getInstance().docelDocumentToCustodyServiceOutputDocument(pfDocelwebDocumentDTO.getPfFile());
							documentInfo.setContenido(custodyOutputService.downloadDocelFile(outputDocument));
						} else if (pfDocelwebDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.DISPOSITION_DOC)) {
							// URL
							documentInfo.setContenido(pfDocelwebDocumentDTO.getdDispositionUrl().getBytes());
						} else if (pfDocelwebDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.PAPER_DOC)) {
							// NULL
							documentInfo.setContenido(null);
						}
					}
					if (pfDocelwebDocumentDTO.getdReqSign()) {
						documentInfo.setNecesitaFirma(DocelwebConstants.YES);
					} else {
						documentInfo.setNecesitaFirma(DocelwebConstants.NO);
					}
					documentInfo.setDescripcion(pfDocelwebDocumentDTO.getdDescription());
					documentInfo.setNombreDocumento(pfDocelwebDocumentDTO.getdDocumentPath());
					documentInfo.setTipoDocumento(pfDocelwebDocumentDTO.getdDocelType());
					
					reformularNombreFirma(documentInfo, pfSignDTO, pfDocelwebDocumentDTO);
					
					response.setResult(documentInfo);
					return response;
				} catch (DocumentoSolicitudFault e) {
					throw e;
				} catch (Exception e) {
					throw new ConsultarDocumentoFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + e.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, e);
				}
			} else {
				throw new ConsultarDocumentoFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null document ID", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E00_UNKNOWN);
			}
		} else {
			throw new ConsultarDocumentoFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "InterfazGenerica.consultarDocumento(parameters) WS request received with null params", Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	/**
	 * @param documentInfo
	 * @param pfSignDTO
	 * @param pfDocelwebDocumentDTO
	 */
	private void reformularNombreFirma(TODocumentoSalida documentInfo, PfSignsDTO pfSignDTO, PfDocelwebDocumentDTO pfDocelwebDocumentDTO) {
		if (pfSignDTO != null) {
			documentInfo.setNombreDocumento(pfDocelwebDocumentDTO.getdDocumentPath() + "_firmado.xsig");
		}
	}

	/**
	 * Transactional method to register a DOCEL request in Portafirmas.
	 * @param parameters Manager code and the existing request params.
	 * @return The unique request code 'S' if success.
	 * @throws RegistrarSolicitudFault
	 * @throws SistemaGestionFault
	 * @throws SolicitudFault
	 * @throws DocumentoSolicitudFault
	 */
	@Transactional
	public RegistrarSolicitudResponseElement registrarSolicitud(RegistrarSolicitudElement parameters) throws RegistrarSolicitudFault, SistemaGestionFault, SolicitudFault, DocumentoSolicitudFault {
		
		if (parameters != null) {
			RegistrarSolicitudResponseElement response = new RegistrarSolicitudResponseElement();
			// Comprobamos los parámetros de entrada para el registro de la solicitud.
			String managerCode = parameters.getCodigoSistemaGestor();
			Portafirmas systemManagerDTO = checkSystemManagerCode(managerCode);
			Long transactionId = parameters.getIdSolicitud();
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = checkDocelRequestTransactionForSpfirma(transactionId);
			checkRequestRegistrationParams(docelRequestDTO);
			checkCorrespondenceSystemManagerRequest(systemManagerDTO, docelRequestDTO);
			List<Long> documentIdList = parameters.getDocumento();
			checkDocumentListToSign(docelRequestDTO, documentIdList);
			List<PfDocelwebDocumentDTO> documentRequestedList = new ArrayList<PfDocelwebDocumentDTO>();
			for (Iterator<Long> iterator = documentIdList.iterator(); iterator.hasNext();) {
				Long documentId = iterator.next();
				documentRequestedList.add(checkCorrespondenceDocumentRequest(docelRequestDTO, documentId));
			}
			// Creamos los datos de la petición en Portafirmas
			
			PfRequestsDTO pfirmaRequestDTO = new PfRequestsDTO();
			String hash = Util.getInstance().createHash();
			String idApp = managerCode + "_" + docelRequestDTO.getdSignatureFormat();
			PfApplicationsDTO applicationDTO = (PfApplicationsDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_APPLICATION_IG, "idApp", idApp);
			pfirmaRequestDTO.setPfApplication(applicationDTO);
			pfirmaRequestDTO.setChash(hash);
			pfirmaRequestDTO.setAccionesPrevia(false);
			pfirmaRequestDTO.setFentry(new Date());
			pfirmaRequestDTO.setFstart(null);
			//Petición no invitada (default)
			pfirmaRequestDTO.setLinvited(false);
			//Petición no aceptada (default)
			pfirmaRequestDTO.setLaccepted(false);
			pfirmaRequestDTO.setFexpiration(null);
			pfirmaRequestDTO.setDsubject(docelRequestDTO.getdDescription());
			pfirmaRequestDTO.setDreference(systemManagerDTO.getcPortafirmas() + DocelwebConstants.FAULT_CODE_SEPARATOR + docelRequestDTO.getPrimaryKeyString());
			pfirmaRequestDTO.setLtimestamp(DocelwebConstants.NO);
			pfirmaRequestDTO.setLOnlyNotifyActionsToRemitter(false);
			insertImportanceLevel(pfirmaRequestDTO, docelRequestDTO.getdPriority());
			// Cuerpo de la petición
			PfRequestsTextDTO requestTextDTO = new PfRequestsTextDTO();
			requestTextDTO.setChash(hash);
			requestTextDTO.setTrequest(getRequestBody(systemManagerDTO.getNombre(), docelRequestDTO.getdSenderName(), documentRequestedList, docelRequestDTO.getfDeadline()));
			// Línea de firma para los firmantes
			List<UserEnvelope> signers = new ArrayList<UserEnvelope>();
			for (Iterator<PfUsersDTO> iterator = docelRequestDTO.getPfSigners().iterator(); iterator.hasNext();) {
				UserEnvelope userEnvelope = new UserEnvelope((PfUsersDTO) iterator.next());
				signers.add(userEnvelope);
			}
			
			String signType = Constants.SIGN_TYPE_CASCADE;
			
			if (docelRequestDTO.getdMultisignType().equalsIgnoreCase(DocelwebConstants.MULTISIGN_PARALLEL)) {
				signType = Constants.SIGN_TYPE_PARALLEL;
			}
			
			try {
				requestBO.saveDocelwebRequest(pfirmaRequestDTO, requestTextDTO, signers, signType, notices, applicationDTO, applicationVO.getStateTags(), queryGenericUser(), documentRequestedList);
				noticeBO.noticeNewRequest(pfirmaRequestDTO);
				noticeBO.noticeNewRequestValidador(pfirmaRequestDTO);
				
			} catch (Exception e) {
				log.error("Error al registrar la petición.", e);
				throw new RegistrarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + e.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
			}
			// Una vez creada registrada la petición en portafirmas, la petición
			// DOCEL pasa al estado pendiente.
			docelRequestDTO.setdState(DocelwebConstants.REQUEST_STATE_WAITING);
			docelRequestDTO.setPfRequest(pfirmaRequestDTO);
			baseDAO.insertOrUpdate(docelRequestDTO);
			// Se devuelve el valor 'S', indicando que se ha registrado la
			// solicitud y está lista para ser firmada por el firmante.
			response.setResult(DocelwebConstants.YES);
			return response;
		} else {
			throw new RegistrarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "InterfazGenerica.registrarSolicitud(parameters) WS request received with null params", Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	/**
	 * Transactional method to consult a DOCEL request state.
	 * @param parameters Manager code and the existing request params.
	 * @return The DOCEL request state.
	 * @throws ConsultarEstadoSolicitudFault
	 * @throws SistemaGestionFault
	 * @throws SolicitudFault
	 */
	@Transactional(readOnly=false)
	public ConsultarEstadoSolicitudResponseElement consultarEstadoSolicitud(ConsultarEstadoSolicitudElement parameters) throws ConsultarEstadoSolicitudFault, SistemaGestionFault, SolicitudFault {
		if (parameters != null) {
			ConsultarEstadoSolicitudResponseElement response = new ConsultarEstadoSolicitudResponseElement();
			// Comprobamos los parámetros de entrada para la consulta de estado
			// de la solicitud datos de la solicitud.
			String managerCode = parameters.getCodigoSistemaGestor();
			Portafirmas systemManagerDTO = checkSystemManagerCode(managerCode);
			Long transactionId = parameters.getIdSolicitud();
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = checkDocelRequestTransactionForSpfirma(transactionId);
			checkCorrespondenceSystemManagerRequest(systemManagerDTO, docelRequestDTO);
			response.setResult(docelRequestDTO.getdState());
			return response;
		} else {
			throw new ConsultarEstadoSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "InterfazGenerica.consultarEstadoSolicitud(parameters) WS request received with null params", Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	/**
	 * Transactional method to consult a DOCEL request data.
	 * @param parameters Manager code and the existing request params.
	 * @return The DOCEL request data.
	 * @throws ConsultarSolicitudFault
	 * @throws SistemaGestionFault
	 * @throws SolicitudFault
	 */
	@Transactional(readOnly=false)
	public ConsultarSolicitudResponseElement consultarSolicitud(ConsultarSolicitudElement parameters) throws ConsultarSolicitudFault, SistemaGestionFault, SolicitudFault {
		if (parameters != null) {
			ConsultarSolicitudResponseElement response = new ConsultarSolicitudResponseElement();
			// Comprobamos los parámetros de entrada para la consulta de estado
			// de la solicitud datos de la solicitud.
			String managerCode = parameters.getCodigoSistemaGestor();
			Portafirmas systemManagerDTO = checkSystemManagerCode(managerCode);
			Long transactionId = parameters.getIdSolicitud();
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = checkDocelRequestTransactionForSpfirma(transactionId);
			checkCorrespondenceSystemManagerRequest(systemManagerDTO, docelRequestDTO);
			TOSolicitudConsulta requestData = new TOSolicitudConsulta();
			requestData.setSolicitante(docelRequestDTO.getdSenderName());
			requestData.setPrioridad(docelRequestDTO.getdPriority());
			List<Long> requestDocumentIds = new ArrayList<Long>();
			Set<PfDocelwebDocumentDTO> docelRequetDocumentList = docelRequestDTO.getPfDocuments();
			for (Iterator<PfDocelwebDocumentDTO> iterator = docelRequetDocumentList.iterator(); iterator.hasNext();) {
				PfDocelwebDocumentDTO docelDocumentDTO = iterator.next();
				requestDocumentIds.add(docelDocumentDTO.getPrimaryKey());
			}
			requestData.getIdsDocumentos().addAll(requestDocumentIds);
			requestData.setDescripcion(docelRequestDTO.getdDescription());
			String loginSigners = getSignersLogin(docelRequestDTO.getPfSigners());
			requestData.setLoginFirmante(loginSigners);
			TOSolicitudEstado docelRequestState = new TOSolicitudEstado();
			docelRequestState.setEstadoRevision(getSignersRevisionState(docelRequestDTO.getPfRequest()));
			
			//#301 - Añadir observaciones al rechazo en IGPF.
			PfCommentsDTO comentario = getLastComment(docelRequestDTO.getPfRequest());
			if (comentario != null && !comentario.getTcomment().trim().isEmpty()){
				docelRequestState.setEstadoFirma(docelRequestDTO.getdState() + "###" + comentario.getTcomment());			
			}else
				docelRequestState.setEstadoFirma(docelRequestDTO.getdState());			
			
			requestData.setEstadoSolicitud(docelRequestState);
			response.setResult(requestData);
			return response;
		} else {
			throw new ConsultarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "InterfazGenerica.consultarEstadoSolicitud(parameters) WS request received with null params", Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	/**
	 * Transactional method to invalidate a DOCEL request y new o waiting state.
	 * @param parameters Manager code and the existing request params.
	 * @return The unique request code 'S' if success.
	 * @throws AnularSolicitudFault
	 * @throws SistemaGestionFault
	 * @throws SolicitudFault
	 */
	@Transactional(readOnly=false)
	public AnularSolicitudResponseElement anularSolicitud(AnularSolicitudElement parameters) throws AnularSolicitudFault, SistemaGestionFault, SolicitudFault {
		if (parameters != null) {
			AnularSolicitudResponseElement response = new AnularSolicitudResponseElement();
			// Comprobamos los parámetros de entrada para la consulta de estado
			// de la solicitud datos de la solicitud.
			String managerCode = parameters.getCodigoSistemaGestor();
			Portafirmas systemManagerDTO = checkSystemManagerCode(managerCode);
			Long transactionId = parameters.getIdSolicitud();
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = checkDocelRequestTransactionForSpfirma(transactionId);
			checkCorrespondenceSystemManagerRequest(systemManagerDTO, docelRequestDTO);
			if (docelRequestDTO.getdState().equalsIgnoreCase(DocelwebConstants.REQUEST_STATE_NEW)) {
				log.debug("Se anula la petición DOCEL [" + transactionId + "] con estado 'N'");
				changeDocelRequestStatus(docelRequestDTO, DocelwebConstants.REQUEST_STATE_VOID);
			} else if (docelRequestDTO.getdState().equalsIgnoreCase(DocelwebConstants.REQUEST_STATE_WAITING)) {
				log.debug("Se anula la petición DOCEL [" + transactionId + "] con estado 'P'");
				tagBO.changeStateToRemovedRequest(docelRequestDTO.getPfRequest(), DocelwebConstants.DOCEL_REJECTION_TEXT + "(" + docelRequestDTO.getdSenderName() + ")", queryGenericUser(), false);
				changeDocelRequestStatus(docelRequestDTO, DocelwebConstants.REQUEST_STATE_VOID);
			} else if (docelRequestDTO.getdState().equalsIgnoreCase(DocelwebConstants.REQUEST_STATE_VOID)) {
				throw new AnularSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "There has been a retry. InterfazGenerica.anularSolicitud execution successful", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
			} else {
				throw new AnularSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Can´t cancel a request with state [" + docelRequestDTO.getdState() + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
			}
			response.setResult(DocelwebConstants.YES);
			return response;
		} else {
			throw new AnularSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + "InterfazGenerica.anularSolicitud(parameters) WS request received with null params", Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	// FIN LÓGICA DE LOS SERVICIOS WEB DOCEL
	// INICIO COMPROBACIONES COMUNES

	/**
	 * Check if the system manager exists in the portafirmas system. 
	 * @param managerCode The system manager identifier (applicationId)
	 * @return The system manager DTO
	 * @throws SistemaGestionFault
	 */
	private Portafirmas checkSystemManagerCode(String managerCode) throws SistemaGestionFault {
		Portafirmas systemManager = null;
		Portafirmas portafirmas = new Portafirmas();
		portafirmas.setcPortafirmas(managerCode);
		List<Portafirmas> listaPortafirmas = portafirmasRepository.buscar(portafirmas);
		if (listaPortafirmas != null && !listaPortafirmas.isEmpty()) {
			systemManager = listaPortafirmas.get(0);
		}
		if (systemManager == null) {
			log.error("System manager [" + managerCode + "] is invalid");
			throw new SistemaGestionFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Invalid System Manager [" + managerCode + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		}
		return systemManager;
	}

	/**
	 * Checks if the request transaction is valid and exists in the portafirmas system.
	 * @param idTransaction The request transaction id
	 * @return The portafirmas system request DTO
	 * @throws SolicitudFault
	 */
	private PfDocelwebRequestSpfirmaDTO checkDocelRequestTransactionForSpfirma(Long idTransaction) throws SolicitudFault {
		PfDocelwebRequestSpfirmaDTO docelRequest = null;
		log.debug("Checking InterfazGenerica request transaction [" + idTransaction + "] in the portafirmas system");
		if (idTransaction != null) {
			docelRequest = (PfDocelwebRequestSpfirmaDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_DOCEL_REQUEST_SPFIRMA, DocelwebConstants.QUERY_PARAM_DOCEL_SP_TRANSACTION_ID, idTransaction);
			if (docelRequest != null) {
				log.debug("Portafirmas InterfazGenerica request [" + idTransaction + "] found");
			} else {
				log.debug("Portafirmas InterfazGenerica request [" + idTransaction + "] not found");
			}
		}
		if (docelRequest == null) {
			log.error("Portafirmas InterfazGenerica request [" + idTransaction + "] is invalid");
			throw new SolicitudFault(DocelwebConstants.CODE_E05_UNKNOWN_REQUEST + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unrecognized request with transaction ID [" + idTransaction + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E05_UNKNOWN_REQUEST);
		}
		return docelRequest;
	}

	/**
	 * Checks the correspondene between a system manager and portafirmas request.
	 * @param systemManagerDTO The system manager in the document registration request
	 * @param docelRequestDTO The system manager of the transaction request
	 * @throws SistemaGestionFault
	 */
	private void checkCorrespondenceSystemManagerRequest(Portafirmas systemManagerDTO, PfDocelwebRequestSpfirmaDTO docelRequestDTO) throws SistemaGestionFault {
		String requestSystemManager = docelRequestDTO.getPortafirmas().getcPortafirmas();
		if (!systemManagerDTO.getcPortafirmas().equalsIgnoreCase(requestSystemManager)) {
			throw new SistemaGestionFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "System manager [" + systemManagerDTO.getcPortafirmas() + "], missmatch with the request system manager [" + requestSystemManager + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		}
	}

	/**
	 * Checks and returns whether a document in the application's description InterfazGenerica parameterized.
	 * @param documentDescription The document descripton searched
	 * @param docelRequestDTO The associated request
	 * @return The document DTO already registered if exists
	 */
	private PfDocelwebDocumentDTO existsDocumentDescriptionInRequest(String documentDescription, PfDocelwebRequestSpfirmaDTO docelRequestDTO) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_DOCUMENT_DESCRIPTION, documentDescription);
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_REQUEST, docelRequestDTO);
		PfDocelwebDocumentDTO docelDocumentDTO = (PfDocelwebDocumentDTO) baseDAO.queryElementMoreParameters(DocelwebConstants.QUERY_DOCEL_DOCUMENT_REQUEST, parameters);
		if (docelDocumentDTO != null) {
			log.debug("Descripción de documento repetida [" + documentDescription + "] en la solicitud [" + docelRequestDTO.getPrimaryKeyString() + "]. Se deveulve el ID del documento ya registrado [" + docelDocumentDTO.getPrimaryKeyString() + "] en la misma.");
		}
		return docelDocumentDTO;
	}

	/**
	 * Checks the correspondene between a DOCEL request and document ID.
	 * @param docelRequestDTO The DOCEL request
	 * @param documentId The DOCEL document ID
	 * @return The DOCEL document DTO
	 * @throws DocumentoSolicitudFault
	 */
	private PfDocelwebDocumentDTO checkCorrespondenceDocumentRequest(PfDocelwebRequestSpfirmaDTO docelRequestDTO, Long documentId) throws DocumentoSolicitudFault {
		Set<PfDocelwebDocumentDTO> requestDocumentSet = docelRequestDTO.getPfDocuments();
		PfDocelwebDocumentDTO pfDocelwebDocumentDTO = null;
		for (Iterator<PfDocelwebDocumentDTO> iterator = requestDocumentSet.iterator(); iterator.hasNext();) {
			pfDocelwebDocumentDTO = iterator.next();
			if (pfDocelwebDocumentDTO.getPrimaryKey().equals(documentId)) {
				log.debug("Documento [" + documentId + "] en contrado en la solicitud [" + docelRequestDTO.getPrimaryKey() + "]");
				break;
			}
		}
		// Comprobación de asociación del documento con la solicitud
		if (pfDocelwebDocumentDTO == null) {
			throw new DocumentoSolicitudFault(DocelwebConstants.CODE_E07_MISSPMATCH_DOCUMENT + DocelwebConstants.FAULT_CODE_SEPARATOR + "The associated request hasn´t documents", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E07_MISSPMATCH_DOCUMENT);
		} else if (!pfDocelwebDocumentDTO.getPrimaryKey().equals(documentId)) {
			throw new DocumentoSolicitudFault(DocelwebConstants.CODE_E07_MISSPMATCH_DOCUMENT + DocelwebConstants.FAULT_CODE_SEPARATOR + "The requested document [" + documentId + "] does not match the request [" + docelRequestDTO.getPrimaryKeyString() + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E07_MISSPMATCH_DOCUMENT);
		}
		return pfDocelwebDocumentDTO;
	}

	// FIN COMPROBACIONES COMUNES

	// INICIO COMPROBACIONES POR MÉTODO WS

	/**
	 * Checks the request parameters and returns the signer.
	 * @param newRequest The request params
	 * @return The request signers list
	 * @throws ComenzarSolicitudFault 
	 */
	private List<PfUsersDTO> checkInitRequestParams(TOSolicitudNueva newRequest) throws ComenzarSolicitudFault {
		String signtype = newRequest.getTipoFirma();
		if (signtype == null || signtype.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
			throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E01_UNSUPPORTED_SIGNTYPE + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null signature type", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E01_UNSUPPORTED_SIGNTYPE);
		} else {
			if (!signtype.equalsIgnoreCase(DocelwebConstants.CAdES) && !signtype.equalsIgnoreCase(DocelwebConstants.XAdES) && !signtype.equalsIgnoreCase(DocelwebConstants.PAdES)) {
				throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E01_UNSUPPORTED_SIGNTYPE + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unsupported signature type [" + signtype + "]. Supported types {XAdES, CAdES, PAdES}", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E01_UNSUPPORTED_SIGNTYPE);
			}
		}

		String version = newRequest.getVersionFirma();
		if (signtype.equalsIgnoreCase(DocelwebConstants.XAdES)) {
			if (version == null || version.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
				throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E02_UNSUPPORTED_VERSION + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null XAdES version", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E02_UNSUPPORTED_VERSION);
			} else {
				if (!version.equalsIgnoreCase(DocelwebConstants.XAdES_132)) {
					// No soportamos la versión 1.2.2
					throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E02_UNSUPPORTED_VERSION + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unsupported XAdES version [" + version + "]. Supported XAdES versions {1.3.2}", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E02_UNSUPPORTED_VERSION);
				}
			}
		} else {
			if (version != null && !version.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
				log.warn("El parámetro 'version' solo se tiene en cuenta para firmas XAdES, al utilizar firma [" + signtype + "] la versión [" + version + "] será ignorada");
			}
		}

		// Lista de firmantes
		// ID_LIST = {NIF;NIF;...}
		String nifList = newRequest.getLoginFirmante();
		List<PfUsersDTO> signers = new ArrayList<PfUsersDTO>();
		if (nifList == null || nifList.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
			throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E03_UNRECOGNIZED_SIGNER + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null request signer", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E03_UNRECOGNIZED_SIGNER);
		} else {
			String[ ] nifArray = nifList.split(";");
			if (nifArray != null && nifArray.length > 0) {
				int index = 0;
				for (int i = 0; i < nifArray.length; i++) {
					PfUsersDTO signerDTO = (PfUsersDTO) userAdmBO.getUserByDni(nifArray[i]);
					if (signerDTO == null) {
						throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E03_UNRECOGNIZED_SIGNER + DocelwebConstants.FAULT_CODE_SEPARATOR + "Requested signer not found with ID [" + nifArray[i] + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E03_UNRECOGNIZED_SIGNER);
					} else {
						if (!signers.contains(signerDTO)) {
							signers.add(index, signerDTO);
							index++;
						} else {
							throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "The signer [" + nifArray[i] + "] is already included in the signature line. Can only appear once in the signature line", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
						}
					}
				}
			}
		}

		/*
		   Si se envía un documento firmado y un solo firmante, no deja indicar serie o paralelo.
		   Nuestro Portafirmas ya lo tendrá en cuenta: no validamos la multifirma
		*/
//		String multisignType = newRequest.getFirmaMultiple();
//		if (multisignType == null || multisignType.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
//			throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E09_UNSUPPORTED_MULTISIGN + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null multisign type", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E09_UNSUPPORTED_MULTISIGN);
//		} else {
//			if (!multisignType.equalsIgnoreCase(DocelwebConstants.MULTISIGN_NO) && !multisignType.equalsIgnoreCase(DocelwebConstants.MULTISIGN_SERIES) && !multisignType.equalsIgnoreCase(DocelwebConstants.MULTISIGN_PARALLEL)) {
//				throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E09_UNSUPPORTED_MULTISIGN + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unsupported multisign type [" + multisignType + "]. Supported types {NO, SERIE, PARALELO}", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E03_UNRECOGNIZED_SIGNER);
//			} else if (multisignType.equalsIgnoreCase(DocelwebConstants.MULTISIGN_NO) && signers.size() > 1) {
//				throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "The request contains multiple signers but the multisign type is [NO].", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
//			} else if (!multisignType.equalsIgnoreCase(DocelwebConstants.MULTISIGN_NO) && signers.size() == 1) {
//				throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "The request contains only one signer but the multisign type is [" + multisignType + "].", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
//			}
//		}

		String priority = newRequest.getPrioridad();
		if (priority == null || priority.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
			throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null priority", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		} else {
			if (!priority.equalsIgnoreCase(DocelwebConstants.PRIORITY_HIGH) && !priority.equalsIgnoreCase(DocelwebConstants.PRIORITY_NORMAL)) {
				throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unsupported priority type [" + priority + "]. Supported types {A: high, N: normal}", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
			}
		}

		return signers;
	}

	/**
	 * Checks the document registration parameters.
	 * @param docParam The document registration params
	 * @throws RegistrarDocumentoFault
	 */
	private void checkDocumentRegistrationParams(TODocumento document) throws RegistrarDocumentoFault {
		String docDescription = document.getDescripcion();
		if (docDescription == null || docDescription.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
			throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null document description", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		}

		String docType = document.getTipoDocumento();
		if (docType == null || docType.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
			throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null document type", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		} else {
			if (!docType.equalsIgnoreCase(DocelwebConstants.ELECTRONIC_DOC) && !docType.equalsIgnoreCase(DocelwebConstants.DISPOSITION_DOC) && !docType.equalsIgnoreCase(DocelwebConstants.PAPER_DOC)) {
				throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unsupported document type [" + docType + "]. Supported types {E, U, P}", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
			}
		}

		String reqSign = document.getNecesitaFirma();
		if (reqSign == null || reqSign.equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
			throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null document type", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		} else {
			if (!reqSign.equalsIgnoreCase(DocelwebConstants.YES) && !reqSign.equalsIgnoreCase(DocelwebConstants.NO)) {
				throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unsupported boolean value for required sign param [" + reqSign + "]. Supported types {S, N}", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
			} else {
				if (docType.equalsIgnoreCase(DocelwebConstants.PAPER_DOC) && reqSign.equalsIgnoreCase(DocelwebConstants.YES)) {
					throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Tha paper type document can´t be signed, but the required signed flag is enabled.", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
				}
			}
		}

		// byte[ ] docContent = document.getContenido();
		byte[ ] docContent = document.getContenido();
		if (docType.equalsIgnoreCase(DocelwebConstants.ELECTRONIC_DOC)) {
			if (docContent == null || new String(docContent).equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
				throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E06_VOID_DOC_CONTENT + DocelwebConstants.FAULT_CODE_SEPARATOR + "Void document content received. The document content is mandatory for electronic document type (E type)", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E06_VOID_DOC_CONTENT);
			}
		} else if (docType.equalsIgnoreCase(DocelwebConstants.DISPOSITION_DOC)) {
			if (docContent == null || new String(docContent).equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
				throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E06_VOID_DOC_CONTENT + DocelwebConstants.FAULT_CODE_SEPARATOR + "Void document content received. The document content is mandatory and must be a URL for disposition/URL (U type)", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E06_VOID_DOC_CONTENT);
			}
			try {
				new URL(new String(docContent));
			} catch (MalformedURLException e) {
				throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + e.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E04_OTHER_ERROR);
			}
		} else if (docType.equalsIgnoreCase(DocelwebConstants.PAPER_DOC)) {
			if (docContent != null) {
				log.warn("Ignored document content for paper document type (P type)");
			}
		}
	}

	/**
	 * Checks the document registration parameters.
	 * @param requestDTO The document registration params
	 * @throws RegistrarSolicitudFault
	 */
	private void checkRequestRegistrationParams(PfDocelwebRequestSpfirmaDTO requestDTO) throws RegistrarSolicitudFault {
		if (!requestDTO.getdState().equalsIgnoreCase(DocelwebConstants.REQUEST_STATE_NEW)) {
			throw new RegistrarSolicitudFault(DocelwebConstants.CODE_E08_RETRY_REQUEST + DocelwebConstants.FAULT_CODE_SEPARATOR + "Request already registered. The request state is [" + requestDTO.getdState() + "]. But must be [N]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E08_RETRY_REQUEST);
		}

		if (requestDTO.getdPriority() == null || requestDTO.getdPriority().equals(DocelwebConstants.VOID_CONTENT_PARAM)) {
			throw new RegistrarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Null priority param", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		}
	}

	/**
	 * Checks if the registration request has any document.
	 * @param transactionId The transaction ID
	 * @param documentIdList The document list
	 * @throws SolicitudFault
	 */
	private void checkDocumentListToSign(PfDocelwebRequestSpfirmaDTO docelRequestDTO, List<Long> documentIdList) throws SolicitudFault {
		if (documentIdList == null || documentIdList.isEmpty()) {
			log.error("System registration request for transactionID [" + docelRequestDTO.getPrimaryKeyString() + "] is invalid");
			throw new SolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "The registration request with transactionID [" + docelRequestDTO.getPrimaryKeyString() + "] has no documents", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		}

		Set<PfDocelwebDocumentDTO> registeredDocSet = docelRequestDTO.getPfDocuments();
		List<Long> registerdDocIdList = new ArrayList<Long>();
		for (Iterator<PfDocelwebDocumentDTO> iterator = registeredDocSet.iterator(); iterator.hasNext();) {
			PfDocelwebDocumentDTO pfDocelwebDocumentDTO = iterator.next();
			registerdDocIdList.add(pfDocelwebDocumentDTO.getPrimaryKey());
		}
		for (Iterator<Long> iterator = documentIdList.iterator(); iterator.hasNext();) {
			Long docId = iterator.next();
			if (!registerdDocIdList.contains(docId)) {
				log.error("System registration request for transactionID [" + docelRequestDTO.getPrimaryKeyString() + "] is invalid");
				throw new SolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "The requested documents IDs in transactionID [" + docelRequestDTO.getPrimaryKeyString() + "] missmatch with the registered documents. Unregistered document ID [" + docId + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
			}
		}
		if (registerdDocIdList.size() > documentIdList.size()) {
			log.error("System registration request for transactionID [" + docelRequestDTO.getPrimaryKeyString() + "] is invalid");
			throw new SolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Missing documents registered in transactionID [" + docelRequestDTO.getPrimaryKeyString() + "], requested documents size [" + documentIdList.size() + "], registered documents size [" + registerdDocIdList.size() + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		}
	}

	// FIN COMPROBACIONES POR MÉTODO WS

	// INICIO UTILIDADES

	/**
	 * Query the generica user data to use as remitter.
	 * @return The generic user remitter DTO
	 */
	private PfUsersDTO queryGenericUser() {
		if (genericUserRemitter == null) {
			genericUserRemitter = (PfUsersDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_GENERIC_USER, null, null);
		}
		return genericUserRemitter;
	}

	/**
	 * Gets the body request text, with de system amanger and remitter info.
	 * @param systemManager The system manager
	 * @param remitter The remitter name and surnames.
	 * @param docList The document list info
	 * @param deadline The limit date info. Can be null
	 * @return The body request
	 */
	private String getRequestBody(String systemManager, String remitter, List<PfDocelwebDocumentDTO> docList, Date deadline) {
		String bodyRequest = "<strong>SOLICITUD DE INTERFAZ GENÉRICA</strong></BR></BR>";
		bodyRequest += "<strong>Sistema de gestión:</strong> " + systemManager + "</BR>";
		bodyRequest += "<strong>Remitente:</strong> " + remitter + "</BR>";
		if (deadline != null) {
			bodyRequest += "<strong>Fecha límite:</strong> " + date.dateToString(deadline, DateComponent.EXTENDED_DATE_FORMAT) + "</BR>";
		}
		bodyRequest += "</BR>";
		bodyRequest += "<strong>DOCUMENTOS (E: Documento electrónico, U: Puesta a disposición, P: Documento en papel)</strong></BR></BR>";
		int index = 1;
		for (Iterator<PfDocelwebDocumentDTO> iterator = docList.iterator(); iterator.hasNext();) {
			PfDocelwebDocumentDTO pfDocelwebDocumentDTO = iterator.next();
			bodyRequest += "<strong>Documento" + index + ": </strong>";
			bodyRequest += pfDocelwebDocumentDTO.getdDescription() + " [Tipo: " + pfDocelwebDocumentDTO.getdDocelType() + "] [Firmable: " + (pfDocelwebDocumentDTO.getdReqSign()?"Sí":"No") + "]</BR>";
			index++;
		}
		return bodyRequest;
	}

	/**
	 * Method to get the signers login identifiers from a Pfirma signer list.
	 * @param pfSigners The Pfirma signer list
	 * @return The signers login sequence string separated by ';'
	 */
	private String getSignersLogin(List<PfUsersDTO> pfSigners) {
		String signersLoginStr = new String();
		for (Iterator<PfUsersDTO> iterator = pfSigners.iterator(); iterator.hasNext();) {
			PfUsersDTO pfUsersDTO = iterator.next();
			signersLoginStr += pfUsersDTO.getCidentifier();
			if (iterator.hasNext()) {
				signersLoginStr += ";";
			}
		}
		return signersLoginStr;
	}

	/**
	 * Method to get the pfirma revision state request for each signer.
	 * @param pfSigners The Pfirma request
	 * @return The pfirma revision state request separated by ';' for each signer
	 */
	private String getSignersRevisionState(PfRequestsDTO pfRequest) {
		String revisionsStates = new String();
		if (pfRequest != null) {
			Set<PfRequestTagsDTO> signersRevisionStateTagList = pfRequest.getPfRequestsTags();
			for (Iterator<PfRequestTagsDTO> iterator = signersRevisionStateTagList.iterator(); iterator.hasNext();) {
				PfRequestTagsDTO pfRequestTagsDTO = iterator.next();
				if (pfRequestTagsDTO.getPfTag() != null) {
					revisionsStates += pfRequestTagsDTO.getPfUser().getCidentifier() + ":" + pfRequestTagsDTO.getPfTag().getCtag();
				}
				if (iterator.hasNext()) {
					revisionsStates += ";";
				}
			}
		}
		return revisionsStates;
	}

	/**
	 * Method to insert a priority level in a Pfirma request from DOCEL priority level.
	 * @param requestDTO Pfirma Request
	 * @param importanceLevel DOCEL priority level
	 * @throws RegistrarSolicitudFault
	 */
	private void insertImportanceLevel(PfRequestsDTO requestDTO, String importanceLevel) throws RegistrarSolicitudFault {
		PfImportanceLevelsDTO level = null;
		if (importanceLevel.equalsIgnoreCase(DocelwebConstants.PRIORITY_HIGH)) {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_PRIORITY_TYPE_CODE, DocelwebConstants.QUERY_PARAM_PRIORITY_CODE, "2");
		} else if (importanceLevel.equalsIgnoreCase(DocelwebConstants.PRIORITY_NORMAL)) {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_PRIORITY_NORMAL, null, null);
		} else {
			throw new RegistrarSolicitudFault(DocelwebConstants.CODE_E04_OTHER_ERROR + DocelwebConstants.FAULT_CODE_SEPARATOR + "Usupported priority [" + importanceLevel + "]. Supported values {A, N}", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E04_OTHER_ERROR);
		}
		requestDTO.setPfImportance(level);
	}

	/**
	 * Gets the DOCEL document sign if exists
	 * @param docelDocumentDTO The DOCEL document
	 * @return The pfirma sign DTO
	 */
	private PfSignsDTO getDocelDocumentSign(PfDocelwebDocumentDTO docelDocumentDTO) {
		PfSignsDTO pfSignDTO = null;
		PfDocumentsDTO pfDocument = docelDocumentDTO.getPfDoc();
		if (pfDocument != null) {
			if (pfDocument.getPfSigns() != null && !pfDocument.getPfSigns().isEmpty()) {
				for (Iterator<PfSignsDTO> iterator = pfDocument.getPfSigns().iterator(); iterator.hasNext();) {
					pfSignDTO = (PfSignsDTO) iterator.next();
					// Si tiene varias nos quedamos solo con la primera.
					// En principio los documentos DOCEL firmados solo
					// contendrán una firma
					break;
				}
			}
		}
		return pfSignDTO;
	}

	/**
	 * Change the DOCEL request status to the given status
	 * @param docelRequestDTO The DOCEL request DTO
	 * @param state The new status param
	 */
	public void changeDocelRequestStatus(PfDocelwebRequestSpfirmaDTO docelRequestDTO, String state) {
		docelRequestDTO.setdState(state);
		baseDAO.insertOrUpdate(docelRequestDTO);
	}

	/**
	 * Change the DOCEL request status to the given status
	 * @param selectedRequestDTO The pfirma selected request DTO
	 * @param state The new status param
	 * @return The DOCEL request modified.
	 */
	public PfDocelwebRequestSpfirmaDTO changeDocelRequestStatus(PfRequestsDTO selectedRequestDTO, String state) {
		String cHash = selectedRequestDTO.getChash();
		PfDocelwebRequestSpfirmaDTO docelRequestDTO = (PfDocelwebRequestSpfirmaDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_DOCEL_PFIRMA_REQUEST_BY_HASH, DocelwebConstants.QUERY_PARAM_FILE_HASH, cHash);
		if (docelRequestDTO != null) {
			changeDocelRequestStatus(docelRequestDTO, state);
		}
		return docelRequestDTO;
	}

	/**
	 * Static method to get the MTOM attachment content
	 * @param dh The data handler with the content
	 * @return The content in byte[ ]
	 * @throws IOException
	 */
	public static byte[ ] getBytesFromDataHandler(DataHandler dh) throws IOException {
		byte[ ] docContent = null;
		if (dh != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			dh.writeTo(baos);
			docContent = baos.toByteArray();
		}
		return docContent;
	}

	/**
	 * Static method to set the MTOM atachment content 
	 * @param byteArray The byte[ ] content
	 * @return The DataHandler
	 */
	public static DataHandler getDataHandlerFromByteArray(byte[ ] byteArray) {
		DataHandler dh = null;
		if (byteArray != null) {
			DataSource ds = new ByteArrayDataSource(byteArray, DocelwebConstants.MTOM_MIME_TYPE);
			dh = new DataHandler(ds);
		}
		return dh;
	}

	/**
	 * Metodo que devuelve el ultimo comentario asociado a una peticion.
	 * @param req Peticion.
	 * @return Ultimo comentario de la peticion.
	 */
	private PfCommentsDTO getLastComment(PfRequestsDTO req) {
		log.info("getLastComment init");
		PfCommentsDTO comment = null;
		// Obtain last comment from request
		if (req.getPfComments() != null && !req.getPfComments().isEmpty()) {
			for (PfCommentsDTO commentDTO : req.getPfComments()) {
				if (comment == null	||
					commentDTO.getPrimaryKey().intValue() > comment.getPrimaryKey().intValue()) {
					comment = commentDTO;
				}
			}
		}
		log.info("getLastComment end");
		return comment;
	}
}
