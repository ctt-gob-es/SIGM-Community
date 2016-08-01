package es.dipucr.sigem.api.rule.common.comparece;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;

import es.dipucr.sigem.api.rule.common.DipucrPropertiesConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

/**
 * Clase que gestiona la configuración de ISPAC.
 *
 */
public class CompareceConfiguration extends DipucrPropertiesConfiguration {
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, CompareceConfiguration> instancesHash = 
			new HashMap<String, CompareceConfiguration>(); //[dipucr-Felipe 3#260]
	
	public static final String DEFAULT_CONFIG_FILENAME = "comparece.properties";
	
	/**
	 * Indica si la entidad utiliza COMPARECE
	 */
	public static final String TIENE_COMPARECE = "tieneComparece";
	
	/**
	 * Ruta por defecto de la imagen del logo dipu
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
	public static final String IMAGE_LOGO_PATH_DIPUCR = "imagen_cabecera";
	
	/**
	 * Ruta por defecto de la imagen del logo dipu
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
	public static final String IMAGE_FONDO_PATH_DIPUCR = "imagen_fondo";
	
	/**
	 * Ruta por defecto de la imagen del logo dipu
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
	public static final String IMAGE_PIE_PATH_DIPUCR = "imagen_pie";

	/**
	 * Ruta por defecto de la imagen de fondo del PDF.
	 */
	public static final String DEFAULT_PDF_BG_IMAGE_PATH = "imagen_fondo";
	
	
	/**
	 * Constructor.
	 */
	protected CompareceConfiguration() {
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
	public static synchronized CompareceConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			CompareceConfiguration comparece = new CompareceConfiguration();
			if(entidad!=null){
				comparece.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
			}
			else{
				comparece.createInstance("", DEFAULT_CONFIG_FILENAME);
			}
			instancesHash.put(entidadHash, comparece);
		}
		return instancesHash.get(entidadHash);

	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized CompareceConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
	
	/**
	 * Acceso no singleton
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized CompareceConfiguration getInstanceNoSingleton(String entidad) throws ISPACRuleException{
		
		CompareceConfiguration compareceConfig = new CompareceConfiguration();
		compareceConfig.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
		return compareceConfig;
	}
	
	/**
	 * Acceso no singleton
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized CompareceConfiguration getInstanceNoSingleton(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstanceNoSingleton(entidad);
	}
}
