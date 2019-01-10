package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrGeneraNotificacionesConvocatoriasSubAsociaciones extends DipucrGeneraNotificacionesConvocatoriasSub {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesConvocatoriasSubAsociaciones.class);
    
    public void inicializaCampos(){
        entidadCabecera = ConstantesString.CabeceraTabla.ASOCIACIONES;
    }
}