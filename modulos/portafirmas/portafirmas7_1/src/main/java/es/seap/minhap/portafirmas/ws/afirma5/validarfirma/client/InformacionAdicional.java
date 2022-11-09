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


package es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="firmante" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                   &lt;element name="selloTiempo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="certificadoTSA" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "firmante"
})
@XmlRootElement(name = "informacionAdicional")
public class InformacionAdicional {

    @XmlElement(required = true)
    protected List<InformacionAdicional.Firmante> firmante;

    /**
     * Gets the value of the firmante property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firmante property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirmante().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InformacionAdicional.Firmante }
     * 
     * 
     */
    public List<InformacionAdicional.Firmante> getFirmante() {
        if (firmante == null) {
            firmante = new ArrayList<InformacionAdicional.Firmante>();
        }
        return this.firmante;
    }


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
     *         &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *         &lt;element name="selloTiempo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="certificadoTSA" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
        "certificado",
        "selloTiempo",
        "certificadoTSA"
    })
    public static class Firmante {

        @XmlElement(required = true)
        protected byte[] certificado;
        protected String selloTiempo;
        protected byte[] certificadoTSA;

        /**
         * Gets the value of the certificado property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getCertificado() {
            return certificado;
        }

        /**
         * Sets the value of the certificado property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setCertificado(byte[] value) {
            this.certificado = ((byte[]) value);
        }

        /**
         * Gets the value of the selloTiempo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSelloTiempo() {
            return selloTiempo;
        }

        /**
         * Sets the value of the selloTiempo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSelloTiempo(String value) {
            this.selloTiempo = value;
        }

        /**
         * Gets the value of the certificadoTSA property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getCertificadoTSA() {
            return certificadoTSA;
        }

        /**
         * Sets the value of the certificadoTSA property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setCertificadoTSA(byte[] value) {
            this.certificadoTSA = ((byte[]) value);
        }

    }

}
