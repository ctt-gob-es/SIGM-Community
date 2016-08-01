package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;
import com.sun.star.lang.XComponent;
import es.dipucr.sigem.api.rule.common.documento.DipucrAutogeneraDocumentoEspecificoInitTramite;


/**
 * [dipucr-Felipe #1061]
 * Generación del libro de extractos al iniciar el trámite
 * @author Felipe
 * @since 19.02.14
 */
public class CreateLibroExtractosRule extends DipucrAutogeneraDocumentoEspecificoInitTramite
{
	
	private static final Logger logger = Logger.getLogger(CreateLibroExtractosRule.class);
	
	/**
	 * Clase de libro de decretos
	 */
	private LibroExtractos libro = null;
	
	/**
	 * Validación y obtención de los parámetros
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		bEditarTextos = true;
		
		libro = new LibroExtractos();
		return libro.validarFechas(rulectx);
	}
	
	/**
	 * Método de edición del documento a sobreescribir en los hijos
	 * @param rulectx
	 * @param component
	 * @param refTabla
	 * @param entitiesAPI
	 * @param numexp
	 * @throws ISPACException 
	 */
	public void editarTextosDocumento(IRuleContext rulectx, XComponent xComponent) {

		
		try {
			libro.generarTextosDocumento(rulectx, xComponent);
		} catch (ISPACException e) {
			logger.error("Error al editar los texto del libro de extractos. " + e.getMessage(), e);
		}
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
