package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
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
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

public class DipucrGeneraInformeCierreConvSubEELLDosProyectosEELL extends DipucrAutoGeneraDocIniTramiteRule{

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraInformeCierreConvSubEELLDosProyectosEELL.class);
    
    public static final double[] DISTRIBUCION_6_COLUMNAS = {35, 15, 30, 10, 10, 10};
    public static final double[] DISTRIBUCION_4_COLUMNAS = {35, 15, 25, 25};
    public static final double[] DISTRIBUCION_2_COLUMNAS = {80, 20};
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {    
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try{
            IClientContext cct = rulectx.getClientContext();
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
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
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            
            String numexpConvocatoria = rulectx.getNumExp();
                         
            String convocatoria = ExpedientesUtil.getAsunto(cct, numexpConvocatoria);

            //Obtenemos el expediente de decreto
            String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexpConvocatoria);
            String numDecreto = DecretosUtil.getNumeroDecretoCompleto(cct, numexpDecreto);
            Date fechaDecreto = DecretosUtil.getFechaDecreto(cct, numexpDecreto);
            
            //Obtenemos el número de boletín y la fecha
            String numexpBoletin = SubvencionesUtils.getPrimerNumexpBOP(cct, numexpConvocatoria);
            Date fechaBoletin = BopUtils.getFechaPublicacion(cct, numexpBoletin);
            int numBoletin = BopUtils.getNumBoletin(cct, fechaBoletin);

            cct.setSsVariable("NUM_DECRETO_RESOL", numDecreto);
            cct.setSsVariable("FECHA_DECRETO_RESOL", SubvencionesUtils.formateaFecha(fechaDecreto));
            
            cct.setSsVariable("NUM_BOLETIN_APR", "" + numBoletin);
            cct.setSsVariable("FECHA_BOLETIN_APR", SubvencionesUtils.formateaFecha(fechaBoletin));
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.CONVOCATORIA, convocatoria);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);            
            cct.deleteSsVariable("NUM_DECRETO_RESOL");
            cct.deleteSsVariable("FECHA_DECRETO_RESL");            
            cct.deleteSsVariable("NUM_BOLETIN_APR");
            cct.deleteSsVariable("FECHA_BOLETIN_APR");
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.CONVOCATORIA);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        
        String estadoAdm = ExpedientesUtil.EstadoADM.JI;
        int numFilas = 0;
        int numeroColumnas = 6;
        double[] distribucionColumnas = DISTRIBUCION_6_COLUMNAS;
        
        List<String> expedientesList = new ArrayList<String>();

        String ayuntamiento = "";
        String cif = "";
        
        String proyecto1 = "";
        String puntos1 = "";
        String justificado1 = "";
        String importe1 = "";
        String devolucion1 = "";
        
        String proyecto2 = "";
        String puntos2 = "";
        String justificado2 = "";
        String importe2 = "";
        String devolucion2 = "";
        
        String motivoDenegacion = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();

            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.JI;
                numeroColumnas = 6;
                distribucionColumnas = DISTRIBUCION_6_COLUMNAS;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RN;
                numeroColumnas = 2;
                distribucionColumnas = DISTRIBUCION_2_COLUMNAS;
            }
            
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                for(String numexpHijo : expedientesList){
                    IItemCollection resolucionesCollection = entitiesAPI.queryEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, ConstantesString.WHERE + " NUMEXP = '" + numexpHijo + "'");
                    Iterator<?> resolucionesIterator = resolucionesCollection.iterator();
                    if(resolucionesIterator.hasNext()){
                        IItem resolucion = (IItem) resolucionesIterator.next();
                        String proyecto1CuentaFilas = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.PROYECTO1);
                        String justificado1CuentaFilas = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.JUSTIF_PROY_1);
                        
                        if(StringUtils.isNotEmpty(proyecto1CuentaFilas) && !"NO".equals(justificado1CuentaFilas)){
                            numFilas++;
                        }
                        
                        String proyecto2CuentaFilas = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.PROYECTO2);
                        String justificado2CuentaFilas = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.JUSTIF_PROY_2);
                        
                        if(StringUtils.isNotEmpty(proyecto2CuentaFilas) && !"NO".equals(justificado2CuentaFilas)){
                            numFilas++;
                        }
                    }
                }
            } else {
                numFilas = expedientesList.size();
            }
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
    
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                
                if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.PUNTOS);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, ConstantesString.CabeceraTabla.IMPORTE);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 6, ConstantesString.CabeceraTabla.MINORACION_DEVOLUCION);
                    
                } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION);                
                }   
    
                int i = 1;
                for (String numexpHijo : expedientesList){
                    ayuntamiento = "";
                    cif = "";
                    
                    proyecto1 = "";
                    puntos1 = "";
                    justificado1 = "";
                    importe1 = "";
                    devolucion1 = "";
                    
                    proyecto2 = "";
                    puntos2 = "";
                    justificado2 = "";
                    importe2 = "";
                    devolucion2 = "";
                    
                    motivoDenegacion = "";
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                    ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                    cif = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);                                            
    
                    Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                    if(expResolucion.hasNext()){
                        IItem resolucion = (IItem) expResolucion.next();
                        
                        proyecto1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.PROYECTO1);
                        justificado1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.JUSTIF_PROY_1);
                        puntos1 = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO1, ConstantesString.FORMATO_PUNTOS);
                        importe1 = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO1);
                        devolucion1 = SubvencionesUtils.getFormattedDoubleVacioSiMenorCero(resolucion, ConstantesSubvenciones.DatosResolucion.DEVOLUCION);
                        
                        proyecto2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.PROYECTO2);
                        justificado2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.JUSTIF_PROY_2);
                        puntos2 = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO2, ConstantesString.FORMATO_PUNTOS);
                        importe2 = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO2);
                        devolucion2 = SubvencionesUtils.getFormattedDoubleVacioSiMenorCero(resolucion, ConstantesSubvenciones.DatosResolucion.DEVOLUCION2);
                        
                        motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    }
                    
                    if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){               
                        if(StringUtils.isNotEmpty(proyecto1) && !"NO".equals(justificado1)){
                            i++;
                            LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                            LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                            LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto1);
                            LibreOfficeUtil.setTextoCelda(tabla, 4, i, puntos1);
                            LibreOfficeUtil.setTextoCelda(tabla, 5, i, importe1);
                            LibreOfficeUtil.setTextoCelda(tabla, 6, i, devolucion1);
                        }
                        if(StringUtils.isNotEmpty(proyecto2) && !"NO".equals(justificado2)){
                            i++;
                            LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                            LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                            LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto2);
                            LibreOfficeUtil.setTextoCelda(tabla, 4, i, puntos2);
                            LibreOfficeUtil.setTextoCelda(tabla, 5, i, importe2);
                            LibreOfficeUtil.setTextoCelda(tabla, 6, i, devolucion2);
                        }
                    } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                        i++;
                        LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                        LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto2);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, motivoDenegacion);
                    } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                        i++;
                        LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                        LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }
}
