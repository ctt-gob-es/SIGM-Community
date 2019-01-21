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

public class DipucrGeneraPRENotificacionesConvocatoriasSub2AsociGrupos extends FuncionesComunesDocumentos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraPRENotificacionesConvocatoriasSub2AsociGrupos.class);
    
    protected String refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2;
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.EstadoADM.RC, ExpedientesUtil.EstadoADM.RN};

    protected String plantilla = "";
    protected int templateId = 0;
    protected String tipoDocumento = "";
    protected int documentTypeId = 0;
    
    protected String extractoDecreto = "";
    
    protected String renuncia1 = "";
    protected String renuncia2 = "";

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
            
        } catch (ISPACException e) {
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
        try {
            if(templateId > 0){
                // ----------------------------------------------------------------------------------------------
                IClientContext cct = rulectx.getClientContext();
                IInvesflowAPI invesFlowAPI = cct.getAPI();
                IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
                IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
                // ----------------------------------------------------------------------------------------------
    
                String numexp = rulectx.getNumExp();
                
                List<String> expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));
    
                String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexp);
                extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);
                
                if (!expedientesResolucion.isEmpty()) {
                    for (String numexpHijo : expedientesResolucion) {
                        generaNotificacion(numexpHijo, rulectx, entitiesAPI, genDocAPI);
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaNotificacion(String numexp, IRuleContext rulectx, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI) {
        try {
            String cifAsociacion1 = "";
            String cifAsociacion2 = "";
            
            int taskId = rulectx.getTaskId();
            IClientContext cct = rulectx.getClientContext();

            cct.beginTX();
           
            IItemCollection participantesCollection = ParticipantesUtil.getParticipantes(cct, numexp, "", "");
            Iterator<?> participantesIterator = participantesCollection.iterator();
            if (participantesIterator.hasNext()) {
                IItem participante = (IItem) participantesIterator.next();
                DocumentosUtil.setParticipanteAsSsVariable(cct, participante);

                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, TramitesUtil.getNombreTramite(cct, taskId));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);
                
                IItemCollection resolucionCollection = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexp);
                Iterator<?> resolucionIterator = resolucionCollection.iterator();
                
                if(resolucionIterator.hasNext()){
                    IItem resolucion = (IItem) resolucionIterator.next();
                    
                    renuncia1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA1);
                    cifAsociacion1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                    
                    renuncia2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA2);
                    cifAsociacion2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                }
                
                String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, numexp);

                String textoResol = SubvencionesUtils.getTextoResol(cifAsociacion1, renuncia1, estadoAdm);
                String textoResol2 = SubvencionesUtils.getTextoResol(cifAsociacion2, renuncia2, estadoAdm);
                                
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION, textoResol);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION2, textoResol2);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);

                IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                documento.store(cct);
                
                DocumentosUtil.borraParticipanteSsVariable(cct);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION2);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);

                if (StringUtils.isNotEmpty(refTablas)) {
                    String docRef = DocumentosUtil.getInfoPag(rulectx, documento.getInt("ID"));
                    insertaTablas(genDocAPI, docRef, rulectx, documento.getKeyInt(), refTablas, entitiesAPI, numexp);
                }
            }
            cct.endTX(true);
        } catch (ISPACRuleException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        }
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        
        int numeroColumnas = 5;
        double [] distribucionColumnas;
        String tituloCol5 = "";
        String textoCol5 = "";

        String ayuntamiento = "";
        String nreg = "";
        String cifAsociacion = "";
        String nombreAsociacion = ""; 
        
        try {
            Object[] formatoTabla = SubvencionesUtils.getFormatoTabla(rulectx.getClientContext(), numexp, refTabla, renuncia1, renuncia2, ""); 
            
            distribucionColumnas = (double[]) formatoTabla[0];
            numeroColumnas = (Integer) formatoTabla[1];
            tituloCol5 = (String) formatoTabla[2];
            textoCol5 = (String) formatoTabla[3];
            nreg = (String) formatoTabla[4];
            ayuntamiento = (String) formatoTabla[5];
            cifAsociacion = (String) formatoTabla[6];
            nombreAsociacion = (String) formatoTabla[7];
            
            if(StringUtils.isEmpty(cifAsociacion)){
                LibreOfficeUtil.buscaPosicion(component, refTabla);                
            } else{    
                XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 2, numeroColumnas);                
                if(null != tabla){
                    LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                    
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.NUM_REGISTRO);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.CIF);
        
                    if (5 == numeroColumnas) {
                        LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloCol5);
                    }
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, nreg);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nombreAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, 2, cifAsociacion);
                    
                    if (5 == numeroColumnas) {
                        LibreOfficeUtil.setTextoCelda(tabla, 5, 2, textoCol5);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    } 
}