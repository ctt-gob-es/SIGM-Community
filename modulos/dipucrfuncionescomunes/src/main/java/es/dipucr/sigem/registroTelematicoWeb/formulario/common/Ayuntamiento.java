package es.dipucr.sigem.registroTelematicoWeb.formulario.common;

public class Ayuntamiento {
	private String valor = "";
	private String sustituto = "";
	public Ayuntamiento(String valor, String sustituto){
		this.valor = valor;
		this.sustituto = sustituto;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getSustituto() {
		return sustituto;
	}
	public void setSustituto(String sustituto) {
		this.sustituto = sustituto;
	}
	
}
