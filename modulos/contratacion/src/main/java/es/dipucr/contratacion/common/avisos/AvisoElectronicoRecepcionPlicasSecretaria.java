package es.dipucr.contratacion.common.avisos;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class AvisoElectronicoRecepcionPlicasSecretaria extends AvisoElectronico{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sResponsable = "1-110";
		mensaje = "Aprobado nuevo expediente de contratación "+rulectx.getNumExp()+" inicio recepción de Plicas.";
		return true;
	}
}
