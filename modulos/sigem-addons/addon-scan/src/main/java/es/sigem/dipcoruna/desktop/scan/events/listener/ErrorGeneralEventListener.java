package es.sigem.dipcoruna.desktop.scan.events.listener;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.scan.events.model.ErrorGeneralEvent;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("errorGeneralEventListener")
public class ErrorGeneralEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorGeneralEventListener.class);

	@Autowired
    private SimpleMessageSource messageSource;


	@EventListener
	public void onApplicationEvent(final ErrorGeneralEvent event) {
		LOGGER.error("Se ha detectado un evento de error {}", event.getMensaje());
		JOptionPane.showMessageDialog(null,
		        messageSource.getMessage(event.getMensaje(), event.getArgs()),
		        messageSource.getMessage(event.getTitulo()),
				JOptionPane.ERROR_MESSAGE
		);
	}

}
