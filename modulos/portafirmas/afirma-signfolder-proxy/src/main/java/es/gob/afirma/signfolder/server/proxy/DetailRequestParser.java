package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;

/**
 * Analiza un documento XML para obtener un identificador de una solicitud de la que
 * queremos obtener el detalle.
 * @author Carlos Gamuci
 */
public class DetailRequestParser {

	private static final String DETAIL_REQUEST_NODE = "rqtdtl"; //$NON-NLS-1$
	private static final String REQUEST_ID_ATTRIBUTE = "id"; //$NON-NLS-1$

	private DetailRequestParser() {
		// Se evita el uso del constructor
	}

	/** Analiza un documento XML y, en caso de tener el formato correcto, obtiene de &eacute;l
	 * un identificador de solicitud de firma.
	 * @param doc Documento XML.
	 * @return Identificador de solicitud de firma.
	 * @throws IllegalArgumentException Cuando el XML no tiene el formato esperado.	 */
	static DetailRequest parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!DETAIL_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					DETAIL_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}

		// Recogermos el identificador de la solicitud
		final String id = doc.getDocumentElement().getAttribute(REQUEST_ID_ATTRIBUTE);
		if (id == null) {
			throw new IllegalArgumentException("No se ha indicado el atributo " +  //$NON-NLS-1$
					REQUEST_ID_ATTRIBUTE + " con el identificador la solicitud"); //$NON-NLS-1$
		}
		return new DetailRequest(id);
	}
}
