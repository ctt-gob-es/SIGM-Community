package es.sigem.dipcoruna.framework.events.model;

import org.springframework.context.ApplicationEvent;

public class AbrirNavegadorEvent extends ApplicationEvent {	
	private static final long serialVersionUID = 6588444032129661292L;
	
	private final String ulr;
		
	
	public AbrirNavegadorEvent(final Object source, final String ulr) {
		super(source);		
		this.ulr = ulr;
	}


	public String getUlr() {
		return ulr;
	}

}
