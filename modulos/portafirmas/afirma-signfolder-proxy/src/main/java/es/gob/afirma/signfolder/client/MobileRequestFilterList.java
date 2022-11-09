
package es.gob.afirma.signfolder.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileRequestFilterList complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileRequestFilterList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requestFilter" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileRequestFilter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileRequestFilterList", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "requestFilter"
})
public class MobileRequestFilterList {

    protected List<MobileRequestFilter> requestFilter;

    /**
     * Gets the value of the requestFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requestFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequestFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MobileRequestFilter }
     * 
     * 
     */
    public List<MobileRequestFilter> getRequestFilter() {
        if (requestFilter == null) {
            requestFilter = new ArrayList<MobileRequestFilter>();
        }
        return this.requestFilter;
    }

}
