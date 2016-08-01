package es.aww.sigem.generico.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.EntidadesException;

import org.apache.log4j.Logger;

public class NombreEntidad implements IRule{
	private static final Logger logger = Logger.getLogger(NombreEntidad.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {    
			return true;	
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {		
		String descEntidad = "";		
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		if (info != null) {
			
			String entidad = info.getOrganizationId();
			try {
				descEntidad = LocalizadorServicios.getServicioEntidades().obtenerEntidad(entidad).getNombreLargo();
			} catch (EntidadesException e) {
				logger.error("Error al recuperar la descripción de la entidad. " + e.getMessage(), e);
			} catch (SigemException e) {
				logger.error("Error al recuperar la descripción de la entidad. " + e.getMessage(), e);
			}
			
			return descEntidad;
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

