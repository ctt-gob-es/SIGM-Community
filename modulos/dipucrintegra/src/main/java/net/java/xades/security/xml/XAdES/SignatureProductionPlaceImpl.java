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

public class SignatureProductionPlaceImpl implements SignatureProductionPlace
{
	private String city;
	private String stateOrProvince;
	private String postalCode;
	private String countryName;

	public SignatureProductionPlaceImpl() 
	{
	}

	public SignatureProductionPlaceImpl(String city, String stateOrProvince, String postalCode, String countryName) 
	{
		this.city = city;
		this.stateOrProvince = stateOrProvince;
		this.postalCode = postalCode;
		this.countryName = countryName;
	}
	
	public String getCity() 
	{
		return city;
	}

	public void setCity(String city) 
	{
		this.city = city;
	}

	public String getStateOrProvince() 
	{
		return stateOrProvince;
	}

	public void setStateOrProvince(String stateOrProvince) 
	{
		this.stateOrProvince = stateOrProvince;
	}

	public String getPostalCode() 
	{
		return postalCode;
	}

	public void setPostalCode(String postalCode) 
	{
		this.postalCode = postalCode;
	}

	public String getCountryName() 
	{
		return countryName;
	}

	public void setCountryName(String countryName) 
	{
		this.countryName = countryName;
	}
}