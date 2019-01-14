package es.sigem.dipcoruna.desktop.scan.ui.model;

import java.io.File;

import javax.swing.JCheckBox;

public class FileJCheckBox extends JCheckBox {
    private static final long serialVersionUID = 344541723411667462L;
    
    private final File file;
    
    public FileJCheckBox(final File file) {
        this.file = file;
    }
    
    public File getFile() {
        return file;
    }              
}