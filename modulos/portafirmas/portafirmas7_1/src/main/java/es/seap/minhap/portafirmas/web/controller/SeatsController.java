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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.OrganismoDIR3BO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.SeatBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfOrganismoDIR3DTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.SeatAutocomplete;
import es.seap.minhap.portafirmas.web.beans.SeatOrgDIR3;

@Controller
@RequestMapping("administration/seats")
public class SeatsController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	ProvinceBO provinceBO;
	
	@Autowired
	private SeatBO seatBO;
	
	@Autowired
	private OrganismoDIR3BO organismoDIR3BO;

	/**
	 * Carga del listado de sedes
	 * @return
	 */
	@RequestMapping(value = "/loadSeats", method = RequestMethod.GET)
	public ModelAndView loadSeats() {
		// Carga de las sedes
		List<AbstractBaseDTO> seats = provinceBO.getAllProvinces();
		List<SeatOrgDIR3> listaSedes = new ArrayList<SeatOrgDIR3>();
		for (AbstractBaseDTO abstractSeat : seats) {
			PfProvinceDTO provincia = (PfProvinceDTO) abstractSeat;
			SeatOrgDIR3 sedeOrganismo = new SeatOrgDIR3(provincia);
			PfOrganismoDIR3DTO orgDIR3 = organismoDIR3BO.getOrganismoDIR3ByCode(provincia.getCcodigoprovincia());
			sedeOrganismo.setOrganismoDIR3(orgDIR3);
			if (orgDIR3!=null) {
				sedeOrganismo.setDenominacion(orgDIR3.getDenominacion());
			}
			listaSedes.add(sedeOrganismo);
		}
		
		ModelMap model = new ModelMap();
		model.addAttribute("seatList", listaSedes);		

		return new ModelAndView("seats", model);
	}	
	
	/**
	 * Alta o modificación de una sede
	 * @param primaryKey
	 * @param seatCode
	 * @param seatName
	 * @return
	 */
	@RequestMapping(value = "/saveSeat")
	public @ResponseBody ArrayList<String> saveSeat(
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "seatCode") final String seatCode,
			@RequestParam(value = "seatName") final String seatName,
			@RequestParam(value = "seatLongDuration") final String seatLongDuration,
			@RequestParam(value = "seatURLCSV") final String seatURLCSV,
			@RequestParam(value = "seatLdap") final String seatLdap) {
		
		return seatBO.saveSeat(primaryKey, seatCode, seatName,seatLongDuration, seatURLCSV, seatLdap);
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

			// Se obtiene la sede a borrar			
			PfProvinceDTO pfProvinceDTO = provinceBO.querySeatByPk(Long.parseLong(primaryKey));
			
			// Se validan las reglas de negocio
			provinceBO.validateDeleteSeat(pfProvinceDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se realiza el borrado
			provinceBO.deleteSeat(pfProvinceDTO);
			
		} catch (Exception e) {
			log.error("Error al borrar la sede: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	@RequestMapping(value = "autocompleteSeats")
	public @ResponseBody List<SeatAutocomplete> autocompleteSeats(@RequestParam(value = "term") final String term) {

		List<SeatAutocomplete> results = new ArrayList<SeatAutocomplete>();

		// Se toma para el filtro el último valor introducido
		String[] values = term.split(",");

		// Se filtran los organismos en base a la búsqueda
		List<AbstractBaseDTO> organismosBusqueda = organismoDIR3BO.getAutocompleteOrganismoDIR3ByFind(values[values.length - 1].trim());
		
		// Se convierten los resultados
		for (AbstractBaseDTO dto : organismosBusqueda) {
			results.add(new SeatAutocomplete((PfOrganismoDIR3DTO) dto));
		}
		return results;
	}

}