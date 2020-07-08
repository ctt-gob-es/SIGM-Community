package es.dipucr.metadatos.sw;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.diccionarios.EstadosElaboracion;
import es.dipucr.metadatos.diccionarios.OrigenCiudadanoAdministracion;
import es.dipucr.metadatos.diccionarios.TiposDocumentales;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;

public class MetadatosDocumentoSW implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger.getLogger(MetadatosDocumentoSW.class);
    
    public static final String FORMATO_FECHA = "yyyyMMdd 'T' HH:mm:ss";
    
    public static final String VERSION_NTI_VALOR = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e";
    
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
    
    public MetadatosDocumentoSW(){
        
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

    public MetadatosDocumentoSW(int bookId, int folderId, int pageId, int fileId, String entidadId, String nombreDoc){
        
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
    
    public MetadatosDocumentoSW(int bookId, int folderId, int pageId, int fileId, String entidadId, String nombreDoc, String versionNTI, String organo, String idEspecificoDocumento, Date fechaCaptura, String origen, String estadoElaboracion, String identificadorDocOrigen, String tipoDocumental, String nombreFormato, String tipoFirma, String csv){
        
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
        this.sFechaCaptura = new SimpleDateFormat(MetadatosDocumentoSW.FORMATO_FECHA).format(fechaCaptura);
        
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
        this.sFechaCaptura = new SimpleDateFormat(MetadatosDocumentoSW.FORMATO_FECHA).format(fechaCaptura);
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
        
        MetadatosDocumentoSW other = (MetadatosDocumentoSW) obj;
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
    
    public MetadatosDocumentoBean toMetadatosDocumentoBean(){
    	MetadatosDocumentoBean metadatosDocumentoBean = new MetadatosDocumentoBean();
    	
    	metadatosDocumentoBean.setVersionNTI(this.getVersionNTI());
        
        metadatosDocumentoBean.setOrgano(this.getOrgano());
        metadatosDocumentoBean.setAnioCaptura(this.getAnioCaptura());
        metadatosDocumentoBean.setIdEspecificoDocumento(this.getIdEspecificoDocumento());
        
        metadatosDocumentoBean.setIdentificador(this.getIdentificador());
        
        metadatosDocumentoBean.setFechaCaptura(this.getFechaCaptura());
        metadatosDocumentoBean.setsFechaCaptura(this.getsFechaCaptura());
        
        metadatosDocumentoBean.setOrigen(this.getOrigen());
        metadatosDocumentoBean.setEstadoElaboracion(this.getEstadoElaboracion());
        metadatosDocumentoBean.setIdentificadorDocOrigen(this.getIdentificadorDocOrigen());
        metadatosDocumentoBean.setTipoDocumental(this.getTipoDocumental());
        
        metadatosDocumentoBean.setNombreFormato(this.getNombreFormato());
        
        metadatosDocumentoBean.setTipoFirma(this.getTipoFirma());   
        metadatosDocumentoBean.setCsv(this.getCsv());
        
        metadatosDocumentoBean.setBookId(this.getBookId());
        metadatosDocumentoBean.setFolderId(this.getFolderId());
        metadatosDocumentoBean.setPageId(this.getPageId());
        metadatosDocumentoBean.setFileId(this.getFileId());
        metadatosDocumentoBean.setEntidadId(this.getEntidadId());
        
        metadatosDocumentoBean.setNombreDoc(this.getNombreDoc());
        
        return metadatosDocumentoBean;
    }
}
