package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

/**
 * 
 * @author teresa
 * @date 30/03/2010
 * @propósito Inicializa la entidad de transiciones al iniciar el expediente
 */
public class InitStatusPlaneamientoRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {

        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
    
            
            String numExp = rulectx.getNumExp();
            IItem item = null;
            
            //Inicializar el campo Transición del expediente
            item = entitiesAPI.createEntity("PLAN_POM",numExp);
            item.set("TRANSICION_ANTERIOR", "PLA000-PLA000_C");
            item.set("TRANSICION", "PLA000-PLA000_C");
            item.store(rulectx.getClientContext());
            
            //Inicializar la entidad de transiciones
            
            // Transiciones entre trámites
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA000");
            item.set("FIN", "PLA010");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            /* Los estados 010 y 020 los tratamos como uno único ya que se trata del mismo trámite (Providencia)
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA000");
            item.set("FIN", "PLA020");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
             */
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA010");
            item.set("FIN", "PLA030");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA010");
            item.set("FIN", "PLA041");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA010");
            item.set("FIN", "PLA042");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA041");
            item.set("FIN", "PLA042");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA030");
            item.set("FIN", "PLA050");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA042");
            item.set("FIN", "PLA050");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());

            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA050");
            item.set("FIN", "PLA061");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA061");
            item.set("FIN", "PLA062");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());

            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA061");
            item.set("FIN", "PLA070");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA061");
            item.set("FIN", "PLA110");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA062");
            item.set("FIN", "PLA070");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA062");
            item.set("FIN", "PLA110");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA070");
            item.set("FIN", "PLA080");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA080");
            item.set("FIN", "PLA090");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA090");
            item.set("FIN", "PLA100");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA090");
            item.set("FIN", "PLA110");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA100");
            item.set("FIN", "PLA110");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA110");
            item.set("FIN", "PLA120");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA120");
            item.set("FIN", "PLA130");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA130");
            item.set("FIN", "PLA141");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA130");
            item.set("FIN", "PLA150");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA141");
            item.set("FIN", "PLA142");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA141");
            item.set("FIN", "PLA160");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA141");
            item.set("FIN", "PLA190");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA142");
            item.set("FIN", "PLA160");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA142");
            item.set("FIN", "PLA190");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA150");
            item.set("FIN", "PLA160");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA150");
            item.set("FIN", "PLA190");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA160");
            item.set("FIN", "PLA170");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA170");
            item.set("FIN", "PLA180");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA180");
            item.set("FIN", "PLA190");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA190");
            item.set("FIN", "PLA200");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            /* Los estados 200 y 210 los tratamos como uno único ya que se trata del mismo trámite (Providencia)
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA190");
            item.set("FIN", "PLA210");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            */
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA200");
            item.set("FIN", "PLA141");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA200");
            item.set("FIN", "PLA150");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            //item.set("INICIO", "PLA210"); //Los estados 200 y 210 los tratamos como uno único ya que se trata del mismo trámite (Providencia)
            item.set("INICIO", "PLA200");
            item.set("FIN", "PLA220");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA220");
            item.set("FIN", "PLA241");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA241");
            item.set("FIN", "PLA242");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA242");
            item.set("FIN", "PLA141");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA242");
            item.set("FIN", "PLA150");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA242");
            item.set("FIN", "PLA250");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            

            
            // Transiciones entre fases
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA030");
            item.set("FIN", "Fase Inicio");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA042");
            item.set("FIN", "Fase Inicio");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA130");
            item.set("FIN", "Aprobación inicial");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA242");
            item.set("FIN", "Información pública y Resolución");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            item = entitiesAPI.createEntity("TSOL_TRANSICIONES",numExp);
            item.set("INICIO", "PLA250");
            item.set("FIN", "Información pública y Resolución");
            item.set("EJECUCIONES", 0);
            item.store(rulectx.getClientContext());
            
            
    
            
            
            
        }catch(ISPACException e){
            throw new ISPACRuleException(e);
        }

        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

}
