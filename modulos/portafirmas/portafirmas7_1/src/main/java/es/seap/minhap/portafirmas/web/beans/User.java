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

public class User {

	private String primaryKey;
	private String nif;
	private String name;
	private String lastName1;
	private String lastName2;
	private String password;
	private String ldapId;
	private String province;
	private String provinceName;
	private String valid;
	private String publico;
	private String [] profiles;
	private String [] adminSeats;
	private String group;	
	private String adminOrg;
	private String tipoUsuario;
	private String email;
	
	public User() {
		super();
		primaryKey = "";
		nif = "";
		name = "";
		lastName1 = "";
		lastName2 = "";
		password = "";
		ldapId = "";
		province = "";
		valid = "";
		publico = "";
		profiles = new String[0];
		adminSeats = new String[0];
		group = "";
		adminOrg = "";
		tipoUsuario = "";
		email = "";
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName1() {
		return lastName1;
	}
	public void setLastName1(String lastName1) {
		this.lastName1 = lastName1;
	}
	public String getLastName2() {
		return lastName2;
	}
	public void setLastName2(String lastName2) {
		this.lastName2 = lastName2;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLdapId() {
		return ldapId;
	}
	public void setLdapId(String ldapId) {
		this.ldapId = ldapId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getPublico() {
		return publico;
	}
	public void setPublico(String publico) {
		this.publico = publico;
	}
	public String [] getProfiles() {
		return profiles;
	}
	public void setProfiles(String [] profiles) {
		this.profiles = profiles;
	}
	public String [] getAdminSeats() {
		return adminSeats;
	}
	public void setAdminSeats(String [] adminSeats) {
		this.adminSeats = adminSeats;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getAdminOrg() {
		return adminOrg;
	}
	public void setAdminOrg(String adminOrg) {
		this.adminOrg = adminOrg;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
