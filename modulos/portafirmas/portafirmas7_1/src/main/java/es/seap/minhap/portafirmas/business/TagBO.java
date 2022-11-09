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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSmanagerDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSpfirmaDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUserTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersCommentDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.InterfazGenericaServerBO;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business.NotificacionGenericaClientBO;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business.exception.NotificacionGenericaClientException;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class TagBO {

	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private HistoricRequestBO historicRequestBO;

	@Autowired
	private InterfazGenericaServerBO interfazGenericaServerBO;

	@Autowired
	private NotificacionGenericaClientBO notificacionGenericaClientBO;
	
	@Autowired
	private ApplicationVO applicationVO;
	
	@Autowired
	private UtilComponent util;

	@Resource(name = "messageProperties")
	private Properties messages;

	Logger log = Logger.getLogger(TagBO.class);

	/**
	 * Borra una etiqueta de petición de la base de datos
	 * @param requestTag la etiqueta que va a ser borrada de la bbdd
	 */
	public void deleteUserTagRequest(PfRequestTagsDTO requestTag) {
		log.info("deleteUserTagRequest init");
		baseDAO.delete(requestTag);
		log.info("deleteUserTagRequest end");
	}

	@Transactional(readOnly = false)
	public void deleteUserIndividualTagRequest(PfRequestTagsDTO requestTag) {
		log.info("deleteUserTagRequest init");
		baseDAO.delete(requestTag);
		log.info("deleteUserTagRequest end");
	}
	
	/**
	 * M&eacute;todo que cambia la etiqueta de una peticióna "Rechazada".
	 * @param request Petición a cambiar.
	 * @param text Texto explicativo con el motivo del rechazo.
	 * @param user Usuario que rechaza la petición.
	 */
	public void changeStateToRejected(PfRequestTagsDTO requestTag, String text, PfUsersDTO user) {
		PfUsersDTO userDTO = userDTOFromUserOrJob(user, user.getValidJob(),	false, requestTag);
		List<AbstractBaseDTO> requestTagList = baseDAO.queryListOneParameter(
				"request.stateTagRequest", "request", requestTag.getPfRequest());

		PfTagsDTO etiquetaRechazo = applicationVO.getStateTags().get(Constants.C_TAG_REJECTED);
		for (AbstractBaseDTO reqTag : requestTagList) {
			((PfRequestTagsDTO) reqTag).setPfTag(etiquetaRechazo);
			baseDAO.update(reqTag);
		}

		// save historic request
		historicRequestBO.saveHistoricRequest(requestTag.getPfRequest(), user,
				Constants.C_HISTORIC_REQUEST_REJECTED, userDTO.isJob());

		guardarComentario(requestTag, text, user);

		// execute notices
		// check if request should be noticed when state is read
		if (noticeBO.noticeWhenState(requestTag.getPfRequest(), etiquetaRechazo.getCtag())) {
			noticeBO.noticeStateChange(requestTag.getPfRequest(), applicationVO.getEmail(), applicationVO.getSMS());
		}

		// Actualizamos la petición DOCEL si es de este tipo
		PfDocelwebRequestSpfirmaDTO pfDocelRequestToNotify = interfazGenericaServerBO.changeDocelRequestStatus(requestTag.getPfRequest(), DocelwebConstants.REQUEST_STATE_REJECTED);
		if (pfDocelRequestToNotify != null) {
			try {
				// Se notifica a portafirmas externo si procede
				notificacionGenericaClientBO.notificarDevolucionSolicitud(pfDocelRequestToNotify, text);
			} catch (NotificacionGenericaClientException e) {
				log.error("No se ha podido notificar el rechazo de la solicitud DOCEL al Sistema de Gestión", e);
			}
		}

		// Change request modified date
		requestTag.getPfRequest().setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(requestTag.getPfRequest());
	}

	private void guardarComentario(PfRequestTagsDTO requestTag, String text, PfUsersDTO user) {
		if(util.noEsVacio(text)) {
			PfCommentsDTO comment = new PfCommentsDTO();
			comment.setTcomment(text);
			comment.setDsubject(Constants.C_TAG_REJECTED);
			comment.setPfRequest(requestTag.getPfRequest());
			comment.setPfUser(user);
			
			Set<PfUsersCommentDTO> listUsersComment = new HashSet<PfUsersCommentDTO>();	
			
			List<User> userList = requestBO.cargarRemitentes(requestTag.getPfRequest());
			
			for (User u : userList) {
				String identifier = u.getNif();
				PfUsersDTO userComment = requestBO.getUserOrJobByDNI(identifier);
				PfUsersCommentDTO pfUsersCommentDTO = new PfUsersCommentDTO();
				pfUsersCommentDTO.setPfComment(comment);
				pfUsersCommentDTO.setPfUser(userComment);
				pfUsersCommentDTO.setFcreated(new Date());
				listUsersComment.add(pfUsersCommentDTO);
			}
			
			requestTag.getPfRequest().getPfUserTagsList();
			comment.setPfUsersComments(listUsersComment);
			
			baseDAO.insertOrUpdate(comment);
		}
	}

	/**
	 * M&eacute;todo que cambia la etiqueta de una peticióna "Rechazada".
	 * @param request Petición a cambiar.
	 * @param text Texto explicativo con el motivo del rechazo.
	 * @param user Usuario que rechaza la petición.
	 * @param textoRechazo 
	 */
	public void changeStateToRejected(PfDocelwebRequestSmanagerDTO docelRequest, String textoRechazo) {
		PfTagsDTO etiquetaRechazo = applicationVO.getStateTags().get(Constants.C_TAG_REJECTED);
		PfRequestTagsDTO requestTag = docelRequest.getPfEtiquetaPeticion();
		PfUsersDTO user = requestTag.getPfUser();
		
		List<AbstractBaseDTO> requestTagList = baseDAO.queryListOneParameter("request.stateTagRequest", "request", requestTag.getPfRequest());
		for (AbstractBaseDTO reqTag : requestTagList) {
			((PfRequestTagsDTO) reqTag).setPfTag(etiquetaRechazo);
			baseDAO.update(reqTag);
		}

		guardarComentario(requestTag, textoRechazo, user);

		if (noticeBO.noticeWhenState(requestTag.getPfRequest(), etiquetaRechazo.getCtag())) {
			noticeBO.noticeStateChange(requestTag.getPfRequest(), applicationVO.getEmail(), applicationVO.getSMS());
		}

		requestTag.getPfRequest().setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(requestTag.getPfRequest());
		
		docelRequest.setdState(DocelwebConstants.REQUEST_STATE_REJECTED);
		docelRequest.setTextRejct(textoRechazo);
		baseDAO.insertOrUpdate(docelRequest);
	}

	/**
	 * M&eacute;todo que actualiza a "Rechazada" el estado de todas las peticiones de una lista.
	 * @param requestList Listado de peticiones a rechazar.
	 * @param text Texto de rechazo.
	 * @param user Usuario que rechaza las peticiones.
	 */
	public void changeStateToRejectedList(List<AbstractBaseDTO> requestList, String text, PfUsersDTO user) {
		log.info("changeStateToRejectedList init");
		for (AbstractBaseDTO requestTag : requestList) {
			changeStateToRejected((PfRequestTagsDTO) requestTag, text, user);
			
		}
		log.info("changeStateToRejectedList end");
	}
	
	


	/**
	 * M&eacute;todo que calcula el valor de la etiqueta de una petición de un usuario.
	 * El valor por defecto es "FIRMADA".
	 * @param requestTag Petición a etiquetar.
	 * @param user Usuario al que pertenece la petición.
	 * @return Texto de la etiqueta de la petición.
	 */
	public String nextStateUser(PfRequestTagsDTO requestTag, PfUsersDTO user) {		
		return nextStateUser(requestTag, user, Constants.C_TAG_SIGNED);
	}

	/**
	 * M&eacute;todo que calcula el valor de la etiqueta de una petición de un usuario.
	 * El valor por defecto es "VISTO BUENO".
	 * @param request Petición a etiquetar.
	 * @param user Usuario al que pertenece la petición.
	 * @return Texto de la etiqueta de la petición.
	 */
	public String nextStateUserPass(PfRequestTagsDTO requestTag, PfUsersDTO user) {
		return nextStateUser(requestTag, user, Constants.C_TAG_PASSED);
	}

	/**
	 * M&eacute;todo que calcula el valor de la etiqueta de una petición de un usuario.
	 * @param request Petición a etiquetar.
	 * @param user Usuario al que pertenece la petición.
	 * @param defaultState El estado ha asignar por defecto a la etiqueta.
	 * @return Texto de la etiqueta de la petición.
	 */
	private String nextStateUser(PfRequestTagsDTO requestTag, PfUsersDTO user, String defaultState) {

		log.info("nextStateUser init");
		
		String result = defaultState;

		// Query sign line by user
		log.debug("Query sign line by user");
		List<AbstractBaseDTO> signLineList = querySignLineListByUser(requestTag.getPfRequest(), user.getCidentifier());

		// Check every sign line
		log.debug("Check every sign line");
		for (AbstractBaseDTO sli : signLineList) {
			PfSignLinesDTO signLine = (PfSignLinesDTO) sli;
			// Si es la línea que se ha tratado se pone la etiqueta por defecto
			if (requestTag.getPfSignLine() != null && 
				signLine.getPrimaryKeyString().equals(requestTag.getPfSignLine().getPrimaryKeyString())) {
				result = defaultState;
			} else if (!isSignLineSigned(signLine)) {
				// Linea no firmada
				// ESPERANDO: Petición en cascada y además el usuario no forma parte de la linea anterior (gente pendiente de firmar entre medias),
				// LEIDA: Si la petición no es de tipo primer firmante, y no cumple los requisitos de ESPERANDO.
				// VALOR POR DEFECTO: Si no quedan lineas pendientes para el firmante o la petición es de tipo "Primer Firmante"
				log.debug("Sign line not signed");

				if (requestTag.getPfRequest().getLcascadeSign() && signerFromSignLine(signLine.getPfSignLine(), user) == null) {
					result = Constants.C_TAG_AWAITING;
				} else if (!requestTag.getPfRequest().getLfirstSignerSign()) {
					result = Constants.C_TAG_READ;
				}
				break; //Sólo necesito comprobar la primera linea que no esté firmada.
			}
		}
		log.info("nextStateUser end");
		return result;
	}

	/**
	 * M&eacute;todo que busca a un usuario o cargo de una l&iacute;nea de firma que no ha firmado o no el documento asociado.
	 * @param doc Documento.
	 * @param user El usuario buscado.
	 * @param job El cargo a buscar.
	 * @param signedJob Determina si se busca al usuario habiendo firmado el documento o no habi&eacute;ndolo hecho.
	 * @return El firmante buscado.
	 */
	public PfSignersDTO lastSignerUnresolvedDocument(PfRequestTagsDTO requestTag, PfUsersDTO user, PfUsersDTO job, boolean signedJob) {
		log.info("lastSignerUnresolvedDocument init");
		PfSignersDTO result = null;

		// Check states from user and job
		log.debug("Check states from user and job");

		PfUsersDTO userDTO = userDTOFromUserOrJob(user, job, signedJob, requestTag);
		log.debug("User: " + userDTO.getPrimaryKeyString());

		// Search the last signer
		if (requestTag.getPfSignLine() != null) {
			result = signerFromSignLine(requestTag.getPfSignLine(), userDTO);
		} else {
			log.debug("Cascade request, search the last sign line not signed");
			List<AbstractBaseDTO> signLineList = querySignLineList(requestTag.getPfRequest());
			PfSignLinesDTO signLine = null;
			Iterator<AbstractBaseDTO> it = signLineList.iterator();
			while (it.hasNext() && result == null) {
				signLine = (PfSignLinesDTO) it.next();
				if (!isSignLineSigned(signLine)) {
					log.debug("Last sign line not signed, search signer");
					result = signerFromSignLine(signLine, userDTO);
				}
			}
		}
		log.info("lastSignerUnresolvedDocument end");

		return result;
	}
	
	/**
	 * Devuelve la última línea de firma de tipo FIRMA de una petición
	 * @param pet 
	 * @return
	 */
	/*public PfSignLinesDTO lastSignLineSign (PfRequestsDTO req) {		
		PfSignLinesDTO ultima = null;
		List<AbstractBaseDTO> signLineList = querySignLineList(req);
		PfSignLinesDTO signLine = null;
		Iterator<AbstractBaseDTO> it = signLineList.iterator();
		while (it.hasNext()) {
			signLine = (PfSignLinesDTO) it.next();
			if (signLine.getCtype().contentEquals(Constants.C_TYPE_SIGNLINE_SIGN)) {
				ultima = signLine;
			}
		}
		return ultima;
	}*/
	
	/**
	 * Devuelve las líneas de firma NO FIRMADAS de tipo FIRMA de una petición
	 * @param request
	 * @return
	 */
	public List<PfSignLinesDTO> signLineSignNotSigned (PfRequestsDTO request) {
		List<PfSignLinesDTO> signLinesNotSigned = new ArrayList<PfSignLinesDTO> ();
		List<AbstractBaseDTO> signLineList = querySignLineList(request);
		PfSignLinesDTO signLine = null;
		Iterator<AbstractBaseDTO> it = signLineList.iterator();
		while (it.hasNext()) {
			signLine = (PfSignLinesDTO) it.next();
			if (signLine.getCtype().contentEquals(Constants.C_TYPE_SIGNLINE_SIGN) &&
					!isSignLineSigned(signLine)) {
				signLinesNotSigned.add(signLine);
			}
		}
		return signLinesNotSigned;
	}
	
	/**
	 * Comprueba si un usuario o cargo está en una línea de firma
	 * @param signLine
	 * @param user
	 * @param job
	 * @return
	 */
	public boolean isSignerOfSignLine (PfSignLinesDTO signLine, PfUsersDTO user) {
		Iterator<PfSignersDTO> signers = signLine.getPfSigners().iterator();
		boolean found = false;
		while (signers.hasNext() && !found) {
			PfSignersDTO signer = signers.next();
			if (signer.getPfUser().getPrimaryKeyString().contentEquals(user.getPrimaryKeyString()) ||
					signer.getPfUser().getPrimaryKeyString().contentEquals(user.getValidJob().getPrimaryKeyString())) {
				found = true;
			}
		}
		return found;
	}
		

	public PfSignersDTO lastSignerUnresolvedDocumentPass(PfRequestTagsDTO reqTag, PfUsersDTO user, PfUsersDTO job, boolean signedJob) {
		log.info("lastSignerUnresolvedDocumentPass init");
		PfSignersDTO result = null;

		// Check states from user and job
		log.debug("Check states from user and job");

		PfUsersDTO userDTO = userDTOFromUserOrJob(user, job, signedJob, reqTag);
		log.debug("User: " + userDTO.getPrimaryKeyString());

		// Obtengo el firmante a partir del usuario y la línea de firma
		if (reqTag.getPfSignLine() != null) {
		result = signerFromSignLine(reqTag.getPfSignLine(), userDTO);
		} else {
			// Search the last signer
			log.debug("Cascade request, search the last sign line not signed");
			List<AbstractBaseDTO> signLineList = querySignLineList(reqTag.getPfRequest());
			PfSignLinesDTO signLine = null;
			Iterator<AbstractBaseDTO> it = signLineList.iterator();
			while (it.hasNext() && result == null) {
				signLine = (PfSignLinesDTO) it.next();
				if (!isSignLineSigned(signLine)) {
					log.debug("Last sign line not signed, search signer");
					result = signerFromSignLine(signLine, userDTO);
				}
			}
		}
		log.info("lastSignerUnresolvedDocumentPass end");

		return result;
	}

	/**
	 * M&eacute;todo que devuelve la &uacute;ltima l&iacute;nea de firma no firmada por el usuario en una petición.
	 * @param req Petición.
	 * @param user Usuario que no ha firmado la l&iacute;nea de firma de la petición.
	 * @return Última l&iacute;nea de firma no firmada por el usuario.
	 */
	public PfSignLinesDTO lastSignLineNotSignedOfUser(PfRequestsDTO req, PfUsersDTO user) {
		PfSignLinesDTO result = null;

		List<AbstractBaseDTO> signLineList = querySignLineList(req);
		PfSignLinesDTO signLine = null;
		Iterator<AbstractBaseDTO> it = signLineList.iterator();
		while (it.hasNext() && result == null) {
			signLine = (PfSignLinesDTO) it.next();
			if (!isSignLineSigned(signLine)) {
				boolean isUserLine = false;
				for (PfSignersDTO signer: signLine.getPfSigners()) {
					PfUsersDTO lineUser = signer.getPfUser();
					if (lineUser.getPrimaryKey().equals(user.getPrimaryKey())) {
						isUserLine = true;
						break;
					}
				}
				if (isUserLine) {
					result = signLine;
				}
			}
		}

		return result;
	}	

	/**
	 * M&eacute;todo que devuelve la &uacute;ltima l&iacute;nea de firma no firmada para una petición.
	 * @param req Petición.
	 * @return Última l&iacute;nea de firma no firmada.
	 */
	public PfSignLinesDTO lastSignLineNotSigned(PfRequestsDTO req) {
		PfSignLinesDTO result = null;

		List<AbstractBaseDTO> signLineList = querySignLineList(req);
		PfSignLinesDTO signLine = null;
		Iterator<AbstractBaseDTO> it = signLineList.iterator();
		while (it.hasNext() && result == null) {
			signLine = (PfSignLinesDTO) it.next();
			if (!isSignLineSigned(signLine)) {
				result = signLine;
			}
		}

		return result;
	}

	/**
	 * M&eacute;todo que devuelve la &uacute;ltima l&iacute;nea de firma firmada para una petición.
	 * @param req Petición.
	 * @return Última l&iacute;nea de firma firmada.
	 */
	public PfSignLinesDTO lastSignLineSigned(PfRequestsDTO req) {
		PfSignLinesDTO result = null;

		List<AbstractBaseDTO> signLineList = querySignLineList(req);
		PfSignLinesDTO signLine = null;
		Iterator<AbstractBaseDTO> it = signLineList.iterator();
		while (it.hasNext()) {
			signLine = (PfSignLinesDTO) it.next();
			if (isSignLineSigned(signLine)) {
				result = signLine;
			}
		}

		return result;
	}
	/**
	 * Recupera el usuario o el cargo dependiendo del estado de la etiqueta de la petición que pasamos como par&aacute;metro,
	 * @param user el usuario
	 * @param job el cargo
	 * @param signedJob cargo firmado, false indica que no y true que si
	 * @param request la petición
	 * @return El usuario buscado.
	 */
	private PfUsersDTO userDTOFromUserOrJob(PfUsersDTO user, PfUsersDTO job,
			boolean signedJob, PfRequestTagsDTO requestTag) {
		PfUsersDTO userDTO = null;
		
		if(user.getCtype().equals(Constants.C_TYPE_USER_EXTERNAL)) {
			return user;
		}
		
		PfRequestTagsDTO stateJob = requestTag.getStateJob();
		PfRequestTagsDTO stateUser = requestTag.getStateUser();
		//Si el estado del usuario no es nuevo ni leido se pone a nulo
		if (stateUser != null
				&& !stateUser.getPfTag().getCtag().equals(Constants.C_TAG_NEW)
				&& !stateUser.getPfTag().getCtag().equals(Constants.C_TAG_READ)
				&& !stateUser.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING_PASSED)) {
			stateUser = null;
		//Si el estado del cargo no es nuevo ni leido se pone a nulo
		} else if (stateJob != null
				&& !stateJob.getPfTag().getCtag().equals(Constants.C_TAG_NEW)
				&& !stateJob.getPfTag().getCtag().equals(Constants.C_TAG_READ)
				&& !stateJob.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING_PASSED)){
			stateJob = null;
		}

		if (stateUser != null && stateJob != null && !signedJob) {
			userDTO = user;
		} else if (stateUser != null && stateJob != null && signedJob) {
			userDTO = job;
		} else if (stateUser != null) {
			userDTO = user;
		} else if (stateJob != null) {
			userDTO = job;
		}
		log.info("lastSignerUnresolvedDocument init");
		return userDTO;
	}

	/**
	 * M&eacute;todo que comprueba si alg&uacute;n firmante de una petición ha realizado la firma.
	 * @param sli Lista con las l&iacute;neas de firma de una petición (relación entre un firmante y una petición)
	 * @return True si hay alg&uacute;n firmante que ha realizado la firma.
	 */
	public boolean isSignLineSigned(PfSignLinesDTO sli) {
		boolean result = false;

		Map<String, Object> parameters = new HashMap<String, Object>();
		for (PfSignersDTO signer : sli.getPfSigners()) {
			parameters.put("signer", signer);
			Long signsNumber = baseDAO.queryCount("request.signerWithSigns", parameters);
			if (signsNumber >= sli.getPfRequest().getPfDocumentsList().size()) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * M&eacute;todo que busca un usuario dentro de una linea de firma.
	 * @param sli L&iacute;nea de firma.
	 * @param user Usuario a buscar.
	 * @return El firmante buscado en la l&iacute;nea de firma.
	 */
	public PfSignersDTO signerFromSignLine(PfSignLinesDTO sli, PfUsersDTO user) {
		PfSignersDTO result = null;

		if (sli != null) {
			for (PfSignersDTO signer : sli.getPfSigners()) {
				if (signer.getPfUser().getPrimaryKey().compareTo(
						user.getPrimaryKey()) == 0) {
					result = signer;
				}
			}
		}

		return result;
	}

	/**
	 * Recupera un listado de lineas de firma relacionadas con la petición,
	 * las recuperarlas en cascada si la petición es de tipo'en cascada'
	 * @param req La petición.
	 * @return Listado de l&iacute;neas de firma.
	 */
	public List<AbstractBaseDTO> querySignLineList(PfRequestsDTO req) {
		List<AbstractBaseDTO> result = null;
		//si la firma es en cascada, obtiene las lineas de firma de la petición
		//ordenadas por su clave primaria
		if (req.getLcascadeSign()) {
			result = baseDAO.queryListOneParameter(
					"request.requestSignLinesCascade", "req", req);
		// si no, obtiene las lineas de firma de la petición
		//ordenadas por la clave primaria de las firmas y por su clave primaria
		} else {
			result = baseDAO.queryListOneParameter(
					"request.requestSignLinesNotCascade", "req", req);
		}
		return result;
	}

	/**
	 * M&eacute;todo que recupera todas las l&iacute;neas de firma de un usuario para una determinada petición.
	 * @param req Petición a buscar.
	 * @param identifier Identificador del usuario a buscar.
	 * @return L&iacute;neas de firma del usuario para dicha petición.
	 */
	public List<AbstractBaseDTO> querySignLineListByUser(PfRequestsDTO req,
			String identifier) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", req);
		parameters.put("identifier", identifier.toUpperCase());
		return baseDAO.queryListMoreParameters(
				"request.requestSignLinesByUser", parameters);
	}

	/**
	 * M&eacute;todo que recupera todas las l&iacute;neas de firma de un usuario para una determinada petición.
	 * @param req Petición a buscar.
	 * @param identifier Identificador del usuario a buscar.
	 * @return L&iacute;neas de firma del usuario para dicha petición.
	 */
	public List<AbstractBaseDTO> queryFetchSignLineListByUser(PfRequestsDTO req, String identifier) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", req);
		parameters.put("identifier", identifier.toUpperCase());
		return baseDAO.queryListMoreParameters("request.requestFetchSignLinesByUser", parameters);
	}
	
	/**
	 * Método que consulta la etiqueta-peticion por primary key
	 * @param pk Primary Key de la etiqueta-peticion
	 * @return
	 */
	public PfRequestTagsDTO queryRequestTagByPk (long pk) {
		return (PfRequestTagsDTO) baseDAO.queryElementOneParameter("request.requestTagByPk", "pk", pk);
	}
	

	/**
	 * M&eacute;todo que cambia el estado de una petición a "FIRMADA".
	 * @param requestTag Etiqueta Petición firmada.
	 * @param user Usuario que firma.
	 * @param job Cargo que firma.
	 */
	public void changeStateToSigned(PfRequestTagsDTO requestTag, PfUsersDTO user, boolean job) {
		PfUsersDTO userDTO = userDTOFromUserOrJob(user, user.getValidJob(), job, requestTag);

		PfTagsDTO tagSigned = applicationVO.getStateTags().get(Constants.C_TAG_SIGNED);
		PfTagsDTO tagNextState = tagSigned;
		boolean oldRequest = false;
		PfSignLinesDTO sliSigned = requestTag.getPfSignLine();
		if (sliSigned == null) {
			oldRequest = true;
			sliSigned = lastSignLineSigned(requestTag.getPfRequest());
		}

		PfRequestsDTO request = requestTag.getPfRequest();
		if (oldRequest) {
			tagNextState = applicationVO.getStateTags().get(nextStateUser(requestTag, userDTO));
		}
		changeRequestTagUser(request, requestTag, sliSigned, tagNextState, userDTO);

		// Se cambia el estado de los usuarios que están en la misma línea de firma que el que firma
		for (PfSignersDTO signer : sliSigned.getPfSigners()) {
			if (!signer.getPfUser().getPrimaryKeyString().equals(userDTO.getPrimaryKeyString())) {
				changeRequestTagParallelUser(request, sliSigned, tagNextState, signer.getPfUser());
			}
		}

		// change following signer
		if (request.getLcascadeSign()) {
			changeStateNextSigner(request);
		} else if (request.getLfirstSignerSign()) {
			changeStateFirstSigner(request, sliSigned);
		} 
//		else {
//			changeStateParalellSigner(request, sliSigned);
//		}

		// save historic request
		historicRequestBO.saveHistoricRequest(request, user,
				Constants.C_HISTORIC_REQUEST_SIGNED, userDTO.isJob());

		// List with the request to work with notice and action api
		List<AbstractBaseDTO> auxRequestList = new ArrayList<AbstractBaseDTO>();
		auxRequestList.add(request);

		// LAS ACCIONES NO SE SOPORTAN
		// execute actions
		//actionBO.executeActions(auxRequestList, user.getCidentifier(),
			//	tagSigned.getCtag());

		// execute notices
		// check if request should be noticed when state is read
		if (noticeBO.noticeWhenState(request, tagSigned.getCtag())) {
			noticeBO.noticeStateChange(request, applicationVO.getEmail(),
					applicationVO.getSMS());
		}
		
		// Se notifica a portafirmas externo si procede
		PfDocelwebRequestSpfirmaDTO pfDocelRequestToNotify = interfazGenericaServerBO.changeDocelRequestStatus(requestTag.getPfRequest(), DocelwebConstants.REQUEST_STATE_SIGNED);
		if (pfDocelRequestToNotify != null) {
			log.debug("Notificando que la solicitud con ID de transacción [" + pfDocelRequestToNotify.getPrimaryKeyString() + "] ha sido firmada al sistema de gestión [" + pfDocelRequestToNotify.getPortafirmas().getNombre() + "]");
			try {
				notificacionGenericaClientBO.notificarDevolucionSolicitud(pfDocelRequestToNotify, null);
				//notificacionGenericaClientBO.notificarDevolucionSolicitud(null, pfDocelRequestToNotify);
			} catch (NotificacionGenericaClientException e) {
				log.error("No se ha podido notificar la firma de la solicitud DOCEL al Sistema de Gestión", e);
			}
		}

		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);

	}

	private void changeStateToSignedExternal(PfRequestTagsDTO requestTag) {
		PfTagsDTO tagSigned = applicationVO.getStateTags().get(Constants.C_TAG_SIGNED);
		requestTag.setPfTag(tagSigned);
		baseDAO.insertOrUpdate(requestTag);

		PfRequestsDTO request = requestTag.getPfRequest();
		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);

		if (request.getLcascadeSign()) {
			changeStateNextSigner(request);
		}
		
		if (noticeBO.noticeWhenState(request, tagSigned.getCtag())) {
			noticeBO.noticeStateChange(request, applicationVO.getEmail(), applicationVO.getSMS());
		}		
	}

	/**
	 * Método que cambia el estado de una petición a "RETIRADA".
	 * @param request Petición
	 * @param user Usuario
	 * @param job Cargo
	 */
	public void changeStateToRemoved(PfRequestTagsDTO requestTag, String text, PfUsersDTO user, boolean job) {
		log.debug("Checking user or job");
		PfRequestsDTO request = requestTag.getPfRequest();
		PfUsersDTO userDTO = userDTOFromUserOrJob(user, user.getValidJob(),	false, requestTag);

		PfTagsDTO tagRemoved = applicationVO.getStateTags().get(Constants.C_TAG_REMOVED);

		// Se obtienen las etiquetas de la petición y sus datos
		List<AbstractBaseDTO> etiquetasPeticion = 
			(List<AbstractBaseDTO>) baseDAO.queryListOneParameter("request.allRequestTags", "request", request);

		for (AbstractBaseDTO reqTag : etiquetasPeticion) {
			PfRequestTagsDTO etiquetaPeticion = (PfRequestTagsDTO) reqTag;
			if (etiquetaPeticion.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE)) {
				etiquetaPeticion.setPfTag(tagRemoved);
				baseDAO.insertOrUpdate(etiquetaPeticion);
			}
		}

		// save historic request
		boolean isJob = false;
		if (userDTO != null && userDTO.isJob()) {
			isJob = true;
		}
		historicRequestBO.saveHistoricRequest(request, user, Constants.C_HISTORIC_REQUEST_REMOVED, isJob);

		log.debug("Save comment from text remove");
		
		PfCommentsDTO comment = requestBO.saveComment(text, user, request);
		
		// List with the request to work with notice and action api
		List<AbstractBaseDTO> auxRequestList = new ArrayList<AbstractBaseDTO>();
		auxRequestList.add(request);

		// execute actions
		// Las acciones no se usan
		//actionBO.executeActions(auxRequestList, user.getCidentifier(), tagRemoved.getCtag());
		
		request.setPfComments(new HashSet<PfCommentsDTO>());
		request.getPfComments().add(comment);

		// execute notices
		try { //Aunque falle la notificación se debe retirar la petición
			noticeBO.noticeStateChange(request, applicationVO.getEmail(), applicationVO.getSMS());
		} catch (Exception e) {
			log.error("Error en la notificacion", e);
		}

		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);
		
	}

	/**
	 * Método que cambia el estado de una petición a "RETIRADA".
	 * @param request Petición
	 * @param user Usuario
	 * @param job Cargo
	 */
	public void changeStateToRemovedRequest(PfRequestsDTO request, String text, PfUsersDTO user, boolean job) {
		log.debug("Checking user or job");

		PfTagsDTO tagRemoved = applicationVO.getStateTags().get(Constants.C_TAG_REMOVED);

		// Se obtienen las etiquetas de la petición y sus datos
		List<AbstractBaseDTO> etiquetasPeticion = 
			(List<AbstractBaseDTO>) baseDAO.queryListOneParameter("request.allRequestTags", "request", request);

		for (AbstractBaseDTO reqTag : etiquetasPeticion) {
			PfRequestTagsDTO etiquetaPeticion = (PfRequestTagsDTO) reqTag;
			if (etiquetaPeticion.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE)) {
				etiquetaPeticion.setPfTag(tagRemoved);
				baseDAO.insertOrUpdate(etiquetaPeticion);
			}
		}

		// save historic request
		historicRequestBO.saveHistoricRequest(request, user, Constants.C_HISTORIC_REQUEST_REMOVED, job);

		log.debug("Save comment from text remove");
		PfCommentsDTO comment = requestBO.saveComment(text, user, request);

		// List with the request to work with notice and action api
		List<AbstractBaseDTO> auxRequestList = new ArrayList<AbstractBaseDTO>();
		auxRequestList.add(request);

		// execute actions
		// Las acciones no se usan
		//actionBO.executeActions(auxRequestList, user.getCidentifier(), tagRemoved.getCtag());
		
		request.setPfComments(new HashSet<PfCommentsDTO>());
		request.getPfComments().add(comment);

		// execute notices
		noticeBO.noticeStateChange(request, applicationVO.getEmail(), applicationVO.getSMS());

		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);

	}

	/**
	 * Método que cambia el estado de una petición a "RETIRADA".
	 * @param request Petición
	 * @param user Usuario
	 * @param job Cargo
	 */
	public void changeStateToExpired(PfRequestsDTO request) {
		PfTagsDTO tagExpired = applicationVO.getStateTags().get(Constants.C_TAG_EXPIRED);

		// Se obtienen las etiquetas de la petición y sus datos
		List<AbstractBaseDTO> etiquetasPeticion = 
			(List<AbstractBaseDTO>) baseDAO.queryListOneParameter("request.allRequestTags", "request", request);

		for (AbstractBaseDTO requestTag : etiquetasPeticion) {
			PfRequestTagsDTO etiquetaPeticion = (PfRequestTagsDTO) requestTag;
			if (etiquetaPeticion.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE)) {
				etiquetaPeticion.setPfTag(tagExpired);
				baseDAO.insertOrUpdate(etiquetaPeticion);
			}
		}

		// execute notices
		noticeBO.noticeRequestExpired(request, applicationVO.getEmail(), applicationVO.getSMS());		

		request.setFmodified(Calendar.getInstance().getTime());
		
		// Enviamos la notificación a las aplicaciones externas.
		
		List<AbstractBaseDTO> requests = new ArrayList<AbstractBaseDTO> ();
		requests.add(request);
		try {
			noticeBO.sendAdviceToAppServer(requests);
		} catch (NoticeException ne) {
			log.error("No se ha podido enviar la notificación a la aplicación externa", ne);
		}
		baseDAO.update(request);
	}
	

//	public void changeStateToAnulled(PfRequestsDTO request, boolean anulled) {
//		PfTagsDTO tagAnulled= applicationVO.getStateTags().get(Constants.C_TAG_ANULLED);
//		PfTagsDTO tagSigned = applicationVO.getStateTags().get(Constants.C_TAG_SIGNED);
//
//		// Se obtienen las etiquetas de la petición y sus datos
//		List<AbstractBaseDTO> etiquetasPeticion = 
//			(List<AbstractBaseDTO>) baseDAO.queryListOneParameter("request.allRequestTags", "request", request);
//
//		for (AbstractBaseDTO requestTag : etiquetasPeticion) {
//			PfRequestTagsDTO etiquetaPeticion = (PfRequestTagsDTO) requestTag;
//			if (etiquetaPeticion.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE)) {
//				//Se anula
//				if(anulled){
//					etiquetaPeticion.setPfTag(tagAnulled);
//				}
//				//Se recupera la firma
//				else{
//					etiquetaPeticion.setPfTag(tagSigned);
//				}
//					
//				baseDAO.insertOrUpdate(etiquetaPeticion);
//			}
//		}
//
//		// execute notices (anulled)
//		noticeBO.noticeRequestAnulled(request, applicationVO.getEmail(), applicationVO.getSMS(), anulled);		
//		
//		request.setFmodified(Calendar.getInstance().getTime());
//		
//		// Enviamos la notificación a las aplicaciones externas.
//		List<AbstractBaseDTO> requests = new ArrayList<AbstractBaseDTO> ();
//		requests.add(request);
//		try {
//			noticeBO.sendAdviceToAppServer(requests);
//		} catch (NoticeException ne) {
//			log.error("No se ha podido enviar la notificación a la aplicación externa", ne);
//		}
//		baseDAO.update(request);
//	}

	/**
	 * M&eacute;todo que cambio el estado de una lista de peticiones a "VISTO BUENO".
	 * @param requestList Listado de peticiones.
	 * @param user Usuario que da el visto bueno.
	 * @param job Cargo que da el visto bueno.
	 * @return False.
	 */
	public boolean changeStateToPassedList(List<AbstractBaseDTO> requestList,
			PfUsersDTO user, boolean job) {
		boolean change = false;
		for (AbstractBaseDTO request : requestList) {
			changeStateToPassed((PfRequestTagsDTO) request, user, job);
		}

		return change;
	}

	/**
	 * M&eacute;todo que cambia la etiqueta de una petición a "VISTO BUENO".
	 * @param request Petición que cambia de estado.
	 * @param user Usuario que da el visto bueno.
	 * @param job Cargo que da el visto bueno.
	 */
	public void changeStateToPassed(PfRequestTagsDTO requestTag, PfUsersDTO user,	boolean job) {


		PfUsersDTO userDTO = userDTOFromUserOrJob(user, user.getValidJob(), job, requestTag);

		PfTagsDTO tagPassed = applicationVO.getStateTags().get(Constants.C_TAG_PASSED);
		PfTagsDTO tagNextState = tagPassed;
		boolean oldRequest = false;
		PfSignLinesDTO sliPassed = requestTag.getPfSignLine();
		if (sliPassed == null) {
			oldRequest = true;
			sliPassed = lastSignLineSigned(requestTag.getPfRequest());
		}

		PfRequestsDTO request = requestTag.getPfRequest();
		if (oldRequest) {
			tagNextState = applicationVO.getStateTags().get(nextStateUserPass(requestTag, userDTO));
		}
		changeRequestTagUser(request, requestTag, sliPassed, tagNextState, userDTO);

		// Se cambia el estado de los usuarios que están en la misma línea de firma que el que da el visto bueno
		for (PfSignersDTO signer : sliPassed.getPfSigners()) {
			if (!signer.getPfUser().getPrimaryKeyString().equals(userDTO.getPrimaryKeyString())) {
				changeRequestTagParallelUser(request, sliPassed, tagNextState, signer.getPfUser());
			}
		}

		// change following signer
		if (request.getLcascadeSign()) {
			changeStateNextSigner(request);
		}
		//		else if (!request.getLfirstSignerSign()) {
		//			changeStateParalellSigner(request, sliPassed);
		//		}

		// save historic request
		historicRequestBO.saveHistoricRequest(request, user, Constants.C_HISTORIC_REQUEST_PASSED, userDTO.isJob());

		// List with the request to work with notice and action api
		/*List<AbstractBaseDTO> auxRequestList = new ArrayList<AbstractBaseDTO>();
		auxRequestList.add(request); // execute actions
		actionBO.executeActions(auxRequestList, user.getCidentifier(), tagPassed.getCtag());*/
		// execute notices
		// check if request should be noticed when state is read
		if (noticeBO.noticeWhenState(request, tagPassed.getCtag())) {
			noticeBO.noticeStateChange(request, applicationVO.getEmail(), applicationVO.getSMS());
		}

		request.setFmodified(Calendar.getInstance().getTime());
		baseDAO.update(request);
		

	}

	/**
	 * M&eacute;todo que cambia la etiqueta de una petición para el siguiente firmante.
	 * Cuando ha firmado el anterior firmante m&aacute;s prioritario el siguiente firmante pasa a ver
	 * la petición como "NUEVA" en vez de que como "EN ESPERA".
	 * @param request La petición.
	 */
	public void changeStateNextSigner(PfRequestsDTO request) {
		PfSignLinesDTO sli = lastSignLineNotSigned(request);
		if (sli != null) {
			PfTagsDTO tagNew = applicationVO.getStateTags().get(Constants.C_TAG_NEW);

			PfRequestTagsDTO stateTag = null;
			for (PfSignersDTO signer : sli.getPfSigners()) {
				boolean esUsuarioExterno = signer.getPfUser().getCtype().equals(Constants.C_TYPE_USER_EXTERNAL);
				if(!esUsuarioExterno) {
					//Agustin #1406 enviar aviso también en la firmas intermedias
					noticeBO.noticeNewRequest(request, applicationVO.getEmail(), applicationVO.getSMS());
					
					stateTag = queryStateUserSignLine(request, signer.getPfUser(), sli);
					stateTag.setPfTag(tagNew);
					baseDAO.update(stateTag);
					addOrRemovePassTag(request, sli, signer.getPfUser());
				}
			}
		}
	}
	
	/**
	 * Para todos los usuarios de la linea firmada, buscamos la próxima linea sin firmar, y añadimos/quitamos
	 * la etiqueta de TIPO.VISTOBUENO seg&uacute;n correponda
	 * @param request Petición.
	 * @param sliSigned L&iacute;nea de firma.
	 */
	public void changeStateParalellSigner(PfRequestsDTO request, PfSignLinesDTO sliSigned) {	
		
		PfTagsDTO tagNew = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
		PfRequestTagsDTO stateTag = null;
		
		for (PfSignersDTO signer: sliSigned.getPfSigners()) {			
			PfSignLinesDTO sli = lastSignLineNotSignedOfUser(request, signer.getPfUser());
			if (sli != null) {
				stateTag = queryStateUserSignLine(request, signer.getPfUser(), sliSigned);
				stateTag.setPfTag(tagNew);
				baseDAO.update(stateTag);				
				addOrRemovePassTag(request, sli, signer.getPfUser());
			}			
		}
	}	

	/**
	 * M&eacute;todo que cambia el valor de la etiqueta del primer firmante
	 * de las l&iacute;neas de firma de una petición.
	 * @param request Petición.
	 * @param sliSigned L&iacute;neas de firma.
	 */
	public void changeStateFirstSigner(PfRequestsDTO request, PfSignLinesDTO sliSigned) {		
		PfTagsDTO tagNew = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
		PfRequestTagsDTO stateTag = null;
		
		for (PfSignersDTO signer: sliSigned.getPfSigners()) {
			stateTag = queryStateUserSignLine(request, signer.getPfUser(), sliSigned);		
			String stateStr = (stateTag == null) ? Constants.C_TAG_NEW : stateTag.getPfTag().getCtag();
			if (stateTag != null && !Constants.C_TAG_SIGNED.equals(stateStr) &&
				!Constants.C_TAG_PASSED.equals(stateStr) && !Constants.C_TAG_REJECTED.equals(stateStr)	) {

				PfSignLinesDTO sli = lastSignLineNotSignedOfUser(request, signer.getPfUser());
				if (sli != null) {				
					stateTag.setPfTag(tagNew);
					baseDAO.update(stateTag);				
					addOrRemovePassTag(request, sli, signer.getPfUser());
				}
			}
		}
	}		

	/**
	 * Método que añade o elimina la etiqueta de VISTO BUENO a una línea de firma de una petición de usuario.
	 * Si la l&iacute;nea de firma es de tipo firma y la petición est&aacute; etiquetada como VISTO BUENO la elimina.
	 * Si la l&iacute;nea de firma es de tipo visto bueno y la petición est&aacute; etiquetada como VISTO BUENO la añade.
	 * @param request Petición.
	 * @param sli L&iacute;nea de firma.
	 * @param user Usuario.
	 */
	private void addOrRemovePassTag(PfRequestsDTO request, PfSignLinesDTO sli, PfUsersDTO user) {
		if (Constants.C_TYPE_SIGNLINE_SIGN.equals(sli.getCtype())) {
			PfRequestTagsDTO passTag = queryPassUserSignLine(request, user, sli);
			if (passTag != null) {
				deleteUserTagRequest(passTag);
			}
		} else if (Constants.C_TYPE_SIGNLINE_PASS.equals(sli.getCtype())) {
			PfRequestTagsDTO passTag = queryPassUserSignLine(request, user, sli);
			//En la creacion de invitaciones no existe el usuario hasta que la invitación se acepta
			if (passTag == null) {
				addUserTagRequest(applicationVO.getSystemTags().get(Constants.C_TAG_SYSTEM_PASSED), request, user, sli, null);
			}
		}
	}
	

	/**
	 * Recupera la etiqueta de petición de tipo 'ESTADO' para la petición 
	 * y el usuario pasados como par&aacute;metro
	 * @param request petición
	 * @param user usuario
	 * @return etiqueta de petición de tipo 'ESTADO'
	 */
	public PfRequestTagsDTO queryStateUser(PfRequestsDTO request,
			PfUsersDTO user) {
		PfRequestTagsDTO stateTag = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", request);
		parameters.put("user", user);
		stateTag = (PfRequestTagsDTO) baseDAO.queryElementMoreParameters(
				"request.stateTagRequestUser", parameters);
		return stateTag;
	}

	/**
	 * Recupera la etiqueta de petición de tipo 'ESTADO' para la petición 
	 * y el usuario pasados como par&aacute;metro
	 * @param request petición
	 * @param user usuario
	 * @param signLine línea de firma
	 * @return etiqueta de petición de tipo 'ESTADO'
	 */
	public PfRequestTagsDTO queryStateUserSignLine(PfRequestsDTO request,
			PfUsersDTO user, PfSignLinesDTO signLine) {
		PfRequestTagsDTO stateTag = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", request);
		if(user != null){
			parameters.put("user", user);
		}
		parameters.put("signLine", signLine);
		if(user != null){
			stateTag = (PfRequestTagsDTO) baseDAO.queryElementMoreParameters(
				"request.stateTagRequestUserSignLine", parameters);
		}else{
			stateTag = (PfRequestTagsDTO) baseDAO.queryElementMoreParameters(
					"request.stateTagRequestUserSignLineInvited", parameters);
		}
		return stateTag;
	}

	/**
	 * M&eacute;todo que devuelve la relación entre una petición y un usuario con la etiqueta VISTO BUENO.
	 * @param request Petición
	 * @param user Usuario.
	 * @return Etiqeuta de la petición con VISTO BUENO.
	 */
	public PfRequestTagsDTO queryPassUser(PfRequestsDTO request, PfUsersDTO user) {
		PfRequestTagsDTO stateTag = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", request);
		parameters.put("user", user);
		stateTag = (PfRequestTagsDTO) baseDAO.queryElementMoreParameters(
				"request.passTagRequestUser", parameters);
		return stateTag;
	}

	/**
	 * M&eacute;todo que devuelve la relación entre una petición y un usuario con la etiqueta VISTO BUENO.
	 * @param request Petición
	 * @param user Usuario.
	 * @return Etiqeuta de la petición con VISTO BUENO.
	 */
	public PfRequestTagsDTO queryPassUserSignLine(PfRequestsDTO request, PfUsersDTO user,
			PfSignLinesDTO signLine) {
		PfRequestTagsDTO stateTag = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", request);
		parameters.put("user", user);
		parameters.put("signLine", signLine);
		stateTag = (PfRequestTagsDTO) baseDAO.queryElementMoreParameters(
				"request.passTagRequestUserSignLine", parameters);
		return stateTag;
	}

	/**
	 * M&eacute;todo que actualiza el valor de una etiqueta de una petición de un usuario.
	 * @param request Petición.
	 * @param sli L&iacute;nea de firma.
	 * @param tag Etiqueta a poner.
	 * @param user Usuario que etiqueta.
	 */
	public void changeRequestTagUser(PfRequestsDTO request, PfRequestTagsDTO requestTag,
									 PfSignLinesDTO sli, PfTagsDTO tag, PfUsersDTO user) {
		if (requestTag == null) {
			requestTag = new PfRequestTagsDTO();
			requestTag.setPfRequest(request);
			if(user != null){
				requestTag.setPfUser(user);
			}
			requestTag.setChash(Util.getInstance().createHash());
		}

		requestTag.setPfSignLine(sli);
		requestTag.setPfTag(tag);
		if (requestTag.getChash() == null || "".equals(requestTag.getChash())) {
			requestTag.setChash(Util.getInstance().createHash());
		}
		baseDAO.insertOrUpdate(requestTag);

		addOrRemovePassTag(request, sli, user);
	}

	/**
	 * Método que cambia el estado de un usuario paralelo
	 * @param request petición
	 * @param sli Línea de firma
	 * @param tag Etiqueta
	 * @param user Usuario
	 */
	public void changeRequestTagParallelUser(PfRequestsDTO request, PfSignLinesDTO sli,
			 								 PfTagsDTO tag, PfUsersDTO user) {
		PfRequestTagsDTO requestTag = queryStateUserSignLine(request, user, sli);
		requestTag.setPfSignLine(sli);
		requestTag.setPfTag(tag);
		baseDAO.insertOrUpdate(requestTag);
		
		addOrRemovePassTag(request, sli, user);
	}

//	/**
//	 * M&eacute;todo que actualiza el valor de una etiqueta de una petición de un usuario.
//	 * @param request Petición.
//	 * @param sli L&iacute;nea de firma.
//	 * @param tag Etiqueta a poner.
//	 * @param user Usuario que etiqueta.
//	 */
//	public void changeRequestTagParallelUser(PfRequestsDTO request, PfSignLinesDTO sli,
//									 PfTagsDTO tag, PfUsersDTO user) {
//		PfRequestTagsDTO requestTag = queryStateUserSignLine(request, user, sli);
//		if (requestTag == null) {
//			requestTag = queryStateUser(request, user);
//		}
//
//		if (requestTag == null) {
//			requestTag = new PfRequestTagsDTO();
//			requestTag.setPfRequest(request);
//			requestTag.setPfUser(user);
//		}
//
//		requestTag.setPfSignLine(sli);
//		requestTag.setPfTag(tag);
//		baseDAO.insertOrUpdate(requestTag);
//
//		addOrRemovePassTag(request, sli, user);
//	}

	/**
	 * M&eacute;todo que añade un usuario autorizado a una línea de firma
	 * @param request Petición.
	 * @param sli Línea de firma.
	 * @param tag Etiqueta a poner.
	 * @param user Usuario que etiqueta.
	 */
	public void changeRequestTagUserAut(PfRequestsDTO request, PfSignLinesDTO sli,
									 PfTagsDTO tag, PfUsersDTO user) {
		PfRequestTagsDTO requestTag = new PfRequestTagsDTO();
		requestTag.setPfRequest(request);
		requestTag.setPfUser(user);
		requestTag.setPfSignLine(sli);
		requestTag.setPfTag(tag);
		requestTag.setChash(Util.getInstance().createHash());
		baseDAO.insertOrUpdate(requestTag);

		addOrRemovePassTag(request, sli, user);
	}

	/**
	 * M&eacute;todo que actualiza el valor de una etiqueta de una petición de un usuario desde WS.
	 * @param request Petición.
	 * @param sli Línea de firma.
	 * @param tag Etiqueta a poner.
	 * @param user Usuario que etiqueta.
	 */
	public void changeRequestTagUserWS(PfRequestsDTO request, PfSignLinesDTO sli,
									 PfTagsDTO tag, PfUsersDTO user) {
		PfRequestTagsDTO requestTag = queryStateUserSignLine(request, user, sli);
		if (requestTag == null) {
			requestTag = new PfRequestTagsDTO();
			requestTag.setPfRequest(request);
			requestTag.setPfUser(user);
			requestTag.setChash(Util.getInstance().createHash());
		}

		requestTag.setPfSignLine(sli);
		requestTag.setPfTag(tag);
		baseDAO.insertOrUpdate(requestTag);

		addOrRemovePassTag(request, sli, user);
	}
	
	
	//public Boolean changeRequestToValidated (PfRequestTagsDTO reqTag) {
	@Transactional(readOnly = false)
	public Boolean changeRequestToValidated (PfRequestTagsDTO reqTag, PfUsersDTO user) {
		
		// Se obtiene la etiqueta
		PfTagsDTO tagDTO = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING_PASSED);
		
		reqTag.setPfTag(tagDTO);
		reqTag.setPfUsuarioValidador(user);
		baseDAO.insertOrUpdate(reqTag);
		
		try {
			noticeBO.noticeValidacion(reqTag, applicationVO.getEmail(), applicationVO.getSMS());;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean changed = true;		
		
		return changed;
	}
	
	
	/**
	 * M&eacute;todo que actualiza el valor de una etiqueta de una petición de un usuario,
	 * si la etiqueta no existe no hace nada.
	 * @param request Petición.
	 * @param tag Etiqueta a poner.
	 * @param user Usuario que etiqueta.
	 * @return true si se ha actualizado la etiqueta, false en caso contrario.
	 */
/*	public Boolean changeRequestTagUser(PfRequestsDTO request, String tag, PfUsersDTO user) {

		Boolean resultado = Boolean.FALSE;

		// Se obtiene la etiqueta
		PfTagsDTO tagDTO = null;
		if (tag != null) {
			tagDTO = stateTags.get(tag);
		}

		Set<PfSignLinesDTO> signLines = request.getPfSignsLines();
		for (PfSignLinesDTO signLine : signLines) {
			// Se validan todas las líneas de firma del usuario para esta petición
			if (Constants.C_TAG_AWAITING_PASSED.equals(tagDTO.getCtag())) {
				PfRequestTagsDTO requestTag = queryStateUserSignLine(request, user, signLine);
				
				if (requestTag != null && tagDTO != null) {
					requestTag.setPfTag(tagDTO);
					baseDAO.insertOrUpdate(requestTag);

					// TODO Revisar esto. Está quitado porque no se podían validar peticiones dirigidas a cargos.
					// Para solucionarlo se modificó esta clase y se adjuntó al WAR que había en el servidor de Producción el día 10 de Agosto de 2013
					// save historic request			
					//historicRequestBO.saveHistoricRequest(request, user,
						//	Constants.C_HISTORIC_REQUEST_VALIDATED, user.isJob());

					resultado = Boolean.TRUE;
				}
			}

			// Se da el visto bueno para la primera línea de firma del usuario para esta petición
			if (Constants.C_TAG_PASSED.equals(tagDTO.getCtag())) {
				PfRequestTagsDTO isPass = queryPassUserSignLine(request, user, signLine);
				PfRequestTagsDTO requestTag = queryStateUserSignLine(request, user, signLine);
				
				if (isPass != null && requestTag != null && tagDTO != null &&
					(tagDTO.getCtag().equals(Constants.C_TAG_NEW)  ||
					 tagDTO.getCtag().equals(Constants.C_TAG_READ) ||
					 tagDTO.getCtag().equals(Constants.C_TAG_AWAITING_PASSED))) {
					requestTag.setPfTag(tagDTO);
					baseDAO.insertOrUpdate(requestTag);

					// TODO Revisar esto. Está quitado porque no se podían validar peticiones dirigidas a cargos.
					// Para solucionarlo se modificó esta clase y se adjuntó al WAR que había en el servidor de Producción el día 10 de Agosto de 2013
					// save historic request			
					//historicRequestBO.saveHistoricRequest(request, user,
						//	Constants.C_HISTORIC_REQUEST_VALIDATED, user.isJob());

					resultado = Boolean.TRUE;
					break;
				}
			}
		}

		
		return resultado;
	}*/

	/*
	 * public void addPassTagUser(PfRequestsDTO request, PfTagsDTO tag,
	 * PfUsersDTO user) { PfRequestTagsDTO requestTag = new PfRequestTagsDTO();
	 * requestTag.setPfRequest(request); requestTag.setPfUser(user);
	 * requestTag.setPfTag(tag); baseDAO.insertOrUpdate(requestTag); }
	 */
	
	/**
	 * Cambia en bbdd la etiqueta de petición de una petición por la etiqueta que 
	 * pasamos como par&aacute;metro, si la etiqueta es un job, lo que se indica con el par&aacute;metro
	 * job,se cambiar&aacute; la etiqueta de petición de tipo job, en caso contrario se cambiar&aacute;
	 * una de tipo usuario
	 * @param request la petición de la que extraeremos la etiqueta de petición
	 * @param tag la etiqueta que pondremos a la etiqueta de petición en caso de 
	 * que la cambiemos
	 * @param job indica si la etiqueta de petición es de un job o no
	 * @return indica si se ha cambiado la etiqueta de la petición o no,
	 * true es si y false no
	 * @see es.seap.minhap.portafirmas.domain.PfRequestsDTO#getStateJob()
	 * @see es.seap.minhap.portafirmas.domain.PfRequestsDTO#getStateUser()
	 * @see es.seap.minhap.portafirmas.domain.PfRequestTagsDTO#setPfTag(PfTagsDTO)
	 * @see es.seap.minhap.portafirmas.dao.BaseDAO#update(AbstractBaseDTO)
	 * @see #translateState(PfRequestTagsDTO)
	 */
	private boolean changeRequestTagAuthUser(PfRequestTagsDTO requestTag,
			PfTagsDTO tag, boolean job) {
		boolean change = false;
		PfRequestTagsDTO newRequestTag = null;
		//si es un job recuperamos la etiqueta de job de la petición
		if (job) {
			newRequestTag = requestTag.getStateJob();
		//si no recuperamos la etiqueta de usuario
		} else {
			newRequestTag = requestTag.getStateUser();
		}
		//ponemos la etiqueta de petición a la etiqueta que hemos pasado como par&aacute;metro
		newRequestTag.setPfTag(tag);
		//actualizamos la etiqueta de petición en bbdd
		baseDAO.update(newRequestTag);
		//traslada los mensajes
		translateState(newRequestTag);
		//ponemos que ha cambiado a true
		change = true;
		return change;
	}

	/**
	 * M&eacute;todo que cambia el estado de todas las peticiones de una lista a "FIRMADAS".
	 * @param requestTagList Listado de peticiones.
	 * @param user Usuario que firma.
	 * @param job Cargo que firma.
	 * @return False.
	 */
	public boolean changeStateToSignedList(List<AbstractBaseDTO> requestTagList,
			PfUsersDTO user, boolean job) {
		boolean change = false;
		for (AbstractBaseDTO requestTag : requestTagList) {
			changeStateToSigned((PfRequestTagsDTO) requestTag, user, job);
		}
		return change;
	}

	public boolean changeStateToSignedList(List<AbstractBaseDTO> requestTagList, PfDocelwebRequestSmanagerDTO docelRequest) {
		boolean change = false;
		for (AbstractBaseDTO requestTag : requestTagList) {
			changeStateToSignedExternal((PfRequestTagsDTO) requestTag);
		}
		docelRequest.setdState(DocelwebConstants.REQUEST_STATE_SIGNED);
		baseDAO.insertOrUpdate(docelRequest);
		return change;
	}
	
	/**
	 * M&eacute;todo que marca una petición como le&iacute;da
	 * @param request Petición que se marca como le&iacute;da.
	 * @param user usuario Usuario que marca la petición.
	 * @return Devuelve true si ha cambiado el estado de la petición.
	 */
	@Transactional(readOnly = false)
	public boolean changeStateToRead(PfRequestTagsDTO requestTag, PfUsersDTO user) {

		boolean change = false;
		boolean isJob = false;

		//Obtenemos el tag de lectura
		PfTagsDTO tagRead = applicationVO.getStateTags().get(Constants.C_TAG_READ);
		
		// si la petición tiene la etiqueta de nuevo y es un job
		if (requestTag.getStateJob() != null
				&& Constants.C_TAG_NEW.equals(requestTag.getStateJob().getPfTag().getCtag())) {
						//|| Constants.C_TAG_AWAITING_PASSED.equals(request.getStateJob().getPfTag().getCtag()))) {
			//cambiamos la etiqueta a leido
			change = changeRequestTagAuthUser(requestTag, tagRead, true);
			isJob = true;
		}
		// si la petición tiene la etiqueta de nuevo y es un usuario
		if (requestTag.getStateUser() != null
				&& Constants.C_TAG_NEW.equals(requestTag.getStateUser().getPfTag().getCtag())) {
				//|| Constants.C_TAG_AWAITING_PASSED.equals(request.getStateUser().getPfTag().getCtag()))) {
			//cambiamos la etiqueta a leido
			change = changeRequestTagAuthUser(requestTag, tagRead, false);
			isJob = false;
		}
		//si se ha cambiado la etiqueta
		if (change) {
			// save historic request
			historicRequestBO.saveHistoricRequest(requestTag.getPfRequest(), user, Constants.C_HISTORIC_REQUEST_READ, isJob);

			// List with the request to work with notice and action api
			List<AbstractBaseDTO> auxRequestList = new ArrayList<AbstractBaseDTO>();
			auxRequestList.add(requestTag.getPfRequest());

			// execute actions
//			actionBO.executeActions(auxRequestList, user.getCidentifier(), tagRead.getCtag());

			// execute notices
			// check if request should be noticed when state is read
			if (noticeBO.noticeWhenState(requestTag.getPfRequest(), tagRead.getCtag())) {
				noticeBO.noticeStateChange(requestTag.getPfRequest(), applicationVO.getEmail(), applicationVO.getSMS());
			}

			requestTag.getPfRequest().setFmodified(Calendar.getInstance().getTime());
			baseDAO.update(requestTag.getPfRequest());
		}

		return change;
	}

	/**
	 * M&eacute;todo que marca una lista de peticiones como "Le&iacute;das"
	 * @param pageSize N&uacute;mero de peticiones por paginación.
	 * @param pageActual N&uacute;mero de p&aacute;gina actual.
	 * @param request Listado de peticiones contenidas en la p&aacute;gina actual.
	 * @param user Usuario que marca las peticiones.
	 */
	public void changeStateToReadList(int pageSize, int pageActual,
			List<AbstractBaseDTO> requestTagList, PfUsersDTO user) {

		int initPage = (pageActual - 1) * pageSize;
		int endPage = initPage + pageSize;
		PfRequestTagsDTO requestTag = null;
		for (int i = initPage; i < endPage && i < requestTagList.size(); i++) {
			requestTag = (PfRequestTagsDTO) requestTagList.get(i);
			if (requestTag.getPfRequest().isSelected()) {
				changeStateToRead(requestTag, user);
			}
		}
	}
	
	
	/**
	 * M&eacute;todo que marca una lista de peticiones como "Le&iacute;das"
	 * @param request Listado de peticiones contenidas en la p&aacute;gina actual.
	 * @param user Usuario que marca las peticiones.
	 */
	public void changeStateToReadList(List<AbstractBaseDTO> requestTagList, PfUsersDTO user) {
		for (AbstractBaseDTO abs : requestTagList) {
			PfRequestTagsDTO requestTag = (PfRequestTagsDTO) abs;
			if (requestTag.getPfRequest().isSelected()) {
				changeStateToRead(requestTag, user);
			}
		}
	}

	/**
	 * Método que cambia el estado de una lista de peticiones a "No Leída".
	 * @param pageSize Número de peticiones.
	 * @param pageActual Nte;mero de la p&aacute;gina de la tabla de peticiones actual.
	 * @param requestList Lista de peticiones a marcar.
	 * @param user Usuario que marca las peticiones.
	 */
	public void changeStateToNewList(int pageSize, int pageActual,
			List<AbstractBaseDTO> requestList, PfUsersDTO user) {

		int initPage = (pageActual - 1) * pageSize;
		int endPage = initPage + pageSize;
		PfRequestTagsDTO requestTag = null;
		for (int i = initPage; i < endPage && i < requestList.size(); i++) {
			requestTag = (PfRequestTagsDTO) requestList.get(i);
			if (requestTag.getPfRequest().isSelected()) {
				changeStateToNew(requestTag, user);
			}
		}
	}
	
	/**
	 * Método que cambia el estado de una lista de peticiones a "No Leída".
	 * @param requestList Lista de peticiones a marcar.
	 * @param user Usuario que marca las peticiones.
	 */
	public void changeStateToNewList(List<AbstractBaseDTO> requestList, PfUsersDTO user) {

		for (AbstractBaseDTO abs : requestList) {
			PfRequestTagsDTO requestTag = (PfRequestTagsDTO) abs;
			if (requestTag.getPfRequest().isSelected()) {
				changeStateToNew(requestTag, user);
			}
		}
	}

	/**
	 * M&eacute;todo que cambia el estado de una petición a "No Le&iacute;da".
	 * @param request Petición que se marca como no le&iacute;da.
	 * @param user Usuario que marca la petición.
	 * @return Devuelve true si ha cambiado el estado de la petición. 
	 */
	public boolean changeStateToNew(PfRequestTagsDTO requestTag, PfUsersDTO user) {

		boolean change = false;
		PfTagsDTO tagNew = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
		
		// change state
		if (requestTag.getStateJob() != null
				&& (Constants.C_TAG_READ.equals(requestTag.getStateJob().getPfTag().getCtag())
				|| Constants.C_TAG_AWAITING_PASSED.equals(requestTag.getStateJob().getPfTag().getCtag()))){
			change = changeRequestTagAuthUser(requestTag, tagNew, true);
		}
		if (requestTag.getStateUser() != null
				&& (Constants.C_TAG_READ.equals(requestTag.getStateUser().getPfTag().getCtag())
				|| Constants.C_TAG_AWAITING_PASSED.equals(requestTag.getStateUser().getPfTag().getCtag()))){
			change = changeRequestTagAuthUser(requestTag, tagNew, false);
		}
		if (change) {
			requestTag.getPfRequest().setFmodified(Calendar.getInstance().getTime());
			baseDAO.update(requestTag.getPfRequest());
		}

		return change;
	}

	/**
	 * M&eacute;todo que añade la etiqueta "NUEVA" a una petición para todos los
	 * usuarios pertenecientes a la l&iacute;nea de firma. Si la firma es en cascada
	 * se establece el estado "EN ESPERA". 
	 * @param request Petición a etiquetar.
	 * @return Valor de la etiqueta ("NUEVA", "EN ESPERA").
	 */
	public String addStateNew(PfRequestsDTO request) {
		log.info("nextStateUser init");
		String result = Constants.C_TAG_NEW;

		// Query sign line by user
		log.debug("Query sign line");
		List<AbstractBaseDTO> signLineList = querySignLineList(request);

		// Check every sign line
		log.debug("Check every sign line");
		for (AbstractBaseDTO sli : signLineList) {
			if (request.getLcascadeSign()
					&& ((PfSignLinesDTO) sli).getPfSignLine() != null) {
				result = Constants.C_TAG_AWAITING;
			}
			for (AbstractBaseDTO signer : ((PfSignLinesDTO) sli).getPfSigners()) {
				PfUsersDTO user = ((PfSignersDTO) signer).getPfUser();
				changeRequestTagUserWS(request, (PfSignLinesDTO) sli, applicationVO.getStateTags().get(result), user);
			}
		}

		log.info("nextStateUser end");
		return result;
	}

	/**
	 * M&eacute;todo que añade una serie de etiquetas de usuario a una petición.
	 * @param labelList Lista de etiquetas a añadir a la petición.
	 * @param user Usuario que añade la etiqueta.
	 * @param request Petición que es etiquetada.
	 */
	@Transactional(readOnly = false)
	public void saveTagsUserRequest(List<AbstractBaseDTO> labelList,
			PfUsersDTO user, PfRequestTagsDTO requestTag, PfGroupsDTO groupUserLogged) {

		for (Iterator<AbstractBaseDTO> iteratorLabel = labelList.iterator(); iteratorLabel.hasNext();) {
			PfUserTagsDTO current = (PfUserTagsDTO) iteratorLabel.next();

			PfRequestTagsDTO reqTag = addUserTagRequest(current.getPfTag(),	requestTag.getPfRequest(), user, requestTag.getPfSignLine(), groupUserLogged);
			if (reqTag != null) {
				requestTag.getPfRequest().getPfRequestsTags().add(reqTag);
			}
		}
	}

	/**
	 * Añade la lista de etiquetas a la petición pasada como par&aacute;metro
	 * @param labelList lista de etiquetas 
	 * @param user usuario
	 * @param request petición a la que se asocian las etiquetas de usuario
	 * @see #saveTagsUserRequest(List, PfUsersDTO, PfRequestsDTO)
	 */
	public void saveTagsUserRequestTransactional(
			List<AbstractBaseDTO> labelList, PfUsersDTO user,
			PfRequestTagsDTO requestTag) {
		saveTagsUserRequest(labelList, user, requestTag, null);
	}

	/**
	 * M&eacute;todo que permite al usuario añadir una lista de etiquetas a las peticiones seleccionadas por el mismo. 
	 * La lista de peticiones contendr&aacute; las seleccionables en la paginación actual de la tabla. 
	 * @param pageSize N&uacute;mero de peticiones por paginación.
	 * @param pageActual Paginación actual.
	 * @param labelList Listado de etiquetas.
	 * @param user Usuario que etiqueta las peticiones.
	 * @param requestList Listado de peticiones a etiquetar.
	 */
	public void saveTagsUserRequestList(int pageSize, int pageActual,
			List<AbstractBaseDTO> labelList, PfUsersDTO user,
			List<AbstractBaseDTO> requestList) {

		// iteration over request list to know which of thoses are checked
		int initPage = (pageActual - 1) * pageSize;
		int endPage = initPage + pageSize;

		for (int i = initPage; i < endPage && i < requestList.size(); i++) {
			PfRequestTagsDTO requestTag = (PfRequestTagsDTO) requestList.get(i);

			// only if the request is selected
			if (requestTag.isSelected()) {
				saveTagsUserRequest(labelList, user, requestTag, null);
			}
		}
	}

	/**
	 * M&eacute;todo que devuelve todas las etiquetas de un usuario y un cargo para una petición.
	 * @param request petición.
	 * @param user Usuario.
	 * @param job Cargo.
	 * @return Listado de etiquetas de la petición.
	 */
	public Set<PfRequestTagsDTO> queryRequestTagUser(PfRequestsDTO request,
			PfUsersDTO user, PfUsersDTO job) {
		HashSet<PfRequestTagsDTO> result = new HashSet<PfRequestTagsDTO>();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", request);
		parameters.put("user", user);
		List<AbstractBaseDTO> reqTagUserList = baseDAO.queryListMoreParameters(
				"request.tagsUser", parameters);
		if (reqTagUserList != null && !reqTagUserList.isEmpty()) {
			for (AbstractBaseDTO reqTagUser : reqTagUserList) {
				result.add((PfRequestTagsDTO) reqTagUser);
			}
		}
		List<AbstractBaseDTO> reqTagJobList = null;
		if (job != null) {
			parameters.put("user", user.getValidJob());
			reqTagJobList = baseDAO.queryListMoreParameters("request.tagsUser",
					parameters);
			if (reqTagJobList != null && !reqTagJobList.isEmpty()) {
				for (AbstractBaseDTO reqTagJob : reqTagJobList) {
					result.add((PfRequestTagsDTO) reqTagJob);
				}
			}
		}
		return result;
	}

	/**
	 * Método que devuelve las etiquetas de un usuario para una línea de firma de una petición
	 * @param request
	 * @param user
	 * @param job
	 * @param signLine
	 * @return Lista de etiquetas petición
	 */
	public List<AbstractBaseDTO> queryUserRequestTagsBySignLine(PfRequestsDTO request, PfUsersDTO user, PfSignLinesDTO signLine) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("req", request);
		parameters.put("user", user);
		parameters.put("signLine", signLine);
		List<AbstractBaseDTO> reqTagUserList = baseDAO.queryListMoreParameters("request.tagsUserbySignLine", parameters);
		return reqTagUserList;
	}

	/*
	 * TRANSLATE TAGS
	 */

	/**
	 * M&eacute;todo que define la configuración de color de las etiquetas de una petición en base a las del usuario.
	 * @param request Petición a configurar.
	 * @param userTaglist Lista de etiquetas del usuario.
	 */
	public void translateUserTag(PfRequestsDTO request,	List<PfUserTagsDTO> userTagList) {
		for (Iterator<PfRequestTagsDTO> iterator2 = request.getPfRequestsTags().iterator(); iterator2.hasNext();) {
			PfRequestTagsDTO reqTag = (PfRequestTagsDTO) iterator2.next();
			// Se recorren las etiquetas de usuario de la petición
			if (reqTag.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_USER)) {
				boolean found = false;
				for (Iterator<PfUserTagsDTO> iterator3 = userTagList.iterator(); iterator3.hasNext() && !found;) {
					PfUserTagsDTO userTag = (PfUserTagsDTO) iterator3.next();
					// Si las etiquetas coinciden en id, se define el color en base a la configuración del usuario
					if (userTag.getPfTag().getPrimaryKeyString().equals(reqTag.getPfTag().getPrimaryKeyString())) {
						reqTag.getPfTag().setCcolor(userTag.getCcolor());
						found = true;
					}

				}
			}
		}
	}

	/**
	 * M&eacute;todo que configura las etiquetas de usuario para un grupo de peticiones paginadas.
	 * @param pageSize N&uacute;mero de peticiones por paginación.
	 * @param pageActual Posición de la paginación actual.
	 * @param requestList Listado de peticiones de la paginación.
	 * @param userTagList Listado de etiquetas del usuario.
	 */
	public void translateUserTagList(int pageSize, int pageActual,
			List<AbstractBaseDTO> requestList, List<PfUserTagsDTO> userTagList) {
		int initPage = (pageActual - 1) * pageSize;
		int endPage = initPage + pageSize;
		PfRequestTagsDTO requestTag = null;
		for (int i = initPage; i < endPage && i < requestList.size(); i++) {
			requestTag = (PfRequestTagsDTO) requestList.get(i);
			this.translateUserTag(requestTag.getPfRequest(), userTagList);
		}
	}

	/**
	 * M&eacute;todo que actualiza todas las etiquetas de tipo estado de cada una de las peticiones
	 * de una lista con el mensaje del mapa de mensajes correspondiente.
	 * @param pageSize N&uacute;mero de peticiones por paginación.
	 * @param pageActual Posición de la paginación actual.
	 * @param requestList Listado de peticiones.
	 */
	public void translateStateTagsList(int pageSize, int pageActual,
			List<AbstractBaseDTO> requestList) {
		int initPage = (pageActual - 1) * pageSize;
		int endPage = initPage + pageSize;
		PfRequestTagsDTO requestTag = null;
		for (int i = initPage; i < endPage && i < requestList.size(); i++) {
			requestTag = (PfRequestTagsDTO) requestList.get(i);
			translateStateTags(requestTag.getPfRequest());
		}
	}
	/**
	 * Actualiza todas las etiquetas de tipo estado de la petición,
	 * por el mensaje del mapa de mensajes correspondiente
	 * @param request la petición
	 * @see #translateStateTags(PfRequestsDTO)
	 */
	public void translateStateTags(PfRequestsDTO request) {
		PfRequestTagsDTO reqTag = null;
		for (Iterator<PfRequestTagsDTO> iterator = request.getPfRequestsTags()
				.iterator(); iterator.hasNext();) {
			reqTag = iterator.next();
			if (Constants.C_TYPE_TAG_STATE.equals(reqTag.getPfTag().getCtype())) {
				translateState(reqTag);
			}
		}
	}
	/**
	 * Actualiza el estado traducido de la etiqueta por el mensaje
	 * del mapa de mensajes correspondiente
	 * @param reqTag la etiqueta de petición 
	 * @see #messages
	 * @see es.seap.minhap.portafirmas.domain.PfTagsDTO#setCtagTranslated(String)
	 */
	private void translateState(PfRequestTagsDTO reqTag) {
		if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cAwaiting"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_EXPIRED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cExpired"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_NEW)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cNew"));
		}/* else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_NO_NOTIFIED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cNoNotified"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_NOTIFIED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cNotified"));
		*/ else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_READ)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cRead"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_REJECTED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cRejected"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_REMOVED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cRemoved"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cSigned"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_ENQUEUED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cTailed"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_PASSED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cPassed"));
		} else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING_PASSED)) {
			reqTag.getPfTag().setCtagTranslated(messages.getProperty("cAwaitingPassed"));
		}

	}

	/**
	 * Método que añade una etiqueta de usuario a una petición.
	 * @param tag Etiqueta a añadir.
	 * @param request Petición a la que se añade la etiqueta.
	 * @param user Usuario que añade la etiqueta a la petición.
	 * @return Objeto que representa la relación creada entre la etiqueta, el usuario y la petición.
	 */
	private PfRequestTagsDTO addUserTagRequest(PfTagsDTO tag, PfRequestsDTO request, PfUsersDTO user, PfSignLinesDTO signLine, PfGroupsDTO groupUserLogged) {
		PfRequestTagsDTO requestTag = null;
		requestTag = new PfRequestTagsDTO();
		requestTag.setPfTag(tag);
		requestTag.setPfUser(user);
		requestTag.setPfRequest(request);
		requestTag.setPfSignLine(signLine);
		requestTag.setPfGroup(groupUserLogged);

		// now we need to check if the request already have this
		// tag
		// if it doesnt have it we need to add one new tag
		boolean found = false;

		for (Iterator<PfRequestTagsDTO> iterator = request.getPfRequestsTags().iterator(); iterator.hasNext() && !found;) {
			PfRequestTagsDTO aux = iterator.next();
			if(aux.getPfUser() != null){
				if (aux.getPfTag().getPrimaryKeyString().equals(requestTag.getPfTag().getPrimaryKeyString())
						&& aux.getPfUser().getPrimaryKeyString().equals(requestTag.getPfUser().getPrimaryKeyString())) {
					found = true;
					requestTag = null;
				}
			}

		}
		if (!found) {
			baseDAO.insertOrUpdate(requestTag);
		}

		return requestTag;
	}
	
	/**
	 * Consulta en la BBDD la etiqueta en el momento actual y devuelve true si está en un estado final:
	 * FIRMADO, V.BUENO, RECHAZADO O RETIRADO.
	 * @param reqTag
	 * @return
	 */
	public boolean checkNotFinishedReqTag (PfRequestTagsDTO reqTag) {
		
		PfRequestTagsDTO currentReqTag = queryRequestTagByPk(reqTag.getPrimaryKey());
		String currentTag = currentReqTag.getPfTag().getCtag();
		
		boolean notFinished = true;
		if (currentTag.contentEquals(Constants.C_TAG_PASSED) ||
				currentTag.contentEquals(Constants.C_TAG_REJECTED) ||
				currentTag.contentEquals(Constants.C_TAG_SIGNED) ||
				currentTag.contentEquals(Constants.C_TAG_REMOVED)) {
					notFinished =  false;
		}
		
		return notFinished;
	}
	
	/**
	 * /**
	 * Rellena las listas finishedReqTagList y notfinishedReqTagList con las etiquetas-petición que estén o no
	 * en un estado terminado (FIRMADO, V.BUENO, RECHAZADO, RETIRADO)
	 * @param reqTagList Lista de peticiones de las que se quiere saber si están en un estado terminado.
	 * @param finishedReqTagList lista a la que se irán añadiendo etiquetas-peticion en estado terminado.
	 * @param notFinishedReqTagList lista a la que se irán añadiendo etiquetas-peticion en estado no terminado.
	 */
	public void addNotFinishedAndFinishedReqTags (final List<AbstractBaseDTO> reqTagList, List<AbstractBaseDTO> finishedReqTagList, List<AbstractBaseDTO> notFinishedReqTagList) {
		
		for (AbstractBaseDTO abs : reqTagList) {
			PfRequestTagsDTO reqTag = (PfRequestTagsDTO) abs;
			if (checkNotFinishedReqTag (reqTag)) {
				notFinishedReqTagList.add(reqTag);						
			} else {
				finishedReqTagList.add(reqTag);
			}
		}
	}

	/**
	 * Comprueba si una petición no está en estado TERMINADA
	 * @param chash Hash de la petición
	 * @return True si no está terminada, False en caso contrario
	 */
	public boolean checkNotFinishedRequest(String chash) {
		boolean result = false;
		log.debug("Checking request not finished");

		// Si la petición tiene alguna etiqueta del tipo "NUEVA", "LEIDO",
		// "EN ESPERA" o "VALIDADO" no está finalizada.
		if (chash != null && !"".equals(chash)) {
			List<PfRequestTagsDTO> requestTags = baseDAO.queryListOneParameter("request.allRequestTagsFromHash", "chash", chash);

			for (PfRequestTagsDTO requestTag :  requestTags) {
				if (requestTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING) 		||
					requestTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING_PASSED) ||
					requestTag.getPfTag().getCtag().equals(Constants.C_TAG_NEW)  			||
					requestTag.getPfTag().getCtag().equals(Constants.C_TAG_READ)) {
					result = true;
				}
			}
		}

		return result;
	}

	/**
	 * Método que obtiene una etiqueta de usuario a partir de su clave primaria
	 * @param userTagPK Clave primaria de la etiqueta de usuario
	 * @return La etiqueta de usuario
	 */
	public PfUserTagsDTO queryRequestTagByPK(String userTagPK) {
		AbstractBaseDTO dto = baseDAO.queryElementOneParameter("request.requestUserTagByPK", "pk", Long.parseLong(userTagPK));
		return (PfUserTagsDTO) dto;
	}
	
	/**
	 * Obtiene una realcion etiqueta-petición a partir de una petición y una etiqueta
	 * @param request
	 * @param tag
	 * @return
	 */
	public List<AbstractBaseDTO> queryRequestTags(PfRequestsDTO request, PfTagsDTO tag) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("request", request);
		parameters.put("tag", tag);
		return baseDAO.queryListMoreParameters("request.requestTag", parameters);
	}
}
