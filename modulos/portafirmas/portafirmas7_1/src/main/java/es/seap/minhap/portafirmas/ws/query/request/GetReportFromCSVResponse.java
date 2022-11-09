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

package es.seap.minhap.portafirmas.ws.query.request;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "reportDocument" })
@XmlRootElement(name = "getReportFromCSVResponse")
public class GetReportFromCSVResponse {

	@XmlElement(required = true)
	@XmlMimeType("application/octet-stream")
	protected DataHandler reportDocument;

	/**
	 * Gets the value of the reportDocument property.
	 * 
	 * @return possible object is byte[]
	 */
	public DataHandler getReportDocument() {
		return reportDocument;
	}

	/**
	 * Sets the value of the reportDocument property.
	 * 
	 * @param value
	 *            allowed object is byte[]
	 */
	public void setReportDocument(DataHandler value) {
		this.reportDocument = ((DataHandler) value);
	}
}
