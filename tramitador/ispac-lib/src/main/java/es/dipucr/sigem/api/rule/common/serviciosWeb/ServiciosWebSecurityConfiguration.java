package es.dipucr.sigem.api.rule.common.serviciosWeb;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrPropertiesConfiguration;

/**
 * [dipucr-Felipe #304]
 * Clase que gestiona la configuración de seguridad de los servicios web
 */
public class ServiciosWebSecurityConfiguration extends DipucrPropertiesConfiguration {
	
	/** Logger de la clase **/
	protected static final Logger logger = Logger.getLogger(ServiciosWebSecurityConfiguration.class);
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, ServiciosWebSecurityConfiguration> instancesHash = 
			new HashMap<String, ServiciosWebSecurityConfiguration>(); //[dipucr-Felipe 3#260]
	
	/** Nombre del fichero de propiedades **/
	public static final String DEFAULT_CONFIG_FILENAME = "securityConfiguration.properties";
	
	/** Constantes **/
	public interface KEYSTORE{
		public static final String NAME = "security.keystore.name";
		public static final String TYPE = "security.keystore.type";
		public static final String PASSWORD = "security.keystore.password";
		public static final String CERT_ALIAS = "security.keystore.cert.alias";
		public static final String CERT_PASSWORD = "security.keystore.cert.password";
	}
	
	public interface TRUSTSTORE{
		public static final String NAME = "truststore.name";
		public static final String TYPE = "truststore.type";
		public static final String PASSWORD = "truststore.password";
	}
	
	public interface REGTEL_WS{
		public static final String SELLO_CERT_NAME = "ws.sello.cert.name";
	}
	
	/** Ruta base de los certificados **/
	private static String CERTIFICADOS_BASEPATH;
	
	/** Constructor **/
	protected ServiciosWebSecurityConfiguration() {
		super();
	}

	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized ServiciosWebSecurityConfiguration getInstance() throws Exception{
		
		String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
		return getInstance(idEntidad);
	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized ServiciosWebSecurityConfiguration getInstance(String entidad) throws Exception{

		if (StringUtils.isEmpty(CERTIFICADOS_BASEPATH)){
			CERTIFICADOS_BASEPATH = ISPACConfiguration.getInstance().get(ISPACConfiguration.CERTIFICADOS_PATH);
		}
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			ServiciosWebSecurityConfiguration serviciosWeb = new ServiciosWebSecurityConfiguration();
			if(entidad!=null){
				serviciosWeb.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
			}
			else{
				serviciosWeb.createInstance("", DEFAULT_CONFIG_FILENAME);
			}
			instancesHash.put(entidadHash, serviciosWeb);
		}
		return instancesHash.get(entidadHash);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getCompletePath(String key){
		return CERTIFICADOS_BASEPATH + File.separator + this.get(key);
	}
}
