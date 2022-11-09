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

package es.seap.minhap.portafirmas.utils;

public class AuthenticatorConstants {

	// Authenticator properties names
	
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_TYPE = "authenticator.impl";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_IMPL_AFIRMA5 = "authenticator.impl.afirma5";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_APPLICATION = "authenticator.afirma5.application";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_MAPPING = "authenticator.afirma5.mapping";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_MAPPING_AFIRMA5 = "authenticator.afirma5.mapping.afirma5";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_PROTOCOL = "authenticator.afirma5.protocol";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_ENDPOINT = "authenticator.afirma5.endpoint";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SUFIX = "authenticator.afirma5.sufix";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_PORT = "authenticator.afirma5.port";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE = "authenticator.afirma5.trustedstore";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_MODE = "authenticator.afirma5.security.mode";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE_PASS = "authenticator.afirma5.trustedstore.pass";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_SERVER_HTTP_USER = "authenticator.afirma5.http.user";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_SERVER_HTTP_PASS = "authenticator.afirma5.http.pass";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_USER = "authenticator.afirma5.security.usertoken.user";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_PASSWORD = "authenticator.afirma5.security.usertoken.password";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_PASSWORD_TYPE = "authenticator.afirma5.security.usertoken.passwordType";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_KEYSTORE = "authenticator.afirma5.security.keystore";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_KEYSTORE_TYPE = "authenticator.afirma5.security.keystore.type";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_KEYSTORE_PWD = "authenticator.afirma5.security.keystore.password";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_CERT_ALIAS = "authenticator.afirma5.security.keystore.cert.alias";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AUTHENTICATOR_AFIRMA5_SECURITY_CERT_PWD = "authenticator.afirma5.security.keystore.cert.password";

	// DB Parameters names
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_TYPE = "FIRMA.TIPO";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_IMPL_AFIRMA5 = "FIRMA.IMPL.AFIRMA5";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_APPLICATION = "FIRMA.APLICACION";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_MAPPING = "FIRMA.AFIRMA5.MAPPING";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_MAPPING_AFIRMA5 = "FIRMA.AFIRMA5.MAPPING.AFIRMA5";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_URL = "FIRMA.URL";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_TRUSTEDSTORE = "FIRMA.TRUSTEDSTORE";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_TRUSTEDSTORE_PASS = "FIRMA.TRUSTEDSTORE.PASS";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_MODE = "FIRMA.SECURITY.MODE";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_SIGNATURE_FORMAT = "FIRMA.SIGNATURE.FORMAT";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_HASH_ALGORITHM = "FIRMA.HASH.ALGORITHM";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_SERVER_CERT_ALIAS = "FIRMA.SERVER.CERT.ALIAS";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_SERVER_HTTP_USER = "FIRMA.SERVER.HTTP.USER";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_SERVER_HTTP_PASS = "FIRMA.SERVER.HTTP.PASS";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_USER = "FIRMA.SECURITY.USER";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_PASSWORD = "FIRMA.SECURITY.PASSWORD";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_PASSWORD_TYPE = "FIRMA.SECURITY.PASSWORD.TYPE";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_KEYSTORE = "FIRMA.SECURITY.KEYSTORE";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_KEYSTORE_PWD = "FIRMA.SECURITY.KEYSTORE.PWD";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_KEYSTORE_TYPE = "FIRMA.SECURITY.KEYSTORE.TYPE";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_CERT_ALIAS = "FIRMA.SECURITY.CERT.ALIAS";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String PARAM_AFIRMA5_SECURITY_CERT_PWD = "FIRMA.SECURITY.CERT.PWD";
	 /**
     *  El valor de esta constante es {@value}.
     */
	public static final String AFIRMA_IMPL = "afirma5";
}
