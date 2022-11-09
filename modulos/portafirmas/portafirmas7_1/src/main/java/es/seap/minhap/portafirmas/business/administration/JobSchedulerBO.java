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

package es.seap.minhap.portafirmas.business.administration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.ObjectComparator;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;
import es.seap.minhap.portafirmas.utils.quartz.bean.JobScheduler;

@Service
public class JobSchedulerBO {

	private Logger log = Logger.getLogger(JobSchedulerBO.class);

	@Autowired
	private QuartzInvoker quartzInvoker;

	@Resource(name = "messageProperties")
	private Properties messages;

	/**
	 * Devuelve una lista de todos los jobs que no pertenezcan al grupo 'PREFIRMA',
	 * 'LIMPIEZA_TEMPORAL','EXIRED_REQUEST','CLEAN_OLD_SESSIONS','GENERATE_REPORTS',
	 * 'DOCELWEB_CLEANER', JOB_LINEAFIRMA_AUTORIZACION
	 * @return la lista de jobs
	 */
	public List<JobScheduler> queryJobsScheduler(String filter) {
		log.info("queryJobsScheduler init");
		List<JobScheduler> jobs = new ArrayList<JobScheduler>();
		// String[] groups = sched.getJobGroupNames();
		//Obtenemos los nombres de todos los grupos conocidos de los JobDetail del Scheduler
		List<String> groups = quartzInvoker.queryGroups();
		//Recorremos los grupos
		for (String grupo : groups) {
			//Si el grupo no es ni 'PREFIRMA' ni 'LIMPIEZA_TEMPORAL'
			if (!grupo.equals("PREFIRMA")
					&& !grupo.equals("LIMPIEZA_TEMPORAL")
					&& !grupo.equals(Constants.JOB_EXPIRED_REQUESTS)
					&& !grupo.equals(Constants.JOB_CLEAN_OLD_SESSIONS)
					&& !grupo.equals(Constants.JOB_LINEAFIRMA_AUTORIZACION)
					&& !grupo.equals(Constants.JOB_GENERATE_REPORTS)
					&& !grupo.equals(Constants.JOB_REQUESTS_TO_HISTORIC)) {
				//Obtenemos los nombres de todos los org.quartz.JobDetail en el grupo que pasamos como par&aacute;metro
				List<String> names = quartzInvoker.queryNames(grupo);
				
				// Si el filtro no es nulo, entonces sólo nos quedamos con los nombres
				if (filter != null && !filter.equalsIgnoreCase("")) {
					names = deleteNamesNoFilterMatch (names, filter);
				}
				
				//recorremos los nombres y creamos un JobScheduler para cada uno de ellos
				for (String nombre : names) {
					JobScheduler job = new JobScheduler();
					job.setGroup(grupo);
					job.setName(nombre);

					String error = quartzInvoker
							.queryError(nombre, grupo);
					job.setError(error);
					//Obtetemos el estado actual del Trigger identificado
					String state = quartzInvoker.queryState(nombre, grupo);
					//Si el Trigger no tiene estado
					if (Trigger.TriggerState.NONE.toString().equals(state)) {
						log.info("El trigger no tiene estado");
						//pone el estado del job a parado
						job.setState(messages.getProperty("stopped"));
					//Si el Trigger tiene estado normal
					} else if (Trigger.TriggerState.NORMAL.toString().equals(state)) {
						//pone el estado del job a ejecutando
						job.setState(messages.getProperty("executing"));
					}
					//Ponemos la fecha del job, obtenemos la fecha a partir del nombre del job
					int posSpace = nombre.lastIndexOf("_");
					if (posSpace != -1) {
						String time = nombre.substring(posSpace + 1);
						Calendar cal = new GregorianCalendar();
						cal.setTimeInMillis(Long.parseLong(time));
						log.info("Time: " + cal);
						Date date = cal.getTime();
						log.info("Date: " + date);
						job.setDate(date);
					}
					//ponemos el job en la lista de jobs
					jobs.add(job);
				}
			}
		}
		//Ordenamos la lista de jobs de mayor a menor
		ObjectComparator objectComparator = new ObjectComparator(-1);
		Collections.sort(jobs, objectComparator);
		log.info("queryJobsScheduler end");
		return jobs;

	}

	
	/**
	 * M&eacute;todo que guarda/elimina en la BD una serie de tareas programados.
	 * @param jobSchedulerList Listado de tareas programadas a a&ntilde;adir a la BD.
	 * @param jobSchedulerDeleteList Listado de tareas programadas a eliminar de la BD.
	 */
	public void saveJobSchedulerList(List<JobScheduler> jobSchedulerList,
			List<JobScheduler> jobSchedulerDeleteList) {

		java.util.Iterator<JobScheduler> it = jobSchedulerDeleteList.iterator();
		while (it.hasNext()) {
			JobScheduler jobScheduler = it.next();
			deleteSchedulerJob(jobScheduler.getName(), jobScheduler.getGroup());
		}
	}

	/**
	 * M&eacute;todo que ejecuta una tarea programada. La tarea se repetir&aacute; 5 veces
	 * en intervalos de 30 segundos.
	 * @param name Nombre de la tare.
	 * @param group Grupo al que pertenece la tares.
	 */
	public void runSchedulerJob(String name, String group) {
		// quartzInvoker.scheduleJobInterval(jobDataMap, cla, name, group,
		// repeatCount, repeatInterval);
		quartzInvoker.reScheduleJobInterval(name, group, 5, 30000);
	}

	/**
	 * M&eacute;todo que detiene una tarea programada.
	 * @param name Nombre de la tarea.
	 * @param group Grupo al que pertenece la tarea.
	 */
	public void stopSchedulerJob(String name, String group) {
		quartzInvoker.stopJob(name, group);
	}

	/**
	 * M&eacute;todo que elimina una tarea programada.
	 * @param name Nombre de la tarea.
	 * @param group Grupo al que pertenece la tarea.
	 */
	public void deleteSchedulerJob(String name, String group) {
		quartzInvoker.deleteJob(name, group);
	}
	
	/**
	 * Elimina de una lista de nombres, aquéllos que no contienen la cadena pasada como filtro.
	 * @param names
	 * @param filter
	 * @return
	 */
	private List <String> deleteNamesNoFilterMatch (List <String> names, String filter) {
		filter = filter.toUpperCase();
		List <String> namesFiltered = new ArrayList<> ();
		for (String name : names) {
			if (name.toUpperCase().contains(filter)) {
				namesFiltered.add(name);
			}
		}
		return namesFiltered;				
	}
}
