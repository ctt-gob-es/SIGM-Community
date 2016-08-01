package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.FirmaUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [ecenpri-Felipe Ticket #39] Nuevo procedimiento Propuesta de Solicitud de Anuncio
 * @since 04.08.2010
 * @author Felipe
 * 
 * @propósito Valida que el documento de anuncio exista y este firmado
 * En caso contrario muestra un mensaje y no permite cerrar el trámite
 */
public class ValidateFirmaAnuncioRule extends GenerateLiquidacionRecibos implements IRule 
{
	/**
	 * Constantes
	 */
	public static String _DOC_ANUNCIO = Constants.BOP._DOC_ANUNCIO;
	
	/**
	 * Funciones
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return FirmaUtil.validarDocumentoFirmado(rulectx, _DOC_ANUNCIO);
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
