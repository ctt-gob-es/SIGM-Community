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

/*
<!-- Start SignerRole -->
    <xsd:element name="SignerRole" type="SignerRoleType"/>
    <xsd:complexType name="SignerRoleType">
        <xsd:sequence>
            <xsd:element name="ClaimedRoles" type="ClaimedRolesListType"
                ETSI
                ETSI TS 101 903 V1.3.2 (2006-03) 68
                minOccurs="0"/>
            <xsd:element name="CertifiedRoles" type="CertifiedRolesListType"
                minOccurs="0"/>
        </xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="ClaimedRolesListType">
        <xsd:sequence>
            <xsd:element name="ClaimedRole" type="AnyType" maxOccurs="unbounded"/>
        </xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="CertifiedRolesListType">
        <xsd:sequence>
            <xsd:element name="CertifiedRole" type="EncapsulatedPKIDataType"
                maxOccurs="unbounded"/>
        </xsd:sequence>
    </xsd:complexType>
<!-- End SignerRole -->
*/


/**
 *
 * @author miro
 */
public interface RoleType
{
}
