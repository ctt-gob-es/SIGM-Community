package es.sigem.dipcoruna.desktop.asyncUploader.util.ftp;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.asyncUploader.model.FTPUploadStatus;
import es.sigem.dipcoruna.desktop.asyncUploader.service.state.StateService;
import es.sigem.dipcoruna.desktop.asyncUploader.work.AsyncUploaderWorker;

/**
 * Transfer listener propio para el ftp
 */
@Component("dataTransferListener")
public class DataTransferListener implements FTPDataTransferListener {

	private long totalTransferred = 0; 
	
	private AsyncUploaderWorker worker;
	
	@Autowired
	private StateService stateService;
	
	private String key;

	private long size;

	/**
	 * Set the total transferred in resumed operations.
	 * @param bytes Bytes
	 */
	public final void setTotalTransferred(final long bytes) {
		this.totalTransferred = bytes;
	}
	
	/**
	 * Set the AsyncUploaderWorker
	 * @param bean Bean
	 */
	public final void setWorker(final AsyncUploaderWorker bean) {
		this.worker = bean;
	}
	
	/**
	 * set Key
	 * @param value The value to set
	 */
	public final void setKey(final String value) {
		this.key = value;
	}
	
	/**
	 * set size
	 * @param value The value to set
	 */
	public final void setSize(final long value) {
		this.size = value;
	}
	
	@Override
	public final void aborted() {
	}

	@Override
	public final void completed() {
		worker.updateStatus(FTPUploadStatus.COMPLETED);
	}

	@Override
	public final void failed() {
		worker.updateStatus(FTPUploadStatus.FAILED);
	}

	@Override
	public final void started() {
	}

	@Override
	public final void transferred(final int bytes) {
		totalTransferred += bytes;
		stateService.updateState(key, totalTransferred);
		int percent = 0;
		if (size > 0) {
			percent = (int) ((totalTransferred * 100.0f) / size);
		}
		worker.updateProgress(percent);
	}

}
