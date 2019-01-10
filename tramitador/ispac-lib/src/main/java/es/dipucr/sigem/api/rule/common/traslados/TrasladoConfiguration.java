package es.dipucr.sigem.api.rule.common.traslados;

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
public class TrasladoConfiguration extends DipucrPropertiesConfiguration {
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, TrasladoConfiguration> instancesHash =	new HashMap<String, TrasladoConfiguration>();
	
	public static final String DEFAULT_CONFIG_FILENAME = "traslado.properties";
	
	public static final String ARCHIVO_TRASLADO_EMAIL = "archivo_traslado_email";
	public static final String ARCHIVO_TRASLADO_ID = "archivo_traslado_id";

	public static final String BOP_TRASLADO_EMAIL = "bop_traslado_email";
	public static final String BOP_TRASLADO_ID = "bop_traslado_id";
	
	public static final String CEX_TRASLADO_EMAIL = "cex_traslado_email";
	public static final String CEX_TRASLADO_ID = "cex_traslado_id";

	public static final String CONTRATACION_COMPRAS_TRASLADO_EMAIL = "contratacion_compras_traslado_email";
	public static final String CONTRATACION_COMPRAS_TRASLADO_ID = "contratacion_compras_traslado_id";

	public static final String CONTRATACION_TRASLADO_EMAIL = "contratacion_traslado_email";
	public static final String CONTRATACION_TRASLADO_ID = "contratacion_traslado_id";

	public static final String CULTURA_TRASLADO_EMAIL = "cultura_traslado_email";
	public static final String CULTURA_TRASLADO_ID = "cultura_traslado_id";

	public static final String GESTION_TRIBUTARIA_TRASLADO_EMAIL = "gestion_tributaria_traslado_email";
	public static final String GESTION_TRIBUTARIA_TRASLADO_ID = "gestion_tributaria_traslado_id";

	public static final String GUARDERIA_TRASLADO_EMAIL = "guarderia_traslado_email";
	public static final String GUARDERIA_TRASLADO_ID = "guarderia_traslado_id";
	
	public static final String IMPRENTA_TRASLADO_EMAIL = "imprenta_traslado_email";
	public static final String IMPRENTA_TRASLADO_ID = "imprenta_traslado_id";

	public static final String INFORMATICA_TRASLADO_EMAIL = "informatica_traslado_email";
	public static final String INFORMATICA_TRASLADO_ID = "informatica_traslado_id";

	public static final String INTERVENCION_TRASLADO_EMAIL = "intervencion_traslado_email";
	public static final String INTERVENCION_TRASLADO_ID = "intervencion_traslado_id";
	
	public static final String MEDIO_AMBIENTE_TRASLADO_EMAIL = "medio_ambiente_traslado_email";
	public static final String MEDIO_AMBIENTE_TRASLADO_ID = "medio_ambiente_traslado_id";

	public static final String PATRIMONIO_TRASLADO_EMAIL = "patrimonio_traslado_email";
	public static final String PATRIMONIO_TRASLADO_ID = "patrimonio_traslado_id";

	public static final String PERSONAL_TRASLADO_EMAIL = "personal_traslado_email";
	public static final String PERSONAL_TRASLADO_ID = "personal_traslado_id";

	public static final String PLANES_PROVINCIALES_TRASLADO_EMAIL = "planes_provinciales_traslado_email";
	public static final String PLANES_PROVINCIALES_TRASLADO_ID = "planes_provinciales_traslado_id";

	public static final String PRESIDENCIA_ALCALDIA_TRASLADO_EMAIL = "presidencia_alcaldia_traslado_email";
	public static final String PRESIDENCIA_ALCALDIA_TRASLADO_ID = "presidencia_alcaldia_traslado_id";

	public static final String PREVENCION_TRASLADO_EMAIL = "prevencion_traslado_email";
	public static final String PREVENCION_TRASLADO_ID = "prevencion_traslado_id";

	public static final String PROMOCION_ECONOMICA_TRASLADO_EMAIL = "promocion_economica_traslado_email";
	public static final String PROMOCION_ECONOMICA_TRASLADO_ID = "promocion_economica_traslado_id";

	public static final String PSIQUIATRICO_TRASLADO_EMAIL = "psiquiatrico_traslado_email";
	public static final String PSIQUIATRICO_TRASLADO_ID = "psiquiatrico_traslado_id";

	public static final String RECAUDACION_EJECUTIVA_TRASLADO_EMAIL = "recaudacion_ejecutiva_traslado_email";
	public static final String RECAUDACION_EJECUTIVA_TRASLADO_ID = "recaudacion_ejecutiva_traslado_id";

	public static final String RESIDENCIA_UNIVERSITARIA_TRASLADO_EMAIL = "residencia_universitaria_traslado_email";
	public static final String RESIDENCIA_UNIVERSITARIA_TRASLADO_ID = "residencia_universitaria_traslado_id";

	public static final String SECRETARIA_TRASLADO_EMAIL = "secretaria_traslado_email";
	public static final String SECRETARIA_TRASLADO_ID = "secretaria_traslado_id";

	public static final String SEGURIDAD_TRASLADO_EMAIL = "seguridad_traslado_email";
	public static final String SEGURIDAD_TRASLADO_ID = "seguridad_traslado_id";

	public static final String SERVICIOS_SOCIALES_TRASLADO_EMAIL = "servicios_sociales_traslado_email";
	public static final String SERVICIOS_SOCIALES_TRASLADO_ID = "servicios_sociales_traslado_id";

	public static final String TESORERIA_TRASLADO_EMAIL = "tesoreria_traslado_email";
	public static final String TESORERIA_TRASLADO_ID = "tesoreria_traslado_id";

	public static final String UATA_TRASLADO_EMAIL = "uata_traslado_email";
	public static final String UATA_TRASLADO_ID = "uata_traslado_id";

	public static final String VICEPRESIDENCIA_PRIMERA_TRASLADO_EMAIL = "vicepresidencia_primera_traslado_email";
	public static final String VICEPRESIDENCIA_PRIMERA_TRASLADO_ID = "vicepresidencia_primera_traslado_id";

	public static final String VYO_TRASLADO_EMAIL = "vyo_traslado_email";
	public static final String VYO_TRASLADO_ID = "vyo_traslado_id";
	
	/**
	 * Constructor.
	 */
	protected TrasladoConfiguration() {
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
	public static synchronized TrasladoConfiguration getInstance(String entidad) throws ISPACRuleException{

		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			TrasladoConfiguration correo = new TrasladoConfiguration();
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
	public static synchronized TrasladoConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
