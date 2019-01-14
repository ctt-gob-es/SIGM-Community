package es.sigem.dipcoruna.desktop.editlauncher.service.monitor.impl;

import java.io.File;


public class LinuxFileMonitorServiceImpl extends AbstractFileMonitorServiceImpl {
     
    @Override
    protected boolean isFileLocked(File file) {
       //El fichero está bloqueado si existe su equivalente .lock
        File f = new File(file.getParent() + "/.~lock." + file.getName() + "#");           
        return f.exists();  
    }           
}
