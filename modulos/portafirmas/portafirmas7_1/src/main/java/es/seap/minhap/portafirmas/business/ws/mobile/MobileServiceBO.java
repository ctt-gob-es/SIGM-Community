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

package es.seap.minhap.portafirmas.business.ws.mobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.mail.util.ByteArrayDataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.guadaltel.framework.signer.impl.util.ConstantsSigner;
import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.AuthenticateBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.FireBO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.PassBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.SignBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.business.administration.ApplicationAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.beans.ValidateCertificateResponse;
import es.seap.minhap.portafirmas.business.login.LoginBusinessService;
import es.seap.minhap.portafirmas.business.login.impl.ConstantsClave;
import es.seap.minhap.portafirmas.business.ws.CertificateBO;
import es.seap.minhap.portafirmas.business.ws.EEUtilMiscBO;
import es.seap.minhap.portafirmas.business.ws.EEUtilUtilFirmaBO;
import es.seap.minhap.portafirmas.business.ws.mobile.json.Response;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.exceptions.BlockRequestException;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.exceptions.InvalidSignException;
import es.seap.minhap.portafirmas.exceptions.TimeStampingException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.MimeType;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.utils.previsualizacion.PrevisualizacionBO;
import es.seap.minhap.portafirmas.web.beans.fire.FireDocument;
import es.seap.minhap.portafirmas.web.beans.fire.FireRequest;
import es.seap.minhap.portafirmas.web.beans.fire.FireTransaction;
import es.seap.minhap.portafirmas.web.beans.signature.DocumentSignatureConfig;
import es.seap.minhap.portafirmas.web.converter.UserConverter;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileAccesoClave;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileApplication;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileApplicationList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocSignInfo;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocSignInfoList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocument;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocumentList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileError;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileFireDocument;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileFireDocumentList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileFireRequest;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileFireRequestList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileFireTrasactionResponse;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequest;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequestFilterList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequestList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileSIMUser;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileSIMUserStatus;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileSignLine;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileSignLineList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileStringList;
import es.seap.minhap.portafirmas.ws.mobile.exception.MobileException;
import es.seap.minhap.portafirmas.ws.mobile.util.MobileConstants;
import es.seap.minhap.portafirmas.ws.mobile.util.MobileServiceUtil;
import es.seap.minhap.portafirmas.ws.sim.util.SimConstants;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;

@Service
public class MobileServiceBO {

	Logger log = Logger.getLogger(MobileServiceBO.class);

	@Resource(name="claveMobileProperties")
	private Properties claveMobileProperties;
	
	@Autowired
	private AuthenticateBO authenticateBO;
	
	@Autowired
	private LoginBusinessService loginBusinessService;
	
	@Autowired
	private RequestBO requestBO;

	@Autowired
	private SignBO signBO;

	@Autowired
	private TagBO tagBO;

	@Autowired
	private PassBO passBO;

	@Autowired
	private ApplicationBO appBO;

	@Autowired
	private CertificateBO certificateBO;

	@Autowired
	private EEUtilUtilFirmaBO eeUtilUtilFirmaBO;

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private CustodyServiceFactory custodyServiceFactory;

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private ApplicationAdmBO applicationAdmBO;

	@Autowired 
	private BinaryDocumentsBO binaryDocumentsBO;

	@Autowired
	private DateComponent date;

	@Autowired
	private UtilComponent util;
	
	@Autowired
	private EEUtilMiscBO eeUtilMiscBO;
	
	@Autowired
	private UserAdmBO	userAdmBO;
	
	@Autowired
	private ApplicationVO applicationVO;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private PrevisualizacionBO previsualizacionBO;
	
	@Autowired
	private UserConverter userConverter;
	
	@Autowired
	private FireBO fireBO;
	
	@Transactional(readOnly=false)
	public MobileRequestList queryRequestList(String certificate, String state, String initPage, String sizePage,
			MobileStringList signFormats, MobileRequestFilterList requestFilters) throws MobileException {
		log.debug("EJECUTANDO queryRequestList: " + state);
		List<AbstractBaseDTO> requestTagList;
		MobileRequestList mobileRequestList = null;

		try {
			PfUsersDTO user = obtenerUsuario(certificate);
			PfUsersDTO job = user.getValidJob();

			int pageSize = Integer.parseInt(sizePage);
			int pageActual = Integer.parseInt(initPage);

			// Se preparan los filtros de búsqueda
			String order = MobileServiceUtil.getInstance().getFilterValue(requestFilters, MobileConstants.ORDER_ASC_DESC);

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date initDateFilter = null;
			String initDateString = MobileServiceUtil.getInstance().getFilterValue(requestFilters, MobileConstants.INIT_DATE_FILTER);
			if (initDateString != null && !initDateString.equals("")) {
				initDateFilter = df.parse(initDateString);
			}

			Date endDateFilter = null;
			String endDateString = MobileServiceUtil.getInstance().getFilterValue(requestFilters, MobileConstants.END_DATE_FILTER);
			if (endDateString != null && !endDateString.equals("")) {
				endDateFilter = df.parse(endDateString);
				endDateFilter = date.addDays(endDateFilter, 1);
				endDateFilter = date.addSeconds(endDateFilter, -1);
			}

			String orderAttribute = MobileServiceUtil.getInstance().getFilterValue(requestFilters, MobileConstants.ORDER_ATTRIBUTE_FILTER);
			String searchFilter = MobileServiceUtil.getInstance().getFilterValue(requestFilters, MobileConstants.SEARCH_FILTER);
			String labelFilter = MobileServiceUtil.getInstance().getFilterValue(requestFilters, MobileConstants.LABEL_FILTER);
			String applicationFilter = MobileServiceUtil.getInstance().getFilterValue(requestFilters, MobileConstants.APPLICATION_FILTER);
			String filtroValidadorMovilActivo = userConverter.getUserParameter(user, Constants.FILTRO_VALIDADOR_MOVIL_ACTIVO);
			
			if (!user.getValidadores().isEmpty() && (filtroValidadorMovilActivo == null || Constants.C_YES.equalsIgnoreCase(filtroValidadorMovilActivo))){
				requestTagList = requestBO.queryListPaginated(pageSize, pageActual, orderAttribute, order, user, null,
						searchFilter, labelFilter, applicationFilter, initDateFilter, endDateFilter, state, job, null, true, false, null);
			}else{
				requestTagList = requestBO.queryListPaginated(pageSize, pageActual, orderAttribute, order, user, null,
						searchFilter, labelFilter, applicationFilter, initDateFilter, endDateFilter, state, job, null, false, false, null);
			}
			// Me quedo con las peticiones que se incluyan dentro de la página solicitada
			List<AbstractBaseDTO> subList = new ArrayList<AbstractBaseDTO>();
			int totalSize = requestTagList.size();
			if (pageActual*pageSize < totalSize) {
				subList = requestTagList.subList((pageActual - 1)*pageSize, pageActual*pageSize);
			} else {
				subList = requestTagList.subList((pageActual - 1)*pageSize, totalSize);
			}

			// Convierto la lista de etiquetas petición a peticiones del Portafirmas móvil

			mobileRequestList = new MobileRequestList();
			mobileRequestList.setSize(totalSize);
			for (AbstractBaseDTO requestTag : subList) {
				PfRequestTagsDTO reqTag = (PfRequestTagsDTO) requestTag;

				// Se obtiene la configuración de la petición
				Configuration conf = signBO.loadSignProperties(reqTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey());

				// Se filtran las peticiones por formato de firma
				if (filterBySignFormat(signFormats, conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT))) {
					MobileRequest mobileRequest = convertRequest(reqTag, user, state, false, false);
					mobileRequestList.getRequestList().add(mobileRequest);
				}
			}

		} catch (Throwable t) {
			log.error("Excepción en método queryRequestList: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		log.debug("EJECUTADO CORRECTAMENTE queryRequestList: " + state);
		return mobileRequestList;
	}

	@Transactional(readOnly=false)
	public MobileRequest queryRequest(String certificate, String requestId)	throws MobileException {
		log.debug("EJECUTANDO queryRequest: etiqueta petición = " + requestId);

		MobileRequest mobileRequest = null;

		try {
			// Se obtiene el usuario
			PfUsersDTO user = obtenerUsuario(certificate);

			// Se obtiene la información de la petición de la base de datos
			PfRequestTagsDTO reqTag = requestBO.queryRequestTagByHash(requestId);

			if (reqTag == null) {
				log.debug("Excepción en método queryRequest: " + MobileConstants.MESSAGE_003);
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_003);
				error.setMessage(MobileConstants.MESSAGE_003);
				throw new MobileException(MobileConstants.MESSAGE_003, error);
			}

			mobileRequest = convertRequest(reqTag, user, Constants.MESSAGES_UNRESOLVED, true, true);

			// Se marca la petición como leída si no está en un estado final
			if (tagBO.checkNotFinishedReqTag(reqTag)) {
				tagBO.changeStateToRead(reqTag, user);
			}

		} catch (MobileException me) {
			log.error("Excepción en método registqueryRequesterSIMUser: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción en método queryRequest: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		log.debug("EJECUTADO CORRECTAMENTE queryRequest: etiqueta petición = " + requestId);
		return mobileRequest; 
	}

	@Transactional(readOnly=false)
	public MobileDocumentList getDocumentsToSign(String certificate, String requestTagId) throws MobileException {
		log.debug("EJECUTANDO getDocumentsToSign: etiqueta petición = " + requestTagId);

		MobileDocumentList mobileDocumentList = null;

		// Se obtiene el usuario
		PfUsersDTO usuarioConectado = obtenerUsuario(certificate);

		try {
			// Se obtienen los documentos de la petición solicitada
			PfRequestTagsDTO reqTag = requestBO.queryRequestTagByHash(requestTagId);

			requestBO.bloquearPeticion(reqTag, usuarioConectado);
			
			List<PfDocumentsDTO> documents = reqTag.getPfRequest().getPfDocumentsList();

			// Se obtiene la configuración de la petición
			Configuration conf = signBO.loadSignProperties(reqTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey());

			// Se obtiene el contenido y la configuración de firma de todos los documentos de la petición
			mobileDocumentList = new MobileDocumentList();
			for (PfDocumentsDTO doc : documents) {

				// Se obtiene la configuración de firma y la operación
				List<AbstractBaseDTO> signs = signBO.orderedSigns(doc.getChash());
				DocumentSignatureConfig documentSignatureConfig = signBO.obtenerConfiguracionFirma(signs, reqTag, conf, doc);
				MobileDocument mobileDocument = this.obtenerContenidoFirma(signs, reqTag, conf, doc, documentSignatureConfig);

				mobileDocumentList.getDocument().add(mobileDocument);
			}

		} catch (CustodyServiceException e) {
			log.error("Excepción en método queryRequest: ", e);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_004);
			error.setMessage(MobileConstants.MESSAGE_004);
			throw new MobileException(e.getMessage(), error);
		} catch (BlockRequestException e) {
			log.error("Excepción en método queryRequest: ", e);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_013);
			error.setMessage(MobileConstants.MESSAGE_013);
			throw new MobileException(e.getMessage(), error);
		} catch (Throwable t) {
			log.error("Excepción en método queryRequest: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		log.debug("EJECUTADO CORRECTAMENTE presign: etiqueta petición = " + requestTagId);
		return mobileDocumentList;
	}

	private MobileDocument obtenerContenidoFirma(List<AbstractBaseDTO> signs, PfRequestTagsDTO reqTag, Configuration conf, PfDocumentsDTO doc, DocumentSignatureConfig documentSignatureConfig) throws CustodyServiceException, Exception {
		CustodyServiceOutput custodyService = null;
		MobileDocument mobileDocument = new MobileDocument();
		DataHandler data = null;

		String signatureFormat = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);
		// Si el documento no contiene firmas la operación es firmar
		if (signs.isEmpty()) {
			// Si el documento no es un PDF y el formato de firma es PADES se cambia a XADES 
			if (signatureFormat.equals(Constants.SIGN_FORMAT_PDF) && 
					!doc.getDmime().equals("application/pdf")) {
				signatureFormat = Constants.SIGN_FORMAT_XADES_IMPLICIT;
			}

			// Se obtiene el contenido del documento			
			custodyService = custodyServiceFactory.createCustodyServiceOutput(doc.getPfFile().getCtype());
			data = MobileServiceUtil.getInstance().getDocumentData(doc, custodyService);

		} else {
			// Se obtiene el contenido de la firma
			PfSignsDTO sign = (PfSignsDTO) signs.get(0);
			custodyService = custodyServiceFactory.createCustodyServiceOutput(sign.getCtype());
			data = MobileServiceUtil.getInstance().getSignatureData(sign, custodyService);
		}

		mobileDocument.setData(data);
		mobileDocument.setName(doc.getDname());
		mobileDocument.setMime(doc.getDmime());
		mobileDocument.setIdentifier(doc.getChash());

		mobileDocument.setSignatureType(signatureFormat);
		mobileDocument.setSignAlgorithm(documentSignatureConfig.getHashAlgorithm());
		mobileDocument.setOperationType(documentSignatureConfig.getOperation());
		String params = util.getSignatureParameters(signatureFormat, doc.getDmime());
		if (!"".equals(params)) {
			mobileDocument.setSignatureParameters(params);
		}
		return mobileDocument;
	}

	@Transactional(readOnly = false, rollbackFor=MobileException.class)
	public String saveSign(String certificate, String requestTagId, MobileDocSignInfoList docSignInfoList) throws MobileException {
		log.debug("EJECUTANDO saveSign: etiqueta petición = " + requestTagId);

		// Se obtiene el usuario
		PfUsersDTO user = obtenerUsuario(certificate);

		try {
			//obtenemos la fecha de validez del certificado
			Date fechaValidezCertificado = certificateBO.getFechaValidezCertificado(certificate);


			// Se obtiene la información de la etiqueta petición de la base de datos
			PfRequestTagsDTO reqTag = requestBO.queryRequestTagByHash(requestTagId);

			// Se comprueba si la petición está en un estado final para el usuario
			if (tagBO.checkNotFinishedReqTag(reqTag)) {

				// Se la información de la petición
				PfRequestsDTO req = reqTag.getPfRequest();

				// Guardo cada una de las firmas generadas
				for (MobileDocSignInfo signInfo : docSignInfoList.getMobileDocSignInfo()) {

					// Obtengo el documento firmado
					PfDocumentsDTO doc = requestBO.checkDocumentFetch(signInfo.getDocumentId());

					// Genero la información de la firma
					PfSignsDTO signDTO = new PfSignsDTO();
					signDTO.setPfDocument(doc);
					signDTO.setCapplication(req.getPfApplication().getCapplication());
					signDTO.setCformat(signInfo.getSignFormat());
					signDTO.setCtype(Constants.C_TYPE_SIGNLINE_SIGN);
					signDTO.setPfUser(user);
					String transactionId = new Long(new Date().getTime()).toString();
					signDTO.setCtransaction(transactionId);
					signDTO.setFstate(Calendar.getInstance().getTime());

					String storageType = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.FIRMAS.name());
					CustodyServiceInput custodyService = custodyServiceFactory.createCustodyServiceInput(storageType);
					signDTO.setCtype(storageType);

					// Buscamos al firmante
					PfSignersDTO signerDTO = tagBO.lastSignerUnresolvedDocument(reqTag, user, user.getValidJob(), false);

					signDTO.setPfSigner(signerDTO);

					signDTO.setfValid(fechaValidezCertificado);
					
					//Se incluye este campo para distinguir cuando la firma se ha realizado con la app movil
					signDTO.setLmovil(true);

					// Obtengo los bytes de la firma
					byte[] sign = Util.getInstance().getBytes(signInfo.getSignature().getInputStream());

					sign = signBO.addTimeStamp(reqTag, user, signDTO, sign);

					signBO.validateSign(reqTag, doc, sign);

					// Generamos el CSV de la firma
					if (eeUtilUtilFirmaBO.checkCSV()) {
						String csv = eeUtilUtilFirmaBO.getCSV(sign, doc.getDmime());
						signDTO.setCsv(csv);
					}

					// Se guarda la firma en base de datos
					baseDAO.insertOrUpdate(signDTO);

					signBO.saveSign(signDTO, user, sign, custodyService);

				}

				// Se marca la etiqueta petición como firmada y se actualizan las etiquetas
				reqTag.setValidada(true);
				tagBO.changeStateToSigned(reqTag, user, false);

				List<AbstractBaseDTO> signedRequests = new ArrayList<AbstractBaseDTO>();
				signedRequests.add(reqTag);
				noticeBO.sendAdviceToAppServer(signedRequests);
			} else {
				log.info ("No se ha podido llevar a cabo la firma porque la etiqueta peticion se encuentra en un estado final para el usuario");
				throw new PfirmaException ("No se ha podido llevar a cabo la firma porque la etiqueta peticion se encuentra en un estado final para el usuario");
			}
			requestBO.desbloquearPeticion(reqTag.getChash(), user);

		} catch (PfirmaException e) {
			procesarError(e, MobileConstants.COD_005, MobileConstants.MESSAGE_005);
		} catch (CustodyServiceException e) {
			procesarError(e, MobileConstants.COD_004, MobileConstants.MESSAGE_004);
		} catch (EeutilException e) {
			procesarError(e, MobileConstants.COD_007, MobileConstants.MESSAGE_007);
		} catch (TimeStampingException e) {
			procesarError(e, MobileConstants.COD_010, MobileConstants.MESSAGE_010);
		} catch (InvalidSignException e) {
			procesarError(e, MobileConstants.COD_011, MobileConstants.MESSAGE_011);
		} catch (Exception e) {
			procesarError(e, MobileConstants.COD_006, MobileConstants.MESSAGE_006);
		} catch (Throwable t) {
			procesarError(t, MobileConstants.COD_000, MobileConstants.MESSAGE_000);
		}

		log.debug("EJECUTADO CORRECTAMENTE postsign: etiqueta petición = " + requestTagId);
		return "OK";
	}

	private void procesarError(Throwable e, String codigo, String mensaje) throws MobileException {
		log.error("Excepción en método postsign: ", e);
		MobileError error = new MobileError();
		error.setCode(codigo);
		error.setMessage(mensaje);
		throw new MobileException(e.getMessage(), error);
	}

	@Transactional(readOnly = false, rollbackFor=MobileException.class)
	public String rejectRequest(String certificate, String requestId, String textRejection) throws MobileException {
		log.debug("EJECUTANDO rejectRequest: etiqueta petición = " + requestId);

		try {
			PfUsersDTO user = obtenerUsuario(certificate);

			// Se obtiene la información de la petición de la base de datos
			PfRequestTagsDTO reqTag = requestBO.queryRequestTagByHash(requestId);

			if (tagBO.checkNotFinishedReqTag(reqTag)) {
				List<AbstractBaseDTO> rejectList = new ArrayList<AbstractBaseDTO>();
				rejectList.add(reqTag);

				requestBO.insertReject(user, rejectList, textRejection);

				List<AbstractBaseDTO> signedRequests = new ArrayList<AbstractBaseDTO>();
				signedRequests.add(reqTag);
				noticeBO.sendAdviceToAppServer(signedRequests);

			} else {
				log.info ("No se ha podido llevar a cabo el rechazo porque la etiqueta peticion se encuentra en un estado final para el usuario");
				throw new PfirmaException ("No se ha podido llevar a cabo el rechazo porque la etiqueta peticion se encuentra en un estado final para el usuario");
			}
		} catch (Throwable t) {
			log.error("Excepción en método rejectRequest: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		log.debug("EJECUTADO CORRECTAMENTE rejectRequest: etiqueta petición = " + requestId);
		return requestId;
	}

	@Transactional(readOnly=false)
	public MobileDocument documentPreview(String certificate, String documentId) throws MobileException {
		log.debug("EJECUTANDO documentPreview: documento = " + documentId);

		// Se obtiene el usuario
		PfUsersDTO user = obtenerUsuario(certificate);
		
		MobileDocument mobileDocument = null;
		InputStream dataHandler = null;
		byte[] arrayByte = null;
		byte[] pdfByte = null;
		DataSource dataSourcePdf = null;
		DataHandler pdfHandlerPdf = null;

		try {
			// Comprueba si el documento existe en base de datos
			PfDocumentsDTO doc = requestBO.checkDocument(documentId);

			if (doc != null) {
				final CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(doc.getPfFile().getCtype());
				DataHandler data = MobileServiceUtil.getInstance().getDocumentData(doc, custodyService);

				mobileDocument = new MobileDocument();
				
				dataHandler = data.getInputStream();
				arrayByte = IOUtils.toByteArray(dataHandler);
				
				//Se crea una opción para poder visualizar los archivos TCN como PDFs
				if (doc.getDmime().equalsIgnoreCase("text/tcn")){
					pdfByte = eeUtilMiscBO.getPdfFromTCN(arrayByte);
					dataSourcePdf = new ByteArrayDataSource(pdfByte, MobileConstants.MIME_PDF);
					pdfHandlerPdf = new DataHandler(dataSourcePdf);

					mobileDocument.setData(pdfHandlerPdf);					
					mobileDocument.setName(doc.getDname().replace("tcn", "pdf"));
					mobileDocument.setMime(MobileConstants.MIME_PDF);

				} else if (Constants.C_DOCUMENTTYPE_FACTURAE.equals(doc.getPfDocumentType().getCdocumentType())){
					pdfByte = eeUtilMiscBO.getPdfFromFacturae(arrayByte);
					dataSourcePdf = new ByteArrayDataSource(pdfByte, MobileConstants.MIME_PDF);
					pdfHandlerPdf = new DataHandler(dataSourcePdf);

					mobileDocument.setData(pdfHandlerPdf);					
					mobileDocument.setName(doc.getDname().replace(MimeType.getInstance().extensionOf(doc.getDname()), "pdf"));
					mobileDocument.setMime(MobileConstants.MIME_PDF);
					
				} else if (doc.getLissign() && applicationVO.getViewPreSignActivated()) {
//					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						previsualizacionBO.previsualizacion(new ByteArrayInputStream (arrayByte), baos, applicationBO.queryApplicationPfirma());
						data = new DataHandler(new ByteArrayDataSource(baos.toByteArray(), doc.getDmime()));
						mobileDocument.setData(data);
						mobileDocument.setName(doc.getDname().replace(MimeType.getInstance().extensionOf(doc.getDname()), "pdf"));
						mobileDocument.setMime(MobileConstants.MIME_PDF);
//					} catch (Exception e) {
//						e.printStackTrace();
//						log.error("Error generar la previsualización del documento: ", e);
//						//pdfByte = eeutilVisBO.getVisualizarContenidoOriginal(doc.getChash(), doc.getDname(), arrayByte, doc.getDmime());
//						mobileDocument.setData(data);
//						mobileDocument.setName(doc.getDname());
//						mobileDocument.setMime(doc.getDmime());
//					}
				} else {					
					mobileDocument.setData(data);
					mobileDocument.setName(doc.getDname());
					mobileDocument.setMime(doc.getDmime());
				}

				mobileDocument.setIdentifier(doc.getChash());
			}

		} catch (PfirmaException e) {
			log.error("Excepción en método documentPreview: ", e);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_005);
			error.setMessage(MobileConstants.MESSAGE_005);
			throw new MobileException(e.getMessage(), error);
		} catch (CustodyServiceException e) {
			log.error("Excepción en método documentPreview: ", e);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_004);
			error.setMessage(MobileConstants.MESSAGE_004);
			throw new MobileException(e.getMessage(), error);
		} catch (Throwable t) {
			log.error("Excepción en método documentPreview: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		log.debug("EJECUTADO CORRECTAMENTE documentPreview: documento = " + documentId);
		return mobileDocument;
	}

	/**
	 * Método que devuelve la lista de aplicaciones registradas en Portafirmas
	 * @return Listado de aplicaciones
	 */
	@Transactional(readOnly=false)
	public MobileApplicationList queryApplicationsMobile(String certificate) throws MobileException {
		List<AbstractBaseDTO> applications = applicationAdmBO.queryListByHierarchy();
		MobileApplicationList mobileApplications = new MobileApplicationList();

		try {
			for (AbstractBaseDTO app : applications) {
				PfApplicationsDTO application = (PfApplicationsDTO) app;
				MobileApplication mobileApplication = new MobileApplication();
				mobileApplication.setId(application.getCapplication());
				mobileApplication.setName(application.getDapplication());
				mobileApplications.getApplicationList().add(mobileApplication);
			}
		} catch (Throwable t) {
			log.error("Excepción en método queryApplicationsMobile: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		return mobileApplications;
	}

	/**
	 * Método que da el visto bueno a una petición
	 * @param certificate Certificado del usuario
	 * @param requestTagId Identificador de la etiqueta de la petición
	 * @return Resultado de la operación
	 * @throws MobileException
	 */
	@Transactional(readOnly = false, rollbackFor=MobileException.class)
	public String approveRequest(String certificate, String requestTagId) throws MobileException {
		log.debug("EJECUTANDO approveRequest: etiqueta petición = " + requestTagId);

		try {
			PfUsersDTO user = obtenerUsuario(certificate);

			// Se obtiene la información de la petición de la base de datos
			PfRequestTagsDTO reqTag = requestBO.queryRequestTagByHash(requestTagId);

			if (tagBO.checkNotFinishedReqTag(reqTag)) {
				// Se cambia la etiqueta a visto bueno
				List<AbstractBaseDTO> passList = new ArrayList<AbstractBaseDTO>();
				passList.add(reqTag);
				passBO.pass(user, user.getValidJob(), passList, false, true);
			} else {
				log.info ("No se ha podido llevar a cabo el visto bueno porque la petición se encuentra en un estado final para el usuario");
				throw new PfirmaException ("No se ha podido llevar a cabo el visto bueno porque la petición se encuentra en un estado final para el usuario");
			}

		} catch (Throwable t) {
			log.error("Excepción en método rejectRequest: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		log.debug("EJECUTADO CORRECTAMENTE approveRequest: etiqueta petición = " + requestTagId);
		return requestTagId;
	}

	/**
	 * Método que devuelve el contenido de una firma de Portafirmas
	 * @param certificate Certificado del usuario
	 * @param signId Identificador de la firma
	 * @return Contenido de la firma
	 * @throws MobileException
	 */
	@Transactional(readOnly=false)
	public MobileDocument signPreview(String certificate, String documentId) throws MobileException {

		MobileDocument sign = new MobileDocument();

		try {
			// Se obtiene el usuario
			PfUsersDTO user = obtenerUsuario(certificate);

			CustodyServiceOutputSign custodySign = new CustodyServiceOutputSign();
			String storageType = null;
			String name = null;
			String mime = null;

			PfSignsDTO fileSign = custodyServiceFactory.signFileQuery(documentId);

			if (fileSign == null) {
				log.debug("Excepción en método signPreview: El documento no ha sido firmado");
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_008);
				error.setMessage(MobileConstants.MESSAGE_008);
				throw new MobileException("El documento no ha sido firmado", error);
			}

			storageType = fileSign.getCtype();
			custodySign.setType(Constants.SIGN_TYPE_SERVER);
			custodySign.setIdentifier(fileSign.getPrimaryKeyString());
			custodySign.setUri(fileSign.getCuri());
			custodySign.setIdEni(fileSign.getRefNASIdEniFirma());
			custodySign.setRefNasDir3(fileSign.getRefNASDir3Firma());
			// Obtiene la extensión de la firma en base a su formato
			String extension = null;
			if (fileSign.getCformat().toUpperCase().contains("XADES")) {
				extension =	Util.getInstance().loadSignExtensions().get(Constants.SIGN_FORMAT_XADES);
			}
			else {
				extension =	Util.getInstance().loadSignExtensions().get(fileSign.getCformat());
			}

			name = Util.getInstance().getNombreFichero(fileSign.getPfDocument().getDname(), true, null, "_firmado", extension);
			mime = Util.getInstance().loadSignMime().get(fileSign.getCformat());

			CustodyServiceOutput service = custodyServiceFactory.createCustodyServiceOutput(storageType);
			byte[] bytes = service.downloadSign(custodySign);
			DataHandler data = new DataHandler(new ByteArrayDataSource(bytes, mime));

			// Se crea el documento de firma
			sign.setData(data);
			sign.setName(name);
			sign.setMime(mime);
			sign.setIdentifier(documentId);
			sign.setSignatureType(fileSign.getCformat());
			sign.setSize(bytes.length);

		} catch (CustodyServiceException e) {
			log.error("Excepción en método signPreview: ", e);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_008);
			error.setMessage(MobileConstants.MESSAGE_008);
			throw new MobileException(e.getMessage(), error);
		} catch (Throwable t) {
			log.error("Excepción en método signPreview: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		return sign;
	}

	/**
	 * Método que devuelve el informe de una firma
	 * @param certificate Certificado del usuario
	 * @param signId Indentificador de la firma 
	 * @return Informe de la firma
	 * @throws MobileException
	 */
	/*public MobileDocument reportPreview(String certificate, String documentId) throws MobileException {

		MobileDocument report = new MobileDocument();;

		try {
			// Se obtiene el usuario
			PfUsersDTO user = obtenerUsuario(certificate);

			// Se obtiene la firma del documento
			PfSignsDTO fileSign = custodyServiceFactory.signFileQuery(documentId);

			// Se obtiene el nombre del informe
			String name = Util.getInstance().getNombreFichero(fileSign.getPfDocument().getDname(), true, null, "_informe", "pdf");
			String mime = "application/pdf";

			// Se genera el informe a partir de la firma
			byte[] reportBytes = reportBO.procesarFirma(fileSign, false);

			report.setData(new DataHandler(new ByteArrayDataSource(reportBytes, mime)));
			report.setName(name);
			report.setMime(mime);
			report.setIdentifier(documentId);
			report.setSignatureType(fileSign.getCformat());
			report.setSize(reportBytes.length);

		} catch (CustodyServiceException e) {
			log.error("Excepción en método reportPreview: ", e);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_008);
			error.setMessage(MobileConstants.MESSAGE_008);
			throw new MobileException(e.getMessage(), error);
		} catch (Throwable t) {
			log.error("Excepción en método reportPreview: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		return report;
	}*/

	/**
	 * Método que devuelve el informe de una firma
	 * @param certificate Certificado del usuario
	 * @param signId Indentificador de la firma 
	 * @return Informe de la firma
	 * @throws MobileException
	 */
	@Transactional(readOnly=false)
	public MobileDocument reportPreview(String certificate, String documentId) throws MobileException {

		MobileDocument report = new MobileDocument();;

		try {
			// Se obtiene el usuario
			PfUsersDTO user = obtenerUsuario(certificate);

			// Se obtiene la firma del documento
			PfSignsDTO signDTO = custodyServiceFactory.signFileQuery(documentId);

			byte [] reportBytes = binaryDocumentsBO.getReportBySignDTO(signDTO, signDTO.getPfDocument().getPfRequest().getPfApplication());

			// Se obtiene el nombre del informe
			/*String name = Util.getInstance().getNombreFichero(fileSign.getPfDocument().getDname(), true, null, "_informe", "pdf");
			String mime = "application/pdf";

			// Se genera el informe a partir de la firma
			byte[] reportBytes = reportBO.procesarFirma(fileSign, false);*/

			

			report.setData(new DataHandler(new ByteArrayDataSource(reportBytes, Constants.PDF_MIME)));
			report.setName(binaryDocumentsBO.getNombreReport(signDTO.getPfDocument().getDname()));
			report.setMime(Constants.PDF_MIME);
			report.setIdentifier(documentId);
			report.setSignatureType(signDTO.getCformat());
			report.setSize(reportBytes.length);

		} catch (DocumentCantBeDownloadedException e) {
			log.error("Excepción en método reportPreview: ", e);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_008);
			error.setMessage(MobileConstants.MESSAGE_008);
			throw new MobileException(e.getMessage(), error);
		} catch (Throwable t) {
			log.error("Excepción en método reportPreview: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		return report;
	}


	/**
	 * Método para dar de alta a un usuario en SIM.
	 * 
	 * @param certificate
	 * @param register
	 * @return
	 * @throws MobileException
	 */
	public MobileSIMUserStatus registerSIMUser(String certificate, MobileSIMUser register) throws MobileException {
		try {

			PfUsersDTO user = obtenerUsuario(certificate);

			HashMap<String,String> parametros = appBO.obtenerParametrosSIM();

			if(parametros.get(Constants.SIM_USER)==null || parametros.get(Constants.SIM_USER).trim().length()==0 ||
					parametros.get(Constants.SIM_PASSWORD)==null || parametros.get(Constants.SIM_PASSWORD).trim().length()==0 ||
					parametros.get(Constants.SIM_URL)==null ||parametros.get(Constants.SIM_URL).trim().length()==0 ||
					parametros.get(Constants.SIM_ENVIO_URL)==null ||parametros.get(Constants.SIM_ENVIO_URL).trim().length()==0 ||
					parametros.get(Constants.SIM_SERVICE)==null ||parametros.get(Constants.SIM_SERVICE).trim().length()==0){
				log.error(MobileConstants.MESSAGE_009);
				String msError = MobileConstants.MESSAGE_009;
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_009);
				throw new MobileException(msError,error);
			}else{
				HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(parametros.get(Constants.SIM_USER), parametros.get(Constants.SIM_PASSWORD));
				Client cliente = ClientBuilder.newClient().register(feature);

				String target = parametros.get(Constants.SIM_URL);
				target += "?IdDispositivo=" + register.getIdDispositivo();
				target += "&IdRegistro=" + register.getIdRegistro();
				target += "&IdUsuario=" + user.getCidentifier();
				target += "&Plataforma=" + register.getPlataforma();
				target += "&Servicio=" + parametros.get(Constants.SIM_SERVICE);


				Response respuesta = cliente.target(target).request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

				MobileSIMUserStatus mobileSIMUserStatus = new MobileSIMUserStatus();
				mobileSIMUserStatus.setStatusCode(String.valueOf(respuesta.getStatus().getStatusCode()));
				mobileSIMUserStatus.setStatusText(respuesta.getStatus().getStatusText());
				mobileSIMUserStatus.setDetails(respuesta.getStatus().getDetails());
				mobileSIMUserStatus.setIdDispositivo(respuesta.getIdDispositivo());

				//Si la respuesta del registroSIM es code = 0 y text = OK, se activan las notificaciones push para dicho usuario
				if(respuesta.getStatus() != null 
						&& respuesta.getStatus().getStatusCode() == Integer.parseInt(SimConstants.REG_SIM_0000_COD) 
						&& respuesta.getStatus().getStatusText().equals("OK")){
					user.setLNotifyPush(true);
					baseDAO.insertOrUpdate(user);
				}
				
				return mobileSIMUserStatus;
			}



		} catch (MobileException me) {
			log.error("Excepción en método registerSIMUser: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción en método registerSIMUser: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
	}

	@Transactional(readOnly=false)
	public String validateUser(String certificate) throws MobileException {
		log.debug("EJECUTANDO validateUser");

		String nifCif;

		try {
			PfUsersDTO user = authenticate(certificate);
			
			if (user != null) {
				nifCif = user.getCidentifier();	
			} else {
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_012);
				error.setMessage(MobileConstants.MESSAGE_012);
				throw new MobileException("El usuario del certificado siguiente no existe en el sistema: " + certificate, error);	
			}

		} catch (MobileException me) {
			log.error("Excepción en método validateUser: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción en método validateUser: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
		
		return nifCif;
	}

	@Transactional(readOnly=false)
	public String updateNotifyPush(String certificate, String estadoNotifyPush) throws MobileException {
		log.info("EJECUTANDO updateNotifyPush");
		log.info(certificate);
		log.info(estadoNotifyPush);

		String resultado = "OK";

		try {
			PfUsersDTO user = obtenerUsuario(certificate);
			
			if (user != null) {
				if (Constants.C_YES.equals(estadoNotifyPush)) {
					userAdmBO.updateLNotifyPush(user, true);	
				} else if (Constants.C_NOT.equals(estadoNotifyPush)) {
					userAdmBO.updateLNotifyPush(user, false);
				} else { 
					resultado = "KO";
				}
			} else {
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_012);
				error.setMessage(MobileConstants.MESSAGE_012);
				throw new MobileException("El usuario del certificado no existe en el sistema", error);	
			}

		} catch (MobileException me) {
			log.error("Excepción controlada en método updateNotifyPush: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción inesperada en método updateNotifyPush: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
		log.info("Resultado updateNotifyPush: " + resultado);
		return resultado;
	}
	
	public String estadoNotifyPush(String certificate) throws MobileException {
		log.debug("EJECUTANDO estadoNotifyPush");

		String valorNotifyPush = "N";

		try {
			PfUsersDTO user = obtenerUsuario(certificate);
			
			if (user != null) {
				if (user.getLNotifyPush()!=null) {
					if (user.getLNotifyPush()) {
						valorNotifyPush = "S";	
					}
				} 
			} else {
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_012);
				error.setMessage(MobileConstants.MESSAGE_012);
				throw new MobileException("El usuario del certificado no existe en el sistema", error);	
			}

		} catch (MobileException me) {
			log.error("Excepción controlada en método estadoNotifyPush: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción inesperada en método estadoNotifyPush: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
		
		return valorNotifyPush;
	}

	
	/**
	 * Método que realiza la autenticación del usuario a partir de su certificado
	 *  
	 * @param certificate
	 * @return
	 * @throws MobileException
	 */
	private PfUsersDTO authenticate(String certificate) throws MobileException {
		String nifCif = null;
		try {
			ValidateCertificateResponse respuesta = certificateBO.validarCertificado(certificate);
			if (respuesta.isError() || !respuesta.isValido()) {
				log.debug("El certificado no es valido : " + respuesta.getMensajeAmpliado());
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_001);
				error.setMessage(MobileConstants.MESSAGE_001);
				throw new MobileException("El certificado no es valido: " + respuesta.getMensajeAmpliado(), error);						
			}
			nifCif = respuesta.getNifCif();

		} catch (MobileException me) {
			log.error("Excepción controlada en método authenticate: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción inesperada en método authenticate: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}

		return (PfUsersDTO) userAdmBO.getUserByDni(nifCif.toUpperCase());
	}
	
	/**
	 * Método que obitiene el usuario a partir de su DNI
	 *  
	 * @param dni
	 * @return
	 * @throws MobileException
	 */
	private PfUsersDTO obtenerUsuario(String dni) throws MobileException {
		PfUsersDTO usuario = null;
		try {
			byte[] dniDecodificado = org.apache.commons.codec.binary.Base64.decodeBase64(dni);
			String dniDec = new String(dniDecodificado);
			usuario = (PfUsersDTO) userAdmBO.getUserByDni(dniDec.toUpperCase());
		} catch (Exception e) {
			usuario = authenticate(dni);
		}
		if (usuario == null || usuario.getCidentifier() == null || "".equals(usuario.getCidentifier())) {
			usuario = authenticate(dni);
		}
	
		return usuario;
		
	}

	/**
	 * Método que convierte una etiqueta petición estándar a una petición del Portafirmas móvil sin incluir sus documentos
	 * @param reqTag Etiqueta petición
	 * @return Petición
	 */
	//	private MobileRequest convertRequest(PfRequestTagsDTO reqTag, PfUsersDTO user) {
	//
	//		// Se convierten los parámetros simples de la petición móvil
	//		MobileRequest request = MobileServiceUtil.getInstance().requestTagToMobileRequest(reqTag);
	//		
	//		// Se obtiene el texto de la petición
	//		PfRequestsTextDTO text = requestBO.queryRequestText(reqTag.getPfRequest());
	//		request.setText(text.getTrequest());
	//
	//		// Se obtienen los remitentes de la petición
	//		MobileStringList senders = new MobileStringList();
	//		Set<PfUsersRemitterDTO> remitters = reqTag.getPfRequest().getPfUsersRemitters();
	//		for (PfUsersRemitterDTO remitter : remitters) {
	//			senders.getStr().add(remitter.getPfUser().getFullName());
	//		}
	//		request.setSenders(senders);
	//
	//		// Se obtienen las líneas de firma de la petición
	//		MobileSignLineList signLineList = new MobileSignLineList();
	//		Set<PfSignLinesDTO> signLines = reqTag.getPfRequest().getPfSignsLines();
	//		for (PfSignLinesDTO signLine : signLines) {
	//			MobileSignLine mobileSignLine = MobileServiceUtil.getInstance().signLineDTOToMobileSignLine(signLine);
	//			signLineList.getMobileSignLine().add(mobileSignLine);
	//		}
	//		request.setSignLineList(signLineList);
	//
	//		// Se comprueba la acción previa
	//		request.setWorkflow(requestBO.checkAccionPreviaRequest(reqTag.getPfRequest().getChash(), user));
	//
	//		return request;
	//	}

	/**
	 * Método que genera la cadena de formato de firma para el applet de firma
	 * @param formerFormat Formato original
	 * @return Formato de firma para el applet
	 */
	//	private String generateSignatureFormat(String formerFormat) {
	//		String formatParam = "";
	//		if (formerFormat.equals("XADES IMPLICITO")) {
	//			formatParam = "XAdES";
	//		} else if (formerFormat.equals("XADES EXPLICITO")) {
	//			formatParam = "XAdES";
	//		} else if (formerFormat.equals("XADES ENVELOPING")) {
	//			formatParam = "XAdES";
	//		} else if (formerFormat.equals("XADES ENVELOPED")) {		
	//			formatParam = "XAdES";
	//		} else if (formerFormat.equals("CADES")) {
	//			formatParam = "CAdES";
	//		} else if (formerFormat.equals("PDF")) {
	//			formatParam = "Adobe PDF";
	//		}
	//		return formatParam;
	//	}
	


	/**
	 * Método que convierte una etiqueta petición estándar a una petición del Portafirmas móvil incluyendo sus documentos
	 * @param reqTag Etiqueta petición
	 * @return Petición
	 * @throws MobileException 
	 */
	private MobileRequest convertRequest(PfRequestTagsDTO reqTag, PfUsersDTO user, String state, boolean size, boolean isQueryDetail) throws MobileException {

		// Se obtiene la configuración de la petición
		Configuration conf = signBO.loadSignProperties(reqTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey());

		// Se convierten los parámetros simples de la petición móvil
		MobileRequest request = MobileServiceUtil.getInstance().requestTagToMobileRequest(reqTag, isQueryDetail);

		// Se obtiene el tipo de petición
		if (reqTag.getPfSignLine() != null) {
			request.setRequestType(reqTag.getPfSignLine().getCtype());
		}

		// Se obtiene el texto de la petición
		PfRequestsTextDTO text = requestBO.queryRequestText(reqTag.getPfRequest());
		//Vamos a truncar el texto de la petición en 1000 caracteres y le añadimos .. al final
		String textoPeticion = text.getTrequest();
		if (textoPeticion !=null && textoPeticion.length()>1000){
			textoPeticion = textoPeticion.substring(0, 999) + "..";
		}
		request.setText(textoPeticion);

		// Se obtienen los remitentes de la petición
		MobileStringList senders = new MobileStringList();
		Set<PfUsersRemitterDTO> remitters = reqTag.getPfRequest().getPfUsersRemitters();
		for (PfUsersRemitterDTO remitter : remitters) {
			senders.getStr().add(remitter.getPfUser().getFullName());
		}
		request.setSenders(senders);

		//Sólo se muestra los documentos/adjuntos en el método queryRequest, no en el queryRequestList.
		if(isQueryDetail){
			// Se obtienen los documentos de la petición si el estado no es terminal
			if (!Constants.MESSAGES_SIGNED.equals(state) &&
					!Constants.MESSAGES_REJECTED.equals(state)) {
				request.setDocumentList(obtenerDocumentos(reqTag, size, conf));
				request.setAttachList(obtenerAdjuntos(reqTag, size, conf));
			}
			//En el resultado del método queryRequestList sólo se devuelven los documentos, los adjuntos no.
		}else{
			// Se obtienen los documentos de la petición si el estado no es terminal
			if (!Constants.MESSAGES_SIGNED.equals(state) &&
				!Constants.MESSAGES_REJECTED.equals(state)) {
				request.setDocumentList(obtenerDocumentos(reqTag, size, conf));
			}
		}

		//Se obtiene el tipo de firma de la petición (CASCADA/PARALELO)
		Boolean lcascadeSign = reqTag.getPfRequest().getLcascadeSign();
		request.setCascadeSign(lcascadeSign);
		
		/**
		 * Se obtienen las líneas de firma de la petición sólo cuando se invoca al método queryRequest, 
		 * no en el queryRequestList
		 */
		if(isQueryDetail){
		MobileSignLineList signLineList = new MobileSignLineList();
		List<PfSignLinesDTO> signLines = reqTag.getPfRequest().getPfSignsLinesList();
		for (PfSignLinesDTO signLine : signLines) {
				MobileSignLine mobileSignLine = MobileServiceUtil.getInstance().signLineDTOToMobileSignLine(signLine);
			//mobileSignLine.setTerminate(isTerminate(reqTag, signLine));
			signLineList.getMobileSignLine().add(mobileSignLine);
		}
		request.setSignLineList(signLineList);
		}

		// Se comprueba la acción previa
		request.setWorkflow(requestBO.checkAccionPreviaRequest(reqTag.getPfRequest().getChash(), user));

		return request;
	}

	private MobileDocumentList obtenerDocumentos(PfRequestTagsDTO reqTag, boolean size, Configuration conf)
			throws MobileException {
		List<MobileDocument> documentList = new ArrayList<MobileDocument>();
		String signatureFormat = null;
		String signAlgorithm = conf.getString(ConstantsSigner.AFIRMA5_HASH_ALGORITH);
		for (PfDocumentsDTO doc : reqTag.getPfRequest().getPfDocumentsList()) {
			signatureFormat = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);
			if (signatureFormat.equals(Constants.SIGN_FORMAT_PDF) && 
					!doc.getDmime().equals("application/pdf")) {
				signatureFormat = Constants.SIGN_FORMAT_XADES_IMPLICIT;
			}
			MobileDocument document = MobileServiceUtil.getInstance().documentDTOToMobileDocument(doc, applicationVO.getViewPreSignActivated());
			document.setSignatureType(signatureFormat);
			document.setSignAlgorithm(signAlgorithm);

			// Si se indica en la llamada se obtiene el tamaño de los documentos
			if (size) {
				try {
					final CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(doc.getPfFile().getCtype());
					final CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
					custodyDocument.setIdentifier(doc.getChash());
					custodyDocument.setUri(doc.getPfFile().getCuri());
					custodyDocument.setIdEni(doc.getPfFile().getIdEni());
					custodyDocument.setRefNasDir3(doc.getPfFile().getRefNasDir3());
					document.setSize(custodyService.downloadFile(custodyDocument).length);
				} catch (CustodyServiceException e) {
					log.error("Excepción en obtenerDocumentos: ", e);
					MobileError error = new MobileError();
					error.setCode(MobileConstants.COD_004);
					error.setMessage(MobileConstants.MESSAGE_004);
					throw new MobileException(e.getMessage(), error);
				}
			}

			documentList.add(document);
		}
		MobileDocumentList mobileDocumentList = new MobileDocumentList();
		mobileDocumentList.setDocument(documentList);
		return mobileDocumentList;
	}


	private MobileDocumentList obtenerAdjuntos(PfRequestTagsDTO reqTag, boolean size, Configuration conf) throws MobileException {
		List<MobileDocument> attachList = new ArrayList<MobileDocument>();
		for (PfDocumentsDTO doc : reqTag.getPfRequest().getPfAttachedDocumentsList()) {
			MobileDocument attach = MobileServiceUtil.getInstance().documentDTOToMobileDocument(doc, applicationVO.getViewPreSignActivated());

			// Si se indica en la llamada se obtiene el tamaño de los documentos
			if (size) {
				try {
					final CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(doc.getPfFile().getCtype());
					final CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
					custodyDocument.setIdentifier(doc.getChash());
					custodyDocument.setUri(doc.getPfFile().getCuri());
					custodyDocument.setIdEni(doc.getPfFile().getIdEni());
					custodyDocument.setRefNasDir3(doc.getPfFile().getRefNasDir3());
					attach.setSize(custodyService.downloadFile(custodyDocument).length);
				} catch (CustodyServiceException e) {
					log.error("Excepción en obtenerAdjuntos: ", e);
					MobileError error = new MobileError();
					error.setCode(MobileConstants.COD_004);
					error.setMessage(MobileConstants.MESSAGE_004);
					throw new MobileException(e.getMessage(), error);
				}
			}

			attachList.add(attach);
		}
		MobileDocumentList mobileAttachList = new MobileDocumentList();
		mobileAttachList.setDocument(attachList);
		return mobileAttachList;
	}

	/**
	 * Método que define si un formato de firma está dentro de la lista de formatos aceptados
	 * @param acceptedSignFormat Lista de formatos de firma aceptados
	 * @param signFormat Formato de firma
	 * @return True si está dentro de los aceptados, False en caso contrario
	 */
	private boolean filterBySignFormat(MobileStringList acceptedSignFormat, String signFormat) {
		boolean accepted = false;

		if (acceptedSignFormat.getStr() == null || acceptedSignFormat.getStr().isEmpty()) {
			accepted = true;
		} else {
			for (String format : acceptedSignFormat.getStr()) {
				if (signFormat.toUpperCase().contains(format.toUpperCase())) {
					accepted = true;
					break;
				}
			}
		}

		return accepted;
	}
	
	public String procesarRespuestaClave(String samlResponse, String remoteHost) throws MobileException {
		log.debug("EJECUTANDO procesarRespuestaClave");
		String dni = "";
		STORKAuthnResponse authnResponse = null;
		IPersonalAttributeList personalAttributeList = null;
		try{
			
			if (!StringUtils.isBlank(samlResponse)) {
				/* Decodificamos la respuesta SAML */
				byte[] decSamlToken = PEPSUtil.decodeSAMLToken(samlResponse);

				/* Obtenemos la instancia de SAMLEngine */
				STORKSAMLEngine engine = STORKSAMLEngine.getInstance(ConstantsClave.SP_CONF);

				/* Validamos el token SAML */
				try {				
					authnResponse = engine.validateSTORKAuthnResponse(decSamlToken,	remoteHost);
				} catch (STORKSAMLEngineException e) {
					MobileError error = new MobileError();
					error.setCode(MobileConstants.COD_015);
					error.setMessage(MobileConstants.MESSAGE_015);
					throw new MobileException(MobileConstants.MESSAGE_015, error);
				}

				if (authnResponse.isFail()) {
					MobileError error = new MobileError();
					error.setCode(MobileConstants.COD_016);
					error.setMessage(MobileConstants.MESSAGE_016);
					throw new MobileException(MobileConstants.MESSAGE_016, error);
				} else {
					/* Recuperamos los atributos */
					personalAttributeList = authnResponse.getPersonalAttributeList();

					PersonalAttribute identificadorAttribute = personalAttributeList.get(claveMobileProperties.getProperty(Constants.PROPERTY_IDENTIFICADOR));
					List<String> identificadores = identificadorAttribute.getValue();
					dni =  identificadores.get(0).split("/")[2];
					try {
						authenticateBO.autenticarUsuario(dni);
					} catch (Exception e) {
						MobileError error = new MobileError();
						error.setCode(MobileConstants.COD_017);
						error.setMessage(MobileConstants.MESSAGE_017);
						throw new MobileException(MobileConstants.MESSAGE_017, error);
					}
				}
			} else {
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_018);
				error.setMessage(MobileConstants.MESSAGE_018);
				throw new MobileException(MobileConstants.MESSAGE_018, error);
			}
		} catch (MobileException me) {
			log.error("Excepción controlada en método solicitudAccesoClave: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción inesperada en método solicitudAccesoClave: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
		
		
		return dni;
	}
	
	public MobileAccesoClave solicitudAccesoClave(String spUrl, String spReturn) throws MobileException {
		log.debug("EJECUTANDO solicitudAccesoClave");
		MobileAccesoClave mobileAccesoClave = new MobileAccesoClave();
		try {
			if (spUrl!=null && !"".equals(spUrl)) {
				claveMobileProperties.setProperty(Constants.PROPERTY_SP_URL_CLAVE, spUrl);
			}
			if (spReturn!=null && !"".equals(spReturn)) {
				claveMobileProperties.setProperty(Constants.PROPERTY_SP_RETURN_URL_CLAVE, spReturn);
			}

			String claveServiceUrl = claveMobileProperties.getProperty(Constants.PROPERTY_URL_CLAVE);
			String excludedIdPList = claveMobileProperties.getProperty(Constants.PROPERTY_EXCLUDED_IDPLIST);
			String forcedIdP = claveMobileProperties.getProperty(Constants.PROPERTY_FORCED_IDP);

			byte[] token = null;
			try {
				token = loginBusinessService.generaTokenClave(claveMobileProperties);
			} catch (Exception e) {
				MobileError error = new MobileError();
				error.setCode(MobileConstants.COD_014);
				error.setMessage(MobileConstants.MESSAGE_014);
				throw new MobileException(Constants.CREDENTIALS_ERROR_GENERATE_SAML, error);
			}

			String samlRequest = PEPSUtil.encodeSAMLToken(token);
			String samlRequestXML = new String(token);
			if (log.isInfoEnabled()) {
				log.debug(samlRequestXML);
			}

			mobileAccesoClave.setClaveServiceUrl(claveServiceUrl);
			mobileAccesoClave.setExcludedIdPList(excludedIdPList);
			mobileAccesoClave.setForcedIdP(forcedIdP);
			mobileAccesoClave.setSamlRequest(samlRequest);

		} catch (MobileException me) {
			log.error("Excepción controlada en método solicitudAccesoClave: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción inesperada en método solicitudAccesoClave: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
		
		return mobileAccesoClave;
	}

	/**
	 * @param certificate
	 * @param idRequestList
	 * @return
	 */
	public MobileFireTrasactionResponse getFIReTransaction(String certificate, MobileStringList idRequestList) throws MobileException {
		MobileFireTrasactionResponse fireTransactionResponse = null;
		try {
			PfUsersDTO usuario = obtenerUsuario(certificate);
			String[] hashes = idRequestList.getStr().toArray(new String[idRequestList.getStr().size()]);
			FireTransaction fireTransaction = fireBO.getTransactionMobile(hashes, usuario);
			fireTransactionResponse = obtenerMobileFireTransaction(fireTransaction);
		} catch (MobileException me) {
			log.error("Excepción controlada en método getFIReTransaction: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción inesperada en método getFIReTransaction: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
		return fireTransactionResponse;
	}

	private MobileFireTrasactionResponse obtenerMobileFireTransaction(FireTransaction fireTransaction) {
		MobileFireTrasactionResponse mobileFT = new MobileFireTrasactionResponse();
		mobileFT.setTransactionId(fireTransaction.getTransactionId());
		mobileFT.setUrlRedirect(fireTransaction.getUrlRedirect());
		return mobileFT;
	}

	/**
	 * @param certificate
	 * @param mobileFireTransaction
	 * @return
	 */
	public MobileFireRequestList signFIReCloud(String certificate, MobileStringList idRequestList, String transactionId) throws MobileException {
		MobileFireRequestList mobileFireRequestList = null;
		try {
			PfUsersDTO usuario = obtenerUsuario(certificate);
			String[] hashes = obtenerHashRequest(idRequestList);
			List<FireRequest> fireRequest = fireBO.signCloud(transactionId, hashes, usuario);
			mobileFireRequestList = obtenerFireRequestList(fireRequest);
		} catch (MobileException me) {
			log.error("Excepción controlada en método signFIReCloud: ", me);
			throw me;
		} catch (Throwable t) {
			log.error("Excepción inesperada en método signFIReCloud: ", t);
			MobileError error = new MobileError();
			error.setCode(MobileConstants.COD_000);
			error.setMessage(MobileConstants.MESSAGE_000);
			throw new MobileException(t.getMessage(), error);
		}
		return mobileFireRequestList ;
	}

	private String[] obtenerHashRequest(MobileStringList idRequestList) {
		String[] hashes = new String[idRequestList.getStr().size()];
		int index = 0;
		for (String hash : idRequestList.getStr()) {
			hashes[index++] = hash;
		}
		return hashes;
	}

	private MobileFireRequestList obtenerFireRequestList(List<FireRequest> fireRequestList) {
		MobileFireRequestList mobileFireRequestList = new MobileFireRequestList();
		for (FireRequest fireRequest : fireRequestList) {
			MobileFireRequest mobileFireRequest = new MobileFireRequest();
			mobileFireRequest.setId(fireRequest.getId());
			mobileFireRequest.setAsunto(fireRequest.getAsunto());
			mobileFireRequest.setErrorPeticion(fireRequest.getErrorPeticion());
			mobileFireRequest.setDocumentos(obtenerDocumentos(fireRequest.getDocumentos()));
			mobileFireRequestList.getMobileFireRequest().add(mobileFireRequest);
		}
		return mobileFireRequestList;
	}

	private MobileFireDocumentList obtenerDocumentos(List<FireDocument> documentos) {
		MobileFireDocumentList mobileFireDocumentList = new MobileFireDocumentList();
		for (FireDocument fireDocument : documentos) {
			MobileFireDocument mobileFireDocument = new MobileFireDocument();
			mobileFireDocument.setId(fireDocument.getId());
			mobileFireDocument.setNombre(fireDocument.getNombre());
			mobileFireDocument.setError(fireDocument.getError());
			mobileFireDocumentList.getMobileFireDocumentList().add(mobileFireDocument );
		}
		return mobileFireDocumentList;
	}

}
