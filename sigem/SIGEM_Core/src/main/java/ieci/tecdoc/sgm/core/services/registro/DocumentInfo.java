package ieci.tecdoc.sgm.core.services.registro;


/**
 * Informacion de un document
 * 
 */
public class DocumentInfo {
	/**
	 * Nombre del documento
	 */
	protected String documentName = null;

	/**
	 * Nombre del fichero
	 */
	protected String fileName = null;

	/**
	 * Nombre de la página del documento
	 */
	protected String pageName = null;

	/**
	 * Contenido del fichero
	 */
	protected byte[] documentContent = null;

	/**
	 * Extensión del fichero
	 */
	protected String extension = null;
	
    /* Tipo documental
     */
    private String tipoDocumental;

    /* Tipo de firma del documento
     */
    private String tipoFirma;
    
    /* CSV del documento
     */
    private String csv;
    
    /* Metadatos del documento
     */
    private Object metadatosDocumento = null;

	/**
	 * @return
	 */
	public byte[] getDocumentContent() {
		return documentContent;
	}

	/**
	 * @param documentContent
	 */
	public void setDocumentContent(byte[] documentContent) {
		this.documentContent = documentContent;
	}

	/**
	 * @return
	 */
	public String getDocumentName() {
		return documentName;
	}

	/**
	 * @param documentName
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	public String getTipoDocumental() {
		return tipoDocumental;
	}

	public void setTipoDocumental(String tipoDocumental) {
		this.tipoDocumental = tipoDocumental;
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

	public Object getMetadatosDocumento() {
		return metadatosDocumento;
	}

	public void setMetadatosDocumento(Object metadatosDocumento) {
		this.metadatosDocumento = metadatosDocumento;
	}
	
}
