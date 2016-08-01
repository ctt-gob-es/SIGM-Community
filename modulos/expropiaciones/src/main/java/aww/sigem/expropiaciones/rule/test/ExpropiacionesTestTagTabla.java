package aww.sigem.expropiaciones.rule.test;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.inamik.utils.SimpleTableFormatter;
import com.inamik.utils.TableFormatter;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class ExpropiacionesTestTagTabla implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ExpropiacionesTestTagTabla.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			logger.warn("Ejecutando regla ExpropiacionesTestTagTabla");
			
			String[] table = generarEncabezados();
			
			String resultado = "";

			for (int i = 0, size = table.length; i < size; i++)
			{
				resultado+= table[i] + "\n";
			}
			
			
			String textoTabla = resultado;
			
			
			logger.warn("Tabla dibujada:\n" + textoTabla);
			
			logger.warn("Fin regla ExpropiacionesTestTagTabla");
			return textoTabla;
		} catch (Exception e) {
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	public String dibujarTabla()
	{
		TableFormatter tf = new SimpleTableFormatter(true) // true = show border
		.nextRow()
			.nextCell()
				.addLine(".")
			.nextCell()
				.addLine("..........")
			.nextCell()
				.addLine(" ")
			.nextCell()
				.addLine("..........")
			.nextCell()
				.addLine(" ")
			.nextCell()
				.addLine("..........")
		.nextRow()
			.nextCell()
				.addLine(".")
				.addLine(".")
				.addLine(".")
				.addLine(".")
			.nextCell(TableFormatter.ALIGN_LEFT, TableFormatter.VALIGN_TOP)
				.addLine("Left")
				.addLine("Top")
			.nextCell()
			.nextCell(TableFormatter.ALIGN_LEFT, TableFormatter.VALIGN_CENTER)
				.addLine("Left")
				.addLine("Center")
			.nextCell()
			.nextCell(TableFormatter.ALIGN_LEFT, TableFormatter.VALIGN_BOTTOM)
				.addLine("Left")
				.addLine("Bottom")
		.nextRow().nextCell().addLine(" ")
		.nextRow()
			.nextCell()
				.addLine(".")
				.addLine(".")
				.addLine(".")
				.addLine(".")
			.nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_TOP)
				.addLine("Center")
				.addLine("Top")
			.nextCell()
			.nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
				.addLine("Center")
				.addLine("Center")
			.nextCell()
			.nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_BOTTOM)
				.addLine("Center")
				.addLine("Bottom")
		.nextRow().nextCell().addLine(" ")
		.nextRow()
			.nextCell()
				.addLine(".")
				.addLine(".")
				.addLine(".")
				.addLine(".")
			.nextCell(TableFormatter.ALIGN_RIGHT, TableFormatter.VALIGN_TOP)
				.addLine("Right")
				.addLine("Top")
			.nextCell()
			.nextCell(TableFormatter.ALIGN_RIGHT, TableFormatter.VALIGN_CENTER)
				.addLine("Right")
				.addLine("Center")
			.nextCell()
			.nextCell(TableFormatter.ALIGN_RIGHT, TableFormatter.VALIGN_BOTTOM)
				.addLine("Right")
				.addLine("Bottom")
		;
		
		String[] table = tf.getFormattedTable();
		
		String resultado = "";

		for (int i = 0, size = table.length; i < size; i++)
		{
			resultado+= (i + 1) + "\t" + table[i];
		}

		
		resultado+="\nTable size = " + tf.getTableWidth() + " x " + tf.getTableHeight();
		
		return resultado;
	}
	
	

	private static String[] generarEncabezados (){	
		
	
		List titulos = new ArrayList();
		
		titulos.add("Finca Num.");
		titulos.add("Propietario");
		titulos.add("Polígono");
		titulos.add("Parcela");
		titulos.add("Municipio de Parcela");
		titulos.add("Superficie de Parcela (m2)");
		titulos.add("Superficie a ocupar (m2)");
		titulos.add("Calificación");	
		
		List contenido = new ArrayList();
		
		contenido.add(titulos);
		contenido.add(titulos);
		contenido.add(titulos);
		
		return generarTabla(titulos, contenido, 70);
		
	}

	
	public static String[] generarTabla(List titulos, List contenido, int anchototal) {
		
		//División entera
		int anchocolumna = (((anchototal - 10)/ titulos.size())- 2);
		String separadorTitulo = "";
		String separadorFilas = "";
		for (int i=0; i<= anchocolumna; i++) {
			separadorTitulo+="=";
			separadorFilas+="-";			
		}
		
		
		
		String [] palabras;
		TableFormatter tf = new SimpleTableFormatter(false); // true = show border
		
		//Dibujar el encabezado de la tabla (títulos)	
		
		tf.nextRow();
		for (int i = 0, tam = titulos.size(); i < tam; i++){	
			palabras = wordWrap((String)titulos.get(i),anchocolumna);
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
			tf.addLine(separadorTitulo);				
			tf.nextCell().addLine("  ");
		}
		
		
		//Dibujar los contenidos
		
		for (Iterator iterator = contenido.iterator(); iterator.hasNext();) {
			List fila = (List) iterator.next();
			tf.nextRow();
			for (Iterator iterator2 = fila.iterator(); iterator2.hasNext();) {
				String celda = (String) iterator2.next();
				palabras = wordWrap(celda,anchocolumna);
				tf.nextCell();
				for (int j= 0, size = palabras.length;j < size; j++)
				{			
					tf.addLine( palabras[j]);				
				}
				tf.nextCell().addLine("  ");
				
			}
			tf.nextRow();
			for (int i = 0, tam = fila.size(); i < tam; i++){	
				tf.nextCell();
				tf.addLine(separadorFilas);				
				tf.nextCell().addLine("  ");
			}
			
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
