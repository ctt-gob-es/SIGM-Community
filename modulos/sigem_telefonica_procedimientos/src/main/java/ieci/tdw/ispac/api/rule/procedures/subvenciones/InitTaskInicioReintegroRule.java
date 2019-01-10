package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskInicioReintegroRule extends InitTaskAcuerdoDecretoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_REINTEGRO";
        strQueryDocumentos = "DESCRIPCION = 'Propuesta de inicio del expediente' OR " + "DESCRIPCION = 'Informe de inicio del expediente'" ;        
        return true;
    }
}
