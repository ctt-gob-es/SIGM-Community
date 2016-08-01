package es.dipucr.sigem.api.rule.common.cartaDigital;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrEnviaCartaDigitalASolicitud implements IRule{
	private static final Logger logger = Logger
			.getLogger(DipucrEnviaCartaDigitalASolicitud.class);
	
	private IClientContext cct;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		cct = (ClientContext) rulectx.getClientContext();
    	try{
			//----------------------------------------------------------------------------------------------	        
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
	        logger.info("INICIO - "+this.getClass().getName());	       
	        String numexp = rulectx.getNumExp();
	        int tramiteId = rulectx.getTaskId();
	        
	        String numexpPadre = "";

	        IItemCollection expRelCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO = '" +numexp + "'");
	        Iterator expRelIterator = expRelCollection.iterator();
	        if(expRelIterator.hasNext()){
	        	IItem expRel = (IItem) expRelIterator.next();
	        	numexpPadre = expRel.getString("NUMEXP_PADRE");
	        }
	        
	        if(StringUtils.isNotEmpty(numexpPadre)){
	        	//Recuperamos los hijos del padre (solicitudes)	        	
	        	IItemCollection expRelHijosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE = '" +numexpPadre + "'");
		        Iterator expRelHijosIterator = expRelHijosCollection.iterator();		        
		        while(expRelHijosIterator.hasNext()){
	        		IItem expRelHijo = (IItem) expRelHijosIterator.next();
	        		String numexpSolicitud = expRelHijo.getString("NUMEXP_HIJO");
	        		IItem expSolicitud = ExpedientesUtil.getExpediente(cct, numexpSolicitud);
	        		if(expSolicitud!= null){
	        			String interesadoPpal = expSolicitud.getString("IDENTIDADTITULAR");
	        			if(StringUtils.isNotEmpty(interesadoPpal)){
	        				IItemCollection docsCol = entitiesAPI.getDocuments(numexp, "ID_TRAMITE = '"+tramiteId+"' AND UPPER(NOMBRE) = 'CARTA DIGITAL' AND ESTADOFIRMA='02' AND TRIM(UPPER(DESTINO)) = TRIM(UPPER('" + interesadoPpal + "'))", "");
					        Iterator docsIt = docsCol.iterator();
					        while (docsIt.hasNext()){
				        		boolean existe = false;
				        		
				        		IItem doc = (IItem)docsIt.next();
				        		String descripcion = doc.getString("DESCRIPCION");
			        			
			        			IItemCollection existeDocCollection = entitiesAPI.getDocuments(numexpSolicitud, "UPPER(NOMBRE) = 'CARTA DIGITAL'", "");
			        			Iterator existeDocIterator = existeDocCollection.iterator();
			        			while(existeDocIterator.hasNext() && !existe){
				        			IItem existeDoc = (IItem) existeDocIterator.next();
				        			if(doc.getString("NOMBRE").equals(existeDoc.getString("NOMBRE")) && doc.getString("DESCRIPCION").equals(existeDoc.getString("DESCRIPCION")) && doc.getDate("FDOC").equals(existeDoc.getDate("FDOC"))){
				        				if(doc.getString("COD_COTEJO")!= null && existeDoc.getString("COD_COTEJO") != null){
				        					if(doc.getString("COD_COTEJO").equals(existeDoc.getString("COD_COTEJO"))){
				        						existe = true;
				        					}
				        				}
				        				else if(doc.getString("COD_COTEJO") == null && existeDoc.getString("COD_COTEJO") == null)
				        					existe = true;
				        			}
				        		}						        		
				        		if(!existe){				
				        			cct.beginTX();
				        			IProcess itemProcess = invesFlowAPI.getProcess(numexpSolicitud);
				                	int idProcess = itemProcess.getInt("ID");
				        			IItemCollection collExpsAux = invesFlowAPI.getStagesProcess(idProcess);
					        		Iterator itExpsAux = collExpsAux.iterator();
			
					        		IItem iExpedienteAux = ((IItem)itExpsAux.next());
					        		int idFase = iExpedienteAux.getInt("ID");
				        			
				        			String infoPag = doc.getString("INFOPAG");
				    				String infoPagRDE = doc.getString("INFOPAG_RDE");
				    				String extension = "";
				
				    				File documento = null;
				    				if (StringUtils.isNotBlank(infoPagRDE)){
				    					documento = DocumentosUtil.getFile(cct, infoPagRDE, null, null);
				    					extension = doc.getString("EXTENSION_RDE");
				    				}
				    				else{
				    					documento = DocumentosUtil.getFile(cct, infoPag, null, null);
				    					extension = doc.getString("EXTENSION");
				    				}    				
				        			
				        	        int tpdoc = DocumentosUtil.getTipoDoc(cct, "CARTA DIGITAL", DocumentosUtil.BUSQUEDA_EXACTA, true);

				        	        IItem newdoc = null;
				    		        try{
				    		        	newdoc = genDocAPI.createStageDocument(idFase, tpdoc);
				    		        }
				    		        catch(Exception e){
				    		        	logger.error("ERROR al enviar la carta digital al expediente " + numexpSolicitud + ".", e);
				    		        	throw new ISPACRuleException("ERROR al enviar la carta digital al expediente " + numexpSolicitud + ".", e);
				    		        }
				    		        
				    		        if(newdoc != null){
					    				FileInputStream inGD = new FileInputStream(documento);
					    				int docId = newdoc.getInt("ID");
					    				
					    				IItem entityDoc = genDocAPI.attachStageInputStream(genDocAPI.createConnectorSession(), idFase, docId, inGD, (int)documento.length(), Constants._MIMETYPE_PDF, descripcion);
					
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
					    				doc.delete(cct);
				    		        }
				    				cct.endTX(true);
					        	}//Si no existe ya el documento
					        }//while (docsIt.hasNext()){  	
		        		}//si hay interesado ppal
	        		}//if(expSolicitud!= null){
	        	}//while(expRelHijosIterator.hasNext()){
	        }//if(StringUtils.isNotEmpty(numexpPadre)){
        	logger.info("FIN - "+this.getClass().getName());
    		return true;
        }catch(Exception e) {
        	try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				logger.error(e1);
			}
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}