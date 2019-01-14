package es.sigem.dipcoruna.desktop.editlauncher.service;

public interface LocalFileService {
    
       
    String descargarDocumento(String urlDocumento);

    void actualizarDocumentoRemoto(String urlDocumento, String pathDocumento);

    void limpiarDocumento(String pathDocumento);
}
