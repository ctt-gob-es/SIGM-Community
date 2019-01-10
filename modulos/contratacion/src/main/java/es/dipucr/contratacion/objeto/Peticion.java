package es.dipucr.contratacion.objeto;

public class Peticion {
	private String presupuestoConIva;
	private String presupuestoSinIva;
	private String objetoContrato;
	
	public String getPresupuestoConIva() {
		return presupuestoConIva;
	}
	public void setPresupuestoConIva(String presupuestoConIva) {
		this.presupuestoConIva = presupuestoConIva;
	}
	public String getPresupuestoSinIva() {
		return presupuestoSinIva;
	}
	public void setPresupuestoSinIva(String presupuestoSinIva) {
		this.presupuestoSinIva = presupuestoSinIva;
	}
	public String getObjetoContrato() {
		return objetoContrato;
	}
	public void setObjetoContrato(String objetoContrato) {
		this.objetoContrato = objetoContrato;
	}
}
