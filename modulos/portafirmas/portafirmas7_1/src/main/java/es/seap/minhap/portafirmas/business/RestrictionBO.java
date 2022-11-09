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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.administration.UpdateRequestAdmBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.User;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RestrictionBO {

	@Autowired
	private BaseDAO baseDAO;
	
	@Resource(name = "messageProperties")
	private Properties messages;
	
	private Logger log = LogManager.getLogger(UpdateRequestAdmBO.class);

	public List<PfUsersDTO> queryUserRestrict (PfUsersDTO userRestrict) {		
		List<PfUsersDTO> list = baseDAO.queryListOneParameter("request.queryRestrictUsers", "userRestrict", userRestrict);
		return list;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public ArrayList<String> saveRestrinction(String idUserRestringido, String idUserValido) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			// Se realiza la persistencia del usuario
			saveRestrict(idUserRestringido, idUserValido);
			
		} catch (Exception e) {
			log.error("Error al insertar el cargo: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		return errors;
	}
	
	public void validate(User job, ArrayList<String> errors) {
		if(Util.esVacioONulo(job.getNif())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("code")));
		} else if(Util.vacioSiNulo(job.getNif()).length() > 30) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("code"), 30));
		}
		if(Util.esVacioONulo(job.getName())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("description")));
		} else if(Util.vacioSiNulo(job.getName()).length() > 50) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("code"), 50));
		}
		if(Util.esVacioONulo(job.getProvince())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("province")));
		}
	}
	
	/**
	 * Se graba el usuario valido
	 * @param pfJobDTO
	 */
	@Transactional(readOnly = false)
	public void saveRestrict(String idUserRestringido, String idUserValido) {
		String insertUserRestriction = " INSERT INTO PF_USUARIOS_RESTRINGIDOS VALUES "
				+ "( " + idUserRestringido + "," + idUserValido + ")";
		baseDAO.invokeDdl(insertUserRestriction);
	}
	
	/**
	 * Borra el usuario valido del cargo
	 * @param pfJobDTO
	 */
	@Transactional(readOnly = false)
	public void deleteRestrict(String idUserRestringido, String idUserValido) {
		String insertUserRestriction = " DELETE FROM PF_USUARIOS_RESTRINGIDOS WHERE  "
				+ " USU_X_USUARIO_RESTRINGIDO = " + idUserRestringido + " "
				+ "AND USU_X_USUARIO_VALIDO = " + idUserValido;
		baseDAO.invokeDdl(insertUserRestriction);
	}
}

