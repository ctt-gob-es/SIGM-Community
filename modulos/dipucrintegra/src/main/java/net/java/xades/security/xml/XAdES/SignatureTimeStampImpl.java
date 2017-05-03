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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.xml.crypto.dsig.XMLSignature;

import net.java.xades.security.timestamp.TimeStampFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SignatureTimeStampImpl implements SignatureTimeStamp
{
    private byte[] data;

    public SignatureTimeStampImpl(byte[] data)
    {
        this.data = data;
    }

    private Node getSignatureValue(Document base)
    {
        NodeList nl = base.getElementsByTagNameNS(XMLSignature.XMLNS, "SignatureValue");

        if (nl.getLength() > 0)
        {
            return nl.item(0);
        }
        else
        {
            return null;
        }
    }

    public byte[] generateEncapsulatedTimeStamp(Document parent, String tsaURL)
            throws NoSuchAlgorithmException, SignatureException, IOException
    {
        Node signatureValue = getSignatureValue(parent);

        return TimeStampFactory.getTimeStamp(tsaURL, this.data, true);
    }
}
