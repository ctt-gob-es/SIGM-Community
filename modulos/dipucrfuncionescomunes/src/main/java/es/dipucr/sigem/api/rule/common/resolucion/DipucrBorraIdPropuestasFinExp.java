package es.dipucr.sigem.api.rule.common.resolucion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class DipucrBorraIdPropuestasFinExp implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrBorraIdPropuestasFinExp.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			logger.info("INICIO - " + this.getClass().getName());
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			
			IItemCollection idsPropCollection = entitiesAPI.getEntities("DPCR_ID_PROPUESTA", numexp);
			Iterator<?> idsPropIterator = idsPropCollection.iterator();
			while(idsPropIterator.hasNext()){
				IItem idProp = (IItem) idsPropIterator.next();
				idProp.delete(cct);
			}
			
		}
		catch(ISPACRuleException e){
			logger.error("Error al borrar los id's de las propuestas, " +e.getMessage(), e);
			throw new ISPACRuleException("Error al borrar los id's de las propuestas, " +e.getMessage(), e);
		} catch (ISPACInfo e) {
			logger.error("Error al borrar los id's de las propuestas, " +e.getMessage(), e);
			throw new ISPACRuleException("Error al borrar los id's de las propuestas, " +e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al borrar los id's de las propuestas, " +e.getMessage(), e);
			throw new ISPACRuleException("Error al borrar los id's de las propuestas, " +e.getMessage(), e);
		}
		logger.info("FIN - " + this.getClass().getName());
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}
