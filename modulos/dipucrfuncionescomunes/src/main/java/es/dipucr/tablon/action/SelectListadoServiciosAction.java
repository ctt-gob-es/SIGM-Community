package es.dipucr.tablon.action;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.tablon.services.ItemLista;
import es.dipucr.tablon.services.TablonWSProxy;

public class SelectListadoServiciosAction extends SelectSubstituteAction {
	
	protected int MAX_TBL_SEARCH_VALUES_DEFAULT = 50;
	protected final String _PARAM_LISTADO = "listado";
	protected final String _TIPO_SERVICIO = "SERVICIOS";
	protected final String _TIPO_CATEGORIA = "CATEGORIAS";
	
	public ActionForward executeAction(ActionMapping mapping, 
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {

		//Obtenemos el código de nuestra entidad
		IClientContext cct = session.getClientContext();
		String codEntidad = EntidadesAdmUtil.obtenerEntidad((ClientContext) cct);
		
		//Acceso al servicio web del tablón
		TablonWSProxy wsProxy = new TablonWSProxy();
		ItemLista [] arrListado = wsProxy.getServicios(codEntidad);
				
		ArrayList<ItemBean> vect = new ArrayList<ItemBean>();
		ItemLista itemlista = null;
		
		for(int i=0; i<arrListado.length; i++){
			
			itemlista = arrListado[i];
			ItemBean itembean = new ItemBean();
			itembean.setProperty("VALOR", itemlista.getValor());
			itembean.setProperty("SUSTITUTO", itemlista.getDescripcion());
			vect.add(itembean);
		}
//		List list = Arrays.asList(vect);
		List list = vect.subList(0, vect.size());
		request.setAttribute("SubstituteList", list);
		
		//Máximo de resultados
//		request.setAttribute("maxResultados", 20);
		
		// Obtener formateador
		processFormatter(request, "/digester/substituteformatter.xml");
		
		return mapping.findForward("success");
	}
	
}