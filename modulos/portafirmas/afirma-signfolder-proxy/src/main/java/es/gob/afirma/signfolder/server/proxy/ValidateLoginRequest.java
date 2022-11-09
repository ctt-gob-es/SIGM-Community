package es.gob.afirma.signfolder.server.proxy;

/**
 * Datos obtenidos de una petici&oacute;n de validaci&oacute;n de acceso.
 * @author Carlos Gamuci
 */
public class ValidateLoginRequest {

	private final byte[] pkcs1;
	private final byte[] certificate;

	/**
	 * Construye los datos de la validaci&oacute;n de acceso.
	 * @param pkcs1 Firma PKCS#1.
	 * @param certificate Certificado con el que se genero la firma.
	 * @param ssid Identificador de sesi&oacute;n compartida.
	 */
	public ValidateLoginRequest(final byte[] pkcs1, final byte[] certificate) {
		this.pkcs1 = pkcs1;
		this.certificate = certificate;
	}

	/**
	 * Recupera la firma de la validaci&oacute;n.
	 * @return Firma de acceso.
	 */
	public byte[] getPkcs1() {
		return this.pkcs1;
	}

	/**
	 * Recupera el certificado de la validaci&oacute;n.
	 * @return Certificado con el que se genero la firma.
	 */
	public byte[] getCertificate() {
		return this.certificate;
	}
}
