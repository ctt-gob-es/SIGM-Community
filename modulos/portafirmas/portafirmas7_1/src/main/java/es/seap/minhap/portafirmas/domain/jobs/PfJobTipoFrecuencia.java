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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.seap.minhap.portafirmas.domain.jobs;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;

/**
 *
 * @author eSoluzion
 */
@Entity
@Table(name = "PF_JOB_TIPO_FRECUENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PfJobTipoFrecuencia.findAll", query = "SELECT p FROM PfJobTipoFrecuencia p"),
    @NamedQuery(name = "PfJobTipoFrecuencia.findById", query = "SELECT p FROM PfJobTipoFrecuencia p WHERE p.id = :id"),
    @NamedQuery(name = "PfJobTipoFrecuencia.findByCodigo", query = "SELECT p FROM PfJobTipoFrecuencia p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "PfJobTipoFrecuencia.findByDescripcion", query = "SELECT p FROM PfJobTipoFrecuencia p WHERE p.descripcion = :descripcion")})
public class PfJobTipoFrecuencia extends AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_JOB_TIPO_FRECUENCIA")
	@SequenceGenerator(allocationSize = 1, name = "SQ_JOB_TIPO_FRECUENCIA", sequenceName = "SQ_JOB_TIPO_FRECUENCIA")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFrecuenciaTipo", fetch = FetchType.LAZY)
    private List<PfJobJobs> pfJobJobsList;

    public PfJobTipoFrecuencia() {
    }

    public PfJobTipoFrecuencia(Long id) {
        this.id = id;
    }

    public PfJobTipoFrecuencia(Long id, String codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<PfJobJobs> getPfJobJobsList() {
        return pfJobJobsList;
    }

    public void setPfJobJobsList(List<PfJobJobs> pfJobJobsList) {
        this.pfJobJobsList = pfJobJobsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PfJobTipoFrecuencia)) {
            return false;
        }
        PfJobTipoFrecuencia other = (PfJobTipoFrecuencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PfJobTipoFrecuencia[ id=" + id + " ]";
    }

	@Override
	@Transient
	public void setCcreated(String ccreated) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transient
	public void setFcreated(Date fcreated) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transient
	public void setCmodified(String cmodified) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transient
	public void setFmodified(Date fmodified) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transient
	public String getCcreated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public Date getFcreated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public String getCmodified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public Date getFmodified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return id;
	}

	@Override
	@Transient
	public void setPrimaryKey(Long primaryKey) {
		id=primaryKey;
	}
    
}
