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

import java.math.BigDecimal;
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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eSoluzion
 */
@Entity
@Table(name = "PF_RESPONSE_ORGANISMOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PfResponseOrganismosDTO.findAll", query = "SELECT p FROM PfResponseOrganismosDTO p"),
    @NamedQuery(name = "PfResponseOrganismosDTO.findById", query = "SELECT p FROM PfResponseOrganismosDTO p WHERE p.id = :id"),
    @NamedQuery(name = "PfResponseOrganismosDTO.findMaxDate", query = "SELECT max(p.fechaConsulta) FROM PfResponseOrganismosDTO p"),
    @NamedQuery(name = "PfResponseOrganismosDTO.findByFechaConsulta", query = "SELECT p FROM PfResponseOrganismosDTO p WHERE p.fechaConsulta = :fechaConsulta")})
public class PfResponseOrganismosDTO extends AbstractBaseDTO {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_SQ_RESPONSE_ORGANISMOS")
	@SequenceGenerator(allocationSize = 1, name = "PF_SQ_RESPONSE_ORGANISMOS", sequenceName = "PF_SQ_RESPONSE_ORGANISMOS")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "FECHA_CONSULTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConsulta;
    @Basic(fetch=FetchType.LAZY,optional = false)
    @Lob
    @Column(name = "DOCUMENTO_OBTENIDO")
    private byte[] documentoObtenido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPfResponseOrganismos", fetch = FetchType.LAZY)
    private List<PfOrganismoDIR3DTO> pfOrganismoDIR3List;
    @Column(name = "CODIGO_RESPUESTA")
    private String codigoRespuesta;
    @Column(name = "DESCRIPCION_RESPUESTA")
    private String descripcionRespuesta;
    
	public PfResponseOrganismosDTO() {
    }

    public PfResponseOrganismosDTO(BigDecimal id) {
        this.id = id;
    }

    public PfResponseOrganismosDTO(BigDecimal id, Date fechaConsulta, byte[] documentoObtenido) {
        this.id = id;
        this.fechaConsulta = fechaConsulta;
        this.documentoObtenido = documentoObtenido;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

	@Temporal(TemporalType.TIMESTAMP)
    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public byte[] getDocumentoObtenido() {
        return documentoObtenido;
    }

    public void setDocumentoObtenido(byte[] documentoObtenido) {
        this.documentoObtenido = documentoObtenido;
    }

    @XmlTransient
    public List<PfOrganismoDIR3DTO> getPfUnidadOrganizacionalList() {
        return pfOrganismoDIR3List;
    }

    public void setPfUnidadOrganizacionalList(List<PfOrganismoDIR3DTO> pfUnidadOrganizacionalList) {
        this.pfOrganismoDIR3List = pfUnidadOrganizacionalList;
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
        if (!(object instanceof PfResponseOrganismosDTO)) {
            return false;
        }
        PfResponseOrganismosDTO other = (PfResponseOrganismosDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PfResponseOrganismos[ id=" + id + " ]";
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
		if(id==null){
			return null;
		}
		else{
			return id.longValueExact();
		}
	}

	@Override
	@Transient
	public void setPrimaryKey(Long primaryKey) {
		id = new BigDecimal(primaryKey);
		
	}
	
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public String getDescripcionRespuesta() {
		return descripcionRespuesta;
	}

	public void setDescripcionRespuesta(String descripcionRespuesta) {
		this.descripcionRespuesta = descripcionRespuesta;
	}
    
}
