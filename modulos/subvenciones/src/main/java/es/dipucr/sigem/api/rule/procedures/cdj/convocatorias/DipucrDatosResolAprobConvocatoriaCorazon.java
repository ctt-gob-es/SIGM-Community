package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrDatosResolAprobConvocatoriaCorazon extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriaCorazon.class);
    
    public static final double [] DISTRIBUCION_4_COLUMNAS = {50, 20, 15, 15};
    public static final double [] DISTRIBUCION_3_COLUMNAS = {40, 20, 40};

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try{
            IClientContext cct = rulectx.getClientContext();
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
            
            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2;
        } catch(ISPACException e){
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
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        int numeroColumnas = 4;
        double[] distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
        int numFilas = 0;
        
        List<String> expedientesList;
        
        
        String ayuntamiento = "";
        String nifCifTitular = "";
        String desfibriladores = "";
        String displays = "";
        String motivoDenegacion = "";        
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                numeroColumnas = 3;
                distribucionColumnas = DISTRIBUCION_3_COLUMNAS;
            }
            
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            
            if( null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                
                if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.N_DESFIBRILADORES);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.N_DISPLAYS);
                    
                } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION);
                }
                
                int i = 1;
                for (String numexpHijo : expedientesList){
                    i++;
                    
                    ayuntamiento = "";
                    nifCifTitular = "";
                    desfibriladores = "";
                    displays = "";
                    motivoDenegacion = "";
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);

                    ayuntamiento = solicitudConvocatoria.getBeneficiario();
                    nifCifTitular = solicitudConvocatoria.getNifCifTitular();

                    desfibriladores = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.PROYECTO1);
                    displays = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.PROYECTO2);
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                    
                    if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, desfibriladores);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, displays);
                        
                    } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, motivoDenegacion);
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
