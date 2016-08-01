package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.GenerateNotificacion;

public class GenerateNotifRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(GenerateNotifRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	
    	/**
    	 * 1º 
    	 * 2º la relacion de los participantes
    	 * 3º el nombre del tramite donde se inserta el texto
    	 * 4º nombre del tramite donde se genera las notificaciones
    	 * **/
    	
		try {
			IClientContext cct = rulectx.getClientContext();
	    	IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
	    	
	    	//Compruebo que tenga anexos este expediente y si tiene que se ejecute la regla y si no tiene que
			//no haga nada
			String sqlQuery = "NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo'";
			IItemCollection documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");
			Iterator<IItem> iDocAnexar = documentosAnexar.iterator();
			
			if(!iDocAnexar.hasNext()){
				GenerateNotificacion.GenerarNotificaciones(rulectx, "TRAS", "Plantilla Carta digital", "Carta digital");
			}
			
		} catch (ISPACException e) {
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
    	
    	return new Boolean(true);
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
		
	}

}
