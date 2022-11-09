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

package es.seap.minhap.portafirmas.utils.quartz;

import static org.quartz.JobBuilder.newJob;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.service.ExternalAppNoticeServiceJob;

import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

public class JobFailedListener implements JobListener {

	private static final Logger log = Logger.getLogger(JobFailedListener.class);

	public String getName() {
		return "FAILED JOB";
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		log.info("jobToBeExecuted: " + context.getJobDetail().getKey().getName());
	}

	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException exception) {
		log.info("jobWasExecuted: " + context.getJobDetail().getKey().getName()+". Proxima ejecucion: " + context.getTrigger().getNextFireTime());

		if (exception != null) {
			log.error("Error in execution job: "
					+ context.getJobDetail().getKey().getName() + " " + exception.getMessage() + ". Proxima ejecucion: " + context.getTrigger().getNextFireTime(), exception);

			if (!context.getTrigger().mayFireAgain()) {
				try {
					if (context.getJobDetail().getKey().getName().contains(Constants.JOB_DAILY_ADVICE_SERVICE)) {
						log.debug("BORRA LA TAREA DAILY: " + context.getJobDetail().getKey().getName());
						context.getScheduler().deleteJob(context.getJobDetail().getKey());
					} else if (context.getJobDetail().getKey().getName().contains(Constants.JOB_ADVICE_SERVICE)) {
						// Se añade el servicio diario de notificaciones a aplicaciones externas si falla la tarea estándar
						String hash = context.getJobDetail().getJobDataMap().getString(Constants.NOTICE_REQUEST_HASH);
						String name = Constants.JOB_DAILY_ADVICE_SERVICE + "_" + hash + "_" + System.currentTimeMillis();
						
						GregorianCalendar date = new GregorianCalendar();
						date.roll(Calendar.WEEK_OF_YEAR, 1);

						// Se crea el disparador cron, solo debe lanzarse durante un mes y se adapta a la nueva version del Quartz (2.2.3)
						Trigger cronTrigger = newTrigger()
								.withIdentity(name, Constants.NOTICE)
								.withSchedule(cronSchedule(Constants.JOB_EXTERNAL_APP_EVERY_DAY)
										.withMisfireHandlingInstructionFireAndProceed())
								.endAt(date.getTime())
								.build();
						
						// Se crea el nuevo data map
						JobDataMap newJobDataMap = new JobDataMap();
						newJobDataMap.put(Constants.NOTICE_CONFIGURATION,
										  context.getJobDetail().getJobDataMap().get(Constants.NOTICE_CONFIGURATION));
						newJobDataMap.put(Constants.NOTICE_REQUEST_HASH, hash);

						// Se crea el nuevo job details con la nueva version del Quartz (2.2.3).				
						JobDetail jobDetail = newJob(ExternalAppNoticeServiceJob.class)
								.withIdentity(name, Constants.NOTICE)
								.setJobData(newJobDataMap)
								.storeDurably(true)
								.build();
						
						// Se programa la tarea
						context.getScheduler().scheduleJob(jobDetail, cronTrigger);

						// Borramos la tarea Advice Service
						context.getScheduler().deleteJob(context.getJobDetail().getKey());

					} else {
						log.info("Rescheduling job: " + context.getJobDetail().getKey().getName());
						context.getScheduler().addJob(context.getJobDetail(), true);
					}
				} catch (SchedulerException e) {
					log.error("Error in scheduler: adding job or deleting advice service", e);
				}
			} else {
				// Reintenta el envío //
			}

		} else {
			try {
				if (!context.getJobDetail().getKey().getName().equals(Constants.JOB_EXPIRED_REQUESTS) &&
					!context.getJobDetail().getKey().getGroup().equals(Constants.JOB_EXPIRED_REQUESTS) &&
					!context.getJobDetail().getKey().getName().equals(Constants.JOB_CLEANUP_TEMP) &&
					!context.getJobDetail().getKey().getGroup().equals(Constants.JOB_CLEANUP_TEMP) &&
					!context.getJobDetail().getKey().getName().equals(Constants.JOB_CLEAN_OLD_SESSIONS) &&
					!context.getJobDetail().getKey().getGroup().equals(Constants.JOB_CLEAN_OLD_SESSIONS) &&
					!context.getJobDetail().getKey().getName().equals(Constants.JOB_GENERATE_REPORTS) &&
					!context.getJobDetail().getKey().getGroup().equals(Constants.JOB_GENERATE_REPORTS) &&
					!context.getJobDetail().getKey().getName().equals(Constants.JOB_LINEAFIRMA_AUTORIZACION) &&
					!context.getJobDetail().getKey().getGroup().equals(Constants.JOB_LINEAFIRMA_AUTORIZACION) &&
					!context.getJobDetail().getKey().getGroup().equals(Constants.JOB_REQUESTS_TO_HISTORIC) &&
					!context.getJobDetail().getKey().getGroup().equals("principalPF")
					) {
					
					context.getScheduler().deleteJob(context.getJobDetail().getKey());
				}
			} catch (SchedulerException e) {
				log.error("Error in scheduler: deleting job", e);
			}
		}
		log.info("jobWasExecuted end");
	}

	public void jobExecutionVetoed(JobExecutionContext context) {
		log.info("jobExecutionVetoed: " + context.getJobDetail().getKey().getName());
	}
}
