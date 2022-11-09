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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.job.CleanTempService;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class TempFileBO {


	Logger log = Logger.getLogger(TempFileBO.class);

	@Autowired
	private QuartzInvoker quartzInvoker;
	/**
	 * Ejecuta el job de limpieza temporal con la expresi&oacute;n 0 0 0 * * ?
	 * @see es.seap.minhap.portafirmas.utils.Constants#JOB_CLEANUP_TEMP
	 * @see es.seap.minhap.portafirmas.utils.job.CleanTempService
	 */
	public void executeCleanup() {
		log.debug("executeCleanup init");
		
		//deleteCleanup();
		if (!existsCleanup ()) {
			log.debug("job Cleanup no existe, se crea");
			// TODO: Cambiar para coger parÃ¡metro de BBDD
			String cronExpression = "0 0 0 * * ?";
			quartzInvoker.scheduleJobCron(null, CleanTempService.class,
					Constants.JOB_CLEANUP_TEMP, Constants.JOB_CLEANUP_TEMP,
					cronExpression);
			
		} else {
			log.debug("job Cleanup ya existia");
		}
		log.debug("executeCleanup end");
	}
	
	/**
	 * Comprueba si existe el job de limpieza temporal 
	 * @see es.seap.minhap.portafirmas.utils.Constants#JOB_CLEANUP_TEMP
	 */
	private boolean existsCleanup () {
		return quartzInvoker.existJob(Constants.JOB_CLEANUP_TEMP,
				Constants.JOB_CLEANUP_TEMP);
	}
	
	/**
	 * Borra el job de limpieza temporal si existe. 
	 * @see es.seap.minhap.portafirmas.utils.Constants#JOB_CLEANUP_TEMP
	 */
	public void deleteCleanup() {
		log.info("deleteCleanup init");
		if (quartzInvoker.existJob(Constants.JOB_CLEANUP_TEMP,
				Constants.JOB_CLEANUP_TEMP)) {
			quartzInvoker.deleteJob(Constants.JOB_CLEANUP_TEMP,
					Constants.JOB_CLEANUP_TEMP);
		}
		log.info("deleteCleanup end");
	}

}
