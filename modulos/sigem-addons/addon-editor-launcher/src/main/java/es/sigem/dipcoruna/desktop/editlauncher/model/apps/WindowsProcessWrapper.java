package es.sigem.dipcoruna.desktop.editlauncher.model.apps;

public class WindowsProcessWrapper implements ProcessWrapper {
    private final Process process;
    
    public WindowsProcessWrapper(Process process) {
        this.process = process;
    }
    
    
    @Override
    public void destroy() {
        process.destroy();        
    }

}
