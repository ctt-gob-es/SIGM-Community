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

import aww.sigem.expropiaciones.util.ConsignaTransferenciaUtil;
import aww.sigem.expropiaciones.util.FuncionesUtil;
import aww.sigem.expropiaciones.util.PropietariosUtil;
import aww.sigem.expropiaciones.util.TablaUtilSigem;

/**
 * Tag que muestra una tabla con un listado de Fincas por expropiado
 */
public class ListaFincasDecretoTransferenciaPropietarioTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ListaFincasDecretoTransferenciaPropietarioTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla ListaFincasDecretoTransferenciaPropietarioTagRule");
						
			//Genera los encabezados de la tabla
			List titulos = new ArrayList();
			
			titulos.add("Propietario");
			titulos.add("Polígono");
			titulos.add("Parcela");		
			titulos.add("Municipio");
			titulos.add("Importe en Euros");			
			 
			
			//Obtener las fincas relacionadas con el propietario
			
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			
			//String numExp = rulectx.getNumExp();
			// Obtener el num del expediente.
			String numExp = cct.getSsVariable("NUMEXP");
					
			//logger.warn("Expediente de Expropiado: " + numExp);
			
			//Obtiene los números de expediente de las fincas
			String listado = "";
			String strQuery = "WHERE NUMEXP_HIJO = '" + numExp + "' AND RELACION = 'Expropiado/Finca'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator it = collection.iterator();
			IItem item = null;
			List expFincas = new ArrayList();
			
						
			while (it.hasNext()) {
			   item = (IItem)it.next();
			   expFincas.add(item.getString("NUMEXP_PADRE"));
			   //logger.warn("Expediente de Finca: " + item.getString("NUMEXP_PADRE"));			
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
			
			//IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);
			
			String clausulas = "ORDER BY MUNICIPIO, CASE WHEN NUM_POLIGONO < 'A' THEN LPAD(NUM_POLIGONO, 255, '0') ELSE NUM_POLIGONO END, " +
			"CASE WHEN NUM_PARCELA < 'A' THEN LPAD(NUM_PARCELA, 255, '0') ELSE NUM_PARCELA END";
			
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + clausulas);
			
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
				item = (IItem)itFincas.next();
				List listaExpedientesPropietarios = ConsignaTransferenciaUtil.listaExpedientesPropietariosTransferencia(entitiesAPI,item.getString("NUMEXP"));
				Iterator itPropietarios = listaExpedientesPropietarios.iterator();
				
				while(itPropietarios.hasNext()){					
					fila = new ArrayList();
					String expPropietario =  (String)itPropietarios.next();	
				
					IItem propietario = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", "WHERE NUMEXP ='"+expPropietario+"'").value();
					fila.add(PropietariosUtil.datosExpropiado(entitiesAPI, item.getString("NUMEXP"),propietario));
					fila.add(item.getString("NUM_POLIGONO"));
					fila.add(item.getString("NUM_PARCELA"));
					fila.add(item.getString("MUNICIPIO"));					
					fila.add(FuncionesUtil.imprimirDecimales(ConsignaTransferenciaUtil.cantidadPagoPropietarioFinca(entitiesAPI, item.getString("NUMEXP"), expPropietario)));
					
					//Si hay algun nulo reemplazarlo por cadena vacia
					for (int i=0;i<fila.size();i++){
						if (fila.get(i) == null) {
							fila.set(i, "");
						}
					}				
					contenido.add(fila);
				}
					
									
				
			}			
			return TablaUtilSigem.formateaTabla(titulos,contenido);
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	

}
