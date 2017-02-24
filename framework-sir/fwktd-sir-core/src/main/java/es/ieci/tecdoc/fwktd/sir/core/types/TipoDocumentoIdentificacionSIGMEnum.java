/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.ieci.tecdoc.fwktd.sir.core.types;

import es.ieci.tecdoc.fwktd.core.enums.StringValuedEnum;

/**
 * Enumerados para las constantes de los tipos de documentos de identificación
 * de interesados y representantes.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class TipoDocumentoIdentificacionSIGMEnum extends StringValuedEnum {

	private static final long serialVersionUID = -7451760723494525790L;

	private static final String NIF_VALUE = "2";
	private static final String CIF_VALUE = "1";
	private static final String PASAPORTE_VALUE = "3";
	private static final String NIE_VALUE = "4";
	private static final String OTROS_PERSONA_FISICA_VALUE = "5";
	private static final String CODIGO_ORIGEN_VALUE = "6";

	private static final String NIF_STRING = "N";
	private static final String CIF_STRING = "C";
	private static final String PASAPORTE_STRING = "P";
	private static final String NIE_STRING = "E";
	private static final String OTROS_PERSONA_FISICA_STRING = "X";
	private static final String CODIGO_ORIGEN_STRING = "O";

	/**
	 * NIF
	 */
	public static final TipoDocumentoIdentificacionSIGMEnum NIF = new TipoDocumentoIdentificacionSIGMEnum(NIF_STRING,
			NIF_VALUE);

	/**
	 * CIF.
	 */
	public static final TipoDocumentoIdentificacionSIGMEnum CIF = new TipoDocumentoIdentificacionSIGMEnum(
			CIF_STRING, CIF_VALUE);

	/**
	 * Pasaporte.
	 */
	public static final TipoDocumentoIdentificacionSIGMEnum PASAPORTE = new TipoDocumentoIdentificacionSIGMEnum(
			PASAPORTE_STRING, PASAPORTE_VALUE);

	/**
	 * NIE.
	 */
	public static final TipoDocumentoIdentificacionSIGMEnum NIE = new TipoDocumentoIdentificacionSIGMEnum(
			NIE_STRING, NIE_VALUE);

	/**
	 * Otros de persona física.
	 */
	public static final TipoDocumentoIdentificacionSIGMEnum OTROS_PERSONA_FISICA = new TipoDocumentoIdentificacionSIGMEnum(
			OTROS_PERSONA_FISICA_STRING, OTROS_PERSONA_FISICA_VALUE);

	/**
	 * Código de origen.
	 */
	public static final TipoDocumentoIdentificacionSIGMEnum CODIGO_ORIGEN = new TipoDocumentoIdentificacionSIGMEnum(
			CODIGO_ORIGEN_STRING, CODIGO_ORIGEN_VALUE);


	/**
	 * Constructor.
	 *
	 * @param name
	 *            Nombre del enumerado.
	 * @param value
	 *            Valor del enumerado.
	 */
	protected TipoDocumentoIdentificacionSIGMEnum(String name, String value) {
		super(name, value);
	}

	/**
	 * Obtiene la constante asociada al valor.
	 * @param value Valor de la constante
	 * @return Constante.
	 */
	public static TipoDocumentoIdentificacionSIGMEnum getTipoDocumentoIdentificacion(String value) {
		return (TipoDocumentoIdentificacionSIGMEnum) StringValuedEnum.getEnum(TipoDocumentoIdentificacionSIGMEnum.class, value);
	}
}
