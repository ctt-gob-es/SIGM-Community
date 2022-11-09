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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.configuration.FilterBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.RequestDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfAccionFirmanteDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebDocumentDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSpfirmaDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentScopesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfFilesDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfInvitedUsersDTO;
import es.seap.minhap.portafirmas.domain.PfMessagesDTO;
import es.seap.minhap.portafirmas.domain.PfNoticeRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUserMessageDTO;
import es.seap.minhap.portafirmas.domain.PfUsersCommentDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.domain.PfUsersMessageDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.domain.enums.EstadosParaRemitente;
import es.seap.minhap.portafirmas.exceptions.BlockRequestException;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.MimeType;
import es.seap.minhap.portafirmas.utils.SignDataUtil;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.web.beans.Paginator;
import es.seap.minhap.portafirmas.web.beans.Signer;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.web.beans.UserSelection;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ArchivoTemporalResponse;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ArchivoTemporalResponseList;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.NuevosArchivosAnexosResponse;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.NuevosArchivosAnexosResponseList;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestBO {
	
	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	private RequestDAO requestDAO;

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private TagBO tagBO;

	@Autowired
	private ApplicationVO applicationVO;

	@Autowired
	private CustodyServiceFactory custodyServiceFactory;

	@Autowired
	private SignDataUtil signDataUtil;

	Logger log = Logger.getLogger(RequestBO.class);

	@Autowired
	private FilterBO filterBO;

	@Autowired
	private ApplicationBO applicationBO;
	
	/**
	 * The Notice BO.
	 */
	@Autowired
	private NoticeBO noticeBO;
	
	@Autowired
	private RedactionBO redactionBO;
	
	@Autowired
	private UtilComponent utilComponent;
	
	@Autowired
	private DateComponent dateComponent;
	
	@Autowired
	private RestrictionBO restrictionBO;
	
	@Autowired
	private HistoricRequestBO historicRequestBO;
	
	/**
	 * Metodo que devuelve el numero de peticiones a mostrar
	 * segun la pestaña seleccionada y el usuario que solicita la
	 * operacion.
	 * 
	 * @param userDTO
	 *            Objeto que contiene la informacion del usuario que
	 *            esta dado de alta en el sistema.
	 * @param requestFilter
	 *            Cadena de texto que representa el filtro de las peticiones a
	 *            mostrar. Las opciones son: "redaction" (pestaña
	 *            "Redaccion") "finished" (pestaña "Terminadas")
	 *            "awaiting" (pestaña "En Espera")
	 * @return Numero de peticiones a mostrar.
	 */
	public String queryListCount(PfUsersDTO userDTO, String requestFilter) {
		List<String> tagList = new ArrayList<String>();

		if (Constants.MESSAGES_UNRESOLVED.equals(requestFilter) || Constants.MESSAGES_REDACTION.equals(requestFilter)) {
			tagList.add(Constants.C_TAG_NEW);
			tagList.add(Constants.C_TAG_READ);
			tagList.add(Constants.C_TAG_AWAITING_PASSED);
		} else if (Constants.MESSAGES_FINISHED.equals(requestFilter)) {
			tagList.add(Constants.C_TAG_SIGNED);
			tagList.add(Constants.C_TAG_REJECTED);
			tagList.add(Constants.C_TAG_PASSED);
		} else if (Constants.MESSAGES_AWAITING.equals(requestFilter)) {
			tagList.add(Constants.C_TAG_AWAITING);
		}

		return requestDAO.queryRequestListCount(userDTO, tagList);
	}

	/**
	 * Metodo que obtiene la lista de peticiones a mostrar en la bandeja
	 * de peticiones correspondiente.
	 * 
	 * @param pageSize
	 *            Numero de peticiones a mostrar por pagina.
	 * @param pageActual
	 *            Posicion de la pagina actual respecto al total.
	 * @param orderAttribute
	 *            Indica el atributo o columna de la tabla de peticiones que se
	 *            usara para ordenarlas.
	 * @param order
	 *            Indica que la ordenacion de las peticiones debe ser
	 *            ascendente.
	 * @param userDTO
	 *            Objeto que contiene la informacion del usuario que
	 *            esta dado de alta en el sistema.
	 * @param searchFilter
	 *            Valor introducido por el usuario en el filtro de
	 *            busqueda de peticiones.
	 * @param labelFilter
	 *            Valor para filtrar las peticiones segun su etiqueta.
	 * @param applicationFilter
	 *            Valor para filtrar las peticiones segun la
	 *            aplicacion a la que pertenecen.
	 * @param initDateFilter
	 *            Fecha de inicio para el filtro
	 * @param endDateFilter
	 *            Fecha de fin para el filtro
	 * @param requestFilter
	 *            Cadena de texto que representa el filtro de las peticiones a
	 *            mostrar.
	 * @param job
	 *            Objeto que define un rol de usuario en el caso de haber hecho
	 *            login con el mismo.
	 * @return Lista de peticiones filtradas en base a los parametros de
	 *         entrada.
	 */
	public List<AbstractBaseDTO> queryListPaginated(int pageSize, int pageActual, String orderAttribute, String order,
			PfUsersDTO userDTO, PfGroupsDTO groupDTO, String searchFilter, String labelFilter, String applicationFilter,
			Date initDateFilter, Date endDateFilter, String requestFilter, PfUsersDTO job, String filtroTipo,
			boolean hasValidator, boolean noValidadas, List <PfApplicationsDTO> appValidators) {
		List<AbstractBaseDTO> result = null;

		boolean isSent = false;
		if (Constants.MESSAGES_SENT.equals(requestFilter) || Constants.MESSAGES_SENT_FINISHED.equals(requestFilter)
				|| Constants.MESSAGES_GROUP_SENT.equals(requestFilter)
				|| Constants.MESSAGES_GROUP_SENT_FINISHED.equals(requestFilter)
				|| Constants.MESSAGES_GROUP_EXPIRED.equals(requestFilter)
				|| Constants.MESSAGES_INVITED.equals(requestFilter) || Constants.MESSAGES_SENT_CANCELLED.equals(requestFilter)) {
			isSent = true;
			hasValidator = false;
		}

		// Define la lista de etiquetas a buscar
		List<String> tagList = new ArrayList<String>();
		tagList.add(Constants.C_TAG_NONE);
		if (hasValidator) {
			if (Constants.MESSAGES_UNRESOLVED.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_AWAITING_PASSED);
			}
		} else if (noValidadas) {
			tagList.add(Constants.C_TAG_NEW);
			tagList.add(Constants.C_TAG_READ);
		} else {
			
			if (Constants.MESSAGES_CANCELLED.equals(requestFilter) 
					|| Constants.MESSAGES_SENT_CANCELLED.equals(requestFilter)){
				tagList.add(Constants.C_TAG_ANULLED_TYPE);
				tagList.add(Constants.C_TAG_REJECTED);
				tagList.add(Constants.C_TAG_REMOVED);
			}else if (Constants.MESSAGES_UNRESOLVED.equals(requestFilter)
					|| Constants.MESSAGES_REDACTION.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_NEW);
				tagList.add(Constants.C_TAG_READ);
				tagList.add(Constants.C_TAG_AWAITING_PASSED);
			} else if (Constants.MESSAGES_FINISHED.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_SIGNED);
				tagList.add(Constants.C_TAG_REJECTED);
				tagList.add(Constants.C_TAG_PASSED);
				tagList.add(Constants.C_TAG_REMOVED);
			} else if (Constants.MESSAGES_AWAITING.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_AWAITING);
			} else if (Constants.MESSAGES_EXPIRED.equals(requestFilter)
					|| Constants.MESSAGES_GROUP_EXPIRED.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_EXPIRED);
			} else if (Constants.MESSAGES_SIGNED.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_SIGNED);
				tagList.add(Constants.C_TAG_PASSED);
			} else if (Constants.MESSAGES_REJECTED.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_REJECTED);
			} else if (Constants.MESSAGES_SENT_FINISHED.equals(requestFilter)
					|| Constants.MESSAGES_SENT.equals(requestFilter)
					|| Constants.MESSAGES_GROUP_SENT_FINISHED.equals(requestFilter)
					|| Constants.MESSAGES_GROUP_SENT.equals(requestFilter)) {
				tagList.add(Constants.C_TAG_NEW);
				tagList.add(Constants.C_TAG_READ);
				tagList.add(Constants.C_TAG_AWAITING);
				tagList.add(Constants.C_TAG_AWAITING_PASSED);
			} 
		}

		// Peticiones que son enviadas, y expiradas de grupo
		if (isSent) {
			result = requestDAO.queryRequestSentListPaginated(pageSize, pageActual, orderAttribute, order, userDTO,
					groupDTO, searchFilter, labelFilter, applicationFilter, initDateFilter, endDateFilter, tagList,
					requestFilter, filtroTipo, appValidators);
			// Peticiones recibidas
		} else {
			result = requestDAO.queryRequestListPaginated(pageSize, pageActual, orderAttribute, order, userDTO,
					searchFilter, labelFilter, applicationFilter, initDateFilter, endDateFilter, tagList, job,
					requestFilter, filtroTipo, appValidators);
		}
		
		return result;
	}

	/**
	 * Recupera una peticion a partir de un codigo hash, un
	 * usuario y un cargo.
	 * 
	 * @param request
	 *            El codigo hash de la peticion a buscar
	 * @param user
	 *            Objeto que contiene la informacion del usuario al que
	 *            pertenece la peticion.
	 * @param job
	 *            Objeto que define un cargo al que pertenece la
	 *            peticion.
	 * @return La peticion con el hash, usuario y/o cargo asociados.
	 */
	public PfRequestsDTO queryRequest(PfRequestsDTO request, PfUsersDTO user, PfUsersDTO job) {
		PfRequestsDTO result = queryRequestHash(request.getChash());
		
		return result;
	}

	/**
	 * Recupera una peticion a partir de un codigo hash.
	 * 
	 * @param request
	 *            El codigo hash de la peticion a buscar
	 * @return La peticion que tiene el codigo hash especicificado
	 */
	public PfRequestsDTO queryRequestHash(String request) {
		return (PfRequestsDTO) baseDAO.queryElementOneParameter("request.requestDataHash", "hash", request);
	}

	/**
	 * Recupera la peticion de texto a partir de una peticion, es
	 * igual que la peticion pero con solo los campos de codigo y texto
	 * de la peticion
	 * 
	 * @param request
	 *            La peticion
	 * @return La peticion de texto
	 */
	public PfRequestsTextDTO queryRequestText(AbstractBaseDTO request) {
		return requestDAO.queryRequestText(request);
	}

	/**
	 * Comprueba si existen determinadas etiquetas de una petición para otros
	 * usuarios.
	 * 
	 * @param request
	 *            Hash de la petición
	 * @param tags
	 *            Etiquetas de las que se quiere saber si existen para otros
	 *            usuarios.
	 * @param user
	 *            Usuario para el que no se quieren recuperar las etiquetas.
	 * @return true si el número de filas encontradas es mayor que 0, false en
	 *         caso contrario.
	 */
	public boolean queryTagsOtherUsers(String request, List<String> tags, PfUsersDTO user) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("chash", request);
		parameters.put("ctag", tags);
		parameters.put("user", user);

		long num = baseDAO.queryCount("request.otherRequestTagsFromHash", parameters);

		return num > 0;
	}

	/**
	 * Recorre la lista de etiquetas peticion, mirando si las peticiones
	 * asociadas tienen una acción previa de otro usuario. Si así fuera, pone el
	 * atributo accionPrevia de pfRequestDTO a true
	 */
	public void fillAccionPrevia(RequestTagListDTO reqTagList, PfUsersDTO user) {
		for (int i = 0; i < reqTagList.size(); i++) {
			PfRequestTagsDTO reqTag = (PfRequestTagsDTO) reqTagList.get(i);
			reqTag.getPfRequest().setAccionesPrevia(checkAccionPreviaRequest(reqTag.getPfRequest().getChash(), user));
		}
	}

	/**
	 * Método que calcula si sobre una petición se han hecho acciones previas
	 * 
	 * @param request
	 *            Petición
	 * @param user
	 *            Lista de etiquetas
	 * @return Acción previa
	 */
	public boolean checkAccionPreviaRequest(String request, PfUsersDTO user) {
		List<String> tags = new ArrayList<String>();
		tags.add("FIRMADO");
		tags.add("VISTOBUENO");
		return queryTagsOtherUsers(request, tags, user);
	}

	/**
	 * Método que recupera la información de las líneas de firma de una petición
	 * 
	 * @param requestHash
	 *            Identificador hash de la petición
	 * @return Petición que contiene las líneas de firma
	 */
	public PfRequestsDTO queryRequestSignLines(String requestHash) {
		return (PfRequestsDTO) baseDAO.queryElementOneParameter("request.requestHashSigners", "hash",
				requestHash);
	}
	
	public PfRequestsDTO queryRequestSignLinesInvited(String requestHash) {
		return (PfRequestsDTO) baseDAO.queryElementOneParameter("request.requestHashSignersInvited", "hash",
				requestHash);
	}

	/**
	 * Metodo que almacena en base de datos toda la informacion
	 * referente a una nueva peticion.
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que se va a guardar.
	 * @param requestTextDTO
	 *            Texto de la peticion que se va a guardar.
	 * @param requestDocumentList
	 *            Lista de documentos de la peticion.
	 * @param requestFileList
	 * @param destinatarios
	 *            Listado de firmantes de la peticion.
	 * @param signType
	 *            Valor que define el tipo de firma.
	 * @param notices
	 *            Notificaciones que se asocian a la peticion.
	 * @param remitter
	 * @param applicationDTO
	 *            Objeto que define la informacion de la
	 *            aplicacion a la que se asocia la peticion.
	 * @param stateTags
	 *            Etiquetas que se pueden asociar a la peticion.
	 * @throws CustodyServiceException
	 * @throws IOException
	 */
	@Transactional(readOnly = false, rollbackFor = { CustodyServiceException.class, IOException.class })
	public void saveRequest(PfRequestsDTO requestDTO, PfRequestsTextDTO requestTextDTO,
			List<PfDocumentsDTO> requestDocumentList, List<File> requestFileList, List<UserEnvelope> destinatarios,
			String signType, String[] notices, PfUsersDTO remitter, PfGroupsDTO groupRemitter,
			PfApplicationsDTO applicationDTO, Map<String, PfTagsDTO> stateTags, PfDocumentScopesDTO documentScope)
					throws CustodyServiceException, IOException {

		//Invitación
		if(requestDTO.getLinvited() != null && requestDTO.getLinvited()){
			baseDAO.insertOrUpdate(requestDTO.getInvitedUser());
		}
		
		// Request
		insertRequest(requestDTO, requestTextDTO, signType, applicationDTO);

		// Check Signers and signLines
		insertSignersAndNewTag(requestDTO, destinatarios, stateTags);

		// Remitter
		insertRemitter(requestDTO, remitter, groupRemitter, notices);

		// Notices
		if (notices != null && notices.length > 0) {
			insertNotices(requestDTO, notices);
		}

		// Upload files
		insertDocuments(requestDTO, requestDocumentList, requestFileList, documentScope);
	}
	
	@Transactional(readOnly = false, rollbackFor = { CustodyServiceException.class, IOException.class })
	public void saveRequest(PfRequestsDTO requestDTO, Set<PfSignLinesDTO> signLines, Set<PfRequestTagsDTO> reqTags)
					throws CustodyServiceException, IOException {
		//Petición
		baseDAO.insertOrUpdate(requestDTO);
		Iterator<PfSignLinesDTO> itSignLines = signLines.iterator();
		while(itSignLines.hasNext()){
			baseDAO.insertOrUpdate(itSignLines.next());
		}
		Iterator<PfRequestTagsDTO> itReqTags = reqTags.iterator();
		while(itReqTags.hasNext()){
			baseDAO.insertOrUpdate(itReqTags.next());
		}
		
		
	}

	/**
	 * Chequea que los firmantes de la cadena que pasamos como parametro,
	 * sean usuarios validos
	 * 
	 * @param signers
	 *            firmantes una cadena separados por ';'
	 * @return devuelve true si todos los firmantes de la cadena exiten en bbdd,
	 *         false en caso contrario
	 */
	public boolean checkRequest(String signers) {
		boolean result = true;
		// Check Signers and signLines
		signers = signers.toUpperCase();
		String[] signersArray = signers.split(";");
		for (int i = 0; i < signersArray.length; i++) {
			signersArray[i] = signersArray[i].trim();
		}
		List<AbstractBaseDTO> usersDTO = baseDAO.queryListOneParameterList("request.usersNameAndSurnames",
				"nameAndSurnames", signersArray);

		if (signersArray.length != usersDTO.size()) {
			result = false;
		}
		return result;
	}

	/**
	 * Método que devuelve todas las peticiones existen que tienen una
	 * determinada etiqueta
	 * 
	 * @param tag
	 *            Etiqueta
	 * @return Listado de peticiones etiquetadas
	 */
	public List<AbstractBaseDTO> queryRequestsTaggedWith(String[] tags) {
		List<AbstractBaseDTO> requestNotTagged = (ArrayList<AbstractBaseDTO>) baseDAO
				.queryListOneParameterList("request.requestsTagged", "tags", tags);
		return requestNotTagged;
	}

	/**
	 * Método que devuelve todo los usuarios existentes que cumplan un filtro
	 * 
	 * @param find
	 *            Filtro
	 * @param findProvince
	 *            Filtro de provincia
	 * @return Lista de usuarios que cumplen el filtro
	 */
	public List<AbstractBaseDTO> queryUsersComplete(String find, String findProvince) {
		Map<String, Object> params = new HashMap<String, Object>();
		String findAux = find.toUpperCase();
		findAux = "%" + findAux + "%";
		params.put("find", findAux);
		params.put("findProvince", findProvince);
		return baseDAO.queryListMoreParameters("request.usersCompleteWithProvince", params);
	}

	public List<AbstractBaseDTO> queryUsersCompleteWithoutVisibles(String find, String findProvince) {
		Map<String, Object> params = new HashMap<String, Object>();
		String findAux = find.toUpperCase();
		findAux = "%" + findAux + "%";
		params.put("find", findAux);
		params.put("findProvince", findProvince);
		return baseDAO.queryListMoreParameters("request.usersCompleteWithProvinceWithoutVisible", params);
	}

	/**
	 * Metodo que devuelve la lista de usuarios que contienen en su
	 * nombre completo la cadena de texto de busqueda, de un tipo determinado y
	 * provincia determinada, excluyendo al usuario pasado como parámetro. Si
	 * cualquiera de estos parámetros viene a vacío se ignora en el criterio de
	 * búsqueda.
	 * 
	 * @param user
	 *            Usuario que se quiere excluir de la búsqueda.
	 * @param userNameOrId
	 *            Cadena de texto a buscar en el nombre.
	 * @param typeUser
	 *            tipo del usuario: USUARIO o CARGO
	 * @param province
	 *            código de la provincia.
	 * @param provinceUser
	 *            código de la provincia del usuario que está usando la
	 *            aplicación.
	 * @return Lista de objetos que cumplen con el filtro.
	 */
	public List<AbstractBaseDTO> querySelectableUsersCompleteNoWebServiceUsers(PfUsersDTO user,
			String userNameOrIdentifier, String typeUser, String province, String provinceUser) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user.getPrimaryKey());
		parameters.put("userNameOrId", "%" + userNameOrIdentifier.toUpperCase() + "%");
		parameters.put("typeUser", typeUser);
		parameters.put("province", province);
		parameters.put("provinceUser", provinceUser);
		return baseDAO.queryListMoreParameters("request.usersSelectableCompleteNoWebServiceUsers", parameters);
	}

	/**
	 * Método que devuelve el número de usuarios que contienen en su nombre
	 * completo la cadena de texto de busqueda, de un tipo determinado y
	 * provincia determinada, excluyendo al usuario pasado como parámetro. Si
	 * cualquiera de estos parámetros viene a vacío se ignora en el criterio de
	 * búsqueda.
	 * 
	 * @param user
	 *            Usuario que se quiere excluir de la búsqueda.
	 * @param userNameOrId
	 *            Cadena de texto a buscar en el nombre.
	 * @param typeUser
	 *            tipo del usuario: USUARIO o CARGO
	 * @param province
	 *            código de la provincia.
	 * @param provinceUser
	 *            código de la provincia del usuario que está usando la
	 *            aplicación.
	 * @return Lista de objetos que cumplen con el filtro.
	 */
	public Long querySelectableUsersCompleteNoWebServiceUsersCount(PfUsersDTO user,
			String userNameOrIdentifier, String typeUser, String province, String provinceUser) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user.getPrimaryKey());
		parameters.put("userNameOrId", "%" + userNameOrIdentifier.toUpperCase() + "%");
		parameters.put("typeUser", typeUser);
		parameters.put("province", province);
		parameters.put("provinceUser", provinceUser);
		return baseDAO.queryCount("request.usersSelectableCompleteNoWebServiceUsersCount", parameters);
	}

	/**
	 * Método que devuelve la lista de usuarios del sistema (excepto el pasado
	 * como parámetro) filtrada y paginada
	 * 
	 * @param user
	 *            Usuario que se quiere excluir de la búsqueda.
	 * @param userNameOrId
	 *            Cadena de texto a buscar en el nombre.
	 * @param typeUser
	 *            tipo del usuario: USUARIO o CARGO
	 * @param province
	 *            código de la provincia.
	 * @param provinceUser
	 *            código de la provincia del usuario que está usando la
	 *            aplicación.
	 * @param firstPosition
	 *            Posición del primer resultado de la lista
	 * @param maxResults
	 *            Número máximo de resultados
	 * @return Lista de objetos que cumplen con el filtro.
	 */
	public List<AbstractBaseDTO> queryPaginatedSelectableUsersCompleteNoWebServiceUsers(PfUsersDTO user, 
			String userNameOrIdentifier, String typeUser, String province, String provinceUser, int firstPosition,
			int maxResults) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user.getPrimaryKey());
		parameters.put("userNameOrId", "%" + userNameOrIdentifier.toUpperCase() + "%");
		parameters.put("typeUser", typeUser);
		parameters.put("province", province);
		parameters.put("provinceUser", provinceUser);
		return baseDAO.queryPaginatedListMoreParameters("request.usersSelectableCompleteNoWebServiceUsers", parameters,
				firstPosition, maxResults);
	}

	/**
	 * Metodo que devuelve la lista de usuarios que contienen los
	 * usuarios que contienen en su nombre completo la cadena de texto de
	 * busqueda, de un tipo determinado y provincia determinada. Si cualquiera
	 * de estos parámetros viene a vacío se ignora en el criterio de búsqueda.
	 * 
	 * @param userNameOrId
	 *            Cadena de texto a buscar en el nombre.
	 * @param typeUser
	 *            tipo del usuario: USUARIO o CARGO
	 * @param province
	 *            código de la provincia.
	 * @return Lista de objetos que cumplen con el filtro.
	 */
	public List<AbstractBaseDTO> queryUsersCompleteNoWebServiceUsers(String userNameOrIdentifier, String typeUser, String province, String idPortafirmas, String provinceUser) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userNameOrId", "%" + userNameOrIdentifier.toUpperCase() + "%");
		parameters.put("typeUser", typeUser);
		parameters.put("province", province);
		parameters.put("idPortafirmas", idPortafirmas);
		parameters.put("provinceUser", provinceUser);

		return baseDAO.queryListMoreParameters("request.usersCompleteNoWebServiceUsers", parameters);
	}

	/**
	 * Método que devuelve la lista de cargos que contienen la cadena de texto a
	 * buscar.
	 * 
	 * @param find
	 *            Cadena de texto que se busca como parte del nombre completo de
	 *            los cargos del Portafirmas.
	 * @param userPk
	 *            Clave primaria del cargo
	 * @return Listado de cargos que contienen la cadena de texto.
	 */
	public List<AbstractBaseDTO> queryJobsComplete(String find, Long userPk) {
		String findAux = find.toUpperCase();
		findAux = "%" + findAux + "%";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("find", findAux);
		parameters.put("pk", userPk);
		return baseDAO.queryListMoreParameters("request.jobsComplete", parameters);
	}

	/**
	 * Método que devuelve la lista de cargos con sus provincias que contienen
	 * la cadena de texto a buscar.
	 * 
	 * @param find
	 *            Cadena de texto que se busca como parte del nombre completo de
	 *            los cargos del Portafirmas.
	 * @param userPk
	 *            Clave primaria del cargo
	 * @return Listado de cargos que contienen la cadena de texto.
	 */
	public List<AbstractBaseDTO> queryJobsCompleteWithProvince(String find, Long userPk) {
		String findAux = find.toUpperCase();
		findAux = "%" + findAux + "%";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("find", findAux);
		parameters.put("pk", userPk);
		return baseDAO.queryListMoreParameters("request.jobsCompleteWithProvince", parameters);
	}

	/**
	 * Metodo que devuelve una lista de objetos UserEnvelope que
	 * contienen los usuarios que contienen en su nombre completo la cadena de
	 * texto de busqueda, de un tipo determinado y provincia determinada. Si
	 * cualquiera de estos parámetros viene a vacío se ignora en el criterio de
	 * búsqueda.
	 * 
	 * @param userNameOrId
	 *            Cadena de texto a buscar en el nombre.
	 * @param typeUser
	 *            tipo del usuario: USUARIO o CARGO
	 * @param province
	 *            código de la provincia del filtro.
	 * @param provinceUser
	 *            código de la provincia del usuario.
	 * @return Lista de objetos UserEnvelope que cumplen con el filtro.
	 */
	public List<UserEnvelope> queryUsersEnvelopeCompleteNoWebServiceUsers(String userNameOrId, String typeUser,
			String province, String idPortafirmas, String provinceUser) {
		List<UserEnvelope> ret = new ArrayList<UserEnvelope>();
		List<AbstractBaseDTO> listAux = queryUsersCompleteNoWebServiceUsers(userNameOrId, typeUser, province, idPortafirmas, provinceUser);
		if (listAux != null && !listAux.isEmpty()) {

			// Se filtran los usuarios eliminando los que sean cargos que no tengan un usuario vigente
			List<AbstractBaseDTO> usersSinCargosNoVigentes = new ArrayList<AbstractBaseDTO>();
			for (AbstractBaseDTO dto : listAux) {
				PfUsersDTO autocompletedUser = (PfUsersDTO) dto;
				if (autocompletedUser.isJob()){
					List<AbstractBaseDTO> usersJobsAbstract = baseDAO.queryListOneParameter("request.queryUsersFromJob", "job", autocompletedUser);
					
					for (AbstractBaseDTO userJobsAbstract : usersJobsAbstract) {
						PfUsersJobDTO userJob = (PfUsersJobDTO) userJobsAbstract;
						Date fechaActual = new Date();
						if ((userJob.getFstart() == null || userJob.getFstart().before(fechaActual)) &&
							((userJob.getFend() == null) || (userJob.getFend() != null && userJob.getFend().after(fechaActual)))) {					
							usersSinCargosNoVigentes.add(autocompletedUser);
							break;
						}
					}
				} else {
					usersSinCargosNoVigentes.add(autocompletedUser);
				}
			}
			
			
			//Se transforman
			for (AbstractBaseDTO abstractBaseDTO : usersSinCargosNoVigentes) {
				if (abstractBaseDTO != null) {
					UserEnvelope env = new UserEnvelope((PfUsersDTO) abstractBaseDTO);
					env.setSelected(false);
					ret.add(env);
				}
			}
		}
		return ret;
	}

	/**
	 * Metodo que devuelve una lista de objetos UserEnvelope que
	 * contienen los usuarios, a excepción del pasado como parámetro, que
	 * contienen en su nombre completo la cadena de texto de busqueda, de un
	 * tipo determinado y provincia determinada. Si cualquiera de estos
	 * parámetros viene a vacío se ignora en el criterio de búsqueda.
	 * 
	 * @param user
	 *            usuario que se desea excluir de la búsqueda.
	 * @param userNameOrId
	 *            Cadena de texto a buscar en el nombre.
	 * @param typeUser
	 *            tipo del usuario: USUARIO o CARGO
	 * @param province
	 *            código de la provincia.
	 * @param provinceUser
	 *            código de la provincia del usuario que está usando la
	 *            aplicación.
	 * @return Lista de objetos UserEnvelope que cumplen con el filtro.
	 */
	public List<UserEnvelope> querySelectableUsersEnvelopeCompleteNoWebServiceUsers(PfUsersDTO user,
			String userNameOrId, String typeUser, String province, String provinceUser) {
		List<UserEnvelope> ret = new ArrayList<UserEnvelope>();
		List<AbstractBaseDTO> listAux = querySelectableUsersCompleteNoWebServiceUsers(user, userNameOrId, typeUser,
				province, provinceUser);
		if (listAux != null && !listAux.isEmpty()) {
			for (AbstractBaseDTO abstractBaseDTO : listAux) {
				if (abstractBaseDTO != null) {
					UserEnvelope env = new UserEnvelope((PfUsersDTO) abstractBaseDTO);
					ret.add(env);
				}
			}
		}
		return ret;
	}

	/**
	 * Metodo que devuelve una lista de objetos UserEnvelope que
	 * contienen los usuarios, a excepción del pasado como parámetro, que
	 * contienen en su nombre completo la cadena de texto de busqueda, de un
	 * tipo determinado y provincia determinada. Si cualquiera de estos
	 * parámetros viene a vacío se ignora en el criterio de búsqueda.
	 * 
	 * @param user
	 *            usuario que se desea excluir de la búsqueda.
	 * @param userNameOrId
	 *            Cadena de texto a buscar en el nombre.
	 * @param typeUser
	 *            tipo del usuario: USUARIO o CARGO
	 * @param province
	 *            código de la provincia.
	 * @param provinceUser
	 *            código de la provincia del usuario que está usando la
	 *            aplicación.
	 * @param firstPosition
	 *            posición del primer resultado
	 * @param maxResults
	 *            número máximo de resultados
	 * @return Lista de objetos UserEnvelope que cumplen con el filtro.
	 */
	public List<UserEnvelope> queryPaginatedSelectableUsersEnvelopeCompleteNoWebServiceUsers(PfUsersDTO user,
			String userNameOrId, String typeUser, String province, String provinceUser, int firstPosition,
			int maxResults) {
		List<UserEnvelope> ret = new ArrayList<UserEnvelope>();
		List<AbstractBaseDTO> listAux = queryPaginatedSelectableUsersCompleteNoWebServiceUsers(user, userNameOrId,
				typeUser, province, provinceUser, firstPosition, maxResults);
		if (listAux != null && !listAux.isEmpty()) {
			for (AbstractBaseDTO abstractBaseDTO : listAux) {
				if (abstractBaseDTO != null) {
					UserEnvelope env = new UserEnvelope((PfUsersDTO) abstractBaseDTO);
					ret.add(env);
				}
			}
		}
		return ret;
	}

	public List<AbstractBaseDTO> queryUsers(String signers) {

		List<AbstractBaseDTO> result = baseDAO.queryListOneParameter("request.userAll", null, null);

		if (null != signers && !"".equals(signers)) {

			signers = signers.toUpperCase();
			String[] signersArray = signers.split(";");
			List<AbstractBaseDTO> selectedUsers = baseDAO.queryListOneParameterList("request.usersNameAndSurnames",
					"nameAndSurnames", signersArray);

			int index = -1;
			for (AbstractBaseDTO userDTO : selectedUsers) {
				index = result.indexOf(userDTO);
				if (index > -1) {
					result.get(index).setSelected(true);
				}
			}
		}

		return result;
	}

	/**
	 * recupera una lista de objetos de aplicaciones del usario que pasamos como
	 * parametro con el tipo de etiqueta 'ESTADO'.
	 * 
	 * @param usuario
	 * @return la lista de aplicaciones del usuario.
	 */
	public List<AbstractBaseDTO> queryApplicationMenu(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("request.applicationsUser", "user", userDTO);
	}

	public List<AbstractBaseDTO> queryApplicationMenuSent() {
		return baseDAO.queryListOneParameter("request.applicationsSent", null, null);
	}

	/**
	 * Metodo que cambia el estado de una peticion
	 * marcandola como rechazada.
	 * 
	 * @param userDTO
	 *            Objeto que contiene la informacion del usuario que
	 *            rechaza la aplicacion.
	 * @param rejectList
	 *            Lista de peticiones a rechazar por el usuario.
	 * @param textRejection
	 *            Comentario sobre el rechazo de la peticion.
	 */
	@Transactional
	public void insertReject(PfUsersDTO userDTO, List<AbstractBaseDTO> rejectList, String textRejection) {
		tagBO.changeStateToRejectedList(rejectList, textRejection, userDTO);
	}

	@Transactional
	public void insertRemove(PfUsersDTO userDTO, PfRequestTagsDTO removedRequest, String textRemove) {
		tagBO.changeStateToRemoved(removedRequest, textRemove, userDTO, false);
	}
	

	@Transactional
	public void insertRemoveRequest(PfUsersDTO userDTO, PfRequestsDTO removedRequest, String textRemove) {
		tagBO.changeStateToRemovedRequest(removedRequest, textRemove, userDTO, false);
	}

	@Transactional
	public void insertExpire(PfRequestsDTO expiredRequest) {
		tagBO.changeStateToExpired(expiredRequest);
	}


//	@Transactional
//	public void insertAnulled(PfRequestsDTO anulledRequest, boolean anulled) {
//		tagBO.changeStateToAnulled(anulledRequest, anulled);
//	}

	
	/**
	 * Método que comprueba si una petición ha sido enviada
	 * 
	 * @param request
	 *            Petición a comprobar
	 * @return True si NO ha sido enviada
	 */
	public boolean isSendedRequest(PfRequestsDTO request) {
		boolean result = true;
		log.debug("Checking request not sended");

		if (request.getChash() != null && !"".equals(request.getChash())) {
			List<PfRequestTagsDTO> requestTags = baseDAO.queryListOneParameter("request.allRequestTagsFromHash",
					"chash", request.getChash());
			if (requestTags.isEmpty()){
				return false;
			}
		}

		return result;
	}
	
	/**
	 * Método que comprueba si una petición se encuentra en un estado final
	 * 
	 * @param request
	 *            Petición a comprobar
	 * @return True si NO se encuentra en un estado final
	 */
	public boolean isFinishedRequest(PfRequestsDTO request) {
		boolean result = true;
		log.debug("Checking request not finished");

		// Si la petición tiene alguna etiqueta del tipo "NUEVA", "LEIDO",
		// "EN ESPERA" o "VALIDADO" no está finalizada.
		if (request.getChash() != null && !"".equals(request.getChash())) {
			List<PfRequestTagsDTO> requestTags = baseDAO.queryListOneParameter("request.allRequestTagsFromHash",
					"chash", request.getChash());

			for (PfRequestTagsDTO requestTag : requestTags) {
				if (requestTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING)
						|| requestTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING_PASSED)
						|| requestTag.getPfTag().getCtag().equals(Constants.C_TAG_NEW)
						|| requestTag.getPfTag().getCtag().equals(Constants.C_TAG_READ)) {
					result = false;
				}
			}
		}

		return result;
	}


	/**
	 * Método que obtiene un ámbito de documento a partir de su PK
	 * 
	 * @param scopeId
	 *            PK del ámbito
	 * @return Ámbito de documento
	 */
	public PfDocumentScopesDTO getDocumentScopeById(Long scopeId) {
		return (PfDocumentScopesDTO) baseDAO.queryElementOneParameter("request.queryScopeById", "idScope", scopeId);
	}

	/**
	 * Método que obtiene el ámbito interno de los documentos
	 * 
	 * @return Ámbito interno
	 */
	public PfDocumentScopesDTO getInternalDocumentScope() {
		return (PfDocumentScopesDTO) baseDAO.queryElementOneParameter("request.queryInternalScope", null, null);
	}

	/**
	 * Método que obtiene el ámbito externo de los documentos
	 * 
	 * @return Ámbito externo
	 */
	public PfDocumentScopesDTO getExternalDocumentScope() {
		return (PfDocumentScopesDTO) baseDAO.queryElementOneParameter("request.queryExternalScope", null, null);
	}

	/**
	 * Método que obtiene un usuario o cargo a partir de su DNI
	 * 
	 * @param dni
	 *            DNI
	 * @return Usuario o cargo
	 */
	public PfUsersDTO getUserOrJobByDNI(String dni) {
		return (PfUsersDTO) baseDAO.queryElementOneParameter("request.usersAndJobDni", "dni", dni);
	}

	/*
	 * AUXILIARY METHODS
	 */

	/**
	 * Metodo que guarda en la base de datos los documentos de una
	 * peticion.
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que contiene los
	 *            documentos.
	 * @param requestDocumentList
	 *            Lista de documentos de la peticion.
	 * @param requestFileList
	 *            Lista de ficheros de los documentos de la peticion.
	 * @throws CustodyServiceException
	 * @throws IOException
	 */
	private void insertDocuments(PfRequestsDTO requestDTO, List<PfDocumentsDTO> requestDocumentList,
			List<File> requestFileList, PfDocumentScopesDTO documentScope) throws CustodyServiceException, IOException {

		Iterator<PfDocumentsDTO> itDocumentsDTO = requestDocumentList.iterator();
		PfDocumentsDTO documentDTO = null;
		PfFilesDTO fileDTO = null;

		// Obtiene el ámbito del documento
		PfDocumentScopesDTO scope = getDocumentScopeById(documentScope.getPrimaryKey());

		// Si no se ha definido ámbito se pone por defecto como interno
		if (scope == null) {
			scope = (PfDocumentScopesDTO) baseDAO.queryElementOneParameter("request.queryInternalScope", null, null);
		}

		for (File file : requestFileList) {
			documentDTO = itDocumentsDTO.next();
			documentDTO.setChash(Util.getInstance().createHash());
			documentDTO.setPfRequest(requestDTO);
			documentDTO.setPfDocumentScope(scope);

			InputStream inputCheck = new FileInputStream(file);
			InputStream inputStore = new FileInputStream(file);
			fileDTO = insertFile(documentDTO, inputStore, inputCheck, file.length());
			documentDTO.setPfFile(fileDTO);
			baseDAO.insertOrUpdate(documentDTO);
		}
	}

	/**
	 * Metodo que inserta notificaciones de avisos cuando se cumplen
	 * condiciones sobre una peticion (avisar cuando se ha leido, firmado
	 * o rechazado).
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que se va a guardar.
	 * @param notices
	 *            Listado de avisos que se asocian a la peticion.
	 */
	private void insertNotices(PfRequestsDTO requestDTO, String[] notices) {
		PfNoticeRequestsDTO noticeRequestDTO = null;

		for (String notice : notices) {
			log.info("VALOR DE NOTICE: " + notice);
			noticeRequestDTO = new PfNoticeRequestsDTO();
			noticeRequestDTO.setPfRequest(requestDTO);

			String noticeAux = null;
			if ("noticeRead".equals(notice)) {
				noticeAux = Constants.C_TAG_READ;
			} else if ("noticeReject".equals(notice)) {
				noticeAux = Constants.C_TAG_REJECTED;
			} else if ("noticeSign".equals(notice)) {
				noticeAux = Constants.C_TAG_SIGNED;
			} else if ("noticePassed".equals(notice)) {
				noticeAux = Constants.C_TAG_PASSED;
			}
			log.info("VALOR DE noticeAux: " + noticeAux);
			if (applicationVO.getStateTags().get(noticeAux) == null) {
				log.info("NO SE RECUPERA NADA");
			}
			noticeRequestDTO.setPfTag(applicationVO.getStateTags().get(noticeAux));
			baseDAO.insertOrUpdate(noticeRequestDTO);
		}
	}

	/**
	 * Metodo que añade el remitente a la peticion.
	 * Corresponde con el usuario dado de alta en el sistema.
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que tiene asociado al
	 *            nuevo remitente.
	 * @param remitter
	 *            Objeto que define al remitente de la peticion.
	 * @param groupRemitter
	 *            Objeto que define al grupo que manda la petición. Si es nulo
	 *            la petición es individual
	 * @param notices
	 *            Parametro que define si el remitente recibe
	 *            notificaciones al su correo electronico o no.
	 */
	private void insertRemitter(PfRequestsDTO requestDTO, PfUsersDTO remitter, PfGroupsDTO groupRemitter,
			String[] notices) {
		PfUsersRemitterDTO userRemitterDTO = new PfUsersRemitterDTO();
		if (notices != null && notices.length > 0) {
			userRemitterDTO.setLnotifyEmail(true);
		} else {
			userRemitterDTO.setLnotifyEmail(false);
		}
		userRemitterDTO.setLnotifyMobile(false);
		userRemitterDTO.setPfRequest(requestDTO);
		userRemitterDTO.setPfUser(remitter);
		userRemitterDTO.setEstadoParaRemitente(EstadosParaRemitente.ESTADO_DEFAULT);
		// Si es una petición redactada por un grupo se añade como remitente
		if (groupRemitter != null) {
			userRemitterDTO.setPfGroup(groupRemitter);
		}
		baseDAO.insertOrUpdate(userRemitterDTO);
	}
	
	

	/**
	 * Metodo que guarda los firmantes de una peticion en la base
	 * de datos y crea la nueva etiqueta de la peticion ('En espera' o
	 * 'Nueva')
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que contiene los
	 *            firmantes y la etiqueta a guardar.
	 * @param destinatarios
	 *            Lista de firmantes de la peticion.
	 * @param stateTags
	 *            Listado de etiquetas posibles a asignar a la peticion.
	 * @return The SignLineDTO
	 */
	private PfSignLinesDTO insertSignersAndNewTag(PfRequestsDTO requestDTO, List<UserEnvelope> destinatarios,
			Map<String, PfTagsDTO> stateTags) {

		Set<PfSignersDTO> signerList = new LinkedHashSet<PfSignersDTO>();
		PfSignLinesDTO signLineDTO = null;
		PfSignLinesDTO signLinePrev = null;
		PfUsersDTO userDTO = null;
		
		// Signers and signLines
		if(requestDTO.getLinvited() == null || !requestDTO.getLinvited()){
			for (UserEnvelope destinatario : destinatarios) {
				userDTO = getUserByPk(destinatario.getPk());
				userDTO.setPasser(destinatario.isPasser());	
				signLineDTO = generarLineaFirma(requestDTO, destinatario);					
				guardarLineaFirma(requestDTO, stateTags, signLineDTO, signLinePrev, userDTO);	
				signLinePrev = signLineDTO;	
				saveSigner(signerList, signLineDTO, userDTO);	
			}
		}else if(requestDTO.getLinvited()){
			for (UserEnvelope destinatario : destinatarios) {
				signLineDTO = generarLineaFirmaInvited(requestDTO, destinatario);			
				guardarLineaFirmaInvitado(requestDTO, stateTags, signLineDTO, signLinePrev, userDTO);
				signLinePrev = signLineDTO;				
				saveSigner(signerList, signLineDTO, null);
			}	
		}
			
		// Actualizamos los firmantes de línea de firma para que estén
		// disponibles en la transacción actual.
		if(signLineDTO != null){
			signLineDTO.setPfSigners(signerList);
		}
		
		return signLineDTO;
	}

	/**Guarda una linea de firma para un firmante invitado
	 * @param requestDTO
	 * @param stateTags
	 * @param signLineDTO
	 * @param signLinePrev
	 * @param userDTO
	 * @param invUserList
	 * @param invitedUser
	 */
	private void guardarLineaFirmaInvitado(PfRequestsDTO requestDTO, Map<String, PfTagsDTO> stateTags,
			PfSignLinesDTO signLineDTO, PfSignLinesDTO signLinePrev, PfUsersDTO userDTO) {
		
		List<PfInvitedUsersDTO> invUserList = new ArrayList<PfInvitedUsersDTO>();
		PfInvitedUsersDTO invitedUser = requestDTO.getInvitedUser();
		
		//Para que se puedan incluir 2 líneas de firma con el mismo destinatario, se deberia comentar el if
		if(!invUserList.contains(invitedUser)){
			invUserList.add(invitedUser);
			saveTagRequest(requestDTO, stateTags, signLineDTO, signLinePrev, userDTO);			
		//Se comenta para el tema de repetir el mismo destinatario
		} else {
			baseDAO.insertOrUpdate(signLineDTO);
		}
	}

	/**Guarda una linea de firma para un usuario
	 * @param requestDTO
	 * @param stateTags
	 * @param signLineDTO
	 * @param signLinePrev
	 * @param userDTO
	 * @param userList
	 */
	private void guardarLineaFirma(PfRequestsDTO requestDTO, Map<String, PfTagsDTO> stateTags,
			PfSignLinesDTO signLineDTO, PfSignLinesDTO signLinePrev, PfUsersDTO userDTO) {
		
		List<PfUsersDTO> userList = new ArrayList<PfUsersDTO>();
		
		//Para que se puedan incluir 2 líneas de firma con el mismo destinatario, se deberia comentar el if
		if (!userList.contains(userDTO)) {
			userList.add(userDTO);
			saveTagRequest(requestDTO, stateTags, signLineDTO, signLinePrev, userDTO);		
		//Se comenta para el tema de repetir el mismo destinatario
		} else {
			baseDAO.insertOrUpdate(signLineDTO);
		}
		
//		validatorUsersConfBO.checkAppValidate(userDTO, requestDTO);
	}
	
	/**Guarda una etiqueta de peticion para la linea de firma
	 * @param requestDTO
	 * @param stateTags
	 * @param signLineDTO
	 * @param signLinePrev
	 * @param userDTO
	 */
	private void saveTagRequest(PfRequestsDTO requestDTO, Map<String, PfTagsDTO> stateTags, PfSignLinesDTO signLineDTO,
			PfSignLinesDTO signLinePrev, PfUsersDTO userDTO) {
		PfTagsDTO tagDTO = null;
		boolean esFirmaEnCascada = requestDTO.getLcascadeSign();
		boolean esPrimeraLineaFirma = signLinePrev == null;
		boolean esUsuarioExterno = userDTO != null && userDTO.getPortafirmas() != null;

		if ((esFirmaEnCascada && !esPrimeraLineaFirma)) {
			signLineDTO.setPfSignLine(signLinePrev);
			tagDTO = stateTags.get(Constants.C_TAG_AWAITING);
		} else if (esUsuarioExterno) {
			tagDTO = stateTags.get(Constants.C_TAG_AWAITING);
		} else {
			tagDTO = stateTags.get(Constants.C_TAG_NEW);
		}

		baseDAO.insertOrUpdate(signLineDTO);
		PfRequestTagsDTO newRequestTag = null;
		tagBO.changeRequestTagUser(requestDTO, newRequestTag, signLineDTO, tagDTO, userDTO);		
//		validatorUsersConfBO.checkAppValidate(userDTO, requestDTO);
	}

	/**Metodo que genera una linea de firma
	 * @param requestDTO
	 * @param userDTO
	 * @param destinatario
	 * @return
	 */
	private PfSignLinesDTO generarLineaFirma(PfRequestsDTO requestDTO, UserEnvelope destinatario) {
		
		PfSignLinesDTO signLineDTO = new PfSignLinesDTO();
		
		if (destinatario.isPasser()) {
			signLineDTO.setCtype(Constants.C_TYPE_SIGNLINE_PASS);
		} else {
			signLineDTO.setCtype(Constants.C_TYPE_SIGNLINE_SIGN);
		}

		signLineDTO.setPfRequest(requestDTO);
		signLineDTO.setAccionFirmante(obtainAccionFirmanteByCode(destinatario.getAccionFirma()));
		return signLineDTO;
	}
	
	/**Metodo que genera una linea de firma para invitado
	 * @param requestDTO
	 * @param userDTO
	 * @param destinatario
	 * @return
	 */
	private PfSignLinesDTO generarLineaFirmaInvited (PfRequestsDTO requestDTO, UserEnvelope destinatario) {
		
		PfSignLinesDTO signLineDTO = new PfSignLinesDTO();
		
		if (destinatario.isPasser()) {
			signLineDTO.setCtype(Constants.C_TYPE_SIGNLINE_PASS);
		} else {
			signLineDTO.setCtype(Constants.C_TYPE_SIGNLINE_SIGN);
		}

		signLineDTO.setPfRequest(requestDTO);
		signLineDTO.setAccionFirmante(obtainAccionFirmanteByCode(destinatario.getAccionFirma()));
		return signLineDTO;
	}

	/**Se guarda el firmante y la linea de firma en BBDD
	 * @param signerList
	 * @param signLineDTO
	 * @param userDTO
	 */
	private void saveSigner(Set<PfSignersDTO> signerList, PfSignLinesDTO signLineDTO, PfUsersDTO userDTO) {
		
		PfSignersDTO signerDTO = new PfSignersDTO();		
		if (userDTO != null){			
			signerDTO.setPfUser(userDTO);
		}		
		signerDTO.setPfSignLine(signLineDTO);		
		baseDAO.insertOrUpdate(signerDTO);
		// Añadimos el firmante a la linea de firma
		signerList.add(signerDTO);
	}

	/**
	 * Metodo que inserta una nueva peticion en la base de datos.
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que se va a insertar.
	 * @param requestTextDTO
	 *            Texto de la peticion a insertar.
	 * @param signType
	 *            Valor que define el tipo de firma de la peticion.
	 * @param applicationDTO
	 *            Objeto que define la informacion de la
	 *            aplicacion a la que se asocia la peticion.
	 * @return El codigo hash que distingue un&iacute;vocamente a la
	 *         peticion insertada.
	 */
	private String insertRequest(PfRequestsDTO requestDTO, PfRequestsTextDTO requestTextDTO, String signType,
			PfApplicationsDTO applicationDTO) {

		requestDTO.setChash(Util.getInstance().createHash());
		requestDTO.setFentry(new Date());
		requestDTO.setLcascadeSign(false);
		requestDTO.setLfirstSignerSign(false);

		if (requestDTO.getFexpiration() != null) {
			requestDTO.setFexpiration(Util.getInstance().getDateWithOtherTime(requestDTO.getFexpiration(), 23, 59, 0));
		}

		if (Constants.SIGN_TYPE_CASCADE.equals(signType)) {
			requestDTO.setLcascadeSign(true);
		} else if ("signTypeFirstSigner".equals(signType)) {
			requestDTO.setLfirstSignerSign(true);
		}

		requestDTO.setPfApplication(applicationDTO);
		baseDAO.insertOrUpdate(requestDTO);
		if (requestTextDTO != null) {
			PfRequestsTextDTO reqTextAux = (PfRequestsTextDTO) baseDAO
					.queryElementOneParameter("request.requestTextHash", "hash", requestDTO.getChash());
			reqTextAux.setTrequest(requestTextDTO.getTrequest());
			baseDAO.update(reqTextAux);
			// requestDAO.updateRequestText(reqTextAux);
		}

		return requestDTO.getChash();
	}

	// TODO Query no implementada
	public List<AbstractBaseDTO> queryHistoricList(PfRequestsDTO request) {
		return baseDAO.queryListOneParameter("request.historicalAll", "request", request);
	}

	/**
	 * Lista de solicitudes del día del año anterior que se pueden pasar al
	 * histórico.
	 * 
	 * @return
	 */
	public List<AbstractBaseDTO> queryRequestsToHistoric() {

		Map<String, Object> filters = new HashMap<String, Object>();

		// Configuración de la lista de estados
		ArrayList<String> tags = new ArrayList<String>();
		tags.add(Constants.C_TAG_NEW);
		tags.add(Constants.C_TAG_READ);
		tags.add(Constants.C_TAG_AWAITING);
		tags.add(Constants.C_TAG_AWAITING_PASSED);
		filters.put("tags", tags);

		// Configuración del día
		Calendar dia = Calendar.getInstance();
		// Se calcula el día del año anterior y se trunca
		dia.set(dia.get(Calendar.YEAR) - 1, dia.get(Calendar.MONTH), dia.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		filters.put("dia", dia.getTime());
		// Se calcula el día siguiente del día de año anterior
		dia.set(dia.get(Calendar.YEAR), dia.get(Calendar.MONTH), dia.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
		filters.put("diaSiguiente", dia.getTime());

		return baseDAO.queryListMoreParameters("request.requestsToHistoric", filters);
	}

	/**
	 * Metodo que devuelve todos los documentos de una peticion.
	 * 
	 * @param requestsDTO
	 *            La peticion de la cual se quiere obtener los
	 *            documentos.
	 * @return Lista de documentos pertenecientes ala peticion de
	 *         entrada.
	 */
	public List<AbstractBaseDTO> getPfDocumentsByRequest(PfRequestsDTO requestsDTO) {
		List<AbstractBaseDTO> aux = baseDAO.queryListOneParameter("request.documentsAll", "request", requestsDTO);
		return aux;
	}

	/**
	 * Metodo que devuelve los comentarios asociados a una
	 * peticion.
	 * 
	 * @param requestsDTO
	 *            Peticion que tiene los comentarios asociados.
	 * @return Listado de comentarios asociados a la peticion.
	 */
	public List<AbstractBaseDTO> getPfCommentsByRequest(PfRequestsDTO requestsDTO) {
		List<AbstractBaseDTO> aux = baseDAO.queryListOneParameter("request.commentsAll", "request", requestsDTO);
		return aux;
	}

	/**
	 * Metodo que devuelve la lista de historicos de la
	 * peticion de entrada.
	 * 
	 * @param request
	 *            La peticion de la que se desea obtener el
	 *            historico.
	 * @return Lista de peticiones historico.
	 */
	public List<AbstractBaseDTO> getPfHistoricRequestByRequest(PfRequestsDTO requestsDTO) {
		List<AbstractBaseDTO> aux = baseDAO.queryListOneParameter("request.historicRequestAll", "request", requestsDTO);
		return aux;
	}

	/**
	 * Metodo que añade un comentario a una peticion.
	 * 
	 * @param request
	 *            Peticion a la que se añade el comentario.
	 * @param comment
	 *            Comentario que se desea añadir a la peticion.
	 * @param userDTO
	 *            Usuario que añade el comentario a la peticion.
	 */
	@Transactional
	public void insertRequestComment(PfRequestsDTO request, PfCommentsDTO comment, PfUsersDTO userDTO) {

		// añade el comentario a la peticion
		comment.setPfRequest(request);
		comment.setPfUser(userDTO);
		comment.setDsubject(Constants.C_TYPE_SUBJECT_COMMENT);

		baseDAO.insertOrUpdate(comment);

		// Actualiza la fecha de modificacion de la peticion
		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);
	}
	
	public PfRequestsDTO existsInvRequestsByUserMail(String eMail) {
		return (PfRequestsDTO) baseDAO.queryElementOneParameter("invitedRequest.byMail", "cMail", eMail);
	}

	/**
	 * Metodoq ue elimina un comentario de una peticion.
	 * 
	 * @param comment
	 *            Comentario a eliminar.
	 * @param request
	 *            Peticion de la que se elimina el comentario.
	 */
	@Transactional
	public void removeRequestComment(PfCommentsDTO comment, PfRequestsDTO request) {
		// Elimina el comentario
		baseDAO.delete(comment);

		// Actualiza la fecha de modificacion de la peticion
		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);
	}

	/**
	 * Metodo que obtiene una lista de usuarios a partir de sus nombres.
	 * 
	 * @param signers
	 * @param signLinesConfig
	 * @return
	 */
	public List<UserEnvelope> getUserListFromString(String signers, String signLinesConfig, boolean invitedRequest,
			PfInvitedUsersDTO invitedUser, String signlinesAccion) {
		signers = signers.trim().toUpperCase();
		String[] signersArray = signers.split(",");
		String[] signLinesConfigArray = new String[0];		
		String[] signLinesAccionArray = new String[0];
		if (signLinesConfig != null) {
			signLinesConfig = signLinesConfig.trim().toUpperCase();
			signLinesConfigArray = signLinesConfig.split(",");
		}
		if (signlinesAccion != null) {
			signlinesAccion = signlinesAccion.trim().toUpperCase();
			signLinesAccionArray = signlinesAccion.split(",");
		}
		return getUserListFromString(signersArray, signLinesConfigArray, invitedRequest, invitedUser, signLinesAccionArray);
	}

	/**
	 * Metodo que obtiene una lista de usuarios a partir de sus nombres.
	 * 
	 * @param signersArray
	 * @param signLinesConfigArray
	 * @return
	 */
	private List<UserEnvelope> getUserListFromString(String[] signersArray, String[] signLinesConfigArray, 
			boolean invitedRequest, PfInvitedUsersDTO invitedUser, String[] signLinesAccionArray) {
		boolean correcto = true;
		List<UserEnvelope> signerList_correcto = new ArrayList<UserEnvelope>();
		List<UserEnvelope> signerList_incorrecto = new ArrayList<UserEnvelope>();
		int count = 0;
		PfUsersDTO userDTO = null;
		if (signersArray != null && signersArray.length > 0) {
			for (String signer : signersArray) {
				signer = signer.trim();
				if (utilComponent.noEsVacio(signer)) {
					String fullName = null;
					if (!invitedRequest) {
					userDTO = (PfUsersDTO) redactionBO.getUser(signer);
						fullName = userDTO.getFullName();
					UserEnvelope userEnv = new UserEnvelope(userDTO);
					userEnv.setValid(true);
					// Se indica el tipo de línea de firma
					if (signLinesConfigArray.length == 0 || "FIRMA".equals(signLinesConfigArray[count])) {
						userEnv.setPasser(false);	
					} else {
						userEnv.setPasser(true);
					}
					// Se indica la accion del firmante de línea de firma
					if (signLinesAccionArray.length == 0) {
						userEnv.setAccionFirma("1");
					} else {
						userEnv.setAccionFirma(signLinesAccionArray[count]);
					}
					signerList_correcto.add(userEnv);

					} else if(invitedRequest && invitedUser != null){
					UserEnvelope userEnv = new UserEnvelope();
					userEnv.setValid(true);
					userEnv.setEmail(invitedUser.getcMail());
					// Se indica el tipo de línea de firma
					if (signLinesConfigArray.length == 0 || "FIRMA".equals(signLinesConfigArray[count])) {
						userEnv.setPasser(false);
					} else {
						userEnv.setPasser(true);
					}
					// Se indica la accion del firmante de línea de firma
					if (signLinesAccionArray.length == 0) {
						userEnv.setAccionFirma("1");
					} else {
						userEnv.setAccionFirma(signLinesAccionArray[count]);
					}
					signerList_correcto.add(userEnv);
					} else {
					correcto = false;
					UserEnvelope userEnv = new UserEnvelope();
					userEnv.setDname(fullName);
					userEnv.setValid(false);
					signerList_incorrecto.add(userEnv);
				}

				count++;
			}
		}
		}

		if (correcto) {
			return signerList_correcto;
		} else {
			return signerList_incorrecto;
		}
	}

	/**
	 * Metodo que devuelve una cadena de texto con los nombre de los
	 * usuarios contenidos en una lista.
	 * 
	 * @param users
	 *            Lista de usuarios.
	 * @return Cadena de texto con los nombres de los usuarios.
	 */
	public String getUserStringFromList(List<UserEnvelope> users) {
		String signers = "";
		if (users != null && !users.isEmpty()) {
			for (UserEnvelope userEnvelope : users) {
				signers += Util.getInstance().completeUserNameWithProvince(userEnvelope) + ";";
			}
		}
		return signers;
	}

	/**
	 * Metodo que comprueba si existen todos los firmantes.
	 * 
	 * @param signers
	 *            Lista con los nombres y apellidos de los firmantes separados
	 *            por ";"
	 * @return true si existen todos ellos, false en caso contrario.
	 */
	public boolean allSignersExist(String signers) {
		boolean allExist = true;

		signers = signers.trim().toUpperCase();
		String[] signersArray = signers.split(";");

		if (signersArray != null && signersArray.length > 0) {
			int i = 0;

			while (i < signersArray.length && allExist) {
				String signer = signersArray[i];
				signer = signer.trim();
				String fullName = null;
				String province = null;
				PfUsersDTO userDTO = null;

				int index = signer.lastIndexOf(']');
				if (index > 0) {
					fullName = (signer.substring(index + 1)).trim();
					province = signer.substring(1, index);

					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("nameAndSurnames", fullName);
					parameters.put("province", province);
					userDTO = (PfUsersDTO) baseDAO.queryElementMoreParameters("request.userNameSurnamesAndProvince",
							parameters);
				} else {
					userDTO = (PfUsersDTO) baseDAO.queryElementOneParameter("request.userNameAndSurnames",
							"nameAndSurnames", signer);
				}

				if (userDTO == null) {
					allExist = false;
				}
				i++;
			}
		}
		return allExist;
	}

	/**
	 * Método que devuelve un usuario a partir de su nombre completo
	 * 
	 * @param userNameAndSeat
	 *            Nombre completo (incluyendo sede) del usuario
	 * @return Usuario
	 */
	public PfUsersDTO queryUserByNameAndSeat(String userNameAndSeat) {
		String signer = userNameAndSeat.toUpperCase().trim();
		String fullName = null;
		String province = null;
		PfUsersDTO userDTO = null;

		int index = signer.lastIndexOf(']');
		if (index > 0) {
			fullName = (signer.substring(index + 1)).trim();
			province = signer.substring(1, index);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nameAndSurnames", fullName);
			parameters.put("province", province);
			userDTO = (PfUsersDTO) baseDAO.queryElementMoreParameters("request.userNameSurnamesAndProvince",
					parameters);
		} else {
			userDTO = (PfUsersDTO) baseDAO.queryElementOneParameter("request.userNameAndSurnames", "nameAndSurnames",
					signer);
		}

		return userDTO;
	}

	/**
	 * Metodo que devuelve una lista de usuarios filtrada en base a los
	 * parametros de entrada.
	 * 
	 * @param filter
	 *            Texto a buscar como parte del nombre completo de los usuarios
	 *            de la lista.
	 * @param type
	 *            Tipo de filtro a aplicar sobre los usuarios. Puede ser
	 *            "Todos", "Personas", "Cargos" o "Favoritos".
	 * @param filterProvincio
	 *            código de la provincia por la que se desea filtrar. Puede ser
	 *            null.
	 * @param signerList
	 *            Lista de usuarios seleccionados hasta el momento.
	 * @param user
	 *            Objeto que defien al usuario que realiza la seleccion
	 *            de firmantes.
	 * @param provinceUser
	 *            código de provincia del usuario que realiza la selección de
	 *            firmantes.
	 * @return Lista de usuarios filtrada en base a los parametros de
	 *         entrada.
	 */
	public List<UserEnvelope> filterUserList(String filter, String type, String filterProvince, String portafirmasFilter, PfUsersDTO user, String provinceUser) {
		List<UserEnvelope> returnList = new ArrayList<UserEnvelope>();
		String typeUsers = "";
		if (type.equals(Constants.SIGNER_FILTER_JOB)) {
			typeUsers = Constants.C_TYPE_USER_JOB;
		} else if (type.equals(Constants.SIGNER_FILTER_PEOPLE)) {
			typeUsers = Constants.C_TYPE_USER_USER;
			// Si el filtro es "mostUsed" filtraremos después, no en la
			// consulta, por eso se deja en blanco.
		} else if (type.equals(Constants.SIGNER_FILTER_ALL) || type.equals(Constants.SIGNER_FILTER_MOST_USED)) {
			typeUsers = "";
		}

		returnList = queryUsersEnvelopeCompleteNoWebServiceUsers(filter, typeUsers, filterProvince, portafirmasFilter, provinceUser);

		if (type.equals(Constants.SIGNER_FILTER_MOST_USED)) {
			returnList = filterUserListByMostUsed(returnList, user);
		}

		return returnList;
	}

	/**
	 * Metodo que devuelve una lista de usuarios exceptuando al logeado
	 * filtrada en base a los parametros de entrada.
	 * 
	 * @param filter
	 *            Texto a buscar como parte del nombre completo de los usuarios
	 *            de la lista.
	 * @param type
	 *            Tipo de filtro a aplicar sobre los usuarios. Puede ser
	 *            "Todos", "Personas", "Cargos" o "Favoritos".
	 * @param filterProvincio
	 *            código de la provincia por la que se desea filtrar. Puede ser
	 *            null.
	 * @param signerList
	 *            Lista de usuarios seleccionados hasta el momento.
	 * @param user
	 *            Objeto que defien al usuario que realiza la seleccion
	 *            de firmantes.
	 * @param provinceUser
	 *            provincia del usuario que está usando la aplicación
	 * @return Lista de usuarios filtrada en base a los parametros de
	 *         entrada.
	 */
	public List<UserEnvelope> filterSelectableUserList(String filter, String type, String filterProvince,
			List<UserEnvelope> signerList, PfUsersDTO user, String provinceUser) {
		List<UserEnvelope> returnList = new ArrayList<UserEnvelope>();
		String typeUsers = "";
		if (type.equals(Constants.SIGNER_FILTER_JOB)) {
			typeUsers = Constants.C_TYPE_USER_JOB;
		} else if (type.equals(Constants.SIGNER_FILTER_PEOPLE)) {
			typeUsers = Constants.C_TYPE_USER_USER;
			// Si el filtro es mostUsed filtraremos después, no en la consulta,
			// por eso se deja en blanco la cadena.
		} else if (type.equals(Constants.SIGNER_FILTER_ALL) || type.equals(Constants.SIGNER_FILTER_MOST_USED)) {
			typeUsers = "";
		}

		returnList = querySelectableUsersEnvelopeCompleteNoWebServiceUsers(user, filter, typeUsers, filterProvince,
				provinceUser);

		if (type.equals(Constants.SIGNER_FILTER_MOST_USED)) {
			returnList = filterUserListByMostUsed(returnList, user);
		}

		// Remove from list if user was already selected
		if (signerList != null && !signerList.isEmpty()) {
			for (UserEnvelope env : signerList) {
				if (returnList.contains(env)) {
					returnList.remove(env);
				}
			}
		}
		return returnList;
	}

	/**
	 * Método que carga los formatos de firma disponibles en la configuración
	 * 
	 * @param model
	 *            Modelo de datos del formulario
	 */
	public Map<String, Object> loadSignatureFormats() {
		Map<String, Object> signatureConfigurations = new LinkedHashMap<String, Object>();

		List<PfApplicationsDTO> applications = applicationBO.queryApplicationVisibles();

		for (PfApplicationsDTO pad : applications) {
			if ("PFIRMA".equalsIgnoreCase(pad.getCapplication())) {
				signatureConfigurations.put(pad.getCapplication(), pad.getDescripcionWeb());
				break;
			}
		}

		for (PfApplicationsDTO pad : applications) {
			if (!"PFIRMA".equalsIgnoreCase(pad.getCapplication())) {
				signatureConfigurations.put(pad.getCapplication(), pad.getDescripcionWeb());
			}
		}
		return signatureConfigurations;
	}

	/**
	 * Metodo que filtra la lista de usuarios seleccionados filtrada en
	 * base a los firmantes favoritos del usuario.
	 * 
	 * @param returnList
	 *            Lista de usuarios seleccionados que se va a filtrar en base a
	 *            los favoritos.
	 * @param user
	 *            Usuario que selecciona la lista de firmantes. Se filtra en
	 *            base a sus firmantes favoritos.
	 * @return lista de usuarios seleccionados filtrada en base a los firmantes
	 *         favoritos del usuario.
	 */
	private List<UserEnvelope> filterUserListByMostUsed(List<UserEnvelope> returnList, PfUsersDTO user) {
		List<UserEnvelope> mostUsed = queryMostUsedSigners(user);
		List<UserEnvelope> aux = new ArrayList<UserEnvelope>();
		for (UserEnvelope userEnv : returnList) {
			if (mostUsed.contains(userEnv)) {
				aux.add(userEnv);
			}
		}
		return aux;
	}

	/**
	 * Metodo que devuelve la informacion de las peticiones
	 * dirigidas a usuarios seleccionadas actualizada con sus l&iacute;neas de
	 * firma.
	 * 
	 * @param selectedRequestList
	 *            Lista de peticiones seleccionadas.
	 * @return Lista de peticiones dirigidas a usuarios seleccionadas con la
	 *         informacion de sus l&iacute;neas de firma cargada.
	 */
	public List<AbstractBaseDTO> getSelectedRequestUserList(List<AbstractBaseDTO> selectedRequestList) {
		List<AbstractBaseDTO> result = new ArrayList<AbstractBaseDTO>();
		for (Iterator<AbstractBaseDTO> iterator = selectedRequestList.iterator(); iterator.hasNext();) {
			PfRequestTagsDTO requestTagsDTO = (PfRequestTagsDTO) iterator.next();
			if (requestTagsDTO.getStateUser() != null) {
				PfRequestsDTO req = this.queryRequestSignLines(requestTagsDTO.getPfRequest());
				requestTagsDTO.setPfRequest(req);
				result.add(requestTagsDTO);
			}
		}
		return result;
	}

	/**
	 * Metodo que obtiene las l&iacute;neas de firma de una
	 * peticion.
	 * 
	 * @param requestsDTO
	 *            Peticion de la que se quieren obtener las l&iacute;neas
	 *            de firma.
	 * @return Devuelve el objeto peticion con la informacion de
	 *         sus l&iacute;neas de firma cargada.
	 */
	private PfRequestsDTO queryRequestSignLines(PfRequestsDTO requestsDTO) {
		return (PfRequestsDTO) baseDAO.queryElementOneParameter("request.queryRequestSignLines", "pk",
				requestsDTO.getPrimaryKey());
	}

	/**
	 * Metodo que devuelve la informacion de las peticiones
	 * dirigidas a cargos seleccionadas actualizada con sus l&iacute;neas de
	 * firma.
	 * 
	 * @param selectedRequestList
	 *            Lista de peticiones seleccionadas.
	 * @return Lista de peticiones dirigidas a cargos seleccionadas con la
	 *         informacion de sus l&iacute;neas de firma cargada.
	 */
	public List<AbstractBaseDTO> getSelectedRequestJobList(List<AbstractBaseDTO> selectedRequestList) {
		List<AbstractBaseDTO> result = new ArrayList<AbstractBaseDTO>();
		for (Iterator<AbstractBaseDTO> iterator = selectedRequestList.iterator(); iterator.hasNext();) {
			PfRequestTagsDTO requestTagsDTO = (PfRequestTagsDTO) iterator.next();
			if (requestTagsDTO.getStateJob() != null) {
				PfRequestsDTO req = this.queryRequestSignLines(requestTagsDTO.getPfRequest());
				requestTagsDTO.setPfRequest(req);
				result.add(requestTagsDTO);
			}
		}
		return result;
	}

	/**
	 * Metodo que de vuelve una lista de usuarios sobre los que puede
	 * delegar una peticion el usuario que esta logeado en el
	 * sistema.
	 * 
	 * @param suggest
	 *            Cadena de texto que filtra los resultados de usuarios sobre
	 *            los que delegar.
	 * @param primaryKey
	 *            Clave primario del usuario logeado.
	 * @return Lista de usuarios sobre los que puede delegar una peticion
	 *         el usuario que esta logeado en el sistema.
	 */
	public List<AbstractBaseDTO> queryDelegatedUserComplete(String suggest, Long primaryKey) {
		String findAux = suggest.toUpperCase();
		findAux = "%" + findAux + "%";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("find", findAux);
		parameters.put("usrPk", primaryKey);
		return baseDAO.queryListMoreParameters("request.delegatedUsersComplete", parameters);
	}

	/**
	 * Metodo que busca un usuario por su nombre completo.
	 * 
	 * @param fullName
	 *            Nombre completo del usuario.
	 * @return Usuario cuyo nombre completo coincide con la busqueda.
	 */
	public PfUsersDTO queryUserByFullName(String fullName) {
		return (PfUsersDTO) baseDAO.queryElementOneParameter("request.userByFullName", "fullName", fullName);
	}

	/**
	 * Metodo que devuelve un objeto Map con la relacion de cada
	 * peticion con su modo de firma. Se usa en configuracion
	 * Multifirma.
	 * 
	 * @param requests
	 *            Lista de peticiones.
	 * @param signMode
	 *            Modo de firma establecido en la configuracion del
	 *            servidor seleccionado.
	 * @return Objeto Map con la relacion de cada peticion con su
	 *         modo de firma.
	 */
	public Map<String, String> queryModeMultiSignRequests(List<AbstractBaseDTO> requests, String signMode) {
		Map<String, String> map = new HashMap<String, String>();

		List<AbstractBaseDTO> requestList = baseDAO.queryListOneParameterList("request.requestSignMode", "reqs",
				requests.toArray());
		PfRequestsDTO req = null;
		String signModeReq = null;
		for (AbstractBaseDTO abstractBaseDTO : requestList) {
			req = (PfRequestsDTO) abstractBaseDTO;
			// Obtiene el modo de firma usado para firmar los documentos de la
			// peticion
			signModeReq = signModeRequest(req, signMode);
			map.put(req.getChash(), signModeReq);
		}

		return map;
	}

	public PfRequestTagsDTO queryRequestTagByHash(String hash) {
		PfRequestTagsDTO requestTag = (PfRequestTagsDTO) baseDAO.queryElementOneParameter("request.requestTagByHash",
				"hash", hash);
		return requestTag;
	}
	
	public PfRequestTagsDTO queryRequestTagByHashInvited(String hash) {
		PfRequestTagsDTO requestTag = (PfRequestTagsDTO) baseDAO.queryElementOneParameter("request.requestTagByHashInvited",
				"hash", hash);
		return requestTag;
	}

	/**
	 * Metodo que devuelve el modo de firma asociado a una
	 * peticion.
	 * 
	 * @param req
	 *            Peticion de la que se quiere saber el modo de firma.
	 * @param signMode
	 *            Modo de firma de la configuracion del servidor actual
	 * @return
	 */
	private String signModeRequest(PfRequestsDTO req, String signMode) {
		// Si la peticion no contiene documentos firmados el modo de
		// firma toma el valor de la configuracion establecida.
		String signModeReq = signMode;

		for (PfDocumentsDTO doc : req.getPfDocuments()) {
			if (doc.getPfSigns() != null && !doc.getPfSigns().isEmpty()) {
				for (PfSignsDTO sign : doc.getPfSigns()) {
					if (!Constants.C_TYPE_SIGNLINE_PASS.equals(sign.getCtype())) {
						// Devuelve que el modo de firma es MASIVO
						signModeReq = Constants.SIGN_MODE_MASSIVE;
						if (sign.getPfBlockSigns() != null && !sign.getPfBlockSigns().isEmpty()) {
							// Devuelde que el modo de firma es EN BLOQUE
							signModeReq = Constants.SIGN_MODE_BLOCK;
						}
					}
				}
			}
		}

		return signModeReq;
	}

	/**
	 * Metodo que añade las peticiones para ser firmadas a los
	 * usuarios en los que se delega la firma de las mismas.
	 * 
	 * @param request
	 *            Lista de peticiones a delegar.
	 * @param userList
	 *            Lista de usuarios receptores de las peticiones que se van a
	 *            delegar.
	 * @param currentUser
	 *            usuario que delega las peticiones.
	 */
	private void saveDelegation(List<AbstractBaseDTO> requestTagList, List<AbstractBaseDTO> userList,
			PfUsersDTO currentUser) {

		// add signer and tag
		Set<PfSignLinesDTO> sliSet = null;
		PfSignersDTO signerDTO = null;
		PfRequestTagsDTO reqTag = null;
		PfTagsDTO tagNextUser = null;

		// user request list
		for (AbstractBaseDTO rt : requestTagList) {
			PfRequestTagsDTO requestTag = (PfRequestTagsDTO) rt;
			PfRequestsDTO request = requestTag.getPfRequest();
			sliSet = ((PfRequestsDTO) request).getPfSignsLines();
			for (PfSignLinesDTO sli : sliSet) {
				for (PfSignersDTO signer : sli.getPfSigners()) {
					if (currentUser != null
							&& signer.getPfUser().getPrimaryKey().compareTo(currentUser.getPrimaryKey()) == 0) {
						for (AbstractBaseDTO receiver : userList) {
							// Si el usuario en el que se delega no pertenece a
							// la l&iacute;nea de firma se le añade a la
							// misma.
							if (!filterBO.existSignerSignLine(sli.getPfSigners(), (PfUsersDTO) receiver)) {
								signerDTO = new PfSignersDTO();
								signerDTO.setPfUser((PfUsersDTO) receiver);
								signerDTO.setPfSigner(signer);
								signerDTO.setPfSignLine(sli);
								baseDAO.insertOrUpdate(signerDTO);

								// Se obtiene la etiqueta a asignar al siguiente
								// usuario
								reqTag = tagBO.queryStateUserSignLine((PfRequestsDTO) request, currentUser,
										(PfSignLinesDTO) sli);
								if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_NEW)
										|| reqTag.getPfTag().getCtag().equals(Constants.C_TAG_READ)) {
									tagNextUser = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
								} else {
									tagNextUser = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING);
								}

								// Se actualiza la etiqueta de la
								// peticion para el nuevo usuario
								// destinatario
								tagBO.changeRequestTagUser((PfRequestsDTO) request, requestTag, (PfSignLinesDTO) sli,
										tagNextUser, (PfUsersDTO) receiver);

								// Se actualiza la informacion.
								request.setFmodified(Calendar.getInstance().getTime());
								baseDAO.update(request);

							}
						}
					}
				}
			}
		}
	}

	/**
	 * Metodo que guarda un nuevo fichero en la base de datos.
	 * 
	 * @param documentDTO
	 *            Documento asociado al fichero a guardar.
	 * @param inputStore
	 *            Flujo de entrada para el almacenamiento del fichero.
	 * @param inputCheck
	 *            Flujo de entrada para almacenar la clave hash del fichero y
	 *            comprobar que no existe en la base de datos.
	 * @param size
	 *            Tamaño del fichero.
	 * @return El objeto que define el nuevo fichero dado de alta en la base de
	 *         datos.
	 * @throws CustodyServiceException
	 * @throws IOException
	 */
	public PfFilesDTO insertFile(PfDocumentsDTO documentDTO, InputStream inputStore, InputStream inputCheck, long size)
			throws CustodyServiceException, IOException {
		String checkHash = Util.getInstance().createHash(inputCheck);
		PfFilesDTO fileDTO = (PfFilesDTO) baseDAO.queryElementOneParameter("file.parameterFileHash", "hash", checkHash);
		if (fileDTO == null) {
			fileDTO = new PfFilesDTO();
			fileDTO.setChash(checkHash);
			fileDTO.setChashAlg(Constants.C_HASH_ALG_SHA1);
			String storageType = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.DOCUMENTOS.name());
			CustodyServiceInput custodyService = custodyServiceFactory.createCustodyServiceInput(storageType);
			fileDTO.setCtype(storageType);
			
			String dir3 = "";
			try {
				//Agustin--> saco el dir3 de la entidad de sigem que viene en el documento
//				UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
//				PfUsersDTO user = authorization.getUserDTO();
//				dir3 = user.getPfProvince().getCcodigoprovincia();
				dir3 = documentDTO.getPfRequest().getDreference().split("_")[2];
				
			} catch (Exception e) {
				//En caso de que sea un WS quien crea la petición no hay usuario logado
				// TODO: Hasta que las aplicaciones no estén asociadas a un dir3 ponemos el genérico de Administración del Estado EA9999999 es configurable en administracion custodia
				dir3 = custodyServiceFactory.custodiaDir3PorDefecto();
			}
			 
			fileDTO.setRefNasDir3(dir3);
			fileDTO.setIdEni(Util.getInstance().generarIDEni(checkHash, dir3));
			baseDAO.insertOrUpdate(fileDTO);
			CustodyServiceInputDocument document = Util.getInstance()
					.documentDTOToCustodyServiceInputDocument(documentDTO, fileDTO, size);
			String uri = custodyService.uploadFile(document, inputStore);
			inputStore.close();
			if (null != uri && !"".equals(uri)) {
				fileDTO.setCuri(uri);
				baseDAO.insertOrUpdate(fileDTO);
			}
		}
		return fileDTO;
	}

	/**
	 * Metodo que permite a un usuario delegar una lista de peticiones a
	 * unos usuarios y/o cargos determinados.
	 * 
	 * @param selectedUserRequestList
	 *            Lista de peticiones de usuario a ser delegadas.
	 * @param delegatedUserList
	 *            Lista de usuarios a los que se les van a delegar las
	 *            peticiones.
	 * @param selectedJobRequestList
	 *            Lista de peticiones de cargo a ser delegadas.
	 * @param delegatedJobList
	 *            Lista de cargos a los que se les van a delegar las peticiones.
	 * @param currentUser
	 *            Usuario que delega las peticiones.
	 */
	@Transactional
	public void saveRequestDelegation(List<AbstractBaseDTO> selectedUserRequestList,
			List<AbstractBaseDTO> delegatedUserList, List<AbstractBaseDTO> selectedJobRequestList,
			List<AbstractBaseDTO> delegatedJobList, PfUsersDTO currentUser) {
		saveDelegation(selectedUserRequestList, delegatedUserList, currentUser);
		saveDelegation(selectedJobRequestList, delegatedJobList, currentUser.getValidJob());
	}

	/**
	 * Metodo que define si se debe mostrar la opcion para
	 * descargar el documento de una peticion o no.
	 * 
	 * @param request
	 *            Peticion de la que se quiere mostrar (o no) la
	 *            opcion de descarga de documentos.
	 * @return True si se debe mostrar.
	 */
	public boolean isShowDownloadSign(PfRequestsDTO request) {
		List<AbstractBaseDTO> requestTagList = baseDAO.queryListOneParameter("request.stateTagRequest", "request",
				request);
		boolean anySigned = false;
		boolean anyRejected = false;

		// Download sign is shown when request is signed by anyone and not
		// rejected by nobody
		for (Iterator<AbstractBaseDTO> iterator = requestTagList.iterator(); iterator.hasNext();) {
			PfRequestTagsDTO stateTag = (PfRequestTagsDTO) iterator.next();
			if (stateTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED)) {
				anySigned = true;
			} else if (stateTag.getPfTag().getCtag().equals(Constants.C_TAG_REJECTED)) {
				anyRejected = true;
			}
		}
		return (!anyRejected && anySigned);
	}

	/**
	 * Metodo que devuelve el listado de firmantes favoritos de un
	 * usuario.
	 * 
	 * @param userDTO
	 *            Usuario del que se buscan sus firmantes favoritos
	 * @return Listado de firmantes favoritos de un usuario.
	 */
	public List<UserEnvelope> queryMostUsedSigners(PfUsersDTO userDTO) {
		List<UserEnvelope> mostUsedSigners = new ArrayList<UserEnvelope>();
		List<AbstractBaseDTO> abstractList = baseDAO.queryListOneParameterWithTransformer("request.mostUsedSigners",
				"remitter", userDTO, PfUsersDTO.class);
		if (abstractList != null && !abstractList.isEmpty()) {
			for (AbstractBaseDTO abstractBaseDTO : abstractList) {
				PfUsersDTO user = (PfUsersDTO) abstractBaseDTO;
				UserEnvelope userEnvelope = new UserEnvelope(user);
				mostUsedSigners.add(userEnvelope);
			}
		}

		return mostUsedSigners;
	}

	/**
	 * Método que obtiene el nivel de importancia a partir de su PK
	 * 
	 * @param idLevel
	 *            Pk del nivel de importancia
	 */
	public PfImportanceLevelsDTO obtainImportanceLevel(Long idLevel) {
		PfImportanceLevelsDTO level = null;

		if (idLevel == null) {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter("request.queryNormalLevel", null, null);
		} else {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter("request.queryImportanceLevelById",
					"idLevel", idLevel);
		}

		return level;
	}

	/**
	 * Método que obtiene el nivel de importancia a partir de su código
	 * 
	 * @param levelCode
	 *            código del nivel de importancia
	 */
	public PfImportanceLevelsDTO obtainImportanceLevelByCode(String levelCode) {
		PfImportanceLevelsDTO level = null;

		if (levelCode == null || levelCode.equals("")) {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter("request.queryNormalLevel", null, null);
		} else {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter("request.queryImportanceLevelByCode",
					"codLevel", levelCode);
		}

		return level;
	}
	
	/**
	 * Método que obtiene la acción del firmante a partir de su código
	 * 
	 * @param code
	 *            código del nivel de importancia
	 */
	public PfAccionFirmanteDTO obtainAccionFirmanteByCode(String code) {
		PfAccionFirmanteDTO accion = null;

		if (code == null || code.equals("")) {
			accion = (PfAccionFirmanteDTO) baseDAO.queryElementOneParameter("request.queryAccionFirmanteSinEspecificar", null, null);
		} else {
			accion = (PfAccionFirmanteDTO) baseDAO.queryElementOneParameter("request.queryAccionFirmanteByCode","code", code);
		}

		return accion;
	}

	/**
	 * Método que permite elimina el objeto request de la sesión actual de
	 * Hibernate.
	 * 
	 * @param request
	 *            Objeto persistente a eliminar de la sesión de Hibernate.
	 */
	public void detach(PfRequestsDTO request) {
		baseDAO.detach(request);
	}

	/**
	 * Devuelve el listado de ámbitos de documentos disponibles
	 * 
	 * @return Listado de ámbitos
	 */
	public List<PfDocumentScopesDTO> queryDocumentScopes() {
		List<PfDocumentScopesDTO> scopeTypes = baseDAO.queryListOneParameter("request.scopeTypes", null, null);
		return scopeTypes;
	}


	/**
	 * Devuelve el listado de niveles de importancia disponibles
	 * 
	 * @return Listado de niveles de importancia
	 */
	public List<PfImportanceLevelsDTO> queryImportanceLevels() {
		List<PfImportanceLevelsDTO> importanceLevels = baseDAO.queryListOneParameter("request.importanceLevels", null,
				null);
		return importanceLevels;
	}

	/**
	 * Devuelve el nivel de importancia "Normal"
	 * 
	 * @return Nivel de importancia "Normal"
	 */
	public PfImportanceLevelsDTO queryNormalLevel() {
		PfImportanceLevelsDTO importanceLevel = (PfImportanceLevelsDTO) baseDAO
				.queryElementOneParameter("request.queryNormalLevel", null, null);
		return importanceLevel;
	}

	/**
	 * Método que reenvía una petición a una serie de usuarios
	 * 
	 * @param user
	 *            Usuario que reenvía
	 * @param request
	 *            Petición
	 * @param forwardedUsers
	 *            Usuarios reenviados
	 */
	@Transactional(readOnly = false)
	public boolean doForward(PfUsersDTO user, PfRequestsDTO request, PfUsersDTO forwardedUser) {
		// Obtengo las líneas de firma en las que aparece el usuario que reenvía
		List<AbstractBaseDTO> signLines = tagBO.queryFetchSignLineListByUser(request, user.getCidentifier());
		List<AbstractBaseDTO> signLinesFU = tagBO.queryFetchSignLineListByUser(request, forwardedUser.getCidentifier());

		boolean aviso = false;

		// Busco las apariciones como firmante del usuario que reenvía
		for (AbstractBaseDTO sl : signLines) {
			PfSignLinesDTO signLine = (PfSignLinesDTO) sl;
			// Sólo se hace el reenvío si el usuario destinatario no se encuentra 
			// ya en la línea de firma y si la línea de firma no está firmada, 
			// rechazada, caducada, visto bueno o retirada
			PfRequestTagsDTO reqTag = tagBO.queryStateUserSignLine(request, user, signLine);
			if (!signLinesFU.contains(signLine) && !reqTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED)
					&& !reqTag.getPfTag().getCtag().equals(Constants.C_TAG_REJECTED)
					&& !reqTag.getPfTag().getCtag().equals(Constants.C_TAG_EXPIRED)
					&& !reqTag.getPfTag().getCtag().equals(Constants.C_TAG_PASSED)
					&& !reqTag.getPfTag().getCtag().equals(Constants.C_TAG_REMOVED)) {
				aviso = true;
				Set<PfSignersDTO> signers = signLine.getPfSigners();
				for (PfSignersDTO signer : signers) {
					// Se debe sustituir cada firmante que sea el usuario que
					// reenvía por el nuevo destinatario
					if (signer.getPfUser().getCidentifier().equals(user.getCidentifier())) {
						signer.setPfUser(forwardedUser);

						// Crea la etiqueta REENVIADA para ese usuario en la petición
						PfRequestTagsDTO etiquetaPeticion = new PfRequestTagsDTO();
						etiquetaPeticion.setPfRequest(request);
						etiquetaPeticion.setPfUser(forwardedUser);
						etiquetaPeticion.setPfTag(applicationVO.getSystemTags().get(Constants.C_TAG_SYSTEM_FORWARED));
						etiquetaPeticion.setPfSignLine(signLine);
						baseDAO.insertOrUpdate(etiquetaPeticion);
					}
				}

				// Se actualiza la línea de firma
				baseDAO.insertOrUpdate(signLine);

				reqTag.setPfUser(forwardedUser);
				if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING)) {
					reqTag.setPfTag(applicationVO.getStateTags().get(Constants.C_TAG_AWAITING));
				} else {
					reqTag.setPfTag(applicationVO.getStateTags().get(Constants.C_TAG_NEW));
				}

				// Se actualiza la etiqueta petición
				baseDAO.insertOrUpdate((AbstractBaseDTO) reqTag);
			}
		}

		if (aviso) {
			// Si hay reenvio, se elimina las etiquetas de usuario
			Set<PfRequestTagsDTO> setRequestTagsDTO = request.getPfRequestsTags();
			for (Iterator<PfRequestTagsDTO> iterator = setRequestTagsDTO.iterator(); iterator.hasNext();) {
				PfRequestTagsDTO pfRequestTagsDTO = iterator.next();
				PfTagsDTO pfTagsDTO = pfRequestTagsDTO.getPfTag();
				if (pfTagsDTO.getCtype().equals(Constants.C_TYPE_TAG_USER)) {
					baseDAO.delete(pfRequestTagsDTO);
				}
			}
			// Se añade el comentario que avisa sobre el reenvío
			PfCommentsDTO comment = new PfCommentsDTO();
			comment.setPfRequest(request);
			comment.setPfUser(user);
			comment.setDsubject(Constants.C_TAG_FORWARDED);
			comment.setTcomment(Constants.FORWARED_COMMENT + forwardedUser.getFullName());
			baseDAO.insertOrUpdate(comment);
		}
		
		noticeBO.noticeForwardRequest(request, forwardedUser);

		return aviso;
	}

	public List<PfUsersDTO> getForwarderUsers(List<UserEnvelope> destinatarios) {
		
		List<PfUsersDTO> forwardedUsers = new ArrayList<PfUsersDTO>();
				
		for(UserEnvelope user: destinatarios){
			PfUsersDTO forwardedUser = (PfUsersDTO) baseDAO.queryElementOneParameter("request.usersDni", "dni", user.getCidentifier().toUpperCase());
			forwardedUsers.add(forwardedUser);
		}
		return forwardedUsers;
	}


	public PfUsersDTO getUserByPk(Long pk) {
		return (PfUsersDTO) baseDAO.queryElementOneParameter("request.usersAndJobWithProvincePk", "pk", pk);
	}

	public PfUsersDTO userEnvelopeToUserDTO(UserEnvelope userEnvelope) {
		PfUsersDTO userDTO = getUserByPk(userEnvelope.getPk());
		userDTO.setPasser(userEnvelope.isPasser());
		return userDTO;
	}

	public List<PfUsersDTO> userEnvelopeListToUserDTOList(List<UserEnvelope> userEnvelopeList) {
		List<PfUsersDTO> userDTOList = new ArrayList<PfUsersDTO>();
		for (UserEnvelope userEnv : userEnvelopeList) {
			userDTOList.add(userEnvelopeToUserDTO(userEnv));
		}
		return userDTOList;
	}

	public List<AbstractBaseDTO> userEnvelopeListToAbstractDTOList(List<UserEnvelope> userEnvelopeList) {
		List<AbstractBaseDTO> absDTOList = new ArrayList<AbstractBaseDTO>();
		for (UserEnvelope userEnv : userEnvelopeList) {
			absDTOList.add((AbstractBaseDTO) userEnvelopeToUserDTO(userEnv));
		}
		return absDTOList;
	}

	/**
	 * Metodo que comprueba si un documento existe en BD.
	 * 
	 * @param chash
	 *            Hash del documento.
	 * @return Documento.
	 */
	public PfDocumentsDTO checkDocument(String chash) throws PfirmaException {
		PfDocumentsDTO doc = null;
		if (chash != null && !"".equals(chash)) {
			doc = (PfDocumentsDTO) baseDAO.queryElementOneParameter("request.documentHash", "hash", chash);
			if (doc == null) {
				throw new PfirmaException("Document not found");
			}
		} else {
			throw new PfirmaException("Document hash not valid");
		}
		return doc;
	}

	/**
	 * Metodo que comprueba si un documento existe en BD.
	 * 
	 * @param chash
	 *            Hash del documento.
	 * @return Documento.
	 */
	public PfDocumentsDTO checkDocumentFetch(String chash) throws PfirmaException {
		PfDocumentsDTO doc = null;
		log.debug("Checking document");
		if (chash != null && !"".equals(chash)) {
			doc = (PfDocumentsDTO) baseDAO.queryElementOneParameter("request.documentHashFetch", "hash", chash);
			if (doc != null) {
				log.debug("Document found");
			} else {
				log.error("Document not found");
				throw new PfirmaException("Document not found");
			}
		} else {
			log.error("Document hash not valid");
			throw new PfirmaException("Document hash not valid");
		}
		return doc;
	}

	/**
	 * Método que devuelve la lista de etiquetas que tiene un usuario para una
	 * petición
	 * 
	 * @param request
	 *            Petición
	 * @param user
	 *            Usuario
	 * @return Listado de etiquetas
	 */
	public List<AbstractBaseDTO> queryUserTagsForRequest(PfRequestsDTO request, PfUsersDTO user, PfGroupsDTO grupo) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		String nameQuery = "request.requestUserTags";

		parameters.put("request", request);
		if (grupo == null) {
			parameters.put("user", user);
		} else {
			parameters.put("grupo", grupo);
			nameQuery = "request.requestUserTagsByGroup";
		}
		return baseDAO.queryListMoreParameters(nameQuery, parameters);
	}

	/**
	 * Concatena los asuntos de las peticiones asociadas a etiquetas-peticion
	 */
	public String concatenarAsuntos(List<AbstractBaseDTO> reqTagList) {
		String conc = "";
		for (AbstractBaseDTO abs : reqTagList) {
			PfRequestTagsDTO reqTag = (PfRequestTagsDTO) abs;
			conc += "\"" + reqTag.getPfRequest().getDsubject() + "\", ";
		}

		conc = conc.substring(0, conc.length() - 2);
		return conc;
	}

	// MÉTODOS DOCEL

	/**
	 * Metodo que almacena en base de datos toda la informacion
	 * referente a una nueva peticion.
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que se va a guardar.
	 * @param requestTextDTO
	 *            Texto de la peticion que se va a guardar.
	 * @param destinatarios
	 *            Listado de firmantes de la peticion.
	 * @param signType
	 *            Valor que define el tipo de firma.
	 * @param notices
	 *            Notificaciones que se asocian a la peticion.
	 * @param applicationDTO
	 *            Objeto que define la informacion de la
	 *            aplicacion a la que se asocia la peticion.
	 * @param stateTags
	 *            Etiquetas que se pueden asociar a la peticion.
	 * @param remitter
	 *            The remitter user DTO
	 * @param requestDocumentList
	 *            The DOCEL documents to include in the portafirmas request.
	 * @throws CustodyServiceException
	 * @throws IOException
	 */
	@Transactional
	public void saveDocelwebRequest(PfRequestsDTO requestDTO, PfRequestsTextDTO requestTextDTO,
			List<UserEnvelope> destinatarios, String signType, String[] notices, PfApplicationsDTO applicationDTO,
			Map<String, PfTagsDTO> stateTags, PfUsersDTO remitter, List<PfDocelwebDocumentDTO> requestDocumentList)
					throws CustodyServiceException, IOException {

		// Request
		insertRequest(requestDTO, requestTextDTO, signType, applicationDTO);

		// Check Signers and signLines
		PfSignLinesDTO signLineDTO = insertSignersAndNewTag(requestDTO, destinatarios, stateTags);

		// Remitter
		insertRemitter(requestDTO, remitter, null, notices);

		// Notices
		if (notices != null && notices.length > 0) {
			insertNotices(requestDTO, notices);
		}

		insertDocelwebDocuments(requestDTO, requestDocumentList);

		// Actualizamos la información de linea de firmantes de la petición, para que 
		// estén disponibles durante la transacción actual y para permitir
		// enviar la notificación de nueva petición.
		Set<PfSignLinesDTO> signLineList = new LinkedHashSet<PfSignLinesDTO>();
		signLineList.add(signLineDTO);
		requestDTO.setPfSignsLines(signLineList);
	}

	/**
	 * Metodo que guarda en la base de datos los documentos de las
	 * peticion DOCEL.
	 * 
	 * @param requestDTO
	 *            Objeto que define la peticion que contiene los
	 *            documentos.
	 * @param requestDocumentList
	 *            Lista de documentos de la peticion.
	 * @throws CustodyServiceException
	 * @throws IOException
	 */
	private void insertDocelwebDocuments(PfRequestsDTO requestDTO, List<PfDocelwebDocumentDTO> requestDocumentList)
			throws CustodyServiceException, IOException {
		// Obtiene el ámbito del documento por defecto
		PfDocumentScopesDTO documentScopeDTO = (PfDocumentScopesDTO) baseDAO
				.queryElementOneParameter("request.queryInternalScope", null, null);
		for (Iterator<PfDocelwebDocumentDTO> iterator = requestDocumentList.iterator(); iterator.hasNext();) {
			PfDocelwebDocumentDTO docelDocumentDTO = iterator.next();
			if (docelDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.ELECTRONIC_DOC)) {
				PfDocumentsDTO pfirmaDocumentDTO = new PfDocumentsDTO();
				pfirmaDocumentDTO.setChash(Util.getInstance().createHash());
				setGenericDocumentType(pfirmaDocumentDTO);
				pfirmaDocumentDTO.setDname(docelDocumentDTO.getdDocumentPath());
				setDocelMimeType(pfirmaDocumentDTO, docelDocumentDTO);
				pfirmaDocumentDTO.setLsign(docelDocumentDTO.getdReqSign());
				pfirmaDocumentDTO.setPfRequest(requestDTO);
				pfirmaDocumentDTO.setPfDocumentScope(documentScopeDTO);
				pfirmaDocumentDTO.setPfFile(docelDocumentDTO.getPfFile());
				pfirmaDocumentDTO.setLissign(esDocumentoFirmado(docelDocumentDTO));
				baseDAO.insertOrUpdate(pfirmaDocumentDTO);
				docelDocumentDTO.setPfDoc(pfirmaDocumentDTO);
				baseDAO.insertOrUpdate(docelDocumentDTO);
			} else if (docelDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.DISPOSITION_DOC)
					|| docelDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.PAPER_DOC)) {
				PfDocumentsDTO pfirmaDocumentDTO = new PfDocumentsDTO();
				pfirmaDocumentDTO.setChash(Util.getInstance().createHash());
				setGenericDocumentType(pfirmaDocumentDTO);
				pfirmaDocumentDTO.setDname(docelDocumentDTO.getdDocumentPath());
				pfirmaDocumentDTO.setLsign(docelDocumentDTO.getdReqSign());
				pfirmaDocumentDTO.setPfRequest(requestDTO);
				pfirmaDocumentDTO.setPfDocumentScope(documentScopeDTO);
				PfFilesDTO fileDTO = getDocelTemporalFile(pfirmaDocumentDTO, docelDocumentDTO);
				pfirmaDocumentDTO.setPfFile(fileDTO);
				pfirmaDocumentDTO.setLissign(esDocumentoFirmado(docelDocumentDTO));
				baseDAO.insertOrUpdate(pfirmaDocumentDTO);
				docelDocumentDTO.setPfFile(fileDTO);
				docelDocumentDTO.setPfDoc(pfirmaDocumentDTO);
				baseDAO.insertOrUpdate(docelDocumentDTO);
			}
		}
	}

	private Boolean esDocumentoFirmado(PfDocelwebDocumentDTO docelDocumentDTO) throws CustodyServiceException {
		CustodyServiceOutput custodyOutputService = custodyServiceFactory
				.createCustodyServiceOutput(docelDocumentDTO.getPfFile().getCtype());
		CustodyServiceOutputDocument outputDocument = Util.getInstance()
				.docelDocumentToCustodyServiceOutputDocument(docelDocumentDTO.getPfFile());
		 
		byte[] bytes = custodyOutputService.downloadDocelFile(outputDocument);
		String isSign = signDataUtil.checkIsSign(bytes);
		return isSign != null;
	}

	private PfFilesDTO getDocelTemporalFile(PfDocumentsDTO pfirmaDocumentDTO, PfDocelwebDocumentDTO docelDocumentDTO)
			throws CustodyServiceException, IOException {
		File file = null;
		InputStream inputStore = null;
		InputStream inputCheck = null;
		OutputStream osPaperDoc = null;
		String fileText = "";
		// TODO: Ver que hacer con los documentos en papel y disposición.
		// (JJLP)
		try {
			if (docelDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.PAPER_DOC)) {
				pfirmaDocumentDTO.setDmime("text/plain");
				// TODO: Se cambia la creación del File pero no lo probamos
				// file = File.createTempFile("DOCEL", "PAPER_DOC_INFO");

				file = new File(Constants.PATH_TEMP + "PAPER_DOC_INFO.tmp");
				osPaperDoc = new FileOutputStream(file);
				fileText += "Referencia de documento en papel (DOCEL)\n";
				fileText += "\tNOMBRE: " + docelDocumentDTO.getdDocumentPath() + "\n";
				fileText += "\tDESCRIPCIÓN: " + docelDocumentDTO.getdDescription() + "\n";
				fileText += "\tREQUIERE FIRMA: " + docelDocumentDTO.getdReqSign() + "\n";
				osPaperDoc.write(fileText.getBytes());
			} else if (docelDocumentDTO.getdDocelType().equalsIgnoreCase(DocelwebConstants.DISPOSITION_DOC)) {
				// file =
				// getUrlContentFile(docelDocumentDTO.getdDispositionUrl(),
				// pfirmaDocumentDTO);
				pfirmaDocumentDTO.setDmime("text/plain");
				// TODO: Se cambia la creación del File pero no lo probamos
				// file = File.createTempFile("DOCEL", "DISPOSITION_URL_INFO");

				file = new File(Constants.PATH_TEMP + "DISPOSITION_URL_INFO.tmp");
				osPaperDoc = new FileOutputStream(file);
				fileText += "Referencia de puesta en disposición / URL (DOCEL)\n";
				fileText += "\tNOMBRE: " + docelDocumentDTO.getdDocumentPath() + "\n";
				fileText += "\tDESCRIPCIÓN: " + docelDocumentDTO.getdDescription() + "\n";
				fileText += "\tURL: " + docelDocumentDTO.getdDispositionUrl() + "\n";
				fileText += "\tREQUIERE FIRMA: " + docelDocumentDTO.getdReqSign() + "\n";
				osPaperDoc.write(fileText.getBytes());

			}
			inputCheck = new FileInputStream(file);
			inputStore = new FileInputStream(file);
			
		} catch (NullPointerException | IOException | SecurityException e) {
			log.error("ERROR: RequestBO.getDocelTemporalFile, ", e);
		} finally {
			if (osPaperDoc != null) {
				try {
					osPaperDoc.close();
				} catch (IOException e) {
					log.error("ERROR: RequestBO.getDocelTemporalFile, ", e);
				}
			}
		}

		if(file != null){
			return insertFile(pfirmaDocumentDTO, inputStore, inputCheck, file.length());
		}else{
			return null;
		}
	}

	private void setDocelMimeType(PfDocumentsDTO pfirmaDocumentDTO, PfDocelwebDocumentDTO docelDocumentDTO) {
		String fileName = docelDocumentDTO.getdDocumentPath();
		if (Util.getInstance().hasExtension(fileName)) {
			pfirmaDocumentDTO.setDmime(Util.getInstance().getMimeTypeOf(fileName));
		} else {
			pfirmaDocumentDTO.setDmime("application/octet-stream");
		}
	}

	private void setGenericDocumentType(PfDocumentsDTO pfirmaDocumentDTO) {
		pfirmaDocumentDTO.setPfDocumentType(
				(PfDocumentTypesDTO) baseDAO.queryElementOneParameter("request.documentTypeId", "type", "GENERICO"));
	}

	/**
	 * Metodo que añade un comentario a una peticion.
	 * 
	 * @param request
	 *            Peticion a la que se añade el comentario.
	 * @param comment
	 *            Comentario que se desea añadir a la peticion.
	 * @param userDTO
	 *            Usuario que añade el comentario a la peticion.
	 */
	@Transactional
	public void insertUserComment(PfUsersCommentDTO pfUsersCommentDTO) {

		// añade el comentario a la peticion
		baseDAO.insertOrUpdate(pfUsersCommentDTO);
	}

	/**
	 * Metodo que devuelve los comentarios asociados a una
	 * peticion.
	 * 
	 * @param requestsDTO
	 *            Peticion que tiene los comentarios asociados.
	 * @return Listado de comentarios asociados a la peticion.
	 */
	public PfCommentsDTO getCommentById(Long pk) {
		PfCommentsDTO pfCommentsDTO = (PfCommentsDTO) baseDAO.queryElementOneParameter("request.requestCommentById",
				"idComment", pk);
		return pfCommentsDTO;
	}

	/**
	 * Metodoq ue elimina un comentario de una peticion.
	 * 
	 * @param comment
	 *            Comentario a eliminar.
	 * @param request
	 *            Peticion de la que se elimina el comentario.
	 */
	@Transactional
	public void removeUserComment(PfUsersCommentDTO pfUsersCommentDTO) {
		// Elimina el comentario
		baseDAO.delete(pfUsersCommentDTO);
	}

	public PfUsersCommentDTO queryCommentByIdComment(Long primaryKey, String userComment) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idComment", primaryKey);
		parameters.put("identifier", userComment);

		PfUsersCommentDTO pfUsersCommentDTO = (PfUsersCommentDTO) baseDAO
				.queryElementMoreParameters("request.usersCommentByIdComment", parameters);

		return pfUsersCommentDTO;
	}

	/**
	 * Metodo que añade un comentario a una peticion.
	 * 
	 * @param request
	 *            Peticion a la que se añade el comentario.
	 * @param comment
	 *            Comentario que se desea añadir a la peticion.
	 * @param userDTO
	 *            Usuario que añade el comentario a la peticion.
	 */
	@Transactional
	public void insertRequestComment(PfRequestsDTO request, PfCommentsDTO comment, PfUsersDTO pfUser,
			List<String> users) {
		boolean isNew = false;
		// añade el comentario a la peticion
		comment.setPfRequest(request);
		comment.setPfUser(pfUser);
		comment.setDsubject(Constants.C_TYPE_SUBJECT_COMMENT);
		if (comment.getPrimaryKey() == null) {
			isNew = true;
			comment.setFcreated(new Date());
		} else {
			comment.setFmodified(new Date());
			;
		}

		baseDAO.insertOrUpdate(comment);

		Set<PfUsersCommentDTO> listUsersComment = new HashSet<PfUsersCommentDTO>();
		if (isNew) {
			for (String identifier : users) {
				PfUsersDTO userComment = getUserOrJobByDNI(identifier);

				PfUsersCommentDTO pfUsersCommentDTO = new PfUsersCommentDTO();
				pfUsersCommentDTO.setPfComment(comment);
				pfUsersCommentDTO.setPfUser(userComment);
				pfUsersCommentDTO.setFcreated(new Date());

				insertUserComment(pfUsersCommentDTO);

				listUsersComment.add(pfUsersCommentDTO);
			}
		} else {
			listUsersComment = comment.getPfUsersComments();
		}

		// Actualiza la fecha de modificacion de la peticion
		// request.setFmodified(Calendar.getInstance().getTime());
		// baseDAO.update(request);

		comment.setPfUsersComments(listUsersComment);
	}

	@Transactional
	public void removeRequestComment(PfCommentsDTO pfCommentsDTO, PfRequestsDTO request, PfUsersDTO pfUser) {

		deleteSet(pfCommentsDTO.getPfUsersComments());

		baseDAO.delete(pfCommentsDTO);

		// Actualiza la fecha de modificacion de la peticion
		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);
	}

	@Transactional
	public List<AbstractBaseDTO> queryCommentByRequest(PfRequestsDTO request, PfUsersDTO user) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", request);
		parameters.put("user", user);

		return baseDAO.queryListMoreParameters("request.usersCommentByRequest", parameters);
	}

	private void deleteSet(Set<?> set) {
		Iterator<?> it = set.iterator();
		while (it.hasNext()) {
			AbstractBaseDTO dto = (AbstractBaseDTO) it.next();
			baseDAO.delete(dto);
		}
	}

	/**
	 * Método que almacena en base de datos la información referente al reenvío
	 * de una petición.
	 * 
	 * @param requestDTO
	 *            Objeto que define la petición que se va a modificar.
	 * @param destinatarios
	 *            Listado de firmantes de la petición.
	 * @param stateTags
	 *            Etiquetas que se pueden asociar a la petición.
	 * @throws CustodyServiceException
	 * @throws IOException
	 */
	@Transactional(readOnly = false, rollbackFor = { CustodyServiceException.class, IOException.class })
	public void forwardRequest(PfRequestsDTO requestDTO, List<UserEnvelope> destinatarios, Map<String, PfTagsDTO> stateTags,
			Long ultimoUsuario, Boolean asignarDocumentos) throws CustodyServiceException, IOException {
		requestDTO.setFmodified(new Date());
		baseDAO.insertOrUpdate(requestDTO);
		insertSignersAndNewTag(requestDTO, destinatarios, stateTags);

		List<Long> idNuevosUsuarios = new ArrayList<Long>();

		for (UserEnvelope ue : destinatarios) {
			idNuevosUsuarios.add(ue.getPk());
		}

		if (asignarDocumentos) {
			addCommentsAndAttachmentsToUser(ultimoUsuario, requestDTO.getPrimaryKey(), idNuevosUsuarios);
		}
	}

	public RequestTagListDTO getMessageListPaginatedByUser(Paginator paginator, PfUsersDTO user) {
		PfProvinceDTO province = user.getPfProvince();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user.getPrimaryKey());
		parameters.put("province", province.getPrimaryKey());

		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();

		long countData = baseDAO.queryCount("request.userMessagesByUserCount", parameters);
		
		List<AbstractBaseDTO> abstractList = baseDAO.queryListMoreParametersWithTransformer(
				"request.userMessagesByUser", parameters, firstPosition, paginator.getPageSize(),
				PfUserMessageDTO.class);

		return new RequestTagListDTO(abstractList, countData);
	}
	
	public RequestTagListDTO getPfInvitedRequestListPaginatedByUser (Paginator paginator, PfUsersDTO user, String searchFilter){
		long countData = 0;
		List<AbstractBaseDTO> abstractList = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user.getPrimaryKey());
		
		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();
		
		if(!Util.esVacioONulo(searchFilter)){
			parameters.put("searchFilter", "%" + searchFilter + "%");
			countData = baseDAO.queryCount("request.invitedRequestByCreationUserCountWithSearchFilter", parameters);
			abstractList = baseDAO.queryPaginatedListMoreParameters("request.invitedRequestByCreationUserWithSearchFilter", 
					parameters, firstPosition, paginator.getPageSize());
		}else{
			countData = baseDAO.queryCount("request.invitedRequestByCreationUserCount", parameters);
			abstractList = baseDAO.queryPaginatedListMoreParameters("request.invitedRequestByCreationUser", 
					parameters, firstPosition, paginator.getPageSize());
		}					
			
		return new RequestTagListDTO(abstractList, countData);
	}

	public String getMessagesUnresolved(PfUsersDTO user) {

		PfProvinceDTO province = user.getPfProvince();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user.getPrimaryKey());
		parameters.put("province", province.getPrimaryKey());

		long countData = baseDAO.queryCount("request.userMessagesUnresolvedByUserCount", parameters);

		return String.valueOf(countData);
	}

	public PfUsersMessageDTO queryUsersMessageByPk(Long primaryKey) {

		PfUsersMessageDTO pfUsersMessageDTO = (PfUsersMessageDTO) baseDAO
				.queryElementOneParameter("request.usersMessageByPk", "pk", primaryKey);

		return pfUsersMessageDTO;
	}

	public PfMessagesDTO queryMessageByPk(Long primaryKey) {

		PfMessagesDTO pfMessagesDTO = (PfMessagesDTO) baseDAO
				.queryElementOneParameter("administration.messageAdmPkQuery", "pk", primaryKey);

		return pfMessagesDTO;
	}

	/**
	 * Metodo que añade un mensaje de un usuario.
	 * 
	 * @param message
	 *            Mensaje.
	 * @param currentUser
	 *            Usuario actual.
	 * @param tagID
	 *            Etiqueta
	 */
	@Transactional
	public PfUsersMessageDTO insertUsersMessage(PfMessagesDTO message, PfUsersDTO currentUser, String tagID)
			throws PfirmaException {

		List<AbstractBaseDTO> tagList = baseDAO.queryListOneParameter("request.tagID", "tagID", tagID);
		PfTagsDTO tagNuevo = (PfTagsDTO) tagList.get(0);

		PfUsersMessageDTO pfUsersMessageDTO = new PfUsersMessageDTO();
		pfUsersMessageDTO.setPfUser(currentUser);
		pfUsersMessageDTO.setPfMessage(message);
		pfUsersMessageDTO.setPfTag(tagNuevo);
		pfUsersMessageDTO.setCcreated(currentUser.getCidentifier());
		pfUsersMessageDTO.setFcreated(Calendar.getInstance().getTime());

		baseDAO.insertOrUpdate(pfUsersMessageDTO);

		return pfUsersMessageDTO;

	}

	@Transactional(readOnly = false, rollbackFor = { CustodyServiceException.class, IOException.class })
	public void changeStatusUsersMessage(PfUsersMessageDTO pfUsersMessageDTO, PfUsersDTO user, String tag) {

		// Cambia el estado del mensaje del usuario
		List<AbstractBaseDTO> tagList = baseDAO.queryListOneParameter("request.tagID", "tagID", tag);
		if (tagList != null && tagList.size() > 0) {
			PfTagsDTO tagRead = (PfTagsDTO) tagList.get(0);
			pfUsersMessageDTO.setPfTag(tagRead);
			pfUsersMessageDTO.setCmodified(user.getCidentifier());
			pfUsersMessageDTO.setFmodified(Calendar.getInstance().getTime());

			baseDAO.insertOrUpdate(pfUsersMessageDTO);
		}
	}

	public List<User> cargarRemitentes(PfRequestsDTO request) {
		List<User> users = new ArrayList<User>();
		List<String> remitterList = new ArrayList<String>();
		// carga los remitentes de la petición
		User userWeb = null;
		if (request.getPfUsersRemitters() != null && !request.getPfUsersRemitters().isEmpty()) {
			for (PfUsersRemitterDTO userRemitterDTO : request.getPfUsersRemitters()) {
				remitterList.add(userRemitterDTO.getPfUser().getCidentifier());
				userWeb = new User();
				userWeb.setNif(userRemitterDTO.getPfUser().getCidentifier());
				if (userRemitterDTO.getPfGroup() != null) {
					userWeb.setGroup(userRemitterDTO.getPfGroup().getPrimaryKeyString());
				} else {
					userWeb.setGroup("");
				}

				users.add(userWeb);
			}
		}

		// carga los firmantes de la petición
		for (PfSignLinesDTO signLineDTO : request.getPfSignsLinesList()) {
			for (PfSignersDTO signerDTO : signLineDTO.getPfSigners()) {
				if (!remitterList.contains(signerDTO.getPfUser().getCidentifier())) {
					userWeb = new User();
					userWeb.setNif(signerDTO.getPfUser().getCidentifier());
					users.add(userWeb);
				}
			}
		}
		return users;
	}

	@Transactional
	public NuevosArchivosAnexosResponseList guardarNuevosAnexos(ArchivoTemporalResponseList archivoTemporalResponseList,
			PfUsersDTO pfUser) throws IOException, CustodyServiceException {

		NuevosArchivosAnexosResponseList returnValue = new NuevosArchivosAnexosResponseList();
		returnValue.setLista(new ArrayList<NuevosArchivosAnexosResponse>());

		for (ArchivoTemporalResponse it : archivoTemporalResponseList.getListArchivoTemporalResponse()) {
			String pathname = Constants.PATH_TEMP + Constants.FS + Constants.PATH_TEMP_NEW_ATTACHMENTS_REQUEST
					+ Constants.FS + it.getIdCarpetaTemporal() + Constants.FS + it.getIdCarpetaTemporalArchivo()
					+ Constants.FS;
			File f = new File(pathname);
			File[] archivosInternos = f.listFiles();

			if (archivosInternos != null) {
				for (int i = 0; i < archivosInternos.length; ++i) {
					if (archivosInternos[i].isFile()) {
						FileInputStream fis = new FileInputStream(archivosInternos[i]);
						byte[] contenidoArchivo = IOUtils.toByteArray(fis);
						fis.close();
						PfDocumentsDTO documento = createDocument(archivosInternos[i].getName(), it.getTipoDeArchivo(),
								contenidoArchivo, false, archivoTemporalResponseList.getPeticionAsociada(), pfUser);

						documento.setUsuXUsuarioAnexa(pfUser);

						InputStream inputCheck = new FileInputStream(archivosInternos[i]);
						InputStream inputStore = new FileInputStream(archivosInternos[i]);
						PfFilesDTO fileDTO = insertFile(documento, inputStore, inputCheck,
								archivosInternos[i].length());
						inputCheck.close();
						inputStore.close();
						documento.setPfFile(fileDTO);

						PfDocumentScopesDTO documentScopeDTO = (PfDocumentScopesDTO) baseDAO
								.queryElementOneParameter("request.queryInternalScope", null, null);

						documento.setPfDocumentScope(documentScopeDTO);					

						documento.setComentarioAnexo(archivoTemporalResponseList.getComentarioDeAnexos());

						baseDAO.insertOrUpdate(documento);
						
						PfDocumentTypesDTO tipoDocumento = (PfDocumentTypesDTO) baseDAO.getEntityManager().find(
								documento.getPfDocumentType().getClass(),
								documento.getPfDocumentType().getPrimaryKey());
						returnValue.getLista()
								.add(new NuevosArchivosAnexosResponse(documento.getDname(),
										tipoDocumento.getDdocumentType(),
								pfUser.getFullName(), documento.getComentarioAnexo(), documento.getChash(), documento.isTcn() && applicationVO.getViewTCNActivated(), tipoDocumento.getCdocumentType().equals( "FACTURAE")));
					}
				}
			}
		}
		return returnValue;
	}

	@SuppressWarnings("unused")
	private PfDocumentsDTO createDocument(String fileName, String fileMime, byte[] file, boolean firmable,
			String requestId, PfUsersDTO pfUser) {
		PfDocumentsDTO documentDTO = new PfDocumentsDTO();

		documentDTO.setChash("tempHash");

		documentDTO.setPfUsuariosList(new ArrayList<PfUsersDTO>());

		// Trunca la ruta del nombre del documento
		documentDTO.setDname(Util.getInstance().getNameFile(fileName));

		// Si el documento tiene extensi&oacute;n
		if (Util.getInstance().hasExtension(fileName)) {
			// Ponemos el tipo de documento en el DTO a partir de la extesión
			documentDTO.setDmime(Util.getInstance().getMimeTypeOf(fileName));
			// Si no tiene extensi&oacute;n
		} else if (fileMime != null && !"".equals(fileMime)) {
			// Ponemos el tipo de documento en el DTO a partir del fileMime
			documentDTO.setDmime(fileMime);
		} else {//Si no ponemos la de por defecto de la base de datos
			documentDTO.setDmime(MimeType.DEFAULT_MIMETYPE);
		}

		// Indica que el documento es firmable
		documentDTO.setLsign(firmable);
		PfDocumentTypesDTO documentType = new PfDocumentTypesDTO();
		PfDocumentTypesDTO documentTypeDefault = applicationBO.queryDocumentTypeGeneric();
		documentType.setPrimaryKey(documentTypeDefault.getPrimaryKey());
		documentDTO.setPfDocumentType(documentType);

		documentDTO.setLissign(false);

		PfRequestsDTO request = queryRequestHash(requestId);

		documentDTO.setPfRequest(request);

		List<Signer> signers = new ArrayList<Signer>();

		for (PfSignLinesDTO signLineDTO : request.getPfSignsLinesList()) {
			// Se recuperan los firmantes de la línea de firma
			boolean isTerminate = false;
			for (PfSignersDTO signerDTO : signLineDTO.getPfSigners()) {
				Signer signer = new Signer();
				signer.setPfUser(signerDTO.getPfUser());
				// Se recupera el estado de la petición por petición, usuario y
				// linea de firma
				PfRequestTagsDTO stateRequestTag = tagBO.queryStateUserSignLine(request, signerDTO.getPfUser(),
						signLineDTO);
				String status = stateRequestTag.getPfTag().getCtag();
				isTerminate = status.equals(Constants.C_TAG_SIGNED) || status.equals(Constants.C_TAG_PASSED);
				// Se deja cargado el estado para cada linea de firma y cada
				// usuario por si fuera necesario en el futuro.
				signer.setStateTag(stateRequestTag.getPfTag());
				signers.add(signer);
			}
		}

		boolean encontroUsuario = false;

		for (int i = 0; i < signers.size(); ++i) {
			if (signers.get(i).getPfUser().getPrimaryKey().equals(pfUser.getPrimaryKey())) {
				encontroUsuario = true;
			}
			if (encontroUsuario) {
				documentDTO.getPfUsuariosList().add(signers.get(i).getPfUser());
			}
		}

		return documentDTO;
	}

	public void addCommentsAndAttachmentsToUser(Long idUltimoUsuario, Long idRequest, List<Long> idsNuevosUsuarios) {

		String idsConcatenados = "";

		for (Long it : idsNuevosUsuarios) {
			idsConcatenados += it + " , ";
		}

		idsConcatenados += " null ";

		String insertCommentarios = " INSERT " + " INTO PF_USUARIOS_COMENTARIO " + "   ( "
				+ "     X_USUARIO_COMENTARIO, C_CREADO, " + "     F_CREADO, " + "     C_MODIFICADO, "
				+ "     F_MODIFICADO, " + "     COM_X_COMENTARIO, " + "     USU_X_USUARIO " + "   ) " + " ( "
				+ " select PF_S_UCOM.nextval, comentarios_query.C_CREADO, sysdate f_creado, comentarios_query.C_MODIFICADO, sysdate f_modificado, comentarios_query.X_COMENTARIO id_comentario ,usuarios_query.X_USUARIO id_usuario from ( "
				+ " SELECT co.X_COMENTARIO, co.C_CREADO, co.C_MODIFICADO " + " FROM PF_USUARIOS_COMENTARIO uc "
				+ " left join PF_COMENTARIOS co on ( co.X_COMENTARIO = uc.COM_X_COMENTARIO ) "
				+ " where uc.USU_X_USUARIO = " + idUltimoUsuario + " and co.PET_X_PETICION=" + idRequest
				+ ") comentarios_query, " + " (SELECT X_USUARIO " + " FROM PF_USUARIOS  " + " where X_USUARIO in ( " +

		idsConcatenados +

		" ) and X_USUARIO not in ( " +

		" select distinct USU_X_USUARIO from PF_USUARIOS_COMENTARIO where COM_X_COMENTARIO in ( "
				+ " select uc.COM_X_COMENTARIO "
				+ " from PF_USUARIOS_COMENTARIO uc left join PF_COMENTARIOS co on ( co.X_COMENTARIO = uc.COM_X_COMENTARIO ) "
				+ " where uc.USU_X_USUARIO = " + idUltimoUsuario + " and co.PET_X_PETICION = " + idRequest + ") "
				+ " ) ) usuarios_query " +

		" ) ";

		baseDAO.invokeDdl(insertCommentarios);

		insertCommentarios = " INSERT " + " INTO PF_USUARIOS_DOC_VISIBLES " + "   ( " + "     USU_X_USUARIO, "
				+ "     DOC_X_DOCUMENTO " + "   ) "
				+ " ( select usuarios_query.X_USUARIO, docs.DOC_X_DOCUMENTO from (select udv.DOC_X_DOCUMENTO from PF_USUARIOS_DOC_VISIBLES udv "
				+ " left join PF_DOCUMENTOS d on ( udv.DOC_X_DOCUMENTO = d.X_DOCUMENTO ) " + " where d.PET_X_PETICION="
				+ idRequest + " and udv.USU_X_USUARIO = " + idUltimoUsuario + ") docs, " + " (SELECT X_USUARIO "
				+ " FROM PF_USUARIOS  " + " where X_USUARIO in ( " + idsConcatenados + " ) and X_USUARIO not in ( " +

		" select distinct USU_X_USUARIO  " + " from PF_USUARIOS_DOC_VISIBLES where DOC_X_DOCUMENTO in ( "
				+ " select udv.DOC_X_DOCUMENTO  " + " from PF_USUARIOS_DOC_VISIBLES udv  "
				+ " left join PF_DOCUMENTOS d on ( d.X_DOCUMENTO = udv.DOC_X_DOCUMENTO ) "
				+ " where udv.USU_X_USUARIO = " + idUltimoUsuario + " and d.PET_X_PETICION = " + idRequest + " ) " +

		" ) ) usuarios_query ) ";

		baseDAO.invokeDdl(insertCommentarios);

	}

	public void loadUsersToPick(UserSelection userSelection, PfUsersDTO user, String seatCode, String signers,
			String signLinesConfig, String userNameFilter, String userTypeFilter, String userSeatFilter,
			String portafirmasFilter, Boolean firstTime, String signLinesAccion) {
		List<UserEnvelope> userSelectionList = filterUserList(userNameFilter, userTypeFilter, userSeatFilter, portafirmasFilter, user, seatCode);
		// Se filtran los usuarios si el usuario tiene restringidos los destinatarios
		List<UserEnvelope> usersBorrar = new ArrayList<UserEnvelope>();
		List<PfUsersDTO> validUserList = restrictionBO.queryUserRestrict(user);
		if(!validUserList.isEmpty()){
			for (UserEnvelope userAux : userSelectionList){
				boolean encontrado = false;
				for (PfUsersDTO userValidDTO : validUserList){
					UserEnvelope userEvelope= new UserEnvelope(userValidDTO);
					if (userEvelope.getPk().equals(userAux.getPk())){
						encontrado = true;
					}
				}
				if (!encontrado){
					usersBorrar.add(userAux);
				}
			}
		}
		if(!usersBorrar.isEmpty()){
			userSelectionList.removeAll(usersBorrar);
		}
		List<UserEnvelope> signerSelectionList = getUserListFromString(signers, signLinesConfig, false, null, signLinesAccion);
		List<UserEnvelope> resultList = markSelected(userSelectionList, signerSelectionList);
		userSelection.setUserSelectionList(resultList);
	}

	public List<UserEnvelope> markSelected(List<UserEnvelope> nonSelected, List<UserEnvelope> selected) {
		List<UserEnvelope> resultList = new ArrayList<UserEnvelope>();
		for (UserEnvelope item : selected) {
			item.setSelected(true);
			resultList.add(item);
		}
		for (UserEnvelope item : nonSelected) {
			int index = resultList.indexOf(item);
			item.setSelected(false);
			if (index < 0) {
				resultList.add(item);
			}
		}
		
		return resultList;
	}

	@Transactional
	public void markAsReadForRemitter(PfRequestsDTO pfRequest, PfUsersDTO user) {
		for (PfUsersRemitterDTO it : pfRequest.getPfUsersRemitters()) {
			if (it.getPfUser().getPrimaryKey().equals(user.getPrimaryKey())) {
				it.setEstadoParaRemitente(EstadosParaRemitente.LEIDO);
				baseDAO.insertOrUpdate(it);
				break;
			}
		}
	}

	@Transactional
	public void markAsUnReadForRemitter(PfRequestsDTO pfRequest, PfUsersDTO user) {
		for (PfUsersRemitterDTO it : pfRequest.getPfUsersRemitters()) {
			if (it.getPfUser().getPrimaryKey().equals(user.getPrimaryKey())) {
				it.setEstadoParaRemitente(EstadosParaRemitente.PENDIENTE_DE_LECTURA);
				baseDAO.insertOrUpdate(it);
				break;
			}
		}
	}

	@Transactional
	public void markAsUnReadForRemitters(PfRequestsDTO pfRequest) {
		for (PfUsersRemitterDTO it : pfRequest.getPfUsersRemitters()) {
			it.setEstadoParaRemitente(EstadosParaRemitente.PENDIENTE_DE_LECTURA);
			baseDAO.insertOrUpdate(it);
		}
	}
	
	/**
	 * Metodo que marca como anulada una peticion o cancela una anulacion.
	 * 
	 * @param request Peticion que se va a anular.
	 * @param anular Indica si la peticion se ha de anular (true) o cancelar su anulacion (false).
	 * 
	 */
	@Transactional
	public void cancelRequest(PfRequestsDTO request, boolean anular) {
		request.setFmodified(new Date());
		if(anular){
			List<AbstractBaseDTO> requestTagList = baseDAO.queryListOneParameter("request.stateTagRequest", "request",	request);
			//Recorre las etiquetas de estado y por cada una añade una de sistema de tipo "TIPO.ANULADA'
			for(int i =0;i<requestTagList.size();i++){
				PfRequestTagsDTO reqTag = (PfRequestTagsDTO) requestTagList.get(i);
				// Crea la etiqueta TIPO.ANULADA para cada línea de la petición
				PfRequestTagsDTO reqTagAnulledType = new PfRequestTagsDTO();
				reqTagAnulledType.setPfRequest(request);
				reqTagAnulledType.setPfUser(reqTag.getPfUser());
				reqTagAnulledType.setPfTag(applicationVO.getSystemTags().get(Constants.C_TAG_ANULLED_TYPE));
				reqTagAnulledType.setPfSignLine(reqTag.getPfSignLine());
				reqTagAnulledType.setChash(Util.getInstance().createHash());
				baseDAO.insertOrUpdate(reqTagAnulledType);
			}
		}else{
			//Se borran las etiquetas TIPO.ANULADA de tipo SISTEMA de todas las líneas de la petición
			List<PfRequestTagsDTO> requestTagList = this.getAnulledRequestTags(request);
			for(int i = 0;i<requestTagList.size();i++){
				PfRequestTagsDTO reqTag = requestTagList.get(i);
				baseDAO.delete(reqTag);
			}
		}
		baseDAO.insertOrUpdate(request);
	}
	
	
	/**Se obtiene una lista de Etiquetas de peticion anulada.
	 * @param request
	 * @return
	 */
	public List<PfRequestTagsDTO> getAnulledRequestTags(PfRequestsDTO request){
		return baseDAO.queryListOneParameter("request.systemAnulledType", "request", request);
	}
	
	public PfCommentsDTO saveComment(String text, PfUsersDTO user, PfRequestsDTO request){
		
		Set<PfUsersCommentDTO> listUsersComment = new HashSet<PfUsersCommentDTO>();
		
		PfCommentsDTO comment = new PfCommentsDTO();
		comment.setTcomment(text);
		comment.setDsubject(Constants.C_TAG_REMOVED);
		comment.setPfRequest(request);
		comment.setPfUser(user);
		
		baseDAO.insertOrUpdate(comment);
		
		listUsersComment = saveUserComment(comment, user, listUsersComment);
		
		comment.setPfUsersComments(listUsersComment);
		
		return comment;
	}
	
	public Set<PfUsersCommentDTO> saveUserComment(PfCommentsDTO comment, PfUsersDTO user, Set<PfUsersCommentDTO> listUsersComment){

		PfUsersCommentDTO pfUsersCommentDTO = new PfUsersCommentDTO();
		pfUsersCommentDTO.setPfComment(comment);
		pfUsersCommentDTO.setPfUser(user);
		pfUsersCommentDTO.setFcreated(new Date());
		
		insertUserComment(pfUsersCommentDTO);
		
		listUsersComment.add(pfUsersCommentDTO);
		
		return listUsersComment;
	}

	@Transactional
	public void bloquearPeticion(PfRequestTagsDTO requestTagDTO, PfUsersDTO usuarioConectado) throws BlockRequestException {
		PfRequestsDTO peticion = requestTagDTO.getPfRequest();
		if (!peticion.getLcascadeSign()) {
			if (esPeticionBloqueada(peticion, usuarioConectado)) {
				throw new BlockRequestException();
			} else {
				peticion.setFechaIntentoFirma(new Date());
				peticion.setUsuarioBloqueo(usuarioConectado);
				baseDAO.update(peticion);
			}
		}
	}

	private boolean esPeticionBloqueada(PfRequestsDTO peticion, PfUsersDTO usuarioConectado) {
		boolean bloqueada = false;
		Date fechaIntentoFirma = peticion.getFechaIntentoFirma();
		PfUsersDTO usuarioBloqueo = peticion.getUsuarioBloqueo();
		if(usuarioBloqueo != null && fechaIntentoFirma != null) {
			// Sólo se evalua si hay bloqueo cuando el usuario que bloqueó y el conectado NO son el mismo
			if (!usuarioBloqueo.equals(usuarioConectado)) {
				PfConfigurationsParameterDTO signWaitTime = applicationBO.getSignWaitTime();
				String minutos = signWaitTime.getTvalue();
				Date ahoraMenosMargen = dateComponent.addMinutes(new Date(), - new Integer(minutos));
				bloqueada =  fechaIntentoFirma.after(ahoraMenosMargen);
			}
		}
		return bloqueada;
	}

	@Transactional()
	public void desbloquearPeticion(String requestTagHash, PfUsersDTO usuarioConectado) {
		try {
			PfRequestTagsDTO requestTagDTO = this.queryRequestTagByHash(requestTagHash);
			PfRequestsDTO requestDTO = requestTagDTO.getPfRequest();
			// El usuario conectado sólo puede desbloquear si es el usuario que bloqueó
			if(requestDTO.getUsuarioBloqueo() != null && requestDTO.getUsuarioBloqueo().equals(usuarioConectado)) {
				requestDTO.setFechaIntentoFirma(null);
				requestDTO.setUsuarioBloqueo(null);
				baseDAO.insertOrUpdate(requestDTO);
			}
		} catch (Exception e) {
			log.warn("Falló el desbloque de la peticion: " + requestTagHash, e);
		}
	}
	
	
	/**
	 * Recorre la lista de etiquetas peticion, mirando si la anterior linea de firma esta firmada. Si así fuera, pone el
	 * atributo usuarioFirma anterior con el usuario
	 */
	public void fillUltimaFirma(RequestTagListDTO reqTagList) {
		for (int i = 0; i < reqTagList.size(); i++) {
			PfRequestTagsDTO reqTag = (PfRequestTagsDTO) reqTagList.get(i);
			reqTag.getPfRequest().setUltimoFirmante(obtenerUltimoFirmante(reqTag.getPfRequest()));
		}
	}
	
	private PfUsersDTO obtenerUltimoFirmante(PfRequestsDTO req) {
		
		PfUsersDTO retorno = null;
		List<AbstractBaseDTO> userHistoricRequests = historicRequestBO.getUsersHistoricRequest(req);
		
		for (AbstractBaseDTO abstractBaseDTO : userHistoricRequests) {
			PfHistoricRequestsDTO historicRequest = (PfHistoricRequestsDTO) abstractBaseDTO;
			if (Constants.C_HISTORIC_REQUEST_SIGNED
					.equals(historicRequest.getChistoricRequest())) {
				retorno = historicRequest.getPfUser();
			}
		}
		return retorno;
	}
	
	public void fillRemitenteInterfazGenerica(RequestTagListDTO reqTagList) {
		for (int i = 0; i < reqTagList.size(); i++) {
			PfRequestTagsDTO reqTag = (PfRequestTagsDTO) reqTagList.get(i);
			PfRequestsDTO requestDTO = reqTag.getPfRequest();
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = (PfDocelwebRequestSpfirmaDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_DOCEL_PFIRMA_REQUEST_BY_HASH, DocelwebConstants.QUERY_PARAM_FILE_HASH, requestDTO.getChash());
			if (docelRequestDTO != null && docelRequestDTO.getdSenderName()!= null && !"".equals(docelRequestDTO.getdSenderName())) {
				requestDTO.setRemitenteInterfazGenerica(docelRequestDTO.getdSenderName());
			}
		}
	}
	
}
