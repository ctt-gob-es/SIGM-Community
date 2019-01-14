package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.IEntitiesAPI;
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
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;

public class GenerateCualquierDocumentoConAnexosInteresadoRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(GenerateCualquierDocumentoConAnexosInteresadoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Empty method
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
			/*********************************************************/
			//File con el contenido en pdf
			File file = null;
			
			String nombreDocumento = "";
			
	    	int documentTypeId = 0;
	    	
	    	FileInputStream fisFileAnexo = null;
	    	PdfWriter writer = null;
	    	
	    	FileOutputStream resultadoFO = null;
	    	PdfReader reader = null;
	    	Document document = null;
	    	
	    	Iterator<IItem> iDocAnexar = null;
	    	boolean tieneDocAnexar = false;
			
			String numExp = rulectx.getNumExp();
			
			
        	documentTypeId = DocumentosUtil.getTipoDoc(cct, "Carta digital", DocumentosUtil.BUSQUEDA_EXACTA, false);
        	
        	int templateId = 0;
			IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
        	}else{
        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
	        	templateId = tpDocsTemplate.getInt("ID");
        	}  
        	
        	IItemCollection documentosODT = entitiesAPI.getDocuments(rulectx.getNumExp(), "(EXTENSION='odt' or EXTENSION='doc') AND NOMBRE!='Anexo'", "");
			Iterator iDocODT = documentosODT.iterator();
			IItem docODT = null;
			if(iDocODT!=null && iDocODT.hasNext()){
				docODT = (IItem)iDocODT.next();
			}
			
			IItemCollection documentosPDF = entitiesAPI.getDocuments(rulectx.getNumExp(), "EXTENSION='pdf' AND NOMBRE!='Anexo'", "FDOC DESC");
			Iterator iDocPDF = documentosPDF.iterator();
			IItem docPDF = null;
			if(iDocPDF!=null && iDocPDF.hasNext()){
				docPDF = (IItem)iDocPDF.next();
			}

			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL = 'INT'", "ID");
			
			// 2. Comprobar que hay algún participante para el cual generar su notificación
			for (int i=0;i<participantes.toList().size();i++){
				
				File resultado = null;
				String docFilePath= "";
				
				
				String rutaFileNameResultado = "";
				String extensionFinal = "";
				String templateDescripcion = "";
				
				IItem participante = (IItem) participantes.toList().get(i);
				String nombreParticipante = "";
				String idExt = "";
				if (participante!=null){
					if ((String)participante.getString("NOMBRE")!=null){
						nombreParticipante = participante.getString("NOMBRE");
		        	}else{
		        		nombreParticipante = "";
		        	}
					if ((String)participante.getString("ID_EXT")!=null){
		        		idExt = participante.getString("ID_EXT");
		        	}else{
		        		idExt = "";
		        	}
				}
				//Compruebo que tenga anexos este expediente y si tiene que se ejecute la regla y si no tiene que no haga nada
				String sqlQuery1 = "NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo'";
				IItemCollection documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery1, "");
				iDocAnexar = documentosAnexar.iterator();
				
				if(docODT!=null){
					String infoPag = docODT.getString("INFOPAG");
					String extension = docODT.getString("EXTENSION");
					templateDescripcion = docODT.getString("DESCRIPCION");				

					if (participante!=null){
						DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
					}
					
		        	//Plantilla de Notificaciones
		        	resultado = DocumentosUtil.getFile(cct, infoPag, null, null); 
					
					if(iDocAnexar.hasNext()){
						IItem entityDoc1 = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), documentTypeId, templateId, "Prueba Carta Digital Plantilla", resultado, "pdf");
			    		entityDoc1.set("DESTINO", nombreParticipante);
			    		entityDoc1.store(cct);
			    		
			    		String docInfoPag = entityDoc1.getString("INFOPAG");
						// Convertir el documento original a PDF
			    		docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), docInfoPag, extension);
			    		// Obtener la información del fichero convertido
			    		file = new File(docFilePath);
			    		extensionFinal = "pdf";
			    		if(resultado != null && resultado.exists()) {
			    			resultado.delete();
			    		}
				        resultado = null;
				        tieneDocAnexar = true;
					} else {
			    		extensionFinal = extensionEntidad;
					}	    		
		    		
				} else {
					
					if(docPDF!=null){
						String infopag = "";						
						if(docPDF.getString("INFOPAG")!=null) infopag = docPDF.getString("INFOPAG");
						file = DocumentosUtil.getFile(cct, infopag, "", docPDF.getString("EXTENSION"));
						templateDescripcion = docPDF.getString("DESCRIPCION");
					}
					if(iDocAnexar.hasNext()){
						rutaFileNameResultado = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+FileTemporaryManager.getInstance().newFileName("." + extensionFinal);
						resultado = new File(rutaFileNameResultado);
						tieneDocAnexar = true;
					} else {
						resultado = file;
					}
					extensionFinal = "pdf";
					
				}
	    						
				if(tieneDocAnexar){
					rutaFileNameResultado = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+FileTemporaryManager.getInstance().newFileName("." + extensionFinal);
					resultado = new File(rutaFileNameResultado);
					resultadoFO = new FileOutputStream(resultado.getPath());
					
					fisFileAnexo = new FileInputStream(file);
					// Creamos un reader para el documento
					reader = new PdfReader((InputStream) fisFileAnexo);
					reader.consolidateNamedDestinations();
					int n = reader.getNumberOfPages();
					
					document = new Document(reader.getPageSizeWithRotation(1));
			
					document.setMargins(0, 0, 0, 0);
					
					writer = PdfWriter.getInstance(document, resultadoFO);				
					writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
	
					document.open();
					for(int j = 1; j <= n; j++){
						document.newPage();
						Image imagen = Image.getInstance(writer.getImportedPage(reader, j));
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
						
						//[dipucr-Felipe 3#91]
						PdfUtil.anexarDocumento(writer, fileAnexo.getAbsolutePath(), nombreDocumento, normalizar(descripcion));
		
						fileAnexo.delete();
						fileAnexo = null;																	
					}
					
				}
				
				if(document!=null) {
					document.close();								
				}
				if(resultadoFO!=null) {
					resultadoFO.close();								
				}
				if(reader!=null) {
					reader.close();
				}
				
				IItem entityDoc = null;
				//Guarda el resultado en gestor documental
				if("pdf".equals(extensionFinal)){
					entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, "Carta digital", resultado, "pdf"); 
				} else {
					entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), documentTypeId, templateId, "", resultado, extensionFinal);
				}
				
	    		entityDoc.set("EXTENSION", extensionFinal);
				
				templateDescripcion = templateDescripcion + " - " + nombreParticipante;
				entityDoc.set("DESCRIPCION", templateDescripcion);
				entityDoc.set("DESTINO", nombreParticipante);
				/**
	        	 * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
	        	 * **/
				entityDoc.set("DESTINO_ID", idExt);
				/**
	        	 * FIN[Teresa] Ticket#106#: añadir el campo id_ext
	        	 */
				entityDoc.store(cct);
	    		
	    		if(file!=null) {
	    			file.delete();
	    		}
	    		file = null;
	    		DocumentosUtil.deleteFile(rutaFileNameResultado);  		
		          
		        DocumentosUtil.borraParticipanteSsVariable(cct);
				
				templateDescripcion = "";
				nombreParticipante = "";
	    		
	    		//Borra los documentos intermedios del gestor documental
		        IItemCollection collectionBorrar = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION = 'Prueba Carta Digital Plantilla'", "");
		        Iterator itBorrar = collectionBorrar.iterator();
		        while (itBorrar.hasNext()) {
		        	IItem docBorrar = (IItem)itBorrar.next();
		        	entitiesAPI.deleteDocument(docBorrar);
		        }
				
				if(resultado != null && resultado.exists()) {
					resultado.delete();
				}
		        resultado = null;
		        if(fisFileAnexo != null) {
		        	fisFileAnexo.close();
		        }
		        fisFileAnexo = null;
				
				if(writer!=null) {
					writer.close();			        
				}
		        writer = null;	        
		        
		        DocumentosUtil.deleteFile(docFilePath);
			}//fin for
			
			
		} catch (ISPACException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		}
		return true;
	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean crearTramite = true;
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			/*********************************************************/
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE!='Anexo'", "");
			Iterator iDoc = documentos.iterator();
			if(!iDoc.hasNext()){
				crearTramite = false;
				rulectx.setInfoMessage("Tiene que existe un documento que sea distinto al tipo de documento Anexo");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		}
		return crearTramite;
	}

}
