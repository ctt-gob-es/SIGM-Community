/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/


package es.msssi.sigm.ws.beans.jaxb.solicitud;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for elementoSolicitud complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="elementoSolicitud">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datos" type="{http://www.msssi.es/Regtel/2015/1}elementoDatos"/>
 *         &lt;element name="ficheros" type="{http://www.msssi.es/Regtel/2015/1}elementoFicheros"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "elementoSolicitud", propOrder = {
    "datos",
    "ficheros"
})
public class ElementoSolicitud {

    @XmlElement(required = true)
    protected ElementoDatos datos;
    @XmlElement(required = false)
    protected ElementoFicheros ficheros;

    /**
     * Gets the value of the datos property.
     * 
     * @return
     *     possible object is
     *     {@link ElementoDatos }
     *     
     */
    public ElementoDatos getDatos() {
        return datos;
    }

    /**
     * Sets the value of the datos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementoDatos }
     *     
     */
    public void setDatos(ElementoDatos value) {
        this.datos = value;
    }

    /**
     * Gets the value of the ficheros property.
     * 
     * @return
     *     possible object is
     *     {@link ElementoFicheros }
     *     
     */
    public ElementoFicheros getFicheros() {
        return ficheros;
    }

    /**
     * Sets the value of the ficheros property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementoFicheros }
     *     
     */
    public void setFicheros(ElementoFicheros value) {
        this.ficheros = value;
    }

}
