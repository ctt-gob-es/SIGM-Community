package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.traslados.TrasladoConfiguration;

public class DipucrInsertarParticipanteTrasladoVicepresidencia1Rule extends DipucrInsertarParticipantesTrasladoRule{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		TrasladoConfiguration trasladoConfig = TrasladoConfiguration.getInstance(rulectx.getClientContext());
		email = trasladoConfig.get(TrasladoConfiguration.VICEPRESIDENCIA_PRIMERA_TRASLADO_EMAIL);
		idTraslado = trasladoConfig.get(TrasladoConfiguration.VICEPRESIDENCIA_PRIMERA_TRASLADO_ID);
		
		return true;
	}
}