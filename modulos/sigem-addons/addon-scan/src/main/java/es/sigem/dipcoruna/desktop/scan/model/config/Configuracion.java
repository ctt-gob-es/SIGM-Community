package es.sigem.dipcoruna.desktop.scan.model.config;

import java.util.ArrayList;
import java.util.List;

public class Configuracion {
    private List<ScanProfile> scanProfiles = new ArrayList<ScanProfile>();

    public List<ScanProfile> getScanProfiles() {
        return scanProfiles;
    }

    public void setScanProfiles(final List<ScanProfile> profiles) {
        this.scanProfiles = profiles;
    }
}
