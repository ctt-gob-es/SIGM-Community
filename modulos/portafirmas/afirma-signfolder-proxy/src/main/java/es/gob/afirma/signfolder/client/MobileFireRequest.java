
package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileFireRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileFireRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documentos" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileFireDocumentList" minOccurs="0"/>
 *         &lt;element name="errorPeticion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileFireRequest", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "id",
    "asunto",
    "documentos",
    "errorPeticion"
})
public class MobileFireRequest {

    protected String id;
    protected String asunto;
    protected MobileFireDocumentList documentos;
    protected String errorPeticion;

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad asunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Define el valor de la propiedad asunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsunto(String value) {
        this.asunto = value;
    }

    /**
     * Obtiene el valor de la propiedad documentos.
     * 
     * @return
     *     possible object is
     *     {@link MobileFireDocumentList }
     *     
     */
    public MobileFireDocumentList getDocumentos() {
        return documentos;
    }

    /**
     * Define el valor de la propiedad documentos.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileFireDocumentList }
     *     
     */
    public void setDocumentos(MobileFireDocumentList value) {
        this.documentos = value;
    }

    /**
     * Obtiene el valor de la propiedad errorPeticion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorPeticion() {
        return errorPeticion;
    }

    /**
     * Define el valor de la propiedad errorPeticion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorPeticion(String value) {
        this.errorPeticion = value;
    }

}
