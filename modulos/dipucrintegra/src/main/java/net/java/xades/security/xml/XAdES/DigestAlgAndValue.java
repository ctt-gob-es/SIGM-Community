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
 * This file is part of the jXAdES library. 
 * jXAdES is an open implementation for the Java platform of the XAdES standard for advanced XML digital signature. 
 * This library can be consulted and downloaded from http://universitatjaumei.jira.com/browse/JXADES.
 * 
 */
package net.java.xades.security.xml.XAdES;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.cert.X509CRL;

import javax.xml.crypto.dsig.DigestMethod;

import net.java.xades.util.Base64;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 <DigestAlgAndValue>
 <DigestMethod Algorithm= />
 <DigestValue />
 </DigestAlgAndValue>
 */

/**
 * 
 * @author miro
 */
public class DigestAlgAndValue extends XAdESStructure
{
    private static final String ALGORITHM_ATTR = "Algorithm";
    private static final String DIGEST_METHOD_ELEMENT = "DigestMethod";
    private static final String DIGEST_VALUE_ELEMENT = "DigestValue";

    // public DigestAlgAndValue(XAdESStructure parent, OCSPResponse ocspResponse)
    // throws GeneralSecurityException
    // {
    // this(parent, "DigestAlgAndValue", ocspResponse.getResponseData());
    // }

    public DigestAlgAndValue(XAdESStructure parent, X509CRL crl, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix) throws GeneralSecurityException
    {
        this(parent, "DigestAlgAndValue", crl.getEncoded(), xadesPrefix, xadesNamespace,
                xmlSignaturePrefix);
    }

    protected DigestAlgAndValue(XAdESStructure parent, String elementName, byte[] data,
            String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
            throws GeneralSecurityException
    {
        super(parent, elementName, xadesPrefix, xadesNamespace, xmlSignaturePrefix);

        Element thisElement = getElement();

        Element element = createElement(DIGEST_METHOD_ELEMENT);
        element.setPrefix(xmlSignaturePrefix);
        thisElement.appendChild(element);
        element.setAttribute(ALGORITHM_ATTR, DigestMethod.SHA1);

        MessageDigest md = MessageDigest.getInstance("SHA-1");

        String digestValue = Base64.encodeBytes(md.digest(data));
        element = createElement(DIGEST_VALUE_ELEMENT);
        element.setPrefix(xmlSignaturePrefix);
        thisElement.appendChild(element);
        element.setTextContent(digestValue);
    }

    protected DigestAlgAndValue(XAdESStructure parent, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix) throws GeneralSecurityException
    {
        super(parent, "DigestAlgAndValue", xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public DigestAlgAndValue(Node node, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public String getDigestMethod()
    {
        return getChildElementTextContent(DIGEST_METHOD_ELEMENT);
    }

    public byte[] getDigestValue() throws IOException
    {
        String value = getChildElementTextContent(DIGEST_VALUE_ELEMENT);

        if (value != null && (value = value.trim()).length() > 0)
        {
            return Base64.decode(value);
        }

        return null;
    }
}
