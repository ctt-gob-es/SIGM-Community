package es.dipucr.sigem.api.rule.common.pdf;

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
@SuppressWarnings("serial")
public class PdfConfiguration extends DipucrPropertiesConfiguration {
	
	private static HashMap<String, PdfConfiguration> instancesHash = 
			new HashMap<String, PdfConfiguration>(); //[dipucr-Felipe 3#260]
	
	public static final String DEFAULT_CONFIG_FILENAME = "pdf.properties";
	
	public interface ENCRIPTAR{
		public static String PASSWORD = "encriptar.password";
		public static String MENSAJE_GUARDAR = "encriptar.mensajeGuardar";
	}
	
	/**
	 * Constructor.
	 */
	protected PdfConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 * 
	 * @since 06.04.2016 [dipucr-Felipe 3#260] Reescribo por completo el método usando el hash de objetos 
	 */
	public static synchronized PdfConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			PdfConfiguration pdfConfig = new PdfConfiguration();
			if(entidad!=null){
				pdfConfig.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
			}
			else{
				pdfConfig.createInstance("", DEFAULT_CONFIG_FILENAME);
			}
			instancesHash.put(entidadHash, pdfConfig);
		}
		return instancesHash.get(entidadHash);
		
	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized PdfConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
