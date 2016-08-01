package es.dipucr.sigem.api.rule.procedures.portaFirmasExterno;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.SetSubstituteAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SetListadoPortaFirmaAction extends SetSubstituteAction {

	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
		
				
		// Cogemos los valores del bean
		String valor = request.getParameter("value");
		String sustituto = request.getParameter("sustituto");
		String cargo = request.getParameter("cargo");
		String descripcion = request.getParameter("descripcion_cargo");
		
	
		ItemBean item = new ItemBean();
		item.setProperty("VALOR", valor);
		item.setProperty("SUSTITUTO", sustituto);
		
		if(!StringUtils.isEmpty(cargo)){
			item.setProperty("CARGO", cargo);
		}
		if(cargo==null){
			item.setProperty("CARGO", "");
		}
		
		if(!StringUtils.isEmpty(descripcion)){
			item.setProperty("DESCRIPCION_CARGO", descripcion);
		}
		if(descripcion==null){
			item.setProperty("DESCRIPCION_CARGO", "");
		}
		
		request.setAttribute("Value", item);

		return mapping.findForward("success");
	}
	
}
