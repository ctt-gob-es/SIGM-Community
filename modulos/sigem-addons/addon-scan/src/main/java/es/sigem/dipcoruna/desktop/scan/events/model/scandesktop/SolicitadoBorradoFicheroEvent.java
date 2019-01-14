package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import java.io.File;

import org.springframework.context.ApplicationEvent;

public class SolicitadoBorradoFicheroEvent extends ApplicationEvent {
	private static final long serialVersionUID = -2005309504318263818L;

	private final File file;

	public SolicitadoBorradoFicheroEvent(final Object source, final File file) {
		super(source);
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}
