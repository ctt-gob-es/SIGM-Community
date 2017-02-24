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
 * Java class for pageInfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="pageInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maxNumItems" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="objectsPerPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pageNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pageInfo", propOrder = { "maxNumItems", "objectsPerPage", "pageNumber" })
public class PageInfo {

    protected int maxNumItems;
    protected int objectsPerPage;
    protected int pageNumber;

    /**
     * @return Gets the value of the maxNumItems property.
     * 
     */
    public int getMaxNumItems() {
	return maxNumItems;
    }

    /**
     * @param value
     * 	Sets the value of the maxNumItems property.
     * 
     */
    public void setMaxNumItems(
	int value) {
	this.maxNumItems = value;
    }

    /**
     * @return Gets the value of the objectsPerPage property.
     * 
     */
    public int getObjectsPerPage() {
	return objectsPerPage;
    }

    /**
     *  @param value
     *  	Sets the value of the objectsPerPage property.
     * 
     */
    public void setObjectsPerPage(
	int value) {
	this.objectsPerPage = value;
    }

    /**
     * @return Gets the value of the pageNumber property.
     * 
     */
    public int getPageNumber() {
	return pageNumber;
    }

    /**
     * @param value
     * 	Sets the value of the pageNumber property.
     * 
     */
    public void setPageNumber(
	int value) {
	this.pageNumber = value;
    }

}
