package es.sigem.dipcoruna.desktop.editlauncher.events.listener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.editlauncher.events.model.ErrorGeneralEvent;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("errorGeneralEventListener")
public class ErrorGeneralEventListener implements ApplicationListener<ErrorGeneralEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorGeneralEventListener.class);
			
	@Autowired
	private SimpleMessageSource messageSource;
			
	
	@Override
	public void onApplicationEvent(ErrorGeneralEvent event) {
		LOGGER.error("Se ha detectado un evento de error {}. Irrecuperable: {}", event.getKeyMensaje(), event.isEsIrrecuperable());
		
		JOptionPane.showMessageDialog(new JFrame(),
		        messageSource.getMessage(event.getKeyMensaje(), event.getParamsMensaje()),
		        messageSource.getMessage(event.getKeyTitulo()),		        
				JOptionPane.ERROR_MESSAGE
		);
		
		if (event.isEsIrrecuperable()) {
		    LOGGER.error("Error irrecuperable. Saliendo del programa");
		    System.exit(-1);
		}		
	}	
}
