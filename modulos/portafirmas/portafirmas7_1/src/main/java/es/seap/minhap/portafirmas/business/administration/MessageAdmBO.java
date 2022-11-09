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

package es.seap.minhap.portafirmas.business.administration;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfMessageScopesDTO;
import es.seap.minhap.portafirmas.domain.PfMessagesDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class MessageAdmBO {
	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private ProvinceBO provinceBO;
	
	/**
	 * Obtiene la lista de servidores existentes
	 * @return lista de servidores
	 */
	public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters("administration.messageQueryAll", null);
	}
	
	/**
	 * Obtiene la lista de servidores existentes
	 * @return lista de servidores
	 */
	public PfMessagesDTO messagesByPk(Long pk) {
		return (PfMessagesDTO) baseDAO.queryElementOneParameter("administration.messageAdmPkQuery", "pk", pk);
	}
	
	
	
	/**
	 * M&eacute;todo que a&ntilde;ade un comentario a una petici&oacute;n.
	 * @param request Petici&oacute;n a la que se Ra&ntilde;ade el comentario.
	 * @param comment Comentario que se desea a&ntilde;adir a la petici&oacute;n.
	 * @param userDTO Usuario que a&ntilde;ade el comentario a la petici&oacute;n.
	 */
	@Transactional
	public void insertMessage(PfMessagesDTO message, String scope, PfUsersDTO currentUser) throws PfirmaException {
		//boolean isCreated = false;
		if(message.getPrimaryKey() == null) {
			// A&ntilde;ade el mensaje a la petici&oacute;n
			message.setCcreated(currentUser.getCidentifier());
			message.setFcreated(Calendar.getInstance().getTime());
			//isCreated = true;
		} else {
			message.setCmodified(currentUser.getCidentifier());
			message.setFmodified(Calendar.getInstance().getTime());
		}
		
		message.setPfMessageScope(getMessageScopeById(Long.valueOf(scope)));
		
		baseDAO.insertOrUpdate(message); 
		
	}
	
	/**
	 * M&eacute;todo que a&ntilde;ade un comentario a una petici&oacute;n.
	 * @param request Petici&oacute;n a la que se Ra&ntilde;ade el comentario.
	 * @param comment Comentario que se desea a&ntilde;adir a la petici&oacute;n.
	 * @param userDTO Usuario que a&ntilde;ade el comentario a la petici&oacute;n.
	 */
	@Transactional
	public void deleteMessage(Long primaryKey) {
		
		PfMessagesDTO messageDTO = messagesByPk(primaryKey);
		if (messageDTO != null) {

			// Eliminamos todas las relaciones PfUsuariosMensaje del mensaje
			baseDAO.deleteList(getUserMessageById(primaryKey));
			
			// Eliminamos el mensaje
			baseDAO.delete(messageDTO);
			
		}
		
	}
	
	
	
	/**
	 * Devuelve el listado de ámbitos de mesajes de difusión
	 * @return Listado de ámbitos
	 */
	public Map<String, Object> getScopeList() {
		List<PfMessageScopesDTO> scopeTypes = baseDAO.queryListOneParameter("administration.scopeMessageTypes", null, null);
		
		Map<String, Object> scopeListMap = new LinkedHashMap<String, Object>();  
		for (PfMessageScopesDTO scope : scopeTypes) {
			scopeListMap.put(scope.getPrimaryKeyString(), scope.getCdescription());			
		}
		
		return scopeListMap;
	}

	
	/**
	 * Devuelve el listado de ámbitos de mesajes de difusión
	 * @return Listado de ámbitos
	 */
	public Map<String, Object> getProvices() {
		List<AbstractBaseDTO> provinceList = provinceBO.getAllProvinces();
		
		Map<String, Object> provincesMap = new LinkedHashMap<String, Object>();  
		for (AbstractBaseDTO province : provinceList) {
			
			provincesMap.put(((PfProvinceDTO) province).getCcodigoprovincia(), ((PfProvinceDTO) province).getCnombre());			
		}
		
		return provincesMap;
	}
	
	/**
	 * Método que obtiene un ámbito de documento a partir de su PK
	 * @param scopeId PK del ámbito
	 * @return Ámbito de documento
	 */
	public PfMessageScopesDTO getMessageScopeById(Long scopeId) {
		return (PfMessageScopesDTO) baseDAO.queryElementOneParameter("administration.scopeMessageTypeById", "idScope", scopeId);
	}
	
	/**
	 * Método que obtiene el 
	 * @param scopeId PK del ámbito
	 * @return Ámbito de documento
	 */
	public List<AbstractBaseDTO> getUserMessageById(Long primaryKey) {
		return baseDAO.queryListOneParameter("administration.usersMessageByPkMessage", "pk", primaryKey);
	}
	

}
