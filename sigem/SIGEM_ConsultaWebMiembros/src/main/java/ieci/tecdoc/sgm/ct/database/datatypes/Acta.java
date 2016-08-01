package ieci.tecdoc.sgm.ct.database.datatypes;


public class Acta {
	
	private String numexp = "";

	private String extracto = "";
	
	private FicheroPropuesta documentoActa = null;



	public String getExtracto() {
		return extracto;
	}

	public void setExtracto(String extracto) {
		this.extracto = extracto;
	}

	public String getNumexp() {
		return numexp;
	}

	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}

	public FicheroPropuesta getDocumentoActa() {
		return documentoActa;
	}

	public void setDocumentoActa(FicheroPropuesta documentoActa) {
		this.documentoActa = documentoActa;
	}

}
