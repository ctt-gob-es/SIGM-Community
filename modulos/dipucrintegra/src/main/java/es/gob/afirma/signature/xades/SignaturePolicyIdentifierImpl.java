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
 * <b>File:</b><p>es.gob.afirma.signature.xades.SignaturePolicyIdentifier.java.</p>
 * <b>Description:</b><p>Implements the class net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>04/08/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/08/2011.
 */
package es.gob.afirma.signature.xades;

import net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier;

/**
 * <p>Implements the class net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier. It is a interface
 * that represents a SignaturePolicyIdentifier element included in a XAdES signature (<a href="http://www.w3.org/TR/XAdES/#Syntax_for_XAdES_The_SignaturePolicyIdentifier_element">The SignaturePolicyIdentifier element</a>) .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 21/09/2011.
 */
public class SignaturePolicyIdentifierImpl implements SignaturePolicyIdentifier {

    /** The implied. */
    private boolean implied;

    /** The identifier. */
    private String identifier;

    /** The description. */
    private String description;

    /** The qualifier. */
    private String qualifier;

    /** The hash base64. */
    private String hashBase64;

    /**
     * Instantiates a new signature policy identifier impl.
     *
     * @param impliedParam the implied
     */
    public SignaturePolicyIdentifierImpl(boolean impliedParam) {
	implied = impliedParam;
    }

    /**
     * Constructor method for the class SignaturePolicyIdentifierImpl.java.
     *
     * @param impliedParam the implied param
     * @param identifierParam the identifier param
     * @param descriptionParam the description param
     * @param qualifierParam the qualifier param
     * @param digestValue the digest value
     */
    public SignaturePolicyIdentifierImpl(boolean impliedParam, String identifierParam, String descriptionParam, String qualifierParam, String digestValue) {
	super();
	implied = impliedParam;
	identifier = identifierParam;
	description = descriptionParam;
	qualifier = qualifierParam;
	hashBase64 = digestValue;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#setIdentifier(java.lang.String)
     */
    public final void setIdentifier(String identifierParam) {
	identifier = identifierParam;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#isImplied()
     */
    public final boolean isImplied() {
	return implied;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#setImplied(boolean)
     */
    public final void setImplied(boolean impliedParam) {
	implied = impliedParam;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#getIdentifier()
     */
    public final String getIdentifier() {
	return identifier;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#getHashBase64()
     */
    public final String getHashBase64() {
	return hashBase64;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#setHashBase64(java.lang.String)
     */
    public final void setHashBase64(String hashBase64Param) {
	hashBase64 = hashBase64Param;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#getDescription()
     */
    public final String getDescription() {
	return description;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#setDescription(java.lang.String)
     */
    public final void setDescription(String descrParam) {
	description = descrParam;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#getQualifier()
     */
    public final String getQualifier() {
	return qualifier;
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier#setQualifier(java.lang.String)
     */
    public final void setQualifier(String qualifierParam) {
	qualifier = qualifierParam;
    }
}
