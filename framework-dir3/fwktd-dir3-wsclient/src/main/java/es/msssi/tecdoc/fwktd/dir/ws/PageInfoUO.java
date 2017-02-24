
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pageInfoUO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pageInfoUO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maxNumItems" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="objectsPerPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pageNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pageInfoUO", propOrder = {
    "maxNumItems",
    "objectsPerPage",
    "pageNumber"
})
public class PageInfoUO {

    protected int maxNumItems;
    protected int objectsPerPage;
    protected int pageNumber;

    /**
     * Gets the value of the maxNumItems property.
     * 
     */
    public int getMaxNumItems() {
        return maxNumItems;
    }

    /**
     * Sets the value of the maxNumItems property.
     * 
     */
    public void setMaxNumItems(int value) {
        this.maxNumItems = value;
    }

    /**
     * Gets the value of the objectsPerPage property.
     * 
     */
    public int getObjectsPerPage() {
        return objectsPerPage;
    }

    /**
     * Sets the value of the objectsPerPage property.
     * 
     */
    public void setObjectsPerPage(int value) {
        this.objectsPerPage = value;
    }

    /**
     * Gets the value of the pageNumber property.
     * 
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the value of the pageNumber property.
     * 
     */
    public void setPageNumber(int value) {
        this.pageNumber = value;
    }

}
