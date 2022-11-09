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

/**
 * Represents actions executed when requests are signed or passed.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "action", propOrder = { "state", "action", "type" })
public class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(required = true)
	protected State state;
	@XmlElement(required = true)
	protected String action;
	@XmlElement(required = true)
	protected String type;

	/**
	 * Gets the value of the state property.
	 * 
	 * @return possible object is {@link String }
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the value of the state property.
	 * 
	 * @param state
	 *            allowed object is {@link String }
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Gets the value of the action property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the value of the action property.
	 * 
	 * @param action
	 *            allowed object is {@link String }
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param type
	 *            allowed object is {@link String }
	 */
	public void setType(String type) {
		this.type = type;
	}

}
