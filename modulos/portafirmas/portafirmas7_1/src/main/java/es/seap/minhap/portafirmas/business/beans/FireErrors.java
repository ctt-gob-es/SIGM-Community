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

package es.seap.minhap.portafirmas.business.beans;

import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component("FireErrors")
public class FireErrors {

	@Resource(name="messageFireErrors")
	private Properties fireErrors;

	public String getErrorType(String errorCode) {

		String mensajeError = null;

		for (Entry<Object, Object> e : fireErrors.entrySet()) {
			Object oKey = e.getKey();
			if (oKey instanceof String) {
				if (((String) oKey).equalsIgnoreCase(errorCode)) {
					mensajeError = e.getValue().toString();
				}
			}
		}

		return mensajeError;
	}
}
