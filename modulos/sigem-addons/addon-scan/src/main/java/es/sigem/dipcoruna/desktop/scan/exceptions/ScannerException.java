package es.sigem.dipcoruna.desktop.scan.exceptions;

public class ScannerException extends RuntimeException {
	private static final long serialVersionUID = 7728554447503256847L;
	

	public ScannerException (String msg, Throwable thrw) {
		super(msg, thrw);
	}
	
	public ScannerException (String msg) {
		super(msg);
	}
}
