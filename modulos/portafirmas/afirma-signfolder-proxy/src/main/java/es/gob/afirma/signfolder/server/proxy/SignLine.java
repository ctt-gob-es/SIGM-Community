package es.gob.afirma.signfolder.server.proxy;

/**
 * L&iacute;nea de firma. Contiene el listado de firmantes de la linea, el tipo
 * de operaci&oacute;n que deben llevar a cabo los "firmantes" (firma o visto bueno)
 * y si est&aacute; procesada o no.
 */
public class SignLine {

	/**
	 * Define los tipos de l&iacute;neas de firma existentes.
	 */
	enum SignLineType {
		/** L&iacute;nea de visto bueno. */
		VISTOBUENO,
		/** L&iacute;nea de firma. */
		FIRMA;
	}

	private final String[] signers;
	private SignLineType type = SignLineType.FIRMA;

	/**
	 * Construye la informaci&oacute;n b&aacute;sica de la l&iacute;nea de firma.
	 * @param signers Listado de firmantes.
	 */
	public SignLine(String[] signers) {
		this.signers = signers;
	}

	/**
	 * Obtiene el listado de firmantes de la l&iacute;nea de firma.
	 * @return Listado de firmantes.
	 */
	public String[] getSigners() {
		return this.signers;
	}

	/**
	 * Obtiene el tipo de l&iacute;nea de firma.
	 * @return Tipo de l&iacute;nea de firma.
	 */
	public SignLineType getType() {
		return this.type;
	}

	/**
	 * Establece el tipo de l&iacute;nea de firma.
	 * @param type Tipo de l&iacute;nea de firma.
	 */
	public void setType(SignLineType type) {
		this.type = type;
	}
}
