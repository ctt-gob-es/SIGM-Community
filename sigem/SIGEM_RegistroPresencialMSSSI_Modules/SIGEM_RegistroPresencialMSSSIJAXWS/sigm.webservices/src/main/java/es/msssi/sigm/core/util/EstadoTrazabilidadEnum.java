/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util;

public enum EstadoTrazabilidadEnum {
	DESCONOCIDO("DESCONOCIDO"),
	REGISTRO_PENDIENTE("REGISTRO PENDIENTE"),
	ENVIO("ENVIO"),
	REENVIO("REENVIO"),
	RECHAZO("RECHAZO"),
	REGISTRO_RECIBIDO_ENVIO("REGISTRO RECIBIDO-ENVIO"),
	REGISTRO_ENVIADO("REGISTRO ENVIADO"),
	REGISTRO_REENVIADO("REGISTRO REENVIADO"),
	REGISTRO_RECHAZADO("REGISTRO RECHAZADO"),
	REGISTRO_CONFIRMADO("REGISTRO CONFIRMADO"),
	MENSAJE_PENDIENTE("MENSAJE PENDIENTE"),
	MENSAJE_ENVIADO("MENSAJE ENVIADO"),
	MENSAJE_RECIBIDO_ENVIO("MENSAJE RECIBIDO-ENVIO"),
	MENSAJE_PROCESADO("MENSAJE PROCESADO"),
	RECEPCION_CORRECTA("RECEPCIÓN CORRECTA"),
	ERROR("ERROR");
	
	
	private String value;

	private EstadoTrazabilidadEnum(String value) {
		this.value = value;
	}

	public static String getDescripcion(String codigo){

		if (codigo == null) {
			return DESCONOCIDO.value;
		}
		else if (codigo.equals("01")) {
			return REGISTRO_PENDIENTE.value;
		}
		else if (codigo.equals("02")) {
			return ENVIO.value;
		}
		else if (codigo.equals("03")) {
			return REENVIO.value;
		}
		else if (codigo.equals("04")) {
			return RECHAZO.value;
		}
		else if (codigo.equals("05")) {
			return REGISTRO_RECIBIDO_ENVIO.value;
		}
		else if (codigo.equals("06")) {
			return REGISTRO_ENVIADO.value;
		}
		else if (codigo.equals("07")) {
			return REGISTRO_REENVIADO.value;
		}
		else if (codigo.equals("08")) {
			return REGISTRO_RECHAZADO.value;
		}
		else if (codigo.equals("09")) {
			return REGISTRO_CONFIRMADO.value;
		}
		else if (codigo.equals("10")) {
			return REGISTRO_RECHAZADO.value;
		}
		else if (codigo.equals("11")) {
			return MENSAJE_PENDIENTE.value;
		}
		else if (codigo.equals("12")) {
			return MENSAJE_ENVIADO.value;
		}
		else if (codigo.equals("13")) {
			return MENSAJE_RECIBIDO_ENVIO.value;
		}
		else if (codigo.equals("14")) {
			return MENSAJE_PROCESADO.value;
		}
		else if (codigo.equals("15")) {
			return RECEPCION_CORRECTA.value;
		}
		else if (codigo.equals("99")) {
			return ERROR.value;
		}
		else
			return DESCONOCIDO.value;
		 
	}
}