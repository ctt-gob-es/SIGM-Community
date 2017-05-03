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

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 <IssuerSerial>
 <X509IssuerName></X509IssuerName>
 <X509SerialNumber><X509SerialNumber>
 </IssuerSerial>
 */

/**
 * 
 * @author miro
 */
public class IssuerSerial extends XAdESStructure
{
    public IssuerSerial(XAdESStructure parent, X509Certificate cert, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        this(parent, cert.getIssuerX500Principal(), cert.getSerialNumber(), xadesPrefix,
                xadesNamespace, xmlSignaturePrefix);
    }

    public IssuerSerial(XAdESStructure parent, X500Principal issuer, BigInteger serialNumber,
            String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
    {
        super(parent, "IssuerSerial", xadesPrefix, xadesNamespace, xmlSignaturePrefix);

        Element thisElement = getElement();

        Element element = createElement("X509IssuerName");
        thisElement.appendChild(element);
        element.setTextContent(issuer.getName());

        element = createElement("X509SerialNumber");
        thisElement.appendChild(element);
        element.setTextContent(serialNumber.toString());
    }

    public IssuerSerial(Node node, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public String getIssuerName()
    {
        return getChildElementTextContent("X509IssuerName");
    }

    public String getSerialNumber()
    {
        return getChildElementTextContent("X509SerialNumber");
    }
}
