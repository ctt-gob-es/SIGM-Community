package com.tsol.modulos.buscador.beans;


public class SearchBean 
{
	/* Código de cotejo.  */
	private String codCotejo;
	
	/* Nombre del documento.  */
	private String nombre;
	
	/* Número del expediente.  */
	private String numExp;
	
	/* Fecha de alta del documento.  */
	private String fechaDoc;
	
	/* Tipo de registro.  */
	private String tpReg;

	/* Identificador del documento.  */
	private String id;

	/* Infopag del documento.  */
	private String infopag;
	
	/* [eCenpri-Felipe #625] Número de registro.  */
	private String nreg;
	
	/* [eCenpri-Felipe #625] Fecha de registro.  */
	private String freg;
	
	/* [eCenpri-Felipe #625] Origen.  */
	private String origen;
	
	/* [eCenpri-Felipe #625] Destino.  */
	private String destino;
	
	/* [eCenpri-Felipe #828] Estado del documento.  */
	private String estado;
	
	
	public SearchBean() {
		super();
		
		codCotejo = null;
		nombre = null;
		numExp = null;
		fechaDoc = null;
		tpReg = null;
		//INICIO [eCenpri-Felipe #625]
		nreg = null;
		freg = null;
		origen = null;
		destino = null;
		//FIN [eCenpri-Felipe #625]
		estado = null; //[eCenpri-Felipe #828]
	}

	public String getCodCotejo() {
		return codCotejo;
	}

	public void setCodCotejo(String codCotejo) {
		this.codCotejo = codCotejo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumExp() {
		return numExp;
	}

	public void setNumExp(String numExp) {
		this.numExp = numExp;
	}

	public String getFechaDoc() {
		return fechaDoc;
	}

	public void setFechaDoc(String fechaDoc) {
		this.fechaDoc = fechaDoc;
	}

	public String getTpReg() {
		return tpReg;
	}

	public void setTpReg(String tpReg) {
		this.tpReg = tpReg;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfopag() {
		return infopag;
	}

	public void setInfopag(String infopag) {
		this.infopag = infopag;
	}

	public String getNreg() {
		return nreg;
	}

	public void setNreg(String nreg) {
		this.nreg = nreg;
	}

	public String getFreg() {
		return freg;
	}

	public void setFreg(String freg) {
		this.freg = freg;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
