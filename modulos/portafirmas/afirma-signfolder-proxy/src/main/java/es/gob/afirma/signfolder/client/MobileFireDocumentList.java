
package es.gob.afirma.signfolder.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileFireDocumentList complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileFireDocumentList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mobileFireDocumentList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileFireDocument" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileFireDocumentList", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "mobileFireDocumentList"
})
public class MobileFireDocumentList {

    protected List<MobileFireDocument> mobileFireDocumentList;

    /**
     * Gets the value of the mobileFireDocumentList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mobileFireDocumentList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMobileFireDocumentList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MobileFireDocument }
     * 
     * 
     */
    public List<MobileFireDocument> getMobileFireDocumentList() {
        if (mobileFireDocumentList == null) {
            mobileFireDocumentList = new ArrayList<MobileFireDocument>();
        }
        return this.mobileFireDocumentList;
    }

}
