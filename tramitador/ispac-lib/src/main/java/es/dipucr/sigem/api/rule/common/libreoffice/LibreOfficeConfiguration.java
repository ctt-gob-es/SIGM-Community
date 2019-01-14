package es.dipucr.sigem.api.rule.common.libreoffice;

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
public class LibreOfficeConfiguration extends DipucrPropertiesConfiguration {
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, LibreOfficeConfiguration> instancesHash = 
			new HashMap<String, LibreOfficeConfiguration>();
	
	public static final String DEFAULT_CONFIG_FILENAME = "libreoffice.properties";

	public final static String OPEN_OFFICE_CONNECT = "OPEN_OFFICE_CONNECT";
	public final static String OPEN_OFFICE_ADDITIONAL_INSTANCES = "OPEN_OFFICE_ADDITIONAL_INSTANCES";
	public final static String OPEN_OFFICE_TIMEOUT = "OPEN_OFFICE_TIMEOUT"; //TODO:Sin usar -> Borrar de ispac.properties
	
	/**
	 * Constructor.
	 */
	protected LibreOfficeConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized LibreOfficeConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			LibreOfficeConfiguration serviciosWeb = new LibreOfficeConfiguration();
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
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized LibreOfficeConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
