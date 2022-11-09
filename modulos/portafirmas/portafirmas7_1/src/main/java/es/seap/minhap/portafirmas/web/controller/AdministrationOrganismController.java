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

package es.seap.minhap.portafirmas.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.OrganismBO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceAdminDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.OrganismoSelect;
import es.seap.minhap.portafirmas.web.beans.Seat;
import es.seap.minhap.portafirmas.web.converter.SeatConverter;
import es.seap.minhap.portafirmas.web.validation.SeatValidator;

@Controller
@RequestMapping("adminOrganism")
public class AdministrationOrganismController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private OrganismBO organismBO;

	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private SeatConverter seatConverter;
	
	@Autowired
	private SeatValidator seatValidator;
	
	@Autowired
	private ProvinceBO provinceBO;
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model) {
		loadOrganismos(model);		
		return "organismsManagement";
	}

	@RequestMapping(value = "/loadOrganism", method = RequestMethod.GET)
	public ModelAndView loadOrganism(ModelMap model) {
		loadOrganismos(model);
		return new ModelAndView("sedesAdministradasOrganismo", model);
	}
	
	public void loadOrganismos(ModelMap model) {

		PfUsersDTO user = getAuthenticationUser();
		
		PfUsersDTO userFromBD = (PfUsersDTO) baseDAO.findEntitity(PfUsersDTO.class, user.getPrimaryKey());
		List <PfUnidadOrganizacionalDTO> unidadOrgList = userFromBD.getPfUnidadOrganizacionalList();
		List<AbstractBaseDTO> provincesList = new ArrayList<AbstractBaseDTO>();

		for (PfUnidadOrganizacionalDTO unidadOrg : unidadOrgList){
			provincesList.addAll(organismBO.getAdminProvincesRelation(unidadOrg));
		}

		model.addAttribute("provincesList", provincesList);

		Set<Long> organismos = new HashSet<Long>();
		for(PfUnidadOrganizacionalDTO org: userFromBD.getPfUnidadOrganizacionalList()){
			organismos.add(org.getPrimaryKey());
		}

		model.addAttribute("organismos", organismos);		
		model.addAttribute("organismosCombo", userFromBD.getPfUnidadOrganizacionalList());
	}

	@RequestMapping(value = "queryOrganismos")
	public @ResponseBody List<OrganismoSelect> queryOrganismos(@RequestParam(value = "term") final String filtro) {	
		return organismBO.queryOrganismos(filtro);
	}
	
	@RequestMapping(value = "/saveSeat")
	public @ResponseBody ArrayList<String> saveSeat(
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "seatCode") final String seatCode,
			@RequestParam(value = "seatName") final String seatName,
			@RequestParam(value = "seatParent") final String seatParent) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			PfUsersDTO user = getAuthenticationUser();
			// Se obtienen la información de la vista
			Seat seat = new Seat(seatCode, seatName);
			if(!Util.esVacioONulo(primaryKey)) {
				seat.setPrimaryKey(Long.parseLong(primaryKey));
			}
			
			// Se valida la vista
			seatValidator.validate(seat, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// se vuelcan los datos obtenidos de la vista
			PfProvinceDTO pfProvinceDTO = seatConverter.envelopeToDTO(seat);

			// Se validan las reglas de negocio
			provinceBO.validateSeat(pfProvinceDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se realiza la persistencia
			provinceBO.salvarSedeYAdministrador(pfProvinceDTO, user, seatParent);
			
		} catch (Exception e) {
			log.error("Error al insertar la sede: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Borrado de una sede
	 * @param primaryKey
	 * @return
	 */
	@RequestMapping(value = "/deleteSeat")
	public @ResponseBody ArrayList<String> deleteSeat(
			@RequestParam(value = "primaryKey") final String primaryKey) {
		ArrayList<String> errors = new ArrayList<String>();
		try {

			PfUsersDTO user = getAuthenticationUser();
			// Se obtiene la sede a borrar			
			PfProvinceDTO pfProvinceDTO = provinceBO.querySeatByPk(Long.parseLong(primaryKey));			

			// Se validan las reglas de negocio
			organismBO.validateDeleteOrganismSeat(pfProvinceDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}

			PfProvinceAdminDTO provinceAdmin = organismBO.getProvinceAdminByUser(user, pfProvinceDTO);

			if(provinceBO.checkSeatReferences(pfProvinceDTO, user, provinceAdmin)) {
				errors.add("Existen usuarios en una sede del organismo y no se permite su borrado.");
			}else{		
				// Se realiza el borrado
				organismBO.deleteSedeParaAdministradorDeOrganismo(provinceAdmin, pfProvinceDTO);
			}
		} catch (Exception e) {
			log.error("Error al borrar la sede: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		return errors;
	}
	
	public PfUsersDTO getAuthenticationUser(){		
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();		
		return user;
	}
}
