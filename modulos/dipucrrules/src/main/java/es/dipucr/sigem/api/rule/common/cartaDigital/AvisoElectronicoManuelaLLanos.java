package es.dipucr.sigem.api.rule.common.cartaDigital;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class AvisoElectronicoManuelaLLanos extends AvisoElectronico{
	
	private static final Logger LOGGER = Logger.getLogger(AvisoElectronicoManuelaLLanos.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sResponsable = "1-18";
		String nombreProc = "";
		try {
			IItem expProc = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());			
			if (expProc != null) {
				nombreProc = expProc.getString("NOMBREPROCEDIMIENTO");
			}

		} catch (ISPACException e) {
			LOGGER.error("Error al obtener el nombre del procedimiento "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre del procedimiento "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		mensaje = "Aviso electrónico: " + rulectx.getNumExp() + " Nombre procedimiento:" + nombreProc;
		return true;
	}
}
