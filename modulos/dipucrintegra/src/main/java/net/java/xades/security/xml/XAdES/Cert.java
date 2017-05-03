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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
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
 */

/**
 * 
 * @author miro
 */
public class Cert extends XAdESStructure
{
    private CertDigest certDigest;
    private IssuerSerial issuerSerial;

    public Cert(XAdESStructure parent, X509Certificate cert, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix) throws GeneralSecurityException
    {
        super(parent, "Cert", xadesPrefix, xadesNamespace, xmlSignaturePrefix);

        Element thisElement = getElement();

        certDigest = new CertDigest(this, cert, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
        issuerSerial = new IssuerSerial(this, cert, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public Cert(Node node, String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public CertDigest getCertDigest()
    {
        if (certDigest == null)
        {
            Element element = getChildElementNS("CertDigest");
            if (element != null)
                certDigest = new CertDigest(element, xadesPrefix, xadesNamespace,
                        xmlSignaturePrefix);
        }

        return certDigest;
    }

    public IssuerSerial getIssuerSerial()
    {
        if (issuerSerial == null)
        {
            Element element = getChildElementNS("IssuerSerial");
            if (element != null)
                issuerSerial = new IssuerSerial(element, xadesPrefix, xadesNamespace,
                        xmlSignaturePrefix);
        }

        return issuerSerial;
    }
}
