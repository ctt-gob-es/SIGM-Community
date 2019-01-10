package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * [ecenpri-Felipe Ticket #39] Nuevo procedimiento Propuesta de Solicitud de Anuncio
 * @since 04.08.2010
 * @author Felipe
 */
public class AvisoAnuncio {

	public static String _ADVERTISE_MESSAGE = "Nuevo Anuncio Interno de la Diputación";
	
	public static void generarAvisoAnuncio(IRuleContext rulectx, int idTask, String sResponsable)
			throws ISPACException
	{
		
		IClientContext ctx = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
		String numexp = rulectx.getNumExp();		
		int processId = invesflowAPI.getProcess(numexp).getInt("ID");
//		ITask task = invesflowAPI.getTask(idTask);
//		String nombreTramite = task.getString("NOMBRE");
		IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
		String asunto = itemExpediente.getString("ASUNTO");
		//Enlace que dirige directamente al trámite
		String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + idTask +
			"\" class=\"displayLink\">" + _ADVERTISE_MESSAGE + "</a><br/>Asunto: " + asunto;
		
		AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, sResponsable, ctx);
	}
}
