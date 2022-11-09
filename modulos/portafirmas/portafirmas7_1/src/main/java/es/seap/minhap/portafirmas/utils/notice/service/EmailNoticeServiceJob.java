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

import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.configuration.EmailBO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.configuration.EmailNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.notice.message.EmailNoticeMessage;

/**
 * Service used to execute an email notice from a
 * {@link EmailNoticeConfiguration} and a {@link EmailNoticeMessage}. Uses
 * quartz to schedule as jobs, retrying notices in case of fail
 * 
 * @author Guadaltel
 */
public class EmailNoticeServiceJob implements NoticeServiceJob {

	private static final Logger log = Logger.getLogger(EmailNoticeServiceJob.class);
	
	@Autowired
	private EmailBO emailBO;
	
	/**
	 * @see org.quartz.Job
	 * M&eacute;todo que ejecuta una tarea de env&iacute;o de una notificaci&oacute;n de correo electr&oacute;nico.
	 * @param context Contexto de la tarea en ejecuci&oacute;n.
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		EmailNoticeConfiguration emailConfiguration = 
			(EmailNoticeConfiguration) context.getJobDetail().getJobDataMap().get(Constants.NOTICE_CONFIGURATION);
		EmailNoticeMessage emailMessage = 
			(EmailNoticeMessage) context.getJobDetail().getJobDataMap().get(Constants.NOTICE_MESSAGE);
		
		// Necesario para que se inyecten los Autowired
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this); 

		if (emailBO.checkEmailNotice(emailConfiguration, emailMessage)) {
			try {
				emailBO.doNotice(emailConfiguration, emailMessage,null);
			} catch (UnsupportedEncodingException e) {
				context.getJobDetail().getJobDataMap().put("errors", e.getMessage());
				throw new JobExecutionException(e);
			} catch (MessagingException e) {
				context.getJobDetail().getJobDataMap().put("errors", getMessageMsgEx(e));
				throw new JobExecutionException(e);
			} catch (Exception e) {
				String error = "Email unknown error";
				log.error(error, e);
				context.getJobDetail().getJobDataMap().put("errors", error);
				throw new JobExecutionException(new NoticeException(error, new Throwable(error)));
			}
		} else {
			String error = "Email parameters incorrect or not found. Check email notice configuration";
			log.error(error);
			context.getJobDetail().getJobDataMap().put("errors", error);
			throw new JobExecutionException(new NoticeException(error, new Throwable(error)));
		}
	}

	/**
	 * Monta el mensaje de error a partar de un objeto <code>MessagingException</cod>
	 * @param e
	 * @return
	 */
	private String getMessageMsgEx(MessagingException e) {
		String error = e.getMessage();
		if (e instanceof SendFailedException) {
			SendFailedException sfe = (SendFailedException) e;
			Address[] address = sfe.getInvalidAddresses();
			error += ": ";
			for (int i = 0; i < address.length; i++) {
				error += address[i];
				if (i + 1 < address.length) {
					error += ", ";
				}
			}
		}
		return error;
	}

}
