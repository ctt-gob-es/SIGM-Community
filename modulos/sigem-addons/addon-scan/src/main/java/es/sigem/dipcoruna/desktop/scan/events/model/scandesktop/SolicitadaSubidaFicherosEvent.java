package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import org.springframework.context.ApplicationEvent;

public class SolicitadaSubidaFicherosEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1937940226579867498L;

	public SolicitadaSubidaFicherosEvent(final Object source) {
		super(source);
	}
}
