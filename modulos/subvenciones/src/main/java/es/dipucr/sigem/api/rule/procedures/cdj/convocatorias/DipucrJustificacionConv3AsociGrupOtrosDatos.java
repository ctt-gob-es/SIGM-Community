package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConfiguracionColumna;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrJustificacionConv3AsociGrupOtrosDatos extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrJustificacionConv3AsociGrupOtrosDatos.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA3
                        + "," + LibreOfficeUtil.ReferenciasTablas.TABLA4 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA5 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA6
                        + "," + LibreOfficeUtil.ReferenciasTablas.TABLA7 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA8 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA9;
            
        } catch (ISPACException e) {
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
            String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexpConvocatoria);
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
        if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)) {
            insertaTablaResolucion(rulectx, component, refTabla, 1);                
                            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)) {
            insertaTablaRechazadosRenuncian(rulectx, component, refTabla, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)) {
            insertaTablaRechazadosRenuncian(rulectx, component, refTabla, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA4.equals(refTabla)) {
            insertaTablaResolucion(rulectx, component, refTabla, 2);                
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA5.equals(refTabla)) {            
            insertaTablaRechazadosRenuncian(rulectx, component, refTabla, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA6.equals(refTabla)) {            
            insertaTablaRechazadosRenuncian(rulectx, component, refTabla, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA2);
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA7.equals(refTabla)) {
            insertaTablaResolucion(rulectx, component, refTabla, 3);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA8.equals(refTabla)) {
            insertaTablaRechazadosRenuncian(rulectx, component, refTabla, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO3);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA9.equals(refTabla)) {            
            insertaTablaRechazadosRenuncian(rulectx, component, refTabla, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA3);
        }
    }
    
    private void insertaTablaResolucion(IRuleContext rulectx, XComponent component, String refTabla, int grupo) {
        try{
            List<ConfiguracionColumna> configuracionTabla = SubvencionesUtils.getConfiguracionColumnas(rulectx, refTabla);
            
            String orden = SubvencionesUtils.getOrdenTabla(rulectx, refTabla);
            
            List<ObjetoSolictudConvocatoriaSubvencion> expedientesList = new ArrayList<ObjetoSolictudConvocatoriaSubvencion>();
            if(!configuracionTabla.isEmpty()){
                List<String> expedientesHijos = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.JF, orden);
                
                for(String numexpSol: expedientesHijos){
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), numexpSol);
                    
                    if(solicitudConvocatoria.esAprobado(grupo)){
                        expedientesList.add(solicitudConvocatoria);
                    } 
                }
                SubvencionesUtils.insertaTabla(rulectx, component, refTabla, configuracionTabla, expedientesList);
            }
        } catch (ISPACException e){
            LOGGER.error("ERROR: " + e.getMessage(), e);
        }
    }

    private void insertaTablaRechazadosRenuncian(IRuleContext rulectx, XComponent component, String refTabla, String columna) {        
        try{
            List<String> estadosADM = new ArrayList<String>();
            estadosADM.add(ExpedientesUtil.EstadoADM.JF);
            estadosADM.add(ExpedientesUtil.EstadoADM.RC);
            estadosADM.add(ExpedientesUtil.EstadoADM.RN);
             
             List<ConfiguracionColumna> configuracionTabla = SubvencionesUtils.getConfiguracionColumnas(rulectx, refTabla);
             
             String orden = SubvencionesUtils.getOrdenTabla(rulectx, refTabla);
             
             List<ObjetoSolictudConvocatoriaSubvencion> expedientesList = new ArrayList<ObjetoSolictudConvocatoriaSubvencion>();
             
             if(!configuracionTabla.isEmpty()){
                 List<String> expedientesHijos = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosADM, orden);
                 
                 for(String numexpSol: expedientesHijos){
                     ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), numexpSol);
                     
                     String motivo = solicitudConvocatoria.getCampoResolucion(columna);
                     if (StringUtils.isNotEmpty(motivo)){
                         expedientesList.add(solicitudConvocatoria);
                     }
                 }
                 SubvencionesUtils.insertaTabla(rulectx, component, refTabla, configuracionTabla, expedientesList);
             }
         } catch (ISPACException e){
             LOGGER.error("ERROR: " + e.getMessage(), e);
         }
    }
}
