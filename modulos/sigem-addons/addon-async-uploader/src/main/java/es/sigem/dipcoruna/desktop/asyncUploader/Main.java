package es.sigem.dipcoruna.desktop.asyncUploader;

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

import es.sigem.dipcoruna.desktop.asyncUploader.util.ApplicationParams;
import es.sigem.dipcoruna.framework.service.util.ParametersWrapper;

/**
 * Clase principal de entrada del addon
 */
public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Main
	 * @param args Argumentos
	 * @throws MalformedURLException Error
	 */
	public static void main(final String[] args) throws MalformedURLException {
		
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
						
			AsyncUploaderApp asyncUploaderApp = appContext.getBean(AsyncUploaderApp.class);
			asyncUploaderApp.checkVersion(appParams.getUrlCheckForUpdates());
			if (StringUtils.isEmpty(appParams.getParamUser())) {
				asyncUploaderApp.launchResumeUploads();
			} else {
				asyncUploaderApp.launchAsyncUpload();
			}
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error", e);
			JOptionPane.showMessageDialog(new JFrame(),	e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);			
		} finally {
			System.exit(0);
		}
		
	}


	/**
	 * Validar argumentos
	 * @param args argumentos
	 */
	private static void validarArgumentos(final String[] args) {
		if (args.length != 0 && args.length != 1) {
			LOGGER.error("Se esperaban 0 ó 1 argumentos");
			throw new IllegalArgumentException("Se esperaban 0 ó 1 argumentos");
		}
		LOGGER.debug("Argumentos recibido: {}", Arrays.toString(args));
	}


	/**
	 * Devuelve los parametros
	 * @param args argumentos
	 * @return Primer elementos del array de argumentos
	 */
	private static String getParametros(final String[] args) {
		if (args.length == 0) {
			return Strings.EMPTY;
		} else {
			return args[0];
		}
	}


	/**
	 * Valida los parametros que llegan al addon
	 * @param appParams Parametros
	 */
	private static void validarParametros(final ApplicationParams appParams) {
		boolean error = false;
		StringBuilder sb = new StringBuilder();
		// Solamente se validan los parametros si viene el usuario. Sin usuario determina que no es subida directa.
		if (StringUtils.hasText(appParams.getParamUser())) {
			if (!StringUtils.hasText(appParams.getParamTipoDocumento())) {
				LOGGER.error("Es obligatorio incluir el tipo de documento que se quiere adjuntar.");
				sb.append("Es obligatorio incluir el tipo de documento que se quiere adjuntar.");
				error = true;
			}
			if (!StringUtils.hasText(appParams.getTramite())) {
				LOGGER.error("Es obligatorio incluir el identificador del trámite al que adjuntar el documento.");
				sb.append("Es obligatorio incluir el identificador del trámite al que adjuntar el documento.");
				error = true;
			}
			if (!StringUtils.hasText(appParams.getFase())) {
				LOGGER.error("Es obligatorio incluir el valor del indicador de fase.");
				sb.append("Es obligatorio incluir el valor del indicador de fase.");
				error = true;
			}
			if (!StringUtils.hasText(appParams.getIdDestino())) {
				LOGGER.error("Es obligatorio incluir el valor del identificador de destino.");
				sb.append("Es obligatorio incluir el valor del identificador de destino.");
				error = true;
			}
			if (!StringUtils.hasText(appParams.getTipoDestino())) {
				LOGGER.error("Es obligatorio incluir el valor del tipo de destino.");
				sb.append("Es obligatorio incluir el valor del tipo de destino.");
				error = true;
			}
			if (!StringUtils.hasText(appParams.getParamEntidad())) {
				LOGGER.error("Es obligatorio incluir el valor del directorio de la entidad.");
				sb.append("Es obligatorio incluir el valor del directorio de la entidad.");
				error = true;
			}
		}
		if (error) {
			throw new IllegalArgumentException("Error: " + sb.toString());
		}
	}

	
	/**
	 * Establecer idioma
	 * @param idioma Idioma
	 */
	private static void establecerIdiomaDefectoIfNecesary(final String idioma) {
		if (!StringUtils.hasText(idioma)) {
			return;
		}
		Locale.setDefault(new Locale(idioma));			
		JComponent.setDefaultLocale(new Locale(idioma)); //Siempre se pone castellano por defecto	
	}

	
	/**
     * Setea propiedades del sistema para que luego se carguen junto con el contexto de spring
     * @param appParams Parametros
     */
    private static void establecerSystemProperties(final ApplicationParams appParams) {
        System.setProperty("param.urlCheckForUpdates", appParams.getUrlCheckForUpdates() != null ? appParams.getUrlCheckForUpdates() : "");
        System.setProperty("param.paramLang", appParams.getLang() != null ? appParams.getLang() : "");
        System.setProperty("param.tpdoc", appParams.getParamTipoDocumento() != null ? appParams.getParamTipoDocumento() : "");
        System.setProperty("param.user", appParams.getUser() != null ? appParams.getUser() : "");
        System.setProperty("param.tramite", appParams.getTramite() != null ? appParams.getTramite() : "");
        System.setProperty("param.entidad", appParams.getParamEntidad() != null ? appParams.getParamEntidad() : "");
        System.setProperty("param.fase", appParams.getFase() != null ? appParams.getFase() : "");
        System.setProperty("param.tipodestino", appParams.getTipoDestino() != null ? appParams.getTipoDestino() : "");
        System.setProperty("param.iddestino", appParams.getIdDestino() != null ? appParams.getIdDestino() : "");
    }
    
}
