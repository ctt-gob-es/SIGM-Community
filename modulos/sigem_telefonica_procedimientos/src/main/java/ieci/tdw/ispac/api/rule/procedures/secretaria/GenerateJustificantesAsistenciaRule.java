package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
//import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;


import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateJustificantesAsistenciaRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(GenerateJustificantesAsistenciaRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
        
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
            boolean esJustificante = false;
            boolean esCertificacion = false;
            
            //Obtener participantes del expediente actual
            IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, numExp);
            
            //Comprobar que hay algún participante para el cual generar su acuse
            if (participantes!=null && participantes.toList().size()>=1) {
                // 3. Obtener plantillas "Justificante de asistencia" y "Certificación de Asistentes"
                // Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
                
                IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
                
                if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
                    throw new ISPACInfo("No hay tipo de documento asociado al trámite");
                } else {
                    //Hay dos tipos de documento asociados al trámite: Certificado de Asistentes
                    //y Justificante de Asistencia. El Certificado es para los integrantes
                    //juntos en una lista. Los justificantes son uno por integrante.
                    Iterator<?> it = taskTpDocCollection.iterator();
                    IItem taskTpDoc = null;
                    
                    while (it.hasNext()) {
                        taskTpDoc = (IItem)it.next();
                        esJustificante = "Justificante de asistencia".equals(taskTpDoc.get("CT_TPDOC:NOMBRE"));
                        esCertificacion = "Certificación de Asistentes".equals(taskTpDoc.get("CT_TPDOC:NOMBRE"));
                        
                        if ( esJustificante || esCertificacion ) {
                            documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                        
                            // Comprobar que el tipo de documento tiene asociado una plantilla
                            IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
                            
                            if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
                                throw new ISPACInfo("No hay plantilla asociada al tipo de documento");
                                
                            } else {
                                IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
                                templateId = tpDocsTemplate.getInt("ID");
                                
                                cct.setSsVariable("DESCR_ORGANO",getDescripcionOrgano(rulectx));
                                
                                if ( esJustificante ) {
                                    // 4. Para cada participante generar un justificante de asistencia
                                    for (int i=0;i<participantes.toList().size();i++) {
                                        try {
                                            connectorSession = gendocAPI.createConnectorSession();
                                            IItem participante = (IItem) participantes.toList().get(i);
                                            // Abrir transacción para que no se pueda generar un documento sin fichero
                                            cct.beginTX();
                                        
                                            if (participante!=null) {
                                                //Comprobamos que el participante ha asistido a la sesión de gobierno
                                                String strAsiste = (String)participante.get("ASISTE");
                                                
                                                if ( strAsiste!=null && strAsiste.compareTo("SI")==0) {
                                                    
                                                    DocumentosUtil.setParticipanteAsSsVariable(cct, participante);                                                    
                    
                                                    entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
                                                    documentId = entityDocument.getKeyInt();
                    
                                                    // Generar el documento a partir la plantilla Justificante de asistencia
                                                    IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
                    
                                                    // Referencia al fichero del documento en el gestor documental
                                                    //En la versión 1.9 ha cambiado esto del MimetypeMapping. 
                                                    //Cuando pasemos de la 1.5 a la 1.9 lo cambiamos
                                                    //String docref = entityTemplate.getString(DocumentosUtil.INFOPAG);
                                                    //String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
                                                    //String sExt = MimetypeMapping.getInstance().getExtension(sMimetype);
                                                    //entityTemplate.set(DocumentosUtil.EXTENSION, sExt);
                                                    entityTemplate.set(DocumentosUtil.EXTENSION, "doc");
                                                    String templateDescripcion = entityTemplate.getString(DocumentosUtil.DESCRIPCION);
                                                    templateDescripcion = templateDescripcion + " - " + cct.getSsVariable(ParticipantesUtil.NOMBRE);
                                                    entityTemplate.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
                                                    entityTemplate.store(cct);
                                                    
                                                    /*
                                                    // 5. Actualizar el campo 'Acuse_Generado' con valor 'Y'
                                                    IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
                                                    participanteAActualizar.set("ACUSE_GENERADO", "Y");
                                                    participanteAActualizar.store(cct);
                                                    */
                                                    
                                                    // Si ha sido correcto borrar las variables de la session
                                                    DocumentosUtil.borraParticipanteSsVariable(cct);
                                                }                                    
                                            }
                                        } catch (Exception e) {
                                            LOGGER.info("No mostraba nada, solo captura el posible error: " + e.getMessage(), e);
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
                                            
                                            throw new ISPACInfo(message, extraInfo);
                                            
                                        } finally {
                                            if (connectorSession != null) {
                                                gendocAPI.closeConnectorSession(connectorSession);
                                            }
                                        }
                                    }// for                                    
                                } else { //Es certificación
                                    try  {
                                        connectorSession = gendocAPI.createConnectorSession();
                                        // Abrir transacción para que no se pueda generar un documento sin fichero
                                        cct.beginTX();
                                    
                                        entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
                                        documentId = entityDocument.getKeyInt();
        
                                        // Generar el documento a partir la plantilla Justificante de asistencia
                                        IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
        
                                        // Referencia al fichero del documento en el gestor documental
                                        entityTemplate.set(DocumentosUtil.EXTENSION, "doc");
                                        entityTemplate.store(cct);
                                        
                                    } catch (Exception e) {
                                        LOGGER.info("No mostraba nada, solo captura el posible error: " + e.getMessage(), e);

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
                                        
                                        throw new ISPACInfo(message, extraInfo);
                                        
                                    } finally {
                                        if (connectorSession != null) {
                                            gendocAPI.closeConnectorSession(connectorSession);
                                        }
                                    }
                                }
                                cct.deleteSsVariable("DESCR_ORGANO");
                              }
                        }
                    }
                    
                    if ( documentTypeId == 0) {
                        throw new ISPACInfo("No existe el tipo de documento Justificante de asistencia ni Certificación de Asistentes.");
                    }
                }
            }
            // Si ha sido correcto se hace commit de la transacción
            cct.endTX(true);
        } catch(ISPACRuleException e) {
            LOGGER.info("No mostraba nada, solo captura el posible error: " + e.getMessage(), e);
               throw new ISPACRuleException(e);
            
        } catch(Exception e) {            
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

    private String getDescripcionOrgano(IRuleContext rulectx) throws ISPACException    {
        String strDescr = "";

        try {
            String strOrgano = SecretariaUtil.getSesion(rulectx, null).getString("ORGANO");
            
            if (strOrgano.compareTo("PLEN")==0) {
                strDescr = "del ";
                
            } else {
                strDescr = "de la ";
            }
            
            strDescr += SecretariaUtil.getNombreOrganoSesion(rulectx, null);
            
            if (strOrgano.compareTo("COMI")==0) {
                strDescr += " del área de " + SecretariaUtil.getNombreAreaSesion(rulectx, null);
            }
            
        } catch(ISPACException e) {
            throw new ISPACException(e);
        }
        
        return strDescr;
    }
}
