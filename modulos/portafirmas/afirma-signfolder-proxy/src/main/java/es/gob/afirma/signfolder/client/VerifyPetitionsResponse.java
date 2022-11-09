package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa la respuesta del servicio "verifyPetitions".
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyPetitionsResponse", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
		"verifyResult" })
public class VerifyPetitionsResponse {

	/**
	 * Bandera que indica si el resultado de la operación ha sido satisfactoria
	 * o no.
	 */
	@XmlElement(required = true)
	private boolean verifyResult;

	/**
	 * @return the verifyResult
	 */
	public boolean isVerifyResult() {
		return verifyResult;
	}

	/**
	 * @param verifyResult
	 *            the verifyResult to set
	 */
	public void setVerifyResult(boolean verifyResult) {
		this.verifyResult = verifyResult;
	}

}
