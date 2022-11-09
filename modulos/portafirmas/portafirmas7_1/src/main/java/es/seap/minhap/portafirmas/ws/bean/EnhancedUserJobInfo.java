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
@XmlType(name = "enhancedUserJobInfo", propOrder = { "seat", "valid", "visibleOtherSeats", "parameterList"})

public class EnhancedUserJobInfo implements Serializable {

	private static final long serialVersionUID = 1743542860025427549L;
	
	@XmlElement(required = false)
	private Seat seat;
	
	
	@XmlElement(required = true)
	private boolean valid;
	
	@XmlElement(required = true)
	private boolean visibleOtherSeats;	
	
	@XmlElement(required = false)
	private ParameterList parameterList;
	
	
	/**
	 * Gets the value of the seat property.
	 * 
	 * @return possible object is {@link Seat }
	 * 
	 */
	public Seat getSeat() {
		return seat;
	}

	/**
	 * Sets the value of the seat property.
	 * 
	 * @param value
	 *            allowed object is {@link Seat }
	 * 
	 */
	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	
	/**
	 * Gets the value of the visibleOtherSeats property.
	 * 
	 * @return possible object is {@link boolean }
	 * 
	 */
	public boolean isVisibleOtherSeats() {
		return visibleOtherSeats;
	}

	/**
	 * Set the value of the valid property.
	 * 
	 * @param value
	 *            allowed object is {@link boolean }
	 * 
	 */

	public void setVisibleOtherSeats(boolean visibleOtherSeats) {
		this.visibleOtherSeats = visibleOtherSeats;
	}
	
	public boolean isValid() {
		return valid;
	}

	/**
	 * Set the value of the valid property.
	 * 
	 * @param value
	 *            allowed object is {@link boolean }
	 * 
	 */

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public ParameterList getParameterList () {
		return this.parameterList;
	}
	
	public void setParameterList (ParameterList parameterList) {
		this.parameterList = parameterList;
	}
	
	
	

}
