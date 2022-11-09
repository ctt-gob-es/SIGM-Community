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

//package es.seap.minhap.portafirmas.business.ws.legacy.remotesign;
//
//import java.io.ByteArrayOutputStream;
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.apache.commons.configuration.CompositeConfiguration;
//import org.apache.commons.configuration.Configuration;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import es.guadaltel.framework.signer.SignDocument;
//import es.guadaltel.framework.signer.SignTransaction;
//import es.guadaltel.framework.signer.impl.util.ConstantsSigner;
//import es.juntadeandalucia.cice.pfirma.factory.PfSignFactory;
//import es.juntadeandalucia.cice.pfirma.sign.PfSign;
//import es.seap.minhap.portafirmas.business.SignBO;
//import es.seap.minhap.portafirmas.business.TagBO;
//import es.seap.minhap.portafirmas.dao.BaseDAO;
//import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
//import es.seap.minhap.portafirmas.domain.PfBlockSignsDTO;
//import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
//import es.seap.minhap.portafirmas.domain.PfSignsDTO;
//import es.seap.minhap.portafirmas.utils.AuthenticatorConstants;
//import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
//import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
//import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
//import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
//import es.seap.minhap.portafirmas.ws.legacy.exception.FirmaRemotaException;
//
//// TODO: Adaptar excepciones FacesException
//@Service
//public class RemoteSignWSLegacyBO implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	private Map<String, RemoteSignDocument> documents;
//
//	@Autowired
//	private TagBO tagBO;
//
//	@Autowired
//	private SignBO signBO;
//
//	@Autowired
//	private BaseDAO baseDAO;
//
//	@Autowired
//	private CustodyServiceFactory custodyServiceFactory;
//
//	// TODO: Migrar a quartz
//	private static RemoteSignDaemon daemon;
//
//	private Logger log = Logger.getLogger(RemoteSignWSLegacyBO.class);
//
//	private class RemoteSignDaemon extends TimerTask {
//		public void run() {
//			log.debug("RemoteSignDaemon: " + daemon + ".");
//			checkDocuments();
//		}
//	}
//
//	/**
//	 * M&eacute;todo que comprueba la caducidad de la lista de documentos, eliminando los caducados.
//	 * @return "True"
//	 */
//	private boolean checkDocuments() {
//		synchronized (documents) {
//			log.info("Init checking documents ...");
//			if (documents != null && documents.size() > 0) {
//				log.debug("Found " + documents.size()
//						+ " unresolved documents.");
//				Iterator<RemoteSignDocument> i = documents.values().iterator();
//				RemoteSignDocument documento = null;
//				while (i.hasNext()) {
//					documento = (RemoteSignDocument) i.next();
//
//					Calendar calAhora = Calendar.getInstance();
//					calAhora.setTimeInMillis(System.currentTimeMillis());
//
//					Calendar calInicio = Calendar.getInstance();
//					calInicio.setTimeInMillis(documento.getInitSignDate().getTime());
//
//					calInicio.add(Calendar.HOUR, 2);
//					// calInicio.add(Calendar.SECOND, 200); //test
//
//					log.debug("Checking time");
//					if (calInicio.before(calAhora)) {
//						log.debug("Removing document: " + documento.getHash());
//						documents.remove(documento.getHash());
//					}
//				}
//				if (documents.size() == 0) {
//					log.debug("Not unresolved documents, stopping daemon...");
//					daemon.cancel();
//					log.debug("RemoteSignDaemon STOP: " + daemon);
//				}
//			} else {
//				log.debug("Not unresolved documents.");
//			}
//		}
//		return true;
//	}
//
//	public RemoteSignWSLegacyBO() {
//		documents = Collections.synchronizedMap(new HashMap<String, RemoteSignDocument>());
//	}
//
//	/**
//	 * M&eacute;todo que inicializa el proceso de firma remota.
//	 * @param hashDoc Hash del documento a firmar.
//	 * @param anagram Anagrama que contiene el ID del usuario firmante.
//	 * @return Transacci&oacute;n de firma.
//	 * @throws FirmaRemotaException
//	 */
//	@Transactional
//	public String initSign(String hashDoc, String anagram)
//			throws FirmaRemotaException {
//		log.info("initSign init: " + hashDoc + ", " + anagram);
//		try {
//			log.debug("Obtain id from anagram: " + anagram);
//			String idUser = "";
//			if (anagram != null && anagram.length() > 8) {
//				idUser = anagram.substring(0, 9);
//			} else {
//				throw new FirmaRemotaException("Anagram not valid");
//			}
//			log.debug("Id obtained:" + idUser);
//
//			if (hashDoc == null || "".equals(hashDoc)) {
//				throw new FirmaRemotaException("Hash not valid");
//			}
//			if (existDocument(hashDoc)) {
//				log.debug("Document " + hashDoc + " already registered.");
//
//				RemoteSignDocument doc = getDocument(hashDoc);
//				if (doc.getIdUser().equals(idUser)) {
//					return doc.getDataToSign();
//				} else {
//					throw new FirmaRemotaException(
//							"Document is being signed by other user");
//				}
//			} else {
//				PfDocumentsDTO docDTO = checkDocument(hashDoc, idUser);
//
//				log.debug("Download document ...");
//				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
//				CustodyServiceOutput custodyService = custodyServiceFactory
//						.createCustodyServiceOutput(docDTO.getPfFile()
//								.getCtype());
//				CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
//				custodyDocument.setIdentifier(docDTO.getChash());
//				custodyDocument.setUri(docDTO.getPfFile().getCuri());
//				custodyService.downloadFile(custodyDocument, byteArray);
//				byte[] file = byteArray.toByteArray();
//
//				if (file == null)
//					throw new FirmaRemotaException("Document not found");
//				log.debug("Document found");
//
//				log.debug("Obtain information sign service ...");
//				Configuration conf = loadSignProperties(hashDoc);
//				PfSign signer = createSignService(conf);
//
//				log.debug("Query last document sign");
//				List<AbstractBaseDTO> signs = baseDAO.queryListOneParameter(
//						"request.signsOrderedDateReport", "hashDoc", hashDoc);
//
//				String transactionId = "";
//				List<String> transationsList = new ArrayList<String>();
//				SignTransaction signTransaction = new SignTransaction();
//				HashMap<Object, Object> multiSign = null;
//
//				// Sign not exist or block sign not found
//				if (signs.isEmpty()
//						|| (!signs.isEmpty() && ((PfSignsDTO) signs.get(0))
//								.getPfBlockSigns().isEmpty())) {
//
//					log.debug("Register document init");
//					SignDocument signDocument = new SignDocument();
//					signDocument.setContent(file);
//					signDocument.setMimeType(docDTO.getDmime());
//					signDocument.setName(docDTO.getDname());
//					transactionId = signer.registerDocument(signDocument);
//					signTransaction.addDocument(signDocument);
//					transationsList.add(transactionId);
//					log.debug("Register end:" + transactionId);
//				} else {
//					log.debug("Obtain sign transaction");
//					PfSignsDTO sign = (PfSignsDTO) signs.get(0);
//
//					Long idTransactionBlock = Long
//							.valueOf(((PfBlockSignsDTO) sign.getPfBlockSigns()
//									.toArray()[0]).getPfBlock()
//									.getCtransaction());
//
//					multiSign = new HashMap<Object, Object>();
//
//					List<Long> idDocs = new ArrayList<Long>();
//					idDocs.add(Long.valueOf(sign.getCtransaction()));
//					multiSign.put(idTransactionBlock, idDocs);
//				}
//
//				log.debug("Second phase init");
//				signer.initSignBlockThreePhases(signTransaction,
//						transationsList, multiSign);
//				log.debug("Second phase end");
//
//				// complete document
//				RemoteSignDocument document = createRemoteSignDocument(hashDoc,
//						idUser, signTransaction);
//
//				// add to list
//				addDocument(hashDoc, document);
//				log.debug("Document add to unresolved list");
//
//				log.debug("call start daemon");
//				startDaemon();
//
//				log.debug("init sign end");
//				return signTransaction.getHash();
//			}
//		} catch (Exception e) {
//			log.error("Error in init sign.", e);
//			throw new FirmaRemotaException("Error en iniciar firma: "
//					+ e.getMessage());
//		}
//	}
//
//	//////////// ESTE MÉTODO SE COMENTA POR QUE YA NO SE UTILIZA Y ES INCOMPATIBLE CON LA MODIFICACIÓN /////////
//	//////////// NECESARIA PARA CORREGIR EL PROBLEMA DE LAS LÍNEAS DE FIRMA CIRCULARES                 /////////
//	
////	/**
////	 * M&eacute;todo que finaliza el proceso de firma remota.
////	 * @param hashDoc Hash del documento a firmar.
////	 * @param signData Datos a firmar.
////	 * @param anagram Anagrama que contiene el ID del firmante.
////	 * @param stateTags Etiquetas de estado.
////	 * @return Identificador de la transacci&oacute;n de firma.
////	 * @throws FirmaRemotaException
////	 */
////	@Transactional
////	public double endSign(String hashDoc, String signData, String anagram,
////			Map<String, PfTagsDTO> stateTags) throws FirmaRemotaException {
////		log.info("endSign init: " + hashDoc + ", " + anagram);
////		try {
////			String idUser = "";
////			if (anagram != null && anagram.length() > 8) {
////				idUser = anagram.substring(0, 9);
////			} else {
////				throw new FirmaRemotaException("Anagram not valid");
////			}
////			log.debug("Id obtained:" + idUser);
////
////			if (hashDoc == null || "".equals(hashDoc)) {
////				throw new FirmaRemotaException("Hash not valid");
////			}
////			if (existDocument(hashDoc)) {
////				RemoteSignDocument doc = getDocument(hashDoc);
////				if (!doc.getIdUser().equals(idUser)) {
////					throw new FirmaRemotaException(
////							"This document is being signed by other user");
////				}
////
////				PfDocumentsDTO docDTO = checkDocument(hashDoc, idUser);
////
////				// get document
////				log.debug("Getting document ...");
////				String idTransaction = getDocument(hashDoc).getIdTransaction();
////				log.debug("Document: " + hashDoc + " getted.");
////
////				log.debug("Obtain information sign service ...");
////				Configuration conf = loadSignProperties(hashDoc);
////				PfSign signer = createSignService(conf);
////
////				log.debug("Completing sign ...");
////				SignTransaction signTransaction = new SignTransaction();
////				signTransaction.setId(idTransaction);
////				String certificate = signer.validateSignature(signData);
////
////				log.debug("Checking user");
////				Map<String, String> dniSign = signer
////						.validateCertificate(certificate);
////				if (dniSign == null) {
////					throw new FirmaRemotaException(
////							"El certificado no es v&aacute;lido");
////
////				} else {
////					String dniSignString = dniSign
////							.get(SignerDataConstants.DNI_FIELD);
////
////					if (!dniSignString.equals(idUser)) {
////
////						throw new FirmaRemotaException(
////								"El anagrama no coincide con el de la firma:"
////										+ idUser + ", " + dniSign);
////					}
////				}
////
////				log.debug("Completing sign ... ");
////				signer.completeSignBlockThreePhases(signTransaction, signData,
////						certificate);
////				log.debug("Completing sign ... ok");
////
////				log.debug("Removing document from list");
////				removeDocument(hashDoc);
////
////				log.debug("Querying user");
////				PfUsersDTO userDTO = (PfUsersDTO) baseDAO
////						.queryElementOneParameter("request.usersDni", "dni",
////								idUser);
////
////				List<PfSignsDTO> serverSigns = new ArrayList<PfSignsDTO>();
////				PfSignsDTO signServer = new PfSignsDTO();
////				signServer.setPfDocument(docDTO);
////				signServer.setCtransaction(signTransaction.getId());
////				serverSigns.add(signServer);
////
////				log.debug("Insert block sign");
////				PfBlocksDTO blockDTO = signBO.insertBlock(signTransaction,
////						signData, conf, userDTO, serverSigns);
////
////				log.debug("Query sign info to service");
////				Map<String, SignerBlock> signs = signer
////						.getCompleteInformationSignaturesBlockRequest(signTransaction);
////
////				log.debug("Insert sign");
////				signBO.insertSign(blockDTO, serverSigns, userDTO, null, signs, conf, false, false);
////
////				// boolean signRequest = checkRequestSigns(idUser, docDTO);
////
////				// if (signRequest)
////				{
////					log.debug("Request sign complete");
////					tagBO.changeStateToSigned(docDTO.getPfRequest(), userDTO, false);
////				}
////
////				log.info("End sign end");
////				return Long.parseLong(idTransaction);
////			} else {
////				throw new FirmaRemotaException("Init sign not realized");
////			}
////		} catch (Exception e) {
////			log.error("Error in end sign.", e);
////			throw new FirmaRemotaException("Error en finalizar firma: "
////					+ e.getMessage());
////		}
////	}
//
//	/**
//	 * M&eacute;todo que comprueba si un documento existe.
//	 * @param hashDoc Hash del documento.
//	 * @return "True" si existe, "false" en caso contrario.
//	 */
//	private boolean existDocument(String hashDoc) {
//		log.info("existDocument init");
//		synchronized (documents) {
//			if (documents != null && documents.size() > 0) {
//				return documents.containsKey(hashDoc);
//			}
//			return false;
//		}
//	}
//
//	/**
//	 * M&eacute;todo que a&ntilde;ade un documento a la lista.
//	 * @param hashDoc hash del documento.
//	 * @param documento Firma remota de un documento.
//	 */
//	private void addDocument(String hashDoc, RemoteSignDocument documento) {
//		log.info("addDocument init");
//		synchronized (documents) {
//			documents.put(hashDoc, documento);
//		}
//		log.info("addDocument end");
//	}
//
//	/**
//	 * M&eacute;todo que obtiene la firma remota de un documento.
//	 * @param hashDoc hash del documento.
//	 * @return Firma remota del documento.
//	 */
//	private RemoteSignDocument getDocument(String hashDoc) {
//		log.info("getDocument init");
//		synchronized (documents) {
//			return (RemoteSignDocument) documents.get(hashDoc);
//		}
//	}
//
//	/**
//	 * M&eacute;todo que elimina un documento de la lista.
//	 * @param hashDoc Hash del documento.
//	 */
////	private void removeDocument(String hashDoc) {
////		log.info("removeDocument init");
////		synchronized (documents) {
////			documents.remove(hashDoc);
////		}
////		log.info("removeDocument end");
////	}
//
//	/**
//	 * M&eacute;todo que arranca el demonio de firma remota.
//	 */
//	private void startDaemon() {
//		log.info("start daemon init");
//		synchronized (documents) {
//			if (documents.size() == 1) {
//				log.debug("Document firt, start daemon...");
//				Timer timer = new Timer();
//				daemon = new RemoteSignDaemon();
//				timer.schedule(daemon, 0, 1000000);
//				// timer.schedule(daemon, 0, 10000); //test
//				log.debug("RemoteSignDaemon START: " + daemon);
//			}
//		}
//		log.info("start daemon end");
//	}
//
//	/**
//	 * M&eacute;todo que carga las propiedades de configuraci&oacute;n de la firma de un documento.
//	 * @param hashDoc Hash del documento a firmar.
//	 * @return Configuraci&oacute;n de la firma.
//	 * @throws FacesException
//	 */
//	private Configuration loadSignProperties(String hashDoc) { //throws FacesException {
//		Configuration conf = new CompositeConfiguration();
//
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("cparam", "FIRMA.%");
//		parameters.put("chash", hashDoc);
//		List<AbstractBaseDTO> configurationParameters = baseDAO
//				.queryListMoreParameters("request.signConfigParamDoc", parameters);
//		// Build Properties object for authenticator
//		// Load map to get parameters and values
//		Map<String, String> auxMap = ConfigurationUtil.convierteListaParametrosConfiguracionEnMapa(configurationParameters);
//		if (auxMap.containsValue(null) || auxMap.containsValue("")) {
////			throw new FacesException("Error loading authenticator parameters");
//		} else {
//
//			conf.addProperty(ConstantsSigner.AFIRMA5_SERVER_CERT_ALIAS, auxMap
//					.get(AuthenticatorConstants.PARAM_SERVER_CERT_ALIAS));
//
//			conf.addProperty(ConstantsSigner.AFIRMA5_APPLICATION, auxMap
//					.get(AuthenticatorConstants.PARAM_APPLICATION));
//			conf.addProperty(ConstantsSigner.AFIRMA5_TRUSTSTORE, auxMap
//					.get(AuthenticatorConstants.PARAM_AFIRMA5_TRUSTEDSTORE));
//			conf.addProperty(ConstantsSigner.AFIRMA5_ENDPOINT, auxMap
//					.get(AuthenticatorConstants.PARAM_AFIRMA5_URL));
//			conf.addProperty(ConstantsSigner.AFIRMA5_HASH_ALGORITH, auxMap
//					.get(AuthenticatorConstants.PARAM_HASH_ALGORITHM));
//			conf.addProperty(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT, auxMap
//					.get(AuthenticatorConstants.PARAM_SIGNATURE_FORMAT));
//
//			conf.addProperty(AuthenticatorConstants.AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE_PASS, auxMap
//									.get(AuthenticatorConstants.PARAM_AFIRMA5_TRUSTEDSTORE_PASS));
//			conf.addProperty(
//					AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_USER,
//					auxMap.get(AuthenticatorConstants.PARAM_SERVER_HTTP_USER));
//			conf.addProperty(
//					AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_PASS,
//					auxMap.get(AuthenticatorConstants.PARAM_SERVER_HTTP_PASS));
//
//		}
//		return conf;
//	}
//
//	/**
//	 * M&eacute;todo que comprueba si un documento existe y no ha sido firmado.
//	 * @param hashDoc hash del documento.
//	 * @param idUser identificador del usuario.
//	 * @return Documento.
//	 * @throws FirmaRemotaException Error en firma remota.
//	 */
//	private PfDocumentsDTO checkDocument(String hashDoc, String idUser)
//			throws FirmaRemotaException {
//		log.debug("Checking state document ...");
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("chash", hashDoc);
//		parameters.put("user", idUser);
//		PfDocumentsDTO docDTO = (PfDocumentsDTO) baseDAO
//				.queryElementMoreParameters("request.documentStateHash", parameters);
//		if (docDTO == null) {
//			throw new FirmaRemotaException(
//					"Document not exist or not correct state.");
//		}
//		log.debug("Correct state ");
//
//		log.debug("Checking sign document ...");
//		Long signOk = baseDAO.queryCount("request.documentSignHash", parameters);
//		if (signOk > 0) {
//			throw new FirmaRemotaException("Document already sign.");
//		}
//		log.debug("Correct sign ");
//		return docDTO;
//	}
//
//	/**
//	 * M&eacute;todo que crea el servicio de firma remota.
//	 * @param conf Configuraci&oacute;n del servicio de firma.
//	 * @return Servicio de firma.
//	 * @throws FirmaRemotaException
//	 */
//	private PfSign createSignService(Configuration conf)
//			throws FirmaRemotaException {
//		PfSign signer = PfSignFactory.createPfSign(conf, "afirma5",
//						conf.getString(AuthenticatorConstants.AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE_PASS),
//						conf.getString(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_USER),
//						conf.getString(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_PASS));
//
//		if (signer == null) {
//			throw new FirmaRemotaException("Sign service not inited.");
//		}
//		log.debug("Info sign service... ok");
//		return signer;
//	}
//
//	/**
//	 * M&eacute;todo que crea un objeto de documento firmado remotamente.
//	 * @param hashDoc Hash del documento.
//	 * @param idUser Identificador del firmante.
//	 * @param signTransaction Identificador de la transacci&oacute;n de firma.
//	 * @return Documento firmado remotamente.
//	 */
//	private RemoteSignDocument createRemoteSignDocument(String hashDoc,
//			String idUser, SignTransaction signTransaction) {
//		RemoteSignDocument document = new RemoteSignDocument();
//		document.setDataToSign(signTransaction.getHash());
//		document.setInitSignDate(new Timestamp(System.currentTimeMillis()));
//		document.setHash(hashDoc);
//		document.setIdTransaction(signTransaction.getId());
//		document.setIdUser(idUser);
//		return document;
//	}
//
//	/**
//	 * M&eacute;todo que comprueba que todos los documentos de una petici&oacute;n han sido firmados por el firmante.
//	 * @param idUser Identificador del firmante.
//	 * @param docDTO Documento.
//	 * @return "True" si han sido firmados, "false" en caso contrario.
//	 */
////	private boolean checkRequestSigns(String idUser, PfDocumentsDTO docDTO) {
////		log.debug("Checking signs request");
////		log.debug("Change state and save history");
////		Map<String, Object> paramDoc = new HashMap<String, Object>();
////		paramDoc.put("chash", docDTO.getPfRequest().getChash());
////		Long docReqs = baseDAO.queryCount("request.documentsRequestCount", paramDoc);
////		paramDoc.put("user", idUser);
////		Long signDocs = baseDAO.queryCount("request.documentsSignCount", paramDoc);
////		return signDocs.compareTo(docReqs) == 0;
////	}
//}