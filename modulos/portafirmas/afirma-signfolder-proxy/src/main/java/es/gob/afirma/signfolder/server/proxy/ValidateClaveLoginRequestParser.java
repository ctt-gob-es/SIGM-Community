package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Analiza un documento XML para obtener una solicitud de validacion de
 * inicio de sesi&oacute;n.
 * @author Carlos Gamuci
 */
public class ValidateClaveLoginRequestParser {

	private static final String VALIDATE_LOGIN_REQUEST_NODE = "rqtvl"; //$NON-NLS-1$

	private static final String SAML_TOKEN_NODE = "samltkn"; //$NON-NLS-1$
	private static final String REDIRECTION_URL_NODE = "redirecturl"; //$NON-NLS-1$

	static ValidateClaveLoginRequest parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!VALIDATE_LOGIN_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					VALIDATE_LOGIN_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}

		final NodeList nodes = doc.getDocumentElement().getChildNodes();
		int nodeIndex = XmlUtils.nextNodeElementIndex(nodes, 0);

		final Element samlTokenNode = (Element) nodes.item(nodeIndex);
		if (!SAML_TOKEN_NODE.equalsIgnoreCase(samlTokenNode.getNodeName())) {
			throw new IllegalArgumentException(
					"No se ha encontrado el nodo " + SAML_TOKEN_NODE + //$NON-NLS-1$
					" en su lugar se encontro " + samlTokenNode.getNodeName()); //$NON-NLS-1$
		}

		final String samlToken;
		try {
			samlToken = samlTokenNode.getTextContent().trim();
		} catch (final Exception e) {
			throw new IllegalArgumentException(
					"No se ha podido obtener la codificacion del token SAML a partir del XML: " + e); //$NON-NLS-1$
		}

		nodeIndex = XmlUtils.nextNodeElementIndex(nodes, ++nodeIndex);

		final Element redirectionNode = (Element) nodes.item(nodeIndex);
		if (!REDIRECTION_URL_NODE.equalsIgnoreCase(redirectionNode.getNodeName())) {
			throw new IllegalArgumentException(
					"No se ha encontrado el nodo " + REDIRECTION_URL_NODE + //$NON-NLS-1$
					" en su lugar se encontro " + redirectionNode.getNodeName()); //$NON-NLS-1$
		}

		final String redirectionUrl;
		try {
			redirectionUrl = redirectionNode.getTextContent().trim();
		} catch (final Exception e) {
			throw new IllegalArgumentException(
					"No se ha podido obtener a partir del XML la URL del proxy a la que redirigir tras el proceso de autenticacion: " + e); //$NON-NLS-1$
		}

		final ValidateClaveLoginRequest loginRequest = new ValidateClaveLoginRequest();
		loginRequest.setSaml(samlToken);
		loginRequest.setRedirectionUrl(redirectionUrl);

		return loginRequest;
	}

}
