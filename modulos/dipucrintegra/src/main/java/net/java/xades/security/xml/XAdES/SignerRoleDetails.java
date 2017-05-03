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

/**
 * 
 * <p:SignerRole>
 * <p:ClaimedRoles>
 * <p:ClaimedRole>
 * ANYTYPE
 * </p:ClaimedRole>
 * </p:ClaimedRoles>
 * <p:CertifiedRoles>
 * <p:CertifiedRole Encoding="http://tempuri.org" Id="id">
 * 0
 * </p:CertifiedRole>
 * </p:CertifiedRoles> </p:SignerRole>
 * 
 */

public class SignerRoleDetails extends XAdESStructure
{
    public SignerRoleDetails(SignedSignatureProperties ssp, SignerRole signerRole,
            String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix)
    {
        super(ssp, "SignerRole", xadesPrefix, xadesNamespace, xmlSignaturePrefix);

        Element claimedRoles = createElement("ClaimedRoles");
        Element certifiedRoles = createElement("ClaimedRoles");

        for (String sr : signerRole.getClaimedRole())
        {
            Element claimedRole = createElement("ClaimedRole");
            claimedRole.setTextContent(sr);
            claimedRoles.appendChild(claimedRole);
        }

        // TODO: Implement support for certified role and attribute certificates management
        for (String sr : signerRole.getCertifiedRole())
        {
            Element certifiedRole = createElement("CertifiedRole");
            certifiedRole.setTextContent(sr);
            certifiedRoles.appendChild(certifiedRole);
        }

        if (signerRole.getClaimedRole().size() > 0)
        {
            getNode().appendChild(claimedRoles);
        }

        if (signerRole.getCertifiedRole().size() > 0)
        {
            getNode().appendChild(certifiedRoles);
        }
    }

    public SignerRoleDetails(Node node, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }
}
