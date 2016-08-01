package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class NoIniciaTramiteSiExistenTramitesAbiertosRule implements IRule {
	protected static final Logger logger = Logger.getLogger(NoIniciaTramiteSiExistenTramitesAbiertosRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cerrar = true;
		try{
        	//----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();	        
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();			
            //----------------------------------------------------------------------------------------------
            
            String sql = "WHERE NUMEXP='" + rulectx.getNumExp()+"' AND FECHA_CIERRE IS NULL";
			IItemCollection tramiteAbierto = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, sql);
			Iterator<IItem> itTramites = tramiteAbierto.iterator();
	        if (tramiteAbierto.toList().size()>1){
	        	cerrar = false;
	        	rulectx.setInfoMessage("Existen trámites abiertos.");
	        }
		} catch (Exception e) {
	        throw new ISPACRuleException("Error obteniendo el tag. ", e);
	    }   
		return cerrar;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
