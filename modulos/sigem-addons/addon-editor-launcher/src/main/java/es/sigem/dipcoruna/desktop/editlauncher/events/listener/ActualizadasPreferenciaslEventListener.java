package es.sigem.dipcoruna.desktop.editlauncher.events.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.editlauncher.events.model.ActualizadasPreferenciaslEvent;
import es.sigem.dipcoruna.framework.service.util.PreferenciasHolder;

@Component("actualizadasPreferenciaslEventListener")
public class ActualizadasPreferenciaslEventListener implements ApplicationListener<ActualizadasPreferenciaslEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActualizadasPreferenciaslEventListener.class);
		
	@Autowired
	private PreferenciasHolder preferenciasHolder;
			
	@Override
	public void onApplicationEvent(ActualizadasPreferenciaslEvent event) {		
		LOGGER.debug("Se van actualizan las preferencias", event.getSource().getClass());
		
		
		preferenciasHolder.clearAllKeys();
		for(String key : event.getPreferencias().keySet()) {
			preferenciasHolder.putProperty(key,  event.getPreferencias().get(key));
		}
		LOGGER.info("Se actualizaron {} preferencias", event.getPreferencias().keySet().size());	
	}

}
