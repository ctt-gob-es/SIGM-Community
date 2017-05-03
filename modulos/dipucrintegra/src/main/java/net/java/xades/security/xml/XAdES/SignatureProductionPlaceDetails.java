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
 * Sample usage of signing SignatureProductionPlace:
 * 
 * <xades:SignatureProductionPlace> <xades:City>p:City</p:City>
 * <xades:StateOrProvince>p:StateOrProvince</p:StateOrProvince>
 * <xades:PostalCode>p:PostalCode</p:PostalCode> <xades:CountryName>p:CountryName</p:CountryName>
 * </xades:SignatureProductionPlace>
 * 
 */

public class SignatureProductionPlaceDetails extends XAdESStructure
{
    public SignatureProductionPlaceDetails(SignedSignatureProperties ssp,
            SignatureProductionPlace signatureProductionPlace, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        super(ssp, "SignatureProductionPlace", xadesPrefix, xadesNamespace, xmlSignaturePrefix);

        if (signatureProductionPlace.getCity() != null)
        {
            Element city = createElement("City");
            city.setTextContent(signatureProductionPlace.getCity());
            getNode().appendChild(city);
        }

        if (signatureProductionPlace.getStateOrProvince() != null)
        {
            Element stateOrProvince = createElement("StateOrProvince");
            stateOrProvince.setTextContent(signatureProductionPlace.getStateOrProvince());
            getNode().appendChild(stateOrProvince);
        }

        if (signatureProductionPlace.getPostalCode() != null)
        {
            Element postalCode = createElement("PostalCode");
            postalCode.setTextContent(signatureProductionPlace.getPostalCode());
            getNode().appendChild(postalCode);
        }

        if (signatureProductionPlace.getCountryName() != null)
        {
            Element countryName = createElement("CountryName");
            countryName.setTextContent(signatureProductionPlace.getCountryName());
            getNode().appendChild(countryName);
        }
    }

    public SignatureProductionPlaceDetails(Node node, String xadesPrefix, String xadesNamespace,
            String xmlSignaturePrefix)
    {
        super(node, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
    }
}