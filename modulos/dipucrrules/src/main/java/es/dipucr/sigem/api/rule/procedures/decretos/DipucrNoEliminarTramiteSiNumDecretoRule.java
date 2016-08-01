package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * 
 * @author teresa
 * @date 17/03/2010
 * @propósito Valida que si tiene número de decreto, de ser así, no permite eliminar el trámite
 * 
 */
public class DipucrNoEliminarTramiteSiNumDecretoRule implements IRule{
	
	protected static final Logger logger = Logger.getLogger(DipucrNoEliminarTramiteSiNumDecretoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			logger.info("INICIO - "+this.getClass().getName());
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			String numexp = rulectx.getNumExp();
	        
			//Recuperamos el número de decreto
        	IItemCollection decretoCollection = entitiesAPI.getEntities(Constants.TABLASBBDD.SGD_DECRETO, numexp);
        	Iterator<?> decretoIterator = decretoCollection.iterator();
        	if(decretoIterator.hasNext()){
        		IItem decreto = (IItem)decretoIterator.next();
        		String numero_decreto = decreto.getString("NUMERO_DECRETO");
//        		if (StringUtils.isNotEmpty(numero_decreto) && Integer.parseInt(numero_decreto.trim())>0){
        		if (numero_decreto != null && !numero_decreto.equals("") && Integer.parseInt(numero_decreto.trim())>0){

        			rulectx.setInfoMessage("No se puede eliminar el trámite porque el expediente ya tiene número de decreto.");        			
        			return false;
        		}
        	}        	
        	logger.info("FIN - "+this.getClass().getName());
			return true;
		}
		catch (Exception e){
	        logger.error("Error al comprobar si existe número de decreto. " + e.getMessage(), e);
	        throw new ISPACRuleException("Error al comprobar si existe número de decreto. " + e.getMessage(), e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
