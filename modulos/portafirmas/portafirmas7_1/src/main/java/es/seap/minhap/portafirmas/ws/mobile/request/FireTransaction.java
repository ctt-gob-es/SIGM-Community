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


package es.seap.minhap.portafirmas.ws.mobile.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileStringList;


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
 *         &lt;element name="certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="idRequestList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileStringList"/>
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
    "certificate",
    "idRequestList"
})
@XmlRootElement(name = "fireTransaction")
public class FireTransaction {

    @XmlElement(required = true)
    protected String certificate;
    @XmlElement(required = true)
    protected MobileStringList idRequestList;

    /**
     * Gets the value of the certificate property.
     * 
     * @return
     *     possible object is
     *     String
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * Sets the value of the certificate property.
     * 
     * @param value
     *     allowed object is
     *     String
     */
    public void setCertificate(String value) {
        this.certificate = value;
    }

    /**
     * Gets the value of the idRequestList property.
     * 
     * @return
     *     possible object is
     *     {@link MobileStringList }
     *     
     */
    public MobileStringList getIdRequestList() {
        return idRequestList;
    }

    /**
     * Sets the value of the idRequestList property.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileStringList }
     *     
     */
    public void setIdRequestList(MobileStringList value) {
        this.idRequestList = value;
    }

}
