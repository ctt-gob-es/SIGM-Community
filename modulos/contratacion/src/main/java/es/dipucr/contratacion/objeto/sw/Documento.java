package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;

public class Documento {

	//private String publicationId;
	private String expedientNumber = null;
	private String buyerProfileId = null;
	private String urlDocument = null;
	private Calendar fechaFirma = null;
	private String typeDoc = null;
	private String idTypeDoc = null;
	private String descripcion = null;
	private byte [] contenido = null;
	private String organoContratacion = null;
	
	private String nameDoc = "";
	
	private String mimeCode = "";
	
	
	public String getIdTypeDoc() {
		return idTypeDoc;
	}
	public void setIdTypeDoc(String idTypeDoc) {
		this.idTypeDoc = idTypeDoc;
	}
	public String getTypeDoc() {
		return typeDoc;
	}
	public void setTypeDoc(String typeDoc) {
		this.typeDoc = typeDoc;
	}
	public String getPublicationId() {
		return String.valueOf(System.currentTimeMillis());
	}
//	public void setPublicationId(String publicationId) {
//		this.publicationId = publicationId;
//	}
	public String getExpedientNumber() {
		return expedientNumber;
	}
	public void setExpedientNumber(String expedientNumber) {
		this.expedientNumber = expedientNumber;
	}
	public String getBuyerProfileId() {
		return buyerProfileId;
	}
	public void setBuyerProfileId(String buyerProfileId) {
		this.buyerProfileId = buyerProfileId;
	}
	public String getUrlDocument() {
		return urlDocument;
	}
	public void setUrlDocument(String urlDocument) {
		this.urlDocument = urlDocument;
	}
	public byte[] getContenido() {
		return contenido;
	}
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	public String getNameDoc() {
		return nameDoc;
	}
	public void setNameDoc(String nameDoc) {
		this.nameDoc = nameDoc;
	}
	public String getMimeCode() {
		return mimeCode;
	}
	public void setMimeCode(String mimeCode) {
		this.mimeCode = mimeCode;
	}
	public Calendar getFechaFirma() {
		return fechaFirma;
	}
	public void setFechaFirma(Calendar fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getOrganoContratacion() {
		return organoContratacion;
	}
	public void setOrganoContratacion(String organoContratacion) {
		this.organoContratacion = organoContratacion;
	}	
}
