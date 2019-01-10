package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.security.SecurityMgr;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;


public class AvisoComprobarDecretoRespTramiteRule implements IRule {
	
	public static String _NEWTASK_MESSAGE = "Nuevo Trámite de Decreto ";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		String respName = TramitesUtil.getDatosEspecificosOtrosDatos
				(rulectx.getClientContext(), rulectx.getTaskProcedureId());
		if (StringUtils.isEmpty(respName)){
			rulectx.setInfoMessage("Consulte con el Administrador. Debe rellenar el nombre del usuario responsable del "
					+ "trámite dentro de la pestaña 'Datos Específicos' del trámite en el Catálogo de Procedimientos");
			return false;
		}
		else{
			return true;
		}
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IRespManagerAPI respAPI = invesflowAPI.getRespManagerAPI();
			
			String sNombrePropietario = null;
			
			String numexp = rulectx.getNumExp();
			
			IItem itemProcess = invesflowAPI.getProcess(numexp);
			String sRespProceso = itemProcess.getString("ID_RESP");

			IResponsible responsable = respAPI.getResp(sRespProceso);
			sNombrePropietario = responsable.getName();
			
			ITask task = invesflowAPI.getTask(rulectx.getTaskId());
			
			String respName = TramitesUtil.getDatosEspecificosOtrosDatos(ctx, rulectx.getTaskProcedureId());
			String respId = "1-" + UsuariosUtil.getCampoUsuario((ClientContext) ctx, respName, "ID");
				
			int processId = invesflowAPI.getProcess(numexp).getInt("ID");
			String nombreTramite = task.getString("NOMBRE");
			IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
			String asunto = itemExpediente.getString("ASUNTO");
			String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + rulectx.getTaskId() +
				"\" class=\"displayLink\">" + _NEWTASK_MESSAGE +  nombreTramite + " desde " + sNombrePropietario +
				"</a>.<br/>Asunto: " + asunto;
			
			AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, respId, ctx);
			
			// Obtener los responsables sustituidos
			SecurityMgr secMgr = new SecurityMgr(ctx.getConnection());
			IItemCollection substitutes = secMgr.getSubstitutesAssets(respId);
        	while (substitutes.next()) {

        		IItem substitute = (IItem) substitutes.value();
        		String respIdSubs = substitute.getString("SUSTITUCION:UID_SUSTITUTO");
    			AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, respIdSubs, ctx);
        	}
			
			return true;
			
		} catch (Exception e){
			
			throw new ISPACRuleException
				("Error al notificar al responsable del trámite.", e);
		}
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
