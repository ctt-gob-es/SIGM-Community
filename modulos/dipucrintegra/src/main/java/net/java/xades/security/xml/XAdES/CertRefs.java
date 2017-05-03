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

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 <CertRefs Id="">
 <Cert>
 <CertDigest>
 <DigestMethod Algorithm="" />
 <DigestValue></DigestValue>
 </CertDigest>
 <IssuerSerial>
 <X509IssuerName></X509IssuerName>
 <X509SerialNumber><X509SerialNumber>
 </IssuerSerial>
 </Cert>
 </CertRefs>
 */

/**
 * 
 * @author miro
 */
public class CertRefs extends XAdESStructure
{
    private List<Cert> certs;

    public CertRefs(XAdESStructure parent, Collection<X509Certificate> certificates,
            String signatureIdPrefix, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix) throws GeneralSecurityException
    {
        super(parent, "CertRefs", xadesPrefix, xadesNamespace, xmlSignaturePrefix);

        if (certificates == null || certificates.isEmpty())
            throw new IllegalArgumentException(
                    "The certificates collection can not be NULL or empty.");

        Element thisElement = getElement();
        if (signatureIdPrefix != null)
        {
            setAttribute("Id", signatureIdPrefix + "-CertRefs");
        }

        certs = new ArrayList<Cert>(certificates.size());

        for (X509Certificate certificate : certificates)
        {
            Cert cert = new Cert(this, certificate, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
            certs.add(cert);
        }
    }

    public CertRefs(Node node, String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public List<Cert> getCerts()
    {
        if (certs == null)
        {
            List<Element> elements = getChildElementsNS("Cert");
            if (elements != null && elements.size() > 0)
            {
                certs = new ArrayList<Cert>(elements.size());
                for (Element element : elements)
                {
                    certs.add(new Cert(element, xadesPrefix, xadesNamespace, xmlSignaturePrefix));
                }
            }
            else
            {
                certs = Collections.<Cert> emptyList();
            }
        }

        return certs;
    }
}
