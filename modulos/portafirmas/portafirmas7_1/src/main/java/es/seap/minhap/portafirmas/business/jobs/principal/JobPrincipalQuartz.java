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

package es.seap.minhap.portafirmas.business.jobs.principal;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.jobs.beans.AsyncJobEmail;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.jobs.PfJobJobs;

public class JobPrincipalQuartz implements Job{

	private Logger log = Logger.getLogger(JobPrincipalQuartz.class);

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private AsyncJobEmail asyncJobEmail;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		log.info("Se ejecuta el job principal");

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		Map<String, Object> parameters = new HashMap<> ();
		List<AbstractBaseDTO> jobsEnBD = baseDAO.queryListMoreParameters("PfJobJobs.findAll", parameters);

		for(int i=0; i<jobsEnBD.size(); ++i){
			PfJobJobs jobProgramado = (PfJobJobs) jobsEnBD.get(i);
			if(debeEjecutarseJobEntity(jobProgramado, ConstantsJobs.JOB_MAIL)){
				asyncJobEmail.sendEmails(jobProgramado.getId());
				guardarFechaEjecucion(jobProgramado);
			}
		}
	}

	private void guardarFechaEjecucion(PfJobJobs jobProgramado){
		Calendar c = Calendar.getInstance();
		jobProgramado.setUltimaEjecucion(c.getTime());
		baseDAO.insertOrUpdate(jobProgramado);
	}

	private boolean debeEjecutarseJobEntity(PfJobJobs jobProgramado, String tipo){
		return tipo.equalsIgnoreCase(jobProgramado.getIdTipoJob().getCodigo()) 
				&& !"1".equalsIgnoreCase(jobProgramado.getEnEjecucion().toString())
				&& "1".equalsIgnoreCase(jobProgramado.getOnOff().toString())
				&& debeEjecutarse(jobProgramado.getFrecuenciaNumero().intValue(), jobProgramado.getIdFrecuenciaTipo().getCodigo(), jobProgramado.getUltimaEjecucion());
	}

	private boolean debeEjecutarse(int cuantasUnidades, String tipoFrecuencia, Date ultimaEjecucion){

		Calendar c = Calendar.getInstance();

		if(ultimaEjecucion==null){
			return true;
		}

		if(ConstantsJobs.MINUTOS.equalsIgnoreCase(tipoFrecuencia)){
			c.add(Calendar.MINUTE, cuantasUnidades*-1);
		}else if(ConstantsJobs.HORAS.equalsIgnoreCase(tipoFrecuencia)){
			c.add(Calendar.HOUR_OF_DAY, cuantasUnidades*-1);
		}else if(ConstantsJobs.DIAS.equalsIgnoreCase(tipoFrecuencia)){
			c.add(Calendar.DAY_OF_MONTH, cuantasUnidades*-1);
		}else if(ConstantsJobs.SEMANAS.equalsIgnoreCase(tipoFrecuencia)){
			c.add(Calendar.DAY_OF_MONTH, cuantasUnidades*-1*7);
		}else if(ConstantsJobs.MESES.equalsIgnoreCase(tipoFrecuencia)){
			c.add(Calendar.MONTH, cuantasUnidades*-1);
		}

		Calendar c2 = Calendar.getInstance();
		c2.setTime(ultimaEjecucion);

		if(c.after(c2)){
			return true;
		}

		return false;
	}
}
