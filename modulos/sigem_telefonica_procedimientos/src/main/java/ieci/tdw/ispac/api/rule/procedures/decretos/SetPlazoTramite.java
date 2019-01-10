package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author diezp
 * @proposito Introduce en el trámite, el plazo definido en el Catálogo de procedimientos 
 * evento: al iniciar un trámite
 */
public class SetPlazoTramite implements IRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //Obtener valores del catalogo e insertarlos en el trámite
            IEntitiesAPI entapi=rulectx.getClientContext().getAPI().getEntitiesAPI();
            IItem catalogo=entapi.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS,rulectx.getProcedureId());
            
            IItem item = (IItem)rulectx.getItem();
            item.set("PLAZO",catalogo.get("PLZ_RESOL"));
            item.set("UPLAZO",catalogo.get("UNID_PLZ"));
            
            //rulectx.getClientContext().getAPI().getGenDocAPI().attachTaskInputStream(connectorSession, taskId, docId, in, length, sMimeType, sName)     
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
        // No se da nunca este caso
    }

}
