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

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Group;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ErrorsAndWarnings;

@Component
public class GroupValidator {

	@Resource(name = "messageProperties")
	private Properties messages;


	public void validateGroup(Group group, ErrorsAndWarnings errorsAndWarnings) {
		if(Util.esVacioONulo(group.getCode())) {
			errorsAndWarnings.getErrors().add(String.format(messages.getProperty("field.required"), messages.getProperty("code")));
		} else if(Util.vacioSiNulo(group.getCode()).length() > 255) {
			errorsAndWarnings.getErrors().add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("code"), 255));
		}
		if(Util.esVacioONulo(group.getDescription())) {
			errorsAndWarnings.getErrors().add(String.format(messages.getProperty("field.required"),messages.getProperty("description")));
		} else if(Util.vacioSiNulo(group.getDescription()).length() > 255) {
			errorsAndWarnings.getErrors().add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("description"), 255));
		}
	}

}
