package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Felipe ticket #810]
 * Valida que se haya rellenado el extracto de decreto
 * @author Felipe
 * @since 20.12.2012
 */
public class ValidarExtractoDecretoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(ValidarExtractoDecretoRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validaciones
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			IItemCollection collection = entitiesAPI.getEntities("SGD_DECRETO", numexp);
			String sErrorExtracto = "Debe rellenar el campo Extracto en la pestaña de Decreto.";
			
			if (!collection.next()){
				rulectx.setInfoMessage(sErrorExtracto);
				return false;
			}
			else{
				IItem itemDecreto = (IItem)collection.iterator().next();
				if (StringUtils.isEmpty(itemDecreto.getString("EXTRACTO_DECRETO"))){
					rulectx.setInfoMessage(sErrorExtracto);
					return false;
				}
				else{
					return true;
				}
			}
		}
		catch (Exception e) {
        	logger.error("Error al validar el extracto de decreto en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al validar el extracto de decreto en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
