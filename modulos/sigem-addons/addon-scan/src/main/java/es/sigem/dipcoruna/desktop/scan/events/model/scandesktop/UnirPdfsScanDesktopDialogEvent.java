package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import java.io.File;
import java.util.List;

import org.springframework.context.ApplicationEvent;

public class UnirPdfsScanDesktopDialogEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;
    private final List<File> files;

	public UnirPdfsScanDesktopDialogEvent(final Object source, final List<File> files) {
		super(source);
		this.files = files;
	}

    public List<File> getFiles() {
        return files;
    }	
}
