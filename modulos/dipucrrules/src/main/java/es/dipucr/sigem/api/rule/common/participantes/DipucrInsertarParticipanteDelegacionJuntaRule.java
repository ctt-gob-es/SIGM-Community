package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * [eCenpri-Felipe #363 03.06.2011]
 * @author Felipe
 */
public class DipucrInsertarParticipanteDelegacionJuntaRule extends DipucrInsertarParticipantesTrasladoRule{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		email = "mifranco@jccm.es";
		idTraslado = "133581";
		return true;
	}
}