package es.dipucr.contratacion.objeto;

import java.util.Calendar;


public class BOP {
	private String nombreBOP="Boletín Oficial de la Provincia de Ciudad Real";
	private Calendar fechaPublicacion;
	private String numAnuncio;
	private String urlPublicacion;
	public String getNombreBOP() {
		return nombreBOP;
	}
	public void setNombreBOP(String nombreBOP) {
		this.nombreBOP = nombreBOP;
	}
	public Calendar getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(Calendar fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public String getNumAnuncio() {
		return numAnuncio;
	}
	public void setNumAnuncio(String numAnuncio) {
		this.numAnuncio = numAnuncio;
	}
	public String getUrlPublicacion() {
		return urlPublicacion;
	}
	public void setUrlPublicacion(String urlPublicacion) {
		this.urlPublicacion = urlPublicacion;
	}

}
