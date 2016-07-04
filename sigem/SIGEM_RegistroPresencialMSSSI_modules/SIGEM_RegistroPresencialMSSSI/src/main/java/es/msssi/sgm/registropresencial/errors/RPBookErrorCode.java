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
 * Clase que recoge los códigos de error referentes a operaciones con los libros.
 * 
 * @author jortizs
 */
public class RPBookErrorCode
		extends ErrorCode {
	/** Constante de código RPB003: Error en la obtención de los libros. */
	public static final RPBookErrorCode GET_BOOKS_ERROR = new RPBookErrorCode("RPB003",
			"GET BOOKS ERROR");
	/** Constante de código RPB004: Error en la obtención del libro. */
	public static final RPBookErrorCode GET_BOOK_ERROR = new RPBookErrorCode("RPB004",
			"GET BOOK ERROR");
	/** Constante de código RPB005: Error al abrir el libro. */
	public static final RPBookErrorCode OPEN_BOOK_ERROR = new RPBookErrorCode("RPB005",
			"OPEN BOOK ERROR");
	/** Constante de código RPB006: Error al cerrar el libro. */
	public static final RPBookErrorCode CLOSE_BOOK_ERROR = new RPBookErrorCode("RPB006",
			"CLOSE BOOK ERROR");
	/** Constante de código RPB007: Error al obtener el histórico del libro. */
	public static final RPBookErrorCode GET_BOOK_HISTORICAL_ERROR = new RPBookErrorCode(
			"RPB007", "GET BOOK HISTORICAL ERROR");
	/** Constante de código RPB008: Error al obtener los datos de sesión del libro. */
	public static final RPBookErrorCode GET_SESSION_BOOK_ERROR = new RPBookErrorCode(
			"RPB008", "GET SESSION BOOK ERROR");
	
	/**
	 * Constructor con código de error y mensaje.
	 * 
	 * @param code
	 *            Código interno del error.
	 * @param message
	 *            Mensaje descriptivo del error.
	 */
	protected RPBookErrorCode(String code, String message) {
		super(code, message);
	}
}