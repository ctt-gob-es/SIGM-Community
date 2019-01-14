package es.dipucr.sigem.api.rule.procedures.personal;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;

public class AvisoElectronicoPersonalPetConcurso extends AvisoElectronico{
	protected static final Logger logger = Logger.getLogger(AvisoElectronicoPersonalPetConcurso.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		//iusergrouphdr name='PROC-SUMNISTRO-CONTRA-CENPRI'
		//1- usuario
		//2-departamento
		//3-grupo
		 try{
			sResponsable = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "AVISOELEC_DEPPERSONAL_PETPUESTOTRAB");
			//sResponsable = "3-400";
			mensaje = "Se ha creado un nuevo procedimiento de Petición de provisión de puesto de trabajo en el expediente: "+rulectx.getNumExp();
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}
}
