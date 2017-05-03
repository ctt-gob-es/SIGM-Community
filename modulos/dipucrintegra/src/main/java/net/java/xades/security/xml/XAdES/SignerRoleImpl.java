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

import java.util.ArrayList;

public class SignerRoleImpl implements SignerRole
{
	private ArrayList<String> claimedRole;
	private ArrayList<String> certifiedRole;

	public SignerRoleImpl()
	{
		this.claimedRole = new ArrayList<String>();
		this.certifiedRole = new ArrayList<String>();
	}

	public ArrayList<String> getCertifiedRole()
	{
		return certifiedRole;
	}

	public void setCertifiedRole(ArrayList<String> certifiedRole)
	{
		this.certifiedRole = certifiedRole;
	}

	public ArrayList<String> getClaimedRole()
	{
		return claimedRole;
	}

	public void setClaimedRole(ArrayList<String> claimedRole)
	{
		this.claimedRole = claimedRole;
	}

	public void addClaimedRole(String role)
	{
		this.claimedRole.add(role);
	}

	public void addCertifiedRole(String role)
	{
		this.certifiedRole.add(role);
	}
}