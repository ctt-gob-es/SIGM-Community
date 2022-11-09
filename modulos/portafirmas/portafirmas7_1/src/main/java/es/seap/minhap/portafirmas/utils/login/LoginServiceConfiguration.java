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
 * Clase que sirve como almacen de la configuraci&oacute;n de login
 * @author daniel.palacios
 *
 */
public class LoginServiceConfiguration {
	
	// General constants
	public final static String LOGIN_TYPE_LDAP="LDAP";
	public final static String LOGIN_TYPE_DB="DB";
	public final static String DEFAULT_LOGIN_TYPE=LOGIN_TYPE_DB; 
	
	// Response Constants
	public final static int RESPONSE_OK=200;
	public final static int RESPONSE_NOT_ALLOWED=403;
	public final static int RESPONSE_NOT_FOUND=404;
	public final static int RESPONSE_INTERNAL_ERROR=500;
	
	// Ldap constants
	public final static String DEFAULT_ID_ATTRIBUTE_NAME="uid";
	public final static String DEFAULT_SECURITY_AUTHENTICACION="simple";
	
	// General properties
	private String LoginType=DEFAULT_LOGIN_TYPE;
	// DB properties
	
	// Ldap properties
	private String ldapUrl="";
	private String ldapDn="";
	private String ldapBaseDn="";
	private String identifierAttributeName="";
	private String ldapIdAttributeName=DEFAULT_ID_ATTRIBUTE_NAME;
	private String ldapSecurityAuthenticacion=DEFAULT_SECURITY_AUTHENTICACION;
	
	public String getLoginType() {
		return LoginType;
	}
	public void setLoginType(String logonType) {
		LoginType = logonType;
	}
	public String getLdapUrl() {
		return ldapUrl;
	}
	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}
	public String getLdapDn() {
		return ldapDn;
	}
	public void setLdapDn(String ldapDn) {
		this.ldapDn = ldapDn;
	}
	public String getLdapBaseDn() {
		return ldapBaseDn;
	}
	public void setLdapBaseDn(String ldapBaseDn) {
		this.ldapBaseDn = ldapBaseDn;
	}
	public String getLdapIdAttributeName() {
		return ldapIdAttributeName;
	}
	public void setLdapIdAttributeName(String ldapIdAttributeName) {
		this.ldapIdAttributeName = ldapIdAttributeName;
	}
	public String getLdapSecurityAuthenticacion() {
		return ldapSecurityAuthenticacion;
	}
	public void setLdapSecurityAuthenticacion(String ldapSecurityAuthenticacion) {
		this.ldapSecurityAuthenticacion = ldapSecurityAuthenticacion;
	}
	public String getIdentifierAttributeName() {
		return identifierAttributeName;
	}
	public void setIdentifierAttributeName(String identifierAttributeName) {
		this.identifierAttributeName = identifierAttributeName;
	}
}
