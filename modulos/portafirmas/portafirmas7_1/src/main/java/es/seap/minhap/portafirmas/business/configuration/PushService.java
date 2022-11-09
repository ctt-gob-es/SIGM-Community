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

package es.seap.minhap.portafirmas.business.configuration;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.notice.message.NoticeMessageFactory;
import es.seap.minhap.portafirmas.utils.notice.message.PushNoticeMessage;
import es.seap.minhap.portafirmas.utils.notice.service.PushNoticeServiceJob;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;
import es.seap.minhap.portafirmas.ws.sim.respuesta.Respuesta;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class PushService {

	private static final Logger log = Logger.getLogger(PushService.class);
	
	@Autowired
	private NoticeMessageFactory noticeMessageFactory;
	
	@Autowired
	private QuartzInvoker quartzInvoker;

	/**
	 * Notificación push a través de SIM
	 * @param abstractDTO
	 * @param event
	 * @throws NoticeException
	 */
	public void doNotice(String event, AbstractBaseDTO abstractDTO) throws NoticeException {
		try {
			PushNoticeMessage pushNoticeMessage = (PushNoticeMessage) noticeMessageFactory.createNoticeMessage(Constants.PUSH_NOTICE, event, abstractDTO);	
			if(!pushNoticeMessage.getDestinatarios().isEmpty()) {
				// Se obtiene le nombre para cada proceso
				String noticeName = Constants.PUSH_NOTICE;
				DateTime fechaInicio = null;
				if(abstractDTO instanceof PfRequestsDTO) {
					noticeName += "_" +((PfRequestsDTO) abstractDTO).getChash();
					fechaInicio = new DateTime(((PfRequestsDTO) abstractDTO).getFstart());
				}
				noticeName += "_" +System.currentTimeMillis();
				
				JobDataMap jobDataMap = new JobDataMap();

				jobDataMap.put(Constants.NOTICE_MESSAGE, pushNoticeMessage);
				jobDataMap.put(Constants.NOTICE_EVENT, event);
				jobDataMap.put(Constants.NOTICE_ABSTRACT_DTO, abstractDTO);
				// Planificación de tarea Quartz
				if (fechaInicio != null && fechaInicio.isAfterNow()) { //Si es fecha futura se programa un quartz
					quartzInvoker.scheduleJobIntervalDesdeFecha(
							jobDataMap, PushNoticeServiceJob.class, noticeName, Constants.NOTICE, Constants.NOTICE_RETRY, Constants.NOTICE_MS_INTERVAL, fechaInicio);
				} else {
					quartzInvoker.scheduleJobInterval(
					jobDataMap, PushNoticeServiceJob.class, noticeName, Constants.NOTICE, Constants.NOTICE_RETRY, Constants.NOTICE_MS_INTERVAL);
				}
			}	
		} catch (NoticeException e) {
			log.error("Error al enviar petición a SIM.", e);
			throw new NoticeException("Error controlado al enviar petición a SIM.", e);
		} catch (Throwable e) {
			imprimirRespuesta(null);
			log.error("Error al enviar petición a SIM.", e);
			throw new NoticeException("Error desconocido al enviar petición a SIM.", e);
		}
	}


	/**
	 * @param respuesta
	 */
	public void imprimirRespuesta(Respuesta respuesta) {
		if(respuesta != null && respuesta.getStatus() != null) {
			if(respuesta.getStatus().getStatusText().equals("KO")) {
				log.info("Status code: " + respuesta.getStatus().getStatusCode());
				log.info("Status text: " + respuesta.getStatus().getStatusText());
				log.info("Status details: " + respuesta.getStatus().getDetails());
			} else {
				log.debug("Status code: " + respuesta.getStatus().getStatusCode());
				log.debug("Status text: " + respuesta.getStatus().getStatusText());
				log.debug("Status details: " + respuesta.getStatus().getDetails());
			}
		} else {
			log.error("Respuesta: " + respuesta);
		}
	}

}
