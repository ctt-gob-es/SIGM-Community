package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemplateManager;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil.ConstantesTagsSsVariables;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * 
 * @author teresa
 * @date 09/09/2009
 * @propósito Genera las notificaciones para el expediente de Decreto actual a partir de la plantilla dinámica "Plantilla de Notificaciones"
 * adjuntada en el primer trámite "Creación del decreto".
 */
public class GenerateNotificationDecretoRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(GenerateNotificationDecretoRule.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            LOGGER.info("GenerateNotificacionDecreto - Init");
        
            // APIs
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
            
            // Variables
            IItem entityDocument = null;
            int documentTypeId = 0;
            int templateId = 0;
            int taskId = rulectx.getTaskId();
            
            IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");
            
            String numExp = rulectx.getNumExp();

            int documentId = 0;
            Object connectorSession = null;
            String sFileTemplate = null;
            
            // 1. Obtener participantes del expediente actual, con relación != "Trasladado"
            IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, numExp, "(ROL != 'TRAS' OR ROL IS NULL)", ParticipantesUtil.ID);
            
            // 2. Comprobar que hay algún participante para el cual generar su notificación
            if (participantes!=null && participantes.toList().size()>=1) {
            
                // 3. Obtener plantilla "Notificación Decreto"
                // Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
                IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
                
                if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
                    throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
                    
                } else {

                    //IItem taskTpDoc = (IItem)taskTpDocCollection.iterator().next();
                    //documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                    
                    //Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
                    //Necesitamos el de Notificación del Decreto
                    Iterator<?> it = taskTpDocCollection.iterator();
                    
                    while (it.hasNext()){
                        IItem taskTpDoc = (IItem)it.next();
                        
                        if ("Notificación Decreto".equals(taskTpDoc.get("CT_TPDOC:NOMBRE"))){
                            documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                        }
                    }
                    
                    //Comprobamos que haya encontrado el Tipo de documento
                    if (documentTypeId != 0){
                        
                        // Comprobar que el tipo de documento tiene asociado una plantilla
                        IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
                        
                        if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
                            throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
                            
                        } else {
                            IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
                            templateId = tpDocsTemplate.getInt("ID");
                            
                            // 4. Para cada participante generar una notificación
                            for (int i=0;i<participantes.toList().size();i++){
                                try {
                                    connectorSession = gendocAPI.createConnectorSession();
                                    IItem participante = (IItem) participantes.toList().get(i);
                                    
                                
                                    if (participante!=null){
                                        DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                                        
                                        entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
                                        documentId = entityDocument.getKeyInt();
        
                                        IItem plantillaNotificacionesDecreto = getInfoPag(rulectx);
                                        String infoPag = plantillaNotificacionesDecreto.getString(DocumentosUtil.INFOPAG);
                                        int idPlantilla = getDocumentId(rulectx);
                                        
                                        sFileTemplate = getFile(gendocAPI, connectorSession, infoPag, templateId, idPlantilla);
                                        
                                        // Generar el documento a partir la plantilla "Notificación Decreto"
                                        IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
                                        
                                        // Referencia al fichero del documento en el gestor documental
                                        String docref = entityTemplate.getString(DocumentosUtil.INFOPAG);
                                        String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
                                        entityTemplate.set(DocumentosUtil.EXTENSION, MimetypeMapping.getExtension(sMimetype));
                                        String templateDescripcion = entityTemplate.getString(DocumentosUtil.DESCRIPCION);
                                        templateDescripcion = templateDescripcion + " - " + cct.getSsVariable(ConstantesTagsSsVariables.NOMBRE);
                                        
                                        entityTemplate.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
                                        entityTemplate.set(DocumentosUtil.DESTINO, cct.getSsVariable(ConstantesTagsSsVariables.NOMBRE));
                                        entityTemplate.set(DocumentosUtil.DESTINO_ID, cct.getSsVariable(ConstantesTagsSsVariables.ID_EXT));
                                        
                                        String autor = plantillaNotificacionesDecreto.getString(DocumentosUtil.AUTOR);
                                        String autorInfo = plantillaNotificacionesDecreto.getString(DocumentosUtil.AUTOR_INFO);                                        
                                        entityTemplate.set(DocumentosUtil.AUTOR, autor);
                                        entityTemplate.set(DocumentosUtil.AUTOR_INFO, autorInfo);

                                        entityTemplate.store(cct);
                                        
                                        // 5. Actualizar el campo 'DECRETO_NOTIFICADO' con valor 'Y'
                                        IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
                                        participanteAActualizar.set("DECRETO_NOTIFICADO", "Y");
                                        participanteAActualizar.store(cct);
                                        
                                        DocumentosUtil.borraParticipanteSsVariable(cct);
                                        
                                        deleteFile(sFileTemplate);
                                    }
                                    
                                } catch (Exception e) {
                                    
                                    // Si se produce algún error se hace rollback de la transacción
                                    cct.endTX(false);
                                    
                                    String message = "exception.documents.generate";
                                    String extraInfo = null;
                                    Throwable eCause = e.getCause();
                                    
                                    if (eCause instanceof ISPACException) {
                                        
                                        if (eCause.getCause() instanceof NoConnectException) {
                                            extraInfo = "exception.extrainfo.documents.openoffice.off";
                                            
                                        } else {
                                            extraInfo = eCause.getCause().getMessage();
                                        }
                                        
                                    } else if (eCause instanceof DisposedException) {
                                        extraInfo = "exception.extrainfo.documents.openoffice.stop";
                                        
                                    } else {
                                        extraInfo = e.getMessage();
                                    }
                                    
                                    LOGGER.warn("Numexp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
                                    throw new ISPACInfo(message, extraInfo);
                                    
                                } finally {
                                    
                                    if (connectorSession != null) {
                                        gendocAPI.closeConnectorSession(connectorSession);
                                    }
                                }
                            }// for
                          }
                    } else {//if (documentTypeId != 0){
                        throw new ISPACInfo("No existe el tipo de documento Notificación Decreto.");
                    }
                }
            }
            // Si ha sido correcto se hace commit de la transacción
            //cct.endTX(true);
        } catch(ISPACRuleException e) {
            LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);        
               throw new ISPACRuleException(e);
               
        } catch (Exception e){
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

    private IItem getInfoPag (IRuleContext rulectx){

        try{
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            // Obtener el documento "Plantilla de Notificaciones" del expediente que se adjuntó en el primer trámite de "Creación del decreto"
            IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Plantilla de Notificaciones'", "FDOC DESC");
            IItem document = null;

            if (documentsCollection!=null && documentsCollection.next()){
                document = (IItem)documentsCollection.iterator().next();
            }
                
            return document;

        } catch(Exception e){
            LOGGER.error ("Error en getInfoPag al obtener el valor INFOPAG del documento: " + e.getMessage(), e);
            return null;
        }    
    }
    
    private int getDocumentId (IRuleContext rulectx){
        try{
            int id = -1;
            
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            // Obtener el documento "Plantilla de Notificaciones" del expediente que se adjuntó en el primer trámite de "Creación del decreto"
            IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Plantilla de Notificaciones'", "FDOC DESC");
            IItem document = null;

            if (documentsCollection!=null && documentsCollection.next()){
                document = (IItem)documentsCollection.iterator().next();
            }
                
            // Obtener el valor del campo ID
            if (document!=null){
                id = document.getInt("ID");
            }
            
            return id;
        
        } catch(Exception e){
            LOGGER.error ("Error en getDocumentId al obtener el valor ID del documento: " + e.getMessage(), e);
            return -1;
        }
    }
    
    
    /**
     * Obtiene el fichero correspondiente al infoPag indicado 
     *
     * @param rulectx
     * @param infoPag
     * @param templateId
     * @return 
     * @throws ISPACException
     */
    private String getFile(IGenDocAPI gendocAPI, Object connectorSession, String infoPag, int templateId, int idPlantilla) throws ISPACException{
        
        // API        
        File file = null;
        try{
            connectorSession = gendocAPI.createConnectorSession();
            
            String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, infoPag));
            
            FileTemplateManager templateManager = null;

            // Obtiene el manejador de plantillas
            templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
            
            //Se almacena documento
            String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
            //fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            
            //String fileNamePath = FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + fileName;
            String fileNamePath = templateManager.getFileMgrPath() + "/" + fileName;
            
            // Nombre de la plantilla
            String sName = Integer.toString(templateId) + "." + extension;
            
            //Control de plantillas por multientidad
            OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();

            if (info != null){
                String organizationId = info.getOrganizationId();
                //Se añade el numExp al nombre de la plantilla para evitar colisiones al generar notificaciones simultaneamente desde
                //dos expedientes distintos de la misma entidad
                sName = organizationId + "_" + idPlantilla + "_" + sName; 
            }
            
            OutputStream out = new FileOutputStream(fileNamePath);
            gendocAPI.getDocument(connectorSession, infoPag, out);
            file = new File(fileNamePath);
            File file2 = new File(templateManager.getFileMgrPath() + "/" + sName);
            file.renameTo(file2);
            file.delete();
                            
            
            OutputStream out2 = new FileOutputStream(fileNamePath);
            gendocAPI.getDocument(connectorSession, infoPag, out2);
            File file3 = new File(fileNamePath);
            File file4 = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sName);
            file3.renameTo(file4);
            file3.delete();
                            
            return sName;

        } catch (FileNotFoundException e) {
            throw new ISPACRuleException("Error al intentar obtener el documento, no existe. " + e.getMessage(), e);

        } finally {
            if (connectorSession != null) {
                gendocAPI.closeConnectorSession(connectorSession);
            }
        }
    } 

    
    /**
     * Elimina el fichero template y el temporary correspondientes 
     *
     * @param rulectx
     * @param infoPag
     * @param templateId
     * @return 
     * @throws ISPACException
     */
    private boolean deleteFile(String sFileTemplate) throws ISPACException{
        
        FileTemplateManager templateManager = null;
        
        try{

            // Obtiene el manejador de plantillas
            templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
            
            boolean resultado = true;

            File fTemplate = new File(templateManager.getFileMgrPath() + "/" + sFileTemplate);
            File fTemporary = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
            
            try{
            
                if (fTemplate!= null && fTemplate.exists() && !fTemplate.delete()){
                    LOGGER.error ("No se pudo eliminar el documento: "+templateManager.getFileMgrPath() + "/" + sFileTemplate);
                    resultado = false;
                }
                
            } catch(Exception e){
                LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
            }
            
            try{
                if (fTemporary!=null && fTemporary.exists() && !fTemporary.delete()){
                    LOGGER.error("No se pudo eliminar el documento: "+FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
                    resultado = false;
                }
                
            } catch(Exception e){
                LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
            }
            
            return resultado;
            
        } catch (Exception e) {
            throw new ISPACRuleException("Error al eliminar el documento "+templateManager.getFileMgrPath() + "/" + sFileTemplate + ". " + e.getMessage(), e);
        }
    } 
}
