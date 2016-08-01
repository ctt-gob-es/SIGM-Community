package es.dipucr.sigem.api.rule.common.estadoExpediente;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrSetEstado implements IRule{
	
	public static final Logger logger = Logger.getLogger(DipucrSetEstado.class);
	
	public String nuevoEstado = "";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			logger.info("INICIO - " + this.getClass().getName());
			
			IClientContext cct = rulectx.getClientContext();
		
			String numexp = rulectx.getNumExp();
			
			IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
			
			expediente.set("ESTADOADM", nuevoEstado);
			expediente.store(cct);
			
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) {
			logger.error("ERROR al asignar el estado administrativo " + nuevoEstado + " al expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}	
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
