package ieci.tdw.ispac.api.rule.procedures.sancionador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

/**
 * 
 * @author teresa
 * @date 24/03/2010
 * @propósito Inicializa la entidad de transiciones al iniciar el expediente
 */
public class InitStatusSancionadorRule implements IRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
	        
	        String numExp = rulectx.getNumExp();
	        IItem item = null;
	        
	        //Inicializar el campo Transición del expediente
	        item = entitiesAPI.createEntity("URB_SANCIONADOR",numExp);
        	item.set("TRANSICION_ANTERIOR", "SAN00-SAN00_C");
        	item.set("TRANSICION", "SAN00-SAN00_C");
	        item.store(rulectx.getClientContext());
	        
	        //Inicializar la entidad de transiciones
	        
	        // Transiciones entre trámites
	        
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN00");
            item.set("FIN", "SAN01");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN01");
            item.set("FIN", "SAN02");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN02");
            item.set("FIN", "SAN04");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN04");
            item.set("FIN", "SAN05");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN05");
            item.set("FIN", "SAN04");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN05");
            item.set("FIN", "SAN06");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN05");
            item.set("FIN", "SAN08");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN06");
            item.set("FIN", "SAN07");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN07");
            item.set("FIN", "SAN08");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN08");
            item.set("FIN", "SAN09");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN08");
            item.set("FIN", "SAN10");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN09");
            item.set("FIN", "SAN08");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN09");
            item.set("FIN", "SAN12");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN12");
            item.set("FIN", "SAN13");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN12");
            item.set("FIN", "SAN17");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN12");
            item.set("FIN", "SAN13");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN13");
            item.set("FIN", "SAN14");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN14");
            item.set("FIN", "SAN15");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN15");
            item.set("FIN", "SAN16");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN15");
            item.set("FIN", "SAN17");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN16");
            item.set("FIN", "SAN12");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN17");
            item.set("FIN", "SAN19");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN17");
            item.set("FIN", "SAN20");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN19");
            item.set("FIN", "SAN20");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN20");
            item.set("FIN", "SAN21");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN20");
            item.set("FIN", "SAN23");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN21");
            item.set("FIN", "SAN22");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN23");
            item.set("FIN", "SAN24");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN23");
            item.set("FIN", "SAN25");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES", numExp);
            item.set("INICIO", "SAN24");
            item.set("FIN", "SAN23");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
	        
	        
	        // Transiciones entre fases
	        
	        item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
        	item.set("INICIO", "SAN09");
        	item.set("FIN", "Fase Instrucción");
        	item.set("EJECUCIONES", 0);
	        item.store(rulectx.getClientContext());
	        
	        item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
        	item.set("INICIO", "SAN17");
        	item.set("FIN", "Fase Resolución");
        	item.set("EJECUCIONES", 0);
	        item.store(rulectx.getClientContext());
	        
	        item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
        	item.set("INICIO", "SAN19");
        	item.set("FIN", "Fase Resolución");
        	item.set("EJECUCIONES", 0);
	        item.store(rulectx.getClientContext());
	        
	        item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
        	item.set("INICIO", "SAN20");
        	item.set("FIN", "Fase Ejecución");
        	item.set("EJECUCIONES", 0);
	        item.store(rulectx.getClientContext());
	        
	        item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
        	item.set("INICIO", "SAN22");
        	item.set("FIN", "Fase Ejecución");
        	item.set("EJECUCIONES", 0);
	        item.store(rulectx.getClientContext());
	        
	        item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
        	item.set("INICIO", "SAN23");
        	item.set("FIN", "Fase Ejecución");
        	item.set("EJECUCIONES", 0);
	        item.store(rulectx.getClientContext());
	        
	        item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
        	item.set("INICIO", "SAN25");
        	item.set("FIN", "Fase Ejecución");
        	item.set("EJECUCIONES", 0);
	        item.store(rulectx.getClientContext());
	        
	        
	        
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}

		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}