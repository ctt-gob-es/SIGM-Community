/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for contact complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="contact">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir3.msssi.es/}entity">
 *       &lt;sequence>
 *         &lt;element name="contactInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contact",
    propOrder = { "contactInfo", "contactTypeId", "observations", "statusId" })
public class Contact extends Entity {

    protected String contactInfo;
    protected String contactTypeId;
    protected String observations;
    protected String statusId;

    /**
     * Gets the value of the contactInfo property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getContactInfo() {
	return contactInfo;
    }

    /**
     * Sets the value of the contactInfo property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setContactInfo(
	String value) {
	this.contactInfo = value;
    }

    /**
     * Gets the value of the contactTypeId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getContactTypeId() {
	return contactTypeId;
    }

    /**
     * Sets the value of the contactTypeId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setContactTypeId(
	String value) {
	this.contactTypeId = value;
    }

    /**
     * Gets the value of the observations property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getObservations() {
	return observations;
    }

    /**
     * Sets the value of the observations property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setObservations(
	String value) {
	this.observations = value;
    }

    /**
     * Gets the value of the statusId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStatusId() {
	return statusId;
    }

    /**
     * Sets the value of the statusId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStatusId(
	String value) {
	this.statusId = value;
    }

}
