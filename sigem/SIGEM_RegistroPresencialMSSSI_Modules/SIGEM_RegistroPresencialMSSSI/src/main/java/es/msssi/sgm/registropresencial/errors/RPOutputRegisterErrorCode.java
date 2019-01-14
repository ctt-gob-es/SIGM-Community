/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.errors;

import core.error.ErrorCode;

/**
 * Clase que recoge los códigos de error referentes a operaciones con registros de salida.
 * 
 * @author jortizs
 */
public class RPOutputRegisterErrorCode
		extends ErrorCode {
	/** Constante de código RPOR001: Error . */
	public static final RPOutputRegisterErrorCode ERROR = new RPOutputRegisterErrorCode(
			"RPOR001", "");
	/**
	 * Constante de código RPOR002: Error en la carga de los registros de salida.
	 */
	public static final RPOutputRegisterErrorCode LOAD_OUTPUT_REGISTER_ERROR =
			new RPOutputRegisterErrorCode("RPOR002", "LOAD OUTPUT REGISTER ERROR");
	
	/**
	 * Constante de código RPOR003: Error en la creación de los registros de salida.
	 */
	public static final RPOutputRegisterErrorCode CREATE_OUTPUT_REGISTER_ERROR =
			new RPOutputRegisterErrorCode("RPOR003", "CREATE OUTPUT REGISTER ERROR");

	/**
	 * Constante de código RPOR004: Error en la modificación de los registros de salida.
	 */
	public static final RPOutputRegisterErrorCode UPDATE_OUTPUT_REGISTER_ERROR =
			new RPOutputRegisterErrorCode("RPOR004", "UPDATE OUTPUT REGISTER ERROR");	
	/**
	 * Constante de código RPOR005: Error recogiendo de un registro de salida.
	 */
	public static final RPOutputRegisterErrorCode GET_OUTPUT_REGISTER_ERROR =
			new RPOutputRegisterErrorCode("RPOR005", "GET OUTPUT REGISTER ERROR");	
	
	/**
	 * Constructor con código de error y mensaje.
	 * 
	 * @param code
	 *            Código interno del error.
	 * @param message
	 *            Mensaje descriptivo del error.
	 */
	protected RPOutputRegisterErrorCode(String code, String message) {
		super(code, message);
	}
}