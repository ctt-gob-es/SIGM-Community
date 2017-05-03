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

import javax.xml.crypto.dsig.XMLSignature;
import org.w3c.dom.Node;

/*
 * 
 * <SignedProperties>
 *   <SignedSignatureProperties>
 *     (SigningTime)?
 *     (SigningCertificate)?
 *     (SignatureProductionPlace)?
 *     (SignerRole)?
 *   </SignedSignatureProperties>
 *   <SignedDataObjectProperties>
 *     (DataObjectFormat)*
 *     (CommitmentTypeIndication)*
 *     (AllDataObjectsTimeStamp)*
 *     (IndividualDataObjectsTimeStamp)*
 *   </SignedDataObjectProperties>
 *   </SignedProperties>
 *   
 */

/**
 * 
 * @author miro
 */
public class SignedProperties extends XAdESStructure
{
    private SignedSignatureProperties signedSignatureProperties;
    private SignedDataObjectProperties signedDataObjectProperties;

    public SignedProperties(QualifyingProperties qp, String signatureIdPrefix, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        super(qp, "SignedProperties", xadesPrefix, xadesNamespace, xmlSignaturePrefix);

        setAttributeNS(xadesNamespace, ID_ATTRIBUTE, signatureIdPrefix + "-SignedProperties");
    }

    public SignedProperties(Node node, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }

    public SignedSignatureProperties getSignedSignatureProperties()
    {
        if (signedSignatureProperties == null)
        {
            signedSignatureProperties = new SignedSignatureProperties(this, xadesPrefix,
                    xadesNamespace, xmlSignaturePrefix);
        }

        return signedSignatureProperties;
    }

    public SignedDataObjectProperties getSignedDataObjectProperties()
    {
        if (signedDataObjectProperties == null)
        {
            signedDataObjectProperties = new SignedDataObjectProperties(this, xadesPrefix,
                    xadesNamespace, xmlSignaturePrefix);
        }

        return signedDataObjectProperties;
    }
}
