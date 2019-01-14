package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;

/**
 * [ecenpri-Felipe Ticket #739]
 * Utilidad para operar con los responsables de un trámite, fase o procedimiento
 * @since 16.10.2010
 * @author Felipe
 */
public class ResponsablesUtil {
		
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(ResponsablesUtil.class);
	
	/**
	 * 
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static String getMailsResponsableTramite(IRuleContext rulectx) throws ISPACException{
		
		//Obtenemos el responsable del trámite
//		IResponsible resp = cct.getResponsible(); LO COGE DEL PROCEDIMIENTO
		IResponsible resp = getRespTramite(rulectx);
		String respName = resp.getName();
		String listaMails = null;
		
		//Obtenemos la entidad
		ClientContext cct = (ClientContext) rulectx.getClientContext();
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		AccesoBBDDRegistro accesoRegistro;
		
		try {
			accesoRegistro = new AccesoBBDDRegistro(entidad);
		
			if (resp.isUser()){
				//[Manu Ticket #739] * SIGEM Regla genérica para enviar un mail al usuario, grupo o dept responsable del trámite (al iniciar)
				listaMails = accesoRegistro.getEmailUsuario(resp.getUID());
				//listaMails = accesoRegistro.getEmailUsuario(respName);
				//[Manu Ticket #739] * SIGEM Regla genérica para enviar un mail al usuario, grupo o dept responsable del trámite (al iniciar)
			}
			else if (resp.isGroup()){
				listaMails = accesoRegistro.getEmailGrupo(respName);
			}
			else if (resp.isOrgUnit()){
				listaMails = accesoRegistro.getEmailDepartamento(respName);	
			}
			
			return listaMails;
			
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el mail del responsable del trámite", e);
		}	
	}
	
	
	/**
	 * 
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static String getMailsResponsable(ClientContext cct, IResponsible responsable) throws ISPACException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		AccesoBBDDRegistro accesoRegistro;
		String listaMails = null;
		
		try {
			accesoRegistro = new AccesoBBDDRegistro(entidad);
		
			if (responsable.isUser()){
				//[Manu Ticket #739] * SIGEM Regla genérica para enviar un mail al usuario, grupo o dept responsable del trámite (al iniciar)
				listaMails = accesoRegistro.getEmailUsuario(responsable.getUID());
				//listaMails = accesoRegistro.getEmailUsuario(respName);
				//[Manu Ticket #739] * SIGEM Regla genérica para enviar un mail al usuario, grupo o dept responsable del trámite (al iniciar)
			}
			else if (responsable.isGroup()){
				listaMails = accesoRegistro.getEmailGrupo(responsable.getName());
			}
			else if (responsable.isOrgUnit()){
				listaMails = accesoRegistro.getEmailDepartamento(responsable.getName());	
			}
			
			return listaMails;
			
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el mail del responsable del trámite", e);
		}	
	}
	
	/**
	 * Devuelve el id del responsable del trámite
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static IResponsible getRespTramite(IRuleContext rulectx) throws ISPACException{
	
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			ITask task = invesflowAPI.getTask(rulectx.getTaskId());
			//sRespTramite = task.getString("ID_RESP"); No esta cargado todavía el trámite
			IItem taskPCD = invesflowAPI.getProcedureTaskPCD(task.getInt("ID_TRAMITE"));
			String sIdRespTramite = taskPCD.getString("ID_RESP");
			//INICIO [dipucr-Felipe #1151]
			//Para los casos en los que el responsable se setea en tiempo de ejecución
			if (StringUtils.isEmpty(sIdRespTramite)){
				sIdRespTramite = task.getString("ID_RESP");
			}
			//FIN [dipucr-Felipe #1151]
			return getResponsible(cct, sIdRespTramite);
		
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el responsable del trámite", e);
		}
	}
	
	/**
	 * Devuelve la interfaz IResponsible a partir de un ID_RESP
	 * @param cct
	 * @param idResp
	 * @return
	 * @throws ISPACException
	 */
	public static IResponsible getResponsible(IClientContext cct, String idResp) throws ISPACException{
		
		IRespManagerAPI respAPI = cct.getAPI().getRespManagerAPI();
		return respAPI.getResp(idResp);
	}
	
	/**
	 * Devuelve el id del responsable del Procedimiento
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static IResponsible getRespProcedimiento(IRuleContext rulectx) throws ISPACException{
	
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			IItem procedure = invesflowAPI.getProcedure(rulectx.getProcedureId());
			String sIdRespTramite = procedure.getString("ID_RESP");
			return getResponsible(cct, sIdRespTramite);
		
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el responsable del procedimiento", e);
		}
	}
	
	/**
	 * Devuelve el id del responsable del Procedimiento
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static IResponsible getRespProcedimientoByNumexp(IRuleContext rulectx, String numexp) throws ISPACException{
	
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			String sId_pcd = ExpedientesUtil.getExpediente(cct, numexp).getString("ID_PCD");
			int id_pcd = 0;
			if(StringUtils.isNotEmpty(sId_pcd)){
				id_pcd = Integer.parseInt(sId_pcd);
			}
			IItem procedure = invesflowAPI.getProcedure(id_pcd);
			String sIdRespTramite = procedure.getString("ID_RESP");
			return getResponsible(cct, sIdRespTramite);
		
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el responsable del procedimiento", e);
		}
	}
	
	/**
	 * Asigna responsable a un trámite
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static void setRespTramite(IRuleContext rulectx, IResponsible responsable) throws ISPACException{
	
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			IItem tramite = invesflowAPI.getTask(rulectx.getTaskId());
			tramite.set("ID_RESP", responsable.getUID());
			tramite.store(cct);
		
		} catch (ISPACException e) {
			throw new ISPACException("Error al asignar responsable al trámite", e);
		}
	}
	
	/**
	 * Asigna responsable a un trámite
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static void setRespTramite(IRuleContext rulectx, int id_tramite, IResponsible responsable) throws ISPACException{
	
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			IItem tramite = invesflowAPI.getTask(id_tramite);
			tramite.set("ID_RESP", responsable.getUID());
			tramite.store(cct);
		
		} catch (ISPACException e) {
			throw new ISPACException("Error al asignar responsable al trámite", e);
		}
	}
	
	/**
	 * Devuelve el responsable del usuario actual.
	 * @param rulectx
	 * @return
	 * @throws ISPACException
	 */	
	public static String getRespSesion(IRuleContext rulectx){
		return rulectx.getClientContext().getRespId();
	}
	
	/**
	 * Devuelve el responsable asignado en la definición del procedimiento y, si no tiene 
	 * (no debería darse el caso), devuelve el responsable del usuario actual.
	 * @param rulectx
	 * @return
	 * @throws ISPACException
	 */	
	public static String get_ID_RESP_Procedimiento(IRuleContext rulectx) throws ISPACException{
		
		String sIdRespTramite = "";
		
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			IItem procedure = invesflowAPI.getProcedure(rulectx.getProcedureId());
			sIdRespTramite = procedure.getString("ID_RESP");
			sIdRespTramite = setDefaultResp(sIdRespTramite,getRespSesion(rulectx));
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el responsable del procedimiento.", e);
		}
		return sIdRespTramite;
	}
	
	/**
	 * Devuelve el responsable asignado en la definición de la fase, si no tiene devuelve el 
	 * del procedimiento, y (no debería darse el caso) si no tiene, devuelve
	 * el responsable del usuario actual.
	 * @param rulectx
	 * @return
	 * @throws ISPACException
	 */	
	public static String get_ID_RESP_Fase(IRuleContext rulectx) throws ISPACException{
		
		String sIdRespTramite = "";
		
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			IItem stage = invesflowAPI.getStage(rulectx.getStageId());
			sIdRespTramite = stage.getString("ID_RESP");
			sIdRespTramite = setDefaultResp(sIdRespTramite,get_ID_RESP_Procedimiento(rulectx));
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el responsable de la fase.", e);
		}
		return sIdRespTramite;
	}

	/**
	 * Devuelve el responsable asignado en la definición del trámite, si no tiene, devuelve el de la fase,
	 * si sigue sin tener devuelve el del procedimiento, y (no debería darse el caso) si no tiene, devuelve
	 * el responsable del usuario actual.
	 * @param rulectx
	 * @return
	 * @throws ISPACException
	 */
	public static String get_ID_RESP_Tramite(IRuleContext rulectx) throws ISPACException{
		
		String sIdRespTramite = "";
		
		IClientContext cct = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		try{
			IItem task = invesflowAPI.getTask(rulectx.getTaskId());
			sIdRespTramite = task.getString("ID_RESP");
			sIdRespTramite = setDefaultResp(sIdRespTramite,get_ID_RESP_Fase(rulectx));
		} catch (ISPACException e) {
			throw new ISPACException("Error al recuperar el responsable del trámite.", e);
		}
		return sIdRespTramite;
	}
	
	private static String setDefaultResp(String objectResp ,String sRespId)	throws ISPACException{
		if (objectResp == null || objectResp.length()==0 ){
			objectResp = sRespId;
		}
		return objectResp;
	}
}
