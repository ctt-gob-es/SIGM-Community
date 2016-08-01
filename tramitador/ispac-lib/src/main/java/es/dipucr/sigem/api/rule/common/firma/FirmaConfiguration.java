package es.dipucr.sigem.api.rule.common.firma;

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
public class FirmaConfiguration extends DipucrPropertiesConfiguration {

	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, FirmaConfiguration> instancesHash = 
			new HashMap<String, FirmaConfiguration>(); //[dipucr-Felipe 3#260]

	public static final String DEFAULT_CONFIG_FILENAME = "firma.properties";

	public interface LIBRO_DECRETOS{
		public static String VER_TOOLBAR = "librodecretos.verToolbar";
		public static String LIMITAR_PERMISOS = "librodecretos.limitarPermisos";
		public static String MOSTRAR_FIRMADO = "librodecretos.mostrarFirmado";
		public static String PASSWORD = "librodecretos.password";
	}
	
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
}
