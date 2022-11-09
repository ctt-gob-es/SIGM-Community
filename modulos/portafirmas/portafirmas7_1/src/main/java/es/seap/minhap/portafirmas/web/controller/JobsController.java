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
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.administration.JobAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;
import es.seap.minhap.portafirmas.web.converter.JobConverter;

@Controller
@RequestMapping("userManagement/jobs")
public class JobsController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private JobAdmBO jobAdmBO;
	
	@Autowired
	private ProvinceBO provinceBO;

	
	@Autowired
	private JobConverter jobConverter;

	@RequestMapping(method = RequestMethod.GET)
	public String loadJobs(ModelMap model) {
		process(model, new UsersParameters());
		return "jobs";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String loadJobs(@ModelAttribute("jobsParameters") UsersParameters jobsParameters, ModelMap model) {
		process(model, jobsParameters);
		return "jobs";
	}

	private void process(ModelMap model, UsersParameters jobsParameters) {
		// Se obtiene el usuario conectado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO userLogDTO = authorization.getUserDTO();
		jobsParameters.setUser(userLogDTO);
		
		// Se obtiene la información para el conformar el modelo
		List<AbstractBaseDTO> jobsList = userAdmBO.queryJobsComplete(jobsParameters);
		List<AbstractBaseDTO> seatList = provinceBO.getSeatList(userLogDTO);

		model.addAttribute("jobsList", jobsList);
		model.addAttribute("jobsParameters", jobsParameters);
		model.addAttribute("job", new User());
		model.addAttribute("seatList", seatList);
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> save(@ModelAttribute User job) {
		return userAdmBO.saveJob(job);
	}
	
	@RequestMapping(value = "load", method = RequestMethod.POST)
	public String load(@ModelAttribute User job, Model model) {
		try {
			// Se recupera el usuario autenticado para obtener la lista de sedes que administra
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO userLogDTO = authorization.getUserDTO();
			List<AbstractBaseDTO> seatList = provinceBO.getSeatList(userLogDTO);

			// Se crea obtiene el usuario DTO
			PfUsersDTO pfJobDTO = userAdmBO.queryUsersByPk(Long.parseLong(job.getPrimaryKey()));
			job = jobConverter.DTOtoEnvelope(pfJobDTO);
			
			model.addAttribute("seatList", seatList);
			model.addAttribute("job", job);

		} catch (Exception e) {
			log.error("Error al obtener el cargo: ", e);
		}
		return "jobModal";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, ArrayList<String>> deleteUser(@ModelAttribute User job) {
		ArrayList<String> warnings = new ArrayList<>();
		ArrayList<String> isUndelete = new ArrayList<>();
		HashMap<String, ArrayList<String>> response = new HashMap<>();
		try {
			PfUsersDTO pfJobsDTO = userAdmBO.queryUsersByPk(Long.parseLong(job.getPrimaryKey()));
			warnings = jobAdmBO.jobAssociated(pfJobsDTO);
			isUndelete = jobAdmBO.requestAssociated(pfJobsDTO);
			if(warnings.isEmpty() && isUndelete.isEmpty()) {
				jobAdmBO.deleteJob(pfJobsDTO);
			}
			response.put("warnings", warnings);
			response.put("isUndelete", isUndelete);

		} catch (Exception e) {
			log.error("Error al borrar el cargo: ", e);
			ArrayList<String> errors = new ArrayList<String>();
			errors.add(Constants.MSG_GENERIC_ERROR);
			response.put("errors", errors);
		}
		return response;
	}

	@RequestMapping(value = "revoke", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> revokeUser(@ModelAttribute User job) {
		ArrayList<String> error =new ArrayList<>();
		try {
			PfUsersDTO pfJobDTO = userAdmBO.queryUsersByPk(Long.parseLong(job.getPrimaryKey()));
			jobAdmBO.revokeUser(pfJobDTO);
		} catch (Exception e) {
			log.error("Error al borrar el cargo: ", e);
			error.add(Constants.MSG_GENERIC_ERROR);
		}
		return error;
	}

}
