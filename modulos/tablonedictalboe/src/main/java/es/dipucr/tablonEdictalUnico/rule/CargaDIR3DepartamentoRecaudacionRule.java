package es.dipucr.tablonEdictalUnico.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;

public class CargaDIR3DepartamentoRecaudacionRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CargaDIR3DepartamentoRecaudacionRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IItem itTeu = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_DATOS");
			itTeu.set("IDENT_DEPART_DIR3", "LA0006601");
			itTeu.set("NAME_DEPART_DIR3", "GESTIÓN TRIBUTARIA, INSPECCION Y RECAUDACIÓN");
			itTeu.store(rulectx.getClientContext());
		} catch (ISPACException e) {
			logger.error("Error al crear la entidad 'TABLON_EDICTAL_BOE_DATOS' *IDENT_DEPART_DIR3-> LA0006601. "+e.getMessage(), e);
			throw new ISPACRuleException("Error al crear la entidad 'TABLON_EDICTAL_BOE_DATOS' *IDENT_DEPART_DIR3-> LA0006601. "+e.getMessage(),e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
