package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * [eCenpri-Felipe #363 03.06.2011]
 * @author Felipe
 */
public class DipucrInsertarParticipanteSubdelegacionGobRule extends DipucrInsertarParticipantesTrasladoRule{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		email = "administracion_territorial.ciudadreal@map.es";
		idTraslado = "129053";
		return true;
	}
}