package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa la respuesta del servicio "createRole".
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createRoleResponse", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
		"result" })
public class CreateRoleResponse {

	/**
	 * Bandera que indica si el resultado de la operación ha sido satisfactoria
	 * o no.
	 */
	@XmlElement(required = true)
	private boolean result;

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

}
