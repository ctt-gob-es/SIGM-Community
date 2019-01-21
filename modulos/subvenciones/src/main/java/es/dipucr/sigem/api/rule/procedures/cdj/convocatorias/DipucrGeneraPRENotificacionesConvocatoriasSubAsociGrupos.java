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

public class DipucrGeneraPRENotificacionesConvocatoriasSubAsociGrupos extends FuncionesComunesDocumentos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraPRENotificacionesConvocatoriasSubAsociGrupos.class);

    protected String refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;

    protected String plantilla = "";
    protected int templateId = 0;
    protected String tipoDocumento = "";
    protected int documentTypeId = 0;
    
    protected String extractoDecreto = "";
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.EstadoADM.RC, ExpedientesUtil.EstadoADM.RN};
    
    protected int numeroTramites = 0;

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
                    numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
                    for (int i = 0; i < expedientesResolucion.size(); i++) {
                        generaNotificacion(expedientesResolucion.get(i), rulectx, entitiesAPI, genDocAPI);
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

                String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, numexp);

                String textoResol = "";

                if (ExpedientesUtil.EstadoADM.RS.equals(estadoAdm)) {
                    textoResol = "Esta Vicepresidencia propone la concesión de subvención, conforme al siguiente detalle: ";
                } else if (ExpedientesUtil.EstadoADM.RN.equals(estadoAdm)) {
                    textoResol = "Ayuntamientos que han renunciado: ";
                } else {
                    textoResol = "Esta Vicepresidencia propone la denegación de la siguiente solicitud, por el motivo que se indica: ";
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
                
                if (StringUtils.isNotEmpty(refTablas)) {
                    String docRef = DocumentosUtil.getInfoPag(rulectx, documento.getInt("ID"));
                    insertaTablas(genDocAPI, docRef, rulectx, documento.getKeyInt(), refTablas, entitiesAPI, numexp);
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

    public void insertaTabla(IRuleContext rulectx, XComponent component, IEntitiesAPI entitiesAPI, String numexp) {
        String ayuntamiento = "";
        String nreg = "";
        String motivoDenegacion = "";
        String motivoRenuncia = "";
        String cifAsociacion = "";
        String nombreAsociacion = "";
        String tituloCol4 = "";
        
        try {
            String estadoAdm = "";
            IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);
            estadoAdm = SubvencionesUtils.getString(expediente, ExpedientesUtil.ESTADOADM);

            ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
            nreg = SubvencionesUtils.getString(expediente, ExpedientesUtil.NREG);

            double importe = 0;

            motivoDenegacion = "";
            motivoRenuncia = "";

            Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexp).iterator();
            if (expResolucion.hasNext()) {
                IItem resolucion = (IItem) expResolucion.next();
                importe += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);

                motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                cifAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                nombreAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                motivoRenuncia = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
            }

            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, LibreOfficeUtil.ReferenciasTablas.TABLA1, 2, 5);
            if(null != tabla){
                if (ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)) {
                    tituloCol4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                    double [] distribucionColumnas = {10, 30, 30, 10, 20};
                    LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                }else if (ExpedientesUtil.EstadoADM.RN.equals(estadoAdm)) {
                    tituloCol4 = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
                    double [] distribucionColumnas = {10, 30, 30, 10, 20};
                    LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                } else {
                    tituloCol4 = ConstantesString.CabeceraTabla.IMPORTE;
                    double [] distribucionColumnas = {15, 30, 30, 15, 10};
                    LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                }
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.NUM_REGISTRO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloCol4);
    
                if (ExpedientesUtil.EstadoADM.RS.equals(estadoAdm)) {
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, nreg);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nombreAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, 2, cifAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 5, 2, "" + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe));
    
                } else if (ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)) {
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, nreg);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nombreAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, 2, cifAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 5, 2, motivoDenegacion);
                } else if (ExpedientesUtil.EstadoADM.RN.equals(estadoAdm)) {
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, nreg);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nombreAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, 2, cifAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 5, 2, motivoRenuncia);
                }else {
                    LibreOfficeUtil.setTextoCelda(tabla, 1, 2, "");
                    LibreOfficeUtil.setTextoCelda(tabla, 2, 2, "");
                    LibreOfficeUtil.setTextoCelda(tabla, 3, 2, "");
                    LibreOfficeUtil.setTextoCelda(tabla, 4, 2, "");
                    LibreOfficeUtil.setTextoCelda(tabla, 5, 2, "");
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}