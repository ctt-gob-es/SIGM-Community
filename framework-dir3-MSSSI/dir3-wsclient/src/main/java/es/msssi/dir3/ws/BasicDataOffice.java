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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for basicDataOffice complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="basicDataOffice">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir3.msssi.es/}entity">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="officeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="officeTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responsibleUnitId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responsibleUnitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "basicDataOffice", propOrder = { "name", "officeType", "officeTypeName",
    "responsibleUnitId", "responsibleUnitName", "status" })
@XmlSeeAlso({ Office.class })
public class BasicDataOffice extends Entity {

    protected String name;
    protected String officeType;
    protected String officeTypeName;
    protected String responsibleUnitId;
    protected String responsibleUnitName;
    protected String status;

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setName(
	String value) {
	this.name = value;
    }

    /**
     * Gets the value of the officeType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOfficeType() {
	return officeType;
    }

    /**
     * Sets the value of the officeType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOfficeType(
	String value) {
	this.officeType = value;
    }

    /**
     * Gets the value of the officeTypeName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOfficeTypeName() {
	return officeTypeName;
    }

    /**
     * Sets the value of the officeTypeName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOfficeTypeName(
	String value) {
	this.officeTypeName = value;
    }

    /**
     * Gets the value of the responsibleUnitId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getResponsibleUnitId() {
	return responsibleUnitId;
    }

    /**
     * Sets the value of the responsibleUnitId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setResponsibleUnitId(
	String value) {
	this.responsibleUnitId = value;
    }

    /**
     * Gets the value of the responsibleUnitName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getResponsibleUnitName() {
	return responsibleUnitName;
    }

    /**
     * Sets the value of the responsibleUnitName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setResponsibleUnitName(
	String value) {
	this.responsibleUnitName = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStatus() {
	return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStatus(
	String value) {
	this.status = value;
    }

}
