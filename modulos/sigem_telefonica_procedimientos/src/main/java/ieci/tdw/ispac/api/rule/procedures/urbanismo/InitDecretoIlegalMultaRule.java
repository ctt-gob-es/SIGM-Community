package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoIlegalMultaRule extends InitDecretoIlegalRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_ILEGAL";
        strExtracto = "Resolución de multa";
        strEstado = "ESTADO_MULTA";
        strQueryDocumento = "DESCRIPCION = 'Decreto de multa - Obra ilegal'";        
        return true;
    }
}
