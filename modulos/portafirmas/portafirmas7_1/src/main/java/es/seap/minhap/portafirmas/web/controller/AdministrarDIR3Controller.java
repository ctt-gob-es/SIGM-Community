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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.OrganismoDIR3BO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.administration.MessageAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ArchivoTemporalRequest;
import es.seap.minhap.portafirmas.web.validation.MessageValidator;

@Controller
@RequestMapping("administration/administrarDIR3")
public class AdministrarDIR3Controller {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private OrganismoDIR3BO organismoDIR3BO;
	
	@Autowired
	private MessageAdmBO messageAdmBO;
	
	
	@Autowired
	private ProvinceBO provinceBO;
	
	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private MessageValidator messageValidator;
	
	@Resource(name = "messageProperties")
	private Properties messages;
	
	@RequestMapping(value = "/cargaActualizarDIR3", method = RequestMethod.GET)
	public ModelAndView cargaActualizarDIR3() {
		ModelMap model = null;
		
		try {
			// Carga de las últimas actualizaciones
			List<AbstractBaseDTO> actualizacionesList = organismoDIR3BO.getUltimasActualizacionesDIR3();
		
			
			model = new ModelMap();
			
			model.addAttribute("actualizacionesList", actualizacionesList);		

		} catch (Throwable t) {
			log.error("Error al cargar las actualizaciones: ", t);
		}
		return new ModelAndView("administrarDIR3", model);
	}
	
	@RequestMapping(value = "/actualizarDIR3", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> actualizarDIR3(ModelMap model, @RequestParam(value = "messageFStart") final String fechaInicio, @RequestParam(value = "filtroCodigo") final String filtroCodigo) {
		ArrayList<String> errors = new ArrayList<String>();
		
		try {
			organismoDIR3BO.actualizarOrganismosDir3(fechaInicio, filtroCodigo);
		
		} catch (Throwable t) {
			log.error("Error al cargar las actualizaciones: ", t);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
	
	
	return errors;
	}
	
	@RequestMapping(value = "/subirFicheroDIR3AProcesar", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> subirFicheroDIR3AProcesar(@RequestBody ArchivoTemporalRequest archivoTemporalRequest) throws IOException {
		log.error("Inicio subir fichero DIR3 manual");
		ArrayList<String> errors = new ArrayList<String>();
		
		try {
			organismoDIR3BO.actualizarOrganismosDir3Manual(archivoTemporalRequest);
		
		} catch (Throwable t) {
			log.error("Error al cargar las actualizaciones Manualmente: ", t);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
	
		log.error("Fin subir fichero DIR3 manual");
	return errors;
	}	
		
		
		
        
	
}
