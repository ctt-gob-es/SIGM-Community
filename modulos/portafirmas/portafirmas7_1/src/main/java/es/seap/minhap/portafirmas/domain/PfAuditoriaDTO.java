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

package es.seap.minhap.portafirmas.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PF_AUDITORIA")
public class PfAuditoriaDTO extends AbstractBaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5998566053539349828L;
	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;


	private Long usuarioReal;
	private Long usuarioSimulado;
	private String operacion;
	private String clase;
	private String metodo;
	private String parametros;
	

	public PfAuditoriaDTO(Long usuarioReal, Long usuarioSimulado, String operacion, String clase, String metodo, String parametros) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.usuarioReal = usuarioReal;
		this.usuarioSimulado = usuarioSimulado;
		this.operacion = operacion;
		this.clase = clase;
		this.metodo = metodo;
		this.parametros=parametros;
	}	
	
	@Override
	@Column(name = "C_CREADO", nullable = false, length = 20)
	public String getCcreated() {
		return ccreated;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_CREADO", nullable = false, length = 7)
	public Date getFcreated() {
		return fcreated;
	}

	@Override
	@Column(name = "C_MODIFICADO", nullable = false, length = 20)
	public String getCmodified() {
		return cmodified;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_MODIFICADO", nullable = false, length = 7)
	public Date getFmodified() {
		return fmodified;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_SQ_AUDITORIA")
	@SequenceGenerator(allocationSize = 1, name = "PF_SQ_AUDITORIA", sequenceName = "PF_SQ_AUDITORIA")
	@Column(name = "X_AUDITORIA", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Column(name = "USU_X_USUARIO", nullable = false, length = 22)	
	public Long getUsuarioReal() {
		return usuarioReal;
	}


	@Column(name = "USU_X_USU_SIMULADO", nullable = false, length = 22)
	public Long getUsuarioSimulado() {
		return usuarioSimulado;
	}

	@Column(name = "D_OPERACION", nullable = false, length = 256)
	public String getOperacion() {
		return operacion;
	}

	@Column(name = "D_CLASE", nullable = false, length = 256)
	public String getClase() {
		return clase;
	}

	@Column(name = "D_METODO", nullable = false, length = 256)	
	public String getMetodo() {
		return metodo;
	}
	@Column(name = "D_PARAMETROS", nullable = true, length = 512)
	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}	
	
	public void setClase(String clase) {
		this.clase = clase;
	}	
	
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}	

	public void setUsuarioSimulado(Long usuarioSimulado) {
		this.usuarioSimulado = usuarioSimulado;
	}
	
	public void setUsuarioReal(Long usuarioReal) {
		this.usuarioReal = usuarioReal;
	}	
	
	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
		
	}
	
	@Override
	public void setCcreated(String ccreated) {
		this.ccreated=ccreated;
		
	}

	@Override
	public void setFcreated(Date fcreated) {
		this.fcreated=fcreated;
		
	}

	@Override
	public void setCmodified(String cmodified) {
		this.cmodified=cmodified;
		
	}

	@Override
	public void setFmodified(Date fmodified) {
		this.fmodified=fmodified;
		
	}
	
	@Override
	@PrePersist
	public void createAuditing() {
		super.createAuditing();
		this.setCcreated(usuarioReal!=null?usuarioReal.toString():null);
		this.setCmodified(usuarioReal!=null?usuarioReal.toString():null);
	}

}
