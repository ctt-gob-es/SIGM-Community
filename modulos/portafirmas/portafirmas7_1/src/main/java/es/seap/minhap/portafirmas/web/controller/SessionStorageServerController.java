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

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.web.controller.requestsandresponses.VariableSessionRequest;

@Controller
@RequestMapping("sessionStorageServer")
public class SessionStorageServerController {

	@RequestMapping(value = "/addSessionStorageVariable", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String addSessionStorageVariable(@RequestBody VariableSessionRequest variable, HttpSession sessionObj) throws IOException {
		sessionObj.setAttribute(variable.getNombre(), variable.getValor());
		return "{}";
	}
	
	@RequestMapping(value = "/getSessionStorageVariable", method = RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String getSessionStorageVariable(@RequestBody VariableSessionRequest variable, HttpSession sessionObj) throws IOException {
		return (String) sessionObj.getAttribute(variable.getNombre());
		
	}
	
	@RequestMapping(value = "/deleteSessionStorageVariable", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String deleteSessionStorageVariable(@RequestBody VariableSessionRequest variable, HttpSession sessionObj) throws IOException {
		sessionObj.removeAttribute(variable.getNombre());
		return "{}";
	}
	
}
