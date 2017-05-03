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
 * <b>File:</b><p>es.gob.afirma.signature.SignersFactory.java.</p>
 * <b>Description:</b><p>Class implements a factory that provides signers implementations.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/06/2011.
 */
package es.gob.afirma.signature;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.cades.CadesSigner;
import es.gob.afirma.signature.pades.PadesSigner;
import es.gob.afirma.signature.xades.XadesSigner;

/**
 * <p>Class implements a factory that provides signers implementations.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/06/2011.
 */
public final class SignersFactory {

    /**
     *  Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(SignersFactory.class);

    /**
     * Attribute that represents a <code>SignerFactory</code> instance.
     */
    private static SignersFactory signersFactory = null;

    /**
     * Attribute that represents list of signers supported with their handler associated.
     */
    private static Map<String, String> signers;

    /**
     * Constructor method for the class SignersFactory.java.
     */
    private SignersFactory() {
    }

    /**
     * Gets a instance of signers factory.
     * @return a instance of signers factory.
     */
    public static SignersFactory getInstance() {
	if (signersFactory != null) {
	    return signersFactory;
	} else {

	    // Cargamos
	    signersFactory = new SignersFactory();
	    signers = new HashMap<String, String>();

	    String xadesInstanceName = XadesSigner.class.getName();
	    signers.put(SignatureConstants.SIGN_FORMAT_CADES, CadesSigner.class.getName());
	    signers.put(SignatureConstants.SIGN_FORMAT_PADES, PadesSigner.class.getName());
	    signers.put(SignatureConstants.SIGN_FORMAT_XADES_DETACHED, xadesInstanceName);
	    signers.put(SignatureConstants.SIGN_FORMAT_XADES_ENVELOPED, xadesInstanceName);
	    signers.put(SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING, xadesInstanceName);
	    signers.put(SignatureConstants.SIGN_FORMAT_XADES_EXTERNALLY_DETACHED, xadesInstanceName);
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.SF_LOG002, new Object[ ] { signersFactory.toString() }));
	    }
	    return signersFactory;
	}
    }

    /**
     * Gets a implementation of {@link es.gob.afirma.signature.Signer Signer} for the signature format given.
     * @param signatureFormat  signature format for get a type of Signer implementation.
     * @return a implementation of Signer for sign a document with the format indicated.
     * @throws SigningException if format signature isn't supported.
     */
    public Signer getSigner(String signatureFormat) throws SigningException {
	if (signers.containsKey(signatureFormat)) {
	    try {
		return (Signer) Class.forName(signers.get(signatureFormat)).newInstance();
	    } catch (InstantiationException e) {
		throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.SF_LOG001, new Object[ ] { signers.get(signatureFormat) }), e);
	    } catch (IllegalAccessException e) {
		throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.SF_LOG001, new Object[ ] { signers.get(signatureFormat) }), e);
	    } catch (ClassNotFoundException e) {
		throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.SF_LOG001, new Object[ ] { signers.get(signatureFormat) }), e);
	    }
	} else {
	    throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.SF_LOG003, new Object[ ] { signatureFormat }));
	}
    }

    /**
     * Gets a list with formats of signature supported.
     * @return a list with formats of signature supported.
     */
    public String toString() {
	StringBuilder exstr = new StringBuilder(Language.getResIntegra(ILogConstantKeys.SF_LOG004));
	for (String signer: signers.keySet()) {
	    exstr.append("\n\t\t").append(signer);
	}
	return exstr.toString();
    }

}
