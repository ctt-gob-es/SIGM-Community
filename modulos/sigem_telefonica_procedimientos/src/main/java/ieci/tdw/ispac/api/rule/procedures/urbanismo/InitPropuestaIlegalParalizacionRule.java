package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaIlegalParalizacionRule extends InitPropuestaIlegalRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_ILEGAL";
        strExtracto = "Resolución de paralización de obras";
        strEstado = "ESTADO_PARALIZACION";
        strQueryDocumento = "DESCRIPCION = 'Decreto de paralización de obra - Obra ilegal'";        
        return true;
    }
}
