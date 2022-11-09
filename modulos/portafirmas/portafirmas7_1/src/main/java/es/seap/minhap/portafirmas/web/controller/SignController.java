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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Controller
@RequestMapping("sign")
public class SignController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RequestBO requestBO;


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/desbloquearPeticion")
	public @ResponseBody String desbloquearPeticion(@RequestParam(value="requestTagHash") String requestTagHash) {
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();
		requestBO.desbloquearPeticion(requestTagHash, user);
		return Boolean.TRUE.toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getFirmadoSign")
	public @ResponseBody String getFirmadoSign (final HttpServletRequest request) throws PfirmaException {

		BufferedImage bImage;
		String document = null;
		File file = null;
		
		HttpSession session = request.getSession(true);
	    String str = session.getServletContext().getRealPath("/");
		
		try {
			
			file = new File (str + "/images/firmadoSign.jpg");
			bImage = ImageIO.read(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bImage, "jpg", bos );
			byte[] data = bos.toByteArray();		
			document = Base64.encodeBase64String(data);
		} catch (IOException e) {
			log.error("Error al recuperar la imagen de Firmado: ", e);
			throw new PfirmaException("Error al recuperar la imagen de Firmado");
		}

		return document;
	}
}