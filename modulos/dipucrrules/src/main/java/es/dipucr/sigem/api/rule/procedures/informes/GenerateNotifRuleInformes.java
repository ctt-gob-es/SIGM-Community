package es.dipucr.sigem.api.rule.procedures.informes;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.GenerateNotificacion;

public class GenerateNotifRuleInformes implements IRule {
	
	private static final Logger logger = Logger.getLogger(GenerateNotifRuleInformes.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	
    	/**
    	 * 1º 
    	 * 2º la relacion de los participantes
    	 * 3º el nombre del tramite donde se inserta el texto
    	 * 4º nombre del tramite donde se genera las notificaciones
    	 * **/
    	
		try {
			GenerateNotificacion.GenerarNotificaciones(rulectx, "TRAS", "Certificación", "Certificación administrativa");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
    	
    	return new Boolean(true);
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
		
	}

}
