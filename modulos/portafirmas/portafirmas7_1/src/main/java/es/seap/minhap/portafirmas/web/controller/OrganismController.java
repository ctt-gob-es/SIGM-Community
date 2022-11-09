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
import java.util.List;

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
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.OrganismoSelect;

@Controller
@RequestMapping("organismsManagement")
public class OrganismController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private OrganismBO organismBO;

	/**
	 * Carga del listado de organismos
	 * @return
	 */
	@RequestMapping(value = "/loadListOrganism", method = RequestMethod.GET)
	public ModelAndView loadListOrganism() {
		// Carga de las sedes
		List<AbstractBaseDTO> organisms = organismBO.getAllOrganisms();
		ModelMap model = new ModelMap();
		model.addAttribute("organismList", organisms);		

		return new ModelAndView("organism", model);
	}

	/**
	 * Alta o modificación de una sede
	 * @param primaryKey
	 * @param organismCode
	 * @param organismName
	 * @return
	 */
	@RequestMapping(value = "/saveOrganism")
	public @ResponseBody ArrayList<String> saveOrganism(
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "organismName") final String organismName,
			@RequestParam(value = "organismCode") final String organismCode) {		
//		PfUsersDTO user = getAuthenticationUser();
		
		ArrayList<String> errors = new ArrayList<String>();
		try {

			OrganismoSelect organism = new OrganismoSelect(primaryKey, organismName, organismCode);			
			if(!Util.esVacioONulo(primaryKey)) {
				organism.setId(primaryKey);
			}			
			
			if(Util.esVacioONulo(organism.getDenominacion())) {
				errors.add("El nombre del Organismo es nulo");
			}
			
			if(Util.esVacioONulo(organism.getCodigo())) {
				errors.add("El código del Organismo es nulo");
			}
			
			if(!errors.isEmpty()) {
				return errors;
			}			
			
			// se vuelcan los datos obtenidos de la vista
			PfUnidadOrganizacionalDTO pfUnidadOrganizacional = organismBO.envelopeToDTO(organism);
			organismBO.saveOrganism(pfUnidadOrganizacional);
			
//			user.getPfUnidadOrganizacionalList().add(pfUnidadOrganizacional);
//			
//			organismBO.saveUser(user);
			
		} catch (Exception e) {
			log.error("Error al insertar el organismo: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}

	/**
	 * Borrado de un organismo
	 * @param primaryKey
	 * @return
	 */
	@RequestMapping(value = "/deleteOrganism")
	public @ResponseBody ArrayList<String> deleteOrganism(
			@RequestParam(value = "primaryKey") final String primaryKey) {
		ArrayList<String> errors = new ArrayList<String>();
		try {

			PfUsersDTO user = getAuthenticationUser();
			// Se obtiene la sede a borrar			
			PfUnidadOrganizacionalDTO pfUnidadOrganizacionalDTO = organismBO.getOrganismoById(primaryKey);

			// Se validan las reglas de negocio
			organismBO.validateDeleteOrganismo(pfUnidadOrganizacionalDTO, user, errors);

			if(!errors.isEmpty()) {
				return errors;
			}

			// Se realiza el borrado
			organismBO.deleteOrganismo(pfUnidadOrganizacionalDTO);
			
		} catch (Exception e) {
			log.error("Error al borrar la sede: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}

		return errors;
	}

	@RequestMapping(value = "queryOrganismos")
	public @ResponseBody List<OrganismoSelect> queryOrganismos(@RequestParam(value = "term") final String filtro) {	
		return organismBO.queryOrganismos(filtro);
	}
	
	public PfUsersDTO getAuthenticationUser(){		
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();		
		return user;
	}
}
