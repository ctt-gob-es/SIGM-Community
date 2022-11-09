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

package es.seap.minhap.portafirmas.utils.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;

public class BDLoginServiceImpl implements LoginService {

	private final static String BD_USER = "dni";
	private final static String BD_PASS = "password";

	private Logger log = Logger.getLogger(BDLoginServiceImpl.class);

	@Autowired
	private BaseDAO baseDAO;
	/**
	 * {@link es.seap.minhap.portafirmas.utils.login.LoginService#check(String, String)}
	 */
	public LoginServiceResponse check(String user, String password) {
		log.info("BD Authentication init...");
		if (user == null || user.equals("")) {
			throw new IllegalArgumentException("User is null!");
		}
		LoginServiceResponse result = new LoginServiceResponse();
		PfUsersDTO userDTO = null;
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(BD_USER, user);
		log.debug("User: " + user);
		parameters.put(BD_PASS, password);
		log.debug("Password: *****");
		userDTO = (PfUsersDTO) baseDAO.queryElementMoreParametersWithOutLog(
				"request.userProfilesDniPass", parameters);
		if (userDTO != null) {
			result.setIdentifier(userDTO.getCidentifier());
			log.debug("Identifier: " + result.getIdentifier());
			result.setStatus(LoginServiceConfiguration.RESPONSE_OK);
		} else {
			result.setStatus(LoginServiceConfiguration.RESPONSE_NOT_FOUND);
		}
		log
				.info("BD Authentication finish. Status code: "
						+ result.getStatus());
		return result;
	}

}
