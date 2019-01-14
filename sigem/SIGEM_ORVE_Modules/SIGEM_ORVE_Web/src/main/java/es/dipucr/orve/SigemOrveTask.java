package es.dipucr.orve;

import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispacweb.scheduler.SchedulerTask;
import ieci.tecdoc.sgm.core.admin.web.AdministracionHelper;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.orve.sw.utils.SWOrveUtils;


/**
 * Tarea para lanzar la gestión de hitos de SIGEM.
 * 
 */
public class SigemOrveTask extends SchedulerTask {

	/** Logger de la clase. */
    private static final Logger LOGGER = Logger.getLogger(SigemOrveTask.class);

    
    /**
     * Ejecuta la tarea del scheduler.
     */
    public void run() {
    	
        LOGGER.warn("######### Ejecutando tarea de consolidación de entradas CONFIRMADAS en ORVE en sus respectivos libros de registro #########");
        
        // Obtener la lista de entidades
        List<?> entidades = AdministracionHelper.obtenerListaEntidades();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Se han encontrado " + (entidades != null ? entidades.size() : 0) + " entidades");
        }
        
        if (!CollectionUtils.isEmpty(entidades)) {
        	for (int i = 0; i < entidades.size(); i++) {
        		
        		// Información de la entidad
        		Entidad entidad = (Entidad) entidades.get(i);
        		if (entidad != null) {

                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Inicio de proceso de entidad #" + (i+1) + ": " + entidad.getIdentificador() + " - " + entidad.getNombre());
                    }

        			// Establecer la entidad en el thread local
    				EntidadHelper.setEntidad(entidad);

    				SWOrveUtils.recuperaRegistros(entidad);

                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Fin de proceso de entidad #" + (i+1) + ": " + entidad.getIdentificador() + " - " + entidad.getNombre());
                    }
        		}
        	}
        }
    }
}