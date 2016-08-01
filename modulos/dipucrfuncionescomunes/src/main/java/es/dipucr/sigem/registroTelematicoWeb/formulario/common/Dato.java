package es.dipucr.sigem.registroTelematicoWeb.formulario.common;

import java.util.ArrayList;
import java.util.List;

public class Dato {
	
	private String valor = "";
	private String sustituto = "";
	private List<String> listaResultados = new ArrayList<String>(); 
	
	public Dato(String valor, String sustituto){
		this.valor = valor;
		this.sustituto = sustituto;
		this.setListaResultados(new ArrayList<String>());
	}
	
	public Dato(String valor, String sustituto, List<String> listaResultados){
		this.valor = valor;
		this.sustituto = sustituto;
		this.setListaResultados(listaResultados);
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

	public List<String> getListaResultados() {
		return listaResultados;
	}

	public void setListaResultados(List<String> listaResultados) {
		this.listaResultados = listaResultados;
	}
	
}
