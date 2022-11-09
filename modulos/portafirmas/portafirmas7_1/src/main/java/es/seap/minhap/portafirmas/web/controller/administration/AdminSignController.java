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

package es.seap.minhap.portafirmas.web.controller.administration;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.ReSignBO;
import es.seap.minhap.portafirmas.business.ReSignHistoricBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.web.beans.Paginator;
import es.seap.minhap.portafirmas.web.beans.administration.AdminSign;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;

@ Controller
@ RequestMapping("administration/adminSign")
public class AdminSignController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	ReSignBO reSignBO;
	
	@Autowired
	ReSignHistoricBO reSignHistoricBO;
	
	@Autowired
	DateComponent dateComponent;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
	
	@RequestMapping(value = "/loadSign", method = RequestMethod.POST)
	public ModelAndView loadSign(ModelMap model, @ModelAttribute ("adminSign") AdminSign adminSign) throws ClassNotFoundException, SQLException {
		try {
			log.debug("** Comienzo de loadSign");
			
			Paginator paginator = adminSign.getPaginatorResign();
			if (paginator == null) {
				paginator = new Paginator();
				adminSign.setPaginatorResign(paginator);
			}
			
			String fValidez = adminSign.getfValidez();
			Date fValidezDate = dateComponent.addMonths(new Date(), 1);
			if (!StringUtils.isEmpty(fValidez)) {
				fValidezDate = dateComponent.stringToDate(fValidez);
			} else {
				adminSign.setfValidez(dateComponent.dateToString(fValidezDate));
				adminSign.setApplicationMode(Constants.APPLICATION_MODE.HITORIC.getValue());
			}
			
			// Se cargan las peticiones pendientes de resellar
			RequestTagListDTO data = null; 
			if (Constants.APPLICATION_MODE.ACTUAL.getValue() == adminSign.getApplicationMode()) {
				data = reSignBO.getListPaginatedByValidDate(paginator, fValidezDate);
			} else {
				data = reSignHistoricBO.getListPaginatedByValidDate(paginator, fValidezDate);
			}
			adminSign.setData(data);
			adminSign.getPaginatorResign().setInboxSize((int) data.getInboxSize());
			
			//datos para paginación
			model.put("adminSign", adminSign);
			
			// Se crea/ adjunta el modelo del formulario
			return new ModelAndView("administration/adminSign", model);
		} catch (ClassNotFoundException e) {
			log.error("Error realizando busqueda de firmas para resellar: + " + e.getMessage(), e);
			throw e;
		} catch (SQLException e) {
			log.error("Error realizando busqueda de firmas para resellar: + " + e.getMessage(), e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/reSign", method = RequestMethod.POST)
	public ModelAndView reSign(ModelMap model, @ModelAttribute ("adminSign") AdminSign adminSign) throws NumberFormatException, Throwable {
		try {
			log.debug("Inicio reSign");
			
			//hacemos el resellado de la firma/s que llega
			String[] identificadores = adminSign.getIdSign().split(Constants.RESIGN_REQUEST_VAR_SEPARATOR);
			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
					.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			
			for (String idSign : identificadores) {
				if (Constants.APPLICATION_MODE.ACTUAL.getValue() == adminSign.getApplicationMode()) {
					reSignBO.reSign(Long.valueOf(idSign), user);
				} else {
					reSignHistoricBO.reSign(Long.valueOf(idSign), user);
				}
			}
		} catch (CustodyServiceException  e) {
			log.error("Error realizando el resellado de firma: + " + e.getMessage(), e);
			adminSign.setMensajeError(e.getMessage());
		} catch (EeutilException e) {
			log.error("Error realizando el resellado de firma: + " + e.getMessage(), e);
			adminSign.setMensajeError(e.getMessage());
		} catch (IOException e) {
			log.error("Error realizando el resellado de firma: + " + e.getMessage(), e);
			adminSign.setMensajeError(e.getMessage());
		} catch (Afirma5Exception e) {
			log.error("Error realizando el resellado de firma: + " + e.getMessage(), e);
			adminSign.setMensajeError(e.getMessage());
		}
		
		return loadSign(model, adminSign);
	}
	
	
}
