package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.FirmaUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [ecenpri-Felipe Ticket #903]
 * Control de que la diligencia esta firmada en el primer trámite 
 * @since 07.06.2013
 * @author Felipe
 * 
 * @propósito Valida que el documento de anuncio exista y este firmado
 * En caso contrario muestra un mensaje y no permite cerrar el trámite
 */
public class ValidateFirmaDiligenciaLibroDecRule implements IRule 
{
	/**
	 * Constantes
	 */
	public static String _DOC_LIBRODEC_DILIGENCIA = Constants.DECRETOS._DOC_LIBRO_DECRETOS_DILIGENCIA;
	
	/**
	 * Funciones
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return FirmaUtil.validarDocumentoFirmado(rulectx, _DOC_LIBRODEC_DILIGENCIA);
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
