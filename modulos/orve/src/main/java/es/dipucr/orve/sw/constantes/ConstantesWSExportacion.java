package es.dipucr.orve.sw.constantes;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dipucr.metadatos.diccionarios.EstadosElaboracion;

public class ConstantesWSExportacion {

    public static final String POOL_NAME_REGISTRO = "java:comp/env/jdbc/registroDS_{0}";
    public static final String POOL_NAME_TRAMTIADOR = "java:comp/env/jdbc/tramitadorDS_{0}";
    
     /**
     * Constantes de los valores de los parámetros del SWExportacion de ORVE
     */
    public static final String FORMATO_FECHA_ORVE_ENVIO = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATO_FECHA_ORVE_RECEPCION = "yyyyMMddHHmmss";
    
    public static final String ID_TIPO_TRANSPORTE_DEFECTO = "03";
    public static final String COD_TIPO_TRANSPORTE_DEFECTO = "ORVE";

    public static final String COD_OFICINA_DEFECTO = "998";
    public static final String ACRON_OFICINA_DEFECTO = "ORVE";
    
    public static final String COD_TIPO_ASUNTO_DEFECTO = "ORVE";
    
    public static final int UNIDAD_TIEMPO_RESTAR = Calendar.MINUTE;
    //Para restar el número debe ser negativo
    public static final int CANTIDAD_TIEMPO_RESTAR = -3; 
    
    public static final String IMAGEN_CORREO_ERROR = "logoCabecera.png";
    
     /**
      * Filtro Estado
      **/
    public final class FiltroEstado{
        public static final String PENDIENTES_ENVIO = "PE";
        public static final String ENVIADOS_PENDIENTES_CONFIRMACION = "EPC";
        public static final String ENVIADOS_RECHAZADOS = "ER";
        public static final String ENVIADOS_CONFIRMADOS = "EC";
        public static final String ENVIADOS_REENVIADOS = "EREEN";
        public static final String RECIBIDOS_PENDIENTES_CONFIRMACION = "RPC";
        public static final String RECIBIDOS_RECHAZADOS = "RR";
        public static final String RECIBIDOS_CONFIRMADOS = "RC";
        public static final String RECIBIDOS_REENVIADOS = "RREEN";
        
        private FiltroEstado(){
        }
    }
    
    /**
     * Filtro Estado
     **/
    public final class FiltroHistorico{
        public static final String DESACTIVADO = "0";
        public static final String ACTIVADO = "1";
        
        private FiltroHistorico(){
        }
    }
    
    public static final Map<String, String> RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES = new HashMap<String, String>();
    static{
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_EXITO, ObtenerIdentificadoresMensajesSW.MENSAJE_EXITO);
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_SIN_RESULTADOS, ObtenerIdentificadoresMensajesSW.MENSAJE_SIN_RESULTADOS);
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_ERROR_CAMPO_OBLIGATORIO, ObtenerIdentificadoresMensajesSW.MENSAJE_ERROR_CAMPO_OBLIGATORIO);
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_ERROR_ESTADO_INVALIDO, ObtenerIdentificadoresMensajesSW.MENSAJE_ERROR_ESTADO_INVALIDO);
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_ERROR_OFICINA_INVALIDA, ObtenerIdentificadoresMensajesSW.MENSAJE_ERROR_OFICINA_INVALIDA);
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_ERROR_UNIDAD_INVALIDA, ObtenerIdentificadoresMensajesSW.MENSAJE_ERROR_UNIDAD_INVALIDA);
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_ERROR_FORMATO_FECHA_INVALIDO, ObtenerIdentificadoresMensajesSW.MENSAJE_ERROR_FORMATO_FECHA_INVALIDO);
        RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.put(ObtenerIdentificadoresMensajesSW.CODIGO_ERROR_INTERVALO_FECHAS_INVALIDO, ObtenerIdentificadoresMensajesSW.MENSAJE_ERROR_INTERVALO_FECHAS_INVALIDO);
    }    
    
    public static final Map<String, String> RESULTADO_GET_REGISTRO_CODIGO_VALORES = new HashMap<String, String>();
    static{
        RESULTADO_GET_REGISTRO_CODIGO_VALORES.put(ObtenerRegistroMensajesSW.CODIGO_EXITO, ObtenerRegistroMensajesSW.MENSAJE_EXITO);
        RESULTADO_GET_REGISTRO_CODIGO_VALORES.put(ObtenerRegistroMensajesSW.CODIGO_NO_ENCONTRADO, ObtenerRegistroMensajesSW.MENSAJE_NO_ENCONTRADO);
        RESULTADO_GET_REGISTRO_CODIGO_VALORES.put(ObtenerRegistroMensajesSW.CODIGO_ERROR, ObtenerRegistroMensajesSW.MENSAJE_ERROR);
    } 
    
    public static final Map<String, String> MAPEO_VALIDEZ_ORVE_ESTADO_ELABORACION_NTI = new HashMap<String, String>();
    static{
        MAPEO_VALIDEZ_ORVE_ESTADO_ELABORACION_NTI.put("01", EstadosElaboracion.COPIA_PARCIAL_AUTENTICA);
        MAPEO_VALIDEZ_ORVE_ESTADO_ELABORACION_NTI.put("02", EstadosElaboracion.OTROS);
        MAPEO_VALIDEZ_ORVE_ESTADO_ELABORACION_NTI.put("03", EstadosElaboracion.COPIA_AUTENTICA_DOC_PAPEL);
        MAPEO_VALIDEZ_ORVE_ESTADO_ELABORACION_NTI.put("04", EstadosElaboracion.ORIGINAL);
    }
    
    private ConstantesWSExportacion(){
    }
}
