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
@XmlType(name = "distribucionRegistro", propOrder = {
	    "fechaDistribucion",
	    "origenDistribucion",
	    "destinoDistribucion",
	    "estado",
	    "fechaEstado",
	    "comentario",
	    "estadosDistribucion"
	})

public class DistribucionRegistro {

    @XmlElement(required = true)
    protected String fechaDistribucion;
    @XmlElement(required = true)
    protected String origenDistribucion;
    @XmlElement(required = true)
    protected String destinoDistribucion;
    @XmlElement(required = true)
    protected String estado;
    @XmlElement(required = true)
    protected String fechaEstado;
    @XmlElement(required = true)
    protected String comentario;
    @XmlElement(required = true)
    protected String estadosDistribucion;
    
	public String getFechaDistribucion() {
		return fechaDistribucion;
	}
	public void setFechaDistribucion(String fechaDistribucion) {
		this.fechaDistribucion = fechaDistribucion;
	}
	public String getOrigenDistribucion() {
		return origenDistribucion;
	}
	public void setOrigenDistribucion(String origenDistribucion) {
		this.origenDistribucion = origenDistribucion;
	}
	public String getDestinoDistribucion() {
		return destinoDistribucion;
	}
	public void setDestinoDistribucion(String destinoDistribucion) {
		this.destinoDistribucion = destinoDistribucion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getEstadosDistribucion() {
		return estadosDistribucion;
	}
	public void setEstadosDistribucion(String estadosDistribucion) {
		this.estadosDistribucion = estadosDistribucion;
	}
 
    
}
