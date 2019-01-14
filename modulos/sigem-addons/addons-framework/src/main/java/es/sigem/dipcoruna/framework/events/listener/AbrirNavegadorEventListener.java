package es.sigem.dipcoruna.framework.events.listener;

import java.awt.Desktop;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.framework.events.model.AbrirNavegadorEvent;
import es.sigem.dipcoruna.framework.service.util.PreferenciasHolder;

@Component("abrirNavegadorEventListener")
public class AbrirNavegadorEventListener implements ApplicationListener<AbrirNavegadorEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbrirNavegadorEventListener.class);
		
	@Autowired
	private PreferenciasHolder preferenciasHolder;
			
	@Override
	public void onApplicationEvent(AbrirNavegadorEvent event) {		
		LOGGER.debug("Se abrir la siguiente página de navegador", event.getUlr());
			
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URL(event.getUlr()).toURI());
			} catch (Exception e) {
				LOGGER.error("Error al abrir el navegador para ir a la URL {}", event.getUlr(), e);
			}	
		}		
	}

}
