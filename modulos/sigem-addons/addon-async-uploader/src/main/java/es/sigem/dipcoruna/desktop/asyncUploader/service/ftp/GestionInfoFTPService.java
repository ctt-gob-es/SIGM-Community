package es.sigem.dipcoruna.desktop.asyncUploader.service.ftp;

import es.sigem.dipcoruna.desktop.asyncUploader.service.infoftp.dto.FTPInfo;

/**
 * Interfaz para gestionar la recuperación de la información de conexión al FTP
 */
public interface GestionInfoFTPService {

	/**
	 * Obtiene la información de conexión con el FTP
	 * @return FTPInfo
	 */
	FTPInfo getFTPInfo();

}
