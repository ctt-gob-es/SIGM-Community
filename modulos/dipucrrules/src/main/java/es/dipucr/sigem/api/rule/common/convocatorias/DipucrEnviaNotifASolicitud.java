package es.dipucr.sigem.api.rule.common.convocatorias;

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
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrEnviaNotifASolicitud implements IRule{
	private static final Logger logger = Logger.getLogger(DipucrEnviaNotifASolicitud.class);
	
	private IClientContext cct;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        String numexpSolicitud = "";
    	try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
	        logger.info("INICIO - "+this.getClass().getName());
	        
	        String numexp = rulectx.getNumExp();
	        int tramiteId = rulectx.getTaskId();

	        IItemCollection docsCol = entitiesAPI.getDocuments(numexp, "ID_TRAMITE = '"+tramiteId+"' AND UPPER(NOMBRE) = 'NOTIFICACIÓN'", "");
	        Iterator docsIt = docsCol.iterator();	  	        
	        if(docsIt.hasNext()){
	        	while (docsIt.hasNext()){
	        		numexpSolicitud = "";
	        		boolean existe = false;
	        		
	        		IItem doc = (IItem)docsIt.next();
	        		String descripcion = doc.getString("DESCRIPCION");
	        		String[] descripcionSplit = descripcion.split("-");
	        		for(String cadena : descripcionSplit){
	        			if (cadena.contains("DPCR")){
	        				numexpSolicitud = cadena.trim();
	        			}
	        		}
	        		
	        		if(StringUtils.isNotEmpty(numexpSolicitud)){
	        			
		        		IItemCollection existeDocCollection = entitiesAPI.getDocuments(numexpSolicitud, "UPPER(NOMBRE) LIKE 'NOTIFICACIÓN'", "");
	        			Iterator existeDocIterator = existeDocCollection.iterator();
	        			while(existeDocIterator.hasNext() && !existe){
	        				IItem existeDoc = (IItem) existeDocIterator.next();
	        				
	        				if(doc.getString("NOMBRE").equals(existeDoc.getString("NOMBRE")) ||
	        					doc.getString("DESCRIPCION").equals(existeDoc.getString("DESCRIPCION")) ||
	    	        			doc.getString("FDOC").equals(existeDoc.getString("FDOC")) ||
	    	    	        	doc.getString("COD_VERIFICACION").equals(existeDoc.getString("COD_VERIFICACION"))){
	        					existe = true;
	        				}
	        			}
	        			
		        		if(!existe){
			        		//Creamos el trámite
			        		String strQueryAux = "WHERE NUMEXP='" + numexpSolicitud + "'";
			        		IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
			        		Iterator itExpsAux = collExpsAux.iterator();
	
			        		IItem iExpedienteAux = ((IItem)itExpsAux.next());
			        		int idFase = iExpedienteAux.getInt("ID");
			        		int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");
			        		strQueryAux = "WHERE ID_FASE = "+idFaseDecreto+" AND UPPER(NOMBRE) LIKE 'NOTIFICACI%' ORDER BY ORDEN ASC";
			        		IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
			        		Iterator ITramiteProp = iTramiteProp.iterator();
			        		//error al generar el trámite 
			        		//[Ticket #1007 Teresa]
			        		if(ITramiteProp.hasNext()){
			        			int idTramite=0;
				        		IItem tramite = (IItem)ITramiteProp.next();
				        		idTramite = tramite.getInt("ID");
				        		
				        		//Creo el tramite 'Creación del Decreto, traslado y notificaciones'			
				        		int tramiteNuevo = transaction.createTask(idFase, idTramite);
			        			
			        			String infoPag = doc.getString("INFOPAG");
			    				String infoPagRDE = doc.getString("INFOPAG_RDE");
			    				String extension = "";
			
			    				File documento = null;
			    				if (StringUtils.isNotBlank(infoPagRDE)) {
			    					documento = getFile( infoPagRDE);
			    					extension = doc.getString("EXTENSION_RDE");
			    				} else {
			    					documento = getFile( infoPag);
			    					extension = doc.getString("EXTENSION");
			    				}    				
			        			
			        	        int tpdoc = DocumentosUtil.getTipoDoc(cct, "Notificación", DocumentosUtil.BUSQUEDA_EXACTA, false);

			    				IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tramiteNuevo, tpdoc, descripcion, documento, extension);
			
			    				entityDoc.set("FDOC", doc.getDate("FDOC"));
			    				entityDoc.set("NOMBRE", doc.getString("NOMBRE"));
			    				entityDoc.set("AUTOR", doc.getString("AUTOR"));
			    				entityDoc.set("TP_REG", doc.getString("TP_REG"));
			    				entityDoc.set("NREG", doc.getString("NREG"));
			    				entityDoc.set("FREG", doc.getDate("FREG"));
			    				entityDoc.set("ORIGEN", doc.getString("ORIGEN"));
			    				entityDoc.set("DESCRIPCION", doc.getString("DESCRIPCION"));
			    				entityDoc.set("ORIGEN_ID", doc.getString("ORIGEN_ID"));
			    				entityDoc.set("DESTINO", doc.getString("DESTINO"));
			    				entityDoc.set("AUTOR_INFO", doc.getString("AUTOR_INFO"));
			    				entityDoc.set("ESTADOFIRMA", doc.getString("ESTADOFIRMA"));
			    				entityDoc.set("ID_NOTIFICACION", doc.getString("ID_NOTIFICACION"));
			    				entityDoc.set("ESTADONOTIFICACION", doc.getString("ESTADONOTIFICACION"));
			    				entityDoc.set("DESTINO_ID", doc.getString("DESTINO_ID"));
			    				entityDoc.set("FNOTIFICACION", doc.getDate("FNOTIFICACION"));
			    				entityDoc.set("FAPROBACION", doc.getDate("FAPROBACION"));
			    				entityDoc.set("ORIGEN_TIPO", doc.getString("ORIGEN_TIPO"));
			    				entityDoc.set("DESTINO_TIPO", doc.getString("DESTINO_TIPO"));
			    				entityDoc.set("ID_PLANTILLA", doc.getInt("ID_PLANTILLA"));		    				
			    				entityDoc.set("EXTENSION", extension);
			    				entityDoc.set("FFIRMA", doc.getDate("FFIRMA"));
			    				entityDoc.set("EXTENSION_RDE", extension);
			    				entityDoc.set("COD_COTEJO", doc.getString("COD_COTEJO"));
			    				entityDoc.set("NDOC", doc.getInt("NDOC"));
			    				entityDoc.set("COD_VERIFICACION", doc.getString("COD_VERIFICACION"));
			    				entityDoc.set("MOTIVO_REPARO", doc.getString("MOTIVO_REPARO"));
			    				entityDoc.set("MOTIVO_RECHAZO", doc.getString("MOTIVO_RECHAZO"));
			    				entityDoc.set("REPOSITORIO", doc.getString("REPOSITORIO"));
			    				
			    				entityDoc.store(cct);
			    				
			    				documento.delete();
			        		}
		        		}
		        	}
	        	}
	        }    	      
        	logger.info("FIN - "+this.getClass().getName());
    		return true;
    		
        } catch(Exception e) {
        	logger.error("No se ha podido obtener la lista de solicitudes. " + numexpSolicitud,e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes. " + numexpSolicitud,e);
        }
    }
	
	protected File getFile(String docRef) throws ISPACException{

		IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
		Object connectorSession = null;
		File file = null;
		
		try {
			connectorSession = gendocAPI.createConnectorSession();

			String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, docRef));
			if(extension != "pdf") {
				
				// Convertir el documento original a PDF
				file = convert2PDF(docRef, extension);
				
			} else {
				
				// Se obtiene el documento del repositorio documental
				String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;

				OutputStream out = new FileOutputStream(fileName);
				gendocAPI.getDocument(connectorSession, docRef, out);

				file = new File(fileName);
			}
			
			//signDocument.setDocument(new FileInputStream(file));
				
		} catch (FileNotFoundException e) {
			logger.error("Error al obtener el fichero: " + docRef, e);
			throw new ISPACException(e);
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
		
		// Obtener la información del fichero convertido
		File file = new File( docFilePath);
		if (!file.exists())
			throw new ISPACException("No se ha podido convertir el documento a PDF");
	
		return file;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}