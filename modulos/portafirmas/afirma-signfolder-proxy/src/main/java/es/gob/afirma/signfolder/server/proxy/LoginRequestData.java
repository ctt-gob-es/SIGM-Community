package es.gob.afirma.signfolder.server.proxy;

/**
 * Datos necesarios para el inicio de sesi&oacute;n.
 * @author Carlos Gamuci
 */
public class LoginRequestData {

	private final String id;

	private byte[] data = null;

	private byte[] presign = null;

	LoginRequestData(String id) {
		this.id = id;
	}

	void setData(byte[] data) {
		this.data = data;
	}

	void setPresign(byte[] presign) {
		this.presign = presign;
	}

	String getId() {
		return this.id;
	}

	byte[] getData() {
		return this.data;
	}

	byte[] getPresign() {
		return this.presign;
	}
}
