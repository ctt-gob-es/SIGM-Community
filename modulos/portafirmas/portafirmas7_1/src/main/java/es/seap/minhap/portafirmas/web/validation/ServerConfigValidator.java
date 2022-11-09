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
import es.seap.minhap.portafirmas.web.beans.ServerConfig;

@Component
public class ServerConfigValidator {

	@Resource(name = "messageProperties")
	private Properties messages;

	public void validate(ServerConfig serverConfig, ArrayList<String> errors) {
		if(Util.esVacioONulo(serverConfig.getcConfiguration())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("code")));
		}
		if(Util.vacioSiNulo(serverConfig.getcConfiguration()).length() > 30) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("code"), 30));			
		}
		if(Util.esVacioONulo(serverConfig.getServerPk())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("serverLabel")));
		}
	}

}
