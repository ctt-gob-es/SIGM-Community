package es.dipucr.tablonEdictalUnico.objetos;

public class RecaudacionFichero {
	private String ruta = "";
	private boolean providenciaApremio = false;
	private boolean diligenciasEmbargo = false;
	private String nombreTabla ="";
	private boolean recaudacionFicheroZona = false;
	private boolean ficheroTexto = false;
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	public boolean isRecaudacionFicheroZona() {
		return recaudacionFicheroZona;
	}
	public void setRecaudacionFicheroZona(boolean recaudacionFicheroZona) {
		this.recaudacionFicheroZona = recaudacionFicheroZona;
	}
	public boolean isFicheroTexto() {
		return ficheroTexto;
	}
	public void setFicheroTexto(boolean ficheroTexto) {
		this.ficheroTexto = ficheroTexto;
	}
	public boolean isProvidenciaApremio() {
		return providenciaApremio;
	}
	public void setProvidenciaApremio(boolean providenciaApremio) {
		this.providenciaApremio = providenciaApremio;
	}
	public boolean isDiligenciasEmbargo() {
		return diligenciasEmbargo;
	}
	public void setDiligenciasEmbargo(boolean diligenciasEmbargo) {
		this.diligenciasEmbargo = diligenciasEmbargo;
	}
}
