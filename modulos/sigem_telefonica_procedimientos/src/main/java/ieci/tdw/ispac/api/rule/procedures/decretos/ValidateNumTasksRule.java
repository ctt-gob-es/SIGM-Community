package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author diezp
 * @date 09/12/2008
 * @propósito Valida que sólo se haya creado un trámite en la fase actual
 */
public class ValidateNumTasksRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getStageTasks(rulectx.getNumExp(), rulectx.getStageProcedureId());
			
			// Se compara el tamaño > 1, ya que itemCollection contiene el elemento que estamos intentando crear 
			if (itemCollection!=null && itemCollection.toList().size()>1){
				rulectx.setInfoMessage("Sólo se puede crear un trámite para la fase actual");
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar el número de trámites de la fase actual", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
