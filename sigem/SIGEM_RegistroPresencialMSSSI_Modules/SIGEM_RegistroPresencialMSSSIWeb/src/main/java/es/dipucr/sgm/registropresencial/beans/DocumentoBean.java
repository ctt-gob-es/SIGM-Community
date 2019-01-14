package es.dipucr.sgm.registropresencial.beans;

import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.msssi.sgm.registropresencial.arboldocumentos.Document;
import es.msssi.sgm.registropresencial.beans.GenericBean;

public class DocumentoBean extends GenericBean {
	
    private static final long serialVersionUID = 1L;
    
    private UseCaseConf useCaseConf;
    private Object registerBean;
	private Integer bookId;
	private String entidadId;
    
	private Document selectAnexo;
	private TreeNode arbolDocumentos;
	
	private String nombreViejoDocumento;
	private String nombreNuevoDocumento;
	private String nombreModificarDocumento;
	private String carpetaAnexarDoc;
	
	private UploadedFile file;
	
	
	public UseCaseConf getUseCaseConf() {
		return useCaseConf;
	}

	public void setUseCaseConf(UseCaseConf useCaseConf) {
		this.useCaseConf = useCaseConf;
	}
	
	public Object getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(Object registerBean) {
		this.registerBean = registerBean;
	}
	
	public Integer getBookId() {
		return bookId;
	}

	public String getEntidadId() {
		return entidadId;
	}

	public void setEntidadId(String entidadId) {
		this.entidadId = entidadId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	
	public Document getSelectAnexo() {
		return selectAnexo;
	}

	public void setSelectAnexo(Document selectAnexo) {
		this.selectAnexo = selectAnexo;
	}

	public TreeNode getArbolDocumentos() {
		return arbolDocumentos;
	}

	public void setArbolDocumentos(TreeNode arbolDocumentos) {
		this.arbolDocumentos = arbolDocumentos;
	}

	public String getNombreViejoDocumento() {
		return nombreViejoDocumento;
	}

	public void setNombreViejoDocumento(String nombreViejoDocumento) {
		this.nombreViejoDocumento = nombreViejoDocumento;
	}

	public String getNombreNuevoDocumento() {
		return nombreNuevoDocumento;
	}

	public void setNombreNuevoDocumento(String nombreNuevaDocumento) {
		this.nombreNuevoDocumento = nombreNuevaDocumento;
	}

	public String getNombreModificarDocumento() {
		return nombreModificarDocumento;
	}

	public void setNombreModificarDocumento(String nombreModificarDocumento) {
		this.nombreModificarDocumento = nombreModificarDocumento;
	}

	public String getCarpetaAnexarDoc() {
		return carpetaAnexarDoc;
	}

	public void setCarpetaAnexarDoc(String carpetaAnexarDoc) {
		this.carpetaAnexarDoc = carpetaAnexarDoc;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}
