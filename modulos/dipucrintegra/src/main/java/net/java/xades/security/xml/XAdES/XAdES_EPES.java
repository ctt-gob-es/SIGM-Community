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


/**
 *
4.4.2 Explicit policy electronic signatures (XAdES-EPES)
An Explicit Policy based Electronic Signature (XAdES-EPES) form in accordance with the present document,
extends the definition of an electronic signature to conform to the identified signature policy. A XAdES-EPES builds up
on a XMLDSIG or XAdES-BES forms by incorporating the SignaturePolicyIdentifier element. This signed
property indicates that a signature policy MUST be used for signature validation. It MAY explicitly identify the
signature policy. Other properties may be required by the mandated policy.
Clause 7.2.3 provides details on the specification of SignaturePolicyIdentifier property. Specification of the
actual signature policies is outside the scope of the current document. Further information on signature policies is
provided in TR 102 038 [12].
The structure of the XAdES-EPES (created by direct incorporation of the qualifying information to a XAdES-BES
form) is illustrated below.
        <ds:Signature ID?>
            ...
            <ds:Object>
                <QualifyingProperties>
                    <SignedProperties>
                        <SignedSignatureProperties>
                            (SignaturePolicyIdentifier)
                        </SignedSignatureProperties>
                    </SignedProperties>
                </QualifyingProperties>
            </ds:Object>
        </ds:Signature>-
 *
 **/


/**
 *
 * @author miro
 */
public interface XAdES_EPES
    extends XAdES_BES
{
    public SignaturePolicyIdentifier getSignaturePolicyIdentifier();
    public void setSignaturePolicyIdentifier(SignaturePolicyIdentifier signaturePolicyIdentifier);    
}
