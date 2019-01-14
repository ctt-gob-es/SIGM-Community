package es.sigem.dipcoruna.desktop.editlauncher;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.editlauncher.util.ApplicationParams;
import es.sigem.dipcoruna.framework.service.util.ParametersWrapper;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	
	public static void main(String[] args) throws MalformedURLException {
		try {
			LOGGER.info("Invocada aplicación");		
			validarArgumentos(args);					
							
			ApplicationParams appParams = ApplicationParams.build(new ParametersWrapper(getParametros(args)));
			LOGGER.debug("Generados parámetros: {}", appParams);
			
			validarParametros(appParams);
			LOGGER.debug("Parámetros validados correctamente");	
			
			establecerIdiomaDefectoIfNecesary(appParams.getLang());
			establecerSystemProperties(appParams);
			
			@SuppressWarnings("resource")
			ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");					
			LOGGER.debug("Contexto de spring iniciado correctamente");			
			
						
			EditLauncherApp editLauncherApp = appContext.getBean(EditLauncherApp.class);
			editLauncherApp.checkVersion(appParams.getUrlCheckForUpdates());
			
			if (appParams.esModoConfiguracion()) {
				editLauncherApp.lanzarModoConfiguracion();
			}
			else {
				editLauncherApp.lanzarEditorAsociado();
			}	
			
			editLauncherApp.lanzarEditorAsociado();
		}
		catch(Exception e) {
			LOGGER.error("Se ha producido un error", e);
			JOptionPane.showMessageDialog(new JFrame(),	e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);			
		}
		finally {
			System.exit(0);
		}
	}
			

	private static void validarArgumentos(String[] args) {		
		if (args.length != 0 && args.length != 1) {
			LOGGER.error("Se esperaban 0 ó 1 argumentos");
			throw new IllegalArgumentException("Se esperaban 0 ó 1 argumentos");
		}
		LOGGER.debug("Argumentos recibido: {}", Arrays.toString(args));
	}

	private static String getParametros(String[] args) {
		if (args.length == 0) {
			return Strings.EMPTY;
		}
		else {
			return args[0];
		}
	}
	
	private static void validarParametros(ApplicationParams appParams) {	
		if (!appParams.esModoConfiguracion()) {
			if (!StringUtils.hasText(appParams.getUrlDoc())) {
				LOGGER.error("Es obligatorio incluir la URL del documento a editar");
				throw new IllegalArgumentException("URL de documento no encontrada");
			}
		}		
	}
	
	private static void establecerIdiomaDefectoIfNecesary(String idioma) {
		if (!StringUtils.hasText(idioma)) {
			return;
		}
		Locale.setDefault(new Locale(idioma));			
		JComponent.setDefaultLocale(new Locale(idioma)); //Siempre se pone castellano por defecto	
	}
	
	/**
     * Setea propiedades del sistema para que luego se carguen junto con el contexto de spring
     * @param appParams
     */
    private static void establecerSystemProperties(final ApplicationParams appParams) {
        System.setProperty("param.urlCheckForUpdates", appParams.getUrlCheckForUpdates() != null ? appParams.getUrlCheckForUpdates() : "");
        System.setProperty("param.urlDoc", appParams.getUrlDoc() != null ? appParams.getUrlDoc() : "");        
        System.setProperty("param.paramLang", appParams.getLang() != null ? appParams.getLang() : "");    
    }
}
