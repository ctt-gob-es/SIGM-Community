package es.sigem.dipcoruna.desktop.scan.events.model.visor;

import java.io.File;

import org.springframework.context.ApplicationEvent;

public class MostrarVisorFicheroEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;

	private final File file;
	
	public MostrarVisorFicheroEvent(final Object source, final File file) {
		super(source);
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}
