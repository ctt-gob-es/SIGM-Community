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

package es.seap.minhap.portafirmas.utils.notice.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.configuration.EmailBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.configuration.EmailNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.configuration.ExternalAppNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.configuration.NoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.configuration.NoticeConfigurationFactory;
import es.seap.minhap.portafirmas.utils.notice.configuration.SMSNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.notice.message.EmailNoticeMessage;
import es.seap.minhap.portafirmas.utils.notice.message.NoticeMessage;
import es.seap.minhap.portafirmas.utils.notice.message.NoticeMessageFactory;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;
import es.seap.minhap.portafirmas.web.beans.FileAttachedDTO;

/**
 * Provide method to add job to quartz schedule. Job creates
 * {@link NoticeConfiguration} and {@link NoticeMessage} and calls service to
 * send notice type.
 * 
 * @author Guadaltel
 */
@Service
@EnableAsync
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class NoticeService {

	Logger log = Logger.getLogger(NoticeService.class);

	@Autowired
	private EmailBO emailBO;
	
	@Autowired
	private NoticeConfigurationFactory noticeConfigurationFactory;

	@Autowired
	private NoticeMessageFactory noticeMessageFactory;

	@Autowired
	private QuartzInvoker quartzInvoker;
	
	@Autowired
	private AsyncService asyncService;

	/**
	 * Intenta la notificación en linea y si no se consigue, se delega a una tarea de Quartz
	 * @param type
	 * @param event
	 * @param request
	 * @throws NoticeException
	 */
	@SuppressWarnings("rawtypes")
	public void doNotice(String type, String event, AbstractBaseDTO abstractDTO, List<PfUsersDTO> forwardedUsers) throws NoticeException {
		// Se deduce el servicio al que llamar, e_mail o SMS
		Class noticeService = null;
		if (type != null && (type.equals(Constants.EMAIL_NOTICE)||(type.equals(Constants.INVITATION_NOTICE)) )) {
			noticeService = EmailNoticeServiceJob.class;
		} else if (type != null && type.equals(Constants.SMS_NOTICE)) {
			noticeService = SMSNoticeConfiguration.class;
		}else {
			throw new NoticeException("Invalid notice type");
		}
		// Se obtiene la configuración y el mensaje de la notificación
		NoticeConfiguration noticeConf = noticeConfigurationFactory.createNoticeConfiguration(type);
		NoticeMessage noticeMessage = noticeMessageFactory.createNoticeMessage(type, event, abstractDTO, forwardedUsers);

		// Se obtiene le nombre para cada proceso
		String noticeName = type;
		DateTime fechaInicio = null;
		if(abstractDTO instanceof PfRequestsDTO) {
			noticeName += "_" +((PfRequestsDTO) abstractDTO).getChash();
			fechaInicio = new DateTime(((PfRequestsDTO) abstractDTO).getFstart());
		}
		noticeName += "_" +System.currentTimeMillis();

		// Si es un email
		if(noticeService.equals(EmailNoticeServiceJob.class)) {
			if (emailBO.checkEmailNotice((EmailNoticeConfiguration)noticeConf, (EmailNoticeMessage)noticeMessage)) {
				asyncService.sendEmail(noticeService, noticeName, noticeConf, noticeMessage,null, fechaInicio);
			} else {
				if (noticeMessage == null) {
					log.warn("No hay ningún e-mail al que notificar.");
				} else {
					log.error("Parámetros de e-mail incorrectos o no encontrados." +
							" Comprobar la configuración de notificación de e-mails.");
				}
			}
		// Si es un SMS
		} else if(noticeService.equals(SMSNoticeConfiguration.class)) {
			doNoticeOffLine(noticeService, noticeName, noticeConf, noticeMessage);
		}
	}
	public void doNotice(String type, String event, AbstractBaseDTO abstractDTO) throws NoticeException {
		doNotice(type, event, abstractDTO, null);
	}
	
	public void doNoticeReports(String type, String event, List<String> destinatarios, List<FileAttachedDTO> ficheros, String subjectRequest) throws NoticeException {
		// Se deduce el servicio al que llamar, e_mail o SMS
		Class<?> noticeService = null;
		if (type != null && type.equals(Constants.EMAIL_NOTICE)) {
			noticeService = EmailNoticeServiceJob.class;
		} else if (type != null && type.equals(Constants.SMS_NOTICE)) {
			noticeService = SMSNoticeConfiguration.class;
		} else {
			throw new NoticeException("Invalid notice type");
		}
		// Se obtiene la configuración y el mensaje de la notificación
		NoticeConfiguration noticeConf = noticeConfigurationFactory.createNoticeConfiguration(type);
		NoticeMessage noticeMessage = noticeMessageFactory.createNoticeMessage(type, event, destinatarios, subjectRequest);

		// Se obtiene le nombre para cada proceso
		String noticeName = type;
		noticeName += "_" +System.currentTimeMillis();

		// Si es un email
		if(noticeService.equals(EmailNoticeServiceJob.class)) {
			if (emailBO.checkEmailNotice((EmailNoticeConfiguration)noticeConf, (EmailNoticeMessage)noticeMessage)) {
				asyncService.sendEmail(noticeService, noticeName, noticeConf, noticeMessage, ficheros, null);
			} else {
				if (noticeMessage == null) {
					log.warn("No hay ningún e-mail al que notificar.");
				} else {
					log.error("Parámetros de e-mail incorrectos o no encontrados." +
							" Comprobar la configuración de notificación de e-mails.");
				}
			}
		// Si es un SMS
		} else if(noticeService.equals(SMSNoticeConfiguration.class)) {
			doNoticeOffLine(noticeService, noticeName, noticeConf, noticeMessage);
		}
	}
	
	
	/**
	 * Para programar una tarea en Quartz de lo que no se ha podio hacer en línea
	 * @param service
	 * @param name
	 * @param configuration
	 * @param message
	 * @throws NoticeException
	 */
	@SuppressWarnings("rawtypes")
	public void doNoticeOffLine(Class service, String name, NoticeConfiguration configuration, NoticeMessage message)
			throws NoticeException {
		// Si existe mensaje, se programa una tarea en Quartz
		if (message != null) {
			log.info("Receivers found, notice schedule job");
			// Configuración del objeto con los parámetros para la tarea
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put(Constants.NOTICE_CONFIGURATION, configuration);
			jobDataMap.put(Constants.NOTICE_MESSAGE, message);
			// Planificación de tarea Quartz
			quartzInvoker.scheduleJobInterval(
				jobDataMap, service, name, Constants.NOTICE, Constants.NOTICE_RETRY, Constants.NOTICE_MS_INTERVAL);
		} else {
			log.info("Receivers not found, notice not scheduled");
		}
	}
	
	/**
	 * Realiza una notificaci&oacute;n por email a una aplicaci&oacute;n externa
	 * @param noticeConfiguration configuraci&oacute;n de la notificaci&oacute;n
	 * @param hash Código hash de la petición
	 * @throws NoticeException
	 * @see {@link es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker#scheduleJobInterval(JobDataMap, Class, String, String, int, int)}
	 * @see es.seap.minhap.portafirmas.utils.notice.service.ExternalAppNoticeServiceJob
	 */
	public void doExternalAppNotice(ExternalAppNoticeConfiguration noticeConfiguration, String hash) throws NoticeException {							
		Class<ExternalAppNoticeServiceJob> noticeService = ExternalAppNoticeServiceJob.class;
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(Constants.NOTICE_CONFIGURATION, noticeConfiguration);
		jobDataMap.put(Constants.NOTICE_REQUEST_HASH, hash);
		// Schedule job (5 retrys, 300 sec interval)
		// Agustin lo cambio a 10 con 10 minutos de espera
		quartzInvoker.scheduleJobInterval(jobDataMap, noticeService, Constants.JOB_ADVICE_SERVICE
				+ "_" + hash + "_"+ System.currentTimeMillis(), Constants.NOTICE, 10, 600000);					
	}

	@SuppressWarnings("rawtypes")
	public void doNoticeEeutilException(String type, String event, String eeutilEvent, String host, String csv) throws NoticeException {
		Class noticeService = null;
		JobDataMap jobDataMap = new JobDataMap();
		if (type != null && type.equals(Constants.EMAIL_NOTICE)) {
			noticeService = EmailNoticeServiceJob.class;
		} else if (type != null && type.equals(Constants.SMS_NOTICE)) {
			noticeService = SMSNoticeConfiguration.class;
		} else {
			throw new NoticeException("Invalid notice type");
		}
		// Obtain configuration and message
		NoticeConfiguration noticeConf = noticeConfigurationFactory.createNoticeConfiguration(type);
		NoticeMessage noticeMessage = noticeMessageFactory.createEeutilNoticeMessage(type, event, eeutilEvent, host, csv);
		if (noticeMessage != null) {
			log.info("Receivers found, notice schedule job");
			jobDataMap.put(Constants.NOTICE_CONFIGURATION, noticeConf);
			jobDataMap.put(Constants.NOTICE_MESSAGE, noticeMessage);
			// Schedule job (5 retrys, 30 secs interval)
			quartzInvoker.scheduleJobInterval(jobDataMap, noticeService, type
					+ "_" + System.currentTimeMillis(), Constants.NOTICE, 5,
					300000);
		} else {
			log.info("Receivers not found, notice not scheduled");
		}
	}
}
