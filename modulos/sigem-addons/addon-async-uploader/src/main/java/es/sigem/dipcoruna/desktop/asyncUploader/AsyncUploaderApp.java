package es.sigem.dipcoruna.desktop.asyncUploader;

/**
 * Interfaz para la base del addon de async uploader
 */
public interface AsyncUploaderApp {

	/**
	 * checkVersion
	 * @param urlCheckForUpdates urlCheckForUpdates
	 */
	void checkVersion(String urlCheckForUpdates);

	/**
	 * Lanzar subida asíncrona
	 */
	void launchAsyncUpload();

	/**
	 * Lanzar continuar subidas
	 */
	void launchResumeUploads();
	
}
