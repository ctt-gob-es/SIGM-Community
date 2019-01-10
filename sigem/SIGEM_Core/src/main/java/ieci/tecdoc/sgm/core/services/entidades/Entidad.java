package ieci.tecdoc.sgm.core.services.entidades;

public class Entidad {

	private String identificador;
	private String nombreCorto;
	private String nombreLargo;
	private String codigo_ine;
	private String cif;
	private String password_entidad;
	private String dir3;
	private String sia;
	private String deh;
	
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getNombreCorto() {
		return nombreCorto;
	}
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}
	public String getNombreLargo() {
		return nombreLargo;
	}
	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	public String getCodigo_ine() {
		return codigo_ine;
	}
	public void setCodigo_ine(String codigo_ine) {
		this.codigo_ine = codigo_ine;
	}
	public String getDir3() {
		return dir3;
	}
	public void setDir3(String dir3) {
		this.dir3 = dir3;
	}
	public String getSia() {
		return sia;
	}
	public void setSia(String sia) {
		this.sia = sia;
	}
	public String getDeh() {
		return deh;
	}
	public void setDeh(String deh) {
		this.deh = deh;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	public String getPassword_entidad() {
		return password_entidad;
	}
	public void setPassword_entidad(String password_entidad) {
		this.password_entidad = password_entidad;
	}
}
