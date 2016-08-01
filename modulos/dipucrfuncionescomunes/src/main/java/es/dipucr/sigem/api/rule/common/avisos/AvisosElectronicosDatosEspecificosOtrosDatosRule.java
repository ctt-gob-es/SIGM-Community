package es.dipucr.sigem.api.rule.common.avisos;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class AvisosElectronicosDatosEspecificosOtrosDatosRule extends AvisoElectronico{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		IClientContext cct = rulectx.getClientContext();
		String otrosDatos = TramitesUtil.getDatosEspecificosOtrosDatos(cct, rulectx.getTaskProcedureId());
		
		//responsable##mensaje
		String [] datos = otrosDatos.split("##");
		if(datos.length>0){
			sResponsable = datos[0];
			mensaje = datos[1]+" - "+rulectx.getNumExp();
		}
		return true;
	}
}
