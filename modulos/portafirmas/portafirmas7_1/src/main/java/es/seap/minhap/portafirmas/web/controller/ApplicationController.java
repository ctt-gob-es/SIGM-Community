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
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.administration.ApplicationAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.application.ApplicationParameterUtil;
import es.seap.minhap.portafirmas.utils.eni.ComboUtils;
import es.seap.minhap.portafirmas.web.beans.Application;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
import es.seap.minhap.portafirmas.web.converter.ApplicationConverter;
import es.seap.minhap.portafirmas.web.converter.ApplicationParameterConverter;
import es.seap.minhap.portafirmas.web.validation.ApplicationValidator;

@Controller
@RequestMapping("administration/applications")
public class ApplicationController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ApplicationAdmBO appBO;
	
	@Autowired
	private ApplicationValidator applicationValidator;
	
	@Autowired
	private ApplicationConverter applicationConverter;
	
	@Autowired
	private ApplicationParameterConverter parameterConverter;
	
	@Autowired 
	private ApplicationParameterUtil applicationParameterUtil;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Resource(name = "messageProperties")
	private Properties messages;
	
	/*
	 * Carga del listado de aplicaciones
	 */
	@RequestMapping (value = "loadApplications", method = RequestMethod.GET)
	public ModelAndView loadApplications(){
		List<AbstractBaseDTO> appsList = appBO.queryList();
		ModelMap model = new ModelMap();
		model.addAttribute("appsList", appsList);
		model.addAttribute("conf", appBO.prepareApp());
		model.addAttribute("app", new Application());
		//model.addAttribute("documentEni", new DocumentEni());
		loadCombosMetadatos(model);
		
		//parametros de informes
		model.addAttribute("parametersInforme", applicationParameterUtil.getParametersInformes(applicationBO.queryParameterListAll(), null));
		
		return new ModelAndView("applications", model);
	}
	
	/**
	 * Alta o modificación de una aplicacion
	 * @param primaryKey
	 * @param appCode, pfApplicationsDTO
	 * @param appName
	 * @param appConf
	 * @return
	 */
	@RequestMapping(value = "guardarApp", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> guardarApp(@ModelAttribute ("application") Application application){
		ArrayList<String> errors = new ArrayList<String>();
		
		try {
			// Se valida la vista
			applicationValidator.validate(application, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			
			// Se obtienen los DTO
			PfApplicationsDTO pfApplicationDTO = applicationConverter.envelopeToDTO(application);
			List<PfApplicationsParameterDTO> pfApplParameterDTOList = parameterConverter.envelopeToDTO(application);
			pfApplParameterDTOList.addAll(parameterConverter.parametersInformeToDTO(application.getParamInfomes()));

			// Reglas de negocio
			appBO.validateApplication(pfApplicationDTO, errors);

			if(!errors.isEmpty()) {
				return errors;
			}

			// Se persiste
			appBO.saveApplicationAndParameters(pfApplicationDTO, pfApplParameterDTOList);				
			//appBO.saveParameters(pfApplParameterDTOList, pfApplicationDTO);
			appBO.saveMetadata(pfApplicationDTO, application.getDocumentEni(), user);
						
		} catch (Exception e) {
			log.error("Error al insertar la aplicacion: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Borrado de una aplicacion
	 * @param primaryKey
	 * @return
	 */
	@RequestMapping(value = "deleteApplication")
	public @ResponseBody ArrayList<String> deleteApplication(
			@RequestParam(value = "primaryKey") final Long primaryKey) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			
			// Se obtiene la aplicacion a borrar			
			/*PfApplicationsDTO pfApplicationsDTO = (PfApplicationsDTO) appBO.applicationPkQuery(primaryKey);				
			// Se validan las reglas de negocio
			appBO.validateDelete(pfApplicationsDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}			
			// Se realiza el borrado
			appBO.deleteApplication(pfApplicationsDTO);*/
			
			appBO.validateAndDelete(primaryKey, errors);
			if (!errors.isEmpty()) {
				return errors;
			}
			
		} catch (Exception e) {
			log.error("Error al borrar la aplicacion: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	 
	
	
	@RequestMapping(value = "recoverApp", method = RequestMethod.GET)
	public ModelAndView recoverApp(
			@RequestParam(value = "primaryKey") final String primaryKey) {
		
		Application app = new Application();
		PfApplicationsDTO appl = (PfApplicationsDTO) appBO.applicationPkQueryTrans(Long.parseLong(primaryKey));
		Set<PfApplicationsParameterDTO> params = appl.getPfApplicationsParameters();
		
		ModelMap model = new ModelMap();
		
		app.setAppCode(appl.getCapplication());
		app.setAppName(appl.getDapplication());
		app.setPfConfiguration(appl.getPfConfiguration().getCconfiguration());
		if(appl.getPfApplication() != null) {
			app.setAppParent(appl.getPfApplication().getCapplication());
		}
		
		for (PfApplicationsParameterDTO pfAppParamDTO : params) {
			
			PfParametersDTO paramDTO = pfAppParamDTO.getPfParameter();
			
			if(paramDTO.getCparameter().equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_USUARIO)){
				app.setWsUser(pfAppParamDTO.getTvalue());
			}
			else if(paramDTO.getCparameter().equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_PASSWORD)){
				app.setWsPassword(pfAppParamDTO.getTvalue());
			}
			else if(paramDTO.getCparameter().equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_ACTIVA)){
				app.setWsActiva(pfAppParamDTO.getTvalue());
			}
			else if(paramDTO.getCparameter().equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_WSDLLOCATION)){
				;app.setWsLocation(pfAppParamDTO.getTvalue());
			}
			else if(paramDTO.getCparameter().equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_NOTIFINTERMEDIOS)){
				app.setWsNotifIntermedios(pfAppParamDTO.getTvalue());
			}
		}
		loadCombosMetadatos(model);
		
		model.addAttribute("app", app);
		model.addAttribute("conf", appBO.prepareApp());
		model.addAttribute("appsList", appBO.obtenerListaAppsPadre(appl));
		//model.addAttribute("documentEni", documentEni);
		
		//parametros de informes
		model.addAttribute("parametersInforme", applicationParameterUtil.getParametersInformes(applicationBO.queryParameterListAll(), appl));
		
		return new ModelAndView("modalApps", model);
	}
	
	
	/**
	 * Método que marcar los mensajes seleccionados como leidos
	 * @param labelId Identificador de la etiqueta de usuario
	 * @param response Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/loadMetadatasEni", method = RequestMethod.GET)
	public @ResponseBody DocumentEni  loadMetadatasEni(
			@RequestParam(value = "primaryKey") final String primaryKey) throws IOException {
		
		PfApplicationsDTO appl = (PfApplicationsDTO) appBO.applicationPkQueryTrans(Long.parseLong(primaryKey));
		
		
		DocumentEni documentEni = appBO.getDocumentEniByApp(appl);
		return documentEni;
	}

	
	/**
	 * @param model
	 */
	private void loadCombosMetadatos(ModelMap model) {
		
		model.addAttribute("origen", ComboUtils.getOrigen(messages));
		model.addAttribute("estadosElaboracion", ComboUtils.getEstadosElaboracion(messages));
		model.addAttribute("tiposDocumentales", ComboUtils.getTiposDocumentales(messages));

	}

}
