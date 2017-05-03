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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 <CRLRef>
 <DigestAlgAndValue>
 <DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1" />
 <DigestValue>...</DigestValue>
 </DigestAlgAndValue>
 <CRLIdentifier URI="#Signature_1_EncapsulatedCRLValue_1">
 <Issuer>...</Issuer>
 <IssueTime>...</IssueTime>
 <Number>...</Number>
 </CRLIdentifier>
 <ValidationResult />
 </CRLRef>
 */

/**
 * 
 * @author miro
 */
public class CRLRef extends XAdESStructure
{
    private DigestAlgAndValue digestAlgAndValue;
    private CRLIdentifier crlIdentifier;
    private ValidationResult validationResult;

    // public CRLRef(XAdESStructure parent, XAdESRevocationStatus revocationStatus)
    // throws GeneralSecurityException
    // {
    // super(parent, "CRLRef");
    //
    // Element thisElement = getElement();
    //
    // X509CRL crl = revocationStatus.getCheckedCRL();
    // X509CRLEntry crlEntry = revocationStatus.getCRLEntry();
    //
    // DigestAlgAndValue digestAlgAndValue;
    // digestAlgAndValue = new DigestAlgAndValue(this, crl);
    //
    // CRLIdentifier crlIdentifier = new CRLIdentifier(this, revocationStatus);
    //
    // ValidationResult validationResult;
    // validationResult = new ValidationResult(this, revocationStatus);
    // }

    public CRLRef(Node node, String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public CRLIdentifier getCRLIdentifier()
    {
        if (crlIdentifier == null)
        {
            Element element = getChildElementNS("CRLIdentifier");
            if (element != null)
                crlIdentifier = new CRLIdentifier(element, xadesPrefix, xadesNamespace,
                        xmlSignaturePrefix);
        }

        return crlIdentifier;
    }

    public DigestAlgAndValue getDigestAlgAndValue()
    {
        if (digestAlgAndValue == null)
        {
            Element element = getChildElementNS("DigestAlgAndValue");
            if (element != null)
                digestAlgAndValue = new DigestAlgAndValue(element, xadesPrefix, xadesNamespace,
                        xmlSignaturePrefix);
        }

        return digestAlgAndValue;
    }

    public ValidationResult getValidationResult()
    {
        if (validationResult == null)
        {
            Element element = getChildElementNS("ValidationResult");
            if (element != null)
                validationResult = new ValidationResult(element, xadesPrefix, xadesNamespace,
                        xmlSignaturePrefix);
        }

        return validationResult;
    }

}
