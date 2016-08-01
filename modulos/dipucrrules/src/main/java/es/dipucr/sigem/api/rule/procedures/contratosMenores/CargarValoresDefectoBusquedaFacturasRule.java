package es.dipucr.sigem.api.rule.procedures.contratosMenores;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

/**
 * [dipucr-Felipe #1211]
 * @author Felipe
 * @since 18.11.2015
 */
public class CargarValoresDefectoBusquedaFacturasRule  implements IRule {
	
	public static String DEFAULT_PARTIDAS_EXCLUIDAS = "CONTA_DEFAULT_PARTIDAS_EXCLUIDAS";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		try{
			//----------------------------------------------------------------------------------------------
	        IClientContext ctx = rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = ctx.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 

	        String numexp = rulectx.getNumExp();
	        IItem itemBusquedaFacturas = entitiesAPI.createEntity("BUSQUEDA_FACTURAS", numexp);
	        
	        itemBusquedaFacturas.set("EJERCICIO", String.valueOf(FechasUtil.getAnyoActual()));
	        
	        //Cargamos las partidas exlcuidas por defecto para contratos menores
	        String sPartidasExcluidas = ConfigurationMgr.getVarGlobal(ctx, DEFAULT_PARTIDAS_EXCLUIDAS);
	        itemBusquedaFacturas.set("PARTIDAS_EXCLUIDAS", sPartidasExcluidas);

	        itemBusquedaFacturas.store(ctx);
	        
			return new Boolean(true);
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al cargar los datos por defecto de búsqueda en la pestaña 'Búsqueda Facturas'", e);
	    }
	}

		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
