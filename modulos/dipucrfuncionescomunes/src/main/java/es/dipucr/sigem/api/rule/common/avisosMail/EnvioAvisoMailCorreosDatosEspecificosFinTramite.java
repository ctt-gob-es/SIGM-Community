package es.dipucr.sigem.api.rule.common.avisosMail;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class EnvioAvisoMailCorreosDatosEspecificosFinTramite extends DipucrEnviaDocEmailConAcuse{
	
	protected static final Logger LOGGER = Logger.getLogger(EnvioAvisoMailCorreosDatosEspecificosFinTramite.class);

	/**
	 * Va a hacer uso de una propiedad en los datos específicos llamada CORREOS_FIN_TRAMITE
	 * donde se almacenarán las direcciones a las que enviar un correo al terminar el trámite.
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		IClientContext cct = rulectx.getClientContext();

		try {
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		        //----------------------------------------------------------------------------------------------
			VAR_EMAILS = TramitesUtil.getCorreosFinTramiteDatosEspecificos(cct, rulectx.getTaskProcedureId());
			
			String numexp = rulectx.getNumExp();
			
			if (StringUtils.isEmpty(VAR_EMAILS)){
				LOGGER.info("Ninguna lista de mails configurada para el responsable " + cct.getResponsible());
			}
			else{
				IItem itemTramite = entitiesAPI.getTask(rulectx.getTaskId());
				
				contenido = "Tiene un nuevo Trámite " + itemTramite.get("NOMBRE") + " a gestionar en el expediente " + numexp;
				asunto = "[AL-SIGM] Tiene un nuevo trámite: " + itemTramite.get("NOMBRE") + " a gestionar en el expediente " + numexp;				
			}
			conDocumento = false;
		} catch (ISPACException e) {
			LOGGER.error("Error al enviar el mail de aviso de nuevo trámite al responsable. " + "Resp: " + cct.getResponsible() + " Lista mails:" + VAR_EMAILS + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el mail de aviso de nuevo trámite al responsable. " + "Resp: " + cct.getResponsible() + " Lista mails:" + VAR_EMAILS + ". " + e.getMessage(), e);
		}
		return true;
	}
}