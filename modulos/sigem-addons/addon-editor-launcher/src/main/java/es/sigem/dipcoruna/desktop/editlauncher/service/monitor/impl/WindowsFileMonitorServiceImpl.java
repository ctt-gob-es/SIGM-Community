package es.sigem.dipcoruna.desktop.editlauncher.service.monitor.impl;

import java.io.File;


public class WindowsFileMonitorServiceImpl extends AbstractFileMonitorServiceImpl {
     
    @Override
    protected boolean isFileLocked(File file) {
       //El fichero está bloqueado si no se puede renombrar
       return file.renameTo(file) == false;
    }    
       
}
