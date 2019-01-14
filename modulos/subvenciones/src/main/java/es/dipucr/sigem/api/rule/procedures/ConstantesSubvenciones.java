package es.dipucr.sigem.api.rule.procedures;


public class ConstantesSubvenciones {
    
    public static final String CAMPO_NUMEXP = "NUMEXP";

   
     /**
     * Mapeo de las tablas
     */
    
     /**
      *    Tabla con los datos de la solicitud
      * Tabla DPCR_SOL_CONV_SUB
      **/
    public final class DatosSolicitud{
        public static final String NOMBRE_TABLA = "DPCR_SOL_CONV_SUB";
        
        public static final String NUMEXP = CAMPO_NUMEXP;
        public static final String FINALIDAD = "FINALIDAD";
        public static final String PRESUPUESTO = "PRESUPUESTO";
        public static final String SUBVENCION = "SUBVENCION";
        
        private DatosSolicitud(){
        }
    }
    
    /**
      *    Tabla con los datos de la resolución de la solicitud
      * Tabla DPCR_RESOL_SOL_CONV_SUB
      **/
    public final class DatosResolucion{
        public static final String NOMBRE_TABLA = "DPCR_RESOL_SOL_CONV_SUB";
        
        public static final String NUMEXP = CAMPO_NUMEXP;
        public static final String FINALIDAD = "FINALIDAD";
        public static final String IMPORTE = "IMPORTE";
        public static final String IMPORTE2 = "IMPORTE2";
        public static final String IMPORTE3 = "IMPORTE3";
        
        public static final String DEVOLUCION = "DEVOLUCION";
        
        public static final String PROYECTO1 = "PROYECTO1";
        public static final String PUNTOSPROYECTO1 = "PUNTOSPROYECTO1";
        public static final String IMPORTEPROYECTO1 = "IMPORTEPROYECTO1";
        public static final String JUSTIF_PROY_1 = "JUSTIF_PROY_1";
        public static final String FECHA1 = "FECHA1";
        
        public static final String PROYECTO2 = "PROYECTO2";
        public static final String PUNTOSPROYECTO2 = "PUNTOSPROYECTO2";
        public static final String IMPORTEPROYECTO2 = "IMPORTEPROYECTO2";
        public static final String JUSTIF_PROY_2 = "JUSTIF_PROY_2";
        public static final String DEVOLUCION2 = "DEVOLUCION2";
        public static final String FECHA2 = "FECHA2";
        
        public static final String PROYECTO3 = "PROYECTO3";
        public static final String PUNTOSPROYECTO3 = "PUNTOSPROYECTO3";
        public static final String IMPORTEPROYECTO3 = "IMPORTEPROYECTO3";
        public static final String JUSTIF_PROY_3 = "JUSTIF_PROY_3";
        public static final String DEVOLUCION3 = "DEVOLUCION3";
        public static final String FECHA3 = "FECHA3";

        public static final String MOTIVO_RECHAZO = "MOTIVO_RECHAZO";
        public static final String MOTIVO_RECHAZO2 = "MOTIVO_RECHAZO2";
        public static final String MOTIVO_RECHAZO3 = "MOTIVO_RECHAZO3";

        public static final String NUMESCUELAS = "NUMESCUELAS";
        public static final String ESCUELAS = "ESCUELAS";
        
        public static final String TALLERES_EDU = "TALLERES_EDU";
        public static final String MOT_RECHAZO_TALLERES_IGU = "MOT_RECHAZO_TALLERES_IGU";
        public static final String ACTIVIDADES_INTERCULT = "ACTIVIDADES_INTERCULT";
        public static final String MOT_RECHAZO_ACTIVIDAD_INTERCUL = "MOT_RECHAZO_ACTIVIDAD_INTERCUL";
        public static final String TALLERES_BULLYING = "TALLERES_BULLYING";
        public static final String MOT_RECHAZO_BULLYING = "MOT_RECHAZO_BULLYING";
        
        public static final String NOMBREGRUPOASOCIACION = "NOMBREGRUPOASOCIACION";
        public static final String CIFGRUPOASOCIACION = "CIFGRUPOASOCIACION";
        
        public static final String NOMBREGRUPOASOCIACION2 = "NOMBREGRUPOASOCIACION2";
        public static final String CIFGRUPOASOCIACION2 = "CIFGRUPOASOCIACION2";
        
        public static final String NOMBREGRUPOASOCIACION3 = "NOMBREGRUPOASOCIACION3";
        public static final String CIFGRUPOASOCIACION3 = "CIFGRUPOASOCIACION3";
        
        public static final String RENUNCIA1 = "RENUNCIA1";
        public static final String RENUNCIA2 = "RENUNCIA2";
        public static final String RENUNCIA3 = "RENUNCIA3";
        
        public static final String MOTIVO_RENUNCIA1 = "MOT_RENUNCIA1";
        public static final String MOTIVO_RENUNCIA2 = "MOT_RENUNCIA2";
        public static final String MOTIVO_RENUNCIA3 = "MOT_RENUNCIA3";
        
        public static final String MODALIDAD1 = "MODALIDAD1";
        public static final String MODALIDAD2 = "MODALIDAD2";
        public static final String MODALIDAD3 = "MODALIDAD3";

        private DatosResolucion(){
        }
    }
    
    public final class DatosCulturalProvincial{
        public static final String NOMBRE_TABLA = "DPCR_DATOS_CONV_CULT_PROV";
        
        public static final String TEXTO_MODIFICADO1 = "TEXTO_MODIFICADO1";
        public static final String TEXTO_CACHE_CANT1 = "TEXTO_CACHE_CANT1";
        public static final String TEXTO_PAGO_AYTO11 = "TEXTO_PAGO_AYTO11";
        public static final String TEXTO_PAGO_AYTO12 = "TEXTO_PAGO_AYTO12";
        public static final String TEXTO_PAGO_AYTO13 = "TEXTO_PAGO_AYTO13";
        public static final String TEXTO_PAGO_DIPU11 = "TEXTO_PAGO_DIPU11";
        public static final String TEXTO_PAGO_DIPU12 = "TEXTO_PAGO_DIPU12";
        public static final String TEXTO_PAGO_DIPU13 = "TEXTO_PAGO_DIPU13";
        
        public static final String TEXTO_MODIFICADO2 = "TEXTO_MODIFICADO2";
        public static final String TEXTO_CACHE_CANT2 = "TEXTO_CACHE_CANT2";
        public static final String TEXTO_PAGO_AYTO21 = "TEXTO_PAGO_AYTO21";
        public static final String TEXTO_PAGO_AYTO22 = "TEXTO_PAGO_AYTO22";
        public static final String TEXTO_PAGO_AYTO23 = "TEXTO_PAGO_AYTO23";
        public static final String TEXTO_PAGO_DIPU21 = "TEXTO_PAGO_DIPU21";
        public static final String TEXTO_PAGO_DIPU22 = "TEXTO_PAGO_DIPU22";
        public static final String TEXTO_PAGO_DIPU23 = "TEXTO_PAGO_DIPU23";
        
        public static final String TEXTO_MODIFICADO3 = "TEXTO_MODIFICADO3";
        public static final String TEXTO_CACHE_CANT3 = "TEXTO_CACHE_CANT3";
        public static final String TEXTO_PAGO_AYTO31 = "TEXTO_PAGO_AYTO31";
        public static final String TEXTO_PAGO_AYTO32 = "TEXTO_PAGO_AYTO32";
        public static final String TEXTO_PAGO_AYTO33 = "TEXTO_PAGO_AYTO33";
        public static final String TEXTO_PAGO_DIPU31 = "TEXTO_PAGO_DIPU31";
        public static final String TEXTO_PAGO_DIPU32 = "TEXTO_PAGO_DIPU32";
        public static final String TEXTO_PAGO_DIPU33 = "TEXTO_PAGO_DIPU33";
        
        private DatosCulturalProvincial(){
        }
    }
    
    public final class MunicipiosValidationTable extends ValidationTable{
        public static final String NOMBRE_TABLA = "REC_VLDTBL_MUNICIPIOS";
    }
    
    public class ValidationTable{

        public static final String VALOR = "VALOR";
        public static final String SUSTITUTO = "SUSTITUTO";
        
        private ValidationTable(){
        }
    }
    
    public final class DatosEspecificosOtrosDatos{
        public static final String PROPIEDAD_MODALIDAD = "MODALIDAD";
        
        private DatosEspecificosOtrosDatos(){
        }
    }
    
    public final class VariablesSesion{
        
        public static final String NRESOLUCIONPARCIAL = "NRESOLUCIONPARCIAL";
        public static final String ANIO = "ANIO";
        public static final String NOMBRE_TRAMITE = "NOMBRE_TRAMITE";
        public static final String NUMEXPSOLICITUD = "NUMEXPSOLICITUD";
        public static final String TEXTO_RESOLUCION = "TEXTO_RESOLUCION";
        public static final String TEXTO_RESOLUCION2 = "TEXTO_RESOLUCION2";
        public static final String TEXTO_RESOLUCION3 = "TEXTO_RESOLUCION3";
        public static final String EXTRACTO_DECRETO = "EXTRACTO_DECRETO";
        
        public static final String NUM_DECRETO_APRB = "NUM_DECRETO_APRB";
        public static final String FECHA_DECRETO_APRB = "FECHA_DECRETO_APRB";
        public static final String NUM_DECRETO = "NUM_DECRETO";
        public static final String FECHA_DECRETO = "FECHA_DECRETO";
        public static final String NUM_BOLETIN = "NUM_BOLETIN";
        public static final String FECHA_BOLETIN = "FECHA_BOLETIN";
        public static final String NUM_INFORME = "NUM_INFORME";
        public static final String CONVOCATORIA = "CONVOCATORIA";
        
        public static final String DATOSSOLICITUD = "DATOSSOLICITUD";
        public static final String TEXTO_ALTERNATIVO = "TEXTO_ALTERNATIVO";
        public static final String TEXTO_ALTERNATIVO2 = "TEXTO_ALTERNATIVO2";
        public static final String TEXTO_ALTERNATIVO3 = "TEXTO_ALTERNATIVO3";
        public static final String TEXTO_ALTERNATIVO4 = "TEXTO_ALTERNATIVO4";
        public static final String TEXTO_ALTERNATIVO5 = "TEXTO_ALTERNATIVO5";
        public static final String TEXTO_ALTERNATIVO6 = "TEXTO_ALTERNATIVO6";
        public static final String TEXTO_ALTERNATIVOPUNTOTERCERO = "TEXTO_ALTERNATIVOPUNTOTERCERO";
        public static final String TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO = "TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO";
        public static final String TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO2 = "TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO2";
                
        public static final String CABECERA = "CABECERA";
        public static final String TEXTO_MODIFICACION = "TEXTO_MODIFICACION";
        public static final String TEXTO_CACHE = "TEXTO_CACHE";
        public static final String TEXTOCACHECANT = "textoCacheCant";
        public static final String TEXTOPAGOAYTO = "textoPagoAyto";
        public static final String TEXTOPAGOAYTO1 = "textoPagoAyto1";
        public static final String TEXTOPAGOAYTO2 = "textoPagoAyto2";
        public static final String TEXTOPAGOAYTO3 = "textoPagoAyto3";
        public static final String TEXTOPAGODIPU = "textoPagoDipu";
        public static final String TEXTOPAGODIPU1 = "textoPagoDipu1";
        public static final String TEXTOPAGODIPU2 = "textoPagoDipu2";
        public static final String TEXTOPAGODIPU3 = "textoPagoDipu3";
        
        public static final String IMPORTE = "IMPORTE";
        public static final String TEXTO1 = "TEXTO1";
        public static final String TEXTO2 = "TEXTO2";
        public static final String TEXTO3 = "TEXTO3";
        public static final String TEXTO4 = "TEXTO4";
        public static final String TEXTO5 = "TEXTO5";
        public static final String TEXTO6 = "TEXTO6";
        public static final String TEXTO7 = "TEXTO7";
        public static final String TEXTO8 = "TEXTO8";
        public static final String TEXTO9 = "TEXTO9";
        
        private VariablesSesion(){
        }
    }
    
    private ConstantesSubvenciones(){
    }
}
