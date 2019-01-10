package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrSERSOEnviaNotifASolicitud implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOEnviaNotifASolicitud.class);
    
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
            IClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            String numexp = rulectx.getNumExp();
            int tramiteId = rulectx.getTaskId();            
            
            IItemCollection docsCol = entitiesAPI.getDocuments(numexp, DocumentosUtil.ID_TRAMITE +" = '" +tramiteId+"' AND UPPER(" + DocumentosUtil.NOMBRE + ") = 'NOTIFICACIÓN CHEQUES' ", "");
            Iterator<?> docsIt = docsCol.iterator();                  
            if(docsIt.hasNext()){
                while (docsIt.hasNext()){
                    IItem doc = (IItem)docsIt.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String descripcion = doc.getString(DocumentosUtil.DESCRIPCION);
                    numexpSolicitud = (descripcion.split("-"))[2].trim();
                    
                    //Creamos el trámite
                    String strQueryAux = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " = '" + numexpSolicitud + "'";
                    IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
                    Iterator<?> itExpsAux = collExpsAux.iterator();

                    IItem iExpedienteAux = (IItem)itExpsAux.next();
                    int idFase = iExpedienteAux.getInt("ID");
                    int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");
                    strQueryAux = ConstantesString.WHERE + " ID_FASE = " +idFaseDecreto+" ORDER BY ORDEN ASC";
                    IItemCollection iTramitePropCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
                    Iterator<?> iTramitePropIterator = iTramitePropCollection.iterator();
                    int idTramite=0;
                    IItem tramite = (IItem)iTramitePropIterator.next();
                    idTramite = tramite.getInt("ID");
                    
                    cct.beginTX();
                    //Creo el tramite 'Creación del Decreto, traslado y notificaciones'            
                    int tramiteNuevo = transaction.createTask(idFase, idTramite);

                    //Pasamos el documento
                    String nombre = doc.getString(DocumentosUtil.DESCRIPCION);
                    
                    String infoPag = doc.getString(DocumentosUtil.INFOPAG);
                    String infoPagRDE = doc.getString(DocumentosUtil.INFOPAG_RDE);
                    String extension = "";

                    File documento = null;

                    if (StringUtils.isNotBlank(infoPagRDE)) {
                        extension = doc.getString(DocumentosUtil.EXTENSION_RDE);
                        documento = DocumentosUtil.getFile(cct, infoPagRDE, "", extension);
                        
                    } else {
                        extension = doc.getString(DocumentosUtil.EXTENSION);
                        documento = DocumentosUtil.getFile(cct, infoPag, "", extension);
                    }
                    
                    if(null != documento){
                    	int tpdoc = DocumentosUtil.getIdTipoDocByCodigo(cct, "NOT-CHEQUES");
                        
                        IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tramiteNuevo, tpdoc, nombre, documento, extension);
                        entityDoc.set(DocumentosUtil.EXTENSION, Constants._EXTENSION_PDF);
                        entityDoc.set(DocumentosUtil.EXTENSION_RDE, Constants._EXTENSION_PDF);
                        entityDoc.set(DocumentosUtil.DESCRIPCION, descripcion);
                        
                        entityDoc.set(DocumentosUtil.ESTADOFIRMA, doc.getString(DocumentosUtil.ESTADOFIRMA));
                        entityDoc.set(DocumentosUtil.DESTINO, doc.getString(DocumentosUtil.DESTINO));
                        entityDoc.set(DocumentosUtil.DESTINO_ID, doc.getString(DocumentosUtil.DESTINO_ID));
                        entityDoc.set(DocumentosUtil.NREG, doc.getString(DocumentosUtil.NREG));
                        entityDoc.set(DocumentosUtil.FREG, doc.getDate(DocumentosUtil.FREG));
                        entityDoc.set(DocumentosUtil.ORIGEN, doc.getString(DocumentosUtil.ORIGEN));
                        entityDoc.set(DocumentosUtil.FAPROBACION, doc.getDate(DocumentosUtil.FAPROBACION));
                        entityDoc.set(DocumentosUtil.COD_VERIFICACION, doc.getString(DocumentosUtil.COD_VERIFICACION));
                        entityDoc.set(DocumentosUtil.FFIRMA, doc.getDate(DocumentosUtil.FFIRMA));
                        entityDoc.set(DocumentosUtil.REPOSITORIO, doc.getString(DocumentosUtil.REPOSITORIO));
                        entityDoc.store(cct);
                        
                        if(documento.exists()){
                            documento.delete();
                        }
                        
                        //Cerramos el trámite
                        transaction.closeTask(tramiteNuevo);
                        
                        cct.endTX(true);
                        
                        //Borramos el documento original
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
    
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}