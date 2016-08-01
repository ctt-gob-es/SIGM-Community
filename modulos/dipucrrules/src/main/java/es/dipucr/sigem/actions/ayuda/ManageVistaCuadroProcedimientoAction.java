package es.dipucr.sigem.actions.ayuda;

import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.ISessionAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.bean.NodeSelectionHandlerAction;
import ieci.tdw.ispac.ispaclib.bean.TreeModel;
import ieci.tdw.ispac.ispaclib.bean.TreeNode;
import ieci.tdw.ispac.ispaclib.bean.TreeView;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.util.ActionRedirect;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.actions.ayuda.tree.CuadroEntidad;
import es.dipucr.sigem.actions.ayuda.tree.CuadroEntidadTreeView;
import es.dipucr.sigem.actions.ayuda.tree.ElementoCuadro;

public class ManageVistaCuadroProcedimientoAction extends TreeViewManager implements NodeSelectionHandlerAction {
	public final static String CUADRO_PROCEDIMIENTO = "CUADRO_PROCEDIMIENTO";
	public final static String REFRESH_VIEW_KEY = "REFRESH_VIEW_KEY";
	
	// [eCenpri-Manu #281] - INICIO - Edición de plantillas desde el tramitador por usuarios
	public final static String PUEDE_EDITAR_TEMPLATES = "PUEDE_EDITAR_TEMPLATES";
	
	public ActionForward defaultExecute(ActionMapping mappings, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, SessionAPI sessionApi)
			throws ISPACException {
		request.getSession().removeAttribute(CUADRO_PROCEDIMIENTO);
		
		ActionRedirect ret = new ActionRedirect(mappings.findForward("home"));
		String path = ret.getPath();
		path = path +"&entityId=22&";
		path = path +"regId="+request.getParameter("regId");
		ret.setPath(path);
		ret.setRedirect(true);
		return ret;
	}
	
	public ActionForward home(ActionMapping mappings, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI sessionApi) throws ISPACException {
		
		// Comprobar si hay que recargar la información del procedimiento
		String reload = request.getParameter("reload");
		if ("true".equals(reload)) {
			request.getSession().removeAttribute(CUADRO_PROCEDIMIENTO);
		}
		
		TreeView tree = loadTreeView(request, sessionApi);
		String pEntityId = request.getParameter("entityId");
		String pRegId = request.getParameter("entityId");
		if (StringUtils.isNotBlank(pEntityId) && StringUtils.isNotBlank(pRegId) ){
			TreeNode treeNode =  tree.getSelectedNode();
			if (treeNode==null){
				Collection<?> nodes = tree.getRootNodes();
				for (Iterator<?> iter = nodes.iterator(); iter.hasNext();) {
					TreeNode nodeToSelect = (TreeNode) iter.next();
					tree.setSelectedNode(nodeToSelect);
					break;
				}
			}
		}		
		return mappings.findForward("success");
	}

	private TreeView loadTreeView(HttpServletRequest request, ISessionAPI sessionApi) throws ISPACException{
		TreeView treeView = (TreeView) request.getSession().getAttribute(CUADRO_PROCEDIMIENTO);
		if (treeView == null) {
			int entityId = Integer.parseInt(request.getParameter("entityId"));
			int regId = Integer.parseInt(request.getParameter("regId"));
			String language = sessionApi.getClientContext().getAppLanguage();

			//obtener jerarquia del procedimiento
			CuadroEntidad cuadro = new CuadroEntidad(entityId, regId, sessionApi, language);
			treeView = new CuadroEntidadTreeView((TreeModel) cuadro, language);
			treeView.setNodeSelectionHandler((NodeSelectionHandlerAction) this);
			request.getSession().setAttribute(CUADRO_PROCEDIMIENTO, treeView);

			// [eCenpri-Manu #281] - INICIO - Edición de plantillas desde el tramitador por usuarios			
			boolean puedeEditarPlantillas = FunctionHelper.userHasFunctions(request, sessionApi.getClientContext(), new int[] {ISecurityAPI.FUNC_INV_TEMPLATES_EDIT});
			
			request.getSession().setAttribute(PUEDE_EDITAR_TEMPLATES, puedeEditarPlantillas);
			// [eCenpri-Manu #281] - FIN - Edición de plantillas desde el tramitador por usuarios
		}
		return treeView;
	}
	
	public ActionForward crearVista(ActionMapping mappings, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, SessionAPI sessionApi) throws ISPACException  {
		TreeView tree = loadTreeView(request, sessionApi);
        String id = request.getParameter("id");
    	TreeNode node = tree.getSelectedNode();
    	if (node!=null) {
    	}
        if (StringUtils.isNotBlank(id))
        {
            ActionRedirect view = new ActionRedirect(mappings.findForward("verElemento"));
            view.addParameter("id", id);
            return view;
        }else{
    		return mappings.findForward("done");
        }
	}

	public ActionForward getForwardVistaNodo(TreeNode node, ActionMapping mappings,
			HttpServletRequest request, SessionAPI sessionApi) throws ISPACException  {
		
		return null;
	}

	public ActionForward verNodo(ActionMapping mappings, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, SessionAPI sessionApi)
			throws Exception {
		return verNodo(mappings, form, request, response, (ISessionAPI)sessionApi);
	}

	public ActionForward verNodo(ActionMapping mappings, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, ISessionAPI sessionApi)
			throws Exception {

		request.setAttribute(REFRESH_VIEW_KEY, Boolean.TRUE);

		TreeView treeView = loadTreeView(request, sessionApi);
		if (treeView != null && request.getParameter("node") != null) {
			String pNode = request.getParameter("node");
			if (isRootNode(pNode)) {
				ActionForward ret = new ActionForward();
				String path = "/manageVistaCuadroProcedimiento.do?method=vistaproc";
				path = path +"&entityId="+request.getParameter("entityId") + "&";
				path = path +"regId="+request.getParameter("regId");
				ret.setPath(path);
				ret.setRedirect(true);
				return ret;
			} else {
				try {
					TreeNode node = treeView.getNode(URLDecoder.decode(pNode, "ISO-8859-1"));
					if (node != null) {
						treeView.setSelectedNode(node);
						node.setVisible();
						ElementoCuadro elem = (ElementoCuadro) node.getModelItem();
						if (elem.isEntityPlantillaTipoDoc() || elem.isEntityPlantillaStageTipoDoc()) {
							Boolean bool = new Boolean(sessionApi.getAPI().getTemplateAPI()
									.isProcedureTemplate(Integer.parseInt(elem.getRegId())));
							request.setAttribute("PLANT_ESPECIFICA", bool);						
						}
						return getForwardVistaNodo(node, mappings, request);
					} else {
						String id = request.getParameter("id");
						if (StringUtils.isNotBlank(id)) {
							ActionRedirect view = new ActionRedirect(mappings
									.findForward("verElemento"));
							view.addParameter("id", id);
							return view;
						}
					}
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		return mappings.findForward("viewRefresher");
	}
    
	public ActionForward getForwardVistaNodo(TreeNode node, ActionMapping mappings, HttpServletRequest request) {
		ActionForward ret = new ActionForward();
		ret.setRedirect(false);
		return ret;
	}
	
	public String getHandlerPath() {
		return "/manageVistaCuadroProcedimiento.do?method=verNodo";
	}

}
