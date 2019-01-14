package es.dipucr.sigem.api.rule.common.utils;

public class FirmaLotesError {

	private String titulo;
	private String texto;
	
	public FirmaLotesError(String titulo, String texto) {
		super();
		this.titulo = titulo;
		this.texto = texto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
}
