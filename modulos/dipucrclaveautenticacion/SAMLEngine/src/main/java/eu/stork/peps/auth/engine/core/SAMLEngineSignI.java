/* 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 * 
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

package eu.stork.peps.auth.engine.core;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.SignableSAMLObject;

import eu.stork.peps.exceptions.SAMLEngineException;

/**
 * The Interface SAMLEngineSignI.
 * 
 * @author fjquevedo
 */
public interface SAMLEngineSignI {

    /**
     * Sign.
     * 
     * @param tokenSaml the token SAML
     * 
     * @return the sAML object
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    SAMLObject sign(SignableSAMLObject tokenSaml) throws SAMLEngineException;

    /**
     * Gets the certificate.
     * 
     * @return the certificate
     */
    X509Certificate getCertificate();

    /**
     * Gets the trustStore used when validating SAMLTokens
     * 
     * @return the trustStore
     *   
     */
	KeyStore getTrustStore();
	
    /**
     * Validate signature.
     * 
     * @param tokenSaml the token SAML
     * 
     * @return the sAML object
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    SAMLObject validateSignature(SignableSAMLObject tokenSaml)
	    throws SAMLEngineException;

    /**
     * Initialize the signature module.
     * 
     * @param fileConf the configuration file.
     * 
     * @throws SAMLEngineException the STORKSAML engine runtime
     *             exception
     */
    void init(String fileConf) throws SAMLEngineException;

    /**
     * Load cryptographic service provider.
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    void loadCryptServiceProvider() throws SAMLEngineException;
    
}
