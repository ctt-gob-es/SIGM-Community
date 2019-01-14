package es.dipucr.integracion.beans;

public class ResultEstadoBean {
	
	private String codigoEstado = null;
	private String codigoEstadoSecundario = null;
	private String literalError = null;
	private String tiempoEstimadoRespuesta = null;
	
	
	public ResultEstadoBean() {	}
	
	
	
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getCodigoEstadoSecundario() {
		return codigoEstadoSecundario;
	}
	public void setCodigoEstadoSecundario(String codigoEstadoSecundario) {
		this.codigoEstadoSecundario = codigoEstadoSecundario;
	}
	public String getLiteralError() {
		return literalError;
	}
	public void setLiteralError(String literalError) {
		this.literalError = literalError;
	}
	public String getTiempoEstimadoRespuesta() {
		return tiempoEstimadoRespuesta;
	}
	public void setTiempoEstimadoRespuesta(String tiempoEstimadoRespuesta) {
		this.tiempoEstimadoRespuesta = tiempoEstimadoRespuesta;
	}
	
	
	

}
