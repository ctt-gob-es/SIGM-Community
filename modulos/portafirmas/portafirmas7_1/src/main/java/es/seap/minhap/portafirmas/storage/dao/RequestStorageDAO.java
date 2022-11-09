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

package es.seap.minhap.portafirmas.storage.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import es.seap.minhap.portafirmas.storage.domain.StoredRequest;
import es.seap.minhap.portafirmas.web.beans.Paginator;

public interface RequestStorageDAO extends Serializable {

	public void moveToStorage(List<String> requests) throws Throwable;

	public void returnFromStorage(List<String> requests) throws Throwable;

	public List<StoredRequest> queryStoredRequests(Map<String,String> filters, Paginator paginator);

	public List<StoredRequest> queryRequests(Map<String,String> filters, Paginator paginator);

	public void deleteForever(List<String> requests) throws Throwable;

	public List<StoredRequest> queryRequestsBySigner(Map<String,String> filters, String signerQuery, Paginator paginator);

	public List<StoredRequest> queryStoredRequestsBySigner(Map<String,String> filters, String signerQuery, Paginator paginator);

}
