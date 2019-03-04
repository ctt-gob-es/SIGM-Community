
package es.dipucr.contratacion.objeto.sw;

public class Departamento{
    private String calle;
    private String ciudad;
    private Campo codFormatoDirec;
    private String cp;
    private String email;
    private Campo localizacionGeografica;
    private String nombreContacto;
    private String numeroEdificio;
    private Campo pais;
    private String provincia;
    private String telefono;
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public Campo getCodFormatoDirec() {
		return codFormatoDirec;
	}
	public void setCodFormatoDirec(Campo codFormatoDirec) {
		this.codFormatoDirec = codFormatoDirec;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Campo getLocalizacionGeografica() {
		return localizacionGeografica;
	}
	public void setLocalizacionGeografica(Campo localizacionGeografica) {
		this.localizacionGeografica = localizacionGeografica;
	}
	public String getNombreContacto() {
		return nombreContacto;
	}
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	public String getNumeroEdificio() {
		return numeroEdificio;
	}
	public void setNumeroEdificio(String numeroEdificio) {
		this.numeroEdificio = numeroEdificio;
	}
	public Campo getPais() {
		return pais;
	}
	public void setPais(Campo pais) {
		this.pais = pais;
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
}
