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

import es.seap.minhap.portafirmas.domain.PfOrganismoDIR3DTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Util;

public class SeatAutocomplete {

	private String label;
	private String id;
	private String denom;
	private String codigoDIR3;

	
	public SeatAutocomplete(PfOrganismoDIR3DTO organismoDIR3) {
		super();
		this.id = organismoDIR3.getPrimaryKeyString();
		String descripcion = "";
		if(organismoDIR3.getDenominacion() != null && !Util.esVacioONulo(organismoDIR3.getDenominacion())) {
			descripcion = " [" + organismoDIR3.getDenominacion() + "]";
		}
		this.denom = organismoDIR3.getDenominacion();
		this.codigoDIR3 = organismoDIR3.getCodigo();
		this.label = organismoDIR3.getCodigo() + descripcion;
	}

	public String getCodigoDIR3() {
		return codigoDIR3;
	}

	public void setCodigoDIR3(String codigoDIR3) {
		this.codigoDIR3 = codigoDIR3;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDenom() {
		return denom;
	}

	public void setDenom(String denom) {
		this.denom = denom;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatAutocomplete other = (SeatAutocomplete) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
		
}
