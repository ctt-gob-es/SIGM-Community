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
public class HistoryVO extends Entity {

    private static final long serialVersionUID = 4502717489198772621L;

    /**
     * código anterior.
     */
    private String previousId;

    /**
     * nombre anterior.
     */
    private String previousName;

    /**
     * código de la última entidad.
     */
    private String lastId;

    /**
     * nombre de la última entidad.
     */
    private String lastName;

    /**
     * id del motivo.
     */
    private String causeId;

    /**
     * observaciones del motivo de baja.
     */
    private String observations;

    /**
     * estado del elemento anterior.
     */
    private String previousStatus;

    /**
     * estado del elemento último.
     */
    private String lastStatus;

    /**
     * estado de la relación.
     */
    private String status;

    /**
     * fecha extinción.
     */
    private String extinctionDate;

    /**
     * Obtiene el valor del parámetro previousId.
     * 
     * @return previousId valor del campo a obtener.
     */
    public String getPreviousId() {
	return previousId;
    }

    /**
     * Guarda el valor del parámetro previousId.
     * 
     * @param previousId
     *            valor del campo a guardar.
     */
    public void setPreviousId(
	String previousId) {
	this.previousId = previousId;
    }

    /**
     * Obtiene el valor del parámetro previousName.
     * 
     * @return previousName valor del campo a obtener.
     */
    public String getPreviousName() {
	return previousName;
    }

    /**
     * Guarda el valor del parámetro previousName.
     * 
     * @param previousName
     *            valor del campo a guardar.
     */
    public void setPreviousName(
	String previousName) {
	this.previousName = previousName;
    }

    /**
     * Obtiene el valor del parámetro lastId.
     * 
     * @return lastId valor del campo a obtener.
     */
    public String getLastId() {
	return lastId;
    }

    /**
     * Guarda el valor del parámetro lastId.
     * 
     * @param lastId
     *            valor del campo a guardar.
     */
    public void setLastId(
	String lastId) {
	this.lastId = lastId;
    }

    /**
     * Obtiene el valor del parámetro lastName.
     * 
     * @return lastName valor del campo a obtener.
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * Guarda el valor del parámetro lastName.
     * 
     * @param lastName
     *            valor del campo a guardar.
     */
    public void setLastName(
	String lastName) {
	this.lastName = lastName;
    }

    /**
     * Obtiene el valor del parámetro causeId.
     * 
     * @return causeId valor del campo a obtener.
     */
    public String getCauseId() {
	return causeId;
    }

    /**
     * Guarda el valor del parámetro causeId.
     * 
     * @param causeId
     *            valor del campo a guardar.
     */
    public void setCauseId(
	String causeId) {
	this.causeId = causeId;
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
     * Obtiene el valor del parámetro previousStatus.
     * 
     * @return previousStatus valor del campo a obtener.
     */
    public String getPreviousStatus() {
	return previousStatus;
    }

    /**
     * Guarda el valor del parámetro previousStatus.
     * 
     * @param previousStatus
     *            valor del campo a guardar.
     */
    public void setPreviousStatus(
	String previousStatus) {
	this.previousStatus = previousStatus;
    }

    /**
     * Obtiene el valor del parámetro lastStatus.
     * 
     * @return lastStatus valor del campo a obtener.
     */
    public String getLastStatus() {
	return lastStatus;
    }

    /**
     * Guarda el valor del parámetro lastStatus.
     * 
     * @param lastStatus
     *            valor del campo a guardar.
     */
    public void setLastStatus(
	String lastStatus) {
	this.lastStatus = lastStatus;
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
    public void setStatus(
	String status) {
	this.status = status;
    }

    /**
     * Obtiene el valor del parámetro extinctionDate.
     * 
     * @return extinctionDate valor del campo a obtener.
     */
    public String getExtinctionDate() {
	return extinctionDate;
    }

    /**
     * Guarda el valor del parámetro extinctionDate.
     * 
     * @param extinctionDate
     *            valor del campo a guardar.
     */
    public void setExtinctionDate(
	String extinctionDate) {
	this.extinctionDate = extinctionDate;
    }

}
