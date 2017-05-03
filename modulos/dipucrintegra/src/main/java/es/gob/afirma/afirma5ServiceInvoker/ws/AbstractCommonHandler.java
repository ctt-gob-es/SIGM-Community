// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/*
* Este fichero forma parte de la plataforma de @firma. 
* La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
* y descargado desde http://forja-ctt.administracionelectronica.gob.es
*
* Copyright 2009-,2011 Gobierno de España
* Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
* condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este 
* fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
*/

/** 
 * <b>File:</b><p>es.gob.afirma.afirma5ServiceInvoker.ws.AbstractCommonHandler.java.</p>
 * <b>Description:</b><p>Class that represents handlers used in the service invoker.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>03/10/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 23/03/2011.
 */
package es.gob.afirma.afirma5ServiceInvoker.ws;

import java.util.Properties;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;


/** 
 * <p>Class that represents handlers used in the service invoker.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 28/09/2011.
 */
public class AbstractCommonHandler extends BasicHandler {

	/**
	 * Attribute that represents serial version UID. 
	 */
	private static final long serialVersionUID = -4453701134094417213L;
	
	/** Usuario para el token de seguridad UserNameToken. Además, indica el alias del certificado usado para firmar
	 * el tag soapBody de la petición y que será alojado en el token BinarySecurityToken */
	private String userAlias = "";
	/** Password para el token de seguridad UserNameToken y del certificado usado para firmar
	 * el tag soapBody de la petición y que será alojado en el token BinarySecurityToken. */
	private String password = "";

	/**
	 * Attribute that represents type of password. 
	 */
	private String passwordType = WSConstants.PASSWORD_TEXT;

	/**
	 * Attribute that represents user Keystore.
	 */
	private String userKeystore;

	/**
	 * Attribute that represents user Keystore Pass.
	 */
	private String userKeystorePass;

	/**
	 * Attribute that represents user Keystore Type.
	 */
	private String userKeystoreType;

	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext msgContext) throws AxisFault {}
	
	/**
	 * Establece el conjunto de propiedades con el que será inicializado el gestor criptográfico de WSS4J.
	 * @return Devuelve el conjunto de propiedades con el que será inicializado el gestor criptográfico de WSS4J.
	 * @throws WSSecurityException 
	 */
	final Crypto getCryptoInstance() throws WSSecurityException {
		
		Properties properties = new Properties();
		properties.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
		properties.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", this.userKeystoreType);
		properties.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", this.userKeystorePass);
		properties.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", this.userAlias);
		properties.setProperty("org.apache.ws.security.crypto.merlin.alias.password", this.password);
		properties.setProperty("org.apache.ws.security.crypto.merlin.file", this.userKeystore);
//		properties.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
//		return CryptoFactory.getInstance("org.apache.ws.security.components.crypto.Merlin", properties);
		return CryptoFactory.getInstance(properties);
	}
	
	
	/**
	 * Gets the value of the attribute {@link #password}.
	 * @return the value of the attribute {@link #password}.
	 */
	public final String getPassword() {
		return password;
	}

	
	/**
	 * Sets the value of the attribute {@link #password}.
	 * @param passParam The value for the attribute {@link #password}.
	 */
	public final void setPassword(String passParam) {
		this.password = passParam;
	}

	
	/**
	 * Gets the value of the attribute {@link #passwordType}.
	 * @return the value of the attribute {@link #passwordType}.
	 */
	public final String getPasswordType() {
		return passwordType;
	}

	
	/**
	 * Sets the value of the attribute {@link #passwordType}.
	 * @param passTypeParam The value for the attribute {@link #passwordType}.
	 */
	public final void setPasswordType(String passTypeParam) {
		if("digest".equalsIgnoreCase(passTypeParam)) {
			this.passwordType = WSConstants.PASSWORD_DIGEST;
		} else if ("clear".equalsIgnoreCase(passTypeParam)) {
			this.passwordType = WSConstants.PASSWORD_TEXT;
		}
	}

	
	/**
	 * Gets the value of the attribute {@link #userKeystore}.
	 * @return the value of the attribute {@link #userKeystore}.
	 */
	public final String getUserKeystore() {
		return userKeystore;
	}

	
	/**
	 * Sets the value of the attribute {@link #userKeystore}.
	 * @param userKeystoreParam The value for the attribute {@link #userKeystore}.
	 */
	public final void setUserKeystore(String userKeystoreParam) {
		this.userKeystore = userKeystoreParam;
	}

	
	/**
	 * Gets the value of the attribute {@link #userKeystorePass}.
	 * @return the value of the attribute {@link #userKeystorePass}.
	 */
	public final String getUserKeystorePass() {
		return userKeystorePass;
	}

	
	/**
	 * Sets the value of the attribute {@link #userKeystorePass}.
	 * @param userKeyPassParam The value for the attribute {@link #userKeystorePass}.
	 */
	final void setUserKeystorePass(String userKeyPassParam) {
		this.userKeystorePass = userKeyPassParam;
	}

	
	/**
	 * Gets the value of the attribute {@link #userKeystoreType}.
	 * @return the value of the attribute {@link #userKeystoreType}.
	 */
	final String getUserKeystoreType() {
		return userKeystoreType;
	}

	
	/**
	 * Sets the value of the attribute {@link #userKeystoreType}.
	 * @param userKeyType The value for the attribute {@link #userKeystoreType}.
	 */
	public final void setUserKeystoreType(String userKeyType) {
		this.userKeystoreType = userKeyType;
	}

	
	/**
	 * Gets the value of the attribute {@link #userAlias}.
	 * @return the value of the attribute {@link #userAlias}.
	 */
	public final String getUserAlias() {
		return userAlias;
	}

	
	/**
	 * Sets the value of the attribute {@link #userAlias}.
	 * @param userAliasParam The value for the attribute {@link #userAlias}.
	 */
	public final void setUserAlias(String userAliasParam) {
		this.userAlias = userAliasParam;
	}
	
	

}
