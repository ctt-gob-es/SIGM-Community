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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.configuration.EmailBO;
import es.seap.minhap.portafirmas.business.jobs.dto.EmailJobDTO;
import es.seap.minhap.portafirmas.business.jobs.principal.ConstantsJobs;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.jobs.PfJobJobs;
import es.seap.minhap.portafirmas.domain.jobs.PfJobParametrosEjecucion;
import es.seap.minhap.portafirmas.utils.notice.configuration.NoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.notice.message.NoticeMessage;
import es.seap.minhap.portafirmas.web.beans.FileAttachedDTO;

@Service
public class AsyncService {
 
	Logger log = Logger.getLogger(AsyncService.class);
	
	@Autowired
	private EmailBO emailBO;
	
	@Autowired
	private BaseDAO baseDAO;
	
	@SuppressWarnings("rawtypes")
	@Async
	public void sendEmail(Class noticeService, String noticeName, NoticeConfiguration noticeConf, NoticeMessage noticeMessage, List<FileAttachedDTO> ficheros, DateTime fechaNotificacion) throws NoticeException{
		if (fechaNotificacion != null && fechaNotificacion.isAfterNow()) { //Si es fecha futura se programa un quartz

			emailBO.doNoticeQuartzEmail(noticeConf, noticeMessage, noticeName, fechaNotificacion);

		} else { //Si no va a fecha futura
			//Se hace la notificación por email de forma asíncrona, en caso de fallo, se programa en quartz
			try {
				emailBO.doNotice(noticeConf, noticeMessage, ficheros);
			} catch (Exception e) {
	//			log.warn("Si falla en línea, se hace fuera de línea: ", e);
				EmailJobDTO ejd = new EmailJobDTO(noticeConf, noticeMessage, ficheros);
				
				PfJobParametrosEjecucion jpe = new PfJobParametrosEjecucion(null, ejd); 
				Map<String, Object> parameters = new HashMap<String, Object> ();
				parameters.put("codigoJob", ConstantsJobs.JOB_MAIL);
				List<AbstractBaseDTO> jobEmail = baseDAO.queryListMoreParameters("PfJobJobs.findByCode", parameters);
				
				PfJobJobs jobMail = (PfJobJobs)jobEmail.get(0);
				
				jpe.setIdJob(jobMail);
				
				baseDAO.insertOrUpdate(jpe);
				
	//			service.doNoticeOffLine(noticeService, noticeName, noticeConf, noticeMessage);
			}
		}
	}
	
	
}