package es.sigem.dipcoruna.desktop.asyncUploader.service.ftp;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.sigem.dipcoruna.desktop.asyncUploader.model.FTPUploadStatus;
import es.sigem.dipcoruna.desktop.asyncUploader.model.State;
import es.sigem.dipcoruna.desktop.asyncUploader.service.exception.FTPServiceException;
import es.sigem.dipcoruna.desktop.asyncUploader.service.exception.StateServiceException;
import es.sigem.dipcoruna.desktop.asyncUploader.service.infoftp.dto.FTPInfo;
import es.sigem.dipcoruna.desktop.asyncUploader.service.state.StateService;
import es.sigem.dipcoruna.desktop.asyncUploader.ui.AppChooserDialog;
import es.sigem.dipcoruna.desktop.asyncUploader.ui.AppResumeDialog;
import es.sigem.dipcoruna.desktop.asyncUploader.util.ftp.DataTransferListener;


/**
 * Implementacion del servicio de FTP
 */
@Service("ftpService")
public class FtpServiceImpl implements FtpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpServiceImpl.class);
	
	private static final String SEPARADOR = "_";
	
	private FTPClient client;
	
	private FTPInfo infoFTP;
	
	@Value("${general.urlInfoFTP}")
	private String urlInfoFTP;
	
	@Autowired
	private AppChooserDialog appChooserDialog;

	@Autowired
	private AppResumeDialog appResumeDialog;
	
	@Autowired
	private StateService stateService;
	
	@Autowired
	private DataTransferListener dtl;

	@Autowired
	private GestionInfoFTPService gestionInfoFTPService;
	
	@Override
	public final void subirFichero(final String key) throws FTPServiceException, StateServiceException {

		// Crear cliente
		client = new FTPClient();
		
		// Recuperar el estado
		State state = stateService.getState(key);
		if (state == null) {
			throw new StateServiceException("No se ha podido recuperar el estado por key " + key);
		}
		
		// Obtener el archivo del sistema de ficheros local 
		File file = new File(state.getFilePath());
		try {
			// Conectar y situarse en directorio. Depende si es nueva operacion (crear folder) o es reintento 
			if (state.getTransferidos() != 0L) {
				conectarCliente(state, false);
				client.upload(file, state.getTransferidos(), dtl);
			} else {
				conectarCliente(state, true);
				client.upload(file, dtl);
			}

			// Renombrar fichero
			renombrarFichero(state, file.getName());
			
		} catch (SocketTimeoutException e) {
			// A veces, al subir un fichero grande se produce un SocketTimeout (independientemente del tiempo de actividad)
			// al finalizar la operación de subida (problema de ftp4j).
			// En este caso, recuperamos el state y comprobamos si el size del fichero es similar a la cantidad de bytes
			// confirmados satisfactoriamente. Si es asi, conectamos el cliente y finalizamos la operacion sin interaccion del usuario.
			state = stateService.getState(key);
			try {
				desconectarCliente();
				if (file.length() == state.getTransferidos()) {
					conectarCliente(state, false);
					renombrarFichero(state, file.getName());
				} else {
					if (appChooserDialog.isVisible()) {
						appChooserDialog.uploadResult(FTPUploadStatus.FAILED);
					} else {
						appResumeDialog.uploadResult(FTPUploadStatus.FAILED);
					}
				}
			} catch (FTPServiceException ftpException) {
				LOGGER.error("Error subiendo fichero [" + state.getFilePath() + " | " + state.getNombre(), ftpException);
			}
		} catch (FTPAbortedException e) {
			LOGGER.error("Abortada por el usuario [" + state.getFilePath() + " | " + state.getNombre());
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPServiceException e) {
			LOGGER.error("Error subiendo fichero [" + state.getFilePath() + " | " + state.getNombre(), e);
			if (appChooserDialog.isVisible()) {
				appChooserDialog.uploadResult(FTPUploadStatus.FAILED);
			} else {
				appResumeDialog.uploadResult(FTPUploadStatus.FAILED);
			}
		} catch (Exception e) {
			LOGGER.error("Error subiendo fichero [" + state.getFilePath() + " | " + state.getNombre(), e);
			if (appChooserDialog.isVisible()) {
				appChooserDialog.uploadResult(FTPUploadStatus.FAILED);
			} else {
				appResumeDialog.uploadResult(FTPUploadStatus.FAILED);
			}
		} finally {
			desconectarCliente();
		}
		
	}


	@Override
	public final void abortarSubida(final String key) throws FTPServiceException {
		try {
			if (client != null) {
				client.abortCurrentDataTransfer(true);
				desconectarCliente();
				State state = stateService.getState(key);
				conectarCliente(state, false);
				File file = new File(state.getFilePath());
				client.deleteFile(file.getName());
				client.changeDirectoryUp();
				client.deleteDirectory(state.getFolder());
				if (appChooserDialog.isVisible()) {
					appChooserDialog.uploadResult(FTPUploadStatus.ABORTED);
				} else {
					appResumeDialog.uploadResult(FTPUploadStatus.ABORTED);
				}
			}
		} catch (IOException | FTPIllegalReplyException | IllegalStateException | FTPException e) {
			LOGGER.error("Se ha producido un error abortando la subida actual", e);
			throw new FTPServiceException();
		} finally {
			desconectarCliente();
		}
	}
	
	
	/**
	 * Conecta el cliente recibido al FTP configurado en la llamada
	 * @param state State
	 * @param createFolder createFolder
	 * @throws FTPServiceException Si hay problemas en la conexión
	 */
	private void conectarCliente(final State state, final boolean createFolder) throws FTPServiceException {
		try {
			setFTPInfo();
			this.client.connect(infoFTP.getHost(), Integer.parseInt(infoFTP.getPort()));
			this.client.login(infoFTP.getUser(), infoFTP.getPass());
			this.client.changeDirectory(infoFTP.getPath() + "/" + state.getEntidad());
			if (createFolder) {
				this.client.createDirectory(state.getFolder());
			}
			this.client.changeDirectory(state.getFolder());
		} catch (NumberFormatException | IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			LOGGER.error("Error conectando al FTP: " + e.getMessage(), e);
			throw new FTPServiceException(e);
		}
	}
	
	
	/**
	 * Renombra el fichero al nombre final
	 * @param state state
	 * @param fileName fileName
	 * @throws FTPServiceException Si hay problemas con el renombrado
	 */
	private void renombrarFichero(final State state, final String fileName) throws FTPServiceException {
		try {
			String indicarFase = "";
			if ("true".equals(state.getFase())) {
				indicarFase = "f";
			}
			String nombreFichero = state.getUsuario() + SEPARADOR + indicarFase + state.getTramite() + SEPARADOR + state.getTipoDocumento()
					+ SEPARADOR + state.getTipoDestino() + SEPARADOR + state.getIdDestino() + SEPARADOR + state.getNombre() 
					+ "." + FilenameUtils.getExtension(state.getFilePath());  
			client.rename(fileName, nombreFichero);
			LOGGER.debug("Renombrado de fichero a " + nombreFichero);
			if (appChooserDialog.isVisible()) {
				appChooserDialog.uploadResult(FTPUploadStatus.COMPLETED);
			} else {
				appResumeDialog.uploadResult(FTPUploadStatus.COMPLETED);
			}
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			throw new FTPServiceException(e);
		}
	}

	
	/**
	 * Desconecta el FTP. Un fallo en la desconexión no implica error.
	 */
	private void desconectarCliente() {
		try {
			client.disconnect(true);
		} catch (IllegalStateException e) {
			LOGGER.error("No existia conexion abierta.");
		} catch (IOException | FTPIllegalReplyException | FTPException e) {
			LOGGER.error("No se pudo desconectar la sesion del FTP", e);
		}
	}

	
	/**
	 * Obtiene la informacion del FTP
	 */
	private void setFTPInfo() {
		if (this.infoFTP == null) {
			this.infoFTP = gestionInfoFTPService.getFTPInfo();
		}		
	}
	
}
