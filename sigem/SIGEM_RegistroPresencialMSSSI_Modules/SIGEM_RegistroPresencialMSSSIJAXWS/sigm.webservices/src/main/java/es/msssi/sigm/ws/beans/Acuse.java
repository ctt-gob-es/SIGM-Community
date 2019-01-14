/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.ws.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acuse", propOrder = {
	    "contenido",
	    "nombre",
	    "csv"
	})
public class Acuse {
 
    	@XmlElement(required = false)
	    protected byte[] contenido;
	    @XmlElement(required = false)
	    protected String nombre;
	    @XmlElement(required = true)
	    protected String csv;

	    /**
	     * Gets the value of the contenido property.
	     * 
	     * @return
	     *     possible object is
	     *     byte[]
	     */
	    public byte[] getContenido() {
	        return contenido;
	    }

	    /**
	     * Sets the value of the contenido property.
	     * 
	     * @param value
	     *     allowed object is
	     *     byte[]
	     */
	    public void setContenido(byte[] value) {
	        this.contenido = ((byte[]) value);
	    }
	    public void setNombre(String nombre) {
			this.nombre = nombre;
		}
	    public String getNombre() {
			return nombre;
		}
	    
	    public void setCsv(String csv) {
			this.csv = csv;
		}
	    public String getCsv() {
			return csv;
		}
}
