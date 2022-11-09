/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.ws.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedUser", propOrder = { "user", "enhancedUserJobInfo"})

public class EnhancedUser implements Serializable {

	private static final long serialVersionUID = 1976014565601293106L;
	
	@XmlElement(required = true)
	private User user;
	@XmlElement(required = true)
	private EnhancedUserJobInfo enhancedUserJobInfo;
	
	/**
	 * Gets the value of the user property.
	 * 
	 * @return possible object is {@link User }
	 * 
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the value of the user property.
	 * 
	 * @param value
	 *            allowed object is {@link User }
	 * 
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the value of the enhancedUserJobInfo property.
	 * 
	 * @return possible object is {@link EnhancedUserJobInfo }
	 * 
	 */
	public EnhancedUserJobInfo getEnhancedUserJobInfo() {
		return enhancedUserJobInfo;
	}

	/**
	 * Sets the value of the enhancedUserJobInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link EnhancedUserJobInfo }
	 * 
	 */
	public void setEnhancedUserJobInfo(EnhancedUserJobInfo enhancedUserJobInfo) {
		this.enhancedUserJobInfo = enhancedUserJobInfo;
	}
	

}
