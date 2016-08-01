package es.dipucr.contratacion.objeto;

import java.util.Calendar;

public class DocumentacionPresentar {
	private Calendar fechaApertura;
	private String hora;
	public Calendar getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(Calendar fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	
}
