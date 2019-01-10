package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.planemp;

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

public class DipucrDatosResolAprobConvDiscapacitadosEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvDiscapacitadosEELL.class);
    
    public static final double[] DISTRIBUCION_7_COLUMNAS = {25, 15, 20, 10, 10, 10, 10};
    public static final double[] DISTRIBUCION_4_COLUMNAS = {25, 15, 30, 30};

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
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
    
    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            
            double importeTotal = 0;
            double importeDiscapacitados = 0;
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);
            
            for (String numexpHijo : expedientesList){
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                if( resolucionIterator.hasNext()){
                    IItem resolucion = (IItem) resolucionIterator.next();
                    
                    importeTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                    importeDiscapacitados += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE2);
                    
                } else{
                    cct.getAPI().getEntitiesAPI().createEntity(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo);
                    importeTotal += 0;
                }
            }
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
            cct.setSsVariable("IMPORTE_DISCAPACITADOS", SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeDiscapacitados));
            cct.setSsVariable("IMPORTE_TOTAL", SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeDiscapacitados + importeTotal));
            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE);
            cct.deleteSsVariable("IMPORTE_DISCAPACITADOS");
            cct.deleteSsVariable("IMPORTE_TOTAL");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        int numeroColumnas = 7;
        double[] distribucionColumnas = DISTRIBUCION_7_COLUMNAS;
        String tituloColumna4 = ConstantesString.CabeceraTabla.IMPORTE_SOLICITUD;
        
        String ayuntamiento = "";
        String nifCifTitular = "";
        String proyecto = "";
        String presupuesto = "";
        double importe = 0;
        double discapacitados = 0;
        String motivoDenegacion = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                numeroColumnas = 7;
                distribucionColumnas = DISTRIBUCION_7_COLUMNAS;
                tituloColumna4 = ConstantesString.CabeceraTabla.IMPORTE_SOLICITUD;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
                tituloColumna4 = ConstantesString.CabeceraTabla.MEMORIA_DENEGACION;
            }
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            
            int numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, tituloColumna4);
                
                if( 7 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, ConstantesString.CabeceraTabla.SOLICITUD_SUBVENCIO_DISCAPACITADOS);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 6, ConstantesString.CabeceraTabla.SUBVENCION_PREASIGNADA);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 7, ConstantesString.CabeceraTabla.TOTAL_SUBVENCION);
                }
                
                int i = 1;
                for (String numexpHijo: expedientesList){
                    ayuntamiento = "";
                    nifCifTitular = "";
                    proyecto = "";
                    presupuesto = "";
                    importe = 0;
                    discapacitados = 0;
                    motivoDenegacion = "";
                            
                    i++;
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);                            
                    
                    ayuntamiento = solicitudConvocatoria.getBeneficiario();
                    nifCifTitular = solicitudConvocatoria.getNifCifTitular();

                    proyecto = solicitudConvocatoria.getCampoSolicitud(ConstantesSubvenciones.DatosSolicitud.FINALIDAD);
                    presupuesto = solicitudConvocatoria.getCampoSolicitud(ConstantesSubvenciones.DatosSolicitud.PRESUPUESTO);

                    importe = solicitudConvocatoria.getImporte1();
                    discapacitados = solicitudConvocatoria.getImporte2();
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto);
                    
                    if(7 == numeroColumnas){
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, presupuesto);
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, discapacitados));
                        LibreOfficeUtil.setTextoCelda(tabla, 6, i, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe));
                        LibreOfficeUtil.setTextoCelda(tabla, 7, i, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, discapacitados + importe));
                    } else if (4 == numeroColumnas){
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
