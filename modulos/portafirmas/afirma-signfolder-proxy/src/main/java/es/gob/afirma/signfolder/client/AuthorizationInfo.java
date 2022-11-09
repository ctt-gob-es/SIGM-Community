package es.gob.afirma.signfolder.client;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa el conjunto de parámetros de una autorización necesarios para su creación.
 */
public class AuthorizationInfo implements Serializable {

	/**
	 * Número de serie UID.
	 */
	private static final long serialVersionUID = -8844602577903384631L;
	
	/**
	 * Atributo que representa la fecha de inicio de la autorización.
	 */
	private Date initDate;
	
	/**
	 * Atributo que representa la fecha de renovación de la autorización.
	 */
	private Date endDate;
	
	/**
	 * Atributo que representa el tipo de autorización.
	 */
	private String type;
	
	/**
	 * Atributo que representa las observaciones añadidas a la autorización.
	 */
	private String observations;

	/**
	 * @return the initDate
	 */
	public Date getInitDate() {
		return initDate;
	}

	/**
	 * @param initDate the initDate to set
	 */
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the observations
	 */
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations the observations to set
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
