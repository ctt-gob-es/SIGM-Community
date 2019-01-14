package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTRuleDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

/**
 * [dipucr-Felipe #665]
 * @since 06.02.2018
 * @author FELIPE
 *
 * @deprecated La regla no funciona. No encuentro la forma de conseguir que recuperemos el id_regla
 * o el nombre en el cátalogo desde este contexto. Sólo tenemos acceso a la clase de la regla, pero la clase
 * para todas las instancias de la regla es la misma, por lo que no hay manera de saber cúal la llamó
 */
public class AvisoElectronicoUsuarioDatosRegla extends AvisoElectronico{
	
	private static final Logger LOGGER = Logger.getLogger(AvisoElectronicoUsuarioDatosRegla.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		IClientContext cct = rulectx.getClientContext();
		String nombreProc = "";
		
		try {
			DbCnt cnt = cct.getConnection();
			CTRuleDAO rule = CTRuleDAO.findRuleByClass(cnt, this.getClass().getCanonicalName());
			String nombreUsuario = rule.getString("DESCRIPCION");
			if (StringUtils.isEmpty(nombreUsuario)){
				LOGGER.error("En la configuración de la regla, debe incluir el nombre del usuario a avisar en el campo descripción.");	
			}
			else{
				sResponsable = UsuariosUtil.getCampoUsuario((ClientContext) cct, nombreUsuario, "ID");
				if (StringUtils.isEmpty(sResponsable)){
					LOGGER.error("En la configuración de la regla, el usuario incluido '" + nombreUsuario + "' no existe");
				}
				else{
					sResponsable = "1-" + sResponsable;
					
					IItem expProc = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());			
					if (expProc != null) {
						nombreProc = expProc.getString("NOMBREPROCEDIMIENTO");
					}
				}
			}

		} catch (ISPACException e) {
			LOGGER.error("Error al obtener el nombre del procedimiento "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre del procedimiento "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		mensaje = "Aviso electrónico: " + rulectx.getNumExp() + " Nombre procedimiento:" + nombreProc;
		return true;
	}
}
