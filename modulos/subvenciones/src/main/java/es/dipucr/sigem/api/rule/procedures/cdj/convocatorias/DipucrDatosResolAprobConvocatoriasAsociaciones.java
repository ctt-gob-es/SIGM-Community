package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Calendar;
import java.util.Iterator;
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
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrDatosResolAprobConvocatoriasAsociaciones extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasAsociaciones.class);
    
    public static final double[] DISTRIBUCION_4_COLUMNAS_RS = {35, 15, 35, 15};
    public static final double[] DISTRIBUCION_4_COLUMNAS_RC = {35, 15, 25, 25};

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
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            
            double importeTotal = 0;
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);
            
            for(String numexpHijo : expedientesList){
                Iterator<?> resolucionesIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                if(resolucionesIterator.hasNext()){
                    IItem resolucion = (IItem) resolucionesIterator.next();
                    importeTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                }
            }
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        int numFilas = 0;
        double[] distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RS;
        String textoCabeceraColumna4 = ConstantesString.CabeceraTabla.IMPORTE;
        
        List<String> expedientesList;
        
        String asociacion = "";
        String nifCifTitular = "";
        String proyecto = "";
        String importe = "";
        String motivoDenegacion = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RS;
                textoCabeceraColumna4 = ConstantesString.CabeceraTabla.IMPORTE;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RC;
                textoCabeceraColumna4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
            }
            
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 4);
            
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, textoCabeceraColumna4);
                
                int i = 1;
                for (String numexpHijo : expedientesList){
                    i++;

                    asociacion = "";
                    nifCifTitular = "";
                    proyecto = "";
                    importe = "";
                    motivoDenegacion = "";
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                    
                    asociacion = solicitudConvocatoria.getBeneficiario();
                    nifCifTitular = solicitudConvocatoria.getNifCifTitular();

                    proyecto = solicitudConvocatoria.getCampoSolicitud( ConstantesSubvenciones.DatosSolicitud.FINALIDAD);

                    importe = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_PERSONAS_FISICAS_EMPR);
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, asociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto);
                    
                    if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, importe);
                        
                    } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, motivoDenegacion);
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
