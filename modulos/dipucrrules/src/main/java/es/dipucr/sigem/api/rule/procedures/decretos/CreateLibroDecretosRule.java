package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.decretos.DecretosConfiguration;


/**
 * [eCenpri-Felipe ticket #164]
 * Clase para la generación del libro de decretos
 * Se rehace la clase de Telefónica por tratarse de un código ininteligible
 * Se pasa el código a una Regla en vez de un action, por ser más cómodo 
 * @author Felipe
 * @since 05.10.2010
 */
public class CreateLibroDecretosRule implements IRule 
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
		
		IClientContext cct = rulectx.getClientContext();
		//Obtenemos si limitamos los permisos del properties
		boolean bLimitarPermisos = Boolean.valueOf
				(DecretosConfiguration.getInstance(cct).getProperty(DecretosConfiguration.LIBRO_DECRETOS.LIMITAR_PERMISOS));//[dipucr-Felipe 3#99]
		
		//Obtenemos si se visualizará o no la toolbar
		boolean bVerToolbar = Boolean.valueOf
				(DecretosConfiguration.getInstance(cct).getProperty(DecretosConfiguration.LIBRO_DECRETOS.VER_TOOLBAR));//[dipucr-Felipe 3#99]
		int tipoVisualizacion = Integer.MIN_VALUE;
		if (bVerToolbar) tipoVisualizacion = PdfWriter.PageModeUseOutlines;
		else tipoVisualizacion = PdfWriter.HideToolbar;
		
		return libro.generarLibro(rulectx, bLimitarPermisos, null, tipoVisualizacion);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
