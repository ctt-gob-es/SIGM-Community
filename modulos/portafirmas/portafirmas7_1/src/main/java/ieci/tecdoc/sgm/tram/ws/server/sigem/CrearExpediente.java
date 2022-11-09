
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.DatosComunesExpediente;
import ieci.tecdoc.sgm.tram.ws.server.dto.DocumentoExpediente;


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
 *         &lt;element name="idEntidad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="commonData" type="{}DatosComunesExpediente"/>
 *         &lt;element name="specificDataXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="documents" type="{}DocumentoExpediente" maxOccurs="unbounded"/>
 *         &lt;element name="initSystem" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "idEntidad",
    "commonData",
    "specificDataXML",
    "documents",
    "initSystem"
})
@XmlRootElement(name = "crearExpediente")
public class CrearExpediente {

    @XmlElement(required = true)
    protected String idEntidad;
    @XmlElement(required = true)
    protected DatosComunesExpediente commonData;
    @XmlElement(required = true)
    protected String specificDataXML;
    @XmlElement(required = true)
    protected List<DocumentoExpediente> documents;
    @XmlElement(required = true)
    protected String initSystem;

    /**
     * Obtiene el valor de la propiedad idEntidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEntidad() {
        return idEntidad;
    }

    /**
     * Define el valor de la propiedad idEntidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEntidad(String value) {
        this.idEntidad = value;
    }

    /**
     * Obtiene el valor de la propiedad commonData.
     * 
     * @return
     *     possible object is
     *     {@link DatosComunesExpediente }
     *     
     */
    public DatosComunesExpediente getCommonData() {
        return commonData;
    }

    /**
     * Define el valor de la propiedad commonData.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosComunesExpediente }
     *     
     */
    public void setCommonData(DatosComunesExpediente value) {
        this.commonData = value;
    }

    /**
     * Obtiene el valor de la propiedad specificDataXML.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecificDataXML() {
        return specificDataXML;
    }

    /**
     * Define el valor de la propiedad specificDataXML.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecificDataXML(String value) {
        this.specificDataXML = value;
    }

    /**
     * Gets the value of the documents property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documents property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocuments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentoExpediente }
     * 
     * 
     */
    public List<DocumentoExpediente> getDocuments() {
        if (documents == null) {
            documents = new ArrayList<DocumentoExpediente>();
        }
        return this.documents;
    }

    /**
     * Obtiene el valor de la propiedad initSystem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitSystem() {
        return initSystem;
    }

    /**
     * Define el valor de la propiedad initSystem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitSystem(String value) {
        this.initSystem = value;
    }

}
