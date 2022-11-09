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

package es.seap.minhap.portafirmas.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.web.beans.Paginator;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ReSignHistoricBO {
	
	@Autowired
	private StorageBO storageBO;
	
	@Autowired
	private ReSignBO reSignBO;

	public RequestTagListDTO getListPaginatedByValidDate(Paginator paginator, Date validDate) throws ClassNotFoundException, SQLException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fecha", validDate);
		
		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();
		int lastPosition = firstPosition + paginator.getPageSize();
		
		return storageBO.getSignsPaginatedByValidDate(validDate, firstPosition, lastPosition);
	}
		
	public void reSign(Long idSign, PfUsersDTO userDTO) throws Throwable {
		//obtenemos la peticion a mover
		Long idPeticion = storageBO.findIdRequestBySign(idSign);
		
		//movemos la peticion a portafirmas
		List<String> peticiones = new ArrayList<String>();
		peticiones.add(idPeticion.toString());
		storageBO.returnFromStorage(peticiones);
		
		try {
			reSignBO.reSign(idSign, userDTO);
		} catch (Exception e) {
			throw e;
		} finally {
			//movemos la peticion al historico
			storageBO.moveToStorage(peticiones);
		}
		
	}
	
}
