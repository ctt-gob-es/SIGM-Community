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
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

public class DipucrGeneraInformeCierreConvSubEELLDevol extends DipucrAutoGeneraDocIniTramiteRule{

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraInformeCierreConvSubEELLDevol.class);
    
    public static final double[] DISTRIBUCION_5_COLUMNAS = {35, 15, 30, 10, 10};
    public static final double[] DISTRIBUCION_4_COLUMNAS = {35, 15, 25, 25};
    public static final double[] DISTRIBUCION_2_COLUMNAS = {70, 30};
    
    
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
                         
            //Obtenemos el asunto de la convocatoria
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
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
        int numeroColumnas = 5;
        
        String ayuntamiento = "";
        String cif = "";
        String proyecto = "";
        double importe = 0;
        double devolucion = 0;
        String motivoRechazo = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            //Recuperamos los expedientes a justificar
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.JI;
                numeroColumnas = 5;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RN;
                numeroColumnas = 2;
                distribucionColumnas = DISTRIBUCION_2_COLUMNAS;
            }
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            
            int numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1 , numeroColumnas);
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                
                if(5 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.SUBVENCION);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, ConstantesString.CabeceraTabla.DEVOLUCION);
                } else if(4 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.MOTIVO_RECHAZO);
                }
                
                int i = 1;
                for (String numexpHijo : expedientesList){
                    ayuntamiento = ""; 
                    cif = ""; 
                    proyecto = "";
                    importe = 0;
                    devolucion = 0;
                    motivoRechazo = "";
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                    ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                    cif = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);
                    
                    IItem solicitud = (IItem) entitiesAPI.getEntities(ConstantesSubvenciones.DatosSolicitud.NOMBRE_TABLA, numexpHijo).iterator().next();
                    proyecto = SubvencionesUtils.getString(solicitud, ConstantesSubvenciones.DatosSolicitud.FINALIDAD);
                    
                    Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                    if(expResolucion.hasNext()){
                        IItem resolucion = (IItem) expResolucion.next();
                        
                        importe = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                        devolucion = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.DEVOLUCION);                        
                        motivoRechazo = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    }
                    
                    i++;
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                    
                    if(5 == numeroColumnas){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe));
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, devolucion));
                    } else if(4 == numeroColumnas){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, motivoRechazo);
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}