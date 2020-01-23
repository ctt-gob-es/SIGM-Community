
package es.dipucr.contratacion.objeto.sw;

public class Lote {
    private Campo[] cpv;
    private String descripcion;
    private String idLote;
    private Campo lugarEjecucionContratoPais;
    private Campo lugarEjecucionContratoPaisNUTS;
    private Campo lugarEjecucionContratoProvincia;
    private Double presupuestoConIva;
    private Double presupuestoSinIva;
    private DatosTramitacion datosTramitacion;
	public Campo[] getCpv() {
		return cpv;
	}
	public void setCpv(Campo[] cpv) {
		this.cpv = cpv;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdLote() {
		return idLote;
	}
	public void setIdLote(String idLote) {
		this.idLote = idLote;
	}
	public Campo getLugarEjecucionContratoPais() {
		return lugarEjecucionContratoPais;
	}
	public void setLugarEjecucionContratoPais(Campo lugarEjecucionContratoPais) {
		this.lugarEjecucionContratoPais = lugarEjecucionContratoPais;
	}
	public Campo getLugarEjecucionContratoPaisNUTS() {
		return lugarEjecucionContratoPaisNUTS;
	}
	public void setLugarEjecucionContratoPaisNUTS(
			Campo lugarEjecucionContratoPaisNUTS) {
		this.lugarEjecucionContratoPaisNUTS = lugarEjecucionContratoPaisNUTS;
	}
	public Campo getLugarEjecucionContratoProvincia() {
		return lugarEjecucionContratoProvincia;
	}
	public void setLugarEjecucionContratoProvincia(
			Campo lugarEjecucionContratoProvincia) {
		this.lugarEjecucionContratoProvincia = lugarEjecucionContratoProvincia;
	}
	public Double getPresupuestoConIva() {
		return presupuestoConIva;
	}
	public void setPresupuestoConIva(Double presupuestoConIva) {
		this.presupuestoConIva = presupuestoConIva;
	}
	public Double getPresupuestoSinIva() {
		return presupuestoSinIva;
	}
	public void setPresupuestoSinIva(Double presupuestoSinIva) {
		this.presupuestoSinIva = presupuestoSinIva;
	}
	public DatosTramitacion getDatosTramitacion() {
		return datosTramitacion;
	}
	public void setDatosTramitacion(DatosTramitacion datosTramitacion) {
		this.datosTramitacion = datosTramitacion;
	}

}
