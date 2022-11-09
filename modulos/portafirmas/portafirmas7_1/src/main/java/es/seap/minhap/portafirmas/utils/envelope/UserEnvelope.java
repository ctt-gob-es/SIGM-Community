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

package es.seap.minhap.portafirmas.utils.envelope;

import es.seap.minhap.portafirmas.domain.PfInvitedUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Util;
/**
 * Hace una envoltura para la clase PfUsersDTO
 * @author daniel.palacios
 * @see es.seap.minhap.portafirmas.domain.PfUsersDTO
 */
public class UserEnvelope {
	
	
	String cidentifier;
	String dname;
	String dapell1;
	String dapell2;
	String codProvincia;
	String nombreProvincia;	
	String ctype;
	boolean selected;
	boolean passer;
	boolean validator;
	boolean valid;	
	Long pk;
	String email;
	String accionFirma;
	
	
	public UserEnvelope() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserEnvelope (PfUsersDTO user) {
		super ();
		
		this.cidentifier = user.getCidentifier();
		this.dname = user.getDname();
		this.dapell1 = user.getDsurname1();
		this.dapell2 = user.getDsurname2();
		if (user.getPfProvince() != null) {
			this.codProvincia = user.getPfProvince().getCcodigoprovincia();
			this.nombreProvincia = user.getPfProvince().getCnombre();
		}
		if (user.getPortafirmas() != null) {
			this.codProvincia = user.getPortafirmas().getIdPortafirmas().toString();
			this.nombreProvincia = user.getPortafirmas().getNombre();
		}
		this.passer = user.getPasser();
		this.ctype = user.getCtype();	
		this.validator = user.getValidator();
		
		this.pk = user.getPrimaryKey();
	}
	
	public UserEnvelope (PfInvitedUsersDTO invitedUser) {
		super ();
		this.email = invitedUser.getcMail();
	}
	
	public UserEnvelope (String properties) {
		
		String[] list = properties.split("@#@");
		if (!list[0].contentEquals("null")) {
			cidentifier = list[0];
		}
		if (!list[1].contentEquals("null")) {
			dname = list[1];
		}
		if (!list[2].contentEquals("null")) {
			dapell1 = list[2];
		}
		if (!list[3].contentEquals("null")) {
			dapell2 = list[3];
		}
		if (!list[4].contentEquals("null")) {
			codProvincia = list[4];
		}
		if (!list[5].contentEquals("null")) {
			nombreProvincia = list[5];
		}
		if (!list[6].contentEquals("null")) {
			ctype = list[6];
		}
		if (!list[7].contentEquals("null")) {
			passer = Boolean.parseBoolean(list[7]);
		}
		if (!list[8].contentEquals("null")) {
			validator = Boolean.parseBoolean(list[8]);
		}
		if (!list[9].contentEquals("null")) {
			pk = Long.parseLong(list[9]);
		}
	}

	public String toString () {
		StringBuffer sb = new StringBuffer ("");
		String carSep = "@#@";
		sb.append(cidentifier + carSep);
		sb.append(dname + carSep);
		sb.append(dapell1 + carSep);
		sb.append(dapell2 + carSep);
		sb.append(codProvincia + carSep);
		sb.append(nombreProvincia + carSep);
		sb.append(ctype + carSep);
		sb.append(passer + carSep);
		sb.append(validator + carSep);
		sb.append(pk);
		
		return sb.toString();
	}
	
	
	
	public String getCidentifier() {
		return cidentifier;
	}



	public void setCidentifier(String cidentifier) {
		this.cidentifier = cidentifier;
	}



	public String getDname() {
		return dname;
	}



	public void setDname(String dname) {
		this.dname = dname;
	}



	public String getDapell1() {
		return dapell1;
	}



	public void setDapell1(String dapell1) {
		this.dapell1 = dapell1;
	}



	public String getDapell2() {
		return dapell2;
	}



	public void setDapell2(String dapell2) {
		this.dapell2 = dapell2;
	}



	public String getCodProvincia() {
		return codProvincia;
	}



	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}



	public String getNombreProvincia() {
		return nombreProvincia;
	}



	public void setNombreProvincia(String nombreProvincia) {
		this.nombreProvincia = nombreProvincia;
	}



	public boolean isPasser() {
		return passer;
	}



	public void setPasser(boolean passer) {
		this.passer = passer;
	}
	
	


	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	
	
	

	public boolean isValidator() {
		return validator;
	}

	public void setValidator(boolean validator) {
		this.validator = validator;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	/**
	 * Sobrescribe hashCode para adaptarlo a la obtenci&oacute;n de un
	 * hashcode para usuarios
	 */
	public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime
			* result
			+ ((this.pk == null) ? 0 : this.pk.hashCode());
			return result;

	}
	
	public String getFullNameWithProvince () {
		return Util.getInstance().completeUserNameWithProvince(this);
	}
	
	public String getFullName () {
		return Util.getInstance().completeUserName(this);
	}
	
	@Override
	public boolean equals (Object obj) {
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		UserEnvelope user = (UserEnvelope) obj;
		return user.getPk().equals(this.pk);
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccionFirma() {
		return accionFirma;
	}

	public void setAccionFirma(String accionFirma) {
		this.accionFirma = accionFirma;
	}
	
}
