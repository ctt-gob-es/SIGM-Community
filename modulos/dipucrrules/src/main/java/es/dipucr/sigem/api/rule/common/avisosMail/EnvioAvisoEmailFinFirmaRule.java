package es.dipucr.sigem.api.rule.common.avisosMail;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class EnvioAvisoEmailFinFirmaRule extends DipucrEnviaDocEmailConAcuse{

	/**
	 * [Ticket #486 TCG](SIGEM creación método genérico para el envío de avisos por mail)
	 * [Manu Ticket #1181] Procedimiento de Recursos Administrativos de Servicios Sociales
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailFinFirmaRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			numexp = itemDocumento.getString("NUMEXP");
				
			VAR_EMAILS = "javier_garcia@dipucr.es";
			nombreNotif = "Javier Garcia Caballero";
			contenido = "Fin de firma de Trámite de Intervención en SIGEM a visualizar con el número de expediente " + numexp;
			asunto = "[AL-SIGM] Fin de firma de Trámite de Intervención en SIGEM a visualizar con el número de expediente " + numexp;
			
			conDocumento = false;
		} catch (ISPACException e) {
			 logger.error("Error al enviar email a: " + nombreNotif + ", en el expediente: " + numexp + ". "+ e.getMessage(), e);
		}
		
		return true;
	}
}
