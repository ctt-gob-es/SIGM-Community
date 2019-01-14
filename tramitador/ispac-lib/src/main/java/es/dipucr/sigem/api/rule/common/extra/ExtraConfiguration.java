package es.dipucr.sigem.api.rule.common.extra;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;

import es.dipucr.sigem.api.rule.common.DipucrPropertiesConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

/**
 * [dipucr-Felipe 3#325]
 * Clase que gestiona la configuración de ISPAC.
 */
@SuppressWarnings("serial")
public class ExtraConfiguration extends DipucrPropertiesConfiguration {
	
	private static HashMap<String, ExtraConfiguration> instancesHash = 
			new HashMap<String, ExtraConfiguration>(); //[dipucr-Felipe 3#260]

	public static final String DEFAULT_CONFIG_FILENAME = "extra.properties";
	
	public interface ALERTAS_VOLUMENES{

		public static String OCCUPANCY_PERCENT = "alertas.volumen.percent";
		
		public interface MAIL{
			public static String TO = "alertas.volumen.mail.to";
			public static String SUBJECT = "alertas.volumen.mail.subject";
			public static String CONTENT = "alertas.volumen.mail.content";
		}
		
		public interface MAIL_ERROR{
			public static String TO = "alertas.volumen.mail_error.to";
			public static String SUBJECT = "alertas.volumen.mail_error.subject";
			public static String CONTENT = "alertas.volumen.mail_error.content";
		}
	}
	
	public interface CONSOLIDACION{
		
		public interface MAIL{
			public static String REGISTROS = "consolidacion.mail.registros";
			public static String ERRORES = "consolidacion.mail.errores";
		}
		
		public interface MAIL_ERROR{
			public static String TO = "consolidacion.mail_error.to";
			public static String SUBJECT = "consolidacion.mail_error.subject";
			public static String CONTENT = "consolidacion.mail_error.content";
		}
	}
	
	public interface NOTIFICA{
		
		public interface MAIL{
			public static String REGISTROS = "notifica.mail.registros";
			public static String ERRORES = "notifica.mail.errores";
		}
		
		public interface MAIL_ERROR{
			public static String TO = "notifica.mail_error.to";
			public static String SUBJECT = "notifica.mail_error.subject";
			public static String CONTENT = "notifica.mail_error.content";
		}
	}
	
	/**
	 * Constructor.
	 */
	protected ExtraConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized ExtraConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			ExtraConfiguration decretosConfig = new ExtraConfiguration();
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
	public static synchronized ExtraConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
