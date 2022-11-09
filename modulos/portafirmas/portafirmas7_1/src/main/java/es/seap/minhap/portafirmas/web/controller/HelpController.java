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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;

@Controller
@RequestMapping("help")
public class HelpController {

	@Autowired
	private UserAdmBO userAdmBO;

	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/handbook", method = RequestMethod.POST)
	public void downloadHandbook(@RequestParam("handbookId") final String handbookId,
			final HttpServletResponse response) throws IOException {
		try {

			boolean isAdministrator = false;
			boolean isAdminSeat = false;
			/*
			 *  Se obtiene el usuario conectado, teniendo en cuenta que esta petición
			 *  se puede recibir aún estando el usuario sin autenticar
			 */
			PfUsersDTO userLogDTO = null;
			Object authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication instanceof UserAuthentication) {
				userLogDTO = (PfUsersDTO)((UserAuthentication) authentication).getPrincipal();
				isAdministrator = userAdmBO.isAdministrator(userLogDTO.getPfUsersProfiles());
				isAdminSeat = userAdmBO.isAdminSeat(userLogDTO.getPfUsersProfiles());
			}

			OutputStream out = null;
			InputStream in = null;

			File file = new File(Constants.PATH_DOC + handbookId + "_manual.pdf");

			String handbookName = null;
			if (Constants.ID_USER_HANDBOOK.equals(handbookId)) {
				handbookName = Constants.NAME_USER_HANDBOOK;
			} else if(Constants.ID_ADM_HANDBOOK.equals(handbookId) && isAdministrator) {
				handbookName = Constants.NAME_ADM_HANDBOOK;
			} else if(Constants.ID_ADM_SEAT_HANDBOOK.equals(handbookId) && isAdminSeat) {
				handbookName = Constants.NAME_SEAT_HANDBOOK;
			} else if(Constants.ID_GUIA_RAPIDA_HANDBOOK.equals(handbookId)) {
				handbookName = Constants.NAME_GUIA_RAPIDA_HANDBOOK;
			}

			handbookName +=  "." + Constants.EXPORT_PDF_TYPE;
			
			response.setHeader("Content-Disposition", "attachment;filename=\"" + handbookName + "\"");
			response.setHeader("Content-Transfer-Encoding","binary");
			response.setHeader("Expires","0");
			response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma","public");
			response.setContentType("application/pdf");

			out = response.getOutputStream();
			in = new BufferedInputStream(new FileInputStream(file));

			IOUtils.copy(in, out);

			in.close();
			out.flush();
			out.close();

		} catch (NullPointerException e) {
			log.error("Error al obtener el manual de usuario");
			response.sendError(500, Constants.MSG_GENERIC_ERROR);
		}
	}

}