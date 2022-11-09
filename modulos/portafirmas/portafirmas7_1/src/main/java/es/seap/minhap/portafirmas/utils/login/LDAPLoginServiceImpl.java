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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import es.seap.minhap.portafirmas.utils.Constants;

public class LDAPLoginServiceImpl implements LoginService {

	private Logger log = Logger.getLogger(LDAPLoginServiceImpl.class);

	private final static String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

	private LoginServiceConfiguration properties;

	public LDAPLoginServiceImpl(LoginServiceConfiguration properties) {
		this.properties = properties;
		if (this.properties.getLdapUrl() == null
				|| this.properties.getLdapUrl().equals("")) {
			throw new IllegalArgumentException("LDAP url not found!");
		}
		if (this.properties.getIdentifierAttributeName() == null
				|| this.properties.getIdentifierAttributeName().equals("")) {
			throw new IllegalArgumentException(
					"Identifier Attribute Name is null!");
		}
	}
	/**
	 * {@link es.seap.minhap.portafirmas.utils.login.LoginService#check(String, String)}
	 */
	@SuppressWarnings("rawtypes")
	public LoginServiceResponse check(String user, String password) {
		//validarParametrosLDAP(user, password);
		Hashtable<String, String> auth = obtenerParametrosAutorizacion(user, password);
		//Hacemos una instancia de InitialDirContext con los par&aacute;metros de configuraci&oacute;n
		LoginServiceResponse result = new LoginServiceResponse();
		DirContext ctx = obtenerContexto(auth, result);
		//Si la instancia se ha creado correctamente
		if (ctx != null) {
			String filter = properties.getLdapIdAttributeName().replace(Constants.LOGIN_LDAP_USER_VAR, user);
			String[] attrIDs = { properties.getIdentifierAttributeName() };
			SearchControls ctls = new SearchControls();
			ctls.setReturningAttributes(attrIDs);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			try {
				String dn = this.properties.getLdapBaseDn().replace(Constants.LOGIN_LDAP_USER_VAR, user);
				NamingEnumeration answer = ctx.search(dn, filter, ctls);
				while (answer.hasMore()) {
					SearchResult sr = (SearchResult) answer.next();
					Attributes attrs = sr.getAttributes();
					//Si la busqueda ha obtenido resultados y tiene atributos
					if (sonAtributosCorrectos(attrs)) {
						result.setIdentifier(attrs.get(properties.getIdentifierAttributeName()).get().toString());
						result.setStatus(LoginServiceConfiguration.RESPONSE_OK);
					} else {
						result.setStatus(LoginServiceConfiguration.RESPONSE_NOT_FOUND);
					}

				}
			} catch (NamingException e) {
				log.error("LDAP Identifier Searching failure.", e);
				result.setStatus(LoginServiceConfiguration.RESPONSE_INTERNAL_ERROR);
			}
			try {
				ctx.close();
			} catch (NamingException e) {
				log.error("LDAP Context closing failure.", e);
				result.setStatus(LoginServiceConfiguration.RESPONSE_INTERNAL_ERROR);
			}
		}
		return result;
	}

	private DirContext obtenerContexto(Hashtable<String, String> auth, LoginServiceResponse result) {
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(auth);
		} catch (NamingException e) {
			log.error("LDAP Authentication failure. Cause: " + e);
			result.setStatus(LoginServiceConfiguration.RESPONSE_NOT_ALLOWED);
		}
		return ctx;
	}

	private boolean sonAtributosCorrectos(Attributes attrs) throws NamingException {
		return attrs != null
				&& attrs.get(properties.getIdentifierAttributeName()) != null
				&& attrs.get(properties.getIdentifierAttributeName()).get() != null;
	}

	private Hashtable<String, String> obtenerParametrosAutorizacion(String user, String password) {
		Hashtable<String, String> auth = new Hashtable<String, String>();
		auth.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
		auth.put(Context.PROVIDER_URL, properties.getLdapUrl());
		auth.put(Context.SECURITY_AUTHENTICATION, properties.getLdapSecurityAuthenticacion());
		auth.put(Context.SECURITY_PRINCIPAL, properties.getLdapDn().replace(Constants.LOGIN_LDAP_USER_VAR, user));
		auth.put(Context.SECURITY_CREDENTIALS, password);
		return auth;
	}
	
	private void validarParametrosLDAP(String user, String password) {
		//Si el usuario no exite lanza una excepci&oacute;n
		if (user == null || user.equals("")) {
			log.error("ERROR: LDAPLoginServiceImpl.check: User is null or empty");
			throw new IllegalArgumentException("User is null!");
		}

		//Si el password no exite lanza una excepci&oacute;n
		if (password == null || password.equals("")) {
			log.error("ERROR: LDAPLoginServiceImpl.check: Password is null or empty");
			throw new IllegalArgumentException("Password is null!");
		}

		// Check that user var was declared at ldap id pattern
		if (properties.getLdapIdAttributeName().indexOf(
				Constants.LOGIN_LDAP_USER_VAR) < 0) {
			log.error("ERROR: LDAPLoginServiceImpl.check: Incorrect user id pattern. No var was declared.");
			throw new IllegalArgumentException(
					"Incorrect user id pattern. No var was declared");
		}
		// Check that user var was declared at ldap base dn pattern
		if (properties.getLdapBaseDn().indexOf(Constants.LOGIN_LDAP_USER_VAR) < 0) {
			log.error("ERROR: LDAPLoginServiceImpl.check: Incorrect dn pattern. No var was declared.");
			throw new IllegalArgumentException(
					"Incorrect dn pattern. No var was declared");
		}
	}

}