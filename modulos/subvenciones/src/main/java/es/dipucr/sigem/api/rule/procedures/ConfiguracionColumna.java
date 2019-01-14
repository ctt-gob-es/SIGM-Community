package es.dipucr.sigem.api.rule.procedures;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class ConfiguracionColumna {
    
    private String tablaDato;
    private String nombreColumna;
    private double ancho;
    private String formatter;
    
    private Map<String, String> mapeoCabeceras = null;

    public ConfiguracionColumna(){
        tablaDato = "";
        nombreColumna = "";
        ancho = 0;
        formatter = "";
    }    

    public String getTablaDato() {
        return tablaDato;
    }

    public void setTablaDato(String tablaDato) {
        this.tablaDato = tablaDato;
    }
    
    public String getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    /**
     * 
     * @return
     */
    public String getFormatter() {
        String formato = "";
        
        if(ConstantesString.FORMATO_IMPORTE.equalsIgnoreCase(formatter)){
            formato = ConstantesString.FORMATO_IMPORTE;
        } else if(ConstantesString.FORMATO_PUNTOS.equalsIgnoreCase(formatter)){
            formato = ConstantesString.FORMATO_PUNTOS;
        } else if(ConstantesString.FORMATO_FECHA_DD_MM_YYYY.equalsIgnoreCase(formatter)){
            formato = ConstantesString.FORMATO_FECHA_DD_MM_YYYY;
        }
        
        return formato;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }
    
    public boolean tieneDistribucion(){
        return 0 < ancho;
    }
    
    public Map<String, String> getMapeoCabeceras() {
        if(null == mapeoCabeceras){
            initMapeoCabeceras();
        }        
        return mapeoCabeceras;
    }
    
    private void initMapeoCabeceras(){
        mapeoCabeceras = new HashMap<String, String>();
        
        mapeoCabeceras.put("EXP.NUMEXP", ConstantesString.CabeceraTabla.NUMEXP);
        mapeoCabeceras.put("EXP.NREG", ConstantesString.CabeceraTabla.NUM_REGISTRO);
        mapeoCabeceras.put("EXP.IDENTIDADTITULAR", ConstantesString.CabeceraTabla.SOLICITANTE);
        mapeoCabeceras.put("EXP.AYUNTAMIENTO", ConstantesString.CabeceraTabla.AYUNTAMIENTO);
        mapeoCabeceras.put("EXP.ASOCIACION", ConstantesString.CabeceraTabla.ASOCIACIONES);        
        mapeoCabeceras.put("EXP.NIFCIFTITULAR", ConstantesString.CabeceraTabla.CIF);
        
        mapeoCabeceras.put("SOL.NUMEXP", ConstantesString.CabeceraTabla.NUMEXP);
        mapeoCabeceras.put("SOL.FINALIDAD", ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
        mapeoCabeceras.put("SOL.PRESUPUESTO", ConstantesString.CabeceraTabla.PRESUPUESTO);
        mapeoCabeceras.put("SOL.SUBVENCION", ConstantesString.CabeceraTabla.SUBVENCION);
        mapeoCabeceras.put("SOL.FECHA", ConstantesString.CabeceraTabla.FECHA);
        
        mapeoCabeceras.put("RESOL.NUMEXP", ConstantesString.CabeceraTabla.NUMEXP);
        mapeoCabeceras.put("RESOL.FINALIDAD", ConstantesString.CabeceraTabla.FINALIDAD);
        
        mapeoCabeceras.put("RESOL.CIFGRUPOASOCIACION", ConstantesString.CabeceraTabla.CIFGRUPOASOCIACION);
        mapeoCabeceras.put("RESOL.NOMBREGRUPOASOCIACION", ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
        mapeoCabeceras.put("RESOL.PROYECTO1", ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
        mapeoCabeceras.put("RESOL.IMPORTE", ConstantesString.CabeceraTabla.IMPORTE);
        mapeoCabeceras.put("RESOL.PUNTOSPROYECTO1", ConstantesString.CabeceraTabla.PUNTOS);
        mapeoCabeceras.put("RESOL.IMPORTEPROYECTO1", ConstantesString.CabeceraTabla.IMPORTE);        
        mapeoCabeceras.put("RESOL.FECHA1", ConstantesString.CabeceraTabla.FECHA);
        mapeoCabeceras.put("RESOL.MODALIDAD1", ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA);
        mapeoCabeceras.put("RESOL.DEVOLUCION", ConstantesString.CabeceraTabla.DEVOLUCION);
        mapeoCabeceras.put("RESOL.MOTIVO_RECHAZO", ConstantesString.CabeceraTabla.MOTIVO_RECHAZO);
        mapeoCabeceras.put("RESOL.MOT_RENUNCIA1", ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA);
        
        mapeoCabeceras.put("RESOL.CIFGRUPOASOCIACION2", ConstantesString.CabeceraTabla.CIFGRUPOASOCIACION);
        mapeoCabeceras.put("RESOL.NOMBREGRUPOASOCIACION2", ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
        mapeoCabeceras.put("RESOL.PROYECTO2", ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
        mapeoCabeceras.put("RESOL.IMPORTE2", ConstantesString.CabeceraTabla.IMPORTE);
        mapeoCabeceras.put("RESOL.PUNTOSPROYECTO2", ConstantesString.CabeceraTabla.PUNTOS);
        mapeoCabeceras.put("RESOL.IMPORTEPROYECTO2", ConstantesString.CabeceraTabla.IMPORTE);        
        mapeoCabeceras.put("RESOL.FECHA2", ConstantesString.CabeceraTabla.FECHA);
        mapeoCabeceras.put("RESOL.MODALIDAD2", ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA);
        mapeoCabeceras.put("RESOL.DEVOLUCION2", ConstantesString.CabeceraTabla.DEVOLUCION);
        mapeoCabeceras.put("RESOL.MOTIVO_RECHAZO2", ConstantesString.CabeceraTabla.MOTIVO_RECHAZO);
        mapeoCabeceras.put("RESOL.MOT_RENUNCIA2", ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA);
        
        mapeoCabeceras.put("RESOL.CIFGRUPOASOCIACION3", ConstantesString.CabeceraTabla.CIFGRUPOASOCIACION);
        mapeoCabeceras.put("RESOL.NOMBREGRUPOASOCIACION3", ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
        mapeoCabeceras.put("RESOL.PROYECTO3", ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
        mapeoCabeceras.put("RESOL.IMPORTE3", ConstantesString.CabeceraTabla.IMPORTE);
        mapeoCabeceras.put("RESOL.PUNTOSPROYECTO3", ConstantesString.CabeceraTabla.PUNTOS);
        mapeoCabeceras.put("RESOL.IMPORTEPROYECTO3", ConstantesString.CabeceraTabla.IMPORTE);        
        mapeoCabeceras.put("RESOL.FECHA3", ConstantesString.CabeceraTabla.FECHA);
        mapeoCabeceras.put("RESOL.MODALIDAD3", ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA);
        mapeoCabeceras.put("RESOL.DEVOLUCION3", ConstantesString.CabeceraTabla.DEVOLUCION);
        mapeoCabeceras.put("RESOL.MOTIVO_RECHAZO3", ConstantesString.CabeceraTabla.MOTIVO_RECHAZO);
        mapeoCabeceras.put("RESOL.MOT_RENUNCIA3", ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA);
        
        mapeoCabeceras.put("RESOL.TALLERES_EDU", ConstantesString.CabeceraTabla.N_TALLERES_EDU);
        mapeoCabeceras.put("RESOL.MOT_RECHAZO_TALLERES_IGU", ConstantesString.CabeceraTabla.MOTIVO_RECHAZO);
        mapeoCabeceras.put("RESOL.ACTIVIDADES_INTERCULT", ConstantesString.CabeceraTabla.N_ACTIVIDADES_INTERCULT);
        mapeoCabeceras.put("RESOL.MOT_RECHAZO_ACTIVIDAD_INTERCUL", ConstantesString.CabeceraTabla.MOTIVO_RECHAZO);
        mapeoCabeceras.put("RESOL.TALLERES_BULLYING", ConstantesString.CabeceraTabla.N_TALLERES_BULLYNG);
        mapeoCabeceras.put("RESOL.MOT_RECHAZO_BULLYING", ConstantesString.CabeceraTabla.MOTIVO_RECHAZO);
    }
    
    public String getCabeceraColumna() {
        String resultado = getMapeoCabeceras().get(tablaDato + "." + nombreColumna);
        if(StringUtils.isEmpty(resultado)){
            resultado = "";
        }        
        return resultado;
    }
}
