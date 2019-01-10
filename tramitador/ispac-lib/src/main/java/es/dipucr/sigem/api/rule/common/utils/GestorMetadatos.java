package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.XmlFacade;

import java.io.ByteArrayOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class GestorMetadatos {
	
	protected static final Logger logger = Logger.getLogger("loggerFirma");
	
	  /**
     * Almacena metadatos del documento
	 * @param infopagRdeBefore 
	 * @return 
	 * @throws Exception 
     */
 	public static void storeMetada(SessionAPI session, SignDocument signDoc, String firmante, String infopagRdeBefore)  throws Exception {
 		
 		IGenDocAPI genDocAPI = session.getClientContext().getAPI().getGenDocAPI();
		IEntitiesAPI entitiesAPI = session.getClientContext().getAPI().getEntitiesAPI();
		Object connectorSession = null;
 		String xPath = "";
		 		
 		IItem itemDoc = DocumentosUtil.getDocumento(entitiesAPI, signDoc.getItemDoc().getKeyInt());
 		
 		connectorSession = genDocAPI.createConnectorSession();
 		
 		// Referencia al documento firmado almacenado en el Repositorio de
 		// Documentos Electronicos
 		String infoPageRDE = itemDoc.getString("INFOPAG_RDE");
 		
		if (logger.isInfoEnabled()){
				logger.info("Actualizando idTransaccion sobre el documento RDE: " + infoPageRDE);
		}
 		
		if (!genDocAPI.existsDocument(connectorSession, infoPageRDE)) {
			logger.error("No se ha encontrado el documento fisico con identificador: '"
					+ infoPageRDE + "' en el repositorio de documentos");
			throw new ISPACInfo("exception.documents.notExists", false);
		}
 		
 		try {
			
			//Obtenemos el xml con las firmas adjuntadas antes de añadir la nueva
 			//INICIO [dipucr #186]
 			String infoFirmas = null;
 			if (!StringUtils.isEmpty(infopagRdeBefore)){
 				infoFirmas = genDocAPI.getDocumentProperty(connectorSession, infopagRdeBefore, "Firma");
 			}
 			else{
 				infoFirmas = genDocAPI.getDocumentProperty(connectorSession, infoPageRDE, "Firma");
 			}
 			//FIN [dipucr #186]
		    
    		XmlFacade xmlInfoFirmas = new XmlFacade(infoFirmas);
    		
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (StringUtils.isBlank(infoPageRDE)|| !genDocAPI.existsDocument(connectorSession, infoPageRDE)) {
				logger.error("No se ha encontrado el documento fisico con identificador: '"	+ infoPageRDE + "' en el repositorio de documentos");
				throw new ISPACInfo("exception.documents.notExists", false);
			}
			genDocAPI.getDocument(connectorSession, infoPageRDE, baos);			
    		
    		if(xmlInfoFirmas.getList("/" + SignDocument.TAG_FIRMAS + "/" + SignDocument.TAG_FIRMA).size()==0)
    		{ 
	 			xPath = "/firmas";
	 			xmlInfoFirmas.set(xPath+"/@tipo", "Firma3Fases");
    		}
    		
    		Base64 ba = new Base64();    		 		    
 			xPath = "/firmas/firma[@id=\'"+ (ba.encode(signDoc.getHash().getBytes())).toString() +"\']";
 			
 			try{ 				
 			
 			xmlInfoFirmas.set(xPath, firmante);
 			genDocAPI.setDocumentProperty(connectorSession, infoPageRDE, "Firma", xmlInfoFirmas.toString() );
 			
 			}
 			catch(Exception e){
 				
 				logger.error("Error al setear metadatos");
 				logger.error("Hash:"+signDoc.getHash());
 				logger.error("Metadatos:"+xmlInfoFirmas.toString());
 				throw e;
 				
 			}
 			
 			logger.debug(xmlInfoFirmas.toString());
 		}
 		finally {
 			if (connectorSession != null) {
 				genDocAPI.closeConnectorSession(connectorSession);
 			}
 		}
 	}
 	
 	  /**
     * Almacena metadatos del documento
	 * @param infopagRdeBefore 
	 * @return 
	 * @throws Exception 
     */
 	public static void storeMetadaDocBeforeDocAfter(IRuleContext rulectx, SignDocument signDocBefore, SignDocument signDocAfter, String infopagRdeAfter, String nombreFirmante)  throws Exception {
 		
 		IGenDocAPI genDocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		Object connectorSession = null;
 		String xPath = "";
		 		
 		IItem itemDoc = DocumentosUtil.getDocumento(entitiesAPI, signDocBefore.getItemDoc().getKeyInt());
 		
 		connectorSession = genDocAPI.createConnectorSession();
 		
 		// Referencia al documento firmado almacenado en el Repositorio de
 		// Documentos Electronicos
 		String infoPageRDE = itemDoc.getString("INFOPAG_RDE");
 		String xml = "";
 		if(StringUtils.isEmpty(infoPageRDE)){
 			xml = genDocAPI.getDocumentProperty(connectorSession, infopagRdeAfter, "Firma");
 		}
 		else{
 			xml = genDocAPI.getDocumentProperty(connectorSession, infoPageRDE, "Firma");
 		}
 		
		if (logger.isInfoEnabled()){
				logger.info("Actualizando idTransaccion sobre el documento RDE: " + infoPageRDE);
		}
		
	    XmlFacade xmlInfoFirmas = new XmlFacade(xml);
		if (genDocAPI.existsDocument(connectorSession, infoPageRDE)) {
			//logger.error("No se ha encontrado el documento fisico con identificador: '"+ infoPageRDE + "' en el repositorio de documentos");
			//throw new ISPACInfo("exception.documents.notExists", false);
			
			//Obtenemos el xml con las firmas adjuntadas antes de añadir la nueva
 			String infoFirmas = genDocAPI.getDocumentProperty(connectorSession, infoPageRDE, "Firma");

		    
 			xmlInfoFirmas = new XmlFacade(infoFirmas);
		}
 		try {			
			
    		
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (StringUtils.isBlank(infopagRdeAfter)|| !genDocAPI.existsDocument(connectorSession, infopagRdeAfter)) {
				logger.error("No se ha encontrado el documento fisico con identificador: '"	+ infopagRdeAfter + "' en el repositorio de documentos");
				throw new ISPACInfo("exception.documents.notExists", false);
			}
			genDocAPI.getDocument(connectorSession, infopagRdeAfter, baos);	
			if(xmlInfoFirmas.getList("/" + SignDocument.TAG_FIRMAS + "/" + SignDocument.TAG_FIRMA).size()==0)
    		{ 
	 			xPath = "/firmas";
	 			xmlInfoFirmas.set(xPath+"/@tipo", "Firma3Fases");
    		}
    		
    		Base64 ba = new Base64();    		 		    
 			xPath = "/firmas/firma[@id=\'"+ (ba.encode(signDocAfter.getHash().getBytes())).toString() +"\']";
 					
 			
 			xmlInfoFirmas.set(xPath, nombreFirmante);
    		
 			genDocAPI.setDocumentProperty(connectorSession, infopagRdeAfter, signDocAfter.PROPERTY_FIRMA, xmlInfoFirmas.toString() ); 			
 			
 			logger.debug(xmlInfoFirmas.toString());
 			
		}
		catch(ISPACRuleException e){			
			logger.error("Error al setear metadatos");
			logger.error("Hash:"+signDocAfter.getHash());
			logger.error("Metadatos:"+xmlInfoFirmas.toString());
			logger.error("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			
		}

		if (connectorSession != null) {
			genDocAPI.closeConnectorSession(connectorSession);
		}
 	}

}
