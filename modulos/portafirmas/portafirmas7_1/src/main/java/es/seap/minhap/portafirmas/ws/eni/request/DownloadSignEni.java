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

package es.seap.minhap.portafirmas.ws.eni.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.bean.Authentication;

/**
 * Java class for downloadSign element declaration.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "authentication", "documentId", "metadatosEni", "metadatosAdicionales" })
@XmlRootElement(name = "downloadSignEni")
public class DownloadSignEni {

	@XmlElement(required = true)
	protected String documentId;
	
	@XmlElement(required = true)
	protected Authentication authentication;
	
	@XmlElement(required = true)
	protected MetadataEni metadatosEni;
	
	@XmlElement(required = false)
	protected TipoMetadatosAdicionales metadatosAdicionales;

	/**
	 * Gets the value of the documentId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * Sets the value of the documentId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDocumentId(String value) {
		this.documentId = value;
	}

	/**
	 * @return the authentication
	 */
	public Authentication getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * @return the metadatosEni
	 */
	public MetadataEni getMetadatosEni() {
		return metadatosEni;
	}

	/**
	 * @param metadatosEni the metadatosEni to set
	 */
	public void setMetadatosEni(MetadataEni metadatosEni) {
		this.metadatosEni = metadatosEni;
	}

	/**
	 * @return the metadatosAdicionales
	 */
	public TipoMetadatosAdicionales getMetadatosAdicionales() {
		return metadatosAdicionales;
	}

	/**
	 * @param metadatosAdicionales the metadatosAdicionales to set
	 */
	public void setMetadatosAdicionales(
			TipoMetadatosAdicionales metadatosAdicionales) {
		this.metadatosAdicionales = metadatosAdicionales;
	}

	
}
