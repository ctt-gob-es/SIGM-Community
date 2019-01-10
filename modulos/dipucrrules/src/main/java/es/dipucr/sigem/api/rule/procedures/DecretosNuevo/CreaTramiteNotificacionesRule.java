package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class CreaTramiteNotificacionesRule implements IRule{
	private static final Logger logger = Logger.getLogger(CreaTramiteNotificacionesRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		

		try{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			
			//Compruebo que no haya sido rechazado el decreto
			IItem item = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
			logger.warn("Numexp: "+rulectx.getNumExp()+" - "+item.getString("ESTADOADM"));
			logger.warn("Numexp: "+item.getString("NUMEXP")+" - "+item.getString("ID_PCD"));
			String estadoAdm = item.getString("ESTADOADM");
			if(!estadoAdm.equals("RC")){
				//Compruebo el documento para que no se haya rechazado
				int taskId = rulectx.getTaskId();
				String sqlQuery = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '04'";
				IItemCollection itemCollection = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), sqlQuery, "");
				Iterator<IItem> itemCollectionIterator = itemCollection.iterator();
				if(!itemCollectionIterator.hasNext()){
					String sqlQueryPart = "WHERE (ROL != 'TRAS' OR ROL IS NULL) AND NUMEXP = '"+rulectx.getNumExp()+"' ORDER BY ID";	
					IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
					
					// 2. Comprobar que hay algún participante para el cual generar su notificación
					if (participantes==null || participantes.toList().size()<=0) {
						
						String numexp = rulectx.getNumExp();		
						int processId = invesflowAPI.getProcess(numexp).getInt("ID");
						IItem itemExpediente = entitiesAPI.getExpedient(numexp);
						String asunto = "";
						if(itemExpediente.getString("ASUNTO")!=null) asunto = itemExpediente.getString("ASUNTO");
						//Enlace que dirige directamente al trámite
						String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?stageId=" + rulectx.getStageId() +
								"\" class=\"displayLink\"> No existen documentos de notificaciones para generar </a><br/>Asunto: " + asunto;
						AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, ResponsablesUtil.get_ID_RESP_Fase(rulectx), cct);
						
					}
					else{
						TramitesUtil.crearTramite("Not-Decr", rulectx);
					}
				}
				logger.warn("El expediente "+rulectx.getNumExp()+ "va a ser cerrado.");
			}
			
		} catch(Exception e) {
			logger.error("Error al recuperar los documentos.: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
