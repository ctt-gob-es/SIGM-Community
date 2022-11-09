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
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.web.beans.RequestRedaction;

@Component
public class RedactionValidator implements Validator {

	@Autowired
	private RequestBO requestBO;
	
	@Resource(name = "messageProperties")
	private Properties messages;

	@Override
	public boolean supports(Class<?> clazz) {
		return RequestRedaction.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		RequestRedaction requestRedaction = (RequestRedaction) target;
		
		if (requestRedaction.getRequest() != null){
		
			if(requestRedaction.getRequest().getLinvited() == null || !requestRedaction.getRequest().getLinvited()){
				if (requestRedaction.getSigners() == null || requestRedaction.getSigners().isEmpty()) {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "signers", "field.required.signers");
				}
			}
		}
	}

	public List<UserEnvelope> validate(RequestRedaction redaction, List<String> errors) {
		List<UserEnvelope> destinatarios = null;
		if(missingSigner(redaction)) {
			errors.add(messages.getProperty("receiver.required"));
		} else {
				if(redaction.getRequest().getLinvited() != null && !redaction.getRequest().getLinvited()){
					destinatarios = requestBO.getUserListFromString(redaction.getSigners(), redaction.getSignLinesConfig(),
							redaction.getRequest().getLinvited(), redaction.getRequest().getInvitedUser(), redaction.getSignlinesAccion());
					
					if (destinatarios.isEmpty()){
						errors.add(String.format(messages.getProperty("receiverNotFound")));
					}
					
					for(int i = 0; i< destinatarios.size(); i++) {
						if(!destinatarios.get(i).isValid()) {
							if(Util.esVacioONulo(destinatarios.get(i).getDname())) {
								errors.add(messages.getProperty("receiver.required"));
								break;
							}
							errors.add(String.format(messages.getProperty("receiver.unknown"), destinatarios.get(i).getDname()));
						}
					}
				}else if(redaction.getRequest().getLinvited()!= null && redaction.getRequest().getLinvited()){
					PfRequestsDTO req = requestBO.existsInvRequestsByUserMail(redaction.getSigner());
					if(req != null){
						errors.add(messages.getProperty("error.invitedRequestWithSameEmail"));
					}					
					destinatarios = requestBO.getUserListFromString(redaction.getSigner(), redaction.getSignLinesConfig(), 
							redaction.getRequest().getLinvited(), redaction.getRequest().getInvitedUser(), redaction.getSignlinesAccion());
				}
		}
		return destinatarios;
	}

	private boolean missingSigner(RequestRedaction redaction) {
		boolean missing = false;
		if(redaction.getRequest().getLinvited() != null && redaction.getRequest().getLinvited()){
			if(Util.esVacioONulo(redaction.getSigner()) || redaction.getSigner().startsWith(",") ){
				missing = true;
			}
		}else{
			String[] signersArray = redaction.getSigners().trim().toUpperCase().split(",");
			String[] signLinesConfigArray = new String[0];
			if (redaction.getSignLinesConfig() != null) {
				signLinesConfigArray = redaction.getSignLinesConfig().trim().toUpperCase().split(",");
			}
			if(redaction.getSigners().startsWith(",") || signersArray.length != signLinesConfigArray.length){
				missing = true;
			}
		}
		return missing;
	}

}
