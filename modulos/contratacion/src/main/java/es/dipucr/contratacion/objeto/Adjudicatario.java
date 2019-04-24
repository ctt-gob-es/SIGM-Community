package es.dipucr.contratacion.objeto;

import ieci.tecdoc.sgm.core.services.tramitacion.dto.DocumentoExpediente;

public class Adjudicatario {	
	
	private String identificador = null;
	private String direccionPostal = null;
	private String codigoPostal = null;
	private String municipio = null;
	private String provincia = null;
	private String telefono = null;
	private String nombre = null;
	private String mail = null;
	private String lote = null;
	private DocumentoExpediente documento = null;
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public DocumentoExpediente getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoExpediente documento) {
		this.documento = documento;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getDireccionPostal() {
		return direccionPostal;
	}
	public void setDireccionPostal(String direccionPostal) {
		this.direccionPostal = direccionPostal;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
}
