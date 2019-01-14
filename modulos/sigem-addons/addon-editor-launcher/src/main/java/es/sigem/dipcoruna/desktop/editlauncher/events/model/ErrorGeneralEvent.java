package es.sigem.dipcoruna.desktop.editlauncher.events.model;

import org.springframework.context.ApplicationEvent;

public class ErrorGeneralEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1523944631703927006L;

    private final String keyTitulo;
    private final String keyMensaje;
    private final String[] paramsMensaje;
    private boolean esIrrecuperable;

    

    public static ErrorGeneralEvent buildErrorIrrecuperable(final Object source, final String keyTitulo, final String keyMensaje, final String[] paramsMensaje) {
        ErrorGeneralEvent event = new ErrorGeneralEvent(source, keyTitulo, keyMensaje, new String[] {});
        event.esIrrecuperable = true;
        return event;
    }
               

    public static ErrorGeneralEvent buildErrorRecuperable(final Object source, final String keyTitulo, final String keyMensaje, final String[] paramsMensaje) {
        ErrorGeneralEvent event = new ErrorGeneralEvent(source, keyTitulo, keyMensaje, new String[] {});
        event.esIrrecuperable = false;
        return event;
    }
    
        
    private ErrorGeneralEvent(final Object source, final String keyTitulo, final String keyMensaje, final String[] paramsMensaje) {
        super(source);
        this.keyTitulo = keyTitulo;
        this.keyMensaje = keyMensaje;
        this.paramsMensaje = paramsMensaje;
    }

    public String getKeyTitulo() {
        return keyTitulo;
    }

    public String getKeyMensaje() {
        return keyMensaje;
    }

    public String[] getParamsMensaje() {
        return paramsMensaje;
    }


    public boolean isEsIrrecuperable() {
        return esIrrecuperable;
    }        
    
}
