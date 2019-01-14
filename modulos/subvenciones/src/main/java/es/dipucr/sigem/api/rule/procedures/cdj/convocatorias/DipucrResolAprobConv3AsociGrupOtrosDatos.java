package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.procedures.ConfiguracionColumna;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrResolAprobConv3AsociGrupOtrosDatos extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrResolAprobConv3AsociGrupOtrosDatos.class);
    
    public static final String TEXTO_APROBACION = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
    public static final String TEXTO_RECHAZO = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";
    public static final String TEXTO_RENUNCIA = "Ayuntamientos que renuncian:";

    public static final String TEXTO1 = TEXTO_APROBACION;
    public static final String TEXTO2 = TEXTO_RECHAZO;
    public static final String TEXTO3 = TEXTO_RENUNCIA;
    public static final String TEXTO4 = TEXTO_APROBACION;
    public static final String TEXTO5 = TEXTO_RECHAZO;
    public static final String TEXTO6 = TEXTO_RENUNCIA;
    public static final String TEXTO7 = TEXTO_APROBACION;
    public static final String TEXTO8 = TEXTO_RECHAZO;
    public static final String TEXTO9 = TEXTO_RENUNCIA;

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
        String numexp = "";
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            numexp = rulectx.getNumExp();

            String importeTotal = getImporteTotal(rulectx);            

            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, importeTotal);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO1, TEXTO1);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO2, TEXTO2);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO3, TEXTO3);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO4, TEXTO4);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO5, TEXTO5);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO6, TEXTO6);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO7, TEXTO7);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO8, TEXTO8);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO9, TEXTO9);
            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO1);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO2);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO3);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO4);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO5);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO6);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO7);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO8);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO9);

        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    private String getImporteTotal(IRuleContext rulectx) {
        String numexp = "";
        double importeTotal = 0;
        
        try{
            numexp = rulectx.getNumExp();
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.NREG);
    
            for (String numexpHijo : expedientesList) {
                ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), numexpHijo);
    
                if (solicitudConvocatoria.esAprobadoGrupo1()){
                    importeTotal += solicitudConvocatoria.getImporte1();
                }
                if (solicitudConvocatoria.esAprobadoGrupo2()){
                    importeTotal += solicitudConvocatoria.getImporte2();
                }
                if (solicitudConvocatoria.esAprobadoGrupo3()){
                    importeTotal += solicitudConvocatoria.getImporte3();
                }
            }
        } catch(ISPACException e){
            LOGGER.error("ERROR al recuperar el importe total de la resolución del expediente: " + numexp + ". " + e.getMessage(), e);
        }
        
        return SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal);
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
                List<String> expedientesHijos = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, orden);
                
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
            estadosADM.add(ExpedientesUtil.EstadoADM.RS);
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
