package es.dipucr.sigem.api.rule.common.participantes.interesados;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

public class DipucrInsertarParticipanteInteresadoDelegJuntaRule  extends DipucrInsertarParticipantesInteresadoRule{
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DipucrInsertarParticipanteInteresadoDelegJuntaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		email = "";		
		//Cursos
		//idTraslado = "47";
		//Dipuctación Provincial
		idTraslado = "133581";
		return true;
	}
}