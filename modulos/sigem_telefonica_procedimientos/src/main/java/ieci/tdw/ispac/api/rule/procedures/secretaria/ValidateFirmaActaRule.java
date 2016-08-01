package ieci.tdw.ispac.api.rule.procedures.secretaria;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class ValidateFirmaActaRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException 
	{
		boolean ok = true;
		try
		{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			int idTramite = rulectx.getTaskId();
			String strQuery = "ID_TRAMITE="+idTramite+"";
			IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), strQuery, "");
			Iterator it = itemCollection.iterator();
			if (it.hasNext())
			{
				while (ok && it.hasNext())
				{
					IItem doc = (IItem)it.next();
					String estado = doc.getString("ESTADOFIRMA");
					ok = ok && (estado.compareTo("02")==0) ;
		        }
				if (!ok)
				{
					rulectx.setInfoMessage("No se puede enviar el borrador de acta porque no está firmado.");
				}
			}
			else
			{
				rulectx.setInfoMessage("No se ha generado el borrador de acta.");
				ok = false;
			}
		} catch (Exception e) 
		{
	        throw new ISPACRuleException("Error al comprobar el estado de la firma del borrador de acta", e);
	    } 
		return ok;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
