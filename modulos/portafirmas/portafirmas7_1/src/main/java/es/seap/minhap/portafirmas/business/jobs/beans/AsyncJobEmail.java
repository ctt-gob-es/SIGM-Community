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

package es.seap.minhap.portafirmas.business.jobs.beans;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.configuration.EmailBO;
import es.seap.minhap.portafirmas.business.jobs.dto.EmailJobDTO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.jobs.PfJobJobs;
import es.seap.minhap.portafirmas.domain.jobs.PfJobParametrosEjecucion;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.configuration.NoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.configuration.NoticeConfigurationFactory;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;

@Service
public class AsyncJobEmail {
 
	Logger log = Logger.getLogger(AsyncJobEmail.class);
	
	@Autowired
	private EmailBO emailBO;

	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private NoticeConfigurationFactory noticeConfigurationFactory;
	
	@Async
	public void sendEmails(Long idJob){
		
		log.info("INICIA EL JOB DE ENVIO DE CORREO ELECTRONICO");
		
		PfJobJobs jobProgramado = (PfJobJobs) baseDAO.findEntitity(PfJobJobs.class, idJob);
		
		jobProgramado.setEnEjecucion(new BigInteger("1"));
		
		baseDAO.insertOrUpdate(jobProgramado);
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("idJob", idJob);
		List<AbstractBaseDTO> parametrosJobs = baseDAO.queryListMoreParameters("PfJobParametrosEjecucion.findAllByIdJob", parameters);
		
		log.info("Se ejecuta el job de correo electronico, hay "+parametrosJobs.size()+" correos por enviar");
		NoticeConfiguration noticeConf = null;
		try {
			noticeConf = noticeConfigurationFactory.createNoticeConfiguration(Constants.EMAIL_NOTICE);
		} catch (NoticeException e1) {
			log.error("Ha ocurrido un error obteniendo la configuracion de envio de correo en el metodo del job", e1);
		}
		for(int i=0; i<parametrosJobs.size(); ++i){
			PfJobParametrosEjecucion parametros = (PfJobParametrosEjecucion) parametrosJobs.get(i);
			EmailJobDTO parametroEmail = (EmailJobDTO) parametros.getValor();
			try {
				
				emailBO.doNotice(noticeConf, parametroEmail.getEmailMessage(), parametroEmail.getFicheros());
			} catch (UnsupportedEncodingException e) {
				log.error("Ha ocurrido un error enviando el correo en el metodo del job", e);
			} catch (MessagingException e) {
				log.error("Ha ocurrido un error enviando el correo en el metodo del job", e);
			}
			baseDAO.delete(parametrosJobs.get(i));
		}
		
		jobProgramado.setEnEjecucion(new BigInteger("0"));
		
		baseDAO.insertOrUpdate(jobProgramado);
		
		log.info("FINALIZA EL JOB DE ENVIO DE CORREO ELECTRONICO");
		
	}
}
