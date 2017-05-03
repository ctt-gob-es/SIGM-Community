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
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 <SignedSignatureProperties>
 (SigningTime)?
 (SigningCertificate)?
 (SignatureProductionPlace)?
 (SignerRole)?
 </SignedSignatureProperties>
 */

/**
 * 
 * @author miro
 */
public class SignedSignatureProperties extends XAdESStructure
{
    public SignedSignatureProperties(SignedProperties sp, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        super(sp, "SignedSignatureProperties", xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public SignedSignatureProperties(Node node, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public void setSigningTime()
    {
        setSigningTime(new Date());
    }

    public void setSigningTime(Date signingTime)
    {
        new SigningTime(this, signingTime, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public void setSigner(Signer signer)
    {
        new SignerDetails(this, signer, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public void setSigningCertificate(SigningCertificate signingCertificate)
            throws GeneralSecurityException
    {
        if (signingCertificate != null)
        {
            new SigningCertificateDetails(this, signingCertificate, xadesPrefix, xadesNamespace,
                    xmlSignaturePrefix);
        }
    }

    public void setSignerRole(SignerRole signerRole)
    {
        if (signerRole != null)
        {
            if (signerRole.getClaimedRole().size() > 0 || signerRole.getCertifiedRole().size() > 0)
            {
                new SignerRoleDetails(this, signerRole, xadesPrefix, xadesNamespace,
                        xmlSignaturePrefix);
            }
        }
    }

    public Signer getSigner()
    {
        SignerDetails details = getSignerDetails();
        if (details != null)
        {
            Signer signer = details.getSigner();
            return signer;
        }

        return null;
    }

    protected SignerDetails getSignerDetails()
    {
        Element element = getChildElementNS("SignerDetails");
        if (element != null)
            return new SignerDetails(element, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
        else
            return null;
    }

    public void setSignatureProductionPlace(SignatureProductionPlace signatureProductionPlace)
    {
        if (signatureProductionPlace != null)
        {
            new SignatureProductionPlaceDetails(this, signatureProductionPlace, xadesPrefix,
                    xadesNamespace, xmlSignaturePrefix);
        }
    }

    public void setSignaturePolicyIdentifier(SignaturePolicyIdentifier signaturePolicyIdentifier)
    {
        if (signaturePolicyIdentifier != null)
        {
            new SignaturePolicyIdentifierDetails(this, signaturePolicyIdentifier, xadesPrefix,
                    xadesNamespace, xmlSignaturePrefix);
        }
    }
}
