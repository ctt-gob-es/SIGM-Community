package es.dipucr.sigem.api.rule.procedures.rrhh;

import java.util.Calendar;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.pempleado.services.licencias.LicenciasAytosWSProxy;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.webempleado.services.licencias.LicenciasWSProxy;

public class LicenciasWSDispatcher {

	protected static final String LICENCIAS_DPCR_VARNAME = "LICENCIAS_DPCR";
	protected static final String COD_LICENCIA_VACACIONES = "05";
	protected static final String COD_LICENCIA_MOSCOSOS = "07";
	protected static final String BBDD_SI = "S";
	
	
	public static boolean esLicenciasDipucr(IClientContext cct) throws ISPACException{
		
		String strLicenciasDpcr = ConfigurationMgr.getVarGlobal(cct, LICENCIAS_DPCR_VARNAME);
		return (!StringUtils.isEmpty(strLicenciasDpcr) && Boolean.valueOf(strLicenciasDpcr));
	}
	
	/**
	 * Pone la licencia en estado anulada
	 * @param cct
	 * @param nif
	 * @param ano
	 * @param nlic
	 * @param observaciones
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public static boolean ponerLicenciaAnulada(IClientContext cct, String idLicencia, String observaciones) throws Exception{
	
		if (esLicenciasDipucr(cct)){
			LicenciasWSProxy ws = new LicenciasWSProxy();
			LicenciasId id = new LicenciasId(idLicencia);
			return ws.ponerLicenciaAnulada(id.getNif(), id.getAnio(), id.getNlic(), observaciones);
		}
		else{
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			LicenciasAytosWSProxy ws = new LicenciasAytosWSProxy();
			return ws.ponerLicenciaAnulada(idEntidad, Integer.valueOf(idLicencia), observaciones);
		}
	}
	
	/**
	 * Pone la licencia en estado validada
	 * @param cct
	 * @param nif
	 * @param ano
	 * @param nlic
	 * @param bFirmada
	 * @param motivo
	 * @param firmantes
	 * @return
	 * @throws java.rmi.RemoteException
	 */
    public static boolean ponerLicenciaValidada(IClientContext cct, String idLicencia, 
    		boolean bFirmada, String motivo, String firmantes) throws Exception{
    	
    	if (esLicenciasDipucr(cct)){
			LicenciasWSProxy ws = new LicenciasWSProxy();
			LicenciasId id = new LicenciasId(idLicencia);
			return ws.ponerLicenciaValidada(id.getNif(), id.getAnio(), id.getNlic(), bFirmada, motivo, firmantes);
		}
		else{
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			LicenciasAytosWSProxy ws = new LicenciasAytosWSProxy();
			return ws.ponerLicenciaValidada(idEntidad, Integer.valueOf(idLicencia), bFirmada, motivo, firmantes);
		}
    }
    
    /**
     * Crea una nueva licencia pendiente de validar
     * @param cct
     * @param nif
     * @param tlic
     * @param ano
     * @param finicio
     * @param ffinal
     * @param dias
     * @param observaciones
     * @param sigemNumexp
     * @param sigemNreg
     * @param sigemFreg
     * @return
     * @throws java.rmi.RemoteException
     */
    public static String crearLicenciaPendiente(IClientContext cct, String nif, String tlic, int ano, Calendar finicio, Calendar ffinal, 
    		String numDias, String detalleDias, String observaciones, String sigemNumexp, String sigemNreg, Calendar sigemFreg) throws Exception{
    	
    	if (esLicenciasDipucr(cct)){
			LicenciasWSProxy ws = new LicenciasWSProxy();
			return ws.crearLicenciaPendiente(nif, tlic, ano, finicio, ffinal, numDias, 
					observaciones, sigemNumexp, sigemNreg, sigemFreg);
		}
		else{
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			LicenciasAytosWSProxy ws = new LicenciasAytosWSProxy();
			int idLicencia = ws.crearLicenciaPendiente(idEntidad, nif, tlic, String.valueOf(ano), finicio, ffinal, 
					Integer.valueOf(numDias), detalleDias, observaciones, sigemNumexp, sigemNreg, sigemFreg);
			return String.valueOf(idLicencia);
		}
    }
    
    /**
     * Comprueba la exstencia de licencias coincidentes antes de la creación de la licencia
     * @param cct
     * @param nif
     * @param ano
     * @param finicio
     * @param ffinal
     * @return
     * @throws java.rmi.RemoteException
     */
    public static boolean existenLicenciasCoincidentes(IClientContext cct, String nif, int ano, Calendar finicio, Calendar ffinal) throws Exception{
    
    	if (esLicenciasDipucr(cct)){
			LicenciasWSProxy ws = new LicenciasWSProxy();
			return ws.existenLicenciasCoincidentes(nif, ano, finicio, ffinal);
		}
		else{
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			LicenciasAytosWSProxy ws = new LicenciasAytosWSProxy();
			return ws.existenLicenciasCoincidentes(idEntidad, nif, String.valueOf(ano), finicio, ffinal);
		}
    }
    
    
    /**
	 * Comprueba si la licencia recibida necesita control de número de días
	 * @return
     * @throws ISPACException 
	 */
	public static boolean tieneMaximoDias(IClientContext cct, IItem itemSolicitudLicencias) throws ISPACException{
		
		if (esLicenciasDipucr(cct)){
			String strTipoLicencia = itemSolicitudLicencias.getString("COD_LICENCIA");
			return (strTipoLicencia.equals(COD_LICENCIA_MOSCOSOS) ||
					strTipoLicencia.equals(COD_LICENCIA_VACACIONES));
		}
		else{
			String strMaximoDias = itemSolicitudLicencias.getString("MAXIMO_DIAS");
    		return BBDD_SI.equals(strMaximoDias);
		}
	}

}
