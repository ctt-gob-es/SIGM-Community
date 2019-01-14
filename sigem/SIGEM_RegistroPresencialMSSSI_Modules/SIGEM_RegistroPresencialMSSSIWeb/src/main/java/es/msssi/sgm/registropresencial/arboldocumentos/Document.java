package es.msssi.sgm.registropresencial.arboldocumentos;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.bussinessobject.MetadatosBo;
import es.ieci.tecdoc.fwktd.util.file.FileUtils;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
 
public class Document implements Serializable, Comparable<Document> {
	
	private static final long serialVersionUID = 1L;

	private Axdoch doch;
	private Axpageh documento;
	
	private List<Document> listaDocumentos;
	
	private String nombre;
	private String extension;
    private String fileSizeKB;
    private Integer tipoDocumento;
    private Integer tipoValidez;
    private String comentario;
    private Date crtndate;
    private boolean esDocumento;
    private boolean esFirmado;
    
    //page de la firma
    private Axpageh pageSigned; 
    
    private String hash;
    private String fechaFirma;
    
    private MetadatosDocumentoBean metadatos;
    private String entidadId;
     
    public String getEntidadId() {
		return entidadId;
	}

	public void setEntidadId(String entidadId) {
		this.entidadId = entidadId;
	}

	public Document(String nombre, String fileSizeKB, Integer tipoDocumento, Integer tipoValidez, String comentario, Date crtndate,  boolean esDocumento, Axdoch doch, Axpageh documento, Axpageh pageSigned, String hash, String fechaFirma, Integer bookId, String entidadId) {
    	this.doch = doch;
    	this.documento = documento;
    	
    	this.listaDocumentos = new ArrayList<Document>();
    	
        this.nombre = nombre;
        this.extension = FileUtils.getExtension(nombre);
        this.fileSizeKB = fileSizeKB;
        this.tipoDocumento = tipoDocumento;
        this.tipoValidez = tipoValidez;
        this.comentario = comentario;
        this.crtndate = crtndate;
        this.esDocumento = esDocumento;        
        this.pageSigned = pageSigned;
        this.hash = hash;
        this.fechaFirma = fechaFirma;
        this.entidadId = entidadId;
        
        if(null != documento){
        	this.metadatos = new MetadatosDocumentoBean(bookId, documento.getFdrid(), documento.getPageId(), documento.getFileId(), entidadId, nombre);
        	MetadatosBo.getMetadatos(this.metadatos);
        }
        			
    }
 
    public Document(Axdoch doch) {
    	this.doch = doch;    	
		this.documento = null;
		
		this.listaDocumentos = new ArrayList<Document>();
		
		this.nombre = doch.getName();
		this.extension = "";
        this.fileSizeKB = "";
        this.tipoDocumento = new Integer(0);
        this.tipoValidez = new Integer(0);
        this.comentario = "";
        this.crtndate = doch.getCrtndate();
        this.esDocumento = false;       
        this.pageSigned = null;
        this.hash = null;
        this.fechaFirma = null;
        
        this.metadatos = null;
    }
    
    public Document(Axpageh documento, Integer bookId, String entidadId) {
    	this.doch = null;
    	this.documento = documento;
    	
    	this.listaDocumentos = new ArrayList<Document>();
    	
    	this.nombre = documento.getName();
    	this.extension = FileUtils.getExtension(documento.getName());
        this.fileSizeKB = documento.getFileSizeKB();
        this.tipoDocumento = documento.getDocumentType();
        this.tipoValidez = documento.getValidityType();
        this.comentario = documento.getComments();
        this.crtndate = documento.getCrtndate();
        this.esDocumento = true;
        this.pageSigned = documento.getPageSigned();
        
        this.hash = null;
        if(null != documento.getHash()){
        	this.hash = new String(documento.getHash());
        }
        
        this.fechaFirma = null;
        if(null != documento.getFechaFirma()){
        	this.fechaFirma = new String(documento.getFechaFirma());
        }
        
        this.entidadId = entidadId;
        
        if(null != documento){
        	this.metadatos = new MetadatosDocumentoBean(bookId, documento.getFdrid(), documento.getPageId(), documento.getFileId(), entidadId, this.nombre);
        	MetadatosBo.getMetadatos(this.metadatos);
        	
        	if(StringUtils.isEmpty(this.metadatos.getVersionNTI())){
        		MetadatosBo.setMetadatosDefecto(this.metadatos, this.crtndate, this.nombre);
        	}
        }
    }

	//Eclipse Generated hashCode and equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((fileSizeKB == null) ? 0 : fileSizeKB.hashCode());
        result = prime * result + ((tipoDocumento == null) ? 0 : tipoDocumento.hashCode());
        result = prime * result + ((tipoValidez == null) ? 0 : tipoValidez.hashCode());
        result = prime * result + ((comentario == null) ? 0 : comentario.hashCode());
        result = prime * result + ((crtndate == null) ? 0 : crtndate.hashCode());
        result = prime * result + ((doch == null) ? 0 : doch.hashCode());
        result = prime * result + ((documento == null) ? 0 : documento.hashCode());  
        result = prime * result + ((pageSigned == null) ? 0 : pageSigned.hashCode());
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
        result = prime * result + ((fechaFirma == null) ? 0 : fechaFirma.hashCode());
        result = prime * result + ((listaDocumentos == null) ? 0 : listaDocumentos.hashCode());
        result = prime * result + ((metadatos == null) ? 0 : metadatos.hashCode());
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Document other = (Document) obj;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (fileSizeKB == null) {
            if (other.fileSizeKB != null)
                return false;
        } else if (!fileSizeKB.equals(other.fileSizeKB))
            return false;
        if (tipoDocumento == null) {
            if (other.tipoDocumento != null)
                return false;
        } else if (!tipoDocumento.equals(other.tipoDocumento))
            return false;
        if (tipoValidez == null) {
            if (other.tipoValidez != null)
                return false;
        } else if (!tipoValidez.equals(other.tipoValidez))
            return false;
        if (comentario == null) {
            if (other.comentario != null)
                return false;
        } else if (!comentario.equals(other.comentario))
            return false;
        if (crtndate == null) {
            if (other.crtndate != null)
                return false;
        } else if (!crtndate.equals(other.crtndate))
            return false;
        if (doch == null) {
            if (other.doch != null)
                return false;
        } else if (!doch.equals(other.doch))
            return false;
        if (documento == null) {
            if (other.documento != null)
                return false;
        } else if (!documento.equals(other.documento))
            return false;
        if (pageSigned == null) {
            if (other.pageSigned != null)
                return false;
        } else if (!pageSigned.equals(other.pageSigned))
            return false;
        
        if (hash == null) {
            if (other.hash != null)
                return false;
        } else if (!hash.equals(other.hash))
            return false;        
        if (fechaFirma == null) {
            if (other.fechaFirma != null)
                return false;
        } else if (!fechaFirma.equals(other.fechaFirma))
            return false;
        if (listaDocumentos == null) {
            if (other.listaDocumentos != null)
                return false;
        } else if (!listaDocumentos.equals(other.listaDocumentos))
            return false;
        if (metadatos == null) {
            if (other.metadatos != null)
                return false;
        } else if (!metadatos.equals(other.metadatos))
            return false;
        
        return esDocumento == other.esDocumento;        
    }
 
 
    public int compareTo(Document document) {
        return this.getNombre().compareTo(document.getNombre());
    }

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFileSizeKB() {
		return fileSizeKB;
	}

	public void setFileSizeKB(String fileSizeKB) {
		this.fileSizeKB = fileSizeKB;
	}

	public Integer getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(Integer tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public Integer getTipoValidez() {
		return tipoValidez;
	}

	public void setTipoValidez(Integer tipoValidez) {
		this.tipoValidez = tipoValidez;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getCrtndate() {
		return crtndate;
	}

	public void setCrtndate(Date crtndate) {
		this.crtndate = crtndate;
	}

	public Axpageh getDocumento() {
		return documento;
	}

	public void setDocumento(Axpageh documento) {
		this.documento = documento;
	}
	
	public Axpageh getPageSigned() {
		return pageSigned;
	}

	public void setPageSigned(Axpageh pageSigned) {
		this.pageSigned = pageSigned;
	}

	public boolean isEsDocumento() {
		return esDocumento;
	}

	public void setEsDocumento(boolean esDocumento) {
		this.esDocumento = esDocumento;
	}

	public Axdoch getDoch() {
		return doch;
	}

	public void setDoch(Axdoch doch) {
		this.doch = doch;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getFechaFirma() {
		return fechaFirma;
	}

	public void setFechaFirma(String fechaFirma) {
		this.fechaFirma = fechaFirma;
	}

	public MetadatosDocumentoBean getMetadatos() {
		return metadatos;
	}

	public void setMetadatos(MetadatosDocumentoBean metadatos) {
		this.metadatos = metadatos;
	}

	public boolean isEsFirmado() {
		esFirmado = StringUtils.isNotEmpty(fechaFirma);
		return esFirmado;
	}

	public void setEsFirmado(boolean esFirmado) {
		this.esFirmado = esFirmado;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public List<Document> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<Document> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}
}
