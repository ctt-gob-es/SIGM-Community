package es.dipucr.sigem.api.rule.common.participantes;

import es.dipucr.sigem.api.rule.common.traslados.TrasladoConfiguration;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class DipucrInsertarParticipanteTrasladoUATAGeneralRule extends DipucrInsertarParticipantesTrasladoRule{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		TrasladoConfiguration trasladoConfig = TrasladoConfiguration.getInstance(rulectx.getClientContext());
		email = trasladoConfig.get(TrasladoConfiguration.UATA_TRASLADO_EMAIL);
		idTraslado = trasladoConfig.get(TrasladoConfiguration.UATA_TRASLADO_ID);
		
		return true;
	}
}
