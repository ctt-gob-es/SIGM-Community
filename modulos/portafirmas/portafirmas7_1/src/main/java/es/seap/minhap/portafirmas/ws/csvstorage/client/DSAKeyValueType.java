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


package es.seap.minhap.portafirmas.ws.csvstorage.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DSAKeyValueType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DSAKeyValueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="P" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="Q" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="G" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="Y" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="J" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="Seed" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="PgenCounter" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DSAKeyValueType", namespace = "http://www.w3.org/2000/09/xmldsig#", propOrder = {
    "p",
    "q",
    "g",
    "y",
    "j",
    "seed",
    "pgenCounter"
})
public class DSAKeyValueType {

    @XmlElement(name = "P", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected byte[] p;
    @XmlElement(name = "Q", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected byte[] q;
    @XmlElement(name = "G", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected byte[] g;
    @XmlElement(name = "Y", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected byte[] y;
    @XmlElement(name = "J", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected byte[] j;
    @XmlElement(name = "Seed", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected byte[] seed;
    @XmlElement(name = "PgenCounter", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected byte[] pgenCounter;

    /**
     * Obtiene el valor de la propiedad p.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getP() {
        return p;
    }

    /**
     * Define el valor de la propiedad p.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setP(byte[] value) {
        this.p = value;
    }

    /**
     * Obtiene el valor de la propiedad q.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getQ() {
        return q;
    }

    /**
     * Define el valor de la propiedad q.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setQ(byte[] value) {
        this.q = value;
    }

    /**
     * Obtiene el valor de la propiedad g.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getG() {
        return g;
    }

    /**
     * Define el valor de la propiedad g.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setG(byte[] value) {
        this.g = value;
    }

    /**
     * Obtiene el valor de la propiedad y.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getY() {
        return y;
    }

    /**
     * Define el valor de la propiedad y.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setY(byte[] value) {
        this.y = value;
    }

    /**
     * Obtiene el valor de la propiedad j.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getJ() {
        return j;
    }

    /**
     * Define el valor de la propiedad j.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setJ(byte[] value) {
        this.j = value;
    }

    /**
     * Obtiene el valor de la propiedad seed.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSeed() {
        return seed;
    }

    /**
     * Define el valor de la propiedad seed.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSeed(byte[] value) {
        this.seed = value;
    }

    /**
     * Obtiene el valor de la propiedad pgenCounter.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPgenCounter() {
        return pgenCounter;
    }

    /**
     * Define el valor de la propiedad pgenCounter.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPgenCounter(byte[] value) {
        this.pgenCounter = value;
    }

}
