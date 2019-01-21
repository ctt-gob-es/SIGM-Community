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

public class DipucrGeneraNotificacionesConvocatoriasSub extends FuncionesComunesDocumentos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesConvocatoriasSub.class);
    
    public static final double[] DISTRIBUCION_4_COLUMNAS_AP = {35, 15, 40, 10};
    public static final double[] DISTRIBUCION_4_COLUMNAS_RC = {35, 15, 25, 25};
    public static final double[] DISTRIBUCION_4_COLUMNAS_RN = {35, 15, 25, 25};
    
    public static final double[] DISTRIBUCION_5_COLUMNAS_RS = {30, 15, 25, 20, 10};
    public static final double[] DISTRIBUCION_5_COLUMNAS_RC = {30, 10, 20, 20, 20};
    public static final double[] DISTRIBUCION_5_COLUMNAS_RN = {30, 10, 20, 20, 20};

    protected String plantilla = "";
    protected int templateId = 0;
    protected String tipoDocumento = "";
    protected int documentTypeId = 0;
    
    private boolean conModalidadCategoria = false;
    
    protected String refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.RC, ExpedientesUtil.EstadoADM.RN};

    protected int numeroTramites = 0;
    protected String nombreTramite = "";
    
    protected String entidadCabecera = "";
    
    public void inicializaCampos(){
        entidadCabecera = ConstantesString.CabeceraTabla.AYUNTAMIENTO;
    }
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try{
            IClientContext cct = rulectx.getClientContext();
            
            conModalidadCategoria = "TRUE".equalsIgnoreCase(TramitesUtil.getPropiedadDatosEspecificos(cct, rulectx.getTaskProcedureId(), ConstantesSubvenciones.DatosEspecificosOtrosDatos.PROPIEDAD_MODALIDAD));
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
            documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);

            templateId = DocumentosUtil.getTemplateId(cct, plantilla, documentTypeId);
            
            inicializaCampos();
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
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            //----------------------------------------------------------------------------------------------
            
            String numexp = rulectx.getNumExp();            
            String extractoDecreto = "";
                        
            List<String> expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));
            
            String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexp);
            extractoDecreto = DecretosUtil.getExtractoDecreto(cct, numexpDecreto);
             
            if(!expedientesResolucion.isEmpty()){
                numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
                nombreTramite = TramitesUtil.getNombreTramite(cct, rulectx.getTaskId());
                for(String expedienteResolucion : expedientesResolucion){
                    generaNotificacion(expedienteResolucion, rulectx, cct, entitiesAPI, genDocAPI, extractoDecreto);
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaNotificacion(String numexp, IRuleContext rulectx, ClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, String extractoDecreto) {
        try{
            int taskId = rulectx.getTaskId();
           
            String estadoAdm = "";
            String textoResol = "";
            
            cct.beginTX();
            
            IItemCollection participantesCollection = ParticipantesUtil.getParticipantes( cct, numexp);
            Iterator<?> participantesIterator = participantesCollection.iterator();
            
            if(participantesIterator.hasNext()){
                IItem participante = (IItem) participantesIterator.next();
                                
                DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL, "" + numeroTramites );
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, nombreTramite);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);
                
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
                estadoAdm = SubvencionesUtils.getString(expediente, ExpedientesUtil.ESTADOADM);
                
                textoResol = getTextoResolucion(estadoAdm);
                
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION, textoResol);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO, extractoDecreto);
            
                IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                documento.store(cct);
                        
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);
                
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_RESOLUCION);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.EXTRACTO_DECRETO);
                
                if(StringUtils.isNotEmpty(refTablas)){  
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
        
        double[] distribucionColumnas = DISTRIBUCION_4_COLUMNAS_AP;
        
        String estadoAdm = "";
        String beneficiario = "";
        String nifCifTitular = "";
        String proyecto = "";
        
        String tituloColumna4 = "";
        String textoColumna4 = "";
        String tituloColumna5 = "";
        String textoColumna5 = "";
        int numColumnas = 4;
        
        try{
            ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), numexp);

            estadoAdm = solicitudConvocatoria.getCampoExpediente(ExpedientesUtil.ESTADOADM);
            beneficiario = solicitudConvocatoria.getBeneficiario();
            nifCifTitular = solicitudConvocatoria.getNifCifTitular();

            proyecto = solicitudConvocatoria.getCampoSolicitud(ConstantesSubvenciones.DatosSolicitud.FINALIDAD);
            
            solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
            solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
            solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), rulectx.getNumExp());

            if(!conModalidadCategoria){
                Object[] configuracion = configuracion4Columnas(estadoAdm, solicitudConvocatoria);
                
                distribucionColumnas = (double[]) configuracion[0];
                tituloColumna4 = (String) configuracion[1];
                textoColumna4 = (String) configuracion[2];
                
                numColumnas = 4;
                
            } else {
                Object[] configuracion = configuracion5Columnas(estadoAdm, solicitudConvocatoria);
                
                distribucionColumnas = (double[]) configuracion[0];
                tituloColumna4 = (String) configuracion[1];
                textoColumna4 = (String) configuracion[2];
                tituloColumna5 = (String) configuracion[3];
                textoColumna5 = (String) configuracion[4];
                
                numColumnas = 5;
            }
            
            //Esta tabla tendrá únicamente 2 filas una para la cabecera y otra para los datos y 4 columnas
            XTextTable  tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 2, numColumnas);
            if(null != tabla){     
                
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                    
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, entidadCabecera);    
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);  
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, tituloColumna4);
                if(conModalidadCategoria){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloColumna5);
                }
                
                LibreOfficeUtil.setTextoCelda(tabla, 1, 2, beneficiario);
                LibreOfficeUtil.setTextoCelda(tabla, 2, 2, nifCifTitular);
                LibreOfficeUtil.setTextoCelda(tabla, 3, 2, proyecto);
                LibreOfficeUtil.setTextoCelda(tabla, 4, 2, textoColumna4);
                if(conModalidadCategoria){
                    LibreOfficeUtil.setTextoCelda(tabla, 5, 2, textoColumna5);
                }
            }
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar la tabla. " + e.getMessage(), e);
        }
    }
    
    public Object[] configuracion4Columnas(String estadoAdm, ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria) {
        
        Object[] configuracion = new Object[3];
        
        double[] distribucionColumnas = DISTRIBUCION_4_COLUMNAS_AP;
        String tituloColumna4 = ConstantesString.CabeceraTabla.IMPORTE;
        String textoColumna4 = "";
        
        if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_AP;                    
            tituloColumna4 = ConstantesString.CabeceraTabla.IMPORTE;
            textoColumna4 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
            
        } else if(ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)){
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RC;
            tituloColumna4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
            textoColumna4 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
            
        }else if(ExpedientesUtil.EstadoADM.RN.equals(estadoAdm)){
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RN;
            tituloColumna4 = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
            textoColumna4 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
            
        } else{
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_AP;                    
            tituloColumna4 = "";
            textoColumna4 = "";
        }
                
        configuracion[0] = distribucionColumnas;
        configuracion[1] = tituloColumna4;
        configuracion[2] = textoColumna4;
        
        return configuracion;
    }
    
    public Object[] configuracion5Columnas(String estadoAdm, ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria) {
        
        Object[] configuracion = new Object[5];
        
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
        String tituloColumna4 = ConstantesString.CabeceraTabla.IMPORTE;
        String textoColumna4 = "";
        String tituloColumna5 = ConstantesString.CabeceraTabla.IMPORTE;
        String textoColumna5 = "";
        
        if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;                    
            tituloColumna4 = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
            textoColumna4 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MODALIDAD1);
            tituloColumna5 = ConstantesString.CabeceraTabla.IMPORTE;
            textoColumna5 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
            
        } else if(ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)){
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RC;
            tituloColumna4 = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
            textoColumna4 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MODALIDAD1);
            tituloColumna5 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
            textoColumna5 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
            
        }else if(ExpedientesUtil.EstadoADM.RN.equals(estadoAdm)){
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RN;
            tituloColumna4 = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
            textoColumna4 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MODALIDAD1);
            tituloColumna5 = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
            textoColumna5 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
            
        } else{
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;                    
            tituloColumna4 = "";
            textoColumna4 = "";
            tituloColumna5 = "";
            textoColumna5 = "";
        }
                
        configuracion[0] = distribucionColumnas;
        configuracion[1] = tituloColumna4;
        configuracion[2] = textoColumna4;        
        configuracion[3] = tituloColumna5;
        configuracion[4] = textoColumna5;
        
        return configuracion;
    }

    public String getTextoResolucion(String estadoAdm){
        String textoResol;
        
        if(ExpedientesUtil.EstadoADM.AP.equals(estadoAdm)){
            textoResol = "La concesión de subvención, conforme al siguiente detalle: ";
        } else if(ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)){
            textoResol = "La denegación de la siguiente solicitud, por el motivo que se indica: ";
        } else if(ExpedientesUtil.EstadoADM.RN.equals(estadoAdm)){
            textoResol = "Ayuntamientos que renuncian: ";
        } else {
            textoResol = "";
        }
        
        return textoResol;
    }
}