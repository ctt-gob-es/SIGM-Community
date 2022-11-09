
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1DocElectronico;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1DocFisico;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1Emplazamiento;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1Interesado;


/**
 * <p>Clase Java para Expediente complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Expediente">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="asunto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="documentosElectronicos" type="{}ArrayOf_tns1_DocElectronico"/>
 *         &lt;element name="documentosFisicos" type="{}ArrayOf_tns1_DocFisico"/>
 *         &lt;element name="emplazamientos" type="{}ArrayOf_tns1_Emplazamiento"/>
 *         &lt;element name="fechaFinalizacion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fechaInicio" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="idOrgProductor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="informacionBasica" type="{}InfoBExpediente"/>
 *         &lt;element name="interesados" type="{}ArrayOf_tns1_Interesado"/>
 *         &lt;element name="nombreOrgProductor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Expediente", propOrder = {
    "asunto",
    "documentosElectronicos",
    "documentosFisicos",
    "emplazamientos",
    "fechaFinalizacion",
    "fechaInicio",
    "idOrgProductor",
    "informacionBasica",
    "interesados",
    "nombreOrgProductor"
})
public class Expediente
    extends RetornoServicio
{

    @XmlElement(required = true, nillable = true)
    protected String asunto;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1DocElectronico documentosElectronicos;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1DocFisico documentosFisicos;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1Emplazamiento emplazamientos;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaFinalizacion;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaInicio;
    @XmlElement(required = true, nillable = true)
    protected String idOrgProductor;
    @XmlElement(required = true, nillable = true)
    protected InfoBExpediente informacionBasica;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1Interesado interesados;
    @XmlElement(required = true, nillable = true)
    protected String nombreOrgProductor;

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
     * Obtiene el valor de la propiedad documentosElectronicos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1DocElectronico }
     *     
     */
    public ArrayOfTns1DocElectronico getDocumentosElectronicos() {
        return documentosElectronicos;
    }

    /**
     * Define el valor de la propiedad documentosElectronicos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1DocElectronico }
     *     
     */
    public void setDocumentosElectronicos(ArrayOfTns1DocElectronico value) {
        this.documentosElectronicos = value;
    }

    /**
     * Obtiene el valor de la propiedad documentosFisicos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1DocFisico }
     *     
     */
    public ArrayOfTns1DocFisico getDocumentosFisicos() {
        return documentosFisicos;
    }

    /**
     * Define el valor de la propiedad documentosFisicos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1DocFisico }
     *     
     */
    public void setDocumentosFisicos(ArrayOfTns1DocFisico value) {
        this.documentosFisicos = value;
    }

    /**
     * Obtiene el valor de la propiedad emplazamientos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1Emplazamiento }
     *     
     */
    public ArrayOfTns1Emplazamiento getEmplazamientos() {
        return emplazamientos;
    }

    /**
     * Define el valor de la propiedad emplazamientos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1Emplazamiento }
     *     
     */
    public void setEmplazamientos(ArrayOfTns1Emplazamiento value) {
        this.emplazamientos = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaFinalizacion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    /**
     * Define el valor de la propiedad fechaFinalizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaFinalizacion(XMLGregorianCalendar value) {
        this.fechaFinalizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaInicio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Define el valor de la propiedad fechaInicio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaInicio(XMLGregorianCalendar value) {
        this.fechaInicio = value;
    }

    /**
     * Obtiene el valor de la propiedad idOrgProductor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrgProductor() {
        return idOrgProductor;
    }

    /**
     * Define el valor de la propiedad idOrgProductor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrgProductor(String value) {
        this.idOrgProductor = value;
    }

    /**
     * Obtiene el valor de la propiedad informacionBasica.
     * 
     * @return
     *     possible object is
     *     {@link InfoBExpediente }
     *     
     */
    public InfoBExpediente getInformacionBasica() {
        return informacionBasica;
    }

    /**
     * Define el valor de la propiedad informacionBasica.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoBExpediente }
     *     
     */
    public void setInformacionBasica(InfoBExpediente value) {
        this.informacionBasica = value;
    }

    /**
     * Obtiene el valor de la propiedad interesados.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1Interesado }
     *     
     */
    public ArrayOfTns1Interesado getInteresados() {
        return interesados;
    }

    /**
     * Define el valor de la propiedad interesados.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1Interesado }
     *     
     */
    public void setInteresados(ArrayOfTns1Interesado value) {
        this.interesados = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreOrgProductor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreOrgProductor() {
        return nombreOrgProductor;
    }

    /**
     * Define el valor de la propiedad nombreOrgProductor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreOrgProductor(String value) {
        this.nombreOrgProductor = value;
    }

}
