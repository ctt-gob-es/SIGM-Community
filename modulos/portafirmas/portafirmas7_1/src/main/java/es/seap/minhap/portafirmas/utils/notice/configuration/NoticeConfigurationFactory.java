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

package es.seap.minhap.portafirmas.utils.notice.configuration;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;

/**
 * Provide method to create {@link NoticeConfiguration} implementations from
 * notice type.
 * 
 * @author Guadaltel
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class NoticeConfigurationFactory {

	private Logger log = Logger.getLogger(NoticeConfigurationFactory.class);

	@Autowired
	private NoticeBO noticeBO;

	private Map<String, String> noticeParameters;

	/**
	 * Creates {@link NoticeConfiguration} implementation depending on notice
	 * type received
	 * 
	 * @param type
	 *            Notice type (Email or SMS)
	 * @param request
	 *            {@link PfRequestsDTO} associated to notice
	 * @return {@link NoticeConfiguration} implementation with data obtained
	 *         from request
	 * @throws NoticeException
	 *             if an error occurs during {@link NoticeConfiguration}
	 *             creation
	 *@see #loadEmailNoticeConfiguration(Map)
	 */
	public NoticeConfiguration createNoticeConfiguration(String type) throws NoticeException {
		NoticeConfiguration noticeConfiguration = null;
		//Recupera el mapa con la clave y el valor de los par&aacute;metros de configuraci&oacute;n
		noticeParameters = noticeBO.queryNoticeConfigurationParameterList();
		log.info("createNoticeConfiguration init");
		//Si el tipo notificaci&oacute;n es EMAIL
		if (type != null && (type.equals(Constants.EMAIL_NOTICE) || type.equals(Constants.INVITATION_NOTICE))) {
			//Cargamos el objeto de almacenamiento de configuraci&oacute;n de email
			noticeConfiguration = loadEmailNoticeConfiguration(noticeParameters);
		//Si el tipo notificaci&oacute;n es SMS
		} else if (type != null && type.equals(Constants.SMS_NOTICE)) {
			//Crea un objeto de almacenamiento de configuraci&oacute;n de SMS
			noticeConfiguration = new SMSNoticeConfiguration();
		//En otro caso se lanza una excepci&oacute;n
		} else {
			throw new NoticeException("Invalid notice type");
		}
		log.info("createNoticeConfiguration end");
		return noticeConfiguration;
	}
	/**
	 * Carga un objeto de almacenamiento de datos de configuraci&oacute;n para el servidor de email
	 * y usuarios que env&iacute;an notificaciones por email con el valor del par&aacute;metro de configuraci&oacute;n
	 * del mapa que pasamos como par&aacute;metro
	 * @param noticeParameters el mapa con la clave y el valor de los par&aacute;metros de configuraci&oacute;n
	 * @return el objeto de almacenamiento cargado con los datos del mapa que pasamos como par&aacute;metro
	 */
	private NoticeConfiguration loadEmailNoticeConfiguration(
			Map<String, String> noticeParameters) {
		log.info("loadEmailNoticeConfiguration init");
		EmailNoticeConfiguration noticeConfiguration = new EmailNoticeConfiguration();
		//Si el mapa no esta vac&iacute;o
		if (noticeParameters != null && !noticeParameters.isEmpty()) {
			noticeConfiguration.setFromName(noticeParameters
					.get(Constants.EMAIL_REMITTER_NAME));
			noticeConfiguration.setMailUser(noticeParameters
					.get(Constants.EMAIL_USER));
			noticeConfiguration.setMailPassword(noticeParameters
					.get(Constants.EMAIL_PASSWORD));
			noticeConfiguration.setMailRemitter(noticeParameters
					.get(Constants.EMAIL_REMITTER));
			noticeConfiguration.setSmtpHost(noticeParameters
					.get(Constants.SMTP_SERVER));
			noticeConfiguration.setSmtpPort(noticeParameters
					.get(Constants.SMTP_PORT));
			noticeConfiguration.setAuthenticate(noticeParameters
					.get(Constants.AUTHENTICATION_MODE));
		}
		log.info("loadEmailNoticeConfiguration end");
		return noticeConfiguration;
	}
}
