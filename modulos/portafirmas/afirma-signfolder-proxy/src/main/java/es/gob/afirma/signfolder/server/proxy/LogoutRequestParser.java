package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;

/**
 * Analiza un documento XML para detectar si se trata de una petici&oacute;n de cierre de
 * sesi&oacute;n v&aacute;lida.
 * @author Carlos Gamuci
 */
public class LogoutRequestParser {

	private static final String LOGOUT_REQUEST_NODE = "lgorq"; //$NON-NLS-1$

	static void parse(Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!LOGOUT_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					LOGOUT_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}
	}

}
