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

/*
 * <p:CommitmentTypeIndication>
 *   <p:CommitmentTypeId>
 *      <p:Identifier Qualifier="OIDAsURI">http://tempuri.org</p:Identifier>
 *      <p:Description>p:Description</p:Description>
 *      <p:DocumentationReferences>
 *        <p:DocumentationReference>http://tempuri.org</p:DocumentationReference>
 *      </p:DocumentationReferences>
 *   </p:CommitmentTypeId>
 *   <p:ObjectReference>http://tempuri.org</p:ObjectReference>
 *   <p:CommitmentTypeQualifiers>
 *     <p:CommitmentTypeQualifier>ANYTYPE</p:CommitmentTypeQualifier>
 *   </p:CommitmentTypeQualifiers>
 * </p:CommitmentTypeIndication>
 * 
 */

public class CommitmentTypeIndicationDetails extends XAdESStructure
{
    public CommitmentTypeIndicationDetails(SignedDataObjectProperties signedDataObjectProperties,
            CommitmentTypeIndication commitmentTypeIndication, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        super(signedDataObjectProperties, "CommitmentTypeIndication", xadesPrefix, xadesNamespace,
                xmlSignaturePrefix);

        CommitmentTypeId commitmentTypeId = commitmentTypeIndication.getCommitmentTypeId();

        if (commitmentTypeId != null)
        {
            new CommitmentTypeIdDetails(this, commitmentTypeId, xadesPrefix, xadesNamespace,
                    xmlSignaturePrefix);
        }

        Element objectReference = createElement("ObjectReference");
        objectReference.setTextContent(commitmentTypeIndication.getObjectReference());

        Element commitmentTypeQualifiers = createElement("CommitmentTypeQualifiers");

        for (String qualifier : commitmentTypeIndication.getCommitmentTypeQualifiers())
        {
            Element commitmentTypeQualifier = createElement("CommitmentTypeQualifier");
            commitmentTypeQualifier.setTextContent(qualifier);
            commitmentTypeQualifiers.appendChild(commitmentTypeQualifier);
        }

        getNode().appendChild(objectReference);
        getNode().appendChild(commitmentTypeQualifiers);
    }
}
