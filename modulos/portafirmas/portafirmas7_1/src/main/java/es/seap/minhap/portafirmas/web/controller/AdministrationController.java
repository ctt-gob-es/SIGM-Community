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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.utils.Constants;

@Controller
@RequestMapping("administration")
public class AdministrationController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ApplicationBO appBO;
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model) {
		
		List<AbstractBaseDTO> resultado_global = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_GLOBAL);
		List<AbstractBaseDTO> listValores = new ArrayList<AbstractBaseDTO>();
		if(!resultado_global.isEmpty())
			listValores=appBO.listValuesParams(resultado_global);
		model.addAttribute("listGlobal",resultado_global);
		model.addAttribute("listValores", listValores);
		
		List<AbstractBaseDTO> resultado_globalparte2 = appBO.listTypesApp("GLOBAL-2");
		List<AbstractBaseDTO> listValoresparte2 = new ArrayList<AbstractBaseDTO>(); 
		if(!resultado_globalparte2.isEmpty())
			listValoresparte2 = appBO.listValuesParams(resultado_globalparte2);
		model.addAttribute("listProxy",resultado_globalparte2);
		model.addAttribute("listValoresProxy", listValoresparte2);
		
		List<AbstractBaseDTO> resultado_globalparte3 = appBO.listTypesApp("GLOBAL-3");
		List<AbstractBaseDTO> listValoresparte3 = new ArrayList<AbstractBaseDTO>(); 
		if(!resultado_globalparte3.isEmpty())
			listValoresparte3=appBO.listValuesParams(resultado_globalparte3);
		model.addAttribute("listAlmacenConfianza",resultado_globalparte3);
		model.addAttribute("listValoresAlmacenConfianza", listValoresparte3);
		
		List<AbstractBaseDTO> resultado_login = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_LOGIN);
		List<AbstractBaseDTO> listValoresLogin = new ArrayList<AbstractBaseDTO>();
		if(!resultado_login.isEmpty())
			listValoresLogin = appBO.listValuesParams(resultado_login);
		model.addAttribute("listLogin", resultado_login);
		model.addAttribute("listValoresLogin", listValoresLogin);
		
		List<AbstractBaseDTO> resultado_SIM = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_SIM);
		List<AbstractBaseDTO> listValoresSIM = new ArrayList<AbstractBaseDTO>();
		if(!resultado_SIM.isEmpty())
			listValoresSIM = appBO.listValuesParams(resultado_SIM);
		model.addAttribute("listSIM", resultado_SIM);
		model.addAttribute("listValoresSIM", listValoresSIM);
		
		List<AbstractBaseDTO> resultado_estilo = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_STYLE);
		List<AbstractBaseDTO> listValoresEstilo = new ArrayList<AbstractBaseDTO>();
		if(!resultado_estilo.isEmpty())
			listValoresEstilo = appBO.listValuesParams(resultado_estilo);
		model.addAttribute("listEstilo", resultado_estilo);
		model.addAttribute("listValoresEstilo", listValoresEstilo);
		
		List<AbstractBaseDTO> resultado_notificacion = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_NOTIFICATION);
		List<AbstractBaseDTO> listValoresNotificacion = new ArrayList<AbstractBaseDTO>();
		if(!resultado_notificacion.isEmpty())
			listValoresNotificacion = appBO.listValuesParams(resultado_notificacion);
		model.addAttribute("listNotificacion", resultado_notificacion);
		model.addAttribute("listValoresNotificacion", listValoresNotificacion);
		
		List<AbstractBaseDTO> resultado_smd = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_SM_DOCEL);
		List<AbstractBaseDTO> listValoresSmd = new ArrayList<AbstractBaseDTO>();
		if(!resultado_smd.isEmpty())
			listValoresSmd = appBO.listValuesParams(resultado_smd);
		model.addAttribute("listSMD", resultado_smd);
		model.addAttribute("listValoresSMD", listValoresSmd);
		
		List<AbstractBaseDTO> resultado_smp = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_SP_DOCEL);
		List<AbstractBaseDTO> listValoresSmp = new ArrayList<AbstractBaseDTO>();
		if(!resultado_smp.isEmpty())
			listValoresSmp = appBO.listValuesParams(resultado_smp);
		model.addAttribute("listSMP", resultado_smp);
		model.addAttribute("listValoresSMP", listValoresSmp);
		
		List<AbstractBaseDTO> resultado_validaciones = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_VALIDATION);
		List<AbstractBaseDTO> listValoresValidaciones = new ArrayList<AbstractBaseDTO>();
		if(!resultado_validaciones.isEmpty())
			listValoresValidaciones = appBO.listValuesParams(resultado_validaciones);
		model.addAttribute("listValidaciones", resultado_validaciones);
		model.addAttribute("listValoresValidaciones", listValoresValidaciones);
		
		List<AbstractBaseDTO> resultado_jobs = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_JOBS);
		List<AbstractBaseDTO> listValoresJobs = new ArrayList<AbstractBaseDTO>();
		if(!resultado_jobs.isEmpty())
			listValoresJobs = appBO.listValuesParams(resultado_jobs);
		model.addAttribute("listJobsParameters", resultado_jobs);
		model.addAttribute("listValoresJobs", listValoresJobs);

		List<AbstractBaseDTO> resultado_autentica = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_AUTENTICA);
		List<AbstractBaseDTO> listValoresAutentica = new ArrayList<AbstractBaseDTO>();
		if(!resultado_autentica.isEmpty())
			listValoresAutentica = appBO.listValuesParams(resultado_autentica);
		model.addAttribute("listAutenticaParameters", resultado_autentica);
		model.addAttribute("listValoresAutentica", listValoresAutentica);
		
		List<AbstractBaseDTO> resultado_broker = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_BROKER);
		List<AbstractBaseDTO> listValoresBroker = new ArrayList<AbstractBaseDTO>();
		if(!resultado_broker.isEmpty())
			listValoresBroker = appBO.listValuesParams(resultado_broker);
		model.addAttribute("listBrokerParameters", resultado_broker);
		model.addAttribute("listValoresBroker", listValoresBroker);
		
		List<AbstractBaseDTO> resultado_DIR3 = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_DIR3);
		List<AbstractBaseDTO> listValoresDIR3 = new ArrayList<AbstractBaseDTO>();
		if(!resultado_DIR3.isEmpty())
			listValoresDIR3 = appBO.listValuesParams(resultado_DIR3);
		model.addAttribute("listDIR3Parameters", resultado_DIR3);
		model.addAttribute("listValoresDIR3", listValoresDIR3);
		
		//Valores de la conexion con Fire
		List<AbstractBaseDTO> resultado_cf = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_FIRE);
		List<AbstractBaseDTO> listValoresCf = new ArrayList<AbstractBaseDTO>();
		if(!resultado_cf.isEmpty())
			listValoresCf = appBO.listValuesParams(resultado_cf);
		model.addAttribute("listCF", resultado_cf);
		model.addAttribute("listValoresCF", listValoresCf);
		
		List<AbstractBaseDTO> resultado_CUSTODIA = appBO.listTypesApp(Constants.C_TYPE_PARAMETER_CUSTODIA);
		List<AbstractBaseDTO> listValoresCUSTODIA = new ArrayList<AbstractBaseDTO>();
		if(!resultado_CUSTODIA.isEmpty())
			listValoresCUSTODIA = appBO.listValuesParams(resultado_CUSTODIA);
		model.addAttribute("listCUSTODIAParameters", resultado_CUSTODIA);
		model.addAttribute("listValoresCUSTODIA", listValoresCUSTODIA);
		
		return "administration";
	}
	
	@RequestMapping(value = "/updateField", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String>  updateField(@RequestParam(value = "id") final String id,
			@RequestParam(value = "valor") final String valorCodificadoEnBase64) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			String valor = StringUtils.newStringUtf8(Base64.decodeBase64(valorCodificadoEnBase64));
			PfConfigurationsParameterDTO configParam = appBO.valueParamByPk(id);
			configParam.setTvalue(valor);
			appBO.actualizarConfigParam(configParam);
		} catch (Exception e) {
			log.error("Error al actualizar parametro genérico de administración: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}

		return errors;
	}
	
}