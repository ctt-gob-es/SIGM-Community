package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.gob.afirma.core.misc.Base64;

/**
 * Analiza un documento XML para obtener una solicitud de validacion de
 * inicio de sesi&oacute;n.
 * @author Carlos Gamuci
 */
public class ValidateLoginRequestParser {

	private static final String VALIDATE_LOGIN_REQUEST_NODE = "rqtvl"; //$NON-NLS-1$

	private static final String CERT_NODE = "cert"; //$NON-NLS-1$
	private static final String PKCS1_NODE = "pkcs1"; //$NON-NLS-1$

	static ValidateLoginRequest parse(final Document doc) {
		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		final Element rootElement = doc.getDocumentElement();

		if (!VALIDATE_LOGIN_REQUEST_NODE.equalsIgnoreCase(rootElement.getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					VALIDATE_LOGIN_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					rootElement.getNodeName());
		}

		final NodeList nodes = rootElement.getChildNodes();
		int nodeIndex = XmlUtils.nextNodeElementIndex(nodes, 0);
		if (nodeIndex == -1) {
			throw new IllegalArgumentException(
					"No se ha indicado el certificado necesario para la autenticacion en el nodo " + //$NON-NLS-1$
							CERT_NODE);
		}
		final Element certNode = (Element) nodes.item(nodeIndex);
		if (!CERT_NODE.equalsIgnoreCase(certNode.getNodeName())) {
			throw new IllegalArgumentException(
					"No se ha encontrado el nodo " + CERT_NODE + //$NON-NLS-1$
					" en su lugar se encontro " + certNode.getNodeName()); //$NON-NLS-1$
		}
		nodeIndex = XmlUtils.nextNodeElementIndex(nodes, ++nodeIndex);

		final byte[] cert;
		try {
			cert = Base64.decode(certNode.getTextContent().trim());
		} catch (final Exception e) {
			throw new IllegalArgumentException(
					"No se ha podido obtener la codificacion del certificado a partir del XML: " + e); //$NON-NLS-1$
		}

		final Element signatureNode = (Element) nodes.item(nodeIndex);
		if (!PKCS1_NODE.equalsIgnoreCase(signatureNode.getNodeName())) {
			throw new IllegalArgumentException(
					"No se ha encontrado el nodo " + PKCS1_NODE + //$NON-NLS-1$
					" en su lugar se encontro " + signatureNode.getNodeName()); //$NON-NLS-1$
		}

		final byte[] pkcs1;
		try {
			pkcs1 = Base64.decode(signatureNode.getTextContent().trim());
		} catch (final Exception e) {
			throw new IllegalArgumentException(
					"No se ha podido obtener la codificacion de la firma PKCS#1 a partir del XML: " + e); //$NON-NLS-1$
		}

		return new ValidateLoginRequest(pkcs1, cert);
	}

}
