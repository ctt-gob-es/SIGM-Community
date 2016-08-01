package aww.sigem.expropiaciones.rule.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.PropietariosUtil;
import aww.sigem.expropiaciones.util.TablaUtil;
import aww.sigem.expropiaciones.util.TablaUtilSigem;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class ListaFincasActaPreviaTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ListaFincasActaPreviaTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla ListaFincasActaPreviaTagRule");
			
			//Genera los encabezados de la tabla
			List titulos = new ArrayList();
			
			titulos.add("Finca Num.");
			titulos.add("Fecha # Hora");
			titulos.add("Propietario");
			titulos.add("Polígono");
			titulos.add("Parcela");
			titulos.add("Municipio de Parcela");
			titulos.add("Superficie de Parcela (m2)");
			titulos.add("Superficie a ocupar (m2)");
			titulos.add("Calificación");			
			
			
			//Obtener las fincas relacionadas con la Expropiacion
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			//Obtiene los números de expediente de las fincas
			String listado = "";
			String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator it = collection.iterator();
			IItem item = null;
			List expFincas = new ArrayList();
			
			//logger.warn("Expediente de Expropiacion: " + rulectx.getNumExp());
			
			while (it.hasNext()) {
			   item = (IItem)it.next();
			   expFincas.add(item.getString("NUMEXP_HIJO"));
			   //logger.warn("Expediente de Finca: " + item.getString("NUMEXP_HIJO"));			
			}
			
			//Si la lista de fincas está vacía no dibujar la tabla
			if(expFincas.isEmpty()){
				return "La lista de fincas a expropiar está vacía\n";
			}
			
			//Obtiene los datos de las fincas
			Iterator itExpFincas = expFincas.iterator();
			strQuery="WHERE NUMEXP = '" + itExpFincas.next() + "'";
			while (itExpFincas.hasNext()) {
				strQuery+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
			}
				
			//logger.warn("Fincas a buscar: " + strQuery);
			
			/**
			 * #[Teresa - Ticket 200] INICIO SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/
			
			//IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + "ORDER BY MUNICIPIO, NUM_POLIGONO ASC, NUM_PARCELA ASC");
			
			String clausulas = "ORDER BY LEVANTAMIENTO_FECHA, LEVANTAMIENTO_HORA ASC";
			
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + clausulas);
		
			/**
			 * #[Teresa - Ticket 200] FIN SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/
	
			//Si la lista de fincas está vacía no dibujar la tabla
			if(collectionFincas.toList().isEmpty()){
				return "La lista de fincas a expropiar está vacía\n";
			}
			
			Iterator itFincas = collectionFincas.iterator();
			
			//Genera los datos de las filas de datos			
			//Lista de listas de filas
			List contenido = new ArrayList();
			//Cada fila individual
			List fila = null;
			
			while (itFincas.hasNext()) {
				fila = new ArrayList();
				item = (IItem)itFincas.next();
				fila.add(item.getString("NUMEXP"));
				fila.add(item.getString("LEVANTAMIENTO_FECHA")+ " # " + item.getString("LEVANTAMIENTO_HORA"));
				//Código que extrae una lista de propietarios
				fila.add(PropietariosUtil.propietariosFinca(entitiesAPI,item.getString("NUMEXP")));				
				fila.add(item.getString("NUM_POLIGONO"));
				fila.add(item.getString("NUM_PARCELA"));
				fila.add(item.getString("MUNICIPIO"));
				fila.add(item.getString("SUP_PARCELA"));
				fila.add(item.getString("SUP_EXPROPIADA"));
				fila.add(item.getString("APROVECHAMIENTO"));
				
				//Si hay algun nulo reemplazarlo por cadena vacia
				//logger.warn("Contenido de la fila con null: " + fila.toString());
				for (int i=0;i<fila.size();i++){
					if (fila.get(i) == null) {
						fila.set(i, "");
					}
				}
				
				contenido.add(fila);				
				//logger.warn("Detalle de Expediente de Finca: " + item.getString("NUMEXP") + " Num Finca: " + item.getString("NUM_FINCA"));
				
			}		
					
			return TablaUtilSigem.formateaTabla(titulos, contenido);
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	
	
private String propietariosFinca(IEntitiesAPI entitiesAPI, String numExpFinca, List titulos) throws Exception{
		
		//Buscar los expedientes de los Expropiados
		//logger.warn("Expediente de Finca: " + numExpFinca);
		String strQuery = "WHERE NUMEXP_PADRE = '" + numExpFinca + "' AND RELACION = 'Expropiado/Finca'";
		IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
		Iterator it = collection.iterator();
		IItem item = null;	
		IItem itemP = null;	
		List expExpropiados = new ArrayList();		
		
		while (it.hasNext()) {
		   item = (IItem)it.next();
		   expExpropiados.add(item.getString("NUMEXP_HIJO"));
		   //logger.warn("Expediente de Expropiado: " + item.getString("NUMEXP_HIJO"));			
		}
		
		//Si la lista de expropiados está vacía devolver Desconocido
		if(expExpropiados.isEmpty()){
			return "Desconocido";
		}
		
		//Obtiene los datos de los expropiados
		Iterator itExpExpropiados = expExpropiados.iterator();
		strQuery="WHERE NUMEXP = '" + itExpExpropiados.next() + "'";
		while (itExpExpropiados.hasNext()) {
			strQuery+=" OR NUMEXP = '" + itExpExpropiados.next() + "'";				 		
		}			
		
		//logger.warn("Expropiados a buscar: " + strQuery);	
		
		IItemCollection collectionExpropiados = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
		
		
		// Consulta que debe devolver tantos porcentajes como propietarios haya, ya que hay una entrada para cada uno.
		String strQueryPor = "WHERE NUMEXP = '" + numExpFinca + "'";
		IItemCollection collectionPorcentajes = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryPor);
		
		//Si la lista de expropiados está vacía devolver Desconocido
		if(collectionExpropiados.toList().isEmpty()){
			return "Desconocido";
		}
		
		Iterator itExpropiados = collectionExpropiados.iterator();
		Iterator itPorcentajes = collectionPorcentajes.iterator();
		
		String expropiados = "";
		
		while (itExpropiados.hasNext()) {			 
			item = (IItem)itExpropiados.next();
			itemP = (IItem)itPorcentajes.next();
			expropiados+=item.getString("NOMBRE");
			//Descomentar esto para que salgan los datos completos de los expropiados
			
			expropiados+=" ";
			expropiados+=item.getString("DIRNOT");
			expropiados+=" ";
			expropiados+=item.getString("LOCALIDAD");
			expropiados+=" (";
			expropiados+=item.getString("C_POSTAL");
			expropiados+=") ";
			if (item.getString("CAUT") != null){
				expropiados+=item.getString("CAUT");
			}
			
			expropiados+=" ";
			String porcentaje = itemP.getString("PORCENTAJE_PROP");
			if (!porcentaje.equals("100,00")){
				expropiados+="Porcentaje de propiedad: ";
				expropiados+=porcentaje;
			}
			
			if(itExpropiados.hasNext()) {
				expropiados+=" ";	
				int [] anchos = TablaUtil.calcularAnchos(titulos, 160, true, 2, true);				
				
				for (int i=0; i<anchos[2]-2 ; i++) {
					expropiados+="-";	
				}
				expropiados+=" ";				
								
			}
			
		}			
		
		//Si no se encuentra, es desconocido
		return expropiados;
		
	}
}
