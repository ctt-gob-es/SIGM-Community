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


package es.seap.minhap.portafirmas.ws.eeutil.utilfirma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for opcionesPagina complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="opcionesPagina">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="porcentajeDocumento" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="separacionX" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="separacionY" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "opcionesPagina", propOrder = {
    "porcentajeDocumento",
    "separacionX",
    "separacionY"
})
public class OpcionesPagina {

    protected Float porcentajeDocumento;
    protected Float separacionX;
    protected Float separacionY;

    /**
     * Gets the value of the porcentajeDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPorcentajeDocumento() {
        return porcentajeDocumento;
    }

    /**
     * Sets the value of the porcentajeDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPorcentajeDocumento(Float value) {
        this.porcentajeDocumento = value;
    }

    /**
     * Gets the value of the separacionX property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSeparacionX() {
        return separacionX;
    }

    /**
     * Sets the value of the separacionX property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSeparacionX(Float value) {
        this.separacionX = value;
    }

    /**
     * Gets the value of the separacionY property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSeparacionY() {
        return separacionY;
    }

    /**
     * Sets the value of the separacionY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSeparacionY(Float value) {
        this.separacionY = value;
    }

}
