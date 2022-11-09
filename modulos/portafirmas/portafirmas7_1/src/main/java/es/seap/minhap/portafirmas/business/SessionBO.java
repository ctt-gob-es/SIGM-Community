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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfSessionAttributesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SessionBO {
		
		@Autowired
		private BaseDAO baseDAO;

		/**
		 * Inserta en la base de datos los atributos de una sesión.
		 * @sessionID identificador de sesión
		 * @ip IP Dirección IP
		 * @userAgent user-agent del cliente
		 * @userName Nombre de usuario.
		 */
		@Transactional(readOnly = false)
		public void insertSessionAttributes(String sessionId, 
											String ip, 
											String userAgent, 
											String userName,
											PfUsersDTO pfUser) {
			PfSessionAttributesDTO atts = querySessionAttributes(sessionId);
			if(atts == null) {
				atts = new PfSessionAttributesDTO();
			}
			atts.setCsessionid(sessionId);
			atts.setCip(ip);
			atts.setCuseragent(userAgent);
			atts.setCusername(userName);
			atts.setPfUser(pfUser);
			baseDAO.insertOrUpdate(atts);
		}

		/**
		 * Elimina de la base de datos los atributos de una sesión.Inserta en la base de datos los atributos de una sesión.
		 * @sessionID identificador de sesión
		 */
		@Transactional(readOnly = false)
		public void deleteSessionAttributes(String sessionId) {
			PfSessionAttributesDTO atts = (PfSessionAttributesDTO) baseDAO.queryElementOneParameter
					("request.querySessionAttributes",
					"id", 
					sessionId);
			
			if (atts != null) {
				baseDAO.delete(atts);
			}
		}
		
		/**
		 * Elimina de la base de datos los atributos de una sesión.Inserta en la base de datos los atributos de una sesión.
		 * @sessionID identificador de sesión
		 */
		@Transactional(readOnly = false)
		public void deleteSessionAttributesList(List<AbstractBaseDTO> list) {
			baseDAO.deleteList(list);
		}
		
		
		public PfSessionAttributesDTO querySessionAttributes (String sessionId) {
			/*PfSessionAttributesDTO atts = (PfSessionAttributesDTO) baseDAO.queryElementOneParameter
			("request.querySessionAttributes",
			"id", 
			sessionId);
			
			return atts;*/
			
			PfSessionAttributesDTO atts = null;
			
			List<AbstractBaseDTO> list = baseDAO.queryListOneParameter("request.querySessionAttributes",
					"id", 
					sessionId);
			
			if (list != null && list.size() >0) {
				atts = (PfSessionAttributesDTO) list.get(0);
			}
			return atts;
	
		}
		
		public List<AbstractBaseDTO> queryOldSessions (Date date) {
			List<AbstractBaseDTO> list = baseDAO.queryListOneParameter
			("request.queryOldSessions",
			"date", 
			date);
			
			return list;
		}

}
