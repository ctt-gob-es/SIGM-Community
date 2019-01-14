package es.dipucr.sigem.api.rule.common.avisos;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class AvisoElectronicoResponExped extends AvisoElectronico{
	
	private static final Logger LOGGER = Logger.getLogger(AvisoElectronicoResponExped.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		String nombreProc = "";
		IItem tramite = null;
		String tramiteNombre = "";
		try {
			sResponsable = ResponsablesUtil.get_ID_RESP_Procedimiento(rulectx);
			IItem expProc = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());			
			if (expProc != null) {
				nombreProc = expProc.getString("NOMBREPROCEDIMIENTO");
			}
			
			tramite = TramitesUtil.getTramite(rulectx.getClientContext(), rulectx.getNumExp(), rulectx.getTaskId());		
			
			if(tramite!=null){
				if(tramite.getString("NOMBRE")!=null){
					tramiteNombre = "Nombre Trámite: "+tramite.getString("NOMBRE");
				}
			}
		} catch (ISPACException e) {
			LOGGER.error("Error al obtener el nombre del procedimiento "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre del procedimiento "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		
		mensaje = "Aviso electrónico: " + rulectx.getNumExp() + " Nombre procedimiento:" + nombreProc + ""+ tramiteNombre;
		return true;
	}
}