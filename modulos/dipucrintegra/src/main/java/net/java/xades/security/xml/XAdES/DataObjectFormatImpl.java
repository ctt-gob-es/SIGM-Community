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

public class DataObjectFormatImpl implements DataObjectFormat 
{
	private String description;
	private ObjectIdentifier objectIdentifier;
	private String mimeType;
	private String encoding;
	private String objectReference;
	
	public DataObjectFormatImpl()
	{		
	}

	public DataObjectFormatImpl(String description, ObjectIdentifier objectIdentifier, String mimeType, String encoding, String objectReference)
	{		
		this.description = description;
		this.objectIdentifier = objectIdentifier;
		this.mimeType = mimeType;
		this.encoding = encoding;
		this.objectReference = objectReference;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	public ObjectIdentifier getObjectIdentifier() 
	{
		return objectIdentifier;
	}
	
	public void setObjectIdentifier(ObjectIdentifier objectIdentifier) 
	{
		this.objectIdentifier = objectIdentifier;
	}
	
	public String getMimeType() 
	{
		return mimeType;
	}
	
	public void setMimeType(String mimeType) 
	{
		this.mimeType = mimeType;
	}
	
	public String getEncoding() 
	{
		return encoding;
	}
	
	public void setEncoding(String encoding) 
	{
		this.encoding = encoding;
	}

	public String getObjectReference() {
		return objectReference;
	}

	public void setObjectReference(String objectReference) {
		this.objectReference = objectReference;
	}
	
	
}
