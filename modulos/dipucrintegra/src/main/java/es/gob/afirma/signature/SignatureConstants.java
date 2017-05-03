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
 * <b>File:</b><p>es.gob.afirma.signature.SignatureConstants.java.</p>
 * <b>Description:</b><p>Commonly used constant for signature process.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/06/2011.
 */
package es.gob.afirma.signature;

import java.security.Provider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.dsig.DigestMethod;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import es.gob.afirma.utils.CryptoUtil;

/**
 * <p>Commonly used constant for signature process.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/06/2011.
 */
public final class SignatureConstants {

    /**
     * Constructor method for the class SignatureConstants.java.
     */
    private SignatureConstants() {
    }

    // ************************************************************
    // ************* FORMATOS DE FIRMA*****************************
    // ************************************************************

    /**
     * Attribute that represents CAdES signature.
     */
    public static final String SIGN_FORMAT_CADES = "CAdES";

    /**
     * Attribute that represents XAdES Detached signature identificator.
     */
    public static final String SIGN_FORMAT_XADES_DETACHED = "XAdES Detached";

    /**
     * Attribute that represents XAdES Externally Detached signature.
     */
    public static final String SIGN_FORMAT_XADES_EXTERNALLY_DETACHED = "XAdES Externally Detached";

    /**
     * Attribute that represents XAdES Enveloping enveloped identificator. .
     */
    public static final String SIGN_FORMAT_XADES_ENVELOPED = "XAdES Enveloped";

    /**
     * Attribute that represents XAdES Enveloping signature identificator.
     */
    public static final String SIGN_FORMAT_XADES_ENVELOPING = "XAdES Enveloping";

    /**
     * Attribute that represents PAdES signature identificator.
     */
    public static final String SIGN_FORMAT_PADES = "PAdES";

    // ************************************************************

    /**
     * Attribute that represents explicit signature mode.
     */
    public static final String SIGN_MODE_EXPLICIT = "explicit mode";

    /**
     * Attribute that represents implicit signature mode.
     */
    public static final String SIGN_MODE_IMPLICIT = "implicit mode";

    /**
     * Attribute that represents default signature mode.
     */
    public static final String DEFAULT_SIGN_MODE = SIGN_MODE_EXPLICIT;

    /**
     * Attribute that represents default mimetype.
     */
    public static final String DEFAULT_MIMETYPE = "application/octet-stream";

    /**
     * Attribute that represents supported xades signature formats.
     */
    public static final Set<String> SUPPORTED_XADES_SIGN_FORMAT = new HashSet<String>() {

	/**
	 * Attribute that represents serialVersionUID.
	 */
	private static final long serialVersionUID = -3526322449162410326L;

	{
	    add(SIGN_FORMAT_XADES_ENVELOPED);
	    add(SIGN_FORMAT_XADES_ENVELOPING);
	    add(SIGN_FORMAT_XADES_DETACHED);
	    add(SIGN_FORMAT_XADES_EXTERNALLY_DETACHED);
	}
    };

    // ************************************************************
    // ************* ALGORITMOS DE FIRMA **************************
    // ************************************************************

    /**
     * Attribute that represents SHA1withRSA algorithm.
     */
    public static final String SIGN_ALGORITHM_SHA1WITHRSA = "SHA1withRSA";

    /**
     * Attribute that represents SHA256withRSA algorithm.
     */
    public static final String SIGN_ALGORITHM_SHA256WITHRSA = "SHA256withRSA";

    /**
     * Attribute that represents SHA384withRSA algorithm.
     */
    public static final String SIGN_ALGORITHM_SHA384WITHRSA = "SHA384withRSA";

    /**
     * Attribute that represents SHA512withRSA algorithm.
     */
    public static final String SIGN_ALGORITHM_SHA512WITHRSA = "SHA512withRSA";

    // ************************************************************

    /**
     * Attribute that represents the URI of algorithm in XADES signatures.
     */
    public static final Map<String, String> SIGN_ALGORITHM_URI = new HashMap<String, String>() {

	/**
	 * Attribute that represents .
	 */
	private static final long serialVersionUID = -3091842386857850550L;

	{
	    put(SIGN_ALGORITHM_SHA1WITHRSA, "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
	    put(SIGN_ALGORITHM_SHA256WITHRSA, "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
	    put(SIGN_ALGORITHM_SHA384WITHRSA, "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384");
	    put(SIGN_ALGORITHM_SHA512WITHRSA, "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512");
	}
    };

    /**
     *  Attribute that represents algorithms used in digest method of XAdES signature.
     */
    public static final Map<String, String> DIGEST_METHOD_ALGORITHMS_XADES = new HashMap<String, String>() {

	/**
	 * Attribute that represents serialVersionUID.
	 */
	private static final long serialVersionUID = -3091842386857850550L;

	{
	    put(SIGN_ALGORITHM_SHA1WITHRSA, DigestMethod.SHA1);
	    put(SIGN_ALGORITHM_SHA256WITHRSA, DigestMethod.SHA256);
	    put(SIGN_ALGORITHM_SHA384WITHRSA, "http://www.w3.org/2001/04/xmldsig-more#sha384");
	    put(SIGN_ALGORITHM_SHA512WITHRSA, DigestMethod.SHA512);
	}
    };

    /**
     * Attribute that represents algorithms used in digest method of CAdES signature.
     */
    public static final Map<String, String> SIGN_ALGORITHMS_SUPPORT_CADES = new HashMap<String, String>() {

	/**
	 * Attribute that represents serialVersionUID.
	 */
	private static final long serialVersionUID = -3091842386857850550L;

	{
	    put(SIGN_ALGORITHM_SHA1WITHRSA, CryptoUtil.HASH_ALGORITHM_SHA1);
	    put(SIGN_ALGORITHM_SHA256WITHRSA, CryptoUtil.HASH_ALGORITHM_SHA256);
	    put(SIGN_ALGORITHM_SHA384WITHRSA, CryptoUtil.HASH_ALGORITHM_SHA384);
	    put(SIGN_ALGORITHM_SHA512WITHRSA, CryptoUtil.HASH_ALGORITHM_SHA512);
	}
    };

    /**
     * Attribute that represents default signature algorithm.
     */
    public static final String DEFAULT_SIGN_ALGO = SIGN_ALGORITHM_SHA1WITHRSA;

    /**
     * Attribute that represents property name used in Manifest references list (external references in a detached signature).
     */
    public static final String MF_REFERENCES_PROPERTYNAME = "Manifest-References";


    /**
     * Attribute that represents the BouncyCastle provider.
     */
    public static final Provider BC_PROVIDER = new BouncyCastleProvider();

    /**
     *  Constant attribute that represents the string to identify the value for the key <i>SubFilter</i> of the signature dictionary for a
     *  PAdES Enhanced signature.
     */
    public static final String PADES_SUBFILTER_VALUE = "ETSI.CAdES.detached";
}
