
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
 *         &lt;element ref="{http://xml.antivirus.msps/Peticion}peticion"/>
 *         &lt;element name="fichero" type="{http://www.bea.com/servers/wls90/wsee/attachment}datahandler"/>
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
    "fichero"
})
@XmlRootElement(name = "scanDoc")
public class ScanDoc {

    @XmlElement(namespace = "http://xml.antivirus.msps/Peticion", required = true)
    protected Peticion peticion;
    @XmlElement(required = true)
    protected Datahandler fichero;

    /**
     * Gets the value of the peticion property.
     * 
     * @return
     *     possible object is
     *     {@link Peticion }
     *     
     */
    public Peticion getPeticion() {
        return peticion;
    }

    /**
     * Sets the value of the peticion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Peticion }
     *     
     */
    public void setPeticion(Peticion value) {
        this.peticion = value;
    }

    /**
     * Gets the value of the fichero property.
     * 
     * @return
     *     possible object is
     *     {@link Datahandler }
     *     
     */
    public Datahandler getFichero() {
        return fichero;
    }

    /**
     * Sets the value of the fichero property.
     * 
     * @param value
     *     allowed object is
     *     {@link Datahandler }
     *     
     */
    public void setFichero(Datahandler value) {
        this.fichero = value;
    }

}
