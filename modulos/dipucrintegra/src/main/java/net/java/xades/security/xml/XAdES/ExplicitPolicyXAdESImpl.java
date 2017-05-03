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
 */

/**
 * 
 * @author miro
 */
public class ExplicitPolicyXAdESImpl extends BasicXAdESImpl implements XAdES_EPES
{
    /*
     * private boolean useExplicitPolicy;
     * 
     * public ExplicitPolicyXAdES(Element baseElement) { this(baseElement, true); }
     * 
     * public ExplicitPolicyXAdES(Element baseElement, boolean useExplicitPolicy) {
     * super(baseElement); this.useExplicitPolicy = useExplicitPolicy; }
     */

    public ExplicitPolicyXAdESImpl(Element baseElement, boolean readOnlyMode, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix, String digestMethod)
    {
        super(baseElement, readOnlyMode, xadesPrefix, xadesNamespace, xmlSignaturePrefix,
                digestMethod);
    }

    public void setSignaturePolicyIdentifier(SignaturePolicyIdentifier signaturePolicyIdentifier)
    {
        if (readOnlyMode)
        {
            throw new UnsupportedOperationException("Set Method is not allowed. Read-only mode.");
        }

        if (signaturePolicyIdentifier != null)
        {
            data.put(XAdES.Element.SIGNATURE_POLICY_IDENTIFIER, signaturePolicyIdentifier);
        }
        else
        {
            data.remove(XAdES.Element.SIGNATURE_POLICY_IDENTIFIER);
        }
    }

    public SignaturePolicyIdentifier getSignaturePolicyIdentifier()
    {
        return (SignaturePolicyIdentifier) data.get(XAdES.Element.SIGNATURE_POLICY_IDENTIFIER);
    }
}
