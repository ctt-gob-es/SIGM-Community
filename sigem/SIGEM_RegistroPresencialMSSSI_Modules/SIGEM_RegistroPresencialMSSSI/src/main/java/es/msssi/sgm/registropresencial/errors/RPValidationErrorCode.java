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
 * Clase que recoge los códigos de error referentes a operaciones con validaciones.
 * 
 * @author jortizs
 */
public class RPValidationErrorCode
		extends ErrorCode {
	/** Constante de código RPV001: Error . */
	public static final RPValidationErrorCode ERROR = new RPValidationErrorCode("RPV001",
			"");
	
	/**
	 * Constructor con código de error y mensaje.
	 * 
	 * @param code
	 *            Código interno del error.
	 * @param message
	 *            Mensaje descriptivo del error.
	 */
	protected RPValidationErrorCode(String code, String message) {
		super(code, message);
	}
}