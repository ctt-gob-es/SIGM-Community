package es.sigem.dipcoruna.desktop.scan.repository;

import java.util.List;

import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;

public interface ConfiguracionRepository {

//    List<ScanProfile> getAllScanProfiles();

    List<ScanProfile> getAllScanProfiles(String nombreDispositivo);

	//List<String> getNombreProfiles(String nombreDispositivo);

	boolean existsScanProfile(String nombre, String nombreDispositivo);

	ScanProfile getScanProfile(String nombre, String nombreDispositivo);

	void updateScanProfile(ScanProfile scanProfile);

	void insertScanProfile(ScanProfile scanProfile);

	void deleteScanProfile(String nombre, String nombreDispositivo);

}
