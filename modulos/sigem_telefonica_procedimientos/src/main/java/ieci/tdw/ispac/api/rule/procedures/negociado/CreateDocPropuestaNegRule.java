package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * 
 * @author teresa
 * @date 13/11/2009
 * @propósito Actualiza el campo estado de la entidad para mostrar los enlaces de Propuesta/Decreto, crea el documento zip de
 * Contenido de la propuesta a partir de los documentos especificados en la consulta strQueryDocumentos y lo asocia al trámite actual.
 */
public class CreateDocPropuestaNegRule implements IRule {
    
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(CreateDocPropuestaNegRule.class);

    protected String strEntidad = "";
    protected String strQueryDocumentos = "" ;    
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            //----------------------------------------------------------------------------------------------
            
            //Actualiza el campo estado de la entidad
            //de modo que permita mostrar los enlaces para crear Propuesta/Decreto
            String numexp = rulectx.getNumExp();
            IItemCollection col = entitiesAPI.getEntities(strEntidad, numexp);
            Iterator<?> it = col.iterator();
            
            if (it.hasNext()) {
                IItem entidad = (IItem)it.next();
                entidad.set("ESTADO", "Inicio");
                entidad.store(cct);
            }
            
            //Si la consulta strQueryDocumentos no está vacía generamos el documento zip Contenido de la propuesta y lo asignamos
            //al trámite actual
            if (StringUtils.isNotEmpty(strQueryDocumentos)){
                String strQuery = "NOMBRE = '";
                IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, "WHERE " + strQueryDocumentos);
                int i = 0;
                it = collection.iterator();
                
                while (it.hasNext() && i<5){
                    IItem itemDoc = (IItem)it.next();
                
                    if (i==0){
                        strQuery += itemDoc.getString("NOMBRE") + "'";
                    
                    } else {
                        strQuery += " OR NOMBRE = '"+ itemDoc.getString("NOMBRE") + "'";
                    }
                    i++;
                }
    
                LOGGER.debug("strQuery: "+strQuery);
                
                IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), strQuery, "FDOC DESC");
    
                List<IItem> filesConcatenate = new ArrayList<IItem>();
                i=0;
                it = documentsCollection.iterator();
                IItem itemDoc = null;
                
                while (it.hasNext() && i<5){
                    itemDoc = (IItem)it.next();
                    filesConcatenate.add(itemDoc);
                    i++;
                }
                
                
                //Obtenemos el id del tipo de documento de "Contenido de la propuesta"
                strQuery = "WHERE NOMBRE = 'Contenido de la propuesta'";
                
                LOGGER.debug("strQuery: "+strQuery);
    
                IItemCollection typesDocumentCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery);
                
                int idTypeDocument = 0;
                if (typesDocumentCollection!=null && documentsCollection.next()){
                    idTypeDocument = ((IItem)typesDocumentCollection.iterator().next()).getInt("ID");
                }
                if (idTypeDocument == 0){
                    throw new ISPACInfo("Error al obtener el tipo de documento.");
                }
    
                File zipFile = DocumentosUtil.createDocumentsZipFileInfoPag(genDocAPI, filesConcatenate);
                
                //Generamos el documento zip
                String sExtension = "zip";
                String sName = "Contenido de la propuesta";//Campo descripción del documento
                String sMimeType = MimetypeMapping.getMimeType(sExtension);
                
                if (sMimeType == null) {
                    throw new ISPACInfo("Error al obtener el tipo Mime");
                }
                
                int keyId = 0;
                IItem entityDocument = null;
                int taskId = rulectx.getTaskId(); 
                int currentId = taskId;
                        
                // Ejecución en un contexto transaccional
                boolean bCommit = false;
                
                try {
                    // Abrir transacción para que no se pueda generar un documento sin fichero
                    cct.beginTX();
                    entityDocument = genDocAPI.createTaskDocument(currentId, idTypeDocument);
                    
                    keyId = entityDocument.getKeyInt();
                    // Establecer la extensión del documento para mostrar un icono descriptivo del tipo de documento en la lista de documentos
                    entityDocument.set(DocumentosUtil.EXTENSION, sExtension);
                    entityDocument.store(cct);
                    
                    InputStream in = new FileInputStream(zipFile);
                    int length = (int)zipFile.length();
                    Object connectorSession = null;

                    try {
                        connectorSession = genDocAPI.createConnectorSession();
                        genDocAPI.attachTaskInputStream(connectorSession, currentId, keyId, in, length, sMimeType, sName);
                    
                    } finally {
                        if (connectorSession != null) {
                            genDocAPI.closeConnectorSession(connectorSession);
                        }
                    }
                    // Si ha sido correcto se hace commit de la transacción
                    bCommit = true;
                    
                } catch (Exception e) {
                    LOGGER.info("No mostraba nada, solo devuelve un mensaje. El error es: " + e.getMessage(), e);
                    throw new ISPACInfo("Error al generar el documento.");
                    
                } finally {
                    cct.endTX(bCommit);
                }
                    
                // Eliminar el zip
                FileUtils.deleteFile(zipFile);
            }            
        } catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
    
    public static File getFile(IGenDocAPI gendocAPI, String strInfoPag) throws ISPACException {
        File file = null;
        Object connectorSession = null;
        
        try {
            connectorSession = gendocAPI.createConnectorSession();

            String extension = "doc";
                
            String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            
            OutputStream out = new FileOutputStream(fileName);
            gendocAPI.getDocument(connectorSession, strInfoPag, out);
            
            file = new File(fileName);
            
        } catch(Exception e) {
            throw new ISPACException(e);     
        
        } finally {
            gendocAPI.closeConnectorSession(connectorSession);
        }
        return file;
    }
    
}
