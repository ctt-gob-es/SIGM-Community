package es.sigem.dipcoruna.desktop.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.scan.events.model.config.listaperfiles.MostrarConfigListDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.MostrarDesktopScanDialogEvent;
import es.sigem.dipcoruna.desktop.scan.ui.ScanDesktopDialog;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;
import es.sigem.dipcoruna.framework.service.versionado.GestionVersionesService;

@Service("editLauncherApp")
public class ScanAppImpl implements ScanApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


	@Value("${app.version}")
	private String versionActual;

	@Value("${app.code}")
	private String codigoAplicacion;


	@Autowired
	private GestionVersionesService gestionVersionesService;

	@Autowired
	private ScanDesktopDialog appChooserDialog;

	@Autowired
	private SimpleMessageSource messageSource;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	private boolean descargarMasTarde = false;

	public boolean isDescargarMasTarde() {
		return descargarMasTarde;
	}
	public void setDescargarMasTarde(boolean descargarMasTarde) {
		this.descargarMasTarde = descargarMasTarde;
	}

	@Override
	public void checkVersion(final String urlCheckForUpdates) {
		if (StringUtils.hasLength(urlCheckForUpdates)) {
			descargarMasTarde = gestionVersionesService.comprobarSiUltimaVersion(versionActual, codigoAplicacion, urlCheckForUpdates);
		}
		else {
			descargarMasTarde = gestionVersionesService.comprobarSiUltimaVersion(versionActual, codigoAplicacion);
		}
	}


	@Override
	public void lanzarModoConfiguracion() {
		LOGGER.info("Invocando pantalla de configuración");
		applicationEventPublisher.publishEvent(new MostrarConfigListDialogEvent(this));

	}


	@Override
	public void lanzarOpcionEscaneo() {
		LOGGER.info("Invocando pantalla de escaneo");
		applicationEventPublisher.publishEvent(new MostrarDesktopScanDialogEvent(this));
	}
}
