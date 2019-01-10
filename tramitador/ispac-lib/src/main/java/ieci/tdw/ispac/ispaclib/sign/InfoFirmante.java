package ieci.tdw.ispac.ispaclib.sign;

public class InfoFirmante {
	
	private String docIdentidadCertificado;
	private String nombreFirmante;	
	private String numeroSerie;
	private String cargo;
	
	public String getDocIdentidadCertificado() {
		return docIdentidadCertificado;
	}

	public void setDocIdentidadCertificado(String docIdentidadCertificado) {
		this.docIdentidadCertificado = docIdentidadCertificado;
	}
	
	public String getNombreFirmante() {
		return nombreFirmante;
	}

	public void setNombreFirmante(String nombreFirmante) {
		this.nombreFirmante = nombreFirmante;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
}
