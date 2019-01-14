package es.dipucr.portafirmas.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.PropertiesConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;

import es.dipucr.ownCloud.OwnCloudConfiguration;
import es.dipucr.sigem.api.rule.common.DipucrPropertiesConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

/**
 * Clase que gestiona la configuración de ISPAC.
 *
 */

@SuppressWarnings("serial")
public class PortafirmasConfiguration extends PropertiesConfiguration {
	
	private static PortafirmasConfiguration	 mInstance = null;

	public static final String DEFAULT_CONFIG_FILENAME = "portafirmasRedSara.properties";
	
	public interface PORTAFIRMAS{
		public static String PORTAFIRMAS_XADES_USER = "portafirmas.xades.user.";
		public static String PORTAFIRMAS_XADES_PASSW = "portafirmas.xades.passw.";
		public static String PORTAFIRMAS_PADES_USER = "portafirmas.pades.user.";
		public static String PORTAFIRMAS_PADES_PASSW = "portafirmas.pades.passw.";
		public static String PORTAFIRMAS_APLICACION_XADES = "portafirmas.aplicacion.xades.";
		public static String PORTAFIRMAS_APLICACION_PADES = "portafirmas.aplicacion.pades.";
		
		public static String PORTAFIRMAS_REMITENTE_XADES_IDENT = "portafirmas.remitente.xades.ident.";
		public static String PORTAFIRMAS_REMITENTE_XADES_NAME = "portafirmas.remitente.xades.name.";
		public static String PORTAFIRMAS_REMITENTE_XADES_SURNAME1 = "portafirmas.remitente.xades.surname1.";
		public static String PORTAFIRMAS_REMITENTE_XADES_SURNAME2 = "portafirmas.remitente.xades.surname2.";
		
		public static String PORTAFIRMAS_REMITENTE_PADES_IDENT = "portafirmas.remitente.pades.ident.";
		public static String PORTAFIRMAS_REMITENTE_PADES_NAME = "portafirmas.remitente.pades.name.";
		public static String PORTAFIRMAS_REMITENTE_PADES_SURNAME1 = "portafirmas.remitente.pades.surname1.";
		public static String PORTAFIRMAS_REMITENTE_PADES_SURNAME2 = "portafirmas.remitente.pades.surname2.";
	}
	
	/**
	 * Constructor.
	 */
	protected PortafirmasConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized PortafirmasConfiguration getInstance() 
			throws ISPACException {
		
		if (mInstance == null) {
			mInstance = new PortafirmasConfiguration();
			mInstance.initiate(DEFAULT_CONFIG_FILENAME);
		}
		return mInstance;
	}
}
