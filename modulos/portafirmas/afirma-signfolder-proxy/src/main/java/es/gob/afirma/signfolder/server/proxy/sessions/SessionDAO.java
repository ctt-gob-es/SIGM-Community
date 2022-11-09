package es.gob.afirma.signfolder.server.proxy.sessions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gob.afirma.signfolder.server.proxy.ConfigManager;

public class SessionDAO {

	private static final String SESSIONS_TEMP_DIR = "sessions"; //$NON-NLS-1$

	private static final long DEFAULT_MAX_SESSION_PERIOD = 8 * 60 * 60 * 1000; // 8 horas

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionDAO.class);

	private static SessionDAO instance = null;

	static SessionDAO getInstance() {
		if (instance == null) {
			instance = new SessionDAO();
		}
		return instance;
	}


	/** Directorio para la comparticion de sesiones. */
	private final File dir;

	private final int requestToClean;

	private int currentRequest = 0;

	/**
	 * Crea el DAO para la compartici&oacute;n de sesiones.
	 */
	private SessionDAO() {

		final String tDir = ConfigManager.getTempDir();

		this.requestToClean = ConfigManager.getRequestToClean();

		this.dir = new File(tDir, SESSIONS_TEMP_DIR);

		if (!this.dir.exists()) {
			this.dir.mkdirs();
		}
		else {
			// Al iniciar el DAO limpiamos las sesiones caducadas que existiesen previamente
			try {
				new CleanerThread(this.dir).start();
			}
			catch (final Exception e) {
				LOGGER.warn("Error durante la limpieza del directorio temporal", e); //$NON-NLS-1$
			}
		}
	}


	/**
	 * Crea una nueva sesi&oacute;n compartida.
	 * @return Identificador de la sesi&oacute;n compartida.
	 * @throws IOException Cuando no se puede crear la sesi&oacute;n compartida.
	 */
	public String createSharedSession() throws IOException {
		String uuid;
		do {
			uuid = UUID.randomUUID().toString();
		}
		while (!new File(this.dir, uuid).createNewFile());

		return uuid;
	}

	/** Actualiza la sesi&oacute;n compartida.
	 * @param session Sesi&oacute;n con los datos a volcar.
	 * @param sessionId Identificador de la sesi&oacute;n compartida. */
	public void writeSession(final HttpSession session, final String sessionId) throws IOException {

		final File sharedFile = new File(this.dir, sessionId);
		if (!sharedFile.isFile()) {
			LOGGER.warn("No se ha encontrado la sesion compartida: " + sessionId); //$NON-NLS-1$
			throw new IOException("No se ha encontrado la sesion compartida: " + sessionId); //$NON-NLS-1$
		}

		// Se guardan todos los datos de la sesion
		try (final OutputStream os = new FileOutputStream(sharedFile);
			 final ObjectOutputStream oos = new ObjectOutputStream(os)) {

			final SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.load(session);
			sessionInfo.setExpirationDate(System.currentTimeMillis() +
					session.getMaxInactiveInterval() * 1000);

			oos.writeObject(sessionInfo);

			os.flush();
		}
	}

	/**
	 * Carga los datos de una sesion del espacio compartido.
	 * @param sessionId Identificador de la sesi&oacute;n.
	 * @return Informaci&oacute;n extraida de la sesi&oacute;n solicitada.
	 * @throws Exception La sesion no existe o no ha podido cargarse.
	 */
	public SessionInfo recoverSessionInfo(final String sessionId) throws Exception {

		// Comprobamos que exista la sesion y que no este caducada
		final File sessionFile = new File(this.dir, sessionId);
		if (!sessionFile.isFile()) {
			throw new FileNotFoundException("La sesion compartida indicada no existe"); //$NON-NLS-1$
		} else if (sessionFile.lastModified() + DEFAULT_MAX_SESSION_PERIOD < new Date().getTime()) {
			Files.delete(sessionFile.toPath());
			LOGGER.info("Se intenta cargar una sesion caducada. Se elimina."); //$NON-NLS-1$
			throw new FileNotFoundException("La sesion compartida indicada no existe"); //$NON-NLS-1$
		}

		// Cargamos la informacion de la sesion
		final SessionInfo sessionInfo;
		try (final InputStream is = new FileInputStream(sessionFile);
			final ObjectInputStream ois = new ObjectInputStream(is)) {
			sessionInfo = (SessionInfo) ois.readObject();
		}

		// Incrementamos el conteo de recuperaciones realizadas y, si hemos llegado al maximo,
		// lanzamos el proceso de limpieza del directorio principal y reiniciamos el contador
		synchronized (this) {
			if (++this.currentRequest >= this.requestToClean) {
				this.currentRequest = 0;
				new CleanerThread(this.dir).start();
			}
		}
		return sessionInfo;
	}

	/**
	 * Elimina una sesi&oacute;n compartida.
	 * @param ssid Identificador de la sesi&oacute;n compartida.
	 */
	public void removeSession(final String ssid) {
		try {
			Files.deleteIfExists(new File(this.dir, ssid).toPath());
		} catch (final IOException e) {
			LOGGER.warn("No se pudo eliminar la sesion compartida: " + ssid); //$NON-NLS-1$
		}
	}

	/**
	 * Hilo para la limpieza de sesiones en disco.
	 */
	class CleanerThread extends Thread {

		private final Logger LOGGER_THREAD = LoggerFactory.getLogger(CleanerThread.class);

		private final File cleaningDir;

		/**
		 * Crea un hilo para le borrado de los ficheros de sesi&oacute;n caducados.
		 * @param dir Directorio con los ficheros de sesion.
		 * @param soft Indica si se desea una limpieza cuidadosa {@code true}}
		 */
		public CleanerThread(final File dir) {
			this.cleaningDir = dir;
		}

		@Override
		public void run() {

			this.LOGGER_THREAD.debug("Se inicia el hilo de limpieza de los ficheros de sesion"); //$NON-NLS-1$

			File[] files;
			try {
				files = this.cleaningDir.listFiles();
			}
			catch (final Exception e) {
				this.LOGGER_THREAD.warn("No se pudo realizar la limpieza del directorio de sesiones", e); //$NON-NLS-1$
				return;
			}

			final long currentTime = System.currentTimeMillis();
			for (final File file : files) {
				if (currentTime > file.lastModified() + DEFAULT_MAX_SESSION_PERIOD) {
					try {
						Files.deleteIfExists(file.toPath());
					} catch (final IOException e) {
						this.LOGGER_THREAD.warn("No se pudo eliminar un fichero del directorio de sesiones: " + file.getName(), e); //$NON-NLS-1$
					}
				}
			}
		}
	}
}
