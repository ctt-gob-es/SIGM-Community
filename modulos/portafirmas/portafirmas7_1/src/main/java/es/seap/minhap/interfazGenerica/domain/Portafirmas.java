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

package es.seap.minhap.interfazGenerica.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.seap.minhap.portafirmas.domain.PfUsersDTO;

@Entity
@Table(name = "pf_portafirmas")
public class Portafirmas extends AbstractBaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idPortafirmas;
	private String nombre;
	private String dir3;
	private String urlInterfaz;
	private String urlNotificacion;
	private String cPortafirmas;
	private Short soapVersion;
	private Set<PfUsersDTO> usuarios;

	public Portafirmas() {
		super();
		this.nombre = "";
		this.urlInterfaz = "";
		this.urlNotificacion = "";
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portafirmas_generator")
	@SequenceGenerator(name = "portafirmas_generator", sequenceName = "pf_portafirmas_seq", allocationSize = 1)
	@Column(name = "ID_PORTAFIRMAS", nullable=false)
	public Long getIdPortafirmas() {
		return idPortafirmas;
	}
	public void setIdPortafirmas(Long idPortafirmas) {
		this.idPortafirmas = idPortafirmas;
	}

	@Column(nullable=false)
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(nullable=false)
	public String getDir3() {
		return dir3;
	}

	public void setDir3(String dir3) {
		this.dir3 = dir3;
	}

	@Column(name = "URL_INTERFAZ", nullable=false)
	public String getUrlInterfaz() {
		return urlInterfaz;
	}
	public void setUrlInterfaz(String urlInterfaz) {
		this.urlInterfaz = urlInterfaz;
	}
	
	@Column(name = "URL_NOTIFICACION", nullable=false)
	public String getUrlNotificacion() {
		return urlNotificacion;
	}
	public void setUrlNotificacion(String urlNotificacion) {
		this.urlNotificacion = urlNotificacion;
	}
	
	@Column(name = "C_PORTAFIRMAS", nullable=false)
	public String getcPortafirmas() {
		return cPortafirmas;
	}

	public void setcPortafirmas(String cPortafirmas) {
		this.cPortafirmas = cPortafirmas;
	}

	@Column(name = "SOAP_VERSION", nullable=false)
	public Short getSoapVersion() {
		return soapVersion;
	}

	public void setSoapVersion(Short soapVersion) {
		this.soapVersion = soapVersion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "portafirmas", cascade=CascadeType.REMOVE)
	public Set<PfUsersDTO> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(Set<PfUsersDTO> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public String toString() {
		return "Portafirmas [idPortafirmas=" + idPortafirmas + ", nombre=" + nombre + ", urlInterfaz=" + urlInterfaz
				+ ", urlNotificacion=" + urlNotificacion + ", soapVersion=" + soapVersion + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Portafirmas other = (Portafirmas) obj;
		if (idPortafirmas != other.idPortafirmas)
			return false;
		return true;
	}

}
