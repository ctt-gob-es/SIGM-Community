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

public class LoginServiceFactory {
	/**
	 * Crea un servicio de login para la configuraci&oacute;n que pasamos como par&aacute;metro,
	 * si el login es de tipo LDAP crea un servicio {@link es.seap.minhap.portafirmas.utils.login.LDAPLoginServiceImpl}
	 * si es de tipo DB crea un servicio del tipo {@link es.seap.minhap.portafirmas.utils.login.BDLoginServiceImpl}
	 * @param properties la configuraci&oacute;n del login con todos sus par&aacute;metros
	 * @return el servicio de login para la configuraci&oacute;n que pasamos como par&aacute;metro
	 * @exception IllegalArgumentException si el objeto que pasamos como par&aacute;metro no tiene
	 * configurado como tipo de login 'LDAP' o 'DB', o si el objeto pasado como parametro es nulo
	 */
	public static LoginService create(LoginServiceConfiguration properties) {
		LoginService result = null;
		if (properties != null) {
			//Si el login es de tipo LDAP crea un servicio de tipo LDAP
			if (properties.getLoginType().equals(
					LoginServiceConfiguration.LOGIN_TYPE_LDAP)) {
				result = new LDAPLoginServiceImpl(properties);
			//Si el login es de tipo DB crea un servicio de tipo base de datos
			} else if (properties.getLoginType().equals(
					LoginServiceConfiguration.LOGIN_TYPE_DB)) {
				result = new BDLoginServiceImpl();
			//en otro caso lanza una excepci&oacute;n
			} else {
				throw new IllegalArgumentException("Login type unrecognized");
			}
		//si el objeto es nulo lanza una excepci&oacute;n
		} else {
			throw new IllegalArgumentException("Login type empty");
		}
		return result;
	}

}
