package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskInicioRaminpRule extends InitTaskResolucionRaminpRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_LIC_RAMINP";
        strQueryDocumentos = 
            "NOMBRE = 'Providencia' OR " +
            "NOMBRE = 'Informe jurídico' OR " +        
            "NOMBRE = 'Informe técnico' OR " +
            "NOMBRE = 'Informe Jefe Local de Sanidad'";        
        return true;
    }
}
