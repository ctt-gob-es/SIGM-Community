package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

/**
 * Ticket #603
 * @author Felipe-ecenpri
 * @since 14.05.2012
 */
public class PonerPlazoVencidoRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(PonerPlazoVencidoRule.class);

	//Número de días para el plazo. Variable para heredar
	protected int numDias = -1;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
		return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
	  	    IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
	  	    Date now = new Date();
	  	    Date dFechaAlarma = FechasUtil.addDias(now, numDias);
	  	    
	  	    //La ponemos en la tabla SPAC_DT_TRAMITES para que se visualice
			IItem itemDtTramite = entitiesAPI.getTask(rulectx.getTaskId());
			itemDtTramite.set("FECHA_LIMITE", dFechaAlarma);
			itemDtTramite.store(cct);
			
			//La ponemos en la tabla SPAC_TRAMITES para que la tenga en cuenta el publicador
			String strQuery = "WHERE ID = " + rulectx.getTaskId();
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
	        IItem itemTramite = (IItem)collection.iterator().next();
	        itemTramite.set("FECHA_LIMITE", dFechaAlarma);
			itemTramite.store(cct);
    	}
    	catch (Exception e) {
			logger.error("Error al poner fecha límite a la factura. Expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al poner fecha límite a la factura. Expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }
}
