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
 * Datos de relaciones entre oficinas y organismos.
 * 
 * @author cmorenog
 * 
 */
public class RelationshipOFIUOVO extends Entity {

	private static final long serialVersionUID = 4502717489198772621L;

	/**
	 * Código oficina.
	 */
	private String officeId;

	/**
	 * nombre oficina.
	 */
	private String officeName;

	/**
	 * Código unidad.
	 */
	private String unitId;

	/**
	 * nombre unidad.
	 */
	private String unitName;

	/**
	 * Id de estado.
	 */
	private String status;

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

	/**
	 * Obtiene el valor del parámetro officeName.
	 * 
	 * @return officeName valor del campo a obtener.
	 */
	public String getOfficeName() {
		return officeName;
	}

	/**
	 * Guarda el valor del parámetro officeName.
	 * 
	 * @param officeName
	 *            valor del campo a guardar.
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	/**
	 * Obtiene el valor del parámetro unitId.
	 * 
	 * @return unitId valor del campo a obtener.
	 */
	public String getUnitId() {
		return unitId;
	}

	/**
	 * Guarda el valor del parámetro unitId.
	 * 
	 * @param unitId
	 *            valor del campo a guardar.
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	/**
	 * Obtiene el valor del parámetro unitName.
	 * 
	 * @return unitName valor del campo a obtener.
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * Guarda el valor del parámetro unitName.
	 * 
	 * @param unitName
	 *            valor del campo a guardar.
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * Obtiene el valor del parámetro status.
	 * 
	 * @return status valor del campo a obtener.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Guarda el valor del parámetro status.
	 * 
	 * @param status
	 *            valor del campo a guardar.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
