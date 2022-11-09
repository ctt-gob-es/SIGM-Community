package es.gob.afirma.signfolder.server.proxy;

/**
 * Conjunto de nombres de atributo que se utilizan para almacenar informaci&oacute;n en
 * la sesi&oacute;n de las aplicaciones Cliente.
 */
public class SessionParams {

	/** Indica si se trata de inciciar sesi&oacute;n usando Cl@ve. */
	public static final String INIT_WITH_CLAVE = "initializing-clave"; //$NON-NLS-1$

	/** URL de acceso a Cl@ve. */
	public static final String CLAVE_URL = "claveServiceUrl"; //$NON-NLS-1$

	/** Listado de IdP excluidos. */
	public static final String CLAVE_EXCLUDED_IDPS = "excludedIdPList"; //$NON-NLS-1$

	/** IdP que cargar autom&aacute;ticamente. */
	public static final String CLAVE_FORCED_IDP = "forcedIdP"; //$NON-NLS-1$

	/** Token de solicitud de login con Cl@ve. */
	public static final String CLAVE_REQUEST_TOKEN = "SAMLRequest"; //$NON-NLS-1$

	/** Identificador de autenticaci&oacute;n. */
	public static final String CLAVE_AUTHENTICATION_ID = "caid"; //$NON-NLS-1$

	/** Identificador de la transacci&oacute;n de FIRe. */
	public static final String FIRE_TRID = "fireTrid"; //$NON-NLS-1$

	/** Array con las referencias de las peticiones enviadas a firmar con FIRe. */
	public static final String FIRE_REQUESTS = "fireRefs"; //$NON-NLS-1$

	/** Identificador de sesion compartida. */
	public static final String SHARED_SESSION_ID = "ssid"; //$NON-NLS-1$

	/** Identificador del objeto con todos los datos de sesi&oacute;n. */
	public static final String PROXY_SESSION = "psession"; //$NON-NLS-1$

	public static final String INIT_TOKEN = "token"; //$NON-NLS-1$
	public static final String CERT = "cert"; //$NON-NLS-1$
	public static final String DNI = "dni"; //$NON-NLS-1$


	public static final String VALID_SESSION = "validsession"; //$NON-NLS-1$
}
