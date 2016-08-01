package ieci.tdw.ispac.ispacmgr.action.form;

import java.util.Calendar;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

public class SignForm extends BatchForm {

	private static final long serialVersionUID = 1L;

	private String method;
	private String hash;
	private String sign;
	private String signs;
	private String documentId;
	
	private String listaDocs = null; 
	private String codEntidad = null;
	private String serialNumber = null;
	private HashMap<String, String> infoFirma =null;

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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public HashMap<String, String> getInfoFirma() {
		return infoFirma;
	}

	public void setInfoFirma(HashMap<String, String> infoFirma) {
		this.infoFirma = infoFirma;
	}

	//Propiedades del proceso de firma
    private String subject;
	private String fstart;
	private String fexpiration;
	private String content;
	private String levelOfImportance;

	/**
	 * Certificado de usuario.
	 */
	private String signCertificate;

	/**
	 * Formato de firma.
	 */
	private String signFormat;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return El valor de signs
	 */
	public String getSigns() {
		return signs;
	}

	/**
	 * @param signs El valor de signs
	 */
	public void setSigns(String signs) {
		this.signs = signs;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSignCertificate() {
		return signCertificate;
	}

	public void setSignCertificate(String signCertificate) {
		this.signCertificate = signCertificate;
	}

	public String getSignFormat() {
		return signFormat;
	}

	public void setSignFormat(String signFormat) {
		this.signFormat = signFormat;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFstart() {
		return fstart;
	}

	public void setFstart(String fstart) {
		this.fstart=fstart;
	}

	public String getFexpiration() {
		return fexpiration;
	}

	public void setFexpiration(String fexpiration) {
		this.fexpiration = fexpiration;

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLevelOfImportance() {
		return levelOfImportance;
	}

	public void setLevelOfImportance(String levelOfImportance) {
		this.levelOfImportance = levelOfImportance;
	}


	public void resetProperties (){
		fstart="";
		levelOfImportance="";
		fexpiration="";
		content="";
		subject="";
	}

	public void clean(){
		signs = null;
		setMultibox(null);
	}


}