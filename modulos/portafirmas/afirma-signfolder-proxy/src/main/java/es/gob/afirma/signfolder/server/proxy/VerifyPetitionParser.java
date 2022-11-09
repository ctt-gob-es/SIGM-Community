package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Clase que analiza la estructura del servicio de validación de peticiones.
 */
public class VerifyPetitionParser {
	
	/**
	 * Constante que define el nombre del elemento principal de la petición.
	 */
	private static final String REQUEST_NODE = "verfreq";
	
	/**
	 * Método que parsea la petición recibida como objeto Document y comprueba si tiene una estructura válida.
	 * @param doc Documento que representa la petición recibida.
	 */
	static void parse(final Document doc) {
		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");
		}

		final Element xmlNode = doc.getDocumentElement();

		if (!REQUEST_NODE.equalsIgnoreCase(xmlNode.getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + REQUEST_NODE + "' y aparece: "
					+ doc.getDocumentElement().getNodeName());
		}
	}


}
