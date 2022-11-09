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


package es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValidacionSimpleInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValidacionSimpleInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoResultado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descResultado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="excepcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValidacionSimpleInfo", propOrder = {
    "codigoResultado",
    "descResultado",
    "excepcion"
})
public class ValidacionSimpleInfo {

    @XmlElement(required = true)
    protected String codigoResultado;
    @XmlElement(required = true)
    protected String descResultado;
    protected String excepcion;

    /**
     * Gets the value of the codigoResultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoResultado() {
        return codigoResultado;
    }

    /**
     * Sets the value of the codigoResultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoResultado(String value) {
        this.codigoResultado = value;
    }

    /**
     * Gets the value of the descResultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescResultado() {
        return descResultado;
    }

    /**
     * Sets the value of the descResultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescResultado(String value) {
        this.descResultado = value;
    }

    /**
     * Gets the value of the excepcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExcepcion() {
        return excepcion;
    }

    /**
     * Sets the value of the excepcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExcepcion(String value) {
        this.excepcion = value;
    }

}
