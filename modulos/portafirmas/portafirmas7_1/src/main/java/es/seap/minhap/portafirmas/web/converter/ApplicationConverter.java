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

package es.seap.minhap.portafirmas.web.converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.administration.ApplicationAdmBO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.web.beans.Application;

@Component
public class ApplicationConverter {

	@Autowired
	private ApplicationAdmBO appBO;

	protected final Log log = LogFactory.getLog(getClass());
	
	@Transactional(readOnly=false)
	public PfApplicationsDTO envelopeToDTO(Application application){
		PfApplicationsDTO pfApplicationsDTO;
		if (application.getPrimaryKey() == null) {
			pfApplicationsDTO = new PfApplicationsDTO();
		} else {
			pfApplicationsDTO = (PfApplicationsDTO) appBO.applicationPkQuery(application.getPrimaryKey());
		}
		//Setteo de los campos de aplicacion obtenidos de la vista
		if(pfApplicationsDTO != null){
		
			pfApplicationsDTO.setCapplication(application.getAppCode());
			
			pfApplicationsDTO.setDapplication(application.getAppName());
			
			pfApplicationsDTO.setPfConfiguration((PfConfigurationsDTO) appBO.configurationPkQuery(application.getPfConfiguration()));
			
			pfApplicationsDTO.setPfApplication((PfApplicationsDTO)appBO.applicationPkQuery((Long.parseLong(application.getAppParent()))));
			
			if(pfApplicationsDTO.getVisibleEnPortaFirmaWeb()==null){
				pfApplicationsDTO.setVisibleEnPortaFirmaWeb(Long.valueOf(0));
			}
		}
		
		return pfApplicationsDTO;
	}
}
