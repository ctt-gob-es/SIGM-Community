package ieci.tecdoc.sgm.core.services.consulta;

import java.util.Vector;


public class Propuesta {
	
	String numexp = "";

	String extracto = "";
	
	Vector documentosPropuestas = new Vector();
	
	public String getNumexp() {
		return numexp;
	}

	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}


	public String getExtracto() {
		return extracto;
	}

	public void setExtracto(String extracto) {
		this.extracto = extracto;
	}

	public Vector getDocumentosPropuestas() {
		return documentosPropuestas;
	}

	public void setDocumentosPropuestas(Vector documentosPropuestas) {
		this.documentosPropuestas = documentosPropuestas;
	}



	
}
