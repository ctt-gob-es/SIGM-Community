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
import es.seap.minhap.portafirmas.web.beans.UserJob;

@Component
public class UserJobValidator {

	@Resource(name = "messageProperties")
	private Properties messages;

	public void validate(UserJob userJob, ArrayList<String> errors) {
		if(Util.esVacioONulo(userJob.getJobId())) {
			errors.add(messages.getProperty("field.userJob.job.required"));
		}
		if(Util.esVacioONulo(userJob.getfStart())) {
			errors.add(messages.getProperty("field.userJob.fstart.required"));
		} else {
			Date startDate = Util.getInstance().stringToDate(userJob.getfStart());
			Date endDate = null;
			if(!Util.esVacioONulo(userJob.getfEnd())) {
				endDate = Util.getInstance().stringToDate(userJob.getfEnd());
			}
			if(!Util.getInstance().checkDate(startDate, endDate)) {
				errors.add(messages.getProperty("errorFEndBeforeFStart"));
			}
		}
	}

}
