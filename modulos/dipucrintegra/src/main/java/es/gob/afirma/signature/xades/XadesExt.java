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
 * <b>File:</b><p>es.gob.afirma.signature.xades.XadesExt.java.</p>
 * <b>Description:</b><p>Class extends from  <code>net.java.xades.security.xml.XAdES.XMLAdvancedSignature</code></p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>04/08/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/08/2011.
 */
package es.gob.afirma.signature.xades;

import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;

import net.java.xades.security.xml.WrappedKeyStorePlace;
import net.java.xades.security.xml.XmlWrappedKeyInfo;
import net.java.xades.security.xml.XAdES.XAdES_BES;
import net.java.xades.security.xml.XAdES.XMLAdvancedSignature;

/**
 * <p>Class extends from  {@link XMLAdvancedSignature} with the following changes:
 * <ul>
 *  <li><code>SubjectX500Principal</code> and <code>X509IssuerSerial</code> elements are included in the <code>KeyInfo</code> element.</li>
 *  <li>Signature algorithm can be set.</li>
 *  <li>Canonicalization algorithm of the signature can be set.</li>
 *  <li>Namespace of Xades can be set.</li>
 * </ul>
 * 
 * </p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 21/09/2011.
 */
public final class XadesExt extends XMLAdvancedSignature {

    /**
     * Constructor method for the class XadesExt.java.
     * @param xades
     */
    private XadesExt(final XAdES_BES xades) {
	super(xades);
    }

    /**
     * Attribute that represents canonicalization method.
     */
    private String canonicalizationMethod = CanonicalizationMethod.INCLUSIVE;

    /**
     * Establece el algoritmo de canonicalizaci&oacute;n.
     * @param canMethod URL del algoritmo de canonicalizaci&oacute;n. Debe estar soportado en XMLDSig 1.0 &oacute; 1.1
     */
    public void setCanonicalizationMethod(final String canMethod) {
	if (canMethod != null) {
	    canonicalizationMethod = canMethod;
	}
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.XMLAdvancedSignature#newKeyInfo(java.security.cert.X509Certificate, java.lang.String)
     */
    @Override
    protected KeyInfo newKeyInfo(final X509Certificate certificate, final String keyInfoId) throws KeyException {

	final KeyInfoFactory keyInfoFactory = getXMLSignatureFactory().getKeyInfoFactory();
	final List<Object> x509DataList = new ArrayList<Object>();
	if (!XmlWrappedKeyInfo.PUBLIC_KEY.equals(getXmlWrappedKeyInfo())) {
	    x509DataList.add(certificate);
	}
	final List<Object> newList = new ArrayList<Object>();
	newList.add(keyInfoFactory.newKeyValue(certificate.getPublicKey()));
	newList.add(keyInfoFactory.newX509Data(x509DataList));
	return keyInfoFactory.newKeyInfo(newList, keyInfoId);
    }

    /**
     * {@inheritDoc}
     * @see net.java.xades.security.xml.XAdES.XMLAdvancedSignature#sign(java.security.cert.X509Certificate, java.security.PrivateKey, java.lang.String, java.util.List, java.lang.String, java.lang.String)
     */
    @Override
    public void sign(final X509Certificate certificate, final PrivateKey privateKey, final String signatureMethod, final List refsIdList, final String signatureIdPrefix, final String tsaURL) throws MarshalException, XMLSignatureException, GeneralSecurityException {

	List<?> referencesIdList = new ArrayList(refsIdList);

	if (WrappedKeyStorePlace.SIGNING_CERTIFICATE_PROPERTY.equals(getWrappedKeyStorePlace())) {
	    xades.setSigningCertificate(certificate);
	}

	addXMLObject(marshalXMLSignature(xadesNamespace, signatureIdPrefix, referencesIdList, tsaURL));

	final XMLSignatureFactory fac = getXMLSignatureFactory();

	final List<Reference> documentReferences = getReferences(referencesIdList);
	final String keyInfoId = getKeyInfoId(signatureIdPrefix);
	documentReferences.add(fac.newReference("#" + keyInfoId, getDigestMethod()));

	this.signature = fac.newXMLSignature(fac.newSignedInfo(fac.newCanonicalizationMethod(canonicalizationMethod, (C14NMethodParameterSpec) null), fac.newSignatureMethod(signatureMethod, null), documentReferences), newKeyInfo(certificate, keyInfoId), getXMLObjects(), getSignatureId(signatureIdPrefix), getSignatureValueId(signatureIdPrefix));

	this.signContext = new DOMSignContext(privateKey, baseElement);
	this.signContext.putNamespacePrefix(XMLSignature.XMLNS, xades.getXmlSignaturePrefix());
	this.signContext.putNamespacePrefix(xadesNamespace, xades.getXadesPrefix());

	registerIdAttrs();
	signature.sign(signContext);
    }

    /**
     * Declares Id attributes of signature elements for to allow resolve references.
     */
    private void registerIdAttrs() {
	// Registro de todos los nodos con atributo 'Id' (para que se puedan
	// resolver las referencias)
	IdRegister.registerElements(baseElement);
	// Registro de los nodos correspondientes a <xades:SignedProperties>
	for (XMLObject xmlObject: getXMLObjects()) {
	    for (Object xmlStructure: xmlObject.getContent()) {
		if (xmlStructure instanceof DOMStructure) {
		    IdRegister.registerElements(((DOMStructure) xmlStructure).getNode());
		}
	    }
	}
	// IdRegister.setIdAttributeIntoContext(signContext);
    }

    /**Create a new instance of type es.gob.afirma.signature.xades.XadesExt.
     * @param xades data container.
     * @return a new object of type es.gob.afirma.signature.xades.XadesExt
     * @throws GeneralSecurityException in error case.
     */
    public static XadesExt newInstance(final XAdES_BES xades) throws GeneralSecurityException {
	XadesExt result = new XadesExt(xades);
	result.setDigestMethod(xades.getDigestMethod());
	result.setXadesNamespace(xades.getXadesNamespace());
	return result;
    }

}
