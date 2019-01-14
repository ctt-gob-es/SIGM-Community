package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import java.io.File;

import org.springframework.context.ApplicationEvent;

public class CambioNombreFicheroEvent extends ApplicationEvent {
	private static final long serialVersionUID = -2005309504318263818L;

	private final File file;
	private final String nombreFichero;

	public CambioNombreFicheroEvent(final Object source, final File file, String nombreFichero) {
		super(source);
		this.file = file;
		this.nombreFichero = nombreFichero;
	}

	public File getFile() {
		return file;
	}
	
	public String getNombreFichero(){
		return this.nombreFichero;
	}
}
