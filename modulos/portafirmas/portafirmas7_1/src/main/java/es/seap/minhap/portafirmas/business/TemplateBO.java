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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfRequestTemplatesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class TemplateBO {


	Logger log = Logger.getLogger(RequestBO.class);

	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Método que devuele las plantillas guardadas por un usuario
	 * @param user Usuario
	 * @return Listado de plantillas
	 */
	public List<PfRequestTemplatesDTO> getUserTemplates(PfUsersDTO user) {
		return baseDAO.queryListOneParameter("request.userTemplates", "user", user);
	}

	/**
	 * Método que carga una plantilla de petición de un usuario
	 * @param templateId Identificador de plantilla
	 * @param  user usuario autenticado
	 * @return Plantilla de petición
	 */
	public PfRequestTemplatesDTO loadTemplateUser(Long templateId, PfUsersDTO user) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("templateId", templateId);
		queryParams.put("user", user);
		return (PfRequestTemplatesDTO) baseDAO.queryElementMoreParameters("request.loadTemplateUser", queryParams);
	}
	
	/**
	 * Método que guarda una plantilla de petición
	 * @param template Plantilla de petición
	 */
	@Transactional(readOnly = false)
	public void saveTemplate(PfRequestTemplatesDTO template) {
		log.debug("savetemplate: " + template.getPrimaryKeyString());
		baseDAO.insertOrUpdate(template);
	}

	/**
	 * Método que elimina una plantilla de petición
	 * @param template Plantilla de petición
	 */
	@Transactional(readOnly = false)
	public void deleteTemplate(PfRequestTemplatesDTO template) {
		log.debug("deleteTemplate: " + template.getPrimaryKeyString());
		baseDAO.delete(template);
		baseDAO.flush();
	}
}
