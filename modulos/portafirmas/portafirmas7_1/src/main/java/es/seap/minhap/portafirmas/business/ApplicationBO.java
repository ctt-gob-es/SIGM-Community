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

package es.seap.minhap.portafirmas.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.job.RequestsToHistoricJob;
import es.seap.minhap.portafirmas.utils.notice.service.EmailNoticeServiceJob;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;
import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.ConfigManagerBO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ApplicationBO {


	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private ConfigManagerBO configManagerBO;

	@Autowired
	private QuartzInvoker quartzInvoker;
	
	@Autowired
	UtilComponent utilComponent;

	private volatile HashMap<String, String> parametrosSIM = null;

	Logger log = Logger.getLogger(ApplicationBO.class);
	
	
	/**
	 * Recupera una lista de objetos de tipo documento con todos los tipos de documentos vigentes sin aplicaci&oacute;n asociada.
	 * @return la lista de objetos con todos los tipos de documentos vigentes sin aplicaci&oacute;n asociada.
	 */
	public List<AbstractBaseDTO> queryDocumentTypePfirma() {
		return baseDAO.queryListOneParameter("request.documentTypePfirma",
				null, null);
	}

	/**
	 * Recupera una lista de objetos de tipo accionFirmante con todas las acciones del firmante vigentes.
	 * @return la lista de objetos con todas las acciones del firmante vigentes.
	 */
	public List<AbstractBaseDTO> queryAccionFirmante() {
		return baseDAO.queryListOneParameter("request.accionFirmante",
				null, null);
	}
	
	/**
	 * Carga los par&aacute;metros de la aplicaci&oacute;n y los par&aacute;metros de las aplicaciones padre
	 * @param application la aplicaci&oacute;n
	 * @return el mapa 
	 */
	public Map<String, String> getMapaParametrosAplicacion(PfApplicationsDTO application) {
		//Mapa con los valores de los parÃ¡metros de la aplicaciÃ³n
		Map<String, String> mapaParametros = new HashMap<String, String>();
		getMapaParametrosAplicacion(application, mapaParametros);
		return mapaParametros;
	}

	/**
	 * Carga los par&aacute;metros de la aplicaci&oacute;n y los par&aacute;metros de las aplicaciones padre en el mapa que se pasa como par&aacute;metro,
	 * la clave es el c&oacute;digo de par&aacute;metro y el valor el valor del par&aacute;metro para la aplicaci&oacute;n
	 * @param application la aplicaci&oacute;n
	 * @param mapaParametros el mapa de par&aacute;metros
	 */
	private void getMapaParametrosAplicacion(PfApplicationsDTO application, Map<String, String> mapaParametros) {
		for (PfApplicationsParameterDTO param: application.getPfApplicationsParameters()) {
			//SÃ³lo cargamos un parÃ¡metro si no estÃ¡ ya incluido en el mapa. 			
			if (!mapaParametros.containsKey(param.getPfParameter().getCparameter())) {
				mapaParametros.put(param.getPfParameter().getCparameter(), param.getTvalue());
			}			
		}
		//DespuÃ©s llamamos a la aplicaciÃ³n padre para recoger mÃ¡s parÃ¡metros (SÃ³lo los que no estÃ¡n rellenos)
		if (application.getPfApplication() != null) {
			PfApplicationsDTO parent = (PfApplicationsDTO) 
											baseDAO.queryElementOneParameter("administration.applicationWithParameters",
																		  "primaryKey", application.getPfApplication().getPrimaryKey());			
			getMapaParametrosAplicacion(parent, mapaParametros);
		}
	}

	/**
	 * Recupera un objeto de tipo documento con el tipo de documento gen&eacute;rico (tipo de documento general) sin aplicaci&oacute;n asociada.
	 * @return el objeto tipo documento con el documento gen&eacute;rico sin aplicaci&oacute;n asociada.
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_DOCUMENTTYPE_GENERIC Valor del tipo de documento generico
	 */
	public PfDocumentTypesDTO queryDocumentTypeGeneric() {
		return (PfDocumentTypesDTO) baseDAO.queryElementOneParameter(
				"request.documentTypeId", "type",
				Constants.C_DOCUMENTTYPE_GENERIC);
	}

	/**
	 * Método que devuelve un tipo de documento a partir de su identificador
	 * @param docTypeId Identificador de tipo de documento
	 * @return Tipo de documento
	 */
	public PfDocumentTypesDTO queryDocumentTypeById(String docTypeId) {
		return (PfDocumentTypesDTO) baseDAO.queryElementOneParameter(
				"request.documentTypeId", "type", docTypeId);
	}

	/**
	 * Recupera un objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA'.
	 * @return el objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA'.
	 */
	public PfApplicationsDTO queryApplicationPfirma() {
		return (PfApplicationsDTO) baseDAO.queryElementOneParameter(
				"request.applicationPfirma", null, null);
	}

	/**
	 * Recupera un objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA PADES'.
	 * @return el objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA PADES'.
	 */
	public PfApplicationsDTO queryApplicationPfirmaPADES() {
		return (PfApplicationsDTO) baseDAO.queryElementOneParameter(
				"request.applicationPfirmaPADES", null, null);
	}

	public List<PfApplicationsDTO> queryApplicationVisibles() {
		List<PfApplicationsDTO> aplicacionesRetorno = new ArrayList<PfApplicationsDTO>();
		List<AbstractBaseDTO> returnDAO = baseDAO.queryListOneParameter("request.applicationPfirmaCombo", null, null);
		for(AbstractBaseDTO abd: returnDAO){
			aplicacionesRetorno.add((PfApplicationsDTO)abd);
		}
		return aplicacionesRetorno;
	}
	
	/**
	 * Recupera un objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA XADES'.
	 * @return el objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA XADES'.
	 */
	public PfApplicationsDTO queryApplicationPfirmaXADES() {
		return (PfApplicationsDTO) baseDAO.queryElementOneParameter(
				"request.applicationPfirmaXADES", null, null);
	}

	/**
	 * Recupera un objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA XADES ENVELOPED'.
	 * @return el objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA XADES ENVELOPED'.
	 */
	public PfApplicationsDTO queryApplicationPfirmaXADESENVELOPED() {
		return (PfApplicationsDTO) baseDAO.queryElementOneParameter(
				"request.applicationPfirmaXADESENVELOPED", null, null);
	}

	/**
	 * Recupera un objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA CADES'.
	 * @return el objeto de tipo aplicaciones con la aplicaci&oacute;n portafirmas 'PFIRMA CADES'.
	 */
	public PfApplicationsDTO queryApplicationPfirmaCADES() {
		return (PfApplicationsDTO) baseDAO.queryElementOneParameter(
				"request.applicationPfirmaCADES", null, null);
	}

	/**
	 * Recupera un  Mapa de etiquetas del tipo 'ESTADO'
	 * @return el mapa de etiquetas del tipo 'ESTADO'
	 */
	public Map<String, PfTagsDTO> queryStateTags() {
		Map<String, PfTagsDTO> result = new HashMap<String, PfTagsDTO>();
		List<AbstractBaseDTO> allStateTags = baseDAO.queryListOneParameter(
				"request.stateTags", null, null);
		for (Iterator<AbstractBaseDTO> iterator = allStateTags.iterator(); iterator
				.hasNext();) {
			PfTagsDTO tag = (PfTagsDTO) iterator.next();
			result.put(tag.getCtag(), tag);
		}
		return result;
	}

	/**
	 * Recupera un Mapa de etiquetas del tipo 'SISTEMA'
	 * @return el mapa de etiquetas del tipo 'SISTEMA'
	 */
	public Map<String, PfTagsDTO> querySystemTags() {
		Map<String, PfTagsDTO> result = new HashMap<String, PfTagsDTO>();
		List<AbstractBaseDTO> allSystemTags = baseDAO.queryListOneParameter(
				"request.systemTags", null, null);
		for (Iterator<AbstractBaseDTO> iterator = allSystemTags.iterator(); iterator
				.hasNext();) {
			PfTagsDTO tag = (PfTagsDTO) iterator.next();
			result.put(tag.getCtag(), tag);
		}
		return result;
	}

	/**
	 * Devuelve un mapa con los parametros de configuraci&oacute;n de tipo 'PROXY'
	 * @return El mapa con los par&aacute;metros de configuraci&oacute;n
	 */
	public Map<String, String> queryProxyParameters() {		
		List<AbstractBaseDTO> proxyParameters = baseDAO.queryListOneParameter("administration.proxyParameters", null, null);
		Map<String, String> result = ConfigurationUtil.convierteListaParametrosConfiguracionEnMapa(proxyParameters);
		return result;
	}
		
	/**
	 * recupera una lista de objetos de tipo configuraci&oacute;n con todos los servidores existentes.
	 * @return la lista de objetos de tipo configuraci&oacute;n con todos los servidores existentes
	 * @see es.seap.minhap.portafirmas.dao.BaseDAO#queryElementOneParameter(String, String, Object)
	 */
	public List<AbstractBaseDTO> queryConfigurationList() {
		return baseDAO.queryListOneParameter(
				"administration.serverConfigurationAll", null, null);
	}

	/**
	 * M&eacute;todo que recupera de la BD una lista con todos los par&aacute;metros de aplicaci&oacute;n.
	 * @return Listado de par&aacute;metros de aplicaci&oacute;n.
	 */
	public List<AbstractBaseDTO> queryParameterListAll() {
		return baseDAO.queryListMoreParameters(
				"administration.applicationParametersAll", null);
	}

	/**
	 * Recupera una lista de objetos par&aacute;metros de tipo "SERVIDOR".
	 * @return la lista de objetos par&aacute;metros de tipo "SERVIDOR".
	*/
	public List<AbstractBaseDTO> queryServerParameterList() {
		return baseDAO.queryListMoreParameters(
				"administration.serverParametersAll", null);
	}

	/**
	 * recupera el par&aacute;metro de configuraci&oacute;n de tipo DEBUG,
	 * par&aacute;metro de tipo 'LOGIN.DEBUG' que Controla si se permite el acceso con usuario y clave.
	 * @return el par&aacute;metro de configuraci&oacute;n de tipo DEBUG.
	 * @see es.seap.minhap.portafirmas.dao.BaseDAO#queryElementOneParameter(String, String, Object)
	 */
	public PfConfigurationsParameterDTO queryDebug() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.debugParameter", null, null);
	}
	/**
	 * recupera EL par&aacute;metro de configuraci&oacute;n de tipo LDAP,
	 * par&aacute;metro de tipo 'LOGIN.LDAP' que Indica si se permite la autenticaci&oacute;n mediante LDAP.
	 * @return el par&aacute;metro de configuraci&oacute;n de tipo LDAP.
	 * @see es.seap.minhap.portafirmas.dao.BaseDAO#queryElementOneParameter(String, String, Object)
	 */
	public PfConfigurationsParameterDTO queryLdap() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.ldapParameter", null, null);
	}
	/**
	 * Recupera una lista de objetos de tipo perfiles con todos los perfiles de la aplicaci&oacute;n excepto los de tipo 'REDACCARGO'.
	 * @return la lista de objetos de tipo perfiles
	 */
	public List<AbstractBaseDTO> queryProfileList() {
		return baseDAO.queryListMoreParameters("administration.profilesAll", null);
	}
	/**
	 * Recupera una lista de objetos de tipo perfiles con todos los perfiles de la aplicaci&oacute;n excepto los de tipo 'REDACCARGO'.
	 * @return la lista de objetos de tipo perfiles
	 */
	public List<AbstractBaseDTO> queryAdminProvinceProfileList() {
		return baseDAO.queryListMoreParameters("administration.profilesAdminProvince", null);
	}
	/**
	 * Recupera una lista de objetos de tipo perfiles con todos los perfiles de la aplicaci&oacute;n excepto los de tipo 'REDACCARGO'.
	 * @return la lista de objetos de tipo perfiles
	 */
	public List<AbstractBaseDTO> queryAdminCAIDProfileList() {
		return baseDAO.queryListMoreParameters("administration.profilesAdminCAID", null);
	}

	/**
	 * Recupera los perfiles en función del perfil del usuario logado
	 * 
	 * @param userLogDTO
	 * @return
	 */
	public List<AbstractBaseDTO> queryProfileList(PfUsersDTO userLogDTO) {
		List<AbstractBaseDTO> profiles = null;
		if (userAdmBO.isAdministrator(userLogDTO.getPfUsersProfiles())) {
			profiles = queryProfileList();
		} else if (userAdmBO.isAdminCAID(userLogDTO.getPfUsersProfiles())) {
			profiles = queryAdminCAIDProfileList();
		} else {
			profiles = queryAdminProvinceProfileList();
		}
		return profiles;
	}
	/**
	 * Recupera la lista de Job validos con su provincia
	 * @return lista de usuarios de tipo 'CARGO' que esten vigentes 'S'
	 * @see 
	 */
	public List<AbstractBaseDTO> queryValidJobWithProvinceList() {
		return baseDAO.queryListMoreParameters("administration.jobAdmProvinceAll", null);
	}

	/**
	 * Recupera la lista de Job validos
	 * @return lista de usuarios de tipo 'CARGO' que esten vigentes 'S'
	 * @see 
	 */
	public List<AbstractBaseDTO> queryValidJobList() {
		return baseDAO.queryListMoreParameters("administration.jobAdmValid", null);
	}
	/**
	 * Recupera un objeto de tipo parametros con el password para la autenticaci&oacute;n usuario/contrase&ntilde;a
	 * par&aacute;metro 'USUARIO.PASSWORD' y tipo 'LOGIN'.
	 * @return el objeto de tipo parametros, par&aacute;metro 'USUARIO.PASSWORD' y tipo 'LOGIN'.
	 */
	public PfParametersDTO queryPasswordParameter() {
		return (PfParametersDTO) baseDAO.queryElementOneParameter(
				"administration.userPasswordParameter", null, null);
	}
	/**
	 * Recupera un objeto de tipo parametros con el parametro id para la utenticaci&oacute;n LDAP
	 * par&aacute;metro 'USUARIO.LDAP.IDATRIBUTO' y tipo 'LOGIN'.
	 * @return el objeto de tipo parametros, par&aacute;metro 'USUARIO.LDAP.IDATRIBUTO' y tipo 'LOGIN'.
	 */
	public PfParametersDTO queryLdapIdParameter() {
		return (PfParametersDTO) baseDAO.queryElementOneParameter(
				"administration.ldapIdParameter", null, null);
	}

	/**
	 * Recupera un objeto de tipo parámetro con la configuración de validación de firma
	 * @return El objeto de tipo parámetro
	 */
	public PfParametersDTO queryValidateSignParameter() {
		return (PfParametersDTO) baseDAO.queryElementOneParameter(
				"administration.validateSignParameter", null, null);
	}

	/**
	 * Recoge los datos del fichero de plantilla y los vuelca en un buffer
	 * @return el buffer con los datos del fichero
	 * @see es.seap.minhap.portafirmas.utils.Constants#TEMPLATE_FOLDER Directorio de plantilla
	 * @see es.seap.minhap.portafirmas.utils.Constants#TEMPLATE_FILE_NAME Archivo de plantilla
	 */
	public StringBuilder loadMailTemplate() {
		StringBuilder template = null;
		// Get file path
		String path = EmailNoticeServiceJob.class.getResource(
				Constants.TEMPLATE_FOLDER).toString();
		if (path != null && !path.equals("")) {
			// delete "file:/" string get from getResource method
			path = path.replaceAll("file:/", "");
			// replace %20 with space
			path = path.replaceAll("%20", " ");
			// add / at first to be recognized at linux
			path = "/" + path;
		}
		// Read file
		String filePath = path + "/" + Constants.TEMPLATE_FILE_NAME;
		try {
			template = Util.readFileAsString(filePath);
		} catch (IOException e) {
			log.error("Error loading mail template", e);
		}
		return template;
	}
	/**
	 * recupera un mapa con los par&aacute;metros de configuraci&oacute;n de tipo LDAP,
	 * par&aacute;metros tipo 'LOGIN.LDAP.*' que indica si se permite la autenticaci&oacute;n mediante LDAP
	 * y 'USUARIO.LDAP.*' que es el nombre del atributo uid en servidor LDAP.
	 * @return el mapa con los par&aacute;metros de configuraci&oacute;n de tipo LDAP
	 * @see es.seap.minhap.portafirmas.utils.ConfigurationUtil#convierteListaParametrosConfiguracionEnMapa(java.util.Collection)
	 * @see es.seap.minhap.portafirmas.dao.BaseDAO#queryListOneParameter(String, String, Object)
	 */
	public Map<String, String> queryLDAPParameterList() {
		List<AbstractBaseDTO> configurationParameters = baseDAO.queryListOneParameter("administration.ldapParameters", null, null);
		// Convert list to HashMap
		Map<String, String> paramMap = ConfigurationUtil.convierteListaParametrosConfiguracionEnMapa(configurationParameters);
		return paramMap;
	}
	
	/**
	 * recupera el par&aacute;metro de configuraci&oacute;n de notificaci&oacute;n de correo.
	 * @return el par&aacute;metro de configuraci&oacute;n de notificaci&oacute;n de correo.
	 * @see es.seap.minhap.portafirmas.utils.Constants#EMAIL_NOTICE_PARAM
	 */
	public PfConfigurationsParameterDTO queryEmailNoticeParameter() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.EMAIL_NOTICE_PARAM);
	}
	/**
	 * recupera el par&aacute;metro de configuraci&oacute;n de notificaci&oacute;n de sms.
	 * @return el par&aacute;metro de configuraci&oacute;n de notificaci&oacute;n de sms.
	 * @see es.seap.minhap.portafirmas.utils.Constants#SMS_NOTICE_PARAM
	 */
	public PfConfigurationsParameterDTO querySMSNoticeParameter() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.SMS_NOTICE_PARAM);
	}
	/**
	 * recupera el par&aacute;metro de configuraci&oacute;n de firma de informes, indica si se firman los informes de firma tras ser generados (S/N).
	 * @return el par&aacute;metro de configuraci&oacute;n de firma de informes.
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGNREPORT_PARAM
	 */
	public PfConfigurationsParameterDTO querySignReportParameter() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.SIGNREPORT_PARAM);
	}

	/**
	 * Recupera el parámetro de configuración que delimita el tamaño de los documentos
	 * @return El parámetro de configuración que delimita el tamaño de los documentos
	 * @see es.seap.minhap.portafirmas.utils.Constants#MAX_DOCUMENT_SIZE
	 */
	public PfConfigurationsParameterDTO queryMaxDocumentSizeParameter() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.MAX_DOCUMENT_SIZE);
	}
	
	/**
	 * Recupera el parámetro de configuración de indica si hay que validar las firmas obtenidas
	 * @return
	 */
	public PfConfigurationsParameterDTO getValidateSignParameter() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.VALIDATE_SIGN);
	}
	/**
	 * Recupera el parámetro de configuración que indica si se muestran o no
	 * las opciones de visibilidad en el panel de administración de usuarios y cargos
	 * @return el parámetro de configuración que indica si se muestran o no
	 * las opciones de visibilidad en el panel de administración de usuarios y cargos
	 * @see es.seap.minhap.portafirmas.utils.Constants#SHOW_VISIBILITY_OPTIONS
	 */
	public PfConfigurationsParameterDTO queryShowVisibilityOptions () {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.SHOW_VISIBILITY_OPTIONS);
	}
	
	
	/**
	 * Recupera el parámetro de configuración a nivel de la aplicación PFIRMA
	 * que determina si está activada la funcionalidad de CSV o no
	 * @return El parámetro de configuración que que determina si está activada la funcionalidad de CSV o no
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_EEUTIL_CSV_ACTIVE*/
	public PfConfigurationsParameterDTO queryCSVActivated () {
		return getConfigParameter(Constants.C_PARAMETER_EEUTIL_CSV_ACTIVE);
	}
	
	/**
	 * Recupera el parámetro de configuración a nivel de la aplicación PFIRMA
	 * que determina si está activada la funcionalidad de generar justificante de firma o no
	 * @return El parámetro de configuración que que determina si está activada la funcionalidad de generar justificante de firma o no
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_EEUTIL_REPORT_ACTIVE*/
	public PfConfigurationsParameterDTO queryReportActivated () {
		return getConfigParameter(Constants.C_PARAMETER_EEUTIL_REPORT_ACTIVE);
	}
	
	/**
	 * Recupea el parámetro de configuracón a nive de la aplicación PFIRMA que
	 * determina si está activa la funcionalidad de visualizar documentos previamente firmados
	 * @return
	 */
	public PfConfigurationsParameterDTO queryViewPreSignActivated() {
		return getConfigParameter(Constants.C_PARAMETER_EEUTIL_VIS_PREFIRMA_ACTIVE);
	}

	/**
	 * Recupea el parámetro de configuracón a nive de la aplicación PFIRMA que
	 * determina si está activa la funcionalidad de visualizar documentos TCN
	 * @return
	 */
	public PfConfigurationsParameterDTO queryViewTCNActivated() {
		return getConfigParameter(Constants.C_PARAMETER_EEUTIL_VISUALIZAR_TCN_ACTIVO);
	}
	
	/**
	 * Recupera el parámetro de configuración a nivel de la aplicación PFIRMA
	 * que determina si está activada la funcionalidad de añadir a la firma el sello de tiempo o no a traves de la plataforma eeutil.
	 * @return El parámetro de configuración que que determina si está activada la funcionalidad de añadir el sello de tiempo a la firma o no
	 * a traves de la plataforma eeutil.
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_EEUTIL_SELLO_ACTIVE*/
	public PfConfigurationsParameterDTO queryTimestampActivatedEEUTIL () {
		return getConfigParameter(Constants.C_PARAMETER_EEUTIL_SELLO_ACTIVE);
	}
	
	/**
	 * Recupera el parámetro de configuración a nivel de la aplicación PFIRMA
	 * que determina si está activada la funcionalidad de añadir a la firma el sello de tiempo o no a través
	 * de la plataforma Afirma.
	 * @return El parámetro de configuración que que determina si está activada la funcionalidad de añadir el sello de tiempo a la firma o no
	 * a traves de la plataforma Afirma.
	 * @see es.seap.minhap.portafirmas.utils.Afirma5Constantes#FIRMA_SELLO_ACTIVO*/
	public PfConfigurationsParameterDTO queryTimestampActivatedAfirma () {
		return getConfigParameter(Afirma5Constantes.FIRMA_SELLO_ACTIVO);
	}
	
	/**
	 * @param cParam
	 * @return
	 */
	private PfConfigurationsParameterDTO getConfigParameter(String cParam) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("cApp", Constants.APP_PFIRMA_CODE);
		parameters.put("cParam", cParam);		
		return (PfConfigurationsParameterDTO) baseDAO.queryElementMoreParameters("administration.applicationParameterConfiguration", parameters);
	}

	/**
	 * Método que obtiene una lista con todos los tipos Mime admitidos por la aplicación
	 * @return Lista de tipos MIME
	 */
	public PfConfigurationsParameterDTO getAcceptedMimeTypes() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.ACCEPTED_MIME_TYPES);
	}

	/**
	 * Método que obtiene una lista con todos las extensiones de fichero admitidas por la aplicación
	 * @return Lista de extaensiones de fichero
	 */
	public PfConfigurationsParameterDTO getAcceptedFileExtensions() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.ACCEPTED_FILE_EXTENSIONS);
	}
	
	public PfConfigurationsParameterDTO getSignWaitTime() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.SIGN_WAIT_TIME);
	}
	
	/**
	 * Método que obtiene el entorno donde está desplegada la aplicación.
	 * @return Entorno
	 */
	public PfConfigurationsParameterDTO getEnvironment() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementMoreParameters(
				"administration.queryEnvironment", null);
	}
	
	
	/**
	 * Recupera el parámetro de configuración que activa las validaciones automáticas sobre documentos subidos
	 * @return El parámetro de configuración que activa las validaciones automáticas sobre documentos subidos
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_VALIDATION_AUTOMATICA
	 */
	public boolean isValidacionesAutomaticas() {
		return isActiveParameter(Constants.C_PARAMETER_VALIDATION_AUTOMATICA);
	}
	
	
	/**
	 * Recupera el parámetro de configuración que activa la validación de PDF/A sobre documentos subidos
	 * @return El parámetro de configuración que activa la validación de PDF/A sobre documentos subidos
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_VALIDATION_PDFA
	 */
	public boolean isValidacionPDFA() {
		return isActiveParameter(Constants.C_PARAMETER_VALIDATION_PDFA);
	}
		
	/**
	 * Recupera el parámetro de configuración que activa la validación de tamaño sobre documentos subidos
	 * @return El parámetro de configuración que activa la validación de tamaño sobre documentos subidos
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_VALIDATION_TAMAÑO
	 */
	public boolean isValidacionTamanio() {
		return isActiveParameter(Constants.C_PARAMETER_VALIDATION_TAMANIO);
	}
	
	public boolean isFIReActivo() {
		return isActiveParameter(Constants.C_PARAMETER_FIRE_ACTIVO);
	}
	
	public boolean isAutenticaActivo() {
		return isActiveParameter(Constants.C_PARAMETER_AUTENTICA_ACTIVO);
	}
	
	public boolean isTrifasicaActiva() {
		return isActiveParameter(Constants.C_PARAMETER_TRIFASICA_ACTIVA);
	}
	
	private boolean isActiveParameter(String codeParameter) {
		boolean retorno = false;
		PfConfigurationsParameterDTO configurationParameterDto = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam", codeParameter);
		if (configurationParameterDto != null) {
			retorno = utilComponent.convertStringToBoolean(configurationParameterDto.getTvalue());
		}
		return retorno;
	}
	
	public String obtenerTrifasicaURL() {
		PfConfigurationsParameterDTO configParametro = 
				(PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
						"administration.queryParameterByName", "cParam", Constants.C_PARAMETER_TRIFASICA_URL);
		return configParametro.getTvalue();
	}

	@Transactional(readOnly=false)
	public List<AbstractBaseDTO> listTypesApp(String type) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		//List<String> nombre_parametros = new ArrayList<String>();
	
//		if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_GLOBAL)){
//			nombre_parametros.add(Constants.C_PARAMETER_REPORT_KEY_3DES);
//			nombre_parametros.add(Constants.C_PARAMETER_REPORT_TYPE);
//			nombre_parametros.add(Constants.MAX_DOCUMENT_SIZE);
//			nombre_parametros.add(Constants.ACCEPTED_MIME_TYPES);
//			nombre_parametros.add(Constants.ACCEPTED_FILE_EXTENSIONS);
//			nombre_parametros.add(Constants.ENVIRONMENT);
//			nombre_parametros.add(Constants.C_PARAMETER_CVE_URL_VALIDATION_EXTERNAL);
//			nombre_parametros.add(Constants.C_PARAMETER_CVE_URL_VALIDATION_INTERNAL);
//			nombre_parametros.add(Constants.SHOW_VISIBILITY_OPTIONS);
//			nombre_parametros.add(Constants.C_PARAMETER_DEFAULT_SCOPE);
//			nombre_parametros.add(Constants.C_PARAMETER_ACTIVED_HISTORIC);
//		} else if(type.equalsIgnoreCase("GLOBAL-2")){
//			type= Constants.C_TYPE_PARAMETER_GLOBAL;
//			nombre_parametros.add(Constants.PROXY);
//			nombre_parametros.add(Constants.PROXY_SERVER);
//			nombre_parametros.add(Constants.PROXY_PORT);
//		} else if(type.equalsIgnoreCase("GLOBAL-3")){
//			type= Constants.C_TYPE_PARAMETER_GLOBAL;
//			nombre_parametros.add(Constants.TRUSTSTORE_FILE);
//			nombre_parametros.add(Constants.TRUSTSTORE_PASSWORD);
//			nombre_parametros.add(Constants.TRUSTSTORE_TYPE);
//		}else if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_LOGIN)){
//			nombre_parametros.add(Constants.C_PARAMETER_LOGIN_DEBUG);
//			nombre_parametros.add(Constants.C_PARAMETER_LOGIN_LDAP);
//			nombre_parametros.add(Constants.LOGIN_LDAP_URL);
//			nombre_parametros.add(Constants.LOGIN_LDAP_IDATTRIBUTE);
//			nombre_parametros.add(Constants.LOGIN_LDAP_ID);
//			nombre_parametros.add(Constants.LOGIN_LDAP_BASEDN);
//		}else if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_SIM)){
//			nombre_parametros.add(Constants.SIM_URL);
//			nombre_parametros.add(Constants.SIM_USER);
//			nombre_parametros.add(Constants.SIM_PASSWORD);
//		}else if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_STYLE)){
//			nombre_parametros.add(Constants.C_PARAMETER_INBOX_N_ROWS);
//		}else if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_NOTIFICATION)){
//			type= Constants.C_TYPE_PARAMETER_GLOBAL;
//			nombre_parametros.add(Constants.EMAIL_NOTICE_PARAM);
//			nombre_parametros.add(Constants.EMAIL_USER);
//			nombre_parametros.add(Constants.EMAIL_PASSWORD);
//			nombre_parametros.add(Constants.EMAIL_REMITTER);
//			nombre_parametros.add(Constants.SMTP_SERVER);
//			nombre_parametros.add(Constants.SMTP_PORT);
//			nombre_parametros.add(Constants.EMAIL_REMITTER_NAME);
//			nombre_parametros.add(Constants.C_PARAMETER_NOTIFICATION_NOTICE_ADMIN);
//			nombre_parametros.add(Constants.C_PARAMETER_NOTIFICATION_EMAIL_ADMIN);
//		}else if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_SM_DOCEL)){
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SMC_URL);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SMC_SECURITY_MODE);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SMC_SECURITY_PASSWORD);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SMC_SECURITY_PASSWORD_TYPE);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SMC_SECURITY_CERT_ALIAS);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SMC_SECURITY_CERT_PWD);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SMC_SECURITY_FILENAME);
//		}else if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_VALIDATION)){
//			nombre_parametros.add(Constants.C_PARAMETER_VALIDATION_AUTOMATICA);
//			nombre_parametros.add(Constants.C_PARAMETER_VALIDATION_PDFA);
//			nombre_parametros.add(Constants.C_PARAMETER_VALIDATION_TAMANIO);
//		} else if(type.equalsIgnoreCase(Constants.C_TYPE_PARAMETER_JOBS)){
//			//Se agregan los parametros generales de jobs
//			nombre_parametros.add(Constants.C_PARAMETER_JOBS_JOB_HISTORICO);
//		}
//		else {
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SPC_URL);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SPC_SECURITY_MODE);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SPC_SECURITY_PASSWORD);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SPC_SECURITY_PASSWORD_TYPE);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SPC_SECURITY_CERT_ALIAS);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SPC_SECURITY_CERT_PWD);
//			nombre_parametros.add(Constants.C_PARAMETER_DOCEL_SPC_SECURITY_FILENAME);
//		}
		
		parameters.put("type", type);
//		parameters.put("parametros", nombre_parametros);
		return baseDAO.queryListMoreParameters("administration.queryGralTypes", parameters);
	}

	@Transactional(readOnly=false)
	public List<AbstractBaseDTO> listValuesParams(
			List<AbstractBaseDTO> resultado) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parameters", resultado);
		return baseDAO.queryListMoreParameters("administration.findValuesFromParameters", map);
	}

	@Transactional(readOnly=false)
	public PfConfigurationsParameterDTO valueParamByPk(String id) {
		Long pk = Long.parseLong(id);
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter("administration.findValueParamByPk", "pk", pk);
	}
	
	@Transactional(readOnly=false)
	public boolean historicoActivado () {		
		PfConfigurationsParameterDTO copa = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.C_PARAMETER_ACTIVED_HISTORIC);
		return copa.getTvalue().contentEquals(Constants.C_YES);
	}

	 @Transactional(readOnly = false)
	public void actualizarConfigParam(PfConfigurationsParameterDTO configParam) {
		baseDAO.insertOrUpdate(configParam);
		gestionarJobHistorico(configParam);
		// Se actualiza el almacén de confianza por si cambio alguno de los parámetros relacionados	
		configManagerBO.setupTrustStore();
	}

	/**
	 * Si el parámetro administrado es el del historico, según el valor recibido, se crea o borra la tarea quartz
	 * Se agrega la funcionalidad de que cuando se cambie el parametro de frecuencia tambien se relance el job de historico
	 * @param configParam
	 * @author Andres Harker
	 */
	private void gestionarJobHistorico(PfConfigurationsParameterDTO configParam) {
		if(Constants.C_PARAMETER_ACTIVED_HISTORIC.equals(configParam.getPfParameter().getCparameter())) {
			if(historicoActivado()) {
				if (!quartzInvoker.existJob(Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC)) {
					
					//Se calcula la expresion cron para lanzar el job
					String expresionCron = crearExpresionCronConParametroDeSistema(Constants.C_PARAMETER_JOBS_JOB_HISTORICO);
					
					log.debug ("Creando job: " + Constants.JOB_REQUESTS_TO_HISTORIC);
					quartzInvoker.scheduleJobCron(null, RequestsToHistoricJob.class,
							Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC,
							expresionCron);
				} else {
					log.debug ("Job: " + Constants.JOB_REQUESTS_TO_HISTORIC + " ya existía");
				}
			} else {
				quartzInvoker.deleteJob(Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC);
			}
		}
		else if(Constants.C_PARAMETER_JOBS_JOB_HISTORICO.equals(configParam.getPfParameter().getCparameter())){
			
			if (quartzInvoker.existJob(Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC)) {
				
				quartzInvoker.deleteJob(Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC);
				
			}
			if(historicoActivado()) {
				
				String expresionCron = crearExpresionCronConParametroDeSistema(Constants.C_PARAMETER_JOBS_JOB_HISTORICO);
				
				log.debug ("Creando job: " + Constants.JOB_REQUESTS_TO_HISTORIC);
				quartzInvoker.scheduleJobCron(null, RequestsToHistoricJob.class,
						Constants.JOB_REQUESTS_TO_HISTORIC, Constants.JOB_REQUESTS_TO_HISTORIC,
						expresionCron);
				
			}
		}
	}

	/**
	 * Este metodo nos sirve para crear una expresion cron a partir de un parametro en bd, este parametro tiene un formato del estilo, "# tiempo", por ejemplo "1 dia", 
	 * para el tiempo funciona con dia, mes y año, en el caso de años se ignora el valor numerico
	 * @param cParameterJobsJobHistorico el nombre del parametro general del sistema
	 * @return
	 * @author Andres Harker
	 */
	public String crearExpresionCronConParametroDeSistema(String cParameterJobsJobHistorico) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("nombreParametro", cParameterJobsJobHistorico);
		List<AbstractBaseDTO> parametrosEncontrados = baseDAO.queryListMoreParameters("administration.parametroGeneralPorNombre", parameters);
		//Debe retornar solo un dato.
		PfConfigurationsParameterDTO parametro = (PfConfigurationsParameterDTO) parametrosEncontrados.get(0);
		String valorDelParametro = parametro.getTvalue();
		//0 15 15 2 4 ?	Se ejecuta a las 15:15:00 del día 2 del mes de abril
		String expresion="";
		//Se agrega el valor de segundos, minutos y horas
		expresion += "0 0 2 ";
		//Se agrega el valor de dias
		if(valorDelParametro.split(" ")[1].equalsIgnoreCase("dia")){
			if(valorDelParametro.split(" ")[0].equalsIgnoreCase("1")){
				expresion += "* * ?";
			}
			else{
				expresion += "1/"+valorDelParametro.split(" ")[0]+" * ?";
			}
		}
		else{
			expresion += "1 ";
			//Se agrega el valor del mes
			if(valorDelParametro.split(" ")[1].equalsIgnoreCase("mes")){
				if(valorDelParametro.split(" ")[0].equalsIgnoreCase("1")){
					expresion += "* ?";
				}
				else{
					expresion += "1/"+valorDelParametro.split(" ")[0]+" ?";
				}
			}
			else{
				//ignora el valor numerico si se elije años
				expresion += "1 ?";
			}
		}
		
		return expresion;
	}
	
	public PfApplicationsDTO loadApplication(String idAplicacion) {
		return (PfApplicationsDTO) baseDAO.queryElementOneParameter("request.applicationId", "capp", idAplicacion);
	}

	/**
	 * Recupera los parámetros relacionados con SIM
	 * @return
	 */
	public HashMap<String, String> obtenerParametrosSIM() {
		if(parametrosSIM == null) {
			List<AbstractBaseDTO> resultado_SIM = this.listTypesApp(Constants.C_TYPE_PARAMETER_SIM);
			List<AbstractBaseDTO> listValoresSIM = this.listValuesParams(resultado_SIM);
			HashMap<String,String> parametros = new HashMap<String,String>();
			for (Iterator<AbstractBaseDTO> it = listValoresSIM.iterator(); it.hasNext();) {
				PfConfigurationsParameterDTO pfConfigParam = (PfConfigurationsParameterDTO) it.next();
				parametros.put(pfConfigParam.getPfParameter().getCparameter(), pfConfigParam.getTvalue());
			}
			parametrosSIM = parametros;
		}
		return parametrosSIM;
	}

	public boolean viewUserPasswordField(){
		PfConfigurationsParameterDTO ldapParameter = this.queryLdap();		
		PfConfigurationsParameterDTO debugParameter = this.queryDebug();		
		return Constants.C_YES.equalsIgnoreCase(ldapParameter.getTvalue()) 
				|| Constants.C_YES.equalsIgnoreCase(debugParameter.getTvalue());
	}
	

}
