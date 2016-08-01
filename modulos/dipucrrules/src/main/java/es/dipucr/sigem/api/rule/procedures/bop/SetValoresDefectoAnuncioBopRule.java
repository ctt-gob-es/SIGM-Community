package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Felipe #838]
 * @author Felipe
 * @since 12.02.13
 * Nos solicitan desde el BOP que el estado del anuncio sea siempre por defecto "Aceptado"
 * Creamos un método genérico por si en futuro nos solicitan poner por defecto algún campo más 
 */
public class SetValoresDefectoAnuncioBopRule implements IRule {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(SetValoresDefectoAnuncioBopRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
			// Abrir transacción
	        cct.beginTX();
	        
	        String strQuery = null;
	        IItemCollection collection = null;
	        IItem itemAnuncio = null;
	        String numexp = null;
	        
	        //Obtenemos el número de expediente
	        numexp = cct.getStateContext().getNumexp();
			
	        strQuery = "WHERE NUMEXP = '"+ numexp +"'";
	        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
	        itemAnuncio = (IItem) collection.iterator().next();
	        
	        //Seteamos los valores por defecto, de momento sólo el estado
	        itemAnuncio.set("ESTADO", "Aceptado");
	        itemAnuncio.store(cct);
	        
	        return new Boolean(true);
	        
	    } catch (Exception e) {
	    	
			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				logger.error(e1.getMessage(), e1);
			}
	    	
	        throw new ISPACRuleException("Error al cargar los valores por defecto del anuncio.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
