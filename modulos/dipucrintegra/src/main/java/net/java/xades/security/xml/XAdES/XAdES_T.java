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

import java.util.List;

/**
 *
4.4.3 Electronic signature formats with validation data
Validation of an electronic signature in accordance with the present document requires additional data needed to
validate the electronic signature. This additional data is called validation data; and includes:
• Public Key Certificates (PKCs) and Attributes Certificates (ACs);
• revocation status information for each PKC and AC;
• trusted time-stamps applied to the digital signature or a time-mark that shall be available in an audit log;
• when appropriate, the details of a signature policy to be used to verify the electronic signature.
The validation data may be collected by the signer and/or the verifier. When the signature policy identifier is present, it
shall meet the requirements of the signature policy. Validation data includes CA certificates as well as revocation status
information in the form of Certificate Revocation Lists (CRLs) or certificate status information (OCSP) provided by an
on-line service. Validation data also includes evidence that the signature was created before a particular point in time.
This may be either a time-stamp token or time-mark.
The present document defines properties able to contain validation data. Clauses below summarize some signature
formats that incorporate them and their most relevant characteristics.
4.4.3.1 Electronic signature with time (XAdES-T)
XML Advanced Electronic Signature with Time (XAdES-T) is a signature for which there exists a trusted time
associated to the signature. The trusted time may be provided by two different means:
• the SignatureTimeStamp as an unsigned property added to the electronic signature;
• a time mark of the electronic signature provided by a trusted service provider.
A time-mark provided by a Trusted Service would have similar effect to the SignatureTimeStamp property but in
this case no property is added to the electronic signature as it is the responsibility of the TSP to provide evidence of a
time mark when required to do so. The management of time marks is outside the scope of the current document.
Trusted time provides the initial steps towards providing long term validity.
Below follows the structure of a XAdES-T form built on a XAdES-BES or a XAdES-EPES, by direct incorporation of a
time-stamp token within the SignatureTimeStamp element. A XAdES-T form based on time-marks MAY exist
without such an element.

        <ds:Signature ID?>
            ...
            <ds:Object>
                <QualifyingProperties>
                    ...
                    <UnsignedProperties>
                        <UnsignedSignatureProperties>
                            (SignatureTimeStamp)+
                        </UnsignedSignatureProperties>
                    </UnsignedProperties>
                </QualifyingProperties>
            </ds:Object>
        </ds:Signature>-

XMLDISG
|
<ds:Signature ID?>- - - - - - - - +- - - - +- - - +
<ds:SignedInfo> | | |
<ds:CanonicalizationMethod/> | | |
<ds:SignatureMethod/> | | |
(<ds:Reference URI? > | | |
(<ds:Transforms>)? | | |
<ds:DigestMethod/> | | |
<ds:DigestValue/> | | |
</ds:Reference>)+ | | |
</ds:SignedInfo> | | |
<ds:SignatureValue/> | | |
(<ds:KeyInfo>)? - - - - - - - - + | |
| |
<ds:Object> | |
| |
<QualifyingProperties> | |
| |
<SignedProperties> | |
| |
<SignedSignatureProperties> | |
(SigningTime)? | |
(SigningCertificate)? | |
(SignaturePolicyIdentifier)? | |
(SignatureProductionPlace)? | |
(SignerRole)? | |
</SignedSignatureProperties> | |
| |
<SignedDataObjectProperties> | |
(DataObjectFormat)* | |
(CommitmentTypeIndication)* | |
(AllDataObjectsTimeStamp)* | |
(IndividualDataObjectsTimeStamp)*| |
</SignedDataObjectProperties> | |
| |
</SignedProperties> | |
| |
<UnsignedProperties> | |
| |
<UnsignedSignatureProperties> | |
(CounterSignature)*- - - - - - - + |
(SignatureTimeStamp)+ |
</UnsignedSignatureProperties>- - -+ |
| |
</UnsignedProperties> | |
| |
</QualifyingProperties> | |
| |
</ds:Object> | |
| |
</ds:Signature>- - - - - - - - - - - - - - +- - - +
| |
XAdES-BES(-EPES) |
|
XAdES-T
 *
 **

/**
 *
 * @author miro
 */
public interface XAdES_T
    extends XAdES_EPES
{
    public List<SignatureTimeStamp> getSignatureTimeStamps();
    public void setSignatureTimeStamps(List<SignatureTimeStamp> signatureTimeStamps);    
}
