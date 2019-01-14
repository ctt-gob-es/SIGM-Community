package es.dipucr.contratacion.objeto.sw;


public class DatosEmpresa {
	
	private Campo [] clasificacion;
	private Campo clasificacionEvidence = null;
	private RequisitfiDeclaraciones [] tipoDeclaracion;
	private CondicionesLicitadores condLicit;
	
	public Campo [] getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(Campo [] clasificacion) {
		this.clasificacion = clasificacion;
	}
	
	public CondicionesLicitadores getCondLicit() {
		return condLicit;
	}
	public void setCondLicit(CondicionesLicitadores condLicit) {
		this.condLicit = condLicit;
	}
	public RequisitfiDeclaraciones [] getTipoDeclaracion() {
		return tipoDeclaracion;
	}
	public void setTipoDeclaracion(RequisitfiDeclaraciones [] tipoDeclaracion) {
		this.tipoDeclaracion = tipoDeclaracion;
	}
	public Campo getClasificacionEvidence() {
		return clasificacionEvidence;
	}
	public void setClasificacionEvidence(Campo clasificacionEvidence) {
		this.clasificacionEvidence = clasificacionEvidence;
	}

}
