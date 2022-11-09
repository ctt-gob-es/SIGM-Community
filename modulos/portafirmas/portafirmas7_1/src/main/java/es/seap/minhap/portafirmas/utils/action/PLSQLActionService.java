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

package es.seap.minhap.portafirmas.utils.action;

import javax.persistence.PersistenceException;

import org.hibernate.exception.GenericJDBCException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.utils.Constants;


public class PLSQLActionService implements Job, ApplicationContextAware {

	private static final Logger log = Logger.getLogger(PLSQLActionService.class);

	// Inyecto el contexto de spring para acceder a los beans
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public PLSQLActionService() {
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		log.info("execute init");
		try {
			String action = (String) context.getJobDetail().getJobDataMap().get(Constants.JOB_ACTION);
			BaseDAO baseDAO = applicationContext.getBean("baseDAO", BaseDAO.class);
			baseDAO.executePLSQL(action);
			action = (String) context.getJobDetail().getJobDataMap().get(Constants.JOB_ACTION);
			baseDAO.executePLSQL(action);
		} catch (Exception e) {
			if(e instanceof PersistenceException){
				PersistenceException excep = (PersistenceException) e;
				GenericJDBCException jdbcExcep = (GenericJDBCException) excep
						.getCause();
				String errorSQL = jdbcExcep.getSQLException().getLocalizedMessage();
				context.getJobDetail().getJobDataMap().put("errors", errorSQL);
			} else {
				context.getJobDetail().getJobDataMap().put("errors", e.getLocalizedMessage());
			}
			throw new JobExecutionException(e);
		}
		log.info("execute end");
	}

}
