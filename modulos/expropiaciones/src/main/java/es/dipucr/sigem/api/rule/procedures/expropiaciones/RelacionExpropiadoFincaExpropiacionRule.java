package es.dipucr.sigem.api.rule.procedures.expropiaciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class RelacionExpropiadoFincaExpropiacionRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(RelacionExpropiadoFincaExpropiacionRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try
    	{
    		logger.info("INICIO - " + this.getClass().getName());
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection collection = entitiesAPI.queryEntities("EXPR_RELACION_FINC_EXP", strQuery);
			Iterator<IItem> it = collection.iterator();
			if (it.hasNext()) {
				IItem iDatos = (IItem) it.next();
				String numexpFinca = iDatos.getString("NUMEXFINCA");
				String numexpExp = iDatos.getString("NUMEXEXPROPIACION");
				
				if(!StringUtils.isEmpty(numexpFinca) && numexpFinca != null){
					IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
					registro.set("NUMEXP_PADRE", numexpFinca);
					registro.set("NUMEXP_HIJO", rulectx.getNumExp());
					registro.set("RELACION", "Expropiado/Finca");
					registro.store(cct);
				}
				
				if(!StringUtils.isEmpty(numexpExp) && numexpExp != null){
					IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
					registro.set("NUMEXP_PADRE", numexpExp);
					registro.set("NUMEXP_HIJO", rulectx.getNumExp());
					registro.set("RELACION", "Expropiado/Expropiacion");
					registro.store(cct);
				}
				
			}
	
			
    	}catch (Exception e) {
			logger.error("Error al relacionar el expropiado con la finca/expropiación. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al relacionar el expropiado con la finca/expropiación. " + e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
