package es.dipucr.integracion.beans;

public class ResultSolicitanteBean {
	
	private String identificadorSolicitante = null;
	private String nombreSolicitante = null;
	private String unidadTramitadora = null;
	private ResultProcedimientoBean resultProcedimientoBean = null;
	private String finalidad = null;
	private String consentimiento = null;
	private ResultFuncionarioBean resultFuncionarioBean = null;
	private String idExpediente = null;
	
	
	public ResultSolicitanteBean() {}


	public String getIdentificadorSolicitante() {
		return identificadorSolicitante;
	}


	public void setIdentificadorSolicitante(String identificadorSolicitante) {
		this.identificadorSolicitante = identificadorSolicitante;
	}


	public String getNombreSolicitante() {
		return nombreSolicitante;
	}


	public void setNombreSolicitante(String nombreSolicitante) {
		this.nombreSolicitante = nombreSolicitante;
	}


	public String getUnidadTramitadora() {
		return unidadTramitadora;
	}


	public void setUnidadTramitadora(String unidadTramitadora) {
		this.unidadTramitadora = unidadTramitadora;
	}


	public ResultProcedimientoBean getResultProcedimientoBean() {
		return resultProcedimientoBean;
	}


	public void setResultProcedimientoBean(
			ResultProcedimientoBean resultProcedimientoBean) {
		this.resultProcedimientoBean = resultProcedimientoBean;
	}


	public String getFinalidad() {
		return finalidad;
	}


	public void setFinalidad(String finalidad) {
		this.finalidad = finalidad;
	}


	public String getConsentimiento() {
		return consentimiento;
	}


	public void setConsentimiento(String consentimiento) {
		this.consentimiento = consentimiento;
	}


	public ResultFuncionarioBean getResultFuncionarioBean() {
		return resultFuncionarioBean;
	}


	public void setResultFuncionarioBean(ResultFuncionarioBean resultFuncionarioBean) {
		this.resultFuncionarioBean = resultFuncionarioBean;
	}


	public String getIdExpediente() {
		return idExpediente;
	}


	public void setIdExpediente(String idExpediente) {
		this.idExpediente = idExpediente;
	}
	
	
	
	
	
}
