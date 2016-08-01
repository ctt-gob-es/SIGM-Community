package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

/**
 * Ticket #34 - Controlar trámite duplicado
 * @author Felipe-ecenpri
 * @since 08.06.2010
 */
public class ControlarTramiteDuplicadoRule implements IRule {
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
    	try{    		
    		//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	    	
	        boolean bCrearTramite = true;
	        
	        IItem tramiteActual = invesFlowAPI.getTask(rulectx.getTaskId());
			String nombreTramiteActual = tramiteActual.getString("NOMBRE");
			IItem tramite = null;
			String nombre = null;
			int contador = 0;
	        
	        //Obtenemos la lista de trámites del expediente
	        IItemCollection itemCollection = entitiesAPI.getStageTasks(rulectx.getNumExp(), rulectx.getStageProcedureId());
	        Iterator it = itemCollection.iterator();
			
			while (it.hasNext() && bCrearTramite){
				tramite = (IItem)it.next();
				nombre = tramite.getString("NOMBRE");
				if (nombre.equals(nombreTramiteActual)){
					contador++;
					//Controlamos que no aparezca dos veces: tramite actual más el ya existente
					if (contador >= 2){
						bCrearTramite = false;
						rulectx.setInfoMessage("No se puede crear el trámite \""+
								nombre
								+"\". Ya existe un tramite de este tipo asociado a la fase actual.");
					}
				}
			}
			
			return bCrearTramite;
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar el número de trámites de la fase actual", e);
	    } 
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {        
    	return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }

}
