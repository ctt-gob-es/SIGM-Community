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

import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.XMLSignature;

import org.w3c.dom.Element;

/**
 * 
 * 
 * The correct Structure here is:
 * 
 * <xades:SignaturePolicyIdentifier> <xades:SignaturePolicyId> <xades:SigPolicyId>
 * <xades:SigPolicyQualifiers> <xades:SigPolicyQualifier> </xades:SigPolicyQualifier>
 * </xades:SigPolicyQualifiers> <xades:Identifier> </xades:Identifier> <xades:Description>
 * </xades:Description> </xades:SigPolicyId> <xades:SigPolicyHash> <dsign:DigestMethod
 * Algorithm="http://www.w3.org/2000/09/xmldsig#sha1" /> <dsign:DigestValue> </dsign:DigestValue>
 * </xades:SigPolicyHash> </xades:SignaturePolicyId> </xades:SignaturePolicyIdentifier>
 * 
 * 
 */

public class SignaturePolicyIdentifierDetails extends XAdESStructure
{
    public SignaturePolicyIdentifierDetails(SignedSignatureProperties signedSignatureProperties,
            SignaturePolicyIdentifier signaturePolicyIdentifier, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        super(signedSignatureProperties, "SignaturePolicyIdentifier", xadesPrefix, xadesNamespace,
                xmlSignaturePrefix);

        // TODO: check if when the policy is implied other attributes are not mandatory.
        if (signaturePolicyIdentifier.isImplied())
        {
            Element implied = createElement("SignaturePolicyImplied");
            getNode().appendChild(implied);
        }
        else
        {
            Element signaturePolicyId = createElement("SignaturePolicyId");

            Element identifier = createElement("Identifier");
            identifier.appendChild(getDocument().createTextNode(
                    signaturePolicyIdentifier.getIdentifier()));

            Element description = createElement("Description");
            description.appendChild(getDocument().createTextNode(
                    signaturePolicyIdentifier.getDescription()));

            Element sigPolicyId = createElement("SigPolicyId");
            sigPolicyId.appendChild(identifier);
            sigPolicyId.appendChild(description);
            signaturePolicyId.appendChild(sigPolicyId);

            Element digestMethod = createElementNS(XMLSignature.XMLNS, xmlSignaturePrefix,
                    "DigestMethod");
            digestMethod.setAttribute("Algorithm", DigestMethod.SHA1);

            Element digestValue = createElementNS(XMLSignature.XMLNS, xmlSignaturePrefix,
                    "DigestValue");
            digestValue.setTextContent(signaturePolicyIdentifier.getHashBase64());

            Element sigPolicyHash = createElement("SigPolicyHash");
            sigPolicyHash.appendChild(digestMethod);
            sigPolicyHash.appendChild(digestValue);
            signaturePolicyId.appendChild(sigPolicyHash);

            if (signaturePolicyIdentifier.getQualifier() != null)
            {
                Element spURI = createElement("SPURI");
                spURI.setTextContent(signaturePolicyIdentifier.getQualifier());

                Element sigPolicyQualifier = createElement("SigPolicyQualifier");
                sigPolicyQualifier.appendChild(spURI);

                Element sigPolicyQualifiers = createElement("SigPolicyQualifiers");
                sigPolicyQualifiers.appendChild(sigPolicyQualifier);
                signaturePolicyId.appendChild(sigPolicyQualifiers);
                // getNode().appendChild(sigPolicyQualifiers);

            }

            getNode().appendChild(signaturePolicyId);
            // getNode().appendChild(sigPolicyHash);

        }
    }
}
