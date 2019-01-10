package ieci.tecdoc.sgm.ct.database.datatypes;

import java.util.Date;



public class Acta {
	
	private String numexp = "";

	private String extracto = "";
	
	private FicheroPropuesta documentoActa = null;
	
	private Date fechafirma = null;



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

	public Date getFechafirma() {
		return fechafirma;
	}

	public void setFechafirma(Date fechafirma) {
		this.fechafirma = fechafirma;
	}

}
