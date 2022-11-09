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

import java.util.Date;

import es.seap.minhap.portafirmas.domain.PfOrganismoDIR3DTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;

public class SeatOrgDIR3 {

	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cnombre;
	private String ccodigoprovincia;
	private Boolean lLargaDuracion;
	private String cUrlCSV;
	private Boolean lLdap;
	private String denominacion;
	
	private PfOrganismoDIR3DTO organismoDIR3;

	public SeatOrgDIR3 (PfProvinceDTO sede) {
		this.primaryKey = sede.getPrimaryKey();
		this.ccreated = sede.getCcreated();
		this.fcreated = sede.getFcreated();
		this.cmodified = sede.getCmodified();
		this.fmodified = sede.getFmodified();
		this.cnombre = sede.getCnombre();
		this.ccodigoprovincia = sede.getCcodigoprovincia();
		this.lLargaDuracion = sede.getLLargaDuracion();
		this.cUrlCSV = sede.getCUrlCSV();
		this.lLdap = sede.getLLdap();
	}
	
	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getCcreated() {
		return ccreated;
	}

	public void setCcreated(String ccreated) {
		this.ccreated = ccreated;
	}

	public Date getFcreated() {
		return fcreated;
	}

	public void setFcreated(Date fcreated) {
		this.fcreated = fcreated;
	}

	public String getCmodified() {
		return cmodified;
	}

	public void setCmodified(String cmodified) {
		this.cmodified = cmodified;
	}

	public Date getFmodified() {
		return fmodified;
	}

	public void setFmodified(Date fmodified) {
		this.fmodified = fmodified;
	}

	public String getCnombre() {
		return cnombre;
	}

	public void setCnombre(String cnombre) {
		this.cnombre = cnombre;
	}

	public String getCcodigoprovincia() {
		return ccodigoprovincia;
	}

	public void setCcodigoprovincia(String ccodigoprovincia) {
		this.ccodigoprovincia = ccodigoprovincia;
	}

	public Boolean getlLargaDuracion() {
		return lLargaDuracion;
	}

	public void setlLargaDuracion(Boolean lLargaDuracion) {
		this.lLargaDuracion = lLargaDuracion;
	}

	public String getcUrlCSV() {
		return cUrlCSV;
	}

	public void setcUrlCSV(String cUrlCSV) {
		this.cUrlCSV = cUrlCSV;
	}

	public Boolean getlLdap() {
		return lLdap;
	}

	public void setlLdap(Boolean lLdap) {
		this.lLdap = lLdap;
	}

	public PfOrganismoDIR3DTO getOrganismoDIR3() {
		return organismoDIR3;
	}

	public void setOrganismoDIR3(PfOrganismoDIR3DTO organismoDIR3) {
		this.organismoDIR3 = organismoDIR3;
	}
	
	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	
	

}
