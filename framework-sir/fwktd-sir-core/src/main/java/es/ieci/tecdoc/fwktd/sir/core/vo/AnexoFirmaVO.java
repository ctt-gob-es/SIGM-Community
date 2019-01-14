/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.ieci.tecdoc.fwktd.sir.core.vo;

import es.ieci.tecdoc.fwktd.core.model.Entity;

/**
 * Información de un anexo de un asiento registral con su firma.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class AnexoFirmaVO extends Entity {

	private static final long serialVersionUID = -8849901644287847321L;

	/**
	 * Anexo original.
	 */
	private AnexoVO original = null;

	/**
	 * Anexo firma.
	 */
	private AnexoVO firma = null;


	/**
	 * Constructor.
	 */
	public AnexoFirmaVO() {
		super();
	}


	public AnexoVO getOriginal() {
	    return original;
	}


	public void setOriginal(AnexoVO original) {
	    this.original = original;
	}


	public AnexoVO getFirma() {
	    return firma;
	}


	public void setFirma(AnexoVO firma) {
	    this.firma = firma;
	}

}
