package es.gob.afirma.signfolder.server.proxy;

import org.w3c.dom.Document;

class NotificationRegistryParser {

	private static final String REGISTRY_REQUEST_NODE = "rqtreg"; //$NON-NLS-1$
	private static final String PLATFORM_ID_ATTRIBUTE = "plt"; //$NON-NLS-1$
	private static final String DEVICE_ID_ATTRIBUTE = "dvc"; //$NON-NLS-1$
	private static final String NOTIFICATIONS_ID_REGISTRY = "tkn"; //$NON-NLS-1$

	public static NotificationRegistry parse(final Document doc) {

		if (doc == null) {
			throw new IllegalArgumentException("El documento proporcionado no puede ser nulo");  //$NON-NLS-1$
		}

		if (!REGISTRY_REQUEST_NODE.equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
			throw new IllegalArgumentException("El elemento raiz del XML debe ser '" + //$NON-NLS-1$
					REGISTRY_REQUEST_NODE + "' y aparece: " + //$NON-NLS-1$
					doc.getDocumentElement().getNodeName());
		}

		// Recogermos el identificador de la plataforma de notificacion
		final String platformId = doc.getDocumentElement().getAttribute(PLATFORM_ID_ATTRIBUTE);
		if (platformId == null || platformId.isEmpty()) {
			throw new IllegalArgumentException("No se ha indicado el atributo " +  //$NON-NLS-1$
					PLATFORM_ID_ATTRIBUTE + " con el identificador la plataforma de notificacion"); //$NON-NLS-1$
		}

		// Recogermos el identificador del dispositivo del usuario
		final String deviceId = doc.getDocumentElement().getAttribute(DEVICE_ID_ATTRIBUTE);
		if (deviceId == null || deviceId.isEmpty()) {
			throw new IllegalArgumentException("No se ha indicado el atributo " +  //$NON-NLS-1$
					DEVICE_ID_ATTRIBUTE + " con el identificador del dispositivo del usuario"); //$NON-NLS-1$
		}

		// Recogermos el identificador de de registro en el servicio de notificaciones de Google
		final String idRegistry = doc.getDocumentElement().getAttribute(NOTIFICATIONS_ID_REGISTRY);
		if (idRegistry == null || idRegistry.isEmpty()) {
			throw new IllegalArgumentException("No se ha indicado el atributo " +  //$NON-NLS-1$
					NOTIFICATIONS_ID_REGISTRY + " con el identificador del dispositivo del usuario"); //$NON-NLS-1$
		}

		return new NotificationRegistry(deviceId, platformId, idRegistry);
	}
}
