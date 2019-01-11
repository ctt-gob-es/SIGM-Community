/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.errors;

import core.error.ErrorCode;

/**
 * Clase que recoge los códigos de error referentes a operaciones con los procedimientos.
 * 
 * @author eacuna
 */
public class RPProcedureErrorCode
		extends ErrorCode {
	/** Constante de código RPB003: Error en la obtención de los procedimientos. */
	public static final RPProcedureErrorCode GET_BOOKS_ERROR = new RPProcedureErrorCode("RPP003",
			"GET BOOKS ERROR");
	/** Constante de código RPB004: Error en la obtención del procedimiento. */
	public static final RPProcedureErrorCode GET_BOOK_ERROR = new RPProcedureErrorCode("RPP004",
			"GET BOOK ERROR");
	/** Constante de código RPB008: Error al obtener los datos de sesión del procedimiento. */
	public static final RPProcedureErrorCode GET_SESSION_BOOK_ERROR = new RPProcedureErrorCode(
			"RPB005", "GET SESSION BOOK ERROR");
	
	/**
	 * Constructor con código de error y mensaje.
	 * 
	 * @param code
	 *            Código interno del error.
	 * @param message
	 *            Mensaje descriptivo del error.
	 */
	protected RPProcedureErrorCode(String code, String message) {
		super(code, message);
	}
}