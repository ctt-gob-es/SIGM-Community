package aww.sigem.expropiaciones.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aww.sigem.expropiaciones.rule.tags.ListaFincasTagRule;

import com.inamik.utils.SimpleTableFormatter;
import com.inamik.utils.TableFormatter;


import org.apache.log4j.Logger;
/**
 * @author root
 * Clase con utilidades para formatear tablas en ascii
 *
 */
public class TablaUtil {
	
	
	private static final Logger logger = 
		Logger.getLogger(ListaFincasTagRule.class);	
	
	public static String formateaTabla(List titulos, List contenido, int anchototal, boolean propietarios, int indiceProp, boolean otros) {
		
		
		// Se calculan los anchos de las columnas. Si aparece el campo propietario, habra que aumentar esa columnas y disminuir otras.
		int [] anchos = calcularAnchos(titulos, anchototal, propietarios, indiceProp, otros);
			
		//Prepara los datos para dibujarlos en la tabla			
		String[] table = generarTabla(titulos, contenido, anchos);			
					
		String resultado = "";

		for (int i = 0, size = table.length; i < size; i++)
		{
			resultado+= table[i] + "\n";
		}
		
		return resultado;
	}
	
	public static int[] calcularAnchos(List titulos,int anchototal, boolean propietarios, int indiceProp, boolean otros){
		int [] anchos = new int [titulos.size()];
		
		int anchocolumna = (((anchototal - 10)/ titulos.size())- 2);
		for (int i=0; i<titulos.size(); i++){
			if (propietarios){
				if (i==indiceProp){
					anchos[i] = anchocolumna*2;
					if (otros){
						anchos[i]+=anchocolumna/2;	
					}
				}
				else if ((i==indiceProp+1) || (i==indiceProp+2)){
					anchos[i] = anchocolumna/2;
					
				}
				else if((i==indiceProp+3) && (otros)){
					anchos[i]=anchocolumna + anchocolumna/2;	
				}
				else if (((i==indiceProp+4) || (i==indiceProp+5)) && (otros)){
					anchos[i] = anchocolumna/2;
				}
				
				else
					anchos[i] = anchocolumna;
			}
			else
				anchos[i] = anchocolumna;
		}		
		return anchos;
	}
	

	private static String[] generarTabla(List titulos, List contenido,  int[] anchos) {
		
		//División entera
		//int anchocolumna = (((anchototal - 10)/ titulos.size())- 2);
		
		String [] separadoresTitulos = new String [titulos.size()];
		String [] separadoresFilas = new String [titulos.size()];
		
		
		//Cada columna al tener un ancho diferente variarán los separadores.
		for (int j=0; j<titulos.size(); j++){
			separadoresTitulos[j] = "";
			separadoresFilas[j] = "";
			
			for (int i=0; i<= anchos[j]; i++) {
				separadoresTitulos[j]+="=";
				separadoresFilas[j]+="-";			
			}
		}
				
		String [] palabras;
		TableFormatter tf = new SimpleTableFormatter(false); // true = show border
		
		//Dibujar el encabezado de la tabla (títulos)	
		
		tf.nextRow();
		for (int i = 0, tam = titulos.size(); i < tam; i++){	
			//palabras = wordWrap((String)titulos.get(i),anchocolumna);

			palabras = wordWrap((String)titulos.get(i),anchos[i]);
			tf.nextCell();
			for (int j= 0, size = palabras.length;j < size; j++)
			{			
				tf.addLine( palabras[j]);				
			}
			tf.nextCell().addLine("  ");
		}
		tf.nextRow();
		
		for (int i = 0, tam = titulos.size(); i < tam; i++){	
			tf.nextCell();			
			tf.addLine(separadoresTitulos[i]);				
			tf.nextCell().addLine("  ");
		}
		
		//Dibujar los contenidos
		int indice=0;
		for (Iterator iterator = contenido.iterator(); iterator.hasNext();) {
			List fila = (List) iterator.next();
			tf.nextRow();
			for (Iterator iterator2 = fila.iterator(); iterator2.hasNext();) {
				String celda = (String) iterator2.next();
				palabras = wordWrap(celda,anchos[indice]);
				tf.nextCell();
				for (int j= 0, size = palabras.length;j < size; j++)
				{	
						tf.addLine(palabras[j]);
				
				}
				tf.nextCell().addLine("  ");
				indice++;
			}
			tf.nextRow();
			for (int i = 0, tam = fila.size(); i < tam; i++){	
				tf.nextCell();
				tf.addLine(separadoresFilas[i]);				
				tf.nextCell().addLine("  ");
			}
			indice = 0;
		}
		return tf.getFormattedTable();
	}
	
	
	private static String[] wordWrap(String str, int maxwidth) {
		
		Pattern wrapRE = Pattern.compile("(\\S\\S{" + maxwidth + ",}|.{1," + maxwidth + "})(\\s+|$)");

	    List list = new LinkedList();

	    Matcher m = wrapRE.matcher(str);

	    while (m.find()) list.add(m.group());
	        
	    return (String[]) list.toArray(new String[list.size()]);

	}

}
