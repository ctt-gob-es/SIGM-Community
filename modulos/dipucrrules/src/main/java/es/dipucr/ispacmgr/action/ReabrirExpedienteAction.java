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
	
	protected static final Logger LOGGER = Logger.getLogger(ReabrirExpedienteAction.class);

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
		
		int idProcess = 0;
		IProcess procesos = invesflowAPI.getProcess(numexp);
		
		if(procesos != null){
			idProcess = procesos.getKeyInt();
			procesos.set("ESTADO",  "1");
			procesos.store(cct);
		}		
		
		IItemCollection hitos = HitosUtils.getHitos(cct, numexp);
		int idFase = 0;
		if(hitos != null){
			for (Object oHito : hitos.toList()){
				IItem hito = (IItem)oHito;
				int idFaseAux = hito.getInt("ID_FASE");
				if(idFaseAux > idFase) {
					idFase = idFaseAux;
				}
			}
		}				
				
		ITXAction action = new TXReabrirExpediente(idProcess, idFase);
		run(action, cct, txTransaction);

	    String resp = workListAPI.getRespString();
		IItemCollection activeStagesSet = workListAPI.findActiveStages(processId, resp);
	    if (activeStagesSet.next())	{    
	      stageId = activeStagesSet.value().getInt("ID_STAGE");
	    }
	    
		TXTransactionDataContainer dataContainer = new TXTransactionDataContainer((ClientContext) cct);
		
		TXHitoDAO hito=dataContainer.newMilestone(idProcess, 0, 0, TXConstants.MILESTONE_EXPED_RELOCATED);
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

		try	{			
			dataContainer = new TXTransactionDataContainer((ClientContext) cct);	
			action.lock((ClientContext)cct,dataContainer);
			action.run((ClientContext)cct,dataContainer, itxTransaction);
			dataContainer.persist();
		} catch(ISPACException e) {
			dataContainer.setError();
			throw e;
		} catch(Exception e) {
			dataContainer.setError();
			throw new ISPACException(e);
		} finally {
			dataContainer.release();
		}
	}
	
	public IItem copiaExpediente(IClientContext cct, IItem expedienteViejo, String numexp, boolean reabriendo) throws ISPACException{

		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();

		IItem expedienteNuevo = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_EXPEDIENTES, numexp);
	
		expedienteNuevo.set("ID_PCD", expedienteViejo.getInt("ID_PCD"));
		expedienteNuevo.set("NUMEXP", expedienteViejo.getString("NUMEXP"));
		expedienteNuevo.set("REFERENCIA_INTERNA", expedienteViejo.getString("REFERENCIA_INTERNA"));
		expedienteNuevo.set("NREG", expedienteViejo.getString("NREG"));
		expedienteNuevo.set("FREG", expedienteViejo.getDate("FREG"));
		expedienteNuevo.set("ESTADOINFO", expedienteViejo.getString("ESTADOINFO"));
		expedienteNuevo.set("FESTADO", expedienteViejo.getDate("FESTADO"));
		expedienteNuevo.set("NIFCIFTITULAR", expedienteViejo.getString("NIFCIFTITULAR"));
		expedienteNuevo.set("IDTITULAR", expedienteViejo.getString("IDTITULAR"));
		expedienteNuevo.set("DOMICILIO", expedienteViejo.getString("DOMICILIO"));
		expedienteNuevo.set("CIUDAD", expedienteViejo.getString("CIUDAD"));
		expedienteNuevo.set("REGIONPAIS", expedienteViejo.getString("REGIONPAIS"));
		expedienteNuevo.set("CPOSTAL", expedienteViejo.getString("CPOSTAL"));
		expedienteNuevo.set("IDENTIDADTITULAR", expedienteViejo.getString("IDENTIDADTITULAR"));
		expedienteNuevo.set("TIPOPERSONA", expedienteViejo.getString("TIPOPERSONA"));
		expedienteNuevo.set("ROLTITULAR", expedienteViejo.getString("ROLTITULAR"));
		expedienteNuevo.set("ASUNTO", expedienteViejo.getString("ASUNTO"));
		expedienteNuevo.set("FINICIOPLAZO", expedienteViejo.getDate("FINICIOPLAZO"));
		expedienteNuevo.set("POBLACION", expedienteViejo.getString("POBLACION"));
		expedienteNuevo.set("MUNICIPIO", expedienteViejo.getString("MUNICIPIO"));
		expedienteNuevo.set("LOCALIZACION", expedienteViejo.getString("LOCALIZACION"));
		expedienteNuevo.set("EXPRELACIONADOS", expedienteViejo.getString("EXPRELACIONADOS"));
		expedienteNuevo.set("CODPROCEDIMIENTO", expedienteViejo.getString("CODPROCEDIMIENTO"));
		expedienteNuevo.set("NOMBREPROCEDIMIENTO", expedienteViejo.getString("NOMBREPROCEDIMIENTO"));
		expedienteNuevo.set("PLAZO", expedienteViejo.getInt("PLAZO"));
		expedienteNuevo.set("UPLAZO", expedienteViejo.getString("UPLAZO"));
		expedienteNuevo.set("FORMATERMINACION", expedienteViejo.getString("FORMATERMINACION"));
		expedienteNuevo.set("UTRAMITADORA", expedienteViejo.getString("UTRAMITADORA"));
		expedienteNuevo.set("FUNCIONACTIVIDAD", expedienteViejo.getString("FUNCIONACTIVIDAD"));
		expedienteNuevo.set("MATERIAS", expedienteViejo.getString("MATERIAS"));
		expedienteNuevo.set("SERVPRESACTUACIONES", expedienteViejo.getString("SERVPRESACTUACIONES"));
		expedienteNuevo.set("TIPODEDOCUMENTAL", expedienteViejo.getString("TIPODEDOCUMENTAL"));
		expedienteNuevo.set("PALABRASCLAVE", expedienteViejo.getString("PALABRASCLAVE"));
		expedienteNuevo.set("ESTADOADM", expedienteViejo.getString("ESTADOADM"));
		expedienteNuevo.set("HAYRECURSO", expedienteViejo.getString("HAYRECURSO"));
		expedienteNuevo.set("EFECTOSDELSILENCIO", expedienteViejo.getString("EFECTOSDELSILENCIO"));
		expedienteNuevo.set("FAPERTURA", expedienteViejo.getDate("FAPERTURA"));
		expedienteNuevo.set("OBSERVACIONES", expedienteViejo.getString("OBSERVACIONES"));
		expedienteNuevo.set("IDUNIDADTRAMITADORA", expedienteViejo.getString("IDUNIDADTRAMITADORA"));
		expedienteNuevo.set("IDPROCESO", expedienteViejo.getInt("IDPROCESO"));
		expedienteNuevo.set("TIPODIRECCIONINTERESADO", expedienteViejo.getString("TIPODIRECCIONINTERESADO"));
		expedienteNuevo.set("NVERSION", expedienteViejo.getString("NVERSION"));
		expedienteNuevo.set("IDSECCIONINICIADORA", expedienteViejo.getString("IDSECCIONINICIADORA"));
		expedienteNuevo.set("SECCIONINICIADORA", expedienteViejo.getString("SECCIONINICIADORA"));
		expedienteNuevo.set("TFNOFIJO", expedienteViejo.getString("TFNOFIJO"));
		expedienteNuevo.set("TFNOMOVIL", expedienteViejo.getString("TFNOMOVIL"));
		expedienteNuevo.set("DIRECCIONTELEMATICA", expedienteViejo.getString("DIRECCIONTELEMATICA"));
		expedienteNuevo.set("NUMEXP", expedienteViejo.getString("NUMEXP"));
		expedienteNuevo.set("VERSION", expedienteViejo.getString("VERSION"));
		expedienteNuevo.set("FECHA_APROBACION", expedienteViejo.getDate("FECHA_APROBACION"));
		expedienteNuevo.set("TIPOEXP", expedienteViejo.getString("TIPOEXP")); //[dipucr-Felipe #908]
		
		return expedienteNuevo;
	}
}
