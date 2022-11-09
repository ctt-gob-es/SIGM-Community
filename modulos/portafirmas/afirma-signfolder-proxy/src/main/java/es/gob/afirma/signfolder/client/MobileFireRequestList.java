
package es.gob.afirma.signfolder.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileFireRequestList complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileFireRequestList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mobileFireRequest" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileFireRequest" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileFireRequestList", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "mobileFireRequest"
})
public class MobileFireRequestList {

    protected List<MobileFireRequest> mobileFireRequest;

    /**
     * Gets the value of the mobileFireRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mobileFireRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMobileFireRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MobileFireRequest }
     * 
     * 
     */
    public List<MobileFireRequest> getMobileFireRequest() {
        if (mobileFireRequest == null) {
            mobileFireRequest = new ArrayList<MobileFireRequest>();
        }
        return this.mobileFireRequest;
    }

}
