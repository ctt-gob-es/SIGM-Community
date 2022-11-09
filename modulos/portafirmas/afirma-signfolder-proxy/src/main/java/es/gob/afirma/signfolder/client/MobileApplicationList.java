
package es.gob.afirma.signfolder.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileApplicationList complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileApplicationList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="applicationList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileApplication" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileApplicationList", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = { "applicationList", "roles" })
public class MobileApplicationList {

    protected List<MobileApplication> applicationList;
    
    protected List<String> roles;

    /**
     * Gets the value of the applicationList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicationList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplicationList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MobileApplication }
     * 
     * 
     */
    public List<MobileApplication> getApplicationList() {
        if (applicationList == null) {
            applicationList = new ArrayList<MobileApplication>();
        }
        return this.applicationList;
    }

	/**
	 * Gets the value of the roles property.
	 * @return the roles list.
	 */
	public List<String> getRoles() {
		return roles;
	}

}
