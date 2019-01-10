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

public class DipucrGeneraNotificacionesConvocatoriasSub2AsociGrupos extends FuncionesComunesDocumentos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesConvocatoriasSub2AsociGrupos.class);
    
    public static final double[] DISTRIBUCION_5_COLUMNAS_AP = {22, 25, 30, 13, 10};
    public static final double[] DISTRIBUCION_5_COLUMNAS_NO_AP = {22, 20, 25, 13, 20};

    protected String plantilla = "";
    protected int templateId = 0;
    protected String tipoDocumento = "";
    protected int documentTypeId = 0;
    
    protected String refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.RC};
    
    protected int numeroTramites = 0;
    protected String extractoDecreto = "";

    public static final String BANDAS = "BANDA";
    public static final String FOLCLORE = "AGRUPACIÓN";
    
    public static final String TEXTO_CACHE = "CACHÉ BANDAS";
    public static final String TEXTO_PAGO_AYTO = "PAGO DEL AYUNTAMIENTO";
    public static final String TEXTO_PAGO_DIPU = "PAGO DE LA DIPUTACIÓN";
    
    public static final String TEXTO_CABECERA_BANDAS = "Bandas de Música";
    public static final String TEXTO_CABECERA_FOLCLORE = "Agrupaciones Folclóricas";
    
    public static final String TEXTO_TIPO_A = "TIPO A) ";
    public static final String TEXTO_TIPO_B = "TIPO B) ";
    public static final String TEXTO_TIPO_C = "TIPO C) ";
    
    public static final String TEXTO_EURO = " €";


    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
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
            // ----------------------------------------------------------------------------------------------
            IClientContext cct = rulectx.getClientContext();
            // ----------------------------------------------------------------------------------------------

            String numexp = rulectx.getNumExp();            
            
            String numexpDecreto = SubvencionesUtils.getUltimoNumexpDecreto(cct, numexp);
            extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);
            
            numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));            
            for(String numexpHijo : expedientesList){
                ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                
                solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), numexp);                

                String importe = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
                String motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                String cifAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                String nombreAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                
                String[] aux = { numexpHijo, BANDAS, importe, cifAsociacion, nombreAsociacion, motivoDenegacion };
                generaNotificacion(rulectx, aux);
                    
                importe = "";
                cifAsociacion = "";
                nombreAsociacion = "";

                importe = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE2, ConstantesString.FORMATO_IMPORTE);
                motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2);
                cifAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                nombreAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);

                String[] aux2 = { numexpHijo, FOLCLORE, importe, cifAsociacion, nombreAsociacion, motivoDenegacion };                    
                generaNotificacion(rulectx, aux2);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    
    private void generaNotificacion(IRuleContext rulectx, String[] datos) {
        try {
            
            IClientContext cct = rulectx.getClientContext();
            IInvesflowAPI invesflowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesflowAPI.getGenDocAPI();
            
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
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, TramitesUtil.getNombreTramite(cct, taskId));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);

                String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, numexp);

                if (ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)) {
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION, "El Diputado Delegado del Área de Cultura, Deportes, Juventud y Participación propone la concesión de subvención, conforme al siguiente detalle: ");
                } else {
                    cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION, "El Diputado Delegado del Área de Cultura, Deportes, Juventud y Participación propone la denegación de la siguiente solicitud, por el motivo que se indica: ");
                }

                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);

                setSsVariableDatosCulturalProvincial(cct, rulectx.getNumExp(), tipo);

                IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                documento.store(cct);
                
                DocumentosUtil.borraParticipanteSsVariable(cct);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);

                deleteSsVariableDatosCulturalProvincial(cct);

                if (StringUtils.isNotEmpty(refTablas)) {
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
    
    private void deleteSsVariableDatosCulturalProvincial(IClientContext cct){
        try{
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
            
        } catch (ISPACException e){
            LOGGER.error("Error al borrar los datos de la sesión del cultural provincial. " + e.getMessage(), e);
        }
    }

    private void setSsVariableDatosCulturalProvincial(IClientContext cct, String numexp, String tipo) {
        try{
            String cabecera = "";
            String textoModificado = "";
            String textoCacheCant = "";                
            String textoPagoAyto1 = "";
            String textoPagoAyto2 = "";
            String textoPagoAyto3 = "";
            String textoPagoDipu1 = "";
            String textoPagoDipu2 = "";
            String textoPagoDipu3 = "";
    
            IItemCollection datosDineroCollection = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosCulturalProvincial.NOMBRE_TABLA, numexp);
    
            Iterator<?> datosDineroIterator = datosDineroCollection.iterator();
            if (datosDineroIterator.hasNext()) {
                IItem datosDinero = (IItem) datosDineroIterator.next();
    
                if (tipo.equals(BANDAS)) {
                    cabecera = TEXTO_CABECERA_BANDAS;
                    textoModificado = SubvencionesUtils.getString(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_MODIFICADO1);
                    textoCacheCant = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_CACHE_CANT1);
                    textoPagoAyto1 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO11);
                    textoPagoAyto2 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO12);
                    textoPagoAyto3 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO13);
                    textoPagoDipu1 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU11);
                    textoPagoDipu2 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU12);
                    textoPagoDipu3 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU13);
                } else if (tipo.equals(FOLCLORE)) {
                    cabecera = TEXTO_CABECERA_FOLCLORE;
                    textoModificado = datosDinero.getString(ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_MODIFICADO2);
                    textoCacheCant = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_CACHE_CANT2);
                    textoPagoAyto1 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO21);
                    textoPagoAyto2 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO22);
                    textoPagoAyto3 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_AYTO23);
                    textoPagoDipu1 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU21);
                    textoPagoDipu2 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU22);
                    textoPagoDipu3 = SubvencionesUtils.getFormattedDouble(datosDinero, ConstantesSubvenciones.DatosCulturalProvincial.TEXTO_PAGO_DIPU23);
                }
            }
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.CABECERA, cabecera);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_MODIFICACION, textoModificado);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_CACHE, TEXTO_CACHE);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOCACHECANT, textoCacheCant + TEXTO_EURO);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO, TEXTO_PAGO_AYTO);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO1, TEXTO_TIPO_A + textoPagoAyto1 + TEXTO_EURO);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO2, TEXTO_TIPO_B + textoPagoAyto2 + TEXTO_EURO);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGOAYTO3, TEXTO_TIPO_C + textoPagoAyto3 + TEXTO_EURO);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU, TEXTO_PAGO_DIPU);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU1, textoPagoDipu1 + TEXTO_EURO);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU2, textoPagoDipu2 + TEXTO_EURO);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTOPAGODIPU3, textoPagoDipu3 + TEXTO_EURO);
            
        } catch (ISPACException e){
            LOGGER.error("Error al asignar los datos de la sesión del cultural provincial. " + e.getMessage(), e);
        }
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String[] datos) {
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS_AP;
        String tituloCol4 = "";
        String textoCol5 = "";
         
        String numexp = "";
        String importe = "";
        String estadoAdm = "";
        String ayuntamiento = "";
        String nreg = "";
        String motivoDenegacion = "";
        String cifAsociacion = "";
        String nombreAsociacion = "";

        try {
            numexp = datos[0];
            importe = datos[2];
            cifAsociacion = datos[3];
            nombreAsociacion = datos[4];
            motivoDenegacion = datos[5];

            IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);
            estadoAdm = SubvencionesUtils.getString(expediente, ExpedientesUtil.ESTADOADM);

            if (ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)) {
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_AP;
                tituloCol4 = ConstantesString.CabeceraTabla.IMPORTE;
                textoCol5 = importe;
            } else {
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_NO_AP;
                tituloCol4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                textoCol5 = motivoDenegacion;
            }

            ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
            nreg = SubvencionesUtils.getString(expediente, ExpedientesUtil.NREG);

            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 2, 5);
            if(null != tabla){
                
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.NUM_REGISTRO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloCol4);
                
                LibreOfficeUtil.setTextoCelda(tabla, 1, 2, nreg);
                LibreOfficeUtil.setTextoCelda(tabla, 2, 2, ayuntamiento);
                LibreOfficeUtil.setTextoCelda(tabla, 3, 2, nombreAsociacion);
                LibreOfficeUtil.setTextoCelda(tabla, 4, 2, cifAsociacion);
                LibreOfficeUtil.setTextoCelda(tabla, 5, 2, textoCol5);              
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}