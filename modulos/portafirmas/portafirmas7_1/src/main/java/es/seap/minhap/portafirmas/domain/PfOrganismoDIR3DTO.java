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
@Table(name = "PF_ORGANISMO_DIR3")
public class PfOrganismoDIR3DTO extends AbstractBaseDTO {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_SQ_ORGANISMO_DIR3")
	@SequenceGenerator(allocationSize = 1, name = "PF_SQ_ORGANISMO_DIR3", sequenceName = "PF_SQ_ORGANISMO_DIR3")
	private Long id;
    
    @Column(name = "CODIGO")
    private String codigo;
    
    @Column(name = "VERSION")
    private int version;
    
    @Column(name = "DENOMINACION")
    private String denominacion;
    
    @Column(name = "DNM_LENGUA_COOFICIAL")
    private String dnm_lengua_cooficial;
    
    @Column(name = "IDIOMA_LENGUA")
    private String idioma_lengua;
    
    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "NIF_CIF")
    private String nif_cif;
    
    @Column(name = "SIGLAS")
    private String siglas;
    
    @Column(name = "NIVEL_ADMINISTRACION")
    private int nivelAdministracion;

    @Column(name = "NIVEL_JERARQUICO")
    private int nivelJerarquico;
    
    @Column(name = "COD_UNIDAD_SUPERIOR")
    private String codunidadSuperior;
    
    @Column(name = "V_UNIDAD_SUPERIOR")
    private int vUnidadSuperior;
    
    @Column(name = "DENOM_UNIDAD_SUPERIOR")
    private String denomUnidadSuperior;
    
    @Column(name = "COD_UNIDAD_RAIZ")
    private String codUnidadRaiz;
    
    @Column(name = "V_UNIDAD_RAIZ")
    private int vUnidadRaiz;
    
    @Column(name = "DENOM_UNIDAD_RAIZ")
    private String denomUnidadRaiz;
    
    @Column(name = "ES_EDP")
    private String esEdp;
    
    @Column(name = "COD_EDP_PRINCIPAL")
    private String codEDPprincipal;
    
    @Column(name = "V_EDP_PRINCIPAL")
    private int vEDPprincipal;
    
    @Column(name = "DENOM_EDP_PRINCIPAL")
    private String denomEDPprincipal;
    
    @Column(name = "COD_PODER")
    private int codPoder;
    
    @Column(name = "COD_TIPO_ENT_PUBLICA")
    private String codTipoEntPublica;
    
    @Column(name = "COD_TIPO_UNIDAD")
    private String codTipoUnidad;

    @Column(name = "COD_AMB_TERRITORIAL")
    private String codAmbTerritorial;

    @Column(name = "COD_AMBENT_GEOGRAFICA")
    private String codAmbentGeografica;

    @Column(name = "COD_AMB_PAIS")
    private String codAmbPais;
    
    @Column(name = "COD_AMB_COMUNIDAD")
    private String codAmbComunidad;
    
    @Column(name = "COD_AMB_PROVINCIA")
    private String codAmbProvincia;
    
    @Column(name = "COD_AMB_MUNICIPIO")
    private String codAmbMunicipio;
    
    @Column(name = "COD_AMB_ISLA")
    private String codAmbIsla;
    
    @Column(name = "COD_AMB_ELM")
    private String codAmbELM;
    
    @Column(name = "COD_AMB_LOC_EXTRANJERA")
    private String codAmbLocExtranjera;
    
    @Column(name = "COMPETENCIAS")
    private String competencias;
    
    @Column(name = "DISPOSICION_LEGAL")
    private String disposicionLegal;
    
    @Column(name = "FECHA_ALTA_OFICIAL")
    private String fechaAltaOficial;
    
    @Column(name = "FECHA_BAJA_OFICIAL")
    private String fechaBajaOficial;

    @Column(name = "FECHA_EXTINCION")
    private String fechaExtincion;

    @Column(name = "FECHA_ANULACION")
    private String fechaAnulacion;

    @Column(name = "FECHA_ULTIMA_ACTUALIZACION")
    private String fechaUltimaActualizacion;

    @Column(name = "COD_EXTERNO")
    private String codExterno;
    
    @Column(name = "OBSERV_GENERALES")
    private String observGenerales;
    
    @Column(name = "OBSERV_BAJA")
    private String observBaja;
    
    @Column(name = "DIRECC_TIPO_VIA")
    private String direccTipoVia;
    @Column(name = "DIRECC_NOMBRE_VIA")
    private String direccNombreVia;
    @Column(name = "DIRECC_NUM_VIA")
    private String direccNumVia;
    @Column(name = "DIRECC_COD_POSTAL")
    private String direccCodPostal;
    @Column(name = "DIRECC_COD_PAIS")
    private String direccCodPais;
    @Column(name = "DIRECC_COD_COMUNIDAD")
    private String direccCodComunidad;
    @Column(name = "DIRECC_COD_PROVINCIA")
    private String direccCodProvincia;
    @Column(name = "DIRECC_COD_LOCALIDAD")
    private String direccCodLocalidad;
    @Column(name = "DIRECC_COD_ENT_GEOGRAFICA")
    private String direccCodEntGeografica;
    @Column(name = "DIRECC_DIR_EXTRANJERA")
    private String direccDirExtranjera;
    @Column(name = "DIRECC_LOC_EXTRANJERA")
    private String direccLocExtranjera;
    @Column(name = "DIRECC_OBSERVACIONES")
    private String direccObservaciones;
    
    @Column(name = "COMPARTE_NIF")
    private String comparteNif;
    

    
    @JoinColumn(name = "ID_PF_RESPONSE_ORGANISMOS", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PfResponseOrganismosDTO idPfResponseOrganismos;
    
	public PfOrganismoDIR3DTO(String denominacion, String estado, int nivelAdministracion,
			int nivelJerarquico, String codunidadSuperior, String denomUnidadSuperior, String codUnidadRaiz,
			String denomUnidadRaiz, String esEdp, String codTipoEntPublica, String codTipoUnidad,
			String codAmbTerritorial, String codAmbentGeografica, String codAmbPais,
			String fechaAltaOficial, String codExterno, Long id, String codigo,
			PfResponseOrganismosDTO idPfResponseOrganismos) {
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
		this.idPfResponseOrganismos = idPfResponseOrganismos;
	}

    public PfOrganismoDIR3DTO(Unidad u) {
		super();
		this.id = null;
		this.codigo = u.getCodigo();
		this.version = u.getVersion();
		this.denominacion = u.getDenominacion();
		this.dnm_lengua_cooficial = u.getDnmLenguaCooficial();
		this.idioma_lengua = u.getIdiomaLengua();
		this.estado = u.getEstado().value();
		this.nif_cif = u.getNifCif();
		this.nivelAdministracion = u.getNivelAdministracion();
		this.nivelJerarquico = u.getNivelJerarquico();
		this.codunidadSuperior = u.getCodUnidadSuperior();
		this.vUnidadSuperior = u.getVUnidadSuperior();
		this.denomUnidadSuperior = u.getDenomUnidadSuperior();
		this.codUnidadRaiz = u.getCodUnidadRaiz();
		this.vUnidadRaiz = u.getVUnidadRaiz();
		this.denomUnidadRaiz = u.getDenomUnidadRaiz();
		if (u.getEsEDP()!=null) {
			this.esEdp = u.getEsEDP().value();
		}
		this.codEDPprincipal = u.getCodEDPPrincipal();
		this.vEDPprincipal = u.getVEDPPrincipal();
		this.denomEDPprincipal = u.getDenomEDPPrincipal();
		if (u.getCodPoder()!=null) {
			this.codPoder = u.getCodPoder();
		}
		this.codTipoEntPublica = u.getCodTipoEntPublica();
		this.codTipoUnidad = u.getCodTipoUnidad();
		this.codAmbTerritorial = u.getCodAmbTerritorial();
		this.codAmbentGeografica = u.getCodAmbEntGeografica();
		this.codAmbPais = u.getCodAmbPais();
		this.codAmbComunidad = u.getCodAmbComunidad();
		this.codAmbProvincia = u.getCodAmbProvincia();
		this.codAmbMunicipio = u.getCodAmbMunicipio();
		this.codAmbIsla = u.getCodAmbIsla();
		this.codAmbELM = u.getCodAmbElm();
		this.codAmbLocExtranjera = u.getCodAmbLocExtranjera();
		this.competencias = u.getCompetencias();
		this.disposicionLegal = u.getDisposicionLegal();
		this.fechaAltaOficial = u.getFechaAltaOficial();
		this.fechaBajaOficial = u.getFechaBajaOficial();
		this.fechaExtincion = u.getFechaExtincion();
		this.fechaAnulacion = u.getFechaAnulacion();
		this.fechaUltimaActualizacion = u.getFechaUltimaActualizacion();
		this.codExterno = u.getCodExterno();
		this.observGenerales = u.getObservGenerales();
		this.observBaja = u.getObservBaja();
		if (u.getDireccion()!=null) {
			this.direccTipoVia = u.getDireccion().getTipoVia();
			this.direccNombreVia = u.getDireccion().getNombreVia();
			this.direccNumVia = u.getDireccion().getNumVia();
			this.direccCodPostal = u.getDireccion().getCodPostal();
			this.direccCodPais = u.getDireccion().getCodPais();
			this.direccCodComunidad = u.getDireccion().getCodComunidad();
			this.direccCodProvincia = u.getDireccion().getCodProvincia();
			this.direccCodLocalidad = u.getDireccion().getCodLocalidad();
			this.direccCodEntGeografica = u.getDireccion().getCodEntGeografica();
			this.direccDirExtranjera = u.getDireccion().getDirExtranjera();
			this.direccLocExtranjera = u.getDireccion().getLocExtranjera();
			this.direccObservaciones = u.getDireccion().getObservaciones();
		}
		if (u.getComparteNif()!=null) {
			this.comparteNif = u.getComparteNif().value();
		}
		
		this.idPfResponseOrganismos = null;
	}
    
	public PfOrganismoDIR3DTO() {
    }

    public PfOrganismoDIR3DTO(Long id) {
        this.id = id;
    }

    public PfOrganismoDIR3DTO(Long id, String denominacion) {
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

    public String getFechaAltaOficial() {
        return fechaAltaOficial;
    }

    public void setFechaAltaOficial(String fechaAltaOficial) {
        this.fechaAltaOficial = fechaAltaOficial;
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

    public PfResponseOrganismosDTO getIdPfResponseOrganismos() {
        return idPfResponseOrganismos;
    }

    public void setIdPfResponseOrganismos(PfResponseOrganismosDTO idPfResponseOrganismos) {
        this.idPfResponseOrganismos = idPfResponseOrganismos;
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
        if (!(object instanceof PfOrganismoDIR3DTO)) {
            return false;
        }
        PfOrganismoDIR3DTO other = (PfOrganismoDIR3DTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PfOrganismoDIR3DTO[ id=" + id + " ]";
    }


	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDnm_lengua_cooficial() {
		return dnm_lengua_cooficial;
	}

	public void setDnm_lengua_cooficial(String dnm_lengua_cooficial) {
		this.dnm_lengua_cooficial = dnm_lengua_cooficial;
	}

	public String getIdioma_lengua() {
		return idioma_lengua;
	}

	public void setIdioma_lengua(String idioma_lengua) {
		this.idioma_lengua = idioma_lengua;
	}

	public String getNif_cif() {
		return nif_cif;
	}

	public void setNif_cif(String nif_cif) {
		this.nif_cif = nif_cif;
	}

	public String getSiglas() {
		return siglas;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

	public int getNivelAdministracion() {
		return nivelAdministracion;
	}

	public void setNivelAdministracion(int nivelAdministracion) {
		this.nivelAdministracion = nivelAdministracion;
	}

	public int getNivelJerarquico() {
		return nivelJerarquico;
	}

	public void setNivelJerarquico(int nivelJerarquico) {
		this.nivelJerarquico = nivelJerarquico;
	}

	public int getvUnidadSuperior() {
		return vUnidadSuperior;
	}

	public void setvUnidadSuperior(int vUnidadSuperior) {
		this.vUnidadSuperior = vUnidadSuperior;
	}

	public int getvUnidadRaiz() {
		return vUnidadRaiz;
	}

	public void setvUnidadRaiz(int vUnidadRaiz) {
		this.vUnidadRaiz = vUnidadRaiz;
	}

	public String getCodEDPprincipal() {
		return codEDPprincipal;
	}

	public void setCodEDPprincipal(String codEDPprincipal) {
		this.codEDPprincipal = codEDPprincipal;
	}

	public int getvEDPprincipal() {
		return vEDPprincipal;
	}

	public void setvEDPprincipal(int vEDPprincipal) {
		this.vEDPprincipal = vEDPprincipal;
	}

	public String getDenomEDPprincipal() {
		return denomEDPprincipal;
	}

	public void setDenomEDPprincipal(String denomEDPprincipal) {
		this.denomEDPprincipal = denomEDPprincipal;
	}

	public int getCodPoder() {
		return codPoder;
	}

	public void setCodPoder(int codPoder) {
		this.codPoder = codPoder;
	}

	public String getCodAmbTerritorial() {
		return codAmbTerritorial;
	}

	public void setCodAmbTerritorial(String codAmbTerritorial) {
		this.codAmbTerritorial = codAmbTerritorial;
	}

	public String getCodAmbentGeografica() {
		return codAmbentGeografica;
	}

	public void setCodAmbentGeografica(String codAmbentGeografica) {
		this.codAmbentGeografica = codAmbentGeografica;
	}

	public String getCodAmbPais() {
		return codAmbPais;
	}

	public void setCodAmbPais(String codAmbPais) {
		this.codAmbPais = codAmbPais;
	}

	public String getCodAmbComunidad() {
		return codAmbComunidad;
	}

	public void setCodAmbComunidad(String codAmbComunidad) {
		this.codAmbComunidad = codAmbComunidad;
	}

	public String getCodAmbProvincia() {
		return codAmbProvincia;
	}

	public void setCodAmbProvincia(String codAmbProvincia) {
		this.codAmbProvincia = codAmbProvincia;
	}

	public String getCodAmbMunicipio() {
		return codAmbMunicipio;
	}

	public void setCodAmbMunicipio(String codAmbMunicipio) {
		this.codAmbMunicipio = codAmbMunicipio;
	}

	public String getCodAmbIsla() {
		return codAmbIsla;
	}

	public void setCodAmbIsla(String codAmbIsla) {
		this.codAmbIsla = codAmbIsla;
	}

	public String getCodAmbELM() {
		return codAmbELM;
	}

	public void setCodAmbELM(String codAmbELM) {
		this.codAmbELM = codAmbELM;
	}

	public String getCodAmbLocExtranjera() {
		return codAmbLocExtranjera;
	}

	public void setCodAmbLocExtranjera(String codAmbLocExtranjera) {
		this.codAmbLocExtranjera = codAmbLocExtranjera;
	}

	public String getCompetencias() {
		return competencias;
	}

	public void setCompetencias(String competencias) {
		this.competencias = competencias;
	}

	public String getDisposicionLegal() {
		return disposicionLegal;
	}

	public void setDisposicionLegal(String disposicionLegal) {
		this.disposicionLegal = disposicionLegal;
	}

	public String getFechaBajaOficial() {
		return fechaBajaOficial;
	}

	public void setFechaBajaOficial(String fechaBajaOficial) {
		this.fechaBajaOficial = fechaBajaOficial;
	}

	public String getFechaExtincion() {
		return fechaExtincion;
	}

	public void setFechaExtincion(String fechaExtincion) {
		this.fechaExtincion = fechaExtincion;
	}

	public String getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getFechaUltimaActualizacion() {
		return fechaUltimaActualizacion;
	}

	public void setFechaUltimaActualizacion(String fechaUltimaActualizacion) {
		this.fechaUltimaActualizacion = fechaUltimaActualizacion;
	}

	public String getCodExterno() {
		return codExterno;
	}

	public void setCodExterno(String codExterno) {
		this.codExterno = codExterno;
	}

	public String getObservGenerales() {
		return observGenerales;
	}

	public void setObservGenerales(String observGenerales) {
		this.observGenerales = observGenerales;
	}

	public String getObservBaja() {
		return observBaja;
	}

	public void setObservBaja(String observBaja) {
		this.observBaja = observBaja;
	}

	public String getDireccTipoVia() {
		return direccTipoVia;
	}

	public void setDireccTipoVia(String direccTipoVia) {
		this.direccTipoVia = direccTipoVia;
	}

	public String getDireccNombreVia() {
		return direccNombreVia;
	}

	public void setDireccNombreVia(String direccNombreVia) {
		this.direccNombreVia = direccNombreVia;
	}

	public String getDireccNumVia() {
		return direccNumVia;
	}

	public void setDireccNumVia(String direccNumVia) {
		this.direccNumVia = direccNumVia;
	}

	public String getDireccCodPostal() {
		return direccCodPostal;
	}

	public void setDireccCodPostal(String direccCodPostal) {
		this.direccCodPostal = direccCodPostal;
	}

	public String getDireccCodPais() {
		return direccCodPais;
	}

	public void setDireccCodPais(String direccCodPais) {
		this.direccCodPais = direccCodPais;
	}

	public String getDireccCodComunidad() {
		return direccCodComunidad;
	}

	public void setDireccCodComunidad(String direccCodComunidad) {
		this.direccCodComunidad = direccCodComunidad;
	}

	public String getDireccCodProvincia() {
		return direccCodProvincia;
	}

	public void setDireccCodProvincia(String direccCodProvincia) {
		this.direccCodProvincia = direccCodProvincia;
	}

	public String getDireccCodLocalidad() {
		return direccCodLocalidad;
	}

	public void setDireccCodLocalidad(String direccCodLocalidad) {
		this.direccCodLocalidad = direccCodLocalidad;
	}

	public String getDireccCodEntGeografica() {
		return direccCodEntGeografica;
	}

	public void setDireccCodEntGeografica(String direccCodEntGeografica) {
		this.direccCodEntGeografica = direccCodEntGeografica;
	}

	public String getDireccDirExtranjera() {
		return direccDirExtranjera;
	}

	public void setDireccDirExtranjera(String direccDirExtranjera) {
		this.direccDirExtranjera = direccDirExtranjera;
	}

	public String getDireccLocExtranjera() {
		return direccLocExtranjera;
	}

	public void setDireccLocExtranjera(String direccLocExtranjera) {
		this.direccLocExtranjera = direccLocExtranjera;
	}

	public String getDireccObservaciones() {
		return direccObservaciones;
	}

	public void setDireccObservaciones(String direccObservaciones) {
		this.direccObservaciones = direccObservaciones;
	}

	public String getComparteNif() {
		return comparteNif;
	}

	public void setComparteNif(String comparteNif) {
		this.comparteNif = comparteNif;
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
