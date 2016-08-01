package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import com.lowagie.text.pdf.PdfWriter;


/**
 * [eCenpri-Felipe ticket #164]
 * Clase para la generación del libro de decretos
 * Se rehace la clase de Telefónica por tratarse de un código ininteligible
 * Se pasa el código a una Regla en vez de un action, por ser más cómodo 
 * @author Felipe
 * @since 05.10.2010
 */
public class CreateLibroDecretosProtegidoRule implements IRule 
{
	/**
	 * Clase de libro de decretos
	 */
	private LibroDecretos libro = null;
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validación y obtención de los parámetros
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		libro = new LibroDecretos();
		return libro.validarFechas(rulectx);
	}
	
	/**
	 * Generación del libro de decretos
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return libro.generarLibro(rulectx, true, "sigem", PdfWriter.HideToolbar);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
