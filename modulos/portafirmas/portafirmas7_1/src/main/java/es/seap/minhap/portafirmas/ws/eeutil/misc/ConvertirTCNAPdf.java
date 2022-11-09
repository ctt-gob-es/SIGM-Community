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


package es.seap.minhap.portafirmas.ws.eeutil.misc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for convertirTCNAPdf complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="convertirTCNAPdf">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aplicacionInfo" type="{http://service.ws.inside.dsic.mpt.es/}applicationLogin"/>
 *         &lt;element name="TCNInfo" type="{http://service.ws.inside.dsic.mpt.es/}TCNInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convertirTCNAPdf", propOrder = {
    "aplicacionInfo",
    "tcnInfo"
})
public class ConvertirTCNAPdf {

    @XmlElement(required = true)
    protected ApplicationLogin aplicacionInfo;
    @XmlElement(name = "TCNInfo", required = true)
    protected TCNInfo tcnInfo;

    /**
     * Gets the value of the aplicacionInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationLogin }
     *     
     */
    public ApplicationLogin getAplicacionInfo() {
        return aplicacionInfo;
    }

    /**
     * Sets the value of the aplicacionInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationLogin }
     *     
     */
    public void setAplicacionInfo(ApplicationLogin value) {
        this.aplicacionInfo = value;
    }

    /**
     * Gets the value of the tcnInfo property.
     * 
     * @return
     *     possible object is
     *     {@link TCNInfo }
     *     
     */
    public TCNInfo getTCNInfo() {
        return tcnInfo;
    }

    /**
     * Sets the value of the tcnInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCNInfo }
     *     
     */
    public void setTCNInfo(TCNInfo value) {
        this.tcnInfo = value;
    }

}
