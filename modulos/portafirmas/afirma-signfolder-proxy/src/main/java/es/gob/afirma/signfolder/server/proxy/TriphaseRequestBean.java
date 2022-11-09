package es.gob.afirma.signfolder.server.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/** Petici&oacute;n de firma trif&aacute;sica para m&uacute;ltiples documentos.
 * @author Carlos Gamuci Mill&aacute;n */
public final class TriphaseRequestBean extends ArrayList<TriphaseRequest> {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** Certificado de firma. */
	private final X509Certificate cert;

	/** Identificador asignado a la transacci&oacute;n. */
	private String trId = null;

	/** Construye el listado de peticiones de firma trif&aacute;sica.
	 * @param certEncoded Certificado de firma codificado en Base64.
	 * @param triphaseRequests Listado de firmas solicitadas.
	 * @throws IOException Cuando el certificado no se indica correctamente en Base 64.
	 * @throws CertificateException Cuando no se indica un certificado valido
	 */
	TriphaseRequestBean(final byte[] certEncoded,
			            final List<TriphaseRequest> triphaseRequests) throws CertificateException,
	                                                                     IOException {

		if (certEncoded != null) {
			this.cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate( //$NON-NLS-1$
					new ByteArrayInputStream(certEncoded));
		}
		else {
			this.cert = null;
		}

		if (triphaseRequests != null) {
			this.addAll(triphaseRequests);
		}
	}

	/** Construye el listado de peticiones de firma trif&aacute;sica.
	 * @param cert Certificado.
	 * @param triphaseRequests Listado de firmas solicitadas.
	 */
	TriphaseRequestBean(final X509Certificate cert, final List<TriphaseRequest> triphaseRequests) {
		this.cert = cert;
		if (triphaseRequests != null) {
			this.addAll(triphaseRequests);
		}
	}

	/** Recupera el certificado de firma.
	 * @return Certificado de firma. */
	public X509Certificate getCertificate() {
		return this.cert;
	}

	/**
	 * Obtiene el identificador asociado a la transacci&oacute;n.
	 * @return Identificador de transacci&oacute;n o {@code null} si no est&aacute; definido.
	 */
	public String getTrId() {
		return this.trId;
	}

	/**
	 * Establece el identificador de transacci&oacute;n.
	 * @param trId Identificador de transacci&oacute;n.
	 */
	public void setTrId(final String trId) {
		this.trId = trId;
	}
}
