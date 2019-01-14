package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.form.DocumentsForm;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DeleteDocumentsAction extends BaseAction {
	
	public static final int SIN_TRAMITE = 0;
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		DocumentsForm frm = (DocumentsForm) form;
		String[] documentIds = frm.getMultibox();
		int[] intDocumentIds = new int[documentIds.length];

		try{
			IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
			
			boolean bHayDocsDeTramite = false;
			for (int i = 0; i < documentIds.length && !bHayDocsDeTramite; i++) {
				
				int documentId = Integer.parseInt(documentIds[i]);
				IItem itemDocumento = entitiesAPI.getDocument(documentId);
				int idTramite = itemDocumento.getInt("ID_TRAMITE");
				if (idTramite != SIN_TRAMITE){
					bHayDocsDeTramite = true;
				}
				else{
					intDocumentIds[i] = documentId;
				}
			}
			
			if (bHayDocsDeTramite){
				throw new ISPACInfo("exception.documents.tab.deleteTaskDocument");
			}
			else{
				for (int idDocumento : intDocumentIds){
					entitiesAPI.deleteDocument(idDocumento);
				}
			}
			
			ClientContext cct = session.getClientContext();
			IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
			IState state = managerAPI.currentState(getStateticket(request));
			
			// Establecer el retorno
			String action = "/showExpedient.do";
			String params = "?entity=" + SpacEntities.SPAC_DT_DOCUMENTOS;
			params += "&stageId=" + state.getStageId();
			
			ActionForward actionForward = new ActionForward();
			actionForward.setPath(action + params);
			actionForward.setRedirect(true);

			return actionForward;
				
		}catch (Throwable e) {
			if (e instanceof ISPACInfo){
				throw (ISPACInfo)e;
			}
			else{
				String message = e.getMessage();
				throw new ISPACInfo(message,false);
			}
		}
	}
}