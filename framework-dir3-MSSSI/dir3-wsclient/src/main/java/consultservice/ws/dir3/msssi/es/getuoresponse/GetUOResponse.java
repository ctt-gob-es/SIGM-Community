/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package consultservice.ws.dir3.msssi.es.getuoresponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import es.msssi.dir3.ws.CommonResponse;
import es.msssi.dir3.ws.Unit;

/**
 * <p>
 * Java class for getUOResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getUOResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir3.msssi.es/}commonResponse">
 *       &lt;sequence>
 *         &lt;element name="unit" type="{http://ws.dir3.msssi.es/}unit" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUOResponse", propOrder = { "unit" })
public class GetUOResponse extends CommonResponse {

    protected Unit unit;

    /**
     * Gets the value of the unit property.
     * 
     * @return possible object is {@link Unit }
     * 
     */
    public Unit getUnit() {
	return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *            allowed object is {@link Unit }
     * 
     */
    public void setUnit(
	Unit value) {
	this.unit = value;
    }

}
