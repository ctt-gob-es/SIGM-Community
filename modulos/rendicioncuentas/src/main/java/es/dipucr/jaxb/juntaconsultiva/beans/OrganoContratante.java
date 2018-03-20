//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 02:19:57 PM CET 
//


package es.dipucr.jaxb.juntaconsultiva.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}contrato"/>
 *       &lt;/sequence>
 *       &lt;attribute name="codigoOrganoContratante" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="nombreOrganoContratante" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contrato"
})
@XmlRootElement(name = "organoContratante")
public class OrganoContratante {

    @XmlElement(required = true)
    protected List<Contrato> contrato;
    @XmlAttribute(name = "codigoOrganoContratante", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String codigoOrganoContratante;
    @XmlAttribute(name = "nombreOrganoContratante", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String nombreOrganoContratante;

    /**
     * Gets the value of the contrato property.
     * 
     * @return
     *     possible object is
     *     {@link Contrato }
     *     
     */
    public List<Contrato> getContrato() {
    	if (contrato == null) {
            contrato = new ArrayList<Contrato>();
        }
        return this.contrato;
    }

    /**
     * Gets the value of the codigoOrganoContratante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrganoContratante() {
        return codigoOrganoContratante;
    }

    /**
     * Sets the value of the codigoOrganoContratante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrganoContratante(String value) {
        this.codigoOrganoContratante = value;
    }

    /**
     * Gets the value of the nombreOrganoContratante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreOrganoContratante() {
        return nombreOrganoContratante;
    }

    /**
     * Sets the value of the nombreOrganoContratante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreOrganoContratante(String value) {
        this.nombreOrganoContratante = value;
    }

}