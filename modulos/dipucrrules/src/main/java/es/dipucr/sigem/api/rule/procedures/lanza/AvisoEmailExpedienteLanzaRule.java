package es.dipucr.sigem.api.rule.procedures.lanza;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class AvisoEmailExpedienteLanzaRule implements IRule{

	public static final String DIRECCION_MAIL = "EMAIL_NOTIF_ANUNCIOS_LANZA";
	
	protected static final Logger logger = Logger.getLogger(AvisoEmailExpedienteLanzaRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		
		ClientContext cct = (ClientContext) rulectx.getClientContext();
        String sListaEmails = null;
        
		try {
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			String numexp = rulectx.getNumExp();
	        //----------------------------------------------------------------------------------------------

	        sListaEmails = ConfigurationMgr.getVarGlobal(cct, DIRECCION_MAIL);
			
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + 
					EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");

			Object[] imagen = {rutaImg, new Boolean(true), "logoCabecera.gif", "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);

			// Contenido del mail
			IItem itemExpediente = entitiesAPI.getExpedient(rulectx.getNumExp());
			StringBuffer sbContenido = new StringBuffer();
			sbContenido.append("<img src='cid:escudo' width='200px'/>");
			sbContenido.append("<p align=justify>");
			sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estimado/a tramitador/a:");
			sbContenido.append("<br/> <br/>");
			sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tiene un nuevo expediente en AL-SIGM pendiente de gestionar.\n");
			sbContenido.append("<br/>");
			sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Expediente: ");
			sbContenido.append("<b>" + numexp + "</b>");
			sbContenido.append("<br/>");
			sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Procedimiento: ");
			sbContenido.append("<b>" + itemExpediente.get("NOMBREPROCEDIMIENTO") + "</b>");
			sbContenido.append("<br/>");
							
			sbContenido.append("<br/> <br/>");
			
			// Asunto del mail
			String sAsunto = "[AL-SIGM] Tiene un nuevo expediente '" + itemExpediente.get("NOMBREPROCEDIMIENTO") + "' pendiente de gestionar: " + numexp;

			MailUtil.enviarCorreoVarios(rulectx, sListaEmails, sAsunto, sbContenido.toString(), false, imagenes);
			

		} catch (ISPACException e) {
			logger.error("Error al enviar el mail de aviso de nuevo trámite al responsable. " +
					"Resp: " + cct.getResponsible() + " Lista mails:" + sListaEmails + ". " + e.getMessage(), e);
			throw new ISPACRuleException(
					"Error al enviar el mail de aviso de nuevo trámite al responsable. " +
					"Resp: " + cct.getResponsible() + " Lista mails:" + sListaEmails + ". " + e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
