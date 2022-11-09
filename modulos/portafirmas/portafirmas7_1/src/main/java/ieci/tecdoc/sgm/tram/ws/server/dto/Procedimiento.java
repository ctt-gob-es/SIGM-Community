
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1OrganoProductor;


/**
 * <p>Clase Java para Procedimiento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Procedimiento">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="documentosBasicos" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="informacionBasica" type="{}InfoBProcedimiento"/>
 *         &lt;element name="normativa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="objeto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organosProductores" type="{}ArrayOf_tns1_OrganoProductor"/>
 *         &lt;element name="tramites" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Procedimiento", propOrder = {
    "documentosBasicos",
    "informacionBasica",
    "normativa",
    "objeto",
    "organosProductores",
    "tramites"
})
public class Procedimiento
    extends RetornoServicio
{

    @XmlElement(required = true, nillable = true)
    protected String documentosBasicos;
    @XmlElement(required = true, nillable = true)
    protected InfoBProcedimiento informacionBasica;
    @XmlElement(required = true, nillable = true)
    protected String normativa;
    @XmlElement(required = true, nillable = true)
    protected String objeto;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1OrganoProductor organosProductores;
    @XmlElement(required = true, nillable = true)
    protected String tramites;

    /**
     * Obtiene el valor de la propiedad documentosBasicos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentosBasicos() {
        return documentosBasicos;
    }

    /**
     * Define el valor de la propiedad documentosBasicos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentosBasicos(String value) {
        this.documentosBasicos = value;
    }

    /**
     * Obtiene el valor de la propiedad informacionBasica.
     * 
     * @return
     *     possible object is
     *     {@link InfoBProcedimiento }
     *     
     */
    public InfoBProcedimiento getInformacionBasica() {
        return informacionBasica;
    }

    /**
     * Define el valor de la propiedad informacionBasica.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoBProcedimiento }
     *     
     */
    public void setInformacionBasica(InfoBProcedimiento value) {
        this.informacionBasica = value;
    }

    /**
     * Obtiene el valor de la propiedad normativa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormativa() {
        return normativa;
    }

    /**
     * Define el valor de la propiedad normativa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormativa(String value) {
        this.normativa = value;
    }

    /**
     * Obtiene el valor de la propiedad objeto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjeto() {
        return objeto;
    }

    /**
     * Define el valor de la propiedad objeto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjeto(String value) {
        this.objeto = value;
    }

    /**
     * Obtiene el valor de la propiedad organosProductores.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1OrganoProductor }
     *     
     */
    public ArrayOfTns1OrganoProductor getOrganosProductores() {
        return organosProductores;
    }

    /**
     * Define el valor de la propiedad organosProductores.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1OrganoProductor }
     *     
     */
    public void setOrganosProductores(ArrayOfTns1OrganoProductor value) {
        this.organosProductores = value;
    }

    /**
     * Obtiene el valor de la propiedad tramites.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTramites() {
        return tramites;
    }

    /**
     * Define el valor de la propiedad tramites.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTramites(String value) {
        this.tramites = value;
    }

}
