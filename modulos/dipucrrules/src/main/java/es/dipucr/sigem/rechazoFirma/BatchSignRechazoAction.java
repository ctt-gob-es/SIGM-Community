package es.dipucr.sigem.rechazoFirma;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispacmgr.action.BatchSignAction;
import ieci.tdw.ispac.ispacmgr.action.form.BatchSignForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacmgr.menus.MenuFactory;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.rechazoFirma.api.impl.SignRechazoAPI;


public class BatchSignRechazoAction extends BatchSignAction{

	private IState enterState(HttpServletRequest request, SessionAPI session,
			HttpServletResponse response) throws ISPACException {
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(
				session.getClientContext());
		// Se cambia el estado de tramitación
		Map params = request.getParameterMap();
		// Al estar en un 'DispacthAction', cambiamos de estado si no nos
		// encontramos ya en el estado de BATCHSIGNDOCUMENT
		IState state = managerAPI.currentState(getStateticket(request));
		if (state.getState() != ManagerState.BATCHSIGNLIST)
			state = managerAPI.enterState(getStateticket(request),
					ManagerState.BATCHSIGNLIST, params);
		storeStateticket(state, response);
	
		return state;
	}

	private void setMenu(ClientContext cct, IState state,
			HttpServletRequest request) throws ISPACException {
		request.setAttribute("menus", MenuFactory.getSignedDocumentListMenu(
				cct, state, getResources(request)));
	}		
	
	public ActionForward rechazarFirma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		
			IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
			SignRechazoAPI signAPI = new SignRechazoAPI(session.getClientContext());
	
			IState state = enterState(request, session, response);
	
			BatchSignForm batchSignForm = (BatchSignForm) form;			
	
			// Identificadores de los pasos de circuito de firma asociados a los
			// documentos a firmar
			String[] stepIds = batchSignForm.getMultibox();
			if (stepIds == null) {
	
				return mapping.findForward("refresh");
			}
	
			String entityId = (String) request.getSession().getAttribute(
					"idEntidad");// 2.0
	
			// Rechazamos firmar los documentos asociados a los pasos de firma
			List documentosRechazados = signAPI.rechazarFirma(stepIds,
					batchSignForm.getSignCertificate(), entityId, batchSignForm.getMotivoRechazo());
						
			
			// Establecer el estado de rechazo para cada documento
			List ltSignsDocuments = new ArrayList();
	
			Iterator it = documentosRechazados.iterator();
			while (it.hasNext()) {
	
				SignDocument signDocument = (SignDocument) it.next();
	
				signDocument.setEntityId(entityId);// 2.0
			}
	
			// Lista de documentos rechazados para la vista
			request.setAttribute(ActionsConstants.SIGN_DOCUMENT_LIST,
					ltSignsDocuments);
	
			batchSignForm.clean();
			// /////////////////////////////////////////////
			// Formateador
			CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
			BeanFormatter formatter = factory
					.getFormatter(getISPACPath("/digester/documentsignedlistformatter.xml"));
			request.setAttribute(ActionsConstants.FORMATTER, formatter);
	
			setMenu(session.getClientContext(), state, request);
	
			return mapping.findForward("successRechazar");
	}
	
	public ActionForward confirmaRechazo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {		
		return mapping.findForward("confirmaRechazo");	
	}
	public ActionForward refrescar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {		
		return mapping.findForward("refresh");	
	}
}