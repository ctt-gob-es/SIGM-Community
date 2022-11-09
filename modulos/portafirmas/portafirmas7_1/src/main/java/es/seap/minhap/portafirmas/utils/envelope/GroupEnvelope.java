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

import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
/**
 * Hace una envoltura para la clase PfGroupsDTO
 * @see es.seap.minhap.portafirmas.domain.PfGroupsDTO
 */
public class GroupEnvelope {

	private String cnombre;
	private Long pk;

	public GroupEnvelope() {

	}

	public GroupEnvelope(PfGroupsDTO group) {
		super();
		this.cnombre = group.getCnombre();
		this.pk = group.getPrimaryKey();
	}

	public GroupEnvelope (String properties) {
		
		String[] list = properties.split("@#@");
		if (!list[0].contentEquals("null")) {
			cnombre = list[0];
		}
		if (!list[1].contentEquals("null")) {
			pk = Long.parseLong(list[1]);
		}
	}

	public String toString () {
		StringBuffer sb = new StringBuffer ("");
		String carSep = "@#@";
		sb.append(cnombre + carSep);
		sb.append(pk);
		
		return sb.toString();
	}

	@Override
	/**
	 * Sobrescribe equals para adaptarlo a la comparaci&oacute;n de usuarios
	 */
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this.getClass() != obj.getClass()) return false;
		GroupEnvelope group = (GroupEnvelope) obj;
		return group.getPk().equals(this.pk);
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

	public String getCnombre() {
		return cnombre;
	}

	public void setCnombre(String cnombre) {
		this.cnombre = cnombre;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

}
