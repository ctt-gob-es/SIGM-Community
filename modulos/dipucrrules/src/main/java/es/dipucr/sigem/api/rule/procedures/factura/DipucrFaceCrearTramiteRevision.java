package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;


/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Regla que crea el primer trámite de 'Revisión de eFactura'
 * @author dipucr-Felipe
 * @since 06.05.2014
 */
public class DipucrFaceCrearTramiteRevision implements IRule 
{
	private static final Logger logger = Logger.getLogger(DipucrFaceCrearTramiteRevision.class);
	
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
			TramitesUtil.crearTramite
			(
					(ClientContext) rulectx.getClientContext(), 
					DipucrFaceFacturasUtil.COD_TRAM_REVISION1, 
					rulectx.getNumExp()
			);
		}
		catch (Exception e) {
			String error = "Error al generar el trámite de revisión de eFactura en el expediente: " 
					+ rulectx.getNumExp() + ". " + e.getMessage(); 
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
