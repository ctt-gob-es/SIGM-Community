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

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;

import javax.xml.crypto.dsig.DigestMethod;

import net.java.xades.util.Base64;

public class SigningCertificateImpl implements SigningCertificate
{
	private X509Certificate certificate;
	private String digestMethod;
	
	public SigningCertificateImpl(X509Certificate certificate, String digestMethod) 
	{
		this.certificate = certificate;
		this.digestMethod = digestMethod;
	}

	public String getDigestMethodAlgorithm() 
	{
		return digestMethod;
	}

	public String getDigestValue() throws GeneralSecurityException
	{
		String result;
		
		try
		{
		    String algorithm = "SHA-1";
		    
		    if (DigestMethod.SHA256.equals(digestMethod))
		    {
		        algorithm = "SHA-256";
		    }
		    else if (DigestMethod.SHA512.equals(digestMethod))
		    {
                algorithm = "SHA-512";
		    }
		    else if("http://www.w3.org/2001/04/xmldsig-more#sha384".equals(digestMethod)) {
		    	algorithm = "SHA-384";
		    }
		    
			MessageDigest md = MessageDigest.getInstance(algorithm);	
			md.update(certificate.getEncoded());
			result = Base64.encodeBytes(md.digest());
		}
		catch (Exception e)
		{
			throw new GeneralSecurityException(e);
		}
		
		return result;
	}

	public String getIssuerName() 
	{
		return certificate.getIssuerDN().getName();
	}

	public BigInteger getX509SerialNumber() 
	{
		return certificate.getSerialNumber();
	}
}
