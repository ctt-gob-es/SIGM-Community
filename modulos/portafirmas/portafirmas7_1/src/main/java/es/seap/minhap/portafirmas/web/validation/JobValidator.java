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
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.User;

@Component
public class JobValidator {

	@Resource(name = "messageProperties")
	private Properties messages;

	public void validate(User job, ArrayList<String> errors) {
		if(Util.esVacioONulo(job.getNif())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("code")));
		} else if(Util.vacioSiNulo(job.getNif()).length() > 30) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("code"), 30));
		}
		if(Util.esVacioONulo(job.getName())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("description")));
		} else if(Util.vacioSiNulo(job.getName()).length() > 50) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("code"), 50));
		}
		if(Util.esVacioONulo(job.getProvince())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("province")));
		}
	}

}
