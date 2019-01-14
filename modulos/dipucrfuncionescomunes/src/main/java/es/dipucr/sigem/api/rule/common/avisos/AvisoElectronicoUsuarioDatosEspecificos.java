package es.dipucr.sigem.api.rule.common.avisos;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoElectronico;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class AvisoElectronicoUsuarioDatosEspecificos extends AvisoElectronico{
	
	private static final Logger LOGGER = Logger.getLogger(AvisoElectronicoUsuarioDatosEspecificos.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		IClientContext cct = rulectx.getClientContext();
		String nombreUsuario = TramitesUtil.getDatosEspecificosOtrosDatos(cct, rulectx.getTaskProcedureId());

		String nombreProc = "";
		try {
			sResponsable = UsuariosUtil.getCampoUsuario((ClientContext) cct, nombreUsuario, "ID");
			sResponsable = "1-" + sResponsable;
			
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
