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

package es.seap.minhap.portafirmas.business.configuration;

import java.text.SimpleDateFormat;
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
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfAuthorizationTypesDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.utils.Constants;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AuthorizationBO {


	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private TagBO tagBO;

	@Autowired
	private FilterBO filterBO;

	@Autowired
	private ApplicationVO applicationVO;
	
	private Logger log = Logger.getLogger(AuthorizationBO.class);

	@Resource(name = "messageProperties")
	private Properties messages;

	/**
	 * M&eacute;todo que carga un listado con todos los usuarios de la aplicaci&oacute;n distintos al usuario de entrada.
	 * @param current Usuario de entrada.
	 * @param provinceUser Provincia del usuario. Si es nulo no se aplica el filtro.
	 * @return Listado de usuarios.
	 */
	public List<AbstractBaseDTO> queryReceiversList(PfUsersDTO current, String provinceUser) {
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("pk", current.getPrimaryKey());
		params.put("provinceUser", provinceUser);
		return baseDAO.queryListMoreParameters("configuration.authorizationReceivers", params);
		
		//return baseDAO.queryListOneParameter("configuration.authorizationReceivers", "pk", current.getPrimaryKey());
	}

	/**
	 * M&eacute;todo que carga un listado con todos los usuarios de la aplicación de la provincia del usuario distintos al usuario de entrada.
	 * @param current Usuario de entrada.
	 * @return Listado de usuarios.
	 */
	public List<AbstractBaseDTO> queryProvinceReceiversList(PfUsersDTO current, PfProvinceDTO province) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pk", current.getPrimaryKey());
		parameters.put("province", province.getCcodigoprovincia());
		return baseDAO.queryListMoreParameters("configuration.authorizationProvinceReceivers", parameters);
	}

	/**
	 * M&eacute;todo que guarda una nueva autorizaci&oacute;n de usuario.
	 * @param newAutorization Nueva autorizaci&oacute;n creada.
	 * @param sender Usuario que env&iacute;a la autorizaci&oacute;n.
	 * @param receiver Usuario que recibe la autorizaci&oacute;n.
	 * @param authorizationType Tipo de la nueva autorizaci&oacute;n.
	 */
	@Transactional(readOnly = false)
	public void saveAuthorization(PfUsersAuthorizationDTO newAutorization,
			PfUsersDTO sender, PfUsersDTO receiver,
			PfAuthorizationTypesDTO authorizationType) {
		newAutorization.setPfAuthorizationType(authorizationType);
		newAutorization.setPfUser(sender);
		newAutorization.setPfAuthorizedUser(receiver);
		/*if (newAutorization.getFrequest() == null) {
			newAutorization.setFrequest(new Date());
		}*/
		baseDAO.insertOrUpdate(newAutorization);
	}

	public List<AbstractBaseDTO> queryUserAutSentList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.userAuthorizationsOutbox",
				"userPk", userDTO.getPrimaryKey());
	}

	public List<AbstractBaseDTO> queryUserAutReceiverList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.userAuthorizationsInbox",
				"userPk", userDTO.getPrimaryKey());
	}

	/**
	 * M&eacute;todo que carga la lista de autorizaciones sin atender enviadas por el usuario.
	 * @param userDTO Usuario que envia las peticiones.
	 * @return Listado de autorizaciones sin atender.
	 */
	public List<AbstractBaseDTO> queryUserAutAwaitingList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.sentAutAwaiting",
				"userPk", userDTO.getPrimaryKey());
	}

	/**
	 * M&eacute;todo que carga la lista de autorizaciones aceptadas o rechazadas enviadas por el usuario.
	 * @param userDTO usuario que env&iacute;a las peticiones.
	 * @return Listado de autorizaciones aceptadas o rechazadas.
	 */
	public List<AbstractBaseDTO> queryUserAutOthersList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.sentAutOthers", "userPk", userDTO.getPrimaryKey());
	}

	/**
	 * Método que carga la lista de todas las autorizaciones enviadas por el usuario.
	 * @param userDTO usuario que envía las peticiones.
	 * @return Listado de todas las autorizaciones del usuario
	 */
	@Transactional(readOnly=false)
	public List<AbstractBaseDTO> queryUserAuthorizationsList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.userAuthorizationsAll",
				"userPk", userDTO.getPrimaryKey());
	}

	/**Metodo que obtiene una autorizacion a partir de su clave primaria.
	 * @param pk
	 * @return
	 */
	public PfUsersAuthorizationDTO queryAuthorizationByPk(Long pk) {
		return (PfUsersAuthorizationDTO) baseDAO.queryElementOneParameter(
				"configuration.userAuthorizationsByPk", "pk", pk);
	}
	
	@Transactional(readOnly=false)
	public PfUsersAuthorizationDTO queryAuthorizationByPkTrans(Long pk) {
		return queryAuthorizationByPk (pk);
	}

	/**
	 * M&eacute;todo que obtiene un tipo de autorizaci&oacute;n a partir de su clave primaria.
	 * @param pk Clave primaria del tipo de autorizaci&oacute;n.
	 * @return El tipo de autorizaci&oacute;n buscado.
	 */
	public PfAuthorizationTypesDTO queryAuthorizationTypeByPk(Long pk) {
		return (PfAuthorizationTypesDTO) baseDAO.queryElementOneParameter(
				"administration.authorizationTypeByPk", "pk", pk);
	}
	
	/**
	 * Método que obtiene un listado de autorizaciones activas a partir de un usuario.
	 * @param sender usuario que envía petición.
	 * @return El tipo de autorizaci&oacute;n buscado.
	 */
	public List<AbstractBaseDTO> queryAuthorizationTypeActivasByAutoriza(PfUsersDTO sender, String entidad) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("userPk", sender.getPrimaryKey());
		parameters.put("fechaActiva", new Date());
		parameters.put("entidad", entidad);
		return baseDAO.queryListMoreParameters("administration.AuthorizationsActivasByUser", parameters);
	}
	
	/**
	 * Método que obtiene un listado de autorizaciones activas
	 * @return Listado de las autorizaciones activas
	 */
	public List<AbstractBaseDTO> queryAuthorizationPeridoRevocar() {
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 00);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String valor = sdf.format(calendar.getTime());
		log.warn("valor "+valor);
		return baseDAO.queryListOneParameter("administration.AuthorizationsPeriodoRevocar", "fechaActiva", valor);
	}
	
	/**
	 * Método que obtiene un listado de autorizaciones activas a partir de un usuario.
	 * @param sender usuario que envía petición.
	 * @return El tipo de autorizaci&oacute;n buscado.
	 */
	public List<AbstractBaseDTO> queryAuthorizationTypeActivasByAutorizado(PfUsersDTO sender, String entidad) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("userPk", sender.getPrimaryKey());
		parameters.put("fechaActiva", new Date());
		parameters.put("entidad", entidad);
		return baseDAO.queryListMoreParameters("administration.AuthorizationsActivasByUserAutorizado", parameters);
	}
	
	public List<AbstractBaseDTO> queryAuthorizationForDate(PfUsersDTO autoriza,PfUsersDTO autorizado, Date fstart, Date fend, String entidad) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("userPk", autoriza.getPrimaryKey());
		parameters.put("userPkAutorizado",autorizado.getPrimaryKey());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		parameters.put("fechaInicio", sdf.format(fstart));
		parameters.put("fend", sdf.format(fend));
		parameters.put("entidad", entidad);
		return baseDAO.queryListMoreParameters("administration.AuthorizationsByUserDate", parameters);
	}

	/**
	 * M&eacute;todo que rechaza una autorizaci&oacute;n por parte de un usuario.
	 * @param usersAuthorizationDTO Objeto que relaciona al usuario y la autorizaci&oacute;n.
	 */
	@Transactional(readOnly = false)
	public void revokeAuthorization(PfUsersAuthorizationDTO usersAuthorizationDTO) {
		usersAuthorizationDTO.setFrevocation(new Date());
		baseDAO.insertOrUpdate(usersAuthorizationDTO);
	}

	/**
	 * M&eacute;todo que permite a un usuario eliminar una autorizaci&oacute;n que ha recibido.
	 * @param authorizationDTO Autorizaci&oacute;n a eliminar.
	 * @param userVO Usuario que elimina la autorizaci&oacute;n.
	 * @param undoVO Objeto para controlar las acciones que se pueden deshacer.
	 */
	/*@Transactional(readOnly = false)
	public void deleteUserAuthorization(PfUsersAuthorizationDTO authorizationDTO, UserVO userVO, UndoVO undoVO) {
		boolean canBeDeleted = true;
		if (authorizationDTO.getPfAuthorizationFilters().size() > 0) {
			for (Iterator<PfAuthorizationFiltersDTO> iterator = 
				authorizationDTO.getPfAuthorizationFilters().iterator(); iterator.hasNext();) {
				PfAuthorizationFiltersDTO autFilter = (PfAuthorizationFiltersDTO) iterator.next();
				if (autFilter.getPfSigners().size() == 0) {
					// it can be deleted
					baseDAO.delete(autFilter);
//					undoVO.changesSaved();
				} else if (canBeDeleted) {
					// it's not possible to delete it cause it has been used in
					// any filter
					//TODO: Adaptar a SPRING MVC
//					FacesMessage facesMessage = FacesMessages.createFacesMessage(FacesMessage.SEVERITY_ERROR,
//									"errorAuthorizationCantBeFullyDeleted", "", authorizationDTO);
//					userVO.addMessage("", facesMessage);
//					canBeDeleted = false;

					//revoke authorization
					authorizationDTO.setFrevocation(new Date());
					baseDAO.insertOrUpdate(authorizationDTO);
				}

			}
		}

		if (canBeDeleted) {
			baseDAO.delete(authorizationDTO);
		}
	}*/

	/**
	 * M&eacute;todo que guarda los cambios en el listado de autorizaciones de salida de un usuario.
	 * @param userAwaitingAutOutboxList Listado con las autorizaciones modificadas.
	 * @param userAuthorizedDeleteList Listado con las autorizaciones a eliminar.
	 */
	@Transactional(readOnly = false)
	public void saveUserAuthorizedList(
			List<AbstractBaseDTO> userAwaitingAutOutboxList,
			List<AbstractBaseDTO> userAuthorizedDeleteList) {
		baseDAO.updateList(userAwaitingAutOutboxList);
		baseDAO.deleteList(userAuthorizedDeleteList);
	}

	/**
	 * M&eacute;todo que devuelve siempre falso (?)
	 * @param userAuthorized Objeto que relaciona usuario y autorizaci&oacute;n.
	 * @return False.
	 */
	public boolean checkUserAuthorizedAssociated(PfUsersAuthorizationDTO userAuthorized) {
		return false;
	}
	
	/**
	 * Comprueba que una autorización no se solape en fechas con ninguna autorización
	 * relacionada con ese emisor (sender) y receptor (receiver)
	 * @param newAutorization Autorización de la que se quiere saber si se solapa en fechas con alguna otra.
	 * @param sender Usuario que envía la autorización.
	 * @param receiver Destinatario de la autorización.
	 * @return true si no hay solapamiento, false en caso contrario
	 */
	public boolean overlapAuthorizations(PfUsersAuthorizationDTO newAutorization, PfUsersDTO sender, PfUsersDTO receiver) {

		List<AbstractBaseDTO> outboxAutAwaitingList = queryUserAutAwaitingList(sender);
		List<AbstractBaseDTO> outboxAutOthersList = queryUserAutOthersList(sender);
		List<AbstractBaseDTO> inboxAutAwaitingList = queryInboxAutAwaitingList(sender);		
		List<AbstractBaseDTO> inboxAutOthersList = queryInboxAutOthersList(sender);

		return overlapAuthorizationInList(newAutorization, sender, receiver, inboxAutAwaitingList) ||
				overlapAuthorizationInList(newAutorization, sender, receiver, inboxAutOthersList) ||
				overlapAuthorizationInList(newAutorization, sender, receiver, outboxAutAwaitingList) ||	
				overlapAuthorizationInList(newAutorization, sender, receiver, outboxAutOthersList);
	}
	
	/**
	 * Comprueba que una autorización que desea enviar #sender a #receiver no se solape con las autorizaciones de una lista.
	 * @param newAutorization Autorización de la que se quiere saber si se solapa en fechas con alguna otra.
	 * @param sender Usuario que envía la autorización.
	 * @param receiver Destinatario de la autorización.
	 * @return true si no hay solapamiento, false en caso contrario.
	 */
	public boolean overlapAuthorizationInList (PfUsersAuthorizationDTO newAuthorization,
													PfUsersDTO sender, PfUsersDTO receiver,	
													List<AbstractBaseDTO> authorizations) {
		boolean overlap = false;
		int i=0;
		
		// Si la fecha de autorizacion es nula, se tomará como inicial la fecha de hoy.
		//Date fInitNew = null;
		//fInitNew = newAuthorization.getFauthorization() != null ? newAuthorization.getFauthorization() : new Date();
		Date fInitNew = newAuthorization.getFrequest();
		
		Date fEndNew = newAuthorization.getFrevocation();
		Date now = new Date();
		while (i<authorizations.size() && !overlap) {
			PfUsersAuthorizationDTO autInList = (PfUsersAuthorizationDTO) authorizations.get(i);
			boolean sameAuthorization = newAuthorization.getPrimaryKey()!=null ? newAuthorization.getPrimaryKey().longValue()==autInList.getPrimaryKey().longValue() :
				false;
			boolean isRevoked = autInList.getFrevocation() != null && autInList.getFrevocation().before(now);
			// Si no está revocada la de la lista y no se trata de la misma autorización (caso que se da en la modificaciones)
			if(!isRevoked && !sameAuthorization) {
				// Si los dos usuarios que toman parte en una autorización, coinciden en las dos autorizaciones
				if ((sender.getPrimaryKeyString().equals(autInList.getPfUser().getPrimaryKeyString()) &&
					 receiver.getPrimaryKeyString().equals(autInList.getPfAuthorizedUser().getPrimaryKeyString())) ||
					(sender.getPrimaryKeyString().equals(autInList.getPfAuthorizedUser().getPrimaryKeyString()) &&
					 receiver.getPrimaryKeyString().equals(autInList.getPfUser().getPrimaryKeyString()))) {
					
					Date fInit = autInList.getFrequest();
					Date fEnd = autInList.getFrevocation();
					if ((fInitNew.after(fInit) && (fEnd == null || fInitNew.before(fEnd))) ||
						(fInit.after(fInitNew) && (fEndNew == null || fInit.before(fEndNew)))) {
						overlap = true;
					}
				}
			}
			i++;
		}
		
		
		return overlap;
		
	}

	/**
	 * M&eacute;todo que a&ntilde;ade un nuevo usuario a la lista de autorizaciones de salida.
	 * @param userAwaitingAutOutboxList Listado de autorizaciones de salida.
	 */
	public void addUserAuthorized(List<AbstractBaseDTO> userAwaitingAutOutboxList) {
		PfUsersAuthorizationDTO userAuthorized = new PfUsersAuthorizationDTO();
		userAwaitingAutOutboxList.add(0, userAuthorized);
	}

	/**
	 * M&eacute;todo que devuelve el listado de autorizaciones recibidas por un usuario que no han sido rechazadas y est&aacute;n en espera.
	 * @param userDTO Usuario que recibe las autorizaciones.
	 * @return Listado de autorizaciones.
	 */
	public List<AbstractBaseDTO> queryInboxAutAwaitingList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.receivedAutAwaiting", "userPk", userDTO.getPrimaryKey());
	}

	/**
	 * M&eacute;todo que devuelve el listado de autorizaciones recibidas por un usuario que han sido rechazadas o que fueron aceptadas.
	 * @param userDTO Usuario que recibe las autorizaciones.
	 * @return Listado de autorizaciones.
	 */
	public List<AbstractBaseDTO> queryInboxAutOthersList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.receivedAutOthers",	"userPk", userDTO.getPrimaryKey());
	}

	/**
	 * Método que permite aceptar autorizaciones recibidas por un usuario.
	 * Actualizará las peticiones no firmadas del usuario que autoriza.
	 * @param authorizationList Listado de autorizaciones a aceptar.
	 */
	@Transactional(readOnly = false)
	public void acceptAuthorizations(List<AbstractBaseDTO> authorizationList, ArrayList<PfUsersAuthorizationDTO> notificar) {
		for (Iterator<AbstractBaseDTO> iterator = authorizationList.iterator(); iterator.hasNext();) {
			PfUsersAuthorizationDTO userAut = (PfUsersAuthorizationDTO) iterator.next();
			if (userAut.isSelected()) {
				notificar.add(userAut);
				acceptAuthorization(userAut);
			}
		}
	}

	/**
	 * M&eacute;todo que permite rechazar autorizaciones recibidas por un usuario.
	 * @param authorizationList Listado de autorizaciones a rechazar.
	 */
	@Transactional(readOnly = false)
	public void rejectAuthorizations(List<AbstractBaseDTO> authorizationList, ArrayList<PfUsersAuthorizationDTO> notificar) {
		for (Iterator<AbstractBaseDTO> iterator = authorizationList.iterator(); iterator.hasNext();) {
			PfUsersAuthorizationDTO userAut = (PfUsersAuthorizationDTO) iterator.next();
			if (userAut.isSelected()) {
				rejectAuthorization(userAut);
				notificar.add(userAut);
			}
		}
	}

	/**
	 * Método que permite aceptar una autorización recibida por un usuario.
	 * Actualizará las peticiones no firmadas del usuario que autoriza.
	 * @param authorizationList Listado de autorizaciones a aceptar.
	 */
	@Transactional(readOnly = false)
	public void acceptAuthorization(PfUsersAuthorizationDTO usersAuthorizationDTO) {
		Date now = new Date();
		usersAuthorizationDTO.setFauthorization(now);
		baseDAO.insertOrUpdate(usersAuthorizationDTO);

		// Se actualizan los destinatarios de las peticiones del usuario que autoriza
		// Listado de peticiones afectadas del usuario que autoriza
		List<PfRequestsDTO> peticiones = 
				baseDAO.queryListOneParameter("request.affectedRequests", "sender", usersAuthorizationDTO.getPfUser());

		List<AbstractBaseDTO> userJobList = baseDAO.queryListOneParameter("request.userJob", "usuario", usersAuthorizationDTO.getPfUser());

		PfUsersJobDTO userJob = null;
		int i = 0;
		while (i < userJobList.size() && userJob == null) {
			AbstractBaseDTO uj = userJobList.get(i);
			PfUsersJobDTO auxUserJob = (PfUsersJobDTO) uj;
			if (auxUserJob.getFend() == null || auxUserJob.getFend().after(now)) {
				userJob = auxUserJob;
			}
			i++;
		}

		// Si el usuario tiene un cargo asociado
		List<PfRequestsDTO> peticionesCargo = null;
		if (userJob != null) {
			// Listado de peticiones afectadas del cargo del usuario que autoriza
			peticionesCargo = baseDAO.queryListOneParameter("request.affectedRequests", "sender", userJob.getPfUserJob());
			// Se agregan a la lista de peticiones, las autorizaciones a cada petición sin firmar, del cargo del usuario que autoriza
			peticiones.addAll(peticionesCargo);
		}

		// Se aplican las autorizaciones a cada petición sin firmar del usuario que autoriza
		for (PfRequestsDTO peticion : peticiones) {
			applyAuthorizations(peticion);
		}
	}

	/**
	 * Método para rechazar una autorización recibida por un usuario.
	 * @param usersAuthorizationDTO
	 */
	@Transactional(readOnly = false)
	public void rejectAuthorization(PfUsersAuthorizationDTO usersAuthorizationDTO) {
		usersAuthorizationDTO.setFrevocation(new Date());
		usersAuthorizationDTO.setFauthorization(new Date());
		baseDAO.insertOrUpdate(usersAuthorizationDTO);
	}
	
	@Transactional(readOnly = false)
	public void applyAuthorizationsTransactional (PfRequestsDTO request) {
		applyAuthorizations (request);
	}
	
	/**
	 * Método que redefine las líneas de firma de una petición eliminado de las líneas de firma
	 * @param request Petición de la cual se redefinen sus líneas de firma.
	 * @param receiver 
	 */
	@Transactional(readOnly = false)
	public void revocarAutorizacionActiva(PfRequestsDTO request, PfUsersDTO autoriza, PfUsersDTO autorizado){
		// Se obtienen la petición y sus datos
		boolean encontrado = false;
		baseDAO.flush();
		baseDAO.clearSession();
		PfRequestsDTO peticion = (PfRequestsDTO) baseDAO.queryElementOneParameter("request.requestAuthorizations", "requestPk", request.getPrimaryKey());
		List<PfSignersDTO> signersToDelete = new ArrayList<PfSignersDTO> ();
		if (peticion != null) {
			Set<PfSignLinesDTO> signLines = peticion.getPfSignsLines();
			
			for (PfSignLinesDTO signLine : signLines) {
				if(!encontrado){
					Set<PfSignersDTO> signers = signLine.getPfSigners();
					for (PfSignersDTO signer : signers) {
						if(!encontrado){
							PfUsersDTO userFirma = signer.getPfUser();
							//Recupera la etiqueta de petición de tipo 'ESTADO'
							PfRequestTagsDTO reqTag = tagBO.queryStateUserSignLine(peticion, userFirma, signLine);
							//Recupera la etiqueta de peticion de tipo 'SISTEMA.VISTOBUENO'
							PfRequestTagsDTO reqPassTag = tagBO.queryPassUserSignLine(peticion, userFirma, signLine);
							
							// Controlamos que para esa línea de firma el usuario no tenga un estado terminado.
							if (!reqTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED) && !reqTag.getPfTag().getCtag().equals(Constants.C_TAG_PASSED)){
								//compruebo que este el que autoriza también en este línea de firma
								if(estaAutorizaLineaFirma(signLine,autoriza)){	
									String entidad = obtenerEntidad(request.getDreference());
									if(!tieneAutorizaEliminarPeticiones(autorizado, autoriza, entidad) && !tieneAutorizasRecursivos(autorizado, autoriza, entidad)){
										//Miro si en esta linea de firma se encuentra el autorizado
										if(userFirma.getCidentifier().equals(autorizado.getCidentifier())){												
											// Elimino las etiquetas petición de la lista de la petición
											Set<PfRequestTagsDTO> rt = new HashSet<PfRequestTagsDTO>();
											for (PfRequestTagsDTO requestTag : peticion.getPfRequestsTags()) {
												if (requestTag.getPrimaryKeyString().equals(reqTag.getPrimaryKeyString())) {
													rt.add(requestTag);
												}
											}
											for (PfRequestTagsDTO rtDTO : rt) {
												peticion.getPfRequestsTags().remove(rtDTO);
											}
				
											baseDAO.delete(reqTag);
											
											if (reqPassTag != null) {
												// Elimino las etiquetas petición de visto bueno de la lista de la petición
												Set<PfRequestTagsDTO> rtPass = new HashSet<PfRequestTagsDTO>();
												for (PfRequestTagsDTO requestTag : peticion.getPfRequestsTags()) {
													if (requestTag.getPrimaryKeyString().equals(reqPassTag.getPrimaryKeyString())) {
														rtPass.add(requestTag);
													}
												}
												for (PfRequestTagsDTO rtPassDTO : rtPass) {
													peticion.getPfRequestsTags().remove(rtPassDTO);
												}
			
												baseDAO.delete(reqPassTag);
											}
											
											baseDAO.delete(signer);
											signersToDelete.add(signer);
											//Compruebo que el autorizado no tengo debajo de el algún autorizado xq habrá que quitarle los doc tb.
											tieneAutorizadoEliminarPeticiones(autorizado, peticion, signLine, signersToDelete, autoriza);										
											encontrado = true;
										}
									}
																					
								}
							}
						}
					}
					if(signersToDelete.size()>0)signers.removeAll(signersToDelete);
				}
			}
			// Actualiza la petición en la base de datos
			baseDAO.insertOrUpdate(peticion);
		}
	}
	
	/**
	 * p1 <- p2
	 * p2 <- p3
	 * p1 <- p3
	 * Elimino p2 <- p3
	 *
	 * **/
	private boolean tieneAutorizasRecursivos(PfUsersDTO autoriza, PfUsersDTO autorizaOriginal, String entidad) {
		boolean esAutorizadodelAutorizaOriginal = false;
		List<AbstractBaseDTO> receiverListDTO = queryAuthorizationTypeActivasByAutorizado(autoriza, entidad);
		for (AbstractBaseDTO abstractBaseDTO : receiverListDTO) {
			PfUsersAuthorizationDTO autorizacion = (PfUsersAuthorizationDTO) abstractBaseDTO;
			if(!autorizacion.getPfUser().getCidentifier().equals(autorizaOriginal.getCidentifier())){				
				List<AbstractBaseDTO> listaAutoriza = queryAuthorizationTypeActivasByAutorizado(autorizaOriginal, entidad);
				for (AbstractBaseDTO abstractBaseDTO2 : listaAutoriza) {
					PfUsersAuthorizationDTO autorizacionOrigi = (PfUsersAuthorizationDTO) abstractBaseDTO2;
					if(autorizacionOrigi.getPfUser().getCidentifier().equals(autorizacion.getPfUser().getCidentifier())){
						esAutorizadodelAutorizaOriginal = true;
					}
				}	
			}
		}
		return esAutorizadodelAutorizaOriginal;
	}
	/**
	 * p1 <- p2
	 * p2 <- p3
	 * p1 <- p3
	 * Elimino enlace p1 <-p3; p3 tiene que seguir viendolo porqu hereda de p2
	 * **/
	private boolean tieneAutorizaEliminarPeticiones(PfUsersDTO autoriza, PfUsersDTO autorizaOriginal, String entidad) {
		boolean esAutorizadodelAutorizaOriginal = false;
		List<AbstractBaseDTO> receiverListDTO = queryAuthorizationTypeActivasByAutorizado(autoriza, entidad);
		for (AbstractBaseDTO abstractBaseDTO : receiverListDTO) {
			PfUsersAuthorizationDTO autorizacion = (PfUsersAuthorizationDTO) abstractBaseDTO;
			//Que no sea p1 <- p3
			if(!autorizacion.getPfUser().getCidentifier().equals(autorizaOriginal.getCidentifier())){
				List<AbstractBaseDTO> listaAutoriza = queryAuthorizationTypeActivasByAutorizado(autorizacion.getPfUser(), entidad);
				for (AbstractBaseDTO abstractBaseDTO2 : listaAutoriza) {
					PfUsersAuthorizationDTO autorizacionOrigi = (PfUsersAuthorizationDTO) abstractBaseDTO2;
					if(autorizacionOrigi.getPfUser().getCidentifier().equals(autorizaOriginal.getCidentifier())){
						esAutorizadodelAutorizaOriginal = true;
					}
				}	
			}
		}
		return esAutorizadodelAutorizaOriginal;
	}

	/**
	 * p1 <-p2
	 * p2 <-p3
	 * p1 <- p3
	 * 
	 * Y elimino p1 <- p2 por lo tanto lo de p3 hay q quitarlo
	 * @param peticion 
	 * @param signLine 

	 * @param signersToDelete 
	 * @param autorizaOriginal 
	 * **/
	private void tieneAutorizadoEliminarPeticiones(PfUsersDTO autoriza, PfRequestsDTO peticion, PfSignLinesDTO signLine, List<PfSignersDTO> signersToDelete, PfUsersDTO autorizaOriginal) {
		//Se obtiene de quien es autoriza p2 <- p3
		String entidad = obtenerEntidad(peticion.getDreference());
		List<AbstractBaseDTO> receiverListDTO = queryAuthorizationTypeActivasByAutoriza(autoriza, entidad);
		for (AbstractBaseDTO abstractBaseDTO : receiverListDTO) {
			PfUsersAuthorizationDTO autorizacion = (PfUsersAuthorizationDTO) abstractBaseDTO;
			//Compruebo que ese autorizado no sea tb autorizado del autorizaorginal
			//p1 <- p2
			//p2 <- p3
			//p1 <- p3 (Elimino p1 <- p2 y compruebo que no tenga p3 y si tiene ya que esta relacionado p3 con p1 no le quito los doc)
			List<AbstractBaseDTO> listaAutoriza = queryAuthorizationTypeActivasByAutorizado(autorizacion.getPfAuthorizedUser(), entidad);
			boolean esAutorizadodelAutorizaOriginal = false;
			for (AbstractBaseDTO abstractBaseDTO2 : listaAutoriza) {
				PfUsersAuthorizationDTO autorizacionOrigi = (PfUsersAuthorizationDTO) abstractBaseDTO2;
				if(autorizacionOrigi.getPfUser().getCidentifier().equals(autorizaOriginal.getCidentifier())){
					esAutorizadodelAutorizaOriginal = true;
				}
			}
			if(!esAutorizadodelAutorizaOriginal){
				 eliminarEtiquetasRequest(peticion, autorizacion, signLine, signersToDelete);
			}				
		}
	}
	
	public void eliminarEtiquetasRequest(PfRequestsDTO peticion, PfUsersAuthorizationDTO autorizacion, PfSignLinesDTO signLine, List<PfSignersDTO> signersToDelete){
		//Recupera la etiqueta de petición de tipo 'ESTADO'			
		PfRequestTagsDTO reqTag = tagBO.queryStateUserSignLine(peticion, autorizacion.getPfAuthorizedUser(), signLine);
		//Recupera la etiqueta de peticion de tipo 'SISTEMA.VISTOBUENO'
		PfRequestTagsDTO reqPassTag = tagBO.queryPassUserSignLine(peticion, autorizacion.getPfAuthorizedUser(), signLine);
		
		Set<PfSignersDTO> signersLinea = signLine.getPfSigners();
		for (PfSignersDTO signerLinea : signersLinea) {
			PfUsersDTO userFirmaLinea = signerLinea.getPfUser();
			//Miro si en esta linea de firma se encuentra el autorizado
			if(userFirmaLinea.getCidentifier().equals(autorizacion.getPfAuthorizedUser().getCidentifier())){
				// Elimino las etiquetas petición de la lista de la petición
				Set<PfRequestTagsDTO> rt = new HashSet<PfRequestTagsDTO>();
				for (PfRequestTagsDTO requestTag : peticion.getPfRequestsTags()) {
					if (requestTag.getPrimaryKeyString().equals(reqTag.getPrimaryKeyString())) {
						rt.add(requestTag);
					}
				}
				for (PfRequestTagsDTO rtDTO : rt) {
					peticion.getPfRequestsTags().remove(rtDTO);
				}

				baseDAO.delete(reqTag);
				
				if (reqPassTag != null) {
					// Elimino las etiquetas petición de visto bueno de la lista de la petición
					Set<PfRequestTagsDTO> rtPass = new HashSet<PfRequestTagsDTO>();
					for (PfRequestTagsDTO requestTag : peticion.getPfRequestsTags()) {
						if (requestTag.getPrimaryKeyString().equals(reqPassTag.getPrimaryKeyString())) {
							rtPass.add(requestTag);
						}
					}
					for (PfRequestTagsDTO rtPassDTO : rtPass) {
						peticion.getPfRequestsTags().remove(rtPassDTO);
					}

					baseDAO.delete(reqPassTag);
				}
				
				baseDAO.delete(signerLinea);
				signersToDelete.add(signerLinea);

			}
		}
	}

	/**
	 * Esto es por ejemplo
	 * Autoriza -> Autorizado
	 * P1 <- P2
	 * P1 <- P3
	 * P2 <- P3
	 * Y quiero eliminar el enlace entre P1 <- P3 
	 * **/
	private boolean estaOtroAutorizaLineaFirma(PfSignLinesDTO signLine, PfUsersDTO autoriza, PfUsersDTO autorizado, String entidad) {
		boolean esta = true;
		List<AbstractBaseDTO> receiverListDTO = queryAuthorizationTypeActivasByAutoriza(autoriza, entidad);
		if (esta) for (AbstractBaseDTO abstractBaseDTO : receiverListDTO) {
			PfUsersAuthorizationDTO autorizacion = (PfUsersAuthorizationDTO) abstractBaseDTO;
			if(!autorizacion.getPfUser().getCidentifier().equals(autoriza.getCidentifier()) || !autorizacion.getPfAuthorizedUser().getCidentifier().equals(autorizado.getCidentifier())){
				Set<PfSignersDTO> signers = signLine.getPfSigners();
				if (esta)for (PfSignersDTO signer : signers) {
					PfUsersDTO userFirma = signer.getPfUser();
					if(userFirma.getCidentifier().equals(autorizacion.getPfAuthorizedUser().getCidentifier())){
						esta = false;
					}
				}
			}	
		}
		return esta;
	}

	private boolean estaAutorizaLineaFirma(PfSignLinesDTO signLine, PfUsersDTO autoriza) {
		boolean encontrado = false;
		Set<PfSignersDTO> signers = signLine.getPfSigners();
		for (PfSignersDTO signer : signers) {
			PfUsersDTO userFirma = signer.getPfUser();
			if(userFirma.getCidentifier().equals(autoriza.getCidentifier())){
				encontrado = true;
			}
		}
		return encontrado;
	}

	/**
	 * Método que redefine las líneas de firma de una petición en base a las
	 * autorizaciones realizadas sobre los usuarios firmantes.
	 * @param request Petición de la cual se redefinen sus líneas de firma.
	 */
	@Transactional(readOnly = false)
	public void applyAuthorizations(PfRequestsDTO request) {

		log.debug ("init applyAuthorizations, request: " + request.getPrimaryKey());
		// Se obtienen la peticiÃ³n y sus datos
		baseDAO.flush();
		baseDAO.clearSession();
		PfRequestsDTO peticion = 
			(PfRequestsDTO) baseDAO.queryElementOneParameter("request.requestAuthorizations",
					"requestPk", request.getPrimaryKey());

		if (peticion != null) {
			Set<PfSignLinesDTO> signLines = peticion.getPfSignsLines();

			PfRequestTagsDTO reqTag = null;
			PfRequestTagsDTO reqPassTag = null;


			// Se obtienen las lÃ­neas de firma de la peticiÃ³n
			for (PfSignLinesDTO signLine : signLines) {
				Set<PfSignersDTO> signers = signLine.getPfSigners();

				// Firmantes que aÃ±adiremos a la lÃ­nea de firma.
				List<PfSignersDTO> signersToAdd = new ArrayList<PfSignersDTO> ();
				// Firmantes que eliminaremos de la lÃ­nea de firma.
				List<PfSignersDTO> signersToDelete = new ArrayList<PfSignersDTO> ();
				
				// Se obtienen los firmantes de cada lÃ­nea de firma
				for (PfSignersDTO signer : signers) {
					PfTagsDTO tagNextUser = null;
					PfUsersDTO user = signer.getPfUser();
					//Recupera la etiqueta de peticiÃ³n de tipo 'ESTADO'
					reqTag = tagBO.queryStateUserSignLine(peticion, user, signLine);
					//Recupera la etiqueta de peticion de tipo 'SISTEMA.VISTOBUENO'
					reqPassTag = tagBO.queryPassUserSignLine(peticion, user, signLine);

					// Si la etiqueta es nula, significa que dicho usuario ya existÃ­a en otra lÃ­nea
					// de firma de la peticiÃ³n y ha sido sustituido. Por tanto, se debe eliminar al usuario
					// de la lÃ­nea de firma actual.
					if (reqTag == null) {
						baseDAO.delete(signer);
						continue;
					}
					// Si encontramos que para esa lÃ­nea de firma el usuario tiene un estado terminado, no hacemos nada.
					else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED) ||
							reqTag.getPfTag().getCtag().equals(Constants.C_TAG_PASSED)) {
						continue;
					}
					//si la etiqueta es nueva o leido 
					else if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_NEW) ||
							reqTag.getPfTag().getCtag().equals(Constants.C_TAG_READ) || 
							reqTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING_PASSED)) {
						//recupera la etiqueta nueva
						tagNextUser = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
					}
					// si no
					else {
						//recupera la etiqueta en espera
						tagNextUser = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING);
					}
					String entidad = obtenerEntidad(request.getDreference());
					
					// Se obtienen las autorizaciones que solicitÃ³ cada firmante
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("userPk", user.getPrimaryKey());
					parameters.put("entidad", entidad);
					List<AbstractBaseDTO> authorizations = baseDAO.queryListMoreParameters("request.userAuthorizations", parameters);
					// Si el firmante es un cargo, se obtienen tambiÃ©n las autorizaciones del usuario asociado a dicho cargo 
					if (signer.getPfUser().isJob()) {
						List<AbstractBaseDTO> usersSignerJob = 
							baseDAO.queryListOneParameter("request.jobsSigner", "job", signer.getPfUser());
						for (AbstractBaseDTO baseDTO :  usersSignerJob) {
							PfUsersDTO userSignerJob = (PfUsersDTO) baseDTO;
							Map<String, Object> parametersCargo = new HashMap<String, Object>();
							parametersCargo.put("userPk", userSignerJob.getPrimaryKey());
							parametersCargo.put("entidad", entidad);
							authorizations.addAll(baseDAO.queryListOneParameter("request.userAuthorizations", "userPk", parametersCargo));						
						}
					}
					
					//Se comprueba si ese autorizado, autoriza a alguien
					//p1 <- p2
					//p2 <- p3

					List<AbstractBaseDTO> receiverListDTO = queryAuthorizationTypeActivasByAutoriza(signer.getPfUser(), entidad);
					for (AbstractBaseDTO authorization :  receiverListDTO) {
						PfUsersAuthorizationDTO autorizacionUser = (PfUsersAuthorizationDTO) authorization;
						List<AbstractBaseDTO> receiverListDTOAutorizado = queryAuthorizationTypeActivasByAutoriza(autorizacionUser.getPfAuthorizedUser(), entidad);
						if(receiverListDTOAutorizado.size()>0){
							authorizations.addAll(receiverListDTOAutorizado);
						}						
					}

					boolean signerMustBeDeleted = false;
					for (AbstractBaseDTO authorizationBaseDTO : authorizations) {
						PfUsersAuthorizationDTO authorization = (PfUsersAuthorizationDTO) authorizationBaseDTO;

						// Primero se comprueba si la fecha de inicio de la autorizaciÃ³n es anterior a la actual,
						// despuÃ©s se comprueba si la autorizaciÃ³n sigue vigente: La fecha de autorizaciÃ³n tiene que ser anterior
						// a la fecha actual, y la de revocaciÃ³n tiene que ser o nula, o posterior a la fecha actual.
						Date fechaActual = new Date();
						if ((authorization.getFrequest() == null ||
								authorization.getFrequest().before(fechaActual)) &&
						(authorization.getFauthorization()!=null && 
								authorization.getFauthorization().before(fechaActual)
						) &&
						((authorization.getFrevocation() == null) ||
								(authorization.getFrevocation() != null && authorization.getFrevocation().after(fechaActual)))) {					

							// Se obtiene al usuario autorizado
							PfUsersDTO usuarioAutorizado  = authorization.getPfAuthorizedUser();

							// Si el usuario autorizado no estÃ¡ ya en la lÃ­nea de firma se le aÃ±ade
							if (!filterBO.existSignerSignLine(signLine.getPfSigners(), (PfUsersDTO) usuarioAutorizado)) {
								
								PfRequestTagsDTO stateRequestTag = tagBO.queryStateUserSignLine(request, usuarioAutorizado, signLine);
								if(stateRequestTag==null){
									PfSignersDTO newSigner = new PfSignersDTO();
									newSigner.setPfUser(usuarioAutorizado);
									newSigner.setPfSignLine(signLine);
									//signers.add(newSigner);
									newSigner.setTipoAutorizacion(authorization.getPfAuthorizationType());
									signersToAdd.add(newSigner);
									baseDAO.insertOrUpdate(newSigner);

									// Se define la etiqueta de la peticiÃ³n para el nuevo usuario
									tagBO.changeRequestTagUserAut(peticion, signLine, tagNextUser, usuarioAutorizado);
								}
								
							}
							// Si es una sustituciÃ³n, se elimina al usuario que autoriza de la lÃ­nea de firma
							if (authorization.getPfAuthorizationType().getCauthorizationType().equals(Constants.SUBSTITUTE)) {
								signerMustBeDeleted = true;
							}
						}
					}
					
					//Si en la lista de autorizaciones, se ha encontrado algÃºn sustituto vÃ¡lido en fechas.
					if(signerMustBeDeleted){
						baseDAO.delete(signer);
						//Si el firmante es el usuario con sustituto, se elimina como firmante de la peticiÃ³n
						if(signer.getPfUser().getPrimaryKey().longValue() == user.getPrimaryKey().longValue()){
							//signers.remove(signer);
							signersToDelete.add(signer);
						}
						
						// Elimino las etiquetas peticiÃ³n de la lista de la peticiÃ³n
						Set<PfRequestTagsDTO> rt = new HashSet<PfRequestTagsDTO>();
						for (PfRequestTagsDTO requestTag : peticion.getPfRequestsTags()) {
							if (requestTag.getPrimaryKeyString().equals(reqTag.getPrimaryKeyString())) {
								rt.add(requestTag);
							}
						}
						for (PfRequestTagsDTO rtDTO : rt) {
							peticion.getPfRequestsTags().remove(rtDTO);
						}

						baseDAO.delete(reqTag);

						if (reqPassTag != null) {
							// Elimino las etiquetas peticiÃ³n de visto bueno de la lista de la peticiÃ³n
							Set<PfRequestTagsDTO> rtPass = new HashSet<PfRequestTagsDTO>();
							for (PfRequestTagsDTO requestTag : peticion.getPfRequestsTags()) {
								if (requestTag.getPrimaryKeyString().equals(reqPassTag.getPrimaryKeyString())) {
									rtPass.add(requestTag);
								}
							}
							for (PfRequestTagsDTO rtPassDTO : rtPass) {
								peticion.getPfRequestsTags().remove(rtPassDTO);
							}

							baseDAO.delete(reqPassTag);
						}
					}
				}
				signers.removeAll(signersToDelete);
				signers.addAll(signersToAdd);

			}
			// Actualiza la peticiÃ³n en la base de datos
			baseDAO.insertOrUpdate(peticion);

		}
		
		log.debug ("end applyAuthorizations, request: " + request.getPrimaryKey());

	}


	private String obtenerEntidad(String dreference) {

		String [] vReferencia = dreference.split("_");
		String entidad = "000";
		if(vReferencia!=null && vReferencia.length == 3){
			entidad = vReferencia[vReferencia.length-1];
		}
		return entidad;
	}

	/**
	 * Inserta una autorización
	 * @param pfUsersAuthorizationDTO
	 * @return Lista de errores si los hay
	 */
	@Transactional(readOnly = false)
	public void saveAuthorization(PfUsersAuthorizationDTO pfUsersAuthorizationDTO) {
		baseDAO.insertOrUpdate(pfUsersAuthorizationDTO);
	}
	
	/**
	 * Valida que se cumplan las reglas de negocio establecidas
	 * @param usersAuthorizationDTO
	 * @param errors
	 */
	public void validateAuthorization(PfUsersAuthorizationDTO usersAuthorizationDTO, ArrayList<String> errors) {
		if(overlapAuthorizations(
				usersAuthorizationDTO,
				usersAuthorizationDTO.getPfUser(),
				usersAuthorizationDTO.getPfAuthorizedUser())) {
			errors.add(messages.getProperty("seat.overlap"));
		}
	}

}
