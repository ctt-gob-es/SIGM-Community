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

/**
 * Respuesta de webservice de sigm.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaSigm", propOrder = {
	    "estadoRespuesta",
	    "errorResponse"
	})
public class SigmResponse {
	
	    @XmlElement(required = true)
	    protected String estadoRespuesta;
	    @XmlElement(required = false)
	    protected ErrorResponse errorResponse;
	    
//		private static Logger log = Logger.getLogger(SigmResponse.class.getName());
		
	    public String getEstadoRespuesta() {
			return estadoRespuesta;
		}
	    public void setEstadoRespuesta(String estadoRespuesta) {
			this.estadoRespuesta = estadoRespuesta;
		}

	    public void setErrorResponse(ErrorResponse errorResponse) {
			this.errorResponse = errorResponse;
		}
	    public ErrorResponse getErrorResponse() {
			return errorResponse;
		}
	    
		public static <T extends SigmResponse> T parse(SigmResponse sigmResponse, Class<T> cls){
			T result = null;
			if (cls.isInstance(sigmResponse)) {
                result = cls.cast(sigmResponse);
            }else{
            	try {
					result = cls.newInstance();
					result.setErrorResponse(sigmResponse.getErrorResponse());
				} catch (InstantiationException e) {
//					log.error("Error casting",e);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
//					log.error("Error casting", e);
					e.printStackTrace();
				}
            }
			return result;
		}
		


}
		    

		    
		 