package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import org.springframework.context.ApplicationEvent;

public class MostrarDesktopScanDialogEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;

	public MostrarDesktopScanDialogEvent(final Object source) {
		super(source);
	}

}
