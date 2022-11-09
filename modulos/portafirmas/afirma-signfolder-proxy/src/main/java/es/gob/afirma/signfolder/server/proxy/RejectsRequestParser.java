package es.gob.afirma.signfolder.server.proxy;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.afirma.core.misc.Base64;

/**
 * Analiza un documento XML para obtener un listado de solicitudes de firma que se
 * desean rechazar.
 * @author Carlos Gamuci
 */
public class RejectsRequestParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(RejectsRequestParser.class);

	private final static String REJECT_REQUEST_NODE = "reqrjcts"; //$NON-NLS-1$
	private final static String REJECTS_NODE = "rjcts"; //$NON-NLS-1$
	private final static String REJECT_NODE = "rjct"; //$NON-NLS-1$
	private final static String REASON_NODE = "rsn"; //$NON-NLS-1$
	private final static String ID_ATTRIBUTE = "id"; //$NON-NLS-1$

	private final static String DEFAULT_ENCODING = "utf-8"; //$NON-NLS-1$

	private RejectsRequestParser() {
		// No se permite el constructor por defecto
	}

	/** Analiza un documento XML y, en caso de tener el formato correcto, obtiene de &eacute;l
	 * un listado de identificador de solicitud de firma y el certificado para autenticar la petici&oacute;n.
	 * @param doc Documento XML.
	 * @return Petici&oacute;n de rechazo.
	 * @throws IllegalArgumentException Cuando el XML no tiene el formato esperado.	 */
	static RejectRequest parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!REJECT_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					REJECT_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}

		// Establecemos el certificado para la autenticacion

		final NodeList rejectRequestNodes = doc.getDocumentElement().getChildNodes();

		int nodeIndex = XmlUtils.nextNodeElementIndex(rejectRequestNodes, 0);

		// Si se indica una razon del rechazo, la almacenamos
		String reason = null;
		if (nodeIndex != -1 && REASON_NODE.equalsIgnoreCase(rejectRequestNodes.item(nodeIndex).getNodeName())) {
			reason = rejectRequestNodes.item(nodeIndex).getTextContent();
			if (reason != null) {
				try {
					reason = new String(Base64.decode(reason.trim()), Charset.forName(DEFAULT_ENCODING)).trim();
				} catch (final Exception e) {
					LOGGER.warn("No se ha podido decodificar la razon del rechazo", e);  //$NON-NLS-1$
					reason = null;
				}
			}
			nodeIndex = XmlUtils.nextNodeElementIndex(rejectRequestNodes, ++nodeIndex);
		}

		if (nodeIndex == -1 || !REJECTS_NODE.equalsIgnoreCase(rejectRequestNodes.item(nodeIndex).getNodeName())) {
			throw new IllegalArgumentException(
					"No se ha encontrado el listado de identificadores en la peticion de rechazo de solicitudes"); //$NON-NLS-1$
		}

		// Listado de peticiones a rechazar
		final List<String> ids = new ArrayList<>();
		final NodeList requestNodes = rejectRequestNodes.item(nodeIndex).getChildNodes();
		for (int i = 0; i < requestNodes.getLength(); i++) {
			i = XmlUtils.nextNodeElementIndex(requestNodes, i);
			if (i == -1) {
				break;
			}
			if (!REJECT_NODE.equalsIgnoreCase(requestNodes.item(i).getNodeName())) {
				throw new IllegalArgumentException("Se ha encontrado el nodo '" + //$NON-NLS-1$
						requestNodes.item(i).getNodeName() + "' en el listado solicitudes de firma"); //$NON-NLS-1$
			}

			final Node idNode = requestNodes.item(i).getAttributes().getNamedItem(ID_ATTRIBUTE);
			if (idNode == null || idNode.getNodeValue() == null || idNode.getNodeValue().trim().length() == 0) {
				throw new IllegalArgumentException("No se ha encontrado el nodo " + ID_ATTRIBUTE + //$NON-NLS-1$
						"en el nodo de rechazo de solicitud de firma"); //$NON-NLS-1$
			}
			ids.add(idNode.getNodeValue().trim());
		}

		return new RejectRequest(ids, reason);
	}
}
