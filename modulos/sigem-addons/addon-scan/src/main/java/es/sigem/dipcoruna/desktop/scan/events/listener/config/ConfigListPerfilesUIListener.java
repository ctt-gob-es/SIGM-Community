package es.sigem.dipcoruna.desktop.scan.events.listener.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.scan.events.model.config.listaperfiles.DispositivoSeleccionadConfigListDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.listaperfiles.MostrarConfigListDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.ScanProfileActualizadoEvent;
import es.sigem.dipcoruna.desktop.scan.repository.ConfiguracionRepository;
import es.sigem.dipcoruna.desktop.scan.service.scan.ScanService;
import es.sigem.dipcoruna.desktop.scan.ui.ConfigListPerfilesDialog;

@Component("configListPerfilesUIListener")
public class ConfigListPerfilesUIListener  {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigListPerfilesUIListener.class);

	@Autowired
	private ConfigListPerfilesDialog configListDialog;

    @Autowired
    private ScanService scanService;

	@Autowired
	private ConfiguracionRepository configuracionRepository;


	@EventListener
	public void onApplicationEvent(final MostrarConfigListDialogEvent event) {
		LOGGER.debug("Detectado evento {}", event);
		configListDialog.repitarListaDispositivos(scanService.getNombresScannersDisponibles());

		final String dispositivoSeleccionado = configListDialog.getSelectedDispositivo();
		configListDialog.repintarPerfilesEnLista(configuracionRepository.getAllScanProfiles(dispositivoSeleccionado));

		if (!configListDialog.isVisible()) {
		    configListDialog.setVisible(true);
		}
	}


    @EventListener
    public void onApplicationEvent(final DispositivoSeleccionadConfigListDialogEvent event) {
        final String dispositivoSeleccionado = configListDialog.getSelectedDispositivo();
        configListDialog.repintarPerfilesEnLista(configuracionRepository.getAllScanProfiles(dispositivoSeleccionado));
    }


	@EventListener
	public void onApplicationEvent(final ScanProfileActualizadoEvent event) {
	    final String dispositivoSeleccionado = configListDialog.getSelectedDispositivo();
        configListDialog.repintarPerfilesEnLista(configuracionRepository.getAllScanProfiles(dispositivoSeleccionado));
	}

}
