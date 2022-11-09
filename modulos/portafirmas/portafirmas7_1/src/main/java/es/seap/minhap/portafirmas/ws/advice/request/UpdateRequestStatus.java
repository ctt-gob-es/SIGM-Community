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


package es.seap.minhap.portafirmas.ws.advice.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.Signature;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "authentication", "request", "signature"
})
@XmlRootElement(name = "updateRequestStatus", namespace = "urn:juntadeandalucia:cice:pfirma:advice:request:v2.0")
public class UpdateRequestStatus {

    @XmlElement(required = true)
    protected Authentication authentication;
    
    @XmlElement(required = true)
    protected Request request;
    
    @XmlElement(required = true)
    protected Signature[] signature;

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
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * @return the signature
	 */
	public Signature[] getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(Signature[] signature) {
		this.signature = signature;
	}


}
