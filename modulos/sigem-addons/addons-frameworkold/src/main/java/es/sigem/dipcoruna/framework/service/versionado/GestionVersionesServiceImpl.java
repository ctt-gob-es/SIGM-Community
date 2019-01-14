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
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private VersionInfoJDialog versionInfoJFrame;
		
	@Value("${general.urlBaseDescargas:}")
	private String urlBaseDescargas; 
	
	
	@Override
	public void comprobarSiUltimaVersion(String version, String codigoAplicacion) {
		comprobarSiUltimaVersion(version, codigoAplicacion, urlBaseDescargas);
	}
	
	
	@Override
	public void comprobarSiUltimaVersion(String version, String codigoAplicacion, String urlBaseDescargas) {
		if (!StringUtils.hasText(urlBaseDescargas)){
			LOGGER.warn("No se comprueba si hay una nueva versión disponible porque no se ha indicado URL de descargas");
			return;
		}
		
		
		Aplicacion aplicacion = obtenerInformacionAplicacion(codigoAplicacion, urlBaseDescargas);		
		if (aplicacion != null && esUnaVersionAnterior(version, aplicacion.getVersion())) {			
			versionInfoJFrame.setTexts(version, aplicacion.getVersion(), aplicacion.getUrlDescarga());
			versionInfoJFrame.setVisible(true);
		}			
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
