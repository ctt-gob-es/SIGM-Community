package es.sigem.dipcoruna.desktop.scan.events.listener.scandesktop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.scan.events.model.ErrorGeneralEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.ScanProfileActualizadoEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.CambioNombreFicheroEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.DispositivoSeleccionadoScanDesktopDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.FicherosSubidosCorrectamenteEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.MostrarDesktopScanDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.NuevoFicheroEscaneadoEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.SolicitadaSubidaFicherosEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.SolicitadoBorradoFicheroEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.SolicitadoEscaneadoEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.SubidaFicherosErroneaEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.UnirPdfsScanDesktopDialogEvent;
import es.sigem.dipcoruna.desktop.scan.exceptions.UploadFilesException;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.desktop.scan.model.upload.UploadFilesResult;
import es.sigem.dipcoruna.desktop.scan.repository.ConfiguracionRepository;
import es.sigem.dipcoruna.desktop.scan.repository.ScanFilesRepository;
import es.sigem.dipcoruna.desktop.scan.service.PdfService;
import es.sigem.dipcoruna.desktop.scan.service.scan.ScanService;
import es.sigem.dipcoruna.desktop.scan.service.upload.UploadServiceFactory;
import es.sigem.dipcoruna.desktop.scan.ui.ScanDesktopDialog;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;
import es.sigem.dipcoruna.framework.service.util.PreferenciasHolder;

@Component("scanDesktopUIListener")
public class ScanDesktopUIListener  {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScanDesktopUIListener.class);

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
    private SimpleMessageSource messageSource;
	
    @Value("${param.maxUploadFiles}")
    private Integer maxUploadFiles;


	@Autowired
	private ScanDesktopDialog scanDesktopDialog;

	@Autowired
	private ScanService scanService;
	
	@Autowired
	private PdfService pdfService;

	@Autowired
	private UploadServiceFactory uploadServiceFactory;

	@Autowired
	private ScanFilesRepository scanFilesRepository;

	@Autowired
	private ConfiguracionRepository configuracionRepository;

	@Autowired
    private PreferenciasHolder preferenciasHolder;


	@EventListener
	public void onApplicationEvent(final MostrarDesktopScanDialogEvent event) {
		if (!scanDesktopDialog.isVisible()) {
			scanDesktopDialog.actualizarListaDispositivos(scanService.getNombresScannersDisponibles());
			if (getUltimoNombreDispositivoUsado() != null) {
			    scanDesktopDialog.setSelectedDispositivo(getUltimoNombreDispositivoUsado());
			}

			final String dispositivoSeleccionado = scanDesktopDialog.getSelectedDispositivo();
			scanDesktopDialog.actualizarListaPerfiles(configuracionRepository.getAllScanProfiles(dispositivoSeleccionado));
			if (getUltimoNombrePerfilUsado() != null) {
                scanDesktopDialog.setSelectedPerfil(getUltimoNombrePerfilUsado());
            }
		}
		scanDesktopDialog.actualizarFicheros(scanFilesRepository.getFiles());
		scanDesktopDialog.setVisible(true);
		scanDesktopDialog.repaint();
	}


	@EventListener
    public void onApplicationEvent(final DispositivoSeleccionadoScanDesktopDialogEvent event){
        final String dispositivoSeleccionado = scanDesktopDialog.getSelectedDispositivo();
        scanDesktopDialog.actualizarListaPerfiles(configuracionRepository.getAllScanProfiles(dispositivoSeleccionado));
    }


	@EventListener
    public void onApplicationEvent(final NuevoFicheroEscaneadoEvent event){
		LOGGER.info("Se ha escaneado correctamente el fichero '{}' con tamaño {}. Ahora se moverá al directorio definitivo", event.getFile(), event.getFile().length());
        scanFilesRepository.moveFile(event.getFile());
        scanDesktopDialog.actualizarFicheros(scanFilesRepository.getFiles());
        scanDesktopDialog.repaint();
    }


	@EventListener
	public void onApplicationEvent(final SolicitadoBorradoFicheroEvent event) {
		LOGGER.debug("Borrando fichero escaneado {}", event.getFile());
		scanFilesRepository.deleteFile(event.getFile());
		scanDesktopDialog.actualizarFicheros(scanFilesRepository.getFiles());
	    scanDesktopDialog.repaint();
	}


	@EventListener
	public void onApplicationEvent(final SolicitadoEscaneadoEvent event) {
		LOGGER.info("Abriendo escaner para obtener fichero");
		final ScanProfile scanProfile = configuracionRepository.getScanProfile(event.getNombrePerfil(), event.getNombreDispositivo());
		scanService.realizarEscaneo(scanProfile);
		guardarUltimoEscaneado(event.getNombrePerfil(), event.getNombreDispositivo());
	}
	


    @EventListener
    public void onApplicationEvent(final UnirPdfsScanDesktopDialogEvent event) {
        LOGGER.info("Uniendo pdfs");
        final File joinFile = pdfService.joinPdfs(event.getFiles());
        
        scanFilesRepository.moveFile(joinFile);
        for (File file : event.getFiles()) {
            scanFilesRepository.deleteFile(file);    
        }                
        scanDesktopDialog.actualizarFicheros(scanFilesRepository.getFiles());
        scanDesktopDialog.repaint();                      
    }
        
	
    @EventListener
    public void onApplicationEvent(final ScanProfileActualizadoEvent event) {
        LOGGER.debug("Actualizando lista de perfiles de escaneo en la pantalla de escaneo");
        final String dispositivoSeleccionado = scanDesktopDialog.getSelectedDispositivo();
        scanDesktopDialog.actualizarListaPerfiles(configuracionRepository.getAllScanProfiles(dispositivoSeleccionado));
    }

	@EventListener
	public void onApplicationEvent(final SolicitadaSubidaFicherosEvent event) {
		LOGGER.debug("Subiendo archivos al servidor");
		final List<File> files = scanFilesRepository.getFiles();
		if (files.isEmpty()) {
		    applicationEventPublisher.publishEvent(new ErrorGeneralEvent(this, "error.error", "error.subirFicheros.noHayArchivos"));
		    return;
		}
		
		if (maxUploadFiles != null && files.size() > maxUploadFiles) {
		    applicationEventPublisher.publishEvent(new ErrorGeneralEvent(this, "error.error", "error.subirFicheros.numeroMaximo", new String[] {maxUploadFiles.toString()}));
            return;
		}
		
	
		try {
		    final UploadFilesResult resultadoSubida = uploadServiceFactory.getUpladService().subirFicheros(scanFilesRepository.getFiles());
		    applicationEventPublisher.publishEvent(new FicherosSubidosCorrectamenteEvent(this, resultadoSubida.getFicherosOK(), resultadoSubida.getFicherosError()));
		}
		catch (final UploadFilesException e) {
		    applicationEventPublisher.publishEvent(new SubidaFicherosErroneaEvent(this, e.getErroresServidor()));
		}

	}



    @EventListener
    public void onApplicationEvent(final SubidaFicherosErroneaEvent event) {
        if (event.getErroresServidor() != null) {
            String errores = "";
            for (final String error : event.getErroresServidor()) {
                errores += error + "\n";
            }

            JOptionPane.showMessageDialog(null,
                    messageSource.getMessage("error.subirFicheros.errorServidor", new String[] {errores}),
                    messageSource.getMessage("error.error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        else {
            JOptionPane.showMessageDialog(null,
                    messageSource.getMessage("error.subirFicheros.errorGeneral"),
                    messageSource.getMessage("error.error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }



	@EventListener
	public void onApplicationEvent(final FicherosSubidosCorrectamenteEvent event) {
	    LOGGER.debug("Actualizando lista de perfiles de escaneo en la pantalla de escaneo");
	    if (!event.getFicherosErroneos().isEmpty()) {

	        final String erroes = Arrays.toString(event.getFicherosErroneos().toArray());

	        JOptionPane.showMessageDialog(null,
	                messageSource.getMessage("subidaFicheros.subidaConErrores", new String[] {erroes}),
	                messageSource.getMessage("subidaFicheros.resultado"),
	                JOptionPane.ERROR_MESSAGE
	        );

	        final List<String> ficherosSubidosSinComillas = new ArrayList<String>();
	        for (final String ficheroConComillas : event.getFicherosSubidos()) {
                ficherosSubidosSinComillas.add(ficheroConComillas.replace("\"", ""));
            }

	        scanFilesRepository.deleteFiles(ficherosSubidosSinComillas);

	        scanDesktopDialog.actualizarFicheros(scanFilesRepository.getFiles());
	        scanDesktopDialog.repaint();
	    }
	    else {
	        scanFilesRepository.deleteAllFiles();
    	    JOptionPane.showMessageDialog(null,
    	            messageSource.getMessage("subidaFicheros.subidaOK"),
    	            messageSource.getMessage("subidaFicheros.resultado"),
                    JOptionPane.INFORMATION_MESSAGE
            );
    	    System.exit(0);
	    }

	}

	@EventListener
	public void onApplicationEvent(final CambioNombreFicheroEvent event) {
		LOGGER.info("Va a renombrar el fichero.");		
        scanFilesRepository.renombraFile(event.getFile(), event.getNombreFichero());
        scanDesktopDialog.actualizarFicheros(scanFilesRepository.getFiles());
        scanDesktopDialog.repaint();
	}


	private void guardarUltimoEscaneado(final String nombrePerfil, final String nombreDispositivo) {
	    preferenciasHolder.putProperty("LastScan", nombreDispositivo + "######" + nombrePerfil);
	}

	private String getUltimoNombreDispositivoUsado() {
	    final String lastScan = preferenciasHolder.getProperty("LastScan");
	    if (StringUtils.hasLength(lastScan)) {
	        return lastScan.substring(0, lastScan.indexOf("######"));
	    }
	    return null;
	}

	private String getUltimoNombrePerfilUsado() {
        final String lastScan = preferenciasHolder.getProperty("LastScan");
        if (StringUtils.hasLength(lastScan)) {
            return lastScan.substring(lastScan.indexOf("######") + 6);
        }
        return null;
    }

}
