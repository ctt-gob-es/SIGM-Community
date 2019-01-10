package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateActaPlenoConDebatesRule extends GenerateActaBaseRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {

        strPrefijo                 = "Borrador de Acta de Pleno";
        strNombreTramite           = strPrefijo + " con debates";
        strNombreCabecera          = strPrefijo + " - Cabecera";
        strNombrePie               = strPrefijo + " - Pie";
        strNombreCabeceraPropuesta = strPrefijo + " - Propuesta - Cabecera";
        strNombrePiePropuesta      = strPrefijo + " - Propuesta - Pie con debate";
        strNombreRuegos            = strPrefijo + " - Ruegos y preguntas";
        return true;
    }
}

