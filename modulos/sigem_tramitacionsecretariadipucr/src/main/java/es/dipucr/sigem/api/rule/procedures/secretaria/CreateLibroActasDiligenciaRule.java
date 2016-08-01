package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;


/**
 * [eCenpri-Felipe ticket #911]
 * Clase para la generación de la previa del libro de actas
 * y de la diligencia correspondiente
 * 
 * @author Felipe
 * @since 20.06.13
 */
public class CreateLibroActasDiligenciaRule implements IRule 
{
	/**
	 * Clase de libro de actass
	 */
	private LibroActas libro = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validación y obtención de los parámetros
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		libro = new LibroActas();
		return libro.validarParametros(rulectx);
	}
	
	/**
	 * Generación del libro de actas
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return libro.generarDiligencia(rulectx);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
