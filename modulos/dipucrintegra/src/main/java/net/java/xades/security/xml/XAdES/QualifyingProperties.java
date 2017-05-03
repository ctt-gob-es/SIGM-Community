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

import javax.xml.XMLConstants;
import javax.xml.crypto.dsig.XMLSignature;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author miro
 */
public class QualifyingProperties extends XAdESStructure
{
    private String signatureIdPrefix;

    private SignedProperties signedProperties;
    private UnsignedProperties unsignedProperties;

    public QualifyingProperties(Node node, String signatureIdPrefix, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        this(node, "QualifyingProperties", signatureIdPrefix, xadesPrefix, xadesNamespace,
                xmlSignaturePrefix);
    }

    private QualifyingProperties(Node node, String elementName, String signatureIdPrefix,
            String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
    {
        this(node.getOwnerDocument().createElementNS(xadesNamespace, elementName), xadesPrefix,
                xadesNamespace, xmlSignaturePrefix);

        this.signatureIdPrefix = signatureIdPrefix;

        Element element = getElement();

        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:" + xmlSignaturePrefix,
                XMLSignature.XMLNS);
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:" + xadesPrefix,
                xadesNamespace);
        element.setPrefix(xadesPrefix);

        String target = "#" + signatureIdPrefix + "-" + SIGNATURE_ELEMENT_NAME;
        setAttributeNS(xadesNamespace, TARGET_ATTRIBUTE, target);

        String id = signatureIdPrefix + "-" + elementName;
        setAttributeNS(xadesNamespace, ID_ATTRIBUTE, id);
    }

    public QualifyingProperties(Node node, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public SignedProperties getSignedProperties()
    {
        if (signedProperties == null)
        {
            signedProperties = new SignedProperties(this, signatureIdPrefix, xadesPrefix,
                    xadesNamespace, xmlSignaturePrefix);
        }

        return signedProperties;
    }

    public UnsignedProperties getUnsignedProperties()
    {
        if (unsignedProperties == null)
        {
            unsignedProperties = new UnsignedProperties(this, xadesPrefix, xadesNamespace,
                    xmlSignaturePrefix);
        }

        return unsignedProperties;
    }
}
