package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
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

public class DipucrDatosResolAprobConvocatoriasDosProyectosEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasDosProyectosEELL.class);
    
    public static final double[] DISTRIBUCION_5_COLUMNAS = {30, 15, 25, 15, 15};
    public static final double[] DISTRIBUCION_4_COLUMNAS = {35, 15, 25, 25};

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
            numexp = rulectx.getNumExp();
            
            double importeTotal = 0;
            
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);
            
            for (String numexpHjo : expedientesList){
                
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHjo).iterator();
                
                if( resolucionIterator.hasNext()){
                    IItem resolucion = (IItem) resolucionIterator.next();

                    double importe = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO1);
                    importeTotal += importe;
                    
                    importe = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO2);
                    importeTotal += importe;
                } else{
                    cct.getAPI().getEntitiesAPI().createEntity(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHjo);
                    importeTotal += 0;
                }
            }
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, ""    + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
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
        int numeroColumnas = 5;
        int numFilas = 0;
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
        
        String beneficiario = "";
        String nifCifTitular = "";
        String proyectoSolicitud = "";
        
        String proyecto1 = "";        
        String puntos1 = "";
        String importe1 = "";
        String proyecto2 = "";
        String puntos2 = "";
        String importe2 = "";
        String motivoDenegacion = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();

            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                numeroColumnas = 5;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
                
            }
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            
            if(5 == numeroColumnas){
                for(String numexpHijo : expedientesList){
                    proyecto1 = "";
                    proyecto2 = "";
                    
                    IItemCollection resolucionesCollection = entitiesAPI.queryEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " = '" + numexpHijo +"'");
                    Iterator<?> resolucionesIterator = resolucionesCollection.iterator();
                    
                    if(resolucionesIterator.hasNext()){
                        IItem resolucion = (IItem) resolucionesIterator.next();
                        
                        proyecto1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.PROYECTO1);                        
                        if(StringUtils.isNotEmpty(proyecto1)){
                            numFilas++;
                        }
                        
                        proyecto2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.PROYECTO2);
                        if(StringUtils.isNotEmpty(proyecto2)){
                            numFilas++;
                        }
                    }
                }
            } else if(4 == numeroColumnas){
                numFilas = expedientesList.size();
            }
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                if(5 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.PUNTOS);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, ConstantesString.CabeceraTabla.IMPORTE);
                } else if (4 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION);
                }
                
                int i = 1;
                for(String numexpHijo : expedientesList){
                    beneficiario = "";
                    nifCifTitular = "";
                    proyectoSolicitud = "";
                    
                    proyecto1 = "";                
                    puntos1 = "";
                    importe1 = "";
                    proyecto2 = "";
                    puntos2 = "";
                    importe2 = "";
                    motivoDenegacion = "";
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                    
                    beneficiario = solicitudConvocatoria.getBeneficiario();
                    nifCifTitular = solicitudConvocatoria.getNifCifTitular();

                    proyectoSolicitud = solicitudConvocatoria.getCampoSolicitud(ConstantesSubvenciones.DatosSolicitud.FINALIDAD);
                    
                    proyecto1 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.PROYECTO1);
                    puntos1 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO1, ConstantesString.FORMATO_PUNTOS);
                    importe1 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO1, ConstantesString.FORMATO_IMPORTE);

                    proyecto2 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.PROYECTO2);
                    puntos2 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO2, ConstantesString.FORMATO_PUNTOS);
                    importe2 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO2, ConstantesString.FORMATO_IMPORTE);

                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                    
                    if(5 == numeroColumnas){
                        if(StringUtils.isNotEmpty(proyecto1)){
                            i++;
                            LibreOfficeUtil.setTextoCelda(tabla, 1, i, beneficiario);
                            LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                            LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto1);
                            LibreOfficeUtil.setTextoCelda(tabla, 4, i, puntos1);
                            LibreOfficeUtil.setTextoCelda(tabla, 5, i, importe1);
                        }
                        if(StringUtils.isNotEmpty(proyecto2)){
                            i++;
                            LibreOfficeUtil.setTextoCelda(tabla, 1, i, beneficiario);
                            LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                            LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto2);
                            LibreOfficeUtil.setTextoCelda(tabla, 4, i, puntos2);
                            LibreOfficeUtil.setTextoCelda(tabla, 5, i, importe2);
                        }
                    } else if (4 == numeroColumnas){
                        i++;
                        LibreOfficeUtil.setTextoCelda(tabla, 1, i, beneficiario);
                        LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyectoSolicitud);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, motivoDenegacion);
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
