package aww.sigem.expropiaciones.util;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.util.ListCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;



public class PropietariosUtil {
	
	private static final Logger logger = 
		Logger.getLogger(PropietariosUtil.class);	

private static IItemCollection getExpropiados(IEntitiesAPI entitiesAPI, String numExp, String relacion) throws Exception{
	//Buscar los expedientes de los Expropiados
	//logger.warn("Expediente de Finca: " + numExp);
	String strQuery = "WHERE NUMEXP_PADRE = '" + numExp + "' AND RELACION = '" +relacion+ "'";
	IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
	Iterator it = collection.iterator();
	IItem item = null;	
	
	List expExpropiados = new ArrayList();		
	
	while (it.hasNext()) {
	   item = (IItem)it.next();
	   expExpropiados.add(item.getString("NUMEXP_HIJO"));
	   //logger.warn("Expediente de Expropiado: " + item.getString("NUMEXP_HIJO"));			
	}
	
	if(expExpropiados.isEmpty()){
		return new ListCollection(new ArrayList());
	}
	
	//Obtiene los datos de los expropiados
	Iterator itExpExpropiados = expExpropiados.iterator();
	strQuery="WHERE NUMEXP = '" + itExpExpropiados.next() + "'";
	while (itExpExpropiados.hasNext()) {
		strQuery+=" OR NUMEXP = '" + itExpExpropiados.next() + "'";				 		
	}			
	
	//logger.warn("Expropiados a buscar: " + strQuery );	
	
	return entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery+ "ORDER BY NOMBRE ASC");
	
}

public static double getMetrosPorPropietario(IEntitiesAPI entitiesAPI, String numExpFinca, String numExpExpropiado) throws Exception{
	
	double totalSupExpropiada = 0;
	double metrosExpropiado = 0;
	
	IItemCollection collectionExpropiados = getExpropiados(entitiesAPI, numExpFinca, "Expropiado/Finca");
	// Consulta que debe devolver el porcentaje para el propietario.
	String strQueryPor = "WHERE NUMEXP = '" + numExpFinca + "' AND NUMEXP_EXPROPIADO = '" + numExpExpropiado + "'";
	IItem itemPropietario = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryPor).value();
	
	// Consulta que debe devolver la finca cuyo expediente sea numExpFinca
	String strQueryFinca = "WHERE NUMEXP = '" + numExpFinca + "'";
	IItem itemFinca = entitiesAPI.queryEntities("EXPR_FINCAS", strQueryFinca).value();
	
	if (itemFinca != null){
		totalSupExpropiada = itemFinca.getDouble("SUP_EXPROPIADA");
		//logger.warn("Extraida totalSupExpropiada= " + totalSupExpropiada);
				
		String superficie = totalSupExpropiada+"";										
		// Si contiene E-, no hay un valor inicial en la base de datos.
		if (superficie.indexOf("E-") != -1){
				totalSupExpropiada = -1;		
		}
	}
	
	if (itemPropietario != null){
		String porcentaje = itemPropietario.getString("PORCENTAJE_PROP").replace(',', '.');		
		
		if (porcentaje != null && !porcentaje.equals("") && totalSupExpropiada>0){
			metrosExpropiado = Double.parseDouble(porcentaje) * totalSupExpropiada * 0.01;
			
			metrosExpropiado = FuncionesUtil.redondeoDecimales(metrosExpropiado);
			//logger.warn("Metodo getMetrosPorPropietario -> metrosExpropiado " + metrosExpropiado);
		}
	}
	
	return metrosExpropiado;	
}

@SuppressWarnings("unchecked")
public static String datosExpropiado(IEntitiesAPI entitiesAPI, String numExpFinca, IItem itemExp) throws Exception{

	// Consulta que debe devolver tantos porcentajes como propietarios haya, ya que hay una entrada para cada uno.
	String strQueryPor = "WHERE NUMEXP = '" + numExpFinca + "' AND NUMEXP_EXPROPIADO = '"+ itemExp.getString("NUMEXP") +"'";
	logger.warn("strQueryPor. "+strQueryPor);
	
	String porcentaje = "Desconocido";
	String expropiado = "";	
	IItemCollection exproPago = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryPor);
	
	Iterator<IItem> itExproPago = exproPago.iterator();	
	double metrosExpropiado = 0.0;
	while(itExproPago.hasNext()){
		IItem itemP = itExproPago.next();
		metrosExpropiado = getMetrosPorPropietario(entitiesAPI, numExpFinca, itemExp.getString("NUMEXP"));
		
		if (itemP != null){
			porcentaje = itemP.getString("PORCENTAJE_PROP").replace(',', '.');
		}else{
			porcentaje = "Desconocido";
		}
	}
	
	
	
	expropiado+=datosPropietarioFinca(entitiesAPI, itemExp.getString("NUMEXP"));
	
	expropiado+=" ";		
	
	if (!porcentaje.equals("100.00")){
		expropiado+="Porcentaje de propiedad: ";
		expropiado+=porcentaje;
		
		expropiado+=" ";	
		expropiado+="Metros a ocupar: ";
		
		if (metrosExpropiado != 0 && metrosExpropiado>0)
			expropiado+=metrosExpropiado;
		else
			expropiado += "Desconocido";				
	}
	
	return expropiado;
}

public static String propietariosFinca(IEntitiesAPI entitiesAPI, String numExpFinca) throws Exception{		
		
		IItemCollection collectionExpropiados = getExpropiados(entitiesAPI, numExpFinca, "Expropiado/Finca");
		// Consulta que debe devolver tantos porcentajes como propietarios haya, ya que hay una entrada para cada uno.
		String strQueryPor = "WHERE NUMEXP = '" + numExpFinca + "'";
		IItemCollection collectionPorcentajes = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryPor);	
	
		//Si la lista de expropiados está vacía devolver Desconocido
		if(collectionExpropiados.toList().isEmpty()){
			return "Desconocido";
		}
		
		Iterator itExpropiados = collectionExpropiados.iterator();
		
		IItem item = null;		
		
		String expropiados = "";	
		
		while (itExpropiados.hasNext()) {			 
			item = (IItem)itExpropiados.next();
			
			expropiados += datosExpropiado(entitiesAPI, numExpFinca, item);
						
			if(itExpropiados.hasNext()) {
				expropiados+="\n";	
				expropiados+=" --- ";
				expropiados+="\n";								
			}			
		}			
		
		//Si no se encuentra, es desconocido
		return expropiados;
		
	}

//[eCenpri-Manu Ticket #276] + ALSIGM3 Etiquetas Expropiaciones
public static List propietariosExpropiacion(IEntitiesAPI entitiesAPI, String numExpropiacion) throws Exception{
	
	IItemCollection collectionExpropiados = getExpropiados(entitiesAPI, numExpropiacion, "Expropiado/Expropiacion");
	
	Iterator itExpropiados = collectionExpropiados.iterator();
	
	List formato =  new ArrayList();	
	String expropiados = "";
	IItem item=null;
	int cabeceras = 0;
	
	while (itExpropiados.hasNext()) {			 
		item = (IItem)itExpropiados.next();
			
		int mod = (formato.size() - cabeceras - 9 ) % 33;
		if(mod == 0 || mod == 1 || mod ==  2 || mod == 18 || mod == 19 || mod == 20 || mod == 21 || mod == 22 || mod == 23){
			expropiados+='\n';				
		}
		expropiados+='\n';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=item.getString("NOMBRE");
		
		//Datos completos de los expropiados			
		expropiados+='\n';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=item.getString("DIRNOT");
		expropiados+='\n';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=item.getString("LOCALIDAD");
		expropiados+='\n';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+=' ';
		expropiados+="(";
		expropiados+=item.getString("C_POSTAL");
		expropiados+=") ";
		if (item.getString("CAUT") != null){
			expropiados+=item.getString("CAUT");
		}
		
		expropiados+=" ";
		
		expropiados+='\n';				

		if(mod != 21 && mod != 22 && mod != 23){
			expropiados+='\n';				
		}		
		
		formato.add(expropiados);
		expropiados = "";
				
		if((formato.size() - cabeceras) % 33 == 0){
			formato.add("Col1\n");
			formato.add("Col2\n");
			formato.add("Col3\n");
			cabeceras += 3;
		}
	}			
	
	return formato;	
}	

	//Obtiene los datos del propietario de la finca sin datos de propiedad.
	static public String datosPropietarioFinca(IEntitiesAPI entitiesAPI, String numExpPropietario) throws ISPACException{
		String strQuery = "WHERE NUMEXP = '" + numExpPropietario + "'";
		IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
		Iterator it = collection.iterator();
		IItem item = null;	
		String datos = "";		
		if (it.hasNext()) {
		   item = (IItem)it.next();
		   datos+=item.getString("NOMBRE");			
		  
		   datos+=" ";
		   datos+=item.getString("DIRNOT");
		   datos+=" ";
		   datos+=item.getString("LOCALIDAD");
		   datos+=" (";
		   datos+=item.getString("C_POSTAL");
		   datos+=") ";
		   if (item.getString("CAUT") != null){
			   datos+=item.getString("CAUT");
		   }		   
	
		   return datos;		   
		} else {
		   return "Desconocido";				
		}			
		
	}
	
	static public String datosPropietarioDecreto(IEntitiesAPI entitiesAPI, String numExpPropietario) throws ISPACException{
		String strQuery = "WHERE NUMEXP = '" + numExpPropietario + "'";
		IItem item = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery).value();
		
		String datos = "";		
		if (item != null) {
		  
		  datos += datosPropietarioFinca(entitiesAPI, numExpPropietario);
		  datos += "\nNIF: " + item.getString("NDOC");
	
		  return datos;		   
		} else {
		   return "Desconocido";				
		}			
		
	}


}
