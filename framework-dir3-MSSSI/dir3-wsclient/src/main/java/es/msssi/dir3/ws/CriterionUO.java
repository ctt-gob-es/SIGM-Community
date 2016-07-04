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
 * Java class for criterionUO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="criterionUO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://ws.dir3.msssi.es/}CriterionUOEnum" minOccurs="0"/>
 *         &lt;element name="operator" type="{http://ws.dir3.msssi.es/}CriterionOperatorEnum" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criterionUO", propOrder = { "name", "operator", "value" })
public class CriterionUO {

    protected CriterionUOEnum name;
    protected CriterionOperatorEnum operator;
    protected String value;

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link CriterionUOEnum }
     * 
     */
    public CriterionUOEnum getName() {
	return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link CriterionUOEnum }
     * 
     */
    public void setName(
	CriterionUOEnum value) {
	this.name = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return possible object is {@link CriterionOperatorEnum }
     * 
     */
    public CriterionOperatorEnum getOperator() {
	return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *            allowed object is {@link CriterionOperatorEnum }
     * 
     */
    public void setOperator(
	CriterionOperatorEnum value) {
	this.operator = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getValue() {
	return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValue(
	String value) {
	this.value = value;
    }

}
