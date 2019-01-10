package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

class CommonFunctions {
    
    private static final Logger LOGGER = Logger.getLogger(CommonFunctions.class);
    
    public static void adjuntarContenido(IRuleContext rulectx, String numexp, XComponent xComponent) throws ISPACException {
        try {
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            
            //Obtenemos los docuementos de la propuesta
            IItemCollection collection = entitiesAPI.getEntities("SPAC_DT_DOCUMENTOS", numexp);
            Iterator<?> it = collection.iterator();
            boolean found = false;
            
            while (it.hasNext() && !found) {
                IItem iDoc = (IItem)it.next();
                
                //El contenido de la propuesta tiene que estar en formato Word (.doc)
                String extension = iDoc.getString(DocumentosUtil.EXTENSION);
                
                if ( extension.toUpperCase().compareTo("DOC")==0) {
                    //En concreto busco documentos de tipo Anexo a la Solicitud (propuestas desde Registro Telemático)
                    //o de tipo Contenido de la propuesta (propuestas iniciadas desde escritorio de tramitación)
                    String nombre = iDoc.getString("NOMBRE");
                    
                    if ( nombre.compareTo("Anexo a Solicitud" )== 0 || nombre.compareTo("Contenido de la propuesta") == 0) {
                        found = true;
                        String strInfoPag = iDoc.getString(DocumentosUtil.INFOPAG);
                        
                        if (strInfoPag != null) {
                            File file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
                            ieci.tdw.ispac.api.rule.procedures.CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                            file.delete();
                        }
                    }
                }
            }
            
        } catch(Exception e) {
            LOGGER.error("Error al obtener el concatena. "+numexp+" - "+e.getMessage(), e);
            throw new ISPACException("Error al obtener el concatena. "+numexp+" - "+e.getMessage(), e);    
        }
    }   
    
    public static String createNumConvocatoria(IRuleContext rulectx) throws ISPACException {
        String numconv = "?";
        
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);

            String strQuery = "WHERE YEAR = '" + year + "'";
            IItemCollection collection = entitiesAPI.queryEntities("SECR_CONVOCATORIA", strQuery);
            int numero = collection.toList().size();
            numero = numero + 1;
            numconv = year + "/" + numero;
            
            IItem iConv = entitiesAPI.createEntity("SECR_CONVOCATORIA", rulectx.getNumExp());
            iConv.set("YEAR", year);
            iConv.set("NUMERO", numero);
            iConv.set("NUMEXP_ORIGEN", rulectx.getNumExp());
            iConv.store(cct);
            
            return numconv;
            
        } catch(ISPACException e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
    }
    
    public static void createPropuestaAprobacionActaAnterior(IRuleContext rulectx) throws ISPACException {
        
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            ITXTransaction tx = invesFlowAPI.getTransactionAPI();
            //----------------------------------------------------------------------------------------------

            //Creación del expediente
            String strQuery = "WHERE NOMBRE='Propuesta'";
            IItemCollection coll = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
            Iterator<?> it = coll.iterator();
            int nProcedure = 0;
            IItem proc = null;
            int n;
            String cod_pcd = "";
            
            while (it.hasNext()) {
                proc = (IItem)it.next();
                n = proc.getInt("ID");
            
                if ( n > nProcedure ) {
                    nProcedure = n;
                    cod_pcd = proc.getString("COD_PCD");
                }
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("COD_PCD", cod_pcd);
            int idExp = tx.createProcess(nProcedure, params);
            IProcess process = invesFlowAPI.getProcess(idExp);
            String numexp = process.getString("NUMEXP");            
            IItem iProp = entitiesAPI.createEntity("SECR_PROPUESTA", numexp);

            //Inicialización de datos de la propuesta
            String strOrgano = SecretariaUtil.getOrganoSesion(rulectx,null);
            iProp.set("ORIGEN", "0001");
            iProp.set("DESTINO", strOrgano);
            iProp.set("EXTRACTO", "Conocimiento y aprobación, si procede, del borrador del acta de la sesión anterior");
            iProp.store(cct);
            
            //Campo Asunto del expediente de la propuesta
            String strEstado = SecretariaUtil.getEstadoAdmPropuesta(rulectx);
            coll = entitiesAPI.getEntities("SPAC_EXPEDIENTES", numexp);
            it = coll.iterator();
            
            if (it.hasNext()) {
                IItem iExp = (IItem)it.next();
                iExp.set("ASUNTO", "Propuesta aprobación acta anterior");
                iExp.set("ESTADOADM", strEstado);
                iExp.store(cct);
            }
            
            //Relación con expediente de sesión
            strQuery = "WHERE NOMBRE='SPAC_EXP_RELACIONADOS'";
            coll = entitiesAPI.queryEntities("SPAC_CT_ENTIDADES", strQuery);
            it = coll.iterator();
            
            if (it.hasNext()) {
                IItem iExpRel = (IItem)it.next();
                int id = iExpRel.getInt("ID");
                IItem iRelacion = entitiesAPI.createEntity(id);
                iRelacion.set("NUMEXP_PADRE", numexp);
                iRelacion.set("NUMEXP_HIJO", rulectx.getNumExp());
                iRelacion.set("RELACION", "Sesión/Propuesta");
                iRelacion.store(cct);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
    }

    public static void generarDocumento(IRuleContext rulectx, String nombrePlantilla, String descripcion) throws ISPACException {
        
        try {
            //APIs
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

            // Obtención de los tipos de documento asociados al trámite
            IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");
            IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
        
            if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()) {
                throw new ISPACInfo("No hay ningún tipo de documento asociado al trámite");
            }

            // Búsqueda de la plantilla indicada
            Iterator<?> itTpDocs = taskTpDocCollection.iterator();
            
            while(itTpDocs.hasNext()) {
                IItem taskTpDoc = (IItem)itTpDocs.next();
                int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                
                IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
                
                if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()) {
                    throw new ISPACInfo("No hay ninguna plantilla asociada al tipo de documento");
                }
                
                IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
                int templateId = tpDocsTemplate.getInt("ID");

                String nombre = tpDocsTemplate.getString("NOMBRE");
                
                if (nombre.compareTo(nombrePlantilla)==0) {
                    Object connectorSession = null;
                    
                    try {
                        connectorSession = gendocAPI.createConnectorSession();
                        // Abrir transacción para que no se pueda generar un documento sin fichero
                        cct.beginTX();
                    
                        int taskId = rulectx.getTaskId();
                        IItem entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
                        int documentId = entityDocument.getKeyInt();
        
                        // Generar el documento a partir de la plantilla
                        IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
                        entityTemplate.set(DocumentosUtil.EXTENSION, "doc");
                        
                        if ( descripcion != null) {
                            String templateDescripcion = entityTemplate.getString(DocumentosUtil.DESCRIPCION);
                            templateDescripcion = templateDescripcion + " - " + descripcion;
                            entityTemplate.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
                            entityTemplate.store(cct);
                        }
                        entityTemplate.store(cct);
                        
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
                        throw new ISPACInfo(message, extraInfo);
                        
                    } finally {
                        
                        if (connectorSession != null) {
                            gendocAPI.closeConnectorSession(connectorSession);
                        }
                    }
                    
                    // Si ha sido correcto se hace commit de la transacción
                    cct.endTX(true);
                }
            }
            
        } catch(Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
    }

    public static void concatenaPartes(IRuleContext rulectx, String numexpOrigen, String strNombreDocCab, String strNombreDocPie, String strNombreDoc, String descripcion, OpenOfficeHelper ooHelper) throws ISPACException {
        //Común a Notificaciones y Traslados
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Cabecera
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strNombreDocCab, "");
            File file1 = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
            
            //Contenido
            CommonFunctions.adjuntarContenido(rulectx, numexpOrigen, xComponent);
            
            //Pie
            strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strNombreDocPie, "");
            File file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
            ieci.tdw.ispac.api.rule.procedures.CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
            file.delete();
            
            //Guarda el resultado
            CommonFunctions.guardarDocumento(rulectx, xComponent, strNombreDoc, descripcion);
            file1.delete();
            
            //Borra los documentos intermedios del gestor documental
            String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
                " AND (DESCRIPCION LIKE '" + strNombreDocCab + "%'" +
                " OR DESCRIPCION LIKE '" + strNombreDocPie + "%')" ;                
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            Iterator<?> it = collection.iterator();
            
            while (it.hasNext()) {
                IItem doc = (IItem)it.next();
                entitiesAPI.deleteDocument(doc);
            }
            
        } catch(Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
    }

    public static void guardarDocumento(IRuleContext rulectx, XComponent xComponent, String strNombreDoc, String descripcion) throws ISPACException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------
            
            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            File file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            
            //Guarda el resultado en gestor documental
            String strQuery = "WHERE NOMBRE = '"+strNombreDoc+"'";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            Iterator<?> it = collection.iterator();
            int tpdoc = 0;
            
            if (it.hasNext()) {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
            }
            
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream in = new FileInputStream(file);
            int docId = newdoc.getInt("ID");
            Object connectorSession = gendocAPI.createConnectorSession();
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strNombreDoc);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            
            if ( descripcion != null) {
                String templateDescripcion = entityDoc.getString(DocumentosUtil.DESCRIPCION);
                templateDescripcion = templateDescripcion + " - " + descripcion;
                entityDoc.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
            }
            
            entityDoc.store(cct);
            file.delete();
            
        } catch(Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
    }
}