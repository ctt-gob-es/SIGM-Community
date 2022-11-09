package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;

/**
 * Analiza un documento XML para obtener una solicitud de token de autenticaci&oacute;n para
 * el inicio de sesi&oacute;n.
 * @author Carlos Gamuci
 */
public class LoginClaveRequestParser {

	private static final String LOGIN_REQUEST_NODE = "lgnrq"; //$NON-NLS-1$

	static void parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!LOGIN_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					LOGIN_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}
	}

}
