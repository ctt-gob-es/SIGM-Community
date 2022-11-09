package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Analiza un documento XML para obtener una solicitud de visto bueno de peticiones.
 * @author Carlos Gamuci
 */
public class ApproveRequestParser {

	private static final String APPROVE_REQUEST_NODE = "apprv"; //$NON-NLS-1$
	private static final String REQUESTS_NODE = "reqs"; //$NON-NLS-1$
	private static final String REQUEST_ID_ATTRIBUTE = "id"; //$NON-NLS-1$

	private ApproveRequestParser() {
		// Se evita el uso del constructor
	}

	/** Analiza un documento XML y, en caso de tener el formato correcto, obtiene de &eacute;l
	 * una solicitud de visto bueno de peticiones.
	 * @param doc Documento XML.
	 * @param certB64 Certificado guardado en sesi&oacute;n.
	 * @return Solicitud de visto bueno.
	 * @throws IllegalArgumentException Cuando el XML no tiene el formato esperado.	 */
	static ApproveRequestList parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!APPROVE_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					APPROVE_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}

		final NodeList nodes = doc.getDocumentElement().getChildNodes();
		final int nodeIndex = XmlUtils.nextNodeElementIndex(nodes, 0);
		if (nodeIndex == -1) {
			throw new IllegalArgumentException(
					"No se ha indicado el certificado necesario para la autenticacion en el nodo " + //$NON-NLS-1$
							REQUESTS_NODE);
		}
		Element currentNode = (Element) nodes.item(nodeIndex);
		currentNode = (Element) nodes.item(nodeIndex);
		if (!REQUESTS_NODE.equalsIgnoreCase(currentNode.getNodeName())) {
			throw new IllegalArgumentException(
					"No se ha encontrado el nodo " + REQUESTS_NODE + //$NON-NLS-1$
					" en su lugar se encontro " + currentNode.getNodeName()); //$NON-NLS-1$
		}

		final ApproveRequestList appRequest = new ApproveRequestList();
		final NodeList idsNodeList = currentNode.getChildNodes();
		for (int i = 0; i < idsNodeList.getLength(); i++) {
			i = XmlUtils.nextNodeElementIndex(idsNodeList, i);
			if (i == -1) {
				break;
			}

			final String id = ((Element) idsNodeList.item(i)).getAttribute(REQUEST_ID_ATTRIBUTE);
			if (id == null || id.length() == 0) {
				throw new IllegalArgumentException(
						"No se encontro el identificador de una peticion para su visto bueno"); //$NON-NLS-1$
			}
			appRequest.add(new ApproveRequest(id));
		}

		return appRequest;
	}
}
