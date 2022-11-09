package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;

/**
 * Analiza un documento XML para obtener una petici&oacute;n de previsualizaci&oacute;n
 * de documento.
 * @author Carlos Gamuci
 */
public class ConfigurationRequestParser {

	private static final String CONFIGURATION_REQUEST_NODE = "rqtconf"; //$NON-NLS-1$

	private ConfigurationRequestParser() {
		// Se evita el uso del constructor
	}

	/** Analiza un documento XML y, en caso de tener el formato correcto, obtiene de &eacute;l
	 * un identificador de documento.
	 * @param doc Documento XML.
	 * @return Identificador de documento.
	 * @throws IllegalArgumentException Cuando el XML no tiene el formato esperado.	 */
	static ConfigurationRequest parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!CONFIGURATION_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					CONFIGURATION_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}
		return new ConfigurationRequest();
	}
}
