package es.dipucr.sigem.api.rule.procedures.contratacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

public class CargarEntidadRetencionCreditoRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CargarEntidadRetencionCreditoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------			
			
			final String ENTIDAD_RETENCION = "CONTRATACION_RET_CREDITO";
			String numexp = rulectx.getNumExp();
			
			IItemCollection colRetenciones = entitiesAPI.getEntities(ENTIDAD_RETENCION, numexp);
			
			if (!colRetenciones.iterator().hasNext()){
				IItem itemRetencion = entitiesAPI.createEntity(ENTIDAD_RETENCION, numexp);
				itemRetencion.set("EJERCICIO", FechasUtil.getAnyoActual());
				
				IItem itemExpediente = entitiesAPI.getExpedient(numexp);
				String asunto = itemExpediente.getString("ASUNTO");
				itemRetencion.set("TEXTO", asunto);
				
				itemRetencion.store(cct);
			}
			
		} catch (Exception e) {
			String error = "Error al crear la entidad de retención de crédito";
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}

		return new Boolean(true);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
