package ieci.tdw.ispac.ispaclib.sign;

public class InfoFirmante {
	
	//dni
	private String idDocumentoDeIdentidadEnCertificado;
	private String nombreFirmante;	
	private String certificado;	
	
	public String getNombreFirmante() {
		return nombreFirmante;
	}
	public void setNombreFirmante(String nombreFirmante) {
		this.nombreFirmante = nombreFirmante;
	}
	public String getIdDocumentoDeIdentidadEnCertificado() {
		return idDocumentoDeIdentidadEnCertificado;
	}
	public void setIdDocumentoDeIdentidadEnCertificado(
			String idDocumentoDeIdentidadEnCertificado) {
		this.idDocumentoDeIdentidadEnCertificado = idDocumentoDeIdentidadEnCertificado;
	}
	public String getCertificado() {
		return certificado;
	}
	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

}
