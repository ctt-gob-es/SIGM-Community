package es.dipucr.sigem.api.rule.common.resolucion;

import java.util.Iterator;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;

public class CompruebaCreadoIdPropuestaRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(CompruebaCreadoIdPropuestaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		Iterator<IItem> propuestas = ConsultasGenericasUtil.queryEntities(rulectx, "DPCR_ID_PROPUESTA", "NUMEXP='"+rulectx.getNumExp()+"'");
		if (propuestas.hasNext()) {
			logger.info("Tiene propuesta el número de expediente: "+rulectx.getNumExp());
			return true;
		}
		else{
			rulectx.setInfoMessage("Debe firmar la propuesta o borrador de resolución y terminar el trámite dónde se encuentra");
			return false;
		}
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
