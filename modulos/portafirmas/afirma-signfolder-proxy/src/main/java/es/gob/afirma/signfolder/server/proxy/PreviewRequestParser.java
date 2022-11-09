package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;

/**
 * Analiza un documento XML para obtener una petici&oacute;n de previsualizaci&oacute;n
 * de documento.
 * @author Carlos Gamuci
 */
public class PreviewRequestParser {

	private static final String PREVIEW_REQUEST_NODE = "rqtprw"; //$NON-NLS-1$
	private static final String DOCUMENT_ID_ATTRIBUTE = "docid"; //$NON-NLS-1$

	private PreviewRequestParser() {
		// Se evita el uso del constructor
	}

	/** Analiza un documento XML y, en caso de tener el formato correcto, obtiene de &eacute;l
	 * un identificador de documento.
	 * @param doc Documento XML.
	 * @return Identificador de documento.
	 * @throws IllegalArgumentException Cuando el XML no tiene el formato esperado.	 */
	static PreviewRequest parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!PREVIEW_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					PREVIEW_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}

		// Recogermos el identificador del documento
		final String docId = doc.getDocumentElement().getAttribute(DOCUMENT_ID_ATTRIBUTE);
		if (docId == null) {
			throw new IllegalArgumentException("No se ha indicado el atributo " +  //$NON-NLS-1$
					DOCUMENT_ID_ATTRIBUTE + " con el identificador del documento a previsualizar"); //$NON-NLS-1$
		}

		return new PreviewRequest(docId);
	}
}
