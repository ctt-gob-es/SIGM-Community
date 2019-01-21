package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConfiguracionColumna;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.FuncionesComunesDocumentos;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrNotificacionesConv3AsociGruposOtrosDatos extends FuncionesComunesDocumentos {

    public static final Logger LOGGER = Logger.getLogger(DipucrNotificacionesConv3AsociGruposOtrosDatos.class);
    
    public static final String TEXTO_APROBADO = "El Diputado Delegado del Área de Cultura, Deportes, Juventud y Participación propone la concesión de subvención, conforme al siguiente detalle: ";
    public static final String TEXTO_RECHAZADO = "El Diputado Delegado del Área de Cultura, Deportes, Juventud y Participación propone la denegación de la siguiente solicitud, por el motivo que se indica: ";
    public static final String TEXTO_RENUNCIADO = "El solicitante ha renunciado por el motivo que se indica: ";
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.RC, ExpedientesUtil.EstadoADM.RN};
    
    protected int numeroTramites = 0;
    protected String nombreTramite = "";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
            
            documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
            
            templateId = DocumentosUtil.getTemplateId(cct, plantilla, documentTypeId);

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
    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        
        try {
            // ----------------------------------------------------------------------------------------------
            IClientContext cct = rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            // ----------------------------------------------------------------------------------------------

            String numexp = rulectx.getNumExp();
            String extractoDecreto = "";
            
            List<ObjetoSolictudConvocatoriaSubvencion> expedientesResolucionGrupo1 = new ArrayList<ObjetoSolictudConvocatoriaSubvencion>();
            List<ObjetoSolictudConvocatoriaSubvencion> expedientesResolucionGrupo2 = new ArrayList<ObjetoSolictudConvocatoriaSubvencion>();
            List<ObjetoSolictudConvocatoriaSubvencion> expedientesResolucionGrupo3 = new ArrayList<ObjetoSolictudConvocatoriaSubvencion>();
            
            List<String> expedientesResolucionList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));
            
            for(String numexpHijo : expedientesResolucionList){
                ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                
                if (StringUtils.isNotEmpty(solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION))){
                    expedientesResolucionGrupo1.add(solicitudConvocatoria);
                }
                
                if (StringUtils.isNotEmpty(solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2))){
                   expedientesResolucionGrupo2.add(solicitudConvocatoria);
                }
                
                if (StringUtils.isNotEmpty(solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION3))){                        
                   expedientesResolucionGrupo3.add(solicitudConvocatoria);
                }
            }

            String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexp);
            extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);
            numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
            nombreTramite = TramitesUtil.getNombreTramite(cct, rulectx.getTaskId());
            
            for (ObjetoSolictudConvocatoriaSubvencion solicitud : expedientesResolucionGrupo1){                    
                generaNotificacion(1, solicitud, rulectx, cct, entitiesAPI, genDocAPI, extractoDecreto);
            }
            
            for (ObjetoSolictudConvocatoriaSubvencion solicitud : expedientesResolucionGrupo2){
                generaNotificacion(2, solicitud, rulectx, cct, entitiesAPI, genDocAPI, extractoDecreto);
            }

            for (ObjetoSolictudConvocatoriaSubvencion solicitud : expedientesResolucionGrupo3){  
                generaNotificacion(3, solicitud, rulectx, cct, entitiesAPI, genDocAPI, extractoDecreto);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
    
    private void generaNotificacion(int tipo, ObjetoSolictudConvocatoriaSubvencion datosSolicitud, IRuleContext rulectx, IClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, String extractoDecreto) {
        try {
            String numexp = datosSolicitud.getNumexp();
            
            cct.beginTX();
            
            IItemCollection participantesCollection = ParticipantesUtil.getParticipantes(cct, numexp, "", "");
            Iterator<?> participantesIterator = participantesCollection.iterator();
            if (participantesIterator.hasNext()) {
                IItem participante = (IItem) participantesIterator.next();
                DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL, "" + numeroTramites);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, nombreTramite);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);

                String textoResol = getTextoResolucion(tipo, datosSolicitud);

                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION, textoResol);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);

                IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, rulectx.getTaskId(), documentTypeId, templateId, numexp, "");
                documento.store(cct);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);

                if (StringUtils.isNotEmpty(refTablas)) {
                    String docRef = DocumentosUtil.getInfoPag(rulectx, documento.getInt("ID"));                                
                    insertaTablas(genDocAPI, docRef, rulectx, documento.getKeyInt(), entitiesAPI, datosSolicitud, tipo);
                }
            }
            cct.endTX(true);
        } catch (ISPACRuleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }    

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, ObjetoSolictudConvocatoriaSubvencion datosSolicitud, int tipo) {
        List<ObjetoSolictudConvocatoriaSubvencion> expedientesList = new ArrayList<ObjetoSolictudConvocatoriaSubvencion>();
        List<ConfiguracionColumna> configuracionTabla = SubvencionesUtils.getConfiguracionColumnas(rulectx, refTabla);
        
        if(datosSolicitud.esAprobado(tipo) && esTablaAprobado(refTabla) && esTablaTipoAprobado(tipo, refTabla)){
               expedientesList.add(datosSolicitud);                   
        } else if (datosSolicitud.esRechazado(tipo) && esTablaRechazado(refTabla) && esTablaTipoRechazado(tipo, refTabla)){
              expedientesList.add(datosSolicitud);
        } else if (datosSolicitud.esRenuncia(tipo) && esTablaRenuncia(refTabla) && esTablaTipoRenuncia(tipo, refTabla)){
               expedientesList.add(datosSolicitud);
        }

        SubvencionesUtils.insertaTabla(rulectx, component, refTabla, configuracionTabla, expedientesList);
    }
    
    private boolean esTablaAprobado(String refTabla) {        
        return LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA4.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA7.equals(refTabla);
    }
    
    private boolean esTablaRechazado(String refTabla) {        
        return LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA5.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA8.equals(refTabla);
    }
    
    private boolean esTablaRenuncia(String refTabla) {        
        return LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA6.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA9.equals(refTabla);
    }
    
    private boolean esTablaTipoAprobado(int tipo, String refTabla) {
        boolean esTabla1 = LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla) && 1 == tipo;
        boolean esTabla2 = LibreOfficeUtil.ReferenciasTablas.TABLA4.equals(refTabla) && 2 == tipo;
        boolean esTabla3 = LibreOfficeUtil.ReferenciasTablas.TABLA7.equals(refTabla) && 3 == tipo;
        
        return  esTabla1 || esTabla2 || esTabla3;
    }
    
    private boolean esTablaTipoRechazado(int tipo, String refTabla) {
        boolean esTabla1 = LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla) && 1 == tipo;
        boolean esTabla2 = LibreOfficeUtil.ReferenciasTablas.TABLA5.equals(refTabla) && 2 == tipo;
        boolean esTabla3 = LibreOfficeUtil.ReferenciasTablas.TABLA8.equals(refTabla) && 3 == tipo;
        
        return  esTabla1 || esTabla2 || esTabla3;
    }
    
    private boolean esTablaTipoRenuncia(int tipo, String refTabla) {
        boolean esTabla1 = LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla) && 1 == tipo;
        boolean esTabla2 = LibreOfficeUtil.ReferenciasTablas.TABLA6.equals(refTabla) && 2 == tipo;
        boolean esTabla3 = LibreOfficeUtil.ReferenciasTablas.TABLA9.equals(refTabla) && 3 == tipo;
        
        return  esTabla1 || esTabla2 || esTabla3;
    }

    private String getTextoResolucion(int tipo, ObjetoSolictudConvocatoriaSubvencion datosSolicitud) {
        String textoResolucion = TEXTO_APROBADO;
        boolean esRechazado = false;
        boolean esRenuncia = false;
        
        if(1 == tipo){
            esRechazado = datosSolicitud.esRechazadoGrupo1();
            esRenuncia = datosSolicitud.esRenunciaGrupo1();
        } else if(2 == tipo){
            esRechazado = datosSolicitud.esRechazadoGrupo2();
            esRenuncia = datosSolicitud.esRenunciaGrupo2();
        } else if(3 == tipo){
            esRechazado = datosSolicitud.esRechazadoGrupo3();
            esRenuncia = datosSolicitud.esRenunciaGrupo3();        
        }
        
        if(esRechazado){
            textoResolucion = TEXTO_RECHAZADO;
        } else if (esRenuncia){
            textoResolucion = TEXTO_RENUNCIADO;
        }

        return textoResolucion;
    }
}