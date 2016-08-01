package es.dipucr.sigem.api.rule.common.decretos;

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
@SuppressWarnings("serial")
public class DecretosConfiguration extends DipucrPropertiesConfiguration {
	
	private static HashMap<String, DecretosConfiguration> instancesHash = 
			new HashMap<String, DecretosConfiguration>(); //[dipucr-Felipe 3#260]

	public static final String DEFAULT_CONFIG_FILENAME = "decretos.properties";
	
	public interface LIBRO_DECRETOS{
		public static String VER_TOOLBAR = "librodecretos.verToolbar";
		public static String LIMITAR_PERMISOS = "librodecretos.limitarPermisos";
		public static String MOSTRAR_FIRMADO = "librodecretos.mostrarFirmado";
		public static String PASSWORD = "librodecretos.password";
	}
	
	/**
	 * Constructor.
	 */
	protected DecretosConfiguration() {
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
	public static synchronized DecretosConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			DecretosConfiguration decretosConfig = new DecretosConfiguration();
			if(entidad!=null){
				decretosConfig.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
			}
			else{
				decretosConfig.createInstance("", DEFAULT_CONFIG_FILENAME);
			}
			instancesHash.put(entidadHash, decretosConfig);
		}
		return instancesHash.get(entidadHash);
	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized DecretosConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
