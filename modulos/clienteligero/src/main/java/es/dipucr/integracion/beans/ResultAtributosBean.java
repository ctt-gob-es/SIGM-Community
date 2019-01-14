package es.dipucr.integracion.beans;

public class ResultAtributosBean {

	private String idPeticion = null;
	private String numElementos = null;
	private String timeStamp = null;
	private ResultEstadoBean resultEstadoBean = null;
	private String codigoCertificado = null;
	
	public ResultAtributosBean() {}
	
	
	

	public String getIdPeticion() {
		return idPeticion;
	}

	public void setIdPeticion(String idPeticion) {
		this.idPeticion = idPeticion;
	}

	public String getNumElementos() {
		return numElementos;
	}

	public void setNumElementos(String numElementos) {
		this.numElementos = numElementos;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ResultEstadoBean getResultEstadoBean() {
		return resultEstadoBean;
	}

	public void setResultEstadoBean(ResultEstadoBean resultEstadoBean) {
		this.resultEstadoBean = resultEstadoBean;
	}

	public String getCodigoCertificado() {
		return codigoCertificado;
	}

	public void setCodigoCertificado(String codigoCertificado) {
		this.codigoCertificado = codigoCertificado;
	}
	
	
	
}
