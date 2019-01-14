package es.dipucr.contratacion.common.avisos;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;

public class AvisoElectronicoInformeTecnicoPetSuministro extends AvisoElectronico{
	protected static final Logger logger = Logger.getLogger(AvisoElectronicoInformeTecnicoPetSuministro.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		//iusergrouphdr name='PROC-SUMNISTRO-CONTRA-CENPRI'
		//1- usuario
		//2-departamento
		//3-grupo
		 try{
			sResponsable = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "AVISOELEC_DEPCONTRA_PETSUM");
			//sResponsable = "3-400";
			mensaje = "Ha sido realizado el informe técnico del expediente: "+rulectx.getNumExp();
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}
}
