package es.dipucr.contratacion.objeto.sw;

public class DatosContrato {
	private String objetoContrato = null;
	private Campo procedimientoContratacion = null;
	private Campo tipoContrato = null;
	private Campo subTipoContrato = null;
	private Campo tipoTramitacion = null;
	private Campo tramitacionGasto = null;
	private Campo [] cpv = null;
	private String valorEstimadoContrato = null;
	private String numContrato = null;
	private boolean criteriosMultiples;
	private boolean regulacionArmonizada = false;
	private Campo provinciaContrato = null;
	private String caracteristicasBienes = null;
	//procNegArticulo.Artículo y apartado de la LCAP por el que se aplica procedimiento negociado
	private String procNegCausa = null;
	private String organoContratacion = null;
	private String presupuestoConImpuesto = null;
	private String presupuestoSinImpuesto = null;
	
	public String getObjetoContrato() {
		return objetoContrato;
	}

	public void setObjetoContrato(String objetoContrato) {
		this.objetoContrato = objetoContrato;
	}

	public Campo getProcedimientoContratacion() {
		return procedimientoContratacion;
	}

	public void setProcedimientoContratacion(Campo procedimientoContratacion) {
		this.procedimientoContratacion = procedimientoContratacion;
	}

	public Campo getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(Campo tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public Campo getSubTipoContrato() {
		return subTipoContrato;
	}

	public void setSubTipoContrato(Campo subTipoContrato) {
		this.subTipoContrato = subTipoContrato;
	}

	public Campo getTipoTramitacion() {
		return tipoTramitacion;
	}

	public void setTipoTramitacion(Campo tipoTramitacion) {
		this.tipoTramitacion = tipoTramitacion;
	}

	public Campo getTramitacionGasto() {
		return tramitacionGasto;
	}

	public void setTramitacionGasto(Campo tramitacionGasto) {
		this.tramitacionGasto = tramitacionGasto;
	}

	public Campo [] getCpv() {
		return cpv;
	}

	public void setCpv(Campo [] cpv) {
		this.cpv = cpv;
	}

	public String getValorEstimadoContrato() {
		return valorEstimadoContrato;
	}

	public void setValorEstimadoContrato(String valorEstimadoContrato) {
		this.valorEstimadoContrato = valorEstimadoContrato;
	}

	public String getNumContrato() {
		return numContrato;
	}

	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
	}

	public boolean getCriteriosMultiples() {
		return criteriosMultiples;
	}

	public void setCriteriosMultiples(boolean criteriosMultiples) {
		this.criteriosMultiples = criteriosMultiples;
	}

	public boolean isRegulacionArmonizada() {
		return regulacionArmonizada;
	}

	public void setRegulacionArmonizada(boolean regulacionArmonizada) {
		this.regulacionArmonizada = regulacionArmonizada;
	}

	public Campo getProvinciaContrato() {
		return provinciaContrato;
	}

	public void setProvinciaContrato(Campo provinciaContrato) {
		this.provinciaContrato = provinciaContrato;
	}

	public String getCaracteristicasBienes() {
		return caracteristicasBienes;
	}

	public void setCaracteristicasBienes(String caracteristicasBienes) {
		this.caracteristicasBienes = caracteristicasBienes;
	}

	public String getOrganoContratacion() {
		return organoContratacion;
	}

	public void setOrganoContratacion(String organoContratacion) {
		this.organoContratacion = organoContratacion;
	}

	public String getProcNegCausa() {
		return procNegCausa;
	}

	public void setProcNegCausa(String procNegCausa) {
		this.procNegCausa = procNegCausa;
	}

	public String getPresupuestoConImpuesto() {
		return presupuestoConImpuesto;
	}

	public void setPresupuestoConImpuesto(String presupuestoConImpuesto) {
		this.presupuestoConImpuesto = presupuestoConImpuesto;
	}

	public String getPresupuestoSinImpuesto() {
		return presupuestoSinImpuesto;
	}

	public void setPresupuestoSinImpuesto(String presupuestoSinImpuesto) {
		this.presupuestoSinImpuesto = presupuestoSinImpuesto;
	}

}
