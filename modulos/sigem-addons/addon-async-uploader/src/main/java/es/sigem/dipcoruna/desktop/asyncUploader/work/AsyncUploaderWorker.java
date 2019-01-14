package es.sigem.dipcoruna.desktop.asyncUploader.work;

import javax.swing.SwingWorker;

import es.sigem.dipcoruna.desktop.asyncUploader.model.FTPUploadStatus;
import es.sigem.dipcoruna.desktop.asyncUploader.service.ftp.FtpService;
import es.sigem.dipcoruna.desktop.asyncUploader.ui.AppChooserDialog;
import es.sigem.dipcoruna.desktop.asyncUploader.ui.AppResumeDialog;

/**
 * SwingWorker para la subida de fichero mediante FTP
 */
public class AsyncUploaderWorker extends SwingWorker<Void, Void> {

	private AppChooserDialog appChooserDialog;
	
	private AppResumeDialog appResumeDialog;
	
	private FtpService ftpService;

	private String key;
	
	private FTPUploadStatus status;

	/**
	 * Set the appChooserDialog
	 * @param bean Bean
	 */
	public final void setAppChooserDialog(final AppChooserDialog bean) {
		this.appChooserDialog = bean;
	}
	
	/**
	 * Set the appResumeDialog
	 * @param bean Bean
	 */
	public final void setAppResumeDialog(final AppResumeDialog bean) {
		this.appResumeDialog = bean;
	}
	
	/**
	 * Set the ftpService
	 * @param bean Bean
	 */
	public final void setFtpService(final FtpService bean) {
		this.ftpService = bean;
	}
	
	@Override
	protected final Void doInBackground() throws Exception {
		ftpService.subirFichero(getKey());
		return null;
	}

	@Override
	protected final void done() {
		if (this.status != null) {
			if (appChooserDialog.isVisible()) {
				appChooserDialog.uploadResult(this.status);
			} else if (appResumeDialog.isVisible()) {
				appResumeDialog.uploadResult(this.status);
			}
		}
	}
	
	/**
	 * Actualiza el resultado de la tarea FTP
	 * @param value value
	 */
	public final void updateStatus(final FTPUploadStatus value) {
		this.status = value;
	}
	
	/**
	 * Actualizar progreso de tarea
	 * @param value Value
	 */
	public final void updateProgress(final int value) {
		setProgress(value);
	}

	/**
	 * @return the key
	 */
	public final String getKey() {
		return key;
	}

	/**
	 * @param value the key to set
	 */
	public final void setKey(final String value) {
		this.key = value;
	}
	
}
