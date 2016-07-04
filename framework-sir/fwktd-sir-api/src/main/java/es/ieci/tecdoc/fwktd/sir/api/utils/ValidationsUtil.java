/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.ieci.tecdoc.fwktd.sir.api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase para gestionar las validaciones realizadas sobre las pantallas.
 * 
 */
public class ValidationsUtil {

    public enum TipoUltCaracter {
	NUMERO, LETRA, AMBOS
    };

    public enum TipoIdentificador {
	N, E, C, P, X, O
    };

    /**
     * Método que valida el tipo de identificación, en el caso de encontrar
     * incidencias o errores lanza excepciones del tipo LibraeException para
     * mostrarlas mediante mensajes popup en la pantalla.
     * 
     * @param tipoIdentificador
     *            tipo de identificación a validar
     * @param identificador
     *            cadena alfanumérica asociada al tipo de identificación a
     *            validar
     */
    public static boolean validarIdentificador(String tipoIdentificador, String identificador) {
	boolean valido = true;
	if (tipoIdentificador != null) {
	    try {
		switch (TipoIdentificador.valueOf(tipoIdentificador)) {
		case N:
		    if (null != identificador) {
			String ident = identificador.toUpperCase();
			ValidationsUtil.validarNIF(ident);
		    }
		    break;
		case E:
		    if (null != identificador) {
			String ident = identificador.toUpperCase();
			ValidationsUtil.validarNIE(ident);
		    }
		    break;
		case C:
		    if (null != identificador) {
			String ident = identificador.toUpperCase();
			ValidationsUtil.validarCIF(ident);
		    }
		    break;
		default:
		    break;
		}
	    }
	    catch (Exception e) {
		valido = false;
	    }
	}
	return valido;
    }

    /**
     * Método que valida el tipo de documento NIE.
     * 
     * @param nie
     *            cadena alfanumérica asociada al tipo de identificación a
     *            validar
     * @throws Exception
     */
    private static void validarNIE(String nie) throws Exception {

	if (null != nie) {

	    // Se comprueba la validez del primer caracter {X,Y,Z}
	    if ("XYZ".indexOf(nie.charAt(0)) == -1) {
		throw new Exception("El número de identificación no es válido");
	    }

	    // Se comprueba la longitud correcta del NIE
	    if (nie.length() != 9) {
		throw new Exception("El número de identificación no es válido");
	    }

	    String nieUP = nie.toUpperCase();
	    String nieval = nieUP.substring(1, 9);
	    Pattern mask = Pattern.compile("[0-9]{7}[A-Z]?");
	    Matcher matcher = mask.matcher(nieval);

	    // Se comprueba la validez del formato
	    if (!matcher.matches()) {
		throw new Exception("El número de identificación no es válido");
	    }

	    // Se genera un documento NIF considerando la letra X de comienzo
	    if (nieUP.charAt(0) == 'X') {
		nieval = "0".concat(nieval);
	    }

	    // Se genera un documento NIF considerando la letra Y de comienzo
	    if (nieUP.charAt(0) == 'Y') {
		nieval = "1".concat(nieval);
	    }

	    // Se genera un documento NIF considerando la letra Z de comienzo
	    if (nieUP.charAt(0) == 'Z') {
		nieval = "2".concat(nieval);
	    }

	    // Se realiza la misma validación que para el NIF
	    ValidationsUtil.validarNIF(nieval);
	}
    }

    /**
     * Método que valida el tipo de documento NIF.
     * 
     * @param nif
     *            cadena alfanumérica asociada al tipo de identificación a
     *            validar
     * @throws Exception
     */
    private static void validarNIF(String nif) throws Exception {

	if (null != nif) {
	    String nifUP = nif.toUpperCase();
	    Pattern mask = Pattern.compile("[0-9]{8}[A-Z]?");
	    Matcher matcher = mask.matcher(nifUP);

	    // Se comprueba la validez del formato
	    if (!matcher.matches()) {
		throw new Exception("El número de identificación no es válido");
	    }

	    char ultimoCar = nifUP.charAt(nifUP.length() - 1);

	    // Se comprueba que el último caracter sea una letra
	    if (ultimoCar >= 'A' && ultimoCar <= 'Z') {
		// Se establece el conjunto de letras válidos
		String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

		String dni = nifUP.substring(0, 8);
		String digitoControl = nifUP.substring(8, 9);

		// Se calcula la letra de control
		int posicion_modulo = Integer.parseInt(dni) % 23;

		String digitoControlCalculado =
			letras.substring(posicion_modulo, posicion_modulo + 1);

		// Se comprueba la validez de la letra de control
		if (!digitoControl.equalsIgnoreCase(digitoControlCalculado)) {
		    throw new Exception("El número de identificación no es válido");
		}
	    }
	    else {
		throw new Exception("El número de identificación no es válido");
	    }
	}
    }



    /**
     * Realiza la validacion si la cadena representa un CIF
     * 
     * @param strCadena
     *            la cadena a comprobar
     * @return true si la cadena representa un CIF del tipo indicado
     */
    private static void validarCIF(String cif) throws Exception {
	try{
	    if (cif != null && !"".equals(cif)){
        	Validador val = new Validador();
        	int ret = val.checkNif(cif);
        	if (ret <= 0){
        	    throw new Exception("El número de identificación no es válido");
        	}
	    }
	}catch (Exception e){
	    throw e;
	}
    }
    

}