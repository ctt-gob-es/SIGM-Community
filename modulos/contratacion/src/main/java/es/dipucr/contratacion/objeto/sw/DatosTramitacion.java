package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;

public class DatosTramitacion {
	private Calendar fechaAperturaProposiones = null;
	private Calendar fechaAprobacionProyecto = null;
	private Calendar fechaAprobacionExpedienteContratacion = null;
	private Calendar fechaBOPExpCont = null;
	private Calendar fechaBOPFormalizacion = null;	
	private Periodo presentacionOfertas = null;
	/**ADJUDICACION**/
	private LicitadorBean[] licitador;
	private String invitacioneLicitar;	
	private Boolean adjudicatarioPYME = null;
	private DiariosOficiales diariosOficiales = null;
	/**FORMALIZACION**/
	private FormalizacionBean formalizacion;	
	/**Texto Acuerdo**/
	private String textoAcuerdo = "";	
	private OfertasRecibidas ofertasRecibidas;
	
	private DuracionContratoBean duracionContrato;
	
	private boolean prorroga = false;
	private int tmpProrroga = 0;
	
	private String estadoExpediente = null;
	
	/** * codigoAdjudicacion
	 * RUTA: http://contrataciondelestado.es/codice/cl/2.02/TenderResultCode-2.02.gc
	 * 1 -> Adjudicado Provisionalmente
	 * 2 -> Adjudicado Definitivamente
	 * 3 -> Desierto
	 * 4 -> Desistimiento
	 * 5 -> Renuncia
	 * 6 -> Desierto Provisionalmente
	 * 7 -> Desierto Definitivamente
	 * 8 -> Adjudicado
	 * 9 -> Formalizado
	 * 10 -> Licitador mejor valorado:Requerimiento de documentacion
	 * **/
	private Campo codigoAdjudicacion;
	

	public LicitadorBean[] getLicitador() {
		return licitador;
	}

	public void setLicitador(LicitadorBean[] licitador) {
		this.licitador = licitador;
	}

	public FormalizacionBean getFormalizacion() {
		return formalizacion;
	}

	public void setFormalizacion(FormalizacionBean formalizacion) {
		this.formalizacion = formalizacion;
	}

	public String getTextoAcuerdo() {
		return textoAcuerdo;
	}

	public void setTextoAcuerdo(String textoAcuerdo) {
		this.textoAcuerdo = textoAcuerdo;
	}

	public OfertasRecibidas getOfertasRecibidas() {
		return ofertasRecibidas;
	}

	public void setOfertasRecibidas(OfertasRecibidas ofertasRecibidas) {
		this.ofertasRecibidas = ofertasRecibidas;
	}

	public Calendar getFechaAprobacionProyecto() {
		return fechaAprobacionProyecto;
	}

	public void setFechaAprobacionProyecto(Calendar fechaAprobacionProyecto) {
		this.fechaAprobacionProyecto = fechaAprobacionProyecto;
	}

	public Calendar getFechaAprobacionExpedienteContratacion() {
		return fechaAprobacionExpedienteContratacion;
	}

	public void setFechaAprobacionExpedienteContratacion(
			Calendar fechaAprobacionExpedienteContratacion) {
		this.fechaAprobacionExpedienteContratacion = fechaAprobacionExpedienteContratacion;
	}

	public Calendar getFechaBOPExpCont() {
		return fechaBOPExpCont;
	}

	public void setFechaBOPExpCont(Calendar fechaBOPExpCont) {
		this.fechaBOPExpCont = fechaBOPExpCont;
	}

	public Calendar getFechaAperturaProposiones() {
		return fechaAperturaProposiones;
	}

	public void setFechaAperturaProposiones(Calendar fechaAperturaProposiones) {
		this.fechaAperturaProposiones = fechaAperturaProposiones;
	}

	public Periodo getPresentacionOfertas() {
		return presentacionOfertas;
	}

	public void setPresentacionOfertas(Periodo presentacionOfertas) {
		this.presentacionOfertas = presentacionOfertas;
	}

	public DuracionContratoBean getDuracionContrato() {
		return duracionContrato;
	}

	public void setDuracionContrato(DuracionContratoBean duracionContrato) {
		this.duracionContrato = duracionContrato;
	}

	public boolean isProrroga() {
		return prorroga;
	}

	public void setProrroga(boolean prorroga) {
		this.prorroga = prorroga;
	}

	public int getTmpProrroga() {
		return tmpProrroga;
	}

	public void setTmpProrroga(int tmpProrroga) {
		this.tmpProrroga = tmpProrroga;
	}

	public Calendar getFechaBOPFormalizacion() {
		return fechaBOPFormalizacion;
	}

	public void setFechaBOPFormalizacion(Calendar fechaBOPFormalizacion) {
		this.fechaBOPFormalizacion = fechaBOPFormalizacion;
	}

	public String getInvitacioneLicitar() {
		return invitacioneLicitar;
	}

	public void setInvitacioneLicitar(String invitacioneLicitar) {
		this.invitacioneLicitar = invitacioneLicitar;
	}

	public String getEstadoExpediente() {
		return estadoExpediente;
	}

	public void setEstadoExpediente(String estadoExpediente) {
		this.estadoExpediente = estadoExpediente;
	}

	public Boolean getAdjudicatarioPYME() {
		return adjudicatarioPYME;
	}

	public void setAdjudicatarioPYME(Boolean adjudicatarioPYME) {
		this.adjudicatarioPYME = adjudicatarioPYME;
	}

	public Campo getCodigoAdjudicacion() {
		return codigoAdjudicacion;
	}

	public void setCodigoAdjudicacion(Campo codigoAdjudicacion) {
		this.codigoAdjudicacion = codigoAdjudicacion;
	}

	public DiariosOficiales getDiariosOficiales() {
		return diariosOficiales;
	}

	public void setDiariosOficiales(DiariosOficiales diariosOficiales) {
		this.diariosOficiales = diariosOficiales;
	}
}
