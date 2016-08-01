package es.dipucr.sigem.api.rule.procedures.secretaria;

import org.apache.log4j.Logger;

import es.dipucr.ownCloud.OwnCloudConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.OwnCloudUtils;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class DipucrEliminaCarpetaOwnCodAudioActa implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrEliminaCarpetaOwnCodAudioActa.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";

		try {
			
			numexp = rulectx.getNumExp();
			
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String entidad = EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext());
			
			String username = ownCloudConfig.getProperty("USR_"+entidad);
			String password = ownCloudConfig.getProperty("USR_PASSWORD_"+entidad);
							    
			String directorio = numexp.replace("/", "_");
			
			OwnCloudUtils.deleteCarpeta(username, password, directorio);
			
			
		} catch (ISPACException e) {
			logger.error("Error al crear la carpeta del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear la carpeta del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
