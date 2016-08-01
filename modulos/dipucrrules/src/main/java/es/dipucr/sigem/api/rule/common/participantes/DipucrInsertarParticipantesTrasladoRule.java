package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrInsertarParticipantesTrasladoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrInsertarParticipantesTrasladoRule.class);
	protected String email = "";
	
	//Id de la tabla scr_pfis y scrPjur de las bbdd de registro
	protected String idTraslado = "";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try {
			if(StringUtils.isNotEmpty(idTraslado))
        		ParticipantesUtil.insertarParticipanteById(rulectx, rulectx.getNumExp(), idTraslado, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil._TIPO_PERSONA_FISICA, email);
		} catch (ISPACException e) {		
			logger.error("ERROR al insertar el participante con id de tercero: " + idTraslado + " y correo: " + email + ", en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		logger.info("FIN - " + this.getClass().getName());
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}