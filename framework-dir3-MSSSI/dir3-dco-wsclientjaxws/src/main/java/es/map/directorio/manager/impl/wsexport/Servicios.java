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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for servicios complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="servicios">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="incluidos" type="{http://impl.manager.directorio.map.es/wsExport}excluidos" minOccurs="0"/>
 *         &lt;element name="excluidos" type="{http://impl.manager.directorio.map.es/wsExport}excluidos" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "servicios", propOrder = { "incluidos", "excluidos" })
public class Servicios {

    protected Excluidos incluidos;
    protected Excluidos excluidos;

    /**
     * Gets the value of the incluidos property.
     * 
     * @return possible object is {@link Excluidos }
     * 
     */
    public Excluidos getIncluidos() {
	return incluidos;
    }

    /**
     * Sets the value of the incluidos property.
     * 
     * @param value
     *            allowed object is {@link Excluidos }
     * 
     */
    public void setIncluidos(
	Excluidos value) {
	this.incluidos = value;
    }

    /**
     * Gets the value of the excluidos property.
     * 
     * @return possible object is {@link Excluidos }
     * 
     */
    public Excluidos getExcluidos() {
	return excluidos;
    }

    /**
     * Sets the value of the excluidos property.
     * 
     * @param value
     *            allowed object is {@link Excluidos }
     * 
     */
    public void setExcluidos(
	Excluidos value) {
	this.excluidos = value;
    }

}
