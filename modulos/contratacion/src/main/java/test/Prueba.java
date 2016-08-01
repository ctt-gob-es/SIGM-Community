package test;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.ValorXML;
import es.dipucr.contratacion.utils.LectorXML;

public class Prueba {
	private static final Logger logger = Logger.getLogger(Prueba.class);
	

	/**
	 * @param args
	 * @throws ISPACRuleException 
	 */
	public static void main (String [] args) throws ISPACRuleException {

		
		ArrayList<ValorXML> nodos = new ArrayList<ValorXML>();	
		logger.warn("---------------------------------- inicio PARSEO---------------------------------------------");
		//String url = "http://contrataciondelestado.es/codice/cl/2.0/CalculationExpressionCode-2.0.gc";
		String url = "https://contrataciondelestado.es/codice/cl/1.04/DocumentIDTypeCode-1.04.gc";
		logger.warn("url--- "+url);
		LectorXML lector = new LectorXML(nodos);
		lector.leer(url);
		logger.warn("----------------------------------realizado PARSEO--------------------------------------------");
		
		//Recorrer el listado y ordenarlo.
		for(int i = 0; i < nodos.size(); i++){
			//Recorrer el vector y cada valor de code ver si es un número y
			//ordenador en un vector
			ValorXML valorXml = (ValorXML) nodos.get(i);
			try{
			}
			catch(NumberFormatException e){
			}
		}
		
		//Ordacion descendente
		HashMap<String, String> hmCodice = new HashMap<String, String>();
		String [] stringArray = obtenerCodeKey(nodos, hmCodice);
		Arrays.sort(stringArray);
		logger.warn("****** Ordenando String Array *******");
		for (String str : stringArray) {
			logger.warn(str);
		}

	}

	private static String[] obtenerCodeKey(ArrayList nodos, HashMap<String, String> hmCodice) {
		String [] sCodeKey = new String [nodos.size()];
		hmCodice = new HashMap<String, String>();
		for(int i = 0; i < nodos.size(); i++){
			ValorXML valorXml = (ValorXML) nodos.get(i);
			hmCodice.put(valorXml.getCodeKey(), valorXml.getNombre());
			sCodeKey[i] = valorXml.getCodeKey();
		}
		return sCodeKey;
		
	}

}
