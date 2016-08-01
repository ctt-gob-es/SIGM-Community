package es.dipucr.sigem.api.object;

public class Expediente {
	private String numexp = "";
	private String asunto = "";
	private String nombreProcedimientos = "";
	public String getNumexp() {
		return numexp;
	}
	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getNombreProcedimientos() {
		return nombreProcedimientos;
	}
	public void setNombreProcedimientos(String nombreProcedimientos) {
		this.nombreProcedimientos = nombreProcedimientos;
	}
}
