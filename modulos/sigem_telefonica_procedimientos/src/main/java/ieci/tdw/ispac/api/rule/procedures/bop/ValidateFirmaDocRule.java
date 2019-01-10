package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author diezp
 * @date 27/11/2008
 * @propósito Valida que el documento esté firmado. Para ello, comprueba que el campo Estado Firma sea igual a "Firmado"
 */
public class ValidateFirmaDocRule implements IRule{

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        try{
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            // Comprobar que se haya anexado un documento
            IItemCollection docsCollection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
            
            if (docsCollection==null || docsCollection.toList().size()==0) {
                rulectx.setInfoMessage("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que no se ha generado el BOP correctamente");
                return false;
            }
            
            // Comprobar que se haya firmado el documento
            int taskId = rulectx.getTaskId();
            String sqlQuery = "ID_TRAMITE = " + taskId + " AND NOMBRE = 'BOP - General' AND ESTADOFIRMA = '02'";
            IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");
            
            if (!itemCollection.next()) {
                rulectx.setInfoMessage("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que el BOP general está sin firmar");
                //throw new ISPACRuleException("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que tiene documentos sin firmar");
                return false;
                
            } else {
                return true;
            }
            
        } catch (Exception e) {
            throw new ISPACRuleException("Error al comprobar el estado de la firma de los documentos", e);
        } 
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este casos

    }
}
