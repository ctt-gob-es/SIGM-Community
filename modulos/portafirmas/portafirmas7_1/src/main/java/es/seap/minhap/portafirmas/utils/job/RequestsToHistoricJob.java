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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.storage.dao.JdbcRequestStorageDAO;
import es.seap.minhap.portafirmas.storage.util.StorageDBConnectionManager;

public class RequestsToHistoricJob implements Job {

	private Logger log = Logger.getLogger(RequestsToHistoricJob.class);

	@Autowired
	RequestBO requestBO;
	
	@Autowired
	private StorageDBConnectionManager storageDBConnectionManager;

	/**
	 * Tarea que pasa al historico las peticiones de hace un año.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("RequestsToHistoricJob: EXECUTING...");
		
		// Necesario para que se inyecten los Autowired
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		// Obtiene todas las peticiones de justo hace un años, que se pueden pasar al histórico
		List<AbstractBaseDTO> requestsDTOList = requestBO.queryRequestsToHistoric();
		List<String> idRequestList = getIdRequestList(requestsDTOList);
		
		// Se mueven las solicitudes al histórico
		JdbcRequestStorageDAO jdbc = new JdbcRequestStorageDAO();
		try {
			jdbc.moveToStorage(idRequestList, storageDBConnectionManager);
		} catch (Throwable e) {
			log.error("Error inesperado en la tarea del histórico: ", e );
		}
		
		log.debug("RequestsToHistoricJob: EXECUTING END.");
	}

	/**
	 * @param requestsDTOList
	 * @return
	 */
	private List<String> getIdRequestList(List<AbstractBaseDTO> requestsDTOList) {
		ArrayList<String> list = new ArrayList<String>();
		for (Iterator<AbstractBaseDTO> it = requestsDTOList.iterator(); it.hasNext();) {
			PfRequestsDTO pfRequestsDTO = (PfRequestsDTO) it.next();
			list.add(pfRequestsDTO.getPrimaryKeyString());
		}
		return list;
	}

}
