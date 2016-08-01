package aww.rule.validation;

import java.util.List;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * Valida que un documento este adjuntado a un tramite.
 */
public class ValidateDocAdjuntoRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItem item = entitiesAPI.getExpedient(rulectx.getNumExp());
	       
        	// Comprobar que se haya anexado un documento
			IItemCollection docsCollection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
			
			List documentos = docsCollection.toList();
			
			for(int i=0; i < documentos.size(); i++){
				IItem doc = (IItem) documentos.get(i);
				doc.getProperties();
				
			}
			
			if (docsCollection==null || docsCollection.toList().size()==0){
				rulectx.setInfoMessage("No se puede cerrar el tramite del expediente "+rulectx.getNumExp()+" ya que no se ha adjuntado un documento");
				return false;
			} else{
				return true;
			}			
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar el estado de los documentos adjuntos", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
