package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import org.springframework.context.ApplicationEvent;

public class DispositivoSeleccionadoScanDesktopDialogEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;

	public DispositivoSeleccionadoScanDesktopDialogEvent(final Object source) {
		super(source);
	}

}
