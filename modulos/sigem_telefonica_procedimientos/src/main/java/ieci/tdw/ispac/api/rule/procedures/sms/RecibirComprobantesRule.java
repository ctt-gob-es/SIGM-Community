package ieci.tdw.ispac.api.rule.procedures.sms;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.XmlFacade;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.ServicioMensajesCortos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.sms.SMSConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class RecibirComprobantesRule implements IRule {
 
    private static final Logger LOGGER = Logger.getLogger(EnvioMensajesRule.class);
    private String strNombreDoc = "Comprobante SMS";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            IItemCollection col = entitiesAPI.getEntities("TSOL_SMS", rulectx.getNumExp());
            Iterator<?> it = col.iterator();
            
            while(it.hasNext()) {
                IItem iDest = (IItem)it.next();
                if (iDest.getString("ENVIADO_SMS").compareTo("SI")==0 && iDest.getString("COMPROBANTE_SMS").compareTo("SI")!=0) {
                    String strTlf = iDest.getString("TFNO_MOVIL");
                    String mtid = iDest.getString("MTID");
                    String id = mtid + ":+34" + strTlf;
                    boolean recibido = recibirComprobanteSMS(rulectx, id, strTlf);
                    String strRecibido = (recibido)? "SI":"NO";
                    iDest.set("COMPROBANTE_SMS", strRecibido);
                    iDest.store(cct);
                }
            }
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
               throw new ISPACRuleException(e);
            
        } catch(Exception e) {
            throw new ISPACRuleException("No se han podido enviar los SMS",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }

    private boolean recibirComprobanteSMS(IRuleContext rulectx, String id, String descr) throws SigemException, ISPACException {
        
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        IGenDocAPI gendocAPI = invesFlowAPI.getGenDocAPI();
        Object connectorSession = null;
        
        SMSConfiguration smsConfig = SMSConfiguration.getInstance(rulectx.getClientContext());

        String strUser = smsConfig.get(SMSConfiguration.USER_SMS);
        String strPwd = smsConfig.get(SMSConfiguration.PWD_SMS);
        
        boolean recibido = false;

        LOGGER.warn("Comprobante ID: " + id);
        ServicioMensajesCortos servicio = LocalizadorServicios.getServicioMensajesCortos();
        byte[] buffer = servicio.getCertSMSSignatureDocument(strUser, strPwd, id);
        File file = null;
        int docId = -1;
        FileInputStream in = null;
        
        try {
            //Guarda el comprobante en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".pdf");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.close();
            in = new FileInputStream(file);
            
        } catch(FileNotFoundException e) {
            LOGGER.warn("Fichero no encontrado: " + e.getMessage(), e);
            
        } catch(IOException e) {
            LOGGER.warn("Error al escribir a fichero: " + e.getMessage(), e);
        }

        //Si el resultado es un XML entonces hay error
        boolean respError = true;
        BufferedReader bufread = null;
        try {
            FileReader fileread = new FileReader(file);
            bufread = new BufferedReader(fileread);
            String strXml = "";
            String line = bufread.readLine();
            
            while(line != null) {
                strXml += line;
                line = bufread.readLine();
            }
            
            XmlFacade xml = new XmlFacade(strXml);
            LOGGER.warn("Respuesta XML: " + xml.toString());
            
        } catch(Exception e) {
            LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
            respError = false;
            
        } finally {
            if (null != bufread){
                try {
                    bufread.close();
                } catch (IOException e) {
                    LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
                }    
            }
        }
        
        //Guarda el resultado en gestor documental
        if (respError) {
            //Compruebo el estado del mensaje para saber por que no está su comprobante
               int status = servicio.getSMSStatus(strUser, strPwd, id); 
               LOGGER.warn("Estado: " + status);
               
        } else {
            String strQuery = "WHERE NOMBRE = '" + strNombreDoc + "'";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            Iterator<?> it = collection.iterator();
            int tpdoc = 0;
            
            if (it.hasNext()) {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
            }
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            docId = newdoc.getInt("ID");
        }
        
        if(docId>0 && in!=null && file.length()>0 ) {
            try {
                connectorSession = gendocAPI.createConnectorSession();
                cct.beginTX();
                
                IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/pdf", strNombreDoc + " - " + descr);
                entityDoc.set(DocumentosUtil.EXTENSION, "pdf");
                entityDoc.store(cct);
                file.delete();
                recibido = true;
                LOGGER.warn("Comprobante OK.");
                
            } catch (Exception e) {
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
                
                LOGGER.info("No se mostraba mensaje alguno, " + extraInfo + ". " + e.getMessage(), e);
                
                throw new ISPACInfo(message, extraInfo);
                
            } finally {
                if (connectorSession != null) {
                    gendocAPI.closeConnectorSession(connectorSession);
                }
            }
            cct.endTX(true);
        }
        
        return recibido;
    }

 }