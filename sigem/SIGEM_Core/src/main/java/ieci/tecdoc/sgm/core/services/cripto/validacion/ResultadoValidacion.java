package ieci.tecdoc.sgm.core.services.cripto.validacion;

import java.util.Map;

public class ResultadoValidacion {

	public static final String VALIDACION_OK 	= "0";
	public static final String VALIDACION_ERROR	= "-1";
	
	private Map<String, Object> propertiesResult;
	
	private String resultadoValidacion;
	
	private String mensajeValidacion;

	private InfoCertificado certificado;
	
	public InfoCertificado getCertificado() {
		return certificado;
	}

	public void setCertificado(InfoCertificado certificado) {
		this.certificado = certificado;
	}

	public String getMensajeValidacion() {
		return mensajeValidacion;
	}

	public void setMensajeValidacion(String mensajeValidacion) {
		this.mensajeValidacion = mensajeValidacion;
	}

	public String getResultadoValidacion() {
		return resultadoValidacion;
	}

	public void setResultadoValidacion(String resultadoValidacion) {
		this.resultadoValidacion = resultadoValidacion;
	}

	public Map<String, Object> getPropertiesResult() {
		return propertiesResult;
	}

	public void setPropertiesResult(Map<String, Object> propertiesResult) {
		this.propertiesResult = propertiesResult;
	}	
	
}
