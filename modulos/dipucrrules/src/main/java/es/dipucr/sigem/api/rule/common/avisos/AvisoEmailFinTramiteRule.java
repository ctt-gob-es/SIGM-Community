package es.dipucr.sigem.api.rule.common.avisos;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

public class AvisoEmailFinTramiteRule   implements IRule 
{

	/**
	 * [Ticket #486 TCG](SIGEM creación método genérico para el envío de avisos por mail)
	 */
	protected static final Logger logger = Logger.getLogger(AvisoEmailFinTramiteRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	 public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		 try{
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			 
			 String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
			Object[] imagen = {rutaImg, new Boolean(true), "logoCabecera.gif", "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
			
			//Se adjunta la direccion de la persona de tesoreria a la que hay que mandarle el email
			IItem itemProcess = invesflowAPI.getProcess(rulectx.getNumExp());
			String sRespProceso = itemProcess.getString("ID_RESP");
			IResponsible responsable = ResponsablesUtil.getResponsible(cct, sRespProceso);
			String sListaEmails = ResponsablesUtil.getMailsResponsable((ClientContext) cct, responsable);
			//String emailNotif = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "EMAILS_CONTRATACION_PROCCONTRATACION");
	        //String emailNotif = "compras@dipucr.es";
			String contenido = "";
			String asunto = "Fin trámite con el número de expediente "+rulectx.getNumExp();
			
			IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());
			contenido = contenido
					+ "<br/>"
					+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Expediente: <b>" + rulectx.getNumExp() + "</b>."
					+ "<br/>"
					+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Asunto: <b>" + expediente.getString("ASUNTO") + "</b>."
					+ "<br/>"
					+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Fin del trámite: <b>" + TramitesUtil.getTramite(rulectx.getClientContext(), rulectx.getNumExp(), rulectx.getTaskId()).getString("NOMBRE") + "</b>."
					+ "<br/>";
			
			contenido = "<img src='cid:escudo' width='200px'/>"
	        		+ "<p align=justify>"
	        		+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Estimado señor/a:" 
	        		+ "<br/> <br/>" 
	        		+ MailUtil.ESPACIADO_PRIMERA_LINEA +  contenido
	        		+ " </p>"
	        		+ "<br/> <br/>";
	        		
			asunto += " - " +expediente.getString("NOMBREPROCEDIMIENTO");

			MailUtil.enviarCorreoVarios(rulectx, sListaEmails, asunto, contenido, false, null, imagenes);
			logger.warn("[EnvioMailTesoreriaRule:execute()]Email enviado a: "+sListaEmails);
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		 return new Boolean(true);

	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
