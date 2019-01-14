package es.sigem.dipcoruna.desktop.scan.events.model.config.perfil;

import org.springframework.context.ApplicationEvent;

import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;

public class CrearOActualizarScanProfileEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;

	private final ScanProfile scanProfile;

	public CrearOActualizarScanProfileEvent(final Object source, final ScanProfile scanProfile) {
		super(source);
		this.scanProfile = scanProfile;
	}

    public ScanProfile getScanProfile() {
        return scanProfile;
    }
}
