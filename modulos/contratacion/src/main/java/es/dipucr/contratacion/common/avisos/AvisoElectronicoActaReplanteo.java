package es.dipucr.contratacion.common.avisos;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class AvisoElectronicoActaReplanteo extends AvisoElectronico{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sResponsable = "1-21";
		mensaje = "Ha sido añadido Acta de Replanteo del expediente: "+rulectx.getNumExp();
		return true;
	}
}
