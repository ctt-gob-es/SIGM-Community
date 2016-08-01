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

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class TotalDecretoTransferenciaTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(TotalDecretoTransferenciaTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla TotalDecretoTransferenciaTagRule");
			
			//Obtener las fincas relacionadas con la Expropiacion
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			//Obtiene los números de expediente de las fincas
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
			
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);
			
			//Si la lista de fincas está vacía no dibujar la tabla
			if(collectionFincas.toList().isEmpty()){
				return "La lista de fincas a expropiar está vacía\n";
			}
			
			Iterator itFincas = collectionFincas.iterator();			
			
			double total = 0.0;
			
			while (itFincas.hasNext()) {
				item = (IItem)itFincas.next();
				List listaExpedientesPropietarios = ConsignaTransferenciaUtil.listaExpedientesPropietariosTransferencia(entitiesAPI,item.getString("NUMEXP"));
				Iterator itPropietarios = listaExpedientesPropietarios.iterator();
				
				while(itPropietarios.hasNext()){					
					String expPropietario =  (String)itPropietarios.next();					
					double valor = ConsignaTransferenciaUtil.cantidadPagoPropietarioFinca(entitiesAPI, item.getString("NUMEXP"), expPropietario);
					total += valor;					
				}									
			}		
			
			//logger.warn("Total:" + total);			
			//logger.warn("Fin regla TotalDecretoTransferenciaTagRule");
						
			return FuncionesUtil.imprimirDecimales(FuncionesUtil.redondeoDecimales(total));
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
