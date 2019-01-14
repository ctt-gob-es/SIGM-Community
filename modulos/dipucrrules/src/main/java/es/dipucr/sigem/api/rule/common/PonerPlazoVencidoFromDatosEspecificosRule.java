package es.dipucr.sigem.api.rule.common;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

/**
 * [dipucr-Felipe #439]
 * @since 24.02.17
 */
public class PonerPlazoVencidoFromDatosEspecificosRule extends PonerPlazoVencidoRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
    {
		IClientContext cct = rulectx.getClientContext();
		String otrosDatos = TramitesUtil.getDatosEspecificosOtrosDatos(cct, rulectx.getTaskProcedureId());
				
        super.numDias = Integer.valueOf(otrosDatos);
        return true;
    }

}
