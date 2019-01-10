package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISearchAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.procedure.PProcedimientoDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;
import ieci.tdw.ispac.ispacweb.manager.ISPACRewrite;

import java.io.File;
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

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class CreateSearchExpedientAssociateAction extends BaseDispatchAction {

	/** Nombre del fichero de recursos. */
	private static final String BUNDLE_NAME = "ieci.tdw.ispac.ispacmgr.resources.ApplicationResources";

	/** Recursos. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CreateSearchExpedientAssociateAction.class);

	public ActionForward form(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

    	ClientContext cct = session.getClientContext();
    	
    	IInvesflowAPI invesflowAPI = session.getAPI();

		ISPACRewrite ispacPath = new ISPACRewrite(getServlet()
				.getServletContext());

		String xml = ispacPath.rewriteRealPath("xml/relateExpedientForm.xml");
		String xsl = ispacPath.rewriteRealPath("xsl/SearchForm.xsl");

		//////////////////////////////////////////////
		// Formulario de búsqueda
		ISearchAPI searchAPI = invesflowAPI.getSearchAPI();
		
		String frm = searchAPI.buildHTMLSearchForm(new File(xml), xsl, ResourceBundle.getBundle(BUNDLE_NAME, cct.getLocale()), request.getParameterMap());
		request.setAttribute("Form", frm);

		return mapping.findForward("form");
	}
	
	public ActionForward enter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		 // Establecer el número de expediente seleccionado en la búsqueda
		 String nombreProcedimiento = request.getParameter("procedimiento");
		 
		 IInvesflowAPI invesflowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();
		ClientContext cct = session.getClientContext();

		//Se obtiene el estado de tramitación(informacion de contexto del usuario)
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState currentstate = managerAPI.currentState(getStateticket(request));

		//Nombre del procedimiento origen
		String strProcPadre = request.getParameter("procPadre");
		//Nombre del procedimiento a crear (Decreto o Propuesta)
		String strProcHijo = request.getParameter("procHijo");
		//Nombre de la regla a ejecutar
		String strRuleName = request.getParameter("rule");
		//Identificador del trámite actual
		String nIdTask = request.getParameter("taskId");
		
		
		
		if(strProcPadre != null && strProcHijo!= null){

			ITask task = null;
			task = invesflowAPI.getTask(Integer.parseInt(nIdTask));
			int nidstage = task.getInt("ID_FASE_EXP");

			//Obtenemos en stage el registro de la tabla spac_fases
			IStage stage = null;
			stage = invesflowAPI.getStage(nidstage);

			String numExp = stage.getString("NUMEXP");
			
			//Obtengo a que grupo pertenece de propuestas o de decretos.
			//String procHijoPertenece = obtenerProcedimientoPertenece(numExp, strProcHijo, entitiesAPI, nidstage, nIdTask, cct);
			
			//String relacion=getString("procedimiento."+strProcHijo+".asociado.relacion."+strProcPadre);
			
			//Se le añade al final el task id para que cuando vaya a utlizar la regla de cerrar este tramite y ver si
			//ha sido la propuesta dictaminada certificada o decretada sepa al tramite del q pertenece
			IItemCollection col = getProcedure(cct, nombreProcedimiento);
			
			nombreProcedimiento = nombreProcedimiento.substring(0, nombreProcedimiento.length()/2);

			String relacion=getString("proc."+nombreProcedimiento+".relacion."+numExp+"|"+nIdTask);

			//IItemCollection col = getProcedure(cct, strProcHijo);
			
			
			Iterator it = col.iterator();
			if(it.hasNext()){
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
					
					IItemCollection reglas = invesflowAPI.getCatalogAPI().getCTRules(strRuleName);
					it = reglas.iterator();
					if (it.hasNext())
					{
						IItem regla = (IItem)it.next();
						int ruleId = regla.getInt("ID");
						cct.setSsVariable("taskId", nIdTask);
						EventManager eventmgr = new EventManager(cct);
						eventmgr.newContext();
						eventmgr.getRuleContextBuilder().addContext(process);
						eventmgr.processRule(ruleId);
					}
					else{
						throw new ISPACRuleException("LA REGLA "+strRuleName+" ESTA SIN CREAR. CONTACTE CON EL ADMINISTRADOR");
					}
				}
			}
			else{
				throw new ISPACInfo("No tiene permisos para iniciar el expediente asociado.");
			}
			
		}else{
			logger.error("El nombre del procedimiento padre o del procedimiento hijo es nulo");
		}
		//return NextActivity.afterStartProcess(request, nIdProcess2, invesflowAPI, mapping);
		//return  mapping.findForward("success");
		return NextActivity.refresh(currentstate, mapping);
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		IInvesflowAPI invesflowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		ClientContext cct = session.getClientContext();
		// API del catálogo
		ICatalogAPI catalogAPI = session.getAPI().getCatalogAPI();


		//Nombre del procedimiento origen
		String strProcPadre = request.getParameter("procPadre");
		//Nombre del procedimiento a crear (Decreto o Propuesta)
		String strProcHijo = request.getParameter("procHijo");
		//Nombre de la regla a ejecutar
		String strRuleName = request.getParameter("rule");
		//Identificador del trámite actual
		String nIdTask = request.getParameter("taskId");
		
		//entity
		String entity = request.getParameter("entity");
		
		request.setAttribute("procPadre", strProcPadre);
		request.setAttribute("procHijo", strProcHijo);
		request.setAttribute("rule", strRuleName);
		request.setAttribute("taskId", nIdTask);
		request.setAttribute("entity", entity);
		
		if(strProcPadre != null && strProcHijo!= null){

			ITask task = null;
			task = invesflowAPI.getTask(Integer.parseInt(nIdTask));
			int nidstage = task.getInt("ID_FASE_EXP");

			//Obtenemos en stage el registro de la tabla spac_fases
			IStage stage = null;
			stage = invesflowAPI.getStage(nidstage);

			String numExp = stage.getString("NUMEXP");
			request.setAttribute("numexp", numExp);

			//Obtengo a que grupo pertenece de propuestas o de decretos.
			Vector procHijoPertenece = obtenerProcedimientosPertenece(numExp, strProcHijo, entitiesAPI, nidstage, nIdTask, cct, catalogAPI);
				
			List lResults = procHijoPertenece.subList(0, procHijoPertenece.size());
			request.setAttribute("ValueList", lResults);
	
			// Obtiene el decorador
			CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
			BeanFormatter formatter = factory.getFormatter(
					getISPACPath("/digester/valueformatter.xml"));
			request.setAttribute("Formatter", formatter);
		}

		return mapping.findForward("results");
	}
	
	private Vector obtenerProcedimientosPertenece(String numExp,
			String strProcHijo, IEntitiesAPI entitiesAPI, int nidstage, String idTask, ClientContext cct, ICatalogAPI catalogAPI) throws ISPACException {
		Vector  vProcHijo = new Vector();
	
        int idAutor = 0;

        String [] vNombreAutor = cct.getUser().getUID().split("-");
    	if(vNombreAutor.length == 2){
    		idAutor = Integer.parseInt(vNombreAutor[1]);
    	}
    	else idAutor = Integer.parseInt(vNombreAutor[1]);
        
        //Recuperamos todos los prodecimientos para los que el usuario tiene permiso de iniciar expediente
		IItemCollection procedimientos = DipucrCommonFunctions.getProcedimientosPermisoIniciarExpUsuario(cct, entitiesAPI, catalogAPI, idAutor);
			
		//En función del tipo que hayamos pasado como parámetros seleccionamos uno u otro o devolvemos todos.
		if(procedimientos != null && procedimientos.next()){
			Iterator itP = procedimientos.iterator();
			if(strProcHijo.equals(Constants.SUBVENCIONES.PROCHIJODECRETO)){					
				while (itP.hasNext()){
					IItem item = (IItem)itP.next();
		        	String srProcHijo = item.getString("NOMBRE");
		        	if(srProcHijo.toUpperCase().indexOf("DECRETO")>=0){
			        	ItemBean itemB = new ItemBean();
						itemB.setProperty("VALOR", srProcHijo);
			        	vProcHijo.add(itemB);
		        	}
		        }
			}
			else if(strProcHijo.equals(Constants.SUBVENCIONES.PROCHIJOPROPUESTA)){
				while (itP.hasNext()){
					IItem item = (IItem)itP.next();
		        	String srProcHijo = item.getString("NOMBRE");
		        	if(srProcHijo.toUpperCase().indexOf("PROP")>=0){
			        	ItemBean itemB = new ItemBean();
						itemB.setProperty("VALOR", srProcHijo);
			        	vProcHijo.add(itemB);
		        	}
				}
			}
			else{//Si no introducinos ningún tipo (decreto, propuesta,...) devuelve todos
				while (itP.hasNext()){
					IItem item = (IItem)itP.next();
		        	String srProcHijo = item.getString("NOMBRE");
			        ItemBean itemB = new ItemBean();
					itemB.setProperty("VALOR", srProcHijo);
			        vProcHijo.add(itemB);		        	
				}
			}			
		}
		return vProcHijo;
	}


	public IItemCollection getProcedure(ClientContext context, String strProcHijo) throws ISPACException
	{
		//Con este metodo que esta en comentarios accede al fichero ApplicationResources.properties
		//de ispac-mgr-webapp
		//String nombreProcedure=getString("procedimiento."+strProcHijo+".asociado.nombre");
		String nombreProcedure = strProcHijo;
		
		DbCnt cnt = null;
		try
		{
			cnt = context.getConnection();
			CollectionDAO pcdset = new CollectionDAO(PProcedimientoDAO.class);

			/* Procedimientos en vigor */
			String sqlquery = "WHERE ESTADO=" + IProcedure.PCD_STATE_CURRENT
			+ " AND TIPO=" + IProcedure.PROCEDURE_TYPE
			//Busqueda si tiene permisos para iniciar un expediente
			//+ " AND ID IN" 
			//+ " (SELECT ID_PCD FROM SPAC_SS_PERMISOS WHERE  PERMISO="
			//+ ISecurityAPI.ISPAC_RIGHTS_CREATEEXP + DBUtil.addAndInResponsibleCondition("UID_USR", resp) + ")"
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
