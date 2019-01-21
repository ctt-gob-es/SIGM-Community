package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
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
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrDatosResolAprobConvocatoriasAsociacionesPuntosProyecto extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasAsociacionesPuntosProyecto.class);
    
    public static final double [] DISTRIBUCION_COLUMNAS_RS = {20, 10, 20, 20, 10, 10, 10};
    public static final double [] DISTRIBUCION_COLUMNAS_RC = {20, 15, 20, 20, 25};

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
            IInvesflowAPI invesflowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            
            double importeTotal = 0;
            double puntosTotal = 0;
            int numSolicitudesAprobadas = 0;
            int numSolicitudesFueraPlazo = 0;
            int numSolicitudesIncumplen = 0;
            
            String numexpConvocatoria = rulectx.getNumExp();
            
             //Obtenemos los expedientes relacionados y aprobados, ordenados por asociacion
            List<String> expedientesRelacionadosList = ExpedientesRelacionadosUtil.getExpRelacionadosHijos(entitiesAPI, numexpConvocatoria);
            
            for(String numexpHijo : expedientesRelacionadosList){
                
                IItemCollection resolucionCollection = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo);
                Iterator<?> resolucionIterator = resolucionCollection.iterator();
                
                String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, numexpHijo);
                
                if(ExpedientesUtil.EstadoADM.RS.equals(estadoAdm)){
                    numSolicitudesAprobadas++;
                    if(resolucionIterator.hasNext()){
                        IItem resolucion = (IItem)resolucionIterator.next();
                        importeTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);                            
                        puntosTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO1);
                    }
                } else if(ExpedientesUtil.EstadoADM.RC.equals(estadoAdm) && resolucionIterator.hasNext()){
                    String motivoRechazo = ((IItem)resolucionIterator.next()).getString(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    if(StringUtils.isNotEmpty(motivoRechazo)){
                        if(motivoRechazo.toUpperCase().contains("PLAZO")){
                            numSolicitudesFueraPlazo++;
                        } else{
                            numSolicitudesIncumplen++;
                        }
                    }
                }            
            }
               
            //Obtenemos el expediente de decreto
            String numexpDecreto = DecretosUtil.getPrimerNumexpDecreto(cct, numexpConvocatoria);
            String numDecreto = DecretosUtil.getNumeroDecretoCompleto(cct, numexpDecreto);
            Date fechaDecreto = DecretosUtil.getFechaDecreto(cct, numexpDecreto);
            
            //Obtenemos el número de boletín y la fecha
            String numexpBoletin = SubvencionesUtils.getPrimerNumexpBOP(cct, numexpConvocatoria);
            Date fechaBoletin = BopUtils.getFechaPublicacion(cct, numexpBoletin);
            int numBoletin = BopUtils.getNumBoletin(cct, fechaBoletin);
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
            cct.setSsVariable("PUNTOS_TOTAL", SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, puntosTotal));
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_DECRETO, numDecreto);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.FECHA_DECRETO, SubvencionesUtils.formateaFecha(fechaDecreto));            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_BOLETIN, "" +numBoletin);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.FECHA_BOLETIN, SubvencionesUtils.formateaFecha(fechaBoletin));
            cct.setSsVariable("NUM_SOLICITUDES", "" + (numSolicitudesAprobadas + numSolicitudesFueraPlazo + numSolicitudesIncumplen));
            cct.setSsVariable("NUM_SOLICITUDES_APROBADAS", "" + numSolicitudesAprobadas);
            cct.setSsVariable("NUM_SOLICITUDES_FUERA_PLAZO", "" + numSolicitudesFueraPlazo);
            cct.setSsVariable("NUM_SOLICITUDES_INCUMPLEN", "" + numSolicitudesIncumplen);
            
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
        
        String estadoADM = ExpedientesUtil.EstadoADM.RS;
        double[] distribucionColumnas = DISTRIBUCION_COLUMNAS_RS;
        int numeroColumnas = 7;
        
        List<String> expedientesList = new ArrayList<String>();
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                
                 estadoADM = ExpedientesUtil.EstadoADM.RS;
                 distribucionColumnas = DISTRIBUCION_COLUMNAS_RS;
                 numeroColumnas = 7;
                 
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                
                estadoADM = ExpedientesUtil.EstadoADM.RC;
                distribucionColumnas = DISTRIBUCION_COLUMNAS_RC;
                numeroColumnas = 5;
            }
            
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoADM, ExpedientesUtil.CIUDAD + ", " + ExpedientesUtil.IDENTIDADTITULAR);
            
            int numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            if(null != tabla){
                
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
             
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.SOLICITANTE);    
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.LOCALIDAD);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                
                if(ExpedientesUtil.EstadoADM.RS.equals(estadoADM)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, ConstantesString.CabeceraTabla.PRESUPUESTO);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 6, ConstantesString.CabeceraTabla.PTOS);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 7, ConstantesString.CabeceraTabla.IMPORTE);
                } else {
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION);
                }
                
                int i = 1;
                for (String numexpHijo : expedientesList){
                    
                    String beneficiario = "";
                    String nifCifBeneficiario = "";
                    String ciudad = "";
                    String proyecto = "";
                    String presupuesto = "";
                    String importe = "";
                    String puntos = "";
                    String motivoDenegacion = "";
                    
                    i++;
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                    
                    beneficiario = solicitudConvocatoria.getBeneficiario();
                    nifCifBeneficiario = solicitudConvocatoria.getNifCifTitular();
                    ciudad = solicitudConvocatoria.getCampoExpediente(ExpedientesUtil.CIUDAD);

                    proyecto = solicitudConvocatoria.getCampoSolicitud(ConstantesSubvenciones.DatosSolicitud.FINALIDAD);
                    presupuesto = solicitudConvocatoria.getCampoSolicitud(ConstantesSubvenciones.DatosSolicitud.PRESUPUESTO);

                    importe = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE);
                    puntos = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO1, ConstantesString.FORMATO_PUNTOS);
                    
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_PERSONAS_FISICAS_EMPR);                   
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, beneficiario);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifBeneficiario);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, ciudad);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, i, proyecto);
                    
                    if(ExpedientesUtil.EstadoADM.RS.equals(estadoADM)){
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, presupuesto);
                        LibreOfficeUtil.setTextoCelda(tabla, 6, i, puntos);
                        LibreOfficeUtil.setTextoCelda(tabla, 7, i, importe);
                    } else {
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, motivoDenegacion);
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
