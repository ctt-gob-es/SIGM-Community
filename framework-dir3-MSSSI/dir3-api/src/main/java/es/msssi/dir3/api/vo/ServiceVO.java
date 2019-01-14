/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.dir3.api.vo;

import es.msssi.dir3.core.vo.Entity;

/**
 * Datos básicos de los servicios de una oficina.
 * 
 * @author cmorenog
 * 
 */
public class ServiceVO extends Entity {

	/**
     * 
     */
	private static final long serialVersionUID = 3022391899522441647L;
	/**
	 * Descripción del servicio.
	 */
	private String description;
	/**
	 * Código de la oficina del servicio.
	 */
	private String officeId;

	/**
	 * Obtiene el valor del parámetro description.
	 * 
	 * @return description valor del campo a obtener.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Guarda el valor del parámetro description.
	 * 
	 * @param description
	 *            valor del campo a guardar.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Obtiene el valor del parámetro officeId.
	 * 
	 * @return officeId valor del campo a obtener.
	 */
	public String getOfficeId() {
		return officeId;
	}

	/**
	 * Guarda el valor del parámetro officeId.
	 * 
	 * @param officeId
	 *            valor del campo a guardar.
	 */
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

}
