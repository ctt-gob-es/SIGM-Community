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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.quartz.DateBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.matchers.GroupMatcher;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.actions.ApplicationVO;

import static org.quartz.JobKey.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerKey.*;

//@Service
//@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class QuartzInvoker {

	Logger log = Logger.getLogger(QuartzInvoker.class);

	private Scheduler sched;
	
	@Resource(name="quartzProperties")
	Properties quartzProperties;
	
	private static boolean isExecuted=false;
	
	
	 private static QuartzInvoker instance;

	  public synchronized static QuartzInvoker getInstance(){
	        if(instance==null){
	          instance = new QuartzInvoker();
	        }
	    return instance;
	  }
	

	public QuartzInvoker() {
	}

	/**
	 * Metodo que crea los objetos necesarios para controlar las tareas programadas del Portafirmas.
	 */
	@PostConstruct
	public void create() {

		if (!isExecuted) {

			isExecuted = true;

			log.info("create init");

			try {
				SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory(quartzProperties);
				sched = schedFact.getScheduler();
				sched.getListenerManager().addJobListener(new JobFailedListener());
				sched.start();
			} catch (SchedulerException e) {
				log.error("Error in create schedule", e);
			}

			log.info("create end");
		}
	}

	/**
	 * Crea un Simpletrigger para ejecutar un job
	 * @param jobDataMap el mapa de datos del job
	 * @param cla clase del job a ejecutar
	 * @param name nombre del job
	 * @param group  grupo del job
	 * @param repeatCount cuenta de repeticion
	 * @param repeatInterval intervalo de repeticion
	 */
	@SuppressWarnings({ "rawtypes" })
	public void scheduleJobInterval(JobDataMap jobDataMap, Class cla,
			String name, String group, int repeatCount, int repeatInterval) {
		
		log.warn("scheduleJob init");		
		
		DateTime dt = new DateTime();
        long ts = DateBuilder.nextGivenSecondDate(null, dt.getSecondOfMinute()).getTime();

		Trigger trigger = newTrigger()
        		.withIdentity(name, group)
        		.startAt(new Date(ts))
        		.withSchedule(simpleSchedule()
        				.withIntervalInMilliseconds(repeatInterval)
        				.withRepeatCount(repeatCount))
        		.build();

		scheduleJob(jobDataMap, cla, name, group, trigger);
		
		log.warn("Job: " + name + " will run at: " + trigger.getStartTime());

		log.info("scheduleJob end");
	}

	/**
	 * Crea un Simpletrigger para ejecutar un job
	 * @param jobDataMap el mapa de datos del job
	 * @param cla clase del job a ejecutar
	 * @param name nombre del job
	 * @param group  grupo del job
	 * @param repeatCount cuenta de repeticion
	 * @param repeatInterval intervalo de repeticion
	 */
	@SuppressWarnings({ "rawtypes" })
	public void scheduleJobIntervalDesdeFecha(JobDataMap jobDataMap, Class cla,
			String name, String group, int repeatCount, int repeatInterval, DateTime dt ) {
		
		log.warn("scheduleJob init");		
		
        long ts = dt.getMillis();

		Trigger trigger = newTrigger()
        		.withIdentity(name, group)
        		.startAt(new Date(ts))
        		.withSchedule(simpleSchedule()
        				.withIntervalInMilliseconds(repeatInterval)
        				.withRepeatCount(repeatCount))
        		.build();

		scheduleJob(jobDataMap, cla, name, group, trigger);
		
		log.warn("Job: " + name + " will run at: " + trigger.getStartTime());

		log.info("scheduleJob end");
	}

	
	/**
	 * Metodo que modifica la planificacion de una tarea.
	 * @param name Nombre de la tarea.
	 * @param group Grupo al que pertenece la tarea.
	 * @param repeatCount N&uacute;mero de repeticiones de la tarea.
	 * @param repeatInterval Tiempo de cada repeticion de la tarea.
	 */
	public void reScheduleJobInterval(String name, String group,
			int repeatCount, int repeatInterval) {
		log.info("reScheduleJob init");

		try {
			JobDetail jobDetail = sched.getJobDetail(jobKey(name, group));

			deleteJob(name, group);
			
			DateTime dt = new DateTime();
	        long ts = DateBuilder.nextGivenSecondDate(null, dt.getSecondOfMinute()).getTime();

	        Trigger trigger = newTrigger()
	        		.withIdentity(name, group)
	        		.startAt(new Date(ts))
	        		.withSchedule(simpleSchedule()
	        				.withIntervalInMilliseconds(repeatInterval)
	        				.withRepeatCount(repeatCount))
	        		.build();

			sched.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("Error rescheduling the job", e);
		}

		log.info("reScheduleJob end");
	}

	/**
	 * Metodo que permite planificar una tarea.
	 * @param jobDataMap Mapa de datos de la tarea.
	 * @param cla Clase de la tarea.
	 * @param name Nombre de la tarea.
	 * @param group Grupo al que pertenece la tarea.
	 * @param cronExpression Expresion cron que define el tiempo de ejecucuon de la tarea.
	 */
	@SuppressWarnings("rawtypes")
	public void scheduleJobCron(JobDataMap jobDataMap, Class cla, String name,
			String group, String cronExpression) {
		log.info("schedule "+name+" init");
		
		Trigger cronTrigger = newTrigger()
				.withIdentity(name, group)
				.withSchedule(cronSchedule(cronExpression))
				.build();
		
		scheduleJob(jobDataMap, cla, name, group, cronTrigger);
		
		log.info("schedule "+name+" end");
	}

	/**
	 * 
	 * @param jobDataMap el mapa de datos del job
	 * @param cla la clase del job a ejecutar
	 * @param name nombre del job
	 * @param group grupo del job
	 * @param trigger
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void scheduleJob(JobDataMap jobDataMap, Class cla, String name,
			String group, Trigger trigger) {
		try {

			if (jobDataMap == null)
				jobDataMap = new JobDataMap();
			
			JobDetail jobDetail = newJob(cla)
					.withIdentity(name, group)
					.storeDurably()
					.usingJobData(jobDataMap)
					.build();
			sched.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("Error in job schedule", e);
		}
	}

	/**
	 * Metodo que comprueba si una tarea ya existe.
	 * @param name Nombre de la tarea a buscar.
	 * @param group Grupo al que pertenece la tarea a buscar.
	 * @return "True" si la tarea ya existe, "false" en caso contrario.
	 */
	public boolean existJob(String name, String group) {
		log.info("existJob " + name + " init");
		boolean exist = false;
		try {
			
			if (sched.getJobDetail(jobKey(name, group)) != null) {
				exist = true;
			}
		} catch (SchedulerException e) {
			log.error("Error in existJob schedule", e);
		}
		log.info("existJob " + name + " finish resultado: " + exist);
		return exist;
	}

	/**
	 * Metodo que obtiene los detalles de una tarea programada.
	 * @param name Nombre de la tarea a buscar.
	 * @param group Grupo al que pertenece la tarea a buscar.
	 * @return Objeto con la informacion de la tarea buscada.
	 */
	public JobDetail queryJob(String name, String group) {
		log.info("queryJob init");
		JobDetail jobDetail = null;
		
		try {
			
			jobDetail = sched.getJobDetail(jobKey(name, group));
		} catch (SchedulerException e) {
			log.error("Error in queryJob schedule", e);
		}
		log.info("queryJob init");
		return jobDetail;
	}

	/**
	 * Elimina una tarea.
	 * @param name Nombre de la tarea.
	 * @param group Grupo al que pertenece la tarea.
	 */
	public void deleteJob(String name, String group) {
		log.info("deleteJob init");
		try {
			
			sched.deleteJob(jobKey(name, group));
			
		} catch (SchedulerException e) {
			log.error("Error in deleteJob schedule", e);
		}
		log.info("deleteJob init");
	}

	/**
	 * Obtiene los nombres de todos los grupos conocidos de los JobDetail del Scheduler
	 * @return el array de cadenas con los nombres de todos los grupos conocidos de los JobDetail
	 * @see org.quartz.Scheduler#getJobGroupNames()
	 */
	public List <String> queryGroups() {
		List <String> groups = new ArrayList<>();
		try {
			//Get the names of all known org.quartz.JobDetail groups.
			groups = sched.getJobGroupNames();
		} catch (SchedulerException e) {
			log.error("Error getting the job groups", e);
		}
		return groups;
	}

	/**
	 * Obtiene los nombres de todos los org.quartz.JobDetail en el grupo que pasamos como parametro
	 * @param group el grupo del JobDetail del Scheduler
	 * @return el array de cadenas con los nombres de todos los org.quartz.JobDetail en el grupo
	 * @see org.quartz.Scheduler
	 */
	public List <String> queryNames(String group) {
		List <String> names = new ArrayList<>();
		Set<JobKey> setJobKey = null;
		try {
			//Obtiene los nombres de todos los org.quartz.JobDetail en el grupo
			setJobKey = sched.getJobKeys(GroupMatcher.jobGroupEquals(group));
			
			for (JobKey jobKey : setJobKey){
				names.add(jobKey.getName());
			}
			
		} catch (SchedulerException e) {
			log.error("Error getting the job name", e);
		}
		
		return names;
	}

	/**
	 * Obtiene el estado actual del Trigger identificado
	 * @param nameTrigger el nombre del trigger
	 * @param groupTrigger el grupo del trigger
	 * @return el estado del trigger
	 * @see org.quartz.Scheduler#getTriggerState(String, String)
	 */
	public String queryState(String nameTrigger, String groupTrigger) {
		TriggerState state = null;
		try {
			
			Trigger trg = sched.getTrigger(triggerKey(nameTrigger, groupTrigger));
			
			//Obtenemos el estado actual del Trigger identificado
			state = sched.getTriggerState(trg.getKey());
		} catch (SchedulerException e) {
			log.error("Error getting the job state", e);
		}
		return state.toString();
	}

	/**
	 * Metodo que consulta los errores generados en la ejecucion de una tarea.
	 * @param name Nombre de la tarea.
	 * @param group Grupo al que pertenece la tarea.
	 * @return Cadena de texto con los errores de la tarea.
	 */
	public String queryError(String name, String group) {
		String errors = "";
		try {
			errors = (String) sched.getJobDetail(jobKey(name, group)).getJobDataMap().get("errors");
			log.info("Errores: " + errors);
		} catch (SchedulerException e) {
			log.error("Error getting the errors", e);
		}
		return errors;
	}

	/**
	 * Metodo que detiene una tarea programada.
	 * @param name Nombre de la tarea.
	 * @param group Grupo al que pertenece la tarea.
	 */
	public void stopJob(String name, String group) {
		try {
			
			sched.unscheduleJob(triggerKey(name, group));
			log.debug("Stopping job");
		} catch (UnableToInterruptJobException e) {
			log.error("Error stopping the job", e);
		} catch (SchedulerException e) {
			log.error("Error stopping job", e);
		}
	}

	@PreDestroy
	public void destroy() {
		log.info("destroy init");
		try {
			sched.shutdown();
		} catch (SchedulerException e) {
			log.error("Error in destroy schedule", e);
		}
		log.info("destroy end");
	}
}
