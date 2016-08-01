package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.EntidadesException;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

import org.apache.log4j.Logger;

public class EntidadesAdmUtil{
	
	private static final Logger logger = Logger.getLogger(EntidadesAdmUtil.class);
	
	public static String obtenerEntidad(IClientContext cct){
		String entidad = "";
		
		try{
			DbCnt cnt = cct.getConnection();
			String pool = cnt.getPoolName();
			String[] vEntidad = pool.split("_");
			entidad = vEntidad[1];
			}
		catch (ISPACException e){
			logger.error(e.getMessage(), e);
		}
		return entidad;
	}
	
	public static String obtenerNombreLargoEntidadById(IClientContext cct){
		String nombreEntidad = "";
		try{
			ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
			Entidad entidad = servicioEntidades.obtenerEntidad(obtenerEntidad(cct));
			nombreEntidad = entidad.getNombreLargo();
			
		} catch (EntidadesException e) {
			logger.debug("Error al obtener el nombre de la entidad. " +e.getMessage(), e);
		} catch (SigemException e) {
			logger.debug("Error al obtener el nombre de la entidad. " +e.getMessage(), e);
		}
		return nombreEntidad;
	}

	public static boolean tieneEntidadTablaHistoricos(){
		boolean historicos = true;
		String valorVariable = DipucrCommonFunctions.getVarGlobal("TABLA_HISTORICOS");
        if(StringUtils.isNotEmpty(valorVariable)){
        	if(valorVariable.equals("NO")){
        		historicos = false;
        	}
        }
        return historicos;
	}
}