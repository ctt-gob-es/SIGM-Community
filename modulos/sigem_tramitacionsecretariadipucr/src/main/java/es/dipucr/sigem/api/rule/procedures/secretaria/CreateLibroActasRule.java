package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.decretos.DecretosConfiguration;


/**
 * [eCenpri-Felipe ticket #163]
 * Clase para la generación del libro de actas
 * Basada en el que código que genera el libro de decretos 
 * @author Felipe
 * @since 17.05.2011
 */
public class CreateLibroActasRule implements IRule 
{
	/**
	 * Clase de libro de actas
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
	 * Generación del libro de decretos
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		//Obtenemos si limitamos los permisos del properties
		IClientContext cct = rulectx.getClientContext();
		boolean bLimitarPermisos = Boolean.valueOf
				(DecretosConfiguration.getInstance(cct).getProperty(DecretosConfiguration.LIBRO_DECRETOS.LIMITAR_PERMISOS));//[dipucr-Felipe 3#99]
		
		//Obtenemos si se visualizará o no la toolbar
		boolean bVerToolbar = true;
		int tipoVisualizacion = Integer.MIN_VALUE;
		if (bVerToolbar) tipoVisualizacion = PdfWriter.PageModeUseOutlines;
		else tipoVisualizacion = PdfWriter.HideToolbar;
		
		return libro.generarLibro(rulectx, bLimitarPermisos, null, tipoVisualizacion);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
