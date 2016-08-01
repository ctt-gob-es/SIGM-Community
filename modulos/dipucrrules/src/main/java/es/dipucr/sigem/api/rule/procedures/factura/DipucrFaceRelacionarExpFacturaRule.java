package es.dipucr.sigem.api.rule.procedures.factura;

import java.util.List;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;

/**
 * #1112 Procedimiento SIGEM para Factur@-FACe - Anulación
 * Regla que relaciona el expediente de anulación de factura
 * con el expediente padre real de la factura
 * @author dipucr-Felipe
 * @since 27.05.2014
 */
public class DipucrFaceRelacionarExpFacturaRule implements IRule {

	/**
	 * logger.
	 */
	private static final Log logger = LogFactory.getLog(DipucrFaceRelacionarExpFacturaRule.class);
	
	/**
	 * Método init
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	/**
	 * Método validate
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Método execute
	 */
	public final Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			String numexp = rulectx.getNumExp();
			
			//Obtenemos la entidad de ANULACION_FACTURAE para obtener el id/nreg
			IItem itemAnulacion = DipucrFaceFacturasUtil.getAnulacionEntity(cct, numexp);
			String nreg = itemAnulacion.getString("NREG_FACE");
			
			//Obtenemos el numexp del expediente de factura
			String query = "WHERE NREG_FACE = '" + nreg + "'";
			@SuppressWarnings({ "unchecked" })
			List<IItem> listFacturas = entitiesAPI.queryEntities("FACTURAE", query).toList();
			
			if (listFacturas.size() == 0){
				throw new IllegalArgumentException(
					"Imposible localizar el expediente de factura relacionado");
			}
			IItem itemFactura = listFacturas.get(0);
			String numexpPadre = itemFactura.getString("NUMEXP");
			
			//Cambiamos el asunto del expediente para dejarlo aún más claro
			IItem itemExpediente = entitiesAPI.getExpedient(numexp);
			StringBuffer sbAsunto = new StringBuffer();
			sbAsunto.append("Anulación de la factura ");
			sbAsunto.append(nreg);
			sbAsunto.append(". Expediente ");
			sbAsunto.append(numexpPadre);
			itemExpediente.set("ASUNTO", sbAsunto.toString());
			itemExpediente.store(cct);
			
			//Relacionamos los expedientes
			ExpedientesRelacionadosUtil.relacionaExpedientes
				(cct, numexpPadre, numexp, "Anulación de factura");
			
			return new Boolean(true);
			
		} catch (Exception e) {
			logger.error("[Factur@] Error al ejecutar la regla", e);
			throw new ISPACRuleException("Error al ejecutar la regla que "
				+ "relaciona el expediente de anulación con el de factura", e);
		}
	}

	/**
	 * Método cancel
	 */
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
	
