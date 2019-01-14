package es.sigem.dipcoruna.desktop.scan.events.model.config.listaperfiles;

import org.springframework.context.ApplicationEvent;

public class DispositivoSeleccionadConfigListDialogEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;

	public DispositivoSeleccionadConfigListDialogEvent(final Object source) {
		super(source);
	}

}
