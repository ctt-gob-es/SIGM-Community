package es.sigem.dipcoruna.desktop.scan.events.listener.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.CrearOActualizarScanProfileEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.EliminarScanProfileEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.MostrarConfigPerfilDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.ScanProfileActualizadoEvent;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.desktop.scan.repository.ConfiguracionRepository;
import es.sigem.dipcoruna.desktop.scan.ui.ConfigPerfilDialog;

@Component("configPerfilDialogUIListener")
public class ConfigPerfilDialogUIListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPerfilDialogUIListener.class);

    @Autowired
    private ConfigPerfilDialog configPerfilDialog;

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void onApplicationEvent(final MostrarConfigPerfilDialogEvent event) {
        LOGGER.debug("Detectado evento {}", event);

        if (StringUtils.hasLength(event.getNombrePerfil())) {
            final ScanProfile scanProfile = configuracionRepository.getScanProfile(event.getNombrePerfil(), event.getNombreDispositivo());
            configPerfilDialog.mostrarPerfil(scanProfile);
        }
        else {
            final ScanProfile scanProfile = new ScanProfile();
            scanProfile.setNombreDispositivo(event.getNombreDispositivo());
            configPerfilDialog.mostrarPerfil(scanProfile);
        }
        configPerfilDialog.setVisible(true);
    }



    @EventListener
    public void onApplicationEvent(final CrearOActualizarScanProfileEvent event) {
        LOGGER.debug("Detectado evento {}", event);
        if (configuracionRepository.existsScanProfile(event.getScanProfile().getNombre(), event.getScanProfile().getNombreDispositivo())) {
            configuracionRepository.updateScanProfile(event.getScanProfile());
        }
        else {
            configuracionRepository.insertScanProfile(event.getScanProfile());
        }
        configPerfilDialog.dispose();
        configPerfilDialog.setVisible(false);

        applicationEventPublisher.publishEvent(new ScanProfileActualizadoEvent(this, event.getScanProfile()));
    }


    @EventListener
    public void onApplicationEvent(final EliminarScanProfileEvent event) {
        LOGGER.debug("Detectado evento {}", event);
        if (configuracionRepository.existsScanProfile(event.getNombrePerfil(), event.getNombreDispositivo())) {
            configuracionRepository.deleteScanProfile(event.getNombrePerfil(), event.getNombreDispositivo());
        }

        configPerfilDialog.dispose();
        configPerfilDialog.setVisible(false);

       // applicationEventPublisher.publishEvent(new MostrarConfigListDialogEvent(this));
        applicationEventPublisher.publishEvent(new ScanProfileActualizadoEvent(this));
    }
}
