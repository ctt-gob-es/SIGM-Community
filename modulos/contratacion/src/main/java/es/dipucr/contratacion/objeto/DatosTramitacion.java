package es.dipucr.contratacion.objeto;

import java.util.Calendar;

import es.dipucr.contratacion.client.beans.DuracionContratoBean;
import es.dipucr.contratacion.client.beans.FormalizacionBean;
import es.dipucr.contratacion.client.beans.LicitadorBean;
import es.dipucr.contratacion.client.beans.OfertasRecibidas;
import es.dipucr.contratacion.client.beans.Periodo;

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
	
	/**FORMALIZACION**/
	private FormalizacionBean formalizacion;
	
	/**Texto Acuerdo**/
	private String textoAcuerdo = "";
	
	private OfertasRecibidas ofertasRecibidas;
	
	private DuracionContratoBean duracionContrato;
	
	private boolean prorroga = false;
	private int tmpProrroga = 0;
	
	private String estadoExpediente = null;



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
}
