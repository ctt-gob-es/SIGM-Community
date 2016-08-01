package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class DipucrEnviaMailDocIntervencion extends DipucrEnviaDocEmailConAcuse {

	private static final Logger logger = Logger.getLogger(DipucrEnviaMailDocIntervencion.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		logger.info("INICIO - " + this.getClass().getName());
				
		VAR_EMAILS = "EMAIL_INFANTIC_SUBV"; //Variable de sistema de la que tomamos los emails

		String plantilla = DocumentosUtil.getPlantillaDefecto(rulectx.getClientContext(), rulectx.getTaskProcedureId());
		
		String tipoDoc = "";
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDoc = DocumentosUtil.getTipoDocumentoByPlantilla(rulectx.getClientContext(), plantilla);
		}
		contenido = tipoDoc + ": \n";			
			
		asunto= "[AL-SIGM] Traslado de Informe de justificación de la subvención con número de expediente: "+rulectx.getNumExp();
		conDocumento = true;

		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

}
