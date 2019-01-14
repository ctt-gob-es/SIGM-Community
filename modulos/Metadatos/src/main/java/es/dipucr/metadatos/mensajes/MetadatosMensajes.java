package es.dipucr.metadatos.mensajes;

public class MetadatosMensajes {

    public static final String ERROR_RECUPERAR_METADATOS = "ERROR al recuperar los metadatos. ";
    public static final String ERROR_GUARDAR_METADATOS = "ERROR al guardar los metadatos. ";

    public static final String ERROR_ACTUALIZAR_DOCUMENTO = "ERROR al cambiar el documento al que están asociados los metadatos. ";

    private static final String ERROR_GUARDAR_METADATO = "ERROR al guardar el metadato: %s con el valor: %s. ";
    private static final String ERROR_ACTUALIZAR_METADATO = "ERROR al actualizar el metadato: %s al valor: %s. ";
    private static final String ERROR_BORRAR_METADATO = "ERROR al borrar el metadato: %s del documento: %s . ";
    private static final String ERROR_RECUPERAR_DIR3 = "ERROR al recuperar el código DIR3 de la entidad: %s. ";

    public static String getMensajeErrorGuardarMetadato(String nombreMetadato, String valorMetadato){
        return String.format(ERROR_GUARDAR_METADATO, nombreMetadato, valorMetadato);
    }

    public static String getMensajeErrorActualizarMetadato(String nombreMetadato, String valorNuevo){
        return String.format(ERROR_ACTUALIZAR_METADATO, nombreMetadato, valorNuevo);
    }

    public static String getMensajeErrorBorrarMetadato(String nombreMetadato, String identificadorDocumento){
        return String.format(ERROR_BORRAR_METADATO, nombreMetadato, identificadorDocumento);
    }

    public static String getMensajeErrorRecuperarDIR3(String idEntidad){
        return String.format(ERROR_RECUPERAR_DIR3, idEntidad);
    }

    private MetadatosMensajes(){
    }
}
