package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author diezp
 * @proposito Inicializa valores de la solapa decreto
 * al iniciar el expediente
 */
public class InitDecretoRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getEntities("SGD_DECRETO", rulectx.getNumExp());
			
			if (itemCollection!=null && itemCollection.toList().size()>1) {
				throw new ISPACRuleException("Se ha producido un error. Se han encontrado varios registros para la entidad Decreto");
			}else if (itemCollection!=null && itemCollection.toList().size()==0) {
				IItem item = entitiesAPI.createEntity("SGD_DECRETO","");
				item.set("NUMEXP", rulectx.getNumExp());
				//Obtener la unidad organizativa del responsable
		        IItem departamento = getDepartamento(rulectx);
		        if (departamento != null) {
		        	item.set("DPTO_SERVICIO", departamento.getString("NAME"));
			    }
		        item.store(rulectx.getClientContext());
			}
			return null;
		} catch (ISPACException e) {
			throw new ISPACRuleException(e);
		}
	}

	/**
     * Obtiene el departamento del responsable del expediente.
     * @param rulectx Contexto de la regla.
     * @return Departamento del responsable.
     * @throws ISPACException si ocurre algún error.
     */
    protected IItem getDepartamento(IRuleContext rulectx) throws ISPACException {
        IItem departamento = null;
        
        IResponsible resp = rulectx.getClientContext().getResponsible();
        if (resp.isUser()) {
        	departamento = resp.getRespOrgUnit();
        } else if (resp.isOrgUnit()) {
        	departamento = resp;
        } else { // resp.isGroup()
        	// No hay departamento
        }
        	
        return departamento;
    }
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
