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
 * <b>File:</b><p>es.gob.afirma.signature.SignatureProperties.java.</p>
 * <b>Description:</b><p>Commonly used constant for optional input parameters in signature process.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/06/2011.
 */
package es.gob.afirma.signature;

/**
 * <p>Commonly used constant for optional input parameters in signature process.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/06/2011.
 */
public final class SignatureProperties {

    /**
     * Constructor method for the class Afirma5ServiceInvokerProperties.java.
     */
    private SignatureProperties() {
    }

    // CAdES properties

    /**
     * Attribute that represents property name for policyIdentifier in CAdES signatures.
     */
    public static final String CADES_POLICY_IDENTIFIER_PROP = "cades.policyIdentifier";

    /**
     * Attribute that represents property name for digest value of the policy in CAdES signatures.
     */
    public static final String CADES_POLICY_DIGESTVALUE_PROP = "cades.policyDigestValue";

    /**
     * Attribute that represents property name for digest algorithm of the policy in CAdES signatures.
     */
    public static final String CADES_POLICY_DIGEST_ALGORITHM_PROP = "cades.policyAlgorithm";

    /**
     * Attribute that represents property name for policy qualifier in CADES signatures.
     */
    public static final String CADES_POLICY_QUALIFIER_PROP = "cades.policyQualifier";

    // XAdES properties
    /**
     * Attribute that represents property name for claimedRole in XAdES signatures.
     */
    public static final String XADES_CLAIMED_ROLE_PROP = "xades.claimedRole";

    /**
     * Attribute that represents property name for policyIdentifier in XAdES signatures.
     */
    public static final String XADES_POLICY_IDENTIFIER_PROP = "xades.policyIdentifier";

    /**
     * Attribute that represents property name for policy qualifier in XAdES signatures.
     */
    public static final String XADES_POLICY_QUALIFIER_PROP = "xades.policyQualifier";

    /**
     * Attribute that represents property name for digest value of the policy in XAdES signatures.
     */
    public static final String XADES_POLICY_DIGESTVALUE_PROP = "xades.policyDigestValue";

    /**
     * Attribute that represents property name for description of the policy in XAdES signatures.
     */
    public static final String XADES_POLICY_DESCRIPTION_PROP = "xades.policyDescription";

    /**
     * Attribute that represents property name for description of the format of the signed data object.
     */
    public static final String XADES_DATA_FORMAT_DESCRIPTION_PROP = "xades.dataFormatObjectDescription";

    /**
     * Attribute that represents property name for mime of the format of the signed data object.
     */
    public static final String XADES_DATA_FORMAT_MIME_PROP = "xades.dataFormatObjectMime";

    /**
     * Attribute that represents property name for encoding of the format of the signed data object.
     */
    public static final String XADES_DATA_FORMAT_ENCODING_PROP = "xades.dataFormatObjectEncoding";

    // PAdES properties
    /**
     * Attribute that represents property name for reason in PAdES signatures.
     */
    public static final String PADES_REASON_PROP = "Pades.signReason";

    /**
     * Attribute that represents property name for contact in PAdES signatures.
     */
    public static final String PADES_CONTACT_PROP = "Pades.signContact";

    /**
     * Attribute that represents property name for location in PAdES signatures.
     */
    public static final String PADES_LOCATION_PROP = "Pades.signLocation";

}
