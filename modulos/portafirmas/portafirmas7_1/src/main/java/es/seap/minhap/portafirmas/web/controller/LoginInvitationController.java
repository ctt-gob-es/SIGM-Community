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
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.beans.RedactionUploadedDocuments;
import es.seap.minhap.portafirmas.business.ws.AdminServiceBO;
import es.seap.minhap.portafirmas.domain.PfInvitedUsersDTO;
import es.seap.minhap.portafirmas.domain.PfProfilesDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersEmailDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.InvitedLogin;
import es.seap.minhap.portafirmas.web.beans.Login;

@Controller
@RequestMapping("guest")
public class LoginInvitationController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private RequestBO requestBO;

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private AdminServiceBO admServiceBO;
	
	@Resource
	private RedactionUploadedDocuments uploadedFiles;

	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model) {
		InvitedLogin invLogin = new	InvitedLogin();
		invLogin.setError("display: none;");
		
		// command object
		model.addAttribute("invitedForm", invLogin);
		
		// return form view
		return "guest";
	}

	@RequestMapping(value = "/validateInvitationMail", method = RequestMethod.GET)
	public @ResponseBody String validateMail(@RequestParam(value = "email") final String email, 
			HttpServletResponse response) throws IOException{

		String retorno = "";
		String mensajeDeError = "";
		
		if(Util.esVacioONulo(email)){
			mensajeDeError = messageSource.getMessage("field.required.email", null, LocaleContextHolder.getLocale());
		}else{
			PfRequestsDTO request = requestBO.existsInvRequestsByUserMail(email);
			
			if(request == null){
				mensajeDeError = messageSource.getMessage("error.invitedRequestWithEmailNotFound", null,
						LocaleContextHolder.getLocale());;
			}else if (request.getLaccepted()){
				mensajeDeError = messageSource.getMessage("error.invitedRequestWithEmailAlreadyAccepted",  null, 
						LocaleContextHolder.getLocale());
			}			
		}
		
		if(!Util.esVacioONulo(mensajeDeError)){
			retorno = "{\"status\": \"error\",\"msgError\":\"" + mensajeDeError + "\"}";
		}else{
			retorno = "{\"status\": \"success\"}";
		}
		return retorno;
	}
	
	@RequestMapping(value = "/validateInvitationFields")
	public @ResponseBody ArrayList<String> validateInvitationFields(@RequestParam(value = "dni") final String dni,
			@RequestParam(value = "name") final String name, @RequestParam(value = "surname1") final String surname1, 
			@RequestParam(value = "surname2") final String surname2, HttpServletResponse response, ModelMap model) throws IOException{
		ArrayList<String> errors =  new ArrayList<>();
		
		if(Util.esVacioONulo(dni)){
			errors.add(messageSource.getMessage("field.required.dni", null, LocaleContextHolder.getLocale()));
		}else{
			PfUsersDTO userWithSameId = userAdmBO.queryUsersByIdentifier(dni);
			if(userWithSameId != null){
				errors.add(messageSource.getMessage("user.nif.repeat", null, LocaleContextHolder.getLocale()));
			}
		}
		if(Util.esVacioONulo(name)){
			errors.add(messageSource.getMessage("field.required.name", null, LocaleContextHolder.getLocale()));
		}
		if(Util.esVacioONulo(surname1)){
			errors.add(messageSource.getMessage("field.required.surname1", null, LocaleContextHolder.getLocale()));
		}
		if(Util.esVacioONulo(surname2)){
			errors.add(messageSource.getMessage("field.required.surname2", null, LocaleContextHolder.getLocale()));
		}
		model.addAttribute("errors", errors);
		return errors;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("invitedForm") @Valid InvitedLogin invLogin) throws Exception {
		ModelAndView result = new ModelAndView();
		try {
			log.debug("Se inicia el alta del usuario invitado con correo electrónico: " + invLogin.getEmail());
			
			PfRequestsDTO invitedRequest = requestBO.existsInvRequestsByUserMail(invLogin.getEmail());
			
			PfInvitedUsersDTO invitedUser = createInvitedUser(invLogin, invitedRequest);
			PfUsersDTO newUser = createUser(invLogin, invitedRequest);
						
			ArrayList<PfUsersProfileDTO> userProfiles = generaPerfilesUsuario();			
			ArrayList<PfUsersEmailDTO> userMails = agregaeEmails(invLogin);
			
			userAdmBO.saveUser(null, newUser, userProfiles, null, null, userMails);
			userAdmBO.saveInvitedUser(invitedUser);
			
			Set<PfSignLinesDTO> signLines = generarLineasFirma(newUser, invitedRequest);			
			Set<PfRequestTagsDTO> reqTags = generarEtiquetasPeticion(newUser, invitedRequest);
			
			invitedRequest.setLaccepted(true);
			requestBO.saveRequest(invitedRequest, signLines, reqTags);
			
		} catch (RuntimeException e) {
			log.error("Error al crear el usuario invitado con correo electrónico: "+ invLogin.getEmail(), e);
			result.getModelMap().addAttribute("errorMessage", Constants.MSG_GENERIC_ERROR);
			result.addObject("invitedForm", invLogin);
			result.setViewName("guest");
			return result;
		}
		
		Login login = new Login();
		login.setError("display: none;");
		login.setErrorClave("display: none;");
		login.setInfo("display: ;");
		
		// command object
		result.addObject("loginForm", login);
		result.setViewName("login");
		return result;
	}

	public PfUsersDTO createUser(InvitedLogin invLogin, PfRequestsDTO invitedRequest){
		
		PfUsersDTO newUser = new PfUsersDTO();
		String creationUserPK = invitedRequest.getCcreated();
		PfUsersDTO creationUser = userAdmBO.getUserByPK(Long.parseLong(creationUserPK));
		
		newUser.setCidentifier(invLogin.getDni());
		newUser.setDname(invLogin.getName());
		newUser.setDsurname1(invLogin.getSurname1());
		newUser.setDsurname2(invLogin.getSurname2());
		newUser.setLvalid(true);
		newUser.setLvisible(false);
		newUser.setCtype(Constants.USER.toUpperCase());
		newUser.setLNotifyPush(false);
		newUser.setLshownotifwarning(false);
		newUser.setPfProvince(creationUser.getPfProvince());
		
		return newUser;
	}
	
	public PfInvitedUsersDTO createInvitedUser (InvitedLogin invLogin, PfRequestsDTO invitedRequest){
		
		PfInvitedUsersDTO invitedUser = invitedRequest.getInvitedUser();			
		
		invitedUser.setcDni(invLogin.getDni());
		invitedUser.setcName(invLogin.getName());
		invitedUser.setcSurname1(invLogin.getSurname1());
		invitedUser.setcSurname2(invLogin.getSurname2());
		
		return invitedUser;
	}
	
	public ArrayList<PfUsersProfileDTO> generaPerfilesUsuario (){
		/**
		 * 
		 * **/
		//Perfiles de usuario
		PfProfilesDTO acceso = (PfProfilesDTO) admServiceBO.getProfile(Constants.C_PROFILES_ACCESS);
		PfProfilesDTO firma = (PfProfilesDTO) admServiceBO.getProfile(Constants.C_PROFILES_SIGN);
		//PfProfilesDTO redaction = (PfProfilesDTO) admServiceBO.getProfile(Constants.C_PROFILES_REDACTION);
		PfUsersProfileDTO perfilAcceso = new PfUsersProfileDTO();
		perfilAcceso.setFstart(new Date());
		perfilAcceso.setPfProfile(acceso);
		PfUsersProfileDTO perfilFirma = new PfUsersProfileDTO();
		perfilFirma.setFstart(new Date());
		perfilFirma.setPfProfile(firma);
//		PfUsersProfileDTO perfilRedaction = new PfUsersProfileDTO();
//		perfilRedaction.setFstart(new Date());
//		perfilRedaction.setPfProfile(redaction);
		
		ArrayList<PfUsersProfileDTO> userProfiles = new ArrayList<>();
		userProfiles.add(perfilFirma);
		userProfiles.add(perfilAcceso);
		//userProfiles.add(perfilRedaction);
		
		return userProfiles;
	}
	
	public ArrayList<PfUsersEmailDTO> agregaeEmails (InvitedLogin invLogin){
		
		//Correo electrónico de usuario
		PfUsersEmailDTO userMail = new PfUsersEmailDTO();
		userMail.setDemail(invLogin.getEmail());
		userMail.setLnotify(true);
		ArrayList<PfUsersEmailDTO> userMails = new ArrayList<>();
		userMails.add(userMail);
		
		return userMails;
	}
	
	public Set<PfSignLinesDTO> generarLineasFirma (PfUsersDTO newUser, PfRequestsDTO invitedRequest){
		
		Set<PfSignLinesDTO> signLines = invitedRequest.getPfSignsLines();
		Iterator<PfSignLinesDTO> signLinesIt = signLines.iterator();
		while(signLinesIt.hasNext()){
			PfSignLinesDTO signLine = signLinesIt.next();
			Set<PfSignersDTO> signers = signLine.getPfSigners();
			Iterator<PfSignersDTO> signersIt = signers.iterator();
			while(signersIt.hasNext()){
				PfSignersDTO signer = signersIt.next();
				signer.setPfUser(newUser);
			}
		}		
		return signLines;
	}
	
	public Set<PfRequestTagsDTO> generarEtiquetasPeticion (PfUsersDTO newUser, PfRequestsDTO invitedRequest){
		
		Set<PfRequestTagsDTO> reqTags = invitedRequest.getPfRequestsTags();
		Iterator<PfRequestTagsDTO> reqTagsIt = reqTags.iterator();
		while(reqTagsIt.hasNext()){
			PfRequestTagsDTO reqTag = reqTagsIt.next();
			reqTag.setPfUser(newUser);
		}
		return reqTags;
	}
}
