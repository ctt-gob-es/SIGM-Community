package ieci.tdw.ispac.ispaclib.sign;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

/**
 * Factoría para la creación del conector de firma digital.
 *
 */
public class SignConnectorFactory {

	/** 
	 * Logger de la clase. 
	 */
	private static final Logger logger = Logger.getLogger(SignConnectorFactory.class);
	
	/**
	 * Conector por defecto
	 */
	public static String defaultImplClass = "ieci.tecdoc.sgm.tram.sign.Sigem3fasesSignConnector";
	/**
	 * [dipucr-Felipe #1246]
	 * Conector del portafirmas del MINHAP
	 */
	public static String portafirmasImplClass = "ieci.tecdoc.sgm.tram.sign.PortafirmasMinhapSignConnector";
	
	/**
     * Instancia de la clase.
     */
	private static HashMap<String, SignConnectorFactory> instancesHash = new HashMap<String, SignConnectorFactory>();

	 /**
     * Nombre de la clase que implementa el conector de sellado.
     */
    private String className = null;
    
    /**
	 * Constructor.
	 *
	 * @throws ISPACException si ocurre algún error.
	 */
    private SignConnectorFactory(String entidadId) throws ISPACException {
    	
		className = FirmaConfiguration.getInstance(entidadId).get(FirmaConfiguration.DIGITAL_SIGN_CONNECTOR_CLASS);
		
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
    public static synchronized SignConnectorFactory getInstance(IClientContext cct) throws ISPACException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}

	public static synchronized SignConnectorFactory getInstance(String entidadId) throws ISPACException {
		
		String entidadHash = StringUtils.defaultIfEmpty(entidadId,  "_");
		
		if (!instancesHash.containsKey(entidadHash)){
			SignConnectorFactory firmaConfig = new SignConnectorFactory(entidadHash);
						
			instancesHash.put(entidadHash, firmaConfig);
		}
		return instancesHash.get(entidadHash);
	}
    
	/**
	 * Obtiene una instancia del conector de firma digital.
	 * 
	 * @return Conector de firma digital.
	 * @throws ISPACException si ocurre algún error.
	 */
	public ISignConnector getSignConnector() throws ISPACException {
		
		ISignConnector signConnector = null;
		
		try {
			
			// Nombre de la clase que implementa el conector de firma digital
			if (StringUtils.isNotBlank(className)) {
				
				// Cargar la clase
				Class clazz = Class.forName(className);
				
				if (!ISignConnector.class.isAssignableFrom(clazz)) {
					throw new ISPACException(className + " no extiende la clase ISignConnector");
				}
				
				// Instanciar la clase
				signConnector = (ISignConnector) clazz.newInstance();

				if (logger.isDebugEnabled()) {
					logger.debug("ISignConnector creado [" + className + "]");
				}
			}
		} catch (ISPACException e) {
			throw e;
		} catch (Exception e) {
			throw new ISPACException(e);
		}
		
		return signConnector;
	}
	
	/**
	 * [dipucr-Felipe #1246] Creamos este nuevo método para compatibilizar la firma antigua
	 * con la nueva firma port@firmas hasta que se firmen todos los docs antiguos
	 * 
	 * @return Conector de firma digital.
	 * @throws ISPACException si ocurre algún error.
	 */
	@Deprecated
	public ISignConnector getDefaultSignConnector() throws ISPACException {
		
		ISignConnector signConnector = null;
		
		try {
			
			// Nombre de la clase que implementa el conector de firma digital
			if (StringUtils.isNotBlank(defaultImplClass)) {
				
				// Cargar la clase
				Class clazz = Class.forName(defaultImplClass);
				
				if (!ISignConnector.class.isAssignableFrom(clazz)) {
					throw new ISPACException(defaultImplClass + " no extiende la clase ISignConnector");
				}
				
				// Instanciar la clase
				signConnector = (ISignConnector) clazz.newInstance();
			}
		} catch (ISPACException e) {
			throw e;
		} catch (Exception e) {
			throw new ISPACException(e);
		}
		
		return signConnector;
	}
	
	/**
	 * [dipucr-Felipe #1246] Compatibilizar firma 3 fases con portafirmas 
	 * @return
	 * @throws ISPACException 
	 */
	public static boolean isDefaultImplClass(String entidadId) throws ISPACException{
		
		ISignConnector connector = SignConnectorFactory.getInstance(entidadId).getSignConnector();
		return defaultImplClass.equalsIgnoreCase(connector.getClass().getName());
	}
	
	/**
	 * [dipucr-Felipe #1246] Compatibilizar firma 3 fases con portafirmas 
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	public static boolean isDefaultImplClass(IClientContext cct) throws ISPACException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return isDefaultImplClass(entidad);
	}
	
	/**
	 * [dipucr-Felipe #1246]
	 * @return
	 * @throws ISPACException 
	 */
	public static boolean isPortafirmasImplClass(String entidadId) throws ISPACException{
		
		ISignConnector connector = SignConnectorFactory.getInstance(entidadId).getSignConnector();
		return portafirmasImplClass.equalsIgnoreCase(connector.getClass().getName());
	}
	
	/**
	 * [dipucr-Felipe #1246] 
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	public static boolean isPortafirmasImplClass(IClientContext cct) throws ISPACException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return isPortafirmasImplClass(entidad);
	}
}