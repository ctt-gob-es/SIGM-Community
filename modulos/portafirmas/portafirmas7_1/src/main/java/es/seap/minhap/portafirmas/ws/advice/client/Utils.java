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

package es.seap.minhap.portafirmas.ws.advice.client;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Utils {

	private static Utils instance;

	public static synchronized Utils getInstance() {
		if (instance == null) {
			instance = new Utils();
		}
		return instance;
	}

	public Map<String, String> loadProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		ResourceBundle resource = ResourceBundle.getBundle("ws");
		Enumeration<String> enumEtiq = resource.getKeys();
		while (enumEtiq.hasMoreElements()) {
			String clave = enumEtiq.nextElement();
			String valor = resource.getString(clave);
			properties.put(clave, valor);
		}
		resource = null;
		return properties;
	}
}
