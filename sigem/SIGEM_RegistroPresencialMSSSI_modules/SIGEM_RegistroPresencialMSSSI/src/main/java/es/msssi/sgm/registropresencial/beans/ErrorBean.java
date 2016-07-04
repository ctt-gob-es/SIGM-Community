/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

/**
 * Bean que guarda los mensajes de error que puedan ocurrir para mostrarlos en la página
 * de error.
 * 
 * @author jortizs
 */
public class ErrorBean
		extends GenericBean {
	private static final long serialVersionUID = 1L;
	/** Código de error. */
	private String errorCode;
	/** Mensaje de error. */
	private String errorMessage;
	
	/**
	 * Obtiene el valor del parámetro errorCode.
	 * 
	 * @return errorCode código de error a obtener.
	 */
	public String getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Guarda el valor del parámetro errorCode.
	 * 
	 * @param errorCode
	 *            Código de error a guardar.
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * Obtiene el valor del parámetro errorMessage.
	 * 
	 * @return errorMessage Mensaje de error a obtener.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * Guarda el valor del parámetro errorMessage.
	 * 
	 * @param errorMessage
	 *            Mensaje de error a guardar.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}