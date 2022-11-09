/* Copyright (C) 2017 [Gobierno de Espana]
 * This file is part of FIRe.
 * FIRe is free software; you can redistribute it and/or modify it under the terms of:
 *   - the GNU General Public License as published by the Free Software Foundation;
 *     either version 2 of the License, or (at your option) any later version.
 *   - or The European Software License; either version 1.1 or (at your option) any later version.
 * Date: 08/09/2017
 * You may contact the copyright holder at: soporte.afirma@correo.gob.es
 */
package es.gob.afirma.signfolder.server.proxy.sessions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gob.afirma.signfolder.server.proxy.ConfigManager;
import es.gob.afirma.signfolder.server.proxy.SessionParams;

/**
 * Gestiona las transacciones de firma de la aplicaciones almacenando los datos de cada
 * una en sesiones. Las sesiones pueden guardarse tanto en memoria como en un espacio
 * compartido por varias instancias de la aplicaci&oacute;n a trav&eacute;s de un DAO.
 * Esta clase gestiona autom&aacute;ticamente el borrado de sesiones caducadas en memoria y
 * en el espacio compartido. Los datos temporales de las sesiones caducadas solo se
 * eliminar&aacute;n a partir de las sesiones almacenadas en memoria, ya que, de hacerlo
 * para las sesiones del espacio compartido, se solicitar&iacute;a su borrado desde cada uno
 * de los nodos del sistema.
 */
public final class SessionCollector {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionCollector.class);

	/** Peri&oacute;do m&aacute;ximo de inactividad. */
	private static final int MAX_INACTIVE_INTERVAL = 8 * 60 * 60; // 8 Horas

	private static final SessionDAO dao;

	private static final boolean shareSession;

    static {

    	try {
    		ConfigManager.checkInitialized();
    	}
    	catch (final Exception e) {
    		LOGGER.error("No se pudo cargar la configuracion del servicio proxy", e); //$NON-NLS-1$
    		throw e;
		}

    	dao = SessionDAO.getInstance();

    	shareSession = ConfigManager.isShareSessionEnabled();
    }

    /**
	 * Crea una nueva sesi&oacute;n (si no existia ya).
	 * @param request Petici&oacute;n enviada por el cliente para el que se desea
	 * crear la sesi&oacute;n.
	 * @return Sesi&oacute;n creada.
	 */
	public static HttpSession createSession(final HttpServletRequest request) {
		LOGGER.debug("Creamos sesion"); //$NON-NLS-1$
    	final HttpSession session = request.getSession();
    	session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
    	return session;
	}

    /**
	 * Crea una nueva sesi&oacute;n compartida a partir de la sesion actual.
	 * @param session Sesi&oacute;n que se desea compartir.
	 * @return Identificador de la sesi&oacute;n compartida.
	 * @throws IOException Cuando no se puede compartir la sesi&oacute;n.
	 */
	public static String createSharedSession(final HttpSession session) throws IOException {
		LOGGER.debug("Creamos sesion compartida"); //$NON-NLS-1$
		final String ssid = dao.createSharedSession();
		session.setAttribute(SessionParams.SHARED_SESSION_ID, ssid);
		dao.writeSession(session, ssid);
		return ssid;
	}

	/**
	 * Si se creo una sesion compartida, se actualiza los datos de sesi&oacute;n.
	 * @param session Datos de la sesi&oacute;n.
	 */
	public static void updateSession(final HttpSession session) {
		if (shareSession) {
			final String ssid = (String) session.getAttribute(SessionParams.SHARED_SESSION_ID);
			LOGGER.debug("Actualizamos la sesion compartida: " + ssid); //$NON-NLS-1$
			if (ssid != null) {
				try {
					dao.writeSession(session, ssid);
				} catch (final IOException e) {
					LOGGER.warn("Error al actualizar la sesion compartida " + ssid, e); //$NON-NLS-1$
				}
			}
		}
	}

	/**
	 * Recupera la sesion del usuario de la memoria o del espacio compartido de sesiones, priorizando
	 * el uso de las sesiones en disco. Si la sesion no existe en alguno de ambos entornos, se
	 * devuelve {@code null}.
	 * @param request Petici&oacute;n realizada.
	 * @param ssid Identificador de sesi&oacute;n compartida si aplica.
	 * @return Sesi&oacute;n de la operaci&oacute;n o {@code null} si no se pudo cargar de memoria
	 * y, estando habilitadas las sesiones compartida, si no se encontr&oacute; compartida.
	 */
	public static HttpSession getSession(final HttpServletRequest request, final String ssid) {

		HttpSession session = request.getSession(false);

		if (ssid != null && shareSession) {
			LOGGER.debug("Cargamos la sesion compartida: " + ssid); //$NON-NLS-1$
			SessionInfo sessionInfo;
			try {
				sessionInfo = dao.recoverSessionInfo(ssid);
			}
			catch (final Exception e) {
				// Si no se puede cargar, usamos la de memoria exista o no
				return session;
			}

			// Si se carga de disco, se crea una nueva sesion en memoria o se carga la existente se
			// actualiza con los datos a disco
			if (session == null) {
				session = request.getSession();
				final long interval = sessionInfo.getExpirationDate() - System.currentTimeMillis();
				if (interval <= 0) {
					LOGGER.warn("La sesion de la peticion " + ssid + " ha caducado"); //$NON-NLS-1$ //$NON-NLS-2$
					return null;
				}
				session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
			}
			sessionInfo.save(session);
		}
		return session;
	}

    /**
     * Busca una sesion en el pool de sesiones para eliminarla junto con sus datos temporales.
     * Si se establecio tambien un DAO de sesiones compartidas, se elimina tambi&eacute;n del mismo.
     * @param id Identificador de la sesi&oacute;n.
     */
    public static void removeSession(final HttpSession session) {
    	if (session == null) {
    		return;
    	}

    	LOGGER.debug("Eliminamos la sesion"); //$NON-NLS-1$

    	// Obtenemos el identificador de sesion compartida (si lo hay)
    	final String ssid = (String) session.getAttribute(SessionParams.SHARED_SESSION_ID);

    	// Invalidamos la sesion
    	session.invalidate();

    	// Eliminamos la sesion compartida
    	if (ssid != null) {
    		LOGGER.debug("Eliminamos la sesion compartida: " + ssid); //$NON-NLS-1$
    		dao.removeSession(ssid);
    	}
    }
}
