package es.dipucr.contratacion.common.avisos;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;

public class AvisosElectronicosValAnorDespTramResolSecret extends AvisoElectronico{
	
	private static final String RESP_ID_VARNAME = "AVISO_VALANORDEP_RESOL_RESPID"; //[dipucr-Felipe #728]
	private static Logger LOGGER = Logger.getLogger(AvisosElectronicosValAnorDespTramResolSecret.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), RESP_ID_VARNAME);
			mensaje = "Realizado el informe de contratación para los Valores Anormales y Desproporcionados "+rulectx.getNumExp();
		}
		catch (ISPACException ex){
			String error = "Error al avisar al departamento de los valores anormales y desproporcionados";
			LOGGER.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}

		return true;
	}
}
