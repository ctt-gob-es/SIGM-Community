package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import com.lowagie.text.pdf.PdfWriter;


/**
 * [eCenpri-Felipe ticket #602]
 * Clase para la generación del libro de decretos sin proteger
 * para ciertos ayuntamientos que lo deseen imprimir 
 * @author Felipe
 * @since 16.04.2010
 */
public class CreateLibroDecretosSinProtegerRule implements IRule 
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
		
		//Obtenemos si limitamos los permisos del properties
		boolean bLimitarPermisos = false;
		//Obtenemos si se visualizará o no la toolbar
		int tipoVisualizacion = PdfWriter.PageModeUseOutlines;
		
		return libro.generarLibro(rulectx, bLimitarPermisos, null, tipoVisualizacion);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
