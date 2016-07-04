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
 * Clase que recoge los códigos de error referentes a operaciones con distribuciones.
 * 
 * @author jortizs
 */
public class RPDistributionErrorCode
		extends ErrorCode {
	/** Constante de código RPD001: Error en la obtención de la distribución. */
	public static final RPBookErrorCode GET_DISTRIBUTION_ERROR = new RPBookErrorCode(
			"RPD001", "GET DISTRIBUTION ERROR");
	/** Constante de código RPD002: Error en el archivado. */
	public static final RPBookErrorCode ARC_DISTRIBUTION_ERROR = new RPBookErrorCode(
			"RPD002", "ARCHIVE DISTRIBUTION ERROR");
	/** Constante de código RPD003: Error ACEP distribución. */
	public static final RPBookErrorCode ACEPTAR_DISTRIBUTION_ERROR = new RPBookErrorCode(
			"RPD003", "ACCEPT DISTRIBUTION ERROR");
	/** Constante de código RPD004: Error RECHAZAR distribución. */
	public static final RPBookErrorCode RECHAZAR_DISTRIBUTION_ERROR = new RPBookErrorCode(
			"RPD004", "REJECT DISTRIBUTION ERROR");
	/** Constante de código RPD005: Error CAMBIAR DESTINO distribución. */
	public static final RPBookErrorCode CAMBIARDEST_DISTRIBUTION_ERROR = new RPBookErrorCode(
			"RPD005", "CHANGE DISTRIBUTION ERROR");
	/** Constante de código RPD006: Error REDISTRIBUTION distribución. */
	public static final RPBookErrorCode REDISTRIBUTION_DISTRIBUTION_ERROR = new RPBookErrorCode(
			"RPD006", "REDISTRIBUTION DISTRIBUTION ERROR");
	/** Constante de código RPD007: Error obteniendo el número de distribuciones. */
	public static final RPBookErrorCode GET_COUNT_DISTRIBUTION_ERROR = new RPBookErrorCode(
		"RPD007", "COUNT DISTRIBUTIONS ERROR");
	
	/**
	 * Constructor con código de error y mensaje.
	 * 
	 * @param code
	 *            Código interno del error.
	 * @param message
	 *            Mensaje descriptivo del error.
	 */
	protected RPDistributionErrorCode(String code, String message) {
		super(code, message);
	}
}