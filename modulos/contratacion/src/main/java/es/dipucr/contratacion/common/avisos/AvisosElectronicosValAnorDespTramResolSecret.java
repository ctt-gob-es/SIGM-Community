package es.dipucr.contratacion.common.avisos;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class AvisosElectronicosValAnorDespTramResolSecret extends AvisoElectronico{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sResponsable = "1-110";
		mensaje = "Realizado el informe de contratación para los Valores Anormales y Desproporcionados "+rulectx.getNumExp();
		return true;
	}
}
