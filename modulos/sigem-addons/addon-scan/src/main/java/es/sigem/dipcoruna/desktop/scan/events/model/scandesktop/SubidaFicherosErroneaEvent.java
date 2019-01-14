package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class SubidaFicherosErroneaEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4596378514466536960L;

    private final List<String> erroresServidor;

    public SubidaFicherosErroneaEvent(final Object source, final List<String> erroresServidor) {
        super(source);
        this.erroresServidor = erroresServidor;
    }

    public List<String> getErroresServidor() {
        return erroresServidor;
    }
}
