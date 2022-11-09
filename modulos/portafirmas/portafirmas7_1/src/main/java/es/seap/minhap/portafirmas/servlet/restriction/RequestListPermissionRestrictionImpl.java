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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.SessionBO;
import es.seap.minhap.portafirmas.domain.PfSessionAttributesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RequestListPermissionRestrictionImpl implements Restriction {
	
	@Autowired
	private SessionBO sessionBO;
	
	@Autowired
	private RestrictionUtil restrictionUtil;

	public boolean check(ServletRequest request) {
		HttpServletRequest req = (HttpServletRequest)request;

		//Identificadores de las etiqueta-peticion
		String reqTagStr = req.getParameter("idRequestsTagList");
		
		// Obtenemos los atributos de la sesión
		PfSessionAttributesDTO atts = sessionBO.querySessionAttributes(req.getSession(false).getId());
		
		if (atts == null) {
			return false;
		}

		PfUsersDTO usuarioSesion = atts.getPfUser();
		
		String[] reqTagList = reqTagStr.split(",");
		
		boolean permitido = true;
		int i=0;
		
		while (i<reqTagList.length && permitido) {
			permitido = restrictionUtil.checkRequestTagByHash(reqTagList[i], usuarioSesion);			
			i++;
		}
		
		return permitido;

	}
}
