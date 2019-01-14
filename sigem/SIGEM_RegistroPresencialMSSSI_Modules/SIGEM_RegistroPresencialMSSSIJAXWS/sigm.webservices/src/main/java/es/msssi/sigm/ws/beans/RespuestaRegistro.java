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
@XmlType(name = "respuestaRegistro", propOrder = {
	    "numeroRegistro",
	    "fechaRegistro",
	    "estadoRegistro",
	    "acuse"
	})
public class RespuestaRegistro extends SigmResponse{
	    @XmlElement(required = false)
	    protected String numeroRegistro;
	    @XmlElement(required = false)
	    protected String fechaRegistro;
	    @XmlElement(required = false)
	    protected String estadoRegistro;
	    @XmlElement(required = false)
	    protected Acuse acuse;
	    
	    public String getNumeroRegistro() {
			return numeroRegistro;
		}
		public void setNumeroRegistro(String numeroRegistro) {
			this.numeroRegistro = numeroRegistro;
		}
		public String getFechaRegistro() {
			return fechaRegistro;
		}
		public void setFechaRegistro(String fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
		}
		public String getEstadoRegistro() {
			return estadoRegistro;
		}
		public void setEstadoRegistro(String estadoRegistro) {
			this.estadoRegistro = estadoRegistro;
		}
		public Acuse getAcuse() {
			return acuse;
		}
		public void setAcuse(Acuse acuse) {
			this.acuse = acuse;
		}
		
//		public static RespuestaRegistro parse(SigmResponse sigmResponse){
//			RespuestaRegistro result = new RespuestaRegistro();
//			if(sigmResponse instanceof RespuestaRegistro){
//				result = (RespuestaRegistro)sigmResponse;
//			}else{
//				result.setErrorResponse(sigmResponse.getErrorResponse());
//			}
//			return result;
//		}
	    
}
