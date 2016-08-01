package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;


/**
 * [eCenpri-Felipe #903]
 * Eliminación del documento previo de Libro de Decretos del primer trámite
 * @author Felipe
 * @since 13.06.13
 */
public class EliminarDocPreviaLibroDecretosRule implements IRule 
{
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Código que ejecutará la regla
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		LibroDecretos libro = new LibroDecretos();
		return libro.eliminarPreviaLibroDecretos(rulectx);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
