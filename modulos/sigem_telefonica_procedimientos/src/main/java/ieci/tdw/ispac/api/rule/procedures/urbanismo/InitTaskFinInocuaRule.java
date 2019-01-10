package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskFinInocuaRule extends InitTaskAcuerdoDecretoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_AUTORIZACION";
        strQueryDocumentos = 
            "DESCRIPCION = 'Providencia - Actividad inocua' OR " +
            "DESCRIPCION = 'Informe técnico - Actividad inocua' OR " +
            "DESCRIPCION = 'Informe jurídico - Actividad inocua'" ;        
        return true;
    }
}
