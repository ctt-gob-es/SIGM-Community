package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Ticket #186 
 * Inserción de Anuncios Internos en el BOP
 * Cargar directamente el departamento del usuario en el campo Entidad de "Solicitud BOP"
 * @author Felipe
 *
 */
public class SetDepartamentoEntidadRule extends GenerateLiquidacionRecibos implements IRule 
{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		ClientContext cct = null;
		IItem itemExpediente = null;
		IItem itemSolicitud = null;
		
        try{
			//******************************************************
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //******************************************************
	        
	        String numexp = rulectx.getNumExp();
	        
	        //Recuperamos el expediente
	        itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
	        String sNombreDepartamento = itemExpediente.getString("SECCIONINICIADORA");
	        
	        //Creamos la nueva entidad solicitud y actualizamos el departamento
	        itemSolicitud = entitiesAPI.createEntity("BOP_SOLICITUD", numexp);
	        itemSolicitud.set("ENTIDAD", sNombreDepartamento.toUpperCase());
	        itemSolicitud.set("CLASIFICACION", "DIPUTACIÓN PROVINCIAL");
	        itemSolicitud.set("URGENCIA", "Normal");
	        itemSolicitud.store(cct);
	        
        }
        catch (Exception e) {
        	throw new ISPACRuleException("Error al recuperar el departamento " +
        			" del usuario en el campo Entidad de la pestaña SOLICITUD BOP" , e);
		}
	        
		
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
