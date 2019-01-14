package es.sigem.dipcoruna.desktop.scan.exceptions;

import java.util.Collections;
import java.util.List;

public class UploadFilesException extends RuntimeException {
	private static final long serialVersionUID = 7728554447503256847L;

	private List<String> erroresServidor = Collections.<String>emptyList();

	public UploadFilesException(final String msg) {
	    super(msg);
	}

	public UploadFilesException (final String msg, final Throwable thrw) {
		super(msg, thrw);
	}

	public UploadFilesException (final String msg, final List<String> erroresServidor) {
		super(msg);
		this.erroresServidor = erroresServidor;
	}

    public List<String> getErroresServidor() {
        return erroresServidor;
    }

}
