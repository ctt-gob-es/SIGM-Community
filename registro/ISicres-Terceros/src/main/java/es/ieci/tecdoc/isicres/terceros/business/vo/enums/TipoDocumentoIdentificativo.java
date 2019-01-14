/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

/**
 * 
 */
package es.ieci.tecdoc.isicres.terceros.business.vo.enums;

import es.ieci.tecdoc.fwktd.core.enums.StringValuedEnum;

/**
 * @author cmorenog
 * 
 */
public class TipoDocumentoIdentificativo extends StringValuedEnum {


	private static final long serialVersionUID = -7451760723494525790L;

	private static final String NIF_VALUE = "N";
	private static final String CIF_VALUE = "C";
	private static final String PASAPORTE_VALUE = "P";
	private static final String NIE_VALUE = "E";
	private static final String OTROS_PERSONA_FISICA_VALUE = "X";
	private static final String CODIGOORIGEN_VALUE = "O";

	private static final String NIF_STRING = "2";
	private static final String CIF_STRING = "1";
	private static final String PASAPORTE_STRING = "3";
	private static final String NIE_STRING = "4";
	private static final String OTROS_PERSONA_FISICA_STRING = "5";
	private static final String CODIGOORIGEN_STRING = "6";
	
	/**
	 * NIF
	 */
	public static final TipoDocumentoIdentificativo NIF = new TipoDocumentoIdentificativo(NIF_STRING,
			NIF_VALUE);

	/**
	 * CIF.
	 */
	public static final TipoDocumentoIdentificativo CIF = new TipoDocumentoIdentificativo(
			CIF_STRING, CIF_VALUE);

	/**
	 * Pasaporte.
	 */
	public static final TipoDocumentoIdentificativo PASAPORTE = new TipoDocumentoIdentificativo(
			PASAPORTE_STRING, PASAPORTE_VALUE);

	/**
	 * NIE.
	 */
	public static final TipoDocumentoIdentificativo NIE = new TipoDocumentoIdentificativo(
			NIE_STRING, NIE_VALUE);

	/**
	 * Otros de persona física.
	 */
	public static final TipoDocumentoIdentificativo OTROS_PERSONA_FISICA = new TipoDocumentoIdentificativo(
			OTROS_PERSONA_FISICA_STRING, OTROS_PERSONA_FISICA_VALUE);
	
	/**
	 * Codigo origen.
	 */
	public static final TipoDocumentoIdentificativo CODIGOORIGEN = new TipoDocumentoIdentificativo(
		CODIGOORIGEN_STRING, CODIGOORIGEN_VALUE);


	/**
	 * Constructor.
	 *
	 * @param name
	 *            Nombre del enumerado.
	 * @param value
	 *            Valor del enumerado.
	 */
	protected TipoDocumentoIdentificativo(String name, String value) {
		super(name, value);
	}

	/**
	 * Obtiene la constante asociada al valor.
	 * @param value Valor de la constante
	 * @return Constante.
	 */
	public static TipoDocumentoIdentificativo getTipoDocumentoIdentificativo(String value) {
		return (TipoDocumentoIdentificativo) StringValuedEnum.getEnum(TipoDocumentoIdentificativo.class, value);
	}

}
