package es.sigem.dipcoruna.desktop.scan.events.listener.visor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.scan.events.model.visor.MostrarVisorFicheroEvent;
import es.sigem.dipcoruna.desktop.scan.ui.VisorPdf;

@Component("visorFicherosUIListener")
public class VisorFicherosUIListener  {
	private static final Logger LOGGER = LoggerFactory.getLogger(VisorFicherosUIListener.class);

	@EventListener
	public void onApplicationEvent(final MostrarVisorFicheroEvent event) {
		LOGGER.debug("Detectado evento {}", event);

		final VisorPdf visorPdf = new VisorPdf();
		visorPdf.verPdf(event.getFile().getAbsolutePath());
		visorPdf.setVisible(true);
	}

}
