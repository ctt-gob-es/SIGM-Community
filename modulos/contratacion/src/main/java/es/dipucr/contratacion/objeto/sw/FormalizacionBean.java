package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;

import es.dipucr.contratacion.services.PlataformaContratacionStub.Documento;

public class FormalizacionBean {
	//Datos del contrato.
	private String numContrato;
	private Calendar fechaContrato;
	private Calendar periodoValidezInicioContrato;
	private Calendar periodoValidezFinContrato;
	private Documento docContrato;
	private String porcentajeSubcontratacion;
	private String textoAcuerdoFormalizacion = null;
	private String descripcionPeriodoFormalizacionContrato = null;
	
	
	public String getNumContrato() {
		return numContrato;
	}
	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
	}
	public Calendar getFechaContrato() {
		return fechaContrato;
	}
	public void setFechaContrato(Calendar fechaContrato) {
		this.fechaContrato = fechaContrato;
	}
	public Calendar getPeriodoValidezInicioContrato() {
		return periodoValidezInicioContrato;
	}
	public void setPeriodoValidezInicioContrato(
			Calendar periodoValidezInicioContrato) {
		this.periodoValidezInicioContrato = periodoValidezInicioContrato;
	}
	public Calendar getPeriodoValidezFinContrato() {
		return periodoValidezFinContrato;
	}
	public void setPeriodoValidezFinContrato(Calendar periodoValidezFinContrato) {
		this.periodoValidezFinContrato = periodoValidezFinContrato;
	}
	public Documento getDocContrato() {
		return docContrato;
	}
	public void setDocContrato(Documento docContrato) {
		this.docContrato = docContrato;
	}
	public String getPorcentajeSubcontratacion() {
		return porcentajeSubcontratacion;
	}
	public void setPorcentajeSubcontratacion(String porcentajeSubcontratacion) {
		this.porcentajeSubcontratacion = porcentajeSubcontratacion;
	}
	public String getTextoAcuerdoFormalizacion() {
		return textoAcuerdoFormalizacion;
	}
	public void setTextoAcuerdoFormalizacion(String textoAcuerdoFormalizacion) {
		this.textoAcuerdoFormalizacion = textoAcuerdoFormalizacion;
	}
	public String getDescripcionPeriodoFormalizacionContrato() {
		return descripcionPeriodoFormalizacionContrato;
	}
	public void setDescripcionPeriodoFormalizacionContrato(
			String descripcionPeriodoFormalizacionContrato) {
		this.descripcionPeriodoFormalizacionContrato = descripcionPeriodoFormalizacionContrato;
	}
}
