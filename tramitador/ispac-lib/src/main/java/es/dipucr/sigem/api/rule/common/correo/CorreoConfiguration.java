package es.dipucr.sigem.api.rule.common.correo;

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
public class CorreoConfiguration extends DipucrPropertiesConfiguration {
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, CorreoConfiguration> instancesHash = 
			new HashMap<String, CorreoConfiguration>(); //[dipucr-Felipe 3#260]
	
	public static final String DEFAULT_CONFIG_FILENAME = "correo.properties";
	public static final String HOST_MAIL = "cHost_mail";
	public static final String PORT_MAIL = "cPort_mail";
	public static final String USR_MAIL = "cUsr_mail";
	public static final String PWD_MAIL = "cPwd_mail";
	public static final String CONV_LUGAR = "cConv_Lugar";
	public static final String CONV_ENTIDAD = "cConv_Entidad";
	public static final String CONV_SEDE = "cConv_Sede";
	//Direccion del emisor
	public static final String CONV_FROM = "cConv_From";
	
	//[Manu Ticket #306] * SIGEM Envio Mail Error en comprobación from de algunos servidores
	public static final String DECR_FROM = "cDecr_From";
	
	/**
	 * Constructor.
	 */
	protected CorreoConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 * 
	 * @since 01.04.2016 [dipucr-Felipe 3#260] Reescribo por completo el método usando el hash de objetos 
	 */
	public static synchronized CorreoConfiguration getInstance(String entidad) throws ISPACRuleException{

		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			CorreoConfiguration correo = new CorreoConfiguration();
			if(entidad!=null){
				correo.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
			}
			else{
				correo.createInstance("", DEFAULT_CONFIG_FILENAME);
			}
			instancesHash.put(entidadHash, correo);
		}
		return instancesHash.get(entidadHash);
	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized CorreoConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
