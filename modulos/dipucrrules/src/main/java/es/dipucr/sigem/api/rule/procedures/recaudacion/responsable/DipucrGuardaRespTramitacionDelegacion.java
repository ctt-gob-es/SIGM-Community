package es.dipucr.sigem.api.rule.procedures.recaudacion.responsable;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class DipucrGuardaRespTramitacionDelegacion implements IRule{
	
	public static final Logger logger = Logger.getLogger(DipucrGuardaRespTramitacionDelegacion.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			String responsable ="";
			
			IItemCollection fase = entitiesAPI.getEntities("SPAC_FASES", numexp);
			Iterator faseIt = fase.iterator();
			
			if(faseIt.hasNext()){
				IItem faseItem = (IItem)faseIt.next();
				responsable = faseItem.getString("ID_RESP");
			}
			
			IItemCollection resp_fase = entitiesAPI.getEntities("RESP_TRAMITACION", numexp);
			Iterator resp_iterator = resp_fase.iterator();
			if(resp_iterator.hasNext()){
				IItem delegacion = (IItem) resp_iterator.next();
				delegacion.set("RESPONSABLE", responsable);
				delegacion.store(cct);
			}
			else{
				if(responsable!= null && !responsable.equals("")){
					IItem delegacion = entitiesAPI.createEntity("RESP_TRAMITACION", numexp);
					delegacion.set("RESPONSABLE", responsable);
					delegacion.store(cct);
				}
			}
		}
		catch (ISPACException e) {
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		catch(Exception e){
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
				
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}