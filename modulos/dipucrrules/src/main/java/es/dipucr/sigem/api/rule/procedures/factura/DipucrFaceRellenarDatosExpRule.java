package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;


/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Regla que rellena el asunto del expediente en función de los datos de factura
 * @author dipucr-Felipe
 * @since 20.08.2014
 */
public class DipucrFaceRellenarDatosExpRule implements IRule 
{
	private static final Logger logger = Logger.getLogger(DipucrFaceRellenarDatosExpRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			IClientContext cct = rulectx.getClientContext();
			String numexp = rulectx.getNumExp();
			
			//Obtenemos el resumen de la factura
			IItem itemFactura = DipucrFaceFacturasUtil.getFacturaEntity(cct, numexp);
			String resumen = DipucrFaceFacturasUtil.getResumenFactura(itemFactura);
			
			//Guardamos el resumen en el asunto del expediente
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			itemExpediente.set("ASUNTO", resumen);
			itemExpediente.store(cct);
		}
		catch (Exception e) {
			String error = "Error al rellenar los datos del expediente con la información"
					+ " de la factura: " + rulectx.getNumExp() + ". " + e.getMessage(); 
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
