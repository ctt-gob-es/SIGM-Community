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

package es.seap.minhap.portafirmas.business.administration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfMobileAgentsDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersParameterDTO;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ThemeBO {


	private Logger log = Logger.getLogger(ThemeBO.class);

	@Autowired
	private BaseDAO baseDAO;

	/**
	 * M&eacute;todo que guarda en BD la configuraci&oacute;n de estilos del usuario.
	 * @param theme Tema del estilo.
	 * @param language Lenguaje del estilo.
	 * @param inboxNRows N&uacute;mero de filas que se muestran en el estilo.
	 * @param user Usuario al que se asocia el estilo.
	 */
	@Transactional(readOnly = false)
	public void saveThemeUser(String theme, String language, String inboxNRows,
			PfUsersDTO user) {
		saveParamUser(Constants.C_PARAMETER_THEME, theme, user);
		saveParamUser(Constants.C_PARAMETER_LANGUAGE, language, user);
		saveParamUser(Constants.C_PARAMETER_INBOX_N_ROWS, inboxNRows, user);
	}

	/**
	 * M&eacute;todo que guarda en BD la configuraci&oacute;n de estilos del administrador.
	 * @param Tema del estilo.
	 * @param language Lenguaje del estilo.
	 * @param inboxNRows N&uacute;mero de filas que se muestran en el estilo.
	 */
	@Transactional(readOnly = false)
	public void saveThemeAdm(String theme, String language, String inboxNRows) {
		saveParamAdm(Constants.C_PARAMETER_THEME, theme);
		saveParamAdm(Constants.C_PARAMETER_LANGUAGE, language);
		saveParamAdm(Constants.C_PARAMETER_INBOX_N_ROWS, inboxNRows);
	}

	/**
	 * M&eacute;todo que guarda en BD el valor de un par&aacute;metro de administrador.
	 * @param paramName Nombre del par&aacute;metro a guardar.
	 * @param paramValue Valor del par&aacute;metro a guardar.
	 */
	@Transactional(readOnly = false)
	private void saveParamAdm(String paramName, String paramValue) {
		log.info("saveParamAdm init");

		PfParametersDTO paramDTO = (PfParametersDTO) baseDAO
				.queryElementOneParameter("administration.parameterId",
						"cparam", paramName);

		PfConfigurationsParameterDTO paramConfDTO = (PfConfigurationsParameterDTO) baseDAO
				.queryElementOneParameter("administration.parameterConfId",
						"cparam", paramName);

		if (paramConfDTO == null) {
			paramConfDTO = new PfConfigurationsParameterDTO();
			paramConfDTO.setPfParameter(paramDTO);
		}
		paramConfDTO.setTvalue(paramValue);
		baseDAO.insertOrUpdate(paramConfDTO);
		log.info("saveParamAdm end");
	}

	/**
	 * M&eacute;todo que guarda en BD el valor de un par&aacute;metro de usuario.
	 * @param paramName Nombre del par&aacute;metro a guardar.
	 * @param paramValue Valor del par&aacute;metro a guardar.
	 * @param user Usuario que guarda su par&aacute;metro.
	 */
	private void saveParamUser(String paramName, String paramValue, PfUsersDTO user) {
		// Se obtiene el parametro
		PfParametersDTO paramTheme = 
			(PfParametersDTO) baseDAO.queryElementOneParameter("configuration.parameter", "cparameter", paramName);

		// Se obtiene la relacion
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("pfUser", user);
		filter.put("pfParameter", paramTheme);
		PfUsersParameterDTO userParameter = (PfUsersParameterDTO) baseDAO.queryElementMoreParameters("configuration.userParam", filter);

		// Si el parámetro tiene un valor, se salva..
		if (!Util.esVacioONulo(paramValue)) {
			// Si no existía la relación, se crea
			if (userParameter == null) {
				userParameter = new PfUsersParameterDTO();
				userParameter.setPfParameter(paramTheme);
				userParameter.setPfUser(user);
			}
			userParameter.setTvalue(paramValue);
			baseDAO.insertOrUpdate(userParameter);
		} else {
			// .. si no lleva parámetro, se borra la relación
			if (userParameter != null) {
				baseDAO.delete(userParameter);
			}
		}
	}

	/**
	 * Obtiene el par&aacute;metro de lenguaje del usuario
	 * @param user el usuario
	 * @return el valor del par&aacute;metro de lenguaje del usuario
	 */
	public String loadUserLanguage(PfUsersDTO user) {
		return loadUserParam(Constants.C_PARAMETER_LANGUAGE, user);
	}
	/**
	 * Carga el tema del usuario que pasamos como par&aacute;metro
	 * @param user el usuario
	 * @return la cadena con el tema del usuario
	 */
	public String loadUserTheme(PfUsersDTO user) {
		return loadUserParam(Constants.C_PARAMETER_THEME, user);
	}
	/**
	 * Obtiene el n&uacute;mero de filas mostradas en la bandeja de peticiones del usuario
	 * @param user el usuario
	 * @return el n&uacute;mero de filas
	 */
	public String loadUserPageSize(PfUsersDTO user) {
		return loadUserParam(Constants.C_PARAMETER_INBOX_N_ROWS, user);
	}
	/**
	 * Obtiene el par&aacute;metro de configuraci&oacute;n del usuario que se le pasa como par&aacute;metro
	 * @param paramName el par&aacute;mero de configuraci&oacute;n del usuario que hay que cargar
	 * @param user el usuario
	 * @return el valor del par&aacute;metro
	 */
	private String loadUserParam(String paramName, PfUsersDTO user) {
		log.info("loadUserParam init");
		String paramValue = "";
		//recupera el par&aacute;metro
		PfParametersDTO param = (PfParametersDTO) baseDAO
				.queryElementOneParameter("configuration.parameter",
						"cparameter", paramName);
		//Si se ha recuperado el par&aacute;metro
		if (param != null && param.getPrimaryKey() != null
				&& !param.getPrimaryKeyString().equals("")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pfUser", user);
			params.put("pfParameter", param);
			//recupera el par&aacute;metro de usuario
			PfUsersParameterDTO paramUserLanguage = (PfUsersParameterDTO) baseDAO
					.queryElementMoreParameters("configuration.userParam",
							params);
			//Si se ha recuperado el par&aacute;metro de usuario recupera el valor del par&aacute;metro
			if (paramUserLanguage != null
					&& paramUserLanguage.getPrimaryKey() != null
					&& !paramUserLanguage.getPrimaryKeyString().equals("")) {
				paramValue = paramUserLanguage.getTvalue();
			}
		}
		log.info("loadUserParam end");
		return paramValue;
	}
	
	/**
	 * Carga el valor del par&aacute;metro de tema de la aplicaci&oacute;n
	 * @return el valor del par&aacute;metro de tema
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_THEME
	 */
	public String loadThemeAdm() {
		return loadParamAdm(Constants.C_PARAMETER_THEME);
	}
	/**
	 * Carga el valor del par&aacute;metro de lenguaje de la aplicaci&oacute;n
	 * @return el valor del par&aacute;metro de lenguaje
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_LANGUAGE
	 */
	public String loadLanguageAdm() {
		return loadParamAdm(Constants.C_PARAMETER_LANGUAGE);
	}
	/**
	 * Carga el valor del par&aacute;metro de n&uacute;mero de filas
	 * @return el valor del par&aacute;metro de n&uacute;mero de filas
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_PARAMETER_INBOX_N_ROWS
	 */
	public String loadAdmPageSize() {
		return loadParamAdm(Constants.C_PARAMETER_INBOX_N_ROWS);
	}

	/**
	 * Obtiene el listado de agentes móviles almacenados
	 * @return Listado de agentes móviles
	 */
	public List<PfMobileAgentsDTO> queryMobileAgents() {
		return baseDAO.queryListOneParameter("request.queryMobileAgents", null, null);
	}

	/**
	 * M&eacute;todo que carga un par&aacute;metro del adminsitrador a partir del nombre del par&aacute;metro en cuesti&oacute;n.
	 * @param paramName Nombre del par&aacute;metro a cargar.
	 * @return El valor del par&aacute;metro cargado.
	 */
	private String loadParamAdm(String paramName) {
		log.info("loadParamAdm init");
		String paramValue = null;
		PfConfigurationsParameterDTO parameter = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter("administration.parameterConfId", "cparam", paramName);
		if (parameter != null && parameter.getPrimaryKey() != null && !parameter.getPrimaryKeyString().equals("")) {
			paramValue = ConfigurationUtil.recuperaValorParametroYSustituyeEntorno(parameter);
		}
		log.info("loadParamAdm end");
		return paramValue;
	}

	@Transactional(readOnly = false)
	public void savePageSizeUser(String pageSize, PfUsersDTO user) {
		saveParamUser(Constants.C_PARAMETER_INBOX_N_ROWS, pageSize, user);
	}

}
