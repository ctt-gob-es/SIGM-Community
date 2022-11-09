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

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.FireBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.fire.FireRequest;
import es.seap.minhap.portafirmas.web.beans.fire.FireTransaction;

/**
 * @author arturo.tejero, domingo.sanchez
 *
 */
@Controller
@RequestMapping("fire")
public class FireController {

	protected final Log log = LogFactory.getLog(getClass());
	
	private final static String FIRE_ID_TRANSACTION_ATTRIBUTE = "FIReIdTransaccion";
	private final static String FIRE_HASHES_ATTRIBUTE = "FIReHashes";
	
	@Autowired
	private FireBO fireBO;

	@Resource(name="messageProperties")
	private Properties messageProperties;

	/**
	 * Método que obtiene las peticiones seleccionadas para firmar y dar visto bueno.
	 * Prepara su configuración de firma para la firma mediante FIRe
	 * 
	 * @param requestsIds  Identificadores de las peticiones a firmar
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getFIReTransaction", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)	
	public String getFIReTransaction(@RequestParam("requestsIds") String[] requestsIds, final HttpServletRequest request) throws Exception {

		String urlRedirect = null;
		String transactionId = null;
		try {

			FireTransaction fireTransaction = fireBO.getTransactionWeb(requestsIds, obtenerUsuario());
			
			urlRedirect = fireTransaction.getUrlRedirect();
			request.getSession().setAttribute(FIRE_ID_TRANSACTION_ATTRIBUTE, fireTransaction.getTransactionId());
			request.getSession().setAttribute(FIRE_HASHES_ATTRIBUTE, fireTransaction.getHashes());

		} catch (Exception e) {
			log.error("Error al firmar con FIRe, transacción: " + transactionId, e);
			throw e;
		}

		return urlRedirect;
	}

	/**
	 * Método que invoca FIRe para guardar en base de datos las firmas realizadas
	 * 
	 * @param response
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/signFIReCloud", method=RequestMethod.GET)
	public ModelAndView signFIReCloud(final HttpServletResponse response, final HttpServletRequest request, ModelAndView model) {
		
		String transactionId = null;
		try {
			
			transactionId = (String) request.getSession().getAttribute(FIRE_ID_TRANSACTION_ATTRIBUTE);
			String[] requestIds = (String[]) request.getSession().getAttribute(FIRE_HASHES_ATTRIBUTE);
			
			List <FireRequest> listaPeticiones = fireBO.signCloud(transactionId, requestIds, obtenerUsuario());

			model.addObject("mostrarResultFirma", true);
			model.addObject("firmaLista", listaPeticiones);
			model.setViewName("fire/resultado");

		} catch (Exception e) {
			log.error("Error al salvar la petición de la transacción: " + transactionId, e);
			model.addObject("errorMessage", Constants.MSG_GENERIC_ERROR);
			model.addObject("errorTime", new Date(System.currentTimeMillis()));
			model.setViewName("fire/error");
		}

		return model;
	}

	/**
	 * Método para procesar la respuesta de FIRe cuando invoca la url de error.
	 * En el fire.properties está especificado en el parámetro "web.redirectErrorUrl"
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/error", method=RequestMethod.GET)
	public String error(Model model, final HttpServletRequest request) {
		PfUsersDTO usuarioConectado = obtenerUsuario();
		String idTransaccion = (String) request.getSession().getAttribute(FIRE_ID_TRANSACTION_ATTRIBUTE);
		try {
			fireBO.obtenerError(idTransaccion, usuarioConectado);
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage()); 
			model.addAttribute("errorTime", new Date(System.currentTimeMillis())); 
		}
		
		return "fire/error";
	}

	private PfUsersDTO obtenerUsuario() {
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return authorization.getUserDTO();
	}

}