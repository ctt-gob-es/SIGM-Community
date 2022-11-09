package ieci.tdw.ispac.ispaclib.sign.portafirmas;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

/**
 * Factoría para la creación del conector de portafirmas
 *
 */
public class ProcessSignConnectorFactory {
	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(ProcessSignConnectorFactory.class);
	
	/**
	 * Conector por defecto
	 */
	public static String defaultImplClass="ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorImpl";
	
	/**
     * Instancia de la clase.
     */
	private static HashMap<String, ProcessSignConnectorFactory> instancesHash = new HashMap<String, ProcessSignConnectorFactory>();

    /**
     * Nombre de la clase que implementa el conector de sellado.
     */
    private String className = null;
    
    /**
     * Entidad a la que corresponde esta instancia de portafirmas.
     */
    private String entidadId = null;

	/**
	 * Constructor.
	 *
	 * @throws ISPACException si ocurre algún error.
	 */
    private ProcessSignConnectorFactory(String entidadId) throws ISPACException {
    	
    	this.entidadId = entidadId;
    	
		className = FirmaConfiguration.getInstance(entidadId).get(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_CLASS);
		
		if(className == null){
			className = defaultImplClass;
		}

   	}

	/**
	 * Obtiene una instancia de la factoría.
	 *
	 * @return Intancia de la factoría.
	 * @throws ISPACException si ocurre algún error.
	 */
    public static synchronized ProcessSignConnectorFactory getInstance(IClientContext cct) throws ISPACException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}

	public static synchronized ProcessSignConnectorFactory getInstance(String entidadId) throws ISPACException {
		
		String entidadHash = StringUtils.defaultIfBlank(entidadId,  "_");
		
		if (!instancesHash.containsKey(entidadHash)){
			ProcessSignConnectorFactory firmaConfig = new ProcessSignConnectorFactory(entidadHash);
						
			instancesHash.put(entidadHash, firmaConfig);
		}
		return instancesHash.get(entidadHash);
	}

	public boolean isDefaultConnector (){
		return StringUtils.equalsIgnoreCase(className, defaultImplClass);
	}
	/**
	 * Obtiene el conector de proceso de firma. Portafirmas
	 * @return Conector de de proceso de firma. Portafirmas
	 * @throws ISPACException si ocurre algún error.
	 */
	public ProcessSignConnector getProcessSignConnector() throws ISPACException {

		ProcessSignConnector processSignConnector = null;

		// Comprobar que se haya establecido el nombre de la clase
		if (StringUtils.isBlank(className)) {
			return new ProcessSignConnectorImpl();
		}

		try {

			// Cargar la clase
			Class clazz = Class.forName(className);
			if (!ProcessSignConnector.class.isAssignableFrom(clazz)) {
				throw new ISPACException(className + " no implementa el interfaz ProcessSignConnector");
			}

			// Instanciar la clase
			processSignConnector = (ProcessSignConnector) clazz.newInstance();
			processSignConnector.inicializar(this.entidadId);

			if (logger.isDebugEnabled()) {
				logger.debug("ProcessSignConnector creado [" + className + "]");
			}
		}
		catch (Exception e) {
			logger.error("Error al instanciar el conector del proceso de firma. Portafirmas", e);
			throw new ISPACException(e);
		}

		return processSignConnector;
	}
}
