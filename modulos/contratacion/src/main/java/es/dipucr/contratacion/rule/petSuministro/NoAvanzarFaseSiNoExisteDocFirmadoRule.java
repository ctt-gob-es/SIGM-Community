package es.dipucr.contratacion.rule.petSuministro;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * @author teresa
 * @date 17/03/2010
 * @propósito No avanza fase si no existe el documento Acta de Recepción
 * 
 */
public class NoAvanzarFaseSiNoExisteDocFirmadoRule implements IRule{
	
	protected static final Logger logger = Logger.getLogger(NoAvanzarFaseSiNoExisteDocFirmadoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			logger.warn("INICIO - "+this.getClass().getName());
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			
			int idFase = rulectx.getStageId();
			String strQuery = "NUMEXP = '" + rulectx.getNumExp() + "' AND ID_FASE="+idFase;
			logger.warn("strQuery - "+strQuery);
        	IItemCollection docCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), strQuery, "FDOC DESC");
        	Iterator<IItem> docIterator = docCollection.iterator();
        	if(!docIterator.hasNext()){
        		rulectx.setInfoMessage("No se puede avanzar fase hasta que no haya un Acta de Recepción.");
        		return false;
        	} 
        	else{
        		return true;
        	}
		}
		catch (Exception e) 
		{
	        throw new ISPACRuleException("Error al comprobar el estado de la fase de Peticion de suministro", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
