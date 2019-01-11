
package es.msssi.sgm.registropresencial.connector.antivirus.beans;

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
 *         &lt;element name="peticion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ficheroBase64" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "peticion",
    "ficheroBase64"
})
@XmlRootElement(name = "scanBase64")
public class ScanBase64 {

    @XmlElement(required = true)
    protected String peticion;
    @XmlElement(required = true)
    protected String ficheroBase64;

    /**
     * Gets the value of the peticion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeticion() {
        return peticion;
    }

    /**
     * Sets the value of the peticion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeticion(String value) {
        this.peticion = value;
    }

    /**
     * Gets the value of the ficheroBase64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFicheroBase64() {
        return ficheroBase64;
    }

    /**
     * Sets the value of the ficheroBase64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFicheroBase64(String value) {
        this.ficheroBase64 = value;
    }

}
