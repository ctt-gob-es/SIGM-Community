package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrAvisoNuevoExpedienteRelacionadoRule implements IRule {
	private static final Logger logger = Logger.getLogger(DipucrAvisoNuevoExpedienteRelacionadoRule.class);
	public static String _NUEVOEXPEDIENTE_MESSAGE = "Nuevo Expediente ";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			//Comprobamos si el expediente tiene un expediente padre
			
			String consultaSQL = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp()+ "'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
		    if(itemCollection.iterator().hasNext()){
		        Iterator<?> iterExpRelacionado = itemCollection.iterator();
		        String numexpPadre = ((IItem)iterExpRelacionado.next()).getString("NUMEXP_PADRE");
		        
		        //Recuperamos la información del expediente padre
		    	IItem expPadre = ExpedientesUtil.getExpediente(ctx, numexpPadre);
		    			
		    	if(expPadre != null){
		    		
		    		String nombreProcedimientoPadre = invesflowAPI.getProcedure(expPadre.getInt("ID_PCD")).getString("NOMBRE");						
					
					String sRespFase = null;
					String responsableId = "";
					
					String numexp = rulectx.getNumExp();
					
					IItem itemProcess = invesflowAPI.getProcess(numexp);
					String sRespProceso = itemProcess.getString("ID_RESP");
					
					IStage stage = invesflowAPI.getStage(rulectx.getStageId());
					sRespFase = stage.getString("ID_RESP");
					
					//Tomamos el responsable de la fase actual, si no existe, el responsable del procedimiento
					if(sRespFase != null && !sRespFase.equals("")){
						responsableId = sRespFase;
					}
					else{
						responsableId = sRespProceso;
					}
					
					int processId = invesflowAPI.getProcess(numexp).getInt("ID");
					String nombreProcedimiento = invesflowAPI.getProcedure(rulectx.getProcedureId()).getString("NOMBRE");
					IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
					String asunto = itemExpediente.getString("ASUNTO");
					String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + rulectx.getTaskId() +
						"\" class=\"displayLink\">" + _NUEVOEXPEDIENTE_MESSAGE +  nombreProcedimiento + " originado por expediente: " + nombreProcedimientoPadre+
						"</a>.<br/>Asunto: " + asunto;
					AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, responsableId, ctx);
		    	}
		    }
			return true;
			
		} catch (Exception e){
			logger.error("Error al notificar al nuevo responsable del trámite. " + e.getMessage(), e);
			
			throw new ISPACRuleException
				("Error al notificar al nuevo responsable del trámite. " + e.getMessage(), e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
