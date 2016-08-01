package es.dipucr.bdns.objetos;

import java.util.Date;



public class EntidadConvocatoria {
	private String idConvocatoria=null;
	private String DescripcionCov=null;
	private String nomenclatura=null;
	private String diarioOficialBR=null;
	private String descripcionBR=null;
	private String uRLEspBR=null;
	//IDENTIFICACIÓN ASOCIADA A LAS SOLICITUDES
	private String abierto=null;
	private String inicioSolicitud=null;
	private Date fechaInicioSolicitud=null;
	public Date getFechaInicioSolicitud() {
		return fechaInicioSolicitud;
	}
	public void setFechaInicioSolicitud(Date fechaInicioSolicitud) {
		this.fechaInicioSolicitud = fechaInicioSolicitud;
	}
	public Date getFechaFinSolicitud() {
		return fechaFinSolicitud;
	}
	public void setFechaFinSolicitud(Date fechaFinSolicitud) {
		this.fechaFinSolicitud = fechaFinSolicitud;
	}
	public Date getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(Date fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public Date getFechaFirma() {
		return fechaFirma;
	}
	public void setFechaFirma(Date fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
	private String finSolicitud=null;
	private Date fechaFinSolicitud=null;
	private String sede=null;
	private String justificacion=null;
	private Date fechaJustificacion=null;
	//INFORMACION DE UN TIPO DE FINANCIACION
	private Tipo [] financiacion=null;
	//INFORMACION DE LOS FONDOS UE CONFINANCIADORES DE LA CONVOCATORIA
	private Tipo [] fondo=null;
	//OTROS DATOS DE LA CONVOCATORIA
	//INFORMACION DE LAS ACTIVIDADES ECONOMICAS
	private String[] sector=null;
	//IDENTIFICACIÓN DE LAS REGIONES GEOGRAFICAS
	private String[] region=null;
	//INFORMACION PARA AYUDAS DE TIPO ADE
	private String autorizacionADE=null;
	private String referenciaUE=null;
	private String reglamento=null;
	//INFORMACION DE OBJETIVOS DEL REGLAMENTO DE EXENCION POR CATEGORIA DE AYUDA
	private String[] objetivo=null;
	//INSTRUMENTOS DE AYUDA
	private String[] instrumento=null;
	private String[] tipoBeneficiario=null;
	private String finalidad=null;
	private String impactoGenero=null;
	private String concesionPublicable=null;
	private String subvencionNominativa=null;
	//INFORMACION DEL EXTRACTO DE LA CONVOCATORIA
	private String diarioOficial=null;
	private String tituloExtracto=null;
	private String[] textoExtracto=null;
	private Date fechaFirma=null;
	private String lugarFirma=null;
	private String firmante=null;
	
	public String[] getTextoExtracto() {
		return textoExtracto;
	}
	public void setTextoExtracto(String[] textoExtracto) {
		this.textoExtracto = textoExtracto;
	}


	public String getLugarFirma() {
		return lugarFirma;
	}
	public void setLugarFirma(String lugarFirma) {
		this.lugarFirma = lugarFirma;
	}
	public String getFirmante() {
		return firmante;
	}
	public void setFirmante(String firmante) {
		this.firmante = firmante;
	}
	public String getIdConvocatoria() {
		return idConvocatoria;
	}
	public void setIdConvocatoria(String idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}
	public String getDescripcionCov() {
		return DescripcionCov;
	}
	public void setDescripcionCov(String descripcionCov) {
		DescripcionCov = descripcionCov;
	}
	public String getNomenclatura() {
		return nomenclatura;
	}
	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}
	public String getDiarioOficialBR() {
		return diarioOficialBR;
	}
	public void setDiarioOficialBR(String diarioOficialBR) {
		this.diarioOficialBR = diarioOficialBR;
	}
	public String getDescripcionBR() {
		return descripcionBR;
	}
	public void setDescripcionBR(String descripcionBR) {
		this.descripcionBR = descripcionBR;
	}
	public String getuRLEspBR() {
		return uRLEspBR;
	}
	public void setuRLEspBR(String uRLEspBR) {
		this.uRLEspBR = uRLEspBR;
	}
	public String getAbierto() {
		return abierto;
	}
	public void setAbierto(String abierto) {
		this.abierto = abierto;
	}
	public String getInicioSolicitud() {
		return inicioSolicitud;
	}
	public void setInicioSolicitud(String inicioSolicitud) {
		this.inicioSolicitud = inicioSolicitud;
	}
	public String getFinSolicitud() {
		return finSolicitud;
	}
	public void setFinSolicitud(String finSolicitud) {
		this.finSolicitud = finSolicitud;
	}
	public String getSede() {
		return sede;
	}
	public void setSede(String sede) {
		this.sede = sede;
	}
	public String getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}
	public Tipo[] getFinanciacion() {
		return financiacion;
	}
	public void setFinanciacion(Tipo[] financiacion) {
		this.financiacion = financiacion;
	}
	public Tipo[] getFondo() {
		return fondo;
	}
	public void setFondo(Tipo[] fondo) {
		this.fondo = fondo;
	}
	public String[] getSector() {
		return sector;
	}
	public void setSector(String[] sector) {
		this.sector = sector;
	}
	public String[] getRegion() {
		return region;
	}
	public void setRegion(String[] region) {
		this.region = region;
	}
	public String getAutorizacionADE() {
		return autorizacionADE;
	}
	public void setAutorizacionADE(String autorizacionADE) {
		this.autorizacionADE = autorizacionADE;
	}
	public String getReferenciaUE() {
		return referenciaUE;
	}
	public void setReferenciaUE(String referenciaUE) {
		this.referenciaUE = referenciaUE;
	}
	public String getReglamento() {
		return reglamento;
	}
	public void setReglamento(String reglamento) {
		this.reglamento = reglamento;
	}
	public String[] getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String[] objetivo) {
		this.objetivo = objetivo;
	}
	public String[] getInstrumento() {
		return instrumento;
	}
	public void setInstrumento(String[] instrumento) {
		this.instrumento = instrumento;
	}
	public String[] getTipoBeneficiario() {
		return tipoBeneficiario;
	}
	public void setTipoBeneficiario(String[] tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}
	public String getFinalidad() {
		return finalidad;
	}
	public void setFinalidad(String finalidad) {
		this.finalidad = finalidad;
	}
	public String getImpactoGenero() {
		return impactoGenero;
	}
	public void setImpactoGenero(String impactoGenero) {
		this.impactoGenero = impactoGenero;
	}
	public String getConcesionPublicable() {
		return concesionPublicable;
	}
	public void setConcesionPublicable(String concesionPublicable) {
		this.concesionPublicable = concesionPublicable;
	}
	public String getSubvencionNominativa() {
		return subvencionNominativa;
	}
	public void setSubvencionNominativa(String subvencionNominativa) {
		this.subvencionNominativa = subvencionNominativa;
	}
	public String getDiarioOficial() {
		return diarioOficial;
	}
	public void setDiarioOficial(String diarioOficial) {
		this.diarioOficial = diarioOficial;
	}
	public String getTituloExtracto() {
		return tituloExtracto;
	}
	public void setTituloExtracto(String tituloExtracto) {
		this.tituloExtracto = tituloExtracto;
	}

}
