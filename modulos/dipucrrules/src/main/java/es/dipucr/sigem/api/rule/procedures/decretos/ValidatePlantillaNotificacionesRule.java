package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author alberto
 * @date 05/01/2018
 * @propósito Comprueba que el documento de Plantilla de Notificaciones existe y no está firmado.
 */
public class ValidatePlantillaNotificacionesRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try {
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();

			int taskId = rulectx.getTaskId();

			// Comprobar que se haya adjuntado y no se haya firmado el documento
			// de Plantilla de Notificaciones
			String sqlQuery = "ID_TRAMITE = " + taskId
					+ " AND NOMBRE = 'Plantilla de Notificaciones' AND ESTADOFIRMA = '00'";
			IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");

			if (!itemCollection.next()) {
				rulectx.setInfoMessage(
						"No se puede cerrar el trámite ya que no se ha generado el documento de Plantilla de Notificaciones o está firmado");
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			throw new ISPACRuleException("Error al comprobar el estado de la plantilla de notificaciones", e);
		}
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
