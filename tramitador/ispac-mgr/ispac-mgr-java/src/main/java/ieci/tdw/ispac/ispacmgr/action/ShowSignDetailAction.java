package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispacmgr.action.form.EntityForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.PortafirmasUtil;

/**
 * @author antoniomaria_sanchez at ieci.es
 * @since 19/01/2009
 */
public class ShowSignDetailAction extends BaseAction {

	public static final Logger logger = Logger.getLogger(ShowSignDetailAction.class);
	

	@SuppressWarnings("unchecked")
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		EntityForm defaultForm = (EntityForm) form;
		
		//INICIO [dipucr-Felipe #208 #1246]
		String documentId = request.getParameter("document");
        if (null == documentId) {
			// Nombre del campo que contiene el valor de la búsqueda
			String field = request.getParameter("field");
			
			// Número de registro a buscar
			documentId = defaultForm.getProperty(field);
        }
        //FIN [dipucr-Felipe #208 #1246]
		
		if (logger.isDebugEnabled()){
			logger.debug("Solicitando detalle de firma de un documento: " + documentId);
		}
		
		IInvesflowAPI invesflowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		ISignAPI singAPI = invesflowAPI.getSignAPI();
	
		int idDoc = Integer.parseInt(documentId);
		List details = null;
		//[dipucr-Felipe #1246] Control portafirmas
		IItem itemDoc = entitiesAPI.getDocument(idDoc);
		if (entitiesAPI.isDocumentPortafirmas(itemDoc)){
//			details = singAPI.showSignInfo(idDoc);
			details = singAPI.showSignInfo(idDoc, true);//[dipucr-Felipe #1352]
			
			//INICIO [dipucr-Felipe #1360]
			//Comprobamos si sigue pendiente de firmas pero ya firmado en portafirmas
			if (SignStatesConstants.PENDIENTE_CIRCUITO_FIRMA.equals(itemDoc.getString("ESTADOFIRMA"))){
				if (PortafirmasUtil.checkFirmado(details) || PortafirmasUtil.checkRechazado(details)){
					request.setAttribute("processed", "true");
					request.setAttribute("document", idDoc);
				}
			}
			//FIN [dipucr-Felipe #1360]
		}
		else{
			details = new ShowSignDetail3FasesAction().showSignInfo(session, idDoc);
		}
		request.setAttribute("details", details);
		
		return mapping.findForward("success");
	}

}
