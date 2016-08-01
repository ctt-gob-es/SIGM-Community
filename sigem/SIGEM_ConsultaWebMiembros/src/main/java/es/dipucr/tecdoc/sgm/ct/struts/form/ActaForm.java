package es.dipucr.tecdoc.sgm.ct.struts.form;

import ieci.tecdoc.sgm.core.services.consulta.Actas;

import org.apache.struts.action.ActionForm;

/**
 * Clase ActionForm representativa del listado de DetalleExpediente.jsp 
 */

public class ActaForm extends ActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Actas actas;

	public Actas getActas() {
		return actas;
	}

	public void setActas(Actas actas) {
		this.actas = actas;
	}
	
}
