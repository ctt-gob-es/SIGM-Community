package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

public class ValidateFirmaRule implements IRule{

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException  {
        boolean ok = true;
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            /**
             * [Teresa]Ticket #202# INICIO SIGEM mejora a la hora de validar si los certificados han sido firmados
             * **/
            
            //String strQuery = "NOMBRE LIKE 'Certificado de%'";
            int taskId = rulectx.getTaskId();
            String strQuery = "NOMBRE LIKE 'Certificado%' AND ID_TRAMITE = " + taskId;
            /**
             * [Teresa]Ticket #202# FIN SIGEM mejora a la hora de validar si los certificados han sido firmados
             * **/
            IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), strQuery, "");
            Iterator<?> it = itemCollection.iterator();
            
            while (ok && it.hasNext()) {
                IItem doc = (IItem)it.next();
                String estado = doc.getString("ESTADOFIRMA");
                ok = ok && (estado.compareTo("02")==0) ;
            }
            
            if (!ok) {
                rulectx.setInfoMessage("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que tiene certificados sin firmar");
            }
            
        } catch (Exception e) {
            throw new ISPACRuleException("Error al comprobar el estado de la firma de los documentos", e);
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
