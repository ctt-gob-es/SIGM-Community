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

package es.seap.minhap.portafirmas.web.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.configuration.AuthorizationBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.UserAuthorization;

@Component
public class UserAuthorizationConverter {
	
	@Autowired
	private AuthorizationBO authorizationBO;

	/**
	 * @param userAuthorization
	 * @return
	 */
	public PfUsersAuthorizationDTO envelopeToDTO(UserAuthorization userAuthorization) {
		PfUsersAuthorizationDTO pfUsersAuthorizationDTO = null;
		if(userAuthorization.getPrimaryKey() == null) {
			pfUsersAuthorizationDTO = new PfUsersAuthorizationDTO();
		} else {
			pfUsersAuthorizationDTO = authorizationBO.queryAuthorizationByPkTrans(userAuthorization.getPrimaryKey());
		}
		pfUsersAuthorizationDTO.setPrimaryKey(userAuthorization.getPrimaryKey());
		pfUsersAuthorizationDTO.setFrequest(Util.getInstance().dateComplete(userAuthorization.getFrequest(), userAuthorization.getHrequest()));
		pfUsersAuthorizationDTO.setFrevocation(Util.getInstance().dateComplete(userAuthorization.getFrevocation(), userAuthorization.getHrevocation()));
		pfUsersAuthorizationDTO.setTobservations(userAuthorization.getTobservations());
		
		return pfUsersAuthorizationDTO;
	}

	/**
	 * @param userAuthorization
	 * @return
	 */
	public PfUsersAuthorizationDTO envelopeToDTOUpdate(UserAuthorization userAuthorization) {
		PfUsersAuthorizationDTO pfUsersAuthorizationDTO = authorizationBO.queryAuthorizationByPkTrans(userAuthorization.getPrimaryKey());

		if(Util.esVacioONulo(userAuthorization.getFrevocation())) {
			pfUsersAuthorizationDTO.setFrevocation(null);
		} else {
			pfUsersAuthorizationDTO.setFrevocation(
				Util.getInstance().dateComplete(userAuthorization.getFrevocation(), userAuthorization.getHrevocation()));
		}
		
		return pfUsersAuthorizationDTO;
	}

	/**
	 * @param authorizationDTO
	 * @return
	 */
	public UserAuthorization DTOtoEnvelope(PfUsersAuthorizationDTO authorizationDTO) {
		Util util = Util.getInstance();
		UserAuthorization authorization = new UserAuthorization();
		authorization.setPrimaryKey(authorizationDTO.getPrimaryKey());
		
		authorization.setAuthorizationType(authorizationDTO.getPfAuthorizationType().getCauthorizationType());
		authorization.setAuthorizationTypeId(authorizationDTO.getPfAuthorizationType().getPrimaryKeyString());
		
		authorization.setRemittent(authorizationDTO.getPfUser().getFullName());
		authorization.setRemittentId(authorizationDTO.getPfUser().getPrimaryKeyString());
		
		authorization.setReceiver(authorizationDTO.getPfAuthorizedUser().getFullName());
		authorization.setReceiverId(authorizationDTO.getPfAuthorizedUser().getFullName());
		
		Date fRequest = authorizationDTO.getFrequest();
		authorization.setFrequest(util.dateToString(fRequest));
		authorization.setHrequest(util.dateToString(fRequest, Util.HOUR_FORMAT));
		if(Util.esVacioONulo(authorization.getHrequest())) {
			authorization.setHrevocation("00:00:00");
		}
		
		Date fAuthorization = authorizationDTO.getFauthorization();
		authorization.setFauthorization(util.dateToString(fAuthorization,Util.EXTENDED_DATE_FORMAT));
		
		Date fRevocation = authorizationDTO.getFrevocation();
		authorization.setFrevocation(util.dateToString(fRevocation));
		authorization.setHrevocation(util.dateToString(fRevocation, Util.HOUR_FORMAT));
		if(Util.esVacioONulo(authorization.getHrevocation())) {
			authorization.setHrevocation("00:00:00");
		}
		
		authorization.setState(authorizationDTO.getState());
		authorization.setTobservations(authorizationDTO.getTobservations());
		
		boolean isRevocable = Util.esVacioONulo(authorizationDTO.getFrevocation())
				|| authorizationDTO.getFrevocation().after(new Date());
		authorization.setRevocable(isRevocable);
		
		return authorization;
	}

	/**
	 * @param sendListDTO
	 * @return
	 */
	public List<UserAuthorization> DTOtoEnvelopeList(List<AbstractBaseDTO> sendListDTO) {
		ArrayList<UserAuthorization> returnList = new ArrayList<UserAuthorization>();
		for (Iterator<AbstractBaseDTO> it = sendListDTO.iterator(); it.hasNext();) {
			PfUsersAuthorizationDTO usersAuthorizationDTO = (PfUsersAuthorizationDTO) it.next();
			returnList.add(DTOtoEnvelope(usersAuthorizationDTO));
		}
		return returnList;
	}

}
