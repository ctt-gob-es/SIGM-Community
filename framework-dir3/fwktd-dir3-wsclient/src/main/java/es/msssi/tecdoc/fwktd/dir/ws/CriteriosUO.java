
package es.msssi.tecdoc.fwktd.dir.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for criteriosUO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="criteriosUO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="criterio" type="{http://ws.dir.fwktd.tecdoc.msssi.es/}criterioUO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="orderBy" type="{http://ws.dir.fwktd.tecdoc.msssi.es/}CriterioUnidadOrganicaUOEnum" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pageInfo" type="{http://ws.dir.fwktd.tecdoc.msssi.es/}pageInfoUO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criteriosUO", propOrder = {
    "criterio",
    "orderBy",
    "pageInfo"
})
public class CriteriosUO {

    @XmlElement(nillable = true)
    protected List<CriterioUO> criterio;
    @XmlElement(nillable = true)
    protected List<CriterioUnidadOrganicaUOEnum> orderBy;
    protected PageInfoUO pageInfo;

    /**
     * Gets the value of the criterio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the criterio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCriterio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CriterioUO }
     * 
     * 
     */
    public List<CriterioUO> getCriterio() {
        if (criterio == null) {
            criterio = new ArrayList<CriterioUO>();
        }
        return this.criterio;
    }

    /**
     * Gets the value of the orderBy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orderBy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrderBy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CriterioUnidadOrganicaUOEnum }
     * 
     * 
     */
    public List<CriterioUnidadOrganicaUOEnum> getOrderBy() {
        if (orderBy == null) {
            orderBy = new ArrayList<CriterioUnidadOrganicaUOEnum>();
        }
        return this.orderBy;
    }

    /**
     * Gets the value of the pageInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PageInfoUO }
     *     
     */
    public PageInfoUO getPageInfo() {
        return pageInfo;
    }

    /**
     * Sets the value of the pageInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageInfoUO }
     *     
     */
    public void setPageInfo(PageInfoUO value) {
        this.pageInfo = value;
    }

}
