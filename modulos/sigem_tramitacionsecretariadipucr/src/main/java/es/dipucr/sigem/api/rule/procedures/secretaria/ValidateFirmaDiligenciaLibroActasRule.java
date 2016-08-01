package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.FirmaUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [ecenpri-Felipe Ticket #911]
 * Control de que la diligencia esta firmada en el primer trámite 
 * @since 20.06.2013
 * @author Felipe
 * 
 * @propósito Valida que el documento de anuncio exista y este firmado
 * En caso contrario muestra un mensaje y no permite cerrar el trámite
 */
public class ValidateFirmaDiligenciaLibroActasRule implements IRule 
{
	/**
	 * Constantes
	 */
	public static String _DOC_LIBRO_ACTAS_DILIG = Constants.TIPODOC.LIBRO_ACTAS_DILIG;
	
	/**
	 * Funciones
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return FirmaUtil.validarDocumentoFirmado(rulectx, _DOC_LIBRO_ACTAS_DILIG);
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
