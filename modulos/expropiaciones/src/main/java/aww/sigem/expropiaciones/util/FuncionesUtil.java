package aww.sigem.expropiaciones.util;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class FuncionesUtil {

	private static final Logger logger = 
		Logger.getLogger(FuncionesUtil.class);	
	
	public static double redondeoDecimales(double numero){
		long mult=(long)Math.pow(10,2);
		double numeroFormateado=(Math.round(numero*mult))/(double)mult;
		
		return numeroFormateado;		
	}
	
	/**
	 * Si solo tiene un decimal, se concatena un 0 al final.
	 * @param numero 
	 * @return Cadena de texto con dos decimales
	 */
	public static String imprimirDecimales(double numero){
		
		//double redondeo = redondeoDecimales(numero);
		
		String formato =""+numero;		
		String decimales = formato.substring(formato.indexOf('.')+1);
		
		if (decimales.length() == 1){			
			formato+="0";
		}		
		return formato;		
	}
	
	public static String eliminarEspacios(String texto) {
        StringTokenizer tokens = new StringTokenizer(texto);
        StringBuilder buff = new StringBuilder();      
        
        while (tokens.hasMoreTokens()) {        	
            buff.append(tokens.nextToken());
        }
        
        return buff.toString().trim();
    }
}
