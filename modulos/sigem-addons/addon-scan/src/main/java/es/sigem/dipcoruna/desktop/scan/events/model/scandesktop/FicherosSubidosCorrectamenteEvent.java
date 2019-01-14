package es.sigem.dipcoruna.desktop.scan.events.model.scandesktop;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class FicherosSubidosCorrectamenteEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4596378514466536960L;
    private final List<String> ficherosSubidos;
    private final List<String> ficherosErroneos;


    public FicherosSubidosCorrectamenteEvent(final Object source, final List<String> ficherosSubidos, final List<String> ficherosErroneos) {
        super(source);
        this.ficherosSubidos = ficherosSubidos;
        this.ficherosErroneos = ficherosErroneos;
    }


    public List<String> getFicherosSubidos() {
        return ficherosSubidos;
    }

    public List<String> getFicherosErroneos() {
        return ficherosErroneos;
    }
}
