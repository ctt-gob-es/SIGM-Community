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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfOrganismoDIR3DTO;
import es.seap.minhap.portafirmas.domain.PfProvinceAdminDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ProvinceBO {
	
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private OrganismBO organismBO;
	
	@Autowired
	private OrganismoDIR3BO organismoDIR3BO;
	
	/**
	 * Método que obtiene la provincia de un usuario
	 * @param user Usuario
	 * @return Provincia del usuario
	 */
	public PfProvinceDTO getUserProvince(PfUsersDTO user) {
		PfProvinceDTO province = null;
		PfUsersDTO userWithProvince = (PfUsersDTO) baseDAO.queryElementOneParameter("request.queryUserProvince", "user", user);
		if (userWithProvince != null && userWithProvince.getPfProvince() != null) {
			province = userWithProvince.getPfProvince();
		}
		return province;
	}

	/**
	 * Método que obtiene una provincia a partir de su código
	 * @param cod Código de provincia
	 * @return Provincia
	 */
	public PfProvinceDTO getProvinceByCod(String cod) {
		PfProvinceDTO province = (PfProvinceDTO)
			baseDAO.queryElementOneParameter("request.queryProvinceByCode", "cod", cod);
		return province;
	}

	/**
	 * Obtiene la lista de sedes en función del perfil del administrador
	 * @param userDTO
	 * @return
	 */
	public List<AbstractBaseDTO> getSeatList(PfUsersDTO userDTO) {
		List<AbstractBaseDTO> provinceList = null;
		// Se filtran los usuarios en base a la búsqueda
		if(userAdmBO.isAdministrator(userDTO.getPfUsersProfiles()) || userAdmBO.isAdminCAID(userDTO.getPfUsersProfiles())) {
			provinceList = this.getAllProvinces();
		} else {
			provinceList = this.getAdminProvinces(userDTO);
		}
		return provinceList;
	}

	/**
	 * Método que devuelve todas las provincias disponibles en BBDD
	 * @return Listado de provincias disponibles
	 */
	public List<AbstractBaseDTO> getAllProvinces() {
		List<AbstractBaseDTO> provinces = 
			baseDAO.queryListOneParameterList("request.queryAllProvinces", null, null);
		return provinces;
	}
	
	/**
	 * Método que devuelve todas las provincias visibles para una provincia.
	 * Serán las provincias en que exista algún usuario público más la pasada como parámetro.
	 * @param province 
	 * @return Listado de provincias disponibles
	 */
	public List<AbstractBaseDTO> getVisibleProvinces(String codProvincia) {
		List<AbstractBaseDTO> provinces = 
			baseDAO.queryListOneParameter("request.queryVisibleProvinces", "province", codProvincia);
		return provinces;
	}

	/**
	 * Método que devuelve el listado de provincias que administra un usuario
	 * @param user Usuario
	 * @return Listado de provincias
	 */
	public List<AbstractBaseDTO> getAdminProvinces(PfUsersDTO user) {
		List<AbstractBaseDTO> provinces =
			baseDAO.queryListOneParameter("request.queryAdminProvinces", "usuario", user);
		return provinces;
	}
	
	/**
	 * Método que devuelve el listado de provincias que administra un usuario de organismo
	 * @param user Usuario
	 * @return Listado de provincias
	 */
	public List<AbstractBaseDTO> getAdminProvincesOrganism(List<PfUnidadOrganizacionalDTO> organismos) {
		
		Object[] pkList = new Object[organismos.size()];
		//Recuperamos las cadenas con los c&oacute;digos de las peticiones
		for (int i = 0; i < organismos.size(); i++) {
			pkList[i] = new Long(((PfUnidadOrganizacionalDTO) organismos.get(i)).getPrimaryKey());
		}
		//Construimos la query
		Map<String, Object[]> parametersList = new HashMap<String, Object[]>();
		parametersList.put("organList", pkList);
		
		List<AbstractBaseDTO> provinces =
			baseDAO.queryListMoreParametersListComplex("request.queryAdminProvincesOrganism", parametersList, null);
		return provinces;
	}
	
	/**
	 * M&eacute;todo que guarda en BD los cambios realizados sobre las listas de sedes a añaadir y a eliminar.
	 * @param jobList Listado de sedes a añadir
	 * @param jobDeleteList Listado de sedes a eliminar.
	 */
	@Transactional(readOnly = false)
	public void saveSeatList(List<AbstractBaseDTO> seatList, List<AbstractBaseDTO> seatDeleteList) {
		baseDAO.updateList(seatList);
		baseDAO.deleteList(seatDeleteList);
	}

	/**
	 * Método que obtiene todas las provincias que se le pueden asociar a un usuario
	 * @param user Usuario
	 * @return Listado de provincias
	 */
	public List<PfProvinceDTO> getSelectableProvinces(List<AbstractBaseDTO> provinceList, 
													  List<AbstractBaseDTO> userProvinceList) {
		List<AbstractBaseDTO> selectableProvinces = new ArrayList<AbstractBaseDTO>();
		selectableProvinces.addAll(provinceList);

		for (AbstractBaseDTO p : provinceList) {
			PfProvinceDTO province = (PfProvinceDTO) p;
			for (AbstractBaseDTO up : userProvinceList) {
				PfProvinceAdminDTO userProvince = (PfProvinceAdminDTO) up;
				if (userProvince.getPfProvince().getCcodigoprovincia().equals(province.getCcodigoprovincia())) {
					selectableProvinces.remove(province);
				}
			}
		}
		
		return convertirAProvincia(selectableProvinces);
	}

	/**
	 * Método que comprueba si una sede tiene referencias a usuarios
	 * @param seat Sede
	 * @return True si tiene referencias, False en caso contrario
	 */
	public boolean checkSeatReferences(PfProvinceDTO seat) {
		List<AbstractBaseDTO> userReferences =
			baseDAO.queryListOneParameter("request.usersFromProvince", "seat", seat);
		List<AbstractBaseDTO> adminReferences =
			baseDAO.queryListOneParameter("request.provinceAdminFromProvince", "seat", seat);
		if (userReferences.isEmpty() && adminReferences.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Método que comprueba si una sede tiene referencias a usuarios
	 * @param seat Sede
	 * @return True si tiene referencias, False en caso contrario
	 */
	public boolean checkSeatReferences(PfProvinceDTO seat, PfUsersDTO usuario, PfProvinceAdminDTO adminDelete) {
		List<AbstractBaseDTO> userReferences =
			baseDAO.queryListOneParameter("request.usersFromProvince", "seat", seat);
		List<AbstractBaseDTO> adminReferences =
			baseDAO.queryListOneParameter("request.provinceAdminFromProvince", "seat", seat);
		if (userReferences.isEmpty() && adminReferences.isEmpty()) {
			return false;
		} else {
			if(userReferences.isEmpty() && adminReferences.size() == 1){
				if(usuario!=null && usuario.getPrimaryKey()!=null &&
						usuario.getPrimaryKey().equals(((PfProvinceAdminDTO)adminReferences.get(0)).getPfUser().getPrimaryKey())){
					adminDelete.setPrimaryKey(((PfProvinceAdminDTO)adminReferences.get(0)).getPrimaryKey());
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Método que comprueba si una sede existe en una lista de sedes.
	 * @param sedeList Listado de sedes.
	 * @param seat Sede a buscar.
	 * @param indexInList Posición de la sede en la lista.
	 * @return "True" si la sede existe, "false" en caso contrario.
	 */
	public boolean existsSeatCode(List<AbstractBaseDTO> seatList, PfProvinceDTO seat, int indexInList) {
		boolean ret = false;
		PfProvinceDTO auxSeat;
		if (seatList != null && !seatList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = seatList.iterator();
			while (it.hasNext() && ret == false) {
				auxSeat = (PfProvinceDTO) it.next();
				// Not the same row and different code
				if (seat.getCcodigoprovincia().toUpperCase().equals(
						auxSeat.getCcodigoprovincia().toUpperCase())
						&& !seat.getPrimaryKeyString().equals(
								auxSeat.getPrimaryKeyString())) {
					ret = true;
				}
				if (auxSeat.getCcodigoprovincia().toUpperCase().equals(
						seat.getCcodigoprovincia().toUpperCase())
						&& auxSeat.getPrimaryKey() == null
						&& seat.getPrimaryKey() == null && !auxSeat.equals(seat)) {
					if (indexInList <= -1
							&& countCSeatInList(auxSeat, seatList) > 0) {
						// this case is when we add a new seat
						ret = true;
					} else if (countCSeatInList(auxSeat, seatList) > 1) {
						// this case is when we are modifing a seat
						ret = true;

					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * Comprueba si existe el código de la sede que se intenta modificar o dar de alta
	 * @param seat
	 * @return
	 */
	public boolean existsSeatCode(PfProvinceDTO seat) {
		boolean exists = false;
		List<AbstractBaseDTO> provincesList = this.getAllProvinces();
		// Se quita la que se modifica, en el alta la lista quedará igual.
		if(seat.getPrimaryKey() != null) {
			provincesList.remove(seat);
		}
		// Se recorre la lista buscando si el código se repite
		for (Iterator<AbstractBaseDTO> iterator = provincesList.iterator(); iterator.hasNext();) {
			PfProvinceDTO provinceDTO = (PfProvinceDTO) iterator.next();
			if(       seat.getCcodigoprovincia().toUpperCase().equals(
			   provinceDTO.getCcodigoprovincia().toUpperCase())) {
				exists = true;
			}
		}
		return exists;
	}
	
	/**
	 * Comprueba si existe el código de la sede que se intenta modificar o dar de alta
	 * @param seat
	 * @return
	 */
	public boolean existsSeatCodeDIR3(PfProvinceDTO seat) {
		boolean exists = false;
		PfOrganismoDIR3DTO organismoDIR3 = organismoDIR3BO.getOrganismoDIR3ByCode(seat.getCcodigoprovincia());
		if (organismoDIR3 !=null ) {
			exists = true;
		}
		return exists;
	}

	/**
	 * Comprueba si existe el nombre de la sede que se intenta modificar o dar de alta
	 * @param seat
	 * @return
	 */
	public boolean existsSeatName(PfProvinceDTO seat) {
		boolean exists = false;
		List<AbstractBaseDTO> provincesList = this.getAllProvinces();
		// Se quita la que se modifica, en el alta la lista quedará igual.
		if(seat.getPrimaryKey() != null) {
			provincesList.remove(seat);
		}
		// Se recorre la lista buscando si el nombre se repite
		for (Iterator<AbstractBaseDTO> iterator = provincesList.iterator(); iterator.hasNext();) {
			PfProvinceDTO provinceDTO = (PfProvinceDTO) iterator.next();
			if(       seat.getCnombre().toUpperCase().equals(
			   provinceDTO.getCnombre().toUpperCase())) {
				exists = true;
			}
		}
		
		return exists;
	}

	/**
	 * M&eacute;todo que calcula el n&uacute;mero de veces que se repite un cargo dentro de una lista.
	 * @param job Cargo a buscar.
	 * @param list Listado de cargos.
	 * @return N&uacute;mero de repeticiones del cargo en la lista.
	 */
	private int countCSeatInList(PfProvinceDTO seat, List<AbstractBaseDTO> list) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator.hasNext();) {
			PfProvinceDTO pfProvinceDTO = (PfProvinceDTO) iterator.next();
			if (pfProvinceDTO.getCcodigoprovincia() != null
					&& pfProvinceDTO.getCcodigoprovincia().toUpperCase().equals(
							seat.getCcodigoprovincia().toUpperCase())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Método que comprueba si el nombre de una sede existe en una lista de sedes.
	 * @param seatList Listado de sedes.
	 * @param seat Sede a buscar.
	 * @param indexInList Posición de la sede en la lista.
	 * @return "True" si la sede existe, "false" en caso contrario.
	 */
	public boolean existsSeatName(List<AbstractBaseDTO> seatList, PfProvinceDTO seat, int indexInList) {
		boolean ret = false;
		PfProvinceDTO auxSeat;
		if (seatList != null && !seatList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = seatList.iterator();
			while (it.hasNext() && ret == false) {
				auxSeat = (PfProvinceDTO) it.next();
				// Not the same row and different code
				if (seat.getCnombre().toUpperCase().equals(
						auxSeat.getCnombre().toUpperCase())
						&& !seat.getPrimaryKeyString().equals(
								auxSeat.getPrimaryKeyString())) {
					ret = true;
				}
				if (auxSeat.getCnombre().toUpperCase().equals(
						seat.getCnombre().toUpperCase())
						&& auxSeat.getPrimaryKey() == null
						&& seat.getPrimaryKey() == null && !auxSeat.equals(seat)) {
					if (indexInList <= -1
							&& countSeatNameInList(auxSeat, seatList) > 0) {
						// this case is when we add a new seat
						ret = true;
					} else if (countSeatNameInList(auxSeat, seatList) > 1) {
						// this case is when we are modifing a seat
						ret = true;

					}
				}
			}
		}
		return ret;
	}

	/**
	 * M&eacute;todo que calcula el n&uacute;mero de veces que se repite la descripción de una sede dentro de una lista.
	 * @param seat Sede a buscar.
	 * @param list Listado de sedes
	 * @return Número de repeticiones de la descripción de la sede en la lista.
	 */
	private int countSeatNameInList(PfProvinceDTO seat, List<AbstractBaseDTO> list) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator.hasNext();) {
			PfProvinceDTO pfProvinceDTO = (PfProvinceDTO) iterator.next();
			if (pfProvinceDTO.getCnombre() != null
					&& pfProvinceDTO.getCnombre().toUpperCase().equals(
							seat.getCnombre().toUpperCase())) {
				count++;
			}
		}
		return count;
	}

	public PfProvinceDTO getSeatCopy(PfProvinceDTO province) {
		PfProvinceDTO copyProvince = new PfProvinceDTO();
		copyProvince.setCcodigoprovincia(province.getCcodigoprovincia());
		copyProvince.setCcreated(province.getCcreated());
		copyProvince.setCmodified(province.getCmodified());
		copyProvince.setCnombre(province.getCnombre());
		copyProvince.setFcreated(province.getFcreated());
		copyProvince.setFmodified(province.getFmodified());
		copyProvince.setPfUsers(province.getPfUsers());
		copyProvince.setPfUsersProvinces(province.getPfUsersProvinces());
		copyProvince.setPrimaryKey(province.getPrimaryKey());
		copyProvince.setSelected(province.isSelected());
		copyProvince.setUpdated(province.isUpdated());
		return copyProvince;
	}

	private List<PfProvinceDTO> convertirAProvincia(List<AbstractBaseDTO> list) {
		List<PfProvinceDTO> provinces = new ArrayList<PfProvinceDTO>();
		for (AbstractBaseDTO p : list) {
			provinces.add((PfProvinceDTO) p);
		}
		return provinces;
	}

	/**
	 * Obtiene una sede a partir de su clave primaria
	 * @param primaryKey
	 * @return
	 */
	public PfProvinceDTO querySeatByPk(Long primaryKey) {
		return (PfProvinceDTO) baseDAO.queryElementOneParameter(
				"administration.provinceByPk", "pk", primaryKey);
	}

	/**
	 * Guarda la sede que recibe como parametro
	 * @param pfProvinceDTO
	 */
	@Transactional(readOnly = false)
	public void saveSeat(PfProvinceDTO pfProvinceDTO) {
		baseDAO.insertOrUpdate(pfProvinceDTO);
	}

	@Transactional(readOnly = false)
	public void salvarSedeYAdministrador(PfProvinceDTO pfProvinceDTO, PfUsersDTO usuario, String codigoProvinciaPadre) {
		boolean esNuevaProvincia = true;
		if(pfProvinceDTO.getPrimaryKey()!=null){
			esNuevaProvincia = false;
		}
		PfUnidadOrganizacionalDTO organismo = organismBO.getOrganismoByCode(codigoProvinciaPadre);
		pfProvinceDTO.setOrganismo(organismo);
		baseDAO.insertOrUpdate(pfProvinceDTO);
		if(esNuevaProvincia){
			PfProvinceAdminDTO adminProvincia = new PfProvinceAdminDTO();
			adminProvincia.setPfUser(usuario);
			adminProvincia.setPfProvince(pfProvinceDTO);
			adminProvincia.setEsSedePrincipal(0L);
			baseDAO.insertOrUpdate(adminProvincia);
		}
	}
	
	/**
	 * Borra la sede que recibe como parametro
	 * @param pfProvinceDTO
	 */
	@Transactional(readOnly = false)
	public void deleteSeat(PfProvinceDTO pfProvinceDTO) {
		baseDAO.delete(pfProvinceDTO);
	}

	/**
	 * Valida el alta y modificación de una sede
	 * @param pfProvinceDTO
	 * @param errors
	 */
	public void validateSeat(PfProvinceDTO pfProvinceDTO, ArrayList<String> errors) {
		if(!existsSeatCodeDIR3(pfProvinceDTO)) {
			errors.add("La sede debe pertenecer al DIR3. ");
		}
		if(existsSeatCode(pfProvinceDTO)) {
			errors.add("Ya existe una sede con el código \"" + pfProvinceDTO.getCcodigoprovincia() + "\".");
		}
		if(existsSeatName(pfProvinceDTO)) {
			errors.add("Ya existe una sede con el nombre \"" + pfProvinceDTO.getCnombre() + "\".");
		}
	}

	/**
	 * Valida el borrado de una sede
	 * @param pfProvinceDTO
	 * @param errors
	 */
	public void validateDeleteSeat(PfProvinceDTO pfProvinceDTO, ArrayList<String> errors) {
		if(checkSeatReferences(pfProvinceDTO)) {
			errors.add("La sede está referenciada y no se permite su borrado.");
		}
	}

	/**
	 * Valida el borrado de un organismo
	 * @param pfProvinceDTO
	 * @param errors
	 */
	public void validateDeleteSedeParaAdministradorDeOrganismo(PfProvinceDTO pfProvinceDTO, PfUsersDTO usuario, ArrayList<String> errors, PfProvinceAdminDTO adminDelete) {
		if(checkSeatReferences(pfProvinceDTO, usuario, adminDelete)) {
			errors.add("Existen usuarios en la sede y no se permite su borrado.");
		}
	}
	
	/**
	 * Obtiene la lista de organismos en función del perfil del administrador
	 * @param userDTO
	 * @return
	 */
	public List<PfUnidadOrganizacionalDTO> getOrgList(PfUsersDTO userDTO) {
		List<PfUnidadOrganizacionalDTO> organismList = null;
		// Se filtran los usuarios en base a la búsqueda
		if(userAdmBO.isAdministrator(userDTO.getPfUsersProfiles())) {
			List<AbstractBaseDTO> abstractBase= organismBO.getAllOrganisms();
			organismList = new ArrayList<>();
			for (AbstractBaseDTO abd : abstractBase){				
				organismList.add((PfUnidadOrganizacionalDTO)abd);
			}			
		} else {
			organismList = userDTO.getPfUnidadOrganizacionalList();
		}
		return organismList;
	}
}
