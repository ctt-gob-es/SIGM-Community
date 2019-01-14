package es.dipucr.sigem.api.rule.common;

public class DipucrDatosParticipante {
	
	String nif ="";
	String nif_participante = "";
	boolean usuario_en_comparece = false;
	String entidad = "";
	String nombreDestinatario ="";
	String apellidosDestinatario="";
	String emailDestinatario="";
	String telefonoDestinatario="";
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public boolean isUsuario_en_comparece() {
		return usuario_en_comparece;
	}
	public void setUsuario_en_comparece(boolean usuario_en_comparece) {
		this.usuario_en_comparece = usuario_en_comparece;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getNombreDestinatario() {
		return nombreDestinatario;
	}
	public void setNombreDestinatario(String nombreDestinatario) {
		this.nombreDestinatario = nombreDestinatario;
	}
	public String getApellidosDestinatario() {
		return apellidosDestinatario;
	}
	public void setApellidosDestinatario(String apellidosDestinatario) {
		this.apellidosDestinatario = apellidosDestinatario;
	}
	public String getEmailDestinatario() {
		return emailDestinatario;
	}
	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}
	public String getTelefonoDestinatario() {
		return telefonoDestinatario;
	}
	public void setTelefonoDestinatario(String telefonoDestinatario) {
		this.telefonoDestinatario = telefonoDestinatario;
	}
	public String getNif_participante() {
		return nif_participante;
	}
	public void setNif_participante(String nif_participante) {
		this.nif_participante = nif_participante;
	}
}
