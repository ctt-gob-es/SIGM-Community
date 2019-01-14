package es.sigem.dipcoruna.desktop.scan.service.scan;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata.Type;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.NuevoFicheroEscaneadoEvent;
import es.sigem.dipcoruna.desktop.scan.exceptions.ScannerException;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.desktop.scan.service.ImageWriterWrapper;
import es.sigem.dipcoruna.desktop.scan.service.device.SigemScannerDevice;
import es.sigem.dipcoruna.desktop.scan.service.device.SigemScannerDeviceParser;


@Service("scanService")
public class ScanServiceImpl implements InitializingBean, ScannerListener, ScanService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScanServiceImpl.class);

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired @Qualifier("pdfImageWriterWrapper")
	private ImageWriterWrapper imageWriterWrapper;

	private Scanner scanner;
	private ScanProfile profile;



	@Override
	public void afterPropertiesSet() throws Exception {
		scanner = Scanner.getDevice();
		scanner.addListener(this);
		LOGGER.debug("Inicializado objeto scanner de tipo {}", scanner);
	}



	@Override
	public List<String> getNombresScannersDisponibles() {
		try {
			return Arrays.asList(scanner.getDeviceNames());
		} catch (final ScannerIOException e) {
			LOGGER.error("Error al recuperar los nombres de los escáners disponibles", e);
			throw new ScannerException("Error al recuperar los nombres de los escáners disponibles", e);
		}
	}


	@Override
	public void realizarEscaneo(final ScanProfile profile) {
		imageWriterWrapper.init();
		this.profile = profile;

		try {
		    seleccionarEscaner(profile.getNombreDispositivo());
			scanner.acquire();
		} catch (final ScannerIOException e) {
		    LOGGER.error("Error al adquirir imagen con el escáner '{}'", profile.getNombreDispositivo());
	        throw new ScannerException("Error al seleccionar el escaner " + profile.getNombreDispositivo() + ": " + e.getMessage());
		}
	}

	private void seleccionarEscaner(final String nombreDispositivo) {
	    try {
            scanner.select(nombreDispositivo);
        }
        catch (final ScannerIOException e) {
          LOGGER.error("Error al seleccionar el escáner '{}'", nombreDispositivo);
          throw new ScannerException("Error al seleccionar el escáner " + nombreDispositivo + ": " + e.getMessage());
        }
	}

	

	@Override
	public void update(final Type type, final ScannerIOMetadata metadata) {
	    if (LOGGER.isDebugEnabled()) {
	        mostrarTipoDeEvento(type, metadata);
	    }
	    
		try {
			if (type.equals(ScannerIOMetadata.ACQUIRED)) {
			    imageWriterWrapper.write(metadata.getImage());
			}
			else if (type.equals(ScannerIOMetadata.STATECHANGE)) {
				if (metadata.isFinished()) {
					imageWriterWrapper.finishWrite();
					if (imageWriterWrapper.seEscribioFichero()) {
						applicationEventPublisher.publishEvent(new NuevoFicheroEscaneadoEvent(this, imageWriterWrapper.getFile()));
					}
				}
			}
			else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {
				establecerPerfilEscaneadoVigente(metadata);
			}
			else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
				LOGGER.error("Se ha producido un error interno en el escaner", metadata.getException());
			}
		}
		catch(final Exception e) {
			LOGGER.error("Error al procesar evento", e);
			throw new ScannerException("Error al procesar evento ", e);
		}

	}


    private void establecerPerfilEscaneadoVigente(final ScannerIOMetadata metadata) {
		try {
			final ScannerDevice device = metadata.getDevice();
			final SigemScannerDevice sigemScannerDevice = SigemScannerDeviceParser.parse(device);
			sigemScannerDevice.setPerfil(profile);
			sigemScannerDevice.setShowUserInterface(false);
			//sigemScannerDevice.setShowProgressBar(false);
		}
		catch (final Exception e) {
			LOGGER.error("Error al establecer el profile de escaneo", e);
		}
	}



	private void mostrarTipoDeEvento(final Type type, final ScannerIOMetadata metadata) {
		if (type.equals(ScannerIOMetadata.INFO)) {
			LOGGER.debug("Recibido evento de tipo INFO");
		} else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
			LOGGER.debug("Recibido evento de tipo EXCEPTION!!!!!");
		} else if (type.equals(ScannerIOMetadata.ACQUIRED)) {
			LOGGER.debug("Recibido evento de tipo ACQUIRED");
		} else if (type.equals(ScannerIOMetadata.FILE)) {
			LOGGER.debug("Recibido evento de tipo FILE");
		} else if (type.equals(ScannerIOMetadata.MEMORY)) {
			LOGGER.debug("Recibido evento de tipo MEMORY");
		} else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {
			LOGGER.debug("Recibido evento de tipo NEGOTIATE");
		} else if (type.equals(ScannerIOMetadata.STATECHANGE)) {
			LOGGER.debug("Recibido evento de tipo STATECHANGE");
		} else {
			LOGGER.debug("Recibido evento de tipo desconocido!!!!");
		}

		LOGGER.debug("Metadata[info: '{}', state: '{}',  stateStr: '{}', imagen: {}, file: '{}', isFinished: '{}'",
				new Object[] {metadata.getInfo(), metadata.getState(), metadata.getStateStr(), metadata.getImage(), metadata.getFile(), metadata.isFinished()});
	}
}
