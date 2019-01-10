package ieci.tdw.ispac.api.rule.procedures.sancionador;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia tarea de Resolución de Sanción del Procedimiento Sancionador
 *
 */

public class InitTaskResolucionSancionSancionadorRule extends InitTaskResolucionSancionadorRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_SANCIONADOR";
        strQueryDocumentos = "NOMBRE = 'Providencia de Alcaldía' OR " +  
                                "NOMBRE = 'Resolución/Acuerdo de Inicio' OR " +
                                "NOMBRE = 'Pliego de Cargos' OR " +
                                "NOMBRE = 'Propuesta de Resolución' OR " +
                                "NOMBRE = 'Informe de Valoración de Alegaciones'"; 
         
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            int pcdId = rulectx.getProcedureId();

            String strQuery = "WHERE ID_PCD = " + pcdId + " AND NOMBRE = 'Inicio de Oficio del Expediente'";
            IItemCollection col = entitiesAPI.queryEntities("SPAC_P_TRAMITES", strQuery);
            Iterator<?> it = col.iterator();
            
            if (it.hasNext()) {
                IItem tramite = (IItem)it.next();
                int tramCuentaId = tramite.getInt("ID");
            
                strQueryDocumentos += " OR " + "ID_TRAMITE_PCD="+tramCuentaId;
            }

        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido iniciar el trámite de resolución.",e);
        }
        
        return true;
    }
}
