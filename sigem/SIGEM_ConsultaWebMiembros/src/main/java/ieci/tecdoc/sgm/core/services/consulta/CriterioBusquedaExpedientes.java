package ieci.tecdoc.sgm.core.services.consulta;

public class CriterioBusquedaExpedientes {

	private String NIF;
	private String fechaDesde;
	private String fechaHasta;
	private String operadorConsulta;
	private String estado;
	
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getNIF() {
		return NIF;
	}
	public void setNIF(String nif) {
		NIF = nif;
	}
	public String getOperadorConsulta() {
		return operadorConsulta;
	}
	public void setOperadorConsulta(String operadorConsulta) {
		this.operadorConsulta = operadorConsulta;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
