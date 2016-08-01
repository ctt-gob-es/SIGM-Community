package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.ParagraphVertAlign;
import com.sun.star.text.TableColumnSeparator;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraRecordatorioJustConvocatoriasSub implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraRecordatorioJustConvocatoriasSub.class);

    protected String tramite = "Recordatorio Justificación";
    protected String tipoDocumento = "";
    protected String refTablas = "%TABLA1%";
    
    protected String plantilla = "";
    
    private final static List<String> ESTADOS_ADM_NO_PERMITIDOS = Arrays.asList("JF", "JP", "JI", "RC");
    private final static List<String> PROCEDIMIENTOS_NO_PERMITIDOS = Arrays.asList("PCD-121", "PCD-37", "PCD-106");

    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {    
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try{
            IClientContext cct = rulectx.getClientContext();
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }

        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
            //----------------------------------------------------------------------------------------------
            
            String numexp = rulectx.getNumExp();            
            String extractoDecreto = "";
                        
            ArrayList<String> expedientesRecordatorio = new ArrayList<String>();            
            
            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
             IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
             Iterator<?> expRelIt = expRelCol.iterator();                  
             if(expRelIt.hasNext()){
                 while (expRelIt.hasNext()){
                     IItem expRel = (IItem)expRelIt.next();
                     String numexpHijo = expRel.getString("NUMEXP_HIJO");
                     IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo);
                     String estadoAdm = expHijo.getString("ESTADOADM");
                     String codProcedimiento = expHijo.getString("CODPROCEDIMIENTO");
                     if(null != expHijo && !ESTADOS_ADM_NO_PERMITIDOS.contains(estadoAdm) && !PROCEDIMIENTOS_NO_PERMITIDOS.contains(codProcedimiento)){
                         expedientesRecordatorio.add(numexpHijo);
                     }                    
                 }
             }    
             
             strQuery = "WHERE NUMEXP_PADRE='" + numexp + "' ORDER BY NUMEXP_HIJO DESC";
            IItemCollection expRelCol2 = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt2 = expRelCol2.iterator();                  
            if(expRelIt2.hasNext()){
                while (expRelIt2.hasNext() && "".equals(extractoDecreto)){
                    IItem expRel2 = (IItem)expRelIt2.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String numexpHijo = expRel2.getString("NUMEXP_HIJO");
                    
                    if("".equals(extractoDecreto)){
                        IItemCollection expHijoCol2 = entitiesAPI.getEntities("SGD_DECRETO", numexpHijo); 
                        Iterator<?> expHijoIt = expHijoCol2.iterator();
                        if(expHijoIt.hasNext()){
                            extractoDecreto = ((IItem)expHijoIt.next()).getString("EXTRACTO_DECRETO");
                        }
                    }
                }
            }
           
            setSsVariables(cct, rulectx);
            
            if(!expedientesRecordatorio.isEmpty()){
                for(int i = 0;i<expedientesRecordatorio.size(); i++){
                    generaRecordatorio(expedientesRecordatorio.get(i), rulectx, cct, entitiesAPI, genDocAPI, procedureAPI, extractoDecreto);
                }
            }            
            deleteSsVariables(cct);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaRecordatorio(String numexp, IRuleContext rulectx, ClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, IProcedureAPI procedureAPI, String extractoDecreto) {
        try{
            IItem entityDocument = null;
            int documentId = 0;
            int documentTypeId = 0;
            int templateId = 0;
            int taskId = rulectx.getTaskId();
            Object connectorSession = null;
            String sFileTemplate = null;
            
            String nombre = "";
            String dirnot = "";
            String cPostal = "";
            String localidad = "";
            String caut = "";
            String recurso = "";
            String idExt = "";                
                       
            connectorSession = genDocAPI.createConnectorSession();
            
            IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");
            
            IItemCollection taskTpDocCollection = (IItemCollection) procedureAPI.getTaskTpDoc(idTramCtl);
            Iterator<?> it = taskTpDocCollection.iterator();
            while (it.hasNext()) {
                IItem taskTpDoc = (IItem) it.next();
                if (( taskTpDoc.getString("CT_TPDOC:NOMBRE").trim()).equalsIgnoreCase((tipoDocumento).trim())) {
                    documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                }
            }
            cct.beginTX();
            if (documentTypeId != 0) {
                IItemCollection tpDocsTemplatesCollection = (IItemCollection) procedureAPI
                        .getTpDocsTemplates(documentTypeId);
                if (tpDocsTemplatesCollection == null
                        || tpDocsTemplatesCollection.toList().isEmpty()) {
                    throw new ISPACInfo(Messages
                            .getString(ConstantesString.LOGGER_ERROR + ".decretos.acuses.tpDocsTemplates"));
                } else {
                    Iterator<?> docs = tpDocsTemplatesCollection.iterator();
                    boolean encontrado = false;
                    while (docs.hasNext() && !encontrado) {
                        IItem tpDocsTemplate = (IItem) docs.next();
                        if (((String) tpDocsTemplate.get("NOMBRE")).trim().equalsIgnoreCase(plantilla.trim())) {
                            templateId = tpDocsTemplate.getInt("ID");
                            encontrado = true;
                        }
                    }
                
                    IItemCollection tramitesCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, "WHERE NUMEXP='" +rulectx.getNumExp()+"' AND NOMBRE='" +tramite+"'"); 
                    List<?> tramitesList = tramitesCollection.toList();            
                    
                    //Recuperamos el participante del expediente que estamos resolviendo
                    IItemCollection participantesCollection = ParticipantesUtil.getParticipantes( cct, numexp, "ROL='INT'", "NOMBRE");
                    Iterator<?> participantesIterator = participantesCollection.iterator();
                    if(participantesIterator.hasNext()){
                        IItem participante = (IItem) participantesIterator.next();
                        if (participante!=null){                    
                            // Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
                            if ((String)participante.get("NOMBRE")!=null){
                                nombre = (String)participante.get("NOMBRE");
                            } else{
                                nombre = "";
                            }
                            if ((String)participante.get("DIRNOT")!=null){
                                dirnot = (String)participante.get("DIRNOT");
                            } else{
                                dirnot = "";
                            }
                            if ((String)participante.get("C_POSTAL")!=null){
                                cPostal = (String)participante.get("C_POSTAL");
                            } else{
                                cPostal = "";
                            }
                            if ((String)participante.get("LOCALIDAD")!=null){
                                localidad = (String)participante.get("LOCALIDAD");
                            } else{
                                localidad = "";
                            }
                            if ((String)participante.get("CAUT")!=null){
                                caut = (String)participante.get("CAUT");
                            } else{
                                caut = "";
                            }
                            if ((String)participante.get("RECURSO")!=null){
                                recurso = (String)participante.get("RECURSO");
                            } else{
                                recurso = "";
                            }
                            /**
                             * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
                             * **/
                            if ((String)participante.get("ID_EXT")!=null){
                                idExt = (String)participante.get("ID_EXT");
                            } else{
                                idExt = "";
                            }
                            /**
                             * FIN[Teresa] Ticket#106#: añadir el campo id_ext
                             * **/                            
            
                            // Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
                            String sqlQueryPart = "WHERE VALOR = '" +recurso+"'";
                            IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
                            if (colRecurso.iterator().hasNext()){
                                IItem iRecurso = (IItem)colRecurso.iterator().next();
                                recurso = iRecurso.getString("SUSTITUTO");
                            }
                            
                            if ("".equals(recurso)){
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
                            } else{
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
                            }
                            
                            cct.setSsVariable("NRESOLUCIONPARCIAL", "" +(tramitesList.size()));
                            cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
                            cct.setSsVariable("NOMBRETRABAJADOR", nombre);
                            cct.setSsVariable("NOMBREBENEFICIARIO", idExt);
                            cct.setSsVariable("NUMEXPSOLICITUD", numexp);
                            cct.setSsVariable("RECURSO", recurso);
                            
                            cct.setSsVariable("NOMBRE", nombre);
                            cct.setSsVariable("DIRNOT", dirnot);
                            cct.setSsVariable("C_POSTAL", cPostal);
                            cct.setSsVariable("LOCALIDAD", localidad);
                            cct.setSsVariable("CAUT", caut);
                                                        
                            cct.setSsVariable("EXTRACTO_DECRETO", extractoDecreto);
                        
                            connectorSession = genDocAPI.createConnectorSession();
                            
                            IItem entityDocumentT = genDocAPI.createTaskDocument(
                                    taskId, documentTypeId);
                            int documentIdT = entityDocumentT.getKeyInt();
        
                            IItem entityTemplateT = genDocAPI.attachTaskTemplate(
                                    connectorSession, taskId, documentIdT, templateId);
                            
                            String infoPagT = entityTemplateT.getString("INFOPAG");
                            entityTemplateT.store(cct);
        
                            entityDocument = genDocAPI.createTaskDocument(taskId,
                                    documentTypeId);
                            documentId = entityDocument.getKeyInt();
        
                            sFileTemplate = DocumentosUtil.getFile(cct, infoPagT, null, null).getName();
        
                            // Generar el documento a partir la plantilla
                            IItem entityTemplate = genDocAPI.attachTaskTemplate(
                                    connectorSession, taskId, documentId, templateId,
                                    sFileTemplate);
                            
                            // Referencia al fichero del documento en el gestor documental
                            String docref = entityTemplate.getString("INFOPAG");
                            String sMimetype = genDocAPI.getMimeType(connectorSession, docref);
                            entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
                            String templateDescripcion = entityTemplate.getString("DESCRIPCION");
                            templateDescripcion = templateDescripcion + " - " + numexp;
                            entityTemplate.set("DESCRIPCION", templateDescripcion);
                            entityTemplate.set("DESTINO", nombre);
                            entityTemplate.set("DESTINO_ID", idExt);            
        
                            entityTemplate.store(cct);
                                    
                            cct.deleteSsVariable("NRESOLUCIONPARCIAL");
                            cct.deleteSsVariable("NOMBRE_TRAMITE");
                            cct.deleteSsVariable("NOMBRETRABAJADOR");
                            cct.deleteSsVariable("NOMBREBENEFICIARIO");
                            cct.deleteSsVariable("NUMEXPSOLICITUD");
                            cct.deleteSsVariable("RECURSO");
                            
                            cct.deleteSsVariable("NOMBRE");
                            cct.deleteSsVariable("DIRNOT");
                            cct.deleteSsVariable("C_POSTAL");
                            cct.deleteSsVariable("LOCALIDAD");
                            cct.deleteSsVariable("CAUT");
                            
                            cct.deleteSsVariable("EXTRACTO_DECRETO");
                            
                            if(null != refTablas && !"".equals(refTablas)){                        
                                insertaTablas(genDocAPI, docref, rulectx, documentId, refTablas, entitiesAPI, numexp);
                            }
                            
                            entityTemplateT.delete(cct);
                            entityDocumentT.delete(cct);
                            DocumentosUtil.deleteFile(sFileTemplate);
                        }
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
            cct.setSsVariable("ANIO", ""
                    + Calendar.getInstance().get(Calendar.YEAR));            
            
            String numexpConvocatoria = ruleContext.getNumExp();
                         
            //Obtenemos el asunto de la convocatoria
            String convocatoria = "";
            IItem expConv = ExpedientesUtil.getExpediente(cct, numexpConvocatoria);
            if (expConv != null){
                convocatoria = expConv.getString("ASUNTO");
            }
            //Obtenemos el expediente de decreto
            IItemCollection expRelacionadosPadreCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" +numexpConvocatoria+"' ORDER BY NUMEXP_HIJO ASC");
            Iterator<?> expRelacionadosPadreIterator = expRelacionadosPadreCollection.iterator();
            String numexpDecreto = "";
            boolean encontrado = false;
            while (expRelacionadosPadreIterator.hasNext() && !encontrado){
                IItem expRel = (IItem)expRelacionadosPadreIterator.next();
                String numexpRel = expRel.getString("NUMEXP_HIJO");
                String nombreProc = ""; 
                IItem expProc = ExpedientesUtil.getExpediente(cct, numexpRel);
                if(expProc != null){
                    nombreProc = expProc.getString("NOMBREPROCEDIMIENTO");
                       if(nombreProc.trim().toUpperCase().contains("DECRETO")){
                        numexpDecreto = numexpRel;
                        encontrado = true;
                       }
                }
            }
            
            IItemCollection decretoCollection = cct.getAPI().getEntitiesAPI().getEntities(Constants.TABLASBBDD.SGD_DECRETO, numexpDecreto);
            Iterator<?> decretoIterator = decretoCollection.iterator();
            String numDecreto = "";
            Date fechaDecreto = new Date();
            if(decretoIterator.hasNext()){
                IItem decreto = (IItem)decretoIterator.next();
                numDecreto = decreto.getInt("ANIO")+"/" +decreto.getInt("NUMERO_DECRETO");
                fechaDecreto = decreto.getDate("FECHA_DECRETO");
            }
            
            //Obtenemos el número de boletín y la fecha
            IItemCollection expRelacionadosPadreCollectionBop = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" +numexpConvocatoria+"' ORDER BY NUMEXP_HIJO ASC");
            Iterator<?> expRelacionadosPadreIteratorBop = expRelacionadosPadreCollectionBop.iterator();
            String numexpBoletin = "";
            encontrado = false;
            while (expRelacionadosPadreIteratorBop.hasNext() && !encontrado){
                IItem expRel = (IItem)expRelacionadosPadreIteratorBop.next();
                String numexpRel = expRel.getString("NUMEXP_HIJO");
                String nombreProc = "";
                IItem expPorc = ExpedientesUtil.getExpediente(cct, numexpRel);
                if(expPorc != null){
                    nombreProc = expPorc.getString("NOMBREPROCEDIMIENTO");
                    if(nombreProc.trim().toUpperCase().contains("BOP")){
                        numexpBoletin = numexpRel;
                        encontrado = true;
                    }
                }
            }            
            IItemCollection boletinCollection = cct.getAPI().getEntitiesAPI().getEntities("BOP_SOLICITUD", numexpBoletin);
            Iterator<?> boletinIterator = boletinCollection.iterator();
            int numBoletin = 0;
            Date fechaBoletin = new Date();
            if(boletinIterator.hasNext()){
                IItem boletin = (IItem)boletinIterator.next();                
                fechaBoletin = boletin.getDate("FECHA_PUBLICACION");
                //Obtenemos el número de boletín
                IItemCollection boletinesCollection = cct.getAPI().getEntitiesAPI().queryEntities("BOP_PUBLICACION", "WHERE FECHA='" + fechaBoletin + "'");
                Iterator<?> boletinesIterator = boletinesCollection.iterator();
                if(boletinesIterator.hasNext()){
                    numBoletin = ((IItem)boletinesIterator.next()).getInt("NUM_BOP");
                }
            }
            
            //Recuperamos el número de informes generados ya
            String consulta = "WHERE NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = '" +numexpConvocatoria+"') " +
                    "AND ID_TPDOC IN (SELECT ID FROM SPAC_CT_TPDOC WHERE NOMBRE='" +tipoDocumento+"')";
            IItemCollection justificacionesCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS, consulta);

            cct.setSsVariable("NUM_DECRETO", numDecreto);
            cct.setSsVariable("FECHA_DECRETO", new SimpleDateFormat("dd/MM/yyyy").format(fechaDecreto));
            
            cct.setSsVariable("NUM_BOLETIN", "" + numBoletin);
            cct.setSsVariable("FECHA_BOLETIN", new SimpleDateFormat("dd/MM/yyyy").format(fechaBoletin));
            
            cct.setSsVariable("NUM_INFORME", "" +(justificacionesCollection.toList().size()+1));
            cct.setSsVariable("CONVOCATORIA", convocatoria);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("ANIO");            
            cct.deleteSsVariable("NUM_DECRETO");
            cct.deleteSsVariable("FECHA_DECRETO");            
            cct.deleteSsVariable("NUM_BOLETIN");
            cct.deleteSsVariable("FECHA_BOLETIN");
            cct.deleteSsVariable("NUM_INFORME");
            cct.deleteSsVariable("CONVOCATORIA");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
    
    public void insertaTablas(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, String string, IEntitiesAPI entitiesAPI, String numexp) {
        
        Object connectorSession = null;
        OpenOfficeHelper ooHelper = null;
        try {
            //Abre el documento
            String extension = "odt";
            String fileName = FileTemporaryManager.getInstance().newFileName("." +extension);
        
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
        
            OutputStream out = new FileOutputStream(fileName);
            connectorSession = gendocAPI.createConnectorSession();
            gendocAPI.getDocument(connectorSession, docref, out);
            File file = new File(fileName);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
            
            String[] refTabla = refTablas.split(",");            
            for(int i = 0; i<refTabla.length; i++){                        
                insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, numexp);
            }
            
            //Guarda el documento
            String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
            fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
            String mime = "application/vnd.oasis.opendocument.text";
            OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
            File fileOut = new File(fileNameOut);
            InputStream in = new FileInputStream(fileOut);
            gendocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);
            
            //Borra archivos temporales
            file.delete();
            fileOut.delete();
            DocumentosUtil.deleteFile(fileName);
            if(in!=null){
                in.close();
            }
            if(out != null){
                out.close();
            }
            ooHelper.dispose();
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        } catch (java.lang.Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
    
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String ayuntamiento = "";
        String cif = "";
        String proyecto = "";
        String motivoDenegacion = "";
        
        try{
            if("%TABLA1%".equals(refTabla)){
                   
                   IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);
                   String estadoAdm = "";
                   if(expediente != null){
                       estadoAdm = expediente.getString("ESTADOADM");
                   }
                   String tituloCol4 = "";
                   
                   if("RC".equals(estadoAdm)){
                       tituloCol4 = "MOTIVO DENEGACIÓN";                   
                   } else{
                       tituloCol4 = "IMPORTE";                   
                   }
             
                   int numFilas = 1;
    
                ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                cif = expediente.getString("NIFCIFTITULAR");
                
                if(ayuntamiento == null){
                    ayuntamiento = "";
                }
                if(cif == null){
                    cif = "";
                }
                proyecto = "";                
                
                IItem solicitud = (IItem) entitiesAPI.getEntities("DPCR_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator().next();
                proyecto = solicitud.getString("FINALIDAD");
            
                if(proyecto == null){
                    proyecto = "";
                }
            
                double importe = 0;
            
                motivoDenegacion = "";
                
                Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                if(expResolucion.hasNext()){
                    IItem resolucion = (IItem) expResolucion.next();
                    importe = resolucion.getDouble("IMPORTE");
                    motivoDenegacion = resolucion.getString("MOTIVO_RECHAZO");
                }
                
                if(motivoDenegacion == null){ 
                    motivoDenegacion = "";
                }
                      
                //Busca la posición de la tabla y coloca el cursor ahí
                //Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString("%TABLA1%");
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
                XTextTable xTable = null;
                
                if (xSearchInterface != null){
                    //Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");
                    
                    //Inserta una tabla de 4 columnas y tantas filas
                    //como nuevas liquidaciones haya mas una de cabecera
                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
                    
                    //Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
                    xTable.initialize(numFilas + 1, 4);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);
    
                    if("AP".equals(estadoAdm)){
                        colocaColumnas1(xTable);
                    } else{
                        colocaColumnas2(xTable);
                    }
    
                    //Rellena la cabecera de la tabla                
                    setHeaderCellText(xTable, "A1", "AYUNTAMIENTO");    
                    setHeaderCellText(xTable, "B1", "C.I.F");                
                    setHeaderCellText(xTable, "C1", "PROYECTO / ACTIVIDAD");                
                    setHeaderCellText(xTable, "D1", tituloCol4);
                    
                    if("AP".equals(estadoAdm)){
                        setCellText(xTable, "A" + (2), ayuntamiento);
                        setCellText(xTable, "B" + (2), cif);
                        setCellText(xTable, "C" + (2), proyecto);
                        setCellText(xTable, "D" + (2), "" + importe);    
                    } else if("RC".equals(estadoAdm)){
                        setCellText(xTable, "A" + (2), ayuntamiento);
                        setCellText(xTable, "B" + (2), cif);
                        setCellText(xTable, "C" + (2), proyecto);
                        setCellText(xTable, "D" + (2), motivoDenegacion);                
                    } else{
                        setCellText(xTable, "A" + (2), "");
                        setCellText(xTable, "B" + (2), "");
                        setCellText(xTable, "C" + (2), "");
                        setCellText(xTable, "D" + (2), "");                
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
    
    private void setHeaderCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(cellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName));

        //Propiedades        
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));    
        xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
        xCPS.setPropertyValue("BackColor", Integer.valueOf(0xC0C0C0));
        
        //Texto de la celda
        xCellText.setString(strText);
    }    

    private void setCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(cellName);        
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

        //Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));    
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

        //Texto de la celda
        xCellText.setString(strText);
    }
  
    private void colocaColumnas1(XTextTable xTextTable){
        
        XPropertySet xPS = ( XPropertySet ) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);
         
        // Get table Width and TableColumnRelativeSum properties values
        int iWidth;
        try {
            iWidth = ( Integer ) xPS.getPropertyValue( "Width" );
            
            short sTableColumnRelativeSum = ( Short ) xPS.getPropertyValue( "TableColumnRelativeSum" );
             
            // Get table column separators
            Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );
             
            TableColumnSeparator[] xSeparators = ( TableColumnSeparator[] )UnoRuntime.queryInterface(
                TableColumnSeparator[].class, xObj );

            
            //Calculamos el tamaño que le queremos dar a la celda
            //Se empieza colocando de la última a la primera
            double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
            double dRelativeWidth = ( double ) 15000 * dRatio;
            
            // Last table column separator position
            double dPosition = sTableColumnRelativeSum - dRelativeWidth;
             
            // Set set new position for all column separators        
            //Número de separadores
            int i = xSeparators.length - 1;
            xSeparators[i].Position = (short) Math.ceil( dPosition );
            
            i--;            
            dRelativeWidth = ( double ) 40000 * dRatio;
            dPosition -= dRelativeWidth;                    
            xSeparators[i].Position = (short) Math.ceil( dPosition );

            i--;            
            dRelativeWidth = ( double ) 15000 * dRatio;
            dPosition -= dRelativeWidth;                    
            xSeparators[i].Position = (short) Math.ceil( dPosition );
                        
            
            // Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
            xPS.setPropertyValue( "TableColumnSeparators", xSeparators );    
        } catch (UnknownPropertyException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (WrappedTargetException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (PropertyVetoException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
    
    private void colocaColumnas2(XTextTable xTextTable){
        
        XPropertySet xPS = ( XPropertySet ) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);
         
        // Get table Width and TableColumnRelativeSum properties values
        int iWidth;
        try {
            iWidth = ( Integer ) xPS.getPropertyValue( "Width" );
            
            short sTableColumnRelativeSum = ( Short ) xPS.getPropertyValue( "TableColumnRelativeSum" );
             
            // Get table column separators
            Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );
             
            TableColumnSeparator[] xSeparators = ( TableColumnSeparator[] )UnoRuntime.queryInterface(
                TableColumnSeparator[].class, xObj );

            
            //Calculamos el tamaño que le queremos dar a la celda
            //Se empieza colocando de la última a la primera
            double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
            double dRelativeWidth = ( double ) 30000 * dRatio;
            
            // Last table column separator position
            double dPosition = sTableColumnRelativeSum - dRelativeWidth;
             
            // Set set new position for all column separators        
            //Número de separadores
            int i = xSeparators.length - 1;
            xSeparators[i].Position = (short) Math.ceil( dPosition );
            
            i--;            
            dRelativeWidth = ( double ) 30000 * dRatio;
            dPosition -= dRelativeWidth;                    
            xSeparators[i].Position = (short) Math.ceil( dPosition );

            i--;            
            dRelativeWidth = ( double ) 15000 * dRatio;
            dPosition -= dRelativeWidth;                    
            xSeparators[i].Position = (short) Math.ceil( dPosition );
           
            
            // Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
            xPS.setPropertyValue( "TableColumnSeparators", xSeparators );    
        } catch (UnknownPropertyException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (WrappedTargetException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (PropertyVetoException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
}