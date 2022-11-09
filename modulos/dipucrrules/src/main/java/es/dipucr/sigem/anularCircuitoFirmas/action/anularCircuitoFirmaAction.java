package es.dipucr.sigem.anularCircuitoFirmas.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.common.constants.SignCircuitStates;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.BaseDispatchAction;
import ieci.tdw.ispac.ispacmgr.action.form.EntityForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.dipucr.sigem.anularCircuitoFirmas.sign.SignCircuitAnular;

public class anularCircuitoFirmaAction extends BaseDispatchAction {
	
	private static final Logger LOGGER = Logger.getLogger(anularCircuitoFirmaAction.class);

	public ActionForward anularCircuito(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {		
		
		ClientContext cct = session.getClientContext();
		
		//SignForm signForm = (SignForm)form;
		
		EntityForm defaultForm = (EntityForm) form;
		
		// Nombre del campo que contiene el valor de la búsqueda
		String field = request.getParameter("field");

		// Número de registro a buscar
		String documentId = defaultForm.getProperty(field);
		
		int documentoId=0;
		try{
			//documentoId = Integer.parseInt(signForm.getDocumentId());
			documentoId = Integer.parseInt(documentId);	
		}
		catch(Exception e){	
			LOGGER.debug("Error al parsear el documentId = " + documentId, e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("es.dipucr.error.message.anularCircuitoFirma.error"));
			saveErrors(request,messages);
			return mapping.findForward("failure");
		}
		
		//INICIO [dipucr-Felipe #1246]
		ISignAPI signAPI = session.getAPI().getSignAPI();
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		boolean bPortafirmas = entitiesAPI.isDocumentPortafirmas(entitiesAPI.getDocument(documentoId));
        int instanciaId = 0;
        
		if (!bPortafirmas){//FIN [dipucr-Felipe #1246]
	        IItemCollection itemcol = signAPI.getStepsByDocument(documentoId);
	        
	        //Si ya hay algún paso del circuito firmado no podemos anular el circuito de firma
			while (itemcol.next()){
				IItem row = itemcol.value();
				int estado = 0;
				try{
					estado = row.getInt("ESTADO");
					instanciaId = row.getInt("ID_INSTANCIA_CIRCUITO");
				}
				catch(Exception e){
					LOGGER.debug("Error al recuperar el circuito de firma.", e);
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("es.dipucr.error.message.anularCircuitoFirma.error"));
					saveErrors(request,messages);
					return mapping.findForward("failure");
				}
				if(estado != SignCircuitStates.SIN_INICIAR && estado != SignCircuitStates.PENDIENTE){
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("es.dipucr.error.message.anularCircuitoFirma.error"));
					saveErrors(request,messages);
					return mapping.findForward("failure");
				}
			}
		}
		
		//Anulamos el circuito de firma
		//INICIO [dipucr-Felipe #1246]
		boolean circuitoAnulado = false;
		if (bPortafirmas){
			circuitoAnulado = signAPI.deleteCircuitPortafirmas(documentoId);
		}
		else{
			SignCircuitAnular signCircuitAnular = new SignCircuitAnular(cct);
			circuitoAnulado = signCircuitAnular.anularCircuito(instanciaId);
		}
		//FIN [dipucr-Felipe #1246]

		if(circuitoAnulado){
			// Situamos el Estado de Firma del documento a PENDIENTE
			IItem itemDoc = entitiesAPI.getDocument(documentoId);
			itemDoc.set("ESTADOFIRMA", SignStatesConstants.SIN_FIRMA);
			
			//Bloqueamos el documento para la edicion
			itemDoc.set("BLOQUEO", "");
			
			//[dipucr-Felipe #1246] Eliminamos id_proceso_firma
			if (bPortafirmas){
				String nullString = null;
				itemDoc.set("ID_PROCESO_FIRMA", nullString);
			}
			itemDoc.store(cct);
		}
		else{
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("es.dipucr.error.message.anularCircuitoFirma.error"));
			saveErrors(request,messages);
			return mapping.findForward("failure");
		}		
		return mapping.findForward("success");
	}
}
