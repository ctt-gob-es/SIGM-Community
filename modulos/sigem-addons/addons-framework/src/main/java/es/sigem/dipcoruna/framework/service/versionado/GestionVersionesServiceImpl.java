package es.sigem.dipcoruna.framework.service.versionado;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.sigem.dipcoruna.framework.service.util.VersionUtil;
import es.sigem.dipcoruna.framework.service.versionado.dto.Aplicacion;
import es.sigem.dipcoruna.framework.ui.versionado.VersionInfoJDialog;


@Service("gestionVersionesService")
public class GestionVersionesServiceImpl implements GestionVersionesService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GestionVersionesServiceImpl.class);
	
	private static final String INSTALADOR_NAME = "Setup-addon-scan-";
	private static final String INSTALADOR_EXTENSION = ".exe";
	
	private boolean descargarMasTarde = true;
	
	public boolean isDescargarMasTarde() {
		return descargarMasTarde;
	}
	public void setDescargarMasTarde(boolean descargarMasTarde) {
		this.descargarMasTarde = descargarMasTarde;
	}

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private VersionInfoJDialog versionInfoJFrame;
		
	@Value("${general.urlBaseDescargas:}")
	private String urlBaseDescargas; 
	
	
	@Override
	public boolean comprobarSiUltimaVersion(String version, String codigoAplicacion) {
		return comprobarSiUltimaVersion(version, codigoAplicacion, urlBaseDescargas);
	}
	
	
	@Override
	public boolean comprobarSiUltimaVersion(String version, String codigoAplicacion, String urlBaseDescargas) {
		if (!StringUtils.hasText(urlBaseDescargas)){
			LOGGER.warn("No se comprueba si hay una nueva versión disponible porque no se ha indicado URL de descargas");
			return true;
		}
		
		
		Aplicacion aplicacion = obtenerInformacionAplicacion(codigoAplicacion, urlBaseDescargas);		
		if (aplicacion != null && esUnaVersionAnterior(version, aplicacion.getVersion())) {			
			String dirNuevaVersion = urlBaseDescargas + "/" + INSTALADOR_NAME + aplicacion.getVersion() + INSTALADOR_EXTENSION;
			
			String whatsNew = "";
			if(null != aplicacion.getWhatsNew()){
				whatsNew = aplicacion.getWhatsNew();
			}
			
			versionInfoJFrame.setTexts(version, aplicacion.getVersion(), dirNuevaVersion, whatsNew);
			versionInfoJFrame.setVisible(true);
			
			descargarMasTarde = versionInfoJFrame.isDescargarMasTarde();
		}
		
		return descargarMasTarde;
	}	
	
	private Aplicacion obtenerInformacionAplicacion(String codigoAplicacion, String urlBaseDescargas) {
		if (!urlBaseDescargas.trim().endsWith("/")) {
			urlBaseDescargas = urlBaseDescargas.trim() + "/";
		}
		
		URI targetUrl = UriComponentsBuilder.fromUriString(urlBaseDescargas).path(codigoAplicacion + ".xml").build().toUri();
		try {			
			return restTemplate.getForObject(targetUrl, Aplicacion.class);
		}
		catch(Exception e) {
			LOGGER.warn("No se ha podido comprobar si la aplicación tiene la última versión disponible en la URL '{}'", targetUrl, e);
			return null;
		}
	}


	private boolean esUnaVersionAnterior(String version, String version2) {		
		return VersionUtil.compareVersiones(version, version2) < 0;
	}	
}
