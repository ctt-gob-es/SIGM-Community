package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerateNotificacionTraslado implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(GenerateNotificacionTraslado.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Empty method
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			/*********************************************************/
			numexp = rulectx.getNumExp();
			
			//File con el contenido en pdf
			File file = null;
			
			Object connectorSession = null;
			String nombreDocumento = "";
			
			String nombre = "";
	    	String recurso = "";
	    	int documentTypeId = 0;
			
			Document document = null;
			// Creamos un reader para el documento
			PdfReader reader = null;
			
			connectorSession = gendocAPI.createConnectorSession();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
			int taskId = rulectx.getTaskId();
			
			//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
			//no haga nada
			String sqlQuery1 = "NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo'";
			IItemCollection documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery1, "");
			Iterator<IItem> iDocAnexar = documentosAnexar.iterator();
			
			String numExp = rulectx.getNumExp();
			
			IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
        	}else {
        		//Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
        		//Necesitamos el de Notificación del Decreto
        		Iterator it = taskTpDocCollection.iterator();
        		while (it.hasNext()){
        			IItem taskTpDoc = (IItem)it.next();
        			if ("Carta digital".equals(taskTpDoc.get("CT_TPDOC:NOMBRE"))){
        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
        			}
        		}
        	}
			if(iDocAnexar.hasNext()){
				// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
				IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL = 'TRAS'", "ID");
				
				// 2. Comprobar que hay algún participante para el cual generar su notificación
				if (participantes!=null && participantes.toList().size()>=1) {
						
					//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
					//no haga nada
					documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Anexo'", "");
					iDocAnexar = documentosAnexar.iterator();

					IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "(EXTENSION='odt' or EXTENSION='doc') AND NOMBRE!='Anexo'", "");
					Iterator iDoc = documentos.iterator();
					
					while(iDoc.hasNext()){
						
			        	//recurso
			        	// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
			        	String sqlQueryPart = "WHERE VALOR = '"+recurso+"'";
			        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
			        	if (colRecurso.iterator().hasNext()){
			        		IItem iRecurso = (IItem)colRecurso.iterator().next();
			        		recurso = iRecurso.getString("SUSTITUTO");
			        	}
			        	/**
			        	 * INICIO
			        	 * ##Ticket #172 SIGEM decretos y secretaria, modificar el recurso
			        	 * **/
			        	if ("".equals(recurso)){
			        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
			        	} else {
			        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
			        	}
			        	
						IItem doc = (IItem)iDoc.next();
						String infoPag = doc.getString("INFOPAG");
						String extension = doc.getString("EXTENSION");
						
			        	//Plantilla de Notificaciones
			        	File resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);
			        	
			        	int templateId = 0;
						IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
			        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
			        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
			        	}else{
			        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
				        	templateId = tpDocsTemplate.getInt("ID");
			        	}
			    		
			        	IItem entityTemplate = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), documentTypeId, templateId, "Prueba Carta Digital Plantilla", resultado1, "pdf");
			    		
			    		entityTemplate.set("DESTINO", nombre);
			    		entityTemplate.store(cct);
			        	
			        	
			        	String docInfoPag = entityTemplate.getString("INFOPAG");
						// Convertir el documento original a PDF
			    		String docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), docInfoPag,extension);
			    		
			    		// Obtener la información del fichero convertido
			    		file = new File(docFilePath);
			    		if (!file.exists()) {
			    			throw new ISPACException("No se ha podido convertir el documento a PDF");
			    		}
		
						String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+FileTemporaryManager.getInstance().newFileName()+".pdf";
						File resultado = new File(rutaFileName);
						FileOutputStream resultadoFO = new FileOutputStream(resultado.getPath());
						
						FileInputStream fisFileAnexo = new FileInputStream(file);
						reader = new PdfReader((InputStream) fisFileAnexo);
						reader.consolidateNamedDestinations();
						int n = reader.getNumberOfPages();
						
						document = new Document(reader.getPageSizeWithRotation(1));
				
						document.setMargins(0, 0, 0, 0);
						
						PdfWriter writer = PdfWriter.getInstance(document, resultadoFO);				
						writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
		
						document.open();
						for(int i = 1; i <= n; i++){
							document.newPage();
							Image imagen = Image.getInstance(writer.getImportedPage(reader, i));
							imagen.scalePercent(100);
							document.add(imagen);							
						}

						while(iDocAnexar.hasNext()){
							IItem docAnexar = (IItem) iDocAnexar.next();
							
							String  infoPagAnexar = "";
							String ext = "";
							if(docAnexar.getString("INFOPAG_RDE")!=null){
								infoPagAnexar = docAnexar.getString("INFOPAG_RDE");
								 ext = docAnexar.getString("EXTENSION_RDE");
							} else {
								infoPagAnexar = docAnexar.getString("INFOPAG");
								 ext = docAnexar.getString("EXTENSION");
							}
							String descripcion = docAnexar.getString("DESCRIPCION");
							
							File fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexar, null, null);
							
							nombreDocumento = descripcion + "." + ext;
							
							addDocumento(writer, fileAnexo.getAbsolutePath(), nombreDocumento, normalizar(descripcion));
			
							fileAnexo.delete();
							fileAnexo = null;							
						}
						
						document.close();								
						resultadoFO.close();								
						reader.close();
						
						//Guarda el resultado en gestor documental
				        int tpdoc = DocumentosUtil.getTipoDoc(cct, "Carta digital", DocumentosUtil.BUSQUEDA_EXACTA, false);
			    		
			    		DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, "Carta digital", resultado, Constants._EXTENSION_PDF);

			    		file.delete();
			    		file = null;
			    		resultado.delete();
			    		resultado = null;
			    		DocumentosUtil.deleteFile(rutaFileName);
			    		
			    		//Borra los documentos intermedios del gestor documental
				        IItemCollection collectionBorrar = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION = 'Prueba Carta Digital Plantilla'", "");
				        Iterator itBorrar = collectionBorrar.iterator();
				        while (itBorrar.hasNext()) {
				        	IItem docBorrar = (IItem)itBorrar.next();
				        	entitiesAPI.deleteDocument(docBorrar);
				        }
				        
				        DocumentosUtil.deleteFile(resultado1.getName());
				        resultado1.delete();
				        resultado1 = null;
				        fisFileAnexo.close();
				        fisFileAnexo = null;
				        writer.close();
				        writer = null;
				        DocumentosUtil.deleteFile(docFilePath);
					}
				}//fin if
			} else { //No existen documentos de anexo
				
				String docNotifTexto = "Plantilla Carta digital";
				String docNotif = "Carta digital";
				int templateId = 0;
				
				// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
				IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL = 'TRAS'", "ID");
				
				// 2. Comprobar que hay algún participante para el cual generar su notificación
				if (participantes!=null && participantes.toList().size()>=1) {
					// 3. Obtener plantilla "Notificación Decreto"
					// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
		        	taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
		        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
		        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
		        	}else {
		        		//Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
		        		//Necesitamos el de Notificación del Decreto
		        		Iterator it = taskTpDocCollection.iterator();
		        		while (it.hasNext()){
		        			IItem taskTpDoc = (IItem)it.next();
		        			if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals(docNotif)){
		        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
		        			}
		        		}
		        		
		        		//Comprobamos que haya encontrado el Tipo de documento
		        		if (documentTypeId != 0){
			        		// Comprobar que el tipo de documento tiene asociado una plantilla
				        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
				        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
				        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
				        	}else{
				        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
					        	templateId = tpDocsTemplate.getInt("ID");

					        	// 4. Para cada participante generar una notificación
								try {
									connectorSession = gendocAPI.createConnectorSession();
									// Abrir transacción para que no se pueda generar un documento sin fichero
							        cct.beginTX();
						        	
							        IItem entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
							        documentTypeId = entityDocument.getKeyInt();
									
									String  infoPag =getInfoDoc(rulectx, docNotifTexto);
									
									//Plantilla de Notificaciones
									String sFileTemplate = DocumentosUtil.getFile(cct, infoPag, null, null).getName();
									
									int documentId = entityDocument.getKeyInt();
									
									// Generar el documento a partir la plantilla "Notificación Decreto"
									IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
																			
									// Referencia al fichero del documento en el gestor documental
									String docref = entityTemplate.getString("INFOPAG");
									String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
									entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
									String templateDescripcion = entityTemplate.getString("DESCRIPCION");
									entityTemplate.set("DESCRIPCION", templateDescripcion);
									entityTemplate.store(cct);
									        
									DocumentosUtil.deleteFile(sFileTemplate);
							        
								}catch (Exception e) {
									// Si se produce algún error se hace rollback de la transacción
									cct.endTX(false);
									
									String message = "exception.documents.generate";
									String extraInfo = null;
									Throwable eCause = e.getCause();
									
									if (eCause instanceof ISPACException) {
										extraInfo = eCause.getCause().getMessage();
									} else {
										extraInfo = e.getMessage();
									}
						        	LOGGER.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
									throw new ISPACInfo(message, extraInfo);
									
								}finally {
									
									if (connectorSession != null) {
										gendocAPI.closeConnectorSession(connectorSession);
									}
								}
				        	}
		        		} else {
	        				throw new ISPACInfo("No existe el tipo de documento 'Carta digital' en el expediente: " + numexp+" asociado al trámite");
	        			}		
		        	}
				}
			}
		} catch (ISPACException e) {
			LOGGER.error("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos en el expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return true;
	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}
	
	private void addDocumento(PdfWriter writer, String rutaOriginal, String nombreDocumento, String descripcionAdjunto) {

		try{			
			PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
			if (pfs != null) {
				writer.addFileAttachment(descripcionAdjunto, pfs);
			}
		} catch(IOException e) {
        	LOGGER.error("Error al añadir el documento. " + e.getMessage(), e);
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	private static String getInfoDoc (IRuleContext rulectx, String docNotifTexto){
		String numexp = "";
		try{
			String infoPag = null;
			numexp = rulectx.getNumExp();
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			// Obtener el documento "Plantilla de Notificaciones" del expediente que se adjuntó en el primer trámite de "Creación del decreto"
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='"+docNotifTexto+"'", "FDOC DESC");
			IItem document = null;
			if (documentsCollection!=null && documentsCollection.next()){
				document = (IItem)documentsCollection.iterator().next();
			}
				
			// Obtener el valor del campo INFOPAG
			if (document!=null){
				infoPag = document.getString("INFOPAG");
			}
			return infoPag;
		}catch(Exception e){
        	LOGGER.error("Error en getInfoPag al obtener el valor INFOPAG del documento: " + docNotifTexto + " del expediente: " + numexp + ". " + e.getMessage(), e);			
			return null;
		}
	}
}
