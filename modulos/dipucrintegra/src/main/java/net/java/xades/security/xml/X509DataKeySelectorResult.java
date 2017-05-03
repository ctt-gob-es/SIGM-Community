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
package net.java.xades.security.xml;

import java.security.Key;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;

/**
 *
 * @author miro
 */
public class X509DataKeySelectorResult
    implements KeySelectorResult
{
    private X509IssuerSerial x509IssuerSerial;
    private X509Certificate x509Certificate;
    private X509CRL x509CRL;
    private String subjectName;
    private byte[] subjectKeyId;
    private List<XMLStructure> unrecognizedObjects;

    public X509DataKeySelectorResult(X509Data data)
    {
        List content = data.getContent();
        unrecognizedObjects = new ArrayList<XMLStructure>(content.size());
        for(Object obj : content)
        {
            if(obj instanceof X509IssuerSerial)
                x509IssuerSerial = (X509IssuerSerial)obj;
            else if(obj instanceof X509Certificate)
                x509Certificate = (X509Certificate)obj;
            else if(obj instanceof X509CRL)
                x509CRL = (X509CRL)obj;
            else if(obj instanceof String)
                subjectName = (String)obj;
            else if(obj instanceof byte[])
                subjectKeyId = (byte[])obj;
            else
                unrecognizedObjects.add((XMLStructure)obj);
        }
    }

    public Key getKey()
    {
        if(x509Certificate != null)
            return x509Certificate.getPublicKey();
        else
            return null;
    }

    public X509IssuerSerial getX509IssuerSerial()
    {
        return x509IssuerSerial;
    }

    public X509Certificate getX509Certificate()
    {
        return x509Certificate;
    }

    public X509CRL getX509CRL()
    {
        return x509CRL;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public byte[] getSubjectKeyId()
    {
        return subjectKeyId;
    }

    public List<XMLStructure> getUnrecognizedObjects()
    {
        return unrecognizedObjects;
    }
}
