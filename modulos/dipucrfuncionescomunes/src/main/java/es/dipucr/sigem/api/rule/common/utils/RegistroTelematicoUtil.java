package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.entidades.EntidadesManager;
import ieci.tecdoc.sgm.entidades.exception.EntidadException;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.AccesoBBDDeTramitacion;

public class RegistroTelematicoUtil {
	
	private static final Logger logger = Logger.getLogger(RegistroTelematicoUtil.class);
	
	public static boolean modificaDestinoRegistroEntrada(IClientContext cct, String nreg, String estado, String codDepartamento) throws ISPACException{
		boolean resultado = false;
		Entidad entidad = null;
		
		ieci.tecdoc.sgm.entidades.beans.Entidad ent;
		try {
			ent = EntidadesManager.obtenerEntidad(EntidadesAdmUtil.obtenerEntidad(cct));		
			entidad = new Entidad();
			entidad.setIdentificador(ent.getIdentificador());
			entidad.setNombre(ent.getNombreLargo());
		
			AccesoBBDDeTramitacion acc = new AccesoBBDDeTramitacion(entidad.getIdentificador());
			acc.actualizaRegistroTelematico(nreg, estado, "organo", codDepartamento);
			resultado = true;			
		} catch (EntidadException e) {
			logger.error("Error al recuperar la entidad para actualizar el destino en el registro telemático. " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar la entidad para actualizar el destino en el registro telemático. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al actualizar el destino en el registro telemático. " + e.getMessage(), e);
			throw new ISPACException("Error al actualizar el destino en el registro telemático. " + e.getMessage(), e);
		}
		return resultado;
	}
}
