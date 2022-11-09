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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.OrganismoDIR3BO;

public class Dir3Job implements Job {

	private Logger log = Logger.getLogger(Dir3Job.class);

	@Autowired
	private OrganismoDIR3BO organismoDIR3BO;

	/**
	 * Tarea que pasa al historico las peticiones de hace un año.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("Dir3Job: EXECUTING...");
		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		try {
			organismoDIR3BO.actualizarOrganismosDir3(null, null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			log.error(e);
		} catch (SecurityException e) {
			e.printStackTrace();
			log.error(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			log.error(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			log.error(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		} catch (JAXBException e) {
			e.printStackTrace();
			log.error(e);
		}
		
		log.debug("Dir3Job: EXECUTING END.");
	}

}
