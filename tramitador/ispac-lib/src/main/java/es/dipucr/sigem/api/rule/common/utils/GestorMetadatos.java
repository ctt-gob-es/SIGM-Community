package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
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
 				// [Ruben CMIS] adapto la referencia al metadato de firma para que funcione con CMIS Firma --> sign
 				infoFirmas = genDocAPI.getDocumentProperty(connectorSession, infopagRdeBefore, "sign");
 			}
 			else{
 			// [Ruben CMIS] adapto la referencia al metadato de firma para que funcione con CMIS Firma --> sign
 				infoFirmas = genDocAPI.getDocumentProperty(connectorSession, infoPageRDE, "sign");
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
 			// [Ruben CMIS] adapto la referencia al metadato de firma para que funcione con CMIS Firma --> sign
 			genDocAPI.setDocumentProperty(connectorSession, infoPageRDE, "sign", xmlInfoFirmas.toString() );
 			
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

}
