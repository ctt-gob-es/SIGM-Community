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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.java.xades.util.Base64;

public class SignaturePolicyIdentifierImpl implements SignaturePolicyIdentifier
{    
    private boolean implied;
    private URL identifier;
    private String description;
    private String qualifier;
    private String hashBase64;
    
    public SignaturePolicyIdentifierImpl(boolean implied)
    {
        this.implied = implied;
    }

    public byte[] inputStreamToByteArray(InputStream in) throws IOException
    {
        byte[] buffer = new byte[2048];
        int length = 0;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while ((length = in.read(buffer)) >= 0)
        {
            baos.write(buffer, 0, length);
        }

        return baos.toByteArray();
    }
    
    public void setIdentifier(String identifier) throws IOException, NoSuchAlgorithmException
    {
        this.identifier = new URL(identifier);
        URLConnection conn = this.identifier.openConnection();
        
        byte[] data = inputStreamToByteArray(conn.getInputStream());
        
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(data);
        byte[] hash = md.digest();

        this.hashBase64 = Base64.encodeBytes(hash);        
    }
    
    public boolean isImplied()
    {
        return implied;
    }

    public void setImplied(boolean implied)
    {
        this.implied = implied;
    }

    public String getIdentifier()
    {
        return identifier.toString();
    }

    public void setIdentifier(URL identifier)
    {
        this.identifier = identifier;
    }

    public String getHashBase64()
    {
        return hashBase64;
    }

    public void setHashBase64(String hashBase64)
    {
        this.hashBase64 = hashBase64;
    }

    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;        
    }
    
    public String getQualifier()
    {
        return qualifier;
    }

    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;        
    }    
}
