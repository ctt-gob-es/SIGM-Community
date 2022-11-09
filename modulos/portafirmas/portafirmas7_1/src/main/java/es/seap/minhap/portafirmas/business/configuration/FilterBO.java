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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.RequestDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfAuthorizationFiltersDTO;
import es.seap.minhap.portafirmas.domain.PfAuthorizationTypesDTO;
import es.seap.minhap.portafirmas.domain.PfFiltersDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.envelope.FilterEnvelope;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class FilterBO {
	private Logger log = Logger.getLogger(FilterBO.class);

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private RequestDAO requestDAO;

	@Autowired
	private TagBO tagBO;

	@Autowired
	private ApplicationVO applicationVO;

	public List<AbstractBaseDTO> queryUserList() {
		return baseDAO.queryListMoreParameters("configuration.userAll", null);
	}
	/**
	 * Obtiene la lista de todos los jobs (CARGO) de la bbdd
	 * @return la lista de todos los cargos
	 */
	public List<AbstractBaseDTO> queryJobList() {
		return baseDAO.queryListMoreParameters("administration.jobsAll", null);
	}
	/**
	 * Obtiene la lista de todos los filtros envoltura
	 * @return la lista de filtros envoltura
	 * @see #queryJobList()
	 */
	public List<FilterEnvelope> queryJobListEnveloped() {
		List<AbstractBaseDTO> list = queryJobList();
		List<FilterEnvelope> envelopedList = new ArrayList<FilterEnvelope>();
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator
				.hasNext();) {
			//TODO:no se puede obtener un filtro de un usuario...mirar
			PfFiltersDTO filter = (PfFiltersDTO) iterator.next();
			envelopedList.add(new FilterEnvelope(filter));
		}
		return envelopedList;
	}
	/**
	 * Obtiene la lista de todos los filtros de la bbdd
	 * @return la lista de todos los filtros
	 */
	public List<AbstractBaseDTO> queryFilterList() {
		return baseDAO.queryListMoreParameters("configuration.filterAll", null);
	}
	/**
	 * Recupera la lista de jobs (CARGO) con la pk distinta a la pasada como par&aacute;metro
	 * @param pk la clave primaria
	 * @return la lista de jobs con la pk distinta a la pasada como par&aacute;metro
	 */
	public List<AbstractBaseDTO> getJobReceiversList(Long pk) {
		return baseDAO.queryListOneParameter(
				"administration.filterReceiverJobs", "pk", pk);
	}
	/**
	 * Recupera una lista de peticiones
	 * @param pfFiltersDTO el filtro
	 * @return la lista de peticiones
	 * @see es.seap.minhap.portafirmas.dao.RequestDAO#queryFilterRequests(PfFiltersDTO, PfRequestsDTO, String)
	 */
	public List<AbstractBaseDTO> queryRequestListAffected(
			PfFiltersDTO pfFiltersDTO) {
		return requestDAO.queryFilterRequests(pfFiltersDTO, null);
	}
	/**
	 * Recupera la autorizaci&oacute;n de usuario con el usuario autorizado pasado como par&aacute;metro y
	 * el tipo de autorizaci&oacute;n pasada como par&aacute;metro
	 * @param user el usuario
	 * @param userAuthorized el usuario autorizado
	 * @param type los tipos de autorizaci&oacute;n
	 * @return la autorizaci&oacute;n de usuario o nulo en caso de no encontrarla para los par&aacute;metros requeridos
	 */
	public PfUsersAuthorizationDTO queryUserAuthorization(PfUsersDTO user,
			PfUsersDTO userAuthorized, PfAuthorizationTypesDTO type) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user);
		parameters.put("userAuthorized", userAuthorized);
		parameters.put("type", type);
		return (PfUsersAuthorizationDTO) baseDAO.queryElementMoreParameters(
				"administration.userAuthorization", parameters);
	}
	/**
	 * Guarda o actualiza el filtro pasado como par&aacute;metro en bbdd, las autorizaciones de usuario
	 * para los receptores que pasamos como par&aacute;metro y la autorizaci&oacute;n de filtros para el usuario logado
	 * @param filter el filtro
	 * @param jobReceiversForFiltersList los receptores del filtro
	 * @param loggedUser el usuario logado
	 * @param application la aplicaci&oacute;n
	 * @param authorizationType el tipod e autorizaci&oacute;n
	 * @param jobFiltersList la lista de filtros
	 */
	@Transactional(readOnly = false)
	public void saveFilter(PfFiltersDTO filter,
			List<AbstractBaseDTO> jobReceiversForFiltersList,
			PfUsersDTO loggedUser, PfApplicationsDTO application,
			PfAuthorizationTypesDTO authorizationType,
			List<AbstractBaseDTO> jobFiltersList) {
		if (application.getPrimaryKey() != null) {
			filter.setPfApplication(application);
		}
		filter.setPfAuthorizationType(authorizationType);
		filter.setLvalid(true);
		filter.setCorder(Util.getInstance().getMaxCorder(jobFiltersList));
		baseDAO.insertOrUpdate(filter);
		for (Iterator<AbstractBaseDTO> iterator = jobReceiversForFiltersList
				.iterator(); iterator.hasNext();) {
			PfUsersDTO receiver = (PfUsersDTO) iterator.next();

			if (receiver.isSelected()) {
				// first we have to give an authorization to user if it doesnt
				// have it
				PfUsersAuthorizationDTO checkAut = queryUserAuthorization(
						filter.getPfUser(), receiver, filter.getPfAuthorizationType());
				if (checkAut == null) {
					checkAut = new PfUsersAuthorizationDTO();
					checkAut.setFauthorization(new Date());
					checkAut.setFrequest(new Date());
					checkAut.setPfAuthorizationType(filter
							.getPfAuthorizationType());
					checkAut.setFauthorization(new Date());
					checkAut.setPfUser(filter.getPfUser());
					checkAut.setPfAuthorizedUser(receiver);

					baseDAO.insertOrUpdate(checkAut);
				} else if (checkAut.getFrevocation() != null
						&& checkAut.getFrevocation().getTime() < System
								.currentTimeMillis()) {
					// if the authorization is finished we change it
					checkAut.setFrevocation(null);
					baseDAO.insertOrUpdate(checkAut);
				}
				PfAuthorizationFiltersDTO autFilter = new PfAuthorizationFiltersDTO();
				autFilter.setCcreated(loggedUser.getPrimaryKeyString());
				autFilter.setFcreated(new Date());
				autFilter.setCmodified(loggedUser.getPrimaryKeyString());
				autFilter.setFmodified(new Date());
				autFilter.setFstart(new Date());
				autFilter.setLvalid(true);
				autFilter.setPfFilter(filter);
				autFilter.setPfUsersAuthorization(checkAut);
				baseDAO.insertOrUpdate(autFilter);
				filter.getPfAuthorizationFilters().add(autFilter);
			}
		}

	}
	/**
	 * No hace nada, todo el m&eacute;todo est&aacute; comentado
	 * @param filter
	 * @param loggedUser
	 * @param deleteFilterAuthorizationList
	 * @param changeUser
	 */
	@Transactional(readOnly = false)
	public void saveEditFilter(PfFiltersDTO filter, PfUsersDTO loggedUser,
			List<AbstractBaseDTO> deleteFilterAuthorizationList,
			PfUsersDTO changeUser) {
		// if (!changeUser.getPrimaryKey().equals(
		// filter.getPfUser().getPrimaryKey())) {
		// filter.setPfUser(changeUser);
		// }
		// List<FilterEnvelope> listAux = this.getJobFiltersEnveloped(filter
		// .getPfUser());
		// filter.setCorder(Util.getInstance().getMaxCorder(listAux));
		// for (Iterator<AbstractBaseDTO> iterator =
		// deleteFilterAuthorizationList
		// .iterator(); iterator.hasNext();) {
		// PfAuthorizationFiltersDTO autFilter = (PfAuthorizationFiltersDTO)
		// iterator
		// .next();
		// baseDAO.delete(autFilter);
		// }
		// for (Iterator<PfAuthorizationFiltersDTO> iterator = filter
		// .getPfAuthorizationFilters().iterator(); iterator.hasNext();) {
		// PfAuthorizationFiltersDTO autFilter = (PfAuthorizationFiltersDTO)
		// iterator
		// .next();
		// if (autFilter.getPrimaryKey() == null) {
		//
		// autFilter.setCcreated(loggedUser.getPrimaryKeyString());
		// autFilter.setFcreated(new Date());
		// autFilter.setCmodified(loggedUser.getPrimaryKeyString());
		// autFilter.setFmodified(new Date());
		// autFilter.setFstart(new Date());
		// autFilter.setLvalid(true);
		//
		// baseDAO.insertOrUpdate(autFilter.getPfUsersAuthorization());
		// baseDAO.insertOrUpdate(autFilter);
		// }
		// }
		// baseDAO.insertOrUpdate(filter);
	}

	/**
	 * M&eacute;todo que carga los diferentes tipos de autorizaciones definidas en la BD.
	 * @return Listado de tipo de autorizaciones.
	 */
	@Transactional(readOnly=false)
	public List<AbstractBaseDTO> queryAuthorizationTypesList() {
		return baseDAO.queryListOneParameter("administration.authorizationTypes", null, null);
	}
	/**
	 * Recupera una lista de filtros activos de la petici&oacute;n pasada como par&aacute;metro
	 * @param req la petici&oacute;n actual
	 * @return lista de filtros
	 */
	public List<AbstractBaseDTO> queryFilterListByRequest(PfRequestsDTO req) {
		return baseDAO.queryListOneParameter("request.filtersByRequest", "req",	req);
	}
	/**
	 * Aplica los filtros relacionados con la petici&oacute;n actual
	 * @param request la petici&oacute;n actual
	 * @see #queryFilterListByRequest(PfRequestsDTO)
	 * @see es.seap.minhap.portafirmas.dao.RequestDAO#queryFilterRequests(PfFiltersDTO, PfRequestsDTO, String)
	 * @see #applyFilter(PfFiltersDTO, List)
	 */
	public void applyFilterOneRequest(PfRequestsDTO request) {
		//recupera la lista de filtros de la petici&oacute;n
		List<AbstractBaseDTO> filterList = queryFilterListByRequest(request);
		List<AbstractBaseDTO> requestList = new ArrayList<AbstractBaseDTO>();
		List<AbstractBaseDTO> requestListAux = null;
		requestList.add(request);
		for (AbstractBaseDTO filter : filterList) {
			//consultamos si hay peticiones para aplicar el filtro
			requestListAux = requestDAO.queryFilterRequests((PfFiltersDTO) filter, request);
			if (requestListAux != null && !requestList.isEmpty()) {
				applyFilter((PfFiltersDTO) filter, requestList);
			}
		}
	}
	/**
	 * Aplica el filtro a todas las peticiones pasadas en la lista
	 * @param filter el filtro
	 * @param affectedRequests lista de peticiones
	 * @see #applyFilter(PfFiltersDTO, List)
	 */
	@Transactional
	public void applyFilterAllRequests(PfFiltersDTO filter,
			List<AbstractBaseDTO> affectedRequests) {
		log.info("applyFilterAllRequests init");
		applyFilter(filter, affectedRequests);
		log.info("applyFilterAllRequests");
	}
	/**
	 * Aplica el filtro a una serie de peticiones pasadas como par&aacute;metro
	 * @param filter filtro
	 * @param affectedRequests lista de peticiones afectadas por el filtro
	 */
	@Transactional(readOnly = false)
	public void applyFilter(PfFiltersDTO filter, List<AbstractBaseDTO> affectedRequests) {
		log.info("applyFilter init: " + filter.getCfilter());

		// add signer and tag
		List<AbstractBaseDTO> sliList = null;
		PfSignersDTO signerDTO = null;
		PfRequestTagsDTO reqTag = null;
		PfTagsDTO tagNextUser = null;

		List<PfUsersDTO> receiverList = new ArrayList<PfUsersDTO>();
		//obtiene una lista de usuarios autorizados para el filtro
		for (PfAuthorizationFiltersDTO filtAuth : filter.getPfAuthorizationFilters()) {
			receiverList.add(filtAuth.getPfUsersAuthorization().getPfAuthorizedUser());
		}

		for (AbstractBaseDTO request : affectedRequests) {
			//Recupera un listado de lineas de firma relacionadas con la petici&oacute;n
			sliList = tagBO.querySignLineList((PfRequestsDTO) request);
			for (AbstractBaseDTO sli : sliList) {
				for (PfSignersDTO signer : ((PfSignLinesDTO) sli).getPfSigners()) {
					//si el firmante de la linea de firma es el mismo que el del filtro
					if (signer.getPfUser().getPrimaryKey().compareTo(
							filter.getPfUser().getPrimaryKey()) == 0) {
						//recorremos la lista de destinatarios
						for (PfUsersDTO receiver : receiverList) {
							//si el usuario receptor est&aacute; en el mapa de firmantes
							if (!existSignerSignLine(((PfSignLinesDTO) sli).getPfSigners(), receiver)) {
								//creamos un nuevo firmante
								signerDTO = new PfSignersDTO();
								signerDTO.setPfUser((PfUsersDTO) receiver);
								signerDTO.setPfSigner(signer);
								signerDTO.setPfSignLine(((PfSignLinesDTO) sli));

								// authorization filter
								Map<String, Object> parameters = new HashMap<String, Object>();
								parameters.put("user", filter.getPfUser());
								parameters.put("userAuthorized", (PfUsersDTO) receiver);
								parameters.put("type", filter.getPfAuthorizationType());
								parameters.put("filter", filter);
								PfAuthorizationFiltersDTO authFilter = (PfAuthorizationFiltersDTO) baseDAO
										.queryElementMoreParameters(
												"administration.filterAuthorization",
												parameters);

								signerDTO.setPfAuthorizationFilter(authFilter);
								//guarda el nuevo firmante en bbdd
								baseDAO.insertOrUpdate(signerDTO);

								//Recupera la etiqueta de petici&oacute;n de tipo 'ESTADO'
								reqTag = tagBO.queryStateUserSignLine((PfRequestsDTO) request, filter.getPfUser(), (PfSignLinesDTO) sli);
								//si la etiqueta es nueva o leido 
								if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_NEW) ||
									reqTag.getPfTag().getCtag().equals(Constants.C_TAG_READ)) {
									//recupera la etiqueta nueva
									tagNextUser = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
								// si no
								} else {
									//recupera la etiqueta en espera
									tagNextUser = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING);
								}

								tagBO.changeRequestTagUser(
										(PfRequestsDTO) request, reqTag,
										(PfSignLinesDTO) sli, tagNextUser,
										(PfUsersDTO) receiver);
								//cambia la hora de modificaci&oacute;n de la petici&oacute;n
								request.setFmodified(Calendar.getInstance().getTime());
								//actualiza la petici&oacute;n en bbdd
								baseDAO.update(request);

							}
						}
					}
				}
			}
		}

		log.info("applyFilter end");
	}
	/**
	 * Indica si un usuario esta o no en una lista de firmantes,
	 * true en caso afirmativo false en el contrario
	 * @param signerSet conjunto de firmantes
	 * @param user usuario
	 * @return si el usuario esta en la lista de firmantes
	 */
	public boolean existSignerSignLine(Set<PfSignersDTO> signerSet,
			PfUsersDTO user) {
		boolean exist = false;

		for (PfSignersDTO signer : signerSet) {
			if (signer.getPfUser().getPrimaryKey().compareTo(
					user.getPrimaryKey()) == 0) {
				exist = true;
				break;
			}
		}

		return exist;
	}
	/**
	 * Si tiene autorizaci&oacute;n borra el filtro de la lista de filtros del cargo pasada como par&aacute;metro
	 * @param jobFiltersList la lista de filtros del cargo
	 * @param filter el filtro
	 * @return true si se ha podido borrar el filtro, false en caso contrario
	 */
	// @Transactional
	// public boolean deleteFilter(PfFiltersDTO filter) {
	public boolean deleteFilter(List<AbstractBaseDTO> jobFiltersList,
			PfFiltersDTO filter) {
		boolean canBeDeleted = true;
		//Obtiene la posici&oacute;n
		int position = Util.getInstance().getPositionInList(jobFiltersList,
				filter);
		for (Iterator<PfAuthorizationFiltersDTO> iterator = filter
				.getPfAuthorizationFilters().iterator(); iterator.hasNext()
				&& canBeDeleted;) {
			PfAuthorizationFiltersDTO autFilter = iterator.next();
			if (autFilter.getPfSigners().size() > 0) {
				canBeDeleted = false;
			} else {
				// baseDAO.delete(autFilter);
				filter.getPfAuthorizationFilters().remove(autFilter);

			}
		}
		if (canBeDeleted) {
			// baseDAO.delete(filter);
			jobFiltersList.remove(position);
		} else {
			filter.setLvalid(false);
			// baseDAO.update(filter);
		}
		return canBeDeleted;
	}
	/**
	 * Rellena los datos del nuevo filtro para hacer un duplicado a partir del filtro
	 * pasado como par&aacute;metro, el nuevo filtro se marca como actualizado y se a&ntilde;ade a
	 * la lista de filtros
	 * @param filterList lista de filtros
	 * @param newFilter el nuevo filtro
	 * @param filter el filtro a duplicar
	 * @param loggedUser el usuario
	 */
	// @Transactional
	public void duplicateFilter(List<AbstractBaseDTO> filterList,
			PfFiltersDTO newFilter, PfFiltersDTO filter, PfUsersDTO loggedUser) {

		// we don't set the name of filter cause this value has to be a new
		// value
		// newFilter.setCfilter(filter.getCfilter());
		newFilter.setFend(filter.getFend());
		newFilter.setFstart(filter.getFstart());
		newFilter.setLvalid(filter.getLvalid());
		newFilter.setPfApplication(filter.getPfApplication());
		newFilter.setPfAuthorizationType(filter.getPfAuthorizationType());

		// new owner of the filter
		newFilter.setPfUser(filter.getPfUser());

		// we don't set the reason cause this value has to be a new value
		// newFilter.setTreason(filter.getTreason());

		newFilter.setTsubjectFilter(filter.getTsubjectFilter());

		// set order at max
		newFilter.setCorder(Util.getInstance().getMaxCorder(filterList));

		// now we have to check for autorization of users and filters
		for (Iterator<PfAuthorizationFiltersDTO> iterator = filter
				.getPfAuthorizationFilters().iterator(); iterator.hasNext();) {
			PfAuthorizationFiltersDTO autFilter = (PfAuthorizationFiltersDTO) iterator
					.next();
			PfAuthorizationFiltersDTO newAutFilter = new PfAuthorizationFiltersDTO();
			newAutFilter.setFcreated(new Date());
			newAutFilter.setCcreated(loggedUser.getPrimaryKeyString());
			newAutFilter.setFmodified(new Date());
			newAutFilter.setCmodified(loggedUser.getPrimaryKeyString());
			newAutFilter.setFend(autFilter.getFend());
			newAutFilter.setFstart(autFilter.getFstart());
			newAutFilter.setLvalid(autFilter.getLvalid());
			newAutFilter.setPfFilter(newFilter);
			newAutFilter.setPfSigners(autFilter.getPfSigners());
			newAutFilter.setPfUsersAuthorization(autFilter
					.getPfUsersAuthorization());
			// baseDAO.insertOrUpdate(newAutFilter);

			// added to avoid reloads problem
			newFilter.getPfAuthorizationFilters().add(newAutFilter);

			PfUsersAuthorizationDTO newUserAut = queryUserAuthorization(filter
					.getPfUser(), autFilter.getPfUsersAuthorization()
					.getPfAuthorizedUser(), autFilter.getPfUsersAuthorization()
					.getPfAuthorizationType());
			if (newUserAut == null) {
				newUserAut = new PfUsersAuthorizationDTO();
				newUserAut.setFrequest(new Date());
				newUserAut.setPfUser(filter.getPfUser());
				newUserAut.setPfAuthorizedUser(autFilter
						.getPfUsersAuthorization().getPfAuthorizedUser());
				newUserAut.setPfAuthorizationType(autFilter
						.getPfUsersAuthorization().getPfAuthorizationType());

				// baseDAO.insertOrUpdate(newUserAut);
			}
		}
		newFilter.setUpdated(true);
		filterList.add(filterList.size(), newFilter);
	}
	/**
	 * Crea una lista de FilterEnvelope a partir de la lista de filtros
	 * pasada como par&aacute;mettro
	 * @param jobFiltersList lista de filtros
	 * @return lista de filtros de tipo FilterEnvelope
	 */
	public List<FilterEnvelope> getJobFiltersEnveloped(
			List<AbstractBaseDTO> jobFiltersList) {
		List<FilterEnvelope> filterList = new ArrayList<FilterEnvelope>();
		for (Iterator<AbstractBaseDTO> iterator = jobFiltersList.iterator(); iterator
				.hasNext();) {
			PfFiltersDTO filter = (PfFiltersDTO) iterator.next();
			filterList.add(new FilterEnvelope(filter));
		}
		return filterList;
	}
	/**
	 * Recupera la lista con todas las aplicaciones que hay en bbdd
	 * @return la lista con todas las aplicaciones
	 */
	public List<AbstractBaseDTO> queryApplicationList() {
		return baseDAO.queryListMoreParameters("administration.applicationAll",
				null);
	}
	/**
	 * Guarda/actualiza los filtros de la lista que pasamos como par&aacute;metro
	 * @param jobsFiltersList la lista de envoltura de filtros
	 */
	@Transactional(readOnly = false)
	public void saveFilterListEnveloped(List<FilterEnvelope> jobsFiltersList) {
		int count = 0;
		for (Iterator<FilterEnvelope> iterator = jobsFiltersList.iterator(); iterator
				.hasNext();) {
			PfFiltersDTO filter = ((FilterEnvelope) iterator.next()).getFilterDTO();
			if (filter.getCorder() == null
					|| filter.getCorder().intValue() != count) {
				filter.setCorder(new Long(count));
				//guarda o actualiza el filtro
				baseDAO.insertOrUpdate(filter);
			}
			count++;
		}

	}
	/**
	 * Devuelve los cargos disponibles para el filtro dentro de la lista pasada como par&aacute;metro
	 * @param jobList la lista de cargos
	 * @param filter el filtro
	 * @return la lista de cargos disponibles para el filtro
	 */
	public List<AbstractBaseDTO> getAvailableJobsForFilter(
			List<AbstractBaseDTO> jobList, PfFiltersDTO filter) {

		List<AbstractBaseDTO> result = new ArrayList<AbstractBaseDTO>();
		for (Iterator<AbstractBaseDTO> iterator = jobList.iterator(); iterator
				.hasNext();) {
			PfUsersDTO job = (PfUsersDTO) iterator.next();
			if (!job.getPrimaryKeyString().equals(
					filter.getPfUser().getPrimaryKeyString())) {
				boolean found = false;
				for (Iterator<PfAuthorizationFiltersDTO> iterator2 = filter
						.getPfAuthorizationFiltersList().iterator(); iterator2
						.hasNext()
						&& !found;) {
					PfAuthorizationFiltersDTO autFilter = (PfAuthorizationFiltersDTO) iterator2
							.next();
					if (autFilter.getPfUsersAuthorization()
							.getPfAuthorizedUser().getPrimaryKeyString()
							.equals(job.getPrimaryKeyString())) {
						found = true;
					}
				}
				if (!found) {
					result.add(job);
				}
			}
		}
		return result;
	}
	/**
	 * Comprueba si alg&uacute;n elemento de la lista que pasamos como par&aacute;metro est&aacute; seleccionado
	 * @param jobReceiversForFiltersList la lista de jobs
	 * @return true si alg&uacute;n elemento de la lista est&aacute; seleccionado, false en caso contrario
	 * @see es.seap.minhap.portafirmas.utils.Util#anySelected(List)
	 */
	public boolean checkReceivers(
			List<AbstractBaseDTO> jobReceiversForFiltersList) {
		if (Util.getInstance().anySelected(jobReceiversForFiltersList) != null) {
			return true;
		}
		return false;
	}
	/**
	 * Verifica que la fecha de fin del filtro no sea anterior a la fecha de inicio
	 * @param filter el filtro
	 * @return true si la fecha de fin del filtro es posterior a la fecha de inicio, false en caso contrario
	 */
	public boolean checkFilterDates(PfFiltersDTO filter) {
		if (filter.getFstart() != null && filter.getFend() != null
				&& !filter.getFstart().before(filter.getFend())) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Verifica que la fecha de fin del filtro no sea anterior a la fecha actual
	 * @param filter el filtro
	 * @return true si la fecha de fin del filtro es posterior a la actual, false en caso contrario
	 */
	public boolean checkFilterCurrentDate(PfFiltersDTO filter) {
		if (filter.getFend() == null || filter.getFend().after(new Date())) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * A&ntilde;ade a la lista de filtros autorizaci&oacute;n los que corresponden con los cargos seleccionados
	 * en la lista de cargos receptores
	 * @param editFilter el filtro
	 * @param jobReceiversForEditingFilter la lista de cargos receptores del filtro
	 * @param authorizationFiltersList la lista de filtros autorizaci&oacute;n
	 * @return la lista de filtros autorizaci&oacute;n con el filtro autorizaci&oacute;n con los filtros autorizaci&oacute;n a&ntilde;adidos
	 * @see #queryUserAuthorization(PfUsersDTO, PfUsersDTO, PfAuthorizationTypesDTO)
	 */
	public List<PfAuthorizationFiltersDTO> addReceivers(
			PfFiltersDTO editFilter,
			List<AbstractBaseDTO> jobReceiversForEditingFilter,
			List<PfAuthorizationFiltersDTO> authorizationFiltersList) {
		for (Iterator<AbstractBaseDTO> iterator = jobReceiversForEditingFilter
				.iterator(); iterator.hasNext();) {
			PfUsersDTO newReceiver = (PfUsersDTO) iterator.next();
			if (newReceiver.isSelected()) {
				// we need a new authorization from an user to another
				PfUsersAuthorizationDTO checkAut = this.queryUserAuthorization(
						editFilter.getPfUser(), newReceiver, editFilter
								.getPfAuthorizationType());
				if (checkAut == null) {
					checkAut = new PfUsersAuthorizationDTO();
					checkAut.setFauthorization(new Date());
					checkAut.setFrequest(new Date());
					checkAut.setPfAuthorizationType(editFilter
							.getPfAuthorizationType());
					checkAut.setFauthorization(new Date());
					checkAut.setPfUser(editFilter.getPfUser());
					checkAut.setPfAuthorizedUser(newReceiver);

				}
				// we need a new authorization for the user with filter
				PfAuthorizationFiltersDTO newAuthorizationFiltersDTO = new PfAuthorizationFiltersDTO();

				newAuthorizationFiltersDTO.setPfFilter(editFilter);
				newAuthorizationFiltersDTO.setPfUsersAuthorization(checkAut);
				editFilter.getPfAuthorizationFilters().add(
						newAuthorizationFiltersDTO);
				authorizationFiltersList.add(newAuthorizationFiltersDTO);
			}
		}
		return authorizationFiltersList;
	}
	/**
	 * Si el filtro autorizaci&oacute;n no tiene firmantes, borra el filtro autorizaci&oacute;n de la lista de filtros autorizaci&oacute;n y
	 * lo a&ntilde;ade a la lista de filtros autorizaci&oacute;n borrados, para ser eliminado posteriormente de la bbdd, tambi&eacute;n lo borra
	 * de la relaci&oacute;n de filtro autorizaci&oacute;n del filtro pasado como par&aacute;metro
	 * @param editFilter el filtro
	 * @param authorizationFilter el filtro autorizaci&oacute;n
	 * @param authorizationFiltersList la lista de filtros autorizaci&oacute;n
	 * @param deleteFilterAuthorizationList la lista de filtros autorizaci&oacute;n borrados
	 * @return true si se ha borrado el filtro de la lista de filtros autorizaci&oacute;n, false en caso contrario
	 */
	public boolean removeFilterReceiver(PfFiltersDTO editFilter,
			PfAuthorizationFiltersDTO authorizationFilter,
			List<PfAuthorizationFiltersDTO> authorizationFiltersList,
			List<AbstractBaseDTO> deleteFilterAuthorizationList) {
		if (authorizationFilter.getPfSigners().size() == 0) {
			editFilter.getPfAuthorizationFilters().remove(authorizationFilter);
			authorizationFiltersList.remove(authorizationFilter);
			if (authorizationFilter.getPrimaryKey() != null) {
				deleteFilterAuthorizationList.add(authorizationFilter);
			}
			return true;
		}
		return false;
	}
	/**
	 * Recupera la lista de filtros asociados al job que pasamos como par&aacute;metro
	 * @param job el job
	 * @return la lista de filtros del job pasado como par&aacute;metro
	 */
	public List<AbstractBaseDTO> getJobFilters(PfUsersDTO job) {
		return baseDAO.queryListOneParameter("administration.jobsFilter",
				"job", job);
	}
	/**
	 * Guarda/actualiza la lista de filtros que pasamos como par&aacute;metro
	 * @param jobFiltersList la lista de filtros
	 */
	@Transactional(readOnly = false)
	public void saveFilterList(List<AbstractBaseDTO> jobFiltersList) {
		baseDAO.insertOrUpdateList(jobFiltersList);
	}

}
