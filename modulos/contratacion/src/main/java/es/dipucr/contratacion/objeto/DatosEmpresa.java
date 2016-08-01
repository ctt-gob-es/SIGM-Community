package es.dipucr.contratacion.objeto;

import es.dipucr.contratacion.client.beans.Campo;
import es.dipucr.contratacion.client.beans.CondicionesLicitadores;
import es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones;

public class DatosEmpresa {
	
	private Campo [] clasificacion;
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

}
