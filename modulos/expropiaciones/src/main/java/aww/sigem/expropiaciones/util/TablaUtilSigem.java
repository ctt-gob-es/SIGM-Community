package aww.sigem.expropiaciones.util;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.Properties;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.api.item.util.ListCollection;
import ieci.tdw.ispac.ispaclib.item.GenericItem;
import ieci.tdw.ispac.ispaclib.templates.TemplateTableInfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class TablaUtilSigem {
	private static final Logger logger = 
		Logger.getLogger(TablaUtilSigem.class);	
	
	private static TemplateTableInfo creaTabla(List titulos, List contenido, String tituloTabla){
		String[] titleColumns = new String[titulos.size()];
		String[] columns = new String[titulos.size()];
		
		for (int i=0; i<titulos.size(); i++){
			titleColumns[i] = (String)titulos.get(i);
			columns[i] = (String)titulos.get(i);
		}
		
		return new TemplateTableInfo(tituloTabla, columns, titleColumns);
	}

	public static TemplateTableInfo formateaEtiquetas(List titulos, List contenido) throws ISPACException {
		//logger.warn("Ejecutando metodo formateaEtiquetas");
		
		TemplateTableInfo table = creaTabla(titulos, contenido,"Etiquetas Postales");
		String[] columns = table.getColumns();
		
		Properties properties = getProperties(titulos);
		List tabla = new ArrayList();
		int n = 0;
		//logger.warn("contenido size " +contenido.size());
		
		for (int i=0; i<contenido.size(); i+=3){
			IItem datos = new GenericItem(properties, "ID");
			
			for (int j=0; j<titulos.size();j++){
				n = j+i;
				//logger.warn("j= " + j + "," + "i= " + i + "," + n);
				if (n<contenido.size())
					datos.set(columns[j], contenido.get(n));	
				
			}
			tabla.add(datos);					
		}		
	
		table.setResults(new ListCollection(tabla));
		//logger.warn("Tabla rellenada correctamente.");
		return table;
	}
	public static TemplateTableInfo formateaTabla(List titulos, List contenido) throws ISPACException {
		//logger.warn("Ejecutando metodo formateaTabla");
		
		TemplateTableInfo table = null;
		String[] titleColumns = new String[titulos.size()];
		String[] columns = new String[titulos.size()];
		
		for (int i=0; i<titulos.size(); i++){
			titleColumns[i] = (String)titulos.get(i);
			columns[i] = (String)titulos.get(i);
		}
		
		table = new TemplateTableInfo("Listado de Fincas", columns, titleColumns);

		Properties properties = getProperties(titulos);
		List fila=null;
		List tabla = new ArrayList();
		
		for (int i=0; i<contenido.size(); i++){
			fila = new ArrayList();
			fila = (List)contenido.get(i);
			IItem datos = new GenericItem(properties, "ID");
			for (int j=0; j<titulos.size();j++){
				datos.set(columns[j], (String)fila.get(j));
			}
			tabla.add(datos);			
		}
		table.setResults(new ListCollection(tabla));
		//logger.warn("Tabla rellenada correctamente.");
		return table;
	}
	
	
	private static Properties getProperties(List titulos)  {
		int ordinal = 0;
	    Properties properties = new Properties();
	    
	    properties.add(new Property(ordinal++, "ID", 6));
	    for (int i=0; i<titulos.size(); i++){
	    	String columna = (String) titulos.get(i);
	    	properties.add(new Property(ordinal++, columna, 6));
	    }	 
	    return properties;
	}
	
}
