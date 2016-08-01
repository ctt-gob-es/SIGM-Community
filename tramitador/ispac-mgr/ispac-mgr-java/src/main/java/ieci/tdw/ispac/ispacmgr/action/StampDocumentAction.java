package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;

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
		if (StringUtils.isNotBlank(documentId)) {
			try {
		  	    IInvesflowAPI invesFlowAPI = session.getAPI();
				IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	
		  		// Obtener el documento a sellar
		  		IItem document = entitiesAPI.getEntity(SpacEntities.SPAC_DT_DOCUMENTOS, TypeConverter.parseInt(documentId));
		  		
				//[Manu Ticket #107] - INICIO - ALSIGM3 Registrar salida, comunicación con Comparece y Gestión de Representantes
		  		SellarDocumentos sellarDocumentos = new SellarDocumentos(session.getClientContext(), document.getInt("ID_TRAMITE"), documentId);
				sellarDocumentos.sellarDocumentos();
				//[Manu Ticket #107] - FIN - ALSIGM3 Registrar salida, comunicación con Comparece y Gestión de Representantes

			} catch (ISPACInfo e) {
				logger.error("Error al sellar el documento", e);
				e.setRefresh(false);
				throw e;
			} catch (Exception e) {
				logger.error("Error al sellar el documento", e);
				throw new ISPACInfo(e,false);
			}
		}

		return null;
	}
   	
}