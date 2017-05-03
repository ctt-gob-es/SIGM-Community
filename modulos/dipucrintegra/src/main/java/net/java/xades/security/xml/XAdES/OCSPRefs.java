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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 <OCSPRefs>
 <OCSPRef>
 <OCSPIdentifier URI= >
 <ResponderID>
 <ByName>String of X500Principal Name</ByName>
 or
 <ByKey>base64Binary of PublicKey DER value</ByKey>
 </ResponderID>
 <ProducedAt />
 </OCSPIdentifier>
 <DigestAlgAndValue>
 <DigestMethod Algorithm= />
 <DigestValue />
 </DigestAlgAndValue>
 <ValidationResult />
 </OCSPRef>
 </OCSPRefs>
 */

/**
 * 
 * @author miro
 */
public class OCSPRefs extends XAdESStructure
{
    private List<OCSPRef> ocspRefs;

    // public OCSPRefs(XAdESStructure parent,
    // CertValidationInfo certValidationInfo)
    // throws GeneralSecurityException
    // {
    // super(parent, "OCSPRefs");
    //
    // if(certValidationInfo == null)
    // throw new IllegalArgumentException("The CertValidationInfo can not be NULL.");
    //
    // Element thisElement = getElement();
    //
    // XAdESCertPathValidatorResult validatorResult;
    // validatorResult = certValidationInfo.getCertPathValidatorResult();
    // if(validatorResult != null)
    // {
    // for(XAdESRevocationStatus revocationStatus : validatorResult.getXAdESRevocationStatuses())
    // {
    // if(revocationStatus.getOCSPResponse() != null)
    // {
    // OCSPRef ocspRef = new OCSPRef(this, revocationStatus);
    // }
    // }
    // }
    // }

    public OCSPRefs(Node node, String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public List<OCSPRef> getOCSPRefs()
    {
        if (ocspRefs == null)
        {
            List<Element> elements = getChildElementsNS("OCSPRef");
            if (elements != null && elements.size() > 0)
            {
                ocspRefs = new ArrayList<OCSPRef>(elements.size());
                for (Element element : elements)
                {
                    ocspRefs.add(new OCSPRef(element, xadesPrefix, xadesNamespace,
                            xmlSignaturePrefix));
                }
            }
            else
            {
                ocspRefs = Collections.<OCSPRef> emptyList();
            }
        }

        return ocspRefs;
    }
}
