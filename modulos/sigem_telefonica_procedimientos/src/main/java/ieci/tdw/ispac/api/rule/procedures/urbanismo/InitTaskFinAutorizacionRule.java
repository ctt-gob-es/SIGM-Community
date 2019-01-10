package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskFinAutorizacionRule extends InitTaskAcuerdoDecretoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_AUTORIZACION";
        strQueryDocumentos = 
            "DESCRIPCION = 'Providencia - Autorización diversa' OR " +
            "DESCRIPCION = 'Informe técnico - Autorización diversa' OR " +
            "DESCRIPCION = 'Informe jurídico - Autorización diversa'" ;        
        return true;
    }
}
