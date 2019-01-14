package es.sigem.dipcoruna.desktop.editlauncher.events.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

public class ActualizadasPreferenciaslEvent extends ApplicationEvent {	
	private static final long serialVersionUID = 1523944631703927006L;
	private Map<String, String> preferencias;
		
	
	public ActualizadasPreferenciaslEvent(Object source, Map<String, String> preferencias) {
		super(source);		
		this.preferencias = preferencias;
	}


	public Map<String, String> getPreferencias() {
		return new HashMap<String, String> (preferencias);
	}

}
