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

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;

/**
 *
 * @author eSoluzion
 */
@Entity
@Table(name = "PF_JOB_PARAMETROS_GENERALES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PfJobParametrosGenerales.findAll", query = "SELECT p FROM PfJobParametrosGenerales p"),
    @NamedQuery(name = "PfJobParametrosGenerales.findById", query = "SELECT p FROM PfJobParametrosGenerales p WHERE p.id = :id"),
    @NamedQuery(name = "PfJobParametrosGenerales.findByParametro", query = "SELECT p FROM PfJobParametrosGenerales p WHERE p.parametro = :parametro"),
    @NamedQuery(name = "PfJobParametrosGenerales.findByValor", query = "SELECT p FROM PfJobParametrosGenerales p WHERE p.valor = :valor")})
public class PfJobParametrosGenerales extends AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_JOB_PARAMETROS_GENERALES")
	@SequenceGenerator(allocationSize = 1, name = "SQ_JOB_PARAMETROS_GENERALES", sequenceName = "SQ_JOB_PARAMETROS_GENERALES")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PARAMETRO")
    private String parametro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "VALOR")
    private String valor;
    @JoinColumn(name = "ID_JOB", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PfJobJobs idJob;

    public PfJobParametrosGenerales() {
    }

    public PfJobParametrosGenerales(Long id) {
        this.id = id;
    }

    public PfJobParametrosGenerales(Long id, String parametro, String valor) {
        this.id = id;
        this.parametro = parametro;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public PfJobJobs getIdJob() {
        return idJob;
    }

    public void setIdJob(PfJobJobs idJob) {
        this.idJob = idJob;
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
        if (!(object instanceof PfJobParametrosGenerales)) {
            return false;
        }
        PfJobParametrosGenerales other = (PfJobParametrosGenerales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PfJobParametrosGenerales[ id=" + id + " ]";
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
