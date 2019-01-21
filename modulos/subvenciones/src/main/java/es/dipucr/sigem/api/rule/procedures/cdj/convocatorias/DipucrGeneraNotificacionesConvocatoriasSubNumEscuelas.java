package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.FuncionesComunesDocumentos;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrGeneraNotificacionesConvocatoriasSubNumEscuelas extends FuncionesComunesDocumentos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesConvocatoriasSubNumEscuelas.class);
    
    public static final String [] ESTADOSADM = {ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.RC};
    
    public static final double[] DISTRIBUCION_4_COLUMNAS_AP = {35, 15, 25, 25};
    public static final double[] DISTRIBUCION_4_COLUMNAS_RC = {35, 15, 15, 35};

    protected String plantilla = "";
    protected int templateId = 0;
    protected String tipoDocumento = "";
    protected int documentTypeId = 0;
    
    protected String refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;

    protected int numeroTramites = 0;
    protected String nombreTramite = "";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try{
            IClientContext cct = rulectx.getClientContext();
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
            
            documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);

            templateId = DocumentosUtil.getTemplateId(cct, plantilla, documentTypeId);
            
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct = rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            //----------------------------------------------------------------------------------------------
            
            String numexp = rulectx.getNumExp();            
            String extractoDecreto = "";
                        
            List<String> expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(ESTADOSADM));            
            
            String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexp);
            extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);
                    
            if(!expedientesResolucion.isEmpty()){
                numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
                nombreTramite = TramitesUtil.getNombreTramite(cct, rulectx.getTaskId());
                for(String numexpHijo : expedientesResolucion){
                    generaNotificacion(numexpHijo, rulectx, cct, entitiesAPI, genDocAPI, extractoDecreto);
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaNotificacion(String numexp, IRuleContext rulectx, IClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, String extractoDecreto) {
        try{
            int taskId = rulectx.getTaskId();
            
            cct.beginTX();
            
            IItemCollection participantesCollection = ParticipantesUtil.getParticipantes( cct, numexp);
            Iterator<?> participantesIterator = participantesCollection.iterator();
            if(participantesIterator.hasNext()){
                IItem participante = (IItem) participantesIterator.next();
                
                DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                    
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" +Calendar.getInstance().get(Calendar.YEAR));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL, "" + numeroTramites);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, nombreTramite);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);
                
                String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, numexp);
                
                String textoResol = "";
                
                if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
                    textoResol = "La concesión de subvención, conforme al siguiente detalle: ";
                } else{
                    textoResol = "La denegación de la siguiente solicitud, por el motivo que se indica: ";
                }
                
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION, textoResol);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);
            
                IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                documento.store(cct);
                
                DocumentosUtil.borraParticipanteSsVariable(cct);
                
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);
                
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);
                
                if(null != refTablas && !"".equals(refTablas)){
                    String docRef = DocumentosUtil.getInfoPag(rulectx, documento.getInt(DocumentosUtil.ID));
                    insertaTablas(genDocAPI, docRef, rulectx, documento.getKeyInt(), refTablas, entitiesAPI, numexp);
                }
            }
            cct.endTX(true);
        } catch(ISPACRuleException e){
            LOGGER.error(e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }
    
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        
        String estadoAdm = "";
        String tituloCol4 = "";
        double[] distribucionColumnas = DISTRIBUCION_4_COLUMNAS_AP;
        
        String beneficiario = "";
        String nifCifTitular = "";
        String nescuelas = "";
        String importe = "";
        String motivoDenegacion = "";
        
        try{
            ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), numexp);
            
            estadoAdm = solicitudConvocatoria.getCampoExpediente(ExpedientesUtil.ESTADOADM);
            
            if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS_AP;
                tituloCol4 = ConstantesString.CabeceraTabla.IMPORTE;
            } else{
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RC;
                tituloCol4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
            }

            beneficiario = solicitudConvocatoria.getBeneficiario();
            nifCifTitular = solicitudConvocatoria.getNifCifTitular();

            importe = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
            nescuelas = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NUMESCUELAS);
            motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
            
            solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
            solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_PERSONAS_FISICAS_EMPR);
            solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), rulectx.getNumExp());
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 2, 4);
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
    
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.N_ESCUELAS);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, tituloCol4);
    
                if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, beneficiario);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, nifCifTitular);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nescuelas);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, 2, importe);
                } else if(ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)){
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, beneficiario);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, nifCifTitular);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nescuelas);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, 2, motivoDenegacion);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }    
}