package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 17/03/2010
 * @propósito Valida que el documento de Decreto esté firmado. Para ello, comprueba que el campo Estado Firma sea igual a "Firmado"
 * Además comprueba que el documento de Plantilla de Notificaciones no esté firmado.
 */
public class ValidateFirmaDocDecretoRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        
        	// Comprobar que se haya anexado un documento
			IItemCollection docsCollection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
			
			if (docsCollection==null || docsCollection.toList().size()==0)
			{
				rulectx.setInfoMessage("No se puede cerrar el trámite ya que no se ha generado el documento de Decreto");
				return false;
			}
			
			// Comprobar que se haya firmado el documento de Decreto
			//MQE modificamos para que además de firmado acepte rechazados y firmados con reparos
			int taskId = rulectx.getTaskId();
			//String sqlQuery = "ID_TRAMITE = "+taskId+" AND NOMBRE = 'Decreto' AND ESTADOFIRMA = '02'";
			String sqlQuery = "ID_TRAMITE = "+taskId+" AND NOMBRE = 'Decreto' AND ESTADOFIRMA in ('02','03','04')";
			IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");
			
			if (!itemCollection.next())
			{
				rulectx.setInfoMessage("No se puede cerrar el trámite ya que no se ha generado el documento de Decreto o está sin firmar");
				return false;
			}
			else
			{
				
				// Comprobar que se haya adjuntado y no se haya firmado el documento de Plantilla de Notificaciones
				sqlQuery = "ID_TRAMITE = "+taskId+" AND NOMBRE = 'Plantilla de Notificaciones' AND ESTADOFIRMA = '00'";
				itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");
				
				if (!itemCollection.next())
				{
					rulectx.setInfoMessage("No se puede cerrar el trámite ya que no se ha generado el documento de Plantilla de Notificaciones o está firmado");
					return false;
				}
				else
				{
					return true;
				}
			}
		}
		catch (Exception e) 
		{
	        throw new ISPACRuleException("Error al comprobar el estado de la firma de los documentos", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
