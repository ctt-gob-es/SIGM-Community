package es.gob.afirma.signfolder.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa la petición cliente para el servicio "verifyPetitions".
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "petitions" })
@XmlRootElement(name = "verifyPetitionsRequest")
public class VerifyPetitionsRequest {

	/**
	 * Atributo que representa el identificador de usuario.
	 */
	@XmlElement(required = true)
	private byte[] id;

	/**
	 * Atributo que representa la lista de identificadores de peticiones.
	 */
	@XmlElement(required = true)
	private List<String> petitions;

	/**
	 * @return the id
	 */
	public byte[] getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(byte[] id) {
		this.id = id;
	}

	/**
	 * @return the petitions
	 */
	public List<String> getPetitions() {
		return petitions;
	}

	/**
	 * @param petitions
	 *            the petitions to set
	 */
	public void setPetitions(List<String> petitions) {
		this.petitions = petitions;
	}

}
