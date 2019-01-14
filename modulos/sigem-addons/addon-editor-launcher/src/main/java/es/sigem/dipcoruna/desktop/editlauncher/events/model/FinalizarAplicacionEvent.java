package es.sigem.dipcoruna.desktop.editlauncher.events.model;

import org.springframework.context.ApplicationEvent;

import es.sigem.dipcoruna.desktop.editlauncher.model.apps.ProcessWrapper;

public class FinalizarAplicacionEvent extends ApplicationEvent {
    private static final long serialVersionUID = -8108499442726805827L;
    private final String filePath;
    private final ProcessWrapper processWrapper;

    public FinalizarAplicacionEvent(final Object source, ProcessWrapper processWrapper, final String filePath) {
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
