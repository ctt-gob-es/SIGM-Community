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
import java.math.BigInteger;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;

/**
 *
 * @author eSoluzion
 */
@Entity
@Table(name = "PF_JOB_JOBS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PfJobJobs.findAll", query = "SELECT p FROM PfJobJobs p left join fetch p.idTipoJob left join fetch p.idFrecuenciaTipo "),
    @NamedQuery(name = "PfJobJobs.findById", query = "SELECT p FROM PfJobJobs p WHERE p.id = :id"),
    @NamedQuery(name = "PfJobJobs.findByCode", query = "SELECT p FROM PfJobJobs p WHERE p.idTipoJob.codigo = :codigoJob"),
    @NamedQuery(name = "PfJobJobs.findByFrecuenciaNumero", query = "SELECT p FROM PfJobJobs p WHERE p.frecuenciaNumero = :frecuenciaNumero"),
    @NamedQuery(name = "PfJobJobs.findByUltimaEjecucion", query = "SELECT p FROM PfJobJobs p WHERE p.ultimaEjecucion = :ultimaEjecucion")})
public class PfJobJobs extends AbstractBaseDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_JOB_JOBS")
	@SequenceGenerator(allocationSize = 1, name = "SQ_JOB_JOBS", sequenceName = "SQ_JOB_JOBS")    
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FRECUENCIA_NUMERO")
    private BigInteger frecuenciaNumero;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ON_OFF")
    private BigInteger onOff;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "EN_EJECUCION")
    private BigInteger enEjecucion;
    
    @Column(name = "ULTIMA_EJECUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaEjecucion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idJob", fetch = FetchType.LAZY)
    private List<PfJobParametrosEjecucion> pfJobParametrosEjecucionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idJob", fetch = FetchType.LAZY)
    private List<PfJobParametrosGenerales> pfJobParametrosGeneralesList;
    @JoinColumn(name = "ID_FRECUENCIA_TIPO", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PfJobTipoFrecuencia idFrecuenciaTipo;
    @JoinColumn(name = "ID_TIPO_JOB", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PfJobTipo idTipoJob;

    public PfJobJobs() {
    }

    public PfJobJobs(Long id) {
        this.id = id;
    }

    public BigInteger getEnEjecucion() {
		return enEjecucion;
	}

	public void setEnEjecucion(BigInteger enEjecucion) {
		this.enEjecucion = enEjecucion;
	}

	public PfJobJobs(Long id, BigInteger frecuenciaNumero) {
        this.id = id;
        this.frecuenciaNumero = frecuenciaNumero;
    }

    public BigInteger getOnOff() {
		return onOff;
	}

	public void setOnOff(BigInteger onOff) {
		this.onOff = onOff;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getFrecuenciaNumero() {
        return frecuenciaNumero;
    }

    public void setFrecuenciaNumero(BigInteger frecuenciaNumero) {
        this.frecuenciaNumero = frecuenciaNumero;
    }

    public Date getUltimaEjecucion() {
        return ultimaEjecucion;
    }

    public void setUltimaEjecucion(Date ultimaEjecucion) {
        this.ultimaEjecucion = ultimaEjecucion;
    }

    @XmlTransient
    public List<PfJobParametrosEjecucion> getPfJobParametrosEjecucionList() {
        return pfJobParametrosEjecucionList;
    }

    public void setPfJobParametrosEjecucionList(List<PfJobParametrosEjecucion> pfJobParametrosEjecucionList) {
        this.pfJobParametrosEjecucionList = pfJobParametrosEjecucionList;
    }

    @XmlTransient
    public List<PfJobParametrosGenerales> getPfJobParametrosGeneralesList() {
        return pfJobParametrosGeneralesList;
    }

    public void setPfJobParametrosGeneralesList(List<PfJobParametrosGenerales> pfJobParametrosGeneralesList) {
        this.pfJobParametrosGeneralesList = pfJobParametrosGeneralesList;
    }

    public PfJobTipoFrecuencia getIdFrecuenciaTipo() {
        return idFrecuenciaTipo;
    }

    public void setIdFrecuenciaTipo(PfJobTipoFrecuencia idFrecuenciaTipo) {
        this.idFrecuenciaTipo = idFrecuenciaTipo;
    }

    public PfJobTipo getIdTipoJob() {
        return idTipoJob;
    }

    public void setIdTipoJob(PfJobTipo idTipoJob) {
        this.idTipoJob = idTipoJob;
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
        if (!(object instanceof PfJobJobs)) {
            return false;
        }
        PfJobJobs other = (PfJobJobs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PfJobJobs[ id=" + id + " ]";
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
		id = primaryKey;
	}
    
}
