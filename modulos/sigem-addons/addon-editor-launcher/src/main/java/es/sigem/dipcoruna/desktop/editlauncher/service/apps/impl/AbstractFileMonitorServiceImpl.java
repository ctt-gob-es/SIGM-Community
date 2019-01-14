package es.sigem.dipcoruna.desktop.editlauncher.service.monitor.impl;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;

import es.sigem.dipcoruna.desktop.editlauncher.events.model.EditorCerradoEvent;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.FicheroModificadoEvent;
import es.sigem.dipcoruna.desktop.editlauncher.service.monitor.FileMonitorService;


public abstract class AbstractFileMonitorServiceImpl implements FileMonitorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileMonitorServiceImpl.class);    
          
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher; 
        
           
    @Override
    @Async
    public void startMonitor(String filePath) {             
        final File file = new File(filePath);
        
        esperarAQueSeBloqueeElFichero(file);
        LOGGER.info("Comienza la monitorización del fichero {}", file);  
        
        long ultimaModificacion = file.lastModified();
        while(true) {        
            LOGGER.debug("Esperando detectar cambios en el fichero editado {}", file.getName());
            esperarSegundos(2);
            
                                   
            if (file.lastModified() != ultimaModificacion) {
                ultimaModificacion = file.lastModified();               
                LOGGER.info("Detectada modificación en el fichero {}", filePath);
                applicationEventPublisher.publishEvent(new FicheroModificadoEvent(this, filePath));
            }                    
            
            if (!isFileLocked(file)) {
            	esperarSegundos(2);
            	//Doble check
            	if (!isFileLocked(file)) {
            		 LOGGER.info("Se detecta el final del proceso del editor en el fichero {}", file.getName());                
            		 applicationEventPublisher.publishEvent(new EditorCerradoEvent(this, null, filePath));
            		 break;
            	 }
            } 
                                         
        }
         
       LOGGER.info("Eliminado monitor para fichero {}", filePath);            
    }



    private void esperarAQueSeBloqueeElFichero(File file) {
       for(int i=0; i<60; i++) {
           esperarSegundos(1);
           if (isFileLocked(file)) {
               return;
           }
       }
       
       LOGGER.warn("El editor no ha sido capaz de bolquear el fichero {} en un tiempo máximo", file);  
    }

    private void esperarSegundos(long segundos) {
        try {
            TimeUnit.SECONDS.sleep(segundos);
        }
        catch (InterruptedException e) {}
    }
    
   
    
    protected abstract boolean isFileLocked(File file);
   
}
