package es.dipucr.contratacion.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
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
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerarInformeTrasladosAnexo implements IRule{
	
	private static final Logger logger = Logger.getLogger(GenerarInformeTrasladosAnexo.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();

			/*********************************************************/
			//File con el contenido en pdf
			File file = null;
			
			Object connectorSession = null;
			String nombreDocumento = "";
			
			Document document = null;
			// Creamos un reader para el documento
			PdfReader reader = null;
			
			connectorSession = gendocAPI.createConnectorSession();
			
			int taskId = rulectx.getTaskId();
			
			
			
			//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
			//no haga nada
			String sqlQuery1 = "NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo'";
			IItemCollection documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery1, "");
			Iterator<IItem> iDocAnexar = documentosAnexar.iterator();
			
			String numExp = rulectx.getNumExp();
			
			if(iDocAnexar.hasNext()){
				// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
				String sqlQueryPart = "WHERE ROL = 'TRAS' AND NUMEXP = '"+numExp+"' ORDER BY ID";	
				IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
				
				// 2. Comprobar que hay algún participante para el cual generar su notificación
				if (participantes!=null && participantes.toList().size()>=1) {
					logger.warn("Hay participantes. "+participantes.toList());
					
						
					//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
					//no haga nada
					sqlQuery1 = "NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo'";
					documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery1, "");
					iDocAnexar = documentosAnexar.iterator();

			
					String sqlQuery = "NUMEXP='"+rulectx.getNumExp()+"' AND (EXTENSION='odt' or EXTENSION='doc') AND NOMBRE='Plantilla Informe Técnico'";			
					IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");
					Iterator<IItem> iDoc = documentos.iterator();
					
					while(iDoc.hasNext()){	
						
						IItem doc = (IItem)iDoc.next();
						String  infoPag = "";
						String extension = "";
						if(doc.getString("INFOPAG_RDE")!=null){
							infoPag = doc.getString("INFOPAG_RDE");
							extension = doc.getString("EXTENSION_RDE");
						}
						else{
							infoPag = doc.getString("INFOPAG");
							extension = doc.getString("EXTENSION");
						}
						
						IItem tipoDoc = DocumentosUtil.getTipoDocByCodigo(cct, "Sol-Inf-Inc-Cont");
						
						int templateId = tipoDoc.getInt("ID");
			        	
			        	//Plantilla de Notificaciones
						int idPlantilla = DocumentosUtil.getIdDocumentByNombre(rulectx.getNumExp(), rulectx, "Plantilla Informe Técnico");
						logger.warn("idPlantilla = " + idPlantilla);
			        	
						File resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);
			        	
			        	
			        	IItem newdoc1 = gendocAPI.createTaskDocument(rulectx.getTaskId(), templateId);
			    		FileInputStream in1 = new FileInputStream(resultado1);
			    		int docId1 = newdoc1.getInt("ID");
			    		
			    		
			    		IItem entityDoc1 = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId1, in1, (int)resultado1.length(), "application/vnd.oasis.opendocument.text", "Propuesta Informe Técnico");
			    		int documentId = entityDoc1.getKeyInt();
			    		IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, resultado1.getName());
			    		entityTemplate.set("EXTENSION", Constants._EXTENSION_ODT);
			    		entityTemplate.set("DESCRIPCION", "Prueba Carta Digital Plantilla");
			    		entityTemplate.store(cct);
			        	
			        	
			        	String docInfoPag = entityTemplate.getString("INFOPAG");
						// Convertir el documento original a PDF
			    		String docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), docInfoPag,extension);
			    		
			    		// Obtener la información del fichero convertido
			    		file = new File(docFilePath);
			    		if (!file.exists())
			    			throw new ISPACException("No se ha podido convertir el documento a PDF");
		
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
							
							String  infoPagAnexar = docAnexar.getString("INFOPAG");
							String ext = docAnexar.getString("EXTENSION");
							String descripcion = docAnexar.getString("DESCRIPCION");
							
							File fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexar, null, null);
							
		
							
							nombreDocumento = descripcion + "." + ext;
							
							añadeDocumento(writer, fileAnexo.getAbsolutePath(), nombreDocumento, normalizar(descripcion));
			
							fileAnexo.delete();
							fileAnexo = null;
							
							
						}
						
						document.close();								
						resultadoFO.close();								
						reader.close();
						
						//Guarda el resultado en gestor documental
						String strQuery = "WHERE NOMBRE = 'Propuesta Informe Técnico'";
				        IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
				        Iterator<IItem> it = collection.iterator();
				        int tpdoc = 0;
				        if (it.hasNext())
				        {
				        	IItem tpd = (IItem)it.next();
				        	tpdoc = tpd.getInt("ID");
				        }
			    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
			    		FileInputStream in = new FileInputStream(resultado);
			    		int docId = newdoc.getInt("ID");
			    		
			    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)resultado.length(), "application/pdf", "Propuesta Informe Técnico");
			    		entityDoc.set("EXTENSION", Constants._EXTENSION_PDF);
			    		String templateDescripcion = entityDoc.getString("DESCRIPCION");
						entityDoc.set("DESCRIPCION", templateDescripcion);
			    		
			    		entityDoc.store(cct);
			    		file.delete();
			    		file = null;
			    		resultado.delete();
			    		resultado = null;
			    		DocumentosUtil.deleteFile(rutaFileName);
			    		
			    		//Borra los documentos intermedios del gestor documental
						strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION = 'Prueba Carta Digital Plantilla'";
				        IItemCollection collectionBorrar = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
				        Iterator<IItem> itBorrar = collectionBorrar.iterator();
				        while (itBorrar.hasNext())
				        {
				        	IItem docBorrar = (IItem)itBorrar.next();
				        	entitiesAPI.deleteDocument(docBorrar);
				        }
				        
				        DocumentosUtil.deleteFile(resultado1.getName());
				        resultado1.delete();
				        resultado1 = null;
				        in1.close();
				        fisFileAnexo.close();
				        fisFileAnexo = null;
				        writer.close();
				        writer = null;
				        in.close();
				        in = null;
				        DocumentosUtil.deleteFile(docFilePath);
						System.gc();
					}
				}//fin if
			}
			
			
			
		} catch (ISPACException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (FileNotFoundException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (DocumentException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return new Boolean (true);
	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}
	
	private void añadeDocumento(PdfWriter writer, String rutaOriginal, String nombreDocumento, String descripcionAdjunto) throws ISPACRuleException {

		try{
			
			PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
			if (pfs != null)
				writer.addFileAttachment(descripcionAdjunto, pfs);
			
		}
		catch(IOException e){
			logger.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
//	private static String getInfoDoc (IRuleContext rulectx, String docNotifTexto){
//		try{
//			String infoPag = null;
//			
//			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
//			//IGenDocAPI genDocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();
//			
//			// Obtener el documento generado en la fase de Inicio, concretamente su campo infopag
//			// Debe haber uno, ya que en la fase de Inicio se comprueba que se haya anexado sólo un doc (ValidateNumDocsTramiteRule)
//			
//			// Obtener el documento "Plantilla de Notificaciones" del expediente que se adjuntó en el primer trámite de "Creación del decreto"
//			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='"+docNotifTexto+"'", "FDOC DESC");
//			IItem document = null;
//			if (documentsCollection!=null && documentsCollection.next()){
//				document = (IItem)documentsCollection.iterator().next();
//			}
//				
//			// Obtener el valor del campo INFOPAG
//			if (document!=null){
//				infoPag = document.getString("INFOPAG");
//			}
//			return infoPag;
//		}catch(Exception e){
//			System.out.println ("Error en getInfoPag al obtener el valor INFOPAG del documento: "+e);
//			logger.error("Se produjo una excepciÃ³n", e);
//			return null;
//		}
//			
//		}

}

