package es.sigem.dipcoruna.desktop.editlauncher.events.model;

import org.springframework.context.ApplicationEvent;

import es.sigem.dipcoruna.desktop.editlauncher.model.apps.ProcessWrapper;

public class LanzadoEditorEvent extends ApplicationEvent {
    private static final long serialVersionUID = -2464620822902175318L;
    private final ProcessWrapper processWrapper;
    private final String filePath;
    

    public LanzadoEditorEvent(final Object source, final ProcessWrapper processWrapper, final String filePath) {
        super(source);
        this.processWrapper = processWrapper;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public ProcessWrapper getProcessWrapper() {
        return processWrapper;
    }
}