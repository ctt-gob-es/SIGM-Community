package es.sigem.dipcoruna.desktop.asyncUploader.service.ftp;

import es.sigem.dipcoruna.desktop.asyncUploader.service.exception.FTPServiceException;
import es.sigem.dipcoruna.desktop.asyncUploader.service.exception.StateServiceException;

/**
 * Interfaz para el servicio de FTP
 */
public interface FtpService {

	/**
	 * Subir el fichero al FTP
	 * @param key Key que identifica el state
	 * @throws FTPServiceException Exception in FTP client
	 * @throws StateServiceException Exception in state service
	 */
	void subirFichero(String key) throws FTPServiceException, StateServiceException;

	/**
	 * Aborta la subida actual
	 * @param key Key que identifica el state
	 * @throws FTPServiceException Si hay algún problema
	 */
	void abortarSubida(String key) throws FTPServiceException;

}
