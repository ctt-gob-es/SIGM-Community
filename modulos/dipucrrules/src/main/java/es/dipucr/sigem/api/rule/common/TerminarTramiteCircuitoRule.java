package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import es.dipucr.sigem.api.rule.common.utils.BloqueosFirmaUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

/**
 * Ticket #504 - Terminar el trámite del documento del circuito
 * @author Felipe-ecenpri
 * @since 19.03.2012
 */
public class TerminarTramiteCircuitoRule implements IRule {
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
    	int idDoc = Integer.MIN_VALUE;
    	int idTramite = Integer.MIN_VALUE;
    	
    	try{    		
    		//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        idDoc = rulectx.getInt("ID_DOCUMENTO");
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			idTramite = itemDocumento.getInt("ID_TRAMITE");
			
			BloqueosFirmaUtil.cerrarSesionYEliminarBloqueos(cct, idTramite); //[dipucr-Felipe #1115]
			TramitesUtil.cerrarTramite(idTramite, rulectx);
			
			return true;
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al cerrar el trámite del circuito. " +
	        		"IdDoc: " + idDoc + " IdTramite:" + idTramite, e);
	    } 
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }

}
