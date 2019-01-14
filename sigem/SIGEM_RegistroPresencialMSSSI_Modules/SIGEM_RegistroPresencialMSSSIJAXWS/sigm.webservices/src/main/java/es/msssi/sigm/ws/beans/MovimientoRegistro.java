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
@XmlType(name = "movimientoRegistro", propOrder = {
	    "usuario",
	    "fecha",
	    "campo",
	    "valorAntiguo",
	    "valorNuevo"
	})

public class MovimientoRegistro {

    @XmlElement(required = true)
    protected String usuario;
    @XmlElement(required = true)
    protected String fecha;
    @XmlElement(required = true)
    protected String campo;
    @XmlElement(required = true)
    protected String valorAntiguo;
    @XmlElement(required = true)
    protected String valorNuevo;

    public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
    public String getUsuario() {
		return usuario;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getValorAntiguo() {
		return valorAntiguo;
	}
	public void setValorAntiguo(String valorAntiguo) {
		this.valorAntiguo = valorAntiguo;
	}
	public String getValorNuevo() {
		return valorNuevo;
	}
	public void setValorNuevo(String valorNuevo) {
		this.valorNuevo = valorNuevo;
	}
    
    
    
}
