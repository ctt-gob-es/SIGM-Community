package es.gob.afirma.signfolder.server.proxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Solicitud para el rechazo de peticiones.
 */
public final class RejectRequest extends ArrayList<String> {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	private final String rejectReason;

	/** Usamos un espacio como valor por defecto igual que el portafirmas web. */
	private static final String DEFAULT_REASON = "Rechazado sin indicar motivo, contacte con el firmante"; //$NON-NLS-1$

	/**
	 * Crea la solicitud de rechazo de una lista de peticiones de firma.
	 * @param ids Identificadores de las peticiones a rechazar.
	 * @param rejectReason Motivo del rechazo. Si no se indica, se establece el valor por defecto.
	 */
	public RejectRequest(final List<String> ids, final String rejectReason) {
		this.addAll(ids);
		// El servicio no permite valores nulos ni vacios como motivo de rechazo
		this.rejectReason = rejectReason != null && rejectReason.length() != 0 ? rejectReason : DEFAULT_REASON;
	}

	/**
	 * Recupera el motivo de rechazo de la petici&oacute;n.
	 * @return Motivo del rechazo.
	 */
	public String getRejectReason() {
		return this.rejectReason;
	}
}
