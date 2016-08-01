package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class ValidateFirmaAsistenciaRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(ValidateFirmaAsistenciaRule.class);

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
			String strQuery = "(NOMBRE LIKE '%Certificado de Asistencia%' OR NOMBRE LIKE '%Certificado de Asistentes%') AND ID_TRAMITE="+idTramite+"";
			//logger.warn("strQuery "+strQuery);
			//logger.warn("rulectx.getNumExp() "+rulectx.getNumExp());
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
					rulectx.setInfoMessage("No se puede enviar el certificado porque no está firmada.");
				}
			}
			else
			{
				rulectx.setInfoMessage("No se ha generado el documento de certificado.");
				ok = false;
			}
		} catch (Exception e) 
		{
	        throw new ISPACRuleException("Error al comprobar el estado de la firma del certificado", e);
	    } 
		return ok;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
