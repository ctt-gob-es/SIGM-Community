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


package es.seap.minhap.portafirmas.ws.mobile.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mobileAccesoClave complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mobileAccesoClave">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claveServiceUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="excludedIdPList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="forcedIdP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="samlRequest" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileAccesoClave", propOrder = {
	"claveServiceUrl",	
    "excludedIdPList",
    "forcedIdP",
    "samlRequest"
})
public class MobileAccesoClave {

    @XmlElement(name = "claveServiceUrl")
    protected String claveServiceUrl;
    @XmlElement(name = "excludedIdPList")
    protected String excludedIdPList;
    @XmlElement(name = "forcedIdP")
    protected String forcedIdP;
    @XmlElement(name = "samlRequest")
    protected String samlRequest;
    
	public String getClaveServiceUrl() {
		return claveServiceUrl;
	}
	public void setClaveServiceUrl(String claveServiceUrl) {
		this.claveServiceUrl = claveServiceUrl;
	}
	public String getExcludedIdPList() {
		return excludedIdPList;
	}
	public void setExcludedIdPList(String excludedIdPList) {
		this.excludedIdPList = excludedIdPList;
	}
	public String getForcedIdP() {
		return forcedIdP;
	}
	public void setForcedIdP(String forcedIdP) {
		this.forcedIdP = forcedIdP;
	}
	public String getSamlRequest() {
		return samlRequest;
	}
	public void setSamlRequest(String samlRequest) {
		this.samlRequest = samlRequest;
	}

}
