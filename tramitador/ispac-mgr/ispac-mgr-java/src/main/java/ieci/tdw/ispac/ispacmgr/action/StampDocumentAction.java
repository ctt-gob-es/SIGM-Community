package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.impl.ManagerAPI;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.sellar.action.SellarDocumentos;

public class StampDocumentAction extends BaseAction {
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		// Obtener el id del documento a sellar
		String documentId = request.getParameter("documentId");
		
		IInvesflowAPI invesflowAPI = session.getAPI();
		ClientContext cct = session.getClientContext();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

		//Se obtiene el estado de tramitación(informacion de contexto del usuario)
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState currentstate = managerAPI.currentState(getStateticket(request));
		IProcess process = invesflowAPI.getProcess(currentstate.getProcessId());
		
		if (StringUtils.isNotBlank(documentId)) {
			try {
		  	    IInvesflowAPI invesFlowAPI = session.getAPI();
	
		  		// Obtener el documento a sellar
		  		IItem document = entitiesAPI.getEntity(SpacEntities.SPAC_DT_DOCUMENTOS, TypeConverter.parseInt(documentId));
		  		
				//[Manu Ticket #107] - INICIO - ALSIGM3 Registrar salida, comunicación con Comparece y Gestión de Representantes
		  		SellarDocumentos sellarDocumentos = new SellarDocumentos(session.getClientContext(), document.getInt("ID_TRAMITE"), documentId);
				sellarDocumentos.sellarDocumentos();
				//[Manu Ticket #107] - FIN - ALSIGM3 Registrar salida, comunicación con Comparece y Gestión de Representantes
				
				//[Agustin #414] Mostrar mensaje cuando va bien el registro o mostrar error en otro caso 
				 LOGGER.info("Se ha registrado correctamente el documento con id: "+documentId);
				 request.setAttribute("HA IDO BIEN", "El documento se ha registrado correctamente");
			     request.setAttribute("HA IDO BIEN, DETALLE", "En el campo estado muestra las novedades de la notificación");
				 return NextActivity.refresh(request, mapping, currentstate);

			} catch (ISPACInfo e) {
				LOGGER.error("Error al sellar el documento", e);
				e.setRefresh(false);
				throw e;
			} catch (Exception e) {
				LOGGER.error("Error al sellar el documento", e);
				throw new ISPACInfo(e,false);
			}
		}

		return null;
	}
   	
}