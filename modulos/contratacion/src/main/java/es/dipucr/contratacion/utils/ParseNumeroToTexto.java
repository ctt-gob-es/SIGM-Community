package es.dipucr.contratacion.utils;

import java.math.BigDecimal;

public class ParseNumeroToTexto {
	
	private final String[] UNIDADES = { "", "un ", "dos ", "tres ",
			"cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve ", "diez ",
			"once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis",
			"diecisiete", "dieciocho", "diecinueve", "veinte" };
	
	private final String[] DECENAS = { "venti", "treinta ", "cuarenta ",
			"cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa ",
			"cien " };
	
	private final String[] CENTENAS = { "ciento ", "doscientos ",
			"trescientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
			"setecientos ", "ochocientos ", "novecientos " };

	
	public String convertNumberToLetter(BigDecimal number)
	throws NumberFormatException {
		String converted = new String();

		// Validamos que sea un numero legal
		if (number.doubleValue() > 999999999)
			throw new NumberFormatException(
					"El numero es mayor de 999'999.999, no es posible convertirlo");

		String splitNumber[] = String.valueOf(number).replace('.', '#')
				.split("#");

		// Descompone el trio de millones - ¡SGT!
		int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				8))
				+ String.valueOf(getDigitAt(splitNumber[0], 7))
				+ String.valueOf(getDigitAt(splitNumber[0], 6)));
		if (millon == 1)
			converted = "Un millón ";
		if (millon > 1)
			converted = convertNumber(String.valueOf(millon)) + " millones ";

		// Descompone el trio de miles - ¡SGT!
		int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				5))
				+ String.valueOf(getDigitAt(splitNumber[0], 4))
				+ String.valueOf(getDigitAt(splitNumber[0], 3)));
		if (miles == 1)
			converted += "mil ";
		if (miles > 1)
			converted += convertNumber(String.valueOf(miles)) + "mil ";

		// Descompone el ultimo trio de unidades - ¡SGT!
		int cientos = Integer.parseInt(String.valueOf(getDigitAt(
				splitNumber[0], 2))
				+ String.valueOf(getDigitAt(splitNumber[0], 1))
				+ String.valueOf(getDigitAt(splitNumber[0], 0)));
		if (cientos == 1)
			converted += "un";

		if (millon + miles + cientos == 0)
			converted += "cero";
		if (cientos > 1)
			converted += convertNumber(String.valueOf(cientos));

		//converted += "PESOS";
		if(splitNumber.length > 1){
			int primero = getDigitAt(splitNumber[1], 2);
			int segundo = getDigitAt(splitNumber[1], 1);
			int tercero = getDigitAt(splitNumber[1], 0);
			int centavos = Integer.parseInt(String.valueOf(primero) + String.valueOf(segundo) + String.valueOf(tercero));
			if (centavos == 1)
				converted += "con uno";
				//converted += " CON UN CENTAVO";
			if (centavos > 1)
				converted += "con " + convertNumber(String.valueOf(centavos));
//				+ "CENTAVOS";
//				converted += " con " + convertNumber(String.valueOf(centavos))
//						+ "CENTAVOS";
		}
		

		return converted;
	}
	
	/**
	 * Retorna el digito numerico en la posicion indicada de derecha a izquierda
	 */
	private int getDigitAt(String origin, int position) {
		if (origin.length() > position && position >= 0)
			return origin.charAt(origin.length() - position - 1) - 48;
		return 0;
	}
	
	/**
	 * Convierte los trios de numeros que componen las unidades, las decenas y
	 * las centenas del numero.
	 */
	private String convertNumber(String number) {
		if (number.length() > 3)
			throw new NumberFormatException(
					"La longitud maxima debe ser 3 digitos");
 
		String output = new String();
		if (getDigitAt(number, 2) != 0)
			output = CENTENAS[getDigitAt(number, 2) - 1];
 
		int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
				+ String.valueOf(getDigitAt(number, 0)));
 
		if (k <= 20)
			output += UNIDADES[k];
		else {
			if (k > 30 && getDigitAt(number, 0) != 0)
				output += DECENAS[getDigitAt(number, 1) - 2] + "y "
						+ UNIDADES[getDigitAt(number, 0)];
			else
				output += DECENAS[getDigitAt(number, 1) - 2]
						+ UNIDADES[getDigitAt(number, 0)];
		}
 
		// Caso especial con el 100
		if (getDigitAt(number, 2) == 1 && k == 0)
			output = "cien";
 
		return output;
	}



}
