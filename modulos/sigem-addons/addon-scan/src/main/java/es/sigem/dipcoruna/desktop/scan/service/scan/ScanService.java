package es.sigem.dipcoruna.desktop.scan.service.scan;

import java.util.List;

import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;

public interface ScanService {

	List<String> getNombresScannersDisponibles();

	void realizarEscaneo(ScanProfile profile);

}