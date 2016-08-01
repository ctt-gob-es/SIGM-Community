package es.dipucr.tablon.action;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.entity.EntityDAO;
import ieci.tdw.ispac.ispacmgr.action.SetSubstituteAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.tablon.services.TablonWSProxy;

public class SetListadoCategoriasAction extends SetSubstituteAction {

	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
				
		//Obtenemos el código de nuestra entidad
		IClientContext cct = session.getClientContext();
		String codEntidad = EntidadesAdmUtil.obtenerEntidad((ClientContext) cct);
		
		// Cogemos los valores del bean
		String valor = request.getParameter("value");
		TablonWSProxy ws = new TablonWSProxy();
		
		String descripcion = ws.getCategoriaByCodigo(codEntidad, valor).getDescripcion();

		EntityDAO entity = new EntityDAO(session.getClientContext().getConnection(), 
				"ETABLON_LISTADO","ID","");
		entity.set("VALOR", valor);
		entity.set("SUSTITUTO", descripcion);
		
		request.setAttribute("Substitute", new ItemBean(entity));

		return mapping.findForward("success");
	}
	
}