package es.dipucr.sigem.api.rule.procedures.contratacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.factura.domain.bean.OperacionGastosFuturosBean;
import es.dipucr.factura.services.factura.FacturaWSProxy;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

public class CrearRetencionCreditoSicalRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CrearRetencionCreditoSicalRule.class);
	protected IItem itemRetencion = null;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------			
			
			final String ENTIDAD_RETENCION = "CONTRATACION_RET_CREDITO";
			String numexp = rulectx.getNumExp();
			
			IItemCollection colRetenciones = entitiesAPI.getEntities(ENTIDAD_RETENCION, numexp);
			itemRetencion = colRetenciones.value();
			
			if (StringUtils.isEmpty(itemRetencion.getString("EJERCICIO"))){
				rulectx.setInfoMessage("El campo 'Ejercicio' de las pestaña 'Retenciones de Crédito' no pueder ser vacío");
				return false;
			}
			if (StringUtils.isEmpty(itemRetencion.getString("TEXTO"))){
				rulectx.setInfoMessage("El campo 'Texto' de las pestaña 'Retenciones de Crédito' no pueder ser vacío");
				return false;
			}
			if (StringUtils.isEmpty(itemRetencion.getString("ECONOMICA"))){
				rulectx.setInfoMessage("El campo 'Económica' de las pestaña 'Retenciones de Crédito' no pueder ser vacío");
				return false;
			}
			if (StringUtils.isEmpty(itemRetencion.getString("ORGANICA"))){
				rulectx.setInfoMessage("El campo 'Orgánica' de las pestaña 'Retenciones de Crédito' no pueder ser vacío");
				return false;
			}
			if (StringUtils.isEmpty(itemRetencion.getString("FUNCIONAL"))){
				rulectx.setInfoMessage("El campo 'Funcional' de las pestaña 'Retenciones de Crédito' no pueder ser vacío");
				return false;
			}
			if (StringUtils.isEmpty(itemRetencion.getString("IMPORTE"))){
				rulectx.setInfoMessage("El campo 'Importe' de las pestaña 'Retenciones de Crédito' no pueder ser vacío");
				return false;
			}

		} catch (Exception e) {

			String error = "Error al crear la retención de crédito en Sical";
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			String numOperacion = itemRetencion.getString("NUM_OPERACION");
			
			if (StringUtils.isEmpty(numOperacion)){
				
				ClientContext cct = (ClientContext) rulectx.getClientContext();
				FacturaWSProxy ws = new FacturaWSProxy();
				OperacionGastosFuturosBean operacion = ws.crearRetencionCredito
				(
						EntidadesAdmUtil.obtenerEntidad(cct),
						itemRetencion.getString("EJERCICIO"),
						null,
						FechasUtil.convertToCalendar(new Date()),
						itemRetencion.getString("TEXTO"),
						itemRetencion.getString("ECONOMICA"),
						itemRetencion.getString("ORGANICA"),
						itemRetencion.getString("FUNCIONAL"), 
						itemRetencion.getDouble("IMPORTE")
				);
				itemRetencion.set("NUM_OPERACION", operacion.getNumOpeSical());
				itemRetencion.set("FECHA_CONTABLE", new Date());
				itemRetencion.store(cct);
			}
			
		} catch (Exception e) {

			String error = "Error al crear la retención de crédito en Sical";
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}

		return new Boolean(true);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
}
