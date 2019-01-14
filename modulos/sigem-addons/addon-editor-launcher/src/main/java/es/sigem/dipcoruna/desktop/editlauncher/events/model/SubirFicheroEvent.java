package es.sigem.dipcoruna.desktop.editlauncher.events.model;

import org.springframework.context.ApplicationEvent;

public class SubirFicheroEvent extends ApplicationEvent {
    private static final long serialVersionUID = -8108499442726805827L;
    private final String filePath;

    public SubirFicheroEvent(final Object source, final String filePath) {
        super(source);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
