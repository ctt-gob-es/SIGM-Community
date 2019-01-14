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
 * Datos de contacto de una oficina.
 * 
 * @author cmorenog
 * 
 */
public class ContactUOVO extends Entity {

    private static final long serialVersionUID = 4502717489198772621L;

    /**
     * Id tipo de contacto.
     */
    private String contactTypeId;

    /**
     * Datos de contacto.
     */
    private String contactInfo;

    /**
     * Observaciones de contacto.
     */
    private String observations;

    /**
     * Id de estado de contacto.
     */
    private String statusId;

    /**
     * Código de la oficina.
     */
    private String unitId;

    /**
     * Visibilidad del contacto.
     */
    private Integer visibility;

    /**
     * Obtiene el valor del parámetro contactTypeId.
     * 
     * @return contactTypeId valor del campo a obtener.
     */
    public String getContactTypeId() {
	return contactTypeId;
    }

    /**
     * Guarda el valor del parámetro contactTypeId.
     * 
     * @param contactTypeId
     *            valor del campo a guardar.
     */
    public void setContactTypeId(
	String contactTypeId) {
	this.contactTypeId = contactTypeId;
    }

    /**
     * Obtiene el valor del parámetro contactInfo.
     * 
     * @return contactInfo valor del campo a obtener.
     */
    public String getContactInfo() {
	return contactInfo;
    }

    /**
     * Guarda el valor del parámetro contactInfo.
     * 
     * @param contactInfo
     *            valor del campo a guardar.
     */
    public void setContactInfo(
	String contactInfo) {
	this.contactInfo = contactInfo;
    }

    /**
     * Obtiene el valor del parámetro observations.
     * 
     * @return observations valor del campo a obtener.
     */
    public String getObservations() {
	return observations;
    }

    /**
     * Guarda el valor del parámetro observations.
     * 
     * @param observations
     *            valor del campo a guardar.
     */
    public void setObservations(
	String observations) {
	this.observations = observations;
    }

    /**
     * Obtiene el valor del parámetro statusId.
     * 
     * @return statusId valor del campo a obtener.
     */
    public String getStatusId() {
	return statusId;
    }

    /**
     * Guarda el valor del parámetro statusId.
     * 
     * @param statusId
     *            valor del campo a guardar.
     */
    public void setStatusId(
	String statusId) {
	this.statusId = statusId;
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
    public void setUnitId(
	String unitId) {
	this.unitId = unitId;
    }

    /**
     * Obtiene el valor del parámetro visibility.
     * 
     * @return visibility valor del campo a obtener.
     */
    public Integer getVisibility() {
	return visibility;
    }

    /**
     * Guarda el valor del parámetro visibility.
     * 
     * @param visibility
     *            valor del campo a guardar.
     */
    public void setVisibility(
	Integer visibility) {
	this.visibility = visibility;
    }

}
