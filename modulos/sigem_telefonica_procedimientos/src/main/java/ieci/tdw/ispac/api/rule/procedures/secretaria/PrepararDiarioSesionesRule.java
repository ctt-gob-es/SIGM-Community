package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class PrepararDiarioSesionesRule extends PrepararLibroActasRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strDocActa = "Borrador de Acta de Pleno con debates";
        return true;
    }
}
