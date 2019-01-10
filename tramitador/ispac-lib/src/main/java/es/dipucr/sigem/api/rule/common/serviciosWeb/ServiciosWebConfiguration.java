package es.dipucr.sigem.api.rule.common.serviciosWeb;

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
public class ServiciosWebConfiguration extends DipucrPropertiesConfiguration {
	
	private static final long serialVersionUID = 5013658649831582105L;

	private static HashMap<String, ServiciosWebConfiguration> instancesHash = 
			new HashMap<String, ServiciosWebConfiguration>(); //[dipucr-Felipe 3#260]
	
	public static final String DEFAULT_CONFIG_FILENAME = "serviciosWeb.properties";
	
	public static final String URL_NOTIFICA_SW = "servicioWeb.notifica.url";
	public static final String URL_COMPARECE_SW = "servicioWeb.comparece.url";
	public static final String URL_WEB_EMPLEADO_SW = "servicioWeb.WebEmpleado.url";
	public static final String URL_ETABLON_SW = "servicioWeb.eTablon.url";
	public static final String URL_SEDE_BOP_SW = "servicioWeb.SedeBOP.url";
	public static final String URL_FACTURA_SW = "servicioWeb.Factura.url";
	public static final String URL_PLATAFORMACONTRATACION_SW = "servicioWeb.plataformaContratacion.url";
	public static final String URL_PEMPLEADO_AYTOS_SW = "servicioWeb.PEmpleadoAytos.url";
	
	public static final String URL_SVD_RECUBRIMIENTO_SW = "servicioWeb.SVD.Recubrimiento.url";
	public static final String URL_SVD_SCSP_SW = "servicioWeb.SVD.SW_SCSP.url";
	public static final String URL_SVD_CLIENTELIGERO_SW = "servicioWeb.SVD.SW_ClienteLigero.url";
	
	public static final String URL_TEU_SW = "servicioWeb.SVD.SW_TEU.url";
	
	public static final String URL_PORTAFIRMASEXTERNOMODIFY = "servicioWeb.PORTAFIRMASEXTERNOMODIFY.url";
	public static final String URL_PORTAFIRMASEXTERNOADMIN = "servicioWeb.PORTAFIRMASEXTERNOADMIN.url";
	public static final String URL_PORTAFIRMASEXTERNO_CONSULTA = "servicioWeb.PORTAFIRMASEXTERNO_CONSULTA.url";
	
	//[dipucr-Felipe #304]
	public static final String URL_BDNS = "servicioWeb.BDNS.url";
	public static final String BDNS_USER = "servicioWeb.BDNS.usuario";
	public static final String BDNS_APP_BOP = "servicioWeb.BDNS.id.bop";
	public static final String BDNS_APP_CONVOCATORIAS = "servicioWeb.BDNS.id.convocatoria";
	public static final String BDNS_APP_CONCESPAGOPROY = "servicioWeb.BDNS.id.concpagoproy";
	public static final String BDNS_APP_DATOSPERSONALES = "servicioWeb.BDNS.id.datospersonales";
	public static final String BDNS_APP_DEVOLUCIONREINTEGRO = "servicioWeb.BDNS.id.devolucionreintegro";
	
	
	//Servicio web de consulta de ORVE (WSExportacion)
	public static final String ORVE_URL = "servicioWeb.ORVE.url";
	public static final String ORVE_USUARIO = "servicioWeb.ORVE.usuario";
	public static final String ORVE_PASSWORD = "servicioWeb.ORVE.password";
	public static final String ORVE_MAIL_ERROR = "servicioWeb.ORVE.emailError";
	public static final String ORVE_USUARIO_REGISTRO = "ORVE.registro.usuario";
	public static final String ORVE_PASSWORD_REGISTRO = "ORVE.registro.password";
	public static final String ORVE_BOOKID_REGISTRO = "ORVE.registro.bookid";
	public static final String ORVE_COD_TIPO_TRANSPORTE = "ORVE.registro.codTipoTransporte";
	public static final String ORVE_COD_OFICINA = "ORVE.registro.codOficinaORVE";
	public static final String ORVE_COD_TIPO_ASUNTO = "ORVE.registro.codTipoAsunto";


	
	/**
	 * Constructor.
	 */
	protected ServiciosWebConfiguration() {
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
	public static synchronized ServiciosWebConfiguration getInstance(String entidad) throws ISPACRuleException{
		
		String entidadHash = entidad;
		if (StringUtils.isEmpty(entidad)) entidadHash = "_";
		
		if (!instancesHash.containsKey(entidadHash)){
			ServiciosWebConfiguration serviciosWeb = new ServiciosWebConfiguration();
			if(entidad!=null){
				serviciosWeb.createInstance(entidad, DEFAULT_CONFIG_FILENAME);
			}
			else{
				serviciosWeb.createInstance("", DEFAULT_CONFIG_FILENAME);
			}
			instancesHash.put(entidadHash, serviciosWeb);
		}
		return instancesHash.get(entidadHash);
		
	}
	
	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACRuleException 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized ServiciosWebConfiguration getInstance(IClientContext cct) throws ISPACRuleException{
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		return getInstance(entidad);
	}
}
