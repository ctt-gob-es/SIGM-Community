package es.dipucr.sigem.api.rule.common.bop;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import es.dipucr.sigem.api.rule.common.DipucrPropertiesConfiguration;

/**
 * Clase que gestiona la configuración de ISPAC.
 *
 */
public class BOPConfiguration extends DipucrPropertiesConfiguration {
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static BOPConfiguration mInstance = null;
	
	public static final String DEFAULT_CONFIG_FILENAME = "bop.properties";
	public static final String COSTE_CARACTER = "costeCaracter";
	public static final String COSTE_URGENCIA = "costeUrgencia";
	public static final String IVA = "iva";
	
	/**
	 * Constructor.
	 */
	protected BOPConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized BOPConfiguration getInstance() throws ISPACRuleException{
		
		if (mInstance == null){
			mInstance = new BOPConfiguration();
			mInstance.createInstance(DEFAULT_CONFIG_FILENAME);
		}
		return mInstance;
	}
}
