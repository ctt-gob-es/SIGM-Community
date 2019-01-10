package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoIlegalPrecintoRule extends InitDecretoIlegalRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_ILEGAL";
        strExtracto = "Resolución de precinto de obras";
        strEstado = "ESTADO_PRECINTO";
        strQueryDocumento = "DESCRIPCION = 'Decreto de precinto de obra - Obra ilegal'";        
        return true;
    }
}
