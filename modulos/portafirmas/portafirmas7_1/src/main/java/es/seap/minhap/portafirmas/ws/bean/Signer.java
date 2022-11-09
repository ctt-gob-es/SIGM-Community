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
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Represents a signer.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signer", propOrder = { "userJob", "state", "fstate" })
public class Signer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(required = true)
	protected UserJob userJob;
	@XmlElement(required = true)
	protected State state;
	@XmlElement(required = true)
	protected XMLGregorianCalendar fstate;

	/**
	 * Gets the value of the userJob property.
	 * 
	 * @return possible object is {@link UserJob }
	 * 
	 */
	public UserJob getUserJob() {
		return userJob;
	}

	/**
	 * Sets the value of the userJob property.
	 * 
	 * @param value
	 *            allowed object is {@link UserJob }
	 * 
	 */
	public void setUserJob(UserJob value) {
		this.userJob = value;
	}

	/**
	 * Gets the value of the state property.
	 * 
	 * @return possible object is {@link State }
	 * 
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the value of the state property.
	 * 
	 * @param value
	 *            allowed object is {@link State }
	 * 
	 */
	public void setState(State value) {
		this.state = value;
	}

	/**
	 * Gets the value of the fstate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getFstate() {
		return fstate;
	}

	/**
	 * Sets the value of the fstate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setFstate(XMLGregorianCalendar value) {
		this.fstate = value;
	}

}
