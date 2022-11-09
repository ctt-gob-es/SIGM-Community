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

/**
 * @author domingo.sanchez
 * @see es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO
 */
public class Seat {
	
	
	private Long primaryKey;
	private String code;
	private String name;
	private boolean longDuration;
	private String urlCSV;
	private boolean ldap;
	
	public Seat() {
		super();
	}
	
	public Seat(String seatCode, String seatName, boolean longDuration, String seatURLCSV, boolean ldap) {
		super();
		this.code = seatCode;
		this.name = seatName;
		this.longDuration = longDuration;
		this.urlCSV = seatURLCSV;
		this.ldap = ldap;
	}
	
	public Seat(String seatCode, String seatName) {
		super();
		this.code = seatCode;
		this.name = seatName;
	}
	
	public Long getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isLongDuration() {
		return longDuration;
	}

	public void setLongDuration(boolean longDuration) {
		this.longDuration = longDuration;
	}
	
	public boolean isLdap() {
		return ldap;
	}

	public void setLdap(boolean ldap) {
		this.ldap = ldap;
	}

	public String getUrlCSV() {
		return urlCSV;
	}

	public void setUrlCSV(String urlCSV) {
		this.urlCSV = urlCSV;
	}
	
	
}
