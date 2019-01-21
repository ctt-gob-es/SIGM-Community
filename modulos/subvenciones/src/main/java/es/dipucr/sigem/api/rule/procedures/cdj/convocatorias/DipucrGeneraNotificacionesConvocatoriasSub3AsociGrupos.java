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
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrGeneraNotificacionesConvocatoriasSub3AsociGrupos extends DipucrDatosResolAprobConvocatoriasDatosAsociGrupoEELL {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesConvocatoriasSub3AsociGrupos.class);

    protected String refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.RC, ExpedientesUtil.EstadoADM.RN};
    
    protected int numeroTramites = 0;
    protected String nombreTramite = "";
    
    protected int templateId = 0;
    protected int documentTypeId = 0;
    protected String tipoDocumento = "";
    protected String plantilla = "";
    
    public static final String BANDAS = "BANDA";
    public static final String FOLCLORE = "AGRUPACIÓN";
    public static final String POPROCK = "POP-ROCK";    
    
    public static final String TEXTO_CACHE = "CACHÉ BANDAS";
    public static final String TEXTO_PAGO_AYTO = "PAGO DEL AYUNTAMIENTO";
    public static final String TEXTO_PAGO_DIPU = "PAGO DE LA DIPUTACIÓN";
    
    public static final String TEXTO_CABECERA_BANDAS = "Bandas de Música";
    public static final String TEXTO_CABECERA_FOLCLORE = "Agrupaciones Folclóricas";
    public static final String TEXTO_CABECERA_POPROCK = "Pop-Rock";
    
    public static final String TEXTO_TIPO_A = "TIPO A) ";
    public static final String TEXTO_TIPO_B = "TIPO B) ";
    public static final String TEXTO_TIPO_C = "TIPO C) ";
    
    public static final String TEXTO_EURO = " €";

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
            
            List<String[]> expedientesResolucion = new ArrayList<String[]>();
            
            List<String> expedientesResolucionList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));
            
            for(String numexpHijo : expedientesResolucionList){
                
                ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                
                solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), rulectx.getNumExp());
                
                double importe = 0;
                String cifAsociacion = "";
                String motivoDenegacion = "";
                String motivoRenuncia = "";
                String nombreAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                
                if (StringUtils.isNotEmpty(nombreAsociacion)){
                    importe += solicitudConvocatoria.getImporte1();
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    motivoRenuncia = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
                    cifAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);

                    String[] aux = { numexpHijo, BANDAS, "" + importe, cifAsociacion, nombreAsociacion, motivoDenegacion, motivoRenuncia};
                    expedientesResolucion.add(aux);
                }
                
                importe = 0;
                cifAsociacion = "";
                motivoDenegacion = "";
                motivoRenuncia = "";
                nombreAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);
                
                if (StringUtils.isNotEmpty(nombreAsociacion)){
                    importe += solicitudConvocatoria.getImporte2();
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2);
                    motivoRenuncia = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA2);
                    cifAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);

                    String[] aux2 = { numexpHijo, FOLCLORE, "" + importe, cifAsociacion, nombreAsociacion, motivoDenegacion, motivoRenuncia};
                    expedientesResolucion.add(aux2);
                }
                
                importe = 0;
                cifAsociacion = "";
                motivoDenegacion = "";
                motivoRenuncia = "";
                nombreAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION3);
                
                if (StringUtils.isNotEmpty(nombreAsociacion)){
                    importe += solicitudConvocatoria.getImporte3();
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO3);                                    
                    motivoRenuncia = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA3);                                    
                    cifAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION3);
                    
                    String[] aux3 = { numexpHijo, POPROCK, "" + importe, cifAsociacion, nombreAsociacion, motivoDenegacion, motivoRenuncia};
                    expedientesResolucion.add(aux3);
                }
            }

            String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexp);
            extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);
            
            if (!expedientesResolucion.isEmpty()) {
                numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
                nombreTramite = TramitesUtil.getNombreTramite(cct, rulectx.getTaskId());
                for (int i = 0; i < expedientesResolucion.size(); i++){                    
                    generaNotificacion(expedientesResolucion.get(i), rulectx, cct, entitiesAPI, genDocAPI, extractoDecreto);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
    
    private void generaNotificacion(String[] datos, IRuleContext rulectx, IClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, String extractoDecreto) {
        try {
            int taskId = rulectx.getTaskId();

            String numexp = datos[0];
            String tipo = datos[1];

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

                String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, numexp);

                String textoResol = "";

                if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
                    textoResol = "El Diputado Delegado del Área de Cultura, Deportes, Juventud y Participación propone la concesión de subvención, conforme al siguiente detalle: ";
                } else if(ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)){
                    textoResol = "El Diputado Delegado del Área de Cultura, Deportes, Juventud y Participación propone la denegación de la siguiente solicitud, por el motivo que se indica: ";
                } else if(ExpedientesUtil.EstadoADM.RN.equals(estadoAdm)){
                    textoResol = "Ayuntamientos que renuncian: ";
                } else {
                    textoResol = "";
                }

                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION, textoResol);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);

                String cabecera = "";
                String textoModificado = "";
                String textoCache = "";
                String textoCacheCant = "";
                String textoPagoAyto = "";
                String textoPagoAyto1 = "";
                String textoPagoAyto2 = "";
                String textoPagoAyto3 = "";
                String textoPagoDipu = "";
                String textoPagoDipu1 = "";
                String textoPagoDipu2 = "";
                String textoPagoDipu3 = "";

                IItemCollection datosDineroCollection = entitiesAPI.getEntities(ConstantesSubvenciones.DatosCulturalProvincial.NOMBRE_TABLA, rulectx.getNumExp());

                Iterator<?> datosDineroIterator = datosDineroCollection.iterator();
                if (datosDineroIterator.hasNext()) {
                    IItem datosDinero = (IItem) datosDineroIterator.next();

                    textoCache = TEXTO_CACHE;
                    textoPagoAyto = TEXTO_PAGO_AYTO;
                    textoPagoDipu = TEXTO_PAGO_DIPU;

                    if (tipo.equals(BANDAS)) {
                        cabecera = TEXTO_CABECERA_BANDAS;
                        textoModificado = SubvencionesUtils.getString(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_MODIFICADO1);
                        textoCacheCant = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_CACHE_CANT1) + " €";
                        textoPagoAyto1 = TEXTO_TIPO_A + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO11) + " €";
                        textoPagoAyto2 = TEXTO_TIPO_B + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO12) + " €";
                        textoPagoAyto3 = TEXTO_TIPO_C + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO13) + " €";
                        textoPagoDipu1 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU11) + " €";
                        textoPagoDipu2 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU12) + " €";
                        textoPagoDipu3 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU13) + " €";
                    } else if (tipo.equals(FOLCLORE)) {
                        cabecera = TEXTO_CABECERA_FOLCLORE;
                        textoModificado = SubvencionesUtils.getString(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_MODIFICADO2);
                        textoCacheCant = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_CACHE_CANT2) + " €";
                        textoPagoAyto1 = TEXTO_TIPO_A + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO21) + " €";
                        textoPagoAyto2 = TEXTO_TIPO_B + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO22) + " €";
                        textoPagoAyto3 = TEXTO_TIPO_C + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO23) + " €";
                        textoPagoDipu1 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU21) + " €";
                        textoPagoDipu2 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU22) + " €";
                        textoPagoDipu3 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU23) + " €";
                    } else if (tipo.equals(POPROCK)) {
                        cabecera = TEXTO_CABECERA_POPROCK;
                        textoModificado = SubvencionesUtils.getString(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_MODIFICADO3);
                        textoCacheCant = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_CACHE_CANT3) + " €";
                        textoPagoAyto1 = TEXTO_TIPO_A + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO31) + " €";
                        textoPagoAyto2 = TEXTO_TIPO_B + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO32) + " €";
                        textoPagoAyto3 = TEXTO_TIPO_C + SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO33) + " €";
                        textoPagoDipu1 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU31) + " €";
                        textoPagoDipu2 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU32) + " €";
                        textoPagoDipu3 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU33) + " €";
                    }
                }
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.CABECERA, cabecera);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_MODIFICACION, textoModificado);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_CACHE, textoCache);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOCACHECANT, textoCacheCant);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO, textoPagoAyto);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO1, textoPagoAyto1);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO2, textoPagoAyto2);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO3, textoPagoAyto3);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU, textoPagoDipu);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU1, textoPagoDipu1);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU2, textoPagoDipu2);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU3, textoPagoDipu3);

                IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                documento.store(cct);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.CABECERA);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_MODIFICACION);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_CACHE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOCACHECANT);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO1);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO2);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO3);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU1);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU2);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU3);

                if (null != refTablas && !"".equals(refTablas)) {
                    String docRef = DocumentosUtil.getInfoPag(rulectx, documento.getInt("ID"));                                
                    insertaTablas(genDocAPI, docRef, rulectx, documento.getKeyInt(), refTablas, entitiesAPI, datos);
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

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String[] datos) {
        String ayuntamiento = "";
        String nreg = "";
        String motivoDenegacion = "";
        String motivoRenuncia = "";
        String cifAsociacion = "";
        String nombreAsociacion = "";
        
        String tituloCol4 = "";
        String textoCol4 = "";

        try {
            String numexp = datos[0];
            String importe = datos[2];
            cifAsociacion = datos[3];
            nombreAsociacion = datos[4];
            motivoDenegacion = datos[5];
            motivoRenuncia = datos[6];
            
            boolean rechazado = StringUtils.isNotEmpty(motivoDenegacion);
            boolean renuncia = StringUtils.isNotEmpty(motivoRenuncia);

            IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);

            ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
            nreg = SubvencionesUtils.getString(expediente, ExpedientesUtil.NREG);

            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 2, 5);
            if(null != tabla){
                double[] distribucionColumnas = {22, 25, 25, 13, 15};
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                if (rechazado) {
                    tituloCol4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                    textoCol4 = motivoDenegacion;
                } else if (renuncia){
                    tituloCol4 = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
                    textoCol4 = motivoRenuncia;
                } else {
                    tituloCol4 = ConstantesString.CabeceraTabla.IMPORTE;
                    textoCol4 = importe;
                }  

                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.NUM_REGISTRO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloCol4);
                
                LibreOfficeUtil.setTextoCelda(tabla, 1, 2, nreg);
                LibreOfficeUtil.setTextoCelda(tabla, 2, 2, ayuntamiento);
                LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nombreAsociacion);
                LibreOfficeUtil.setTextoCelda(tabla, 4, 2, cifAsociacion);
                LibreOfficeUtil.setTextoCelda(tabla, 5, 2, textoCol4);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}