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
@XmlType(name = "intercambioRegistralSalida", propOrder = { 
		"fechaIntercambio", 
		"oficina",
		"tipoOrigen", 
		"entidadDestino", 
		"unidadDestino", 
		"estado", 
		"fechaEstado",
		"trazasIREntrada"
		})
public class IntercambioRegistralSalida {
	@XmlElement(required = true)
	protected String fechaIntercambio;
	@XmlElement(required = true)
	protected String oficina;
	@XmlElement(required = true)
	protected String tipoOrigen;
	@XmlElement(required = true)
	protected String entidadDestino;
	@XmlElement(required = true)
	protected String unidadDestino;
	@XmlElement(required = true)
	protected String estado;
	@XmlElement(required = true)
	protected String fechaEstado;
	@XmlElement(required = true)
	protected String trazasIREntrada;
	
	
	public String getFechaIntercambio() {
		return fechaIntercambio;
	}
	public void setFechaIntercambio(String fechaIntercambio) {
		this.fechaIntercambio = fechaIntercambio;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getTipoOrigen() {
		return tipoOrigen;
	}
	public void setTipoOrigen(String tipoOrigen) {
		this.tipoOrigen = tipoOrigen;
	}
	public String getEntidadDestino() {
		return entidadDestino;
	}
	public void setEntidadDestino(String entidadDestino) {
		this.entidadDestino = entidadDestino;
	}
	public String getUnidadDestino() {
		return unidadDestino;
	}
	public void setUnidadDestino(String unidadDestino) {
		this.unidadDestino = unidadDestino;
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

	public void setTrazasIREntrada(String trazasIREntrada) {
		this.trazasIREntrada = trazasIREntrada;
	}
	public String getTrazasIREntrada() {
		return trazasIREntrada;
	}
	
	
}
