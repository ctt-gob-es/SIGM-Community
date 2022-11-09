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

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.configuration.AuthorizationBO;
import es.seap.minhap.portafirmas.dao.RequestDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfFiltersDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;

public class LineasFirmaFinAutorizacionJob implements Job {

	private Logger log = Logger.getLogger(LineasFirmaFinAutorizacionJob.class);


	@Autowired
	private RequestDAO requestDAO;
	
	@Autowired
	AuthorizationBO autorizacionBO;

	/**
	 * Tarea que pasa al historico las peticiones de hace un año.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("LineasFirmaFinAutorizacionJob: EXECUTING...");
		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		if(autorizacionBO!=null){
			try {
				List<AbstractBaseDTO> listaAutorizacionesActivas = autorizacionBO.queryAuthorizationPeridoRevocar();
				for (AbstractBaseDTO authorization :  listaAutorizacionesActivas) {
					PfUsersAuthorizationDTO autorizacionUser = (PfUsersAuthorizationDTO) authorization;
					//Metodo que obtiene la lista de peticiones a mostrar en la bandeja de peticiones correspondiente.
					PfFiltersDTO filter = new PfFiltersDTO();
					filter.setPfUser(autorizacionUser.getPfUser());
					List<AbstractBaseDTO> resultRequest = requestDAO.queryFilterRequests(filter, null);
					for (AbstractBaseDTO requestTag : resultRequest) {
						PfRequestsDTO  reqTag = (PfRequestsDTO) requestTag;		                                                                  
						// Execute authorizations
						Set<PfRequestTagsDTO> reqTagSet = reqTag.getPfRequestsTags();
						for (PfRequestTagsDTO pfRequestTagsDTO : reqTagSet) {		                                                                  
							// Execute authorizations
							if(pfRequestTagsDTO.getPfRequest().getDreference().contains(autorizacionUser.getEntidad())){
								autorizacionBO.revocarAutorizacionActiva(pfRequestTagsDTO.getPfRequest(), autorizacionUser.getPfUser(), autorizacionUser.getPfAuthorizedUser());
							}
						}
					}
				}
				
				
			}  catch (SecurityException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			} 
		}	
		
		log.debug("LineasFirmaFinAutorizacionJob: EXECUTING END.");
	}

}
