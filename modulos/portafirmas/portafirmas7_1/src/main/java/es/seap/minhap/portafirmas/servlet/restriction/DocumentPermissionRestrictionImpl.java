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

package es.seap.minhap.portafirmas.servlet.restriction;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.SessionBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfSessionAttributesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class DocumentPermissionRestrictionImpl implements Restriction {
	
	Logger log = Logger.getLogger(SessionAttributesRestrictionImpl.class);
	
	@Autowired
	private SessionBO sessionBO;
	
	
	@Autowired
	private BaseDAO baseDAO;

	public boolean check(ServletRequest request) {
		HttpServletRequest req = (HttpServletRequest)request;

		// Obtengo el contexto de Spring
		//ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getSession().getServletContext());

		//Identificador del documento
		String hash = req.getParameter("idDocument");
		
		// Obtenemos los atributos de la sesión
		//SessionBO sessionBO = appContext.getBean(SessionBO.class);
		PfSessionAttributesDTO atts = sessionBO.querySessionAttributes(req.getSession(false).getId());
		
		if (atts == null) {
			return false;
		}
		
		//BaseDAO baseDAO = appContext.getBean(BaseDAO.class);
		//PfSessionAttributesDTO atts = (PfSessionAttributesDTO) baseDAO.queryElementOneParameter("request.querySessionAttributes", "id", req.getSession(false).getId());
		
		// Usuarios que tienen permisos para descargar el documento.
		List<AbstractBaseDTO> listUsersAbstract = baseDAO.queryListOneParameter("file.documentUsers", "hash", hash);

		// Usuarios que pertenecen al mismo grupo que el usuario con permisos para descargar el documento
		PfUsersDTO owner = (PfUsersDTO) baseDAO.queryElementOneParameter("file.documentOwner", "hash", hash);
		List<AbstractBaseDTO> groupList = baseDAO.queryListOneParameter("request.groupsFromUser", "user", owner);
		for (AbstractBaseDTO group : groupList) {
			List<AbstractBaseDTO> groupUsers = baseDAO.queryListOneParameter("request.usersFromGroup", "group", ((PfGroupsDTO) group));
			listUsersAbstract.addAll(groupUsers);
		}
		
		PfUsersDTO usuarioSesion = atts.getPfUser();
		
		boolean encontrado = false;
		int i = 0;
		while (i < listUsersAbstract.size() && !encontrado) {
			  PfUsersDTO usuarioPermiso = (PfUsersDTO) listUsersAbstract.get(i);
			  if (usuarioPermiso.getPrimaryKey() != null && usuarioPermiso.getPrimaryKey().equals(usuarioSesion.getPrimaryKey())) {
				  encontrado = true;
			  } else {
				  i++;
			  }
		}
		
		return encontrado;
	}
}
