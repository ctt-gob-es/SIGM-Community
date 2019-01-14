package es.dipucr.orve.sw.utils;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.dto.Entidad;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import es.dipucr.orve.beans.DEMOORVEBean;
import es.dipucr.orve.beans.ORVEBean;
import es.dipucr.orve.beans.ORVEBeanInterface;
import es.dipucr.orve.beans.ORVEBeanSuperClass;

public class SWOrveUtils {
    
    private static final Logger LOGGER = Logger.getLogger(SWOrveUtils.class);
    
    public static void recuperaRegistros(Entidad entidad) {
        
        LOGGER.warn("-------- Inicio del procesamiento de la entidad " + entidad.getIdentificador() + " - " + entidad.getNombre() + " --------");
    
        ORVEBeanInterface orveBean = new ORVEBean(entidad);
        
        if(null == orveBean || orveBean.isEsDemoOrve()){
            orveBean = new DEMOORVEBean(entidad);
        }    
        
        if(null != orveBean && orveBean.isTieneConfiguracion()){
                
            int[] arrayIdentificadores = orveBean.getIdentificadoresDeORVE();
        
            if(null != arrayIdentificadores && 0 < arrayIdentificadores.length){
                SWOrveUtils.getRegistrosORVEYConsolidaEnSigem(orveBean, arrayIdentificadores);
            }
            
        } else {
            LOGGER.warn("La entidad: " + entidad.getIdentificador() + " - " + entidad.getNombre() + " no tiene correctamente definidas las propiedades de conexión al SW de ORVE en el fichero de propiedades 'serviciosWeb.properties'");
        }

        LOGGER.warn("-------- Fin del procesamiento de la entidad " + entidad.getIdentificador() + " - " + entidad.getNombre() + " --------");
    }
    
    public static void getRegistrosORVEYConsolidaEnSigem(ORVEBeanInterface orveBean, int[] arrayIdentificadores) {
        
        LOGGER.warn("Se van a consolidar " + arrayIdentificadores.length + " registros de ORVE.");
        
        try{ 
	        for(int identificadorORVE : arrayIdentificadores){
	            
	            if(!orveBean.esRegistroYaConsolidado(identificadorORVE)){
	                String nregSigem = orveBean.getRegistroORVEYConsolidaEnSigem(identificadorORVE);
	                
	                if(StringUtils.isNotEmpty(nregSigem)){
	                    orveBean.anotaRegistroEnHistoricoORVE(identificadorORVE, nregSigem);
	                
	                } else {
	                    orveBean.setDeshacerFechaUltimaActualizacion(true);
	                }
	            } 
	        }
	        
	        if(orveBean.isDeshacerFechaUltimaActualizacion()){
	            orveBean.enviaCorreoRollbackFechaUltimaActualizacion();    
	        }
        } catch (RemoteException e){
        	LOGGER.error(ORVEBeanSuperClass.MENSAJE_ERROR_RECUPERAR_REGISTRO + orveBean.getEntidad().getIdentificador() + " - " + orveBean.getEntidad().getNombre() + ". " + e.getMessage(), e);
        	orveBean.setDeshacerFechaUltimaActualizacion(true);
            orveBean.deshacerFechaUltimaActualizacion();
		}
    }
    
    private SWOrveUtils(){
    }
}
