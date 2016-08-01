package es.dipucr.contratacion.common.avisos;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class AvisoElectronicoInformeTecnico extends AvisoElectronico{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sResponsable = "1-21";
		mensaje = "Ha sido realizado el informe técnico del expediente: "+rulectx.getNumExp();
		return true;
	}
}
