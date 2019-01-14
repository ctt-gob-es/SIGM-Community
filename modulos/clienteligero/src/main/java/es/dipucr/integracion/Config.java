package es.dipucr.integracion;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.dipucr.integracion.general.Util;



/**
 * Clase donde se almacenan las propiedades globales que pueden ser usadas en cualquier punto de la aplicación.
 */
public class Config {
	
	private static final Log log = LogFactory.getLog(Config.class);
	
	public static final String PAQUETE_SERVICIOS 						= "com.mptap.dpae.integracion.servicios";
	public static final String PAQUETE_INTERFACE_SERVICIOS_SINCRONOS	= "com.mptap.dpae.integracion.interfaces.ServicioSincrono";
	public static final String PAQUETE_INTERFACE_SERVICIOS_ASINCRONOS	= "com.mptap.dpae.integracion.interfaces.ServicioAsincrono";
	
	
	private static boolean graphicsenv = false;
	
	private static boolean bdSCSPLibrariesConnected = false;	//Indica si existe conexión con la BD de las librerías.
	 
	public static final String PATH_CFG 		= "cfg";	//Directorio con ficheros de configuración para datos de entrada.
	public static final String PATH_LOG			= "logs";	//Almacena ficheros de logs.
	public static final String PATH_FILES		= "files";	//Almacena las peticiones/respuestas de las invocaciones.
	public static final String PATH_EXPORT		= "export";	//Almacena los pdf generados por los servicios.
	public static final String PATH_TEMP		= "temp";	//Almacena documentos temporales para luego transformarlos a pdf.
	
	private static Calendar calendar = null;				//Fecha actual
	private static Locale locale = null;
	private static String userDir = null;					//Directorio donde se ejecuta la aplicación.
	private static ResourceBundle resource = null;			//Fichero de propiedades para los textos de la aplicación.
	private static String filesDir = null;					//Ruta donde se almacenan las peticiones/respuestas.
	
	private static Properties sslProperties = null;			//Fichero con la configuración para la autenticación de cliente.
	private static String sslPropertiesPath = null;

	private static Properties coatingProperties = null;
	private static String coatingPropertiesPath = null;
	
	
	public static final int ENGLISH_LANGUAGE = 0;
	public static final int SPANISH_LANGUAGE = 1;
	
	
	public static int WIDTH_SCREEN;
	public static int HEIGHT_SCREEN;

	
	
	public static final int NORTH_PANEL			 = 1;
	public static final int SOUTH_PANEL			 = 2;
	public static final int EAST_PANEL			 = 3;
	public static final int WEST_PANEL			 = 4;
	public static final int CENTER_PANEL 		 = 5;
	
	public static final String STR_NORTH_PANEL	 = "NORTH_PANEL";
	public static final String STR_SOUTH_PANEL	 = "SOUTH_PANEL";
	public static final String STR_CENTER_PANEL	 = "CENTER_PANEL";

	public static final String INVISIBLE		 = "INVISIBLE";			//for checkBox

	
	public static int MODE 												= -1;
	public static final int MODE_EXAMPLE_ORACULO_SERVICES				= 111;
	public static final int MODE_EXAMPLE_SCSP_SERVICE					= 112;
	public static final int MODE_EXAMPLE_SCSP_SERVICE_BATCH_VDISFWS01	= 113;
	public static final int MODE_EXAMPLE_SCSP_COATING					= 114;
	public static final int MODE_EXAMPLE_SCSP_COATING_BATCH_AEAT103I 	= 115;
	public static final int MODE_EXAMPLE_SCSP_COATING_BATCH_VDISFWS01	= 116;
	public static final int MODE_INVOKE_SERVICES_SCSP 					= 117;
	public static final int MODE_ISSUED_SERVICES_SCSP 					= 118;
	public static final int MODE_INVOKE_SERVICES_COATING				= 119;
	public static final int MODE_ISSUED_SERVICES_COATING				= 120;
	public static final int MODE_INE_SYNC_FORM							= 121;
	public static final int MODE_INE_SYNC_FILE							= 122;
	

	
	public static final String SHORT_KEY_SHOW_INVOKE_SERVICES 			= "INVOKE_SERVICES";
	public static final String SHORT_KEY_SHOW_ISSUED_SERVICES			= "ISSUED_SERVICES";
	public static final String SHORT_KEY_SHOW_INVOKE_SERVICES_COATING	= "INVOKE_SERVICES_COATING";
	public static final String SHORT_KEY_SHOW_ISSUED_SERVICES_COATING	= "ISSUED_SERVICES_COATING";
	
	
	
	public static final String ESTADO_PENDIENTE	 = "0001";
	public static final String ESTADO_EN_PROCESO = "0002";
	public static final String ESTADO_TRAMITADA	 = "0003";


	
	public static enum ACCION_INE {ADD, ALTER, DELETE};

	

	
	
	public Config(boolean graphicsenv) throws Exception {
		
		Config.graphicsenv = graphicsenv;
		
		
		if (graphicsenv) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			WIDTH_SCREEN = (int) screenSize.getWidth();
			HEIGHT_SCREEN = (int) screenSize.getHeight();
			log.info("# SCREEN DIMENSIONS: "+WIDTH_SCREEN+" x "+HEIGHT_SCREEN );
			}
		
		Config.userDir = System.getProperty("user.dir");
		Config.locale = new Locale("es");					//TODO: hacer que sea seleccionable.
		
		
		/*
		Config.calendar = Calendar.getInstance();

		log.info("=============================================================");
		log.info("# # # # # # # # # # # #  DATOS CLIENTE # # # # # # # # # # # ");
		log.info("=============================================================");
		log.info("# SCREEN DIMENSIONS: "+WIDTH_SCREEN+" x "+HEIGHT_SCREEN );
		log.info("# PATH: "+Config.userDir+" with Locale: "+locale.getLanguage());
		log.info("# JAVA HOME: "+System.getProperty("java.home"));
		log.info("# JAVA CLASS PATH: "+System.getProperty("java.class.path"));
		log.info("# JAVA RUNTIME VERSION: "+System.getProperty("java.runtime.version"));
		log.info("# JAVA VM VERSION: "+System.getProperty("java.vm.version"));
		log.info("# OPERATIVE SYSTEM: "+System.getProperty("os.name"));
		log.info("# FILE ENCODING: "+System.getProperty("file.encoding"));
		log.info("# DATE (yyyy/MM/dd): "+Config.calendar.get(Calendar.YEAR)+"/"+(Config.calendar.get(Calendar.MONTH)+1)+"/"+Config.calendar.get(Calendar.DAY_OF_MONTH));
		log.info("=============================================================");
		log.info("#                                                           #");
		log.info("=============================================================");
		
	*/	

      	Config.resource = ResourceBundle.getBundle("project",locale);
      	
      	//Leemos las propiedades de Autenticación de cliente guardadas en un fichero .properties.
      	Config.sslPropertiesPath = Config.userDir+File.separator+"cfg"+File.separator+"ssl-client-authentication.properties";
      	Config.sslProperties = Util.readFileProperties(Config.sslPropertiesPath);
      	Util.setSslConfiguracion(Config.sslProperties);
      	
      	//Leemos las propiedades del Recubrimiento, como la url.
      	Config.coatingPropertiesPath = Config.userDir+File.separator+"cfg"+File.separator+"coating-configuration.properties";
      	Config.coatingProperties = Util.readFileProperties(Config.coatingPropertiesPath);

	}
	


	
	public Config(boolean graphicsenv, boolean bdSCSPLibrariesConnected) throws Exception {
		this(graphicsenv);
		Config.bdSCSPLibrariesConnected = true;
		
	}


	
	/**
	 * @param mode Identifica el panel que se quiere usar.
	 * @throws Exception
	 * Cuando se arranca el entorno gráfico se puede especificar el panel que se muestra por defecto. 
	 * Así que aquí solo deberían encontrarse aquellos paneles posibles que pueden mostrarse por defecto.
	 * Por eso no están declarados todos. 
	 */
	static protected void setConfig(int mode) throws Exception {
		switch(mode) {
			case Config.MODE_EXAMPLE_SCSP_SERVICE:
				 Config.MODE = Config.MODE_EXAMPLE_SCSP_SERVICE;
				 break;
			case Config.MODE_EXAMPLE_SCSP_COATING:
				 Config.MODE = Config.MODE_EXAMPLE_SCSP_COATING;
				 break;
			case Config.MODE_INVOKE_SERVICES_SCSP:
				 Config.MODE = Config.MODE_INVOKE_SERVICES_SCSP;
				 break;
			case Config.MODE_ISSUED_SERVICES_SCSP:
				 Config.MODE = Config.MODE_ISSUED_SERVICES_SCSP;
				 break;
			case Config.MODE_INVOKE_SERVICES_COATING:
				 Config.MODE = Config.MODE_INVOKE_SERVICES_COATING;
				 break;
			case Config.MODE_ISSUED_SERVICES_COATING:
				 Config.MODE = Config.MODE_ISSUED_SERVICES_COATING;
				 break;
	
			case Config.MODE_INE_SYNC_FORM:
				Config.MODE = Config.MODE_INE_SYNC_FORM;
				break;
			case Config.MODE_INE_SYNC_FILE:
				Config.MODE = Config.MODE_INE_SYNC_FILE;
				break;
			default: break;
			}
	}
	

	

	public static Locale getLocale() {
		return locale;
	}

	public static String getUserDir() {
		return userDir;
	}

	public static ResourceBundle getResource() {
		return resource;
	}

	public static String getFilesDir() {
		return filesDir;
	}

	public static Properties getSslProperties() {
		return sslProperties;
	}

	public static void setSslProperties(Properties sslProperties) {
		Config.sslProperties = sslProperties;
	}

	public static String getSslPropertiesPath() {
		return sslPropertiesPath;
	}

	public static Properties getCoatingProperties() {
		return coatingProperties;
	}

	public static void setCoatingProperties(Properties coatingProperties) {
		Config.coatingProperties = coatingProperties;
	}

	public static String getCoatingPropertiesPath() {
		return coatingPropertiesPath;
	}






	public static boolean isGraphicsenv() {
		return graphicsenv;
	}






	public static boolean isBdSCSPLibrariesConnected() {
		return bdSCSPLibrariesConnected;
	}


	
	
	
	
	
}
