
package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="initPage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signFormats" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileStringList"/>
 *         &lt;element name="filters" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileRequestFilterList"/>
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
    "certificate",
    "state",
    "initPage",
    "pageSize",
    "signFormats",
    "filters"
})
@XmlRootElement(name = "queryRequestList")
public class QueryRequestList {

    @XmlElement(required = true)
    protected byte[] certificate;
    @XmlElement(required = true)
    protected String state;
    @XmlElement(required = true)
    protected String initPage;
    @XmlElement(required = true)
    protected String pageSize;
    @XmlElement(required = true)
    protected MobileStringList signFormats;
    @XmlElement(required = true)
    protected MobileRequestFilterList filters;

    /**
     * Obtiene el valor de la propiedad certificate.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCertificate() {
        return certificate;
    }

    /**
     * Define el valor de la propiedad certificate.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCertificate(byte[] value) {
        this.certificate = value;
    }

    /**
     * Obtiene el valor de la propiedad state.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Define el valor de la propiedad state.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Obtiene el valor de la propiedad initPage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitPage() {
        return initPage;
    }

    /**
     * Define el valor de la propiedad initPage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitPage(String value) {
        this.initPage = value;
    }

    /**
     * Obtiene el valor de la propiedad pageSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPageSize() {
        return pageSize;
    }

    /**
     * Define el valor de la propiedad pageSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPageSize(String value) {
        this.pageSize = value;
    }

    /**
     * Obtiene el valor de la propiedad signFormats.
     * 
     * @return
     *     possible object is
     *     {@link MobileStringList }
     *     
     */
    public MobileStringList getSignFormats() {
        return signFormats;
    }

    /**
     * Define el valor de la propiedad signFormats.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileStringList }
     *     
     */
    public void setSignFormats(MobileStringList value) {
        this.signFormats = value;
    }

    /**
     * Obtiene el valor de la propiedad filters.
     * 
     * @return
     *     possible object is
     *     {@link MobileRequestFilterList }
     *     
     */
    public MobileRequestFilterList getFilters() {
        return filters;
    }

    /**
     * Define el valor de la propiedad filters.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileRequestFilterList }
     *     
     */
    public void setFilters(MobileRequestFilterList value) {
        this.filters = value;
    }

}
