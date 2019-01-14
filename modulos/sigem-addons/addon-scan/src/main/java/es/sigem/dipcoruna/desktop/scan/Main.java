package es.sigem.dipcoruna.desktop.scan;

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

import es.sigem.dipcoruna.desktop.scan.util.ApplicationParams;
import es.sigem.dipcoruna.framework.service.util.ParametersWrapper;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //arg: sigemscan:config=false&documentTypeId=93&urlUploadAction=http://localhost:8080/SIGEM_TramitacionWeb/uploadScanned.do&destinoUpload=TRAMITADOR&sessionId=BD92DD40093E24F26E633C6B13F84195&cookies=user=cf792212a8655f1911094f4c4b47;%20contextInfo=97%7C338%7C1938%7C14%7C1232%7C2616%7C674%7C2016%252FU022%252F000028%7C6%7C7%7C2616%7Ctrue%7Cfalse%7C0%7C0%7C0%7C0%7C0%7C0&cookiesPath=/SIGEM_TramitacionWeb&cookiesDomain=localhost&
    //     sigemscan:config=false&folderId=225956&urlUploadAction=http://localhost:8080/SIGEM_RegistroPresencialWeb/FileUploadScan&destinoUpload=REGISTRO&sessionId=DCB8C343C6D7A75EBD0F5CDA1DFC9DAB3&cookies=&cookiesPath=/SIGEM_RegistroPresencialWeb&cookiesDomain=localhost&
    public static void main(final String[] args) throws MalformedURLException {
        try {
            LOGGER.info("Invocada aplicación");
            validarArgumentos(args);

            final ApplicationParams appParams = ApplicationParams.build(new ParametersWrapper(getParametros(args)));
            LOGGER.debug("Generados parámetros: {}", appParams);

            validarParametros(appParams);
            LOGGER.debug("Parámetros validados correctamente");

            establecerIdiomaDefectoIfNecesary(appParams.getLang());
            establecerSystemProperties(appParams);

            @SuppressWarnings("resource")
            final ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
            LOGGER.debug("Contexto de spring iniciado correctamente");

            final ScanApp scanApp = appContext.getBean(ScanApp.class);
            scanApp.checkVersion(appParams.getUrlCheckForUpdates());

            if (appParams.esModoConfiguracion()) {
                scanApp.lanzarModoConfiguracion();
            }
            else if (scanApp.isDescargarMasTarde()){            	
                scanApp.lanzarOpcionEscaneo();
            }

        }
        catch (final Exception e) {
            LOGGER.error("Se ha producido un error", e);
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        finally {
            System.exit(0);
        }
    }

	private static void validarArgumentos(final String[] args) {
        if (args.length != 0 && args.length != 1) {
            LOGGER.error("Se esperaban 0 ó 1 argumentos");
            throw new IllegalArgumentException("Se esperaban 0 ó 1 argumentos");
        }
        LOGGER.debug("Argumentos recibido: {}", Arrays.toString(args));
    }


    private static String getParametros(final String[] args) {
        if (args.length == 0) {
            return Strings.EMPTY;
        }
        else {
            return args[0];
        }
    }

    private static void validarParametros(final ApplicationParams appParams) {
        if (!appParams.esModoConfiguracion()) {
            if (!StringUtils.hasText(appParams.getSessionId())) {
                LOGGER.error("Es obligatorio incluir el id de sesión");
                throw new IllegalArgumentException("Es obligatorio incluir el id de sesión");
            }

            if (!StringUtils.hasText(appParams.getUrlUploadAction())) {
                LOGGER.error("Es obligatorio incluir la URL a la que enviar los documentos escaneados");
                throw new IllegalArgumentException("Es obligatorio incluir la URL a la que enviar los documentos escaneados");
            }

            if (!StringUtils.hasText(appParams.getDestinoUpload())) {
                LOGGER.error("Es obligatorio incluir el tipo de aplicación a la que enviar los documentos escaneados");
                throw new IllegalArgumentException("Es obligatorio incluir el tipo de aplicación a la que enviar los documentos escaneados");
            }

            if (!StringUtils.hasText(appParams.getSessionId())) {
                LOGGER.error("Es obligatorio incluir el id de sesión");
                throw new IllegalArgumentException("Es obligatorio incluir el id de sesión");
            }

        }
    }

    private static void establecerIdiomaDefectoIfNecesary(final String idioma) {
        if (!StringUtils.hasText(idioma)) {
            return;
        }
        Locale.setDefault(new Locale(idioma));
        JComponent.setDefaultLocale(new Locale(idioma));
    }

    /**
     * Setea propiedades del sistema para que luego se carguen junto con el contexto de spring
     * @param appParams
     */
    private static void establecerSystemProperties(final ApplicationParams appParams) {
    	System.setProperty("param.urlCheckForUpdates", appParams.getUrlCheckForUpdates() != null ? appParams.getUrlCheckForUpdates() : "");
    	System.setProperty("param.urlUploadAction", appParams.getUrlUploadAction() != null ? appParams.getUrlUploadAction() : "");
    	System.setProperty("param.destinoUpload", appParams.getDestinoUpload() != null ? appParams.getDestinoUpload() : "");
    	System.setProperty("param.cookies", appParams.getCookies() != null ? appParams.getCookies() : "");
    	System.setProperty("param.cookiesPath", appParams.getCookiesPath() != null ? appParams.getCookiesPath() : "");
    	System.setProperty("param.cookiesDomain", appParams.getCookiesDomain() != null ? appParams.getCookiesDomain() : "");
    	System.setProperty("param.sessionId", appParams.getSessionId() != null ? appParams.getSessionId() : "");
    	System.setProperty("param.uploadToken", appParams.getUploadToken() != null ? appParams.getUploadToken() : "");
    	System.setProperty("param.maxUploadFiles", appParams.getMaxUploadFiles() != null ? appParams.getMaxUploadFiles() : "");
    	
    	
    	System.setProperty("param.tramitador.documentTypeId", appParams.getTramitadorDocumentId() != null ? appParams.getTramitadorDocumentId() : "");
    	System.setProperty("param.registro.folderId", appParams.getRegistroFolderId() != null ? appParams.getRegistroFolderId() : "");
    	System.setProperty("param.registro.sessionPId", appParams.getRegistroSessionPId() != null ? appParams.getRegistroSessionPId() : "");
    	System.setProperty("param.registro.bookId", appParams.getBookId() != null ? appParams.getBookId() : "");
    	System.setProperty("param.registro.entidadId", appParams.getEntidadId() != null ? appParams.getEntidadId() : "");
    	System.setProperty("param.registro.nombreCarpeta", appParams.getNombreCarpeta() != null ? appParams.getNombreCarpeta() : "");

	}
}
