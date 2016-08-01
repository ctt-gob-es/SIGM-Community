package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrSERSOEnviaNotifASolicitud implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOEnviaNotifASolicitud.class);
    
    private IClientContext cct;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        
        String numexpSolicitud = ""; 
        try{
            //----------------------------------------------------------------------------------------------
            cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            String numexp = rulectx.getNumExp();
            int tramiteId = rulectx.getTaskId();            
            
            //Recuperamos los expedientes relacionados
            IItemCollection docsCol = entitiesAPI.getDocuments(numexp, "ID_TRAMITE = '" +tramiteId+"' AND UPPER(NOMBRE) = 'NOTIFICACIÓN'", "");
            Iterator<?> docsIt = docsCol.iterator();                  
            if(docsIt.hasNext()){
                while (docsIt.hasNext()){
                    IItem doc = (IItem)docsIt.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String descripcion = doc.getString("DESCRIPCION");
                    numexpSolicitud = (descripcion.split("-"))[2].trim();
                    
                    //Creamos el trámite
                    String strQueryAux = "WHERE NUMEXP='" + numexpSolicitud + "'";
                    IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
                    Iterator<?> itExpsAux = collExpsAux.iterator();

                    IItem iExpedienteAux = (IItem)itExpsAux.next();
                    int idFase = iExpedienteAux.getInt("ID");
                    int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");
                    strQueryAux = "WHERE ID_FASE = " +idFaseDecreto+" ORDER BY ORDEN ASC";
                    IItemCollection iTramitePropCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
                    Iterator<?> iTramitePropIterator = iTramitePropCollection.iterator();
                    int idTramite=0;
                    IItem tramite = (IItem)iTramitePropIterator.next();
                    idTramite = tramite.getInt("ID");
                    
                    cct.beginTX();
                    //Creo el tramite 'Creación del Decreto, traslado y notificaciones'            
                    int tramiteNuevo = transaction.createTask(idFase, idTramite);

                    //Pasamos el documento
                    String nombre = doc.getString("DESCRIPCION");
                    
                    String infoPag = doc.getString("INFOPAG");
                    String infoPagRDE = doc.getString("INFOPAG_RDE");
                    String extension = "";

                    File documento = null;
                                            
                    try{
                        if (StringUtils.isNotBlank(infoPagRDE)) {                            
                            documento = getFile( infoPagRDE);
                            extension = doc.getString("EXTENSION");
                        } else {
                            documento = getFile( infoPag);
                            extension = doc.getString("EXTENSION_RDE");
                        }
                    } catch(Exception e){
                        LOGGER.debug("No se ha podido recuperar el documento. " + e.getMessage(), e);
                        try{
                            documento = getFile( infoPag);
                        } catch(Exception e1){
                            documento = null;
                            LOGGER.debug("No existe el documento. " + e1.getMessage(), e1);
                        }
                    }
                    
                    if(documento!=null){
                        int tpdoc = DocumentosUtil.getTipoDoc(cct, "Notificación", DocumentosUtil.BUSQUEDA_EXACTA, false);
                        
                        IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tramiteNuevo, tpdoc, nombre, documento, extension);
                        entityDoc.set("EXTENSION", Constants._EXTENSION_PDF);
                        entityDoc.set("EXTENSION_RDE", Constants._EXTENSION_PDF);
                        entityDoc.set("DESCRIPCION", descripcion);
                        try{
                            entityDoc.set("ESTADOFIRMA", doc.getString("ESTADOFIRMA"));
                        } catch(Exception e){
                            LOGGER.debug("El campo ESTADOFIRMA es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("DESTINO", doc.getString("DESTINO"));
                        } catch(Exception e){
                            LOGGER.debug("El campo DESTINO es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("DESTINO_ID", doc.getString("DESTINO_ID"));
                        } catch(Exception e){
                            LOGGER.debug("El campo DESTINO_ID es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("NREG", doc.getString("NREG"));
                        } catch(Exception e){
                            LOGGER.debug("El campo NREG es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("FREG", doc.getDate("FREG"));
                        } catch(Exception e){
                            LOGGER.debug("El campo FREG es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("ORIGEN", doc.getString("ORIGEN"));
                        } catch(Exception e){
                            LOGGER.debug("El campo ORIGEN es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("FAPROBACION", doc.getDate("FAPROBACION"));
                        } catch(Exception e){
                            LOGGER.debug("El campo FAPROBACION es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("COD_VERIFICACION", doc.getString("COD_VERIFICACION"));
                        } catch(Exception e){
                            LOGGER.debug("El campo COD_VERIFICACION es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("FFIRMA", doc.getDate("FFIRMA"));
                        } catch(Exception e){
                            LOGGER.debug("El campo FFIRMA es nulo o vacío" + e.getMessage(), e);
                        }
                        try{
                            entityDoc.set("REPOSITORIO", doc.getString("REPOSITORIO"));
                        } catch(Exception e){
                            LOGGER.debug("El campo REPOSITORIO es nulo o vacío" + e.getMessage(), e);
                        }
                        
                        entityDoc.store(cct);
                        
                        documento.delete();
                        
                        //Cerramos el trámite
                        transaction.closeTask(tramiteNuevo);
                        
                        cct.endTX(true);
                        
                        //Borramos los documentos originales
                        IItemCollection documentosOriginales = DocumentosUtil.getDocumentsByDescripcion(numexp, rulectx, numexpSolicitud);
                        Iterator<?> documentosOriginalesIterator = documentosOriginales.iterator();
                        while (documentosOriginalesIterator.hasNext()){
                            IItem documentoOriginal = (IItem)documentosOriginalesIterator.next();
                            entitiesAPI.deleteDocument(documentoOriginal);                            
                        }
                    }
                }
            }              
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;
            
        } catch(Exception e) {
               LOGGER.error(ConstantesString.LOGGER_ERROR + " al enviar las notificaciones a la solicitud " + numexpSolicitud + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al enviar las notificaciones a la solicitud " + numexpSolicitud + ". " + e.getMessage(), e);
        }
    }
    
    protected File getFile(String docRef) throws ISPACException{

        IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
        Object connectorSession = null;
        File file = null;
        
        try {
            connectorSession = gendocAPI.createConnectorSession();

            String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, docRef));
            if(!"pdf".equalsIgnoreCase(extension)) {
                
                // Convertir el documento original a PDF
                file = convert2PDF(docRef, extension);
                
            } else {
                
                // Se obtiene el documento del repositorio documental
                String fileName = FileTemporaryManager.getInstance().newFileName("." +extension);
                fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;

                OutputStream out = new FileOutputStream(fileName);
                gendocAPI.getDocument(connectorSession, docRef, out);

                file = new File(fileName);
            }
            
            //signDocument.setDocument(new FileInputStream(file));
                
        } catch (FileNotFoundException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al obtener el fichero: " + docRef, e);
            throw new ISPACException(ConstantesString.LOGGER_ERROR + " al obtener el fichero: " + docRef, e);
        } finally {
            if (connectorSession != null) {
                gendocAPI.closeConnectorSession(connectorSession);
            }
        }        
        return file;
    }
    
    private File convert2PDF(String infoPag, String extension) throws ISPACException {
        // Convertir el documento a pdf
        String docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), infoPag,extension);
        
        //String docFilePath = DocumentConverter.convert(clientContext.getAPI(), infoPag, DocumentConverter.PDFWRITER);

        // Obtener la información del fichero convertido
        File file = new File( docFilePath);
        if (!file.exists()){
            throw new ISPACException("No se ha podido convertir el documento a PDF");
        }
        
        return file;
    }
    
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}