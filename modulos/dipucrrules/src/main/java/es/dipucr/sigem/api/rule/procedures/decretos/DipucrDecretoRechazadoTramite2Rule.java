package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrDecretoRechazadoTramite2Rule implements IRule{

	private static final Logger logger = Logger.getLogger(DipucrDecretoRechazadoTramite2Rule.class);
	
	public void cancel(IRuleContext paramIRuleContext)
			throws ISPACRuleException {		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx)
			throws ISPACRuleException {
		try{
			//APIs
			/***************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
			IRespManagerAPI respAPI = invesFlowAPI.getRespManagerAPI();
			/***************************************************************************/
			
			//Comprobamos el estado del documento
			int taskId = rulectx.getTaskId();
			String sqlQuery = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '04'";
			IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");
			Iterator<IItem> itemCollectionIterator = itemCollection.iterator();
			// Generar un aviso en la bandeja de avisos electrónicos
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
			String asunto = itemExpediente.getString("ASUNTO");
			
			StringBuffer sbMessage = new StringBuffer();
			sbMessage.append("El decreto con número de expediente: "+rulectx.getNumExp()+" ha sido rechazado.");
			sbMessage.append("<br/>");
			sbMessage.append("Asunto: "+asunto);
			sbMessage.append("<br/>");
			sbMessage.append(" <b> Se procederá a cerrar el expediente. </b>");
			
			IItem itemProcess = invesFlowAPI.getProcess(rulectx.getNumExp());
			String sRespProceso = itemProcess.getString("ID_RESP");
			IResponsible responsable = respAPI.getResp(sRespProceso);
			String sNombrePropietario = responsable.getUID();
			
			int processId = invesFlowAPI.getProcess(rulectx.getNumExp()).getInt("ID");
			
			if(itemCollectionIterator.hasNext()){
				
				while(itemCollectionIterator.hasNext()){
					IItem item = (IItem) itemCollectionIterator.next();
					String estado= item.getString("ESTADOFIRMA");
					
					//Si el documento ha sido rechazado ejecutamo la llamada de rechazar decreto
					if(estado.equals("04")){
						//Si tiene número de decreto lo insertamos en rechazar
						String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
						IItemCollection tieneDecreto = entitiesAPI.queryEntities("SGD_DECRETO", consulta);
						int numDec = 0;
						try{
							numDec = ((IItem)tieneDecreto.iterator().next()).getInt("NUMERO_DECRETO");
						}
						catch(Exception e){numDec = 0;}
						if( numDec > 0){						
							String consulta2 = "WHERE ID_TRAM_EXP = "+taskId;
							IItemCollection tramitesAbiertos = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, consulta2);
							IItem tramiteDAO = (IItem) tramitesAbiertos.iterator().next();
							String motivoRechazo = tramiteDAO.getString("OBSERVACIONES");
							
							//Creamos el rechazo
							IItem entidad_rechazo = entitiesAPI.createEntity("SGD_RECHAZO_DECRETO", rulectx.getNumExp());
							entidad_rechazo.set("RECHAZO_DECRETO", motivoRechazo);
							entidad_rechazo.store(cct);
						}
						//Cerramos el expediente ya que el trámite ya está cerrado					
						tx.closeProcess(rulectx.getProcessId());
						rulectx.setInfoMessage("El expediente "+rulectx.getNumExp()+" ha sido cerrado.");
						AvisosUtil.generarAviso(entitiesAPI, processId, rulectx.getNumExp(), sbMessage.toString(), sNombrePropietario, cct);
					}
				}
			}
			else{
				IItem item = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
				if(item.getString("ESTADOADM").equals("RC")){
					AvisosUtil.generarAviso(entitiesAPI, processId, rulectx.getNumExp(), sbMessage.toString(), sNombrePropietario, cct);
					rulectx.setInfoMessage("El expediente "+rulectx.getNumExp()+" ha sido cerrado.");
				}
			}
		}
		
			
		catch(Exception e){
			logger.error("ERROR.DecretoRechazadoTramite2Rule: "+e.getMessage(), e);
		}		
		return null;
	}

	public boolean init(IRuleContext paramIRuleContext)
			throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext paramIRuleContext)
			throws ISPACRuleException { 
		return true;
	}

}
