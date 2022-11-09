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

package es.seap.minhap.portafirmas.web.beans;

import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Util;
/**
 * Hace una envoltura para la clase PfUsersAuthorizationDTO
 * @author domingo.sanchez
 * @see es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO
 */
public class UserAuthorization {
	
	
	private Long primaryKey;
	private String authorizationType;
	private String authorizationTypeId;
	private String frequest;
	private String hrequest;
	private String fauthorization;
	private String frevocation;
	private String hrevocation;
	private String tobservations;
	private String remittent = "";
	private String remittentId = "";
	private String receiver = "";
	private String receiverId = "";
	
	private String state;
	private boolean output;
	private boolean isRevocable;
	
	public UserAuthorization(PfUsersAuthorizationDTO userAuthorized, PfUsersDTO user) {
		super();
		this.primaryKey = userAuthorized.getPrimaryKey();
		String aux = userAuthorized.getPfAuthorizationType().getCauthorizationType();
		this.authorizationType = aux.substring(0,1).toUpperCase() + aux.substring(1).toLowerCase();
		this.authorizationTypeId = userAuthorized.getPfAuthorizationType().getPrimaryKeyString();
		this.frequest = Util.vacioSiNulo(Util.getInstance().dateToString(userAuthorized.getFrequest()));
		this.hrequest = Util.vacioSiNulo(Util.getInstance().dateToString(userAuthorized.getFrequest(),"HH:mm:ss"));
		this.fauthorization =  Util.vacioSiNulo(Util.getInstance().dateToString(userAuthorized.getFauthorization(),"dd/MM/yyyy HH:mm:ss"));
		this.frevocation =  Util.vacioSiNulo(Util.getInstance().dateToString(userAuthorized.getFrevocation()));
		this.hrevocation =  Util.vacioSiNulo(Util.getInstance().dateToString(userAuthorized.getFrevocation(),"HH:mm:ss"));
		this.tobservations =  Util.vacioSiNulo(userAuthorized.getTobservations());
		if(userAuthorized.getPfUser() != null) {
			this.remittent = userAuthorized.getPfUser().getFullName();
		}
		if(userAuthorized.getPfAuthorizedUser() != null) {
			this.receiver = userAuthorized.getPfAuthorizedUser().getFullName();
			this.receiverId = userAuthorized.getPfAuthorizedUser().getPrimaryKeyString();
		}
		this.state = userAuthorized.getState();
		this.setOutput(userAuthorized.getPfUser().getPrimaryKey().equals(user.getPrimaryKey()));
	}

	public UserAuthorization() {
		super();
	}

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getFrequest() {
		return frequest;
	}

	public void setFrequest(String frequest) {
		this.frequest = frequest;
	}

	public String getFauthorization() {
		return fauthorization;
	}

	public void setFauthorization(String fauthorization) {
		this.fauthorization = fauthorization;
	}

	public String getFrevocation() {
		return frevocation;
	}

	public void setFrevocation(String frevocation) {
		this.frevocation = frevocation;
	}

	public String getTobservations() {
		return tobservations;
	}

	public void setTobservations(String tobservations) {
		this.tobservations = tobservations;
	}

	public String getRemittent() {
		return remittent;
	}

	public void setRemittent(String remittent) {
		this.remittent = remittent;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isOutput() {
		return output;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}

	public String getAuthorizationType() {
		return authorizationType;
	}

	public void setAuthorizationType(String authorizationType) {
		this.authorizationType = authorizationType;
	}

	public String getHrequest() {
		return hrequest;
	}

	public void setHrequest(String hrequest) {
		this.hrequest = hrequest;
	}

	public String getHrevocation() {
		return hrevocation;
	}

	public void setHrevocation(String hrevocation) {
		this.hrevocation = hrevocation;
	}

	public String getAuthorizationTypeId() {
		return authorizationTypeId;
	}

	public void setAuthorizationTypeId(String authorizationTypeId) {
		this.authorizationTypeId = authorizationTypeId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getRemittentId() {
		return remittentId;
	}

	public void setRemittentId(String remittentId) {
		this.remittentId = remittentId;
	}

	public boolean isRevocable() {
		return isRevocable;
	}

	public void setRevocable(boolean isRevocable) {
		this.isRevocable = isRevocable;
	}

}
