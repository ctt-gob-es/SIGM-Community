package es.sigem.dipcoruna.desktop.scan.service.device;

import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.twain.TwainSource;

public class SigemScannerDeviceParser {

	public static SigemScannerDevice parse(ScannerDevice scannerDevice) {
		if (scannerDevice instanceof TwainSource) {
			return new SigemTwainSourceDevice((TwainSource)scannerDevice);
		}
		throw new IllegalArgumentException("Clase de dispositivo no reconocida: " + scannerDevice.getClass());
	}
}
