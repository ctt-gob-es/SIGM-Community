package es.dipucr.sigem.api.rule.procedures.factura;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Regla que cierra el expediente si la factura está correctamente firmada
 * @author dipucr-Felipe
 * @since 25.08.2014
 */
public class DipucrFaceAvanzarFaseSiOKRule implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrFaceAvanzarFaseSiOKRule.class);
	
	private static final String DOCUMENTO_FACTURA = DipucrFaceFacturasUtil.DOCUMENTO_FACTURA;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	/**
	 * execute
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			IClientContext cct = rulectx.getClientContext();
			String numexp = rulectx.getNumExp();
			
			IItemCollection collection = 
				DocumentosUtil.getDocumentsByNombre(numexp, rulectx, DOCUMENTO_FACTURA);
			IItem itemDocumento = (IItem) collection.iterator().next();
			String estado = itemDocumento.getString("ESTADOFIRMA");
			
			//Sólo se avanzará la fase si el documento está firmado 
			if (estado.equals(SignStatesConstants.FIRMADO) ||
				estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS))
			{
				ExpedientesUtil.avanzarFase(cct, numexp);
			}
		}
		catch(Exception ex){
			String error = "Error al comprobar la firma de la factura "
					+ "para avanzar la fase: " + ex.getMessage();
			logger.error(error);
			throw new ISPACRuleException(error, ex);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
