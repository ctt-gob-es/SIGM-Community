package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.SuperClaseUnaTabla;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

public class DipucrGeneraJustificacionConvocatoriasSubNumEscuelas extends SuperClaseUnaTabla{

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraJustificacionConvocatoriasSubNumEscuelas.class);
    
    public static final double[] DISTRIBUCION_4_COLUMNAS = {45, 15, 20, 20};
    
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
        String ayuntamiento = "";
        String cif = "";
        int nEscuelas = 0;
        String importe = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.JF, ExpedientesUtil.IDENTIDADTITULAR);
            
            int numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 4);
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, DISTRIBUCION_4_COLUMNAS);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.N_ESCUELAS);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.IMPORTE);
    
                int i = 1;
                for (String numexpHijo : expedientesList){
                    ayuntamiento = "";
                    cif = "";
                    nEscuelas = 0;
                    importe = "";
                    
                    i++;
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                    
                    ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                    cif = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);
    
                    Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                    if(expResolucion.hasNext()){
                        IItem resolucion = (IItem) expResolucion.next();
                        
                        importe = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                        nEscuelas = SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.NUMESCUELAS);
                    }

                    if(nEscuelas < 0){
                        nEscuelas = 0;
                    }
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, "" + nEscuelas);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, i, importe);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }
}