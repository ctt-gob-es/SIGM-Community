package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

public class InfoAyudaEstudios {

	protected String nif;
	protected String nombre;
	protected int totalPuntos;
	protected double importePunto;
	protected double total;
	protected String tipoEmpleado;
	
	public InfoAyudaEstudios(){}
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getTotalPuntos() {
		return totalPuntos;
	}
	public void setTotalPuntos(int totalPuntos) {
		this.totalPuntos = totalPuntos;
	}
	public double getImportePunto() {
		return importePunto;
	}
	public void setImportePunto(double importePunto) {
		this.importePunto = importePunto;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getTipoEmpleado() {
		return tipoEmpleado;
	}
	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}
	
	
}
