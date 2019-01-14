package es.sigem.dipcoruna.desktop.editlauncher.service.apps.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sigem.dipcoruna.desktop.editlauncher.model.apps.ProcessWrapper;
import es.sigem.dipcoruna.desktop.editlauncher.model.apps.WindowsProcessWrapper;

public class LinuxGeneralAppServiceImpl extends AbstractGeneralAppServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxGeneralAppServiceImpl.class);
    

    @Override
    public ProcessWrapper lanzarAplicacion(String appPath, String[] argumentos) {
        LOGGER.debug("Se va a lanzar aplicación {} con argumentos {}", appPath, argumentos[0]);
        ProcessBuilder processBuilder = new ProcessBuilder(appPath, argumentos[0]);
        try {
            Process process = processBuilder.start();           
            LOGGER.debug("Lanzamiento correcto");
            return new WindowsProcessWrapper(process);
        } catch (IOException e) {       
            LOGGER.error("Error al lanzar la aplicación {} con argumentos {}", appPath, argumentos, e);
            throw new RuntimeException("Error al lanzar la aplicación " + appPath, e);
        }       
    }

}
