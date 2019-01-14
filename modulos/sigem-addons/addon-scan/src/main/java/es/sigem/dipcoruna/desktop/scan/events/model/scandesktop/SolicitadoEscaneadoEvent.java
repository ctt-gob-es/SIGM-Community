package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import org.springframework.context.ApplicationEvent;

public class SolicitadoEscaneadoEvent extends ApplicationEvent {
	private static final long serialVersionUID = -9188488118970183551L;

	private final String nombreDispositivo;
	private final String nombrePerfil;

	public SolicitadoEscaneadoEvent(final Object source, final String nombreDispositivo, final String nombrePerfil) {
		super(source);
		this.nombreDispositivo = nombreDispositivo;
		this.nombrePerfil = nombrePerfil;
	}

	public String getNombreDispositivo() {
		return nombreDispositivo;
	}

	public String getNombrePerfil() {
		return nombrePerfil;
	}
}
