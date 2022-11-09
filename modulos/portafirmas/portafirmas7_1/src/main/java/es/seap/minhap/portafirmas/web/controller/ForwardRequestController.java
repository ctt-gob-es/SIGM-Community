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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.ForwardRequestBO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.RedactionBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.RestrictionBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.business.beans.RedactionUploadedDocuments;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.envelope.SeatEnvelope;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.web.beans.RequestForward;
import es.seap.minhap.portafirmas.web.beans.Signer;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UserSelection;
import es.seap.minhap.portafirmas.web.validation.ForwardRequestValidator;

/**
 * @author juanmanuel.delgado
 *
 */
@Controller
@RequestMapping("forwardRequest")
public class ForwardRequestController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private ForwardRequestBO forwardRequestBO;
	
	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private ProvinceBO provinceBO;

	@Autowired
	private ForwardRequestValidator forwardRequestValidator;
	
	@Autowired
	private TagBO tagBO;
	
	@Autowired
	private NoticeBO noticeBO;
	
	@Resource
	private RedactionUploadedDocuments uploadedFiles;
	
	@Autowired
	private RedactionBO redactionBO;
	
	@Autowired
	private RestrictionBO restrictionBO;

	/**
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(forwardRequestValidator);
	}

	/**
	 * Método que inicializa el formulario
	 * @param model
	 * @param requestId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model, @RequestParam(value = "requestId") final String requestId) {

		log.debug("Se inicializa el formulario de reenvío de peticiones firmadas.");

		// Se crea el modelo del formulario
		RequestForward forwardRequest = new RequestForward();
		
		forwardRequest.setRequestId(requestId);
		
		PfRequestTagsDTO requestTag = requestBO.queryRequestTagByHash(requestId);
		
		forwardRequestBO.configurarValoresIniciales(forwardRequest, requestTag.getPfRequest());
		
		cargarCombos(model);
		
		model.addAttribute("forwardRequest", forwardRequest);

		return "forwardRequest";
	}
	
	private void cargarCombos(ModelMap model) {
		// Se cargan los formatos de firma
		forwardRequestBO.loadSignatureFormats(model);

		// Se cargan los niveles de importancia
		forwardRequestBO.loadImportanceLevels(model);
	}

	/**
	 * Método que procesa el envío del formulario al servidor
	 * @param forwardRequest Modelo de datos del formulario
	 * @param bindingResult
	 * @param status
	 * @return Modelo de datos de respuesta
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("forwardRequest") @Valid RequestForward forwardRequest,
									  BindingResult bindingResult) throws IOException, CustodyServiceException {
		ModelAndView result = new ModelAndView();
		try {
			if (bindingResult.hasErrors() && "enviar".equals(forwardRequest.getAction())) {
				uploadedFiles.removeAlls();
				cargarCombos(result.getModelMap());
				result.addObject("documentTypeList", applicationBO.queryDocumentTypePfirma());
				result.addObject("forwardRequest", forwardRequest);
				result.setViewName("forwardRequest");
				return result;
			}
			
			if ("enviar".equals(forwardRequest.getAction())) {
				log.debug("Se reenvía la petición con hash " + forwardRequest.getRequestId());
				//Recuperamos los datos de la Petición
				PfRequestsDTO request = requestBO.queryRequestHash(forwardRequest.getRequestId());
				
				Signer ultimoUsuario = null;
				
				for (PfSignLinesDTO signLineDTO : request.getPfSignsLinesList()) {
					// Se recuperan los firmantes de la línea de firma
					boolean isTerminate = false;
					for (PfSignersDTO signerDTO : signLineDTO.getPfSigners()) {
						Signer signer = new Signer();
						signer.setPfUser(signerDTO.getPfUser());
						// Se recupera el estado de la petición por petición, usuario y linea de firma
						PfRequestTagsDTO stateRequestTag = tagBO.queryStateUserSignLine(request, signerDTO.getPfUser(),signLineDTO);
						String status = stateRequestTag.getPfTag().getCtag();
						isTerminate = status.equals(Constants.C_TAG_SIGNED) || status.equals(Constants.C_TAG_PASSED);
						// Se deja cargado el estado para cada linea de firma y cada usuario por si fuera necesario en el futuro.
						signer.setStateTag(stateRequestTag.getPfTag());
						ultimoUsuario = signer;
					}
				}
				
				Boolean asignarDocumentos = Boolean.FALSE;
				
				if("on".equalsIgnoreCase(forwardRequest.getNameCheckDeNuevosComentariosYAnexos())){
					asignarDocumentos = Boolean.TRUE;
				}
				
				List<String> errors = new ArrayList<String>();
				List<UserEnvelope> destinatarios = forwardRequestValidator.validate(forwardRequest, errors);
					
				forwardRequestBO.changeStateToExpired(request);
				
				requestBO.forwardRequest(request, destinatarios, applicationBO.queryStateTags(), ultimoUsuario.getPfUser().getPrimaryKey(), asignarDocumentos);	
				
				requestBO.markAsUnReadForRemitters(request);
				
				// Send notice
				List<PfUsersDTO> forwardedUsers = requestBO.getForwarderUsers(destinatarios);
				noticeBO.noticeForwardRequest(request, forwardedUsers);
			}
			result.setViewName("redirect:/inbox");
			return result;
		} catch (RuntimeException e) {
			log.error("Error al hacer el reenvío de petición: ", e);
			result.getModelMap().addAttribute("errorMessage", Constants.MSG_GENERIC_ERROR);
			result.getModelMap().addAttribute("timeError", new Date(System.currentTimeMillis()));
			result.setViewName("error");
			return result;
		}
	}
	
	/**
	 * Método que carga la ventana modal que permite seleccionar los usuarios destinatarios de la petición
	 * @param signers 
	 * @param signLinesConfig
	 * @param userNameFilter
	 * @param userTypeFilter
	 * @param userSeatFilter
	 * @return
	 */
	@RequestMapping(value = "selectUsers")
	@ResponseBody public  UserSelection seleccionarDestinatarios(@RequestParam(value = "signers") String signersBase64,
												  @RequestParam(value = "signLinesConfig") String signLinesConfig,
												  @RequestParam(value = "signLinesAccion") String signLinesAccion,
												  @RequestParam(value = "userNameFilter") final String userNameFilter,
												  @RequestParam(value = "userTypeFilter") final String userTypeFilter,
												  @RequestParam(value = "userSeatFilter") final String userSeatFilter,
												  @RequestParam(value = "firstInvocation", required=false) final Boolean firstInvocation
												  ) {
		String signers = null;
		try {
			signers=java.net.URLDecoder.decode(new String(org.apache.commons.codec.binary.Base64.decodeBase64(signersBase64)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		UserSelection userSelection = new UserSelection();

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
					.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// Se obtiene la sede del usuario autenticado
		String codSede = null;
		if (user.getPfProvince() != null) {
			codSede = user.getPfProvince().getCcodigoprovincia();
		}

		// Se obtienen las sedes de los usuarios
		loadUserSeats(userSelection, codSede);

		// Se cargan los usuarios a mostrar
		if(firstInvocation==null || !firstInvocation.booleanValue()){
			requestBO.loadUsersToPick(userSelection, user, codSede, signers, signLinesConfig, userNameFilter, userTypeFilter, userSeatFilter, null, firstInvocation, signLinesAccion);
		}

		return userSelection;
	}
	
	/**
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocompleteSigners")
	@ResponseBody public List<UserAutocomplete> autocompleteSigners(@RequestParam(value = "term") final String term) {

		List<UserAutocomplete> results = new ArrayList<UserAutocomplete>();

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
					.getContext().getAuthentication();
		 PfUsersDTO user = authorization.getUserDTO();

		// Se obtiene la sede del usuario autenticado
		String codSede = null;
		if (user.getPfProvince() != null) {
			codSede = user.getPfProvince().getCcodigoprovincia();
		}

		// Se toma para el filtro el último valor introducido
		String[] values = term.split(",");

		// Se filtran los usuarios en base a la búsqueda
		List<AbstractBaseDTO> usersBusqueda = requestBO.queryUsersComplete(values[values.length-1].trim(), codSede);

		// Se filtran los usuarios si el usuario tiene restringidos los destinatarios
		List<AbstractBaseDTO> users = new ArrayList<AbstractBaseDTO>();
		List<PfUsersDTO> validUserList = restrictionBO.queryUserRestrict(user);
		if(!validUserList.isEmpty()){
			for (AbstractBaseDTO userAux : usersBusqueda){
				PfUsersDTO userDTO = (PfUsersDTO) userAux;
				boolean encontrado = false;
				for (PfUsersDTO userValidDTO : validUserList){
					if (userDTO.getPrimaryKey().equals(userValidDTO.getPrimaryKey())){
						encontrado = true;
					}
				}
				if (encontrado){
					users.add(userAux);
				}
			}
		} else {
			users.addAll(usersBusqueda);
		}
		
		// Se convierten los resultados
		for (AbstractBaseDTO dto : users) {			
			results.add(new UserAutocomplete((PfUsersDTO) dto));
		}

		return results;
	}
	
	/**
	 * Método que carga las sedes de los usuarios
	 * @param userSelection 
	 * @param seatCode
	 */
	public void loadUserSeats(UserSelection userSelection, String seatCode) {
		List<AbstractBaseDTO> provinceList = provinceBO.getVisibleProvinces(seatCode);
		List<SeatEnvelope> seatList = new LinkedList<SeatEnvelope>();
		for (AbstractBaseDTO province : provinceList) {
			SeatEnvelope newSeat = new SeatEnvelope();
			PfProvinceDTO seat = (PfProvinceDTO) province;
			newSeat.setCode(seat.getCcodigoprovincia());
			newSeat.setName(seat.getCnombre());
			seatList.add(newSeat);
		}
		userSelection.setSeatList(seatList);
	}
	
	/**
	 * Método que carga los nombres de los firmantes según su id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getSigners")
	public @ResponseBody List<UserAutocomplete> getSigners(
			@RequestParam(value = "signers") final String signers) throws Exception {

		List<UserAutocomplete> result = new ArrayList<UserAutocomplete>();

		if (!signers.isEmpty()){
			String [] signersList = signers.split(",");
			for (String userSigner : signersList) {			
				PfUsersDTO user = (PfUsersDTO) redactionBO.getUser(userSigner);
				result.add(new UserAutocomplete((PfUsersDTO) user));
			}
		}
		return result;
	}
}
