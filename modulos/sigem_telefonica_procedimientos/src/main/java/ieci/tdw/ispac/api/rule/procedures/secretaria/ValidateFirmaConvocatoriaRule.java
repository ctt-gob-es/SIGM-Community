package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class ValidateFirmaConvocatoriaRule implements IRule{

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        boolean ok = true;
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            int idTramite = rulectx.getTaskId();
            /**
               * Inicio [Teresa Ticket #339#] SIGEM secretaria problemas a la hora de registrar
               * **/
            String strQuery = "ID_TRAMITE = " + idTramite + " AND NOMBRE LIKE '%Conv%'";
            /**
               * Fin [Teresa Ticket #339#] SIGEM secretaria problemas a la hora de registrar
               * **/
            IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), strQuery, "");
            Iterator<?> it = itemCollection.iterator();
            
            if (it.hasNext()) {
                
                while (ok && it.hasNext()) {
                    IItem doc = (IItem)it.next();
                    String estado = doc.getString(DocumentosUtil.ESTADOFIRMA);
                    ok = ok && (estado.compareTo("02")==0) ;
                }
                
                if (!ok) {
                    rulectx.setInfoMessage("No se puede enviar la convocatoria porque no está firmada.");
                }
                
            } else {
                rulectx.setInfoMessage("No se ha generado el documento de convocatoria.");
                ok = false;
            }
            
        } catch (Exception e) {
            throw new ISPACRuleException("Error al comprobar el estado de la firma de la convocatoria", e);
        } 
        return ok;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
