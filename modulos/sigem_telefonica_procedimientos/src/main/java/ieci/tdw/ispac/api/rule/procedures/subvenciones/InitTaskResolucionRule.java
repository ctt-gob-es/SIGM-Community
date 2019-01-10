package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskResolucionRule extends InitTaskAcuerdoDecretoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_CONVOCATORIA";
        strQueryDocumentos = 
            "DESCRIPCION = 'Propuesta de resolución convocatoria' OR " +
            "DESCRIPCION = 'Informe del servicio resolución convocatoria' OR " +
            "DESCRIPCION = 'Informe intervención resolución convocatoria' OR " +        
            "NOMBRE = 'Informe jurídico'";
        return true;
    }
}
