package es.dipucr.sigem.api.rule.common.firma;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrPropertiesConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

/**
 * Clase que gestiona la configuración de ISPAC.
 * 
 */
public class FirmaConfiguration extends DipucrPropertiesConfiguration {
	
	/** Logger de la clase **/
	protected static final Logger LOGGER = Logger.getLogger(FirmaConfiguration.class);

	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, FirmaConfiguration> instancesHash = new HashMap<String, FirmaConfiguration>(); //[dipucr-Felipe 3#260]

	public static final String DEFAULT_CONFIG_FILENAME = "firma.properties";

	public interface LIBRO_DECRETOS{
		public static String VER_TOOLBAR = "librodecretos.verToolbar";
		public static String LIMITAR_PERMISOS = "librodecretos.limitarPermisos";
		public static String MOSTRAR_FIRMADO = "librodecretos.mostrarFirmado";
		public static String PASSWORD = "librodecretos.password";
	}
	
	//[dipucr-Felipe #1246]
	public interface GRAYBAND{
		public static String TEXT1 = "firmar.grayband.text1";
		public static String TEXT2 = "firmar.grayband.text2";
		public static String FIRMANTE = "firmar.grayband.firmante";
		public static String PAGINAS = "firmar.grayband.paginas";
	}
	
	/* ============================================================================
	 * Configuración del conector de gestión de firmas. Portafimas
	 */
	public final static String PROCESS_SIGN_CONNECTOR_CLASS="PROCESS_SIGN_CONNECTOR_CLASS";
	
	/* ============================================================================
	 * Configuración del conector de gestión de firmas. Portafimas
	 */
	public final static String PROCESS_SIGN_CONNECTOR_QUERY_URL="PROCESS_SIGN_CONNECTOR_QUERY_URL";
	public final static String PROCESS_SIGN_CONNECTOR_MODIFY_URL="PROCESS_SIGN_CONNECTOR_MODIFY_URL";
	public final static String PROCESS_SIGN_CONNECTOR_ADMIN_URL="PROCESS_SIGN_CONNECTOR_ADMIN_URL";
	public final static String PROCESS_SIGN_CONNECTOR_USER="PROCESS_SIGN_CONNECTOR_USER";
	public final static String PROCESS_SIGN_CONNECTOR_PASSWORD="PROCESS_SIGN_CONNECTOR_PASSWORD";
	public final static String PROCESS_SIGN_CONNECTOR_APPLICATION="PROCESS_SIGN_CONNECTOR_APPLICATION";
	public final static String PROCESS_SIGN_CONNECTOR_DOCTYPE="PROCESS_SIGN_CONNECTOR_DOCTYPE";

	
	/* =========================================================================
	 * Gestión de firmas digitales
	 * ====================================================================== */
	public final static String DIGITAL_SIGN_CONNECTOR_CLASS = "DIGITAL_SIGN_CONNECTOR_CLASS";

	/**
	 * Constructor.
	 */
	protected FirmaConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 * 
	 * @since 06.04.2016 [dipucr-Felipe 3#260] Reescribo por completo el método usando el hash de objetos 
	 */
	public static synchronized FirmaConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			FirmaConfiguration firmaConfig = new FirmaConfiguration();
			if(entidad!=null){
				firmaConfig.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
			}
			else{
				firmaConfig.createInstance("", DEFAULT_CONFIG_FILENAME);
			}
			instancesHash.put(entidadHash, firmaConfig);
		}
		return instancesHash.get(entidadHash);
	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized FirmaConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
	
	/**
	 * Acceso no singleton
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 * @since 06.04.2016 [dipucr-Felipe 3#260] Reescribo el método sin usar mInstance
	 */
	public static synchronized FirmaConfiguration getInstanceNoSingleton(String entidad) throws ISPACRuleException{
		
		FirmaConfiguration firmaConfig = new FirmaConfiguration();
		firmaConfig.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
		return firmaConfig;
	}
	
	/**
	 * Sobre escribimos el método, pues se llamará desde otros proyectos que no sean la tramitación web
	 */
	@Override
	protected void initiate(String configFileName) throws ISPACException {
		
		try {

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Cargando fichero de configuración: " + configFileName);
			}
	
			// Cargar la información del fichero
//			InputStream in = ConfigurationHelper.getConfigFileInputStream(configFileName);
			String fullPath = SigemConfigFilePathResolver.getInstance().resolveFullPath(configFileName, "/SIGEM_Tramitacion");
			FileInputStream in = new FileInputStream(new File(fullPath));
			load(in);
			in.close();
			
		} catch (Exception e) {
			LOGGER.info("Error al inicializar el fichero de configuración: " + configFileName, e);
			throw new ISPACException(e);
		}
	}
}
