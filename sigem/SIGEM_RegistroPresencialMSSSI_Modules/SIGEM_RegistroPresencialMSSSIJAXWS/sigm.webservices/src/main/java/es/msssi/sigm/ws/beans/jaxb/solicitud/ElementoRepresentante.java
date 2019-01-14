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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for elementoRepresentante complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="elementoRepresentante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="representanteJuridico" type="{http://www.msssi.es/Regtel/2015/1}representanteJuridico"/>
 *           &lt;element name="representanteFisico" type="{http://www.msssi.es/Regtel/2015/1}representanteFisico"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "elementoRepresentante", namespace = "http://www.msssi.es/Regtel/2015/1", propOrder = {
    "representanteJuridico",
    "representanteFisico"
})
public class ElementoRepresentante {

    protected RepresentanteJuridico representanteJuridico;
    protected RepresentanteFisico representanteFisico;

    /**
     * Gets the value of the representanteJuridico property.
     * 
     * @return
     *     possible object is
     *     {@link RepresentanteJuridico }
     *     
     */
    public RepresentanteJuridico getRepresentanteJuridico() {
        return representanteJuridico;
    }

    /**
     * Sets the value of the representanteJuridico property.
     * 
     * @param value
     *     allowed object is
     *     {@link RepresentanteJuridico }
     *     
     */
    public void setRepresentanteJuridico(RepresentanteJuridico value) {
        this.representanteJuridico = value;
    }

    /**
     * Gets the value of the representanteFisico property.
     * 
     * @return
     *     possible object is
     *     {@link RepresentanteFisico }
     *     
     */
    public RepresentanteFisico getRepresentanteFisico() {
        return representanteFisico;
    }

    /**
     * Sets the value of the representanteFisico property.
     * 
     * @param value
     *     allowed object is
     *     {@link RepresentanteFisico }
     *     
     */
    public void setRepresentanteFisico(RepresentanteFisico value) {
        this.representanteFisico = value;
    }

}
