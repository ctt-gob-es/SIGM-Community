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

package es.seap.minhap.portafirmas.business.metadata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfApplicationsMetadataDTO;
import es.seap.minhap.portafirmas.utils.UtilComponent;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApplicationMetadataBO {

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	UtilComponent util;
	
	public List<PfApplicationsMetadataDTO> getMetadatasByAplicacion(
			String idAplicacion) {
		return baseDAO.queryListOneParameter(
				"request.applicationMetadasByApplication", "idApplication",
				idAplicacion);
	}
	
	public List<String> getMetadatasNameByAplicacion(String idAplicacion) {
		List<String> retorno = new ArrayList<String>();
		
		List<PfApplicationsMetadataDTO> metadatas = baseDAO.queryListOneParameter(
				"request.aditionalMetadasByApplication", "idApplication",
				idAplicacion);
		
		if (util.isNotEmpty(metadatas)) {
			for (PfApplicationsMetadataDTO metadata : metadatas) {
				retorno.add(metadata.getDname());
			}
		}
		
		return retorno;
	}
}
