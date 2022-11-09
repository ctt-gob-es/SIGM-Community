/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.actions;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.TempFileBO;
import es.seap.minhap.portafirmas.business.administration.PreSignBO;
import es.seap.minhap.portafirmas.business.administration.ThemeBO;
import es.seap.minhap.portafirmas.business.jobs.principal.JobPrincipalQuartz;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.ListComparador;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.job.CleanSessionAttributesJob;
import es.seap.minhap.portafirmas.utils.job.Dir3Job;
import es.seap.minhap.portafirmas.utils.job.ExpiredRequestsJob;
import es.seap.minhap.portafirmas.utils.job.GenerateReportsJob;
import es.seap.minhap.portafirmas.utils.job.LineasFirmaFinAutorizacionJob;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.ConfigManagerBO;

/**
 * Carga los datos de configuracion a nivel de aplicacion, este objeto se crea al iniciarse la aplicacion y
 * se guarda en el scope de aplicacion para ser accesible desde toda la aplicacion.
 * @author daniel.palacios
 *
 */
//@Service
//@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ApplicationVO  {

	private static final String JOB = "Job: ";
	private static final String NO_EXISTE_SE_CREA = " no existe, se crea";
	private static final String YA_EXISTÍA = " ya existía";

	private Logger log = Logger.getLogger(ApplicationVO.class);	
	
	private PfDocumentTypesDTO documentTypeDefault;
	
	private PfApplicationsDTO applicationPfirma;
	private PfApplicationsDTO applicationPfirmaPADES;
	private PfApplicationsDTO applicationPfirmaXADES;
	private PfApplicationsDTO applicationPfirmaXADESEnveloped;
	private PfApplicationsDTO applicationPfirmaCADES;
	
	private Map<String, String> ldapLoginParameters;
	
	private Map<String, PfTagsDTO> stateTags;
	private Map<String, PfTagsDTO> systemTags;
	
	private List<AbstractBaseDTO> configurationListDefault;	
	private List<AbstractBaseDTO> serverParameterDefaultList;
	
	private StringBuilder mailTemplate;

	private List<AbstractBaseDTO> profileDefaultList;
	private List<AbstractBaseDTO> profileDefaultListAdmProvince;
	private List<AbstractBaseDTO> documentTypePfirmaList;
	
	private PfParametersDTO passwordParameter;
	private PfParametersDTO ldapIdParameter;
	private PfParametersDTO serverParameterDefault;	
	
	private PfConfigurationsParameterDTO maxDocumentSize;
	private PfConfigurationsParameterDTO debugParameter;
	private PfConfigurationsParameterDTO ldapParameter;
	private PfConfigurationsParameterDTO emailNoticeParameter;
	private PfConfigurationsParameterDTO smsNoticeParameter;
	private PfConfigurationsParameterDTO signReportParameter;
	private PfConfigurationsParameterDTO csvActivatedParameter;
	private PfConfigurationsParameterDTO reportActivatedParameter;
	private PfConfigurationsParameterDTO viewPreSignActivatedParameter;
	private PfConfigurationsParameterDTO viewTCNActivatedParameter;
	private PfConfigurationsParameterDTO showVisibilityOptions;	
	private PfConfigurationsParameterDTO validateSign;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private ConfigManagerBO configManagerBO;

	@Autowired
	private ThemeBO themeBO;

	@Autowired
	private PreSignBO preSignBO;

	@Autowired
	private TempFileBO tempFileBO;

	@Autowired
	private QuartzInvoker quartzInvoker;

	@Resource(name="fireProperties")
	private Properties fireProperties;

	@Resource(name = "interfazGenericaProperties")
	private Properties interfazGenericaProperties;

	@Resource(name = "afirmaProperties")
	private Properties afirmaProperties;
	
	private List<AbstractBaseDTO> defaultValidJobList;

	private String language;
	private String theme;
	private String inboxNRows;
	
	private static boolean isExecuted=false;
	
	
	 private static ApplicationVO instance;

	  public synchronized static ApplicationVO getInstance(){
	        if(instance==null){
	          instance = new ApplicationVO();
	        }
	    return instance;
	  }
	
	private ApplicationVO()
	{

	}
	
	

	/**
	 * Inicializa los datos de la aplicacion portafirmas, este m&eacute;todo se ejecuta al crearse el objeto.
	 */
	
	@PostConstruct
	private void create() {

		
		//isExecuted= (getStateTags()!=null);
		
		if (!isExecuted) {
			
			isExecuted=true;

			log.info("create init.");

			long inicio = System.currentTimeMillis();

			loadInitConfiguration();

			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "loadInitConfiguration");

			mailTemplate = applicationBO.loadMailTemplate();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "loadMailTemplate");

			// Ejecuta el job de prefirma
			preSignBO.executePreSign();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "executePreSign");

			// Ejecuta el job de limpieza temporal
			tempFileBO.executeCleanup();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "executeCleanup");
			
			/**[Ticket1330]*Quitar las líneas de firma en un request cuando acaba el periodo de autorización*/
			executeLimpiarLineaFirmaFinAtorizaciones();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "executeLimpiarLineaFirmaFinAtorizaciones");

			// Ejecuta la tarea de comprobación de caducidad de las peticiones
			executeExpiredRequestsJob();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "executeExpiredRequestsJob");

			// Elimina las sesiones antiguas.
			executeCleanOldSessions();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "executeCleanOldSessions");

			// Prepara la tarea para generar los informes de firmas diarias
			executeGenerateReportsJob();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "executeGenerateReportsJob");

			// Prepara la tarea para pasar al histórico las peticiones del mismo día pero
			// del año anterior
			// Se deja de ejecutar el job de paso de peticiones al histórico (10/10/2017)
			// executeRequestToHistoricJob();
			// inicio = imprimirDuracion(inicio, System.currentTimeMillis(),
			// "executeRequestToHistoricJob");

			executeDir3Job();
			inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "executeDir3Job");			

			executePrincipalJob();
			imprimirDuracion(inicio, System.currentTimeMillis(), "executePrincipalJob");
			
			
			

			log.info("create end.");
		}
	}

	private long imprimirDuracion(long inicio, long fin, String nombre) {
		log.debug("Duración carga de " + nombre + " : " + (fin - inicio) + " ms.");
		return fin;
	}

	/**
	 * Recarga el tema de la aplicacion, lenguaje y el n&uacute;mero de filas de la bandeja de la aplicacion.
	 */
	public void reLoadTheme() {
		theme = themeBO.loadThemeAdm();
		language = themeBO.loadLanguageAdm();
		inboxNRows = themeBO.loadAdmPageSize();
	}
	/**
	 * Recarga los par&aacute;metros de configuracion de login, los de tipo LDAP,
	 * y de tipo LOGIN.
	 */
	public void reLoadLoginParameters() {
		// Load LDAP login parameters
		ldapLoginParameters = applicationBO.queryLDAPParameterList();
		// Load debug parameter
		debugParameter = applicationBO.queryDebug();
		// Load LDAP parameter
		ldapParameter = applicationBO.queryLdap();
	}
	/**
	 * Recarga los par&aacute;metros de configuracion de la aplicacion.
	 */
	public void reLoadConfigurations() {
		// Load Application configurations
		configurationListDefault = applicationBO.queryConfigurationList();
		ListComparador comparador = new ListComparador("configuration", 1);
		Collections.sort(configurationListDefault, comparador);
	}
	/**
	 * Recarga los par&aacute;metros de noticia de la aplicacion, tanto los de tipo email como los de sms.
	 */
	public void reLoadNoticeParameters() {
		emailNoticeParameter = applicationBO.queryEmailNoticeParameter();
		smsNoticeParameter = applicationBO.querySMSNoticeParameter();
	}
	/**
	 * Recarga los par&aacute;metros de firma de informes de la aplicacion.
	 */
	public void reLoadSignReportParameter() {
		signReportParameter = applicationBO.querySignReportParameter();
	}

	/**
	 * Método que lanza la tarea programada para comprobar la caducidad de las peticiones
	 */
	private void executeExpiredRequestsJob() {
		if (!quartzInvoker.existJob(Constants.JOB_EXPIRED_REQUESTS, Constants.JOB_EXPIRED_REQUESTS)) {
			//quartzInvoker.deleteJob(Constants.JOB_EXPIRED_REQUESTS, Constants.JOB_EXPIRED_REQUESTS);
			log.debug (JOB.concat(Constants.JOB_EXPIRED_REQUESTS).concat(NO_EXISTE_SE_CREA));
			quartzInvoker.scheduleJobCron(null, ExpiredRequestsJob.class,
					Constants.JOB_EXPIRED_REQUESTS, Constants.JOB_EXPIRED_REQUESTS,
					Constants.JOB_EXPIRED_REQUESTS_CRON_EXPRESSION);

		} else {
			log.debug (JOB.concat(Constants.JOB_EXPIRED_REQUESTS).concat(YA_EXISTÍA));
		}
	}
	
	/**
	 * Elimina las sesiones antiguas.
	 */
	private void executeCleanOldSessions () {
		if (!quartzInvoker.existJob(Constants.JOB_CLEAN_OLD_SESSIONS, Constants.JOB_CLEAN_OLD_SESSIONS)) {
			log.debug (JOB + Constants.JOB_CLEAN_OLD_SESSIONS + NO_EXISTE_SE_CREA);
			//quartzInvoker.deleteJob(Constants.JOB_CLEAN_OLD_SESSIONS, Constants.JOB_CLEAN_OLD_SESSIONS);
			quartzInvoker.scheduleJobCron(null, CleanSessionAttributesJob.class,
					Constants.JOB_CLEAN_OLD_SESSIONS, Constants.JOB_CLEAN_OLD_SESSIONS,
					Constants.JOB_CLEAN_OLD_SESSIONS_CRON_EXPRESSION);
		} else {
			log.debug (JOB.concat( Constants.JOB_CLEAN_OLD_SESSIONS).concat(YA_EXISTÍA));
		}	
	}
	
	/**[Ticket1330]*Quitar las líneas de firma en un request cuando acaba el periodo de autorización*/
	private void executeLimpiarLineaFirmaFinAtorizaciones() {
		log.debug("Se creara el job "+Constants.JOB_LINEAFIRMA_AUTORIZACION);
		if (!quartzInvoker.existJob(Constants.JOB_LINEAFIRMA_AUTORIZACION, Constants.JOB_LINEAFIRMA_AUTORIZACION)) {
			//quartzInvoker.deleteJob(Constants.JOB_DOCELWEB_CLEANER, Constants.JOB_DOCELWEB_CLEANER);
			log.debug (JOB + Constants.JOB_LINEAFIRMA_AUTORIZACION + NO_EXISTE_SE_CREA);
			quartzInvoker.scheduleJobCron(null, LineasFirmaFinAutorizacionJob.class,
					Constants.JOB_LINEAFIRMA_AUTORIZACION, Constants.JOB_LINEAFIRMA_AUTORIZACION,
					Constants.JOB_LINEAFIRMA_AUTORIZACION_EVERY_DAY);
		} else {
			log.debug (JOB + Constants.JOB_LINEAFIRMA_AUTORIZACION + YA_EXISTÍA);
		}
	}
	
	/**
	 * Método que lanza la tarea programada para generar los informes de firma
	 */
	private void executeGenerateReportsJob() {
		if (!quartzInvoker.existJob(Constants.JOB_GENERATE_REPORTS, Constants.JOB_GENERATE_REPORTS)) {
			//quartzInvoker.deleteJob(Constants.JOB_GENERATE_REPORTS, Constants.JOB_GENERATE_REPORTS);
			log.debug (JOB + Constants.JOB_GENERATE_REPORTS + NO_EXISTE_SE_CREA);
			quartzInvoker.scheduleJobCron(null, GenerateReportsJob.class,
					Constants.JOB_GENERATE_REPORTS, Constants.JOB_GENERATE_REPORTS,
					Constants.JOB_GENERATE_REPORTS_EVERY_DAY);
		} else {
			log.debug (JOB + Constants.JOB_GENERATE_REPORTS + YA_EXISTÍA);
		}
	}
	
	/**
	 * Método que lanza la tarea programada para eliminar las peticiones de interfaz generica
	 * anuladas 'A' desde el estado alta 'N'
	 */
	/**
	 * Método que pasa las peticiones del mismo día pero del año anterior al histórico
	 */
//	private void executeRequestToHistoricJob() {
//		quartzInvoker.deleteJob(Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC);
//		if(applicationBO.historicoActivado()) {
//			if (!quartzInvoker.existJob(Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC)) {
//				log.debug ("Creando job: ".concat(Constants.JOB_REQUESTS_TO_HISTORIC));
//				
//				//Se saca el parametro de bd y se calcula la expresion cron para lanzar el job
//				String expresionCron = applicationBO.crearExpresionCronConParametroDeSistema(Constants.C_PARAMETER_JOBS_JOB_HISTORICO);
//				
//				quartzInvoker.scheduleJobCron(null, RequestsToHistoricJob.class,
//						Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC,
//						expresionCron);
//			} else {
//				log.debug (JOB.concat( Constants.JOB_REQUESTS_TO_HISTORIC).concat(YA_EXISTÍA));
//			}
//		}
//	}

	private void executeDir3Job() {
		log.debug("Se creara el job "+Constants.JOB_ORGANISM_DIR3);
		quartzInvoker.deleteJob(Constants.JOB_ORGANISM_DIR3, Constants.JOB_ORGANISM_DIR3);
		if (!quartzInvoker.existJob(Constants.JOB_ORGANISM_DIR3, Constants.JOB_ORGANISM_DIR3)) {
			//quartzInvoker.deleteJob(Constants.JOB_DOCELWEB_CLEANER, Constants.JOB_DOCELWEB_CLEANER);
			log.debug (JOB + Constants.JOB_ORGANISM_DIR3 + NO_EXISTE_SE_CREA);
			quartzInvoker.scheduleJobCron(null, Dir3Job.class,
					Constants.JOB_ORGANISM_DIR3, Constants.JOB_ORGANISM_DIR3,
					Constants.JOB_ORGANISM_DIR3_EVERY_DAY);
		} else {
			log.debug (JOB + Constants.JOB_ORGANISM_DIR3 + YA_EXISTÍA);
		}
	}

	private void executePrincipalJob() {
		log.debug("Se creara el job principalPF");
		quartzInvoker.deleteJob(Constants.JOB_PRINCIPAL, Constants.JOB_PRINCIPAL);
		if (!quartzInvoker.existJob(Constants.JOB_PRINCIPAL, Constants.JOB_PRINCIPAL)) {
			//quartzInvoker.deleteJob(Constants.JOB_DOCELWEB_CLEANER, Constants.JOB_DOCELWEB_CLEANER);
			log.debug (JOB.concat( Constants.JOB_PRINCIPAL).concat(NO_EXISTE_SE_CREA));
			quartzInvoker.scheduleJobCron(null, JobPrincipalQuartz.class,
					Constants.JOB_PRINCIPAL, Constants.JOB_PRINCIPAL,
					"0 0/20/40 * * * ?");
		} else {
			log.debug (JOB.concat(Constants.JOB_PRINCIPAL).concat(YA_EXISTÍA));
		}
	}
	
	
	@Transactional(readOnly=false)
	private void loadInitConfiguration () {
		long inicio = System.currentTimeMillis();
		//Obtenemos todos los tipos de documentos vigentes sin aplicacion asociada
		documentTypePfirmaList = applicationBO.queryDocumentTypePfirma();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryDocumentTypePfirma");

		//Obtenemos el tipo de documento gen&eacute;rico (tipo de documento general) sin aplicacion asociada
		documentTypeDefault = applicationBO.queryDocumentTypeGeneric();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryDocumentTypeGeneric");

		//Obtenemos la aplicacion portafirmas
		applicationPfirma = applicationBO.queryApplicationPfirma();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryApplicationPfirma");

		//Obtenemos la aplicación de portafirmas con firma PADES
		applicationPfirmaPADES = applicationBO.queryApplicationPfirmaPADES();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryApplicationPfirmaPADES");
		
		//Obtenemos la aplicación de portafirmas con firma XADES
		applicationPfirmaXADES = applicationBO.queryApplicationPfirmaXADES();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryApplicationPfirmaXADES");
		
		//Obtenemos la aplicación de portafirmas con firma XADES ENVELOPED
		applicationPfirmaXADESEnveloped = applicationBO.queryApplicationPfirmaXADESENVELOPED();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryApplicationPfirmaXADESENVELOPED");
		
		//Obtenemos la aplicación de portafirmas con firma XADES
		applicationPfirmaCADES = applicationBO.queryApplicationPfirmaCADES();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryApplicationPfirmaCADES");
		
		//Obtenemos un  Mapa de etiquetas del tipo 'ESTADO'
		stateTags = applicationBO.queryStateTags();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryStateTags");
		
		//Obtenemos un  Mapa de etiquetas del tipo 'SISTEMA'
		systemTags = applicationBO.querySystemTags();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "querySystemTags");
		
		// Obtenemos la lista de configuracion de los servidores existentes
		configurationListDefault = applicationBO.queryConfigurationList();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryConfigurationList");
		
		//Creamos un objeto comparador para el campo configuration
		ListComparador comparador = new ListComparador("configuration", 1);
		//ordenamos la lista de configuracion de los servidores existentes
		Collections.sort(configurationListDefault, comparador);
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "ListComparador");
		
		//Obtenemos el parametro de configuracion de LOGIN.DEBUG (Controla si se permite el acceso con usuario y clave)
		debugParameter = applicationBO.queryDebug();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryDebug");
		
		//Obtenemos el parametro de configuracion de LOGIN.LDAP (Indica si se permite la autenticacion mediante LDAP)
		ldapParameter = applicationBO.queryLdap();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryLdap");

		// Otenemos un mapa con los par&aacute;metros Load LDAP login parameters
		ldapLoginParameters = applicationBO.queryLDAPParameterList();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryLDAPParameterList");

		// Obtenemos una lista con los par&aacute;metros de tipo "SERVIDOR"
		serverParameterDefaultList = applicationBO.queryServerParameterList();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryServerParameterList");
		
		//Obtenemos el primer par&aacute;metro de la lista y lo almacenamos en serverParameterDefault
		if (serverParameterDefaultList != null
				&& !serverParameterDefaultList.isEmpty()) {
			serverParameterDefault = (PfParametersDTO) serverParameterDefaultList
					.get(0);
		} else {
			serverParameterDefault = new PfParametersDTO();
		}
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "serverParameterDefaultList");
		
		// Obtenemos una lista con todos los perfiles de la aplicacion excepto los de tipo 'REDACCARGO'
		profileDefaultList = applicationBO.queryProfileList();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryProfileList");
		
		// Obtenemos una lista con todos los perfiles de la aplicacion excepto los de tipo 'REDACCARGO', 'ADMIN' y 'WEBSERVICE'
		profileDefaultListAdmProvince = applicationBO.queryAdminProvinceProfileList();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryAdminProvinceProfileList");
		
		// obtenemos el par&aacute;metro de password para la autenticacion usuario/contrase&ntilde;a
		//par&aacute;metro 'USUARIO.PASSWORD' y tipo 'LOGIN'
		passwordParameter = applicationBO.queryPasswordParameter();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryPasswordParameter");
		
		// Obtenemos el parametro id para la utenticacion LDAP
		//par&aacute;metro 'USUARIO.LDAP.IDATRIBUTO' y tipo 'LOGIN'
		ldapIdParameter = applicationBO.queryLdapIdParameter();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryLdapIdParameter");
		
		//cargamos los par&aacute;metros del tema de la aplicacion
		reLoadTheme();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "reLoadTheme");

		// Obtenemos la lista jobs validos, es la lista de usuarios de tipo 'CARGO' que esten vigentes 'S'
		defaultValidJobList = applicationBO.queryValidJobWithProvinceList();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryValidJobWithProvinceList");

		// cargamos los par&aacute;metros de noticias
		//par&aacute;metro de email
		emailNoticeParameter = applicationBO.queryEmailNoticeParameter();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryEmailNoticeParameter");
		
		//par&aacute;metro de sms
		smsNoticeParameter = applicationBO.querySMSNoticeParameter();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "querySMSNoticeParameter");
		
		//parámetro de CSV activado
		csvActivatedParameter = applicationBO.queryCSVActivated();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryCSVActivated");
		
		// parámetro de justificante de firma activado
		reportActivatedParameter = applicationBO.queryReportActivated();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryReportActivated");
		
		// parámetro que indica que está activa la opción de visualizar documentos previamente firmados
		viewPreSignActivatedParameter = applicationBO.queryViewPreSignActivated();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryViewPreSignActivated");
		
		// parámetro que indica que está activa la opición de visualizar documentos TCN
		viewTCNActivatedParameter = applicationBO.queryViewTCNActivated();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryViewTCNActivated");

		
		// parámetro que indica si mostrar o no opciones de visibilidad en el panel de administración
		showVisibilityOptions = applicationBO.queryShowVisibilityOptions();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "queryShowVisibilityOptions");
		
		// Carga el parámetro de configuración de firma de informes, Indica si se firman los informes de firma tras ser generados (S/N)
		signReportParameter = applicationBO.querySignReportParameter();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "querySignReportParameter");
		
		// Configuración del almacén de confianza
		configManagerBO.setupTrustStore();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "setupTrustStore");

		// Carga el parámetro de ruta de acceso a Cl@ve
		cargaRutaAccesoAConfiguracionClave();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "cargaRutaAccesoAConfiguracionClave");
		
		configurarProperties();
		inicio = imprimirDuracion(inicio, System.currentTimeMillis(), "configurarProperties");
		
		validateSign = applicationBO.getValidateSignParameter();
		imprimirDuracion(inicio, System.currentTimeMillis(), "getValidateSignParameter");
		
	}

	public ConfigManagerBO getConfigManagerBO() {
		return configManagerBO;
	}

	public void setConfigManagerBO(ConfigManagerBO configManagerBO) {
		this.configManagerBO = configManagerBO;
	}

	public boolean getValidateSign() {
		boolean validateSign = false;
		if (this.validateSign != null
				&& this.validateSign.getTvalue().equals(Constants.C_YES)) {
			validateSign = true;
		}
		return validateSign;
	}

	/**
	 * Carga la ruta de acceso donde encontrar los ficheros de Clave 
	 */
	private void cargaRutaAccesoAConfiguracionClave() {
		if(Util.esVacioONulo(System.getProperty(Constants.CLAVE_CONFIGPATH))) {
			System.setProperty(Constants.CLAVE_CONFIGPATH,(System.getProperty(Constants.SGTIC_CONFIGPATH)).concat("/properties/clave"));		
		}
		log.info(Constants.CLAVE_CONFIGPATH + ": " + System.getProperty(Constants.CLAVE_CONFIGPATH));
	}
	
	private void configurarProperties() {
		
		fireProperties.setProperty(Constants.FIRE_KEYSTORE_FILE, Constants.PATH_CERTIFICADOS + fireProperties.getProperty(Constants.FIRE_KEYSTORE_FILE));
		fireProperties.setProperty(Constants.FIRE_TRUSTSTORE_FILE, Constants.PATH_CERTIFICADOS + fireProperties.getProperty(Constants.FIRE_TRUSTSTORE_FILE));

		interfazGenericaProperties.setProperty(Constants.MERLIN_TRUSTSTORE_FILE, Constants.PATH_CERTIFICADOS + interfazGenericaProperties.getProperty(Constants.MERLIN_TRUSTSTORE_FILE));
		interfazGenericaProperties.setProperty(Constants.MERLIN_KEYSTORE_FILE, Constants.PATH_CERTIFICADOS + interfazGenericaProperties.getProperty(Constants.MERLIN_KEYSTORE_FILE));
		
		afirmaProperties.setProperty(Constants.MERLIN_KEYSTORE_FILE, Constants.PATH_CERTIFICADOS + afirmaProperties.getProperty(Constants.MERLIN_KEYSTORE_FILE));
	}

	public List<AbstractBaseDTO> getDocumentTypePfirmaList() {
		return documentTypePfirmaList;
	}

	public void setDocumentTypePfirmaList(
			List<AbstractBaseDTO> documentTypePfirmaList) {
		this.documentTypePfirmaList = documentTypePfirmaList;
	}

	public PfDocumentTypesDTO getDocumentTypeDefault() {
		return documentTypeDefault;
	}

	public void setDocumentTypeDefault(PfDocumentTypesDTO documentTypeDefault) {
		this.documentTypeDefault = documentTypeDefault;
	}

	public PfApplicationsDTO getApplicationPfirma() {
		return applicationPfirma;
	}

	public void setApplicationPfirma(PfApplicationsDTO applicationPfirma) {
		this.applicationPfirma = applicationPfirma;
	}

	public PfApplicationsDTO getApplicationPfirmaPADES() {
		return applicationPfirmaPADES;
	}

	public void setApplicationPfirmaPADES(PfApplicationsDTO applicationPfirmaPADES) {
		this.applicationPfirmaPADES = applicationPfirmaPADES;
	}

	public PfApplicationsDTO getApplicationPfirmaXADES() {
		return applicationPfirmaXADES;
	}

	public void setApplicationPfirmaXADES(PfApplicationsDTO applicationPfirmaXADES) {
		this.applicationPfirmaXADES = applicationPfirmaXADES;
	}

	public PfApplicationsDTO getApplicationPfirmaCADES() {
		return applicationPfirmaCADES;
	}
	
	public void setApplicationPfirmaCADES(PfApplicationsDTO applicationPfirmaCADES) {
		this.applicationPfirmaCADES = applicationPfirmaCADES;
	}

	public List<AbstractBaseDTO> getConfigurationListDefault() {
		return configurationListDefault;
	}

	public void setConfigurationListDefault(
			List<AbstractBaseDTO> configurationListDefault) {
		this.configurationListDefault = configurationListDefault;
	}

	
	public boolean getDebug() {
		boolean debug = false;
		if (this.debugParameter != null
				&& this.debugParameter.getTvalue().equals(Constants.C_YES)) {
			debug = true;
		}
		return debug;
	}

	public boolean getLdap() {
		boolean ldap = false;
		if (this.ldapParameter != null
				&& this.ldapParameter.getTvalue().equals(Constants.C_YES)) {
			ldap = true;
		}
		return ldap;
	}

	public boolean getSignReport() {
		boolean signReport = false;
		if (this.signReportParameter != null
				&& this.signReportParameter.getTvalue().equals(Constants.C_YES)) {
			signReport = true;
		}
		return signReport;
	}
	/**
	 * Especifica si el email est&aacute; activado para notificaciones o no
	 * @return si el email est&aacute; activo para notificaciones
	 */
	public boolean getEmail() {
		boolean email = false;
		if (this.emailNoticeParameter != null
				&& this.emailNoticeParameter.getTvalue()
						.equals(Constants.C_YES)) {
			email = true;
		}
		return email;
	}
	/**
	 * Especifica si el sms est&aacute; activado para notificaciones o no
	 * @return si el sms est&aacute; activo para notificaciones o no
	 */
	public boolean getSMS() {
		boolean sms = false;
		if (this.smsNoticeParameter != null
				&& this.smsNoticeParameter.getTvalue().equals(Constants.C_YES)) {
			sms = true;
		}
		return sms;
	}
	
	/**
	 * Especifica si está activada la funcionalidad del CSV de las firmas.
	 */
	public boolean getCsvActivated () {
		boolean csv = false;
		if (this.csvActivatedParameter != null 
				&& this.csvActivatedParameter.getTvalue().equals(Constants.C_YES)) {
			csv = true;
		}
		return csv;
	}
	
	/**
	 * Especifica si está activada la funcionalidad de los justificantes de firma.
	 */
	public boolean getReportActivated () {
		boolean report = false;
		if (this.reportActivatedParameter != null 
				&& this.reportActivatedParameter.getTvalue().equals(Constants.C_YES)) {
			report = true;
		}
		return report;
	}
	
	public boolean getViewPreSignActivated() {
		return this.viewPreSignActivatedParameter != null
			 && this.viewPreSignActivatedParameter.getTvalue().equals(Constants.C_YES);
	}
	
	public boolean getViewTCNActivated() {
		return this.viewTCNActivatedParameter != null
			 && this.viewTCNActivatedParameter.getTvalue().equals(Constants.C_YES);
	}

	/**
	 * Comprueba en caliente si el timestamp está o no activado
	 */
	public boolean getTimestampActivated () {
		boolean timestamp = false;
		
		PfConfigurationsParameterDTO param = applicationBO.queryTimestampActivatedAfirma();
		if (param != null && (param.getTvalue().equals(Constants.C_YES))) {
				timestamp = true;
		}
		
		if (!timestamp) {
			param = applicationBO.queryTimestampActivatedEEUTIL();
			if (param != null && (param.getTvalue().equals(Constants.C_YES)) ){
					timestamp = true;
			}			
		}
		
		return timestamp;
	}
	
	/** 
	 * Comprueba si está activada la opción de mostrar opciones de visibilidad de usuarios.
	 * @return
	 */
	public boolean getShowVisibilityOptions () {
		boolean show = false;
		if (this.showVisibilityOptions != null 
				&& this.showVisibilityOptions.getTvalue().equals(Constants.C_YES)) {
			show = true;
		}
		return show;
	}

	/**
	 * Método que devuelve el valor de configuración de tipos mime aceptados
	 * @return Valor de configuración de tipos mime aceptados
	 */
	public PfConfigurationsParameterDTO getAcceptedMimeTypes() {
		return applicationBO.getAcceptedMimeTypes();
	}

	/**
	 * Método que devuelve el valor de configuración de extensiones aceptadas
	 * @return Valor de configuración de textensiones aceptadas
	 */
	public PfConfigurationsParameterDTO getAcceptedFileExtensions() {
		return applicationBO.getAcceptedFileExtensions();
	}

	/**
	 * Método que devuelve el valor de configuración de extensiones aceptadas
	 * @return Valor de configuración de textensiones aceptadas
	 */
	public PfConfigurationsParameterDTO getDocumentMaxSize() {
		return applicationBO.queryMaxDocumentSizeParameter();
	}

	public PfParametersDTO getServerParameterDefault() {
		return serverParameterDefault;
	}

	public void setServerParameterDefault(PfParametersDTO serverParameterDefault) {
		this.serverParameterDefault = serverParameterDefault;
	}

	public List<AbstractBaseDTO> getServerParameterDefaultList() {
		return serverParameterDefaultList;
	}

	public void setServerParameterDefaultList(
			List<AbstractBaseDTO> serverParameterDefaultList) {
		this.serverParameterDefaultList = serverParameterDefaultList;
	}

	public List<AbstractBaseDTO> getProfileDefaultList() {
		return profileDefaultList;
	}

	public void setProfileDefaultList(List<AbstractBaseDTO> profileDefaultList) {
		this.profileDefaultList = profileDefaultList;
	}

	public PfParametersDTO getPasswordParameter() {
		return passwordParameter;
	}

	public void setPasswordParameter(PfParametersDTO passwordParameter) {
		this.passwordParameter = passwordParameter;
	}

	public StringBuilder getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(StringBuilder mailTemplate) {
		this.mailTemplate = mailTemplate;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getInboxNRows() {
		return inboxNRows;
	}

	public void setInboxNRows(String inboxNRows) {
		this.inboxNRows = inboxNRows;
	}

	public List<AbstractBaseDTO> getDefaultValidJobList() {
		return defaultValidJobList;
	}

	public void setDefaultValidJobList(List<AbstractBaseDTO> validJobList) {
		this.defaultValidJobList = validJobList;
	}

	public PfConfigurationsParameterDTO getDebugParameter() {
		return debugParameter;
	}

	public void setDebugParameter(PfConfigurationsParameterDTO debugParameter) {
		this.debugParameter = debugParameter;
	}

	public PfConfigurationsParameterDTO getLdapParameter() {
		return ldapParameter;
	}

	public void setLdapParameter(PfConfigurationsParameterDTO ldapParameter) {
		this.ldapParameter = ldapParameter;
	}

	public Map<String, String> getLdapLoginParameters() {
		return ldapLoginParameters;
	}

	public void setLdapLoginParameters(Map<String, String> ldapLoginParameters) {
		this.ldapLoginParameters = ldapLoginParameters;
	}

	public PfParametersDTO getLdapIdParameter() {
		return ldapIdParameter;
	}

	public void setLdapIdParameter(PfParametersDTO ldapIdParameter) {
		this.ldapIdParameter = ldapIdParameter;
	}

	public Map<String, PfTagsDTO> getStateTags() {
		return stateTags;
	}

	public void setStateTags(Map<String, PfTagsDTO> stateTags) {
		this.stateTags = stateTags;
	}

	public Map<String, PfTagsDTO> getSystemTags() {
		return systemTags;
	}

	public void setSystemTags(Map<String, PfTagsDTO> systemTags) {
		this.systemTags = systemTags;
	}

	public PfConfigurationsParameterDTO getEmailNoticeParameter() {
		return emailNoticeParameter;
	}

	public void setEmailNoticeParameter(
			PfConfigurationsParameterDTO emailNoticeParameter) {
		this.emailNoticeParameter = emailNoticeParameter;
	}

	public PfConfigurationsParameterDTO getSmsNoticeParameter() {
		return smsNoticeParameter;
	}

	public void setSmsNoticeParameter(
			PfConfigurationsParameterDTO smsNoticeParameter) {
		this.smsNoticeParameter = smsNoticeParameter;
	}

	public void setSignReportParameter(
			PfConfigurationsParameterDTO signReportParameter) {
		this.signReportParameter = signReportParameter;
	}

	public PfConfigurationsParameterDTO getMaxDocumentSize() {
		return maxDocumentSize;
	}

	public void setMaxDocumentSize(PfConfigurationsParameterDTO maxDocumentSize) {
		this.maxDocumentSize = maxDocumentSize;
	}

	public PfApplicationsDTO getApplicationPfirmaXADESEnveloped() {
		return applicationPfirmaXADESEnveloped;
	}

	public void setApplicationPfirmaXADESEnveloped(
			PfApplicationsDTO applicationPfirmaXADESEnveloped) {
		this.applicationPfirmaXADESEnveloped = applicationPfirmaXADESEnveloped;
	}

	public List<AbstractBaseDTO> getProfileDefaultListAdmProvince() {
		return profileDefaultListAdmProvince;
	}

	public void setProfileDefaultListAdmProvince(
			List<AbstractBaseDTO> profileDefaultListAdmProvince) {
		this.profileDefaultListAdmProvince = profileDefaultListAdmProvince;
	}

	public PfConfigurationsParameterDTO getCsvActivatedParameter() {
		return csvActivatedParameter;
	}

	public void setCsvActivatedParameter(
			PfConfigurationsParameterDTO csvActivatedParameter) {
		this.csvActivatedParameter = csvActivatedParameter;
	}

	public PfConfigurationsParameterDTO getReportActivatedParameter() {
		return reportActivatedParameter;
	}

	public void setReportActivatedParameter(
			PfConfigurationsParameterDTO reportActivatedParameter) {
		this.reportActivatedParameter = reportActivatedParameter;
	}

	public PfConfigurationsParameterDTO getSignReportParameter() {
		return signReportParameter;
	}

	public void setShowVisibilityOptions(
			PfConfigurationsParameterDTO showVisibilityOptions) {
		this.showVisibilityOptions = showVisibilityOptions;
	}
}
