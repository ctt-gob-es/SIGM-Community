package ieci.tdw.ispac.ispacmgr.action.form;

import java.util.HashMap;

public class BatchSignForm extends BatchForm {
	private String[] hashs = null;
	private String signs = null;
	private String motivoRechazo = null;
	private String listaDocs = null; 
	private String codEntidad = null;
	private String serialNumber = null;
	private HashMap infoFirma =null;
	private String documentId;
	
	/**
	 * Certificado de usuario.
	 */
	private String signCertificate;

	public String[] getHashs() {
		return hashs;
	}
	
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}


	public void setHashs(String[] hashs) {
		this.hashs = hashs;
	}

	public String getSigns() {
		return signs;
	}

	public void setSigns(String signs) {
		this.signs = signs;
	}
	
	public void clean(){
		hashs = null;
		signs = null;
		setMultibox(null);
	}
	
	/**
	 * 
	 * @return String base64 Con la clave publica del certificado firmanterr
	 * 
	 */
	public String getSignCertificate() {
		return signCertificate;
	}

	/**
	 * 
	 * @param signCertificate
	 */
	public void setSignCertificate(String signCertificate) {
		this.signCertificate = signCertificate;
	}
	

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	public String getMotivoRechazo() {
		return this.motivoRechazo;
	}

	public String getListaDocs() {
		return listaDocs;
	}

	public void setListaDocs(String listaDocs) {
		this.listaDocs = listaDocs;
	}

	public String getCodEntidad() {
		return codEntidad;
	}

	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
	}

	public HashMap getInfoFirma() {
		return infoFirma;
	}

	public void setInfoFirma(HashMap infoFirma) {
		this.infoFirma = infoFirma;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}	
	
}