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

public class DipucrAlmacenaIdPropuesta implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrAlmacenaIdPropuesta.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			logger.info("INICIO - " + this.getClass().getName());
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			
			int taskId = rulectx.getTaskId();
			
			IItemCollection documentoCollection = entitiesAPI.getDocuments(numexp, " ID_TRAMITE='" + taskId + "' AND FAPROBACION IS NOT NULL", "ID DESC");
			Iterator documentoIterator = documentoCollection.iterator();
			if(documentoIterator.hasNext()){
				IItem documento_propuesta = (IItem) documentoIterator.next();
				
				int id_documento = documento_propuesta.getInt("ID");
			
				IItem  id_doc = entitiesAPI.createEntity("DPCR_ID_PROPUESTA", numexp);
				id_doc.set("ID_DOC", id_documento);
				id_doc.store(cct);
			}
			
		}
		catch(ISPACRuleException e){
			logger.error("Error al almacenar el id de la propuesta, " +e.getMessage(), e);
			throw new ISPACRuleException("Error al almacenar el id de la propuesta, " +e.getMessage(), e);
		} catch (ISPACInfo e) {
			logger.error("Error al almacenar el id de la propuesta, " +e.getMessage(), e);
			throw new ISPACRuleException("Error al almacenar el id de la propuesta, " +e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al almacenar el id de la propuesta, " +e.getMessage(), e);
			throw new ISPACRuleException("Error al almacenar el id de la propuesta, " +e.getMessage(), e);
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
