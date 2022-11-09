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

package es.seap.minhap.portafirmas.ws.admin.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for deleteDocumentsTypeResponse element declaration.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "applicationId" })
@XmlRootElement(name = "deleteDocumentsTypeResponse")
public class DeleteDocumentsTypeResponse {

	@XmlElement(required = true)
	protected String applicationId;

	/**
	 * Gets the value of the applicationId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the value of the applicationId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setApplicationId(String value) {
		this.applicationId = value;
	}

}
