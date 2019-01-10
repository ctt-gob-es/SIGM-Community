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
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

public class DipucrGeneraRecordatorioJustConvocatoriasSub extends FuncionesComunesDocumentos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraRecordatorioJustConvocatoriasSub.class);

    protected String tipoDocumento = "";
    protected int documentTypeId = 0;
    protected int templateId = 0;

    protected String refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;
    
    protected String plantilla = "";
    
    protected int numeroTramites = 0;

    private static final List<String> ESTADOS_ADM_NO_PERMITIDOS = Arrays.asList("JF", "JP", "JI", "RC");
    private static final List<String> PROCEDIMIENTOS_NO_PERMITIDOS = Arrays.asList("PCD-121", "PCD-37", "PCD-106");

    
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

        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            //----------------------------------------------------------------------------------------------

            String numexp = rulectx.getNumExp();
            String extractoDecreto = "";

            List<String> expedientesRecordatorio = new ArrayList<String>();
            
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosHijos(entitiesAPI, numexp);
            
            for(String numexpHijo : expedientesRelacionados){
               
                IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo);
                
                String estadoAdm = expHijo.getString(ExpedientesUtil.ESTADOADM);           
                String codProcedimiento = expHijo.getString(ExpedientesUtil.CODPROCEDIMIENTO);
                
                if(!ESTADOS_ADM_NO_PERMITIDOS.contains(estadoAdm) && !PROCEDIMIENTOS_NO_PERMITIDOS.contains(codProcedimiento)){
                    expedientesRecordatorio.add(numexpHijo);
                }
            }

            String numexpDecreto = SubvencionesUtils.getUltimoNumexpDecreto(cct, numexp);
            extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);
           
            setSsVariables(cct, rulectx);
            
            if(!expedientesRecordatorio.isEmpty()){
                numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
                for(String numexpHijo : expedientesRecordatorio){
                    generaRecordatorio(numexpHijo, rulectx, cct, entitiesAPI, genDocAPI, extractoDecreto);
                }
            }            
            deleteSsVariables(cct);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaRecordatorio(String numexp, IRuleContext rulectx, ClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, String extractoDecreto) {
        try{
            int taskId = rulectx.getTaskId();

            if (documentTypeId != 0) {
                IItemCollection participantesCollection = ParticipantesUtil.getParticipantes( cct, numexp, ParticipantesUtil.ROL + " = '" + ParticipantesUtil._TIPO_INTERESADO + "'", ParticipantesUtil.NOMBRE);
                Iterator<?> participantesIterator = participantesCollection.iterator();
                if(participantesIterator.hasNext()){
                    IItem participante = (IItem) participantesIterator.next();
                    
                    DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                        
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL, "" + numeroTramites);
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, TramitesUtil.getNombreTramite(cct, rulectx.getTaskId()));
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);
                    
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);
                    
                    IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                    documento.store(cct);
                    
                    DocumentosUtil.borraParticipanteSsVariable(cct);

                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL);
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);
                    
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);
                    
                    if(null != refTablas && !"".equals(refTablas)){
                        String docRef = DocumentosUtil.getInfoPag(rulectx, documento.getInt("ID"));
                        insertaTablas(genDocAPI, docRef, rulectx, documento.getKeyInt(), refTablas, entitiesAPI, numexp);
                    }    
                }
            }
            cct.endTX(true);
        } catch(ISPACRuleException e){
            LOGGER.error(e.getMessage(),e);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
    
    public void setSsVariables(IClientContext cct, IRuleContext ruleContext) {
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));            
            
            String numexpConvocatoria = ruleContext.getNumExp();
                         
            //Obtenemos el asunto de la convocatoria
            String convocatoria = ExpedientesUtil.getAsunto(cct, numexpConvocatoria);

            //Obtenemos el expediente de decreto
            String numexpDecreto = SubvencionesUtils.getPrimerNumexpDecreto(cct, numexpConvocatoria);
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
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUM_INFORME, "" + TramitesUtil.cuentaTramites(cct, numexpConvocatoria, ruleContext.getTaskProcedureId()));
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.CONVOCATORIA, convocatoria);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
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
            LOGGER.error(e.getMessage(),e);
        }
    }
    
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String estadoAdm = "";
        String tituloCol4 = "";
        String ayuntamiento = "";
        String cif = "";
        String proyecto = "";
        double importe = 0;            
        String motivoDenegacion = "";
        
        try{
            if(LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                
                IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);
                
                estadoAdm = SubvencionesUtils.getString(expediente, ExpedientesUtil.ESTADOADM);
                ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                cif = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);
                
                IItem solicitud = (IItem) entitiesAPI.getEntities(ConstantesSubvenciones.DatosSolicitud.NOMBRE_TABLA, numexp).iterator().next();
                proyecto = SubvencionesUtils.getString(solicitud, ConstantesSubvenciones.DatosSolicitud.FINALIDAD);
            
                Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexp).iterator();
                if(expResolucion.hasNext()){
                    IItem resolucion = (IItem) expResolucion.next();
                    importe = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                    motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                }
                
                XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 2, 4);

                if(null != tabla){
                    if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
                        double[] distribucionColumnas = {35, 15, 40, 10};
                        LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                        tituloCol4 = ConstantesString.CabeceraTabla.IMPORTE;
                    } else{
                        double[] distribucionColumnas = {35, 15, 40, 10};
                        LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                        tituloCol4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                    }
                    
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, tituloCol4);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, cif);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, proyecto);
                    
                    if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
                         LibreOfficeUtil.setTextoCelda(tabla, 4, 2, "" + importe);
                    } else if(ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)){
                        LibreOfficeUtil.setTextoCelda(tabla, 4, 2, motivoDenegacion);
                    } else{
                        LibreOfficeUtil.setTextoCelda(tabla, 4, 2, "");
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
}