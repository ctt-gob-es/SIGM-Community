package es.sigem.dipcoruna.desktop.scan.events.model.config.perfil;

import org.springframework.context.ApplicationEvent;

import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;

public class ScanProfileActualizadoEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;

	private final ScanProfile scanProfile;

	public ScanProfileActualizadoEvent(final Object source) {
		super(source);
		this.scanProfile = null;
	}

	public ScanProfileActualizadoEvent(final Object source, final ScanProfile scanProfile) {
        super(source);
        this.scanProfile = scanProfile;
    }


    public ScanProfile getScanProfile() {
        return scanProfile;
    }
}
