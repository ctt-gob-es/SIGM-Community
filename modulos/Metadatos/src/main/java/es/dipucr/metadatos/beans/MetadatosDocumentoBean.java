package es.dipucr.metadatos.beans;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.metadatos.diccionarios.EstadosElaboracion;
import es.dipucr.metadatos.diccionarios.OrigenCiudadanoAdministracion;
import es.dipucr.metadatos.diccionarios.TiposDocumentales;
import es.dipucr.metadatos.diccionarios.TiposFirmas;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;

public class MetadatosDocumentoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger.getLogger(MetadatosDocumentoBean.class);
    
    public static final String FORMATO_FECHA = "yyyyMMdd 'T' HH:mm:ss";
    
    public static final String VERSION_NTI_VALOR = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e";
    
    public class ObjetoSelectOneMenu{
        private String valor;
        private String sustituto;
        
        public ObjetoSelectOneMenu(){
            this.valor = "";
            this.sustituto = "";
        }
        public ObjetoSelectOneMenu(String valor, String sustituto){
            this.valor = valor;
            this.sustituto = sustituto;
        }
        public String getValor() {
            return valor;
        }
        public void setValor(String valor) {
            this.valor = valor;
        }
        public String getSustituto() {
            return sustituto;
        }
        public void setSustituto(String sustituto) {
            this.sustituto = sustituto;
        }
    }
    
    private String versionNTI;
    
    private String organo;
    private String anioCaptura;
    private String idEspecificoDocumento;
    
    private String identificador;
    
    private Date fechaCaptura;
    private String sFechaCaptura;
    
    private String origen;
    private String estadoElaboracion;
    private String identificadorDocOrigen;
    private String tipoDocumental;
    
    private String nombreFormato;
    
    private String tipoFirma;    
    private String csv;
    
    private int bookId;
    private int folderId;
    private int pageId;
    private int fileId;
    private String entidadId;
    
    private String nombreDoc;
    
    private List<ObjetoSelectOneMenu> listaOrigenCiudadanoAdministracion;
    private List<String> listaEstadosElaboracion;
    private List<String> listaTiposDocumentales;
    private List<String> listaTiposFirma;
    
    public MetadatosDocumentoBean(){
        
        this.bookId = -1;
        this.folderId = -1;
        this.pageId = -1;
        this.fileId = -1;
        this.entidadId = "";
        this.nombreDoc = "";
        
        this.versionNTI = VERSION_NTI_VALOR;
        
        this.organo = "";
        this.anioCaptura = "";
        this.idEspecificoDocumento = "";
        
        this.identificador = "";
        
        this.fechaCaptura = null;
        this.sFechaCaptura = "";
        
        this.origen = OrigenCiudadanoAdministracion.CIUDADANO;
        
        this.estadoElaboracion = EstadosElaboracion.OTROS;
        this.identificadorDocOrigen = "";
        
        this.tipoDocumental = TiposDocumentales.SOLICITUD;
        
        this.nombreFormato = "";
        
        this.tipoFirma = "";
        this.csv = "";
        
    }

    public MetadatosDocumentoBean(int bookId, int folderId, int pageId, int fileId, String entidadId, String nombreDoc){
        
        this.bookId = bookId;
        this.folderId = folderId;
        this.pageId = pageId;
        this.fileId = fileId;
        this.entidadId = entidadId;
        this.nombreDoc = nombreDoc;
        
        this.versionNTI = VERSION_NTI_VALOR;
        
        String organoDIR3 = ""; 
        try {
            ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
            Entidad entidad = servicioEntidades.obtenerEntidad(entidadId);
            if(null != entidad){
                organoDIR3 = entidad.getDir3();
            }
        } catch (SigemException e) {
            LOGGER.error("ERROR al recuperar el código DIR3 de la entidad: " + entidadId + ". " + e.getMessage(), e);
        }
        this.organo = organoDIR3;
        
        this.anioCaptura = "";
        this.idEspecificoDocumento = "";
        
        this.identificador = "";
        
        this.fechaCaptura = null;
        this.sFechaCaptura = "";
        
        this.origen = OrigenCiudadanoAdministracion.CIUDADANO;
        
        this.estadoElaboracion = EstadosElaboracion.OTROS;
        this.identificadorDocOrigen = "";
        
        this.tipoDocumental = TiposDocumentales.SOLICITUD;
        
        if(StringUtils.isNotEmpty(nombreDoc)){
            this.nombreFormato = FileUtils.getExtensionByNombreDoc(nombreDoc);
        }
        
        this.tipoFirma = "";
        this.csv = "";
    }
    
    public MetadatosDocumentoBean(int bookId, int folderId, int pageId, int fileId, String entidadId, String nombreDoc, String versionNTI, String organo, String idEspecificoDocumento, Date fechaCaptura, String origen, String estadoElaboracion, String identificadorDocOrigen, String tipoDocumental, String nombreFormato, String tipoFirma, String csv){
        
        this.bookId = bookId;
        this.folderId = folderId;
        this.pageId = pageId;
        this.fileId = fileId;
        this.entidadId = entidadId;
        
        this.nombreDoc = nombreDoc;
        
        this.versionNTI = versionNTI;
        
        this.organo = organo;        
        this.anioCaptura = getAnio(fechaCaptura);
        this.idEspecificoDocumento = idEspecificoDocumento;
        
        this.identificador = "ES_" + organo + "_" + anioCaptura + "_" + idEspecificoDocumento;        
        
        this.fechaCaptura = fechaCaptura;
        this.sFechaCaptura = new SimpleDateFormat(MetadatosDocumentoBean.FORMATO_FECHA).format(fechaCaptura);
        
        this.origen = origen;
        this.estadoElaboracion = estadoElaboracion;
        this.identificadorDocOrigen = identificadorDocOrigen;
        this.tipoDocumental = tipoDocumental;
        
        this.nombreFormato = nombreFormato;
        
        this.tipoFirma = tipoFirma;
        this.csv = csv;
    }
    
    private String getAnio(Date fechaCaptura) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaCaptura);
        return "" + cal.get(Calendar.YEAR);
    }

    public String getVersionNTI() {
        return versionNTI;
    }
    
    public void setVersionNTI(String versionNTI) {
        this.versionNTI = versionNTI;
    }
    
    public String getOrgano() {
        return organo;
    }
    
    public void setOrgano(String organo) {
        this.organo = organo;
    }
    
    public String getIdEspecificoDocumento() {
        return idEspecificoDocumento;
    }
    
    public void setIdEspecificoDocumento(String idEspecificoDocumento) {
        this.idEspecificoDocumento = idEspecificoDocumento;
    }
    
    public Date getFechaCaptura() {
        return fechaCaptura;
    }
    
    public void setFechaCaptura(Date fechaCaptura){
        this.fechaCaptura = fechaCaptura;        
        this.anioCaptura = getAnio(fechaCaptura);
        this.sFechaCaptura = new SimpleDateFormat(MetadatosDocumentoBean.FORMATO_FECHA).format(fechaCaptura);
    }
    
    public String getOrigen() {
        return origen;
    }
    
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    
    public String getEstadoElaboracion() {
        return estadoElaboracion;
    }
    
    public void setEstadoElaboracion(String estadoElaboracion) {
        this.estadoElaboracion = estadoElaboracion;
    }
    
    public String getIdentificadorDocOrigen() {
        return identificadorDocOrigen;
    }
    
    public void setIdentificadorDocOrigen(String identificadorDocOrigen) {
        this.identificadorDocOrigen = identificadorDocOrigen;
    }
    
    public String getTipoDocumental() {
        return tipoDocumental;
    }
    
    public void setTipoDocumental(String tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }

    public String getAnioCaptura() {
        return anioCaptura;
    }

    public void setAnioCaptura(String anioCaptura) {
        this.anioCaptura = anioCaptura;
    }

    public String getsFechaCaptura() {
        return sFechaCaptura;
    }

    public void setsFechaCaptura(String sFechaCaptura) {
        this.sFechaCaptura = sFechaCaptura;
    }

    public String getIdentificador() {
        this.identificador = "ES_" + organo + "_" + anioCaptura + "_" + idEspecificoDocumento;
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombreFormato() {
        return nombreFormato;
    }

    public void setNombreFormato(String nombreFormato) {
        this.nombreFormato = nombreFormato;
    }

    public String getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(String tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(String entidadId) {
        this.entidadId = entidadId;
    }
    
    public String getNombreDoc(){
        return this.nombreDoc;
    }
    
    public void setNombreDoc(String nombreDoc){
        this.nombreDoc = nombreDoc;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((versionNTI == null) ? 0 : versionNTI.hashCode());
        result = prime * result + ((organo == null) ? 0 : organo.hashCode());
        result = prime * result + ((anioCaptura == null) ? 0 : anioCaptura.hashCode());
        result = prime * result + ((idEspecificoDocumento == null) ? 0 : idEspecificoDocumento.hashCode());
        result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
        result = prime * result + ((fechaCaptura == null) ? 0 : fechaCaptura.hashCode());
        result = prime * result + ((origen == null) ? 0 : origen.hashCode());  
        result = prime * result + ((estadoElaboracion == null) ? 0 : estadoElaboracion.hashCode());
        result = prime * result + ((identificadorDocOrigen == null) ? 0 : identificadorDocOrigen.hashCode());
        result = prime * result + ((tipoDocumental == null) ? 0 : tipoDocumental.hashCode());
        result = prime * result + ((nombreFormato == null) ? 0 : nombreFormato.hashCode());
        result = prime * result + ((tipoFirma == null) ? 0 : tipoFirma.hashCode());
        result = prime * result + ((csv == null) ? 0 : csv.hashCode());        
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        
        MetadatosDocumentoBean other = (MetadatosDocumentoBean) obj;
        if (versionNTI == null) {
            if (other.versionNTI != null){
                return false;
            }
        } else if (!organo.equals(other.organo)){
            return false;
        }
        if (anioCaptura == null) {
            if (other.anioCaptura != null){
                return false;
            }
        } else if (!anioCaptura.equals(other.anioCaptura)){
            return false;
        }
        if (idEspecificoDocumento == null) {
            if (other.idEspecificoDocumento != null){
                return false;
            }
        } else if (!idEspecificoDocumento.equals(other.idEspecificoDocumento)){
            return false;
        }
        if (identificador == null) {
            if (other.identificador != null){
                return false;
            }
        } else if (!identificador.equals(other.identificador)){
            return false;
        }
        if (fechaCaptura== null) {
            if (other.fechaCaptura != null){
                return false;
            }
        } else if (!fechaCaptura.equals(other.fechaCaptura)){
            return false;
        }
        if (origen == null) {
            if (other.origen != null){
                return false;
            }
        } else if (!origen.equals(other.origen)){
            return false;
        }
        if (estadoElaboracion == null) {
            if (other.estadoElaboracion != null){
                return false;
            }
        } else if (!estadoElaboracion.equals(other.estadoElaboracion)){
            return false;
        }
        if (identificadorDocOrigen == null) {
            if (other.identificadorDocOrigen != null){
                return false;
            }
        } else if (!identificadorDocOrigen.equals(other.identificadorDocOrigen)){
            return false;
        }
        
        if (tipoDocumental == null) {
            if (other.tipoDocumental != null){
                return false;
            }
        } else if (!tipoDocumental.equals(other.tipoDocumental)){
            return false;        
        }
        if (nombreFormato == null) {
            if (other.nombreFormato != null){
                return false;
            }
        } else if (!nombreFormato.equals(other.nombreFormato)){
            return false;
        }
        if (tipoFirma == null) {
            if (other.tipoFirma != null){
                return false;
            }
        } else if (!tipoFirma.equals(other.tipoFirma)){
            return false;
        }
        if (csv == null) {
            if (other.csv != null){
                return false;
            }
        } else if (!csv.equals(other.csv)){
            return false;        
        }
        if (entidadId == null) {
            if (other.entidadId != null){
                return false;
            }
        } else if (!entidadId.equals(other.entidadId)){
            return false;
        }
        
        return bookId == other.bookId && folderId == other.folderId && pageId == other.pageId && fileId == other.fileId;        
    }

    public List<ObjetoSelectOneMenu> getListaOrigenCiudadanoAdministracion() {
        if(null == listaOrigenCiudadanoAdministracion || listaOrigenCiudadanoAdministracion.isEmpty()){
            cargaListaOrigenCiudadanoAdministracion();
        }
        return listaOrigenCiudadanoAdministracion;
    }

    public void setListaOrigenCiudadanoAdministracion( List<ObjetoSelectOneMenu> listaOrigenCiudadanoAdministracion) {
        this.listaOrigenCiudadanoAdministracion = listaOrigenCiudadanoAdministracion;
    }

    public List<String> getListaEstadosElaboracion() {
        if(null == listaEstadosElaboracion || listaEstadosElaboracion.isEmpty()){
            cargaListaEstadosElaboracion();
        }
        return listaEstadosElaboracion;
    }

    public void setListaEstadosElaboracion(List<String> listaEstadosElaboracion) {
        this.listaEstadosElaboracion = listaEstadosElaboracion;
    }
    
    public List<String> getListaTiposDocumentales() {
        if(null == listaTiposDocumentales || listaTiposDocumentales.isEmpty()){
            cargaListaTiposDocumentales();
        }
        
        return listaTiposDocumentales;
    }

    public void setListaTiposDocumentales(List<String> listaTiposDocumentales) {
        this.listaTiposDocumentales = listaTiposDocumentales;
    }
    
    public List<String> getListaTiposFirma() {
        if(null == listaTiposFirma || listaTiposFirma.isEmpty()){
            cargaListaTiposFirma();
        }
        
        return listaTiposFirma;
    }

    public void setListaTiposFirma(List<String> listaTiposFirma) {
        this.listaTiposFirma = listaTiposFirma;
    }
    
    private void cargaListaOrigenCiudadanoAdministracion() {
        listaOrigenCiudadanoAdministracion = new ArrayList<ObjetoSelectOneMenu>();
        
        listaOrigenCiudadanoAdministracion.add(new ObjetoSelectOneMenu(OrigenCiudadanoAdministracion.CIUDADANO, OrigenCiudadanoAdministracion.CIUDADANO_SUSTITUTO));
        listaOrigenCiudadanoAdministracion.add(new ObjetoSelectOneMenu(OrigenCiudadanoAdministracion.ADMINISTRACION, OrigenCiudadanoAdministracion.ADMINISTRACION_SUSTITUTO));
    }
    
    private void cargaListaEstadosElaboracion() {
        listaEstadosElaboracion = new ArrayList<String>();
        
        listaEstadosElaboracion.add(EstadosElaboracion.ORIGINAL); 
        listaEstadosElaboracion.add(EstadosElaboracion.COPIA_AUTENTICA_CAMBIO_FORMATO);
        listaEstadosElaboracion.add(EstadosElaboracion.COPIA_AUTENTICA_DOC_PAPEL);
        listaEstadosElaboracion.add(EstadosElaboracion.COPIA_PARCIAL_AUTENTICA);
        listaEstadosElaboracion.add(EstadosElaboracion.OTROS);
    }
    
    private void cargaListaTiposDocumentales() {
        listaTiposDocumentales = new ArrayList<String>();
        
        listaTiposDocumentales.add(TiposDocumentales.RESOLUCION); 
        listaTiposDocumentales.add(TiposDocumentales.ACUERDO);
        listaTiposDocumentales.add(TiposDocumentales.CONTRATO);
        listaTiposDocumentales.add(TiposDocumentales.CONVENIO);
        listaTiposDocumentales.add(TiposDocumentales.DECLARACION);
        listaTiposDocumentales.add(TiposDocumentales.COMUNICACION);
        listaTiposDocumentales.add(TiposDocumentales.NOTIFICACION);
        listaTiposDocumentales.add(TiposDocumentales.PUBLICACION);
        listaTiposDocumentales.add(TiposDocumentales.ACUSE_DE_RECIBO);
        listaTiposDocumentales.add(TiposDocumentales.ACTA);
        listaTiposDocumentales.add(TiposDocumentales.CERTIFICADO);
        listaTiposDocumentales.add(TiposDocumentales.DILIGENCIA);
        listaTiposDocumentales.add(TiposDocumentales.INFORME);
        listaTiposDocumentales.add(TiposDocumentales.SOLICITUD);
        listaTiposDocumentales.add(TiposDocumentales.DENUNCIA);
        listaTiposDocumentales.add(TiposDocumentales.ALEGACION);
        listaTiposDocumentales.add(TiposDocumentales.RECURSO);
        listaTiposDocumentales.add(TiposDocumentales.COMUNICACION_CIUDADANO);
        listaTiposDocumentales.add(TiposDocumentales.FACTURA);
        listaTiposDocumentales.add(TiposDocumentales.OTROS_INCAUTADOS);
    }
    
    private void cargaListaTiposFirma() {
        listaTiposFirma = new ArrayList<String>();

        listaTiposFirma.add(TiposFirmas.TIPO_FIRMA_CSV);
        listaTiposFirma.add(TiposFirmas.TIPO_FIRMA_XADES_INTERNALLY);
        listaTiposFirma.add(TiposFirmas.TIPO_FIRMA_XADES_ENVOLOPED);
        listaTiposFirma.add(TiposFirmas.TIPO_FIRMA_CADES_DETACHED);
        listaTiposFirma.add(TiposFirmas.TIPO_FIRMA_CADES_ATTACHED);
        listaTiposFirma.add(TiposFirmas.TIPO_FIRMA_PADES);
    }
}
