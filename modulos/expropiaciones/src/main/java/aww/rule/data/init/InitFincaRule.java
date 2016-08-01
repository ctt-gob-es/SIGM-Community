package aww.rule.data.init;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class InitFincaRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getEntities("FINCA", rulectx.getNumExp());
			
			
			if (itemCollection!=null && itemCollection.toList().size()>=1) {
				throw new ISPACRuleException("Se ha producido un error. Se han encontrado varios registros para la entidad FINCA");
			}else if (itemCollection!=null && itemCollection.toList().size()==0) {
				IItem item = entitiesAPI.createEntity("FINCA","");
				item.set("NUMEXP", rulectx.getNumExp()); 
		        item.store(rulectx.getClientContext());
			}
			return null;
		} catch (ISPACException e) {
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
