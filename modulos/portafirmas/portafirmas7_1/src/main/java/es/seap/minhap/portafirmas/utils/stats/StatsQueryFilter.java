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

package es.seap.minhap.portafirmas.utils.stats;

import java.util.List;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;

/**
 * Representa un objeto donde se agrupan todos los filtros de las estadísticas.
 * @author rus
 *
 */
public class StatsQueryFilter {
	
	private String fInicio;
	private String fFin;
	private String pkAplicacion;
	private String pkUsuario;
	private List<AbstractBaseDTO> sedesList;
	
	
	
	public StatsQueryFilter(String fInicio, String fFin, String pkAplicacion,
			String pkUsuario, List<AbstractBaseDTO> sedesList) {
		super();
		this.fInicio = fInicio;
		this.fFin = fFin;
		this.pkAplicacion = pkAplicacion;
		this.pkUsuario = pkUsuario;
		this.sedesList = sedesList;
	}
	public String getfInicio() {
		return fInicio;
	}
	public void setfInicio(String fInicio) {
		this.fInicio = fInicio;
	}
	public String getfFin() {
		return fFin;
	}
	public void setfFin(String fFin) {
		this.fFin = fFin;
	}
	public String getPkAplicacion() {
		return pkAplicacion;
	}
	public void setPkAplicacion(String pkAplicacion) {
		this.pkAplicacion = pkAplicacion;
	}
	public String getPkUsuario() {
		return pkUsuario;
	}
	public void setPkUsuario(String pkUsuario) {
		this.pkUsuario = pkUsuario;
	}
	public List<AbstractBaseDTO> getSedesList() {
		return sedesList;
	}
	public void setSedesList(List<AbstractBaseDTO> sedesList) {
		this.sedesList = sedesList;
	}
	
	
	  

}
