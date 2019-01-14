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
 * Clase que recoge los códigos de error referentes a operaciones con registros de
 * entrada.
 * 
 * @author jortizs
 */
public class RPInputRegisterErrorCode
		extends ErrorCode {
	/** Constante de código RPIR001: Error en la obtención del registro de entrada. */
	public static final RPInputRegisterErrorCode GET_INPUT_REGISTER_ERROR =
			new RPInputRegisterErrorCode("RPIR001", "GET INPUT REGISTER ERROR");
	/**
	 * Constante de código RPIR002: Error en la obtención del último registro de entrada
	 * del usuario.
	 */
	public static final RPInputRegisterErrorCode GET_LAST_INPUT_REGISTER_FOR_USER_ERROR =
			new RPInputRegisterErrorCode("RPIR002",
					"GET LAST INPUT REGISTER FOR USER ERROR");
	/**
	 * Constante de código RPIR003: Error en la carga de los listados de registros de
	 * entrada del usuario.
	 */
	public static final RPInputRegisterErrorCode LOAD_INPUT_REGISTER_FOR_USER_ERROR =
			new RPInputRegisterErrorCode("RPIR003",
					"LOAD INPUT REGISTER FOR USER ERROR");
	/**
	 * Constante de código RPIR004: Error en la navegación entre los registros de entrada
	 * del usuario.
	 */
	public static final RPInputRegisterErrorCode NAVIGATE_INTO_INPUT_REGISTER_LIST_ERROR =
			new RPInputRegisterErrorCode("RPIR004",
					"NAVIGATE INTO INPUT REGISTER FOR USER ERROR");
	/**
	 * Constante de código RPIR005: Error en la apertura de la consulta de registros de
	 * entrada
	 * del usuario.
	 */
	public static final RPInputRegisterErrorCode OPEN_REGISTER_QUERY_ERROR =
			new RPInputRegisterErrorCode("RPIR005",
					"OPEN INPUT REGISTER QUERY ERROR");
	/**
	 * Constante de código RPIR006: Error en el cierre de la consulta de registros de
	 * entrada
	 * del usuario.
	 */
	public static final RPInputRegisterErrorCode CLOSE_REGISTER_QUERY_ERROR =
			new RPInputRegisterErrorCode("RPIR006",
					"CLOSE INPUT REGISTER QUERY ERROR");
	
	/**
	 * Constante de código RPIR007: Error al rellenar los parámetros de la consulta de
	 * registros de entrada
	 * del usuario.
	 */
	public static final RPInputRegisterErrorCode LOAD_INPUT_REGISTER_ARGUMENTS_ERROR =
			new RPInputRegisterErrorCode("RPIR007",
					"LOAD INPUT REGISTER QUERY ERROR");
	
	/**
	 * Constante de código RPIR008: Error en la marcar el formulario como sólo lectura.
	 */
	public static final RPInputRegisterErrorCode LOCK_INPUT_REGISTER_ERROR =
			new RPInputRegisterErrorCode("RPIR008", "LOCK INPUT REGISTER ERROR");
	
	/**
	 * Constante de código RPIR009: Error en la obtención del histórico de registros de
	 * entrada.
	 */
	public static final RPInputRegisterErrorCode GET_HISTORICAL_INPUT_REGISTER_ERROR =
			new RPInputRegisterErrorCode("RPIR009",
					"GET HISTORICAL INPUT REGISTER ERROR");
	
	/**
	 * Constante de código RPIR010: Error en la carga de los registros de entrada.
	 */
	public static final RPInputRegisterErrorCode LOAD_INPUT_REGISTER_ERROR =
			new RPInputRegisterErrorCode("RPIR010", "LOAD INPUT REGISTER ERROR");
	
	/**
	 * Constante de código RPIR011: Error en la creación de un nuevo registro de entrada.
	 */
	public static final RPInputRegisterErrorCode CREATE_INPUT_REGISTER_ERROR =
			new RPInputRegisterErrorCode("RPIR011",
					"CREATE INPUT REGISTER ERROR");
	
	/**
	 * Constante de código RPIR012: Error en la modificación de un nuevo registro de
	 * entrada.
	 */
	public static final RPInputRegisterErrorCode UPDATE_INPUT_REGISTER_ERROR =
			new RPInputRegisterErrorCode("RPIR012",
					"UPDATE INPUT REGISTER ERROR");


	/**
	 * Constructor con código de error y mensaje.
	 * 
	 * @param code
	 *            Código interno del error.
	 * @param message
	 *            Mensaje descriptivo del error.
	 */
	protected RPInputRegisterErrorCode(String code, String message) {
		super(code, message);
	}
}