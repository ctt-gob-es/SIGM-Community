package es.dipucr.sigem.api.rule.procedures.bop.imprenta;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisosUtil;


public class DipucrEliminarAvisoNuevoExpTrabImprenta implements IRule {
	
	private static final Logger logger =  Logger.getLogger(DipucrEliminarAvisoNuevoExpTrabImprenta.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			return AvisosUtil.borrarAvisos(rulectx, DipucrAvisoNuevoExpTrabImprenta._NUEVOEXPEDIENTE_MESSAGE);
		}
		catch(Exception e){
			logger.error("Error al eliminar el aviso. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al eliminar el aviso. " + e.getMessage(), e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
