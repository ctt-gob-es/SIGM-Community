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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersGroupsDTO;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.envelope.GroupEnvelope;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ErrorsAndWarnings;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class GroupBO {

	
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private ProvinceBO provinceBO;

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Resource(name = "messageProperties")
	private Properties messages;

	public List<AbstractBaseDTO> getAllGroups() {
		return baseDAO.queryListOneParameter("request.allGroups", null, null); 
	}

	/**
	 * Método que comprueba si un grupo tiene referencias a usuarios
	 * @param group Grupo
	 * @return True si tiene referencias, False en caso contrario
	 */
	public boolean checkGroupReferences(PfGroupsDTO group) {
		List<AbstractBaseDTO> userReferences =
			baseDAO.queryListOneParameter("request.usersFromGroup", "group", group);
		if (userReferences.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Método que devuelve la lista de grupos a los que pertenece un usuario
	 * @param user Usuario
	 * @return Lista de grupos
	 */
	public List<AbstractBaseDTO> getGroupsFromUser(PfUsersDTO user) {
		List<AbstractBaseDTO> groups =
			baseDAO.queryListOneParameter("request.groupsFromUser", "user", user);
		return groups;
	}

	/**
	 * Método que devuelve todos los usuarios que pertenecen a un grupo
	 * @param group Grupo
	 * @return Lista de usuarios
	 */
	public List<AbstractBaseDTO> getUsersGroup(PfGroupsDTO group) {
		List<AbstractBaseDTO> users =
			baseDAO.queryListOneParameter("request.usersFromGroup", "group", group);
		return users;
	}

	public List<AbstractBaseDTO> getUsersGroupList(PfGroupsDTO group) {
		List<AbstractBaseDTO> users =
			baseDAO.queryListOneParameter("request.usersGroup", "group", group);
		return users;
	}
	/**
	 * Método que devuelve la lista de grupos que están asociados a una sede
	 * @param codProvincia Sede
	 * @return Listado de grupos
	 */
	public List<AbstractBaseDTO> getAllGroupsByProvince(String codProvincia) {
		List<AbstractBaseDTO> groups =
			baseDAO.queryListOneParameter("request.groupsBySeat", "seatCod", codProvincia);
		return groups;
	}
	
	/**
	 * Método que convierte un groupEnvelope en un PfGroupDTO
	 * @param groupEnvelope envoltorio de grupo
	 * @return DTO de grupo
	 */
	public PfGroupsDTO toGroupDTO(GroupEnvelope groupEnvelope) {
		PfGroupsDTO groupDTO = (PfGroupsDTO) baseDAO.queryElementOneParameter("request.groupByPk", "pk", groupEnvelope.getPk()); 
		return groupDTO;
	}

	/**
	 * Método que obtiene la relación entre un usuario y un grupo
	 * @param user Usuario
	 * @param group grupo
	 * @return Relación entre user y group
	 */
	public PfUsersGroupsDTO getUserGroup(PfUsersDTO user, PfGroupsDTO group) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user);
		parameters.put("group", group);
		PfUsersGroupsDTO userGroup = (PfUsersGroupsDTO) baseDAO.queryElementMoreParameters("request.userGroup", parameters); 
		return userGroup;
	}

	public PfUsersGroupsDTO getUserGroupById(Long id) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pk", id);
		return (PfUsersGroupsDTO) baseDAO.queryElementMoreParameters("request.userGroupByPk", parameters); 
	}

	/**
	 * Método que filtra una lista de grupos
	 * @param filter Filtro de grupos
	 * @param user Usuario
	 * @return Listado filtrado de grupos
	 */
	public List<GroupEnvelope> filterGroups(String filter, List<GroupEnvelope> selectedGroups) {
		List<AbstractBaseDTO> groupList = baseDAO.queryListOneParameter("request.filterGroups", "filter", "%" + filter + "%");
		List<GroupEnvelope> filteredGroups = Util.getInstance().toGroupEnvelopeList(groupList);
		List<GroupEnvelope> groups = new ArrayList<GroupEnvelope>();
		for (GroupEnvelope group : filteredGroups) {
			if (!selectedGroups.contains(group)) {
				groups.add(group);
			}
		}
		return groups;
	}

	/**
	 * Método que comprueba si el nombre de un grupo existe en una lista de grupos.
	 * @param groupList Listado de grupos.
	 * @param group Grupo a buscar.
	 * @param indexInList Posición del grupo en la lista.
	 * @return "True" si el grupo existe, "false" en caso contrario.
	 */
	public boolean existsGroupName(List<AbstractBaseDTO> groupList, PfGroupsDTO group, int indexInList) {
		boolean ret = false;
		PfGroupsDTO auxGroup;
		if (groupList != null && !groupList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = groupList.iterator();
			while (it.hasNext() && ret == false) {
				auxGroup = (PfGroupsDTO) it.next();
				// Not the same row and different code
				if (group.getCnombre().toUpperCase().equals(
						auxGroup.getCnombre().toUpperCase())
						&& !group.getPrimaryKeyString().equals(
								auxGroup.getPrimaryKeyString())) {
					ret = true;
				}
				if (auxGroup.getCnombre().toUpperCase().equals(
						group.getCnombre().toUpperCase())
						&& auxGroup.getPrimaryKey() == null
						&& group.getPrimaryKey() == null && !auxGroup.equals(group)) {
					if (indexInList <= -1
							&& countGroupNameInList(auxGroup, groupList) > 0) {
						// this case is when we add a new group
						ret = true;
					} else if (countGroupNameInList(auxGroup, groupList) > 1) {
						// this case is when we are modifing a group
						ret = true;

					}
				}
			}
		}
		return ret;
	}

	/**
	 * Método que copia un grupo
	 * @param group Grupo a copiar
	 * @return Copia del grupo
	 */
	public PfGroupsDTO getGroupCopy(PfGroupsDTO group) {
		PfGroupsDTO groupCopy = new PfGroupsDTO();

		groupCopy.setCcreated(group.getCcreated());
		groupCopy.setCdescripcion(group.getCdescripcion());
		groupCopy.setCmodified(group.getCmodified());
		groupCopy.setCnombre(group.getCnombre());
		groupCopy.setFcreated(group.getFcreated());
		groupCopy.setFmodified(group.getFmodified());
		groupCopy.setPfUsersGroups(group.getPfUsersGroups());
		groupCopy.setPrimaryKey(group.getPrimaryKey());
		groupCopy.setPrimaryKeyString(group.getPrimaryKeyString());
		groupCopy.setSelected(group.isSelected());
		groupCopy.setUpdated(group.isUpdated());
		groupCopy.setPfProvince(group.getPfProvince());

		return groupCopy;
	}

	/**
	 * Método que calcula el número de veces que se repite el nombre de un grupo dentro de una lista.
	 * @param group Grupo a buscar.
	 * @param list Listado de grupos
	 * @return Número de repeticiones del nombre del grupo en la lista.
	 */
	private int countGroupNameInList(PfGroupsDTO group, List<AbstractBaseDTO> list) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator.hasNext();) {
			PfGroupsDTO pfGroupsDTO = (PfGroupsDTO) iterator.next();
			if (pfGroupsDTO.getCnombre() != null
					&& pfGroupsDTO.getCnombre().toUpperCase().equals(
							group.getCnombre().toUpperCase())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Método que devuelve el listado de provincias
	 * que administra un usuario
	 * @param user Usuario
	 * @return Listado de provincias
	 */	
	public List<AbstractBaseDTO>  getProvinceByUser(PfUsersDTO user) {
		return baseDAO.queryListOneParameter("request.queryAdminProvinces", "usuario", user);
	}

	@Transactional(readOnly=false)
	public PfGroupsDTO findGroupByIdTrans(long id) {
		return findGroupById (id);
	}
	
	public PfGroupsDTO findGroupById(long id) {
		PfGroupsDTO resultado = (PfGroupsDTO) baseDAO.queryElementOneParameter("administration.findGroupById", "id", id);
		return resultado;
	}

	@Transactional(readOnly = false)
	public void editGroup(PfGroupsDTO grupo) {
		baseDAO.update(grupo);
	}

	@Transactional(readOnly = false)
	public void delete(PfGroupsDTO grupo) {
		baseDAO.delete(grupo);
	}

	public AbstractBaseDTO findUserGroup(PfUsersDTO user, PfGroupsDTO group) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user);
		parameters.put("grupo", group);
		return baseDAO.queryElementMoreParameters("administration.findUserGroup", parameters);
		
	}

	@Transactional(readOnly = false)
	public void insertUserGroup(PfUsersGroupsDTO userGroup) {
		baseDAO.insertOrUpdate(userGroup);
	}
	
	@Transactional(readOnly = false)
	public void deleteUserGroup(PfUsersGroupsDTO removeUsersGroup) {
		baseDAO.delete(removeUsersGroup);
	}

	public List<AbstractBaseDTO> getGroups(PfUsersDTO userDTO) {
		List<AbstractBaseDTO> listGroup = new ArrayList<AbstractBaseDTO>();
		if(userAdmBO.isAdministrator(userDTO.getPfUsersProfiles())) {
			// Si es administrador
			listGroup = getAllGroups();
		} else if(userAdmBO.isAdminSeat(userDTO.getPfUsersProfiles())){
			// Si es administrador de sede 
			List<AbstractBaseDTO> provinceList = provinceBO.getAdminProvinces(userDTO);
			for (AbstractBaseDTO province : provinceList) {
				String idProvince = ((PfProvinceDTO) province).getCcodigoprovincia(); 
				listGroup.addAll(getAllGroupsByProvince(idProvince));
			}
		}
		Collections.sort(listGroup, new GroupComparator());
		return listGroup;
	}

	/**
	 * Validación de las reglas de negocio para grupo
	 * @param groupDTO
	 * @param errors
	 */
	public void validate(PfGroupsDTO groupDTO, ErrorsAndWarnings errorsAndWarnings) {

		Map<String, Object> paramQuery = new HashMap<String, Object>();
		
		paramQuery.put("code", groupDTO.getCnombre());
		paramQuery.put("idProvincia", groupDTO.getPfProvince().getPrimaryKey());

		List<AbstractBaseDTO> groupBD_DTO = baseDAO.queryListMoreParameters("administration.validateFullGroupCode", paramQuery);
		List<PfGroupsDTO> listaGrupos = new ArrayList<PfGroupsDTO>();
		for (Iterator<AbstractBaseDTO> it = groupBD_DTO.iterator(); it.hasNext();) {
			PfGroupsDTO grupo = (PfGroupsDTO)it.next();
			listaGrupos.add(grupo);
		}
		
		// Si el código ya existe en base de datos..
		if (!listaGrupos.isEmpty()) {
			// Se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando
			List<Long> ids = new ArrayList<Long>();
			
			for(PfGroupsDTO pg: listaGrupos){
				ids.add(pg.getPrimaryKey());
			}
			
			if  (groupDTO.getPrimaryKey() == null
					|| ( !(ids.contains(groupDTO.getPrimaryKey()) && ids.size()==1))) {
				errorsAndWarnings.getErrors().add(String.format(messages.getProperty("existsCodeGroup"), groupDTO.getCnombre(), groupDTO.getPfProvince().getCnombre()));
			}
		}
		
		List<PfGroupsDTO> groupsBD_DTO = null;
		
		groupsBD_DTO = baseDAO.queryListOneParameter("administration.validateFullGroupDes", "description", groupDTO.getCdescripcion());
		// Si la descripción ya existe en base de datos..
		if (groupsBD_DTO != null && !groupsBD_DTO.isEmpty()) {
			// Se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando
			
			List<Long> ids = new ArrayList<Long>();
			
			for(PfGroupsDTO pg: groupsBD_DTO){
				ids.add(pg.getPrimaryKey());
			}
			
			if  (groupDTO.getPrimaryKey() == null
					|| ( !(ids.contains(groupDTO.getPrimaryKey()) && ids.size()==1) ) ) {
				errorsAndWarnings.getWarnings().add(String.format(messages.getProperty("existsGroupDescription"), groupDTO.getCdescripcion()));
			}
		}

		
	}

	/**
	 * Validacion de la reglas de negocio para el borrado del grupo
	 * @param group
	 * @param errors
	 */
	public void validateDelete(PfGroupsDTO group, ArrayList<String> errors) {
		List<AbstractBaseDTO> users =  baseDAO.queryListOneParameter("administration.findUserRemitter", "grupo", group); 
		if (!users.isEmpty()){
			errors.add(messages.getProperty("errorGroupHasRequest"));
		} 
		List<AbstractBaseDTO> groupUsers = this.getUsersGroup(group);
		if (!groupUsers.isEmpty()){
			errors.add(messages.getProperty("errorGroupHasUser"));
		}
	}
	
	@Transactional(readOnly = false)
	public void save(PfGroupsDTO groupDTO) {
		baseDAO.insertOrUpdate(groupDTO);
	}

	@Transactional(readOnly = false)
	public void saveUserGroup(PfUsersGroupsDTO pfUsersGroupsDTO) {
		baseDAO.insertOrUpdate(pfUsersGroupsDTO);
	}

	/**
	 * @author domingo
	 *
	 */
	public class GroupComparator implements Comparator<AbstractBaseDTO> {
		@Override
		public int compare(AbstractBaseDTO group1, AbstractBaseDTO group2) {
			return ((PfGroupsDTO) group1).getCnombre()
				.compareToIgnoreCase(((PfGroupsDTO) group2).getCnombre());
		}
		
	}

}
