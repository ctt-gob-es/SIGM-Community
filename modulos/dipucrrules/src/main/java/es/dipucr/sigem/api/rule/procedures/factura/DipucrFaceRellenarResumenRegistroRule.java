package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.RegistroPresencialUtil;


/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Rellena el resumen de la factura en el registro con los datos de factura
 * @author dipucr-Felipe
 * @since 18.08.2014
 */
public class DipucrFaceRellenarResumenRegistroRule implements IRule 
{
	private static final Logger logger = Logger.getLogger(DipucrFaceRellenarResumenRegistroRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Ejecución de la regla
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		String nreg = null;
		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			//Obtenemos el número de registro presencial
			IItem itemExpediente = entitiesAPI.getExpedient(numexp);
			nreg = itemExpediente.getString("NREG");

			//Obtenemos los datos de la factura
			IItem itemFactura = DipucrFaceFacturasUtil.getFacturaEntity(cct, numexp);
			String resumen = DipucrFaceFacturasUtil.getResumenFactura(itemFactura);			
			
			//Modificación del resumen en el registro presencial
			RegistroPresencialUtil.
				modificaResumenRegistroEntrada(cct, nreg, resumen);
			
		}
		catch (Exception e) {
			String error = "Error al rellenar el resumen de la factura en el registro presencial. Numexp: " 
				+ rulectx.getNumExp() + ". Nreg: " + nreg + ". " + e.getMessage();
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
