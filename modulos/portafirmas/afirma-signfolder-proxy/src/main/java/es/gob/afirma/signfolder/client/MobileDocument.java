
package es.gob.afirma.signfolder.client;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileDocument complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileDocument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signatureType" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileSignFormat" minOccurs="0"/>
 *         &lt;element name="signAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="operationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signatureParameters" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileDocument", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "identifier",
    "name",
    "mime",
    "signatureType",
    "signAlgorithm",
    "operationType",
    "signatureParameters",
    "data",
    "size"
})
public class MobileDocument {

    protected String identifier;
    protected String name;
    protected String mime;
    @XmlElementRef(name = "signatureType", type = JAXBElement.class, required = false)
    protected JAXBElement<MobileSignFormat> signatureType;
    @XmlElementRef(name = "signAlgorithm", type = JAXBElement.class, required = false)
    protected JAXBElement<String> signAlgorithm;
    protected String operationType;
    @XmlElementRef(name = "signatureParameters", type = JAXBElement.class, required = false)
    protected JAXBElement<String> signatureParameters;
    @XmlElementRef(name = "data", type = JAXBElement.class, required = false)
    protected JAXBElement<DataHandler> data;
    @XmlElementRef(name = "size", type = JAXBElement.class, required = false)
    protected JAXBElement<Integer> size;

    /**
     * Obtiene el valor de la propiedad identifier.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Define el valor de la propiedad identifier.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Obtiene el valor de la propiedad name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtiene el valor de la propiedad mime.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMime() {
        return mime;
    }

    /**
     * Define el valor de la propiedad mime.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMime(String value) {
        this.mime = value;
    }

    /**
     * Obtiene el valor de la propiedad signatureType.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MobileSignFormat }{@code >}
     *     
     */
    public JAXBElement<MobileSignFormat> getSignatureType() {
        return signatureType;
    }

    /**
     * Define el valor de la propiedad signatureType.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MobileSignFormat }{@code >}
     *     
     */
    public void setSignatureType(JAXBElement<MobileSignFormat> value) {
        this.signatureType = value;
    }

    /**
     * Obtiene el valor de la propiedad signAlgorithm.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSignAlgorithm() {
        return signAlgorithm;
    }

    /**
     * Define el valor de la propiedad signAlgorithm.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSignAlgorithm(JAXBElement<String> value) {
        this.signAlgorithm = value;
    }

    /**
     * Obtiene el valor de la propiedad operationType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * Define el valor de la propiedad operationType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperationType(String value) {
        this.operationType = value;
    }

    /**
     * Obtiene el valor de la propiedad signatureParameters.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSignatureParameters() {
        return signatureParameters;
    }

    /**
     * Define el valor de la propiedad signatureParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSignatureParameters(JAXBElement<String> value) {
        this.signatureParameters = value;
    }

    /**
     * Obtiene el valor de la propiedad data.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DataHandler }{@code >}
     *     
     */
    public JAXBElement<DataHandler> getData() {
        return data;
    }

    /**
     * Define el valor de la propiedad data.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DataHandler }{@code >}
     *     
     */
    public void setData(JAXBElement<DataHandler> value) {
        this.data = value;
    }

    /**
     * Obtiene el valor de la propiedad size.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getSize() {
        return size;
    }

    /**
     * Define el valor de la propiedad size.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setSize(JAXBElement<Integer> value) {
        this.size = value;
    }

}
