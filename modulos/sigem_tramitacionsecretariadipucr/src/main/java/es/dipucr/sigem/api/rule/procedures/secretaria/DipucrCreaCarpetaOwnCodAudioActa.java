package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.ownCloud.OwnCloudConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.OwnCloudUtils;

public class DipucrCreaCarpetaOwnCodAudioActa implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrCreaCarpetaOwnCodAudioActa.class);
	

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
            
            if(OwnCloudUtils.crear(username, password, directorio)){
                        
	            String respuestaCompartir = OwnCloudUtils.compartir(username, password, directorio);
	            
	            if(respuestaCompartir.equals(OwnCloudUtils.COD_ERROR)){
	            	throw new ISPACRuleException("Error al compartir la carpeta al recibir la solicitud de trabajo a imprenta con numexp: " + numexp);
	            }
            }
		} catch (ISPACException e) {
			logger.error("Error al crear la carpeta del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear la carpeta del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
