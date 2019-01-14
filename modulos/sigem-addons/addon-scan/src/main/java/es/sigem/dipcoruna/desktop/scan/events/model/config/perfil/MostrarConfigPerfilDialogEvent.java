package es.sigem.dipcoruna.desktop.scan.events.model.config.perfil;

import org.springframework.context.ApplicationEvent;

public class MostrarConfigPerfilDialogEvent  extends ApplicationEvent {
	private static final long serialVersionUID = 4660679568322552201L;

	private final String nombrePerfil;
    private final String nombreDispositivo;

    public MostrarConfigPerfilDialogEvent(final Object source, final String nombrePerfil, final String nombreDispositivo) {
        super(source);
        this.nombrePerfil = nombrePerfil;
        this.nombreDispositivo = nombreDispositivo;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

	@Override
	public String toString() {
		return "MostrarConfigPerfilDialogEvent [nombrePerfil=" + nombrePerfil
				+ "]";
	}
}
