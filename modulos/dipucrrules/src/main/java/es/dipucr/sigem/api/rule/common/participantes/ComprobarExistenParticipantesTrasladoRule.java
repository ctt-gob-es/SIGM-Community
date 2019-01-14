package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class ComprobarExistenParticipantesTrasladoRule implements IRule{

	private static final Logger logger = Logger.getLogger(ComprobarExistenParticipantesTrasladoRule.class);
		
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		IClientContext cct = rulectx.getClientContext();
		String numexp = rulectx.getNumExp();
		
		try{
			IItemCollection colParticipantes = ParticipantesUtil.getParticipantes(cct, numexp, "ROL = 'TRAS'", "ID");
			if (colParticipantes.toList().size() <= 0){
				rulectx.setInfoMessage("En la pestaña 'Participantes', debe crear al menos un participante con 'Relación' de tipo 'Traslado'");
				return false;
			}
		}
		catch(Exception ex){
			String error = "Error al comprobar los participantes de tipo traslado";
			logger.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}
		
		return true;
	}
}