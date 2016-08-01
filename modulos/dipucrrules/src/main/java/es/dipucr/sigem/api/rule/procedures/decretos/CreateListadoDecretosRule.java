package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CreateListadoDecretosRule  implements IRule 
{
	/**
	 * Clase de libro de decretos
	 */
	private DecretosConsulta libro = null;
	
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validación y obtención de los parámetros
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		libro = new DecretosConsulta();
		return libro.validarFechas(rulectx);
	}
	
	/**
	 * Generación del libro de decretos
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		
		return libro.generarLibro(rulectx);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
