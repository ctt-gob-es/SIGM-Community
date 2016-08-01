package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.CambioResultadoLicitacion;

public class CambioResultadoLicitacionFormalizacion extends CambioResultadoLicitacion{
	
	public static final Logger logger = Logger.getLogger(CambioResultadoLicitacionFormalizacion.class);


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		valorResultadoLicitacion = "9 - Formalizado";
		return true;
	}



}