package es.dipucr.sigem.api.rule.procedures.ayudassociales.planvg;

import ieci.tdw.ispac.api.IEntitiesAPI;
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

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrGeneraNotificacionesPlanVG implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesPlanVG.class);
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.RC};
    
    protected String plantilla = "";
    protected String tipoDocumento = "";
    protected int documentTypeId = 0;
    protected int templateId = 0;

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
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        }

        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct = rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            String numexp = rulectx.getNumExp();            
            String extractoDecreto = "";
                        
            List<String> expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));
            
            String numexpDecreto = SubvencionesUtils.getUltimoNumexpDecreto(cct, numexp);
            extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);

            if(!expedientesResolucion.isEmpty()){
                nombreTramite = TramitesUtil.getNombreTramite(cct, rulectx.getTaskId());
                for(int i = 0;i<expedientesResolucion.size(); i++){
                    generaNotificacion(expedientesResolucion.get(i), rulectx, cct, entitiesAPI, extractoDecreto);
                }
             }            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
                
        LOGGER.info(ConstantesString.FIN +this.getClass().getName());
        return true;
    }

    private void generaNotificacion(String numexp, IRuleContext rulectx, IClientContext cct, IEntitiesAPI entitiesAPI, String extractoDecreto) {
        try{
            int taskId = rulectx.getTaskId();
            
            cct.beginTX();
            // Comprobamos que haya encontrado el Tipo de documento
            if (documentTypeId != 0) {
                    
                //Recuperamos el participante del expediente que estamos resolviendo
                IItemCollection participantesCollection = ParticipantesUtil.getParticipantes( cct, numexp, "", "");
                Iterator<?> participantesIterator = participantesCollection.iterator();
                if(participantesIterator.hasNext()){
                    IItem participante = (IItem) participantesIterator.next();
                    DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                        
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, nombreTramite);
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.DATOSSOLICITUD, getDatosResolucion(rulectx, numexp, cct, entitiesAPI));
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);
                    
                    IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                    documento.store(cct);
                    
                    DocumentosUtil.borraParticipanteSsVariable(cct);
                            
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.DATOSSOLICITUD);
                    cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);
                    
                }
                cct.endTX(true);
            }
        } catch(ISPACRuleException e){
            LOGGER.error(e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }

    public String getDatosResolucion(IRuleContext rulectx, String numexp, IClientContext cct, IEntitiesAPI entitiesAPI) throws ISPACRuleException{
        try{
            IItem expSol = ExpedientesUtil.getExpediente(cct, numexp); 
             if(expSol != null){
                 if(ExpedientesUtil.EstadoADM.AP.equals(expSol.get(ExpedientesUtil.ESTADOADM))){
                     return getDatosResolucionAprobado(rulectx, expSol);
                 } else{
                     return getDatosResolucionRechazado(rulectx, expSol);
                 }
             }            
        } catch (ISPACRuleException e){
            throw new ISPACRuleException(e);
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes", e);
        }
        return "";
    }

    private String getDatosResolucionAprobado(IRuleContext rulectx, IItem expSol) {
        StringBuilder salida = new StringBuilder();
        
        String beneficiario = "";
        String numTalleres = "";
        String motivoTalleres = "";
        String numActividades = "";
        String motivoActividades = "";
        String numBullying = "";
        String motivoBullying = "";
        
        
        try{
            ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), SubvencionesUtils.getString(expSol, ExpedientesUtil.NUMEXP));
            
            beneficiario = solicitudConvocatoria.getBeneficiario();
            
            numTalleres = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.TALLERES_EDU);                        
            motivoTalleres = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOT_RECHAZO_TALLERES_IGU);

            numActividades = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.ACTIVIDADES_INTERCULT);
            motivoActividades = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOT_RECHAZO_ACTIVIDAD_INTERCUL);
            
            numBullying = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.TALLERES_BULLYING);
            motivoBullying = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOT_RECHAZO_BULLYING);
            
            solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
            solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
            solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), rulectx.getNumExp());
            
            salida.append("\n");
            salida.append("\t" + beneficiario + "\n\n");

            salida.append("\tNúmero de talleres de educación en la igualdad: " + numTalleres + "\n");
            if(StringUtils.isNotEmpty(motivoTalleres)){
                salida.append("\tMotivo: " + motivoTalleres + "\n");                            
            }
            salida.append("\tNúmero de talleres de educación en interculturalidad: " + numActividades + "\n");
            if(StringUtils.isNotEmpty(motivoActividades)){
                salida.append("\tMotivo: " + motivoActividades + "\n");
            }
            salida.append("\tNúmero de talleres contra bullying y ciberbullying: " + numBullying + "\n");
            if(StringUtils.isNotEmpty(motivoBullying)){
                salida.append("\tMotivo: " + motivoBullying + "\n");
            }
        } catch(Exception e){
            LOGGER.debug("Ha habido algún problema al recuperar los datos de la resolución aprobada. Expediente: " + expSol + ". " + e.getMessage(), e);
        }
        return salida.toString();
    }

    private String getDatosResolucionRechazado(IRuleContext rulectx, IItem expSol) {
        StringBuilder salida = new StringBuilder();
        try{
            ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), SubvencionesUtils.getString(expSol, ExpedientesUtil.NUMEXP));
            
            String beneficiario = solicitudConvocatoria.getBeneficiario();
            String fechaSolicitud = solicitudConvocatoria.getFechaSolicitud();
            
            String sMotivo = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
            
            solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
            solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
            solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), rulectx.getNumExp());
                
            salida.append("\n");
            salida.append("\t- Así mismo se propone la denegación de los siguientes por el motivo que se indica: ");
            salida.append("\n");
            salida.append("\t1. " + beneficiario + "\n");                    

            if(StringUtils.isNotEmpty(sMotivo)){
                salida.append("Motivo: " + sMotivo + "\n");
                if(sMotivo.toUpperCase().contains("PLAZO") || sMotivo.toUpperCase().contains("FUERA")){
                    salida.append("Solicitud presentada: ");
                    salida.append(fechaSolicitud);
                }
            }
        } catch(Exception e){
            LOGGER.debug("Ha habido algún problema al recuperar lso datos de la resolución rechazada. Expediente: " + expSol + ". " + e.getMessage(), e);
        }
            
        return salida.toString();
    }
}