package es.dipucr.sigem.api.rule.common.sms;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import es.dipucr.sigem.api.rule.common.DipucrPropertiesConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

/**
 * Clase que gestiona la configuración de ISPAC.
 *
 */
public class SMSConfiguration extends DipucrPropertiesConfiguration {
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static SMSConfiguration mInstance = null;
	
	public static final String DEFAULT_CONFIG_FILENAME = "SMS.properties";
	public static final String USER_SMS = "cUser_sms";
	public static final String PWD_SMS = "cPwd_sms";
	public static final String REMITENTE_SMS = "cRemitente_sms";
	public static final String TXT_SMS = "cTxt_sms";
	
	/**
	 * Constructor.
	 */
	protected SMSConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized SMSConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		if (mInstance == null){
			mInstance = new SMSConfiguration();
			mInstance.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
		}
		return mInstance;
	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized SMSConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
