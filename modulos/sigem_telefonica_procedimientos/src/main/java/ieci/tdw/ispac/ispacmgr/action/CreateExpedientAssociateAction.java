package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.procedure.PProcedimientoDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.directory.DirectoryConnectorFactory;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryConnector;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryEntry;
import ieci.tdw.ispac.ispaclib.resp.RespFactory;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.security.SecurityMgr;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class CreateExpedientAssociateAction extends BaseAction{

	/** Nombre del fichero de recursos. */
	private static final String BUNDLE_NAME = "ieci.tdw.ispac.ispacmgr.resources.ApplicationResources";

	/** Recursos. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CreateExpedientAssociateAction.class);

	private String resp;

	public ActionForward executeAction(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			SessionAPI session) throws Exception {

		IInvesflowAPI invesflowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();
		ClientContext cct = session.getClientContext();

		//Se obtiene el estado de tramitación(informacion de contexto del usuario)
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState currentstate = managerAPI.currentState(getStateticket(request));

		//Nombre del procedimiento origen
		String strProcPadre = request.getParameter("procPadre");
		logger.debug("Procedimiento Padre: "+strProcPadre);
		//Nombre del procedimiento a crear (Decreto o Propuesta)
		String strProcHijo = request.getParameter("procHijo");
		logger.debug("Procedimiento Hijo: "+strProcHijo);	
		//Nombre de la regla a ejecutar
		String strRuleName = request.getParameter("rule");
		logger.debug("Regla a ejecutar: "+strRuleName);
		//Identificador del trámite actual
		String nIdTask = request.getParameter("taskId");
		logger.debug("Trámite actual: "+nIdTask);
		
		
		
		if(strProcPadre != null && strProcHijo!= null){

			ITask task = null;
			task = invesflowAPI.getTask(Integer.parseInt(nIdTask));
			int nidstage = task.getInt("ID_FASE_EXP");

			//Obtenemos en stage el registro de la tabla spac_fases
			IStage stage = null;
			stage = invesflowAPI.getStage(nidstage);

			String numExp = stage.getString("NUMEXP");
			
			//Obtengo a que grupo pertenece de propuestas o de decretos.
			String procHijoPertenece = obtenerProcedimientoPertenece(numExp, strProcHijo, entitiesAPI, nidstage, nIdTask, cct);
			
			//String relacion=getString("procedimiento."+strProcHijo+".asociado.relacion."+strProcPadre);
			String relacion=getString("procedimiento."+procHijoPertenece+".asociado.relacion."+strProcPadre);

			//IItemCollection col = getProcedure(cct, strProcHijo);
			IItemCollection col = getProcedure(cct, procHijoPertenece);
			
			Iterator it = col.iterator();
			if(!it.hasNext()){
				throw new ISPACInfo("No tiene permisos para iniciar el expediente asociado.");
			}
			IItem item = null;
			int idProcedure = 0;
			while (it.hasNext()){
				item = ((IItem)it.next());
				idProcedure = item.getInt("ID");
			}

			// Obtener el código de procedimiento para el número de expediente
			IItem ctProcedure = entitiesAPI.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS, idProcedure);
			Map params = new HashMap();
			params.put("COD_PCD", ctProcedure.getString("COD_PCD"));

			// Crear el proceso del expediente
			int nIdProcess2 = tx.createProcess(idProcedure, params);
			IProcess process = invesflowAPI.getProcess(nIdProcess2);
			String numExpHijo = process.getString("NUMEXP");

			IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);

			registro.set("NUMEXP_PADRE", numExp);
			registro.set("NUMEXP_HIJO", numExpHijo);
			registro.set("RELACION", relacion);

			registro.store(cct);


			//Ejecutar la regla asociada
			if (strRuleName != null){
				logger.debug("Buscando regla '"+strRuleName+"'");
				
				IItemCollection reglas = invesflowAPI.getCatalogAPI().getCTRules(strRuleName);
				it = reglas.iterator();
				if (it.hasNext())
				{
					IItem regla = (IItem)it.next();
					int ruleId = regla.getInt("ID");
					logger.debug("Regla número: "+ruleId);
					logger.warn("task id. "+nIdTask);
					cct.setSsVariable("taskId", nIdTask);
					EventManager eventmgr = new EventManager(cct);
					eventmgr.newContext();
					eventmgr.getRuleContextBuilder().addContext(process);
					eventmgr.processRule(ruleId);
				}
			}

		}else{
			logger.error("El nombre del procedimiento padre o del procedimiento hijo es nulo");
		}
		//return NextActivity.afterStartProcess(request, nIdProcess2, invesflowAPI, mapping);
		//return  mapping.findForward("success");
		return NextActivity.refresh(currentstate, mapping);
	}
	
	private String obtenerProcedimientoPertenece(String numExp,
			String strProcHijo, IEntitiesAPI entitiesAPI, int nidstage, String idTask, ClientContext cct) throws ISPACException {
		String srProcHijo = "";
		
		if(strProcHijo.equals(Constants.SUBVENCIONES.PROCHIJODECRETO)){
			//Obtengo la persona que ha iniciado el trámite, para ello de la tabla spac_dt_documentos
			// selecciono el documento que hace referencia al tramite aprobacion
			// y cojo la columna autor.
			String STR_queryDocumentos = "ID_FASE="+nidstage+" AND ID_TRAMITE="+idTask+"";
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numExp, STR_queryDocumentos, "FDOC DESC");
	        Iterator it = documentsCollection.iterator();
	        IItem itemDoc = null;
	        String nombreAutor = "";
	        int idAutor = 0;
	        while (it.hasNext()){
	        	itemDoc = (IItem)it.next();
	        	if (itemDoc.getString("AUTOR") != null) nombreAutor = itemDoc.getString("AUTOR");	        	
	        	String [] vNombreAutor = nombreAutor.split("-");
	        	if(vNombreAutor.length == 2){
	        		idAutor = Integer.parseInt(vNombreAutor[1]);
	        	}
			}
	        //ahora con el id del usuario me voy a la tabla iusergroupuser
	        //y obtengo los grupos a los que pertenece ese usuario
	     // Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			AccesoBBDDRegistro accsRegistro = new AccesoBBDDRegistro(entidad);
			
			Vector idGroupsUser = accsRegistro.getGruposPerteneceUsuario(idAutor);
			
				
			//Con este id del grupo al que pertenece voy a la tabla IUSERGROUPHDR y obtengo el name
			Vector nombreGrupo = accsRegistro.getNombreGrupo(idGroupsUser);
			for(int j = 0; j < nombreGrupo.size(); j++){
				String grupo = (String) nombreGrupo.get(j);
				String [] vGrupo = grupo.split("-");
				if(vGrupo != null){
					grupo = vGrupo[0];
				}
				//Si es igual quiere decir que pertenece a un nombre de grupo
				if(grupo.equals(Constants.DECRETOS.NOMBRE_GRUPO)){
					int idGrupo = ((Integer) idGroupsUser.get(j)).intValue();
					//Obtengo de la tabla spac_p_procedientos cual es el nombre del decreto que
					//le corresponde 
					String grupoResponsable = "3-"+idGrupo+"";
					
					String sqlQuery = "WHERE ID_RESP="+grupoResponsable+"";
					

					logger.warn("strQuery exp "+sqlQuery);
			        IItemCollection collection = entitiesAPI.queryEntities("SPAC_P_PROCEDIMIENTOS", sqlQuery);
			        List list=collection.toList();
			        Iterator itP = list.iterator();
			        
			        while (itP.hasNext())
			        {
			        	IItem item = (IItem)itP.next();
			        	srProcHijo = item.getString("NOMBRE");
			        }
					
				}
			}
		}
		
		return srProcHijo;
	}



	public IItemCollection getProcedure(ClientContext context, String strProcHijo) throws ISPACException
	{
		// Los que puede crear el usuario y los que pueden crear los que sustituye
		String resp = getSubstitutesRespString(context);
		String nombreProcedure=getString("procedimiento."+strProcHijo+".asociado.nombre");
		DbCnt cnt = null;
		try
		{
			cnt = context.getConnection();
			CollectionDAO pcdset = new CollectionDAO(PProcedimientoDAO.class);

			/* Procedimientos en vigor */
			String sqlquery = "WHERE ESTADO=" + IProcedure.PCD_STATE_CURRENT
			+ " AND TIPO=" + IProcedure.PROCEDURE_TYPE
			+ " AND ID IN" 
			+ " (SELECT ID_PCD FROM SPAC_SS_PERMISOS WHERE  PERMISO="
			+ ISecurityAPI.ISPAC_RIGHTS_CREATEEXP + DBUtil.addAndInResponsibleCondition("UID_USR", resp) + ")"
			+ " AND NOMBRE = '"+nombreProcedure+"' ORDER BY NOMBRE";
			pcdset.query(cnt,sqlquery);

			IItemCollection col = pcdset.disconnect();


			return col;
		}
		catch (ISPACException ie)
		{
			throw new ISPACException("Error en WLWorklist:getProcs()", ie);
		}
		finally
		{
			context.releaseConnection(cnt);
		}
	}


	private String getSubstitutesRespString(ClientContext context) throws ISPACException
	{
		if (StringUtils.isEmpty(resp)) {

			StringBuffer respList=new StringBuffer();
			DbCnt cnt = context.getConnection();

			try
			{
				Responsible user = context.getUser();
				respList.append(user.getRespString());


				SecurityMgr security = new SecurityMgr(cnt);

				// Sustituir (sustituidos por el usuario y por los grupos a los que pertenece el usuario)
				IItemCollection collection = security.getAllSubstitutes(user);
				while (collection.next()) {

					IItem substitute = (IItem) collection.value();
					respList.append("," + getRespStringFromEntryUID(substitute.getString("UID_SUSTITUIDO")));
				}

			}
			finally
			{
				context.releaseConnection( cnt);
			}

			resp = respList.toString();
		}

		return resp;
	}

	private String getRespStringFromEntryUID(String entryUID) throws ISPACException {

		IDirectoryConnector directory = DirectoryConnectorFactory.getConnector();

		IDirectoryEntry entry = directory.getEntryFromUID(entryUID);
		Responsible resp= RespFactory.createResponsible(entry);

		return resp.getRespString();
	}

	/**
	 * Obtiene el texto del recurso especificado.
	 * @param key Clave del texto.
	 * @return Texto.
	 */
	private static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}


}
