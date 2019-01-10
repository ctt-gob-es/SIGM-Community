package ieci.tdw.ispac.api.rule.procedures.presupuesto;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

public class InitTaskResolucionInicioPresupuestoRule extends InitTaskResolucionPresupuestoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "PRES_PRESUPUESTO";
        strQueryDocumentos = "NOMBRE = 'Providencia' OR " +  "NOMBRE = 'Propuesta de aprobación inicial'"; 
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            int pcdId = rulectx.getProcedureId();

            String strQuery = "WHERE ID_PCD = " + pcdId + " AND NOMBRE = 'Presupuesto'";
            IItemCollection col = entitiesAPI.queryEntities("SPAC_P_TRAMITES", strQuery);
            Iterator<?> it = col.iterator();
            
            if (it.hasNext()) {
                IItem tramite = (IItem)it.next();
                int tramPresupuestoId = tramite.getInt("ID");
            
                strQueryDocumentos += " OR " + "ID_TRAMITE_PCD="+tramPresupuestoId;
            }

            strQuery = "WHERE ID_PCD = " + pcdId + " AND NOMBRE = 'Informe de intervención'";
            col = entitiesAPI.queryEntities("SPAC_P_TRAMITES", strQuery);
            it = col.iterator();
            
            if (it.hasNext()) {
                IItem tramite = (IItem)it.next();
                int tramIntervencionId = tramite.getInt("ID");
            
                strQueryDocumentos += " OR " + "ID_TRAMITE_PCD="+tramIntervencionId;
            }
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido iniciar el trámite de resolución.",e);
        }
        
        return true;
    }
}
