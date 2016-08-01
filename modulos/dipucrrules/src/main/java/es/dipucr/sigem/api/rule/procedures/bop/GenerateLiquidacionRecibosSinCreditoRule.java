package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * Regla para ser utilizada desde una plantilla. Recibe el parámetro CLASIFICACION que puede tomar los siguientes valores:
 * 
 * diputacion
 * ayuntamiento
 * consejeria
 * ministerio
 * otra
 * particular
 * 
 * Devuelve una cadena con las entidades de cada grupo de clasificacion y sus anuncios correspondientes.
 *
 */
//[ecenpri-Felipe Ticket#21] Extendemos de la clase GenerateLiquidacionRecibos, que tiene el código común
public class GenerateLiquidacionRecibosSinCreditoRule extends GenerateLiquidacionRecibos implements IRule 
{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return generarLiquidacion(rulectx, BopFacturasUtil._TIPO_SIN_CREDITO);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
