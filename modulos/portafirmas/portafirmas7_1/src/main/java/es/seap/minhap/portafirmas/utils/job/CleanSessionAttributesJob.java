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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.SessionBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;

public class CleanSessionAttributesJob implements Job {

	Logger log = Logger.getLogger(CleanSessionAttributesJob.class);

	@Autowired
	SessionBO sessionBO;
	
	/**
	 * Tarea que elimina las sesiones que tienen una antigüedad mayor a 24 horas.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("CleanSessionAttributesJob: EXECUTING...");
		
		// Necesario para que se injecten los Autowired
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		// Obtiene todas las sesiones anteriores a una fecha.
		List<AbstractBaseDTO> sessions = sessionBO.queryOldSessions(getDateFromRemoving());
		
		// Las elimina.
		sessionBO.deleteSessionAttributesList(sessions);
		
		log.debug("CleanSessionAttributesJob: EXECUTING END...");
	}
	
	/**
	 * Obtiene el momento actual y le resta 24 horas.
	 * @return
	 */
	private Date getDateFromRemoving () {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -24);		
		return c.getTime();
	}

}
