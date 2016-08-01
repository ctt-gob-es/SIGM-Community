package es.dipucr.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.IWorklistAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispactx.ITXAction;
import ieci.tdw.ispac.ispactx.TXConstants;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.ispactx.tx.TXReabrirExpediente;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.HitosUtils;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ReabrirExpedienteAction extends BaseAction {
	
	protected static final Logger logger = Logger.getLogger(ReabrirExpedienteAction.class);

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		int stageId = 0;
		
		IInvesflowAPI invesflowAPI = session.getAPI();
		IClientContext cct = (ClientContext) session.getClientContext();
    	ITXTransaction txTransaction = invesflowAPI.getTransactionAPI();
		IWorklistAPI workListAPI = invesflowAPI.getWorkListAPI();
    	
    	//Numero del expediente
		String numexp = request.getParameter("numexp");
		
		//processId
		int processId = Integer.parseInt(request.getParameter("processId"));		
		
		int id_process = 0;
		IProcess procesos = invesflowAPI.getProcess(numexp);
		
		if(procesos != null){
			id_process = procesos.getKeyInt();
			procesos.set("ESTADO",  "1");
			procesos.store(cct);
		}		
		
		IItemCollection hitos = HitosUtils.getHitos(cct, numexp);
		int id_fase = 0;
		if(hitos != null){
			for (Object oHito : hitos.toList()){
				IItem hito = (IItem)oHito;
				int id_fase_aux = hito.getInt("ID_FASE");
				if(id_fase_aux > id_fase) id_fase = id_fase_aux;
			}
		}
				
		//txTransaction.openPreviousStage(id_process, id_fase);	
		
		ITXAction action = new TXReabrirExpediente(id_process, id_fase);
		run(action, cct, txTransaction);

	    String resp = workListAPI.getRespString();
		IItemCollection activeStagesSet = workListAPI.findActiveStages(processId, resp);
	    if (activeStagesSet.next())	    
	      stageId = activeStagesSet.value().getInt("ID_STAGE");
	      
		TXTransactionDataContainer dataContainer = new TXTransactionDataContainer((ClientContext) cct);
		
		TXHitoDAO hito=dataContainer.newMilestone(id_process, 0, 0, TXConstants.MILESTONE_EXPED_RELOCATED);
//		hito.set("INFO", composeInfo(numexp));
		hito.store(cct);	
		
		if(procesos != null){
			procesos.set("ESTADO",  "1");
			procesos.store(cct);
		}
	
		//[Manu Ticket #68] * ALSIGM3 Al reabrir trámite no se borra la fecha de cierre del expediente
		cct.beginTX();
		IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
		int idExp = expediente.getInt("ID");
		
		IItem expedienteNuevo = copiaExpediente(cct, expediente, numexp, true);		
		expediente.delete(cct);		
		
		expedienteNuevo.set("ID", idExp);
		expedienteNuevo.store(cct);
		cct.endTX(true);
		//[Manu Ticket #68] * ALSIGM3 Al reabrir trámite no se borra la fecha de cierre del expediente
				
		IItemCollection tramites = TramitesUtil.getTramites(cct, numexp);
		if(tramites != null){
			for(Object oTramite: tramites.toList()){
				IItem tramite = (IItem) oTramite;
				Integer id = null;
				tramite.set("ID_SUBPROCESO", id);
				tramite.store(cct);
			}
		}
		
		ActionForward showexp = mapping.findForward("showexp");
		return new ActionForward(showexp.getName(),				
	        showexp.getPath() + "?numexp="+numexp+"&stageId="+stageId, true);
	}
	
	protected void run(ITXAction action, IClientContext cct, ITXTransaction itxTransaction) throws ISPACException{
		TXTransactionDataContainer dataContainer = null;

		try
		{			
			dataContainer = new TXTransactionDataContainer((ClientContext) cct);	
			action.lock((ClientContext)cct,dataContainer);
			action.run((ClientContext)cct,dataContainer, itxTransaction);
			dataContainer.persist();
		}
		catch(ISPACException e)
		{
			dataContainer.setError();
			throw e;
		}
		catch(Exception e)
		{
			dataContainer.setError();
			throw new ISPACException(e);
		}
		finally
		{
			dataContainer.release();
			}
	}
	
	public IItem copiaExpediente(IClientContext cct, IItem expediente_viejo, String numexp, boolean reabriendo) throws ISPACException{

		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();

		IItem expediente_nuevo = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_EXPEDIENTES, numexp);
	
		expediente_nuevo.set("ID_PCD", expediente_viejo.getInt("ID_PCD"));
		expediente_nuevo.set("NUMEXP", expediente_viejo.getString("NUMEXP"));
		expediente_nuevo.set("REFERENCIA_INTERNA", expediente_viejo.getString("REFERENCIA_INTERNA"));
		expediente_nuevo.set("NREG", expediente_viejo.getString("NREG"));
		expediente_nuevo.set("FREG", expediente_viejo.getDate("FREG"));
		expediente_nuevo.set("ESTADOINFO", expediente_viejo.getString("ESTADOINFO"));
		expediente_nuevo.set("FESTADO", expediente_viejo.getDate("FESTADO"));
		expediente_nuevo.set("NIFCIFTITULAR", expediente_viejo.getString("NIFCIFTITULAR"));
		expediente_nuevo.set("IDTITULAR", expediente_viejo.getString("IDTITULAR"));
		expediente_nuevo.set("DOMICILIO", expediente_viejo.getString("DOMICILIO"));
		expediente_nuevo.set("CIUDAD", expediente_viejo.getString("CIUDAD"));
		expediente_nuevo.set("REGIONPAIS", expediente_viejo.getString("REGIONPAIS"));
		expediente_nuevo.set("CPOSTAL", expediente_viejo.getString("CPOSTAL"));
		expediente_nuevo.set("IDENTIDADTITULAR", expediente_viejo.getString("IDENTIDADTITULAR"));
		expediente_nuevo.set("TIPOPERSONA", expediente_viejo.getString("TIPOPERSONA"));
		expediente_nuevo.set("ROLTITULAR", expediente_viejo.getString("ROLTITULAR"));
		expediente_nuevo.set("ASUNTO", expediente_viejo.getString("ASUNTO"));
		expediente_nuevo.set("FINICIOPLAZO", expediente_viejo.getDate("FINICIOPLAZO"));
		expediente_nuevo.set("POBLACION", expediente_viejo.getString("POBLACION"));
		expediente_nuevo.set("MUNICIPIO", expediente_viejo.getString("MUNICIPIO"));
		expediente_nuevo.set("LOCALIZACION", expediente_viejo.getString("LOCALIZACION"));
		expediente_nuevo.set("EXPRELACIONADOS", expediente_viejo.getString("EXPRELACIONADOS"));
		expediente_nuevo.set("CODPROCEDIMIENTO", expediente_viejo.getString("CODPROCEDIMIENTO"));
		expediente_nuevo.set("NOMBREPROCEDIMIENTO", expediente_viejo.getString("NOMBREPROCEDIMIENTO"));
		expediente_nuevo.set("PLAZO", expediente_viejo.getInt("PLAZO"));
		expediente_nuevo.set("UPLAZO", expediente_viejo.getString("UPLAZO"));
		expediente_nuevo.set("FORMATERMINACION", expediente_viejo.getString("FORMATERMINACION"));
		expediente_nuevo.set("UTRAMITADORA", expediente_viejo.getString("UTRAMITADORA"));
		expediente_nuevo.set("FUNCIONACTIVIDAD", expediente_viejo.getString("FUNCIONACTIVIDAD"));
		expediente_nuevo.set("MATERIAS", expediente_viejo.getString("MATERIAS"));
		expediente_nuevo.set("SERVPRESACTUACIONES", expediente_viejo.getString("SERVPRESACTUACIONES"));
		expediente_nuevo.set("TIPODEDOCUMENTAL", expediente_viejo.getString("TIPODEDOCUMENTAL"));
		expediente_nuevo.set("PALABRASCLAVE", expediente_viejo.getString("PALABRASCLAVE"));
		expediente_nuevo.set("ESTADOADM", expediente_viejo.getString("ESTADOADM"));
		expediente_nuevo.set("HAYRECURSO", expediente_viejo.getString("HAYRECURSO"));
		expediente_nuevo.set("EFECTOSDELSILENCIO", expediente_viejo.getString("EFECTOSDELSILENCIO"));
		expediente_nuevo.set("FAPERTURA", expediente_viejo.getDate("FAPERTURA"));
		expediente_nuevo.set("OBSERVACIONES", expediente_viejo.getString("OBSERVACIONES"));
		expediente_nuevo.set("IDUNIDADTRAMITADORA", expediente_viejo.getString("IDUNIDADTRAMITADORA"));
		expediente_nuevo.set("IDPROCESO", expediente_viejo.getInt("IDPROCESO"));
		expediente_nuevo.set("TIPODIRECCIONINTERESADO", expediente_viejo.getString("TIPODIRECCIONINTERESADO"));
		expediente_nuevo.set("NVERSION", expediente_viejo.getString("NVERSION"));
		expediente_nuevo.set("IDSECCIONINICIADORA", expediente_viejo.getString("IDSECCIONINICIADORA"));
		expediente_nuevo.set("SECCIONINICIADORA", expediente_viejo.getString("SECCIONINICIADORA"));
		expediente_nuevo.set("TFNOFIJO", expediente_viejo.getString("TFNOFIJO"));
		expediente_nuevo.set("TFNOMOVIL", expediente_viejo.getString("TFNOMOVIL"));
		expediente_nuevo.set("DIRECCIONTELEMATICA", expediente_viejo.getString("DIRECCIONTELEMATICA"));
		expediente_nuevo.set("NUMEXP", expediente_viejo.getString("NUMEXP"));
		expediente_nuevo.set("VERSION", expediente_viejo.getString("VERSION"));
		expediente_nuevo.set("FECHA_APROBACION", expediente_viejo.getDate("FECHA_APROBACION"));
		expediente_nuevo.set("TIPOEXP", expediente_viejo.getString("TIPOEXP")); //[dipucr-Felipe #908]
		
		return expediente_nuevo;
	}
}
