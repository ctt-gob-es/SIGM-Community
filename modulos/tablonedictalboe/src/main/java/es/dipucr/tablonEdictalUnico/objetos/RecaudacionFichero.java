package es.dipucr.tablonEdictalUnico.objetos;

public class RecaudacionFichero {
	private String ruta = "";
	private boolean recaudacionNueva = false;
	private boolean recaudacionSWALExpedientes = false;
	private String nombreTabla ="";
	private boolean recaudacionFicheroZona = false;
	private boolean ficheroTexto = false;
	public boolean isRecaudacionNueva() {
		return recaudacionNueva;
	}
	public void setRecaudacionNueva(boolean recaudacionNueva) {
		this.recaudacionNueva = recaudacionNueva;
	}
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
	public boolean isRecaudacionSWALExpedientes() {
		return recaudacionSWALExpedientes;
	}
	public void setRecaudacionSWALExpedientes(boolean recaudacionSWALExpedientes) {
		this.recaudacionSWALExpedientes = recaudacionSWALExpedientes;
	}
}
