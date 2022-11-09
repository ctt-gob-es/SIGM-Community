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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RestrictionUtil {
	
	@Autowired
	private BaseDAO baseDAO;

	public boolean checkRequestTagByHash (String reqTagHash, PfUsersDTO user) {
	
		List<AbstractBaseDTO> docsList = baseDAO.queryListOneParameter(
				"request.documentsAllByReqTagHash", "requestTag", reqTagHash);
		
		for (AbstractBaseDTO doc : docsList) {
			// Usuarios que tienen permisos para descargar el documento.
			List<AbstractBaseDTO> listUsersAbstract = baseDAO.queryListOneParameter("file.documentUsers.1", "hash", ((PfDocumentsDTO) doc).getChash());			
			
			boolean encontrado = false;
			int i = 0;

			while (i < listUsersAbstract.size() && !encontrado) {
				  PfUsersDTO usuarioPermiso = (PfUsersDTO) listUsersAbstract.get(i);
				  if (usuarioPermiso.getPrimaryKey() != null &&usuarioPermiso.getPrimaryKey().equals(user.getPrimaryKey())) {
					  encontrado = true;
					  break;
				  } else {
					  i++;
				  }
			}

			if (!encontrado) {
				//return false;
				listUsersAbstract = baseDAO.queryListOneParameter("file.documentUsers.2", "hash", ((PfDocumentsDTO) doc).getChash());			
				
				i = 0;

				while (i < listUsersAbstract.size() && !encontrado) {
					  PfUsersDTO usuarioPermiso = (PfUsersDTO) listUsersAbstract.get(i);
					  if (usuarioPermiso.getPrimaryKey() != null &&usuarioPermiso.getPrimaryKey().equals(user.getPrimaryKey())) {
						  encontrado = true;
						  break;
					  } else {
						  i++;
					  }
				}
				
				if(!encontrado){
					listUsersAbstract = baseDAO.queryListOneParameter("file.documentUsers.3", "hash", ((PfDocumentsDTO) doc).getChash());			
					
					i = 0;

					while (i < listUsersAbstract.size() && !encontrado) {
						  PfUsersDTO usuarioPermiso = (PfUsersDTO) listUsersAbstract.get(i);
						  if (usuarioPermiso.getPrimaryKey() != null &&usuarioPermiso.getPrimaryKey().equals(user.getPrimaryKey())) {
							  encontrado = true;
							  break;
						  } else {
							  i++;
						  }
					}
					
					if(!encontrado){
						listUsersAbstract = baseDAO.queryListOneParameter("file.documentUsers.4", "hash", ((PfDocumentsDTO) doc).getChash());			
						
						i = 0;

						while (i < listUsersAbstract.size() && !encontrado) {
							  PfUsersDTO usuarioPermiso = (PfUsersDTO) listUsersAbstract.get(i);
							  if (usuarioPermiso.getPrimaryKey() != null &&usuarioPermiso.getPrimaryKey().equals(user.getPrimaryKey())) {
								  encontrado = true;
								  break;
							  } else {
								  i++;
							  }
						}
						
						if(!encontrado){
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}

		
}
