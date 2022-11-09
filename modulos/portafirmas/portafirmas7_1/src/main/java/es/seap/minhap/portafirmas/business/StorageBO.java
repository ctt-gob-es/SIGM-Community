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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.storage.dao.JdbcRequestStorageDAO;
import es.seap.minhap.portafirmas.storage.domain.StoredRequest;
import es.seap.minhap.portafirmas.web.beans.Paginator;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class StorageBO {

	@Autowired
	private JdbcRequestStorageDAO jdbcRequestStorageDAO;

	/**
	 * Método que mueve una lista de peticiones del Portafirmas al histórico
	 * @param requests Lista de peticiones
	 * @throws SQLException 
	 */
	public void moveToStorage(List<String> requestIds) throws Throwable {
		jdbcRequestStorageDAO.moveToStorage(requestIds);
	}

	/**
	 * Método que recupera una lista de peticiones del histórico
	 * @param requests Lista de peticiones
	 * @throws SQLException
	 */
	public void returnFromStorage(List<String> requestIds) throws Throwable {
		jdbcRequestStorageDAO.returnFromStorage(requestIds);
	}

	/**
	 * Método que recupera las peticiones terminadas de Portafirmas
	 * @param filters Filtro sobre las peticiones terminadas
	 * @return Listado de peticiones terminadas
	 */
	public List<StoredRequest> queryRequests(Map<String,String> filters, Paginator paginator) {
		return jdbcRequestStorageDAO.queryRequests(filters, paginator);
	}

	/**
	 * Método que recupera las peticiones terminadas de Portafirmas
	 * @param filters Filtro sobre las peticiones terminadas
	 * @return Listado de peticiones terminadas
	 */
	public List<StoredRequest> queryStoredRequests(Map<String,String> filters, Paginator paginator) {
		return jdbcRequestStorageDAO.queryStoredRequests(filters, paginator);
	}

	/**
	 * Método que recupera las peticiones firmadas de Portafirma con un filtro de firmantes
	 * @param filters Filtro sobre las peticiones terminadas
	 * @param signerQuery Filtro sobre firmantes
	 * @return Listado de peticiones terminadas
	 */
	public List<StoredRequest> queryRequestsBySigner(Map<String,String> filters, String signerQuery, Paginator paginator) {
		return jdbcRequestStorageDAO.queryRequestsBySigner(filters, signerQuery, paginator);
	}

	/**
	 * Método que recupera las peticiones firmadas del histórico con un filtro de firmantes
	 * @param filters Filtro sobre las peticiones terminadas
	 * @param signerQuery Filtro sobre firmantes
	 * @return Listado de peticiones terminadas
	 */
	public List<StoredRequest> queryStoredRequestsBySigner(Map<String,String> filters, String signerQuery, Paginator paginator) {
		return jdbcRequestStorageDAO.queryStoredRequestsBySigner(filters, signerQuery, paginator);
	}

	/**
	 * Método que elimina definitivamente (irrevocable) un listado de peticiones del histórico
	 * @param requests Listado de peticiones
	 * @throws SQLException
	 */
	public void deleteForever(List<String> requestIds) throws Throwable {
		jdbcRequestStorageDAO.deleteForever(requestIds);
	}
	
	public Long findIdRequestBySign(Long idSign) throws ClassNotFoundException, SQLException {
		return jdbcRequestStorageDAO.findIdRequestBySign(idSign);
	}
	
	public RequestTagListDTO getSignsPaginatedByValidDate(Date validDate, int firstPosition, int maxResults) throws ClassNotFoundException, SQLException {
		return jdbcRequestStorageDAO.getSignsPaginatedByValidDate(validDate, firstPosition, maxResults);
	}
}
