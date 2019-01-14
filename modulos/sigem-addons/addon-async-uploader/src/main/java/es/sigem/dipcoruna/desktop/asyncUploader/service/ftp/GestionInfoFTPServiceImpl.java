package es.sigem.dipcoruna.desktop.asyncUploader.service.ftp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.sigem.dipcoruna.desktop.asyncUploader.service.infoftp.dto.FTPInfo;
import es.sigem.dipcoruna.desktop.asyncUploader.util.rest.RestClient;

/**
 * Servicio para gestionar la recuperación de la información de conexión al FTP
 */
@Service("gestionInfoFTPService")
public class GestionInfoFTPServiceImpl implements GestionInfoFTPService {

	@Value("${general.password}")
	private String username;

	@Value("${general.username}")
	private String password;
	
	@Value("${general.urlInfoFTP}")
	private String urlInfoFtp;
	
	@Override
	public final FTPInfo getFTPInfo() {
		RestClient restClient = new RestClient(username, password);
		return restClient.getForEntity(urlInfoFtp, FTPInfo.class).getBody();
	}
	
}
