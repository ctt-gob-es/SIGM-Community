package es.dipucr.sigem.api.rule.common.avisosMail;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;

public class EnvioAvisoEmailNuevoTramiteInfInterANAPEREZRule extends DipucrEnviaDocEmailConAcuse{

	/**
	 * (SIGEM creación método genérico para el envío de avisos por mail)
	 * [Manu Ticket #1181] Procedimiento de Recursos Administrativos de Servicios Sociales
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailNuevoTramiteInfInterANAPEREZRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			numexp = rulectx.getNumExp();
				
			VAR_EMAILS = "ana_perez@dipucr.es";
			nombreNotif = "Ana Pérez";
			contenido = "Tiene un nuevo Trámite de Informe Económico en SIGEM a visualizar con el número de expediente " + numexp;
			asunto = "[AL-SIGM] Tiene un nuevo Trámite de Informe Económico en SIGEM a visualizar con el número de expediente " + numexp;
			
			conDocumento = false;
		} catch (ISPACException e) {
			 logger.error("Error al enviar email a: " + nombreNotif + ", en el expediente: " + numexp + ". "+ e.getMessage(), e);
		}
		
		return true;
	}
}
