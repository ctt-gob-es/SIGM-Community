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

package es.seap.minhap.portafirmas.utils.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.utils.Constants;

public class ExpiredRequestsJob implements Job {

	Logger log = Logger.getLogger(ExpiredRequestsJob.class);

	@Autowired
	RequestBO requestBO;
	
	/**
	 * Tarea que comprueba las fecha de caducidad de las peticiones sin terminar.
	 * Las peticiones caducadas serán etiquetadas y pasarán al estado final "CADUCADO"
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("ExpiredRequestsJob: EXECUTING...");
		
		// Necesario para que se injecten los Autowired
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		// Obtiene todas las peticiones de la aplicación que no se encuentran en un estado final
		List<AbstractBaseDTO> requests = requestBO.queryRequestsTaggedWith(Constants.NOT_FINISHED_TAGS);
		
		// Cambia el estado a "CADUCADO" de todas las peticiones caducadas
		for (AbstractBaseDTO req : requests) {
			PfRequestsDTO request = (PfRequestsDTO) req;
			if (request.isExpired()) {
				requestBO.insertExpire(request);
			}
		}
		log.debug("ExpiredRequestsJob: EXECUTING END...");
	}

}
