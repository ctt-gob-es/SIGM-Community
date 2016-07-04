/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.map.directorio.manager.impl.wsexport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for RespuestaWS complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fichero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaWS", propOrder = { "codigo", "descripcion", "fichero" })
public class RespuestaWS {

    @XmlElement(required = true)
    protected String codigo;
    @XmlElement(required = true)
    protected String descripcion;
    @XmlElement(required = true)
    protected String fichero;

    /**
     * Gets the value of the codigo property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCodigo() {
	return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCodigo(
	String value) {
	this.codigo = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDescripcion() {
	return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDescripcion(
	String value) {
	this.descripcion = value;
    }

    /**
     * Gets the value of the fichero property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFichero() {
	return fichero;
    }

    /**
     * Sets the value of the fichero property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFichero(
	String value) {
	this.fichero = value;
    }

}
