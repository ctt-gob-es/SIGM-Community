package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

public class DipucrGeneraJustificacionConvocatoriasSubEELL extends DipucrAutoGeneraDocIniTramiteRule{

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraJustificacionConvocatoriasSubEELL.class);
    
    public static final double[] DISTRIBUCION_5_COLUMNAS = {30, 15, 25, 15, 15};
    public static final double[] DISTRIBUCION_4_COLUMNAS = {35, 15, 25, 25};
    public static final double[] DISTRIBUCION_3_COLUMNAS = {35, 15, 50};
    
    public static final double[] DISTRIBUCION_6_COLUMNAS_MODALIDAD_CATEGORIA = {30, 10, 25, 15, 10, 10};
    public static final double[] DISTRIBUCION_5_COLUMNAS_MODALIDAD_CATEGORIA = {30, 10, 25, 20, 15};
    public static final double[] DISTRIBUCION_4_COLUMNAS_MODALIDAD_CATEGORIA = {30, 10, 20, 40};
    
    private boolean conModalidadCategoria = false;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            conModalidadCategoria = "TRUE".equalsIgnoreCase(TramitesUtil.getPropiedadDatosEspecificos(cct, rulectx.getTaskProcedureId(), ConstantesSubvenciones.DatosEspecificosOtrosDatos.PROPIEDAD_MODALIDAD));

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct,  plantilla);
            }
            
            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA3;
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
    
    
    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        String numexpConvocatoria = "";
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            
            numexpConvocatoria = rulectx.getNumExp();
                         
            //Obtenemos el asunto de la convocatoria
            String convocatoria = ExpedientesUtil.getAsunto(cct, numexpConvocatoria);

            //Obtenemos el expediente de decreto
            String numexpDecreto = SubvencionesUtils.getUltimoNumexpDecreto(cct, numexpConvocatoria);
            String numDecreto = DecretosUtil.getNumeroDecretoCompleto(cct, numexpDecreto);
            Date fechaDecreto = DecretosUtil.getFechaDecreto(cct, numexpDecreto);
            
          //Obtenemos el número de boletín y la fecha
            String numexpBoletin = SubvencionesUtils.getPrimerNumexpBOP(cct, numexpConvocatoria);
            Date fechaBoletin = BopUtils.getFechaPublicacion(cct, numexpBoletin);
            int numBoletin = BopUtils.getNumBoletin(cct, fechaBoletin);
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_DECRETO, numDecreto);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.FECHA_DECRETO, SubvencionesUtils.formateaFecha(fechaDecreto));
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_BOLETIN, "" + numBoletin);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.FECHA_BOLETIN, SubvencionesUtils.formateaFecha(fechaBoletin));
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_INFORME, "" + TramitesUtil.cuentaTramites(cct, numexpConvocatoria, rulectx.getTaskProcedureId()));
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.CONVOCATORIA, convocatoria);
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexpConvocatoria + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);            
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_DECRETO);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.FECHA_DECRETO);            
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_BOLETIN);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.FECHA_BOLETIN);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_INFORME);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.CONVOCATORIA);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String estadoAdm = ExpedientesUtil.EstadoADM.JF;
        int numeroColumnas = 5;
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
        String tituloColumna3 = "";
        String tituloColumna4 = "";
        String tituloColumna5 = "";
        String tituloColumna6 = "";
        
        String ayuntamiento = "";
        String cif = "";
        String proyecto = "";
        String importe = "";
        String modalidad = "";
        String devolucion = "";
        String motivoRechazo = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if(!conModalidadCategoria){
                Object[] configuracion = configuracion5Columnas(refTabla);
                
                numeroColumnas = ((Integer) configuracion[0]).intValue();
                estadoAdm = (String) configuracion[1];
                distribucionColumnas = (double[]) configuracion[2];
                tituloColumna3 = (String) configuracion[3];
                tituloColumna4 = (String) configuracion[4];
                tituloColumna5 = (String) configuracion[5];
                
            } else {
                Object[] configuracion = configuracion6Columnas(refTabla);
                
                numeroColumnas = ((Integer) configuracion[0]).intValue();
                estadoAdm = (String) configuracion[1];
                distribucionColumnas = (double[]) configuracion[2];
                tituloColumna3 = (String) configuracion[3];
                tituloColumna4 = (String) configuracion[4];
                tituloColumna5 = (String) configuracion[5];
                tituloColumna6 = (String) configuracion[6];
            }
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            
            int numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, tituloColumna3);
                
                if(6 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, tituloColumna4);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloColumna5);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 6, tituloColumna6);
                } else if(5 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, tituloColumna4);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloColumna5);
                } else if (4 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, tituloColumna4);
                }
                
                int i = 1;
                
                for(String numexpHijo: expedientesList){
                    ayuntamiento = "";
                    cif = "";
                    proyecto = "";
                    importe = "";
                    modalidad = "";
                    devolucion = "";
                    motivoRechazo = "";
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                    
                    ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                    cif = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);
                    
                    IItem solicitud = (IItem) entitiesAPI.getEntities(ConstantesSubvenciones.DatosSolicitud.NOMBRE_TABLA, numexpHijo).iterator().next();
                    proyecto = SubvencionesUtils.getString(solicitud, ConstantesSubvenciones.DatosSolicitud.FINALIDAD);
    
                    Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                    if(expResolucion.hasNext()){
                        IItem resolucion = (IItem) expResolucion.next();
                        
                        importe = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                        modalidad = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MODALIDAD1);
                        devolucion = SubvencionesUtils.getFormattedDoubleVacioSiMenorIgualCero(resolucion, ConstantesSubvenciones.DatosResolucion.DEVOLUCION);
                        motivoRechazo = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    }
                    
                    i++;
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                    
                    if(!conModalidadCategoria){
                        String [] datos = {proyecto, importe, devolucion, motivoRechazo};
                        insertaDatos5Columnas(tabla, refTabla, numeroColumnas, i, datos);
                    } else {
                        String [] datos = {proyecto, modalidad, importe, devolucion, motivoRechazo};
                        insertaDatos6Columnas(tabla, refTabla, numeroColumnas, i, datos);
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void insertaDatos5Columnas(XTextTable tabla, String refTabla, int numeroColumnas, int numeroFila, String[] datos) throws ISPACException {
        try{
            if(5 == numeroColumnas){
                LibreOfficeUtil.setTextoCelda(tabla, 3, numeroFila, datos[0]);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numeroFila, datos[1]);
                LibreOfficeUtil.setTextoCelda(tabla, 5, numeroFila, datos[2]);
            } else if (4 == numeroColumnas){
                LibreOfficeUtil.setTextoCelda(tabla, 3, numeroFila, datos[0]);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numeroFila, datos[3]);
            } else if (3 == numeroColumnas){
                LibreOfficeUtil.setTextoCelda(tabla, 3, numeroFila, datos[3]);
            }
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + numeroFila + ". " + e.getMessage(), e);
            throw new ISPACException(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + numeroFila + ". " + e.getMessage(), e);
        }
    }
    
    public void insertaDatos6Columnas(XTextTable tabla, String refTabla, int numeroColumnas, int numeroFila, String[] datos) throws ISPACException{
        try{
            if(6 == numeroColumnas){
                LibreOfficeUtil.setTextoCelda(tabla, 3, numeroFila, datos[0]);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numeroFila, datos[1]);
                LibreOfficeUtil.setTextoCelda(tabla, 5, numeroFila, datos[2]);
                LibreOfficeUtil.setTextoCelda(tabla, 6, numeroFila, datos[3]);
            } else if (5 == numeroColumnas){
                LibreOfficeUtil.setTextoCelda(tabla, 3, numeroFila, datos[0]);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numeroFila, datos[1]);
                LibreOfficeUtil.setTextoCelda(tabla, 5, numeroFila, datos[4]);
            } else if (4 == numeroColumnas){
                LibreOfficeUtil.setTextoCelda(tabla, 3, numeroFila, datos[1]);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numeroFila, datos[4]);
            }
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + numeroFila + ". " + e.getMessage(), e);
            throw new ISPACException(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + numeroFila + ". " + e.getMessage(), e);
        }
    }

    public Object[] configuracion5Columnas(String refTabla){
        Object[] configuracion = new Object[6];
        
        Integer numeroColumnas = Integer.valueOf(5);
        String estadoAdm = ExpedientesUtil.EstadoADM.JF;
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
        String tituloColumna3 = ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD;
        String tituloColumna4 = ConstantesString.CabeceraTabla.SUBVENCION;
        String tituloColumna5 = ConstantesString.CabeceraTabla.MINORACION_DEVOLUCION;
        
        if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
            numeroColumnas = Integer.valueOf(5);            
            estadoAdm = ExpedientesUtil.EstadoADM.JF;            
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
            tituloColumna3 = ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD;
            tituloColumna4 = ConstantesString.CabeceraTabla.SUBVENCION;
            tituloColumna5 = ConstantesString.CabeceraTabla.MINORACION_DEVOLUCION;
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
            numeroColumnas = Integer.valueOf(4);
            estadoAdm = ExpedientesUtil.EstadoADM.RC;            
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
            tituloColumna3 = ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD;
            tituloColumna4 = ConstantesString.CabeceraTabla.MOTIVO_RECHAZO;            
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
            numeroColumnas = Integer.valueOf(3);
            estadoAdm = ExpedientesUtil.EstadoADM.RN;
            distribucionColumnas = DISTRIBUCION_3_COLUMNAS;
            tituloColumna3 = ConstantesString.CabeceraTabla.MOTIVO_RECHAZO;
        }
        
        configuracion[0] = numeroColumnas;
        configuracion[1] = estadoAdm;
        configuracion[2] = distribucionColumnas;
        configuracion[3] = tituloColumna3;
        configuracion[4] = tituloColumna4;
        configuracion[5] = tituloColumna5;
        
        return configuracion;
    }
    
    public Object[] configuracion6Columnas(String refTabla){
        Object[] configuracion = new Object[7];
        
        Integer numeroColumnas = Integer.valueOf(6);
        String estadoAdm = ExpedientesUtil.EstadoADM.JF;
        double[] distribucionColumnas = DISTRIBUCION_6_COLUMNAS_MODALIDAD_CATEGORIA;
        String tituloColumna3 = ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD;
        String tituloColumna4 = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
        String tituloColumna5 = ConstantesString.CabeceraTabla.SUBVENCION;
        String tituloColumna6 = ConstantesString.CabeceraTabla.MINORACION_DEVOLUCION;
        
        if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
            numeroColumnas = Integer.valueOf(6);            
            estadoAdm = ExpedientesUtil.EstadoADM.JF;            
            distribucionColumnas = DISTRIBUCION_6_COLUMNAS_MODALIDAD_CATEGORIA;
            tituloColumna3 = ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD;
            tituloColumna4 = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
            tituloColumna5 = ConstantesString.CabeceraTabla.SUBVENCION;
            tituloColumna6 = ConstantesString.CabeceraTabla.MINORACION_DEVOLUCION;
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
            numeroColumnas = Integer.valueOf(5);
            estadoAdm = ExpedientesUtil.EstadoADM.RC;            
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_MODALIDAD_CATEGORIA;
            tituloColumna3 = ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD;
            tituloColumna4 = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
            tituloColumna5 = ConstantesString.CabeceraTabla.MOTIVO_RECHAZO;
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
            numeroColumnas = Integer.valueOf(4);
            estadoAdm = ExpedientesUtil.EstadoADM.RN;
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_MODALIDAD_CATEGORIA;
            tituloColumna3 = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
            tituloColumna4 = ConstantesString.CabeceraTabla.MOTIVO_RECHAZO;
        }
        
        configuracion[0] = numeroColumnas;
        configuracion[1] = estadoAdm;
        configuracion[2] = distribucionColumnas;
        configuracion[3] = tituloColumna3;
        configuracion[4] = tituloColumna4;
        configuracion[5] = tituloColumna5;
        configuracion[6] = tituloColumna6;
        
        return configuracion;
    }
}