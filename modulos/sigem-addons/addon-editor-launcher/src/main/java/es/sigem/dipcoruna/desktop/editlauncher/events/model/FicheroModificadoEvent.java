package es.sigem.dipcoruna.desktop.editlauncher.events.model;

import org.springframework.context.ApplicationEvent;

public class FicheroModificadoEvent extends ApplicationEvent {		
    private static final long serialVersionUID = -2464620822902175318L;
    
    private final String pathFichero;
			
	public FicheroModificadoEvent(final Object source, final String pathFichero) {
		super(source);		
		this.pathFichero = pathFichero;	
	}

    public String getPathFichero() {
        return pathFichero;
    }
}
