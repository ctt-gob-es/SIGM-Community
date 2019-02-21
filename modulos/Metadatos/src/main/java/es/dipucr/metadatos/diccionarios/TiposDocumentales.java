package es.dipucr.metadatos.diccionarios;

import java.util.HashMap;
import java.util.Map;

public class TiposDocumentales{
    
    public static final String RESOLUCION = "TD01 - Resolución."; 
    public static final String ACUERDO = "TD02 - Acuerdo.";
    public static final String CONTRATO = "TD03 - Contrato.";
    public static final String CONVENIO = "TD04 - Convenio.";
    public static final String DECLARACION = "TD05 - Declaración.";
    public static final String COMUNICACION = "TD06 - Comunicación.";
    public static final String NOTIFICACION = "TD07 - Notificación.";
    public static final String PUBLICACION = "TD08 - Publicación.";
    public static final String ACUSE_DE_RECIBO = "TD09 - Acuse de recibo.";
    public static final String ACTA = "TD10 - Acta.";
    public static final String CERTIFICADO = "TD11 - Certificado.";
    public static final String DILIGENCIA = "TD12 - Diligencia.";
    public static final String INFORME = "TD13 - Informe.";
    public static final String SOLICITUD = "TD14 - Solicitud.";
    public static final String DENUNCIA = "TD15 - Denuncia.";
    public static final String ALEGACION = "TD16 - Alegación.";
    public static final String RECURSO = "TD17 - Recursos.";
    public static final String COMUNICACION_CIUDADANO = "TD18 - Comunicación ciudadano.";
    public static final String FACTURA = "TD19 - Factura.";
    public static final String OTROS_INCAUTADOS = "TD20 - Otros incautados.";
    
    public static final String COD_TP_DOC_RESOLUCION = "TD01-ENI";
    public static final String COD_TP_DOC_ACUERDO = "TD02-ENI";
    public static final String COD_TP_DOC_CONTRATO = "TD03-ENI";
    public static final String COD_TP_DOC_CONVENIO = "TD04-ENI";
    public static final String COD_TP_DOC_DECLARACION = "TD05-ENI";
    public static final String COD_TP_DOC_COMUNICACION = "TD06-ENI";
    public static final String COD_TP_DOC_NOTIFICACION = "TD07-ENI";
    public static final String COD_TP_DOC_PUBLICACION = "TD08-ENI";
    public static final String COD_TP_DOC_ACUSE_DE_RECIBO = "TD09-ENI";
    public static final String COD_TP_DOC_ACTA = "TD10-ENI";
    public static final String COD_TP_DOC_CERTIFICADO = "TD11-ENI";
    public static final String COD_TP_DOC_DILIGENCIA = "TD12-ENI";
    public static final String COD_TP_DOC_INFORME = "TD13-ENI";
    public static final String COD_TP_DOC_SOLICITUD = "TD14-ENI";
    public static final String COD_TP_DOC_DENUNCIA = "TD15-ENI";
    public static final String COD_TP_DOC_ALEGACION = "TD16-ENI";
    public static final String COD_TP_DOC_RECURSO = "TD17-ENI";
    public static final String COD_TP_DOC_COMUNICACION_CIUDADANO = "TD18-ENI";
    public static final String COD_TP_DOC_FACTURA = "TD19-ENI";
    public static final String COD_TP_DOC_OTROS_INCAUTADOS = "TD20-ENI";

    
    public static final Map<String, String> CODS_TIPOS_DOCS = new HashMap<String, String>();
    static{
    	CODS_TIPOS_DOCS.put( TiposDocumentales.RESOLUCION, TiposDocumentales.COD_TP_DOC_RESOLUCION);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.ACUERDO, TiposDocumentales.COD_TP_DOC_ACUERDO);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.CONTRATO, TiposDocumentales.COD_TP_DOC_CONTRATO);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.CONVENIO, TiposDocumentales.COD_TP_DOC_CONVENIO);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.DECLARACION, TiposDocumentales.COD_TP_DOC_DECLARACION);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.COMUNICACION, TiposDocumentales.COD_TP_DOC_COMUNICACION);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.NOTIFICACION, TiposDocumentales.COD_TP_DOC_NOTIFICACION);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.PUBLICACION, TiposDocumentales.COD_TP_DOC_PUBLICACION);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.ACUSE_DE_RECIBO, TiposDocumentales.COD_TP_DOC_ACUSE_DE_RECIBO);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.ACTA, TiposDocumentales.COD_TP_DOC_ACTA);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.CERTIFICADO, TiposDocumentales.COD_TP_DOC_CERTIFICADO);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.DILIGENCIA, TiposDocumentales.COD_TP_DOC_DILIGENCIA);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.INFORME, TiposDocumentales.COD_TP_DOC_INFORME);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.SOLICITUD, TiposDocumentales.COD_TP_DOC_SOLICITUD);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.DENUNCIA, TiposDocumentales.COD_TP_DOC_DENUNCIA);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.ALEGACION, TiposDocumentales.COD_TP_DOC_ALEGACION);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.RECURSO, TiposDocumentales.COD_TP_DOC_RECURSO);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.COMUNICACION_CIUDADANO, TiposDocumentales.COD_TP_DOC_COMUNICACION_CIUDADANO);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.FACTURA, TiposDocumentales.COD_TP_DOC_FACTURA);
    	CODS_TIPOS_DOCS.put( TiposDocumentales.OTROS_INCAUTADOS, TiposDocumentales.COD_TP_DOC_OTROS_INCAUTADOS);
    }
    
    private TiposDocumentales(){
    }
}
