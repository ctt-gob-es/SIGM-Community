package es.sigem.dipcoruna.desktop.asyncUploader;

import java.awt.Dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.asyncUploader.ui.AppChooserDialog;
import es.sigem.dipcoruna.desktop.asyncUploader.ui.AppResumeDialog;
import es.sigem.dipcoruna.framework.service.versionado.GestionVersionesService;

/**
 * Implementacion de la base del addon async uploader
 */
@Service("asyncUploaderApp")
public class AsyncUploaderAppImpl implements AsyncUploaderApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUploaderAppImpl.class);
	
	@Value("${app.version}")
	private String versionActual;
	
	@Value("${app.code}")
	private String codigoAplicacion;

	@Autowired
	private GestionVersionesService gestionVersionesService;

	@Autowired
	private AppChooserDialog appChooserDialog;
	
	@Autowired
	private AppResumeDialog appResumeDialog;

	
	/**
	 * checkVersion
	 * @param urlCheckForUpdates urlCheckForUpdates
	 */
	@Override
	public final void checkVersion(final String urlCheckForUpdates) {
		if (StringUtils.hasLength(urlCheckForUpdates)) {
			gestionVersionesService.comprobarSiUltimaVersion(versionActual, codigoAplicacion, urlCheckForUpdates);
		} else {
			gestionVersionesService.comprobarSiUltimaVersion(versionActual, codigoAplicacion);	
		}			
	}

	
	@Override
	public final void launchAsyncUpload() {
		LOGGER.info("Abriendo dialog de eleccion de documento");
		appChooserDialog.setVisible(true);
	}


	@Override
	public final void launchResumeUploads() {
		LOGGER.info("Abriendo dialog de gestion de subidas pendientes");
		appResumeDialog.setMinimumSize(new Dimension(700, 10));
		appResumeDialog.setLocationRelativeTo(null);
		appResumeDialog.setVisible(true);
	}

}
