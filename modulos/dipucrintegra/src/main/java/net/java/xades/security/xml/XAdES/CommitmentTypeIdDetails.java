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
 * 
 * <p:CommitmentTypeId>
 *   <p:Identifier Qualifier="OIDAsURI">http://tempuri.org</p:Identifier>
 *   <p:Description>p:Description</p:Description>
 *   <p:DocumentationReferences>
 *     <p:DocumentationReference>http://tempuri.org</p:DocumentationReference>
 *   </p:DocumentationReferences>
 *  </p:CommitmentTypeId>
 * 
 */

public class CommitmentTypeIdDetails extends XAdESStructure
{
    public CommitmentTypeIdDetails(CommitmentTypeIndicationDetails commitmentTypeIndicationDetails,
            CommitmentTypeId commitmentTypeId, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(commitmentTypeIndicationDetails, "CommitmentTypeId", xadesPrefix, xadesNamespace,
                xmlSignaturePrefix);

        Element identifier = createElement("Identifier");
        identifier.setTextContent(commitmentTypeId.getIdentifier());
        identifier.setAttribute("Qualifier", commitmentTypeId.getQualifier());
        getNode().appendChild(identifier);

        Element description = createElement("Description");
        description.setTextContent(commitmentTypeId.getDescription());
        getNode().appendChild(description);

        if (commitmentTypeId.getDocumentationReferences().size() > 0)
        {
            Element documentationReferences = createElement("DocumentationReferences");

            for (String reference : commitmentTypeId.getDocumentationReferences())
            {
                Element documentationReference = createElement("DocumentationReference");
                documentationReference.setTextContent(reference);
                documentationReferences.appendChild(documentationReference);
            }

            getNode().appendChild(documentationReferences);
        }
    }
}