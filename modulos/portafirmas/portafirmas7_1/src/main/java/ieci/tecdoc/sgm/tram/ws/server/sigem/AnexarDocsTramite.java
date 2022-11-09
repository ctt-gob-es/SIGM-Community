
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
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
 *         &lt;element name="numExp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idTramite" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="regNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="regDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="documents" type="{}DocumentoExpediente" maxOccurs="unbounded"/>
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
    "numExp",
    "idTramite",
    "regNum",
    "regDate",
    "documents"
})
@XmlRootElement(name = "anexarDocsTramite")
public class AnexarDocsTramite {

    @XmlElement(required = true)
    protected String idEntidad;
    @XmlElement(required = true)
    protected String numExp;
    protected int idTramite;
    @XmlElement(required = true)
    protected String regNum;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar regDate;
    @XmlElement(required = true)
    protected List<DocumentoExpediente> documents;

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
     * Obtiene el valor de la propiedad numExp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumExp() {
        return numExp;
    }

    /**
     * Define el valor de la propiedad numExp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumExp(String value) {
        this.numExp = value;
    }

    /**
     * Obtiene el valor de la propiedad idTramite.
     * 
     */
    public int getIdTramite() {
        return idTramite;
    }

    /**
     * Define el valor de la propiedad idTramite.
     * 
     */
    public void setIdTramite(int value) {
        this.idTramite = value;
    }

    /**
     * Obtiene el valor de la propiedad regNum.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegNum() {
        return regNum;
    }

    /**
     * Define el valor de la propiedad regNum.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegNum(String value) {
        this.regNum = value;
    }

    /**
     * Obtiene el valor de la propiedad regDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegDate() {
        return regDate;
    }

    /**
     * Define el valor de la propiedad regDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegDate(XMLGregorianCalendar value) {
        this.regDate = value;
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

}
