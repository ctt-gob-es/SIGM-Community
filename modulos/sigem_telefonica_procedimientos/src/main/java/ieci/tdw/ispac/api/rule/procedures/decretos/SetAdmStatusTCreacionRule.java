package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Date;

/**
 * 
 * @author diezp
 * @date 03/12/2008
 * @propósito Modificar el campo Estado Administrativo con el valor "VISTO BUENO"
 * y añadir en la entidad Decreto, quien ha firmado el documento así como la fecha en que lo hizo
 */
public class SetAdmStatusTCreacionRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            // Cambiar Estado Administrativo --> valor "VISTO BUENO"
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            IItem item = entitiesAPI.getExpedient(rulectx.getNumExp());
            item.set("ESTADOADM", "VB");
            item.store(rulectx.getClientContext());
            
            // Añadir a la entidad Decreto el usuario que ha firmado el documento así como su fecha
            // Seleccionar el id del documento anexado en el trámite 
            int taskId = rulectx.getTaskId();
            String sqlDocQuery = "ID_TRAMITE = "+taskId;
            IItemCollection tasksDocuments = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlDocQuery, "");
            //IItemCollection tasksDocuments = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
            
            if (tasksDocuments==null || tasksDocuments.toList().size()==0){
                throw new ISPACRuleException("Error al seleccionar el documento del trámite: No hay documentos");
            }else {
                IItem doc = (IItem)tasksDocuments.iterator().next();
                int docId = doc.getInt("ID");
                
                String sqlQuery = "WHERE ID_DOCUMENTO = "+docId;
                IItemCollection ctosFirmaCollection = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA", sqlQuery);
                
                if(ctosFirmaCollection!=null && ctosFirmaCollection.toList().size()!=0){
                    IItem ctoFirma = (IItem)ctosFirmaCollection.iterator().next();
                    String publicador = ctoFirma.getString("NOMBRE_FIRMANTE");
                    Date fechaFirma = ctoFirma.getDate("FECHA");
                    
                    // Almacenar en la entidad Decreto el usuario firmante y la fecha de firma
                    IItemCollection decretos = entitiesAPI.getEntities("SGD_DECRETO", rulectx.getNumExp());
                    // Sólo hay un registro
                    if (decretos.toList().size()==1){
                        IItem decreto = (IItem)decretos.iterator().next();
                        decreto.set("NOMBRE_PUBLICADOR", publicador);
                        if (fechaFirma!=null){
                            decreto.set("FECHA_PUBLICADOR", fechaFirma);
                        }
                        decreto.store(rulectx.getClientContext());
                    }else {
                        throw new ISPACRuleException("Error al seleccionar el registro Decreto");
                    }    
                }
            }
        }catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

}
