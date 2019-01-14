package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import java.io.File;

import org.springframework.context.ApplicationEvent;

public class NuevoFicheroEscaneadoEvent extends ApplicationEvent {
	private static final long serialVersionUID = 7801662927779231839L;
	
	private final File file;
	
	public NuevoFicheroEscaneadoEvent(Object source, File file) {
		super(source);
		this.file = file;
	}

	public File getFile() {
		return file;
	}	
}
