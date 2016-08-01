package es.dipucr.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispactx.TXConstants;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;
import ieci.tdw.ispac.resp.ResponsibleHelper;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ReabrirTramiteCerradoAction extends BaseAction {
	
	protected static final Logger logger = Logger.getLogger(ReabrirTramiteCerradoAction.class);

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		/**
		 * [Teresa Ticket#434#]Fin Reabrir tramite
		 * **/
		IInvesflowAPI invesflowAPI = session.getAPI();
    	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
    	ClientContext cct = (ClientContext) session.getClientContext();
		//[Manu Ticket #83] INICIO - ALSIGM3 Añadir el campo RESP al reabrir trámite
 		IRespManagerAPI respManagerAPI = invesflowAPI.getRespManagerAPI();

    	
    	//Numero del expediente
		String numexp = request.getParameter("numexp");
		//Tramite que se quiere reabrir
		String taskid = request.getParameter("taskid");
		
		//stagePcdIdActual
		String stagePcdIdActual = request.getParameter("stagePcdIdActual");
		int iStagePcdIdActual = Integer.parseInt(stagePcdIdActual);
		
		//Saco el idTask
		String sqlQueryPart = "WHERE NUMEXP = '"+numexp+"' AND ID_TRAM_EXP="+taskid+" AND ID_FASE_PCD="+stagePcdIdActual;
		logger.warn("sqlQueryPart "+sqlQueryPart);
		IItemCollection tramCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, sqlQueryPart);
		Iterator<?> tramIterator = tramCollection.iterator();
		
		if(tramIterator.hasNext()){
			
			IItem itemTramite = (IItem) tramIterator.next();
		
			//INICIO [dipucr-Felipe #1113]
	    	if (esTramiteProtegido(cct, itemTramite)){
	    		ISPACInfo informacion = new ISPACInfo("Este es un trámite especialmente protegido "
	    				+ "y no se puede reabrir. Si es indispensable, consulte con el administrador", "", false);
				request.getSession().setAttribute("infoAlert", informacion);
	    	}
	    	else{//FIN [dipucr-Felipe #1113]
    		
	    		int idTramite = itemTramite.getKeyInt();
				int id_tram_pcd = itemTramite.getInt("ID_TRAM_PCD");
				int id_fase_pdc = itemTramite.getInt("ID_FASE_EXP");
				
				//[Manu Ticket #83] * ALSIGM3 Añadir el campo RESP al reabrir trámite
				String id_resp_closed = itemTramite.getString("ID_RESP_CLOSED");
				
				int id_process = 0;
				String id_fase_bpm = "";
				
				IProcess procesos = invesflowAPI.getProcess(numexp);
				if(procesos != null){
					id_process = procesos.getKeyInt();
				}
					
				//Obtengo la fase activa
				String strQueryAux = "WHERE NUMEXP='" + numexp + "'";
				IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
				Iterator <IItem> itspacFases = collExpsAux.iterator();
				if(itspacFases.hasNext()){
					IItem faseActiva = itspacFases.next();
					id_fase_bpm = faseActiva.getString("ID_FASE_BPM");
					
					logger.warn("id_fase_pdc "+id_fase_pdc+" id_fase_bpm "+id_fase_bpm);
					
					//Si las dos fases son iguales se puede abrir trámite
					if(id_fase_bpm.equals(id_fase_pdc+"")){
						//Calculamos el responsable de trámite
						 String strQuery = "WHERE ID_TRAMITE_BPM = '"+id_tram_pcd+"'";
					     IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
						Iterator <IItem> it = collection.iterator();
					     String responsable="";
					     
				         if (it.hasNext())
				         {
				        	IItem itPTramites = it.next();
				        	responsable = itPTramites.getString("ID_RESP");
				         }
				         
				         //[Manu Ticket #83] INICIO - ALSIGM3 Añadir el campo RESP al reabrir trámite
				         String nombreRespId = "";
				         if(StringUtils.isEmpty(id_resp_closed)){
				        	 if(StringUtils.isEmpty(responsable)){
				        		 IItem stage = invesflowAPI.getStage(faseActiva.getInt("ID"));
				        		 responsable = stage.getString("ID_RESP");
				        	 }
				        	 nombreRespId = "'"+ ((Responsible)respManagerAPI.getResp(responsable)).getName() +"' ";				        	 
				         }
				         else{
				        	 nombreRespId = "'"+ ((Responsible)respManagerAPI.getResp(id_resp_closed)).getName() +"' ";
				         }
				         //[Manu Ticket #83] FIN - ALSIGM3 Añadir el campo RESP al reabrir trámite
				         
				         if(responsable==null){
				        	 responsable = "b.id_resp ";
				         }
				         else{
				        	 responsable = "'"+responsable+"' ";
				         }
				         
						//sRespTramite = task.getString("ID_RESP"); No esta cargado todavía el trámite
				         
						
						//Reabrir el trámite
						StringBuffer primerInsertSpacTram = new StringBuffer ("insert into spac_tramites " +
								"(id, id_tramite_bpm, id_exp, id_pcd, id_fase_exp, id_fase_pcd, id_tramite, " +
								"id_cttramite, numexp, nombre, fecha_inicio, estado, tipo, id_resp, resp) " +
								"select a.id_tram_exp, a.id_tram_exp, cast(b.id_proceso_bpm as int), b.id_pcd, a.id_fase_exp, a.id_fase_pcd, " +
								"a.id_tram_pcd, a.id_tram_ctl, a.numexp, a.nombre, a.fecha_inicio, 1, 1, "+responsable + 
								//[Manu Ticket #83] INICIO - ALSIGM3 Añadir el campo RESP al reabrir trámite
								", " + nombreRespId +
								" from spac_dt_tramites a, spac_procesos b " +
								//[Manu Ticket #83] FIN - ALSIGM3 Añadir el campo RESP al reabrir trámite
								"where a.numexp = b.numexp and a.id = "+idTramite+"");
		
						StringBuffer primerUpdateSpacDtTra = new StringBuffer ("update spac_dt_tramites set estado = 1 " +
								"where id = "+idTramite+"");
						StringBuffer segUpdateSpacDtTra = new StringBuffer ("update spac_dt_tramites set fecha_cierre = null" +
								" where id = "+idTramite+"");
						
						//realizar la consulta
						DbCnt context = cct.beginTX();
						context.execute(primerInsertSpacTram.toString());
						context.execute(primerUpdateSpacDtTra.toString());
						context.execute(segUpdateSpacDtTra.toString());
						
						TXTransactionDataContainer dataContainer = new TXTransactionDataContainer(cct);
						
						TXHitoDAO hito=dataContainer.newMilestone(id_process,iStagePcdIdActual, id_tram_pcd, TXConstants.MILESTONE_TASK_RESTART);
						hito.set("INFO", composeInfo(taskid));
						hito.store(cct);
					
						context.closeTX(true);
						context.closeConnection();		
						
					}
					else{
						ISPACInfo informacion=new ISPACInfo("El trámite que deseas abrir está en otra fase, retrocede o avanza fase hasta que el trámite se visualice en 'Datos de Trámites'", "",false);
						request.getSession().setAttribute("infoAlert", informacion);
						
					}
				}
				else{
					ISPACInfo informacion=new ISPACInfo("El expediente está cerrado, no se puede reabrir trámite", "",false);
					request.getSession().setAttribute("infoAlert", informacion);
				}
	    	}
		}
    	
		ActionForward actionForward = new ActionForward();
		String action = "/showTask.do";
		String params = "?numexp="+numexp+"&taskId="+taskid+"&stagePcdIdActual="+stagePcdIdActual;
		actionForward.setPath(action + params);
		actionForward.setRedirect(true);
		
		return actionForward;
		
	}
	
	/**
	 * [dipucr-Felipe #1113]
	 * Comprueba si el trámite es de tipo protegido
	 * @param cct
	 * @param itemTramite
	 * @return
	 * @throws ISPACException
	 */
	private boolean esTramiteProtegido(IClientContext cct, IItem itemTramite) 
			throws ISPACException
	{
		try{
			int idCTTramite = itemTramite.getInt("ID_TRAM_CTL");
			String sCodTramiteActual = TramitesUtil.getCodTram(idCTTramite, cct);
			
			//Comprobamos si en la tabla de validación existe el código
			String strQuery = "WHERE VALOR = '" + sCodTramiteActual + "' AND VIGENTE = 1";
			IItemCollection collection = null;
			try{ //Por si no existiera la tabla
				collection = cct.getAPI().getEntitiesAPI().queryEntities
						("DPCR_VLDTBL_TRAM_PROTEGIDOS", strQuery);
			}
			catch(Exception ex1){
				return false;
			}
			
			//Vemos si el código de trámite está en la tabla
			if (collection.toList().size() > 0){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception ex){
			throw new ISPACException("Error al comprobar si "
					+ "el trámite a reabrir es de tipo protegido", ex);
		}
	}

	/**
	 * 
	 * @param mnIdTask
	 * @return
	 */
	private String composeInfo(String mnIdTask){
		return new StringBuffer()
			.append("<?xml version='1.0' encoding='ISO-8859-1'?>")
			.append("<infoaux><id_tramite>")
			.append(mnIdTask)
			.append("</id_tramite></infoaux>")
			.toString();
	}

}
