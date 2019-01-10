package es.dipucr.contratacion.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrIniciaExpGenerico;

public class DipucrIniciaExpContratacion extends DipucrIniciaExpGenerico {
	
	protected static final Logger logger = Logger.getLogger(DipucrIniciaExpContratacion.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		nombre_tabla = "DPCR_CONTR_EXP_CONTR";
		return true;
	}
}