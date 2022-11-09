
package es.gob.afirma.signfolder.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileSignLine complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileSignLine">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mobileSignerList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileStringList" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileSignLine", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "mobileSignerList",
    "type",
    "terminate"
})
public class MobileSignLine {

    @XmlElementRef(name = "mobileSignerList", type = JAXBElement.class, required = false)
    protected JAXBElement<MobileStringList> mobileSignerList;
    @XmlElementRef(name = "type", type = JAXBElement.class, required = false)
    protected JAXBElement<String> type;
    @XmlElementRef(name = "terminate", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> terminate;

    /**
     * Obtiene el valor de la propiedad mobileSignerList.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MobileStringList }{@code >}
     *     
     */
    public JAXBElement<MobileStringList> getMobileSignerList() {
        return mobileSignerList;
    }

    /**
     * Define el valor de la propiedad mobileSignerList.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MobileStringList }{@code >}
     *     
     */
    public void setMobileSignerList(JAXBElement<MobileStringList> value) {
        this.mobileSignerList = value;
    }

    /**
     * Obtiene el valor de la propiedad type.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getType() {
        return type;
    }

    /**
     * Define el valor de la propiedad type.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setType(JAXBElement<String> value) {
        this.type = value;
    }

    /**
     * Obtiene el valor de la propiedad terminate.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getTerminate() {
        return terminate;
    }

    /**
     * Define el valor de la propiedad terminate.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setTerminate(JAXBElement<Boolean> value) {
        this.terminate = value;
    }

}
