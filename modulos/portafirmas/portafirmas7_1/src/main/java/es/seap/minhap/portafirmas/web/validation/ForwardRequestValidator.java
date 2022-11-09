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

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.web.beans.RequestForward;

/**
 * @author juanmanuel.delgado
 *
 */
@Component
public class ForwardRequestValidator implements Validator {

	@Autowired
	private RequestBO requestBO;
	
	@Resource(name = "messageProperties")
	private Properties messages;

	@Override
	public boolean supports(Class<?> clazz) {
		return RequestForward.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		RequestForward requestForward = (RequestForward) target;

		if (!"cancelar".equals(requestForward.getAction()) && (requestForward.getSigners() == null || requestForward.getSigners().isEmpty())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "signers", "field.required.signers");
		}

	}
	
	
	/**
	 * @param forward
	 * @param errors
	 * @return
	 */
	public List<UserEnvelope> validate(RequestForward forward, List<String> errors) {
		List<UserEnvelope> destinatarios = null;
		if(missingSigner(forward)) {
			errors.add(messages.getProperty("receiver.required"));
		} else {
			destinatarios = requestBO.getUserListFromString(forward.getSigners(), forward.getSignLinesConfig(), false, null, forward.getSignlinesAccion() );
			for(int i = 0; i< destinatarios.size(); i++) {
				if(!destinatarios.get(i).isValid()) {
					if(Util.esVacioONulo(destinatarios.get(i).getDname())) {
						errors.add(messages.getProperty("receiver.required"));
						break;
					}
					errors.add(String.format(messages.getProperty("receiver.unknown"), destinatarios.get(i).getDname()));
				}
			}
		}
		return destinatarios;
	}

	private boolean missingSigner(RequestForward forward) {
		boolean missing = false;
		String[] signersArray = forward.getSigners().trim().toUpperCase().split(",");
		String[] signLinesConfigArray = new String[0];
		if (forward.getSignLinesConfig() != null) {
			signLinesConfigArray = forward.getSignLinesConfig().trim().toUpperCase().split(",");
		}
		if(forward.getSigners().startsWith(",") || signersArray.length != signLinesConfigArray.length){
			missing = true;
		}
		return missing;
	}
	

}
