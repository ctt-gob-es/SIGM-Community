package es.dipucr.contratacion.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;

public class AvisoElectronicoRecepcionPlicasSecretaria extends AvisoElectronico{
	
	private static final String RESP_ID_VARNAME = "AVISO_PLICAS_CONTRA_RESPID"; //[dipucr-Felipe #728]
	private static Logger LOGGER = Logger.getLogger(AvisoElectronicoRecepcionPlicasSecretaria.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), RESP_ID_VARNAME);
			mensaje = "Aprobado nuevo expediente de contratación "+rulectx.getNumExp()+" inicio recepción de Plicas.";
		}
		catch (ISPACException ex){
			String error = "Error al avisar al departamento de la recepción de plicas";
			LOGGER.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}

		return true;
	}
}
