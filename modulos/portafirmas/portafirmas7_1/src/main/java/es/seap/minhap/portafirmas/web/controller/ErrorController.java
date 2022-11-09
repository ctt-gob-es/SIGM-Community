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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.seap.minhap.portafirmas.utils.Constants;

@Controller
@RequestMapping("error")
class ErrorController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String forbiddenHandler(HttpServletRequest request, HttpServletResponse response, Model model) {
		String errorTitle = Constants.TITLE_GENERIC_ERROR;
		String errorMessage = Constants.MSG_GENERIC_ERROR;
		
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}
		if(statusCode != null && statusCode.equals(Constants.RESPONSE_FORBIDDEN )) {
			errorTitle = Constants.TITLE_FORBIDDEN_ERROR;
			errorMessage = Constants.MSG_FORBIDDEN_ERROR;
		}

		model.addAttribute("errorTitle", errorTitle); 
		model.addAttribute("errorMessage", errorMessage); 
		return "error";
	}

}