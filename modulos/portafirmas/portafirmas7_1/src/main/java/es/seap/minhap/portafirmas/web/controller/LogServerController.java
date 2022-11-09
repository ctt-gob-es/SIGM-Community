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
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;

@Controller
@RequestMapping("logServer")
public class LogServerController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private RequestBO requestBO;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "logError", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String logError(@RequestBody Map<String, Object> objJSON, HttpSession sessionObj) throws IOException {
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO usuarioConectado = authorization.getUserDTO();
		log.error("Ha ocurrido un error en cliente "+objJSON);
		if(objJSON!=null && objJSON.keySet()!=null){
			if(objJSON.containsKey("accionesEnServidor") && objJSON.get("accionesEnServidor")!=null && (objJSON.get("accionesEnServidor") instanceof Map)
					&& ((Map<?, ?>)objJSON.get("accionesEnServidor")).keySet()!=null){
				Set<String> llavesDeAccionesEnServidor = ((Map<String, ?>)objJSON.get("accionesEnServidor")).keySet();
				for(String llave : llavesDeAccionesEnServidor){
					Map<String, Object> accion = (Map<String, Object>) ((Map<?, ?>)objJSON.get("accionesEnServidor")).get(llave);
					if(es.seap.minhap.portafirmas.utils.Constants.ACCION_DESBLOQUEAR_REGISTRO_FIRMA.equalsIgnoreCase((String) accion.get("accion"))){
						PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash((String) ( (Map<?, ?>) accion.get("parametros")).get(es.seap.minhap.portafirmas.utils.Constants.PARAMETRO_ID_REQUEST_ACCION_DESBLOQUEAR_REGISTRO_FIRMA));
						requestBO.desbloquearPeticion(requestTagDTO.getChash(), usuarioConectado);
					}
				}
			}
		}

		return "{}";
	}

}
