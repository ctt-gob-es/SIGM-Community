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
/**
 * Realiza test de conexi&oacute;n a las implementaciones de la interfaz {@link es.seap.minhap.portafirmas.utils.login.LoginService}
 * @author daniel.palacios
 *
 */
public class LoginServiceTest {

	public static void main(String[] args) {
		LdapTest();
//		BDTest();
	}
	/**
	 * Reliza un test de conexi&oacute;n a la implementaci&oacute;n para LDAP de {@link es.seap.minhap.portafirmas.utils.login.LoginService}
	 */
	public static void LdapTest(){
		LoginServiceConfiguration lsc = new LoginServiceConfiguration();
		lsc.setLoginType(LoginServiceConfiguration.LOGIN_TYPE_LDAP);
		lsc.setLdapUrl("ldap://ldap.guadaltel.es:389");
		lsc.setLdapIdAttributeName("uid=$1");
		lsc.setLdapBaseDn("uid=$1,ou=Personal,dc=guadaltel,dc=es");
		lsc.setIdentifierAttributeName("mail");
		LoginService ls = LoginServiceFactory.create(lsc);
		LoginServiceResponse res= ls.check("fjcv", "brevitas");
		System.out.println(res.getStatus()+" "+res.getIdentifier());
	}
	/**
	 * Reliza un test de conexi&oacute;n a la implementaci&oacute;n para Base de Datos de {@link es.seap.minhap.portafirmas.utils.login.LoginService}
	 */
	public static void BDTest(){
		LoginServiceConfiguration lsc = new LoginServiceConfiguration();
		lsc.setLoginType(LoginServiceConfiguration.LOGIN_TYPE_DB);
		LoginService ls = LoginServiceFactory.create(lsc);
		LoginServiceResponse res= ls.check("75809483B", "123");
		System.out.println(res.getStatus()+" "+res.getIdentifier());
	}

}
