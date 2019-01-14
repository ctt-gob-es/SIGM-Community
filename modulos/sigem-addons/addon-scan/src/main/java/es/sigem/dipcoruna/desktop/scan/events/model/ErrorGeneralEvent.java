package es.sigem.dipcoruna.desktop.scan.events.model;

import org.springframework.context.ApplicationEvent;

public class ErrorGeneralEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1523944631703927006L;

	private final String titulo;
	private final String mensaje;
	private final String[] args;
	private final Throwable e;
    

	public ErrorGeneralEvent(final Object source, final String titulo, final String mensaje) {
		super(source);
		this.titulo = titulo;
		this.mensaje = mensaje;
		this.e = null;
		this.args = new String[]{};
	}

	public ErrorGeneralEvent(final Object source, final String titulo, final String mensaje, String[] args) {
        super(source);
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.args = args;
        this.e = null;
    }
	
   public ErrorGeneralEvent(final Object source, final String titulo, final String mensaje, final Throwable e) {
        super(source);
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.args = new String[]{};
        this.e = e;     
    }

	public String getTitulo() {
		return titulo;
	}

	public String getMensaje() {
		return mensaje;
	}
	
    public String[] getArgs() {
        return args;
    }

    public Throwable getE() {
        return e;
    }
}
