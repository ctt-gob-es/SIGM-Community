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

package es.seap.minhap.portafirmas.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.administration.JobSchedulerBO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.quartz.bean.JobScheduler;

@Controller
@RequestMapping("administration/task")
public class TasksController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	JobSchedulerBO jobSchedulerBO;
	
	@RequestMapping(value = "/loadTasks", method = RequestMethod.GET)
	public ModelAndView loadTasks(@RequestParam(value = "taskFilter") final String filter) {
		ModelMap model = null;
		
		try {			
			List<JobScheduler> taskList =  jobSchedulerBO.queryJobsScheduler(filter);

			model = new ModelMap();
			model.addAttribute("taskList", taskList);
			model.addAttribute("taskFilter", filter);

		} catch (Throwable t) {
			log.error("Error al cargar las tareas: ", t);
			
		}
		return new ModelAndView("programmers", model);
	}
	
	@RequestMapping(value = "/stopTask", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> stopTask ( @RequestParam(value = "taskName") final String name,
						   @RequestParam(value = "taskGroup") final String group) {
		
		ArrayList<String> errores = new ArrayList<String> ();
		try {
			jobSchedulerBO.stopSchedulerJob(name, group);
		} catch (Throwable t) {
			log.error ("Error al parar tarea. Nombre :" + name + " Grupo: " + group);
			errores.add(Constants.MSG_GENERIC_ERROR);
		}
		return errores;
	}
	
	@RequestMapping(value = "/startTask", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> startTask ( @RequestParam(value = "taskName") final String name,
						   @RequestParam(value = "taskGroup") final String group) {
		
		ArrayList<String> errores = new ArrayList<String> ();
		try {
			jobSchedulerBO.runSchedulerJob(name, group);
		} catch (Throwable t) {
			log.error ("Error al arrancar tarea. Nombre :" + name + " Grupo: " + group);
			errores.add(Constants.MSG_GENERIC_ERROR);
		}
		return errores;
	}
	
	@RequestMapping(value = "/deleteTask", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> deleteTask ( @RequestParam(value = "taskName") final String name,
						   @RequestParam(value = "taskGroup") final String group) {
		
		ArrayList<String> errores = new ArrayList<String> ();
		try {
			jobSchedulerBO.deleteSchedulerJob(name, group);
		} catch (Throwable t) {
			log.error ("Error al eliminar tarea. Nombre :" + name + " Grupo: " + group);
			errores.add(Constants.MSG_GENERIC_ERROR);
		}
		return errores;
	}

}
