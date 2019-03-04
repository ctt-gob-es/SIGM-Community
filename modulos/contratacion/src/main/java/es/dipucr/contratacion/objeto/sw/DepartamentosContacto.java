package es.dipucr.contratacion.objeto.sw;

public class DepartamentosContacto {
	private Departamento personalContactoContratacion = null;
	private Departamento personalContactoSecretaria = null;
	private Departamento personalContactoOrganoAsistencia = null;
	public Departamento getPersonalContactoContratacion() {
		return personalContactoContratacion;
	}
	public void setPersonalContactoContratacion(
			Departamento personalContactoContratacion) {
		this.personalContactoContratacion = personalContactoContratacion;
	}
	public Departamento getPersonalContactoSecretaria() {
		return personalContactoSecretaria;
	}
	public void setPersonalContactoSecretaria(
			Departamento personalContactoSecretaria) {
		this.personalContactoSecretaria = personalContactoSecretaria;
	}
	public Departamento getPersonalContactoOrganoAsistencia() {
		return personalContactoOrganoAsistencia;
	}
	public void setPersonalContactoOrganoAsistencia(
			Departamento personalContactoOrganoAsistencia) {
		this.personalContactoOrganoAsistencia = personalContactoOrganoAsistencia;
	}
}
