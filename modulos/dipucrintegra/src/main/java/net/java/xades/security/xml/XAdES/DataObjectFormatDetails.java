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
 * <p:DataObjectFormat ObjectReference="http://tempuri.org">
 *   <p:Description>p:Description</p:Description>
 *   <p:ObjectIdentifier>
 *     <p:Identifier Qualifier="OIDAsURI">http://tempuri.org</p:Identifier>
 *     <p:Description>p:Description</p:Description>
 *     <p:DocumentationReferences>
 *       <p:DocumentationReference>http://tempuri.org</p:DocumentationReference>
 *     </p:DocumentationReferences>
 *   </p:ObjectIdentifier>
 *   <p:MimeType>p:MimeType</p:MimeType>
 *   <p:Encoding>http://tempuri.org</p:Encoding>
 * </p:DataObjectFormat>
 * 
 */

public class DataObjectFormatDetails extends XAdESStructure
{
    public DataObjectFormatDetails(SignedDataObjectProperties signedDataObjectProperties,
            DataObjectFormat dataObjectFormat, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(signedDataObjectProperties, "DataObjectFormat", xadesPrefix, xadesNamespace,
                xmlSignaturePrefix);

        Element description = createElement("Description");
        description.setPrefix(xadesPrefix);
        description.setTextContent(dataObjectFormat.getDescription());

        ObjectIdentifier objectIdentifier = dataObjectFormat.getObjectIdentifier();

        if (objectIdentifier != null)
        {
            new ObjectIdentifierDetails(this, objectIdentifier, xadesPrefix, xadesNamespace,
                    xmlSignaturePrefix);
        }

        Element mimetype = createElement("MimeType");
        mimetype.setPrefix(xadesPrefix);
        mimetype.setTextContent(dataObjectFormat.getMimeType());

        Element encoding = createElement("Encoding");
        encoding.setPrefix(xadesPrefix);
        encoding.setTextContent(dataObjectFormat.getEncoding());
        
        // TODO: must ensure that there is an ObjectReference attribute, otherwise an Exception
        // should be raised
        setAttributeNS(xadesNamespace, "ObjectReference", dataObjectFormat.getObjectReference());
        
        getNode().appendChild(description);
        getNode().appendChild(mimetype);
        getNode().appendChild(encoding);
    }
}
