package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraZIPCertYNotif  implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrGeneraZIPCertYNotif.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try {
			logger.info("INICIO - " + this.getClass().getName());
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			// ----------------------------------------------------------------------------------------------
			
			//Recuperamos los expedientes relacionados de la comisión o el pleno, que son propuestas
			
			 String consultaSQL = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
		     IItemCollection itemCollection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", consultaSQL);
		     //Si no hay expedientes padres (propuestas) no hacemos nada
		     if ((itemCollection != null) && (itemCollection.next())) {
			        Iterator<?> it = itemCollection.iterator();
			        IItem item = null;
			        String expedientePropuesta = "";
			        
			        while (it.hasNext()) {
			        	item = (IItem)it.next();
			        	expedientePropuesta = item.getString("NUMEXP_PADRE");
			        	
			        	String strQuery = "WHERE NUMEXP='" + expedientePropuesta + "' AND UPPER(NOMBRE) LIKE ('%CONTENIDO DE LA PROPUESTA%')";
	                    IItemCollection collectionTrams = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
	                    Iterator<?> itTrams = collectionTrams.iterator();
	                    IItem tram = null;
	                    while (itTrams.hasNext()){
	                    	tram = (IItem)itTrams.next();
	                        int idTram = tram.getInt("ID");	                        
	                        insertarDocumentosNotifDecreto(cct, genDocAPI, entitiesAPI, rulectx, rulectx.getNumExp(), expedientePropuesta, idTram);
	                    }
			      }
		     }
		}
		catch(Exception e){
			throw new ISPACRuleException(e);
		}	
		logger.info("FIN - " + this.getClass().getName());
		return new Boolean(true);
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	private void insertarDocumentosNotifDecreto(ClientContext cct, IGenDocAPI gendocAPI, IEntitiesAPI entitiesAPI, IRuleContext rulectx, String exp_relacionado, String numexpPropuestaDecreto, int taskId) throws ISPACRuleException {
		//Metodo que servira para insetar en el tramite lo documentos de notificacion del decreto y de la propuesta
		//y el certificado de dictamen, certificado de acuerdo o el decreto
		try{
	        IItemCollection collection = DocumentosUtil.getDocumentsByDescripcion(exp_relacionado, rulectx, "numexp="+numexpPropuestaDecreto);
	         
	        Iterator<?> it = collection.iterator();
	        IItem prop = null;
	        String sDescripcion = "";
	        int orden = 0;
	        String tipoPropuesta = "";
	        
	        if(it.hasNext()){
	        	prop = (IItem)it.next();
	        	sDescripcion = prop.getString("DESCRIPCION");
	        	String [] vDesc = sDescripcion.split(" . ");
	        	orden = Integer.parseInt(vDesc[1]);
	        	tipoPropuesta = vDesc[0];
	        }
	        String documento ="";
	        if(tipoPropuesta.equals(Constants.PROPUESTA.PROPUESTA_URGENCIA)){
	        	documento = orden + Constants.PROPUESTA.URGENCIA+" ";
	        }
	        else{
	        	documento = orden+".-";
	        }

			//Insertar el documento de certificado de acuerdo, certificado de dictamen o el decreto
			 //Obtenemos los documentos a partir de su nombre
			List<IItem> filesConcatenate = new ArrayList<IItem>();

			String strQuery = "DESCRIPCION='"+documento+Constants.SECRETARIATRAMITES.CERTIFICADACUERD+"' " +
							"OR DESCRIPCION='"+documento+Constants.TIPODOC.CERTIFICADO_DICTAMEN+"' " +
	        				"OR NOMBRE='"+Constants.DECRETOS._DOC_DECRETO+"'";
			IItemCollection documentsCollection = entitiesAPI.getDocuments(exp_relacionado, strQuery, "FDOC DESC");
			
			it  = documentsCollection.iterator();
	        IItem itemDoc = null;
	        if(it.hasNext()){
		        while (it.hasNext()){
		        	itemDoc = (IItem)it.next();
		        	filesConcatenate.add(itemDoc);
				}  		
				
				//Insertar el documento de notificaciones de cada participante
		    	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexpPropuestaDecreto, "ROL != 'TRAS' OR ROL IS NULL", "ID");
		    	Iterator<?> itParticipante = participantes.iterator();
		    	StringBuffer sNombreSQL = new StringBuffer();;
		    	while (itParticipante.hasNext())
	        	{
	        		IItem participante = (IItem)itParticipante.next();
	        		String nombrePar = participante.getString("NOMBRE");
	        		sNombreSQL.append("DESCRIPCION LIKE '%"+documento+"%"+nombrePar+"%' ");
	        		if(itParticipante.hasNext()){
	        			sNombreSQL.append("OR ");
	        		}
	        	}
		    	
		    	if(!sNombreSQL.toString().equals("")){
		    		strQuery = "NUMEXP = '"+exp_relacionado+"'";
		    		strQuery += " AND ("+sNombreSQL.toString()+")";
		    		documentsCollection = entitiesAPI.getDocuments(exp_relacionado, strQuery, "FDOC DESC");
					
					it  = documentsCollection.iterator();
					IItem itemDocNot = null;
					while (it.hasNext()){
						itemDocNot = (IItem)it.next();
						filesConcatenate.add(itemDocNot);
					}
		    	}
		    	
		    	//obtenemos el tipo de documento que le vamos a poner a zip que se va a crear
				//Obtenemos el id del tipo de documento de "Contenido de la propuesta" para
		        //ponerle el nombre del documento al zip
	
				int idTypeDocument = DocumentosUtil.getTipoDoc(cct, Constants.TIPODOC.DOCUMENTACION_APROBACION, DocumentosUtil.BUSQUEDA_EXACTA, false);
				if (idTypeDocument > Integer.MIN_VALUE){
					String sExtension = "zip";
					//Crear el zip con los documentos
					//http://www.manual-java.com/codigos-java/compresion-decompresion-archivos-zip-java.html
					File zipFile = DocumentosUtil.createDocumentsZipFile(gendocAPI, filesConcatenate);
				        
			        //Generamos el documento zip
			        String sFase = FasesUtil.getFase(rulectx, entitiesAPI, rulectx.getStageProcedureId());
					String sName = sFase+"& Numexp = "+numexpPropuestaDecreto;//Campo descripción del documento
					
					IItem docZip = DocumentosUtil.generaYAnexaDocumento(rulectx, taskId, idTypeDocument, sName, zipFile, sExtension);
					docZip.set("FAPROBACION", itemDoc.getDate("FAPROBACION"));
					docZip.store(cct);
						
					if(zipFile != null && zipFile.exists()) zipFile.delete();
				}
				else{
					throw new ISPACInfo("Error al obtener el tipo de documento");
				}
	        }
				
		}catch(Exception e){
			logger.error("Error. " + e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
	}
}
