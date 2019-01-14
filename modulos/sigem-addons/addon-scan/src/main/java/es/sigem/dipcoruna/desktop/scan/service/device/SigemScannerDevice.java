package es.sigem.dipcoruna.desktop.scan.service.device;

import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;

/**
 * Interface que extiende el interface básico de los dispositivos de escaneo
 * @author Diputacion da Coruña
 *
 */
public interface SigemScannerDevice extends ScannerDevice {
	public void setPerfil(ScanProfile scanProfile) throws ScannerIOException;
}
