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

import java.security.cert.X509Certificate;
import java.util.Collection;

import org.w3c.dom.Element;

/*
 <ds:Signature ID?>
 ...
 <ds:Object>
 <QualifyingProperties>
 ...
 <UnsignedProperties>
 <UnsignedSignatureProperties>
 (CompleteCertificateRefs)
 (CompleteRevocationRefs)
 (AttributeCertificateRefs)?
 (AttributeRevocationRefs)?
 </UnsignedSignatureProperties>
 </UnsignedProperties>
 </QualifyingProperties>
 </ds:Object>
 </ds:Signature>-
 */

/**
 * 
 * @author miro
 */
public class CompleteValidationXAdESImpl extends TimestampXAdESImpl implements XAdES_C
{

    /*
     * public CompleteValidationXAdESImpl(Element baseElement, boolean useExplicitPolicy) {
     * super(baseElement, useExplicitPolicy); }
     */

    public CompleteValidationXAdESImpl(Element baseElement, boolean readOnlyMode,
            String xadesPrefix, String xadesNamespace, String xmlSignaturePrefix,
            String digestMethod)
    {
        super(baseElement, readOnlyMode, xadesPrefix, xadesNamespace, xmlSignaturePrefix,
                digestMethod);
    }

    public CompleteCertificateRefs getCompleteCertificateRefs()
    {
        return (CompleteCertificateRefs) data.get(XAdES.Element.COMPLETE_CERTIFICATE_REFS);
    }

    public void setCompleteCertificateRefs(Collection<X509Certificate> caCertificates)
    {
        if (readOnlyMode)
        {
            throw new UnsupportedOperationException("Set Method is not allowed. Read-only mode.");
        }

        data.put(XAdES.Element.COMPLETE_CERTIFICATE_REFS, caCertificates);
    }

    public CompleteRevocationRefs getCompleteRevocationRefs()
    {
        return (CompleteRevocationRefs) data.get(XAdES.Element.COMPLETE_REVOCATION_REFS);
    }

    // public void setCompleteRevocationRefs(CertValidationInfo certValidationInfo)
    // {
    // if (readOnlyMode)
    // {
    // throw new UnsupportedOperationException("Set Method is not allowed. Read-only mode.");
    // }
    //
    // data.put(XAdES.Element.COMPLETE_REVOCATION_REFS, certValidationInfo);
    // }
}
