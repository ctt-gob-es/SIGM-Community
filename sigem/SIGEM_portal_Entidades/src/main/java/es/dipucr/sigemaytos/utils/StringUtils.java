package es.dipucr.sigemaytos.utils;

import java.util.List;

public class StringUtils {

	public static String SQL_LIKE_CHAR = "%";
	
	public interface HTML{
		public static String NEW_LINE = "<br/>";
		public static String EURO = "&euro;";
	}
	
	/**
	* Función que elimina acentos y caracteres especiales de
	* una cadena de texto.
	* @param input
	* @return cadena de texto limpia de acentos y caracteres especiales.
	*/
	public static String limpiarCaracteresEspeciales(String input) {

		 // Cadena de caracteres original a sustituir.
		 String original = " áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		 // Cadena de caracteres ASCII que reemplazarán los originales.
		 String ascii    = "_aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		 String output = input;
	
		 for (int i=0; i<original.length(); i++) {
			 // Reemplazamos los caracteres especiales.
			 output = output.replace(original.charAt(i), ascii.charAt(i));
		 }
		 return output;
	}
	
	/**
	* Función que elimina acentos y caracteres especiales, no espacios
	* @param input
	* @return cadena de texto limpia de acentos y caracteres especiales.
	*/
	public static String limpiarCaracteresEspecialesNoEspacios(String input) {

		 return limpiarCaracteresEspeciales(input).replace("_", " ");
	}

	/**
	 * Devuelve si una cadena es nula o vacía
	 * @param cadena
	 * @return
	 */
	public static boolean esNuloOVacio(String cadena){
		return (null == cadena || cadena.equals(""));
	}
	
	/**
	 * Convierte una lista en un IN SQL ('a1','a2',...,'an')
	 * @param lista
	 * @return
	 */
	public static String convertirListaInSQL(List<String> lista){

		StringBuffer sb = new StringBuffer(); 
		if (null != lista && lista.size() > 0){
			sb.append("(");
			for (int i=0; i<lista.size(); i++){
				sb.append("'");
				sb.append(lista.get(i));
				sb.append("'");
				if (i < (lista.size() - 1)){
					sb.append(",");
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	public static Boolean esNumero(String num){
		if (num.equals("0") || num.equals("1") || num.equals("2")  ||  num.equals("3")
		 || num.equals("4") || num.equals("5") || num.equals("6")  ||  num.equals("7")
		 || num.equals("8") || num.equals("9")) 
			return true;
		else
			return false;
	}
	public static Float partirHoras(String horas){
		String num="";
		for (int i=0; i< horas.length(); i++){
			if (horas.substring(i,i+1).equals(" "))
				num=num+".";
			if (esNumero(horas.substring(i,i+1)))
					num=num+horas.substring(i,i+1);
		}
		Float deciNum=Float.parseFloat(num);
		return deciNum;
		
	}
	public static Double pasaHoraADecimal(String hora){
		String num="";
		double nummin=0.0;
		double numhor=0.0;
		String strmin="";
		String strhor="";
		boolean finhoras=false;
		for (int i=0; i< hora.length(); i++){
			if (hora.substring(i,i+1).equals(" ")){
				num=num+".";
				finhoras=true;
			}
			if (esNumero(hora.substring(i,i+1))){
				if (finhoras)
					strmin=strmin+hora.substring(i,i+1);
				else
					strhor=strhor+hora.substring(i,i+1);
			}
		}
		nummin=Double.parseDouble(strmin)/60;
		numhor=Double.parseDouble(strhor);
		Double horanum=numhor+nummin;
		return horanum;		
		
	}
	
	/**
	 * Devuelve vacío si la cadena es nula
	 * @param str
	 * @return
	 */
	public static String nuloAVacio(String str){
		if (null == str) return "";
		else return str;
	}
	
	/**
	 * 
	 * @param cadena
	 * @param tamanioTotal
	 * @param caracter
	 * @return
	 */
	public static String completarCadenaConCaracter(String cadena, int tamanioTotal, char caracter){
		
		StringBuffer sbResult = new StringBuffer(cadena);
		while(sbResult.length() < tamanioTotal){
			sbResult.insert(0, caracter);
		}
		return sbResult.toString();
	}
}
