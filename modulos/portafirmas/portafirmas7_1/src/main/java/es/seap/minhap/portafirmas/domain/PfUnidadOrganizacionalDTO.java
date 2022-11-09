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
package es.seap.minhap.portafirmas.domain;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.map.directorio.manager.impl.Unidad;

/**
 *
 * @author eSoluzion
 */
@Entity
@Table(name = "PF_UNIDAD_ORGANIZACIONAL")
public class PfUnidadOrganizacionalDTO extends AbstractBaseDTO {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    
    @Column(name = "DENOMINACION")
    private String denominacion;
    
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "NIVEL_ADMINISTRACION")
    private BigInteger nivelAdministracion;
    @Column(name = "NIVEL_JERARQUICO")
    private BigInteger nivelJerarquico;
    
    @Column(name = "CODUNIDAD_SUPERIOR")
    private String codunidadSuperior;
    
    @Column(name = "DENOM_UNIDAD_SUPERIOR")
    private String denomUnidadSuperior;
    
    @Column(name = "COD_UNIDAD_RAIZ")
    private String codUnidadRaiz;
    
    @Column(name = "DENOM_UNIDAD_RAIZ")
    private String denomUnidadRaiz;
    
    @Column(name = "ES_EDP")
    private String esEdp;
    
    @Column(name = "COD_TIPO_ENT_PUBLICA")
    private String codTipoEntPublica;
    
    @Column(name = "COD_TIPO_UNIDAD")
    private String codTipoUnidad;
    @Column(name = "COD_AMB_TERRITORIAL")
    private BigInteger codAmbTerritorial;
    @Column(name = "COD_AMBENT_GEOGRAFICA")
    private BigInteger codAmbentGeografica;
    @Column(name = "COD_AMB_PAIS")
    private BigInteger codAmbPais;
    
    @Column(name = "FECHA_ALTA_OFICIAL")
    private String fechaAltaOficial;
    @Column(name = "COD_EXTERNO")
    private BigInteger codExterno;

    @Id
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_SQ_UNIDAD_ORGANIZACIONAL")
	@SequenceGenerator(allocationSize = 1, name = "PF_SQ_UNIDAD_ORGANIZACIONAL", sequenceName = "PF_SQ_UNIDAD_ORGANIZACIONAL")
	private Long id;
    
    @Column(name = "CODIGO")
    private String codigo;
    
    @ManyToMany(mappedBy = "pfUnidadOrganizacionalList", fetch = FetchType.LAZY)
    private List<PfUsersDTO> pfUsuariosList;

    public List<PfUsersDTO> getPfUsuariosList() {
		return pfUsuariosList;
	}

	public void setPfUsuariosList(List<PfUsersDTO> pfUsuariosList) {
		this.pfUsuariosList = pfUsuariosList;
	}

	public PfUnidadOrganizacionalDTO(String denominacion, String estado, BigInteger nivelAdministracion,
			BigInteger nivelJerarquico, String codunidadSuperior, String denomUnidadSuperior, String codUnidadRaiz,
			String denomUnidadRaiz, String esEdp, String codTipoEntPublica, String codTipoUnidad,
			BigInteger codAmbTerritorial, BigInteger codAmbentGeografica, BigInteger codAmbPais,
			String fechaAltaOficial, BigInteger codExterno, Long id, String codigo) {
		super();
		this.denominacion = denominacion;
		this.estado = estado;
		this.nivelAdministracion = nivelAdministracion;
		this.nivelJerarquico = nivelJerarquico;
		this.codunidadSuperior = codunidadSuperior;
		this.denomUnidadSuperior = denomUnidadSuperior;
		this.codUnidadRaiz = codUnidadRaiz;
		this.denomUnidadRaiz = denomUnidadRaiz;
		this.esEdp = esEdp;
		this.codTipoEntPublica = codTipoEntPublica;
		this.codTipoUnidad = codTipoUnidad;
		this.codAmbTerritorial = codAmbTerritorial;
		this.codAmbentGeografica = codAmbentGeografica;
		this.codAmbPais = codAmbPais;
		this.fechaAltaOficial = fechaAltaOficial;
		this.codExterno = codExterno;
		this.id = id;
		this.codigo = codigo;

	}

	public PfUnidadOrganizacionalDTO() {
    }

    public PfUnidadOrganizacionalDTO(Long id) {
        this.id = id;
    }

    public PfUnidadOrganizacionalDTO(Long id, String denominacion) {
        this.id = id;
        this.denominacion = denominacion;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigInteger getNivelAdministracion() {
        return nivelAdministracion;
    }

    public void setNivelAdministracion(BigInteger nivelAdministracion) {
        this.nivelAdministracion = nivelAdministracion;
    }

    public BigInteger getNivelJerarquico() {
        return nivelJerarquico;
    }

    public void setNivelJerarquico(BigInteger nivelJerarquico) {
        this.nivelJerarquico = nivelJerarquico;
    }

    public String getCodunidadSuperior() {
        return codunidadSuperior;
    }

    public void setCodunidadSuperior(String codunidadSuperior) {
        this.codunidadSuperior = codunidadSuperior;
    }

    public String getDenomUnidadSuperior() {
        return denomUnidadSuperior;
    }

    public void setDenomUnidadSuperior(String denomUnidadSuperior) {
        this.denomUnidadSuperior = denomUnidadSuperior;
    }

    public String getCodUnidadRaiz() {
        return codUnidadRaiz;
    }

    public void setCodUnidadRaiz(String codUnidadRaiz) {
        this.codUnidadRaiz = codUnidadRaiz;
    }

    public String getDenomUnidadRaiz() {
        return denomUnidadRaiz;
    }

    public void setDenomUnidadRaiz(String denomUnidadRaiz) {
        this.denomUnidadRaiz = denomUnidadRaiz;
    }

    public String getEsEdp() {
        return esEdp;
    }

    public void setEsEdp(String esEdp) {
        this.esEdp = esEdp;
    }

    public String getCodTipoEntPublica() {
        return codTipoEntPublica;
    }

    public void setCodTipoEntPublica(String codTipoEntPublica) {
        this.codTipoEntPublica = codTipoEntPublica;
    }

    public String getCodTipoUnidad() {
        return codTipoUnidad;
    }

    public void setCodTipoUnidad(String codTipoUnidad) {
        this.codTipoUnidad = codTipoUnidad;
    }

    public BigInteger getCodAmbTerritorial() {
        return codAmbTerritorial;
    }

    public void setCodAmbTerritorial(BigInteger codAmbTerritorial) {
        this.codAmbTerritorial = codAmbTerritorial;
    }

    public BigInteger getCodAmbentGeografica() {
        return codAmbentGeografica;
    }

    public void setCodAmbentGeografica(BigInteger codAmbentGeografica) {
        this.codAmbentGeografica = codAmbentGeografica;
    }

    public BigInteger getCodAmbPais() {
        return codAmbPais;
    }

    public void setCodAmbPais(BigInteger codAmbPais) {
        this.codAmbPais = codAmbPais;
    }

    public String getFechaAltaOficial() {
        return fechaAltaOficial;
    }

    public void setFechaAltaOficial(String fechaAltaOficial) {
        this.fechaAltaOficial = fechaAltaOficial;
    }

    public BigInteger getCodExterno() {
        return codExterno;
    }

    public void setCodExterno(BigInteger codExterno) {
        this.codExterno = codExterno;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PfUnidadOrganizacionalDTO)) {
            return false;
        }
        PfUnidadOrganizacionalDTO other = (PfUnidadOrganizacionalDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PfUnidadOrganizacional[ id=" + id + " ]";
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
		return id!=null?id.longValue():null;
	}

	@Override
	@Transient
	public void setPrimaryKey(Long primaryKey) {
		id = new Long(""+primaryKey);
	}

}
