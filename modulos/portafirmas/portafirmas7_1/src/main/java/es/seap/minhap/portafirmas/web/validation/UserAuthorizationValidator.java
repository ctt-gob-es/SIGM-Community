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

package es.seap.minhap.portafirmas.web.validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.UserAuthorization;

@Component
public class UserAuthorizationValidator {

	@Resource(name = "messageProperties")
	private Properties messages;


	/**
	 * @param userAuthorization
	 * @param errors
	 */
	public void validate(UserAuthorization userAuthorization, ArrayList<String> errors) {
		if(Util.esVacioONulo(userAuthorization.getReceiverId())) {
			errors.add(messages.getProperty("field.authorized.required"));
		}
		if(Util.esVacioONulo(userAuthorization.getFrequest())) {
			errors.add(messages.getProperty("field.frequest.required"));
		}
		if(!Util.esVacioONulo(userAuthorization.getFrevocation())) {
			Date fRevocation = Util.getInstance().dateComplete(userAuthorization.getFrevocation(), userAuthorization.getHrevocation());
			Date ahora = new Date();
			if(fRevocation.before(ahora)) {
				errors.add(messages.getProperty("field.frevoke.before"));
			}
		}
		if(!Util.esVacioONulo(userAuthorization.getFrequest()) && !Util.esVacioONulo(userAuthorization.getFrevocation())) {
			Date fRequest = Util.getInstance().dateComplete(userAuthorization.getFrequest(), userAuthorization.getHrequest());
			Date fRevocation =  Util.getInstance().dateComplete(userAuthorization.getFrevocation(), userAuthorization.getHrevocation());
			if(fRequest.after(fRevocation)) {
				errors.add(messages.getProperty("field.frequest.after"));
			}
		}
		if(Util.vacioSiNulo(userAuthorization.getTobservations()).length() > 1000) {
			errors.add(messages.getProperty("field.observations.length"));
		}
	}


	/**
	 * @param userAuthorization
	 * @param errors
	 */
	public void validateSeatAdmin(UserAuthorization userAuthorization, ArrayList<String> errors) {
		if(Util.esVacioONulo(userAuthorization.getRemittentId())) {
			errors.add(messages.getProperty("field.remittent.required"));
		}
		validate(userAuthorization, errors);
	}

}
