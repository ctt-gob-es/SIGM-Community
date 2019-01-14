package es.sigem.dipcoruna.desktop.editlauncher;

import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.editlauncher.events.model.ErrorGeneralEvent;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.LanzadoEditorEvent;
import es.sigem.dipcoruna.desktop.editlauncher.model.apps.ProcessWrapper;
import es.sigem.dipcoruna.desktop.editlauncher.service.LocalFileService;
import es.sigem.dipcoruna.desktop.editlauncher.service.apps.GeneralAppService;
import es.sigem.dipcoruna.desktop.editlauncher.service.monitor.FileMonitorService;
import es.sigem.dipcoruna.desktop.editlauncher.ui.AppChooserDialog;
import es.sigem.dipcoruna.desktop.editlauncher.ui.AppConfigurationDialog;
import es.sigem.dipcoruna.framework.service.util.PreferenciasHolder;
import es.sigem.dipcoruna.framework.service.versionado.GestionVersionesService;

@Service("editLauncherApp")
public class EditLauncherAppImpl implements EditLauncherApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	
	@Value("${app.version}")
	private String versionActual;
	
	@Value("${app.code}")
	private String codigoAplicacion;
	
	@Value("${param.urlDoc}")
	private String urlDoc;
		
	@Autowired
	private GestionVersionesService gestionVersionesService;
	
	
	@Autowired
	private AppChooserDialog appChooserDialog;
	
	@Autowired
	private AppConfigurationDialog appConfigurationDialog;
	
	@Autowired
	private PreferenciasHolder preferenciasHolder;
	
	@Autowired
	private GeneralAppService generalAppService;
	

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher; 
	
	@Autowired
	private FileMonitorService fileMonitorService;
	
	
	@Autowired
	private LocalFileService localFileService;
	

	@Override
	public void checkVersion(String urlCheckForUpdates) {
		if (StringUtils.hasLength(urlCheckForUpdates)) {
			gestionVersionesService.comprobarSiUltimaVersion(versionActual, codigoAplicacion, urlCheckForUpdates);
		}
		else {
			gestionVersionesService.comprobarSiUltimaVersion(versionActual, codigoAplicacion);	
		}			
	}

	
	@Override
	public void lanzarModoConfiguracion() {
		LOGGER.info("Invocando pantalla de configuración");
		Map<String, String> preferencias = preferenciasHolder.getPropertiesAsMap();
		appConfigurationDialog.cargarPropiedades(preferencias);
		appConfigurationDialog.setVisible(true);			
	}
	
	
	@Override
	public void lanzarEditorAsociado() {
		LOGGER.info("Lanzando editor asociado con el documento {}", urlDoc);
		
		String appPath = obtenerPathAplicacionEdicion(FilenameUtils.getExtension(urlDoc));
		
		String filePath = localFileService.descargarDocumento(urlDoc);							
		
		try {
			ProcessWrapper processWrapper = generalAppService.lanzarAplicacion(appPath, new String[] {filePath});
			fileMonitorService.startMonitor(filePath);	
			
			applicationEventPublisher.publishEvent(new LanzadoEditorEvent(this, processWrapper, filePath));
		}
		catch(Exception e) {
			applicationEventPublisher.publishEvent(ErrorGeneralEvent.buildErrorIrrecuperable(this, 
			        "appLauncherApplet.error.error.title", 
			        "appLauncherApplet.error.exception", new String[] {urlDoc}));
		}
	}

	

	private String obtenerPathAplicacionEdicion(final String extensionDocumento) {	
		String appPath = (String) preferenciasHolder.getProperty(extensionDocumento);
		
		if (!StringUtils.hasText(appPath) || !generalAppService.existeLaAplicacion(appPath)) {		
			appChooserDialog.setDefaultAppPath(generalAppService.getPathAplicacionAsociadaConArchivosDeExtension(extensionDocumento));
			appChooserDialog.setDocumentExtension(extensionDocumento);
			appChooserDialog.setVisible(true);
			
			appPath = appChooserDialog.getAppPath();
			
			if (appChooserDialog.getResult() == AppChooserDialog.OK && generalAppService.existeLaAplicacion(appPath)) {
				preferenciasHolder.putProperty(extensionDocumento, appPath);	
			}
			else {
				applicationEventPublisher.publishEvent(ErrorGeneralEvent.buildErrorIrrecuperable(this,
				        "appLauncherApplet.error.error.title",
				        "appLauncherApplet.error.invalidApp", 
				        new String[] {}));
				appPath = null;
			}					
		}
		return appPath;
	}
		
}
